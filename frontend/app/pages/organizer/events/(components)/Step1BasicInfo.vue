<script setup lang="ts">
import { computed } from 'vue'
import type { EventFormState } from '../../(types)/event.type'
import { mockCategories } from '../../mock.data'

const props = defineProps<{
  form: EventFormState
  errors: Record<string, string>
  saving: boolean
}>()

const emit = defineEmits<{
  'update:form': [value: EventFormState]
  save: []
}>()

const form = computed({
  get: () => props.form,
  set: (val) => emit('update:form', val),
})

function toggleCategory(id: number) {
  const ids = [...form.value.categoryIds]
  const idx = ids.indexOf(id)
  if (idx >= 0) ids.splice(idx, 1)
  else ids.push(id)
  emit('update:form', { ...form.value, categoryIds: ids })
}
</script>

<template>
  <div>
    <div class="card bg-reactive-secondary border-0 p-4 mb-4">
      <h5 class="fw-semibold text-reactive-primary mb-4">
        <i class="bi bi-info-circle me-2 text-primary"/>{{ $t('organizer.event_form.step1.title') }}
      </h5>
      <div class="row g-4">

        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step1.name') }} <span class="text-danger">*</span>
          </label>
          <input
            v-model="form.name"
            type="text"
            class="form-control bg-reactive-primary border-0 text-reactive-primary"
            :class="{ 'is-invalid': errors.name }"
            :placeholder="$t('organizer.event_form.step1.name_placeholder')"
          />
          <div class="invalid-feedback">{{ errors.name }}</div>
        </div>

        <!-- Categories: multi-select via toggle badges -->
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step1.category') }} <span class="text-danger">*</span>
          </label>
          <div class="d-flex flex-wrap gap-2 mt-1" :class="{ 'is-invalid': errors.categoryIds }">
            <button
              v-for="cat in mockCategories"
              :key="cat.id"
              type="button"
              class="btn btn-sm"
              :class="form.categoryIds.includes(cat.id) ? 'btn-primary' : 'btn-outline-secondary'"
              @click="toggleCategory(cat.id)"
            >{{ cat.name }}</button>
          </div>
          <div v-if="errors.categoryIds" class="text-danger small mt-1">{{ errors.categoryIds }}</div>
        </div>

        <div class="col-md-6">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step1.status') }}
          </label>
          <select v-model="form.status" class="form-select bg-reactive-primary border-0 text-reactive-primary">
            <option value="EDITING">{{ $t('organizer.events.status.editing') }}</option>
            <option value="SCHEDULED">{{ $t('organizer.events.status.premier') }}</option>
            <option value="PREMIERE">{{ $t('organizer.events.status.premier') }}</option>
            <option value="SELLING">{{ $t('organizer.events.status.selling') }}</option>
            <option value="PAUSED">{{ $t('organizer.events.status.paused') }}</option>
            <option value="ONGOING">{{ $t('organizer.events.status.ongoing') }}</option>
            <option value="FINISHED">{{ $t('organizer.events.status.finished') }}</option>
            <option value="CANCELLED">{{ $t('organizer.events.status.cancelled') }}</option>
          </select>
        </div>

        <div class="col-12">
          <div class="alert alert-info py-2 small mb-0">
            <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step1.session_hint') }}
          </div>
        </div>

        <div class="col-md-6">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step1.start_date') }} <span class="text-danger">*</span>
          </label>
          <input
            v-model="form.startDate"
            type="datetime-local"
            class="form-control bg-reactive-primary border-0 text-reactive-primary"
            :class="{ 'is-invalid': errors.startDate }"
          />
          <div class="invalid-feedback">{{ errors.startDate }}</div>
        </div>

        <div class="col-md-6">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step1.end_date') }} <span class="text-danger">*</span>
          </label>
          <input
            v-model="form.endDate"
            type="datetime-local"
            class="form-control bg-reactive-primary border-0 text-reactive-primary"
            :class="{ 'is-invalid': errors.endDate }"
          />
          <div class="invalid-feedback">{{ errors.endDate }}</div>
        </div>

        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step1.address') }} <span class="text-danger">*</span>
          </label>
          <input
            v-model="form.addressLine"
            type="text"
            class="form-control bg-reactive-primary border-0 text-reactive-primary"
            :class="{ 'is-invalid': errors.addressLine }"
            :placeholder="$t('organizer.event_form.step1.address_placeholder')"
          />
          <div class="invalid-feedback">{{ errors.addressLine }}</div>
        </div>

        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.event_form.step1.banner_url') }}
          </label>
          <input
            v-model="form.bannerUrl"
            type="url"
            class="form-control bg-reactive-primary border-0 text-reactive-primary"
            :placeholder="$t('organizer.event_form.step1.banner_placeholder')"
          />
          <div v-if="form.bannerUrl" class="mt-2">
            <img :src="form.bannerUrl" class="rounded" style="max-height:160px;object-fit:cover;width:100%;"/>
          </div>
        </div>

      </div>
    </div>

    <div class="d-flex justify-content-end">
      <button class="btn btn-primary px-4" :disabled="saving" @click="$emit('save')">
        <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
        {{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/>
      </button>
    </div>
  </div>
</template>