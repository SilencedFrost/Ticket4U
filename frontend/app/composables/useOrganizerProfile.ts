import { ref } from 'vue'

interface OrganizerProfile {
    id:           string
    name:         string
    description?: string
    rating?:      number
    logoUrl?:     string
}

const MOCK_PROFILE: OrganizerProfile = {
    id:          '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    name:        'Absolute Media',
    description: 'Chuyên âm nhạc nghệ thuật cao cấp và giao hưởng.',
    rating:      4.80,
    logoUrl:     'https://salt.tkbcdn.com/ts/ds/be/d4/a4/e6c34b2216009af82feba5f9bca782ad.jpg',
}

const profile = ref<OrganizerProfile | null>(null)
const loaded  = ref(false)
const loading = ref(false)

export const useOrganizerProfile = () => {
    const fetchProfile = async () => {
        if (loaded.value || loading.value) return
        loading.value = true
        profile.value = MOCK_PROFILE
        loaded.value  = true
        loading.value = false
    }

    const reset = () => {
        profile.value = null
        loaded.value  = false
        loading.value = false
    }

    return { profile, loaded, loading, fetchProfile, reset }
}
