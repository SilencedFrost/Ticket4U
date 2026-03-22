// stores/user.ts  (or composables/useUserStore.ts)
// Mock replacement for the real Pinia useUserStore.
// Provides the minimal shape the organizer layout + pages need.

import { ref } from 'vue'
import { mockProfile } from '../mock/organizer.data'

interface MockUser {
  id: string
  username: string
  email: string
  roleId: number
}

// Module-level singleton
const user = ref<MockUser | null>({
  id:       mockProfile.id,
  username: mockProfile.name,
  email:    mockProfile.email,
  roleId:   mockProfile.roleId,   // 2 = Organizer Admin, 1 = Event Manager
})

export const useUserStore = () => {
  const logout = async () => {
    // Mock logout — just clear the user
    user.value = null
  }

  return { user, logout }
}