<script setup lang="ts">
import type { Zone, ZoneFormState } from '../../(types)/zone'
import { formatPrice } from '../../mock.data'
import ZoneModal from './ZoneModal.vue'

const props = defineProps<{
  zones: Zone[]
}>()

const emit = defineEmits<{
  'update:zones': [value: Zone[]]
  next: []
  back: []
}>()

const showModal   = ref(false)
const editingZone = ref<Zone | null>(null)

function openZoneModal(zone: Zone | null) {
  editingZone.value = zone
  showModal.value   = true
}

function onZoneSaved(formData: ZoneFormState) {
  const capacity = formData.isStanding
    ? formData.capacity
    : formData.gridRows * formData.gridCols

  const current = [...props.zones]

  if (editingZone.value) {
    const idx = current.findIndex(z => z.id === editingZone.value!.id)
    if (idx >= 0) current[idx] = { ...current[idx], ...formData, capacity }
  } else {
    current.push({
      id:           'z-new-' + Date.now(),
      quantitySold: 0,
      ...formData,
      capacity,
    })
  }

  emit('update:zones', current)
  showModal.value = false
}

function deleteZone(id: string) {
  emit('update:zones', props.zones.filter(z => z.id !== id))
}
</script>

<template>
  <div>
    <div class="card bg-reactive-primary border-0 p-4 mb-4">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-0">
          <i class="bi bi-grid me-2 text-primary"/>{{ $t('organizer.event_form.step3.title') }}
        </h5>
        <button class="btn btn-sm btn-primary" @click="openZoneModal(null)">
          <i class="bi bi-plus-lg me-1"/>{{ $t('organizer.event_form.step3.add_zone') }}
        </button>
      </div>

      <div v-if="zones.length === 0" class="text-center py-5 text-reactive-secondary">
        <i class="bi bi-grid fs-1 d-block mb-3"/>
        <p>{{ $t('organizer.event_form.step3.empty') }}</p>
      </div>

      <div v-else class="row g-3">
        <div v-for="zone in zones" :key="zone.id" class="col-md-6 col-xl-4">
          <div class="zone-card card border-0 p-3 h-100">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div>
                <div class="fw-semibold text-reactive-primary">{{ zone.name }}</div>
                <small class="text-reactive-secondary">
                  <i :class="zone.isStanding ? 'bi-people' : 'bi-chair'" class="bi me-1"/>
                  {{ zone.isStanding ? $t('organizer.event_form.step3.standing') : $t('organizer.event_form.step3.seated') }}
                </small>
              </div>
              <div class="d-flex gap-1">
                <button class="btn btn-sm btn-outline-primary" @click="openZoneModal(zone)">
                  <i class="bi bi-pencil"/>
                </button>
                <button class="btn btn-sm btn-outline-danger" @click="deleteZone(zone.id)">
                  <i class="bi bi-trash"/>
                </button>
              </div>
            </div>
            <div class="d-flex justify-content-between small text-reactive-secondary">
              <span><i class="bi bi-people me-1"/>{{ zone.capacity }}</span>
              <span class="fw-semibold text-reactive-primary">{{ formatPrice(zone.price) }}</span>
            </div>
            <div v-if="zone.purchaseLimit" class="small text-reactive-secondary mt-1">
              <i class="bi bi-ticket me-1"/>Max {{ zone.purchaseLimit }} / {{ $t('common.ticket') }}
            </div>
            <div v-if="zone.perks?.length" class="mt-2 d-flex flex-wrap gap-1">
              <span v-for="perk in zone.perks" :key="perk" class="badge small perk-badge">{{ perk }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="d-flex justify-content-between gap-2">
      <button class="btn btn-outline-secondary px-4" @click="$emit('back')">
        <i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}
      </button>
      <button class="btn btn-primary px-4" @click="$emit('next')">
        {{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/>
      </button>
    </div>

    <ZoneModal
      v-if="showModal"
      :zone="editingZone"
      @close="showModal = false"
      @saved="onZoneSaved"
    />
  </div>
</template>

<style scoped>
.zone-card {
  background: rgba(var(--bs-secondary-rgb), 0.15);
  border-left: 4px solid #6366f1 !important;
  transition: box-shadow 0.15s;
}
.zone-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.12); }
.perk-badge {
  background: rgba(99,102,241,0.25);
  color: #a5b4fc;
  border: 1px solid rgba(99,102,241,0.4);
}
</style>