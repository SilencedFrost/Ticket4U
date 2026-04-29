<script setup lang="ts">
import { useSettingsApi } from '~/features/settings/composables/useSettingsApi';

// TODO: Change url and make this page work for real

const { formatPrice } = useFormatter();
const { isEmailFormatValid } = useEmailValidation();
const { isPhoneFormatValid } = usePhoneValidation();
const { fetchCurrentUser } = useSettingsApi();

const fullName = ref('');
const email = ref('');
const phone = ref('');
const promoCode = ref('');
const agreedPolicy = ref(false);
const total = 100000;
const transferContent = 'T4UCODE';
const transferContentMaxLength = 20;
const showSepayPopup = ref(false);
const editingField = ref<'name' | 'email' | 'phone' | null>(null);
const shouldCloseOnPointerUp = ref(false);
const eventPanelRef = ref<HTMLElement | null>(null);
const holdPanelHeight = ref<number | null>(null);
let eventPanelResizeObserver: ResizeObserver | null = null;

type MockSelectedTicket = {
  ticket_type: string;
  zone_name: string;
  seat_name: string;
  base_price: number;
  requires_phone?: boolean;
};

const tickets = ref<MockSelectedTicket[]>([
  { ticket_type: 'GA', zone_name: 'Standard', seat_name: 'C10', base_price: 25000 },
  { ticket_type: 'GA', zone_name: 'Standard', seat_name: 'C11', base_price: 25000 },
  { ticket_type: 'GA', zone_name: 'Standard', seat_name: 'C12', base_price: 25000 },
  {
    ticket_type: 'VIP',
    zone_name: 'Premium',
    seat_name: 'A03',
    base_price: 25000,
    requires_phone: false,
  },
]);

const isPhoneRequired = computed(() =>
  tickets.value.some((ticket) => ticket.requires_phone === true),
);

