<template>
  <div class="modal-backdrop-custom" @click.self="$emit('close')">
    <div class="modal-box bg-reactive-secondary p-4 rounded-3 shadow-lg">

      <h5 class="text-reactive-primary fw-bold mb-4">
        {{ zone?.id ? $t('organizer.event_form.step3.edit_zone') : $t('organizer.event_form.step3.add_zone') }}
      </h5>

      <div class="row g-3">

        <!-- Name -->
        <div class="col-12">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.zone_name') }} *</label>
          <input v-model="form.name" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.name }"/>
          <div class="invalid-feedback">{{ errors.name }}</div>
        </div>

        <!-- Type + Capacity -->
        <div class="col-md-6">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.type') }}</label>
          <select v-model="form.isStanding" class="form-select bg-reactive-primary border-0 text-reactive-primary">
            <option :value="false">{{ $t('organizer.event_form.step3.seated') }}</option>
            <option :value="true">{{ $t('organizer.event_form.step3.standing') }}</option>
          </select>
        </div>
        <div class="col-md-6">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.capacity') }} *</label>
          <input v-model.number="form.capacity" type="number" min="1" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.capacity }"/>
          <div class="invalid-feedback">{{ errors.capacity }}</div>
        </div>

        <!-- Price + Purchase limit -->
        <div class="col-md-6">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.price') }} *</label>
          <div class="input-group">
            <input v-model.number="form.price" type="number" min="0" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.price }"/>
            <span class="input-group-text bg-reactive-primary border-0 text-reactive-secondary">₫</span>
          </div>
          <div class="invalid-feedback">{{ errors.price }}</div>
        </div>
        <div class="col-md-6">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.purchase_limit') }}</label>
          <input v-model.number="form.purchaseLimit" type="number" min="1" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
        </div>

        <!-- Description bilingual -->
        <div class="col-12">
          <label class="form-label small text-reactive-secondary mb-1">Description</label>
          <ul class="nav nav-tabs mb-2 border-0">
            <li class="nav-item"><button type="button" class="nav-link px-3 py-1" :class="{ active: descLang === 'vi' }" @click="descLang = 'vi'">🇻🇳 VI</button></li>
            <li class="nav-item"><button type="button" class="nav-link px-3 py-1" :class="{ active: descLang === 'en' }" @click="descLang = 'en'">🇺🇸 EN</button></li>
          </ul>
          <textarea v-if="descLang === 'vi'" v-model="form.descriptionVi" rows="2" class="form-control bg-reactive-primary border-0 text-reactive-primary" placeholder="Mô tả vị trí..."/>
          <textarea v-else v-model="form.descriptionEn" rows="2" class="form-control bg-reactive-primary border-0 text-reactive-primary" placeholder="Zone description..."/>
        </div>

        <!-- Perks -->
        <div class="col-12">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.perks') }}</label>
          <div class="d-flex gap-2 mb-2">
            <input v-model="newPerk" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step3.perk_placeholder')" @keyup.enter="addPerk"/>
            <button type="button" class="btn btn-outline-primary btn-sm" @click="addPerk"><i class="bi bi-plus-lg"/></button>
          </div>
          <div class="d-flex flex-wrap gap-1">
            <span v-for="(perk, i) in form.perks" :key="i" class="badge bg-primary bg-opacity-15 text-primary d-inline-flex align-items-center gap-1">
              {{ perk }}<i class="bi bi-x" style="cursor:pointer" @click="form.perks.splice(i, 1)"/>
            </span>
          </div>
        </div>

        <!-- Seated zone: seat grid builder -->
        <div v-if="!form.isStanding" class="col-12">
          <div class="seat-section rounded-2 p-3">
            <div class="d-flex align-items-center justify-content-between mb-3">
              <div class="fw-semibold text-reactive-primary small">
                <i class="bi bi-grid-3x3 me-2 text-primary"/>Seat Configuration
              </div>
              <div class="d-flex align-items-center gap-2">
                <span v-if="zone?.id && hasSavedSeats" class="badge bg-success bg-opacity-20 text-success small">
                  <i class="bi bi-check2 me-1"/>{{ savedSeatCount }} seats saved
                </span>
                <button v-if="zone?.id" type="button" class="btn btn-sm btn-outline-danger" :disabled="clearing" @click="clearSeats">
                  <span v-if="clearing" class="spinner-border spinner-border-sm me-1"/>
                  <i v-else class="bi bi-trash me-1"/>Clear Seats
                </button>
              </div>
            </div>

            <SeatGridBuilder
              v-model="seatGrid"
              :zone-price="form.price || 0"
              zone-color="#6366f1"
            />

            <div class="alert alert-info py-2 small mt-3 mb-0">
              <i class="bi bi-info-circle me-1"/>
              Seats will be generated when you save. You can fine-tune individual seat prices in Step 4.
            </div>
          </div>
        </div>

      </div>

      <!-- Error -->
      <div v-if="errors.global" class="alert alert-danger mt-3 py-2">
        <i class="bi bi-exclamation-circle me-2"/>{{ errors.global }}
      </div>

      <!-- Actions -->
      <div class="d-flex gap-2 justify-content-end mt-4">
        <button class="btn btn-outline-secondary" @click="$emit('close')">{{ $t('common.action.cancel') }}</button>
        <button class="btn btn-primary" :disabled="saving" @click="handleSave">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>{{ $t('common.action.save') }}
        </button>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import SeatGridBuilder, { type SeatGridConfig } from './SeatGridBuilder.vue'

