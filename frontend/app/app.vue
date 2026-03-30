<script setup lang="ts">
import { useTheme } from '~/composables/useTheme';
const { initializeLocation } = useLocation();

onMounted(async () => {
  if (import.meta.client) {
    await initializeLocation();
  }
});

useUserStore().refresh();
useTheme();
</script>

<template>
  <nuxt-layout class="vh-100 bg-reactive-primary">
    <nuxt-page />
  </nuxt-layout>
  <LocationPermissionPopup />
</template>
