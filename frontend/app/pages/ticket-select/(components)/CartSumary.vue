<template>
  <div class="cart-summary-wrapper">
    <!-- Ticket Information -->
    <div class="mb-4">
      <h5 class="mb-3 text-reactive-primary">Thông tin vé</h5>
      
      <div class="card bg-reactive-primary p-3 mb-2" v-for="ticket in tickets" :key="ticket.id">
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
    <div class="mb-4">
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
              @click="$emit('removeItem', index)"
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
</template>

<script setup lang="ts">
import type { Ticket } from '../types/ticket.type'
import type { CartItem } from '../types/event-payment.type'

interface Props {
  tickets: Ticket[]
  cart: CartItem[]
  totalPrice: number
  totalTickets: number
}

interface Emits {
  (e: 'removeItem', index: number): void
}

defineProps<Props>()
defineEmits<Emits>()

const formatPrice = (price: number) => {
  return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
}
</script>