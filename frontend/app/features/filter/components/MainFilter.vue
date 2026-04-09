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
  overflowY: 'auto',
};

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
    summary-class="btn btn-secondary"
    summary-icon-class="bi bi-funnel fs-5"
    :summary-label="t('event_filter.main.button')"
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
            class="btn btn-sm btn-outline-secondary text-reactive-primary rounded-pill"
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

    <div v-if="showCategorySection && categoryList.length > 0" class="mb-3">
      <h6 class="fw-bold mb-3 small">{{ t('common.category') }}</h6>
      <div class="d-flex flex-wrap gap-2">
        <button
          v-for="category in categoryList"
          :key="category.id"
          type="button"
          class="btn btn-sm btn-outline-secondary text-reactive-primary rounded-pill"
        >
          {{ getCategoryLabel(category.name) }}
        </button>
      </div>
    </div>

    <div class="d-flex gap-2 mt-3">
      <button type="button" class="btn btn-secondary flex-fill">
        {{ t('common.action.reset') }}
      </button>
      <button type="button" class="btn btn-primary flex-fill">
        {{ t('common.action.apply') }}
      </button>
    </div>
  </FilterPanel>
</template>
