<template>
  <div class="event-payment-wrapper container-fluid bg-reactive-primary text-reactive-primary vh-100">
    <!-- Loading -->
    <div v-if="loading" class="d-flex justify-content-center align-items-center vh-100">
      <div class="text-center text-reactive-secondary">
        <div class="spinner-border text-primary mb-3" role="status"/>
        <p>{{ $t('event_payment.loading') }}</p>
      </div>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="d-flex justify-content-center align-items-center vh-100">
      <div class="alert alert-danger text-center" style="max-width: 400px;">
        <i class="bi bi-exclamation-triangle fs-3 mb-2 d-block"/>
        <p class="mb-0">{{ error }}</p>
        <button class="btn btn-outline-danger btn-sm mt-3" @click="retry">
          {{ $t('event_payment.retry') }}
        </button>
      </div>
    </div>

    <!-- Main -->
    <div v-else-if="event" class="row h-100 g-0">
      <!-- Left: Seating Map -->
      <div class="col-lg-8">
        <SeatingMap
          :tickets="tickets"
          :floors="floors"
          @back="handleBack"
          @add-ticket="handleAddTicket"
        />
      </div>

      <!-- Right: Cart Summary -->
      <div class="col-lg-4 bg-reactive-secondary d-flex flex-column h-100">
        <div class="flex-grow-1 overflow-auto px-4 pt-4">
          <EventInfo :event="event" />
          <CartSummary
            :tickets="tickets"
            :cart="cart"
            :total-price="totalPrice"
            :total-tickets="totalTickets"
            @remove-item="removeFromCart"
            @add-ticket="handleAddTicket"
          />
        </div>

        <div class="p-4 pt-3 border-top border-secondary">
          <button
            class="btn btn-primary w-100 py-3 fw-semibold"
            :disabled="cart.length === 0"
            @click="proceedToCheckout"
          >
            {{ $t('event_payment.checkout.button') }}
            <i class="bi bi-arrow-right ms-2"/>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useTicketSelect } from './composables/use-ticket-select'
import { useEventPayment } from './composables/use-event-payment'
import SeatingMap from './(components)/SeatingMap.vue'
import EventInfo from './(components)/EventInfo.vue'
import CartSummary from './(components)/CartSummary.vue'
import type { SelectedSeat } from './(types)/ticket.type'

definePageMeta({ layout: 'minimal' })

// Route param instead of store — survives refresh
const route   = useRoute()
const eventId = route.params.eventId as string

if (!eventId) navigateTo('/')

const { event, tickets, floors, loading, error, fetchTicketSelect } = useTicketSelect()
const { cart, totalPrice, totalTickets, addToCart, removeFromCart }  = useEventPayment()

onMounted(() => fetchTicketSelect(eventId))
const retry = () => fetchTicketSelect(eventId)

const handleAddTicket = (
  zoneId: string,
  zoneName: string,
  quantity: number,
  price: number,
  seats?: SelectedSeat[]
) => {
  addToCart(zoneId, zoneName, quantity, price, seats)
}

const handleBack = () => navigateTo(`/event-detail/${eventId}`)

const proceedToCheckout = () => {
  if (cart.value.length > 0) {
    navigateTo({
      path: '/checkout',
      query: { cart: JSON.stringify(cart.value), eventId: event.value?.id }
    })
  }
}
</script>