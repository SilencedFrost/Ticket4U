<script setup lang="ts">
import NavBar from '~/components/navigation/NavBar.vue';
</script>

<template>
  <div class="layout-container">
    <header class="sticky-top z-3">
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
  min-height: 100vh;
}

.main-content {
  flex: 1;
}
</style>
