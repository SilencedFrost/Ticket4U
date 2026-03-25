<template>
  <div class="container py-3 min-vh-100">
    <div class="row gx-3 h-100 flex-nowrap">
      <!-- Left Sidebar Nav Menu -->
      <aside class="d-none d-md-flex flex-column h-100 settings-sidebar me-3">
        <SettingsNavMenu />
      </aside>
      
      <!-- Right Content Slot -->
      <main class="settings-content pb-4">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup>
import SettingsNavMenu from './SettingsNavMenu.vue'

// <!-- TODO: wire up script -->
</script>

<style scoped>
.min-vh-100 {
  min-height: 100vh;
}

.settings-sidebar {
  max-width: 220px;
  min-width: 200px;
  flex-shrink: 0;
}

.settings-content {
  flex: 1;
  min-width: 0;
}
</style>
