<script setup lang="ts">
import SearchOverlayBrowseCard from './SearchOverlayBrowseCard.vue';
import type { BrowseCardItem, BrowseTab } from './types';

const { t } = useI18n();

defineProps<{
  recentTerms: string[];
  popularTerms: string[];
  activeBrowseTab: BrowseTab;
  browseCards: BrowseCardItem[];
}>();

const emit = defineEmits<{
  'update:activeBrowseTab': [value: BrowseTab];
  pickTerm: [term: string];
}>();

function selectBrowseTab(tab: BrowseTab) {
  emit('update:activeBrowseTab', tab);
}
</script>

<template>
  <div>
    <div class="mb-4">
      <div>
        <p class="small text-uppercase fw-semibold text-secondary mb-2">
          {{ t('navbar.searchOverlay.recentSearches') }}
        </p>
        <button
          v-for="(term, index) in recentTerms"
          :key="`recent-${index}-${term}`"
          type="button"
          class="btn btn-link d-flex align-items-center w-100 px-0 py-2 text-decoration-none text-reset"
          @click="emit('pickTerm', term)"
        >
          <i class="bi bi-clock-history me-2 text-secondary" />
          <span class="text-start">{{ term }}</span>
        </button>
      </div>
    </div>

    <ul class="nav mb-3 border-bottom border-secondary-subtle" role="tablist">
      <li class="nav-item" role="presentation">
        <button
          type="button"
          class="nav-link px-0 pe-4"
          :class="activeBrowseTab === 'category' ? 'active text-primary' : 'text-secondary'"
          @click="selectBrowseTab('category')"
        >
          {{ t('navbar.searchOverlay.browseByCategory') }}
        </button>
      </li>

      <li class="nav-item" role="presentation">
        <button
          type="button"
          class="nav-link px-0"
          :class="activeBrowseTab === 'city' ? 'active text-primary' : 'text-secondary'"
          @click="selectBrowseTab('city')"
        >
          {{ t('navbar.searchOverlay.browseByCity') }}
        </button>
      </li>
    </ul>

    <div class="row g-2 align-items-stretch">
      <div v-for="card in browseCards" :key="card.id" class="col-4 col-md-3 d-flex">
        <search-overlay-browse-card :item="card" @select="emit('pickTerm', $event)" />
      </div>
    </div>
  </div>
</template>
