<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { mockVenues, getVenueZoneNames } from '../../mock.data'
import LayoutPreview from './LayoutPreview.vue'
import LayoutEditor  from './LayoutEditor.vue'
import type { Zone } from '../../(types)/zone'
import type { EventFormState } from '../../(types)/event'
import type { VenueLayout, VenueLayoutZone, VenueLayoutFloor } from '../../(types)/venue'

const props = defineProps<{
  form: EventFormState
  zones: Zone[]
  saving: boolean
}>()

const emit = defineEmits<{
  'update:form': [value: EventFormState]
  save: []
  back: []
}>()

const form = computed({
  get: () => props.form,
  set: (val) => emit('update:form', val),
})

const layoutMode = ref<'venue' | 'custom'>('venue')

const selectedVenue = computed(() =>
  mockVenues.find(v => v.id === form.value.venueId) ?? null
)

const seatedZones = computed(() =>
  props.zones.filter(z => !z.isStanding)
)

const standingZones = computed(() =>
  props.zones.filter(z => z.isStanding)
)

// ── Zone mapping: venue zone name → event zone id ('' = decorative) ──
const zoneMapping = ref<Record<string, string>>({})

// Flag to suppress the sync-back watch while we're loading from saved layout
let _loading = false

/** Populate zoneMapping (and layoutMode) from a saved layout JSON string */
function applyLayoutToMapping(layoutJson: string | null | undefined) {
  if (!layoutJson) return
  try {
    const parsed = JSON.parse(layoutJson)
    if (parsed.venueMode && parsed.zoneLinks) {
      layoutMode.value = 'venue'
      _loading = true
      zoneMapping.value = { ...parsed.zoneLinks }
      _loading = false
    } else if (parsed.floors) {
      layoutMode.value = 'custom'
    }
  } catch { /* ignore malformed */ }
}

// Initialise layoutMode (and zoneMapping) whenever the layout prop changes
watch(() => form.value.layout, applyLayoutToMapping, { immediate: true })

// Reset mapping when venue changes (user picks a different venue)
watch(() => form.value.venueId, (newId, oldId) => {
  if (newId !== oldId) zoneMapping.value = {}
})

// Sync zoneMapping changes → form.layout (venue-mode only)
watch(zoneMapping, (newMapping) => {
  if (_loading || layoutMode.value !== 'venue' || !form.value.venueId) return
  const newLayout = {
    venueId:   form.value.venueId,
    venueMode: true,
    zoneLinks: { ...newMapping },
  }
  form.value = { ...form.value, layout: JSON.stringify(newLayout) }
}, { deep: true })

// Derived layout with accessibility driven by zoneMapping
const mappedLayout = computed<VenueLayout | null>(() => {
  const layout = selectedVenue.value?.layout
  if (!layout) return null

  const applyMapping = (zones: VenueLayoutZone[]): VenueLayoutZone[] =>
    zones.map(z => ({
      ...z,
      accessible: !!zoneMapping.value[z.zone_name],
    }))

  if (layout.floors?.length) {
    return {
      ...layout,
      floors: layout.floors.map((f: VenueLayoutFloor) => ({
        ...f,
        zones: applyMapping(f.zones ?? []),
      })),
    }
  }
  return { ...layout, zones: applyMapping(layout.zones ?? []) }
})

/** Display name for a linked event zone id */
function linkedZoneName(eventZoneId: string): string {
  if (!eventZoneId) return ''
  return props.zones.find(z => z.id === eventZoneId)?.name ?? eventZoneId
}

// ── Custom layout ─────────────────────────────────────────────

/** Parsed custom layout from form.layout (floors-based, no venueMode flag) */
const customLayout = computed<VenueLayout | null>(() => {
  if (!form.value.layout) return null
  try {
    const parsed = JSON.parse(form.value.layout)
    if (parsed.floors && !parsed.venueMode) return parsed as VenueLayout
  } catch { /* ignore */ }
  return null
})

const LOCKED_STATUSES = ['SELLING', 'ONGOING', 'FINISHED'] as const
const isReadonly = computed(() => LOCKED_STATUSES.includes(props.form.status as any))


function deleteCustomLayout() {
  form.value = { ...form.value, layout: null }
}

const customLayoutJson = computed({
  get: () => form.value.layout,
  set: (v: string | null) => { form.value = { ...form.value, layout: v } },
})
</script>

