<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import type { Ticket, SelectedSeat} from '../(types)/ticket.type'
import type { Floor, LayoutZone, LayoutSeat } from '../(types)/seating-layout.type'
import type { CartItem } from '../(types)/event-payment.type'

const props = defineProps<{ tickets: Ticket[]; floors: Floor[] }>()
const emit  = defineEmits<{
  (e: 'back'): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number, isStanding: boolean, seats?: SelectedSeat[]): void
}>()

// ── Floor state ────────────────────────────────────────────
const activeFloorId = ref('')
const activeFloor   = computed(() => props.floors.find(f => f.id === activeFloorId.value))
watch(() => props.floors, floors => {
  if (floors.length > 0 && !activeFloorId.value) activeFloorId.value = floors[0].id
}, { immediate: true })
watch(activeFloor, () => nextTick(() => draw()))

// Canvas
const canvasContainer = ref<HTMLElement | null>(null)
const canvasRef       = ref<HTMLCanvasElement | null>(null)
const canvasSize      = ref({ width: 0, height: 0 })
const CANVAS_W        = 900
const CANVAS_H        = 520

const updateSize = () => {
  if (!canvasContainer.value) return
  canvasSize.value = { width: canvasContainer.value.clientWidth, height: canvasContainer.value.clientHeight || CANVAS_H }
  nextTick(() => draw())
}
onMounted(() => { updateSize(); window.addEventListener('resize', updateSize) })
onUnmounted(() => window.removeEventListener('resize', updateSize))

// Pan / Zoom
const scale     = ref(1)
const pan       = ref({ x: 0, y: 0 })
const ZOOM_STEP = 0.15
const MIN_SCALE = 0.3
const MAX_SCALE = 4

const zoomIn    = () => { scale.value = Math.min(MAX_SCALE, scale.value + ZOOM_STEP); draw() }
const zoomOut   = () => { scale.value = Math.max(MIN_SCALE, scale.value - ZOOM_STEP); draw() }
const resetZoom = () => { scale.value = 1; pan.value = { x: 0, y: 0 }; draw() }

let dragging = false; let dragStart = { x: 0, y: 0 }; let panStart = { x: 0, y: 0 }

const onMouseDown = (e: MouseEvent) => {
  if (e.button !== 0) return
  dragging = true; dragStart = { x: e.clientX, y: e.clientY }; panStart = { ...pan.value }
  if (canvasRef.value) canvasRef.value.style.cursor = 'grabbing'
}
const onMouseMove = (e: MouseEvent) => {
  if (!dragging) return
  pan.value = { x: panStart.x + e.clientX - dragStart.x, y: panStart.y + e.clientY - dragStart.y }
  draw()
}
const onMouseUp = () => {
  dragging = false
  if (canvasRef.value) canvasRef.value.style.cursor = 'grab'
}

const handleWheel = (e: WheelEvent) => {
  const rect     = canvasRef.value!.getBoundingClientRect()
  const mx       = e.clientX - rect.left, my = e.clientY - rect.top
  const dir      = e.deltaY > 0 ? -1 : 1
  const oldScale = scale.value
  const newScale = Math.min(MAX_SCALE, Math.max(MIN_SCALE, oldScale + dir * ZOOM_STEP))
  pan.value      = { x: mx - (mx - pan.value.x) * (newScale / oldScale), y: my - (my - pan.value.y) * (newScale / oldScale) }
  scale.value    = newScale; draw()
}

let lastTouchDist = 0
const onTouchStart = (e: TouchEvent) => {
  if (e.touches.length === 1) { dragging = true; dragStart = { x: e.touches[0].clientX, y: e.touches[0].clientY }; panStart = { ...pan.value } }
}
const onTouchMove = (e: TouchEvent) => {
  if (e.touches.length === 2) {
    const dx = e.touches[0].clientX - e.touches[1].clientX, dy = e.touches[0].clientY - e.touches[1].clientY
    const dist = Math.sqrt(dx * dx + dy * dy)
    if (lastTouchDist > 0) { scale.value = Math.min(MAX_SCALE, Math.max(MIN_SCALE, scale.value * dist / lastTouchDist)); draw() }
    lastTouchDist = dist
  } else if (dragging && e.touches.length === 1) {
    pan.value = { x: panStart.x + e.touches[0].clientX - dragStart.x, y: panStart.y + e.touches[0].clientY - dragStart.y }; draw()
  }
}
const onTouchEnd = () => { dragging = false; lastTouchDist = 0 }

