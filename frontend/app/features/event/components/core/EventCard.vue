<script setup lang="ts">
import { breakpointsBootstrapV5, useBreakpoints } from '@vueuse/core';
import type { EventSummary } from '~/features/event/types/Event';
import ShimmerImg from '~/components/ShimmerImg.vue';

defineProps<{
  event: EventSummary;
}>();

defineEmits<{
  'event-click': [id: string];
}>();

const isLg = useBreakpoints(breakpointsBootstrapV5).greaterOrEqual('lg');

const formatter = useFormatter();
</script>

<template>
  <div class="cursor-pointer" @click="$emit('event-click', event.id)">
    <div
      :class="['ratio', isLg ? 'ratio-16x9' : 'ratio-1x1', 'rounded-3', 'overflow-hidden', 'mb-2']"
    >
      <!-- Add banner url reactivity -->
      <shimmer-img :src="event.bannerUrl" :alt="event.name" />
    </div>

    <div class="d-flex">
      <i class="bi bi-person-circle text-reactive-secondary me-2" style="font-size: 35px" />
      <div class="d-flex flex-column">
        <span class="text-reactive-primary">
          {{ event.name }}
        </span>
        <span class="text-primary fw-bold">
          {{ `${$t('common.currency.from')}: ${formatter.formatPrice(event.minPrice)}` }}
        </span>
        <p class="text-reactive-secondary d-flex align-items-center gap-1 small">
          <i class="bi bi-calendar-event"></i>
          <span>{{ $d(event.startDate, 'short') }}</span>
        </p>
      </div>
    </div>
  </div>
</template>
