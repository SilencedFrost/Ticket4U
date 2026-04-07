<script setup lang="ts">
import type { SearchResultViewItem } from './types';
defineProps<{
  item: SearchResultViewItem;
}>();
</script>

<template>
  <button
    type="button"
    class="bg-reactive-primary list-group-item list-group-item-action border-0 px-2 px-md-3 py-2 text-reset text-start"
  >
    <div class="row g-2 align-items-center">
      <div class="col-4 col-md-2">
        <div class="ratio ratio-4x3">
          <img
            :src="item.imageUrl"
            :alt="item.title"
            class="w-100 h-100 object-fit-cover rounded-2 border border-secondary-subtle"
            loading="lazy"
          />
        </div>
      </div>

      <div class="col-8 col-md-10 text-start">
        <p class="mb-1 fw-semibold small text-truncate">{{ item.title }}</p>
        <p class="mb-1 text-success fw-semibold small">{{ item.priceLabel }}</p>
        <p class="mb-0 text-secondary small">{{ item.releaseDate }}</p>
      </div>
    </div>
  </button>
</template>
