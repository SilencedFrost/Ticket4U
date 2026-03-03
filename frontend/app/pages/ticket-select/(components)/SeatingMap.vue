<template>
  <div class="seating-map-wrapper h-100 d-flex flex-column">

    <!-- Header -->
    <div class="d-flex justify-content-between align-items-center p-3 p-md-4 bg-reactive-secondary flex-shrink-0">
      <button class="btn btn-link text-primary text-decoration-none p-0" @click="$emit('back')">
        <i class="bi bi-arrow-left me-1"></i> {{ $t('event_payment.header.back') }}
      </button>
      <div class="text-center">
        <h5 class="text-primary mb-0">{{ $t('event_payment.header.title') }}</h5>
        <small class="text-reactive-secondary">{{ $t('event_payment.header.subtitle') }}</small>
      </div>
      <div style="width: 80px;" />
    </div>

    <!-- Floor Tabs (hidden if only 1 floor) -->
    <div v-if="floors.length > 1" class="floor-tabs d-flex bg-reactive-secondary border-bottom border-secondary flex-shrink-0">
      <button
        v-for="floor in floors"
        :key="floor.id"
        class="floor-tab btn flex-grow-1 py-2 rounded-0 border-0"
        :class="activeFloorId === floor.id ? 'btn-primary' : 'btn-transparent text-reactive-secondary'"
        @click="activeFloorId = floor.id"
      >
        {{ floor.floor_name }}
      </button>
    </div>

    <!-- Canvas Area -->
    <div ref="canvasContainer" class="flex-grow-1 position-relative overflow-hidden bg-reactive-primary">

      <!-- Zoom Controls -->
      <div class="position-absolute top-0 end-0 m-3 d-flex flex-column gap-1" style="z-index: 10;">
        <button class="btn btn-sm btn-reactive-gray rounded-circle" @click="zoomIn">
          <i class="bi bi-plus-lg"></i>
        </button>
        <button class="btn btn-sm btn-reactive-gray rounded-circle" @click="zoomOut">
          <i class="bi bi-dash-lg"></i>
        </button>
        <button class="btn btn-sm btn-reactive-gray rounded-circle" @click="resetZoom" title="Reset">
          <i class="bi bi-arrows-fullscreen"></i>
        </button>
      </div>

      <!-- Konva Stage -->
      <v-stage
        v-if="stageSize.width > 0"
        ref="stageRef"
        :config="stageConfig"
        @wheel="handleWheel"
        @touchmove="handleTouchMove"
        @touchend="handleTouchEnd"
        @dragend="onStageDragEnd"
      >
        <v-layer>
          <!-- Stage / Screen -->
          <v-rect v-if="activeFloor" :config="stageRectConfig" />
          <v-text v-if="activeFloor" :config="stageLabelConfig" />

          <!-- Zones -->
          <template v-for="zone in activeFloor?.layout.zones" :key="zone.zone_name">
            <v-line
              :config="getZoneConfig(zone)"
              @click="handleZoneClick(zone)"
              @tap="handleZoneClick(zone)"
            />
            <v-text :config="getZoneLabelConfig(zone)" />

            <!-- Seats (sitting zones only) -->
            <template v-if="zone.zone_type === 'sitting'">
              <v-group
                v-for="seat in zone.seats"
                :key="seat.seat_id"
                :config="getSeatGroupConfig(seat)"
                @click="handleSeatClick(seat, zone)"
                @tap="handleSeatClick(seat, zone)"
              >
                <v-rect :config="getSeatRectConfig(seat)" />
                <v-text :config="getSeatLabelConfig(seat)" />
              </v-group>
            </template>
          </template>
        </v-layer>
      </v-stage>

      <!-- Loading placeholder -->
      <div v-else class="d-flex align-items-center justify-content-center h-100">
        <div class="spinner-border text-primary" />
      </div>
    </div>

    <!-- Legend -->
    <div class="legend d-flex gap-3 px-4 py-2 bg-reactive-secondary flex-shrink-0 flex-wrap">
      <div class="d-flex align-items-center gap-1">
        <div class="legend-dot" style="background:#22c55e"></div>
        <small class="text-reactive-secondary">{{ $t('event_payment.legend.available') }}</small>
      </div>
      <div class="d-flex align-items-center gap-1">
        <div class="legend-dot" style="background:#3b82f6"></div>
        <small class="text-reactive-secondary">{{ $t('event_payment.legend.selected') }}</small>
      </div>
      <div class="d-flex align-items-center gap-1">
        <div class="legend-dot" style="background:#6b7280"></div>
        <small class="text-reactive-secondary">{{ $t('event_payment.legend.unavailable') }}</small>
      </div>
    </div>

    <!-- Selection Panel — Standing Zone -->
    <div
      v-if="selectedStandingZone"
      class="selection-panel position-fixed bottom-0 start-0 end-0 p-4 bg-reactive-secondary border-top border-primary"
      style="z-index: 1000;"
    >
      <div class="container" style="max-width: 600px;">
        <div class="d-flex justify-content-between align-items-start mb-3">
          <div>
            <h5 class="text-reactive-primary mb-1">{{ selectedStandingZone.display_name ?? selectedStandingZone.zone_name }}</h5>
            <small class="text-reactive-secondary">
              <i class="bi bi-people me-1"></i>
              {{ getZoneTicket(selectedStandingZone)?.available ?? 0 }} {{ $t('event_payment.selection.available') }}
            </small>
          </div>
          <button class="btn-close" @click="selectedStandingZone = null" />
        </div>

        <div v-if="getZoneTicket(selectedStandingZone)?.soldOut" class="alert alert-danger mb-0">
          <i class="bi bi-exclamation-triangle me-2"></i>
          {{ $t('event_payment.selection.sold_out_message') }}
        </div>

        <div v-else class="row g-3">
          <div v-if="standingQuantity >= maxStandingAllowed" class="col-12">
            <div class="alert alert-warning mb-0 py-2">
              <i class="bi bi-exclamation-triangle me-2"></i>
              {{ $t('event_payment.validation.max_reached', { max: maxStandingAllowed }) }}
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label text-reactive-primary fw-semibold small">{{ $t('event_payment.selection.quantity') }}</label>
            <div class="d-flex gap-2">
              <button class="btn btn-reactive-gray" @click="standingQuantity = Math.max(0, standingQuantity - 1)">
                <i class="bi bi-dash"></i>
              </button>
              <input
                type="number"
                class="form-control text-center bg-reactive-primary text-reactive-primary border-0 fw-bold"
                v-model.number="standingQuantity"
                :max="maxStandingAllowed"
                min="0"
              />
              <button
                class="btn btn-reactive-gray"
                @click="standingQuantity = Math.min(maxStandingAllowed, standingQuantity + 1)"
                :disabled="standingQuantity >= maxStandingAllowed"
              >
                <i class="bi bi-plus"></i>
              </button>
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label text-reactive-primary fw-semibold small">{{ $t('event_payment.selection.total') }}</label>
            <div class="text-primary fs-4 fw-bold">
              {{ formatPrice((getZoneTicket(selectedStandingZone)?.price ?? 0) * standingQuantity) }}
            </div>
          </div>

          <div class="col-12">
            <button
              class="btn btn-primary w-100 py-2 fw-semibold"
              :disabled="standingQuantity === 0"
              @click="addStandingToCart"
            >
              <i class="bi bi-cart-plus me-2"></i>
              {{ $t('event_payment.selection.add_to_cart') }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Selection Panel — Seated Zone -->
    <div
      v-if="selectedSeats.length > 0 && !selectedStandingZone"
      class="selection-panel position-fixed bottom-0 start-0 end-0 p-4 bg-reactive-secondary border-top border-primary"
      style="z-index: 1000;"
    >
      <div class="container" style="max-width: 600px;">
        <div class="d-flex justify-content-between align-items-start mb-3">
          <div class="flex-grow-1">
            <h5 class="text-reactive-primary mb-1">{{ $t('event_payment.selection.selected_seats') }}</h5>
            <div class="d-flex flex-wrap gap-1 mt-1">
              <span
                v-for="seat in selectedSeats"
                :key="seat.seatId"
                class="badge d-inline-flex align-items-center gap-1"
                :style="{ backgroundColor: seat.zoneColor }"
              >
                {{ seat.seatId }}
                <i class="bi bi-x" style="cursor:pointer;" @click="deselectSeat(seat.seatId)" />
              </span>
            </div>
          </div>
          <button class="btn-close ms-2" @click="selectedSeats = []" />
        </div>

        <div class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-reactive-secondary">{{ $t('event_payment.selection.total') }}:</span>
          <span class="text-primary fw-bold fs-5">{{ formatPrice(selectedSeatsTotalPrice) }}</span>
        </div>

        <button class="btn btn-primary w-100 py-2 fw-semibold" @click="addSeatsToCart">
          <i class="bi bi-cart-plus me-2"></i>
          {{ $t('event_payment.selection.add_to_cart') }} ({{ selectedSeats.length }})
        </button>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import type { Ticket } from '../(types)/ticket.type'
import type { Floor, LayoutZone, LayoutSeat } from '../(types)/seating-layout.type'

interface SelectedSeatLocal {
  seatId: string
  seatName: string
  seatUuid?: string
  zoneUuid?: string
  zoneName: string
  zoneColor: string
  price: number
}

interface Props {
  tickets: Ticket[]
  floors: Floor[]
}

interface Emits {
  (e: 'back'): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number, seats?: SelectedSeatLocal[]): void
}

