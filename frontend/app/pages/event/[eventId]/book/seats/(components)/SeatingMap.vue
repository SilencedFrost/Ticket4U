<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { useWindowSize, useDark } from '@vueuse/core'
import { useGesture } from '@vueuse/gesture'
import type { Ticket, SelectedSeat } from '../(types)/ticket'
import type { Floor, LayoutZone, LayoutSeat } from '../(types)/seatingLayout'
import type { CartItem } from '../(types)/eventPayment'

const props = defineProps<{ tickets: Ticket[]; floors: Floor[]; cart: CartItem[] }>()
const emit  = defineEmits<{
  (e: 'back'): void
  (e: 'addTicket', zoneId: string, zoneName: string, quantity: number, price: number, isStanding: boolean, seats?: SelectedSeat[]): void
}>()

// Floor state
const activeFloorId = ref('')
const floorDropdownOpen = ref(false)
const activeFloor   = computed(() => props.floors.find(f => f.id === activeFloorId.value))
watch(() => props.floors, floors => {
  const first = floors[0]
  if (first && !activeFloorId.value) activeFloorId.value = first.id
}, { immediate: true })
watch(activeFloor, () => nextTick(() => draw()))

// ── Pan / Zoom declared before updateSize to avoid TDZ ──
const scale     = ref(1)
const pan       = ref({ x: 0, y: 0 })
const ZOOM_STEP = 0.15
const MIN_SCALE = 0.3
const MAX_SCALE = 4

// Canvas
const canvasContainer = ref<HTMLElement | null>(null)
const canvasRef       = ref<HTMLCanvasElement | null>(null)
const canvasSize      = ref({ width: 0, height: 0 })
const CANVAS_W        = 900
const CANVAS_H        = 520

const { width: windowWidth, height: windowHeight } = useWindowSize()
watch([windowWidth, windowHeight], () => updateSize())

const isDark = useDark()
watch(isDark, () => draw())

function centeredPan(w: number, h: number) {
  return { x: (w - CANVAS_W) / 2, y: (h - CANVAS_H) / 2 }
}

function updateSize() {
  if (!canvasContainer.value) return
  const w       = canvasContainer.value.clientWidth
  const h       = canvasContainer.value.clientHeight || CANVAS_H
  const wasZero = canvasSize.value.width === 0
  canvasSize.value = { width: w, height: h }
  if (wasZero) { pan.value = centeredPan(w, h); scale.value = 1 }
  nextTick(() => draw())
}

function onClickOutside() { floorDropdownOpen.value = false }

onMounted(() => {
  updateSize()
  globalThis.addEventListener('click', onClickOutside)
})
onUnmounted(() => {
  globalThis.removeEventListener('click', onClickOutside)
})

function zoomIn()    { scale.value = Math.min(MAX_SCALE, scale.value + ZOOM_STEP); draw() }
function zoomOut()   { scale.value = Math.max(MIN_SCALE, scale.value - ZOOM_STEP); draw() }
function resetZoom() {
  scale.value = 1
  pan.value   = centeredPan(canvasSize.value.width, canvasSize.value.height)
  draw()
}

let wasDragging = false

useGesture(
  {
    onDrag({ movement: [mx, my], first, last, memo }) {
      if (first) {
        memo = { ...pan.value }
        if (canvasRef.value) canvasRef.value.style.cursor = 'grabbing'
      }
      if (last) {
        if (canvasRef.value) canvasRef.value.style.cursor = 'grab'
        setTimeout(() => { wasDragging = false }, 0)
      } else {
        wasDragging = true
      }
      pan.value = { x: memo.x + mx, y: memo.y + my }
      draw()
      return memo
    },
    onPinch({ offset: [d], first, memo }) {
      if (first) memo = scale.value
      scale.value = Math.min(MAX_SCALE, Math.max(MIN_SCALE, memo * d))
      draw()
      return memo
    },
    onWheel({ delta: [, dy], event }) {
      const rect     = canvasRef.value!.getBoundingClientRect()
      const mx       = event.clientX - rect.left, my = event.clientY - rect.top
      const dir      = dy > 0 ? -1 : 1
      const oldScale = scale.value
      const newScale = Math.min(MAX_SCALE, Math.max(MIN_SCALE, oldScale + dir * ZOOM_STEP))
      pan.value      = { x: mx - (mx - pan.value.x) * (newScale / oldScale), y: my - (my - pan.value.y) * (newScale / oldScale) }
      scale.value    = newScale
      draw()
    },
  },
  {
    domTarget:    canvasRef,
    eventOptions: { passive: false },
    drag:         { filterTaps: true },
  }
)

