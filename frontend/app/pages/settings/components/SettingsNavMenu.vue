<script setup lang="ts">
// <!-- TODO: wire up script -->
</script>

<template>
  <div class="d-flex flex-column h-100 pt-3">
    <!-- Header -->
    <div class="px-3 mb-4 border-bottom border-secondary-subtle pb-3">
      <h2 class="h5 fw-bold text-reactive-primary mb-1">{{ $t('settings.title') }}</h2>
      <p class="small text-reactive-secondary mb-0 text-nowrap">{{ $t('settings.subtitle') }}</p>
    </div>

    <!-- Tab Navigation List -->
    <nav class="d-flex flex-column overflow-y-auto overflow-x-hidden h-100">
      <!-- Tabbing Structure -->
      <ul class="list-unstyled mb-0 d-flex flex-column gap-1">
        <!-- Personal Info Tab (Active by default per instructions) -->
        <li>
          <NuxtLinkLocale
            to="/settings/account"
            class="d-flex align-items-center px-3 py-3 w-100 text-decoration-none menu-item-left settings-nav__item"
            exact-active-class="active"
            style="gap: 8px"
          >
            <i class="bi bi-person fw-bold fs-5"></i>
            <span class="fw-medium small fw-bold text-nowrap">{{
              $t('settings.nav.personal_information')
            }}</span>
          </NuxtLinkLocale>
        </li>
        <!-- Security Tab (for testing UI) -->
        <li>
          <NuxtLinkLocale
            to="/settings/security"
            class="d-flex align-items-center px-3 py-3 w-100 text-decoration-none menu-item-left settings-nav__item"
            exact-active-class="active"
            style="gap: 8px"
          >
            <i class="bi bi-shield-lock fw-bold fs-5"></i>
            <span class="fw-medium small fw-bold text-nowrap">{{
              $t('settings.nav.security')
            }}</span>
          </NuxtLinkLocale>
        </li>
      </ul>
    </nav>
  </div>
</template>

<style scoped>
.menu-item-left {
  transition: all 0.2s ease-in-out;
}

.menu-item-left.active {
  background-color: var(--bg-reactive-secondary);
  border-left-color: var(--bs-primary);
}

.menu-item-left.active > * {
  color: var(--bs-primary) !important;
}
</style>
