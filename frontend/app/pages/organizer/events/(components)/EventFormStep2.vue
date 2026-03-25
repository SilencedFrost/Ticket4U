<template>
  <div>
    <div class="card bg-reactive-secondary border-0 p-4 mb-4">
      <h5 class="fw-semibold text-reactive-primary mb-4">
        <i class="bi bi-file-text me-2 text-primary"/>{{ $t('organizer.event_form.step2.title') }}
      </h5>
      <div class="row g-4">
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.about') }}</label>
          <ul class="nav nav-tabs mb-3 border-0">
            <li class="nav-item"><button type="button" class="nav-link px-3" :class="{ active: contentLang === 'vi' }" @click="$emit('update:contentLang', 'vi')">🇻🇳 Tiếng Việt</button></li>
            <li class="nav-item"><button type="button" class="nav-link px-3" :class="{ active: contentLang === 'en' }" @click="$emit('update:contentLang', 'en')">🇺🇸 English</button></li>
          </ul>
          <ClientOnly>
            <RichTextEditor v-if="contentLang === 'vi'" v-model="content.aboutVi" :placeholder="$t('organizer.event_form.step2.about_vi_placeholder')" :min-height="240"/>
            <RichTextEditor v-else v-model="content.aboutEn" :placeholder="$t('organizer.event_form.step2.about_en_placeholder')" :min-height="240"/>
            <template #fallback><div class="form-control bg-reactive-primary border-0" style="min-height:240px;"/></template>
          </ClientOnly>
        </div>
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.terms') }}</label>
          <ClientOnly>
            <RichTextEditor v-model="content.termsAndConditions" :placeholder="$t('organizer.event_form.step2.terms_placeholder')" :min-height="180"/>
            <template #fallback><div class="form-control bg-reactive-primary border-0" style="min-height:180px;"/></template>
          </ClientOnly>
        </div>
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.refund') }}</label>
          <ClientOnly>
            <RichTextEditor v-model="content.policyRefund" :placeholder="$t('organizer.event_form.step2.refund_placeholder')" :min-height="180"/>
            <template #fallback><div class="form-control bg-reactive-primary border-0" style="min-height:180px;"/></template>
          </ClientOnly>
        </div>
      </div>
    </div>
    <div class="d-flex justify-content-between gap-2">
      <button class="btn btn-outline-secondary px-4" @click="$emit('back')"><i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}</button>
      <button class="btn btn-primary px-4" :disabled="saving" @click="$emit('save')">
        <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
        {{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import RichTextEditor from './RichTextEditor.vue'
import type { EventContent } from '../composables/use-event-form'

defineProps<{
  content:     EventContent
  contentLang: 'vi' | 'en'
  saving:      boolean
}>()

defineEmits<{
  (e: 'save'): void
  (e: 'back'): void
  (e: 'update:contentLang', val: 'vi' | 'en'): void
}>()
</script>

<style scoped>
.nav-link { color: var(--bs-secondary); background: none; border: none; border-bottom: 2px solid transparent; border-radius: 0; padding: 0.5rem 1rem; cursor: pointer; }
.nav-link.active { color: var(--bs-primary); border-bottom-color: var(--bs-primary); }
</style>