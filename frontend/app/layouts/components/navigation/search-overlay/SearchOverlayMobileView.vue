<script setup lang="ts">
import SearchOverlayBrowseCard from './SearchOverlayBrowseCard.vue';
import SearchOverlayResultsState from './SearchOverlayResultsState.vue';
import type { BrowseCardItem, SearchResultViewItem } from './types';

const { t } = useI18n();

defineProps<{
  hasSearchQuery: boolean;
  recentTerms: string[];
  categoryCards: BrowseCardItem[];
  cityCards: BrowseCardItem[];
  results: SearchResultViewItem[];
}>();

const emit = defineEmits<{
  pickTerm: [term: string];
  chooseResult: [result: SearchResultViewItem];
}>();
</script>

<template>
  <div class="px-1">
    <div v-if="!hasSearchQuery">
      <div class="mb-4">
        <div class="list-group list-group-flush rounded-3 overflow-hidden">
          <button
            v-for="(term, index) in recentTerms"
            :key="`mobile-recent-${index}-${term}`"
            type="button"
            class="bg-reactive-primary list-group-item list-group-item-action border-0 d-flex align-items-center w-100 px-2 py-1 text-decoration-none text-reset text-start"
            @click="emit('pickTerm', term)"
          >
            <i class="bi bi-clock-history me-2 text-secondary" />
            <span class="text-start small">{{ term }}</span>
          </button>
        </div>
      </div>

      <div class="mb-4">
        <h3 class="h5 fw-bold mb-2">{{ t('navbar.searchOverlay.browseByCategory') }}</h3>
        <div class="row flex-nowrap g-1 overflow-x-auto pb-1 mobile-scroll-strip">
          <div
            v-for="card in categoryCards"
            :key="`mobile-category-${card.id}`"
            class="col-6 col-sm-5 col-md-4"
          >
            <search-overlay-browse-card :item="card" compact @select="emit('pickTerm', $event)" />
          </div>
        </div>
      </div>

      <div>
        <h3 class="h5 fw-bold mb-2">{{ t('navbar.searchOverlay.browseByCity') }}</h3>
        <div class="row flex-nowrap g-1 overflow-x-auto pb-1 mobile-scroll-strip">
          <div
            v-for="card in cityCards"
            :key="`mobile-city-${card.id}`"
            class="col-6 col-sm-5 col-md-4"
          >
            <search-overlay-browse-card :item="card" compact @select="emit('pickTerm', $event)" />
          </div>
        </div>
      </div>
    </div>

    <search-overlay-results-state
      v-else
      :results="results"
      @choose-result="emit('chooseResult', $event)"
    />
  </div>
</template>

<style scoped>
.mobile-scroll-strip {
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.mobile-scroll-strip::-webkit-scrollbar {
  display: none;
}
</style>
