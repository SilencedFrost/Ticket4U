<script setup lang="ts">
import { breakpointsBootstrapV5, useBreakpoints } from '@vueuse/core';
import SettingsDesktopLayout from './SettingsDesktopLayout.vue';
import SettingsMobileLayout from './SettingsMobileLayout.vue';

const isDesktopViewport = useBreakpoints(breakpointsBootstrapV5).greaterOrEqual('md');
const isMounted = ref(false);

</script>

<template>
  <div>
    <div v-if="!isMounted" class="p-3" style="min-height: 400px" />
    
    <ClientOnly @vue:mounted="isMounted = true">
      <settings-desktop-layout v-if="isDesktopViewport">
        <slot />
      </settings-desktop-layout>
      <settings-mobile-layout v-else>
        <slot />
      </settings-mobile-layout>
    </ClientOnly>
  </div>
</template>