<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import type { Zone, ZoneFormState } from '../../(types)/zone'

const props = defineProps<{
  zone: Zone | null
}>()

const emit = defineEmits<{
  close: []
  saved: [data: ZoneFormState]
}>()

const { t: $t } = useI18n()

const form = reactive<ZoneFormState>({
  name:          '',
  isStanding:    false,
  capacity:      100,
  price:         0,
  purchaseLimit: null,
  descriptionVi: '',
  descriptionEn: '',
  giftImageUrl:  null,
  perks:         [],
  gridRows:      5,
  gridCols:      10,
})

const descLang = ref<'vi' | 'en'>('vi')
const newPerk  = ref('')
const saving   = ref(false)
const errors   = ref<Record<string, string>>({})

const totalSeats = computed(() => form.gridRows * form.gridCols)

function rowLabel(idx: number): string {
  if (idx < 26) return String.fromCharCode(65 + idx)
  return String.fromCharCode(65 + Math.floor(idx / 26) - 1) + String.fromCharCode(65 + idx % 26)
}

function parsedPerks(p?: string | string[]): string[] {
  if (!p) return []
  if (Array.isArray(p)) return p
  try { return JSON.parse(p) } catch { return [] }
}

function addPerk() {
  if (newPerk.value.trim()) {
    form.perks.push(newPerk.value.trim())
    newPerk.value = ''
  }
}

function validate(): boolean {
  errors.value = {}
  if (!form.name.trim())
    errors.value.name = 'Name is required'
  if (form.price == null || form.price < 0)
    errors.value.price = 'Price is required'
  if (form.isStanding && (!form.capacity || form.capacity < 1))
    errors.value.capacity = 'Capacity must be > 0'
  if (!form.isStanding) {
    if (!form.gridRows || form.gridRows < 1) errors.value.gridRows = 'At least 1 row required'
    if (!form.gridCols || form.gridCols < 1) errors.value.gridCols = 'At least 1 seat per row required'
  }
  return Object.keys(errors.value).length === 0
}