const props = defineProps<Props>()
const emit  = defineEmits<Emits>()

// ── Floor state ────────────────────────────────────────────
const activeFloorId = ref<string>('')
const activeFloor   = computed(() => props.floors.find(f => f.id === activeFloorId.value))

watch(() => props.floors, (floors) => {
  if (floors.length > 0 && !activeFloorId.value) {
    activeFloorId.value = floors[0].id
  }
}, { immediate: true })

// ── Seat size — read from layout JSON, fallback 18 ─────────
const seatSize = computed(() => activeFloor.value?.layout.seat_size ?? 18)

// ── Canvas sizing ──────────────────────────────────────────
const canvasContainer = ref<HTMLElement | null>(null)
const stageSize       = ref({ width: 0, height: 0 })
const stageRef        = ref<any>(null)
const PADDING         = 40

const updateSize = () => {
  if (canvasContainer.value) {
    stageSize.value = {
      width:  canvasContainer.value.clientWidth,
      height: canvasContainer.value.clientHeight,
    }
  }
}

onMounted(() => {
  updateSize()
  window.addEventListener('resize', updateSize)
})
onUnmounted(() => window.removeEventListener('resize', updateSize))

// ── Coordinate conversion ──────────────────────────────────
const toCanvasX = (x: number): number => {
  const w = stageSize.value.width - PADDING * 2
  return PADDING + (x + 1) / 2 * w
}

