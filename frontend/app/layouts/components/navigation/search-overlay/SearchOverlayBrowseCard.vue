<script setup lang="ts">
import type { BrowseCardItem } from './types';

withDefaults(
  defineProps<{
    item: BrowseCardItem;
    compact?: boolean;
  }>(),
  {
    compact: false,
  },
);

const emit = defineEmits<{
  select: [label: string];
}>();
</script>

<template>
  <button
    type="button"
    class="btn p-0 bg-transparent border-0 text-start w-100 h-100"
    @click="emit('select', item.label)"
  >
    <div class="card text-bg-dark border-0 overflow-hidden h-100">
      <div :class="['ratio', compact ? 'ratio-21x9' : 'ratio-16x9']">
        <img
          :src="item.imageUrl"
          :alt="item.label"
          class="w-100 h-100 object-fit-cover"
          loading="lazy"
        />
        <div class="position-absolute top-0 start-0 end-0 bottom-0" />
        <span
          :class="[
            'position-absolute bottom-0 start-0 fw-semibold text-white d-inline-block w-100 text-truncate',
            compact ? 'p-1 small' : 'p-2',
          ]"
        >
          {{ item.label }}
        </span>
      </div>
    </div>
  </button>
</template>
