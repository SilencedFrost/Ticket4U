<script setup lang="ts">
import type { EventSummary } from '~/features/event/types/Event';
import EventHeroCarousel from '~/features/event/components/layout/EventHeroCarousel.vue';

const config = useRuntimeConfig();
const { data: eventList } = await useFetch<EventSummary[]>(
  `${config.public.eventServiceUrl}/public/events/featured`,
  {
    default: () => [],
    onRequest() {
      console.log('fetching featured events');
    },
  },
);

// TODO: add hero
// TODO: add hero carousel displaying featured
// TODO: add section displaying hot events
// TODO: add section display events near user
// TODO: add section displaying top 3 categories that the user have bought into
// TODO: add section to display other events
</script>

<template>
  <div id="homepage-container">
    <div id="hero">Todo: add hero</div>
    <div id="event-display" class="container-xl">
      <event-hero-carousel :events="eventList" :wrap-around="true" />
    </div>
  </div>
</template>
