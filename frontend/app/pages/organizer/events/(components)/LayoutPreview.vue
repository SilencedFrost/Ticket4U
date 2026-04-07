<script setup lang="ts">
import { ref, computed } from 'vue'
import type { VenueLayout, VenueLayoutZone, VenueLayoutFloor } from '../../(types)/venue'

const props = defineProps<{
  layout: VenueLayout
}>()

// ── Constants ─────────────────────────────────────────────────
const W = 900
const H = 520

const activeFloor = ref(0)

// ── Coordinate helpers ────────────────────────────────────────
function px(n: number): number {
  return ((n + 1) / 2) * W
}

function py(n: number): number {
  return ((n + 1) / 2) * H
}

// ── Floor resolution ──────────────────────────────────────────
const currentFloor = computed<VenueLayoutFloor | VenueLayout | null>(() => {
  if (!props.layout) return null
  if (props.layout.floors?.length) return props.layout.floors[activeFloor.value] ?? null
  return props.layout
})

// ── Zone geometry ─────────────────────────────────────────────
function zonePoints(zone: VenueLayoutZone): string {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  return corners.map(c => `${px(c.x)},${py(c.y)}`).join(' ')
}

function zoneBounds(zone: VenueLayoutZone): { x: number; y: number; w: number; h: number } {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (!corners.length) return { x: 0, y: 0, w: W, h: H }
  const xs = corners.map(c => px(c.x))
  const ys = corners.map(c => py(c.y))
  const x  = Math.min(...xs)
  const y  = Math.min(...ys)
  return { x, y, w: Math.max(...xs) - x, h: Math.max(...ys) - y }
}

function zoneCenterX(zone: VenueLayoutZone): number {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (!corners.length) return W / 2
  return corners.reduce((s, c) => s + px(c.x), 0) / corners.length
}

function zoneCenterY(zone: VenueLayoutZone): number {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (!corners.length) return H / 2
  return corners.reduce((s, c) => s + py(c.y), 0) / corners.length
}

// ── Zone appearance ───────────────────────────────────────────
function zoneFill(zone: VenueLayoutZone): string {
  return (zone.color ?? '#6366f1') + '44'
}

function zoneStroke(zone: VenueLayoutZone): string {
  return zone.color ?? '#6366f1'
}

function isStandingZone(zone: VenueLayoutZone): boolean {
  return (
    zone.zone_type === 'standing' ||
    zone.zone_name?.toLowerCase().includes('standing') ||
    zone.zone_name?.toLowerCase() === 'ga' ||
    zone.zone_name?.toLowerCase().includes('pit')
  )
}

// ── Seat dot generation ───────────────────────────────────────
interface SeatDot { key: string; cx: number; cy: number; r: number }

function seatDots(zone: VenueLayoutZone, floor: VenueLayoutFloor | VenueLayout | null): SeatDot[] {
  const b              = zoneBounds(zone)
  const globalSeatSize = (floor as any)?.global_seat_size ?? 14
  const seatPx         = (zone as any).seat_size ?? globalSeatSize
  const PADDING        = seatPx * 0.8
  const gap            = seatPx + 2

  const availW = b.w - PADDING * 2
  const availH = b.h - PADDING * 2 - 16
  if (availW <= 0 || availH <= 0) return []

  const cols = Math.max(1, Math.floor(availW / gap))
  const rows = Math.max(1, Math.floor(availH / gap))

  const r = Math.min(seatPx / 2, (availW / cols - 2) / 2, (availH / rows - 2) / 2, 8)
  if (r < 1.5) return []

  const actualGapX = availW / cols
  const actualGapY = availH / rows
  const startX     = b.x + PADDING + actualGapX / 2
  const startY     = b.y + 16 + PADDING + actualGapY / 2

  const dots: SeatDot[] = []
  for (let row = 0; row < rows; row++) {
    for (let col = 0; col < cols; col++) {
      dots.push({ key: `${row}-${col}`, cx: startX + col * actualGapX, cy: startY + row * actualGapY, r })
    }
  }
  return dots
}
</script>

