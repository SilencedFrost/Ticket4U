<script setup lang="ts">
import type { CategorySummary } from '~/features/event/types/Category';
import FilterPanel from './FilterPanel.vue';
import type { FilterStatusOption, MainFilterSection } from '../types';

const props = withDefaults(
  defineProps<{
    sections?: MainFilterSection[];
    statusOptions?: FilterStatusOption[];
  }>(),
  {
    sections: () => ['price', 'category'],
    statusOptions: () => [],
  },
);

const panelStyle: Record<string, string> = {
  width: 'min(96vw, 400px)',
  maxHeight: 'calc(100vh - 148px)',
  overflowY: 'visible',
};

const config = useRuntimeConfig();
const { t, te } = useI18n();

const categoryList = ref<CategorySummary[]>([]);

const activeSections = computed(() => new Set(props.sections));

const showPriceSection = computed(() => activeSections.value.has('price'));
const showCategorySection = computed(() => activeSections.value.has('category'));
const showStatusSection = computed(() => activeSections.value.has('status'));

const searchQuery = ref('');
const isCategoryInputFocused = ref(false);
const selectedCategories = ref<CategorySummary[]>([]);

const filteredList = computed(getFilteredList);

function removeVietnameseTones(str: string): string {
  return str
    .normalize('NFD')
    .replaceAll(/[\u0300-\u036f]/g, '')
    .replaceAll('đ', 'd')
    .replaceAll('Đ', 'D')
    .trim();
}

// TODO: implement multi-language category search.
function getFilteredList() {
  const query = searchQuery.value.toLowerCase();
  const normalizedQuery = removeVietnameseTones(query);

  if (!categoryList.value.length) return [];

  let list = categoryList.value.filter(
    (item) => !selectedCategories.value.some((selected) => selected.id === item.id),
  );

  if (query) {
    list = list.filter((item) => {
      const label = getCategoryLabel(item.name).toLowerCase();
      return removeVietnameseTones(label).includes(normalizedQuery);
    });

    list = list.sort((a, b) => {
      const labelA = removeVietnameseTones(getCategoryLabel(a.name).toLowerCase());
      const labelB = removeVietnameseTones(getCategoryLabel(b.name).toLowerCase());

      const aStartsWith = labelA.startsWith(normalizedQuery) ? 1 : 0;
      const bStartsWith = labelB.startsWith(normalizedQuery) ? 1 : 0;

      if (aStartsWith !== bStartsWith) {
        return bStartsWith - aStartsWith;
      }

      return labelA.localeCompare(labelB);
    });
  }

  return list;
}

const shouldShowOverlay = computed(getShouldShowOverlay);

function getShouldShowOverlay() {
  return isCategoryInputFocused.value && filteredList.value.length > 0;
}

const statusTitle = computed(getStatusTitle);

function getStatusTitle() {
  if (te('common.status')) {
    return t('common.status');
  }

  return 'Status';
}

function selectItem(item: CategorySummary) {
  if (!selectedCategories.value.find((cat) => cat.id === item.id)) {
    selectedCategories.value.push(item);
  }
  searchQuery.value = '';
}

function removeCategory(id: number) {
  selectedCategories.value = selectedCategories.value.filter((cat) => cat.id !== id);
}

function getCategoryLabel(categoryName: string) {
  const fallbackCategoryKey = `categories.${categoryName.toLowerCase()}`;

  if (te(categoryName)) {
    return t(categoryName);
  }

  if (te(fallbackCategoryKey)) {
    return t(fallbackCategoryKey);
  }

  return categoryName;
}

function resetCategorySearch() {
  searchQuery.value = '';
  selectedCategories.value = [];
  isCategoryInputFocused.value = false;
}

async function getCategories() {
  try {
    categoryList.value = await $fetch(`${config.public.eventServiceUrl}/public/categories`, {
      method: 'GET',
    });
  } catch (e) {
    console.log(e);
    categoryList.value = [];
  }
}

