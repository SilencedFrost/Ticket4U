<template>
  <div>
    <div class="card bg-reactive-secondary border-0 p-4 mb-4">
      <div class="d-flex align-items-center justify-content-between mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-0"><i class="bi bi-grid me-2 text-primary"/>{{ $t('organizer.event_form.step3.title') }}</h5>
        <button class="btn btn-sm btn-primary" @click="$emit('openZoneModal', null)"><i class="bi bi-plus-lg me-1"/>{{ $t('organizer.event_form.step3.add_zone') }}</button>
      </div>
      <div v-if="!savedSessionId" class="alert alert-warning py-2 small mb-4">
        <i class="bi bi-exclamation-triangle me-1"/>Session not yet created. Please complete Step 1 first.
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
                <button class="btn btn-sm btn-outline-primary" @click="$emit('openZoneModal', zone)"><i class="bi bi-pencil"/></button>
                <button class="btn btn-sm btn-outline-danger" @click="$emit('confirmDelete', zone)"><i class="bi bi-trash"/></button>
              </div>
            </div>
            <div class="d-flex justify-content-between small text-reactive-secondary">
              <span><i class="bi bi-people me-1"/>{{ zone.capacity }}</span>
              <span class="fw-semibold text-reactive-primary">{{ formatPrice(zone.price) }}</span>
            </div>
            <div v-if="zone.purchaseLimit" class="small text-reactive-secondary mt-1">
              <i class="bi bi-ticket me-1"/>Max {{ zone.purchaseLimit }} / person
            </div>
            <div v-if="zone.perks && parsedPerks(zone.perks).length" class="mt-2 d-flex flex-wrap gap-1">
              <span v-for="perk in parsedPerks(zone.perks)" :key="perk" class="badge small" style="background:rgba(99,102,241,0.25);color:#a5b4fc;border:1px solid rgba(99,102,241,0.4);">{{ perk }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="d-flex justify-content-between gap-2">
      <button class="btn btn-outline-secondary px-4" @click="$emit('back')"><i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}</button>
      <button class="btn btn-primary px-4" @click="$emit('next')">{{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/></button>
    </div>

    <!-- Zone Modal -->
    <ZoneModal v-if="showZoneModal" :zone="editingZone" :session-id="savedSessionId!" :api-url="apiUrl" @close="$emit('closeModal')" @saved="$emit('zoneSaved', $event)"/>

    <!-- Delete Confirm -->
    <div v-if="deleteZoneTarget" class="modal-backdrop-custom" @click.self="$emit('closeDelete')">
      <div class="modal-box bg-reactive-secondary p-4 rounded-3 shadow-lg" style="max-width:420px;">
        <h5 class="text-reactive-primary fw-bold mb-2"><i class="bi bi-exclamation-triangle text-danger me-2"/>{{ $t('organizer.event_form.step3.delete_title') }}</h5>
        <p class="text-reactive-secondary">{{ $t('organizer.event_form.step3.delete_confirm', { name: deleteZoneTarget.name }) }}</p>
        <div class="d-flex gap-2 justify-content-end">
          <button class="btn btn-outline-secondary" @click="$emit('closeDelete')">{{ $t('common.cancel') }}</button>
          <button class="btn btn-danger" :disabled="zoneSaving" @click="$emit('deleteZone')">
            <span v-if="zoneSaving" class="spinner-border spinner-border-sm me-2"/>{{ $t('common.delete') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ZoneModal from './ZoneModal.vue'
import type { Zone } from '../composables/use-zones'

defineProps<{
  zones:            Zone[]
  savedSessionId:   string | null
  showZoneModal:    boolean
  editingZone:      Zone | null
  deleteZoneTarget: Zone | null
  zoneSaving:       boolean
  apiUrl:           string
}>()

defineEmits<{
  (e: 'back'): void
  (e: 'next'): void
  (e: 'openZoneModal', zone: Zone | null): void
  (e: 'zoneSaved', zone: Zone): void
  (e: 'confirmDelete', zone: Zone): void
  (e: 'deleteZone'): void
  (e: 'closeModal'): void
  (e: 'closeDelete'): void
}>()

const parsedPerks = (perks: string | string[] | undefined): string[] => {
  if (!perks) return []
  if (Array.isArray(perks)) return perks
  try { return JSON.parse(perks) } catch { return [] }
}

const formatPrice = (p: number) => p === 0 ? 'Free' : new Intl.NumberFormat('vi-VN').format(p) + ' ₫'
</script>

<style scoped>
.zone-card { background: rgba(var(--bs-secondary-rgb), 0.15); border-left: 4px solid #6366f1; transition: box-shadow 0.15s; }
.zone-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.12); }
.modal-backdrop-custom { position: fixed; inset: 0; background: rgba(0,0,0,0.55); z-index: 1050; display: flex; align-items: center; justify-content: center; padding: 1rem; }
.modal-box { max-width: 420px; width: 100%; }
</style>