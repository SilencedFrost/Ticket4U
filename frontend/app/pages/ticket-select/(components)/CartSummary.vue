<template>
  <div class="cart-summary-wrapper">

    <!-- Ticket Info -->
    <div class="mb-4">
      <h5 class="mb-3 text-reactive-primary">{{ $t('event_payment.ticket_info.title') }}</h5>
      <div v-for="ticket in tickets" :key="ticket.id" class="card bg-reactive-primary mb-2 overflow-hidden">
        <div class="p-3" :class="{ 'opacity-75': ticket.soldOut }">
          <div class="d-flex align-items-center gap-3">
            <div class="rounded flex-shrink-0" :style="{ backgroundColor: ticket.color, width: '40px', height: '40px' }"/>
            <div class="flex-grow-1">
              <div class="d-flex justify-content-between align-items-start mb-1">
                <h6 class="mb-0 text-reactive-primary">{{ ticket.name }}</h6>
                <span class="text-primary fw-bold">{{ formatPrice(ticket.price) }}</span>
              </div>
              <small class="text-reactive-secondary d-block">
                <i class="bi bi-people me-1"/>{{ ticket.available }} {{ $t('event_payment.ticket_info.available') }}
              </small>
              <small :class="isUnlimited(ticket) ? 'text-success' : 'text-warning'">
                <i class="bi bi-info-circle me-1"/>{{ getMaxLimitText(ticket) }}
              </small>
              <small class="ms-2 badge" :class="ticket.isStanding ? 'bg-secondary' : 'bg-info text-dark'">
                {{ ticket.isStanding ? $t('event_payment.ticket_info.standing') : $t('event_payment.ticket_info.seated') }}
              </small>
              <span v-if="ticket.soldOut" class="badge bg-danger ms-2 small">
                {{ $t('event_payment.ticket_info.sold_out') }}
              </span>
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
        <div v-for="(item, index) in cart" :key="index" class="card bg-reactive-primary p-3 mb-3">
          <div class="d-flex justify-content-between align-items-start mb-2">
            <div class="flex-grow-1">
              <h6 class="text-reactive-primary mb-1">{{ item.name }}</h6>
              <small class="text-reactive-secondary">{{ item.quantity }} × {{ formatPrice(item.price) }}</small>
              <div v-if="!item.isStanding && item.seats?.length" class="mt-1 d-flex flex-wrap gap-1">
                <span v-for="seat in item.seats" :key="seat.seatId" class="badge bg-secondary" style="font-size:0.65rem;">
                  {{ seat.seatId }}
                </span>
              </div>
            </div>
            <button class="btn btn-sm btn-outline-danger" @click="$emit('removeItem', index)">
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
import type { Ticket } from '../(types)/ticket.type'
import type { CartItem } from '../(types)/event-payment.type'

const { t } = useI18n()

defineProps<{ tickets: Ticket[]; cart: CartItem[]; totalPrice: number; totalTickets: number }>()
defineEmits<{ (e: 'removeItem', index: number): void }>()

const isUnlimited    = (ticket: Ticket) => !ticket.maxPerAccount
const getMaxLimitText = (ticket: Ticket) =>
  isUnlimited(ticket)
    ? t('event_payment.validation.unlimited')
    : t('event_payment.validation.max_per_account', { max: ticket.maxPerAccount })

const formatPrice = (price: number) => new Intl.NumberFormat('vi-VN').format(price) + ' đ'
</script>