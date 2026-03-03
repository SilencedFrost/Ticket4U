<template>
  <div class="cart-summary-wrapper">
    <!-- Ticket Information -->
    <div class="mb-4">
      <h5 class="mb-3 text-reactive-primary">{{ $t('event_payment.ticket_info.title') }}</h5>

      <div
        v-for="ticket in tickets"
        :key="ticket.id"
        class="card bg-reactive-primary mb-2 overflow-hidden"
      >
        <!-- Ticket Header -->
        <div
          class="p-3"
          :class="{ 'ticket-card-clickable': !ticket.soldOut, 'opacity-75': ticket.soldOut }"
          @click="toggleTicket(ticket.id)"
        >
          <div class="d-flex align-items-center gap-3">
            <div
              class="rounded flex-shrink-0"
              :style="{ backgroundColor: ticket.color, width: '40px', height: '40px' }"
            />
            <div class="flex-grow-1">
              <div class="d-flex justify-content-between align-items-start mb-1">
                <h6 class="mb-0 text-reactive-primary">{{ ticket.name }}</h6>
                <span class="text-primary fw-bold">{{ formatPrice(ticket.price) }}</span>
              </div>
              <small class="text-reactive-secondary d-block">
                <i class="bi bi-people me-1"/>
                {{ ticket.available }} {{ $t('event_payment.ticket_info.available') }}
              </small>
              <small :class="isUnlimited(ticket) ? 'text-success' : 'text-warning'">
                <i class="bi bi-info-circle me-1"/>
                {{ getMaxLimitText(ticket) }}
              </small>
              <!-- Standing / Seated badge -->
              <small class="ms-2 badge" :class="ticket.isStanding ? 'bg-secondary' : 'bg-info text-dark'">
                {{ ticket.isStanding ? $t('event_payment.ticket_info.standing') : $t('event_payment.ticket_info.seated') }}
              </small>
              <span v-if="ticket.soldOut" class="badge bg-danger ms-2 small">
                {{ $t('event_payment.ticket_info.sold_out') }}
              </span>
            </div>
            <i
              v-if="!ticket.soldOut && ticket.isStanding"
              class="bi text-reactive-secondary fs-5"
              :class="expandedTicketId === ticket.id ? 'bi-chevron-up' : 'bi-chevron-down'"
            />
            <!-- Seated hint — select on map -->
            <small v-else-if="!ticket.soldOut && !ticket.isStanding" class="text-reactive-secondary fst-italic">
              {{ $t('event_payment.ticket_info.select_on_map') }}
            </small>
          </div>
        </div>

        <!-- Standing zone quantity picker (dropdown) -->
        <div v-if="expandedTicketId === ticket.id && ticket.isStanding" class="dropdown-content-wrapper">
          <div class="border-top border-secondary p-3">
            <div v-if="ticket.soldOut" class="alert alert-danger mb-0">
              <i class="bi bi-exclamation-triangle me-2"/>
              {{ $t('event_payment.selection.sold_out_message') }}
            </div>
            <div v-else>
              <div v-if="!isUnlimited(ticket) && ticketQuantities[ticket.id] >= getMaxAllowed(ticket)" class="alert alert-warning mb-3">
                <i class="bi bi-exclamation-triangle me-2"/>
                {{ $t('event_payment.validation.max_reached', { max: ticket.maxPerAccount }) }}
              </div>

              <div class="mb-3">
                <label class="form-label text-reactive-primary fw-semibold small">{{ $t('event_payment.selection.quantity') }}</label>
                <div class="d-flex gap-2">
                  <button class="btn btn-reactive-gray btn-sm" @click="decreaseQuantity(ticket.id)" :disabled="ticketQuantities[ticket.id] <= 0">
                    <i class="bi bi-dash"></i>
                  </button>
                  <input
                    v-model.number="ticketQuantities[ticket.id]"
                    type="number"
                    class="form-control form-control-sm text-center bg-reactive-secondary text-reactive-primary border-0 fw-bold"
                    style="width: 80px;"
                    :max="getMaxAllowed(ticket)"
                    min="0"
                  />
                  <button class="btn btn-reactive-gray btn-sm" @click="increaseQuantity(ticket.id, ticket)" :disabled="ticketQuantities[ticket.id] >= getMaxAllowed(ticket)">
                    <i class="bi bi-plus"/>
                  </button>
                </div>
              </div>

              <div class="mb-3 d-flex justify-content-between align-items-center">
                <span class="text-reactive-secondary">{{ $t('event_payment.selection.total') }}:</span>
                <span class="text-primary fw-bold fs-5">
                  {{ formatPrice(ticket.price * (ticketQuantities[ticket.id] || 0)) }}
                </span>
              </div>

              <button
                class="btn btn-primary w-100"
                @click="addTicketToCart(ticket)"
                :disabled="!ticketQuantities[ticket.id] || ticketQuantities[ticket.id] === 0"
              >
                <i class="bi bi-cart-plus me-2"/>
                {{ $t('event_payment.selection.add_to_cart') }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Cart -->
    <div class="mb-4">
      <h5 class="mb-3 text-reactive-primary">{{ $t('event_payment.cart.title') }}</h5>

      <div v-if="cart.length === 0" class="text-center py-5 text-reactive-secondary">
        <i class="bi bi-cart-x fs-1 mb-3 d-block"/>
        <p>{{ $t('event_payment.cart.empty') }}</p>
        <small>{{ $t('event_payment.cart.empty_subtitle') }}</small>
      </div>

      <div v-else>
        <div
          v-for="(item, index) in cart"
          :key="index"
          class="card bg-reactive-primary p-3 mb-3"
        >
          <div class="d-flex justify-content-between align-items-start mb-2">
            <div class="flex-grow-1">
              <h6 class="text-reactive-primary mb-1">{{ item.name }}</h6>
              <small class="text-reactive-secondary">
                {{ item.quantity }} × {{ formatPrice(item.price) }}
              </small>

              <!-- Seat codes for seated zones -->
              <div v-if="!item.isStanding && item.seats && item.seats.length > 0" class="mt-1 d-flex flex-wrap gap-1">
                <span
                  v-for="seat in item.seats"
                  :key="seat.seatId"
                  class="badge bg-secondary"
                  style="font-size: 0.65rem;"
                >
                  {{ seat.seatId }}
                </span>
              </div>
            </div>
            <button class="btn btn-sm btn-outline-danger" @click="$emit('removeItem', index)">
              <i class="bi bi-trash"/>
            </button>
          </div>
          <div class="text-end">
            <span class="text-primary fw-bold fs-5">
              {{ formatPrice(item.price * item.quantity) }}
            </span>
          </div>
        </div>

        <!-- Total -->
        <div class="card bg-reactive-primary p-3 border-primary border-2">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <span class="text-reactive-secondary">{{ $t('event_payment.cart.total_tickets') }}</span>
            <span class="text-reactive-primary fw-bold">{{ totalTickets }}</span>
          </div>
          <div class="d-flex justify-content-between align-items-center border-top border-secondary pt-2">
            <span class="text-reactive-primary fs-5 fw-bold">{{ $t('event_payment.cart.total_price') }}</span>
            <span class="text-primary fs-4 fw-bold">{{ formatPrice(totalPrice) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import type { Ticket } from '../(types)/ticket.type'
import type { CartItem } from '../(types)/event-payment.type'

const { t } = useI18n()

interface Props {
  tickets: Ticket[]
  cart: CartItem[]
  totalPrice: number
  totalTickets: number
}

interface Emits {
  (e: 'removeItem', index: number): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number): void
}

defineProps<Props>()
const emit = defineEmits<Emits>()

const expandedTicketId  = ref<string | null>(null)
const ticketQuantities  = reactive<Record<string, number>>({})

const isUnlimited = (ticket: Ticket) =>
  ticket.maxPerAccount === null || ticket.maxPerAccount === 0

const getMaxAllowed = (ticket: Ticket) => {
  if (isUnlimited(ticket)) return ticket.available
  return Math.min(ticket.available, ticket.maxPerAccount as number)
}

const getMaxLimitText = (ticket: Ticket) => {
  if (isUnlimited(ticket)) return t('event_payment.validation.unlimited')
  return t('event_payment.validation.max_per_account', { max: ticket.maxPerAccount })
}

const toggleTicket = (ticketId: string) => {
  // Only expand for standing zones — seated zones are selected on the map
  const ticket = (props as any).tickets.find((t: Ticket) => t.id === ticketId)
  if (!ticket || !ticket.isStanding) return

  expandedTicketId.value = expandedTicketId.value === ticketId ? null : ticketId
  if (!ticketQuantities[ticketId]) ticketQuantities[ticketId] = 1
}

const increaseQuantity = (ticketId: string, ticket: Ticket) => {
  const current = ticketQuantities[ticketId] || 0
  if (current < getMaxAllowed(ticket)) ticketQuantities[ticketId] = current + 1
}

const decreaseQuantity = (ticketId: string) => {
  const current = ticketQuantities[ticketId] || 0
  if (current > 0) ticketQuantities[ticketId] = current - 1
}

const addTicketToCart = (ticket: Ticket) => {
  const quantity = ticketQuantities[ticket.id] || 0
  if (quantity > 0 && quantity <= getMaxAllowed(ticket)) {
    emit('addTicket', ticket.id, ticket.name, quantity, ticket.price)
    ticketQuantities[ticket.id] = 0
    expandedTicketId.value      = null
  }
}

const formatPrice = (price: number) =>
  new Intl.NumberFormat('vi-VN').format(price) + ' đ'
</script>

<style scoped>
.ticket-card-clickable {
  cursor: pointer;
  transition: all 0.2s ease;
  user-select: none;
}
.ticket-card-clickable:hover {
  background-color: var(--bg-reactive-secondary);
}
.ticket-card-clickable:active {
  transform: scale(0.98);
}
.dropdown-content-wrapper {
  animation: slideDown 0.3s ease;
}
@keyframes slideDown {
  from { opacity: 0; max-height: 0; }
  to   { opacity: 1; max-height: 500px; }
}
</style>