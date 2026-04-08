<script setup lang="ts">
import { onClickOutside } from '@vueuse/core';
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
    panelStyleDesktop?: Record<string, string | number>;
    panelStyleMobile?: Record<string, string | number>;
    panelName?: string;
  }>(),
  {
    mode: 'desktop',
    summaryMobileLabel: '',
    panelStyleDesktop: () => ({}),
    panelStyleMobile: () => ({}),
    panelName: 'dev-event-filter-group',
  },
);

const isPanelOpen = ref(false);
const detailsRef = ref<HTMLDetailsElement | null>(null);
const panelInlineStyle = computed(() =>
  props.mode === 'mobile' ? props.panelStyleMobile : props.panelStyleDesktop,
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
    :class="props.mode === 'mobile' ? 'position-static' : 'position-relative'"
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
      :style="panelInlineStyle"
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
