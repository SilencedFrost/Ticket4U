<script setup lang="ts">
import EventGrid from '~/features/event/components/layout/EventGrid.vue';
import type { EventSummary } from '~/features/event/types/Event';

const config = useRuntimeConfig();
const logger = useLogger();

const eventList = ref<EventSummary[]>([]);

async function getFeaturedEvents() {
  try {
    eventList.value = await $fetch(`${config.public.eventServiceUrl}/public/events/featured`, {
      method: 'GET',
    });
  } catch (e) {
    logger.error(e);
    eventList.value = [];
  }
}

onMounted(() => getFeaturedEvents());
</script>

<template>
  <div class="p-3">
    <event-grid :events="eventList" />
  </div>
</template>
