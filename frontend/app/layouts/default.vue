<script setup lang="ts">
import NavBar from '~/components/navigation/NavBar.vue';
</script>

<template>
  <div class="layout-container">
    <header class="sticky-top" style="z-index: 9999">
      <nav-bar />
    </header>
    <main class="main-content">
      <nuxt-page />
    </main>
    <footer>footer</footer>
  </div>
</template>

<style scoped>
.layout-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
}
</style>
