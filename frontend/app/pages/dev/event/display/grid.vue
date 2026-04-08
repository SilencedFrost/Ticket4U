<script setup lang="ts">
import EventGrid from '~/features/event/components/layout/EventGrid.vue';
import FilterBar from '~/features/filter/components/FilterBar.vue';
import { type EventSummary, EventStatus } from '~/features/event/types/Event';
import type { FilterStatusOption, MainFilterSection } from '~/features/filter/types';

const config = useRuntimeConfig();
const { t } = useI18n();

const eventList = ref<EventSummary[]>([]);

const mainSections: MainFilterSection[] = ['price', 'category', 'status'];

const statusOptions = computed<FilterStatusOption[]>(() => [
  {
    value: EventStatus.PREMIERE,
    label: t('status.status_options.premiere'),
  },
  {
    value: EventStatus.SELLING,
    label: t('status.status_options.selling'),
  },
  {
    value: EventStatus.ONGOING,
    label: t('status.status_options.ongoing'),
  },
  {
    value: EventStatus.FINISHED,
    label: t('status.status_options.finished'),
  },
]);

async function getFeaturedEvents() {
  try {
    eventList.value = await $fetch(`${config.public.eventServiceUrl}/public/events/featured`, {
      method: 'GET',
    });
  } catch (e) {
    console.log(e);
    eventList.value = [];
  }
}

onMounted(() => {
  getFeaturedEvents();
});
</script>

<template>
  <div class="p-3">
    <ClientOnly>
      <FilterBar :main-sections="mainSections" :status-options="statusOptions" />
    </ClientOnly>
    <event-grid :events="eventList" />
  </div>
</template>
