<!-- app/pages/event-payment/index.vue -->
<template>
  <div class="container-fluid vh-100 bg-reactive-primary text-reactive-primary">
    <div class="row h-100 g-0">
      <!-- Left: Seating Map -->
      <div class="col-lg-8 bg-black">
        <SeatingMap 
          @back="handleBack"
          @add-ticket="handleAddTicketFromMap"
        />
      </div>

      <!-- Right: Cart Summary -->
      <div class="col-lg-4 bg-reactive-secondary p-4 d-flex flex-column">
        <div class="flex-grow-1 overflow-auto">
          <EventInfo :event="eventDetails" />

          <!-- Ticket Information -->
          <div class="mb-4">
            <h5 class="mb-3 text-reactive-primary">Thông tin vé</h5>
            
            <div class="card bg-reactive-primary p-3 mb-2" v-for="ticket in ticketInfo" :key="ticket.id">
              <div class="d-flex align-items-center gap-3">
                <div 
                  class="rounded flex-shrink-0" 
                  :style="{ 
                    backgroundColor: ticket.color,
                    width: '40px',
                    height: '40px'
                  }"
                ></div>
                <div class="flex-grow-1">
                  <div class="d-flex justify-content-between align-items-start mb-1">
                    <h6 class="mb-0 text-reactive-primary">{{ ticket.name }}</h6>
                    <span class="text-primary fw-bold">{{ formatPrice(ticket.price) }}</span>
                  </div>
                  <small class="text-reactive-secondary">
                    <i class="bi bi-people me-1"></i>
                    {{ ticket.available }} chỗ còn lại
                  </small>
                  <span 
                    v-if="ticket.soldOut" 
                    class="badge bg-danger ms-2 small"
                  >
                    Sold Out
                  </span>
                </div>
              </div>
            </div>
          </div>

          <!-- Cart -->
          <div class="mt-4">
            <h5 class="mb-3 text-reactive-primary">Giỏ hàng của bạn</h5>

            <div v-if="cart.length === 0" class="text-center py-5 text-reactive-secondary">
              <i class="bi bi-cart-x fs-1 mb-3 d-block"></i>
              <p>Chưa có vé nào trong giỏ hàng</p>
              <small>Chọn khu vực bên trái để thêm vé</small>
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
                  </div>
                  <button 
                    class="btn btn-sm btn-outline-danger"
                    @click="removeFromCart(index)"
                  >
                    <i class="bi bi-trash"></i>
                  </button>
                </div>
                <div class="text-end">
                  <span class="text-primary fw-bold fs-5">
                    {{ formatPrice(item.price * item.quantity) }}
                  </span>
                </div>
              </div>

              <!-- Total Summary -->
              <div class="card bg-reactive-primary p-3 border-primary border-2">
                <div class="d-flex justify-content-between align-items-center mb-2">
                  <span class="text-reactive-secondary">Tổng số vé:</span>
                  <span class="text-reactive-primary fw-bold">{{ totalTickets }}</span>
                </div>
                <div class="d-flex justify-content-between align-items-center border-top border-secondary pt-2">
                  <span class="text-reactive-primary fs-5 fw-bold">Tổng tiền:</span>
                  <span class="text-primary fs-4 fw-bold">{{ formatPrice(totalPrice) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Bottom Button -->
        <button 
          class="btn btn-primary w-100 py-3 mt-3 fw-semibold"
          :disabled="cart.length === 0"
          @click="proceedToCheckout"
        >
          Tiếp tục thanh toán
          <i class="bi bi-arrow-right ms-2"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { EventDetails } from './types/ticket'
import SeatingMap from './components/SeatingMap.vue'
import EventInfo from './components/EventInfo.vue'

interface CartItem {
  zoneId: string
  name: string
  quantity: number
  price: number
}

interface TicketInfo {
  id: string
  name: string
  color: string
  price: number
  available: number
  soldOut: boolean
}

const eventDetails = ref<EventDetails>({
  id: '1',
  title: 'ANH TRAI "SAY HI" 2025 CONCERT - ĐÊM 2',
  date: '14 Tháng 03, 2026',
  time: '12:00 - 23:00',
  venue: 'Sân Vận Động Quốc Gia Mỹ Đình'
})

const ticketInfo = ref<TicketInfo[]>([
  {
    id: 'svip',
    name: 'SVIP - Dừa Lòng',
    color: '#E53E3E',
    price: 3000000,
    available: 100,
    soldOut: false
  },
  {
    id: 'ga-a',
    name: 'Vé Phổ Thông - GA A',
    color: '#06B6D4',
    price: 500000,
    available: 150,
    soldOut: false
  },
  {
    id: 'ga-b',
    name: 'Vé Phổ Thông - GA B',
    color: '#22D3EE',
    price: 500000,
    available: 150,
    soldOut: false
  },
  {
    id: 'budget',
    name: 'Vé Tiết Kiệm',
    color: '#D69E2E',
    price: 300000,
    available: 0,
    soldOut: true
  }
])

const cart = ref<CartItem[]>([])

const totalPrice = computed(() => {
  return cart.value.reduce((sum, item) => {
    return sum + (item.price * item.quantity)
  }, 0)
})

const totalTickets = computed(() => {
  return cart.value.reduce((sum, item) => sum + item.quantity, 0)
})

const handleAddTicketFromMap = (
  zoneId: string, 
  zoneName: string, 
  quantity: number, 
  price: number
) => {
  const existingItem = cart.value.find(item => item.zoneId === zoneId)
  
  if (existingItem) {
    existingItem.quantity += quantity
  } else {
    cart.value.push({
      zoneId,
      name: zoneName,
      quantity,
      price
    })
  }
}

const removeFromCart = (index: number) => {
  cart.value.splice(index, 1)
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

const formatPrice = (price: number) => {
  return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
}
</script>