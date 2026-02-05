<template>
  <div class="border border-secondary rounded mb-3">
    <div 
      class="d-flex justify-content-between align-items-start p-3 text-clickable"
      @click="toggleExpand"
    >
      <div class="flex-grow-1">
        <div class="d-flex align-items-center mb-1">
          <h6 class="mb-0 text-reactive-primary me-2">{{ ticket.name }}</h6>
          <span 
            v-if="ticket.soldOut" 
            class="badge bg-danger small"
          >
            Sold Out
          </span>
        </div>
        <small class="text-reactive-secondary">
          {{ ticket.available }} available
        </small>
      </div>
      
      <div class="d-flex align-items-center gap-3">
        <span class="text-primary fw-bold fs-5">
          {{ formatPrice(ticket.price) }}
        </span>
        <i 
          class="bi fs-5"
          :class="isExpanded ? 'bi-chevron-up' : 'bi-chevron-down'"
        ></i>
      </div>
    </div>

    <!-- Collapsible Content -->
    <div class="dropdown-content" :class="{ open: isExpanded }">
      <div class="border-top border-secondary p-3">
        <div class="mb-3">
          <label class="form-label small text-reactive-secondary">Quantity</label>
          <div class="d-flex align-items-center gap-2">
            <button 
              class="btn btn-reactive-gray btn-sm"
              @click="decreaseQuantity"
              :disabled="quantity <= 0"
            >
              <i class="bi bi-dash"></i>
            </button>
            <input 
              type="number" 
              class="form-control form-control-sm text-center bg-reactive-secondary text-reactive-primary border-0"
              style="width: 70px;"
              v-model.number="quantity"
              :max="ticket.available"
              min="0"
            />
            <button 
              class="btn btn-reactive-gray btn-sm"
              @click="increaseQuantity"
              :disabled="quantity >= ticket.available"
            >
              <i class="bi bi-plus"></i>
            </button>
          </div>
        </div>

        <button 
          class="btn btn-primary w-100"
          @click="addToCart"
          :disabled="quantity === 0"
        >
          Add to Cart
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { Ticket } from '../types/ticket'

interface Props {
  ticket: Ticket
}

interface Emits {
  (e: 'add', ticket: Ticket, quantity: number): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const isExpanded = ref(false)
const quantity = ref(0)

const toggleExpand = () => {
  if (!props.ticket.soldOut) {
    isExpanded.value = !isExpanded.value
  }
}

const increaseQuantity = () => {
  if (quantity.value < props.ticket.available) {
    quantity.value++
  }
}

const decreaseQuantity = () => {
  if (quantity.value > 0) {
    quantity.value--
  }
}

const addToCart = () => {
  if (quantity.value > 0) {
    emit('add', props.ticket, quantity.value)
    quantity.value = 0
    isExpanded.value = false
  }
}

const formatPrice = (price: number) => {
  return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
}
</script>