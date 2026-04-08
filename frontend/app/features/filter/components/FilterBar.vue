<script setup lang="ts">
import { breakpointsBootstrapV5, useBreakpoints } from '@vueuse/core';
import DateRangeFilter from './DateRangeFilter.vue';
import MainFilter from './MainFilter.vue';
import type { FilterBarItem, FilterMode, FilterStatusOption, MainFilterSection } from '../types';

const props = withDefaults(
  defineProps<{
    mode?: FilterMode | 'auto';
    filters?: FilterBarItem[];
    mainSections?: MainFilterSection[];
    statusOptions?: FilterStatusOption[];
  }>(),
  {
    mode: 'auto',
    filters: () => [],
    mainSections: () => ['price', 'category'],
    statusOptions: () => [],
  },
);

const slots = useSlots();
const breakpoints = useBreakpoints(breakpointsBootstrapV5);
const isDesktop = breakpoints.greaterOrEqual('md');

const resolvedMode = computed<FilterMode>(() => {
  if (props.mode !== 'auto') return props.mode;

  return isDesktop.value ? 'desktop' : 'mobile';
});

const resolvedFilters = computed<FilterBarItem[]>(() => {
  if (props.filters?.length) {
    return props.filters;
  }

  return resolvedMode.value === 'mobile' ? ['main', 'date'] : ['date', 'main'];
});
</script>

<template>
  <div
    class="d-flex w-100 gap-2 mb-3 position-relative"
    :class="resolvedMode === 'mobile' ? 'justify-content-end' : 'justify-content-start'"
  >
    <template v-for="filter in resolvedFilters" :key="filter">
      <DateRangeFilter v-if="filter === 'date'" :mode="resolvedMode" />

      <MainFilter
        v-else-if="filter === 'main'"
        :mode="resolvedMode"
        :sections="props.mainSections"
        :status-options="props.statusOptions"
      >
        <template v-if="slots.status" #status="slotProps">
          <slot name="status" v-bind="slotProps" />
        </template>
      </MainFilter>
    </template>
  </div>
</template>
