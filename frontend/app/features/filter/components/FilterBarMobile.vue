<script setup lang="ts">
import FilterBar from './FilterBar.vue';
import type { FilterBarItem, FilterStatusOption, MainFilterSection } from '../types';

const props = withDefaults(
  defineProps<{
    filters?: FilterBarItem[];
    mainSections?: MainFilterSection[];
    statusOptions?: FilterStatusOption[];
  }>(),
  {
    mainSections: () => ['price', 'category'],
    statusOptions: () => [],
  },
);
</script>

<template>
  <FilterBar
    mode="mobile"
    :filters="props.filters"
    :main-sections="props.mainSections"
    :status-options="props.statusOptions"
  >
    <template v-if="$slots.status" #status="slotProps">
      <slot name="status" v-bind="slotProps" />
    </template>
  </FilterBar>
</template>
