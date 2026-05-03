<script setup lang="ts">
import {
  useCheckoutStore,
  type CheckoutCartItem,
  type CheckoutSession,
} from '~/stores/checkoutStore';

definePageMeta({
  path: '/event/:eventId/book/success',
  alias: ['/checkout/payment/success', '/payment/success'],
});

const route = useRoute();
const checkoutStore = useCheckoutStore();
const { formatPrice } = useFormatter();

type PurchasedTicket = {
  key: string;
  ticketType: string;
  zoneName: string;
  seatName: string;
  basePrice: number;
};

const orderId = computed(() => String(route.query.orderId ?? ''));
const transactionId = computed(() => String(route.query.transactionId ?? ''));
const orderCode = computed(() => String(route.query.orderCode ?? ''));
const currency = computed(() => {
  const value = String(route.query.currency ?? 'VND');
  return value === 'USD' ? 'USD' : 'VND';
});
const amount = computed(() => {
  const rawValue = Number(route.query.amount ?? 0);
  return Number.isFinite(rawValue) ? rawValue : 0;
});

const checkoutSnapshot = ref<CheckoutSession | null>(null);

const purchasedTickets = computed<PurchasedTicket[]>(() => {
  const session = checkoutSnapshot.value;

  if (!session) {
    return [];
  }

  return session.cart.flatMap((item: CheckoutCartItem, itemIndex: number) => {
    if (item.seats?.length) {
      return item.seats.map((seat, seatIndex) => ({
        key: `${item.zoneId}-${seat.seatUuid}-${seatIndex}`,
        ticketType: item.isStanding ? 'STANDING' : 'SEAT',
        zoneName: seat.zoneName,
        seatName: seat.seatName,
        basePrice: seat.price,
      }));
    }

    return Array.from({ length: item.quantity }, (_, ticketIndex) => ({
      key: `${item.zoneId}-${itemIndex}-${ticketIndex}`,
      ticketType: item.isStanding ? 'STANDING' : 'SEAT',
      zoneName: item.name,
      seatName: `${item.name}-${ticketIndex + 1}`,
      basePrice: item.price,
    }));
  });
});

const ticketCount = computed(() => purchasedTickets.value.length);
const eventTitle = computed(() => checkoutSnapshot.value?.event.title ?? 'N/A');
const eventVenue = computed(() => checkoutSnapshot.value?.event.venue ?? 'N/A');
const paidAmount = computed(() => checkoutSnapshot.value?.totalPrice ?? amount.value);

onMounted(() => {
  checkoutStore.restoreCheckoutSession();
  checkoutSnapshot.value = checkoutStore.checkoutSession;
  checkoutStore.clearCheckoutSession();
});
</script>

