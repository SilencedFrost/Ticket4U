<template>
  <div class="p-4 p-md-5">

    <!-- Header -->
    <div class="d-flex align-items-center gap-3 mb-4">
      <NuxtLink :to="localePath('/organizer/events')" class="btn btn-sm btn-outline-secondary">
        <i class="bi bi-arrow-left"/>
      </NuxtLink>
      <div>
        <h2 class="fw-bold text-reactive-primary mb-0">
          {{ isNew ? $t('organizer.event_form.create_title') : $t('organizer.event_form.edit_title') }}
        </h2>
        <small class="text-reactive-secondary" v-if="eventId">ID: {{ eventId }}</small>
      </div>
      <div v-if="savedEventId" class="ms-auto d-flex align-items-center gap-2">
        <span class="badge" :class="{
          'bg-secondary':         form.status === 'EDITING',
          'bg-primary':           form.status === 'PREMIERE' || form.status === 'SCHEDULED',
          'bg-success':           form.status === 'SELLING',
          'bg-warning text-dark': form.status === 'PAUSED' || form.status === 'ONGOING',
          'bg-danger':            form.status === 'CANCELLED' || form.status === 'FINISHED',
        }">{{ form.status }}</span>
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
        <span class="step-label d-none d-md-block small fw-semibold">{{ step.label }}</span>
        <div v-if="i < steps.length - 1" class="step-line flex-grow-1"/>
      </div>
    </div>

    <!-- Steps -->
    <EventFormStep1
        v-if="currentStep === 0"
        :form="form" :errors="errors" :categories="categories" :venues="venues" :saving="saving"
        @save="onStep1Saved"
    />
    <EventFormStep2
        v-if="currentStep === 1"
        :content="content" :content-lang="contentLang" :saving="saving"
        @update:content-lang="contentLang = $event"
        @back="currentStep = 0"
        @save="onStep2Saved"
    />
    <EventFormStep3
        v-if="currentStep === 2"
        :zones="zones" :saved-session-id="savedSessionId"
        :show-zone-modal="showZoneModal" :editing-zone="editingZone"
        :delete-zone-target="deleteZoneTarget" :zone-saving="zoneSaving"
        :api-url="config.public.apiUrl"
        @back="currentStep = 1"
        @next="currentStep = 3"
        @open-zone-modal="openZoneModal"
        @zone-saved="onZoneSaved"
        @confirm-delete="confirmDeleteZone"
        @delete-zone="() => doDeleteZone(savedSessionId!)"
        @close-modal="showZoneModal = false"
        @close-delete="deleteZoneTarget = null"
    />
    <EventFormStep4
        v-if="currentStep === 3"
        :layout-editor="layoutEditor"
        :zones="zones"
        :saved-event-id="savedEventId"
        :saved-session-id="savedSessionId"
        :form="form"
        @back="currentStep = 2"
        @global-error="globalError = $event"
    />

    <!-- Error toast -->
    <div v-if="globalError" class="alert alert-danger position-fixed bottom-0 end-0 m-4" style="z-index:2000;max-width:360px;">
      <i class="bi bi-exclamation-circle me-2"/>{{ globalError }}
      <button class="btn-close float-end" @click="globalError = ''"/>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useEventForm }     from './composables/use-event-form'
import { useZones }         from './composables/use-zones'
import { useLayoutEditor }  from './composables/use-layout-editor'
import EventFormStep1 from './(components)/EventFormStep1.vue'
import EventFormStep2 from './(components)/EventFormStep2.vue'
import EventFormStep3 from './(components)/EventFormStep3.vue'
import EventFormStep4 from './(components)/EventFormStep4.vue'

definePageMeta({ layout: 'organizer', middleware: 'organizer' })

const { t }         = useI18n()
const $t            = t
const localePath    = useLocalePath()
const route         = useRoute()
const router        = useRouter()
const config        = useRuntimeConfig()

// ── Route ──────────────────────────────────────────────────
const eventId      = computed(() => { const id = route.params.id as string; return id === 'new' ? null : id })
const isNew        = computed(() => !eventId.value)
const savedEventId = ref<string | null>(eventId.value)
const savedSessionId = ref<string | null>(null)

