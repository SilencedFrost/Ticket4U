<template>
  <div class="layout-preview" ref="containerRef">
    <svg
      v-if="parsed"
      :viewBox="`0 0 ${W} ${H}`"
      preserveAspectRatio="xMidYMid meet"
      class="w-100 h-100"
      style="display:block;"
    >
      <!-- Background -->
      <rect width="100%" height="100%" fill="rgba(0,0,0,0.25)" rx="6"/>

      <!-- Stage -->
      <template v-if="parsed.stage">
        <rect
          :x="px(parsed.stage.x1)"
          :y="py(parsed.stage.y1)"
          :width="px(parsed.stage.x2) - px(parsed.stage.x1)"
          :height="py(parsed.stage.y2) - py(parsed.stage.y1)"
          fill="#f59e0b"
          rx="4"
        />
        <text
          :x="(px(parsed.stage.x1) + px(parsed.stage.x2)) / 2"
          :y="(py(parsed.stage.y1) + py(parsed.stage.y2)) / 2"
          text-anchor="middle"
          dominant-baseline="middle"
          font-size="11"
          font-weight="bold"
          fill="#1a1a1a"
        >STAGE</text>
      </template>

      <!-- Multi-floor layout: render all floors -->
      <template v-if="parsed.floors">
        <template v-for="(floor, fi) in parsed.floors" :key="fi">
          <template v-for="zone in floor.zones" :key="zone.zone_name">
            <template v-if="zone.accessible !== false">
              <polygon
                :points="zonePoints(zone)"
                :fill="zoneFill(zone)"
                :stroke="zoneStroke(zone)"
                stroke-width="1.5"
              />
              <text
                :x="zoneCenterX(zone)"
                :y="zoneCenterY(zone)"
                text-anchor="middle"
                dominant-baseline="middle"
                font-size="10"
                font-weight="600"
                fill="#fff"
                style="pointer-events:none;"
              >{{ zone.zone_name }}</text>
            </template>
            <polygon
              v-else
              :points="zonePoints(zone)"
              fill="rgba(255,255,255,0.04)"
              stroke="#666"
              stroke-width="1"
              stroke-dasharray="5,4"
            />
          </template>
        </template>
      </template>

      <!-- Single-floor layout: render zones directly -->
      <template v-else-if="parsed.zones">
        <template v-for="zone in parsed.zones" :key="zone.zone_name">
          <template v-if="zone.accessible !== false">
            <polygon
              :points="zonePoints(zone)"
              :fill="zoneFill(zone)"
              :stroke="zoneStroke(zone)"
              stroke-width="1.5"
            />
            <text
              :x="zoneCenterX(zone)"
              :y="zoneCenterY(zone)"
              text-anchor="middle"
              dominant-baseline="middle"
              font-size="10"
              font-weight="600"
              fill="#fff"
              style="pointer-events:none;"
            >{{ zone.zone_name }}</text>
          </template>
          <polygon
            v-else
            :points="zonePoints(zone)"
            fill="rgba(255,255,255,0.04)"
            stroke="#666"
            stroke-width="1"
            stroke-dasharray="5,4"
          />
        </template>
      </template>

    </svg>

    <div v-else class="d-flex align-items-center justify-content-center h-100 text-reactive-secondary small">
      <i class="bi bi-exclamation-circle me-2"/>No layout configured
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  layoutJson: string
}>()

const W = 900
const H = 520

// Convert normalized [-1, 1] to pixel coords
const px = (n: number) => ((n + 1) / 2) * W
const py = (n: number) => ((n + 1) / 2) * H

const parsed = computed(() => {
  if (!props.layoutJson) return null
  try { return JSON.parse(props.layoutJson) }
  catch { return null }
})

// Color comes from layout JSON zone.color — not from DB zone entity
const zoneFill   = (zone: any) => (zone.color ?? '#6366f1') + '55'
const zoneStroke = (zone: any) =>  zone.color ?? '#6366f1'

const zonePoints = (zone: any): string => {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (corners.length === 0) return ''
  return corners.map((c: any) => `${px(c.x)},${py(c.y)}`).join(' ')
}

const zoneCenterX = (zone: any): number => {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (corners.length === 0) return W / 2
  return corners.reduce((s: number, c: any) => s + px(c.x), 0) / corners.length
}

const zoneCenterY = (zone: any): number => {
  const corners = [zone.corner1, zone.corner2, zone.corner3, zone.corner4].filter(Boolean)
  if (corners.length === 0) return H / 2
  return corners.reduce((s: number, c: any) => s + py(c.y), 0) / corners.length
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