<script setup lang="ts">
import SettingsNavMenu from './SettingsNavMenu.vue';
import '../style/settings.css';

// <!-- TODO: wire up script -->
</script>

<template>
  <div class="container py-3 min-vh-100">
    <div class="row gx-3 h-100">
      <!-- Left Sidebar Nav Menu -->
      <aside class="d-none d-md-flex flex-column h-100 settings-sidebar me-3 bg-reactive-primary rounded-4 p-2 shadow-sm">
        <SettingsNavMenu />
      </aside>

      <!-- Right Content Slot -->
      <main class="settings-content settings-main-content pb-4">
        <slot />
      </main>
    </div>
  </div>

  <!-- Mobile: bottom nav bar -->
  <nav class="d-flex d-md-none settings-bottom-nav">
    <NuxtLinkLocale
      to="/settings/account"
      class="settings-bottom-nav-item"
      exact-active-class="active"
    >
      <i class="bi bi-person"></i>
      <span class="text-center">{{ $t('settings.nav.personal_information') }}</span>
    </NuxtLinkLocale>
    <NuxtLinkLocale
      to="/settings/security"
      class="settings-bottom-nav-item"
      exact-active-class="active"
    >
      <i class="bi bi-shield-lock"></i>
      <span class="text-center">{{ $t('settings.nav.security') }}</span>
    </NuxtLinkLocale>
  </nav>
</template>

<style scoped>
.settings-sidebar {
  width: fit-content;
  min-width: 240px;
  flex-shrink: 0;
}

.settings-content {
  flex: 1;
  min-width: 0;
}
</style>
