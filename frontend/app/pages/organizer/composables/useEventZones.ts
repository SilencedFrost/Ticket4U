// TODO: refactor to features/organizer/
import { ref } from 'vue'
import type { Zone, ZoneFormState } from '../(types)/zone'

export function useEventZones(initial: Zone[] = []) {
  const zones = ref<Zone[]>([...initial])

  const showZoneModal = ref(false)
  const editingZone   = ref<Zone | null>(null)

  function openZoneModal(zone: Zone | null) {
    editingZone.value  = zone
    showZoneModal.value = true
  }

  function closeZoneModal() {
    showZoneModal.value = false
    editingZone.value   = null
  }

  function saveZone(formData: ZoneFormState) {
    const capacity = formData.isStanding
      ? formData.capacity
      : formData.gridRows * formData.gridCols

    if (editingZone.value) {
      const idx = zones.value.findIndex(z => z.id === editingZone.value!.id)
      if (idx >= 0) {
        zones.value[idx] = { ...zones.value[idx], ...formData, capacity }
      }
    } else {
      zones.value.push({
        id:           'z-new-' + Date.now(),
        quantitySold: 0,
        ...formData,
        capacity,
      })
    }
    closeZoneModal()
  }

  function deleteZone(id: string) {
    zones.value = zones.value.filter(z => z.id !== id)
  }

  function setZones(newZones: Zone[]) {
    zones.value = [...newZones]
  }

  return {
    zones,
    showZoneModal,
    editingZone,
    openZoneModal,
    closeZoneModal,
    saveZone,
    deleteZone,
    setZones,
  }
}