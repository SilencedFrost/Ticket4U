import { defineStore } from 'pinia'
import type { Event } from '~/pages/(home)/types/home'
import type { CategoryOption } from '~/pages/event-display/types/event-display'

interface EventDisplayFilters {
  startDate: string | null
  endDate: string | null
  categoryIds: number[]
  isFreeOnly: boolean
}

export const useEventDisplayStore = defineStore('eventDisplay', () => {
  const config = useRuntimeConfig()

  // State
  const events = ref<Event[]>([])
  const categories = ref<CategoryOption[]>([])
  const loading = ref(false)
  const loadingCategories = ref(false)
  const error = ref<string | null>(null)

  // Current filters
  const currentFilters = ref<EventDisplayFilters>({
    startDate: null,
    endDate: null,
    categoryIds: [],
    isFreeOnly: false,
  })

  // Fetch events with filters
  async function fetchEvents(filters?: Partial<EventDisplayFilters>) {
    loading.value = true
    error.value = null

    try {
      // Merge filters with current filters
      const appliedFilters = { ...currentFilters.value, ...filters }
      currentFilters.value = appliedFilters

      // Build query params
      const params = new URLSearchParams()
      if (appliedFilters.startDate) {
        params.append('startDate', appliedFilters.startDate)
      }
      if (appliedFilters.endDate) {
        params.append('endDate', appliedFilters.endDate)
      }
      if (appliedFilters.categoryIds && appliedFilters.categoryIds.length > 0) {
        appliedFilters.categoryIds.forEach(id => {
          params.append('categoryIds', id.toString())
        })
      }
      if (appliedFilters.isFreeOnly) {
        params.append('isFreeOnly', 'true')
      }

      const queryString = params.toString()
      const url = `${config.public.homeApiUrl}/events/filter${queryString ? `?${queryString}` : ''}`

      const data = await $fetch<Event[]>(url, {
        credentials: 'include',
      })
      events.value = data
    } catch (err) {
      error.value = 'Failed to load events'
      console.error('Error fetching events:', err)
    } finally {
      loading.value = false
    }
  }

  // Fetch all categories for filter
  async function fetchCategories() {
    loadingCategories.value = true
    try {
      const data = await $fetch<Array<{ id: number; name: string }>>((
        `${config.public.homeApiUrl}/categories/all`
      ), {
        credentials: 'include',
      })
      categories.value = data.map(cat => ({
        label: cat.name,
        value: cat.id.toString(),
      }))
    } catch (err) {
      console.error('Error fetching categories:', err)
    } finally {
      loadingCategories.value = false
    }
  }

  // Reset filters
  function resetFilters() {
    currentFilters.value = {
      startDate: null,
      endDate: null,
      categoryIds: [],
      isFreeOnly: false,
    }
  }

  return {
    // State
    events,
    categories,
    loading,
    loadingCategories,
    error,
    currentFilters,
    // Actions
    fetchEvents,
    fetchCategories,
    resetFilters,
  }
})