// ── Steps ──────────────────────────────────────────────────
const currentStep = ref(0)
const steps = computed(() => [
  { label: $t('organizer.event_form.step1.title') },
  { label: $t('organizer.event_form.step2.title') },
  { label: $t('organizer.event_form.step3.title') },
  { label: $t('organizer.event_form.step4.title') },
])
const goToStep = (i: number) => { if (savedEventId.value || i <= currentStep.value) currentStep.value = i }

// ── Shared state ───────────────────────────────────────────
const globalError    = ref('')
const showDraftSaved = ref(false)
let draftSavedTimer: ReturnType<typeof setTimeout> | null = null

const triggerDraftSaved = () => {
  showDraftSaved.value = true
  if (draftSavedTimer) clearTimeout(draftSavedTimer)
  draftSavedTimer = setTimeout(() => { showDraftSaved.value = false }, 2500)
}

// ── Composables ────────────────────────────────────────────
const {
  form, content, contentLang, errors, saving,
  categories, venues,
  fetchCategories, fetchVenues,
  saveStep1, saveStep2,
} = useEventForm(config.public.apiUrl)

const {
  zones, showZoneModal, editingZone, deleteZoneTarget, zoneSaving,
  fetchZones, openZoneModal, onZoneSaved, confirmDeleteZone, doDeleteZone,
} = useZones(config.public.apiUrl)

const layoutEditor = useLayoutEditor(config.public.apiUrl)

// ── Step handlers ──────────────────────────────────────────
const onStep1Saved = async () => {
  try {
    const result = await saveStep1(savedEventId.value, savedSessionId.value)
    if (!result) return
    if (!savedEventId.value) {
      savedEventId.value   = result.eventId
      savedSessionId.value = result.sessionId
      history.replaceState({}, '', localePath(`/organizer/events/${result.eventId}`))
    }
    triggerDraftSaved()
    currentStep.value = 1
  } catch (err: any) { globalError.value = err.message }
}

const onStep2Saved = async () => {
  if (!savedEventId.value) return
  try {
    await saveStep2(savedEventId.value)
    triggerDraftSaved()
    currentStep.value = 2
  } catch (err: any) { globalError.value = err.message }
}

