<template>
  <div class="event-payment-wrapper container-fluid bg-reactive-primary text-reactive-primary vh-100">
    <div class="row h-100 g-0">
      <!-- Left: Seating Map -->
      <div class="col-lg-8">
        <SeatingMap 
          @back="handleBack"
          @add-ticket="handleAddTicket"
        />
      </div>

      <!-- Right: Cart Summary -->
      <div class="col-lg-4 bg-reactive-secondary d-flex flex-column h-100">
        <div class="flex-grow-1 overflow-auto px-4 pt-4">
          <EventInfo :event="eventDetails" />
          
          <CartSummary 
            :tickets="ticketInfo"
            :cart="cart"
            :total-price="totalPrice"
            :total-tickets="totalTickets"
            @remove-item="removeFromCart"
            @add-ticket="handleAddTicket"
          />
        </div>

        <!-- Bottom Button -->
        <div class="p-4 pt-3 border-top border-secondary">
          <button 
            class="btn btn-primary w-100 py-3 fw-semibold"
            :disabled="cart.length === 0"
            @click="proceedToCheckout">
            {{ $t('event_payment.checkout.button') }}
            <i class="bi bi-arrow-right ms-2"/>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ticketData, eventData } from './data/event-data'
import { useEventPayment } from './composables/use-event-payment'
import SeatingMap from './(components)/SeatingMap.vue'
import EventInfo from './(components)/EventInfo.vue'
import CartSummary from './(components)/CartSummary.vue'

definePageMeta({
  layout: 'minimal'
})

const eventDetails = ref(eventData)
const ticketInfo = ref(ticketData)

const { cart, totalPrice, totalTickets, addToCart, removeFromCart } = useEventPayment()

const handleAddTicket = (
  zoneId: string, 
  zoneName: string, 
  quantity: number, 
  price: number
) => {
  addToCart(zoneId, zoneName, quantity, price)
}

const handleBack = () => {
  navigateTo('/event-detail')
}

const proceedToCheckout = () => {
  if (cart.value.length > 0) {
    navigateTo({
      path: '/checkout',
      query: {
        cart: JSON.stringify(cart.value),
        eventId: eventDetails.value.id
      }
    })
  }
}
</script>