<template>
  <div>
    <div v-if="isReadonly" class="alert alert-warning d-flex align-items-start gap-2 mb-4">
      <i class="bi bi-lock-fill flex-shrink-0 mt-1"/>
      <span>{{ $t('manage.event_form.step4.readonly_warning') }}</span>
    </div>

    <div v-if="zones.length === 0" class="alert alert-warning d-flex align-items-center gap-2 mb-4">
      <i class="bi bi-exclamation-triangle-fill"/>
      <span>{{ $t('manage.event_form.step4.no_zones_warning') }}</span>
    </div>

    <div class="card shadow-sm p-4 mb-4">
      <h5 class="fw-semibold text-reactive-primary mb-3">
        <i class="bi bi-layers me-2 text-primary"/>{{ $t('manage.event_form.step4.title') }}
      </h5>

      <!-- Mode toggle -->
      <div class="btn-group mb-4">
        <button
          class="btn"
          :class="layoutMode === 'venue' ? 'btn-primary' : 'btn-outline-secondary'"
          :disabled="isReadonly"
          @click="layoutMode = 'venue'"
        >
          <i class="bi bi-building me-2"/>{{ $t('manage.event_form.step4.venue_mode') }}
        </button>
        <button
          class="btn"
          :class="layoutMode === 'custom' ? 'btn-primary' : 'btn-outline-secondary'"
          :disabled="isReadonly"
          @click="layoutMode = 'custom'"
        >
          <i class="bi bi-pencil-square me-2"/>{{ $t('manage.event_form.step4.custom_mode') }}
        </button>
      </div>

      <!-- ── VENUE MODE ── -->
      <div v-if="layoutMode === 'venue'">

        <div class="mb-4">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('manage.event_form.step4.venue') }} <span class="text-danger">*</span>
          </label>
          <select v-model="form.venueId" class="form-select" :disabled="isReadonly">
            <option value="">— {{ $t('manage.event_form.step4.venue') }} —</option>
            <option v-for="v in mockVenues" :key="v.id" :value="v.id">{{ v.name }} — {{ v.addressLine }}</option>
          </select>
          <div v-if="selectedVenue" class="mt-2 d-flex align-items-center gap-2">
            <img :src="selectedVenue.imageUrl" class="rounded" style="width:48px;height:32px;object-fit:cover;" :alt="selectedVenue.name"/>
            <small class="text-reactive-secondary"><i class="bi bi-geo-alt me-1"/>{{ selectedVenue.addressLine }}</small>
          </div>
        </div>

        <div v-if="selectedVenue" class="row g-4">

          <!-- Layout preview -->
          <div class="col-lg-7">
            <div class="fw-semibold text-reactive-primary mb-2">{{ selectedVenue.name }}</div>
            <LayoutPreview v-if="mappedLayout" :layout="mappedLayout" />
            <div v-else class="layout-preview-placeholder bg-reactive-primary rounded d-flex align-items-center justify-content-center" style="height:340px;">
              <div class="text-center text-reactive-secondary">
                <i class="bi bi-grid-3x3 fs-1 d-block mb-3 opacity-25"/>
                <small class="opacity-50">{{ $t('manage.event_form.step4.venue_no_layout') }}</small>
              </div>
            </div>
          </div>

          <!-- Zone linking -->
          <div class="col-lg-5">
            <div class="fw-semibold text-reactive-primary mb-3">
              <i class="bi bi-link-45deg me-2 text-primary"/>{{ $t('manage.event_form.step4.link_zones') }}
            </div>
            <div v-if="zones.length === 0" class="alert alert-warning py-2 small">
              <i class="bi bi-exclamation-triangle me-1"/>{{ $t('manage.event_form.step4.no_zones_link') }}
            </div>
            <div v-else>
              <p class="small text-reactive-secondary mb-3">{{ $t('manage.event_form.step4.link_description') }}</p>
              <div
                v-for="vz in getVenueZoneNames(selectedVenue)" :key="vz"
                class="zone-link-row rounded p-2 mb-2"
                :class="zoneMapping[vz] ? 'linked' : 'unlinked'"
              >
                <div class="d-flex align-items-center gap-2">
                  <!-- Venue zone pill -->
                  <span class="badge venue-zone-pill text-truncate flex-shrink-0" style="max-width:130px;" :title="vz">{{ vz }}</span>
                  <!-- Arrow + linked badge or "decorative" label -->
                  <i class="bi bi-arrow-right text-reactive-secondary flex-shrink-0"/>
                  <span v-if="zoneMapping[vz]" class="badge bg-primary text-white text-truncate flex-shrink-0" style="max-width:130px;" :title="linkedZoneName(zoneMapping[vz])">
                    <i class="bi bi-check2 me-1"/>{{ linkedZoneName(zoneMapping[vz]) }}
                  </span>
                  <span v-else class="text-reactive-secondary small fst-italic flex-shrink-0">{{ $t('manage.event_form.step4.decorative') }}</span>
                </div>
                <!-- Select (always visible so user can change) -->
                <select v-model="zoneMapping[vz]" class="form-select form-select-sm mt-2" :disabled="isReadonly">
                  <option value="">— {{ $t('manage.event_form.step4.decorative') }} —</option>
                  <optgroup :label="$t('manage.event_form.step4.seated_zones')">
                    <option v-for="z in seatedZones" :key="z.id" :value="z.id">{{ z.name }}</option>
                  </optgroup>
                  <optgroup :label="$t('manage.event_form.step4.standing_zones')">
                    <option v-for="z in standingZones" :key="z.id" :value="z.id">{{ z.name }}</option>
                  </optgroup>
                </select>
              </div>
              <div class="alert alert-info py-2 small mt-3 mb-0">
                <i class="bi bi-info-circle me-1"/>{{ $t('manage.event_form.step4.link_hint') }}
              </div>
            </div>
          </div>

        </div>

        <div v-else class="alert alert-warning py-2 small">
          <i class="bi bi-exclamation-triangle me-1"/>{{ $t('manage.event_form.step4.no_venue') }}
        </div>

      </div>

      <!-- ── CUSTOM MODE ── -->
      <div v-else>

        <!-- Read-only: show preview only -->
        <template v-if="isReadonly">
          <LayoutPreview v-if="customLayout" :layout="customLayout" />
          <div v-else class="alert alert-warning py-2 small">
            <i class="bi bi-exclamation-triangle me-1"/>{{ $t('manage.event_form.step4.no_venue') }}
          </div>
        </template>

        <!-- Editable: live editor -->
        <template v-else>
          <div v-if="customLayout" class="d-flex align-items-center gap-2 mb-3">
            <span class="fw-semibold text-reactive-primary small">
              <i class="bi bi-grid-3x3 me-2 text-primary"/>{{ $t('manage.event_form.step4.custom_layout') }}
            </span>
            <button class="btn btn-sm btn-outline-danger ms-auto" @click="deleteCustomLayout">
              <i class="bi bi-trash me-1"/>{{ $t('manage.event_form.step4.delete_layout') }}
            </button>
          </div>
          <LayoutEditor v-model="customLayoutJson" :zones="zones" />
        </template>

      </div>

    </div>

    <div class="d-flex justify-content-between gap-2 mt-2">
      <button class="btn btn-outline-secondary px-4" @click="$emit('back')">
        <i class="bi bi-arrow-left me-1"/>{{ $t('manage.event_form.back') }}
      </button>
      <div class="d-flex gap-2">
        <button class="btn btn-primary px-4" :disabled="saving || isReadonly" @click="$emit('save')">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          <i v-else class="bi bi-floppy me-2"/>{{ $t('manage.event_form.step4.save_layout') }}
        </button>
        <NuxtLink :to="useLocalePath()('/manage/dashboard/events')" class="btn btn-success px-4">
          <i class="bi bi-check2 me-2"/>{{ $t('manage.event_form.step4.save_close') }}
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Zone link rows */
.zone-link-row {
  border: 1px solid rgba(var(--bs-secondary-rgb), 0.2);
  transition: border-color 0.15s, background 0.15s;
}
.zone-link-row.linked {
  border-color: rgba(var(--bs-primary-rgb), 0.35);
  background: rgba(var(--bs-primary-rgb), 0.04);
}
.zone-link-row.unlinked {
  background: transparent;
}

/* Venue zone pill — solid so white text is legible in both modes */
.venue-zone-pill {
  background: rgba(var(--bs-secondary-rgb), 0.55);
  color: #fff;
  font-size: 0.78rem;
}
</style>