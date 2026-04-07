<script setup lang="ts">
import type { CategorySummary } from '~/features/event/types/Category';

type FilterMode = 'mobile' | 'desktop';

const props = withDefaults(
  defineProps<{
    mode?: FilterMode;
  }>(),
  {
    mode: 'desktop',
  },
);

const config = useRuntimeConfig();
const { t, te } = useI18n();

const categoryList = ref<CategorySummary[]>([]);
const isPanelOpen = ref(false);

function handleDetailsToggle(event: Event) {
  const details = event.target as HTMLDetailsElement;
  isPanelOpen.value = details.open;
}

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

onMounted(() => {
  getCategories();
  isPanelOpen.value = false;
});

function closePanel() {
  isPanelOpen.value = false;
}
</script>

<template>
  <details
    :open="isPanelOpen"
    name="dev-event-filter-group"
    class="position-relative d-inline-block"
    @toggle="handleDetailsToggle"
  >
    <summary
      class="btn btn-outline-secondary d-inline-flex align-items-center gap-2 list-unstyled text-nowrap"
    >
      <i class="bi bi-funnel fs-5"></i>
      <span>{{ t('event_filter.main.button') }}</span>
      <i class="bi bi-chevron-down ms-auto"></i>
    </summary>

    <div
      class="p-3 p-sm-4 border rounded-3 shadow bg-body z-3"
      :class="
        props.mode === 'mobile'
          ? 'position-fixed start-50 translate-middle-x w-100 dev-main-panel-mobile'
          : 'position-absolute start-0 mt-2 dev-main-panel-desktop'
      "
    >
      <div v-if="props.mode === 'mobile'" class="d-flex justify-content-end mb-2">
        <button
          type="button"
          class="btn btn-sm p-0 border-0 bg-transparent text-secondary"
          aria-label="Close"
          @click.stop="closePanel"
        >
          <i class="bi bi-x-lg fs-2"></i>
        </button>
      </div>

      <div class="mb-3">
        <h6 class="fw-bold mb-3 small">{{ t('common.price') }}</h6>
        <div class="d-flex justify-content-between align-items-center">
          <span class="small">{{ t('common.currency.free') }}</span>
          <div class="form-check form-switch m-0">
            <input id="free-event-only" class="form-check-input" type="checkbox" role="switch" />
          </div>
        </div>
      </div>

      <hr class="my-3" />

      <div class="mb-3">
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

      <div class="d-flex gap-2 mt-3">
        <button type="button" class="btn btn-outline-secondary flex-fill rounded-2">
          {{ t('common.action.reset') }}
        </button>
        <button type="button" class="btn btn-primary flex-fill rounded-2">
          {{ t('common.action.apply') }}
        </button>
      </div>
    </div>
  </details>
</template>

<style scoped>
.dev-main-panel-mobile {
  top: 132px;
  width: calc(100vw - 16px);
  max-width: 560px;
  max-height: calc(100vh - 148px);
  overflow-y: auto;
}

.dev-main-panel-desktop {
  min-width: 480px;
  width: min(92vw, 560px);
}
</style>