const minuteBox = '00';
const secondBox = '00';
const transferContentPreview = computed(() => {
  if (transferContent.length <= transferContentMaxLength) {
    return transferContent;
  }

  return `${transferContent.slice(0, transferContentMaxLength - 3)}...`;
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

function toggleEditing(field: 'name' | 'email' | 'phone') {
  editingField.value = editingField.value === field ? null : field;
}

function finishEditing() {
  editingField.value = null;
}

function openPaymentPopup() {
  if (!canPay.value) return;
  showSepayPopup.value = true;
}

function closePaymentPopup() {
  showSepayPopup.value = false;
}

function confirmPaid() {
  showSepayPopup.value = false;
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
  window.addEventListener('keydown', handleEscape);

  window.addEventListener('resize', syncTopPanelsHeight);

  if (import.meta.client && 'ResizeObserver' in window && eventPanelRef.value) {
    eventPanelResizeObserver = new ResizeObserver(() => {
      syncTopPanelsHeight();
    });
    eventPanelResizeObserver.observe(eventPanelRef.value);
  }

  syncTopPanelsHeight();
  void prefillReceiverInfo();
});

onBeforeUnmount(() => {
  window.removeEventListener('keydown', handleEscape);
  window.removeEventListener('resize', syncTopPanelsHeight);

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
            <article ref="eventPanelRef" class="card payment-card event-panel">
              <img
                class="event-thumb"
                src="https://images.unsplash.com/photo-1460723237483-7a6dc9d0b212?w=280&h=180&fit=crop"
                :alt="$t('payment_mockup.event.image_alt')"
              />
              <div class="event-meta">
                <h1 class="event-title">{{ $t('payment_mockup.event.title') }}</h1>
                <div class="event-subline">
                  <span><i class="bi bi-calendar3"></i> {{ $t('payment_mockup.event.date') }}</span>
                  <span><i class="bi bi-clock"></i> {{ $t('payment_mockup.event.time') }}</span>
                </div>
                <p class="event-address">{{ $t('payment_mockup.event.address') }}</p>
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
                  <input
                    v-if="editingField === 'name'"
                    v-model="fullName"
                    class="inline-edit-input"
                    type="text"
                    @blur="finishEditing"
                    @keyup.enter="finishEditing"
                  />
                  <strong v-else>{{ fullName || $t('payment_mockup.receiver.empty') }}</strong>
                  <p v-if="fullNameErrorKey" class="receiver-error">{{ $t(fullNameErrorKey) }}</p>
                </div>
                <button
                  type="button"
                  class="icon-button d-inline-flex align-items-center justify-content-center"
                  @click="toggleEditing('name')"
                >
                  <i :class="editingField === 'name' ? 'bi bi-check-lg' : 'bi bi-pencil'"></i>
                </button>
              </div>

              <div class="receiver-row">
                <i class="bi bi-envelope"></i>
                <div class="receiver-col">
                  <span class="receiver-label"
                    >{{ $t('payment_mockup.receiver.email') }} <b>*</b></span
                  >
                  <input
                    v-if="editingField === 'email'"
                    v-model="email"
                    class="inline-edit-input"
                    type="email"
                    @blur="finishEditing"
                    @keyup.enter="finishEditing"
                  />
                  <strong v-else>{{ email || $t('payment_mockup.receiver.empty') }}</strong>
                  <p v-if="emailErrorKey" class="receiver-error">{{ $t(emailErrorKey) }}</p>
                </div>
                <button
                  type="button"
                  class="icon-button d-inline-flex align-items-center justify-content-center"
                  @click="toggleEditing('email')"
                >
                  <i :class="editingField === 'email' ? 'bi bi-check-lg' : 'bi bi-pencil'"></i>
                </button>
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
                  @click="toggleEditing('phone')"
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

          <section class="right-col">
            <article
              class="card payment-card hold-panel d-flex flex-column align-items-center"
              :style="holdPanelStyle"
            >
              <h2 class="panel-title center">{{ $t('payment_mockup.timer.title') }}</h2>
              <div class="time-boxes">
                <div class="time-cell">{{ minuteBox }}</div>
                <span class="time-dot">:</span>
                <div class="time-cell">{{ secondBox }}</div>
              </div>
            </article>

            <article class="card payment-card policy-panel">
              <h2 class="panel-title">{{ $t('payment_mockup.policy.title') }}</h2>
              <p class="policy-text">
                {{ $t('payment_mockup.policy.content') }}
              </p>
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
            <span>{{ $t('payment_mockup.timer.title') }}</span>
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
              <div class="qr-placeholder" :aria-label="$t('payment_mockup.popup.qr_alt')">
                <i class="bi bi-qr-code"></i>
                <span>QR Placeholder</span>
              </div>
            </div>

            <div class="bank-info">
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.bank') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong>BIDV</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue('BIDV')"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.cart_code') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong class="content-preview" :title="transferContent">{{
                    transferContentPreview
                  }}</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue(transferContent)"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.account_number') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong class="text-primary">123</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue('123')"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.receiver_name') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong>ticket4u</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue('ticket4u')"
                  >
                    <i class="bi bi-copy"></i>
                  </button>
                </div>
              </div>
              <div class="info-row">
                <span>{{ $t('payment_mockup.popup.amount') }}</span>
                <div class="info-row-end d-inline-flex align-items-center gap-2">
                  <strong class="text-primary">{{ formatPrice(total, 'VND') }}</strong>
                  <button
                    type="button"
                    class="copy-icon-btn d-inline-flex align-items-center justify-content-center"
                    :aria-label="$t('payment_mockup.popup.copy')"
                    @click="copyValue(formatPrice(total, 'VND'))"
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
          <button type="button" class="confirm-paid-btn" @click="confirmPaid">
            {{ $t('payment_mockup.popup.confirm_paid') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.flip-payment-page {
  --page-pad: 12px;
  --global-nav-offset: 72px;
  min-height: 100dvh;
  position: relative;
  background:
    radial-gradient(76% 40% at 50% -8%, rgba(255, 255, 255, 0.06) 0%, rgba(0, 0, 0, 0) 70%),
    linear-gradient(180deg, #111111 0%, #0b0b0b 100%);
  padding-bottom: 16px;
}

.surface-glow {
  position: absolute;
  inset: 0;
  background: radial-gradient(55% 30% at 50% 0, rgba(var(--bs-primary-rgb), 0.18), transparent 74%);
  pointer-events: none;
}

.shell-wrap {
  position: relative;
  z-index: 1;
}

.payment-shell {
  max-width: 1020px;
  height: calc(100dvh - var(--global-nav-offset));
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  padding: var(--page-pad);
}

.payment-main {
  min-height: 0;
  display: grid;
  grid-template-columns: 1.35fr 0.95fr;
  gap: 12px;
  align-content: start;
}

.left-col,
.right-col {
  min-height: 0;
  display: grid;
  gap: 12px;
  align-content: start;
}

.left-col {
  grid-template-rows: auto auto auto;
}

.right-col {
  grid-template-rows: auto auto auto;
}

.payment-card {
  background: rgba(17, 17, 17, 0.96) !important;
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
  transition: none;
}

.panel-title {
  font-size: 1.25rem;
  margin: 0 0 8px;
  color: #f8f9ff;
  font-weight: 700;
}

.panel-title.center {
  text-align: center;
}

.panel-subtitle {
  margin: 0;
  color: #96a0ba;
  font-size: 0.95rem;
}

.panel-divider {
  border-color: rgba(122, 132, 165, 0.34);
  margin: 10px 0;
}

.event-panel {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 12px;
}

.event-thumb {
  width: 118px;
  height: 78px;
  border-radius: 12px;
  object-fit: cover;
}

.event-meta {
  min-width: 0;
}

.event-title {
  margin: 0 0 6px;
  color: #f4f7ff;
  font-size: 1.1rem;
  font-weight: 700;
  line-height: 1.3;
}

.event-subline {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  color: #d3daf0;
  font-size: 0.9rem;
}

.event-subline span {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.event-address {
  margin: 5px 0 0;
  color: #a7b0c7;
  font-size: 0.9rem;
}

.receiver-panel {
  overflow: hidden;
}

.receiver-row {
  display: grid;
  grid-template-columns: 20px 1fr 26px;
  gap: 10px;
  align-items: start;
  margin-bottom: 8px;
}

.receiver-row > i {
  color: #aeb7cf;
  padding-top: 3px;
}

.receiver-col {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
}

.receiver-label {
  color: #ced6ed;
  font-size: 0.9rem;
}

.receiver-label b {
  color: #ef5e66;
}

.receiver-col strong {
  font-size: 1rem;
  line-height: 1.3;
  color: #f5f7ff;
  word-break: break-word;
}

.receiver-error {
  margin: 2px 0 0;
  color: #ff5b64;
  font-size: 0.82rem;
  line-height: 1.3;
}

.receiver-optional {
  font-style: normal;
  margin-left: 4px;
  color: #9aa7c8;
  font-size: 0.82rem;
}

.inline-edit-input {
  width: min(320px, 100%);
  height: 36px;
  border-radius: 8px;
  border: 1px solid rgba(139, 148, 171, 0.42);
  background: rgba(255, 255, 255, 0.04);
  color: #e8edf9;
  font-size: 1rem;
  padding: 0 10px;
}

.icon-button {
  border: 0;
  background: transparent;
  color: #d9dfef;
  width: 26px;
  height: 26px;
}

.ticket-note {
  margin: 2px 0 0;
  color: #dbe1f2;
  font-size: 0.9rem;
  line-height: 1.45;
}

.ticket-note i {
  margin-right: 6px;
  color: #aeb7cf;
}

.method-single {
  min-height: 42px;
  border-radius: 10px;
  border: 1px solid rgba(139, 149, 176, 0.36);
  background: rgba(255, 255, 255, 0.05);
  color: #ecf0ff;
  padding: 10px 12px;
  font-size: 0.95rem;
  font-weight: 600;
}

.time-boxes {
  display: flex;
  align-items: center;
  gap: 6px;
}

.time-cell {
  width: 42px;
  height: 42px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(150, 159, 186, 0.34);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  font-weight: 700;
  color: #f8fbff;
}

.time-dot {
  color: #f3f5ff;
  font-size: 1.3rem;
  font-weight: 700;
  transform: translateY(-2px);
}

.policy-text {
  margin: 0;
  color: #f0f4ff;
  font-size: 0.95rem;
  line-height: 1.45;
}

@media (min-width: 992px) {
  .policy-panel {
    padding-top: 10px;
    padding-bottom: 10px;
  }

  .policy-panel .panel-title {
    margin-bottom: 6px;
  }
}

.order-panel {
  overflow: visible;
  display: block;
}

.ticket-scroll {
  max-height: 146px;
  overflow-y: auto;
  padding-right: 6px;
  scrollbar-gutter: stable;
  overscroll-behavior-y: contain;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: thin;
  scrollbar-color: rgba(204, 215, 242, 0.82) rgba(255, 255, 255, 0.12);
}

.ticket-scroll::-webkit-scrollbar {
  width: 10px;
}

.ticket-scroll::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 999px;
}

.ticket-scroll::-webkit-scrollbar-thumb {
  background: rgba(204, 215, 242, 0.82);
  border-radius: 999px;
  border: 2px solid rgba(17, 17, 17, 0.96);
}

.ticket-scroll::-webkit-scrollbar-thumb:active {
  background: rgba(232, 239, 255, 0.96);
}

.ticket-row {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
  min-height: 46px;
  align-items: center;
  padding: 6px 0;
  border-bottom: 1px solid rgba(122, 132, 165, 0.2);
}

.ticket-header {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
  padding: 4px 6px 8px 0;
  border-bottom: 1px solid rgba(122, 132, 165, 0.28);
  background: rgba(17, 17, 17, 0.96);
}

.ticket-header small {
  color: #98a2bf;
  font-size: 0.8rem;
  display: block;
}

.ticket-cell {
  color: #f7f8ff;
  font-size: 1.05rem;
  line-height: 1.2;
  font-weight: 600;
}

.promo-wrap {
  margin-top: 12px;
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 8px;
}

.promo-input {
  height: 42px;
  border-radius: 10px;
  border: 1px solid rgba(139, 148, 171, 0.42);
  background: rgba(255, 255, 255, 0.02);
  color: #e8edf9;
  padding: 0 12px;
  font-size: 1rem;
}

.promo-btn {
  height: 42px;
  border-radius: 10px;
  border: 1px solid rgba(139, 148, 171, 0.42);
  background: rgba(255, 255, 255, 0.07);
  color: #cfd6ea;
  padding: 0 16px;
  font-weight: 700;
  font-size: 0.95rem;
}

.total-line {
  margin-top: 12px;
  color: #f9fbff;
  font-size: 1rem;
}

.total-amount {
  font-size: 1.35rem;
  line-height: 1.1;
}

.policy-check {
  margin-top: 8px;
  color: #edf1ff;
  font-size: 0.9rem;
}

.policy-check input {
  width: 16px;
  height: 16px;
}

.pay-btn {
  margin-top: 10px;
  width: 100%;
  height: 44px;
  border: 0;
  border-radius: 10px;
  background: #ffffff;
  color: #111111;
  font-weight: 700;
  font-size: 1rem;
}

.pay-btn:disabled {
  opacity: 0.72;
}

.error-line {
  margin: 8px 0 0;
  color: #ff5b64;
  font-size: 0.9rem;
}

.sepay-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.62);
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 12px;
}

.sepay-modal {
  width: min(760px, 100%);
  max-height: min(94dvh, 920px);
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 14px;
  background: #111111;
  color: #f6f8ff;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.sepay-header {
  padding: 14px 18px;
  border-bottom: 1px solid rgba(122, 132, 165, 0.3);
}

.sepay-header-left {
  font-size: 1.1rem;
}

.close-btn {
  width: 30px;
  height: 30px;
  border: 0;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.08);
  color: #f7f9ff;
}

.sepay-timer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 14px;
  background: #0a0a0a;
  border-bottom: 1px solid rgba(122, 132, 165, 0.3);
  font-size: 1rem;
  font-weight: 600;
  position: sticky;
  top: 0;
  z-index: 3;
}

.sepay-timer-main {
  display: inline-flex;
  align-items: center;
  gap: 12px;
}

.timer-close-btn {
  width: 30px;
  height: 30px;
  border: 0;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.12);
  color: #f7f9ff;
  flex-shrink: 0;
}

