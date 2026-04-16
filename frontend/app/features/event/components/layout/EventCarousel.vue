<script setup lang="ts">
import type { EventSummary } from '../../types/Event';
import CarouselWrapper from '~/components/CarouselWrapper.vue';
import EventCard from '../core/EventCard.vue';
import { breakpointsBootstrapV5, useBreakpoints, useResizeObserver } from '@vueuse/core';

interface Props {
  events: EventSummary[];
  wrapAround?: boolean;
}

withDefaults(defineProps<Props>(), {
  wrapAround: true,
});

const breakpoints = useBreakpoints(breakpointsBootstrapV5);

const carouselEl = ref<HTMLElement | null>(null);
const componentWidth = ref(0);
const isXxl = breakpoints.greaterOrEqual('xxl');
const isLg = breakpoints.greaterOrEqual('lg');
const isMd = breakpoints.greaterOrEqual('md');

// < 768
const smInterpolateMap = [
  { x: 300, y: 62 },
  { x: 325, y: 58 },
  { x: 350, y: 54 },
  { x: 500, y: 41 },
  { x: 600, y: 35.5 },
  { x: 767, y: 28.5 },
];

// >= 768
const mdInterpolateMap = [
  { x: 450, y: 65 },
  { x: 500, y: 58.5 },
  { x: 600, y: 51 },
  { x: 750, y: 42 },
  { x: 991, y: 33 },
];

// >= 992
const lgInterpolateMap = [
  { x: 450, y: 90 },
  { x: 750, y: 63 },
  { x: 1000, y: 50.5 },
  { x: 1399, y: 40 },
];

// >= 1400
const xxlInterpolateMap = [
  { x: 600, y: 87 },
  { x: 1000, y: 63 },
  { x: 1500, y: 46 },
  { x: 2000, y: 37 },
  { x: 3000, y: 26 },
];

const activeInpolationMap = computed(() => {
  if (isXxl.value) return xxlInterpolateMap;
  if (isLg.value) return lgInterpolateMap;
  if (isMd.value) return mdInterpolateMap;
  return smInterpolateMap;
});

const { interpolate } = useInterpolation(activeInpolationMap);

const visibleCount = computed(() => {
  if (isXxl.value) return 4;
  if (isMd.value) return 3;
  return 2;
});

useResizeObserver(carouselEl, (entries) => {
  if (!entries[0]) return;
  componentWidth.value = entries[0].contentRect.width;
});

const offset = computed(() => interpolate(componentWidth.value));
</script>

<template>
  <client-only>
    <div ref="carouselEl">
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
    </div>
  </client-only>
</template>
