// TODO: refactor to features/organizer/
import { ref } from 'vue'
import { mockEvents } from '../mock.data'
import type { EventFormState, EventContentState, EventStatus } from '../(types)/event'
import type { Session } from '../(types)/session'

export function useEventForm() {
  const { t: $t } = useI18n()

  const saving         = ref(false)
  const globalError    = ref('')
  const showDraftSaved = ref(false)
  const currentStep    = ref(0)

  const form = ref<EventFormState>({
    name:                '',
    categoryIds:         [],
    status:              'EDITING' as EventStatus,
    addressLine:         '',
    startDate:           '',
    endDate:             '',
    bannerUrl:           { wide: '', square: '', tall: '' },
    venueId:             '',
    longitude:           null,
    latitude:            null,
    seatingPlanImageUrl: null,
  })

  const content = ref<EventContentState>({
    aboutVi:            '',
    aboutEn:            '',
    termsAndConditions: '',
    policyRefund:       '',
  })

  const errors = ref<Record<string, string>>({})

  function triggerDraftSaved() {
    showDraftSaved.value = true
    setTimeout(() => { showDraftSaved.value = false }, 2500)
  }

  /** Loads event data and returns its sessions array */
  function loadEvent(eventId: string): Session[] {
    const ev = mockEvents.find(e => e.id === eventId)
    if (!ev) return []

    const firstSession = ev.sessions[0]
    form.value = {
      name:                ev.name,
      categoryIds:         [...ev.categoryIds],
      status:              ev.status,
      addressLine:         ev.addressLine,
      bannerUrl:           ev.bannerUrl,
      venueId:             ev.venueId ?? '',
      longitude:           ev.longitude ?? null,
      latitude:            ev.latitude ?? null,
      seatingPlanImageUrl: ev.seatingPlanImageUrl ?? null,
      startDate:           firstSession?.startDate.slice(0, 16) ?? '',
      endDate:             firstSession?.endDate.slice(0, 16)   ?? '',
    }

    content.value = {
      aboutVi:            ev.aboutVi            ?? '',
      aboutEn:            ev.aboutEn            ?? '',
      termsAndConditions: ev.termsAndConditions ?? '',
      policyRefund:       ev.policyRefund       ?? '',
    }

    return ev.sessions.map(s => ({ ...s, zones: s.zones.map(z => ({ ...z })) }))
  }

  function validateStep1(): boolean {
    errors.value = {}
    if (!form.value.name.trim())
      errors.value.name = $t('organizer.event_form.step1.name') + ' is required'
    if (!form.value.categoryIds.length)
      errors.value.categoryIds = $t('organizer.event_form.step1.category') + ' is required'
    if (!form.value.addressLine.trim())
      errors.value.addressLine = $t('organizer.event_form.step1.address') + ' is required'
    if (!form.value.startDate)
      errors.value.startDate = $t('organizer.event_form.step1.start_date') + ' is required'
    if (!form.value.endDate)
      errors.value.endDate = $t('organizer.event_form.step1.end_date') + ' is required'
    if (form.value.startDate && form.value.endDate && form.value.endDate <= form.value.startDate)
      errors.value.endDate = 'End date must be after start date'
    return Object.keys(errors.value).length === 0
  }

  async function saveStep1(): Promise<boolean> {
    if (!validateStep1()) return false
    saving.value = true
    await new Promise(r => setTimeout(r, 600))
    saving.value = false
    triggerDraftSaved()
    return true
  }

  async function saveStep2(): Promise<void> {
    saving.value = true
    await new Promise(r => setTimeout(r, 600))
    saving.value = false
    triggerDraftSaved()
  }

  async function saveLayout(): Promise<void> {
    saving.value = true
    await new Promise(r => setTimeout(r, 800))
    saving.value = false
    triggerDraftSaved()
  }

  return {
    form,
    content,
    errors,
    saving,
    globalError,
    showDraftSaved,
    currentStep,
    loadEvent,
    validateStep1,
    saveStep1,
    saveStep2,
    saveLayout,
  }
}