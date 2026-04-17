<script setup lang="ts">
import { breakpointsBootstrapV5, useBreakpoints, useResizeObserver } from '@vueuse/core';
import type { EventSummary } from '~/features/event/types/Event';
import ShimmerImg from '~/components/ShimmerImg.vue';

defineProps<{
  event: EventSummary;
}>();

defineEmits<{
  'event-click': [id: string];
}>();

const breakpoints = useBreakpoints(breakpointsBootstrapV5);

const isLg = breakpoints.greaterOrEqual('lg');
const isSm = breakpoints.greaterOrEqual('sm');

const formatter = useFormatter();

const isMounted = ref(false);

const nameRef = ref<HTMLElement | null>(null);
const priceRef = ref<HTMLElement | null>(null);
const nameLineCount = ref(1);
const priceLineCount = ref(1);

useResizeObserver(nameRef, () => {
  const el = nameRef.value;
  if (!el) return;

  const range = document.createRange();
  range.selectNodeContents(el);

  nameLineCount.value = range.getClientRects().length;
});

useResizeObserver(priceRef, () => {
  const el = priceRef.value;
  if (!el) return;

  const range = document.createRange();
  range.selectNodeContents(el);

  priceLineCount.value = range.getClientRects().length;
});
</script>

<template>
  <div class="cursor-pointer" @click="$emit('event-click', event.id)">
    <template v-if="!isMounted">
      <div
        class="ratio ratio-1x1 rounded-3 overflow-hidden mb-2 bg-secondary opacity-25 d-lg-none"
      />
      <div
        class="ratio ratio-16x9 rounded-3 overflow-hidden mb-2 bg-secondary opacity-25 d-none d-lg-block"
      />
    </template>

    <client-only @vue:mounted="isMounted = true">
      <div
        :class="[
          'ratio',
          isLg ? 'ratio-16x9' : 'ratio-1x1',
          'rounded-3',
          'overflow-hidden',
          'mb-2',
        ]"
      >
        <shimmer-img v-if="isLg" :src="event.bannerUrl.wide" :alt="event.name" />
        <shimmer-img v-else :src="event.bannerUrl.square" :alt="event.name" />
      </div>
    </client-only>
    <div class="d-flex">
      <i
        class="bi bi-person-circle text-reactive-secondary me-2 d-none d-md-block"
        style="font-size: 35px"
      />
      <div class="d-flex flex-column" :style="{ fontSize: `${isSm ? 12 : 10}pt` }">
        <span ref="nameRef" class="text-reactive-primary text-truncate-2">
          {{ event.name }}
        </span>
        <span ref="priceRef" class="text-primary fw-bold text-truncate-2">
          {{
            event.minPrice === 0
              ? $t('common.currency.free')
              : `${$t('common.currency.from')}: ${formatter.formatPrice(event.minPrice)}`
          }}
        </span>
        <span class="text-reactive-secondary d-flex align-items-center gap-1 small">
          <i class="bi bi-calendar-event"></i>
          <span class="text-truncate-1">{{ $d(event.startDate, 'short') }}</span>
        </span>
        <span v-if="nameLineCount <= 1" style="visibility: hidden; user-select: none"
          >Padding text</span
        >
        <span v-if="priceLineCount <= 1" style="visibility: hidden; user-select: none"
          >Padding text</span
        >
      </div>
    </div>
  </div>
</template>

<style scoped>
.text-truncate-2 {
  display: -webkit-box;
  line-clamp: 2;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.text-truncate-1 {
  display: -webkit-box;
  line-clamp: 1;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
