<template>
  <div class="pagination-container d-flex justify-content-center align-items-center gap-3 py-4">
    <!-- Previous Button -->
    <button
      class="btn btn-pagination"
      :disabled="currentPage === 0 || loading"
      @click="goToPrevious"
    >
      <i class="bi bi-chevron-left"></i>
      <span class="d-none d-md-inline ms-2">Previous</span>
    </button>

    <!-- Page Info -->
    <div class="page-info d-flex align-items-center gap-2">
      <span class="text-muted">Page</span>
      <input
        v-model.number="pageInput"
        type="number"
        min="1"
        class="form-control page-input"
        :disabled="loading"
        @keyup.enter="goToPageInput"
        @blur="goToPageInput"
      >
      <span class="text-muted">{{ showingText }}</span>
    </div>

    <!-- Next Button -->
    <button
      class="btn btn-pagination"
      :disabled="!hasMore || loading"
      @click="goToNext"
    >
      <span class="d-none d-md-inline me-2">Next</span>
      <i class="bi bi-chevron-right"></i>
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

interface Props {
  currentPage: number;
  pageSize: number;
  hasMore: boolean;
  loading?: boolean;
  totalDisplayed: number; // Number of items currently displayed
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  currentPage: 0,
  pageSize: 20,
  hasMore: true,
});

const emit = defineEmits<{
  goToPage: [page: number];
  previous: [];
  next: [];
}>();

// Internal page input (1-based for user display)
const pageInput = ref(props.currentPage + 1);

// Watch for external page changes
watch(() => props.currentPage, (newPage) => {
  pageInput.value = newPage + 1;
});

// Computed text showing range
const showingText = computed(() => {
  const page = props.currentPage ?? 0;
  const size = props.pageSize ?? 20;
  const displayed = props.totalDisplayed ?? 0;
  const start = page * size + 1;
  const end = page * size + displayed;
  return `(${start}-${end})`;
});

// Navigation handlers
const goToPrevious = () => {
  if (props.currentPage > 0 && !props.loading) {
    emit('previous');
  }
};

const goToNext = () => {
  if (props.hasMore && !props.loading) {
    emit('next');
  }
};

const goToPageInput = () => {
  const targetPage = pageInput.value - 1; // Convert to 0-based
  if (targetPage >= 0 && targetPage !== props.currentPage && !props.loading) {
    emit('goToPage', targetPage);
  } else {
    // Reset to current page if invalid
    pageInput.value = props.currentPage + 1;
  }
};
</script>

<style scoped>
.pagination-container {
  margin-top: 2rem;
  margin-bottom: 2rem;
}

.btn-pagination {
  padding: 0.5rem 1rem;
  border: 1px solid #dee2e6;
  background: white;
  color: #495057;
  border-radius: 0.25rem;
  transition: all 0.2s;
  min-width: 100px;
}

.btn-pagination:hover:not(:disabled) {
  background: #f8f9fa;
  border-color: #adb5bd;
  transform: translateY(-1px);
}

.btn-pagination:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 0.95rem;
}

.page-input {
  width: 70px;
  text-align: center;
  padding: 0.375rem 0.5rem;
  border: 1px solid #dee2e6;
  border-radius: 0.25rem;
}

.page-input:focus {
  border-color: #80bdff;
  outline: 0;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

/* Remove spinner arrows in number input */
.page-input::-webkit-outer-spin-button,
.page-input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.page-input[type="number"] {
  appearance: textfield;
  -moz-appearance: textfield;
}

/* Mobile adjustments */
@media (max-width: 767.98px) {
  .btn-pagination {
    min-width: 50px;
    padding: 0.5rem;
  }

  .page-info {
    font-size: 0.85rem;
  }

  .page-input {
    width: 60px;
  }
}
</style>
