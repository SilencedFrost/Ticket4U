<script setup lang="ts">
import { ref, computed } from 'vue'
import { mockVenues, getVenueZoneNames } from '../../mock.data'
import type { Zone } from '../../(types)/zone'
import type { EventFormState } from '../../(types)/event'

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
</script>

<template>
  <div>
    <div v-if="zones.length === 0" class="alert alert-warning d-flex align-items-center gap-2 mb-4">
      <i class="bi bi-exclamation-triangle-fill"/>
      <span>{{ $t('organizer.event_form.step4.no_zones_warning') }}</span>
    </div>

    <div class="card bg-reactive-secondary border-0 p-4 mb-4">
      <h5 class="fw-semibold text-reactive-primary mb-3">
        <i class="bi bi-layers me-2 text-primary"/>{{ $t('organizer.event_form.step4.title') }}
      </h5>

      <!-- Mode toggle -->
      <div class="btn-group mb-4">
        <button
          class="btn"
          :class="layoutMode === 'venue' ? 'btn-primary' : 'btn-outline-secondary'"
          @click="layoutMode = 'venue'"
        >
          <i class="bi bi-building me-2"/>{{ $t('organizer.event_form.step4.venue_mode') }}
        </button>
        <button
          class="btn"
          :class="layoutMode === 'custom' ? 'btn-primary' : 'btn-outline-secondary'"
          @click="layoutMode = 'custom'"
        >
          <i class="bi bi-pencil-square me-2"/>{{ $t('organizer.event_form.step4.custom_mode') }}
        </button>
      </div>

      <!-- ── VENUE MODE ── -->
      <div v-if="layoutMode === 'venue'">

        <div class="mb-4">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step4.venue') }} <span class="text-danger">*</span>
          </label>
          <select v-model="form.venueId" class="form-select bg-reactive-primary border-0 text-reactive-primary">
            <option value="">— {{ $t('organizer.event_form.step4.venue') }} —</option>
            <option v-for="v in mockVenues" :key="v.id" :value="v.id">{{ v.name }} — {{ v.addressLine }}</option>
          </select>
          <div v-if="selectedVenue" class="mt-2 d-flex align-items-center gap-2">
            <img :src="selectedVenue.imageUrl" class="rounded" style="width:48px;height:32px;object-fit:cover;" :alt="selectedVenue.name"/>
            <small class="text-reactive-secondary"><i class="bi bi-geo-alt me-1"/>{{ selectedVenue.addressLine }}</small>
          </div>
        </div>

        <div v-if="selectedVenue" class="row g-4">

          <!-- Layout preview placeholder -->
          <div class="col-lg-7">
            <div class="fw-semibold text-reactive-primary mb-2">{{ selectedVenue.name }}</div>
            <div class="layout-preview-placeholder bg-reactive-primary rounded d-flex align-items-center justify-content-center" style="height:340px;">
              <div class="text-center text-reactive-secondary">
                <i class="bi bi-grid-3x3 fs-1 d-block mb-3 opacity-25"/>
                <small class="opacity-50">{{ $t('organizer.event_form.step4.venue_no_layout') }}</small>
              </div>
            </div>
          </div>

          <!-- Zone linking -->
          <div class="col-lg-5">
            <div class="fw-semibold text-reactive-primary mb-3">
              <i class="bi bi-link-45deg me-2 text-primary"/>{{ $t('organizer.event_form.step4.link_zones') }}
            </div>
            <div v-if="zones.length === 0" class="alert alert-warning py-2 small">
              <i class="bi bi-exclamation-triangle me-1"/>{{ $t('organizer.event_form.step4.no_zones_link') }}
            </div>
            <div v-else>
              <p class="small text-reactive-secondary mb-3">{{ $t('organizer.event_form.step4.link_description') }}</p>
              <div v-for="vz in getVenueZoneNames(selectedVenue)" :key="vz" class="d-flex align-items-center gap-2 mb-2">
                <small class="text-reactive-primary fw-semibold text-truncate" style="min-width:130px;">{{ vz }}</small>
                <i class="bi bi-arrow-right text-reactive-secondary flex-shrink-0"/>
                <select class="form-select form-select-sm bg-reactive-primary border-0 text-reactive-primary flex-grow-1">
                  <option value="">{{ $t('organizer.event_form.step4.decorative') }}</option>
                  <optgroup :label="$t('organizer.event_form.step4.seated_zones')">
                    <option v-for="z in seatedZones" :key="z.id" :value="z.id">{{ z.name }}</option>
                  </optgroup>
                  <optgroup :label="$t('organizer.event_form.step4.standing_zones')">
                    <option v-for="z in standingZones" :key="z.id" :value="z.id">{{ z.name }}</option>
                  </optgroup>
                </select>
              </div>
              <div class="alert alert-info py-2 small mt-3 mb-0">
                <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step4.link_hint') }}
              </div>
            </div>
          </div>

        </div>

        <div v-else class="alert alert-warning py-2 small">
          <i class="bi bi-exclamation-triangle me-1"/>{{ $t('organizer.event_form.step4.no_venue') }}
        </div>

      </div>

      <!-- ── CUSTOM MODE ── -->
      <div v-else>
        <div class="d-flex align-items-center gap-2 mb-3">
          <button class="btn btn-sm btn-primary">Floor 1</button>
          <button class="btn btn-sm btn-outline-primary">
            <i class="bi bi-plus-lg me-1"/>{{ $t('organizer.event_form.step4.add_floor') }}
          </button>
        </div>

        <div
          class="canvas-placeholder bg-reactive-primary rounded position-relative overflow-hidden"
          style="height:600px; background: repeating-linear-gradient(0deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px), repeating-linear-gradient(90deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px);"
        >
          <div class="position-absolute start-50 translate-middle-x bg-warning rounded d-flex align-items-center justify-content-center" style="top:20px;width:260px;height:44px;">
            <small class="fw-bold text-dark">{{ $t('organizer.event_form.step4.stage_screen') }}</small>
          </div>
          <div v-if="zones.length > 0" class="position-absolute bottom-0 start-0 end-0 p-3 d-flex gap-2 flex-wrap" style="background:rgba(0,0,0,0.3);">
            <small class="text-reactive-secondary me-1 align-self-center">{{ $t('organizer.event_form.step4.place_zone') }}</small>
            <span v-for="zone in zones" :key="zone.id" class="badge zone-palette-badge">{{ zone.name }}</span>
          </div>
          <div class="position-absolute top-50 start-50 translate-middle text-center text-reactive-secondary" style="pointer-events:none;">
            <i class="bi bi-pencil-square fs-1 d-block mb-2 opacity-25"/>
            <small class="opacity-50">{{ $t('organizer.event_form.step4.hint') }}</small>
          </div>
        </div>

        <div class="p-3 border-top border-secondary">
          <small class="text-reactive-secondary">
            <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step4.hint') }}
          </small>
        </div>
      </div>

    </div>

    <div class="d-flex justify-content-between gap-2 mt-2">
      <button class="btn btn-outline-secondary px-4" @click="$emit('back')">
        <i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}
      </button>
      <div class="d-flex gap-2">
        <button class="btn btn-primary px-4" :disabled="saving" @click="$emit('save')">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          <i v-else class="bi bi-floppy me-2"/>{{ $t('organizer.event_form.step4.save_layout') }}
        </button>
        <NuxtLink :to="useLocalePath()('/organizer/events')" class="btn btn-success px-4">
          <i class="bi bi-check2 me-2"/>{{ $t('organizer.event_form.step4.save_close') }}
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
.layout-preview-placeholder {
  border: 1px dashed rgba(var(--bs-secondary-rgb), 0.3);
}
.zone-palette-badge {
  background: transparent;
  border: 1px solid rgba(var(--bs-secondary-rgb), 0.4);
  border-radius: 6px;
  padding: 4px 10px;
  font-size: 0.8rem;
  color: var(--bs-secondary);
  cursor: default;
}
</style>