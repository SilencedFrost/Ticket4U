<script setup lang="ts">
import { ref } from 'vue'
import type { Ticket } from '../(types)/ticket.type'
import type { CartItem } from '../(types)/event-payment.type'

const { t, locale } = useI18n()

const props = defineProps<{
  tickets:      Ticket[]
  cart:         CartItem[]
  totalPrice:   number
  totalTickets: number
}>()

defineEmits<{ (e: 'removeItem', index: number): void }>()

// Expanded state for details dropdown
const expandedIds = ref<Set<string>>(new Set())
const toggleExpanded = (id: string) => {
  if (expandedIds.value.has(id)) expandedIds.value.delete(id)
  else expandedIds.value.add(id)
}

// Helpers
const hasDetails = (ticket: Ticket) =>
    !!(ticket.descriptionVi || ticket.descriptionEn || ticket.perks?.length || ticket.giftImageUrl)

const isUnlimited    = (ticket: Ticket) => !ticket.maxPerAccount
const getMaxLimitText = (ticket: Ticket) =>
    isUnlimited(ticket)
        ? t('select_ticket.validation.unlimited')
        : t('select_ticket.validation.max_per_account', { max: ticket.maxPerAccount })

const getTicketColor = (zoneId: string): string =>
    props.tickets.find(t => t.id === zoneId)?.color ?? '#6366f1'

const formatPrice = (price: number) =>
    new Intl.NumberFormat('vi-VN').format(price) + ' đ'
</script>

