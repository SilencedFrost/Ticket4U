<script setup lang="ts">
import { useMediaQuery } from '@vueuse/core';
import EventGrid from '~/features/event/components/layout/EventGrid.vue';
import FilterBarDesktop from './components/FilterBarDesktop.vue';
import FilterBarMobile from './components/FilterBarMobile.vue';
import type { EventSummary } from '~/features/event/types/Event';

const config = useRuntimeConfig();

const eventList = ref<EventSummary[]>([]);
const isDesktop = useMediaQuery('(min-width: 768px)');

function closeOpenedFilterPanels() {
  const openedPanels = document.querySelectorAll<HTMLDetailsElement>(
    'details[name="dev-event-filter-group"][open]',
  );
  openedPanels.forEach((panel) => {
    panel.open = false;
  });
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
  nextTick(() => closeOpenedFilterPanels());
  getFeaturedEvents();
  document.addEventListener('click', handleClickOutsideFilters);
  window.addEventListener('pageshow', closeOpenedFilterPanels);
});

watch(isDesktop, () => {
  nextTick(() => closeOpenedFilterPanels());
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutsideFilters);
  window.removeEventListener('pageshow', closeOpenedFilterPanels);
});
</script>

<template>
  <div class="p-3">
    <ClientOnly>
      <FilterBarDesktop v-if="isDesktop" />
      <FilterBarMobile v-else />
    </ClientOnly>
    <event-grid :events="eventList" />
  </div>
</template>
