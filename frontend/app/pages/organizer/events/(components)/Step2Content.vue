<script setup lang="ts">
import { ref, computed } from 'vue'
import type { EventContentState } from '../../(types)/event'
import RichTextEditor from './RichTextEditor.vue'

const props = defineProps<{
  content: EventContentState
  saving: boolean
}>()

const emit = defineEmits<{
  'update:content': [value: EventContentState]
  save: []
  back: []
}>()

const content = computed({
  get: function () { return props.content },
  set: function (val) { emit('update:content', val) },
})

const contentLang = ref<'vi' | 'en'>('vi')

function setAboutVi(val: string) {
  content.value = { ...content.value, aboutVi: val }
}

function setAboutEn(val: string) {
  content.value = { ...content.value, aboutEn: val }
}

function setTerms(val: string) {
  content.value = { ...content.value, termsAndConditions: val }
}

function setRefund(val: string) {
  content.value = { ...content.value, policyRefund: val }
}
</script>

<template>
  <div>
    <div class="card bg-reactive-secondary border-0 p-4 mb-4">
      <h5 class="fw-semibold text-reactive-primary mb-4">
        <i class="bi bi-file-text me-2 text-primary"/>{{ $t('organizer.event_form.step2.title') }}
      </h5>

      <div class="row g-4">

        <!-- About (bilingual) -->
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary mb-2">
            {{ $t('organizer.event_form.step2.about') }}
          </label>

          <ul class="nav nav-tabs mb-3 border-0">
            <li class="nav-item">
              <button type="button" class="nav-link px-3" :class="{ active: contentLang === 'vi' }" @click="contentLang = 'vi'">
                🇻🇳 Tiếng Việt
              </button>
            </li>
            <li class="nav-item">
              <button type="button" class="nav-link px-3" :class="{ active: contentLang === 'en' }" @click="contentLang = 'en'">
                🇺🇸 English
              </button>
            </li>
          </ul>

          <RichTextEditor
            v-if="contentLang === 'vi'"
            :model-value="content.aboutVi"
            :placeholder="$t('organizer.event_form.step2.about_vi_placeholder')"
            :min-height="220"
            @update:model-value="setAboutVi"
          />
          <RichTextEditor
            v-else
            :model-value="content.aboutEn"
            :placeholder="$t('organizer.event_form.step2.about_en_placeholder')"
            :min-height="220"
            @update:model-value="setAboutEn"
          />
        </div>

        <!-- Terms & Conditions -->
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary mb-2">
            {{ $t('organizer.event_form.step2.terms') }}
          </label>
          <RichTextEditor
            :model-value="content.termsAndConditions"
            :placeholder="$t('organizer.event_form.step2.terms_placeholder')"
            :min-height="160"
            @update:model-value="setTerms"
          />
        </div>

        <!-- Refund Policy -->
        <div class="col-12">
          <label class="form-label small fw-semibold text-reactive-secondary mb-2">
            {{ $t('organizer.event_form.step2.refund') }}
          </label>
          <RichTextEditor
            :model-value="content.policyRefund"
            :placeholder="$t('organizer.event_form.step2.refund_placeholder')"
            :min-height="140"
            @update:model-value="setRefund"
          />
        </div>

      </div>
    </div>

    <div class="d-flex justify-content-between gap-2">
      <button class="btn btn-outline-secondary px-4" @click="$emit('back')">
        <i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}
      </button>
      <button class="btn btn-primary px-4" :disabled="saving" @click="$emit('save')">
        <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
        {{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/>
      </button>
    </div>
  </div>
</template>

<style scoped>
.nav-link {
  color: var(--bs-secondary);
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  border-radius: 0;
  padding: 0.5rem 1rem;
  cursor: pointer;
}
.nav-link.active {
  color: var(--bs-primary);
  border-bottom-color: var(--bs-primary);
}
</style>
