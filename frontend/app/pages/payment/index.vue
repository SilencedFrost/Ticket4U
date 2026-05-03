<script setup lang="ts">
import { useFormatter } from '~/composables/useFormatter';
import { usePhoneValidation } from '~/composables/usePhoneValidation';
import { useEmailValidation } from '~/composables/useEmailValidation';
import { useSettingsApi } from '~/features/settings/composables/useSettingsApi';
import { useCheckoutStore } from '~/stores/checkoutStore';
import { useUserStore } from '~/stores/userStore';

const { formatPrice } = useFormatter();
const { isEmailFormatValid } = useEmailValidation();
const { isPhoneFormatValid } = usePhoneValidation();
const { fetchCurrentUser } = useSettingsApi();
const checkoutStore = useCheckoutStore();
const userStore = useUserStore();
const route = useRoute();

definePageMeta({
  path: '/event/:eventId/book/checkout',
  alias: ['/checkout/payment', '/payment'],
});

const fullName = ref('');
const email = ref('');
const phone = ref('');
const promoCode = ref('');
const agreedPolicy = ref(false);
const transferContentMaxLength = 20;
const showSepayPopup = ref(false);
const config = useRuntimeConfig();

interface SepayPaymentResponse {
  orderId: string;
  orderCode?: string;
  amount: number;
  currency: 'VND' | 'USD';
  bankCode?: string;
  accountNumber?: string;
  accountName?: string;
  qrTemplate?: string;
  qrUrl?: string;
  paymentStatus?: string;
  orderStatus?: string;
  transactionId?: string | null;
}

const sepay = ref<SepayPaymentResponse | null>(null);
const sepayLoading = ref(false);
const sepayError = ref('');
const paymentSuccessHandled = ref(false);

const pollRate = ref<number>(3000);
const autoPoll = ref<boolean>(true);
let sepayInterval: NodeJS.Timeout | null = null;

const clearSepayPolling = () => {
  if (sepayInterval) {
    clearInterval(sepayInterval);
    sepayInterval = null;
  }
};

const fetchSepayStatus = async (showLoading = false) => {
  if (!sepay?.value?.orderId || paymentSuccessHandled.value) return;
  try {
    if (showLoading) sepayLoading.value = true;
    const status = await $fetch<Partial<SepayPaymentResponse>>(
      `${config.public.paymentServiceUrl}/public/payments/orders/${sepay.value.orderId}/status`,
    );
    // merge known fields
    sepay.value = { ...sepay.value, ...status };
    sepayError.value = '';

    if (status.paymentStatus === 'PAID' && status.orderStatus === 'CONFIRMED') {
      await redirectToPaymentSuccess();
    }
  } catch (error) {
    console.warn('Failed to fetch payment status:', error);
    sepayError.value = 'Unable to fetch payment status.';
  } finally {
    if (showLoading && !paymentSuccessHandled.value) {
      sepayLoading.value = false;
    }
  }
};

const setupSepayPolling = () => {
  clearSepayPolling();
  if (!autoPoll.value || !sepay?.value?.orderId || paymentSuccessHandled.value) return;
  sepayInterval = setInterval(() => {
    void fetchSepayStatus(false);
  }, pollRate.value);
};

const currentEventId = computed(() => {
  const sessionEventId = checkoutStore.checkoutSession?.eventId?.trim();
  if (sessionEventId) {
    return sessionEventId;
  }

  return String(route.params.eventId ?? '').trim();
});

const redirectToPaymentSuccess = async () => {
  if (paymentSuccessHandled.value || !sepay.value) {
    return;
  }

  paymentSuccessHandled.value = true;
  clearSepayPolling();
  showSepayPopup.value = false;
  sepayLoading.value = false;

  const successPath = currentEventId.value
    ? `/event/${currentEventId.value}/book/success`
    : '/payment/success';

  await navigateTo({
    path: successPath,
    query: {
      orderId: sepay.value.orderId,
      transactionId: sepay.value.transactionId ?? undefined,
      amount: String(sepay.value.amount),
      currency: sepay.value.currency,
      orderCode: sepay.value.orderCode ?? undefined,
    },
  });
};

