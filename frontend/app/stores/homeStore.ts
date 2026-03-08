import { defineStore } from 'pinia'
import type { Event, Place, TrendingEvent, CategoryWithEvents } from '~/pages/(home)/types/home'
import type { PlaceResponse, CategoryWithEventsResponse } from '~/pages/(home)/types/api'

export const useHomeStore = defineStore('home', () => {
  const config = useRuntimeConfig()

  // State
  const featuredEvents = ref<Event[]>([])
  const specialEvents = ref<Event[]>([])
  const trendingEvents = ref<TrendingEvent[]>([])
  const suggestedEvents = ref<Event[]>([])
  const places = ref<Place[]>([])
  const categories = ref<CategoryWithEvents[]>([])

  // Loading states
  const loading = ref({
    featured: false,
    special: false,
    trending: false,
    suggested: false,
    places: false,
    categories: false,
  })

  // Error states
  const errors = ref({
    featured: null as string | null,
    special: null as string | null,
    trending: null as string | null,
    suggested: null as string | null,
    places: null as string | null,
    categories: null as string | null,
  })

  // Actions
  async function fetchFeaturedEvents() {
    loading.value.featured = true
    errors.value.featured = null
    try {
      const data = await $fetch<Event[]>(
        `${config.public.homeApiUrl}/featured`,
        {
          credentials: 'include',
        }
      )
      featuredEvents.value = data
    } catch (error) {
      errors.value.featured = 'Failed to load featured events'
      console.error('Error fetching featured events:', error)
    } finally {
      loading.value.featured = false
    }
  }

  async function fetchSpecialEvents() {
    loading.value.special = true
    errors.value.special = null
    try {
      const data = await $fetch<Event[]>(
        `${config.public.homeApiUrl}/special`,
        {
          credentials: 'include',
        }
      )
      specialEvents.value = data
    } catch (error) {
      errors.value.special = 'Failed to load special events'
      console.error('Error fetching special events:', error)
    } finally {
      loading.value.special = false
    }
  }

  async function fetchTrendingEvents() {
    loading.value.trending = true
    errors.value.trending = null
    try {
      const data = await $fetch<Event[]>(
        `${config.public.homeApiUrl}/trending`,
        {
          credentials: 'include',
        }
      )
      // Backend returns top 3, assign ranks
      trendingEvents.value = data
        .slice(0, 3)
        .map((event, index) => ({
          ...event,
          rank: (index + 1) as 1 | 2 | 3,
        }))
    } catch (error) {
      errors.value.trending = 'Failed to load trending events'
      console.error('Error fetching trending events:', error)
    } finally {
      loading.value.trending = false
    }
  }

  async function fetchSuggestedEvents() {
    loading.value.suggested = true
    errors.value.suggested = null
    try {
      const data = await $fetch<Event[]>(
        `${config.public.homeApiUrl}/suggested`,
        {
          credentials: 'include',
        }
      )
      suggestedEvents.value = data
    } catch (error) {
      errors.value.suggested = 'Failed to load suggested events'
      console.error('Error fetching suggested events:', error)
    } finally {
      loading.value.suggested = false
    }
  }

  async function fetchCategories() {
    loading.value.categories = true
    errors.value.categories = null
    try {
      const data = await $fetch<CategoryWithEventsResponse[]>(
        `${config.public.homeApiUrl}/categories`,
        {
          credentials: 'include',
        }
      )
      categories.value = data
    } catch (error) {
      errors.value.categories = 'Failed to load categories'
      console.error('Error fetching categories:', error)
    } finally {
      loading.value.categories = false
    }
  }

  // Fetch all data at once - runs all API calls in parallel
  async function fetchAllHomeData() {
    await Promise.all([
      fetchFeaturedEvents(),
      fetchSpecialEvents(),
      fetchTrendingEvents(),
      fetchSuggestedEvents(),
      fetchCategories(),
    ])
  }

  return {
    // State
    featuredEvents,
    specialEvents,
    trendingEvents,
    suggestedEvents,
    places,
    categories,
    loading,
    errors,
    // Actions
    fetchFeaturedEvents,
    fetchSpecialEvents,
    fetchTrendingEvents,
    fetchSuggestedEvents,
    fetchCategories,
    fetchAllHomeData,
  }
})