// Coordinate helpers
const toCanvas = (nx: number, ny: number) => ({
  x: pan.value.x + ((nx + 1) / 2 * CANVAS_W) * scale.value,
  y: pan.value.y + ((ny + 1) / 2 * CANVAS_H) * scale.value,
})
const toNorm = (cx: number, cy: number) => ({
  x: (cx - pan.value.x) / scale.value / CANVAS_W * 2 - 1,
  y: (cy - pan.value.y) / scale.value / CANVAS_H * 2 - 1,
})

// Seat grid positions
const getSeatGridPositions = (zone: LayoutZone, seatSize: number): Map<string, { x: number; y: number }> => {
  const seats = zone.seats ?? []
  const map   = new Map<string, { x: number; y: number }>()
  if (!seats.length) return map

  const sorted = [...seats].sort((a, b) => {
    const ra = a.seat_id.replace(/\d/g, ''), rb = b.seat_id.replace(/\d/g, '')
    if (ra !== rb) return ra.localeCompare(rb)
    return parseInt(a.seat_id.replace(/\D/g, '') || '0') - parseInt(b.seat_id.replace(/\D/g, '') || '0')
  })

  const minX = Math.min(zone.corner1.x, zone.corner4.x)
  const maxX = Math.max(zone.corner2.x, zone.corner3.x)
  const minY = Math.min(zone.corner1.y, zone.corner2.y)
  const maxY = Math.max(zone.corner3.y, zone.corner4.y)

  const rows = [...new Set(sorted.map(s => s.seat_id.replace(/\d/g, '').toUpperCase()))].sort()
  const cols = Math.max(...rows.map(r => sorted.filter(s => s.seat_id.replace(/\d/g, '').toUpperCase() === r).length))

  // Calculate cell size based on zone dimensions and seat count
  const zoneW = maxX - minX
  const zoneH = maxY - minY
  const cellW = zoneW / (cols + 1)   // +1 for padding
  const cellH = zoneH / (rows.length + 1)

  for (const seat of sorted) {
    const row = seat.seat_id.replace(/\d/g, '').toUpperCase()
    const col = parseInt(seat.seat_id.replace(/\D/g, '') || '1') - 1
    const ri  = rows.indexOf(row)
    map.set(seat.seat_id, {
      x: minX + cellW * (col + 0.5) + cellW / 2,
      y: minY + cellH * (ri + 0.5) + cellH / 2,
    })
  }
  return map
}