.sepay-body {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.time-boxes.compact .time-cell {
  width: 40px;
  height: 40px;
  font-size: 1.05rem;
}

.sepay-warning {
  margin: 14px 18px;
  border-radius: 10px;
  background: #f2df76;
  color: #3f3100;
  display: flex;
  gap: 10px;
  padding: 10px 12px;
  font-size: 1rem;
}

.sepay-warning i {
  font-size: 1.15rem;
  margin-top: 2px;
}

.sepay-warning p {
  margin: 0;
}

.sepay-info-wrap {
  padding: 8px 18px 0;
  display: grid;
  grid-template-columns: 230px minmax(0, 1fr);
  gap: 12px;
}

.qr-wrap {
  border: 1px solid rgba(122, 132, 165, 0.3);
  border-radius: 12px;
  padding: 10px;
}

.qr-placeholder {
  min-height: 230px;
  border-radius: 8px;
  border: 1px dashed rgba(122, 132, 165, 0.45);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #cfd7ec;
  font-size: 0.95rem;
  font-weight: 600;
}

.qr-placeholder i {
  font-size: 2rem;
}

.qr-wrap img {
  width: 100%;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: block;
}

.download-qr-btn {
  width: 100%;
  margin-top: 8px;
  height: 40px;
  border-radius: 10px;
  border: 1px solid rgba(122, 132, 165, 0.3);
  background: transparent;
  color: #edf2ff;
  font-size: 1rem;
}

.bank-info {
  border: 1px solid rgba(122, 132, 165, 0.3);
  border-radius: 12px;
  padding: 8px 14px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.06), rgba(255, 255, 255, 0.03));
}

