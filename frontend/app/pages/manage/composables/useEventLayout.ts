import { ref, computed, type Ref } from 'vue'
import { mockVenues } from '../mock.data'
import type { Zone } from '../(types)/zone'

export function useEventLayout(venueIdRef: Readonly<Ref<string>>, zonesRef: Readonly<Ref<Zone[]>>) {
  const layoutMode = ref<'venue' | 'custom'>('venue')

  const selectedVenue = computed(() =>
    mockVenues.find(v => v.id === venueIdRef.value) ?? null
  )

  const seatedZones = computed(() =>
    zonesRef.value.filter(z => !z.isStanding)
  )

  const standingZones = computed(() =>
    zonesRef.value.filter(z => z.isStanding)
  )

  return {
    layoutMode,
    selectedVenue,
    seatedZones,
    standingZones,
  }
}