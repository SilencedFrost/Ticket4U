// Shared composable so both the layout and pages can access the organizer profile without making duplicate API calls

import { ref } from 'vue'

interface OrganizerProfile {
  id?: string
  name: string
  logoUrl?: string
  logo_url?: string   // user-service returns snake_case
  description?: string
}

// Module-level state — shared across all uses of this composable
const profile  = ref<OrganizerProfile | null>(null)
const loaded   = ref(false)
const loading  = ref(false)
 
export const useOrganizerProfile = () => {
  const config    = useRuntimeConfig()
  const userStore = useUserStore()
 
  const fetchProfile = async () => {
    if (loaded.value || loading.value) return
    loading.value = true
    try {
      const userId = userStore.user?.id
      if (!userId) {
        profile.value = { name: userStore.user?.username ?? 'Organizer' }
        loaded.value  = true
        return
      }
      profile.value = await $fetch<OrganizerProfile>(
        `${config.public.userApiUrl}/api/v1/public/organizers/${userId}`
      )
      loaded.value = true
    } catch {
      // Fallback to username from store if user-service call fails
      profile.value = { name: userStore.user?.username ?? 'Organizer' }
      loaded.value  = true
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
 