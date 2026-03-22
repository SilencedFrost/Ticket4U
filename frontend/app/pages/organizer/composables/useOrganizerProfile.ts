import { ref } from 'vue'
import { mockProfile } from '../mock/organizer.data'

interface OrganizerProfile {
  name: string
  email: string
  logoUrl?: string | null
  logo_url?: string | null
}

// Module-level singleton — shared across all components
// (mirrors how the real composable works)
const profile = ref<OrganizerProfile | null>(null)

export const useOrganizerProfile = () => {
  const fetchProfile = () => {
    // No-op if already loaded
    if (profile.value) return
    profile.value = {
      name:    mockProfile.name,
      email:   mockProfile.email,
      logoUrl: mockProfile.avatarUrl ?? null,
    }
  }

  const reset = () => {
    profile.value = null
  }

  return { profile, fetchProfile, reset }
}