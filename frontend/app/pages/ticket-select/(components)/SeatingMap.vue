<template>
  <div class="seating-map-wrapper h-100 d-flex flex-column bg-reactive-primary">
    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center p-4 bg-reactive-secondary">
      <button class="btn btn-link text-primary text-decoration-none p-0" @click="$emit('back')">
        <i class="bi bi-arrow-left me-1"/> {{ $t('event_payment.header.back') }}
      </button>
      <div class="text-center">
        <h5 class="text-primary mb-0">{{ $t('event_payment.header.title') }}</h5>
        <small class="text-reactive-secondary">{{ $t('event_payment.header.subtitle') }}</small>
      </div>
      <div style="width: 80px;"/>
    </div>

    <!-- Map -->
    <div class="flex-grow-1 overflow-auto p-4 d-flex align-items-center justify-content-center">
      <div style="max-width: 800px; width: 100%;">
        <table class="table table-bordered text-center mb-0">
          <tbody>
            <template v-for="(row, rowIndex) in layout.rows" :key="rowIndex">

              <!-- Stage -->
              <tr v-if="row.type === 'stage'">
                <td :colspan="totalCols" class="py-3 bg-warning text-dark fw-bold">
                  {{ $t('event_payment.stage') }}
                </td>
              </tr>

              <!-- Zone rows -->
              <tr v-else-if="row.type === 'zones'">
                <td
                  v-for="(col, colIndex) in row.cols"
                  :key="colIndex"
                  :colspan="col.colspan"
                  class="p-0"
                >
                  <!-- Zone -->
                  <template v-if="isZoneCol(col)">
                    <div
                      v-if="getTicket(col.zoneId)"
                      class="seat-zone p-4"
                      :class="{ 'opacity-50': getTicket(col.zoneId)!.soldOut }"
                      :style="{ backgroundColor: getTicket(col.zoneId)!.color }"
                      @click="handleZoneClick(col.zoneId)"
                    >
                      <strong class="text-white">{{ getTicket(col.zoneId)!.name }}</strong>
                      <br/>
                      <small v-if="getTicket(col.zoneId)!.soldOut" class="badge bg-danger mt-1">
                        {{ $t('event_payment.ticket_info.sold_out') }}
                      </small>
                      <small v-else class="text-white opacity-75">
                        {{ getMaxLimitText(col.zoneId) }}
                      </small>
                    </div>
                  </template>

                  <!-- FOH -->
                  <template v-else-if="col.type === 'foh'">
                    <div class="p-4 bg-reactive-gray h-100 d-flex align-items-center justify-content-center">
                      <strong class="text-reactive-secondary">{{ $t('event_payment.foh') }}</strong>
                    </div>
                  </template>

                  <!-- Empty -->
                  <template v-else-if="col.type === 'empty'">
                    <div class="bg-reactive-secondary h-100"/>
                  </template>
                </td>
              </tr>

            </template>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Selection Panel -->
    <div
      v-if="selectedZone"
      class="position-fixed bottom-0 start-0 end-0 p-4 bg-reactive-secondary border-top border-primary"
      style="z-index: 1000;"
    >
      <div class="container" style="max-width: 600px;">
        <div class="d-flex justify-content-between align-items-start mb-3">
          <div>
            <h5 class="text-reactive-primary mb-1">{{ selectedZone.name }}</h5>
            <small class="text-reactive-secondary">
              <i class="bi bi-people me-1"/>
              {{ selectedZone.available }} {{ $t('event_payment.selection.available') }}
            </small>
            <br/>
            <small :class="isUnlimited ? 'text-success' : 'text-warning'">
              <i class="bi bi-info-circle me-1"/>
              {{ maxLimitMessage }}
            </small>
          </div>
          <button class="btn-close" @click="closeZoneSelection" aria-label="Close"/>
        </div>

        <div v-if="selectedZone.soldOut" class="alert alert-danger mb-0">
          <i class="bi bi-exclamation-triangle me-2"/>
          {{ $t('event_payment.selection.sold_out_message') }}
        </div>

        <div v-else>
          <div v-if="!isUnlimited && quantity >= maxAllowedQuantity" class="alert alert-warning mb-3">
            <i class="bi bi-exclamation-triangle me-2"/>
            {{ $t('event_payment.validation.max_reached', { max: selectedZone.maxPerAccount }) }}
          </div>

          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label text-reactive-primary fw-semibold small">
                {{ $t('event_payment.selection.quantity') }}
              </label>
              <div class="d-flex gap-2">
                <button class="btn btn-reactive-gray" @click="decreaseQuantity">
                  <i class="bi bi-dash"/>
                </button>
                <input
                  v-model.number="quantity"
                  type="number"
                  :max="maxAllowedQuantity"
                  min="0"
                  class="form-control text-center bg-reactive-primary text-reactive-primary border-0 fw-bold"
                />
                <button class="btn btn-reactive-gray" :disabled="quantity >= maxAllowedQuantity" @click="increaseQuantity">
                  <i class="bi bi-plus"/>
                </button>
              </div>
            </div>

            <div class="col-md-6">
              <label class="form-label text-reactive-primary fw-semibold small">
                {{ $t('event_payment.selection.total') }}
              </label>
              <div class="text-primary fs-4 fw-bold">{{ formatPrice(totalCost) }}</div>
            </div>

            <div class="col-12">
              <button
                class="btn btn-primary w-100 py-2 fw-semibold"
                :disabled="quantity === 0"
                @click="addToCart"
              >
                <i class="bi bi-cart-plus me-2"/>
                {{ $t('event_payment.selection.add_to_cart') }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Ticket } from '../types/ticket.type'
