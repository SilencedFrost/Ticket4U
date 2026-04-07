<script setup lang="ts">
type FilterMode = 'mobile' | 'desktop';

const props = withDefaults(
  defineProps<{
    mode?: FilterMode;
  }>(),
  {
    mode: 'desktop',
  },
);

const isPanelOpen = ref(false);

function handleDetailsToggle(event: Event) {
  const details = event.target as HTMLDetailsElement;
  isPanelOpen.value = details.open;
}

function closePanel() {
  isPanelOpen.value = false;
}

onMounted(() => {
  isPanelOpen.value = false;
});
</script>

<template>
  <details
    :open="isPanelOpen"
    name="dev-event-filter-group"
    class="position-relative d-inline-block"
    @toggle="handleDetailsToggle"
  >
    <summary
      class="btn btn-info text-white d-inline-flex align-items-center gap-2 list-unstyled text-nowrap"
    >
      <i class="bi bi-calendar-event fs-5"></i>
      <span class="d-none d-sm-inline">{{ $t('event_filter.date.all_dates') }}</span>
      <span class="d-sm-none">{{ $t('event_filter.date.short') }}</span>
      <i class="bi bi-chevron-down ms-auto"></i>
    </summary>

    <div
      class="p-3 p-sm-4 border rounded-3 shadow bg-body z-3"
      :class="
        props.mode === 'mobile'
          ? 'position-fixed start-50 translate-middle-x w-100 dev-date-panel-mobile'
          : 'position-absolute start-0 mt-2 dev-date-panel-desktop'
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

<style scoped>
.dev-date-panel-mobile {
  top: 132px;
  width: calc(100vw - 16px);
  max-width: 560px;
  max-height: calc(100vh - 148px);
  overflow-y: auto;
}

.dev-date-panel-desktop {
  min-width: 640px;
  width: min(96vw, 760px);
}
</style>