<template>
  <div class="min-vh-100 bg-body py-2 py-lg-3 px-3 px-lg-4">
    <div class="container-xxl">
      <div class="row justify-content-center">
        <div class="col-12 col-xl-10">
          <div class="card shadow-sm border-0 bg-body">
            <div class="card-body p-3 p-md-4 p-lg-5">
              <div class="text-center mb-4">
                <h1 class="h3 fw-bold mb-2 text-success">{{ $t('payment.success.title') }}</h1>
                <p class="text-body-secondary mb-0">
                  {{ $t('payment.success.subtitle') }}
                </p>
              </div>

              <div class="row g-3 mb-4">
                <div class="col-12 col-md-6 col-xl-3">
                  <div class="card h-100 border bg-body-tertiary">
                    <div class="card-body">
                      <div class="text-body-secondary small mb-1">
                        {{ $t('payment.success.order_id') }}
                      </div>
                      <div class="fw-semibold text-break">{{ orderId || 'N/A' }}</div>
                    </div>
                  </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                  <div class="card h-100 border bg-body-tertiary">
                    <div class="card-body">
                      <div class="text-body-secondary small mb-1">
                        {{ $t('payment.success.order_code') }}
                      </div>
                      <div class="fw-semibold text-break">{{ orderCode || 'N/A' }}</div>
                    </div>
                  </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                  <div class="card h-100 border bg-body-tertiary">
                    <div class="card-body">
                      <div class="text-body-secondary small mb-1">
                        {{ $t('payment.success.transaction_id') }}
                      </div>
                      <div class="fw-semibold text-break">{{ transactionId || 'N/A' }}</div>
                    </div>
                  </div>
                </div>
                <div class="col-12 col-md-6 col-xl-3">
                  <div class="card h-100 border bg-body-tertiary">
                    <div class="card-body">
                      <div class="text-body-secondary small mb-1">
                        {{ $t('payment.success.amount') }}
                      </div>
                      <div class="fw-semibold">{{ formatPrice(paidAmount, currency) }}</div>
                    </div>
                  </div>
                </div>
                <div class="col-12 col-md-6 col-xl-4">
                  <div class="card h-100 border bg-body-tertiary">
                    <div class="card-body">
                      <div class="text-body-secondary small mb-1">
                        {{ $t('payment.success.event') }}
                      </div>
                      <div class="fw-semibold text-break">{{ eventTitle }}</div>
                    </div>
                  </div>
                </div>
                <div class="col-12 col-md-6 col-xl-4">
                  <div class="card h-100 border bg-body-tertiary">
                    <div class="card-body">
                      <div class="text-body-secondary small mb-1">
                        {{ $t('payment.success.venue') }}
                      </div>
                      <div class="fw-semibold text-break">{{ eventVenue }}</div>
                    </div>
                  </div>
                </div>
                <div class="col-12 col-md-6 col-xl-4">
                  <div class="card h-100 border bg-body-tertiary">
                    <div class="card-body">
                      <div class="text-body-secondary small mb-1">
                        {{ $t('payment.success.tickets') }}
                      </div>
                      <div class="fw-semibold">{{ ticketCount }}</div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card border mb-4">
                <div
                  class="card-header bg-transparent border-0 pb-0 d-flex flex-wrap gap-2 justify-content-between align-items-center"
                >
                  <div>
                    <h2 class="h5 fw-semibold mb-1">
                      {{ $t('payment.success.ticket_list_title') }}
                    </h2>
                    <p class="text-body-secondary mb-0">
                      {{ $t('payment.success.ticket_list_subtitle') }}
                    </p>
                  </div>
                  <span class="badge text-bg-primary rounded-pill"
                    >{{ ticketCount }} {{ $t('common.tickets') }}</span
                  >
                </div>

                <div class="card-body pt-3">
                  <div v-if="purchasedTickets.length" class="table-responsive">
                    <table class="table table-sm align-middle mb-0">
                      <thead class="table-light">
                        <tr>
                          <th scope="col" class="text-nowrap">#</th>
                          <th scope="col">{{ $t('payment.success.table_ticket') }}</th>
                          <th scope="col" class="text-nowrap">
                            {{ $t('payment.success.table_zone') }}
                          </th>
                          <th scope="col" class="text-nowrap">
                            {{ $t('payment.success.table_type') }}
                          </th>
                          <th scope="col" class="text-end text-nowrap">
                            {{ $t('payment.success.table_price') }}
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(ticket, index) in purchasedTickets" :key="ticket.key">
                          <td class="fw-semibold text-body-secondary">{{ index + 1 }}</td>
                          <td>
                            <div class="fw-semibold">{{ ticket.seatName }}</div>
                          </td>
                          <td class="text-nowrap">{{ ticket.zoneName }}</td>
                          <td class="text-nowrap">
                            <span class="badge text-bg-secondary">{{ ticket.ticketType }}</span>
                          </td>
                          <td class="text-end text-nowrap">
                            {{ formatPrice(ticket.basePrice, currency) }}
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>

                  <div v-else class="alert alert-warning mb-0" role="alert">
                    {{ $t('payment.success.no_tickets_error') }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