<template>
  <div class="layout-preview">
    <svg
      :viewBox="`0 0 ${W} ${H}`"
      preserveAspectRatio="xMidYMid meet"
      class="w-100 h-100"
      style="display:block;"
    >
      <!-- Background + grid -->
      <rect width="100%" height="100%" fill="rgba(0,0,0,0.3)" rx="6"/>
      <defs>
        <pattern id="lp-grid" width="40" height="40" patternUnits="userSpaceOnUse">
          <path d="M 40 0 L 0 0 0 40" fill="none" stroke="rgba(255,255,255,0.04)" stroke-width="1"/>
        </pattern>
      </defs>
      <rect width="100%" height="100%" fill="url(#lp-grid)" rx="6"/>

      <!-- Floor tabs (multi-floor) -->
      <template v-if="layout.floors && layout.floors.length > 1">
        <g v-for="(floor, fi) in layout.floors" :key="fi">
          <rect
            :x="8 + fi * 72" y="8" width="68" height="22" rx="4"
            :fill="activeFloor === fi ? 'rgba(99,102,241,0.8)' : 'rgba(255,255,255,0.1)'"
            style="cursor:pointer;"
            @click="activeFloor = fi"
          />
          <text
            :x="8 + fi * 72 + 34" y="19"
            text-anchor="middle" dominant-baseline="middle"
            font-size="10" font-weight="600"
            :fill="activeFloor === fi ? '#fff' : 'rgba(255,255,255,0.5)'"
            style="pointer-events:none;"
          >{{ floor.floor_name ?? `Floor ${fi + 1}` }}</text>
        </g>
      </template>

      <g v-if="currentFloor">

        <!-- Stage -->
        <template v-if="(currentFloor as any).stage">
          <rect
            :x="px((currentFloor as any).stage.x1)"
            :y="py((currentFloor as any).stage.y1)"
            :width="px((currentFloor as any).stage.x2) - px((currentFloor as any).stage.x1)"
            :height="py((currentFloor as any).stage.y2) - py((currentFloor as any).stage.y1)"
            fill="#f59e0b" rx="4"
          />
          <text
            :x="(px((currentFloor as any).stage.x1) + px((currentFloor as any).stage.x2)) / 2"
            :y="(py((currentFloor as any).stage.y1) + py((currentFloor as any).stage.y2)) / 2"
            text-anchor="middle" dominant-baseline="middle"
            font-size="11" font-weight="bold" fill="#1a1a1a"
          >STAGE</text>
        </template>

        <!-- Extra stage shapes -->
        <template v-for="s in ((currentFloor as any).stage_shapes ?? [])" :key="s.label">
          <rect
            :x="px(s.corner1?.x ?? -0.1)" :y="py(s.corner1?.y ?? -0.5)"
            :width="Math.abs(px(s.corner2?.x ?? 0.1) - px(s.corner1?.x ?? -0.1))"
            :height="Math.abs(py(s.corner4?.y ?? -0.3) - py(s.corner1?.y ?? -0.5))"
            fill="#f59e0b88" stroke="#f59e0b" stroke-width="1" rx="3"
          />
          <text
            :x="(px(s.corner1?.x ?? -0.1) + px(s.corner2?.x ?? 0.1)) / 2"
            :y="(py(s.corner1?.y ?? -0.5) + py(s.corner4?.y ?? -0.3)) / 2"
            text-anchor="middle" dominant-baseline="middle"
            font-size="9" fill="#1a1a1a" font-weight="600"
          >{{ s.label }}</text>
        </template>

        <!-- Zones -->
        <template v-for="zone in ((currentFloor as any).zones ?? [])" :key="zone.zone_name">

          <polygon
            v-if="zone.accessible !== false"
            :points="zonePoints(zone)"
            :fill="zoneFill(zone)"
            :stroke="zoneStroke(zone)"
            stroke-width="1.5"
          />
          <polygon
            v-else
            :points="zonePoints(zone)"
            fill="rgba(255,255,255,0.03)"
            stroke="#555" stroke-width="1" stroke-dasharray="5,4"
          />

          <!-- Seat dots — seated zones -->
          <template v-if="zone.accessible !== false && !isStandingZone(zone)">
            <circle
              v-for="dot in seatDots(zone, currentFloor)"
              :key="dot.key"
              :cx="dot.cx" :cy="dot.cy" :r="dot.r"
              :fill="(zone.color ?? '#6366f1') + 'cc'"
              stroke="rgba(255,255,255,0.3)" stroke-width="0.5"
            />
          </template>

          <!-- Standing hatching -->
          <template v-else-if="zone.accessible !== false">
            <line
              v-for="n in 6" :key="n"
              :x1="zoneBounds(zone).x + (zoneBounds(zone).w / 7) * n"
              :y1="zoneBounds(zone).y"
              :x2="zoneBounds(zone).x + (zoneBounds(zone).w / 7) * n"
              :y2="zoneBounds(zone).y + zoneBounds(zone).h"
              :stroke="(zone.color ?? '#22c55e') + '44'"
              stroke-width="1"
            />
          </template>

          <!-- Zone label -->
          <text
            v-if="zone.accessible !== false"
            :x="zoneCenterX(zone)"
            :y="!isStandingZone(zone) ? zoneBounds(zone).y + 12 : zoneCenterY(zone)"
            text-anchor="middle" dominant-baseline="middle"
            font-size="10" font-weight="700" fill="#fff"
            style="pointer-events:none; text-shadow: 0 1px 3px rgba(0,0,0,0.8);"
          >{{ zone.zone_name }}</text>

        </template>

      </g>
    </svg>
  </div>
</template>

<style scoped>
.layout-preview {
  width: 100%;
  aspect-ratio: 900 / 520;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 6px;
  overflow: hidden;
}
</style>