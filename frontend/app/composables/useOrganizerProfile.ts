// Shared composable so both the layout and pages can access the organizer profile without making duplicate API calls

import { ref } from 'vue'

interface OrganizerProfile {
  id?: string
  name: string
  avatarUrl?: string
  logoUrl?: string
}

// Module-level state — shared across all uses of this composable
const profile  = ref<OrganizerProfile | null>(null)
const loaded   = ref(false)
const loading  = ref(false)

export const useOrganizerProfile = () => {
  const config = useRuntimeConfig()

  const fetchProfile = async () => {
    // Only fetch once — subsequent calls reuse cached value
    if (loaded.value || loading.value) return
    loading.value = true
    try {
      profile.value = await $fetch<OrganizerProfile>(
        `${config.public.apiUrl}/organizer/me`,
        { credentials: 'include' }
      )
      loaded.value = true
    } catch {
      profile.value = null
    } finally {
      loading.value = false
    }
  }

  const reset = () => {
    profile.value = null
    loaded.value  = false
    loading.value = false
  }

  return { profile, loaded, loading, fetchProfile, reset }
}