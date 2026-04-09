// TODO: refactor to features/organizer/
import { ref } from 'vue'
import { mockProfile } from '../mock.data'

interface OrganizerProfile {
  name: string
  email: string
  logoUrl?: string | null
  logo_url?: string | null
}

const profile = ref<OrganizerProfile | null>(null)

export function useOrganizerProfile() {
  function fetchProfile() {
    if (profile.value) return
    profile.value = {
      name:    mockProfile.name,
      email:   mockProfile.email,
      logoUrl: mockProfile.avatarUrl ?? null,
    }
  }

  function reset() {
    profile.value = null
  }

  return { profile, fetchProfile, reset }
}