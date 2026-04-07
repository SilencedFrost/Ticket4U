<script setup lang="ts">
import type { CategorySummary } from '~/features/event/types/Category';

const config = useRuntimeConfig();
const { t, te } = useI18n();

const categoryList = ref<CategorySummary[]>([]);

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
});
</script>

<template>
  <details name="dev-event-filter-group" class="position-relative">
    <summary class="btn btn-outline-secondary d-inline-flex align-items-center gap-2 list-unstyled">
      <i class="bi bi-funnel fs-5"></i>
      <span class="d-none d-sm-inline">Bộ lọc</span>
      <span class="d-sm-none">Lọc</span>
      <i class="bi bi-chevron-down ms-auto"></i>
    </summary>

    <div
      class="dropdown-menu dropdown-menu-end position-absolute end-0 d-block mt-2 p-4 border rounded-3 shadow bg-body z-3"
      style="min-width: 480px; width: min(92vw, 560px)"
    >
      <div class="mb-3">
        <h6 class="fw-bold mb-3 small">Giá tiền</h6>
        <div class="d-flex justify-content-between align-items-center">
          <span class="small">Miễn phí</span>
          <div class="form-check form-switch m-0">
            <input id="free-event-only" class="form-check-input" type="checkbox" role="switch" />
          </div>
        </div>
      </div>

      <hr class="my-3" />

      <div class="mb-3">
        <h6 class="fw-bold mb-3 small">Thể loại</h6>
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
          Thiết lập lại
        </button>
        <button type="button" class="btn btn-primary flex-fill rounded-2">Áp dụng</button>
      </div>
    </div>
  </details>
</template>