<template>
  <div class="cart-summary-wrapper">

    <!-- Ticket Info -->
    <div class="mb-4">
      <h5 class="mb-3 text-reactive-primary">{{ $t('select_ticket.ticket_info.title') }}</h5>

      <div v-for="ticket in tickets" :key="ticket.id" class="ticket-card card border-0 mb-2 overflow-hidden">
        <div :class="{ 'opacity-60': ticket.soldOut }">

          <!-- Main row -->
          <div class="p-3">
            <div class="d-flex align-items-start gap-3">
              <!-- Color swatch -->
              <div class="rounded flex-shrink-0 mt-1" :style="{ backgroundColor: ticket.color, width: '36px', height: '36px' }"/>

              <!-- Info -->
              <div class="flex-grow-1 min-w-0">
                <div class="d-flex justify-content-between align-items-start gap-2 mb-1">
                  <h6 class="mb-0 text-reactive-primary fw-semibold text-truncate">{{ ticket.name }}</h6>
                  <span class="text-primary fw-bold flex-shrink-0">{{ formatPrice(ticket.price) }}</span>
                </div>

                <div class="d-flex flex-wrap align-items-center gap-2 mb-1">
                  <small class="text-reactive-secondary">
                    <i class="bi bi-people me-1"/>{{ ticket.available }} {{ $t('select_ticket.ticket_info.available') }}
                  </small>
                  <span class="badge" :class="ticket.isStanding ? 'bg-secondary' : 'bg-info text-dark'">
                    {{ ticket.isStanding ? $t('select_ticket.ticket_info.standing') : $t('select_ticket.ticket_info.seated') }}
                  </span>
                  <span v-if="ticket.soldOut" class="badge bg-danger">{{ $t('select_ticket.ticket_info.sold_out') }}</span>
                </div>

                <small :class="isUnlimited(ticket) ? 'text-success' : 'text-warning'">
                  <i class="bi bi-ticket me-1"/>{{ getMaxLimitText(ticket) }}
                </small>
              </div>
            </div>
          </div>

          <!-- Detailed info dropdown -->
          <div v-if="hasDetails(ticket)">
            <button
                class="btn btn-sm w-100 d-flex align-items-center justify-content-between px-3 py-2 border-top border-secondary text-reactive-secondary"
                style="background:transparent;border-radius:0;"
                @click="toggleExpanded(ticket.id)"
            >
              <small>{{ $t('select_ticket.ticket_info.details') }}</small>
              <i class="bi" :class="expandedIds.has(ticket.id) ? 'bi-chevron-up' : 'bi-chevron-down'" style="font-size:0.7rem;transition:transform 0.2s;"/>
            </button>

            <Transition name="detail-expand">
              <div v-if="expandedIds.has(ticket.id)" class="px-3 pb-3">
                <!-- Description -->
                <div v-if="ticket.descriptionVi || ticket.descriptionEn" class="mt-2">
                  <small class="text-reactive-secondary fw-semibold d-block mb-1">
                    <i class="bi bi-info-circle me-1"/>{{ $t('select_ticket.ticket_info.description') }}
                  </small>
                  <small class="text-reactive-secondary">{{ locale === 'vi' ? (ticket.descriptionVi || ticket.descriptionEn) : (ticket.descriptionEn || ticket.descriptionVi) }}</small>
                </div>

                <!-- Perks -->
                <div v-if="ticket.perks?.length" class="mt-2">
                  <small class="text-reactive-secondary fw-semibold d-block mb-1">
                    <i class="bi bi-gift me-1"/>{{ $t('select_ticket.ticket_info.perks') }}
                  </small>
                  <div class="d-flex flex-wrap gap-1">
                  <span
                      v-for="perk in ticket.perks" :key="perk"
                      class="badge small"
                      style="background:rgba(99,102,241,0.2);color:#a5b4fc;border:1px solid rgba(99,102,241,0.3);"
                  >
                    <i class="bi bi-check2 me-1"/>{{ perk }}
                  </span>
                  </div>
                </div>

                <!-- Gift image -->
                <div v-if="ticket.giftImageUrl" class="mt-2">
                  <img :src="ticket.giftImageUrl" class="rounded" style="max-height:80px;object-fit:cover;width:100%;"/>
                </div>
              </div>
            </Transition>
          </div>

        </div>
      </div>
    </div>

    <!-- Cart -->
    <div class="mb-4">
      <h5 class="mb-3 text-reactive-primary">{{ $t('select_ticket.cart.title') }}</h5>

      <div v-if="cart.length === 0" class="text-center py-5 text-reactive-secondary">
        <i class="bi bi-cart-x fs-1 mb-3 d-block"/>
        <p>{{ $t('select_ticket.cart.empty') }}</p>
        <small>{{ $t('select_ticket.cart.empty_subtitle') }}</small>
      </div>

      <div v-else>
        <div v-for="(item, index) in cart" :key="index" class="card bg-reactive-primary p-3 mb-3">
          <div class="d-flex justify-content-between align-items-start mb-2">
            <div class="flex-grow-1 min-w-0">
              <!-- Zone name + color dot -->
              <div class="d-flex align-items-center gap-2 mb-1">
                <div
                    class="rounded-circle flex-shrink-0"
                    :style="{ width: '10px', height: '10px', background: getTicketColor(item.zoneId) }"
                />
                <h6 class="text-reactive-primary mb-0 text-truncate">{{ item.name }}</h6>
              </div>
              <small class="text-reactive-secondary">{{ item.quantity }} × {{ formatPrice(item.price) }}</small>

              <!-- Seat codes — shows seatCode (e.g. A1) which matches DB seatCode field -->
              <div v-if="!item.isStanding && item.seats?.length" class="mt-2 d-flex flex-wrap gap-1">
                <span
                    v-for="seat in item.seats" :key="seat.seatId"
                    class="badge"
                    :style="{ background: getTicketColor(item.zoneId) + '33', color: getTicketColor(item.zoneId), border: `1px solid ${getTicketColor(item.zoneId)}55` }"
                    style="font-size:0.65rem;"
                >
                  {{ seat.seatId }}
                </span>
              </div>
            </div>
            <button class="btn btn-sm btn-outline-danger ms-2 flex-shrink-0" @click="$emit('removeItem', index)">
              <i class="bi bi-trash"/>
            </button>
          </div>
          <div class="text-end">
            <span class="text-primary fw-bold fs-5">{{ formatPrice(item.price * item.quantity) }}</span>
          </div>
        </div>

        <!-- Total -->
        <div class="card bg-reactive-primary p-3 border-primary border-2">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <span class="text-reactive-secondary">{{ $t('select_ticket.cart.total_tickets') }}</span>
            <span class="text-reactive-primary fw-bold">{{ totalTickets }}</span>
          </div>
          <div class="d-flex justify-content-between align-items-center border-top border-secondary pt-2">
            <span class="text-reactive-primary fs-5 fw-bold">{{ $t('select_ticket.cart.total_price') }}</span>
            <span class="text-primary fs-4 fw-bold">{{ formatPrice(totalPrice) }}</span>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>


<style scoped>
.ticket-card { background: rgba(var(--bs-secondary-rgb), 0.15); }
.opacity-60 { opacity: 0.6; }

/* Smooth dropdown expand */
.detail-expand-enter-active,
.detail-expand-leave-active {
  transition: max-height 0.25s ease, opacity 0.2s ease;
  overflow: hidden;
  max-height: 300px;
}
.detail-expand-enter-from,
.detail-expand-leave-to {
  max-height: 0;
  opacity: 0;
}
</style>