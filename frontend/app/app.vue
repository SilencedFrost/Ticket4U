<script setup lang="ts">
import { useTheme } from '~/composables/useTheme';

// TODO: Uncomment when user-service is running on port 8082
// useUserStore().refresh();
useTheme();
</script>

<template>
  <nuxt-layout class="vh-100 bg-reactive-primary">
    <nuxt-page />
  </nuxt-layout>
</template>
