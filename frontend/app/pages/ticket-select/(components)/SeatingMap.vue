<template>
  <div class="seating-map-wrapper h-100 d-flex flex-column">
    <div class="d-flex justify-content-between align-items-center p-4 bg-reactive-secondary">
      <button 
        class="btn btn-link text-primary text-decoration-none p-0"
        @click="$emit('back')">
        <i class="bi bi-arrow-left me-1"></i> {{ $t('event_payment.header.back') }}
      </button>
      <div class="text-center">
        <h5 class="text-primary mb-0">{{ $t('event_payment.header.title') }}</h5>
        <small class="text-reactive-secondary">{{ $t('event_payment.header.subtitle') }}</small>
      </div>
      <div style="width: 80px;"></div>
    </div>
    <div class="flex-grow-1 overflow-auto p-4 d-flex align-items-center justify-content-center">
      <div style="max-width: 800px; width: 100%;">
        <!-- Stadium Layout Table -->
        <table class="table table-bordered text-center mb-0">
          <tbody>
            <tr>
              <td colspan="3" class="py-3 bg-warning text-dark fw-bold">
                {{ $t('event_payment.stage') }}
              </td>
            </tr>

            <tr>
              <td colspan="3" class="p-0">
                <div 
                  class="seat-zone p-4"
                  :style="{ backgroundColor: getSeatColor('svip') }"
                  @click="handleZoneClick('svip')">
                  <strong class="text-white">{{ getTicketName('svip') }}</strong>
                </div>
              </td>
            </tr>

            <tr>
              <td class="p-0" style="width: 40%;">
                <div 
                  class="seat-zone p-4"
                  :style="{ backgroundColor: getSeatColor('ga-a') }"
                  @click="handleZoneClick('ga-a')">
                  <strong class="text-white">{{ getTicketName('ga-a') }}</strong>
                </div>
              </td>
              <td class="p-0 bg-reactive-gray" style="width: 20%;">
                <div class="p-4">
                  <strong class="text-reactive-secondary">{{ $t('event_payment.foh') }}</strong>
                </div>
              </td>
              <td class="p-0" style="width: 40%;">
                <div 
                  class="seat-zone p-4"
                  :style="{ backgroundColor: getSeatColor('ga-b') }"
                  @click="handleZoneClick('ga-b')">
                  <strong class="text-white">{{ getTicketName('ga-b') }}</strong>
                </div>
              </td>
            </tr>

            <tr>
              <td class="p-0">
                <div 
                  class="seat-zone p-4 opacity-50"
                  :style="{ backgroundColor: getSeatColor('budget-left') }"
                  @click="handleZoneClick('budget-left')">
                  <strong class="text-white">{{ getTicketName('budget-left') }}</strong>
                  <br>
                  <small class="badge bg-danger mt-1">{{ $t('event_payment.ticket_info.sold_out') }}</small>
                </div>
              </td>
              <td class="p-0 bg-reactive-secondary"></td>
              <td class="p-0">
                <div 
                  class="seat-zone p-4 opacity-50"
                  :style="{ backgroundColor: getSeatColor('budget-right') }"
                  @click="handleZoneClick('budget-right')">
                  <strong class="text-white">{{ getTicketName('budget-right') }}</strong>
                  <br>
                  <small class="badge bg-danger mt-1">{{ $t('event_payment.ticket_info.sold_out') }}</small>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Selection Panel -->
    <div v-if="selectedZone" class="position-fixed bottom-0 start-0 end-0 p-4 bg-reactive-secondary border-top border-primary" style="z-index: 1000;">
      <div class="container" style="max-width: 600px;">
        <div class="d-flex justify-content-between align-items-start mb-3">
          <div>
            <h5 class="text-reactive-primary mb-1">{{ selectedZone.name }}</h5>
            <small class="text-reactive-secondary">
              <i class="bi bi-people me-1"></i>
              {{ selectedZone.available }} {{ $t('event_payment.selection.available') }}
            </small>
          </div>
          <button 
            class="btn-close" 
            @click="closeZoneSelection"
            aria-label="Close"></button>
        </div>

        <div v-if="selectedZone.soldOut" class="alert alert-danger mb-0">
          <i class="bi bi-exclamation-triangle me-2"></i>
          {{ $t('event_payment.selection.sold_out_message') }}
        </div>

        <div v-else class="row g-3">
          <div class="col-md-6">
            <label class="form-label text-reactive-primary fw-semibold small">{{ $t('event_payment.selection.quantity') }}</label>
            <div class="d-flex gap-2">
              <button class="btn btn-reactive-gray" @click="decreaseQuantity">
                <i class="bi bi-dash"></i>
              </button>
              <input 
                type="number" 
                class="form-control text-center bg-reactive-primary text-reactive-primary border-0 fw-bold"
                v-model.number="quantity" 
                :max="selectedZone.available" 
                min="0" 
              />
              <button class="btn btn-reactive-gray" @click="increaseQuantity">
                <i class="bi bi-plus"></i>
              </button>
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label text-reactive-primary fw-semibold small">{{ $t('event_payment.selection.total') }}</label>
            <div class="text-primary fs-4 fw-bold">
              {{ formatPrice(totalCost) }}
            </div>
          </div>

          <div class="col-12">
            <button 
              class="btn btn-primary w-100 py-2 fw-semibold" 
              @click="addToCart" 
              :disabled="quantity === 0">
              <i class="bi bi-cart-plus me-2"></i>
              {{ $t('event_payment.selection.add_to_cart') }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Ticket } from '../(types)/ticket.type'
import { ticketData } from '../data/event-data'

interface Emits {
  (e: 'back'): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number): void
}

const emit = defineEmits<Emits>()

const selectedZone = ref<Ticket | null>(null)
const quantity = ref(0)

const totalCost = computed(() => {
  if (!selectedZone.value) return 0
  return selectedZone.value.price * quantity.value
})

const getSeatColor = (ticketId: string) => {
  const ticket = ticketData.find(t => t.id === ticketId)
  return ticket?.color || '#ccc'
}

const getTicketName = (ticketId: string) => {
  const ticket = ticketData.find(t => t.id === ticketId)
  return ticket?.name || ''
}

const handleZoneClick = (ticketId: string) => {
  const ticket = ticketData.find(t => t.id === ticketId)
  if (ticket) {
    openZoneSelection(ticket)
  }
}

const openZoneSelection = (ticket: Ticket) => {
  selectedZone.value = ticket
  quantity.value = ticket.soldOut ? 0 : 1
}

const closeZoneSelection = () => {
  selectedZone.value = null
  quantity.value = 0
}

const increaseQuantity = () => {
  if (selectedZone.value && quantity.value < selectedZone.value.available) {
    quantity.value++
  }
}

const decreaseQuantity = () => {
  if (quantity.value > 0) {
    quantity.value--
  }
}

const addToCart = () => {
  if (selectedZone.value && quantity.value > 0) {
    emit('addTicket', selectedZone.value.id, selectedZone.value.name, quantity.value, selectedZone.value.price)
    closeZoneSelection()
  }
}

const formatPrice = (price: number) => {
  return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
}
</script>

<style scoped>
.seat-zone {
  cursor: pointer;
  transition: all 0.2s ease;
  min-height: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.seat-zone:hover:not(.opacity-50) {
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.table-bordered td {
  border-color: #444 !important;
}
</style>