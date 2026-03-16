import { defineStore } from 'pinia';
import type { Event, Place, TrendingEvent, CategoryWithEvents } from '~/pages/(home)/types/home';
import type {
  PlaceResponse,
  CategoryResponse,
  CategoryWithEventsResponse,
} from '~/pages/(home)/types/api';

export const useHomeStore = defineStore('home', () => {
  const config = useRuntimeConfig();

  // State
  const featuredEvents = ref<Event[]>([]);
  const locationalEvents = ref<Event[]>([]);
  const trendingEvents = ref<TrendingEvent[]>([]);
  const suggestedEvents = ref<Event[]>([]);
  const places = ref<Place[]>([]);
  const categories = ref<CategoryWithEvents[]>([]);

  // Loading states
  const loading = ref({
    featured: false,
    locational: false,
    trending: false,
    suggested: false,
    places: false,
    categories: false,
  });

  // Error states
  const errors = ref({
    featured: null as string | null,
    locational: null as string | null,
    trending: null as string | null,
    suggested: null as string | null,
    places: null as string | null,
    categories: null as string | null,
  });

  // Actions
  async function fetchFeaturedEvents() {
    loading.value.featured = true;
    errors.value.featured = null;
    try {
      const data = await $fetch<Event[]>(
        `${config.public.eventServiceUrl}/public/events/featured`,
        {
          credentials: 'include',
        },
      );
      featuredEvents.value = data;
    } catch (error) {
      errors.value.featured = 'Failed to load featured events';
      console.error('Error fetching featured events:', error);
    } finally {
      loading.value.featured = false;
    }
  }

  async function fetchLocationalEvents() {
    loading.value.locational = true;
    errors.value.locational = null;
    try {
      const data = await $fetch<Event[]>(
        `${config.public.eventServiceUrl}/public/events/locational`,
        {
          credentials: 'include',
        },
      );
      locationalEvents.value = data;
    } catch (error) {
      errors.value.locational = 'Failed to load locational events';
      console.error('Error fetching locational events:', error);
    } finally {
      loading.value.locational = false;
    }
  }

  async function fetchTrendingEvents() {
    loading.value.trending = true;
    errors.value.trending = null;
    try {
      const data = await $fetch<Event[]>(
        `${config.public.eventServiceUrl}/public/events/trending`,
        {
          credentials: 'include',
        },
      );
      // Backend returns top 3, assign ranks
      trendingEvents.value = data.slice(0, 3).map((event, index) => ({
        ...event,
        rank: (index + 1) as 1 | 2 | 3,
      }));
    } catch (error) {
      errors.value.trending = 'Failed to load trending events';
      console.error('Error fetching trending events:', error);
    } finally {
      loading.value.trending = false;
    }
  }

  async function fetchSuggestedEvents() {
    loading.value.suggested = true;
    errors.value.suggested = null;
    try {
      const data = await $fetch<Event[]>(
        `${config.public.eventServiceUrl}/public/events/suggested`,
        {
          credentials: 'include',
        },
      );
      suggestedEvents.value = data;
    } catch (error) {
      errors.value.suggested = 'Failed to load suggested events';
      console.error('Error fetching suggested events:', error);
    } finally {
      loading.value.suggested = false;
    }
  }

  async function fetchCategories(): Promise<CategoryResponse[]> {
    try {
      const data = await $fetch<CategoryResponse[]>(
        `${config.public.eventServiceUrl}/public/categories`,
        {
          credentials: 'include',
        },
      );
      return data;
    } catch (error) {
      console.error('Error fetching categories:', error);
      throw new Error('Failed to load categories');
    }
  }
  async function fetchCategoriesWithEvents() {
    loading.value.categories = true;
    errors.value.categories = null;
    try {
      const categoriesList = await fetchCategories();
      if (!categoriesList || categoriesList.length === 0) {
        categories.value = [];
        return;
      }
      const data = await Promise.all(
        //Promise.all: để chạy song song call API
        categoriesList.map((category) =>
          $fetch<CategoryWithEventsResponse>(
            `${config.public.eventServiceUrl}/public/categories/${category.id}/events/upcoming?limit=4`,
            { credentials: 'include' },
          ),
        ),
      );
      categories.value = data;
    } catch (error) {
      errors.value.categories = 'Failed to load categories';
      console.error('Error fetching events in categories:', error);
    } finally {
      loading.value.categories = false;
    }
  }

  // Fetch all data at once - runs all API calls in parallel
  async function fetchAllHomeData() {
    await Promise.all([
      fetchFeaturedEvents(),
      fetchLocationalEvents(),
      fetchTrendingEvents(),
      fetchSuggestedEvents(),
      fetchCategoriesWithEvents(),
    ]);
  }

  return {
    // State
    featuredEvents,
    locationalEvents,
    trendingEvents,
    suggestedEvents,
    places,
    categories,
    loading,
    errors,
    // Actions
    fetchFeaturedEvents,
    fetchLocationalEvents,
    fetchTrendingEvents,
    fetchSuggestedEvents,
    fetchCategories: fetchCategoriesWithEvents,
    fetchAllHomeData,
  };
});
