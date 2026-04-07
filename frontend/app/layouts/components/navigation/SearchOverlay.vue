<script setup lang="ts">
import { useFormatter } from '~/composables/useFormatter';
import SearchOverlayDesktopView from './search-overlay/SearchOverlayDesktopView.vue';
import SearchOverlayMobileView from './search-overlay/SearchOverlayMobileView.vue';
import { CATEGORY_CARD_SEEDS, CITY_CARD_SEEDS, SEARCH_RESULT_SEEDS } from './search-overlay/data';
import type { BrowseCardItem, BrowseTab, SearchResultViewItem } from './search-overlay/types';

const props = defineProps<{
  query: string;
}>();

const emit = defineEmits<{
  semanticSelected: [value: string];
  close: [];
}>();

const { t } = useI18n();
const { formatPrice } = useFormatter();

const historyStorageKey = 'ticket4u.search-history';
const activeBrowseTab = ref<BrowseTab>('category');
const recentSearchTerms = ref<string[]>([]);
const internalQuery = ref<string>('');

const fallbackRecentTerms = ['The Studio', 'Seminars & Workshops', 'Music'];

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

const normalizedQuery = computed(() => internalQuery.value.trim());
const hasSearchQuery = computed(() => normalizedQuery.value.length > 0);

const filteredResults = computed(() => {
  if (!hasSearchQuery.value) return [];

  const keyword = normalizedQuery.value.toLowerCase();

  return SEARCH_RESULT_SEEDS.filter((event) => {
    const searchableText = [event.title, ...event.tags].join(' ').toLowerCase();
    return searchableText.includes(keyword);
  })
    .slice(0, 6)
    .map<SearchResultViewItem>((event) => ({
      id: event.id,
      title: event.title,
      imageUrl: event.imageUrl,
      priceLabel: formatPrice(event.price) ?? event.price.toLocaleString(),
      releaseDate: event.releaseDate,
    }));
});

const effectiveRecentSearchTerms = computed(() => {
  if (recentSearchTerms.value.length > 0) {
    return recentSearchTerms.value;
  }

  return fallbackRecentTerms;
});

const activeBrowseCards = computed(() => {
  return activeBrowseTab.value === 'category' ? categoryCards.value : cityCards.value;
});

function persistRecentSearchTerms(nextTerms: string[]) {
  if (!import.meta.client) return;
  localStorage.setItem(historyStorageKey, JSON.stringify(nextTerms));
}

function saveRecentSearch(term: string) {
  const normalizedTerm = term.trim();

  if (!normalizedTerm) return;

  const nextTerms = [
    normalizedTerm,
    ...recentSearchTerms.value.filter(
      (currentTerm) => currentTerm.toLowerCase() !== normalizedTerm.toLowerCase(),
    ),
  ].slice(0, 5);

  recentSearchTerms.value = nextTerms;
  persistRecentSearchTerms(nextTerms);
}

function loadRecentSearchTerms() {
  if (!import.meta.client) return;

  const cachedTerms = localStorage.getItem(historyStorageKey);
  if (!cachedTerms) return;

  try {
    const parsedTerms = JSON.parse(cachedTerms);

    if (Array.isArray(parsedTerms)) {
      recentSearchTerms.value = parsedTerms.filter((term) => typeof term === 'string').slice(0, 5);
    }
  } catch {
    localStorage.removeItem(historyStorageKey);
  }
}

function applySearchTerm(term: string) {
  const nextSemantic = term.trim();

  if (!nextSemantic) return;

  internalQuery.value = nextSemantic;
  emit('semanticSelected', nextSemantic);
  saveRecentSearch(nextSemantic);
}

function chooseResult(result: SearchResultViewItem) {
  internalQuery.value = result.title;
  emit('semanticSelected', result.title);
  saveRecentSearch(result.title);
  emit('close');
}

watch(
  () => props.query,
  (nextQuery) => {
    if (nextQuery !== internalQuery.value) {
      internalQuery.value = nextQuery;
    }
  },
  { immediate: true },
);

onMounted(() => {
  loadRecentSearchTerms();
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
        :recent-terms="effectiveRecentSearchTerms"
        :active-browse-tab="activeBrowseTab"
        :browse-cards="activeBrowseCards"
        :results="filteredResults"
        @update:active-browse-tab="activeBrowseTab = $event"
        @pick-term="applySearchTerm"
        @choose-result="chooseResult"
      />
    </div>

    <div class="d-lg-none">
      <search-overlay-mobile-view
        :has-search-query="hasSearchQuery"
        :recent-terms="effectiveRecentSearchTerms"
        :category-cards="categoryCards"
        :city-cards="cityCards"
        :results="filteredResults"
        @pick-term="applySearchTerm"
        @choose-result="chooseResult"
      />
    </div>
  </div>
</template>

<style scoped>
.search-overlay-scroll {
  max-height: calc(100vh - 7rem);
}
</style>
