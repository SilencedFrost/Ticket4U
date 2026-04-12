<script setup lang="ts">
import type { EventSummary } from '../../types/Event';
import CarouselWrapper from '~/features/components/CarouselWrapper.vue';
import EventHero from '../core/EventHero.vue';
import { breakpointsBootstrapV5, useBreakpoints } from '@vueuse/core';

interface Props {
  events: EventSummary[];
  wrapAround?: boolean;
}

withDefaults(defineProps<Props>(), {
  wrapAround: true,
});

const breakpoints = useBreakpoints(breakpointsBootstrapV5);

const isMd = breakpoints.greaterOrEqual('md');
</script>

<template>
  <carousel-wrapper
    :items="events"
    :visible-count="1"
    mode="carousel"
    :wrap-around="true"
    :chevron-options="{ height: isMd ? 55 : 50 }"
  >
    <template #item="{ item }">
      <event-hero :event="item" />
    </template>
  </carousel-wrapper>
</template>
