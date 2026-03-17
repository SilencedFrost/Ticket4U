<template>
  <div class="seat-grid-builder">

    <!-- Row builder -->
    <div class="d-flex align-items-center justify-content-between mb-2">
      <div class="d-flex align-items-center gap-3">
        <label class="form-label small fw-semibold text-reactive-secondary mb-0">Seat Rows</label>
        <div class="text-reactive-secondary small">
          <i class="bi bi-info-circle me-1"/>
          Total: <strong class="text-reactive-primary">{{ totalSeats }}</strong> seats
        </div>
      </div>
      <button type="button" class="btn btn-sm btn-outline-primary" @click="addRow">
        <i class="bi bi-plus-lg me-1"/>Add Row
      </button>
    </div>

    <div v-if="modelValue.rows.length === 0" class="text-center py-3 text-reactive-secondary small border border-dashed rounded-2 mb-3">
      No rows yet. Click "Add Row" to start.
    </div>

    <div v-else class="d-flex flex-column gap-2 mb-3">
      <div
        v-for="(row, i) in modelValue.rows"
        :key="i"
        class="row-item d-flex align-items-center gap-2 p-2 rounded-2"
      >
        <!-- Row prefix -->
        <div style="width:100px;flex-shrink:0;">
          <input
            :value="row.prefix"
            type="text"
            class="form-control form-control-sm bg-reactive-primary border-0 text-reactive-primary"
            placeholder="A / VIP-A"
            @change="updateRow(i, 'prefix', ($event.target as HTMLInputElement).value)"
          />
        </div>

        <!-- Seat count -->
        <div class="d-flex align-items-center gap-1" style="width:120px;flex-shrink:0;">
          <button type="button" class="btn btn-sm btn-outline-secondary p-0 px-2" @click="updateRow(i, 'count', Math.max(1, row.count - 1))">−</button>
          <input
            :value="row.count"
            type="number"
            min="1"
            max="100"
            class="form-control form-control-sm bg-reactive-primary border-0 text-reactive-primary text-center"
            style="width:50px;"
            @change="updateRow(i, 'count', Math.max(1, Math.min(100, +($event.target as HTMLInputElement).value)))"
          />
          <button type="button" class="btn btn-sm btn-outline-secondary p-0 px-2" @click="updateRow(i, 'count', Math.min(100, row.count + 1))">+</button>
        </div>

        <!-- Price override -->
        <div class="d-flex align-items-center gap-1 flex-grow-1">
          <i class="bi bi-tag text-reactive-secondary" style="font-size:0.75rem;"/>
          <input
            :value="row.priceOverride ?? ''"
            type="number"
            min="0"
            class="form-control form-control-sm bg-reactive-primary border-0 text-reactive-primary"
            :placeholder="`Default (${formatPrice(zonePrice)})`"
            @change="updateRow(i, 'priceOverride', ($event.target as HTMLInputElement).value === '' ? null : +($event.target as HTMLInputElement).value)"
          />
          <span class="text-reactive-secondary small">₫</span>
        </div>

        <!-- Move up/down -->
        <div class="btn-group btn-group-sm">
          <button type="button" class="btn btn-outline-secondary p-0 px-1" :disabled="i === 0" @click="moveRow(i, -1)"><i class="bi bi-chevron-up"/></button>
          <button type="button" class="btn btn-outline-secondary p-0 px-1" :disabled="i === modelValue.rows.length - 1" @click="moveRow(i, 1)"><i class="bi bi-chevron-down"/></button>
        </div>

        <!-- Delete row -->
        <button type="button" class="btn btn-sm btn-outline-danger p-0 px-2" @click="removeRow(i)">
          <i class="bi bi-x-lg"/>
        </button>
      </div>
    </div>

    <!-- Live preview -->
    <div v-if="modelValue.rows.length > 0" class="seat-preview rounded-2 p-3">
      <div class="text-reactive-secondary small mb-2 fw-semibold">Preview</div>
      <div class="preview-scroll">
        <div
          v-for="(row, ri) in modelValue.rows"
          :key="ri"
          class="d-flex align-items-center gap-1 mb-1"
        >
          <span class="row-label text-reactive-secondary" style="font-size:0.65rem;min-width:44px;text-align:right;padding-right:4px;">{{ row.prefix }}</span>
          <div
            v-for="col in row.count"
            :key="col"
            class="seat-dot rounded-circle"
            :style="{
              width: previewSize + 'px',
              height: previewSize + 'px',
              background: row.priceOverride !== null ? '#f59e0b' : SEAT_COLOR,
              opacity: 0.85,
            }"
            :title="`${row.prefix}-${col}${row.priceOverride !== null ? ' · ' + formatPrice(row.priceOverride) : ''}`"
          />
        </div>
      </div>
      <div class="d-flex gap-3 mt-2" style="font-size:0.7rem;">
        <span class="d-flex align-items-center gap-1">
          <span class="seat-dot rounded-circle d-inline-block" :style="{ width:'10px', height:'10px', background: SEAT_COLOR }"/>
          Default price
        </span>
        <span class="d-flex align-items-center gap-1">
          <span class="seat-dot rounded-circle d-inline-block" style="width:10px;height:10px;background:#f59e0b;"/>
          Row price override
        </span>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

