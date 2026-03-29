<script setup lang="ts">
import SettingsLayout from './components/SettingsLayout.vue';
import { breakpointsBootstrapV5 } from '@vueuse/core';

const localePath = useLocalePath();
let desktopMediaQuery: MediaQueryList | null = null;

// Code to handle mobile/desktop layout change, changes on md breakpoint
const handleViewportChange = (event: MediaQueryListEvent) => {
  if (event.matches) {
    redirectToDefaultDesktopTab();
  }
};

function redirectToDefaultDesktopTab() {
  navigateTo(localePath('/settings/account'), { replace: true });
}

onMounted(() => {
  desktopMediaQuery = globalThis.window.matchMedia(`(min-width: ${breakpointsBootstrapV5.md}px)`);

  if (desktopMediaQuery.matches) {
    redirectToDefaultDesktopTab();
  }

  desktopMediaQuery.addEventListener('change', handleViewportChange);
});

onBeforeUnmount(() => {
  if (desktopMediaQuery) {
    desktopMediaQuery.removeEventListener('change', handleViewportChange);
  }
});
</script>

<template>
  <settings-layout>
    <Title>{{ $t('settings.title') }} | Ticket4U</Title>
  </settings-layout>
</template>
