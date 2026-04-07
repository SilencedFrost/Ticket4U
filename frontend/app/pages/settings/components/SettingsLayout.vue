<script setup lang="ts">
import { breakpointsBootstrapV5, useBreakpoints } from '@vueuse/core';
import SettingsDesktopLayout from './SettingsDesktopLayout.vue';
import SettingsMobileLayout from './SettingsMobileLayout.vue';

// ssrWidth fix lỗi layout vỡ sau khi refresh
const isDesktopViewport = useBreakpoints(breakpointsBootstrapV5).greaterOrEqual('md');
</script>

<template>
  <ClientOnly>
    <settings-desktop-layout v-if="isDesktopViewport">
      <slot />
    </settings-desktop-layout>
    <settings-mobile-layout v-else>
      <slot />
    </settings-mobile-layout>

    <template #fallback>
      <!-- Skeleton hiển thị trong lúc JS chưa load -->
      <div class="p-3" style="min-height: 400px" />
    </template>
  </ClientOnly>
</template>