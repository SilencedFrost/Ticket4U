<template>
  <details name="dev-event-filter-group" class="position-relative">
    <summary class="btn btn-info text-white d-inline-flex align-items-center gap-2 list-unstyled">
      <i class="bi bi-calendar-event fs-5"></i>
      <span class="d-none d-sm-inline">{{ $t('event_filter.date.all_dates') }}</span>
      <span class="d-sm-none">{{ $t('event_filter.date.short') }}</span>
      <i class="bi bi-chevron-down ms-auto"></i>
    </summary>

    <div
      class="dropdown-menu dropdown-menu-end position-absolute end-0 d-block mt-2 p-4 border rounded-3 shadow bg-body z-3"
      style="min-width: 640px; width: min(96vw, 760px)"
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
    </div>
  </details>
</template>
