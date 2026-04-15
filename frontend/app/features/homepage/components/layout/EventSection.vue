<script setup lang="ts">
import type { EventSummary } from '~/features/event/types/Event';
import EventCarousel from '~/features/event/components/layout/EventCarousel.vue';

withDefaults(defineProps<{ events: EventSummary[]; title: string; wrapAround?: boolean }>(), {
  wrapAround: true,
});
</script>

<template>
  <hr />
  <h3 class="fw-bold mb-3">{{ title }}</h3>
  <event-carousel :events="events" :wrap-around="wrapAround" />
</template>
