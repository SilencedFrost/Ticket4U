<script setup lang="ts">
import FilterPanel from './FilterPanel.vue';

const panelStyle: Record<string, string> = {
  width: 'min(96vw, 500px)',
  maxHeight: 'calc(100vh - 148px)',
  overflowY: 'auto',
};

const filterOptions = [
  { label: 'common.date.all_dates', value: 'all', primary: true },
  { label: 'common.date.today', value: 'today' },
  { label: 'common.date.tomorrow', value: 'tomorrow' },
  { label: 'common.date.this_weekend', value: 'weekend' },
  { label: 'common.date.this_month', value: 'month' },
];
</script>

<template>
  <FilterPanel
    summary-class="btn btn-primary"
    summary-icon-class="bi bi-calendar-event fs-5"
    :summary-label="$t('common.date.all_dates')"
    :summary-mobile-label="$t('common.date.short')"
    panel-class="dev-date-panel"
    :panel-style="panelStyle"
  >
    <div class="d-flex flex-wrap gap-2 pb-3 mb-3 border-bottom">
      <button
        v-for="option in filterOptions"
        :key="option.value"
        type="button"
        :class="[
          'btn btn-sm',
          option.primary ? 'btn-primary' : 'btn-outline-secondary text-reactive-primary',
        ]"
      >
        {{ $t(option.label) }}
      </button>
    </div>

    <div class="row g-3 mb-3">
      <div class="col-12 col-sm-6">
        <label class="form-label small">{{ $t('common.date.from_date') }}</label>
        <input type="date" class="form-control" />
      </div>
      <div class="col-12 col-sm-6">
        <label class="form-label small">{{ $t('common.date.to_date') }}</label>
        <input type="date" class="form-control" />
      </div>
    </div>

    <div class="d-flex gap-3 pt-3">
      <button type="button" class="btn btn-secondary flex-fill">
        {{ $t('common.action.reset') }}
      </button>
      <button type="button" class="btn btn-primary flex-fill">
        {{ $t('common.action.apply') }}
      </button>
    </div>
  </FilterPanel>
</template>
