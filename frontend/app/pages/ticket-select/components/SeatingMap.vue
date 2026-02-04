<!-- app/pages/event-payment/components/SeatingMap.vue -->
<template>
  <div class="h-100 d-flex flex-column">
    <div class="d-flex justify-content-between align-items-center p-4 bg-reactive-secondary">
      <button 
        class="btn btn-link text-primary text-decoration-none p-0"
        @click="$emit('back')"
      >
        <i class="bi bi-arrow-left me-1"></i> Trở về
      </button>
      <div class="text-center">
        <h5 class="text-primary mb-0">Chọn khu vực</h5>
        <small class="text-reactive-secondary">Bấm vào hàng để chọn vé</small>
      </div>
      <div style="width: 80px;"></div>
    </div>

    <div class="flex-grow-1 overflow-auto p-4">
      <div class="text-center mb-4">
        <div class="badge bg-warning text-dark px-4 py-2">STAGE</div>
      </div>

      <div class="mx-auto" style="max-width: 900px;">
        <table class="table">
          <thead>
            <tr>
              <th></th>
              <th>Tên vé</th>
              <th>Còn lại</th>
              <th>Giá</th>
              <th>Trạng thái</th>
            </tr>
          </thead>
          <tbody>
            <!-- VIP SECTION -->
            <tr class="table-active">
              <td colspan="5"><strong>VIP SECTION</strong></td>
            </tr>
            <tr 
              v-for="ticket in svipTickets" 
              :key="ticket.id"
              @click="openZoneSelection(ticket)"
              style="cursor: pointer;"
            >
              <td>
                <div :style="{ backgroundColor: ticket.color, width: '30px', height: '30px' }"></div>
              </td>
              <td>{{ ticket.name }}</td>
              <td>{{ ticket.available }}</td>
              <td>{{ formatPrice(ticket.price) }}</td>
              <td>
                <span v-if="ticket.soldOut" class="badge bg-danger">Sold Out</span>
                <span v-else class="badge bg-success">Available</span>
              </td>
            </tr>

            <!-- ECONOMY SECTION -->
            <tr class="table-active">
              <td colspan="5"><strong>ECONOMY SECTION</strong></td>
            </tr>
            <tr 
              v-for="ticket in gaTickets" 
              :key="ticket.id"
              @click="openZoneSelection(ticket)"
              style="cursor: pointer;"
            >
              <td>
                <div :style="{ backgroundColor: ticket.color, width: '30px', height: '30px' }"></div>
              </td>
              <td>{{ ticket.name }}</td>
              <td>{{ ticket.available }}</td>
              <td>{{ formatPrice(ticket.price) }}</td>
              <td>
                <span v-if="ticket.soldOut" class="badge bg-danger">Sold Out</span>
                <span v-else class="badge bg-success">Available</span>
              </td>
            </tr>

            <!-- BUDGET SECTION -->
            <tr class="table-active">
              <td colspan="5"><strong>BUDGET SECTION</strong></td>
            </tr>
            <tr 
              v-for="ticket in budgetTickets" 
              :key="ticket.id"
              @click="openZoneSelection(ticket)"
              style="cursor: pointer;"
            >
              <td>
                <div :style="{ backgroundColor: ticket.color, width: '30px', height: '30px' }"></div>
              </td>
              <td>{{ ticket.name }}</td>
              <td>{{ ticket.available }}</td>
              <td>{{ formatPrice(ticket.price) }}</td>
              <td>
                <span v-if="ticket.soldOut" class="badge bg-danger">Sold Out</span>
                <span v-else class="badge bg-success">Available</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Selection Panel -->
      <div v-if="selectedZone" class="mt-4 mx-auto" style="max-width: 600px;">
        <div class="card p-4">
          <div class="d-flex justify-content-between mb-3">
            <h5>{{ selectedZone.name }}</h5>
            <button class="btn-close" @click="closeZoneSelection"></button>
          </div>

          <div v-if="selectedZone.soldOut" class="alert alert-danger">
            Hết vé
          </div>

          <div v-else>
            <div class="mb-3">
              <label>Số lượng</label>
              <div class="d-flex gap-2">
                <button class="btn btn-secondary" @click="decreaseQuantity">-</button>
                <input type="number" class="form-control text-center" v-model.number="quantity" :max="selectedZone.available" min="0" />
                <button class="btn btn-secondary" @click="increaseQuantity">+</button>
              </div>
            </div>

            <div class="mb-3">
              <div class="d-flex justify-content-between">
                <span>Giá:</span>
                <span>{{ formatPrice(selectedZone.price) }}</span>
              </div>
              <div class="d-flex justify-content-between">
                <span>Số lượng:</span>
                <span>{{ quantity }}</span>
              </div>
              <div class="d-flex justify-content-between fw-bold">
                <span>Tổng:</span>
                <span>{{ formatPrice(totalCost) }}</span>
              </div>
            </div>

            <button class="btn btn-primary w-100" @click="addToCart" :disabled="quantity === 0">
              Thêm vào giỏ
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Ticket } from '../types/ticket'
import { ticketData } from '../data/tickets'

interface Emits {
  (e: 'back'): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number): void
}

const emit = defineEmits<Emits>()

const selectedZone = ref<Ticket | null>(null)
const quantity = ref(0)

const svipTickets = computed(() => ticketData.filter(t => t.zone === 'SVIP'))
const gaTickets = computed(() => ticketData.filter(t => t.zone === 'GA'))
const budgetTickets = computed(() => ticketData.filter(t => t.zone === 'BUDGET'))

const totalCost = computed(() => {
  if (!selectedZone.value) return 0
  return selectedZone.value.price * quantity.value
})

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