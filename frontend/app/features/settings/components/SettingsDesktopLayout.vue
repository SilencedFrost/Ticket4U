<script setup lang="ts">
  import SettingsNavMenu from './SettingsNavMenu.vue';
</script>

<template>
  <div class="p-3 overflow-visible">
    <div class="d-flex">
      <aside class="d-flex flex-column h-100 settings-sidebar card shadow-sm me-2">
        <settings-nav-menu />
      </aside>
      <main class="w-100">
        <slot />
      </main>
    </div>
  </div>
</template>

<style scoped>
.settings-sidebar {
  width: 280px;
  min-width: 280px;
}
</style>