// Coordinate helpers
function toCanvas(nx: number, ny: number) {
  return {
    x: pan.value.x + ((nx + 1) / 2 * CANVAS_W) * scale.value,
    y: pan.value.y + ((ny + 1) / 2 * CANVAS_H) * scale.value,
  }
}
function toNorm(cx: number, cy: number) {
  return {
    x: (cx - pan.value.x) / scale.value / CANVAS_W * 2 - 1,
    y: (cy - pan.value.y) / scale.value / CANVAS_H * 2 - 1,
  }
}

// Seat grid positions — seatSize param removed (unused)
function getSeatGridPositions(zone: LayoutZone): Map<string, { x: number; y: number }> {
  const seats = zone.seats ?? []
  const map   = new Map<string, { x: number; y: number }>()
  if (!seats.length) return map
  const sorted = [...seats].sort((a, b) => {
    const ra = a.seat_id.replaceAll(/\d/g, ''), rb = b.seat_id.replaceAll(/\d/g, '')
    if (ra !== rb) return ra.localeCompare(rb)
    return Number.parseInt(a.seat_id.replaceAll(/\D/g, '') || '0') - Number.parseInt(b.seat_id.replaceAll(/\D/g, '') || '0')
  })
  const minX  = Math.min(zone.corner1.x, zone.corner4.x)
  const maxX  = Math.max(zone.corner2.x, zone.corner3.x)
  const minY  = Math.min(zone.corner1.y, zone.corner2.y)
  const maxY  = Math.max(zone.corner3.y, zone.corner4.y)
  const rows  = [...new Set(sorted.map(s => s.seat_id.replaceAll(/\d/g, '').toUpperCase()))].sort()
  const cols  = Math.max(...rows.map(r => sorted.filter(s => s.seat_id.replaceAll(/\d/g, '').toUpperCase() === r).length))
  const cellW = (maxX - minX) / (cols + 1)
  const cellH = (maxY - minY) / (rows.length + 1)
  for (const seat of sorted) {
    const row = seat.seat_id.replaceAll(/\d/g, '').toUpperCase()
    const col = Number.parseInt(seat.seat_id.replaceAll(/\D/g, '') || '1') - 1
    const ri  = rows.indexOf(row)
    map.set(seat.seat_id, {
      x: minX + cellW * (col + 0.5) + cellW / 2,
      y: minY + cellH * (ri  + 0.5) + cellH / 2,
    })
  }
  return map
}

// Key by UUID — seat_id like "A1" is not globally unique across zones
function isSeatSelected(seat: LayoutSeat): boolean {
  return selectedSeats.value.some(s => s.seatUuid === seat.seat_uuid) ||
      cartSeats.value.has(seat.seat_uuid ?? '')
}

// Draw helpers
function drawStage(ctx: CanvasRenderingContext2D, stage: { x1: number; y1: number; x2: number; y2: number }) {
  const sp1 = toCanvas(stage.x1, stage.y1), sp2 = toCanvas(stage.x2, stage.y2)
  ctx.fillStyle = '#f59e0b'
  ctx.beginPath(); ctx.roundRect(sp1.x, sp1.y, sp2.x - sp1.x, sp2.y - sp1.y, 4); ctx.fill()
  ctx.fillStyle = '#1a1a1a'; ctx.font = 'bold 12px sans-serif'
  ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
  ctx.fillText('Stage / Screen', (sp1.x + sp2.x) / 2, (sp1.y + sp2.y) / 2)
}

