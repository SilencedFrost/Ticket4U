<script setup lang="ts">
import type { EventSummary } from '@/features/event/types/Event';
import EventGrid from '@/features/event/components/layout/EventGrid.vue';
import type { FilterStatusOption, MainFilterSection } from '~/features/filter/types';
import FilterBar from '~/features/filter/components/FilterBar.vue';
import { EventStatus } from '~/features/event/types/Event';

const route = useRoute();
const config = useRuntimeConfig();
const localePath = useLocalePath();

const searchQuery = computed(() => (route.query.q as string) || '');

const { data: events, pending: isLoading } = await useFetch<EventSummary[]>(
  () => `/public/events/search`,
  {
    baseURL: config.public.eventServiceUrl,
    params: {
      q: searchQuery,
      page: 0,
      size: 10,
    },
    key: `search-page-${searchQuery.value}`,
    lazy: true,
    watch: [searchQuery],
    onResponseError({ response }) {
      console.error('Error:', response.status);
    },
  },
);

const { t } = useI18n();

const mainSections: MainFilterSection[] = ['price', 'category', 'status'];

const ACTIVE_STATUS_KEYS = [
  EventStatus.PREMIERE,
  EventStatus.SELLING,
  EventStatus.ONGOING,
  EventStatus.FINISHED,
];

const statusOptions = computed<FilterStatusOption[]>(() =>
  ACTIVE_STATUS_KEYS.map((status) => ({
    value: status,
    label: t(`event.status_options.${status.toLowerCase()}`),
  })),
);

const handleEventClick = (eventId: string) => {
  navigateTo(localePath(`/dev/event/detail/${eventId}`));
};
</script>

<template>
  <div class="p-3">
    <div class="d-flex align-items-center gap-3">
      <div>
        <FilterBar :main-sections="mainSections" :status-options="statusOptions" />
      </div>
      <p v-if="searchQuery" class="fs-6 fw-bold d-none d-md-block">
        {{ t('search.results_for') }}: <span class="text-primary">"{{ searchQuery }}"</span>
      </p>
    </div>

    <div v-if="isLoading" class="text-center py-5">
      <div class="spinner-border text-primary"></div>
    </div>

    <div v-else-if="events && events.length > 0">
      <EventGrid :events="events" @event-click="handleEventClick" />
    </div>

    <div v-else class="text-center py-5">
      <p class="text-muted">{{ t('search.no_results') }}</p>
    </div>
  </div>
</template>