function handleCategorySectionVisibility(shouldShowCategory: boolean) {
  if (shouldShowCategory && categoryList.value.length === 0) {
    getCategories();
  }
}

watch(showCategorySection, handleCategorySectionVisibility, { immediate: true });
</script>

<template>
  <FilterPanel
    summary-class="btn btn-secondary"
    summary-icon-class="bi bi-funnel fs-5"
    :summary-label="t('common.action.filter')"
    panel-class="dev-main-panel"
    :panel-style="panelStyle"
  >
    <div v-if="showPriceSection" class="mb-3">
      <h6 class="fw-bold mb-3 small">{{ t('common.price') }}</h6>
      <div class="d-flex justify-content-between align-items-center">
        <span class="small">{{ t('common.currency.free') }}</span>
        <div class="form-check form-switch m-0">
          <input
            id="free-event-only"
            class="form-check-input bg-reactive-gray"
            type="checkbox"
            role="switch"
          />
        </div>
      </div>
    </div>

    <hr v-if="showPriceSection && (showCategorySection || showStatusSection)" class="my-3" />

    <div v-if="showStatusSection" class="mb-3">
      <h6 class="fw-bold mb-3 small">{{ statusTitle }}</h6>
      <slot name="status" :options="props.statusOptions">
        <div class="d-flex flex-wrap gap-2">
          <button
            v-for="status in props.statusOptions"
            :key="status.value"
            type="button"
            class="btn btn-sm btn-outline-secondary text-reactive-primary"
          >
            {{ status.label }}
          </button>
        </div>
      </slot>
    </div>

    <hr
      v-if="
        showPriceSection && (showCategorySection || showStatusSection) && categoryList.length > 0
      "
      class="my-3"
    />

    <div v-if="showCategorySection && categoryList.length > 0" class="mb-3 position-relative">
      <h6 class="fw-bold mb-3 small">{{ t('common.category') }}</h6>

      <div
        v-if="selectedCategories.length > 0"
        class="d-flex flex-wrap gap-2 mb-2 overflow-auto align-content-start"
        style="max-height: 168px"
      >
        <div
          v-for="cat in selectedCategories"
          :key="cat.id"
          class="btn btn-secondary btn-sm d-inline-flex align-items-stretch p-0 overflow-hidden text-nowrap"
        >
          <span class="px-2 py-1 d-inline-flex align-items-center">{{
            getCategoryLabel(cat.name)
          }}</span>
          <button
            type="button"
            class="border-0 border-start border-light border-opacity-25 bg-transparent text-white d-inline-flex align-items-center justify-content-center px-2 flex-shrink-0"
            @click="removeCategory(cat.id)"
          >
            <i class="bi bi-x-lg small"></i>
          </button>
        </div>
      </div>

      <div class="input-group input-group-sm">
        <input
          v-model="searchQuery"
          type="text"
          class="form-control"
          :placeholder="t('placeholder.category')"
          @input="searchQuery = ($event.target as HTMLInputElement).value"
          @focus="isCategoryInputFocused = true"
          @blur="isCategoryInputFocused = false"
        />
      </div>

      <ul
        v-if="shouldShowOverlay"
        class="dropdown-menu d-block w-100 mt-1 overflow-auto p-1 bg-reactive-primary"
        style="max-height: 220px; scrollbar-width: none; -ms-overflow-style: none"
      >
        <li v-for="item in filteredList" :key="item.id">
          <button
            class="dropdown-item rounded small py-2"
            type="button"
            @mousedown.prevent
            @click="selectItem(item)"
          >
            {{ getCategoryLabel(item.name) }}
          </button>
        </li>
      </ul>
    </div>

    <div class="d-flex gap-2 mt-3">
      <button type="button" class="btn btn-secondary flex-fill" @click="resetCategorySearch">
        {{ t('common.action.reset') }}
      </button>
      <button type="button" class="btn btn-primary flex-fill">
        {{ t('common.action.apply') }}
      </button>
    </div>
  </FilterPanel>
</template>