import type { SeatingLayout, SeatingLayoutCol } from '../types/seating-layout.type'
import { isZoneCol } from '../types/seating-layout.type'

interface Props {
  tickets: Ticket[]
  seatingLayout: SeatingLayout | null
}

interface Emits {
  (e: 'back'): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()
const { t } = useI18n()

// ── Layout ────────────────────────────────────────────────────────────────
// Use API layout if available, otherwise fall back to one full-width row per zone
const layout = computed<SeatingLayout>(() => {
  if (props.seatingLayout) return props.seatingLayout
  return {
    rows: [
      { type: 'stage' },
      ...props.tickets.map(ticket => ({
        type: 'zones' as const,
        cols: [{ zoneId: ticket.id, colspan: 1 }],
      })),
    ],
  }
})

const totalCols = computed(() => {
  const first = layout.value.rows.find(r => r.type === 'zones') as { cols: SeatingLayoutCol[] } | undefined
  if (!first) return 3
  return first.cols.reduce((sum, col) => sum + col.colspan, 0)
})

// ── Helpers ───────────────────────────────────────────────────────────────
const getTicket = (zoneId: string) => props.tickets.find(t => t.id === zoneId) ?? null

const getMaxLimitText = (zoneId: string) => {
  const ticket = getTicket(zoneId)
  if (!ticket) return ''
  if (ticket.maxPerAccount === null || ticket.maxPerAccount === 0)
    return t('event_payment.validation.unlimited')
  return t('event_payment.validation.max_per_account', { max: ticket.maxPerAccount })
}

// ── Zone selection ────────────────────────────────────────────────────────
const selectedZone = ref<Ticket | null>(null)
const quantity = ref(0)

const totalCost = computed(() => selectedZone.value ? selectedZone.value.price * quantity.value : 0)

const isUnlimited = computed(() =>
  !selectedZone.value ? false
  : selectedZone.value.maxPerAccount === null || selectedZone.value.maxPerAccount === 0
)

const maxAllowedQuantity = computed(() => {
  if (!selectedZone.value) return 0
  if (isUnlimited.value) return selectedZone.value.available
  return Math.min(selectedZone.value.available, selectedZone.value.maxPerAccount as number)
})

const maxLimitMessage = computed(() => {
  if (!selectedZone.value) return ''
  if (isUnlimited.value) return t('event_payment.validation.unlimited')
  return t('event_payment.validation.max_per_account', { max: selectedZone.value.maxPerAccount })
})

const handleZoneClick = (zoneId: string) => {
  const ticket = getTicket(zoneId)
  if (ticket) {
    selectedZone.value = ticket
    quantity.value = ticket.soldOut ? 0 : 1
  }
}

const closeZoneSelection = () => {
  selectedZone.value = null
  quantity.value = 0
}

const increaseQuantity = () => {
  if (selectedZone.value && quantity.value < maxAllowedQuantity.value) quantity.value++
}

const decreaseQuantity = () => {
  if (quantity.value > 0) quantity.value--
}

const addToCart = () => {
  if (selectedZone.value && quantity.value > 0 && quantity.value <= maxAllowedQuantity.value) {
    emit('addTicket', selectedZone.value.id, selectedZone.value.name, quantity.value, selectedZone.value.price)
    closeZoneSelection()
  }
}

const formatPrice = (price: number) => new Intl.NumberFormat('vi-VN').format(price) + ' đ'
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
  border-color: var(--bg-reactive-gray) !important;
}
</style>