function drawZonePolygon(ctx: CanvasRenderingContext2D, zone: LayoutZone, soldOut: boolean) {
  const c1 = toCanvas(zone.corner1.x, zone.corner1.y)
  const c2 = toCanvas(zone.corner2.x, zone.corner2.y)
  const c3 = toCanvas(zone.corner3.x, zone.corner3.y)
  const c4 = toCanvas(zone.corner4.x, zone.corner4.y)
  ctx.beginPath(); ctx.moveTo(c1.x, c1.y); ctx.lineTo(c2.x, c2.y)
  ctx.lineTo(c3.x, c3.y); ctx.lineTo(c4.x, c4.y); ctx.closePath()
  ctx.fillStyle   = soldOut ? '#37415133' : zone.color + '44'
  ctx.strokeStyle = soldOut ? '#4B5563'   : zone.color
  ctx.lineWidth   = 2; ctx.globalAlpha = soldOut ? 0.5 : 1
  ctx.fill(); ctx.stroke(); ctx.globalAlpha = 1
}

function drawZoneLabel(ctx: CanvasRenderingContext2D, zone: LayoutZone, soldOut: boolean, price: string) {
  const cx  = (zone.corner1.x + zone.corner2.x + zone.corner3.x + zone.corner4.x) / 4
  const cy  = (zone.corner1.y + zone.corner2.y + zone.corner3.y + zone.corner4.y) / 4
  const lp  = toCanvas(cx, cy)
  const label = zone.display_name ?? zone.zone_name
  ctx.fillStyle = getTextColor(); ctx.font = 'bold 12px sans-serif'
  ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
  ctx.fillText(label, lp.x, lp.y - (price ? 8 : 0))
  if (price) { ctx.font = '11px sans-serif'; ctx.fillStyle = getTextColor(); ctx.fillText(price, lp.x, lp.y + 8) }
  if (zone.zone_type === 'standing' && !soldOut) {
    ctx.font = '10px sans-serif'; ctx.fillStyle = zone.color + 'cc'
    ctx.fillText('[ Standing ]', lp.x, lp.y + 22)
  }
}

function computeSeatRadius(zone: LayoutZone, seatSize: number): number {
  const zoneW = Math.max(zone.corner2.x, zone.corner3.x) - Math.min(zone.corner1.x, zone.corner4.x)
  const zoneH = Math.max(zone.corner3.y, zone.corner4.y) - Math.min(zone.corner1.y, zone.corner2.y)
  const rows  = [...new Set(zone.seats.map(s => s.seat_id.replaceAll(/\d/g, '').toUpperCase()))].sort()
  const cols  = Math.max(...rows.map(r => zone.seats.filter(s => s.seat_id.replaceAll(/\d/g, '').toUpperCase() === r).length))
  const cellW = (zoneW / (cols + 1)) * CANVAS_W * scale.value
  const cellH = (zoneH / (rows.length + 1)) * CANVAS_H * scale.value
  const maxR  = Math.min(cellW, cellH) / 2 * 0.7
  return Math.min(Math.max(4, seatSize / 2 * (canvasSize.value.width / CANVAS_W) * scale.value), maxR)
}

function getSeatColor(unavailable: boolean, selected: boolean): string {
  if (unavailable) return '#6b7280'
  if (selected)    return '#3b82f6'
  return '#22c55e'
}

function drawSeat(ctx: CanvasRenderingContext2D, seat: LayoutSeat, cp: { x: number; y: number }, r: number) {
  const unavailable = seat.status === 'BOOKED' || seat.status === 'HOLD'
  const selected    = isSeatSelected(seat)
  ctx.beginPath(); ctx.arc(cp.x, cp.y, r, 0, Math.PI * 2)
  ctx.fillStyle = getSeatColor(unavailable, selected)
  ctx.fill()
  if (selected) { ctx.strokeStyle = '#1d4ed8'; ctx.lineWidth = 2; ctx.stroke() }
  if (r >= 7) {
    ctx.fillStyle = '#fff'; ctx.font = `${Math.max(6, r * 0.7)}px sans-serif`
    ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
    ctx.fillText(seat.seat_id, cp.x, cp.y)
  }
}

function drawZoneSeats(ctx: CanvasRenderingContext2D, zone: LayoutZone, seatSize: number) {
  const gridPos = getSeatGridPositions(zone)
  const r       = computeSeatRadius(zone, seatSize)
  for (const seat of zone.seats) {
    const np = gridPos.get(seat.seat_id); if (!np) continue
    drawSeat(ctx, seat, toCanvas(np.x, np.y), r)
  }
}

