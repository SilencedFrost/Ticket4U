<template>
  <div class="layout-preview" ref="containerRef">
    <svg
      v-if="parsed"
      :viewBox="`0 0 ${W} ${H}`"
      preserveAspectRatio="xMidYMid meet"
      class="w-100 h-100"
      style="display:block;"
    >
      <!-- Background grid -->
      <rect width="100%" height="100%" fill="rgba(0,0,0,0.3)" rx="6"/>
      <defs>
        <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
          <path d="M 40 0 L 0 0 0 40" fill="none" stroke="rgba(255,255,255,0.04)" stroke-width="1"/>
        </pattern>
      </defs>
      <rect width="100%" height="100%" fill="url(#grid)" rx="6"/>

      <!-- Floor tabs (multi-floor) -->
      <template v-if="parsed.floors && parsed.floors.length > 1">
        <g v-for="(floor, fi) in parsed.floors" :key="fi">
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

      <!-- Render current floor -->
      <g v-if="currentFloor">

        <!-- Stage bar -->
        <template v-if="currentFloor.stage">
          <rect
            :x="px(currentFloor.stage.x1)" :y="py(currentFloor.stage.y1)"
            :width="px(currentFloor.stage.x2) - px(currentFloor.stage.x1)"
            :height="py(currentFloor.stage.y2) - py(currentFloor.stage.y1)"
            fill="#f59e0b" rx="4"
          />
          <text
            :x="(px(currentFloor.stage.x1) + px(currentFloor.stage.x2)) / 2"
            :y="(py(currentFloor.stage.y1) + py(currentFloor.stage.y2)) / 2"
            text-anchor="middle" dominant-baseline="middle"
            font-size="11" font-weight="bold" fill="#1a1a1a"
          >STAGE</text>
        </template>

        <!-- Extra stage shapes -->
        <template v-for="s in (currentFloor.stage_shapes ?? [])" :key="s.label">
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
        <template v-for="zone in (currentFloor.zones ?? [])" :key="zone.zone_name">
          <!-- Zone shape -->
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
            stroke="#555"
            stroke-width="1"
            stroke-dasharray="5,4"
          />

          <!-- Seat dots — only for seated zones -->
          <template v-if="zone.accessible !== false && !isStandingZone(zone)">
            <g v-for="dot in seatDots(zone, currentFloor)" :key="dot.key">
              <circle
                :cx="dot.cx" :cy="dot.cy" :r="dot.r"
                :fill="(zone.color ?? '#6366f1') + 'cc'"
                stroke="rgba(255,255,255,0.3)"
                stroke-width="0.5"
              />
            </g>
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
            :y="showSeats(zone) ? zoneBounds(zone).y + 12 : zoneCenterY(zone)"
            text-anchor="middle" dominant-baseline="middle"
            font-size="10" font-weight="700" fill="#fff"
            style="pointer-events:none; text-shadow: 0 1px 3px rgba(0,0,0,0.8);"
          >{{ zone.zone_name }}</text>
        </template>

      </g>

    </svg>

    <div v-else class="d-flex align-items-center justify-content-center h-100 text-reactive-secondary small">
      <i class="bi bi-exclamation-circle me-2"/>No layout configured
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = defineProps<{
  layoutJson: string
}>()

const W = 900
const H = 520

const activeFloor = ref(0)

// Normalize [-1,1] → pixel
const px = (n: number) => ((n + 1) / 2) * W
const py = (n: number) => ((n + 1) / 2) * H

const parsed = computed(() => {
  if (!props.layoutJson) return null
  try { return JSON.parse(props.layoutJson) } catch { return null }
})

const currentFloor = computed(() => {
  if (!parsed.value) return null
  if (parsed.value.floors) return parsed.value.floors[activeFloor.value] ?? null
  // Single-floor flat format
  return parsed.value
})

const zoneFill   = (zone: any) => (zone.color ?? '#6366f1') + '44'
const zoneStroke = (zone: any) =>  zone.color ?? '#6366f1'

const zonePoints = (zone: any): string => {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  return corners.map((c: any) => `${px(c.x)},${py(c.y)}`).join(' ')
}

const zoneBounds = (zone: any) => {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (corners.length === 0) return { x: 0, y: 0, w: W, h: H }
  const xs = corners.map((c: any) => px(c.x))
  const ys = corners.map((c: any) => py(c.y))
  const x = Math.min(...xs), y = Math.min(...ys)
  return { x, y, w: Math.max(...xs) - x, h: Math.max(...ys) - y }
}

const zoneCenterX = (zone: any) => {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (!corners.length) return W / 2
  return corners.reduce((s: number, c: any) => s + px(c.x), 0) / corners.length
}

const zoneCenterY = (zone: any) => {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (!corners.length) return H / 2
  return corners.reduce((s: number, c: any) => s + py(c.y), 0) / corners.length
}

// Standing zones: color is green-ish or zone_type is standing
const isStandingZone = (zone: any) =>
  zone.zone_type === 'standing' ||
  zone.zone_name?.toLowerCase().includes('standing') ||
  zone.zone_name?.toLowerCase() === 'ga' ||
  zone.zone_name?.toLowerCase().includes('pit')

const showSeats = (zone: any) => !isStandingZone(zone)

// Auto-fit seat dots into zone bounds
const seatDots = (zone: any, floor: any) => {
  const b    = zoneBounds(zone)
  const globalSeatSize = floor?.global_seat_size ?? 14
  // Use zone-specific or global seat size
  const seatPx = (zone.seat_size ?? globalSeatSize)
  const PADDING = seatPx * 0.8
  const gap     = seatPx + 2

  const availW = b.w - PADDING * 2
  const availH = b.h - PADDING * 2 - 16  // 16px for label at top
  if (availW <= 0 || availH <= 0) return []

  const cols = Math.max(1, Math.floor(availW / gap))
  const rows = Math.max(1, Math.floor(availH / gap))

  // Auto-size radius to fit
  const r = Math.min(
    seatPx / 2,
    (availW / cols - 2) / 2,
    (availH / rows - 2) / 2,
    8  // max dot size in preview
  )
  if (r < 1.5) return []

  const actualGapX = availW / cols
  const actualGapY = availH / rows
  const startX = b.x + PADDING + actualGapX / 2
  const startY = b.y + 16 + PADDING + actualGapY / 2  // below label

  const dots: { key: string; cx: number; cy: number; r: number }[] = []
  for (let row = 0; row < rows; row++) {
    for (let col = 0; col < cols; col++) {
      dots.push({
        key:  `${row}-${col}`,
        cx:   startX + col * actualGapX,
        cy:   startY + row * actualGapY,
        r,
      })
    }
  }
  return dots
}
</script>

<style scoped>
.layout-preview {
  width: 100%;
  aspect-ratio: 900 / 520;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 6px;
  overflow: hidden;
}
</style>