interface Zone {
  id?: string; name: string; isStanding: boolean; capacity: number; price: number
  purchaseLimit?: number | null; descriptionVi?: string; descriptionEn?: string
  perks?: string | string[]; seatCount?: number
}

const props = defineProps<{
  zone: Zone | null
  sessionId: string
  apiUrl: string
}>()

const emit = defineEmits<{
  close: []
  saved: [zone: Zone]
}>()

const { t: $t } = useI18n()

const form = reactive({
  name: '',
  isStanding: false,
  capacity: 100,
  price: 0,
  purchaseLimit: null as number | null,
  descriptionVi: '',
  descriptionEn: '',
  perks: [] as string[],
})

const seatGrid = ref<SeatGridConfig>({
  seatShape: 'circle',
  seatSize: 28,
  rows: [],
})

const descLang       = ref<'vi' | 'en'>('vi')
const newPerk        = ref('')
const saving         = ref(false)
const clearing       = ref(false)
const hasSavedSeats  = ref(false)
const savedSeatCount = ref(0)
const errors         = ref<Record<string, string>>({})

onMounted(async () => {
  if (props.zone) {
    Object.assign(form, {
      name:          props.zone.name,
      isStanding:    props.zone.isStanding,
      capacity:      props.zone.capacity,
      price:         props.zone.price,
      purchaseLimit: props.zone.purchaseLimit ?? null,
      descriptionVi: props.zone.descriptionVi ?? '',
      descriptionEn: props.zone.descriptionEn ?? '',
      perks:         parsedPerks(props.zone.perks),
    })
    if (props.zone.id && !props.zone.isStanding) {
      savedSeatCount.value = props.zone.seatCount ?? 0
      hasSavedSeats.value  = savedSeatCount.value > 0
    }
  }
})

const parsedPerks = (p?: string | string[]): string[] => {
  if (!p) return []
  if (Array.isArray(p)) return p
  try { return JSON.parse(p) } catch { return [] }
}

const addPerk = () => {
  if (newPerk.value.trim()) { form.perks.push(newPerk.value.trim()); newPerk.value = '' }
}

const validate = () => {
  errors.value = {}
  if (!form.name.trim())                   errors.value.name     = 'Name is required'
  if (!form.capacity || form.capacity < 1) errors.value.capacity = 'Capacity must be > 0'
  if (form.price == null || form.price < 0) errors.value.price   = 'Price is required'
  if (!form.isStanding && !hasSavedSeats.value && seatGrid.value.rows.length === 0)
    errors.value.global = 'Please add at least one seat row for a seated zone'
  return Object.keys(errors.value).length === 0
}

const handleSave = async () => {
  if (!validate()) return
  saving.value = true; errors.value = {}
  try {
    const payload = {
      name:          form.name,
      isStanding:    form.isStanding,
      capacity:      form.isStanding ? form.capacity : seatGrid.value.rows.reduce((s, r) => s + r.count, 0),
      price:         form.price,
      purchaseLimit: form.purchaseLimit,
      descriptionVi: form.descriptionVi || null,
      descriptionEn: form.descriptionEn || null,
      perks:         form.perks.length > 0 ? JSON.stringify(form.perks) : null,
    }

    let savedZone: Zone
    if (props.zone?.id) {
      savedZone = await $fetch<Zone>(
        `${props.apiUrl}/organizer/sessions/${props.sessionId}/zones/${props.zone.id}`,
        { method: 'PUT', body: payload, credentials: 'include' }
      )
    } else {
      savedZone = await $fetch<Zone>(
        `${props.apiUrl}/organizer/sessions/${props.sessionId}/zones`,
        { method: 'POST', body: payload, credentials: 'include' }
      )
    }

    // Generate seats for seated zones
    if (!form.isStanding && seatGrid.value.rows.length > 0) {
      await $fetch(
        `${props.apiUrl}/organizer/sessions/${props.sessionId}/zones/${savedZone.id}/seats/generate`,
        {
          method: 'POST',
          body: { rows: seatGrid.value.rows.map(r => ({ prefix: r.prefix, count: r.count, priceOverride: r.priceOverride || null })) },
          credentials: 'include',
        }
      )
    }

    emit('saved', savedZone)
  } catch (err: any) {
    errors.value.global = err?.data?.message ?? 'Failed to save zone'
  } finally {
    saving.value = false
  }
}

const clearSeats = async () => {
  if (!props.zone?.id) return
  clearing.value = true
  try {
    await $fetch(
      `${props.apiUrl}/organizer/sessions/${props.sessionId}/zones/${props.zone.id}/seats`,
      { method: 'DELETE', credentials: 'include' }
    )
    hasSavedSeats.value = false; savedSeatCount.value = 0; seatGrid.value.rows = []
  } catch (err: any) {
    errors.value.global = err?.data?.message ?? 'Failed to clear seats'
  } finally {
    clearing.value = false
  }
}
</script>

<style scoped>
.modal-backdrop-custom {
  position: fixed; inset: 0; background: rgba(0,0,0,0.55);
  z-index: 1050; display: flex; align-items: center;
  justify-content: center; padding: 1rem; overflow-y: auto;
}
.modal-box { max-width: 640px; width: 100%; max-height: 92vh; overflow-y: auto; }
.nav-link {
  color: var(--bs-secondary); background: none; border: none;
  border-bottom: 2px solid transparent; border-radius: 0; padding: 0.4rem 1rem; cursor: pointer;
}
.nav-link.active { color: var(--bs-primary); border-bottom-color: var(--bs-primary); }
.seat-section { background: rgba(var(--bs-primary-rgb), 0.04); border: 1px solid rgba(var(--bs-primary-rgb), 0.15); }
</style>