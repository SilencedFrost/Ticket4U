import { ref } from 'vue'

interface OrganizerProfile {
    id?: string
    name: string
    logoUrl?: string
    logo_url?: string
    description?: string
}

const MOCK_PROFILE: OrganizerProfile = {
    id:      '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    name:    'Nguyễn Văn A',
    logoUrl: undefined,
}

const profile = ref<OrganizerProfile | null>(null)
const loaded  = ref(false)
const loading = ref(false)

export const useOrganizerProfile = () => {
    const fetchProfile = async () => {
        if (loaded.value || loading.value) return
        loading.value = true
        // No API call — resolve from mock data
        profile.value = MOCK_PROFILE
        loaded.value  = true
        loading.value = false
    }
    console.log('useOrganizerProfile: fetchProfile called, returning mock data')
    const reset = () => {
        profile.value = null
        loaded.value  = false
        loading.value = false
    }

    return { profile, loaded, loading, fetchProfile, reset }
}