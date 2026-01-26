<script setup lang="ts">
import { useTheme } from '~/composables/useTheme';

const { nextTheme, toggleTheme } = useTheme();
</script>
<template>
  <span @click="toggleTheme()">{{ $t(`theme.${nextTheme}`) }}</span>
</template>
