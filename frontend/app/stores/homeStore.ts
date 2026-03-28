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

  type HomeEventKey = 'featured' | 'locational' | 'trending' | 'suggested';

  async function fetchHomeEvents(
    key: HomeEventKey,
    endpoint: string,
    errorKey: string,
    onSuccess: (data: EventSummary[]) => void,
  ) {
    loading.value[key] = true;
    errors.value[key] = null;
    try {
      const data = await $fetch<EventSummary[]>(`${config.public.eventServiceUrl}${endpoint}`, {
        credentials: 'include',
      });
      onSuccess(data);
    } catch (error) {
      errors.value[key] = errorKey;
      console.error(`Error fetching ${key} events:`, error);
    } finally {
      loading.value[key] = false;
    }
  }

  // Actions
  async function fetchFeaturedEvents() {
    await fetchHomeEvents(
      'featured',
      '/public/events/featured',
      'home_page.error.load_featured',
      (data) => {
        featuredEvents.value = data;
      },
    );
  }

  async function fetchLocationalEvents() {
    await fetchHomeEvents(
      'locational',
      '/public/events/locational',
      'home_page.error.load_locational',
      (data) => {
        locationalEvents.value = data;
      },
    );
  }

  async function fetchTrendingEvents() {
    await fetchHomeEvents(
      'trending',
      '/public/events/trending',
      'home_page.error.load_trending',
      (data) => {
        // Backend returns top 3, assign ranks
        trendingEvents.value = data.slice(0, 3).map((event, index) => ({
          ...event,
          rank: (index + 1) as 1 | 2 | 3,
        }));
      },
    );
  }

  async function fetchSuggestedEvents() {
    await fetchHomeEvents(
      'suggested',
      '/public/events/suggested',
      'home_page.error.load_suggested',
      (data) => {
        suggestedEvents.value = data;
      },
    );
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
      throw new Error('home_page.error.load_categories');
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
        errors.value.categories = 'home_page.error.load_categories';
      }
    } catch (error) {
      errors.value.categories = 'home_page.error.load_categories';
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
