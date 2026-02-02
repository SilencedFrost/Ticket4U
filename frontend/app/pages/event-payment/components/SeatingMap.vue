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
        <small class="text-reactive-secondary">Bấm vào khu vực để chọn vé</small>
      </div>
      <div style="width: 80px;"></div>
    </div>

    <div class="flex-grow-1 overflow-auto p-4">
      <div class="text-center mb-4">
        <div class="badge bg-warning text-dark px-4 py-2 fs-6">STAGE</div>
      </div>

      <!-- Seating Map -->
      <div class="mx-auto" style="max-width: 800px;">
        
        <!-- Row 1: SVIP -->
        <div class="row g-3 mb-3">
          <div class="col-12">
            <div 
              class="seat-zone p-5 text-center rounded menu-item-left"
              :class="{ 'border-primary border-3': selectedZone?.id === 'svip' }"
              style="background-color: #E53E3E;"
              @click="openZoneSelection('svip', 'SVIP - Dừa Lòng', 3000000, 100)"
            >
              <h4 class="mb-0 text-white">SVIP - Dừa Lòng</h4>
              <small class="text-white opacity-75">Click to select</small>
            </div>
          </div>
        </div>

        <!-- Row 2: GA A, FOH, GA B -->
        <div class="row g-3 mb-3">
          <div class="col-4">
            <div 
              class="seat-zone p-4 text-center rounded menu-item-left"
              :class="{ 'border-primary border-3': selectedZone?.id === 'ga-a' }"
              style="background-color: #06B6D4;"
              @click="openZoneSelection('ga-a', 'Vé Phổ Thông - GA A', 500000, 150)"
            >
              <h5 class="mb-0 text-white">GA A</h5>
              <small class="text-white opacity-75">Click to select</small>
            </div>
          </div>
          <div class="col-4">
            <div class="p-4 text-center bg-reactive-gray rounded">
              <h6 class="mb-0 text-reactive-secondary">FOH</h6>
            </div>
          </div>
          <div class="col-4">
            <div 
              class="seat-zone p-4 text-center rounded menu-item-left"
              :class="{ 'border-primary border-3': selectedZone?.id === 'ga-b' }"
              style="background-color: #22D3EE;"
              @click="openZoneSelection('ga-b', 'Vé Phổ Thông - GA B', 500000, 150)"
            >
              <h5 class="mb-0 text-white">GA B</h5>
              <small class="text-white opacity-75">Click to select</small>
            </div>
          </div>
        </div>

        <!-- Row 3: Vé Tiết Kiệm -->
        <div class="row g-3">
          <div class="col-5">
            <div 
              class="seat-zone p-4 text-center rounded menu-item-left"
              :class="{ 'border-primary border-3': selectedZone?.id === 'budget-left' }"
              style="background-color: #D69E2E;"
              @click="openZoneSelection('budget-left', 'Vé Tiết Kiệm - Trái', 300000, 0, true)"
            >
              <h6 class="mb-0 text-white">Vé Tiết Kiệm</h6>
              <small class="text-white opacity-75">
                <span class="badge bg-danger">Sold Out</span>
              </small>
            </div>
          </div>
          <div class="col-2">
            <!-- Empty middle -->
          </div>
          <div class="col-5">
            <div 
              class="seat-zone p-4 text-center rounded menu-item-left"
              :class="{ 'border-primary border-3': selectedZone?.id === 'budget-right' }"
              style="background-color: #D69E2E;"
              @click="openZoneSelection('budget-right', 'Vé Tiết Kiệm - Phải', 300000, 0, true)"
            >
              <h6 class="mb-0 text-white">Vé Tiết Kiệm</h6>
              <small class="text-white opacity-75">
                <span class="badge bg-danger">Sold Out</span>
              </small>
            </div>
          </div>
        </div>
      </div>

      <!-- Zone Selection Modal/Panel -->
      <div v-if="selectedZone" class="mt-4">
        <div class="card bg-reactive-secondary p-4 mx-auto" style="max-width: 600px;">
          <div class="d-flex justify-content-between align-items-start mb-3">
            <div>
              <h5 class="text-reactive-primary mb-1">{{ selectedZone.name }}</h5>
              <small class="text-reactive-secondary">
                {{ selectedZone.available }} tickets available
              </small>
            </div>
            <button 
              class="btn-close" 
              @click="closeZoneSelection"
              aria-label="Close"
            ></button>
          </div>

          <div v-if="selectedZone.soldOut" class="alert alert-danger">
            <i class="bi bi-exclamation-triangle me-2"></i>
            This zone is sold out
          </div>

          <div v-else>
            <!-- Quantity Selector -->
            <div class="mb-4">
              <label class="form-label text-reactive-primary">Số lượng vé</label>
              <div class="d-flex align-items-center gap-3">
                <button 
                  class="btn btn-reactive-gray"
                  @click="decreaseQuantity"
                  :disabled="quantity <= 0"
                >
                  <i class="bi bi-dash-lg"></i>
                </button>
                <input 
                  type="number" 
                  class="form-control text-center bg-reactive-primary text-reactive-primary border-0 fw-bold fs-5"
                  style="width: 100px;"
                  v-model.number="quantity"
                  :max="selectedZone.available"
                  min="0"
                />
                <button 
                  class="btn btn-reactive-gray"
                  @click="increaseQuantity"
                  :disabled="quantity >= selectedZone.available"
                >
                  <i class="bi bi-plus-lg"></i>
                </button>
              </div>
            </div>

            <!-- Price Summary Table -->
            <table class="table table-borderless mb-4">
              <tbody>
                <tr>
                  <td class="text-reactive-secondary">Giá mỗi vé:</td>
                  <td class="text-end text-reactive-primary fw-bold">
                    {{ formatPrice(selectedZone.price) }}
                  </td>
                </tr>
                <tr>
                  <td class="text-reactive-secondary">Số lượng:</td>
                  <td class="text-end text-reactive-primary fw-bold">
                    {{ quantity }}
                  </td>
                </tr>
                <tr class="border-top border-secondary">
                  <td class="text-reactive-primary fs-5 fw-bold">Tổng cộng:</td>
                  <td class="text-end text-primary fs-4 fw-bold">
                    {{ formatPrice(totalCost) }}
                  </td>
                </tr>
              </tbody>
            </table>

            <!-- Add to Cart Button -->
            <button 
              class="btn btn-primary w-100 py-3"
              @click="addToCart"
              :disabled="quantity === 0"
            >
              <i class="bi bi-cart-plus me-2"></i>
              Thêm vào giỏ hàng
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface ZoneSelection {
  id: string
  name: string
  price: number
  available: number
  soldOut: boolean
}

interface Emits {
  (e: 'back'): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number): void
}

const emit = defineEmits<Emits>()

const selectedZone = ref<ZoneSelection | null>(null)
const quantity = ref(0)

const totalCost = computed(() => {
  if (!selectedZone.value) return 0
  return selectedZone.value.price * quantity.value
})

const openZoneSelection = (
  id: string, 
  name: string, 
  price: number, 
  available: number, 
  soldOut: boolean = false
) => {
  if (soldOut) {
    selectedZone.value = { id, name, price, available, soldOut }
    quantity.value = 0
  } else {
    selectedZone.value = { id, name, price, available, soldOut }
    quantity.value = 1
  }
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
  min-height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.seat-zone:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}
</style>