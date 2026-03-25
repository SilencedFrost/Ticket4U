import { ref } from 'vue'

export interface EventFormData {
    name: string
    venueId: string
    categoryId: any
    addressLine: string
    startDate: string
    endDate: string
    bannerUrl: string
    status: string
}

export interface EventContent {
    aboutVi: string
    aboutEn: string
    termsAndConditions: string
    policyRefund: string
}

export interface Category { id: number; name: string }
export interface Venue { id: string; name: string; addressLine: string; imageUrl?: string; layout?: string }

export const useEventForm = (apiUrl: string) => {
    const form = ref<EventFormData>({
        name: '', venueId: '', categoryId: '' as any,
        addressLine: '', startDate: '', endDate: '',
        bannerUrl: '', status: 'EDITING',
    })

    const content = ref<EventContent>({
        aboutVi: '', aboutEn: '', termsAndConditions: '', policyRefund: '',
    })

    const contentLang = ref<'vi' | 'en'>('vi')
    const errors      = ref<Record<string, string>>({})
    const saving      = ref(false)

    const categories = ref<Category[]>([])
    const venues     = ref<Venue[]>([])

    const fetchCategories = async () => {
        try { categories.value = await $fetch<Category[]>(`${apiUrl}/events/categories`, { credentials: 'include' }) }
        catch { categories.value = [] }
    }

    const fetchVenues = async () => {
        try { venues.value = await $fetch<Venue[]>(`${apiUrl}/events/venues`, { credentials: 'include' }) }
        catch { venues.value = [] }
    }

    const validate = () => {
        errors.value = {}
        if (!form.value.name.trim())        errors.value.name        = 'Name is required'
        if (!form.value.categoryId)         errors.value.categoryId  = 'Category is required'
        if (!form.value.addressLine.trim()) errors.value.addressLine = 'Address is required'
        if (!form.value.startDate)          errors.value.startDate   = 'Start date is required'
        if (!form.value.endDate)            errors.value.endDate     = 'End date is required'
        if (form.value.startDate && form.value.endDate && form.value.endDate <= form.value.startDate)
            errors.value.endDate = 'End date must be after start date'
        return Object.keys(errors.value).length === 0
    }

    const buildPayload = () => ({
        name:               form.value.name,
        venueId:            form.value.venueId || null,
        categoryId:         Number(form.value.categoryId),
        addressLine:        form.value.addressLine,
        bannerUrl:          form.value.bannerUrl || null,
        status:             form.value.status,
        startDate:          new Date(form.value.startDate).toISOString(),
        endDate:            new Date(form.value.endDate).toISOString(),
        aboutVi:            content.value.aboutVi || null,
        aboutEn:            content.value.aboutEn || null,
        termsAndConditions: content.value.termsAndConditions || null,
        policyRefund:       content.value.policyRefund || null,
    })

    const saveStep1 = async (savedEventId: string | null, savedSessionId: string | null): Promise<{ eventId: string; sessionId: string | null } | null> => {
        if (!validate()) return null
        saving.value = true
        try {
            if (savedEventId) {
                await $fetch(`${apiUrl}/events/${savedEventId}`, { method: 'PUT', body: buildPayload(), credentials: 'include' })
                if (savedSessionId) {
                    const payload = buildPayload()
                    await $fetch(`${apiUrl}/events/${savedEventId}/sessions/${savedSessionId}`, {
                        method: 'PUT', body: { startDate: payload.startDate, endDate: payload.endDate }, credentials: 'include'
                    })
                }
                return { eventId: savedEventId, sessionId: savedSessionId }
            } else {
                const res = await $fetch<{ id: string; sessions: { id: string }[] }>(`${apiUrl}/events`, { method: 'POST', body: buildPayload(), credentials: 'include' })
                let sessionId = res.sessions?.[0]?.id ?? null
                if (!sessionId) {
                    const sessions = await $fetch<{ id: string }[]>(`${apiUrl}/events/${res.id}/sessions`, { credentials: 'include' }).catch(() => [])
                    sessionId = sessions?.[0]?.id ?? null
                }
                return { eventId: res.id, sessionId }
            }
        } catch (err: any) {
            throw new Error(err?.data?.message ?? 'Failed to save event')
        } finally {
            saving.value = false
        }
    }

    const saveStep2 = async (savedEventId: string): Promise<void> => {
        saving.value = true
        try {
            await $fetch(`${apiUrl}/events/${savedEventId}`, { method: 'PUT', body: buildPayload(), credentials: 'include' })
        } catch (err: any) {
            throw new Error(err?.data?.message ?? 'Failed to save content')
        } finally {
            saving.value = false
        }
    }

    return {
        form, content, contentLang, errors, saving,
        categories, venues,
        fetchCategories, fetchVenues,
        validate, saveStep1, saveStep2,
    }
}