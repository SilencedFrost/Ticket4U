import { defineStore } from 'pinia';

export const useHomeStore = defineStore('home', () => {
  const config = useRuntimeConfig();

  // State
  const featuredEvents = ref<EventSummary[]>([]);
  const locationalEvents = ref<EventSummary[]>([]);
  const trendingEvents = ref<EventSummary[]>([]);
  const suggestedEvents = ref<EventSummary[]>([]);
  const places = ref<VenueSummary[]>([]);
  const categories = ref<Category[]>([]);

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
      const data = await $fetch<EventSummary[]>(
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
      const data = await $fetch<EventSummary[]>(
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
      const data = await $fetch<EventSummary[]>(
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
      const data = await $fetch<EventSummary[]>(
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

  async function fetchCategories(): Promise<CategorySummary[]> {
    try {
      const data = await $fetch<CategorySummary[]>(
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
      const results = await Promise.allSettled(
        categoriesList.map((category) =>
          $fetch<Category>(
            `${config.public.eventServiceUrl}/public/categories/${category.id}/events/upcoming?limit=4`,
            { credentials: 'include' },
          ),
        ),
      );
      categories.value = results
        .filter(
          (result): result is PromiseFulfilledResult<Category> => result.status === 'fulfilled',
        )
        .map((result) => result.value);

      if (categories.value.length === 0 && results.some((result) => result.status === 'rejected')) {
        errors.value.categories = 'Failed to load categories';
      }
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
