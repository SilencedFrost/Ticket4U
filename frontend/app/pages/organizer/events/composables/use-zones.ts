import { ref } from 'vue'

export interface Zone {
    id?: string
    name: string
    isStanding: boolean
    capacity: number
    price: number
    purchaseLimit?: number | null
    descriptionVi?: string
    descriptionEn?: string
    perks?: string | string[]
    quantitySold?: number
    seatCount: number
    gridRows?: number
    gridCols?: number
}

export const useZones = (apiUrl: string) => {
    const zones           = ref<Zone[]>([])
    const showZoneModal   = ref(false)
    const editingZone     = ref<Zone | null>(null)
    const deleteZoneTarget = ref<Zone | null>(null)
    const zoneSaving      = ref(false)
    const zoneError       = ref('')

    const fetchZones = async (sessionId: string) => {
        try {
            zones.value = await $fetch<Zone[]>(`${apiUrl}/events/sessions/${sessionId}/zones`, { credentials: 'include' })
        } catch { zones.value = [] }
    }

    const openZoneModal = (zone: Zone | null) => {
        editingZone.value  = zone ? { ...zone } : null
        showZoneModal.value = true
    }

    const onZoneSaved = (savedZone: Zone) => {
        const idx = zones.value.findIndex(z => z.id === savedZone.id)
        if (idx >= 0) zones.value[idx] = savedZone
        else zones.value.push(savedZone)
        showZoneModal.value = false
    }

    const confirmDeleteZone = (zone: Zone) => {
        deleteZoneTarget.value = zone
        zoneError.value = ''
    }

    const doDeleteZone = async (sessionId: string): Promise<boolean> => {
        if (!deleteZoneTarget.value?.id) return false
        zoneSaving.value = true
        try {
            await $fetch(`${apiUrl}/events/sessions/${sessionId}/zones/${deleteZoneTarget.value.id}`, { method: 'DELETE', credentials: 'include' })
            zones.value = zones.value.filter(z => z.id !== deleteZoneTarget.value!.id)
            deleteZoneTarget.value = null
            return true
        } catch (err: any) {
            zoneError.value = err?.data?.message ?? 'Failed to delete zone'
            return false
        } finally {
            zoneSaving.value = false
        }
    }

    const parsedPerks = (perks: string | string[] | undefined): string[] => {
        if (!perks) return []
        if (Array.isArray(perks)) return perks
        try { return JSON.parse(perks) } catch { return [] }
    }

    return {
        zones, showZoneModal, editingZone, deleteZoneTarget, zoneSaving, zoneError,
        fetchZones, openZoneModal, onZoneSaved, confirmDeleteZone, doDeleteZone, parsedPerks,
    }
}