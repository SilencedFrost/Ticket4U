<script setup lang="ts">
import type { FilterMode } from '../types';

const props = withDefaults(
  defineProps<{
    mode?: FilterMode;
    summaryClass: string;
    summaryIconClass: string;
    summaryLabel: string;
    summaryMobileLabel?: string;
    panelClassDesktop: string;
    panelClassMobile: string;
    panelName?: string;
  }>(),
  {
    mode: 'desktop',
    panelName: 'dev-event-filter-group',
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
    :name="props.panelName"
    class="position-relative d-inline-block"
    @toggle="handleDetailsToggle"
  >
    <summary
      :class="[
        props.summaryClass,
        'd-inline-flex align-items-center gap-2 list-unstyled text-nowrap',
      ]"
    >
      <i :class="props.summaryIconClass"></i>
      <template v-if="props.summaryMobileLabel">
        <span class="d-none d-sm-inline">{{ props.summaryLabel }}</span>
        <span class="d-sm-none">{{ props.summaryMobileLabel }}</span>
      </template>
      <template v-else>
        <span>{{ props.summaryLabel }}</span>
      </template>
      <i class="bi bi-chevron-down ms-auto"></i>
    </summary>

    <div
      class="p-3 p-sm-4 border rounded-3 shadow bg-body z-3"
      :class="props.mode === 'mobile' ? props.panelClassMobile : props.panelClassDesktop"
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

      <slot />
    </div>
  </details>
</template>