const toCanvasY = (y: number): number => {
  const h = stageSize.value.height - PADDING * 2
  return PADDING + (y + 1) / 2 * h
}

// ── Zoom / Pan ─────────────────────────────────────────────
const scale    = ref(1)
const stagePos = ref({ x: 0, y: 0 })

// x/y NOT in stageConfig — Konva manages position internally
// We only update stagePos from dragend + wheel to keep them in sync
const stageConfig = computed(() => ({
  width:     stageSize.value.width,
  height:    stageSize.value.height,
  draggable: true,
  scaleX:    scale.value,
  scaleY:    scale.value,
}))

const onStageDragEnd = (e: any) => {
  stagePos.value = { x: e.target.x(), y: e.target.y() }
}

const ZOOM_STEP = 0.15
const MIN_SCALE = 0.3
const MAX_SCALE = 4

const zoomIn  = () => { scale.value = Math.min(MAX_SCALE, scale.value + ZOOM_STEP) }
const zoomOut = () => { scale.value = Math.max(MIN_SCALE, scale.value - ZOOM_STEP) }

const resetZoom = () => {
  scale.value    = 1
  stagePos.value = { x: 0, y: 0 }
  const stage = stageRef.value?.getStage()
  if (stage) {
    stage.scale({ x: 1, y: 1 })
    stage.position({ x: 0, y: 0 })
    stage.batchDraw()
  }
}

const handleWheel = (e: any) => {
  e.evt.preventDefault()
  const stage = stageRef.value?.getStage()
  if (!stage) return

  const oldScale  = scale.value
  const pointer   = stage.getPointerPosition()
  const direction = e.evt.deltaY > 0 ? -1 : 1
  const newScale  = Math.min(MAX_SCALE, Math.max(MIN_SCALE, oldScale + direction * ZOOM_STEP))

  const mousePointTo = {
    x: (pointer.x - stage.x()) / oldScale,
    y: (pointer.y - stage.y()) / oldScale,
  }
  const newPos = {
    x: pointer.x - mousePointTo.x * newScale,
    y: pointer.y - mousePointTo.y * newScale,
  }

  scale.value    = newScale
  stagePos.value = newPos
  stage.scale({ x: newScale, y: newScale })
  stage.position(newPos)
  stage.batchDraw()
}

