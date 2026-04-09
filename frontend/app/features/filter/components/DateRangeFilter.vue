<script setup lang="ts">
import FilterPanel from './FilterPanel.vue';

const panelStyle: Record<string, string> = {
  width: 'min(96vw, 920px)',
  maxHeight: 'calc(100vh - 148px)',
  overflowY: 'auto',
};

const filterOptions = [
  { label: 'event_filter.date.all_dates', value: 'all', primary: true },
  { label: 'event_filter.date.today', value: 'today' },
  { label: 'event_filter.date.tomorrow', value: 'tomorrow' },
  { label: 'event_filter.date.this_weekend', value: 'weekend' },
  { label: 'event_filter.date.this_month', value: 'month' },
];
</script>

<template>
  <FilterPanel
    summary-class="btn btn-primary"
    summary-icon-class="bi bi-calendar-event fs-5"
    :summary-label="$t('event_filter.date.all_dates')"
    :summary-mobile-label="$t('event_filter.date.short')"
    panel-class="dev-date-panel"
    :panel-style="panelStyle"
  >
    <div class="d-flex flex-wrap gap-2 pb-3 mb-3 border-bottom">
      <button
        v-for="option in filterOptions"
        :key="option.value"
        type="button"
        :class="[
          'btn btn-sm rounded-2',
          option.primary ? 'btn-primary' : 'btn-outline-secondary text-reactive-secondary',
        ]"
      >
        {{ $t(option.label) }}
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
