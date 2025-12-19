<script setup lang="ts">
import NavBar from '~/components/NavBar.vue';
</script>
<template>
  <div class="d-flex flex-column h-100 flex-fill">
    <header class="sticky-top" style="z-index: 9999">
      <nav-bar />
    </header>
    <main class="d-flex flex-fill">
      <nuxt-page />
    </main>
    <footer>footer</footer>
  </div>
</template>
