<script setup lang="ts">
import { useTheme } from '~/composables/useTheme';

useTheme();
</script>

<template>
  <nuxt-layout class="min-vh-100 bg-reactive-primary">
    <nuxt-page />
  </nuxt-layout>
</template>
