<template>
  <div class="event-display bg-reactive-primary" style="min-height: 100vh">
    <div class="container-xxl py-5">
      <!-- Header section with filters -->
      <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
        <h2 class="text-primary fw-normal mb-0 d-none d-md-block" style="font-size: 20px">
          {{ $t('event_display.label.search_result') }}
        </h2>

        <div class="d-flex gap-2 gap-md-3 ms-auto">
          <DateRangeFilter
            :show="showDateFilter"
            :start-date="startDate"
            :end-date="endDate"
            :selected-preset="selectedPreset"
            :date-range-label="formatDateRange()"
            :presets="datePresets"
            :is-mobile="isMobile"
            @toggle="toggleDateFilter"
            @select-preset="selectPreset"
            @update:start-date="startDate = $event"
            @update:end-date="endDate = $event"
            @reset="resetDateFilter"
            @apply="applyDateFilter"
          />

          <MainFilter
            :show="showMainFilter"
            :selected-location="selectedLocation"
            :is-free-event="isFreeEvent"
            :selected-categories="selectedCategories"
            :locations="locations"
            :categories="categories"
            :is-mobile="isMobile"
            :popup-style="mainFilterPopupStyle"
            @toggle="toggleMainFilter"
            @update:selected-location="selectedLocation = $event"
            @update:is-free-event="isFreeEvent = $event"
            @toggle-category="toggleCategory"
            @reset="resetMainFilter"
            @apply="applyMainFilter"
          />
        </div>
      </div>

      <!-- Active Filters Tags -->
      <div v-if="activeFilters.length > 0" class="active-filters-section mb-4">
        <div class="d-flex flex-wrap gap-2 align-items-center">
          <div
            v-for="filter in activeFilters"
            :key="filter.key"
            class="filter-tag d-flex align-items-center gap-2"
          >
            <button
              type="button"
              class="btn-remove-filter"
              :aria-label="`Xóa bộ lọc ${filter.label}`"
              @click="removeFilter(filter.key, filter.value)"
            >
              <i class="bi bi-x-circle-fill" />
            </button>
            <span class="filter-label">{{ filter.label }}</span>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-light" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="alert alert-danger">
        {{ error }}
      </div>

      <!-- Events Grid -->
      <EventGrid v-else :events="events" @event-click="handleEventClick" />

      <!-- Pagination -->
      <Pagination
        v-if="!loading && !error && events.length > 0"
        :current-page="currentPage"
        :page-size="pageSize"
        :has-more="hasMore"
        :loading="loading"
        :total-displayed="events.length"
        @previous="handlePreviousPage"
        @next="handleNextPage"
        @go-to-page="handleGoToPage"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue';
import { storeToRefs } from 'pinia';
import DateRangeFilter from './DateRangeFilter.vue';
import MainFilter from './MainFilter.vue';
import EventGrid from './EventGrid.vue';
import Pagination from './Pagination.vue';
import { useDateRange } from '../composables/use-date-range';
import { useEventFilter } from '../composables/use-event-filter';
import { useEventDisplayStore } from '~/stores/eventDisplayStore';
import { locations, datePresets } from '../data/filters';

// Store
const eventDisplayStore = useEventDisplayStore();
const { events, categories, loading, error, currentPage, pageSize, hasMore } = storeToRefs(eventDisplayStore);

// i18n
const { t } = useI18n();

// Mobile state
const isMobile = ref(false);

// Date range composable
const {
  startDate,
  endDate,
  selectedPreset,
  selectPreset,
  formatDateRange,
  reset: resetDateRange,
} = useDateRange();

// Event filter composable
const {
  selectedLocation,
  isFreeEvent,
  selectedCategories,
  toggleCategory,
  reset: resetEventFilter,
} = useEventFilter();

// Filter visibility
const showDateFilter = ref(false);
const showMainFilter = ref(false);

// Popup style for main filter
const mainFilterPopupStyle = ref<Record<string, string>>({});

