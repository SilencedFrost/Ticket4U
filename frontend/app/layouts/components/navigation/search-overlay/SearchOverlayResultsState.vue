<script setup lang="ts">
import SearchOverlayResultItem from './SearchOverlayResultItem.vue';
import type { SearchResultViewItem } from './types';

const { t } = useI18n();

defineProps<{
  results: SearchResultViewItem[];
}>();

const emit = defineEmits<{
  chooseResult: [result: SearchResultViewItem];
}>();
</script>

<template>
  <div v-if="results.length > 0" class="list-group list-group-flush overflow-hidden">
    <search-overlay-result-item
      v-for="result in results"
      :key="result.id"
      :item="result"
      @choose="emit('chooseResult', result)"
    />
  </div>

  <div v-else class="text-center py-4 text-secondary">
    <i class="bi bi-search fs-3" />
    <p class="mb-0 mt-2">{{ t('navbar.searchOverlay.noResult') }}</p>
  </div>
</template>
