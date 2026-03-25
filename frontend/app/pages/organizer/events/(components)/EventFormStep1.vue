<template>
  <div>
    <div class="card bg-reactive-secondary border-0 p-4 mb-4">
      <h5 class="fw-semibold text-reactive-primary mb-4">
        <i class="bi bi-info-circle me-2 text-primary"/>{{ $t('organizer.event_form.step1.title') }}
      </h5>
      <div class="row g-4">
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.name') }} <span class="text-danger">*</span></label>
          <input v-model="form.name" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.name }" :placeholder="$t('organizer.event_form.step1.name_placeholder')"/>
          <div class="invalid-feedback">{{ errors.name }}</div>
        </div>
        <div class="col-md-6">
          <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.category') }} <span class="text-danger">*</span></label>
          <select v-model="form.categoryId" class="form-select bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.categoryId }">
            <option value="">{{ $t('organizer.event_form.step1.category_placeholder') }}</option>
            <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
          </select>
          <div class="invalid-feedback">{{ errors.categoryId }}</div>
        </div>
        <div class="col-md-6">
          <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.status') }}</label>
          <select v-model="form.status" class="form-select bg-reactive-primary border-0 text-reactive-primary">
            <option value="EDITING">{{ $t('organizer.events.status.editing') }}</option>
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
          <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.start_date') }} <span class="text-danger">*</span></label>
          <input v-model="form.startDate" type="datetime-local" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.startDate }"/>
          <div class="invalid-feedback">{{ errors.startDate }}</div>
        </div>
        <div class="col-md-6">
          <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.end_date') }} <span class="text-danger">*</span></label>
          <input v-model="form.endDate" type="datetime-local" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.endDate }"/>
          <div class="invalid-feedback">{{ errors.endDate }}</div>
        </div>
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.address') }} <span class="text-danger">*</span></label>
          <input v-model="form.addressLine" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.addressLine }" :placeholder="$t('organizer.event_form.step1.address_placeholder')"/>
          <div class="invalid-feedback">{{ errors.addressLine }}</div>
        </div>
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.banner_url') }}</label>
          <input v-model="form.bannerUrl" type="url" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step1.banner_placeholder')"/>
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

<script setup lang="ts">
import type { EventFormData, Category, Venue } from '../composables/use-event-form'

defineProps<{
  form:       EventFormData
  errors:     Record<string, string>
  categories: Category[]
  venues:     Venue[]
  saving:     boolean
}>()

defineEmits<{ (e: 'save'): void }>()
</script>