const editingField = ref<'phone' | null>(null);
const shouldCloseOnPointerUp = ref(false);
const eventPanelRef = ref<HTMLElement | null>(null);
const holdPanelHeight = ref<number | null>(null);
let eventPanelResizeObserver: ResizeObserver | null = null;

watch(
  () => checkoutStore.checkoutSession,
  () => {
    nextTick(() => {
      syncTopPanelsHeight();

      if (
        import.meta.client &&
        'ResizeObserver' in window &&
        eventPanelRef.value &&
        !eventPanelResizeObserver
      ) {
        eventPanelResizeObserver = new ResizeObserver(() => {
          syncTopPanelsHeight();
        });
        eventPanelResizeObserver.observe(eventPanelRef.value);
      }
    });
  },
);

type SelectedTicket = {
  ticket_type: string;
  zone_name: string;
  seat_name: string;
  base_price: number;
  requires_phone?: boolean;
};

const tickets = computed<SelectedTicket[]>(() => {
  const session = checkoutStore.checkoutSession;

  if (!session) {
    return [];
  }

  return session.cart.flatMap((item) => {
    if (item.seats?.length) {
      return item.seats.map((seat) => ({
        ticket_type: item.isStanding ? 'STANDING' : 'SEAT',
        zone_name: seat.zoneName,
        seat_name: seat.seatName,
        base_price: seat.price,
        requires_phone: false,
      }));
    }

    return Array.from({ length: item.quantity }, (_, index) => ({
      ticket_type: item.isStanding ? 'STANDING' : 'SEAT',
      zone_name: item.name,
      seat_name: `${item.name}-${index + 1}`,
      base_price: item.price,
      requires_phone: false,
    }));
  });
});

const total = computed(() => checkoutStore.checkoutSession?.totalPrice ?? 0);
const transferContent = computed(() => checkoutStore.checkoutSession?.orderId ?? '');

const isPhoneRequired = computed(() =>
  tickets.value.some((ticket) => ticket.requires_phone === true),
);

const timeRemaining = ref<number>(900); // 15 minutes in seconds
const minuteBox = computed(() => String(Math.floor(timeRemaining.value / 60)).padStart(2, '0'));
const secondBox = computed(() => String(timeRemaining.value % 60).padStart(2, '0'));

let countdownInterval: NodeJS.Timeout | null = null;

const startCountdown = () => {
  clearCountdownInterval();
  timeRemaining.value = 900; // Reset to 15 minutes
  countdownInterval = setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value--;
    } else {
      clearCountdownInterval();
    }
  }, 1000);
};

const clearCountdownInterval = () => {
  if (countdownInterval) {
    clearInterval(countdownInterval);
    countdownInterval = null;
  }
};

const eventThumbnailUrl = computed(() => {
  return checkoutStore.checkoutSession?.event.bannerUrl?.square || '';
});

const fullNameErrorKey = computed(() =>
  fullName.value.trim().length === 0 ? 'auth.error.blank.full_name' : '',
);

const emailErrorKey = computed(() => {
  const value = email.value.trim();
  if (!value) {
    return 'auth.error.blank.email';
  }

  return isEmailFormatValid(value) ? '' : 'auth.error.format.email';
});

const phoneErrorKey = computed(() => {
  const value = phone.value.trim();
  if (!value) {
    return isPhoneRequired.value ? 'auth.error.blank.phone' : '';
  }

  return isPhoneFormatValid(value) ? '' : 'auth.error.format.phone';
});

const canPay = computed(() => {
  const hasValidContact = !fullNameErrorKey.value && !emailErrorKey.value && !phoneErrorKey.value;
  return hasValidContact && agreedPolicy.value;
});

const errorHintKey = computed(() => {
  if (canPay.value) {
    return '';
  }

  if (fullNameErrorKey.value) {
    return fullNameErrorKey.value;
  }

  if (emailErrorKey.value) {
    return emailErrorKey.value;
  }

  if (phoneErrorKey.value) {
    return phoneErrorKey.value;
  }

  return 'payment_mockup.validation.agree_policy';
});

