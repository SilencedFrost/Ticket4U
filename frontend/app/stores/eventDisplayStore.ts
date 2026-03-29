import { defineStore } from 'pinia';
import type { Event } from '~/pages/(home)/types/home';
import type { CategoryOption } from '~/pages/event-display/types/event-display';
import { useCategoryApi } from '~/composables/useCategoryApi';

interface EventDisplayFilters {
  startDate: string | null;
  endDate: string | null;
  categoryIds: Array<number | string>;
  location: string | null;
  isFreeOnly: boolean;
  tzOffset: number;
  page: number;
  size: number;
}

export const useEventDisplayStore = defineStore('eventDisplay', () => {
  const config = useRuntimeConfig();
  const { fetchPublicCategories } = useCategoryApi();
  let latestRequestId = 0;

  // State
  const events = ref<Event[]>([]);
  const categories = ref<CategoryOption[]>([]);
  const loading = ref(false);
  const loadingCategories = ref(false);
  const error = ref<string | null>(null);

  // Pagination state
  const currentPage = ref(0);
  const pageSize = ref(20);
  const hasMore = ref(true); // True nếu còn data để load

  // Current filters
  const currentFilters = ref<EventDisplayFilters>({
    startDate: null,
    endDate: null,
    categoryIds: [],
    location: null,
    isFreeOnly: false,
    tzOffset: new Date().getTimezoneOffset(),
    page: 0,
    size: 20,
  });

  // Fetch events with filters
  async function fetchEvents(filters?: Partial<EventDisplayFilters>) {
    const requestId = ++latestRequestId;
    loading.value = true;
    error.value = null;

    try {
      // Merge filters with current filters
      const appliedFilters = { ...currentFilters.value, ...filters };
      currentFilters.value = appliedFilters;

      // Build query params
      const params = new URLSearchParams();
      if (appliedFilters.startDate) {
        params.append('startDate', appliedFilters.startDate);
      }
      if (appliedFilters.endDate) {
        params.append('endDate', appliedFilters.endDate);
      }
      if (appliedFilters.categoryIds && appliedFilters.categoryIds.length > 0) {
        const ids = Array.isArray(appliedFilters.categoryIds)
          ? appliedFilters.categoryIds
          : [appliedFilters.categoryIds];
        ids
          .filter((id) => id !== null && id !== undefined && id !== '')
          .forEach((id) => params.append('categoryIds', String(id)));
      }
      if (appliedFilters.location) {
        params.append('location', appliedFilters.location);
      }
      if (appliedFilters.isFreeOnly) {
        params.append('isFreeOnly', 'true');
      }
      // Send client timezone offset for accurate date boundary conversion
      params.append('tzOffset', String(appliedFilters.tzOffset ?? new Date().getTimezoneOffset()));

      // Add pagination params (with defaults)
      const page = appliedFilters.page ?? 0;
      const size = appliedFilters.size ?? 20;
      params.append('page', page.toString());
      params.append('size', size.toString());

      const queryString = params.toString();
      const url = `${config.public.eventServiceUrl}/public/events/filter${queryString ? `?${queryString}` : ''}`;

      const data = await $fetch<Event[]>(url, {
        credentials: 'include',
      });

      if (requestId !== latestRequestId) {
        return;
      }

      // Update pagination state
      currentPage.value = page;
      pageSize.value = size;
      // data.length >= size: nếu trả về >= size items thì có thể còn trang tiếp theo
      // dùng >= thay vì === để tránh false-negative khi API trả về nhiều hơn size
      hasMore.value = data.length >= size;

      events.value = data;
    } catch (err) {
      if (requestId !== latestRequestId) {
        return;
      }
      error.value = 'event_display.error.load_events';
      console.error('Error fetching events:', err);
    } finally {
      if (requestId === latestRequestId) {
        loading.value = false;
      }
    }
  }

  // Fetch all categories for filter
  async function fetchCategories() {
    loadingCategories.value = true;
    try {
      const data = await fetchPublicCategories(config.public.eventServiceUrl);
      categories.value = data.map((cat) => ({
        label: cat.name,
        value: cat.id.toString(),
      }));
    } catch (err) {
      console.error('Error fetching categories:', err);
    } finally {
      loadingCategories.value = false;
    }
  }

  // Reset filters
  function resetFilters() {
    currentFilters.value = {
      startDate: null,
      endDate: null,
      categoryIds: [],
      location: null,
      isFreeOnly: false,
      tzOffset: new Date().getTimezoneOffset(),
      page: 0,
      size: 20,
    };
    currentPage.value = 0;
    hasMore.value = true;
  }

  // Go to next page
  function nextPage() {
    if (hasMore.value && !loading.value) {
      fetchEvents({ page: currentPage.value + 1 });
    }
  }

  // Go to previous page
  function previousPage() {
    if (currentPage.value > 0 && !loading.value) {
      fetchEvents({ page: currentPage.value - 1 });
    }
  }

  // Go to specific page
  function goToPage(page: number) {
    if (page >= 0 && !loading.value) {
      fetchEvents({ page });
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
    // Pagination
    currentPage,
    pageSize,
    hasMore,
    // Actions
    fetchEvents,
    fetchCategories,
    resetFilters,
    nextPage,
    previousPage,
    goToPage,
  };
});
