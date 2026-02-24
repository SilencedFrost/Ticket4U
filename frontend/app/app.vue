<script setup lang="ts">
import { useTheme } from '~/composables/useTheme';

useUserStore().refresh();
useTheme();
</script>

<template>
  <nuxt-layout class="bg-reactive-primary">
    <nuxt-page />
  </nuxt-layout>
</template>
