<script setup lang="ts">
import { breakpointsBootstrapV5, onClickOutside, useBreakpoints } from '@vueuse/core';

const props = withDefaults(
  defineProps<{
    summaryClass: string;
    summaryIconClass: string;
    summaryLabel: string;
    summaryMobileLabel?: string;
    panelClass?: string;
    panelStyle?: Record<string, string | number>;
    panelName?: string;
  }>(),
  {
    summaryMobileLabel: '',
    panelClass: '',
    panelStyle: () => ({}),
    panelName: 'dev-event-filter-group',
  },
);

const isPanelOpen = ref(false);
const detailsRef = ref<HTMLDetailsElement | null>(null);
const breakpoints = useBreakpoints(breakpointsBootstrapV5);
const isDesktop = breakpoints.greaterOrEqual('md');
const panelInlineStyle = computed(() => props.panelStyle);
const detailsPositionClass = computed(() =>
  isDesktop.value ? 'position-relative' : 'position-static',
);
const panelPositionClass = computed(() =>
  isDesktop.value
    ? 'position-absolute top-100 start-0 mt-2'
    : 'position-absolute top-100 start-50 translate-middle-x mt-2',
);

function handleDetailsToggle(event: Event) {
  const details = event.target as HTMLDetailsElement;
  isPanelOpen.value = details.open;
}

function closePanel() {
  isPanelOpen.value = false;
}

onClickOutside(detailsRef, () => {
  if (isPanelOpen.value) {
    closePanel();
  }
});
</script>

<template>
  <details
    ref="detailsRef"
    :open="isPanelOpen"
    :name="props.panelName"
    class="d-inline-block"
    :class="detailsPositionClass"
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
      :class="[panelPositionClass, props.panelClass]"
      :style="panelInlineStyle"
    >
      <div class="d-flex justify-content-end mb-2 d-md-none">
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