const holdPanelStyle = computed(() => {
  return holdPanelHeight.value ? { minHeight: `${holdPanelHeight.value}px` } : null;
});

function finishEditing() {
  editingField.value = null;
}

async function openPaymentPopup() {
  if (!canPay.value) return;

  // Require login before payment
  if (!userStore.isLoggedIn) {
    sepayError.value = 'Vui lòng đăng nhập trước khi thanh toán.';
    return;
  }

  paymentSuccessHandled.value = false;
  sepayError.value = '';
  sepayLoading.value = true;

  try {
    const orderId = checkoutStore.checkoutSession?.orderId;
    const body: Record<string, unknown> = {
      userId: userStore.user.id,
    };

    if (orderId) {
      // Use existing order
      body.orderId = orderId;
    } else if (checkoutStore.checkoutSession) {
      // Create new order from cart
      const session = checkoutStore.checkoutSession;
      const cartTickets: Array<Record<string, unknown>> = [];

      // Flatten cart items to individual ticket requests
      session.cart.forEach((item) => {
        if (item.seats?.length) {
          // If seats are selected, use seat-level info
          item.seats.forEach((seat) => {
            cartTickets.push({
              zoneId: item.zoneId,
              zoneName: seat.zoneName,
              ticketType: item.isStanding ? 'STANDING' : 'SEAT',
              seatId: seat.seatUuid,
              seatName: seat.seatName,
              basePrice: seat.price,
            });
          });
        } else {
          // General admission tickets
          for (let i = 0; i < item.quantity; i++) {
            cartTickets.push({
              zoneId: item.zoneId,
              zoneName: item.name,
              ticketType: item.isStanding ? 'STANDING' : 'SEAT',
              seatId: globalThis.crypto.randomUUID(),
              seatName: `${item.name}-${i + 1}`,
              basePrice: item.price,
            });
          }
        }
      });

      body.createOrder = {
        email: email.value,
        currency: 'VND',
        eventId: session.eventId,
        eventName: session.event.title,
        tickets: cartTickets,
      };
    } else {
      throw new Error('No order ID or checkout session available');
    }

    sepay.value = await $fetch<SepayPaymentResponse>(
      `${config.public.paymentServiceUrl}/public/payments/sepay`,
      {
        method: 'POST',
        body,
      },
    );

    // show modal only when we have a valid response
    showSepayPopup.value = true;
    // fetch initial status and start polling
    await fetchSepayStatus(true);
    setupSepayPolling();
  } catch (error) {
    console.error('Failed to create SePay payment:', error);
    sepayError.value = 'Unable to create SePay payment. Please try again.';
    sepay.value = null;
  } finally {
    sepayLoading.value = false;
  }
}

function closePaymentPopup() {
  showSepayPopup.value = false;
  clearSepayPolling();
  paymentSuccessHandled.value = false;
}

async function copyValue(value: string) {
  if (!import.meta.client || !navigator.clipboard) {
    return;
  }

  try {
    await navigator.clipboard.writeText(value);
  } catch {
    return;
  }
}

function handleOverlayPointerDown(event: PointerEvent) {
  shouldCloseOnPointerUp.value = event.target === event.currentTarget;
}

function handleOverlayPointerUp(event: PointerEvent) {
  if (shouldCloseOnPointerUp.value && event.target === event.currentTarget) {
    closePaymentPopup();
  }
  shouldCloseOnPointerUp.value = false;
}

function handleEscape(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    closePaymentPopup();
  }
}

function syncTopPanelsHeight() {
  if (!import.meta.client || !eventPanelRef.value) {
    return;
  }

  if (!window.matchMedia('(min-width: 992px)').matches) {
    holdPanelHeight.value = null;
    return;
  }

  holdPanelHeight.value = Math.ceil(eventPanelRef.value.getBoundingClientRect().height);
}

function buildFullNameFromUser(user: UserSummary): string {
  const parts = [user.lastName, user.firstName]
    .map((part) => part?.trim() ?? '')
    .filter((part) => part.length > 0);

  if (parts.length > 0) {
    return parts.join(' ');
  }

  return user.username?.trim() ?? '';
}

