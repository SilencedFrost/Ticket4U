<script setup lang="ts">
import DateRangeFilter from './DateRangeFilter.vue';
import MainFilter from './MainFilter.vue';
import type { FilterBarItem, FilterMode, FilterStatusOption, MainFilterSection } from '../types';

const props = withDefaults(
  defineProps<{
    mode?: FilterMode;
    filters?: FilterBarItem[];
    mainSections?: MainFilterSection[];
    statusOptions?: FilterStatusOption[];
  }>(),
  {
    mode: 'desktop',
    mainSections: () => ['price', 'category'],
    statusOptions: () => [],
  },
);

const slots = useSlots();

const resolvedFilters = computed<FilterBarItem[]>(() => {
  if (props.filters?.length) {
    return props.filters;
  }

  return props.mode === 'mobile' ? ['main', 'date'] : ['date', 'main'];
});
</script>

<template>
  <div
    class="d-flex w-100 gap-2 mb-3"
    :class="props.mode === 'mobile' ? 'justify-content-end' : 'justify-content-start'"
  >
    <template v-for="filter in resolvedFilters" :key="filter">
      <DateRangeFilter v-if="filter === 'date'" :mode="props.mode" />

      <MainFilter
        v-else-if="filter === 'main'"
        :mode="props.mode"
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