// Pinch to zoom
let lastDist = 0
const handleTouchMove = (e: any) => {
  const touches = e.evt.touches
  if (touches.length !== 2) return
  e.evt.preventDefault()
  const dx   = touches[0].clientX - touches[1].clientX
  const dy   = touches[0].clientY - touches[1].clientY
  const dist = Math.sqrt(dx * dx + dy * dy)
  if (lastDist > 0) {
    scale.value = Math.min(MAX_SCALE, Math.max(MIN_SCALE, scale.value * (dist / lastDist)))
  }
  lastDist = dist
}
const handleTouchEnd = () => { lastDist = 0 }

// ── Stage / Screen config ──────────────────────────────────
const stageRectConfig = computed(() => {
  if (!activeFloor.value) return {}
  const s = activeFloor.value.layout.stage
  return {
    x:            toCanvasX(s.x1),
    y:            toCanvasY(s.y1),
    width:        toCanvasX(s.x2) - toCanvasX(s.x1),
    height:       toCanvasY(s.y2) - toCanvasY(s.y1),
    fill:         '#F59E0B',
    cornerRadius: 4,
  }
})

const stageLabelConfig = computed(() => {
  if (!activeFloor.value) return {}
  const s = activeFloor.value.layout.stage
  const x = toCanvasX(s.x1)
  const y = toCanvasY(s.y1)
  const w = toCanvasX(s.x2) - x
  const h = toCanvasY(s.y2) - y
  return {
    x, y, width: w, height: h,
    text:          'STAGE',
    align:         'center',
    verticalAlign: 'middle',
    fontSize:      13,
    fontStyle:     'bold',
    fill:          '#1a1a1a',
  }
})

// ── Zone rendering ─────────────────────────────────────────
const getZoneConfig = (zone: LayoutZone) => {
  const pts = [
    toCanvasX(zone.corner1.x), toCanvasY(zone.corner1.y),
    toCanvasX(zone.corner2.x), toCanvasY(zone.corner2.y),
    toCanvasX(zone.corner3.x), toCanvasY(zone.corner3.y),
    toCanvasX(zone.corner4.x), toCanvasY(zone.corner4.y),
  ]
  const ticket  = getZoneTicket(zone)
  const soldOut = ticket?.soldOut ?? false
  return {
    points:      pts,
    closed:      true,
    fill:        soldOut ? '#374151' : zone.color + '55',
    stroke:      soldOut ? '#4B5563' : zone.color,
    strokeWidth: 2,
    opacity:     soldOut ? 0.5 : 1,
    cursor:      soldOut ? 'not-allowed' : 'pointer',
  }
}

const getZoneLabelConfig = (zone: LayoutZone) => {
  const cx = (zone.corner1.x + zone.corner2.x + zone.corner3.x + zone.corner4.x) / 4
  const cy = (zone.corner1.y + zone.corner2.y + zone.corner3.y + zone.corner4.y) / 4
  const ticket = getZoneTicket(zone)
  const label  = zone.display_name ?? zone.zone_name
  const price  = ticket ? formatPrice(ticket.price) : ''
  return {
    x:             toCanvasX(cx) - 80,
    y:             toCanvasY(cy) - 20,
    width:         160,
    text:          `${label}\n${price}`,
    align:         'center',
    fontSize:      12,
    fontStyle:     'bold',
    fill:          '#ffffff',
    listening:     false,
  }
}

// ── Seat rendering ─────────────────────────────────────────
const isSeatSelected = (seatId: string) =>
  selectedSeats.value.some(s => s.seatId === seatId)

const getSeatGroupConfig = (seat: LayoutSeat) => ({
  x:        toCanvasX(seat.seat_pos.x),
  y:        toCanvasY(seat.seat_pos.y),
  rotation: seat.seat_rotation,
  cursor:   'pointer',
  offsetX:  seatSize.value / 2,
  offsetY:  seatSize.value / 2,
})

const getSeatRectConfig = (seat: LayoutSeat) => {
  const selected    = isSeatSelected(seat.seat_id)
  const unavailable = seat.status === 'BOOKED' || seat.status === 'HOLD'
  return {
    width:        seatSize.value,
    height:       seatSize.value,
    fill:         unavailable ? '#6b7280' : selected ? '#3b82f6' : '#22c55e',
    cornerRadius: 3,
    stroke:       selected ? '#1d4ed8' : 'transparent',
    strokeWidth:  1.5,
  }
}

const getSeatLabelConfig = (seat: LayoutSeat) => ({
  x:             0,
  y:             0,
  width:         seatSize.value,
  height:        seatSize.value,
  text:          seat.seat_id,
  fontSize:      Math.max(6, Math.floor(seatSize.value * 0.4)),
  fill:          '#fff',
  align:         'center',
  verticalAlign: 'middle',
  listening:     false,
})