async function handleSave() {
  if (!validate()) return
  saving.value = true
  errors.value = {}

  try {
    // Mock delay — swap for real $fetch when API is ready
    await new Promise(r => setTimeout(r, 400))
    emit('saved', { ...form, perks: [...form.perks] })
  } catch (err: any) {
    errors.value.global = err?.data?.message ?? 'Failed to save zone'
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  if (props.zone) {
    const seatCount = props.zone.seatCount ?? 0
    Object.assign(form, {
      name:          props.zone.name,
      isStanding:    props.zone.isStanding,
      capacity:      props.zone.capacity,
      price:         props.zone.price,
      purchaseLimit: props.zone.purchaseLimit ?? null,
      descriptionVi: props.zone.descriptionVi ?? '',
      descriptionEn: props.zone.descriptionEn ?? '',
      giftImageUrl:  props.zone.giftImageUrl ?? null,
      perks:         parsedPerks(props.zone.perks),
      gridRows:      props.zone.gridRows ?? (seatCount > 0 ? Math.ceil(Math.sqrt(seatCount)) : 5),
      gridCols:      props.zone.gridCols ?? (seatCount > 0 ? Math.ceil(seatCount / Math.ceil(Math.sqrt(seatCount))) : 10),
    })
  }
})
</script>

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

        <!-- Type -->
        <div class="col-12">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.type') }}</label>
          <select v-model="form.isStanding" class="form-select bg-reactive-primary border-0 text-reactive-primary">
            <option :value="false">{{ $t('organizer.event_form.step3.seated') }}</option>
            <option :value="true">{{ $t('organizer.event_form.step3.standing') }}</option>
          </select>
        </div>

        <!-- Seated: grid config -->
        <template v-if="!form.isStanding">
          <div class="col-12">
            <div class="seat-grid-section rounded-2 p-3">
              <div class="fw-semibold text-reactive-primary small mb-3">
                <i class="bi bi-grid-3x3 me-2 text-primary"/>Seat Grid
              </div>
              <div class="row g-3">
                <div class="col-6">
                  <label class="form-label small text-reactive-secondary">Rows *</label>
                  <div class="d-flex align-items-center gap-2">
                    <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="form.gridRows = Math.max(1, form.gridRows - 1)">−</button>
                    <input v-model.number="form.gridRows" type="number" min="1" max="52" class="form-control bg-reactive-primary border-0 text-reactive-primary text-center" :class="{ 'is-invalid': errors.gridRows }"/>
                    <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="form.gridRows = Math.min(52, form.gridRows + 1)">+</button>
                  </div>
                  <div class="invalid-feedback">{{ errors.gridRows }}</div>
                </div>
                <div class="col-6">
                  <label class="form-label small text-reactive-secondary">Seats per row *</label>
                  <div class="d-flex align-items-center gap-2">
                    <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="form.gridCols = Math.max(1, form.gridCols - 1)">−</button>
                    <input v-model.number="form.gridCols" type="number" min="1" max="200" class="form-control bg-reactive-primary border-0 text-reactive-primary text-center" :class="{ 'is-invalid': errors.gridCols }"/>
                    <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="form.gridCols = Math.min(200, form.gridCols + 1)">+</button>
                  </div>
                  <div class="invalid-feedback">{{ errors.gridCols }}</div>
                </div>
              </div>

              <!-- Grid preview -->
              <div class="mt-3 p-2 rounded-2" style="background:rgba(0,0,0,0.15);">
                <div class="d-flex align-items-center justify-content-between mb-2">
                  <small class="text-reactive-secondary fw-semibold">Preview</small>
                  <span class="badge bg-primary">{{ totalSeats }} seats total</span>
                </div>
                <div class="grid-preview overflow-auto" style="max-height:140px;">
                  <div v-for="r in Math.min(form.gridRows, 8)" :key="r" class="d-flex align-items-center gap-1 mb-1">
                    <span class="row-label text-reactive-secondary" style="font-size:0.65rem;min-width:20px;text-align:right;">{{ rowLabel(r - 1) }}</span>
                    <div v-for="c in Math.min(form.gridCols, 30)" :key="c" class="seat-dot rounded-circle" style="width:10px;height:10px;flex-shrink:0;background:#6366f1;opacity:0.8;"/>
                    <span v-if="form.gridCols > 30" class="text-reactive-secondary" style="font-size:0.65rem;">+{{ form.gridCols - 30 }}</span>
                  </div>
                  <div v-if="form.gridRows > 8" class="text-reactive-secondary" style="font-size:0.7rem;">
                    +{{ form.gridRows - 8 }} more rows...
                  </div>
                </div>
              </div>

              <div v-if="zone?.id && (zone.seatCount ?? 0) > 0" class="alert alert-warning py-2 small mt-3 mb-0">
                <i class="bi bi-exclamation-triangle me-1"/>
                This zone already has {{ zone.seatCount }} seats. Saving new dimensions will replace them.
              </div>
              <div v-else class="alert alert-info py-2 small mt-3 mb-0">
                <i class="bi bi-info-circle me-1"/>Seats will be generated automatically when you save.
              </div>
            </div>
          </div>
        </template>

        <!-- Standing: manual capacity -->
        <template v-else>
          <div class="col-12">
            <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.capacity') }} *</label>
            <input v-model.number="form.capacity" type="number" min="1" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.capacity }"/>
            <div class="invalid-feedback">{{ errors.capacity }}</div>
          </div>
        </template>

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
          <label class="form-label small text-reactive-secondary mb-1">{{ $t('organizer.event_form.step3.description') }}</label>
          <ul class="nav nav-tabs mb-2 border-0">
            <li class="nav-item">
              <button type="button" class="nav-link px-3 py-1" :class="{ active: descLang === 'vi' }" @click="descLang = 'vi'">
                🇻🇳 {{ $t('organizer.event_form.step3.desc_vi') }}
              </button>
            </li>
            <li class="nav-item">
              <button type="button" class="nav-link px-3 py-1" :class="{ active: descLang === 'en' }" @click="descLang = 'en'">
                🇺🇸 {{ $t('organizer.event_form.step3.desc_en') }}
              </button>
            </li>
          </ul>
          <textarea v-if="descLang === 'vi'" v-model="form.descriptionVi" rows="2" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step3.desc_vi_placeholder')"/>
          <textarea v-else v-model="form.descriptionEn" rows="2" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step3.desc_en_placeholder')"/>
        </div>

        <!-- Gift Image URL -->
        <div class="col-12">
          <label class="form-label small text-reactive-secondary">Gift Image URL</label>
          <input
            v-model="form.giftImageUrl"
            type="url"
            class="form-control bg-reactive-primary border-0 text-reactive-primary"
            placeholder="https://..."
          />
          <div v-if="form.giftImageUrl" class="mt-2">
            <img :src="form.giftImageUrl" class="rounded" style="max-height:100px;object-fit:cover;width:100%;"/>
          </div>
        </div>

        <!-- Perks -->
        <div class="col-12">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.perks') }}</label>
          <div class="d-flex gap-2 mb-2">
            <input v-model="newPerk" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step3.perk_placeholder')" @keyup.enter="addPerk"/>
            <button type="button" class="btn btn-outline-primary btn-sm" @click="addPerk"><i class="bi bi-plus-lg"/></button>
          </div>
          <div class="d-flex flex-wrap gap-1">
            <span v-for="(perk, i) in form.perks" :key="i" class="badge d-inline-flex align-items-center gap-1 perk-badge">
              {{ perk }}<i class="bi bi-x" style="cursor:pointer" @click="form.perks.splice(i, 1)"/>
            </span>
          </div>
        </div>

      </div>

      <div v-if="errors.global" class="alert alert-danger mt-3 py-2">
        <i class="bi bi-exclamation-circle me-2"/>{{ errors.global }}
      </div>

      <div class="d-flex gap-2 justify-content-end mt-4">
        <button class="btn btn-outline-secondary" @click="$emit('close')">{{ $t('common.cancel') }}</button>
        <button class="btn btn-primary" :disabled="saving" @click="handleSave">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>{{ $t('common.save') }}
        </button>
      </div>

    </div>
  </div>
</template>

<style scoped>
.modal-backdrop-custom {
  position: fixed; inset: 0; background: rgba(0,0,0,0.55);
  z-index: 1050; display: flex; align-items: center;
  justify-content: center; padding: 1rem; overflow-y: auto;
}
.modal-box { max-width: 560px; width: 100%; max-height: 92vh; overflow-y: auto; }
.nav-link {
  color: var(--bs-secondary); background: none; border: none;
  border-bottom: 2px solid transparent; border-radius: 0;
  padding: 0.4rem 1rem; cursor: pointer;
}
.nav-link.active { color: var(--bs-primary); border-bottom-color: var(--bs-primary); }
.seat-grid-section {
  background: rgba(var(--bs-primary-rgb), 0.04);
  border: 1px solid rgba(var(--bs-primary-rgb), 0.15);
}
.seat-dot { display: inline-block; flex-shrink: 0; }
.row-label { font-family: monospace; flex-shrink: 0; }
.grid-preview { overflow-x: auto; }
.perk-badge {
  background: rgba(99,102,241,0.25);
  color: #a5b4fc;
  border: 1px solid rgba(99,102,241,0.4);
}
</style>