// Active filters tracking
interface ActiveFilter {
  key: string;
  value: string;
  label: string;
}

const activeFilters = computed<ActiveFilter[]>(() => {
  const filters: ActiveFilter[] = [];

  // Category filters
  selectedCategories.value.forEach((categoryId) => {
    const category = categories.value.find((cat) => String(cat.value) === categoryId);
    if (category) {
      filters.push({
        key: 'category',
        value: categoryId,
        label: t(`common.category.${category.value}`, category.label),
      });
    }
  });

  // Date range filter
  if (startDate.value || endDate.value) {
    filters.push({
      key: 'dateRange',
      value: 'dateRange',
      label: formatDateRange(),
    });
  }

  // Free event filter
  if (isFreeEvent.value) {
    filters.push({
      key: 'freeEvent',
      value: 'freeEvent',
      label: t('event_display.label.free'),
    });
  }

  // Location filter (if implemented)
  if (selectedLocation.value) {
    const location = locations.find((loc) => loc.value === selectedLocation.value);
    if (location && location.value !== '') {
      filters.push({
        key: 'location',
        value: selectedLocation.value,
        label: location.label,
      });
    }
  }

  return filters;
});

// Update URL with current filter state
const updateURLWithFilters = () => {
  const router = useRouter();
  const localePath = useLocalePath();
  const query: Record<string, string> = {};

  // Add categoryIds to URL if any selected
  if (selectedCategories.value.length > 0) {
    query.categoryIds = selectedCategories.value.join(',');
  }

  // Add date range to URL if set
  if (startDate.value) {
    query.startDate = startDate.value;
  }
  if (endDate.value) {
    query.endDate = endDate.value;
  }

  // Add free event filter to URL if set
  if (isFreeEvent.value) {
    query.isFreeOnly = 'true';
  }

  // Add location to URL if set
  if (selectedLocation.value) {
    query.location = selectedLocation.value;
  }

  // Navigate to update URL (replace to avoid adding to history)
  // Use localePath to preserve language prefix
  router.replace({
    path: localePath('/event-display'),
    query: Object.keys(query).length > 0 ? query : undefined,
  });
};

// Remove individual filter
const removeFilter = (key: string, value: string) => {
  if (key === 'category') {
    toggleCategory(value);
  } else if (key === 'dateRange') {
    resetDateRange();
  } else if (key === 'freeEvent') {
    isFreeEvent.value = false;
  } else if (key === 'location') {
    selectedLocation.value = '';
  }

  // Update URL to reflect removed filter
  updateURLWithFilters();

  // Fetch events with updated filters
  fetchWithFilters();
};

// Fetch events with current filters
const fetchWithFilters = () => {
  // Convert selected category strings to integers
  const categoryIds = selectedCategories.value.map((id) => parseInt(id));

  eventDisplayStore.fetchEvents({
    startDate: startDate.value || null,
    endDate: endDate.value || null,
    categoryIds: categoryIds,
    isFreeOnly: isFreeEvent.value,
    page: 0, // Reset to first page when filters change
  });
};

// Toggle handlers
const toggleDateFilter = () => {
  showDateFilter.value = !showDateFilter.value;
  if (showMainFilter.value) showMainFilter.value = false;
};

const toggleMainFilter = () => {
  showMainFilter.value = !showMainFilter.value;
  if (showDateFilter.value) showDateFilter.value = false;
  nextTick(() => {
    adjustPopupPosition();
  });
};

// Filter handlers
const resetDateFilter = () => {
  resetDateRange();
};

const applyDateFilter = () => {
  showDateFilter.value = false;
  updateURLWithFilters();
  fetchWithFilters();
};

const resetMainFilter = () => {
  resetEventFilter();
};

const applyMainFilter = () => {
  showMainFilter.value = false;
  updateURLWithFilters();
  fetchWithFilters();
};

// Event click handler
const handleEventClick = (eventId: string) => {
  const localePath = useLocalePath();
  navigateTo(localePath(`/event-detail/${eventId}`));
};