async function prefillReceiverInfo() {
  try {
    const user = await fetchCurrentUser();
    fullName.value = buildFullNameFromUser(user);
    email.value = user.email?.trim() ?? '';
    phone.value = user.phoneNumber?.trim() ?? '';
  } catch {
    return;
  }
}

onMounted(() => {
  checkoutStore.restoreCheckoutSession();
  window.addEventListener('keydown', handleEscape);
  window.addEventListener('resize', syncTopPanelsHeight);

  syncTopPanelsHeight();
  void prefillReceiverInfo();
  // Start the mock countdown immediately when the page loads
  startCountdown();
});

onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleEscape);
  window.removeEventListener('resize', syncTopPanelsHeight);

  clearSepayPolling();
  clearCountdownInterval();

  if (eventPanelResizeObserver) {
    eventPanelResizeObserver.disconnect();
    eventPanelResizeObserver = null;
  }
});
</script>

<template>
  <div class="flip-payment-page text-reactive-primary">
    <div class="surface-glow"></div>

    <div class="shell-wrap px-2 px-lg-0">
      <div class="payment-shell">
        <main class="payment-main">
          <section class="left-col">
            <article
              v-if="checkoutStore.checkoutSession"
              ref="eventPanelRef"
              class="card payment-card event-panel"
            >
              <img
                class="event-thumb"
                :src="eventThumbnailUrl"
                :alt="$t('payment_mockup.event.image_alt')"
              />
              <div class="event-meta">
                <h1 class="event-title">
                  {{ checkoutStore.checkoutSession.event.title }}
                </h1>
                <div class="event-subline">
                  <span
                    ><i class="bi bi-calendar3"></i>
                    {{ checkoutStore.checkoutSession.event.date }}</span
                  >
                  <span
                    ><i class="bi bi-clock"></i>
                    {{ checkoutStore.checkoutSession.event.time }}</span
                  >
                </div>
                <p class="event-address">
                  {{ checkoutStore.checkoutSession.event.venue }}
                </p>
              </div>
            </article>

            <article class="card payment-card order-panel">
              <h2 class="panel-title">
                {{ $t('payment_mockup.ticket_info.title', { count: tickets.length }) }}
              </h2>

              <div class="ticket-header">
                <small>{{ $t('payment_mockup.ticket_info.columns.ticket_type') }}</small>
                <small>{{ $t('payment_mockup.ticket_info.columns.zone_name') }}</small>
                <small>{{ $t('payment_mockup.ticket_info.columns.seat_name') }}</small>
                <small>{{ $t('payment_mockup.ticket_info.columns.base_price') }}</small>
              </div>
              <div class="ticket-scroll">
                <div v-for="(ticket, index) in tickets" :key="index" class="ticket-row">
                  <div class="ticket-cell">{{ ticket.ticket_type }}</div>
                  <div class="ticket-cell">{{ ticket.zone_name }}</div>
                  <div class="ticket-cell">{{ ticket.seat_name }}</div>
                  <div class="ticket-cell">{{ formatPrice(ticket.base_price, 'VND') }}</div>
                </div>
              </div>

              <div class="promo-wrap">
                <input
                  v-model="promoCode"
                  type="text"
                  class="promo-input"
                  :placeholder="$t('payment_mockup.ticket_info.promo_placeholder')"
                />
                <button class="promo-btn" type="button">
                  {{ $t('payment_mockup.ticket_info.apply') }}
                </button>
              </div>

              <div class="total-line d-flex justify-content-between align-items-baseline">
                <strong>{{ $t('payment_mockup.checkout.total') }}</strong>
                <strong class="total-amount">{{ formatPrice(total, 'VND') }}</strong>
              </div>

              <label class="policy-check d-inline-flex align-items-center gap-2">
                <input v-model="agreedPolicy" type="checkbox" />
                {{ $t('payment_mockup.checkout.agree_policy') }}
              </label>

              <button class="pay-btn" type="button" :disabled="!canPay" @click="openPaymentPopup">
                {{ $t('payment_mockup.checkout.pay') }}
              </button>

              <p v-if="errorHintKey" class="error-line">{{ $t(errorHintKey) }}</p>
            </article>

            <article class="card payment-card policy-panel">
              <h2 class="panel-title">{{ $t('payment_mockup.policy.title') }}</h2>
              <p class="policy-text">
                {{ $t('payment_mockup.policy.content') }}
              </p>
            </article>
          </section>

          <section class="right-col">
            <article
              class="card payment-card hold-panel d-flex flex-column align-items-center"
              :style="holdPanelStyle"
            >
              <h2 class="panel-title center">Ticket hold time remaining</h2>
              <div class="time-boxes">
                <div class="time-cell">{{ minuteBox }}</div>
                <span class="time-dot">:</span>
                <div class="time-cell">{{ secondBox }}</div>
              </div>
            </article>

            <article class="card payment-card receiver-panel">
              <h2 class="panel-title">{{ $t('payment_mockup.receiver.title') }}</h2>
              <p class="panel-subtitle">{{ $t('payment_mockup.receiver.subtitle') }}</p>
              <hr class="panel-divider" />

              <div class="receiver-row">
                <i class="bi bi-person"></i>
                <div class="receiver-col">
                  <span class="receiver-label"
                    >{{ $t('payment_mockup.receiver.full_name') }} <b>*</b></span
                  >
                  <strong>{{ fullName || $t('payment_mockup.receiver.empty') }}</strong>
                </div>
                <div></div>
              </div>

              <div class="receiver-row">
                <i class="bi bi-envelope"></i>
                <div class="receiver-col">
                  <span class="receiver-label"
                    >{{ $t('payment_mockup.receiver.email') }} <b>*</b></span
                  >
                  <strong>{{ email || $t('payment_mockup.receiver.empty') }}</strong>
                </div>
                <div></div>
              </div>

              <div class="receiver-row">
                <i class="bi bi-telephone"></i>
                <div class="receiver-col">
                  <span class="receiver-label"
                    >{{ $t('payment_mockup.receiver.phone') }}
                    <b v-if="isPhoneRequired">*</b>
                    <em v-else class="receiver-optional">{{
                      $t('payment_mockup.receiver.optional')
                    }}</em></span
                  >
                  <input
                    v-if="editingField === 'phone'"
                    v-model="phone"
                    class="inline-edit-input"
                    type="tel"
                    @blur="finishEditing"
                    @keyup.enter="finishEditing"
                  />
                  <strong v-else>{{ phone || $t('payment_mockup.receiver.empty') }}</strong>
                  <p v-if="phoneErrorKey" class="receiver-error">{{ $t(phoneErrorKey) }}</p>
                </div>
                <button
                  type="button"
                  class="icon-button d-inline-flex align-items-center justify-content-center"
                  @click="editingField = editingField === 'phone' ? null : 'phone'"
                >
                  <i :class="editingField === 'phone' ? 'bi bi-check-lg' : 'bi bi-pencil'"></i>
                </button>
              </div>

              <hr class="panel-divider" />

              <p class="ticket-note">
                <i class="bi bi-qr-code"></i>
                {{ $t('payment_mockup.receiver.note_line_1') }}<br />
                {{ $t('payment_mockup.receiver.note_line_2') }}
                <strong>{{ $t('payment_mockup.receiver.note_ticket_hub') }}</strong>
                {{ $t('payment_mockup.receiver.note_line_3') }}<br />
                {{ $t('payment_mockup.receiver.note_line_4') }}
              </p>
            </article>

            <article class="card payment-card method-panel">
              <h2 class="panel-title">{{ $t('payment_mockup.payment_method.title') }}</h2>
              <div class="method-single d-inline-flex align-items-center gap-2 align-self-start">
                <i class="bi bi-bank"></i>
                {{ $t('payment_mockup.payment_method.sepay_bank') }}
              </div>
            </article>
          </section>
        </main>
      </div>
    </div>

    <div
      v-if="showSepayPopup"
      class="sepay-overlay"
      role="dialog"
      aria-modal="true"
      aria-labelledby="sepay-title"
      @pointerdown="handleOverlayPointerDown"
      @pointerup="handleOverlayPointerUp"
    >
      <div class="sepay-modal">
        <div class="sepay-header d-flex justify-content-between align-items-center">
          <div class="sepay-header-left d-flex align-items-center gap-2">
            <i class="bi bi-bank"></i>
            <strong id="sepay-title">{{ $t('payment_mockup.popup.title') }}</strong>
          </div>
          <button
            type="button"
            class="close-btn d-inline-flex align-items-center justify-content-center"
            @click="closePaymentPopup"
          >
            <i class="bi bi-x-lg"></i>
          </button>
        </div>

        <div class="sepay-timer">
          <div class="sepay-timer-main">
            <span>Ticket hold time remaining</span>
            <div class="time-boxes compact">
              <div class="time-cell">{{ minuteBox }}</div>
              <span class="time-dot">:</span>
              <div class="time-cell">{{ secondBox }}</div>
            </div>
          </div>
          <button
            type="button"
            class="timer-close-btn d-lg-none d-inline-flex align-items-center justify-content-center"
            @click="closePaymentPopup"
          >
            <i class="bi bi-x-lg"></i>
          </button>
        </div>

        <div class="sepay-body">
          <div v-if="sepayError" class="alert alert-danger">{{ sepayError }}</div>
          <div class="sepay-warning">
            <i class="bi bi-exclamation-triangle"></i>
            <div>
              <p>{{ $t('payment_mockup.popup.warning_line_1') }}</p>
              <p>{{ $t('payment_mockup.popup.warning_line_2') }}</p>
              <p>{{ $t('payment_mockup.popup.warning_line_3') }}</p>
            </div>
          </div>

          <div class="sepay-info-wrap">
            <div class="qr-wrap">
              <div v-if="sepayLoading" class="qr-placeholder" aria-live="polite">
                <div class="spinner-border" role="status" aria-hidden="true"></div>
              </div>

              <div v-else-if="sepay && sepay.qrUrl" class="qr-placeholder">
                <img
                  :src="sepay.qrUrl"
                  :alt="$t('payment_mockup.popup.qr_alt')"
                  class="img-fluid"
                />
              </div>

              <div v-else class="qr-placeholder" :aria-label="$t('payment_mockup.popup.qr_alt')">
                <i class="bi bi-qr-code"></i>
                <span>QR Placeholder</span>
              </div>
            </div>

            <div class="bank-info">
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.bank') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong>{{ sepay?.bankCode ?? 'N/A' }}</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue(sepay?.bankCode ?? '')"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.cart_code') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong class="content-preview" :title="sepay?.orderCode ?? transferContent">{{
                    (sepay?.orderCode ?? transferContent).length > transferContentMaxLength
                      ? (sepay?.orderCode ?? transferContent).slice(
                          0,
                          transferContentMaxLength - 3,
                        ) + '...'
                      : (sepay?.orderCode ?? transferContent)
                  }}</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue(sepay?.orderCode ?? transferContent)"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.account_number') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong class="text-primary">{{ sepay?.accountNumber ?? 'N/A' }}</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue(sepay?.accountNumber ?? '')"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.receiver_name') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong>{{ sepay?.accountName ?? 'N/A' }}</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue(sepay?.accountName ?? '')"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.amount') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong class="text-primary">{{
                    sepay ? formatPrice(sepay.amount, sepay.currency) : formatPrice(total, 'VND')
                  }}</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="
                      copyValue(
                        sepay
                          ? formatPrice(sepay.amount, sepay.currency)
                          : formatPrice(total, 'VND'),
                      )
                    "
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div class="sepay-note">
            <strong>{{ $t('payment_mockup.popup.note_title') }}</strong>
            <ul>
              <li>{{ $t('payment_mockup.popup.note_line_1') }}</li>
            </ul>
          </div>
        </div>

        <div class="sepay-footer">
          <div class="payment-pending-bar" role="status" aria-live="polite">
            <span class="payment-pending-spinner" aria-hidden="true"></span>
            <div class="payment-pending-copy">
              <strong>{{ $t('payment_mockup.popup.pending_title') }}</strong>
              <span>{{ $t('payment_mockup.popup.pending_subtitle') }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped src="./payment-page.css"></style>
