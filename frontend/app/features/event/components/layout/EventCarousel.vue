<script setup lang="ts">
import type { EventSummary } from '../../types/Event';
import CarouselWrapper from '~/components/CarouselWrapper.vue';
import EventCard from '../core/EventCard.vue';
import { breakpointsBootstrapV5, useBreakpoints, useWindowSize } from '@vueuse/core';

interface Props {
  events: EventSummary[];
  wrapAround?: boolean;
}

withDefaults(defineProps<Props>(), {
  wrapAround: true,
});

const interpolatePoints = ref([
  { x: 320, y: 62 },
  { x: 480, y: 40 },
  { x: 540, y: 40 },
  // md
  { x: 767, y: 28 },
  { x: 768, y: 40 },
  // lg
  { x: 991, y: 30 },
  { x: 992, y: 50 },
  //xxl
  { x: 1399, y: 35 },
  { x: 1400, y: 47 },
  // really big?
  { x: 2000, y: 30 },
  { x: 3000, y: 22 },
]);

const breakpoints = useBreakpoints(breakpointsBootstrapV5);
const { interpolate } = useInterpolation(interpolatePoints);
const { width } = useWindowSize();

const isXxl = breakpoints.greaterOrEqual('xxl');
const isMd = breakpoints.greaterOrEqual('md');

const visibleCount = computed(() => {
  if (isXxl.value) return 4;
  if (isMd.value) return 3;
  return 2;
});

const offset = computed(() => interpolate(width.value));
</script>

<template>
  <client-only>
    <carousel-wrapper
      :items="events"
      :visible-count="visibleCount"
      mode="carousel"
      :wrap-around="wrapAround"
      :chevron-options="{ offset, height: isMd ? 55 : 50 }"
    >
      <template #item="{ item }">
        <event-card :event="item" />
      </template>
    </carousel-wrapper>
  </client-only>
</template>
