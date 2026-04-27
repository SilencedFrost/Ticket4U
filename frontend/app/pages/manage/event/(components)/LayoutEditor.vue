<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { Zone as TicketZone } from '../../(types)/zone'

const props = defineProps<{
  modelValue: string | null | undefined
  zones: TicketZone[]
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const { t: $t } = useI18n()

// ── Canvas constants ──────────────────────────────────────────
const W = 900
const H = 520

// ── Color palette ─────────────────────────────────────────────
const PALETTE = [
  '#6366f1', '#f59e0b', '#22c55e', '#ef4444',
  '#3b82f6', '#a855f7', '#06b6d4', '#f97316', '#ec4899',
]

// ── Internal types ────────────────────────────────────────────
interface ESeat { seat_id: string; seat_name?: string; status?: string }

interface EZone {
  id:       string
  name:     string
  type:     'sitting' | 'standing'
  color:    string
  linkedId: string   // matches TicketZone.id
  x1: number; y1: number   // top-left, normalized −1…1
  x2: number; y2: number   // bottom-right
  seats:    ESeat[]
}

interface EFloor {
  id:       string
  name:     string
  seatSize: number
  stage:    { x1: number; y1: number; x2: number; y2: number }
  zones:    EZone[]
}

// ── State ─────────────────────────────────────────────────────
const floors    = ref<EFloor[]>([])
const activeIdx = ref(0)
const floor     = computed(() => floors.value[activeIdx.value] ?? null)

const selectedId = ref<string | null>(null)
const selected   = computed(() =>
  selectedId.value ? (floor.value?.zones.find(z => z.id === selectedId.value) ?? null) : null
)

// ── Coordinate helpers ────────────────────────────────────────
const svgRef = ref<SVGSVGElement | null>(null)

function px(n: number) { return ((n + 1) / 2) * W }
function py(n: number) { return ((n + 1) / 2) * H }

function eventToNorm(e: MouseEvent): { nx: number; ny: number } {
  const el = svgRef.value
  if (!el) return { nx: 0, ny: 0 }
  const r = el.getBoundingClientRect()
  return {
    nx: ((e.clientX - r.left) / r.width)  * 2 - 1,
    ny: ((e.clientY - r.top)  / r.height) * 2 - 1,
  }
}

// ── Serialize / deserialize ───────────────────────────────────
function defaultStage() { return { x1: -0.35, y1: -0.96, x2: 0.35, y2: -0.76 } }

function mkFloor(name: string, i: number): EFloor {
  return { id: `fl-${Date.now()}-${i}`, name, seatSize: 16, stage: defaultStage(), zones: [] }
}

let _loading = false

function deserialize(json: string | null | undefined) {
  _loading = true
  if (!json) {
    floors.value   = [mkFloor('Floor 1', 0)]
    activeIdx.value = 0
    _loading = false
    return
  }
  try {
    const p = JSON.parse(json)
    if (!p.floors?.length) {
      floors.value   = [mkFloor('Floor 1', 0)]
      activeIdx.value = 0
      _loading = false
      return
    }
    floors.value = (p.floors as any[]).map((f, i) => ({
      id:       `fl-${i}`,
      name:     f.floor_name ?? `Floor ${i + 1}`,
      seatSize: f.global_seat_size ?? 16,
      stage:    f.stage ?? defaultStage(),
      zones:    ((f.zones ?? []) as any[]).map((z: any) => ({
        id:       z.zone_id ?? `z-${Math.random().toString(36).slice(2)}`,
        name:     z.zone_name ?? 'Zone',
        type:     z.zone_type ?? 'standing',
        color:    z.color ?? '#6366f1',
        linkedId: z.zone_id ?? '',
        x1: z.corner1?.x ?? -0.3,
        y1: z.corner1?.y ?? -0.3,
        x2: z.corner3?.x ?? 0.3,
        y2: z.corner3?.y ?? 0.3,
        seats:    (z.seats ?? []).map((s: any) => ({ seat_id: s.seat_id, seat_name: s.seat_name, status: s.status })).filter((s: ESeat) => s.seat_id),
      })),
    }))
    activeIdx.value = Math.min(activeIdx.value, floors.value.length - 1)
  } catch {
    floors.value   = [mkFloor('Floor 1', 0)]
    activeIdx.value = 0
  }
  _loading = false
}

function serialize(): string {
  return JSON.stringify({
    floors: floors.value.map((f, i) => ({
      floor_name:       f.name,
      floor_order:      i + 1,
      global_seat_size: f.seatSize,
      stage:            f.stage,
      stage_shapes:     [],
      zones: f.zones.map(z => ({
        zone_id:    z.linkedId || z.id,
        zone_name:  z.name,
        zone_type:  z.type,
        accessible: true,
        shape_type: 'rect',
        color:      z.color,
        rotation:   0,
        corner1:    { x: z.x1, y: z.y1 },
        corner2:    { x: z.x2, y: z.y1 },
        corner3:    { x: z.x2, y: z.y2 },
        corner4:    { x: z.x1, y: z.y2 },
        seats:      z.seats ?? [],
      })),
    })),
  })
}

watch(() => props.modelValue, deserialize, { immediate: true })

function commit() {
  if (!_loading) emit('update:modelValue', serialize())
}

// ── Drag ──────────────────────────────────────────────────────
type DragKind = 'zone' | 'tl' | 'tr' | 'bl' | 'br' | 'stage'

const drag = ref<{
  kind: DragKind; id: string
  s0nx: number;  s0ny: number
  x1: number;    y1: number
  x2: number;    y2: number
} | null>(null)

function startDrag(e: MouseEvent, kind: DragKind, id: string) {
  e.preventDefault()
  e.stopPropagation()
  const { nx, ny } = eventToNorm(e)
  const f = floor.value
  if (!f) return

  if (kind === 'stage') {
    selectedId.value = null
    drag.value = { kind, id, s0nx: nx, s0ny: ny, ...f.stage }
  } else {
    selectedId.value = id
    const z = f.zones.find(z => z.id === id)!
    drag.value = { kind, id, s0nx: nx, s0ny: ny, x1: z.x1, y1: z.y1, x2: z.x2, y2: z.y2 }
  }
}

function onMouseMove(e: MouseEvent) {
  if (!drag.value || !floor.value) return
  const { nx, ny } = eventToNorm(e)
  const dx = nx - drag.value.s0nx
  const dy = ny - drag.value.s0ny
  const { kind, id, x1: ox1, y1: oy1, x2: ox2, y2: oy2 } = drag.value
  const MIN = 0.06

  if (kind === 'stage') {
    floor.value.stage = { x1: ox1 + dx, y1: oy1 + dy, x2: ox2 + dx, y2: oy2 + dy }
    return
  }

  const z = floor.value.zones.find(z => z.id === id)
  if (!z) return
  if      (kind === 'zone') { z.x1 = ox1 + dx; z.y1 = oy1 + dy; z.x2 = ox2 + dx; z.y2 = oy2 + dy }
  else if (kind === 'tl')   { z.x1 = Math.min(ox2 - MIN, ox1 + dx); z.y1 = Math.min(oy2 - MIN, oy1 + dy) }
  else if (kind === 'tr')   { z.x2 = Math.max(ox1 + MIN, ox2 + dx); z.y1 = Math.min(oy2 - MIN, oy1 + dy) }
  else if (kind === 'bl')   { z.x1 = Math.min(ox2 - MIN, ox1 + dx); z.y2 = Math.max(oy1 + MIN, oy2 + dy) }
  else if (kind === 'br')   { z.x2 = Math.max(ox1 + MIN, ox2 + dx); z.y2 = Math.max(oy1 + MIN, oy2 + dy) }
}

function onMouseUp() {
  if (drag.value) { drag.value = null; commit() }
}

// ── Zone CRUD ─────────────────────────────────────────────────
let _nc = 0

function addZone() {
  if (!floor.value) return
  const n   = ++_nc
  const off = (n % 5) * 0.06
  const z: EZone = {
    id:       `zone-${Date.now()}`,
    name:     `Zone ${n}`,
    type:     'standing',
    color:    PALETTE[n % PALETTE.length]!,
    linkedId: '',
    x1: -0.35 + off, y1: -0.15 + off,
    x2:  0.35 + off, y2:  0.35 + off,
    seats:    [],
  }
  floor.value.zones.push(z)
  selectedId.value = z.id
  commit()
}

function deleteSelected() {
  if (!selectedId.value || !floor.value) return
  floor.value.zones = floor.value.zones.filter(z => z.id !== selectedId.value)
  selectedId.value = null
  commit()
}

// ── Floor CRUD ────────────────────────────────────────────────
function addFloor() {
  floors.value.push(mkFloor(`Floor ${floors.value.length + 1}`, floors.value.length))
  activeIdx.value = floors.value.length - 1
  commit()
}

function removeFloor(i: number) {
  if (floors.value.length <= 1) return
  floors.value.splice(i, 1)
  activeIdx.value  = Math.min(activeIdx.value, floors.value.length - 1)
  selectedId.value = null
  commit()
}

function switchFloor(i: number) {
  activeIdx.value  = i
  selectedId.value = null
}

// ── Seat rendering — matches SeatingMap logic exactly ────────

/** Returns normalized (−1…1) grid positions for each seat_id, same algorithm as SeatingMap */
function getSeatGridPositions(z: EZone): Map<string, { x: number; y: number }> {
  const seats = z.seats
  const map   = new Map<string, { x: number; y: number }>()
  if (!seats.length) return map
  const sorted = [...seats].sort((a, b) => {
    const ra = a.seat_id.replace(/\d/g, ''), rb = b.seat_id.replace(/\d/g, '')
    if (ra !== rb) return ra.localeCompare(rb)
    return Number.parseInt(a.seat_id.replace(/\D/g, '') || '0') - Number.parseInt(b.seat_id.replace(/\D/g, '') || '0')
  })
  const minX = z.x1, maxX = z.x2, minY = z.y1, maxY = z.y2
  const rows  = [...new Set(sorted.map(s => s.seat_id.replace(/\d/g, '').toUpperCase()))].sort()
  const cols  = Math.max(...rows.map(r => sorted.filter(s => s.seat_id.replace(/\d/g, '').toUpperCase() === r).length))
  const cellW = (maxX - minX) / (cols + 1)
  const cellH = (maxY - minY) / (rows.length + 1)
  for (const seat of sorted) {
    const row = seat.seat_id.replace(/\d/g, '').toUpperCase()
    const col = Number.parseInt(seat.seat_id.replace(/\D/g, '') || '1') - 1
    const ri  = rows.indexOf(row)
    map.set(seat.seat_id, {
      x: minX + cellW * (col + 0.5) + cellW / 2,
      y: minY + cellH * (ri  + 0.5) + cellH / 2,
    })
  }
  return map
}

/** Seat circle radius in SVG px — mirrors SeatingMap's computeSeatRadius (no scale factor in SVG) */
function computeSeatRadius(z: EZone, seatSize: number): number {
  const seats  = z.seats
  const zoneW  = px(z.x2) - px(z.x1)
  const zoneH  = py(z.y2) - py(z.y1)
  const rows   = [...new Set(seats.map(s => s.seat_id.replace(/\d/g, '').toUpperCase()))].sort()
  const cols   = Math.max(...rows.map(r => seats.filter(s => s.seat_id.replace(/\d/g, '').toUpperCase() === r).length))
  const cellW  = zoneW / (cols + 1)
  const cellH  = zoneH / (rows.length + 1)
  const maxR   = Math.min(cellW, cellH) / 2 * 0.7
  return Math.min(Math.max(4, seatSize / 1.5), maxR)
}
</script>

<template>
  <div class="layout-editor" @keydown.delete.prevent="deleteSelected" tabindex="0">

    <!-- Toolbar -->
    <div class="d-flex align-items-center gap-2 flex-wrap mb-3">
      <!-- Floor tabs -->
      <div class="d-flex align-items-center gap-1 flex-wrap">
        <button
          v-for="(f, i) in floors"
          :key="f.id"
          class="btn btn-sm d-flex align-items-center gap-1"
          :class="i === activeIdx ? 'btn-primary' : 'btn-outline-secondary'"
          @click="switchFloor(i)"
        >
          {{ f.name }}
          <span
            v-if="floors.length > 1"
            class="remove-floor-x opacity-50 ms-1"
            @click.stop="removeFloor(i)"
          >✕</span>
        </button>
        <button class="btn btn-sm btn-outline-primary" :title="$t('manage.event_form.step4.add_floor')" @click="addFloor">
          <i class="bi bi-plus-lg"/>
        </button>
      </div>

      <div class="ms-auto d-flex gap-2">
        <button class="btn btn-sm btn-outline-primary" @click="addZone">
          <i class="bi bi-plus-lg me-1"/>{{ $t('manage.event_form.step3.add_zone') }}
        </button>
        <button class="btn btn-sm btn-outline-danger" :disabled="!selectedId" @click="deleteSelected">
          <i class="bi bi-trash"/>
        </button>
      </div>
    </div>

    <!-- Canvas + Sidebar -->
    <div class="d-flex gap-3 align-items-start">

      <!-- SVG Canvas -->
      <div class="flex-grow-1" style="min-width: 0;">
        <svg
          ref="svgRef"
          viewBox="0 0 900 520"
          preserveAspectRatio="xMidYMid meet"
          class="editor-svg w-100"
          style="display:block; aspect-ratio:900/520; user-select:none;"
          @mousemove="onMouseMove"
          @mouseup="onMouseUp"
          @mouseleave="onMouseUp"
          @click.self="selectedId = null"
        >
          <!-- Background + grid -->
          <rect width="900" height="520" fill="rgba(0,0,0,0.3)" rx="6"/>
          <defs>
            <pattern id="eg-grid" width="40" height="40" patternUnits="userSpaceOnUse">
              <path d="M 40 0 L 0 0 0 40" fill="none" stroke="rgba(255,255,255,0.05)" stroke-width="1"/>
            </pattern>
          </defs>
          <rect width="900" height="520" fill="url(#eg-grid)" rx="6"/>

          <g v-if="floor">

            <!-- Stage -->
            <rect
              :x="px(floor.stage.x1)" :y="py(floor.stage.y1)"
              :width="px(floor.stage.x2) - px(floor.stage.x1)"
              :height="py(floor.stage.y2) - py(floor.stage.y1)"
              fill="#f59e0b" rx="4"
              style="cursor:move;"
              @mousedown.prevent="startDrag($event, 'stage', 'stage')"
              @click.stop
            />
            <text
              :x="(px(floor.stage.x1) + px(floor.stage.x2)) / 2"
              :y="(py(floor.stage.y1) + py(floor.stage.y2)) / 2"
              text-anchor="middle" dominant-baseline="middle"
              font-size="11" font-weight="bold" fill="#1a1a1a"
              style="pointer-events:none;"
            >{{ $t('manage.event_form.step4.stage_screen') }}</text>

            <!-- Zones -->
            <g v-for="z in floor.zones" :key="z.id">
              <!-- Zone polygon — same style as SeatingMap (color always, no dimming) -->
              <polygon
                :points="`${px(z.x1)},${py(z.y1)} ${px(z.x2)},${py(z.y1)} ${px(z.x2)},${py(z.y2)} ${px(z.x1)},${py(z.y2)}`"
                :fill="z.color + '44'"
                :stroke="z.color"
                :stroke-width="selectedId === z.id ? 2.5 : 2"
                style="cursor:move;"
                @mousedown.prevent="startDrag($event, 'zone', z.id)"
                @click.stop
              />

              <!-- Seat circles — only when sitting zone has actual seat data (matches SeatingMap drawZoneSeats) -->
              <template v-if="z.type === 'sitting' && z.seats.length > 0">
                <circle
                  v-for="[seatId, pos] in getSeatGridPositions(z)"
                  :key="seatId"
                  :cx="px(pos.x)" :cy="py(pos.y)"
                  :r="computeSeatRadius(z, floor.seatSize)"
                  fill="#22c55e"
                  style="pointer-events:none;"
                />
              </template>

              <!-- Zone label — shown when standing OR sitting with no seats (matches SeatingMap drawZone logic) -->
              <template v-if="z.type === 'standing' || z.seats.length === 0">
                <text
                  :x="(px(z.x1) + px(z.x2)) / 2"
                  :y="(py(z.y1) + py(z.y2)) / 2"
                  text-anchor="middle" dominant-baseline="middle"
                  font-size="12" font-weight="bold" fill="#fff"
                  style="pointer-events:none; text-shadow: 0 1px 3px rgba(0,0,0,0.8);"
                >{{ z.name }}</text>
                <!-- [Standing] badge -->
                <text
                  v-if="z.type === 'standing'"
                  :x="(px(z.x1) + px(z.x2)) / 2"
                  :y="(py(z.y1) + py(z.y2)) / 2 + 18"
                  text-anchor="middle" dominant-baseline="middle"
                  font-size="10" :fill="z.color + 'cc'"
                  style="pointer-events:none;"
                >[ Standing ]</text>
                <!-- [Decorative] badge — editor-only indicator for unlinked zones -->
                <text
                  v-if="!z.linkedId"
                  :x="(px(z.x1) + px(z.x2)) / 2"
                  :y="z.type === 'standing' ? (py(z.y1) + py(z.y2)) / 2 + 32 : (py(z.y1) + py(z.y2)) / 2 + 18"
                  text-anchor="middle" dominant-baseline="middle"
                  font-size="10" :fill="z.color + 'aa'"
                  style="pointer-events:none;"
                >[ Decorative ]</text>
              </template>

              <!-- Corner resize handles (selected zone only) -->
              <template v-if="selectedId === z.id">
                <circle :cx="px(z.x1)" :cy="py(z.y1)" r="7" fill="#fff" stroke="#3b82f6" stroke-width="2" style="cursor:nw-resize;" @mousedown.prevent="startDrag($event, 'tl', z.id)" @click.stop/>
                <circle :cx="px(z.x2)" :cy="py(z.y1)" r="7" fill="#fff" stroke="#3b82f6" stroke-width="2" style="cursor:ne-resize;" @mousedown.prevent="startDrag($event, 'tr', z.id)" @click.stop/>
                <circle :cx="px(z.x1)" :cy="py(z.y2)" r="7" fill="#fff" stroke="#3b82f6" stroke-width="2" style="cursor:sw-resize;" @mousedown.prevent="startDrag($event, 'bl', z.id)" @click.stop/>
                <circle :cx="px(z.x2)" :cy="py(z.y2)" r="7" fill="#fff" stroke="#3b82f6" stroke-width="2" style="cursor:se-resize;" @mousedown.prevent="startDrag($event, 'br', z.id)" @click.stop/>
              </template>
            </g>

          </g>
        </svg>
      </div>

      <!-- Sidebar -->
      <div class="editor-sidebar flex-shrink-0" style="width: 220px;">

        <!-- Zone properties (zone selected) -->
        <div v-if="selected" class="card shadow-sm p-3">
          <div class="fw-semibold text-reactive-primary small mb-3">
            <i class="bi bi-sliders me-1 text-primary"/>{{ $t('manage.event_form.step4.editor_zone_props') }}
          </div>

          <div class="mb-2">
            <label class="form-label small text-reactive-secondary mb-1">{{ $t('manage.event_form.step3.zone_name') }}</label>
            <input v-model="selected.name" type="text" class="form-control form-control-sm" @input="commit"/>
          </div>

          <div class="mb-2">
            <label class="form-label small text-reactive-secondary mb-1">{{ $t('manage.event_form.step3.type') }}</label>
            <select v-model="selected.type" class="form-select form-select-sm" @change="commit">
              <option value="standing">{{ $t('manage.event_form.step3.standing') }}</option>
              <option value="sitting">{{ $t('manage.event_form.step3.seated') }}</option>
            </select>
          </div>

          <div class="mb-2">
            <label class="form-label small text-reactive-secondary mb-1">{{ $t('manage.event_form.step4.color') }}</label>
            <div class="d-flex flex-wrap gap-1 mt-1">
              <div
                v-for="c in PALETTE"
                :key="c"
                class="color-dot rounded-circle"
                :style="{
                  background: c,
                  outline: selected.color === c ? '2px solid #fff' : 'none',
                  'outline-offset': '2px',
                }"
                @click="selected.color = c; commit()"
              />
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label small text-reactive-secondary mb-1">{{ $t('manage.event_form.step4.editor_link_zone') }}</label>
            <select v-model="selected.linkedId" class="form-select form-select-sm" @change="commit">
              <option value="">{{ $t('manage.event_form.step4.editor_unlinked') }}</option>
              <option v-for="z in zones" :key="z.id" :value="z.id">{{ z.name }}</option>
            </select>
          </div>

          <button class="btn btn-sm btn-outline-danger w-100" @click="deleteSelected">
            <i class="bi bi-trash me-1"/>{{ $t('manage.event_form.step4.editor_delete_zone') }}
          </button>
        </div>

        <!-- No selection: hint + floor settings -->
        <template v-else>
          <div class="card shadow-sm p-3 text-center text-reactive-secondary small mb-3">
            <i class="bi bi-cursor d-block fs-3 mb-2 opacity-40"/>
            {{ $t('manage.event_form.step4.editor_click_hint') }}
          </div>

          <div v-if="floor" class="card shadow-sm p-3">
            <div class="fw-semibold text-reactive-primary small mb-3">
              <i class="bi bi-layers me-1 text-primary"/>{{ $t('manage.event_form.step4.editor_floor_settings') }}
            </div>
            <div class="mb-2">
              <label class="form-label small text-reactive-secondary mb-1">{{ $t('manage.event_form.step4.editor_floor_name') }}</label>
              <input v-model="floor.name" type="text" class="form-control form-control-sm" @input="commit"/>
            </div>
            <div>
              <label class="form-label small text-reactive-secondary mb-1">{{ $t('manage.event_form.step4.editor_seat_size') }}</label>
              <input v-model.number="floor.seatSize" type="number" min="8" max="32" class="form-control form-control-sm" @input="commit"/>
            </div>
          </div>
        </template>

      </div>
    </div>
  </div>
</template>

<style scoped>
.layout-editor { outline: none; }

.editor-svg {
  background: rgba(0, 0, 0, 0.2);
  border-radius: 6px;
}

.color-dot {
  width: 20px;
  height: 20px;
  cursor: pointer;
  flex-shrink: 0;
  transition: transform 0.1s;
}
.color-dot:hover { transform: scale(1.25); }

.remove-floor-x {
  font-size: 0.65em;
  line-height: 1;
  cursor: pointer;
}
.remove-floor-x:hover { opacity: 1 !important; }
</style>
