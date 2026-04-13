<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getStatusI18nKey } from '../mock.data'
import { useEventForm } from '../composables/use-event-form'
import Step1BasicInfo from '~/pages/manage/event/(components)/Step1BasicInfo.vue'
import Step2Content   from '~/pages/manage/event/(components)/Step2Content.vue'
import Step3Zones     from '~/pages/manage/event/(components)/Step3Zones.vue'
import Step4Layout    from '~/pages/manage/event/(components)/Step4Layout.vue'
import type { Zone }    from '../(types)/zone'
import type { Session } from '../(types)/session'

const { t: $t } = useI18n()
const localePath  = useLocalePath()
const route       = useRoute()

const {
  form, content, errors, saving, globalError, showDraftSaved, currentStep,
  loadEvent, saveStep1, saveStep2, saveLayout,
} = useEventForm()

const eventId = computed(() => {
  const id = route.params.id as string
  return id === 'new' ? null : id
})
const isNew = computed(() => !eventId.value)

// Sessions loaded from event — zones live on sessions[0]
const sessions = ref<Session[]>([])

// Convenience: zones of the first session (the only one in mock)
const zones = computed({
  get(): Zone[] {
    return sessions.value[0]?.zones ?? []
  },
  set(val: Zone[]) {
    if (sessions.value[0]) {
      sessions.value[0] = { ...sessions.value[0], zones: val }
    }
  },
})

const steps = computed(() => [
  $t('organizer.event_form.step1.title'),
  $t('organizer.event_form.step2.title'),
  $t('organizer.event_form.step3.title'),
  $t('organizer.event_form.step4.title'),
])

const statusBadgeClass = computed(() => ({
  'bg-secondary':         form.value.status === 'EDITING',
  'bg-primary':           form.value.status === 'PREMIERE' || form.value.status === 'SCHEDULED',
  'bg-success':           form.value.status === 'SELLING',
  'bg-warning text-dark': form.value.status === 'PAUSED' || form.value.status === 'ONGOING',
  'bg-danger':            form.value.status === 'CANCELLED' || form.value.status === 'FINISHED',
}))

function goToStep(i: number) {
  if (!isNew.value || i <= currentStep.value) currentStep.value = i
}

async function onSaveStep1() {
  const ok = await saveStep1()
  if (ok) currentStep.value = 1
}

async function onSaveStep2() {
  await saveStep2()
  currentStep.value = 2
}

async function onSaveLayout() {
  await saveLayout()
}

onMounted(() => {
  if (!isNew.value && eventId.value) {
    sessions.value = loadEvent(eventId.value)
  }
})
</script>

<template>
  <div class="p-4 p-md-5">

    <!-- Header -->
    <div class="d-flex align-items-center gap-3 mb-4">
      <NuxtLink :to="localePath('/manage/dashboard/events')" class="btn btn-sm btn-outline-secondary">
        <i class="bi bi-arrow-left"/>
      </NuxtLink>
      <div>
        <h2 class="fw-bold text-reactive-primary mb-0">
          {{ isNew ? $t('organizer.event_form.create_title') : $t('organizer.event_form.edit_title') }}
        </h2>
        <small v-if="!isNew" class="text-reactive-secondary">ID: {{ eventId }}</small>
      </div>
      <div v-if="!isNew" class="ms-auto d-flex align-items-center gap-2">
        <span class="badge" :class="statusBadgeClass">{{ $t(getStatusI18nKey(form.status)) }}</span>
        <Transition name="draft-toast">
          <span v-if="showDraftSaved" class="badge bg-success d-flex align-items-center gap-1">
            <i class="bi bi-check2"/>{{ $t('organizer.event_form.draft_saved') }}
          </span>
        </Transition>
      </div>
    </div>

    <!-- Step Indicators -->
    <div class="d-flex gap-2 mb-5 step-bar">
      <div
        v-for="(step, i) in steps" :key="i"
        class="step-item d-flex align-items-center gap-2 flex-grow-1"
        :class="{ completed: i < currentStep, active: i === currentStep }"
        @click="goToStep(i)"
      >
        <div class="step-dot d-flex align-items-center justify-content-center rounded-circle flex-shrink-0">
          <i v-if="i < currentStep" class="bi bi-check2"/>
          <span v-else>{{ i + 1 }}</span>
        </div>
        <span class="step-label d-none d-md-block small fw-semibold">{{ step }}</span>
        <div v-if="i < steps.length - 1" class="step-line flex-grow-1"/>
      </div>
    </div>

    <!-- Steps -->
    <Step1BasicInfo
      v-if="currentStep === 0"
      v-model:form="form"
      :errors="errors"
      :saving="saving"
      @save="onSaveStep1"
    />

    <Step2Content
      v-if="currentStep === 1"
      v-model:content="content"
      :saving="saving"
      @save="onSaveStep2"
      @back="currentStep = 0"
    />

    <Step3Zones
      v-if="currentStep === 2"
      v-model:zones="zones"
      @next="currentStep = 3"
      @back="currentStep = 1"
    />

    <Step4Layout
      v-if="currentStep === 3"
      v-model:form="form"
      :zones="zones"
      :saving="saving"
      @save="onSaveLayout"
      @back="currentStep = 2"
    />

    <!-- Error toast -->
    <div v-if="globalError" class="alert alert-danger position-fixed bottom-0 end-0 m-4" style="z-index:2000;max-width:360px;">
      <i class="bi bi-exclamation-circle me-2"/>{{ globalError }}
      <button class="btn-close float-end" @click="globalError = ''"/>
    </div>

  </div>
</template>

<style scoped>
.step-bar { align-items: center; }
.step-item { cursor: pointer; min-width: 0; }
.step-dot {
  width: 32px; height: 32px; font-size: 0.8rem; font-weight: 700; flex-shrink: 0;
  background: rgba(var(--bs-secondary-rgb), 0.3); color: var(--bs-secondary);
  transition: background 0.2s, color 0.2s;
}
.step-item.active .step-dot    { background: var(--bs-primary); color: #fff; }
.step-item.completed .step-dot { background: #22c55e; color: #fff; }
.step-label { color: var(--bs-secondary); transition: color 0.2s; }
.step-item.active .step-label,
.step-item.completed .step-label { color: var(--text-reactive-primary, inherit); }
.step-line { height: 2px; background: rgba(var(--bs-secondary-rgb), 0.25); flex-shrink: 0; min-width: 8px; }
.step-item.completed .step-line { background: #22c55e; }
</style>