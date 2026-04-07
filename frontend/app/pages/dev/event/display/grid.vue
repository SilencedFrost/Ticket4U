<script setup lang="ts">
import EventGrid from '~/features/event/components/layout/EventGrid.vue';
import DateRangeFilter from './components/DateRangeFilter.vue';
import MainFilter from './components/MainFilter.vue';
import type { EventSummary } from '~/features/event/types/Event';

const config = useRuntimeConfig();

const eventList = ref<EventSummary[]>([]);

function closeOpenedFilterPanels() {
  const openedPanels = document.querySelectorAll<HTMLDetailsElement>(
    'details[name="dev-event-filter-group"][open]',
  );
  openedPanels.forEach((panel) => panel.removeAttribute('open'));
}

function handleClickOutsideFilters(event: MouseEvent) {
  const target = event.target;
  if (!(target instanceof Element)) return;

  const clickedInsideFilter = target.closest('details[name="dev-event-filter-group"]');
  if (!clickedInsideFilter) {
    closeOpenedFilterPanels();
  }
}

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
  document.addEventListener('click', handleClickOutsideFilters);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutsideFilters);
});
</script>

<template>
  <div class="p-3">
    <div class="d-flex flex-wrap justify-content-end gap-2 mb-3">
      <DateRangeFilter />
      <MainFilter />
    </div>
    <event-grid :events="eventList" />
  </div>
</template>
