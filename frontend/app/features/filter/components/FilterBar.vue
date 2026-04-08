<script setup lang="ts">
import DateRangeFilter from './DateRangeFilter.vue';
import MainFilter from './MainFilter.vue';
import type { FilterBarItem, FilterStatusOption, MainFilterSection } from '../types';

const props = withDefaults(
  defineProps<{
    filters?: FilterBarItem[];
    mainSections?: MainFilterSection[];
    statusOptions?: FilterStatusOption[];
  }>(),
  {
    filters: () => [],
    mainSections: () => ['price', 'category'],
    statusOptions: () => [],
  },
);

const slots = useSlots();

const resolvedFilters = computed<FilterBarItem[]>(() => {
  if (props.filters?.length) {
    return props.filters;
  }

  return ['main', 'date'];
});
</script>

<template>
  <div
    class="d-flex w-100 gap-2 mb-3 position-relative justify-content-end justify-content-md-start"
  >
    <template v-for="filter in resolvedFilters" :key="filter">
      <DateRangeFilter v-if="filter === 'date'" />

      <MainFilter
        v-else-if="filter === 'main'"
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
