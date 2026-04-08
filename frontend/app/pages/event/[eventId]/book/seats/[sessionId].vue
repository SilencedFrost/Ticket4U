<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue'
import { useTicketSelect } from './composables/useTicketSelect'
import { useEventPayment } from './composables/useEventPayment'
import SeatingMap  from './(components)/SeatingMap.vue'
import EventInfo   from './(components)/EventInfo.vue'
import CartSummary from './(components)/CartSummary.vue'
import type { SelectedSeat } from './(types)/ticket.type'


const route     = useRoute()
const eventId   = route.params.eventId as string
const sessionId = route.params.sessionId as string

const seatingMapRef = ref()

const { event, tickets, floors, loading, error, fetchTicketSelect } = useTicketSelect()
const { cart, totalPrice, totalTickets, addToCart, removeFromCart, removeSeatFromCart, formatPrice } = useEventPayment()

watch(cart, (newCart) => {
  seatingMapRef.value?.syncCartSeats(newCart)
}, { deep: true })

function handleRemoveSeat(itemIndex: number, seatUuid: string) {
  removeSeatFromCart(itemIndex, seatUuid)
  // Call syncCartSeats directly — the deep watch on cart may not fire
  // reliably when a nested seats array is mutated in place
  nextTick(() => seatingMapRef.value?.syncCartSeats(cart.value))
}

const drawerOpen = ref(true)
const cartWidth  = ref(420)

onMounted(() => fetchTicketSelect(eventId, sessionId))

function retry() {
  fetchTicketSelect(eventId, sessionId)
}

function handleAddTicket(
    zoneId:     string,
    zoneName:   string,
    quantity:   number,
    price:      number,
    isStanding: boolean,
    seats?:     SelectedSeat[]
) {
  addToCart(zoneId, zoneName, quantity, price, isStanding, seats)
  drawerOpen.value = true
}

function handleBack() {
  navigateTo('/')
}

function proceedToCheckout() {
  if (cart.value.length > 0)
    navigateTo({ path: '/checkout', query: { cart: JSON.stringify(cart.value), eventId: event.value?.id } })
}

function startResize(e: MouseEvent) {
  const startX     = e.clientX
  const startWidth = cartWidth.value
  function onMove(ev: MouseEvent) { cartWidth.value = Math.min(700, Math.max(300, startWidth + startX - ev.clientX)) }
  function onUp() { globalThis.removeEventListener('mousemove', onMove); globalThis.removeEventListener('mouseup', onUp) }
  globalThis.addEventListener('mousemove', onMove)
  globalThis.addEventListener('mouseup', onUp)
}
</script>

