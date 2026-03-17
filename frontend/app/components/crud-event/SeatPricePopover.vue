<template>
  <Teleport to="body">
    <div
      v-if="seat"
      class="seat-popover shadow-lg rounded-3 p-3"
      :style="popoverStyle"
      @click.stop
    >
      <div class="d-flex align-items-center justify-content-between mb-2">
        <div class="fw-semibold text-reactive-primary small">
          <i class="bi bi-chair me-1"/>{{ seat.seatCode }}
        </div>
        <button class="btn-close btn-close-sm" @click="$emit('close')"/>
      </div>

      <div class="text-reactive-secondary mb-2" style="font-size:0.75rem;">
        Zone default: <strong class="text-reactive-primary">{{ formatPrice(zonePrice) }}</strong>
      </div>

      <label class="form-label small text-reactive-secondary mb-1">Price Override</label>
      <div class="input-group input-group-sm mb-2">
        <input
          v-model.number="localPrice"
          type="number"
          min="0"
          class="form-control bg-reactive-primary border-0 text-reactive-primary"
          :placeholder="`Default (${formatPrice(zonePrice)})`"
          @keyup.enter="save"
        />
        <span class="input-group-text bg-reactive-primary border-0 text-reactive-secondary">₫</span>
      </div>

      <div v-if="localPrice !== null" class="mb-2">
        <button class="btn btn-sm btn-link p-0 text-reactive-secondary" style="font-size:0.72rem;" @click="localPrice = null">
          <i class="bi bi-x me-1"/>Clear override (use zone default)
        </button>
      </div>

      <div v-if="saveError" class="alert alert-danger py-1 px-2 small mb-2">{{ saveError }}</div>

      <div class="d-flex gap-2">
        <button class="btn btn-sm btn-outline-secondary flex-grow-1" @click="$emit('close')">Cancel</button>
        <button class="btn btn-sm btn-primary flex-grow-1" :disabled="saving" @click="save">
          <span v-if="saving" class="spinner-border spinner-border-sm me-1"/>Save
        </button>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'

interface SeatInfo {
  id: string
  seatCode: string
  priceOverride: number | null
  zoneId: string
}

const props = defineProps<{
  seat: SeatInfo | null
  pos: { x: number; y: number }
  zonePrice: number
  sessionId: string
  apiUrl: string
}>()

const emit = defineEmits<{
  close: []
  updated: [seatId: string, priceOverride: number | null]
}>()

const POPOVER_W = 240
const POPOVER_H = 200

const localPrice = ref<number | null>(null)
const saving     = ref(false)
const saveError  = ref('')

watch(() => props.seat, (s) => {
  localPrice.value = s?.priceOverride ?? null
  saveError.value  = ''
})

// Clamp popover to viewport so it never overflows off-screen
const popoverStyle = computed(() => {
  const x = Math.min(props.pos.x, (window?.innerWidth  ?? 1200) - POPOVER_W - 12)
  const y = Math.min(props.pos.y, (window?.innerHeight ?? 800)  - POPOVER_H - 12)
  return { top: Math.max(8, y) + 'px', left: Math.max(8, x) + 'px' }
})

const save = async () => {
  if (!props.seat) return
  saving.value = true; saveError.value = ''
  try {
    // explicit null check so price=0 (free ticket) is preserved
    const priceOverride = localPrice.value === null ? null : localPrice.value
    await $fetch(
      `${props.apiUrl}/organizer/sessions/${props.sessionId}/zones/${props.seat.zoneId}/seats/${props.seat.id}/price`,
      { method: 'PATCH', body: { priceOverride }, credentials: 'include' }
    )
    emit('updated', props.seat.id, priceOverride)
    emit('close')
  } catch (e: any) {
    saveError.value = e?.data?.message ?? 'Failed to save — please retry'
  } finally {
    saving.value = false
  }
}

const formatPrice = (p: number | null) => {
  if (p === null || p === undefined) return '—'
  if (p === 0) return 'Free'
  return new Intl.NumberFormat('vi-VN').format(p) + ' ₫'
}
</script>

<style scoped>
.seat-popover {
  position: fixed;
  z-index: 2000;
  background: var(--bs-body-bg, #1a1a2e);
  border: 1px solid rgba(var(--bs-secondary-rgb), 0.25);
  width: 240px;
}
</style>