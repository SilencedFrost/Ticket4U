<script setup lang="ts">
import FilterPanel from './FilterPanel.vue';
import type { FilterMode } from '../types';

const props = withDefaults(
  defineProps<{
    mode?: FilterMode;
  }>(),
  {
    mode: 'desktop',
  },
);
</script>

<template>
  <FilterPanel
    :mode="props.mode"
    summary-class="btn btn-info text-white"
    summary-icon-class="bi bi-calendar-event fs-5"
    :summary-label="$t('event_filter.date.all_dates')"
    :summary-mobile-label="$t('event_filter.date.short')"
    panel-class-mobile="position-fixed start-50 translate-middle-x w-100 dev-date-panel-mobile"
    panel-class-desktop="position-absolute start-0 mt-2 dev-date-panel-desktop"
  >
    <div class="d-flex flex-wrap gap-2 pb-3 mb-3 border-bottom">
      <button type="button" class="btn btn-sm btn-info text-white rounded-2">
        {{ $t('event_filter.date.all_dates') }}
      </button>
      <button type="button" class="btn btn-sm btn-outline-secondary rounded-2">
        {{ $t('event_filter.date.today') }}
      </button>
      <button type="button" class="btn btn-sm btn-outline-secondary rounded-2">
        {{ $t('event_filter.date.tomorrow') }}
      </button>
      <button type="button" class="btn btn-sm btn-outline-secondary rounded-2">
        {{ $t('event_filter.date.this_weekend') }}
      </button>
      <button type="button" class="btn btn-sm btn-outline-secondary rounded-2">
        {{ $t('event_filter.date.this_month') }}
      </button>
    </div>

    <div class="row g-3 mb-3">
      <div class="col-12 col-sm-6">
        <label class="form-label small">{{ $t('event_filter.date.from_date') }}</label>
        <input type="date" class="form-control" />
      </div>
      <div class="col-12 col-sm-6">
        <label class="form-label small">{{ $t('event_filter.date.to_date') }}</label>
        <input type="date" class="form-control" />
      </div>
    </div>

    <div class="d-flex gap-3 pt-3">
      <button type="button" class="btn btn-outline-secondary flex-fill rounded-2">
        {{ $t('common.action.reset') }}
      </button>
      <button type="button" class="btn btn-primary flex-fill rounded-2">
        {{ $t('common.action.apply') }}
      </button>
    </div>
  </FilterPanel>
</template>

<style scoped>
:deep(.dev-date-panel-mobile) {
  top: 132px;
  width: calc(100vw - 16px);
  max-width: 560px;
  max-height: calc(100vh - 148px);
  overflow-y: auto;
}

:deep(.dev-date-panel-desktop) {
  min-width: 700px;
  width: min(96vw, 920px);
}
</style>