// ── Zone click ─────────────────────────────────────────────
const handleZoneClick = (zone: LayoutZone) => {
  const ticket = getZoneTicket(zone)
  if (!ticket || ticket.soldOut) return
  if (zone.zone_type === 'standing') {
    selectedStandingZone.value = zone
    standingQuantity.value     = 1
  }
}

// ── Standing selection ─────────────────────────────────────
const selectedStandingZone = ref<LayoutZone | null>(null)
const standingQuantity     = ref(1)

const maxStandingAllowed = computed(() => {
  if (!selectedStandingZone.value) return 0
  const ticket = getZoneTicket(selectedStandingZone.value)
  if (!ticket) return 0
  if (!ticket.maxPerAccount || ticket.maxPerAccount === 0) return ticket.available
  return Math.min(ticket.available, ticket.maxPerAccount)
})

const addStandingToCart = () => {
  if (!selectedStandingZone.value || standingQuantity.value <= 0) return
  const ticket = getZoneTicket(selectedStandingZone.value)
  if (!ticket) return
  emit('addTicket',
    selectedStandingZone.value.zone_uuid ?? selectedStandingZone.value.zone_name,
    selectedStandingZone.value.display_name ?? selectedStandingZone.value.zone_name,
    standingQuantity.value,
    ticket.price
  )
  selectedStandingZone.value = null
  standingQuantity.value     = 1
}

// ── Seated selection ───────────────────────────────────────
const selectedSeats = ref<SelectedSeatLocal[]>([])

const selectedSeatsTotalPrice = computed(() =>
  selectedSeats.value.reduce((sum, s) => sum + s.price, 0)
)

const handleSeatClick = (seat: LayoutSeat, zone: LayoutZone) => {
  if (seat.status === 'BOOKED' || seat.status === 'HOLD') return
  const ticket = getZoneTicket(zone)
  if (!ticket || ticket.soldOut) return

  const idx = selectedSeats.value.findIndex(s => s.seatId === seat.seat_id)
  if (idx >= 0) {
    selectedSeats.value.splice(idx, 1)
  } else {
    const zoneSelectedCount = selectedSeats.value.filter(
      s => s.zoneUuid === (zone.zone_uuid ?? zone.zone_name)
    ).length
    const max = !ticket.maxPerAccount || ticket.maxPerAccount === 0
      ? ticket.available
      : ticket.maxPerAccount
    if (zoneSelectedCount >= max) return

    selectedSeats.value.push({
      seatId:    seat.seat_id,
      seatName:  seat.seat_name,
      seatUuid:  seat.seat_uuid,
      zoneUuid:  zone.zone_uuid,
      zoneName:  zone.display_name ?? zone.zone_name,
      zoneColor: zone.color,
      price:     ticket.price,
    })
  }
}

const deselectSeat = (seatId: string) => {
  const idx = selectedSeats.value.findIndex(s => s.seatId === seatId)
  if (idx >= 0) selectedSeats.value.splice(idx, 1)
}

const addSeatsToCart = () => {
  if (selectedSeats.value.length === 0) return

  const byZone = selectedSeats.value.reduce((acc, seat) => {
    const key = seat.zoneUuid ?? seat.zoneName
    if (!acc[key]) acc[key] = []
    acc[key].push(seat)
    return acc
  }, {} as Record<string, SelectedSeatLocal[]>)

  for (const [, seats] of Object.entries(byZone)) {
    const first = seats[0]
    emit('addTicket',
      first.zoneUuid ?? first.zoneName,
      first.zoneName,
      seats.length,
      first.price,
      seats
    )
  }
  selectedSeats.value = []
}

// ── Helpers ────────────────────────────────────────────────
const getZoneTicket = (zone: LayoutZone): Ticket | undefined =>
  props.tickets.find(t =>
    t.id === zone.zone_uuid ||
    t.name === (zone.display_name ?? zone.zone_name)
  )

const formatPrice = (price: number) =>
  new Intl.NumberFormat('vi-VN').format(price) + ' đ'
</script>

<style scoped>
.floor-tab {
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.legend-dot {
  width: 12px;
  height: 12px;
  border-radius: 3px;
  flex-shrink: 0;
}

.selection-panel {
  animation: slideUp 0.25s ease;
}

@keyframes slideUp {
  from { transform: translateY(100%); opacity: 0; }
  to   { transform: translateY(0);    opacity: 1; }
}
</style>