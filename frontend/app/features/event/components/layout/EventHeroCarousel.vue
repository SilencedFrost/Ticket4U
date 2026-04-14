<script setup lang="ts">
import type { EventSummary } from '../../types/Event';
import CarouselWrapper from '~/components/CarouselWrapper.vue';
import EventHero from '../core/EventHero.vue';

interface Props {
  events: EventSummary[];
  wrapAround?: boolean;
}

withDefaults(defineProps<Props>(), {
  wrapAround: true,
});
</script>

<template>
  <carousel-wrapper :items="events" :visible-count="1" mode="carousel" :wrap-around="true">
    <template #item="{ item }">
      <event-hero :event="item" />
    </template>
  </carousel-wrapper>
</template>
