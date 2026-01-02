<script setup lang="ts">
import NavBar from '~/components/navigation/NavBar.vue';
</script>

<template>
  <div class="d-flex flex-column overflow-hidden">
    <header class="sticky-top z-3">
      <nav-bar />
    </header>
    <div class="overflow-auto">
      <main>
        <nuxt-page />
      </main>
      <footer>footer</footer>
    </div>
  </div>
</template>