// Draw
const draw = () => {
  const canvas = canvasRef.value; if (!canvas || canvasSize.value.width === 0) return
  const ctx    = canvas.getContext('2d'); if (!ctx) return
  const floor  = activeFloor.value; if (!floor) { ctx.clearRect(0, 0, canvas.width, canvas.height); return }

  ctx.clearRect(0, 0, canvas.width, canvas.height)

  // Stage bar
  const st = floor.layout.stage
  if (st) {
    const sp1 = toCanvas(st.x1, st.y1), sp2 = toCanvas(st.x2, st.y2)
    ctx.fillStyle = '#f59e0b'
    ctx.beginPath(); ctx.roundRect(sp1.x, sp1.y, sp2.x - sp1.x, sp2.y - sp1.y, 4); ctx.fill()
    ctx.fillStyle = '#1a1a1a'; ctx.font = 'bold 500 12px sans-serif'
    ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
    ctx.fillText('Stage / Screen', (sp1.x + sp2.x) / 2, (sp1.y + sp2.y) / 2)
  }

  // Zones
  for (const zone of floor.layout.zones) {
    const ticket  = getZoneTicket(zone)
    const soldOut = ticket?.soldOut ?? false
    const c1 = toCanvas(zone.corner1.x, zone.corner1.y)
    const c2 = toCanvas(zone.corner2.x, zone.corner2.y)
    const c3 = toCanvas(zone.corner3.x, zone.corner3.y)
    const c4 = toCanvas(zone.corner4.x, zone.corner4.y)

    // Zone polygon
    ctx.beginPath(); ctx.moveTo(c1.x, c1.y); ctx.lineTo(c2.x, c2.y)
    ctx.lineTo(c3.x, c3.y); ctx.lineTo(c4.x, c4.y); ctx.closePath()
    ctx.fillStyle   = soldOut ? '#37415133' : zone.color + '44'
    ctx.strokeStyle = soldOut ? '#4B5563'   : zone.color
    ctx.lineWidth   = 2; ctx.globalAlpha = soldOut ? 0.5 : 1
    ctx.fill(); ctx.stroke(); ctx.globalAlpha = 1

    // Zone label + price
    const cx  = (zone.corner1.x + zone.corner2.x + zone.corner3.x + zone.corner4.x) / 4
    const cy  = (zone.corner1.y + zone.corner2.y + zone.corner3.y + zone.corner4.y) / 4
    const lp  = toCanvas(cx, cy)
    const label = zone.display_name ?? zone.zone_name
    const price = ticket ? formatPrice(ticket.price) : ''

    ctx.fillStyle = '#fff'; ctx.font = 'bold 12px sans-serif'
    ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
    ctx.fillText(label, lp.x, lp.y - (price ? 8 : 0))
    if (price) { ctx.font = '11px sans-serif'; ctx.fillStyle = '#d1d5db'; ctx.fillText(price, lp.x, lp.y + 8) }

    // Standing badge
    if (zone.zone_type === 'standing' && !soldOut) {
      ctx.font = '10px sans-serif'; ctx.fillStyle = zone.color + 'cc'
      ctx.fillText('[ Standing ]', lp.x, lp.y + 22)
    }

    // Seats (sitting zones)
    if (zone.zone_type === 'sitting' && zone.seats.length > 0) {
      const gridPos = getSeatGridPositions(zone, floor.layout.seat_size ?? 14)
      const zoneW   = (Math.max(zone.corner2.x, zone.corner3.x) - Math.min(zone.corner1.x, zone.corner4.x))
      const zoneH   = (Math.max(zone.corner3.y, zone.corner4.y) - Math.min(zone.corner1.y, zone.corner2.y))
      const rows    = [...new Set(zone.seats.map(s => s.seat_id.replace(/\d/g, '').toUpperCase()))].sort()
      const cols    = Math.max(...rows.map(r => zone.seats.filter(s => s.seat_id.replace(/\d/g, '').toUpperCase() === r).length))
      const cellW   = (zoneW / (cols + 1)) * CANVAS_W * scale.value
      const cellH   = (zoneH / (rows.length + 1)) * CANVAS_H * scale.value
      const maxR    = Math.min(cellW, cellH) / 2 * 0.7
      const r       = Math.min(Math.max(4, (floor.layout.seat_size ?? 14) / 2 * (canvasSize.value.width / CANVAS_W) * scale.value), maxR)

      for (const seat of zone.seats) {
        const np = gridPos.get(seat.seat_id); if (!np) continue
        const cp = toCanvas(np.x, np.y)
        const unavailable = seat.status === 'BOOKED' || seat.status === 'HOLD'
        const selected    = isSeatSelected(seat.seat_id)

        ctx.beginPath(); ctx.arc(cp.x, cp.y, r, 0, Math.PI * 2)
        ctx.fillStyle = unavailable ? '#6b7280' : selected ? '#3b82f6' : '#22c55e'
        ctx.fill()
        if (selected) { ctx.strokeStyle = '#1d4ed8'; ctx.lineWidth = 2; ctx.stroke() }

        if (r >= 7) {
          ctx.fillStyle = '#fff'
          ctx.font = `${Math.max(6, r * 0.7)}px sans-serif`
          ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
          ctx.fillText(seat.seat_id, cp.x, cp.y)
        }
      }
    }
  }
}