<template>
  <div class="event-payment-wrapper container-fluid bg-reactive-primary text-reactive-primary vh-100 overflow-hidden p-0">

    <!-- Loading -->
    <div v-if="loading" class="d-flex justify-content-center align-items-center vh-100">
      <div class="text-center text-reactive-secondary">
        <div class="spinner-border text-primary mb-3" role="status"/>
        <p>{{ $t('select_ticket.loading') }}</p>
      </div>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="d-flex justify-content-center align-items-center vh-100">
      <div class="alert alert-danger text-center" style="max-width:400px;">
        <i class="bi bi-exclamation-triangle fs-3 mb-2 d-block"/>
        <p class="mb-0">{{ error }}</p>
        <button class="btn btn-outline-danger btn-sm mt-3" @click="retry">
          {{ $t('select_ticket.retry') }}
        </button>
      </div>
    </div>

    <!-- Main -->
    <div v-else-if="event" class="h-100">

      <!-- Desktop -->
      <div class="d-none d-lg-flex h-100">
        <div class="flex-grow-1 h-100 overflow-hidden" style="min-width:0">
          <SeatingMap
              ref="seatingMapRef" :tickets="tickets" :floors="floors" :cart="cart"
              @back="handleBack" @add-ticket="handleAddTicket"/>
        </div>
        <div class="resize-handle" @mousedown="startResize"/>
        <div class="cart-sidebar bg-reactive-primary d-flex flex-column h-100" :style="{ width: cartWidth + 'px', flexShrink: '0' }">
          <div class="flex-grow-1 overflow-auto px-4 pt-4">
            <EventInfo :event="event"/>
            <CartSummary
                :tickets="tickets" :cart="cart"
                :total-price="totalPrice" :total-tickets="totalTickets"
                @remove-item="removeFromCart"
                @remove-seat="handleRemoveSeat"/>
          </div>
          <div class="p-4 pt-3 border-top border-secondary">
            <button class="btn btn-primary w-100 py-3 fw-semibold" :disabled="cart.length === 0" @click="proceedToCheckout">
              {{ $t('select_ticket.checkout.button') }} <i class="bi bi-arrow-right ms-2"/>
            </button>
          </div>
        </div>
      </div>

      <!-- Mobile -->
      <div class="d-flex d-lg-none flex-column h-100 position-relative">
        <div class="flex-grow-1 overflow-hidden">
          <SeatingMap
              ref="seatingMapRef" :tickets="tickets" :floors="floors" :cart="cart"
              @back="handleBack" @add-ticket="handleAddTicket"/>
        </div>
        <transition name="fade">
          <div v-if="drawerOpen" class="drawer-backdrop" @click="drawerOpen = false"/>
        </transition>
        <div class="bottom-drawer bg-reactive-secondary" :class="{ open: drawerOpen }">
          <div class="drawer-handle d-flex align-items-center justify-content-between px-3" @click="drawerOpen = !drawerOpen">
            <div class="d-flex align-items-center gap-2">
              <i class="bi bi-cart3 text-primary fs-5"/>
              <span class="fw-semibold text-reactive-primary">{{ $t('select_ticket.cart.title') }}</span>
              <span v-if="totalTickets > 0" class="badge bg-primary rounded-pill">{{ totalTickets }}</span>
            </div>
            <div class="d-flex align-items-center gap-3">
              <span v-if="totalPrice > 0" class="text-primary fw-bold">{{ formatPrice(totalPrice) }}</span>
              <i class="bi fs-5 text-reactive-secondary" :class="drawerOpen ? 'bi-chevron-down' : 'bi-chevron-up'"/>
            </div>
          </div>
          <div class="drawer-content px-3 pb-3">
            <EventInfo :event="event"/>
            <CartSummary
                :tickets="tickets" :cart="cart"
                :total-price="totalPrice" :total-tickets="totalTickets"
                @remove-item="removeFromCart"
                @remove-seat="handleRemoveSeat"/>
          </div>
          <div class="px-3 pb-3 pt-2 border-top border-secondary">
            <button class="btn btn-primary w-100 py-2 fw-semibold" :disabled="cart.length === 0" @click="proceedToCheckout">
              {{ $t('select_ticket.checkout.button') }} <i class="bi bi-arrow-right ms-2"/>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.resize-handle { width:5px; cursor:col-resize; background:transparent; flex-shrink:0; transition:background 0.2s; z-index:10; }
.resize-handle:hover, .resize-handle:active { background: var(--bs-primary); }
.bottom-drawer { position:absolute; bottom:0; left:0; right:0; border-radius:16px 16px 0 0; box-shadow:0 -4px 24px rgba(0,0,0,.15); transition:transform 0.35s cubic-bezier(.4,0,.2,1); transform:translateY(calc(100% - 58px)); z-index:200; display:flex; flex-direction:column; max-height:85vh; }
.bottom-drawer.open { transform:translateY(0); }
.drawer-handle { min-height:58px; cursor:pointer; border-radius:16px 16px 0 0; flex-shrink:0; position:relative; }
.drawer-handle::before { content:''; position:absolute; top:8px; left:50%; transform:translateX(-50%); width:36px; height:4px; background:currentColor; border-radius:2px; opacity:0.2; }
.drawer-content { flex-grow:1; overflow-y:auto; -webkit-overflow-scrolling:touch; }
.drawer-backdrop { position:fixed; inset:0; background:rgba(0,0,0,.4); z-index:199; }
</style>