function drawZone(ctx: CanvasRenderingContext2D, zone: LayoutZone, seatSize: number) {
  const zoneTicket = getZoneTicket(zone)
  const soldOut    = zoneTicket?.soldOut ?? false
  const price      = zoneTicket ? formatPrice(zoneTicket.price) : ''
  drawZonePolygon(ctx, zone, soldOut)
  if (zone.zone_type === 'standing' || zone.seats.length === 0) drawZoneLabel(ctx, zone, soldOut, price)
  if (zone.zone_type === 'sitting' && zone.seats.length > 0) drawZoneSeats(ctx, zone, seatSize)
}

// Draw
function draw() {
  const canvas = canvasRef.value; if (!canvas || canvasSize.value.width === 0) return
  const ctx    = canvas.getContext('2d'); if (!ctx) return
  const floor  = activeFloor.value; if (!floor) { ctx.clearRect(0, 0, canvas.width, canvas.height); return }

  ctx.clearRect(0, 0, canvas.width, canvas.height)
  if (floor.layout.stage) drawStage(ctx, floor.layout.stage)
  for (const zone of floor.layout.zones) drawZone(ctx, zone, floor.layout.seat_size ?? 14)
}

function findClickedSeat(floor: Floor, cx: number, cy: number): { seat: LayoutSeat; zone: LayoutZone } | null {
  for (const zone of floor.layout.zones) {
    if (zone.zone_type !== 'sitting') continue
    const gridPos = getSeatGridPositions(zone)
    const r       = computeSeatRadius(zone, floor.layout.seat_size ?? 14) + 4
    for (const seat of zone.seats) {
      const np = gridPos.get(seat.seat_id); if (!np) continue
      if (Math.hypot(cx - toCanvas(np.x, np.y).x, cy - toCanvas(np.x, np.y).y) <= r) return { seat, zone }
    }
  }
  return null
}

function findClickedZone(floor: Floor, nx: number, ny: number): LayoutZone | null {
  for (const zone of floor.layout.zones) {
    if (isPointInZone(nx, ny, zone)) return zone
  }
  return null
}

// Click handling
function handleCanvasClick(e: MouseEvent) {
  if (wasDragging) return
  const rect        = canvasRef.value!.getBoundingClientRect()
  const cx          = e.clientX - rect.left, cy = e.clientY - rect.top
  const { x: nx, y: ny } = toNorm(cx, cy)
  const floor       = activeFloor.value; if (!floor) return

  const hitSeat = findClickedSeat(floor, cx, cy)
  if (hitSeat) { handleSeatClick(hitSeat.seat, hitSeat.zone); return }

  const hitZone = findClickedZone(floor, nx, ny)
  if (hitZone) handleZoneClick(hitZone)
}

