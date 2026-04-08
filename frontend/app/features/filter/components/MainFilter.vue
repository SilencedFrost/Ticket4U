<script setup lang="ts">
import type { CategorySummary } from '~/features/event/types/Category';
import FilterPanel from './FilterPanel.vue';
import type { FilterMode, FilterStatusOption, MainFilterSection } from '../types';

const props = withDefaults(
  defineProps<{
    mode?: FilterMode;
    sections?: MainFilterSection[];
    statusOptions?: FilterStatusOption[];
  }>(),
  {
    mode: 'desktop',
    sections: () => ['price', 'category'],
    statusOptions: () => [],
  },
);

const config = useRuntimeConfig();
const { t, te } = useI18n();

const categoryList = ref<CategorySummary[]>([]);

const activeSections = computed(() => new Set(props.sections));

const showPriceSection = computed(() => activeSections.value.has('price'));
const showCategorySection = computed(() => activeSections.value.has('category'));
const showStatusSection = computed(() => activeSections.value.has('status'));

const statusTitle = computed(() => {
  if (te('event_filter.main.status')) {
    return t('event_filter.main.status');
  }

  if (te('common.status')) {
    return t('common.status');
  }

  return 'Status';
});

function getCategoryLabel(categoryName: string) {
  return te(categoryName) ? t(categoryName) : categoryName;
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

watch(
  showCategorySection,
  (shouldShowCategory) => {
    if (shouldShowCategory && categoryList.value.length === 0) {
      getCategories();
    }
  },
  { immediate: true },
);
</script>

<template>
  <FilterPanel
    :mode="props.mode"
    summary-class="btn btn-outline-secondary"
    summary-icon-class="bi bi-funnel fs-5"
    :summary-label="t('event_filter.main.button')"
    panel-class-mobile="position-fixed start-50 translate-middle-x w-100 dev-main-panel-mobile"
    panel-class-desktop="position-absolute start-0 mt-2 dev-main-panel-desktop"
  >
    <div v-if="showPriceSection" class="mb-3">
      <h6 class="fw-bold mb-3 small">{{ t('common.price') }}</h6>
      <div class="d-flex justify-content-between align-items-center">
        <span class="small">{{ t('common.currency.free') }}</span>
        <div class="form-check form-switch m-0">
          <input id="free-event-only" class="form-check-input" type="checkbox" role="switch" />
        </div>
      </div>
    </div>

    <hr v-if="showPriceSection && (showCategorySection || showStatusSection)" class="my-3" />

    <div v-if="showCategorySection" class="mb-3">
      <h6 class="fw-bold mb-3 small">{{ t('common.category') }}</h6>
      <div class="d-flex flex-wrap gap-2">
        <button
          v-for="category in categoryList"
          :key="category.id"
          type="button"
          class="btn btn-sm btn-outline-secondary rounded-pill"
        >
          {{ getCategoryLabel(category.name) }}
        </button>
      </div>
    </div>

    <div v-if="showStatusSection" class="mb-3">
      <h6 class="fw-bold mb-3 small">{{ statusTitle }}</h6>
      <slot name="status" :options="props.statusOptions">
        <div class="d-flex flex-wrap gap-2">
          <button
            v-for="status in props.statusOptions"
            :key="status.value"
            type="button"
            class="btn btn-sm btn-outline-secondary rounded-pill"
          >
            {{ status.label }}
          </button>
        </div>
      </slot>
    </div>

    <div class="d-flex gap-2 mt-3">
      <button type="button" class="btn btn-outline-secondary flex-fill rounded-2">
        {{ t('common.action.reset') }}
      </button>
      <button type="button" class="btn btn-primary flex-fill rounded-2">
        {{ t('common.action.apply') }}
      </button>
    </div>
  </FilterPanel>
</template>

<style scoped>
:deep(.dev-main-panel-mobile) {
  top: 132px;
  width: calc(100vw - 16px);
  max-width: 560px;
  max-height: calc(100vh - 148px);
  overflow-y: auto;
}

:deep(.dev-main-panel-desktop) {
  min-width: 480px;
  width: min(96vw, 560px);
}
</style>
