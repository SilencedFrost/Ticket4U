<script setup lang="ts">
import type { EventSummary } from '../../types/Event';
import CarouselWrapper from '~/features/components/CarouselWrapper.vue';
import EventCard from '../core/EventCard.vue';
import { breakpointsBootstrapV5, useBreakpoints } from '@vueuse/core';

interface Props {
  events: EventSummary[];
  wrapAround?: boolean;
}

withDefaults(defineProps<Props>(), {
  wrapAround: true,
});

const breakpoints = useBreakpoints(breakpointsBootstrapV5);

const xxl = breakpoints.greaterOrEqual('xxl');
const md = breakpoints.greaterOrEqual('md');

const visibleCount = computed(() => {
  if (xxl.value) return 4;
  if (md.value) return 3;
  return 2;
});
</script>

<template>
  <div>
    <carousel-wrapper
      :items="events"
      :visible-count="visibleCount"
      mode="carousel"
      :wrap-around="wrapAround"
      :chevron-options="{ offset: 50 }"
    >
      <template #item="{ item }">
        <event-card :event="item" />
      </template>
    </carousel-wrapper>
  </div>
</template>
