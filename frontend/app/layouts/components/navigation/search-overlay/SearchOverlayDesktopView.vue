<script setup lang="ts">
import SearchOverlayEmptyState from './SearchOverlayEmptyState.vue';
import SearchOverlayResultsState from './SearchOverlayResultsState.vue';
import type { BrowseCardItem, BrowseTab, SearchResultViewItem } from './types';

defineProps<{
  hasSearchQuery: boolean;
  recentTerms: string[];
  activeBrowseTab: BrowseTab;
  browseCards: BrowseCardItem[];
  results: SearchResultViewItem[];
}>();

const emit = defineEmits<{
  'update:activeBrowseTab': [value: BrowseTab];
}>();
</script>

<template>
  <search-overlay-empty-state
    v-if="!hasSearchQuery"
    :recent-terms="recentTerms"
    :active-browse-tab="activeBrowseTab"
    :browse-cards="browseCards"
    @update:active-browse-tab="emit('update:activeBrowseTab', $event)"
  />

  <search-overlay-results-state v-else :results="results" />
</template>