// ── Load existing event ────────────────────────────────────
const loadEvent = async () => {
  if (!savedEventId.value) return
  try {
    const [event, eventSessions, layoutData] = await Promise.all([
      $fetch<any>(`${config.public.apiUrl}/events/${savedEventId.value}`, { credentials: 'include' }),
      $fetch<any[]>(`${config.public.apiUrl}/events/${savedEventId.value}/sessions`, { credentials: 'include' }).catch(() => []),
      $fetch<any>(`${config.public.apiUrl}/events/${savedEventId.value}/layout`, { credentials: 'include' }).catch(() => null),
    ])
    form.value = {
      name: event.name ?? '', venueId: event.venueId ?? '', categoryId: event.categoryId ?? '',
      addressLine: event.addressLine ?? '', bannerUrl: event.bannerUrl ?? '',
      status: event.status ?? 'EDITING',
      startDate: eventSessions[0]?.startDate ? eventSessions[0].startDate.slice(0, 16) : '',
      endDate:   eventSessions[0]?.endDate   ? eventSessions[0].endDate.slice(0, 16)   : '',
    }
    content.value = {
      aboutVi: event.aboutVi ?? '', aboutEn: event.aboutEn ?? '',
      termsAndConditions: event.termsAndConditions ?? '', policyRefund: event.policyRefund ?? '',
    }
    if (eventSessions.length > 0) {
      savedSessionId.value = eventSessions[0].id
      zones.value = await $fetch<any[]>(`${config.public.apiUrl}/events/sessions/${savedSessionId.value}/zones`, { credentials: 'include' }).catch(() => [])
    }

    const eventLayout = event.layout ?? event.eventLayout ?? null
    let venueMarker: any = null
    if (eventLayout) { try { const p = JSON.parse(eventLayout); if (p.venueMode === true) venueMarker = p } catch {} }

    const isVenueLayout  = venueMarker !== null || (!eventLayout && event.venueId != null)
    const isCustomLayout = !isVenueLayout && eventLayout != null
    const rawLayout      = isCustomLayout ? eventLayout : (layoutData?.layout ?? layoutData?.eventLayout ?? null)

    if (isCustomLayout && rawLayout) {
      try {
        const parsed = JSON.parse(rawLayout)
        if (parsed.floors && Array.isArray(parsed.floors)) {
          await layoutEditor.reloadSavedLayout(savedEventId.value)
          // Resolve zone IDs
          layoutEditor.layoutFloors.value.forEach(floor => {
            floor.canvasShapes.forEach(shape => {
              if (!shape.zoneId) return
              const byId = zones.value.find((z: any) => z.id === shape.zoneId)
              if (byId) return
              const byName = zones.value.find((z: any) => z.name === shape.label)
              if (byName?.id) shape.zoneId = byName.id
              else shape.zoneId = undefined
            })
          })
          nextTick(() => layoutEditor.layoutFloors.value.forEach((_, fi) => layoutEditor.drawFloor(fi)))
        }
      } catch {}
    } else if (isVenueLayout) {
      layoutEditor.layoutMode.value  = 'venue'
      layoutEditor.layoutSaved.value = true
      if (venueMarker?.zoneLinks) layoutEditor.venueZoneLinks.value = { ...venueMarker.zoneLinks }
    } else {
      layoutEditor.layoutMode.value  = 'custom'
      layoutEditor.layoutSaved.value = false
    }
  } catch { globalError.value = 'Failed to load event data' }
}

// ── Lifecycle ──────────────────────────────────────────────
onMounted(async () => {
  await Promise.all([fetchCategories(), fetchVenues()])
  if (!isNew.value) await loadEvent()
  if (layoutEditor.layoutFloors.value.length === 0) layoutEditor.layoutFloors.value.push(layoutEditor.makeFloor(1))
})

watch(currentStep, async (step) => {
  if (step === 2 && savedSessionId.value) await fetchZones(savedSessionId.value)
  if (step === 3 && savedSessionId.value) {
    await fetchZones(savedSessionId.value)
    await layoutEditor.loadSeatsForStep4(savedSessionId.value, zones.value)
    await nextTick()
    setTimeout(() => {
      layoutEditor.layoutFloors.value.forEach((floor, fi) => {
        const el = layoutEditor.canvasContainerRefs[fi]
        if (el && floor.stageSize.width === 0)
          floor.stageSize = { width: el.clientWidth, height: el.clientHeight || 520 }
      })
      layoutEditor.layoutFloors.value.forEach((_, fi) => layoutEditor.drawFloor(fi))
      layoutEditor.rebuildAndDraw()
    }, 50)
  }
})
</script>

<style scoped>
.step-bar { align-items: center; }
.step-item { cursor: pointer; min-width: 0; }
.step-dot { width: 32px; height: 32px; font-size: 0.8rem; font-weight: 700; flex-shrink: 0; background: rgba(var(--bs-secondary-rgb), 0.3); color: var(--bs-secondary); transition: background 0.2s, color 0.2s; }
.step-item.active .step-dot    { background: var(--bs-primary); color: #fff; }
.step-item.completed .step-dot { background: #22c55e; color: #fff; }
.step-label { color: var(--bs-secondary); transition: color 0.2s; }
.step-item.active .step-label, .step-item.completed .step-label { color: var(--text-reactive-primary, inherit); }
.step-line { height: 2px; background: rgba(var(--bs-secondary-rgb), 0.25); flex-shrink: 0; min-width: 8px; }
.step-item.completed .step-line { background: #22c55e; }
.draft-toast-enter-active { transition: opacity 0.3s ease; }
.draft-toast-leave-active { transition: opacity 0.8s ease; }
.draft-toast-enter-from, .draft-toast-leave-to { opacity: 0; }
</style>