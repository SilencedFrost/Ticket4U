<script setup lang="ts">
const { currentTheme } = useTheme()
</script>

<template>
  <div
      class="d-flex flex-column overflow-hidden h-100"
      :class="[
      { 'bg-reactive-primary': currentTheme === 'dark' },
      { 'bg-reactive-secondary': currentTheme === 'light' },
    ]"
  >
    <nuxt-page />
  </div>
</template>