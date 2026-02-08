<script setup lang="ts">
const { currentTheme } = useTheme();
</script>

<template>
  <div class="d-flex flex-column overflow-hidden h-100">
    <div class="overflow-auto flex-fill">
      <main :class="[
        { 'bg-reactive-primary': currentTheme == 'dark' },
        { 'bg-reactive-secondary': currentTheme == 'light' },
      ]" style="min-height: 100%">
        <nuxt-page />
      </main>
    </div>
  </div>
</template>