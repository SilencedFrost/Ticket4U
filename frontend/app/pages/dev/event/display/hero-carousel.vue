<script setup lang="ts">
import type { EventSummary } from '~/features/event/types/Event';
import EventHeroCarousel from '~/features/event/components/layout/EventHeroCarousel.vue';

const config = useRuntimeConfig();
const eventList = ref<EventSummary[]>([]);
const logger = useLogger();

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

const wrapAround = ref<boolean>(true);

onMounted(() => getFeaturedEvents());
</script>

<template>
  <div>
    <input v-model="wrapAround" type="checkbox" />Wrap around?
    <div class="p-3">
      <event-hero-carousel :events="eventList" />
    </div>
  </div>
</template>