// Pagination handlers
const handlePreviousPage = () => {
  eventDisplayStore.previousPage();
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleNextPage = () => {
  eventDisplayStore.nextPage();
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleGoToPage = (page: number) => {
  eventDisplayStore.goToPage(page);
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

// Close popup when clicking outside
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (
    showDateFilter.value &&
    !target.closest('.dropdown-popup') &&
    !target.closest('.btn-filter-primary')
  ) {
    showDateFilter.value = false;
  }
  if (
    showMainFilter.value &&
    !target.closest('.dropdown-popup') &&
    !target.closest('.btn-filter-secondary')
  ) {
    showMainFilter.value = false;
  }
};

// Adjust popup position for mobile
const adjustPopupPosition = () => {
  if (!isMobile.value || !showMainFilter.value) return;

  const button = document.querySelector('.btn-filter-secondary') as HTMLElement;
  const popup = document.querySelector('.dropdown-popup') as HTMLElement;

  if (!button || !popup) return;

  const buttonRect = button.getBoundingClientRect();
  const viewportHeight = window.innerHeight;

  const spaceBelow = viewportHeight - buttonRect.bottom;
  const spaceAbove = buttonRect.top;

  mainFilterPopupStyle.value = {};

  if (spaceBelow < 400 && spaceAbove > 400) {
    mainFilterPopupStyle.value = {
      top: 'auto',
      bottom: 'calc(100% + 8px)',
      maxHeight: `${spaceAbove - 16}px`,
    };
  } else {
    mainFilterPopupStyle.value = {
      maxHeight: `${spaceBelow - 16}px`,
    };
  }
};

// Viewport detection
const updateMobileState = () => {
  isMobile.value = window.innerWidth < 768;
  if (showMainFilter.value) {
    nextTick(() => adjustPopupPosition());
  }
};

onMounted(async () => {
  updateMobileState();
  window.addEventListener('resize', updateMobileState);
  document.addEventListener('click', handleClickOutside);

  // Fetch categories first
  await eventDisplayStore.fetchCategories();

  // Check URL query params for pre-filters
  const route = useRoute();
  const urlCategoryIds = route.query.categoryIds;

  if (urlCategoryIds) {
    // Parse categoryIds from URL (can be single value or comma-separated)
    const categoryIdsArray = Array.isArray(urlCategoryIds)
      ? urlCategoryIds.map((id) => String(id))
      : String(urlCategoryIds)
          .split(',')
          .filter((id) => id.trim());

    selectedCategories.value = categoryIdsArray;
  }

  // Fetch initial data with URL filters applied
  fetchWithFilters();
});

onUnmounted(() => {
  window.removeEventListener('resize', updateMobileState);
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
/* Active Filters Section */
.active-filters-section {
  padding: 12px 0;
}

.filter-tag {
  background-color: var(--bg-reactive-secondary);
  border: 1px solid var(--border-reactive-gray, #dee2e6);
  border-radius: 24px;
  padding: 6px 16px 6px 6px;
  font-size: 14px;
  transition: all 0.2s ease;
}

.filter-tag:hover {
  background-color: var(--bg-reactive-gray-hover, #e9ecef);
}

.btn-remove-filter {
  background: transparent;
  border: none;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--text-reactive-secondary);
  transition: color 0.2s ease;
}

.btn-remove-filter:hover {
  color: var(--text-reactive-primary);
}

.btn-remove-filter i {
  font-size: 16px;
}

.filter-label {
  color: var(--text-reactive-primary);
  font-weight: 500;
  white-space: nowrap;
  user-select: none;
}

/* Responsive adjustments */
@media (max-width: 767.98px) {
  .filter-tag {
    font-size: 13px;
    padding: 5px 12px 5px 5px;
  }

  .btn-remove-filter {
    width: 18px;
    height: 18px;
  }

  .btn-remove-filter i {
    font-size: 14px;
  }
}
</style>
