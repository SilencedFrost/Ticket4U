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

const isMounted = ref(false); 
</script>

<template>
<div class="cursor-pointer" @click="$emit('event-click', event.id)">
    <!-- Chỉ hiện khi chưa hydrate (SSR), ẩn sau khi ClientOnly mount xong -->
    <!-- TODO: test fallback behavior khi SSR chưa hydrate -->
    <template v-if="!isMounted">
      <div class="ratio ratio-1x1 rounded-3 overflow-hidden mb-2 bg-secondary opacity-25 d-lg-none" />
      <div class="ratio ratio-16x9 rounded-3 overflow-hidden mb-2 bg-secondary opacity-25 d-none d-lg-block" />
    </template>

    <ClientOnly @vue:mounted="isMounted = true">
      <div :class="['ratio', isLg ? 'ratio-16x9' : 'ratio-1x1', 'rounded-3', 'overflow-hidden', 'mb-2']">
        <shimmer-img v-if="isLg" :src="event.bannerUrl.wide" :alt="event.name" />
        <shimmer-img v-else :src="event.bannerUrl.square" :alt="event.name" />
      </div>
    </ClientOnly>
    <div class="d-flex">
      <i class="bi bi-person-circle text-reactive-secondary me-2" style="font-size: 35px" />
      <div class="d-flex flex-column">
        <span class="text-reactive-primary">
          {{ event.name }}
        </span>
        <span class="text-primary fw-bold">
          {{
            event.minPrice === 0
              ? $t('common.currency.free')
              : `${$t('common.currency.from')}: ${formatter.formatPrice(event.minPrice)}`
          }}
        </span>
        <p class="text-reactive-secondary d-flex align-items-center gap-1 small">
          <i class="bi bi-calendar-event"></i>
          <span>{{ $d(event.startDate, 'short') }}</span>
        </p>
      </div>
    </div>

  </div>
</template>