.info-row {
  min-height: 48px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(122, 132, 165, 0.2);
  gap: 12px;
}

.info-row:last-child {
  border-bottom: 0;
}

.info-row span {
  font-size: 1.05rem;
  color: #d9dfef;
}

.info-row strong {
  font-size: 1.05rem;
  color: #f8fbff;
}

.content-preview {
  display: inline-block;
  max-width: 210px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.info-row .text-primary {
  color: rgb(var(--bs-primary-rgb)) !important;
}

.copy-icon-btn {
  border: 0;
  background: transparent;
  color: #d9dfef;
  width: 24px;
  height: 24px;
  padding: 0;
}

.sepay-note {
  margin: 14px 18px 0;
  font-size: 1rem;
}

.sepay-note ul {
  margin: 6px 0 0;
  padding-left: 20px;
}

.sepay-footer {
  position: sticky;
  bottom: 0;
  z-index: 4;
  background: #0a0a0a;
  border-top: 1px solid rgba(122, 132, 165, 0.3);
  padding: 10px 12px 12px;
}

.confirm-paid-btn {
  margin: 0;
  width: 100%;
  height: 48px;
  border: 0;
  border-radius: 10px;
  background: #ffffff;
  color: #111111;
  font-weight: 700;
  font-size: 1.1rem;
}

[data-bs-theme='light'] .flip-payment-page {
  background:
    radial-gradient(76% 40% at 50% -8%, rgba(0, 0, 0, 0.03) 0%, rgba(0, 0, 0, 0) 70%),
    linear-gradient(180deg, #f7f9ff 0%, #eef2fb 100%);
}

[data-bs-theme='light'] .flip-payment-page .payment-card,
[data-bs-theme='light'] .flip-payment-page .sepay-modal {
  background: rgba(255, 255, 255, 0.95) !important;
  border-color: rgba(122, 132, 165, 0.24);
  box-shadow: 0 10px 24px rgba(23, 32, 59, 0.08);
}

[data-bs-theme='light'] .flip-payment-page .panel-title,
[data-bs-theme='light'] .flip-payment-page .event-title,
[data-bs-theme='light'] .flip-payment-page .receiver-col strong,
[data-bs-theme='light'] .flip-payment-page .method-single,
[data-bs-theme='light'] .flip-payment-page .policy-text,
[data-bs-theme='light'] .flip-payment-page .ticket-cell,
[data-bs-theme='light'] .flip-payment-page .total-line,
[data-bs-theme='light'] .flip-payment-page .policy-check,
[data-bs-theme='light'] .flip-payment-page .sepay-header-left,
[data-bs-theme='light'] .flip-payment-page .info-row strong,
[data-bs-theme='light'] .flip-payment-page .time-dot,
[data-bs-theme='light'] .flip-payment-page .sepay-note {
  color: #1f2a40;
}

[data-bs-theme='light'] .flip-payment-page .panel-subtitle,
[data-bs-theme='light'] .flip-payment-page .event-subline,
[data-bs-theme='light'] .flip-payment-page .event-address,
[data-bs-theme='light'] .flip-payment-page .receiver-label,
[data-bs-theme='light'] .flip-payment-page .ticket-header small,
[data-bs-theme='light'] .flip-payment-page .info-row span {
  color: #4f5b77;
}

[data-bs-theme='light'] .flip-payment-page .inline-edit-input,
[data-bs-theme='light'] .flip-payment-page .promo-input,
[data-bs-theme='light'] .flip-payment-page .promo-btn,
[data-bs-theme='light'] .flip-payment-page .method-single,
[data-bs-theme='light'] .flip-payment-page .time-cell,
[data-bs-theme='light'] .flip-payment-page .qr-wrap,
[data-bs-theme='light'] .flip-payment-page .bank-info,
[data-bs-theme='light'] .flip-payment-page .download-qr-btn,
[data-bs-theme='light'] .flip-payment-page .close-btn,
[data-bs-theme='light'] .flip-payment-page .timer-close-btn {
  background: rgba(255, 255, 255, 0.88);
  border-color: rgba(122, 132, 165, 0.3);
  color: #29334b;
}

[data-bs-theme='light'] .flip-payment-page .ticket-note,
[data-bs-theme='light'] .flip-payment-page .ticket-note i,
[data-bs-theme='light'] .flip-payment-page .receiver-row > i,
[data-bs-theme='light'] .flip-payment-page .icon-button,
[data-bs-theme='light'] .flip-payment-page .copy-icon-btn {
  color: #516186;
}

[data-bs-theme='light'] .flip-payment-page .receiver-optional {
  color: #5a6d94;
}

[data-bs-theme='light'] .flip-payment-page .ticket-header {
  background: rgba(255, 255, 255, 0.95);
}

[data-bs-theme='light'] .flip-payment-page .ticket-scroll {
  scrollbar-color: rgba(64, 83, 126, 0.62) rgba(88, 106, 146, 0.18);
}

[data-bs-theme='light'] .flip-payment-page .ticket-scroll::-webkit-scrollbar-track {
  background: rgba(88, 106, 146, 0.18);
}

[data-bs-theme='light'] .flip-payment-page .ticket-scroll::-webkit-scrollbar-thumb {
  background: rgba(64, 83, 126, 0.62);
  border: 2px solid rgba(255, 255, 255, 0.95);
}

[data-bs-theme='light'] .flip-payment-page .ticket-scroll::-webkit-scrollbar-thumb:active {
  background: rgba(45, 64, 108, 0.74);
}

[data-bs-theme='light'] .flip-payment-page .pay-btn,
[data-bs-theme='light'] .flip-payment-page .confirm-paid-btn {
  background: rgb(var(--bs-primary-rgb));
  color: #111111;
}

[data-bs-theme='light'] .flip-payment-page .sepay-overlay {
  background: rgba(41, 52, 83, 0.45);
}

[data-bs-theme='light'] .flip-payment-page .sepay-footer {
  background: rgba(246, 250, 255, 0.98);
  border-top-color: rgba(122, 132, 165, 0.35);
}

[data-bs-theme='light'] .flip-payment-page .sepay-timer {
  background: rgba(231, 238, 255, 0.8);
  border-bottom-color: rgba(122, 132, 165, 0.3);
  color: #1f2b44;
}

@media (max-width: 991.98px) {
  .payment-shell {
    height: auto;
    min-height: calc(100dvh - var(--global-nav-offset));
    padding: 10px;
  }

  .payment-main {
    grid-template-columns: minmax(0, 1fr);
    gap: 10px;
  }

  .left-col,
  .right-col {
    grid-template-rows: auto;
    overflow: visible;
  }

  .payment-card {
    padding: 12px;
    border-radius: 12px;
  }

  .panel-title {
    font-size: 1.1rem;
  }

  .event-panel {
    align-items: flex-start;
  }

  .event-thumb {
    width: 88px;
    height: 68px;
  }

  .event-title {
    font-size: 1rem;
    margin-bottom: 4px;
  }

  .event-subline,
  .event-address,
  .panel-subtitle,
  .policy-text,
  .ticket-note,
  .receiver-label,
  .policy-check,
  .error-line,
  .promo-btn {
    font-size: 0.875rem;
  }

  .receiver-col strong {
    font-size: 0.95rem;
  }

  .inline-edit-input,
  .promo-input {
    font-size: 1rem;
  }

  .ticket-cell {
    font-size: 0.95rem;
  }

  .time-cell {
    width: 38px;
    height: 38px;
    font-size: 1rem;
  }

  .time-dot {
    font-size: 1.1rem;
  }

  .total-line {
    font-size: 0.95rem;
  }

  .total-amount {
    font-size: 1.2rem;
  }

  .pay-btn {
    font-size: 0.95rem;
    height: 42px;
  }

  .sepay-overlay {
    padding: 0;
    align-items: stretch;
    justify-content: stretch;
  }

  .sepay-modal {
    width: 100dvw;
    height: 100dvh;
    max-height: 100dvh;
    border-radius: 0;
    border-width: 0;
  }

  .sepay-header {
    display: none;
  }

  .sepay-timer {
    font-size: 0.95rem;
    flex-direction: row;
    gap: 8px;
    padding: max(10px, env(safe-area-inset-top)) 12px 10px;
  }

  .sepay-timer-main {
    min-width: 0;
  }

  .sepay-warning {
    margin: 12px;
    font-size: 0.9rem;
  }

  .sepay-info-wrap {
    padding: 0 12px;
    grid-template-columns: 1fr;
  }

  .sepay-note {
    margin: 12px;
    font-size: 0.9rem;
  }

  .sepay-footer {
    padding: 10px 12px calc(12px + env(safe-area-inset-bottom));
  }

  .confirm-paid-btn {
    height: 44px;
    font-size: 1rem;
  }
}

@media (hover: hover) {
  .icon-button:hover,
  .close-btn:hover,
  .download-qr-btn:hover,
  .copy-icon-btn:hover {
    opacity: 0.9;
  }

  .pay-btn:hover:enabled,
  .confirm-paid-btn:hover {
    filter: brightness(0.98);
  }
}
</style>