// Click handling
const handleCanvasClick = (e: MouseEvent) => {
  if (dragging) return
  const rect = canvasRef.value!.getBoundingClientRect()
  const cx = e.clientX - rect.left, cy = e.clientY - rect.top
  const { x: nx, y: ny } = toNorm(cx, cy)
  const floor = activeFloor.value; if (!floor) return

  // Hit test seats first
  for (const zone of floor.layout.zones) {
    if (zone.zone_type !== 'sitting') continue
    const gridPos = getSeatGridPositions(zone)
    const seatPx = (floor.layout.seat_size ?? 14)
    const scaleX  = canvasSize.value.width / CANVAS_W
    const r = Math.max(4, seatPx / 2 * scaleX * scale.value) + 4
    for (const seat of zone.seats) {
      const np = gridPos.get(seat.seat_id); if (!np) continue
      const cp = toCanvas(np.x, np.y)
      if (Math.hypot(cx - cp.x, cy - cp.y) <= r) { handleSeatClick(seat, zone); return }
    }
  }

  // Hit test zones (standing or seated with no seats loaded)
  for (const zone of floor.layout.zones) {
    if (isPointInZone(nx, ny, zone)) {
      handleZoneClick(zone)
      return
    }
  }
}

const isPointInZone = (x: number, y: number, zone: LayoutZone): boolean => {
  const pts = [zone.corner1, zone.corner2, zone.corner3, zone.corner4]
  let inside = false
  for (let i = 0, j = pts.length - 1; i < pts.length; j = i++) {
    const xi = pts[i].x, yi = pts[i].y, xj = pts[j].x, yj = pts[j].y
    if (((yi > y) !== (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi)) inside = !inside
  }
  return inside
}

// Zone click
const handleZoneClick = (zone: LayoutZone) => {
  const ticket = getZoneTicket(zone)
  if (!ticket || ticket.soldOut) return
  if (zone.zone_type === 'standing') {
    selectedStandingZone.value = zone
    standingQuantity.value = 1
  }
  // sitting zones are handled by seat clicks — zone click just highlights it
}

// Standing selection
const selectedStandingZone = ref<LayoutZone | null>(null)
const standingQuantity     = ref(1)
const maxStandingAllowed   = computed(() => {
  if (!selectedStandingZone.value) return 0
  const t = getZoneTicket(selectedStandingZone.value); if (!t) return 0
  return !t.maxPerAccount ? t.capacity : Math.min(t.capacity, t.maxPerAccount)
})

const addStandingToCart = () => {
  if (!selectedStandingZone.value || standingQuantity.value <= 0) return
  const t = getZoneTicket(selectedStandingZone.value); if (!t) return
  // Use DB zone name from tickets prop — matches Zone.name in database
  const dbTicket = props.tickets.find(tk => tk.id === selectedStandingZone.value!.zone_uuid)
  const zoneName = dbTicket?.name ?? selectedStandingZone.value.display_name ?? selectedStandingZone.value.zone_name
  emit('addTicket',
      selectedStandingZone.value.zone_uuid ?? selectedStandingZone.value.zone_name,
      zoneName,
      standingQuantity.value, t.price, true
  )
  selectedStandingZone.value = null; standingQuantity.value = 1
}

// Seated selection
const selectedSeats           = ref<SelectedSeat[]>([])
const cartSeats               = ref<Set<string>>(new Set())
const selectedSeatsTotalPrice = computed(() => selectedSeats.value.reduce((s, seat) => s + seat.price, 0))
const isSeatSelected = (seatId: string) =>
    selectedSeats.value.some(s => s.seatId === seatId) || cartSeats.value.has(seatId)

const handleSeatClick = (seat: LayoutSeat, zone: LayoutZone) => {
  selectedStandingZone.value = null
  if (seat.status === 'BOOKED' || seat.status === 'HOLD') return
  const t = getZoneTicket(zone); if (!t || t.soldOut) return
  const idx = selectedSeats.value.findIndex(s => s.seatId === seat.seat_id)
  if (idx >= 0) { selectedSeats.value.splice(idx, 1) }
  else {
    const max = !t.maxPerAccount ? t.capacity : t.maxPerAccount
    if (selectedSeats.value.filter(s => s.zoneUuid === zone.zone_uuid).length >= max) return
    const price = seat.priceOverride != null ? seat.priceOverride : t.price
    selectedSeats.value.push({
      seatId:    seat.seat_id,
      seatName:  seat.seat_name,
      seatUuid:  seat.seat_uuid ?? '',  // <- handle undefined
      zoneUuid:  zone.zone_uuid,
      zoneName:  zone.display_name ?? zone.zone_name,
      zoneColor: zone.color,
      price
    })
  }
  draw()
}

const deselectSeat = (seatId: string) => {
  const i = selectedSeats.value.findIndex(s => s.seatId === seatId)
  if (i >= 0) { selectedSeats.value.splice(i, 1); draw() }
}

const addSeatsToCart = () => {
  if (!selectedSeats.value.length) return
  const byZone = selectedSeats.value.reduce((acc, s) => {
    const k = s.zoneUuid ?? s.zoneName
    if (!acc[k]) acc[k] = []
    acc[k].push(s); return acc
  }, {} as Record<string, SelectedSeat[]>)
  for (const seats of Object.values(byZone)) {
    const f = seats[0]
    // Use DB zone name from tickets prop — matches Zone.name in database
    const dbTicket = props.tickets.find(t => t.id === f.zoneUuid)
    const zoneName = dbTicket?.name ?? f.zoneName
    emit('addTicket', f.zoneUuid ?? f.zoneName, zoneName, seats.length, f.price, false, seats)
  }
  selectedSeats.value = []; draw()
}
const syncCartSeats = (items: CartItem[]) => {               // ← add here
  cartSeats.value = new Set(
      items.flatMap(item => item.seats?.map(s => s.seatId) ?? [])
  )
  draw()
}
defineExpose({ syncCartSeats })

// Helpers
const getZoneTicket = (zone: LayoutZone): Ticket | undefined =>
    props.tickets.find(t => t.id === zone.zone_uuid || t.name === (zone.display_name ?? zone.zone_name))

const formatPrice = (price: number) => new Intl.NumberFormat('vi-VN').format(price) + ' đ'

watch([() => props.floors, () => props.tickets, selectedSeats], () => nextTick(() => draw()), { deep: true })
</script>
<template>
  <div class="seating-map-wrapper h-100 d-flex flex-column">
    <!-- Toolbar — matches editor style -->
    <div class="p-3 border-bottom border-secondary bg-reactive-secondary flex-shrink-0 d-flex align-items-center justify-content-between flex-wrap gap-2">
      <div class="d-flex align-items-center gap-2">
        <button class="btn btn-sm btn-outline-secondary" @click="$emit('back')">
          <i class="bi bi-arrow-left me-1"/>{{ $t('select_ticket.header.back') }}
        </button>

        <!-- Floor dropdown -->
        <div v-if="floors.length > 1" class="dropdown">
          <button
              class="btn btn-sm btn-outline-secondary dropdown-toggle d-flex align-items-center gap-2"
              type="button"
              data-bs-toggle="dropdown"
          >
            <i class="bi bi-layers me-1"/>
            {{ activeFloor?.floor_name ?? $t('select_ticket.floor.select') }}
          </button>
          <ul class="dropdown-menu">
            <li v-for="floor in floors" :key="floor.id">
              <button
                  class="dropdown-item d-flex align-items-center gap-2"
                  :class="{ active: activeFloorId === floor.id }"
                  @click="activeFloorId = floor.id"
              >
                <i class="bi bi-check2 me-1" :style="{ opacity: activeFloorId === floor.id ? 1 : 0 }"/>
                {{ floor.floor_name }}
              </button>
            </li>
          </ul>
        </div>

        <!-- Zoom controls -->
        <div class="btn-group btn-group-sm">
          <button class="btn btn-outline-secondary" title="Zoom in" @click="zoomIn"><i class="bi bi-plus-lg"/></button>
          <button class="btn btn-outline-secondary" title="Zoom out" @click="zoomOut"><i class="bi bi-dash-lg"/></button>
          <button class="btn btn-outline-secondary" title="Reset zoom" @click="resetZoom"><i class="bi bi-arrows-fullscreen"/></button>
        </div>
      </div>

      <div class="text-center">
        <h6 class="text-primary mb-0">{{ $t('select_ticket.header.title') }}</h6>
        <small class="text-reactive-secondary">{{ $t('select_ticket.header.subtitle') }}</small>
      </div>

      <!-- Legend -->
      <div class="d-flex gap-3 align-items-center">
        <div class="d-flex align-items-center gap-1">
          <div class="legend-dot" style="background:#22c55e"/>
          <small class="text-reactive-secondary">{{ $t('select_ticket.legend.available') }}</small>
        </div>
        <div class="d-flex align-items-center gap-1">
          <div class="legend-dot" style="background:#3b82f6"/>
          <small class="text-reactive-secondary">{{ $t('select_ticket.legend.selected') }}</small>
        </div>
        <div class="d-flex align-items-center gap-1">
          <div class="legend-dot" style="background:#6b7280"/>
          <small class="text-reactive-secondary">{{ $t('select_ticket.legend.unavailable') }}</small>
        </div>
      </div>
    </div>

    <!-- Canvas Area — same grid background as editor -->
    <div
        ref="canvasContainer"
        class="flex-grow-1 position-relative overflow-hidden"
        style="background: repeating-linear-gradient(0deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px), repeating-linear-gradient(90deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px);"
    >
      <canvas
          v-if="canvasSize.width > 0"
          ref="canvasRef"
          :width="canvasSize.width"
          :height="canvasSize.height"
          style="position:absolute;top:0;left:0;cursor:grab;"
          @wheel.prevent="handleWheel"
          @mousedown="onMouseDown"
          @mousemove="onMouseMove"
          @mouseup="onMouseUp"
          @mouseleave="onMouseUp"
          @click="handleCanvasClick"
          @touchstart.prevent="onTouchStart"
          @touchmove.prevent="onTouchMove"
          @touchend="onTouchEnd"
      />
      <div v-else class="d-flex align-items-center justify-content-center h-100">
        <div class="spinner-border text-primary"/>
      </div>

      <!-- No layout message -->
      <div v-if="canvasSize.width > 0 && floors.length === 0" class="position-absolute top-50 start-50 translate-middle text-center text-reactive-secondary">
        <i class="bi bi-map fs-1 d-block mb-2"/>
        <p>{{ $t('select_ticket.no_layout') }}</p>
      </div>
    </div>

    <!-- Standing Zone Popup Panel -->
    <div
        v-if="selectedStandingZone"
        class="selection-panel position-fixed bottom-0 start-0 end-0 p-4 bg-reactive-secondary border-top border-primary"
        style="z-index:1000;"
    >
      <div class="container" style="max-width:600px;">
        <div class="d-flex justify-content-between align-items-start mb-3">
          <div>
            <h5 class="text-reactive-primary mb-1">
              {{ getZoneTicket(selectedStandingZone!)?.name }}
              <small class="text-reactive-secondary fw-normal ms-1">({{ selectedStandingZone?.display_name ?? selectedStandingZone?.zone_name }})</small>
            </h5>
            <small class="text-reactive-secondary">
              <i class="bi bi-people me-1"/>{{ getZoneTicket(selectedStandingZone)?.capacity ?? 0 }}
              {{ $t('select_ticket.selection.available') }}
            </small>
          </div>
          <button class="btn-close" @click="selectedStandingZone = null"/>
        </div>

        <div v-if="getZoneTicket(selectedStandingZone)?.soldOut" class="alert alert-danger mb-0">
          <i class="bi bi-exclamation-triangle me-2"/>{{ $t('select_ticket.selection.sold_out_message') }}
        </div>

        <div v-else class="row g-3">
          <div v-if="standingQuantity >= maxStandingAllowed" class="col-12">
            <div class="alert alert-warning mb-0 py-2">
              <i class="bi bi-exclamation-triangle me-2"/>{{ $t('select_ticket.validation.max_reached', { max: maxStandingAllowed }) }}
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label text-reactive-primary fw-semibold small">{{ $t('select_ticket.selection.quantity') }}</label>
            <div class="d-flex gap-2">
              <button class="btn btn-outline-secondary" @click="standingQuantity = Math.max(0, standingQuantity - 1)"><i class="bi bi-dash"/></button>
              <input
                  v-model.number="standingQuantity"
                  type="number"
                  class="form-control text-center bg-reactive-primary text-reactive-primary border-0 fw-bold"
                  :max="maxStandingAllowed"
                  min="1"
                  @input="(e) => { const v = parseInt((e.target as HTMLInputElement).value); standingQuantity = isNaN(v) ? 1 : v }"
              />
              <button class="btn btn-outline-secondary" :disabled="standingQuantity >= maxStandingAllowed" @click="standingQuantity = Math.min(maxStandingAllowed, standingQuantity + 1)"><i class="bi bi-plus"/></button>
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label text-reactive-primary fw-semibold small">{{ $t('select_ticket.selection.total') }}</label>
            <div class="text-primary fs-4 fw-bold">{{ formatPrice((getZoneTicket(selectedStandingZone)?.price ?? 0) * standingQuantity) }}</div>
          </div>
          <div class="col-12">
            <button class="btn btn-primary w-100 py-2 fw-semibold" :disabled="standingQuantity === 0" @click="addStandingToCart">
              <i class="bi bi-cart-plus me-2"/>{{ $t('select_ticket.selection.add_to_cart') }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Seated Selection Popup Panel -->
    <div
        v-if="selectedSeats.length > 0 && !selectedStandingZone"
        class="selection-panel position-fixed bottom-0 start-0 end-0 p-4 bg-reactive-secondary border-top border-primary"
        style="z-index:1000;"
    >
      <div class="container" style="max-width:600px;">
        <div class="d-flex justify-content-between align-items-start mb-3">
          <div class="flex-grow-1">
            <h5 class="text-reactive-primary mb-1">
              {{ getZoneTicket(activeFloor?.layout.zones.find(z => z.zone_uuid === selectedSeats[0]?.zoneUuid)!)?.name }}
              <small class="text-reactive-secondary fw-normal ms-1">({{ selectedSeats[0]?.zoneName }})</small>
            </h5>
            <div class="d-flex flex-wrap gap-1 mt-1">
              <span
                  v-for="seat in selectedSeats" :key="seat.seatId"
                  class="badge d-inline-flex align-items-center gap-1"
                  :style="{ backgroundColor: seat.zoneColor }"
              >
                {{ seat.seatId }}<i class="bi bi-x" style="cursor:pointer;" @click="deselectSeat(seat.seatId)"/>
              </span>
            </div>
          </div>
          <button class="btn-close ms-2" @click="selectedSeats = []"/>
        </div>
        <div class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-reactive-secondary">{{ $t('select_ticket.selection.total') }}:</span>
          <span class="text-primary fw-bold fs-5">{{ formatPrice(selectedSeatsTotalPrice) }}</span>
        </div>
        <button class="btn btn-primary w-100 py-2 fw-semibold" @click="addSeatsToCart">
          <i class="bi bi-cart-plus me-2"/>{{ $t('select_ticket.selection.add_to_cart') }} ({{ selectedSeats.length }})
        </button>
      </div>
    </div>

  </div>
</template>

<style scoped>
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
  from {
    transform: translateY(100%);
    opacity: 0;
  } to {
        transform: translateY(0);
        opacity: 1;
      }
}
</style>