function isPointInZone(x: number, y: number, zone: LayoutZone): boolean {
  const pts = [zone.corner1, zone.corner2, zone.corner3, zone.corner4]
  let inside = false
  for (let i = 0, j = pts.length - 1; i < pts.length; j = i++) {
    const pi = pts[i], pj = pts[j]
    if (!pi || !pj) continue
    const xi = pi.x, yi = pi.y, xj = pj.x, yj = pj.y
    if (((yi > y) !== (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi)) inside = !inside
  }
  return inside
}

function handleZoneClick(zone: LayoutZone) {
  const zoneTicket = getZoneTicket(zone)
  if (!zoneTicket || zoneTicket.soldOut) return
  if (zone.zone_type === 'standing') { selectedStandingZone.value = zone; standingQuantity.value = 1 }
}

// Standing selection
const selectedStandingZone = ref<LayoutZone | null>(null)
const standingQuantity     = ref(1)

const maxStandingAllowed = computed(() => {
  if (!selectedStandingZone.value) return 0
  const t   = getZoneTicket(selectedStandingZone.value); if (!t) return 0
  const cap = t.capacity ?? 0
  const inCart = props.cart
      .find(item => item.zoneId === selectedStandingZone.value?.zone_uuid && item.isStanding)
      ?.quantity ?? 0
  // null means unlimited — use capacity as ceiling
  const max = Math.min(cap, t.maxPerAccount ?? cap)
  return Math.max(0, max - inCart)
})
watch(maxStandingAllowed, (newMax) => {
  if (newMax === 0) standingQuantity.value = 0
  else if (standingQuantity.value > newMax) standingQuantity.value = newMax
})

function addStandingToCart() {
  if (!selectedStandingZone.value || standingQuantity.value <= 0) return
  const zoneTicket = getZoneTicket(selectedStandingZone.value); if (!zoneTicket) return
  const dbTicket   = props.tickets.find(ticket => ticket.id === selectedStandingZone.value!.zone_uuid)
  const zoneName   = dbTicket?.name ?? selectedStandingZone.value.display_name ?? selectedStandingZone.value.zone_name
  emit('addTicket', selectedStandingZone.value.zone_uuid ?? selectedStandingZone.value.zone_name, zoneName, standingQuantity.value, zoneTicket.price, true)
  selectedStandingZone.value = null; standingQuantity.value = 1
}

// Seated selection — SelectedSeat already has all needed fields including price
const selectedSeats           = ref<SelectedSeat[]>([])
const cartSeats               = ref<Set<string>>(new Set())
const selectedSeatsTotalPrice = computed(() => selectedSeats.value.reduce((s, seat) => s + seat.price, 0))
const seatLimitReached        = ref(false)
const seatLimitMax            = ref(0)

function handleSeatClick(seat: LayoutSeat, zone: LayoutZone) {
  selectedStandingZone.value = null
  if (seat.status === 'BOOKED' || seat.status === 'HOLD') return
  const zoneTicket = getZoneTicket(zone); if (!zoneTicket || zoneTicket.soldOut) return

  const idx = selectedSeats.value.findIndex(s => s.seatUuid === seat.seat_uuid)
  if (idx >= 0) {
    selectedSeats.value.splice(idx, 1); seatLimitReached.value = false
  } else {
    const seatsInCart = props.cart.find(item => item.zoneId === zone.zone_uuid && !item.isStanding)?.seats?.length ?? 0
    const cap         = zoneTicket.capacity ?? 0
    // null means unlimited — use capacity as ceiling
    const max = Math.min(cap, zoneTicket.maxPerAccount ?? cap)
    if (selectedSeats.value.filter(s => s.zoneUuid === zone.zone_uuid).length + seatsInCart >= max) {
      seatLimitReached.value = true; seatLimitMax.value = max
      setTimeout(() => { seatLimitReached.value = false }, 2500)
      return
    }
    seatLimitReached.value = false
    selectedSeats.value.push({
      seatId:    seat.seat_id,
      seatName:  seat.seat_name,
      seatUuid:  seat.seat_uuid ?? '',
      zoneUuid:  zone.zone_uuid,
      zoneName:  zone.display_name ?? zone.zone_name,
      zoneColor: zone.color,
      price: seat.priceOverride ?? zoneTicket.price,
    })
  }
  draw()
}

function deselectSeat(seatUuid: string) {
  const i = selectedSeats.value.findIndex(s => s.seatUuid === seatUuid)
  if (i >= 0) { selectedSeats.value.splice(i, 1); draw() }
}

function addSeatsToCart() {
  if (!selectedSeats.value.length) return
  const byZone = selectedSeats.value.reduce((acc, s) => {
    const k = s.zoneUuid ?? s.zoneName
    if (!acc[k]) acc[k] = []
    acc[k]!.push(s); return acc
  }, {} as Record<string, SelectedSeat[]>)
  for (const seats of Object.values(byZone)) {
    const f = seats[0]
    if (!f) continue
    const dbTicket = props.tickets.find(t => t.id === f.zoneUuid)
    const zoneName = dbTicket?.name ?? f.zoneName
    emit('addTicket', f.zoneUuid ?? f.zoneName, zoneName, seats.length, f.price, false, seats)
  }
  for (const s of selectedSeats.value) cartSeats.value.add(s.seatUuid)
  selectedSeats.value = []
  draw()
}

function syncCartSeats(items: CartItem[]) {
  cartSeats.value = new Set(items.flatMap(item => item.seats?.map(s => s.seatUuid) ?? []))
  draw()
}
defineExpose({ syncCartSeats })

// Helpers
function getZoneTicket(zone: LayoutZone): Ticket | undefined {
  return props.tickets.find(t => t.id === zone.zone_uuid || t.name === (zone.display_name ?? zone.zone_name))
}

function getTextColor(): string {
  return getComputedStyle(document.documentElement).getPropertyValue('--text-reactive-primary').trim()
}

function formatPrice(price: number): string {
  return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
}

watch([() => props.floors, () => props.tickets, selectedSeats], () => nextTick(() => draw()), { deep: true })

// Watch cart prop directly so cartSeats stays in sync whenever the parent
// mutates it — covers both removeFromCart and removeSeatFromCart without
// relying on the parent explicitly calling syncCartSeats via ref
watch(() => props.cart, (newCart) => {
  cartSeats.value = new Set(newCart.flatMap(item => item.seats?.map(s => s.seatUuid) ?? []))
  draw()
}, { deep: true })
</script>

<template>
  <div class="seating-map-wrapper h-100 d-flex flex-column">

    <div class="p-3 bg-reactive-primary flex-shrink-0 d-flex align-items-center justify-content-between flex-wrap gap-2">
      <div class="d-flex align-items-center gap-2">
        <button class="btn btn-sm text-reactive-primary" @click="$emit('back')">
          <i class="bi bi-arrow-left me-1"/>{{ $t('select_ticket.header.back') }}
        </button>
      </div>

      <div class="text-center">
        <h6 class="text-primary mb-0">{{ $t('select_ticket.header.title') }}</h6>
        <small class="text-reactive-secondary">{{ $t('select_ticket.header.subtitle') }}</small>
      </div>

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

    <div ref="canvasContainer" class="flex-grow-1 position-relative overflow-hidden bg-reactive-secondary">
      <div class="position-absolute d-flex flex-column gap-1" style="top:12px;left:12px;z-index:10;">
        <button class="btn btn-sm btn-primary" title="Zoom in" @click="zoomIn" ><i class="bi bi-plus-lg"/></button>
        <button class="btn btn-sm btn-primary" title="Zoom out" @click="zoomOut" ><i class="bi bi-dash-lg"/></button>
        <button class="btn btn-sm btn-primary" title="Reset zoom" @click="resetZoom" ><i class="bi bi-arrows-fullscreen"/></button>
      </div>

      <div v-if="floors.length > 0" class="position-absolute" style="top:12px;right:12px;z-index:10;">
        <div v-if="floors.length === 1" class="floor-panel d-flex align-items-center gap-2 px-3 py-2">
          <i class="bi bi-layers text-reactive-secondary" style="font-size:0.85rem;"/>
          <span class="text-reactive-primary small fw-semibold">{{floors[0].floor_name }}</span>
        </div>

        <div v-else style="position:relative;">
          <button class="floor-panel d-flex align-items-center gap-2 px-3 py-2" @click.stop="floorDropdownOpen = !floorDropdownOpen">
            <i class="bi bi-layers text-primary" style="font-size:0.85rem;"/>
            <span class="text-reactive-primary small fw-semibold">{{ activeFloor?.floor_name }}</span>
            <i class="bi text-reactive-secondary" :class="floorDropdownOpen ? 'bi-chevron-up' : 'bi-chevron-down'" style="font-size:0.7rem;"/>
          </button>
          <div v-if="floorDropdownOpen" style="position:absolute;top:calc(100% + 4px);right:0;min-width:160px;background:var(--bg-reactive-primary);border-radius:var(--bs-border-radius);box-shadow:0 4px 16px rgba(0,0,0,.18);overflow:hidden;z-index:20;">
            <button
                v-for="floor in floors" :key="floor.id"
                class="floor-dropdown-item d-flex align-items-center gap-2"
                :class="{ active: activeFloorId === floor.id }"
                @click="activeFloorId = floor.id; floorDropdownOpen = false"
            >
              <i class="bi bi-check2" :style="{ opacity: activeFloorId === floor.id ? 1 : 0 }"/>
              {{ floor.floor_name }}
            </button>
          </div>
        </div>
      </div>
      <canvas
          v-if="canvasSize.width > 0"
          ref="canvasRef"
          :width="canvasSize.width"
          :height="canvasSize.height"
          style="position:absolute;top:0;left:0;cursor:grab;"
          @click="handleCanvasClick"
      />
      <div v-else class="d-flex align-items-center justify-content-center h-100">
        <div class="spinner-border text-primary"/>
      </div>
      <div v-if="canvasSize.width > 0 && floors.length === 0" class="position-absolute top-50 start-50 translate-middle text-center text-reactive-secondary">
        <i class="bi bi-map fs-1 d-block mb-2"/>
        <p>{{ $t('select_ticket.no_layout') }}</p>
      </div>
    </div>

    <div
        v-if="selectedStandingZone"
        class="selection-panel position-fixed bottom-0 start-0 end-0 p-4 bg-reactive-secondary border-top border-primary"
        style="z-index:1000;"
    >
      <div class="container" style="max-width:600px;">
        <div class="d-flex justify-content-between align-items-start mb-3">
          <div>
            <h5 class="text-reactive-primary mb-1">
              {{ getZoneTicket(selectedStandingZone)?.name }}
              <small class="text-reactive-secondary fw-normal ms-1">({{ selectedStandingZone.display_name ?? selectedStandingZone.zone_name }})</small>
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
                  v-model.number="standingQuantity" type="number" min="1" :max="maxStandingAllowed"
                  class="form-control text-center bg-reactive-primary text-reactive-primary border-0 fw-bold"
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
            <button class="btn btn-primary w-100 py-2 fw-semibold" :disabled="standingQuantity === 0 || maxStandingAllowed === 0" @click="addStandingToCart">
              <i class="bi bi-cart-plus me-2"/>{{ $t('select_ticket.selection.add_to_cart') }}
            </button>
          </div>
        </div>
      </div>
    </div>

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
                  v-for="seat in selectedSeats" :key="seat.seatUuid"
                  class="badge d-inline-flex align-items-center gap-1"
                  :style="{ backgroundColor: seat.zoneColor }"
              >
                {{ seat.seatId }}<i class="bi bi-x" style="cursor:pointer;" @click="deselectSeat(seat.seatUuid)"/>
              </span>
            </div>
          </div>
          <button class="btn-close ms-2" @click="selectedSeats = []"/>
        </div>
        <div class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-reactive-secondary">{{ $t('select_ticket.selection.total') }}:</span>
          <span class="text-primary fw-bold fs-5">{{ formatPrice(selectedSeatsTotalPrice) }}</span>
        </div>
        <Transition name="fade">
          <div v-if="seatLimitReached" class="alert alert-warning py-2 small mb-2">
            <i class="bi bi-exclamation-triangle me-1"/>
            {{ $t('select_ticket.validation.max_reached', { max: seatLimitMax }) }}
          </div>
        </Transition>
        <button class="btn btn-primary w-100 py-2 fw-semibold" @click="addSeatsToCart">
          <i class="bi bi-cart-plus me-2"/>{{ $t('select_ticket.selection.add_to_cart') }} ({{ selectedSeats.length }})
        </button>
      </div>
    </div>

  </div>
</template>

<style scoped>
.legend-dot { width: 12px; height: 12px; border-radius: 3px; flex-shrink: 0; }

.floor-panel {
  background: var(--bg-reactive-primary);
  border: none;
  border-radius: var(--bs-border-radius);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
  cursor: pointer;
}

.floor-dropdown-item {
  width: 100%;
  padding: 8px 12px;
  border: none;
  background: transparent;
  color: var(--text-reactive-primary);
  font-size: 0.875rem;
  text-align: left;
  cursor: pointer;
  transition: background 0.15s;
}

.floor-dropdown-item:hover { background: var(--bg-reactive-secondary); }
.floor-dropdown-item.active { color: var(--bs-primary); font-weight: 600; }

.selection-panel { animation: slideUp 0.25s ease; }
@keyframes slideUp {
  from { transform: translateY(100%); opacity: 0; }
  to   { transform: translateY(0);    opacity: 1; }
}
</style>