export interface SeatRow {
  prefix: string
  count: number
  priceOverride: number | null
}

export interface SeatGridConfig {
  seatShape: 'circle' | 'square'
  seatSize: number
  rows: SeatRow[]
}

const props = defineProps<{
  modelValue: SeatGridConfig
  zonePrice: number
}>()

const emit = defineEmits<{ 'update:modelValue': [v: SeatGridConfig] }>()

// Default seat color — color removed from zone entity
const SEAT_COLOR = '#6366f1'

const totalSeats  = computed(() => props.modelValue.rows.reduce((s, r) => s + (r.count || 0), 0))
const previewSize = computed(() => Math.max(8, Math.min(props.modelValue.seatSize * 0.6, 18)))

const emitRows = (rows: SeatRow[]) => {
  emit('update:modelValue', { ...props.modelValue, rows })
}

const update = (key: keyof SeatGridConfig, value: any) => {
  emit('update:modelValue', { ...props.modelValue, [key]: value })
}

const updateRow = (i: number, key: keyof SeatRow, value: any) => {
  const rows = props.modelValue.rows.map((r, idx) =>
    idx === i ? { ...r, [key]: value } : r
  )
  emitRows(rows)
}

const addRow = () => {
  const nextLetter = String.fromCharCode(65 + props.modelValue.rows.length % 26)
  emitRows([...props.modelValue.rows, { prefix: nextLetter, count: 10, priceOverride: null }])
}

const removeRow = (i: number) => {
  emitRows(props.modelValue.rows.filter((_, idx) => idx !== i))
}

const moveRow = (i: number, dir: -1 | 1) => {
  const rows = [...props.modelValue.rows]
  const temp = rows[i]
  rows[i] = rows[i + dir]
  rows[i + dir] = temp
  emitRows(rows)
}

const formatPrice = (p: number | null) => {
  if (p === null || p === undefined) return '—'
  if (p === 0) return 'Free'
  return new Intl.NumberFormat('vi-VN').format(p) + ' ₫'
}
</script>

<style scoped>
.row-item {
  background: rgba(var(--bs-secondary-rgb), 0.1);
  border: 1px solid rgba(var(--bs-secondary-rgb), 0.15);
  transition: background 0.15s;
}
.row-item:hover { background: rgba(var(--bs-secondary-rgb), 0.16); }
.seat-preview {
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(var(--bs-secondary-rgb), 0.15);
  max-height: 200px;
  overflow-y: auto;
}
.preview-scroll { overflow-x: auto; }
.seat-dot {
  display: inline-block;
  flex-shrink: 0;
  cursor: default;
  transition: transform 0.1s;
}
.seat-dot:hover { transform: scale(1.3); }
.row-label { font-family: monospace; flex-shrink: 0; }
.border-dashed { border-style: dashed !important; }
</style>