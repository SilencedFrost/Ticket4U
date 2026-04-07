<script setup lang="ts">
import { useFormatter } from '~/composables/useFormatter';
import SearchOverlayDesktopView from './search-overlay/SearchOverlayDesktopView.vue';
import SearchOverlayMobileView from './search-overlay/SearchOverlayMobileView.vue';
import {
  CATEGORY_CARD_SEEDS,
  CITY_CARD_SEEDS,
  RECENT_SEARCH_SEEDS,
  SEARCH_RESULT_SEEDS,
} from './search-overlay/data';
import type { BrowseCardItem, BrowseTab, SearchResultViewItem } from './search-overlay/types';

const props = defineProps<{
  query: string;
}>();

const { t } = useI18n();
const { formatPrice } = useFormatter();

const activeBrowseTab = ref<BrowseTab>('category');
const hasSearchQuery = computed(() => props.query.trim().length > 0);

const categoryCards = computed<BrowseCardItem[]>(() =>
  CATEGORY_CARD_SEEDS.map((seed) => ({
    id: seed.id,
    label: t(seed.labelKey),
    imageUrl: seed.imageUrl,
  })),
);

const cityCards = computed<BrowseCardItem[]>(() =>
  CITY_CARD_SEEDS.map((seed) => ({
    id: seed.id,
    label: t(seed.labelKey),
    imageUrl: seed.imageUrl,
  })),
);

const resultItems = computed<SearchResultViewItem[]>(() => {
  if (!hasSearchQuery.value) return [];

  return SEARCH_RESULT_SEEDS.map<SearchResultViewItem>((event) => ({
    id: event.id,
    title: event.title,
    imageUrl: event.imageUrl,
    priceLabel: formatPrice(event.price) ?? event.price.toLocaleString(),
    releaseDate: event.releaseDate,
  }));
});

const activeBrowseCards = computed(() => {
  return activeBrowseTab.value === 'category' ? categoryCards.value : cityCards.value;
});
</script>

<template>
  <div
    class="search-overlay-scroll w-100 rounded-4 border border-secondary-subtle bg-reactive-primary text-reactive-primary p-3 shadow-lg overflow-auto"
    @click.stop
  >
    <div class="d-none d-lg-block">
      <search-overlay-desktop-view
        :has-search-query="hasSearchQuery"
        :recent-terms="RECENT_SEARCH_SEEDS"
        :active-browse-tab="activeBrowseTab"
        :browse-cards="activeBrowseCards"
        :results="resultItems"
        @update:active-browse-tab="activeBrowseTab = $event"
      />
    </div>

    <div class="d-lg-none">
      <search-overlay-mobile-view
        :has-search-query="hasSearchQuery"
        :recent-terms="RECENT_SEARCH_SEEDS"
        :category-cards="categoryCards"
        :city-cards="cityCards"
        :results="resultItems"
      />
    </div>
  </div>
</template>

<style scoped>
.search-overlay-scroll {
  max-height: calc(100vh - 7rem);
}
</style>
