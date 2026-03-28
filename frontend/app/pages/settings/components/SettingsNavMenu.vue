<script setup lang="ts">
const navItems = [
  {
    to: '/settings/account',
    icon: 'bi bi-person',
    labelKey: 'settings.nav.personal_information',
  },
  {
    to: '/settings/security',
    icon: 'bi bi-shield-lock',
    labelKey: 'settings.nav.security',
  },
];
</script>

<template>
  <div class="settings-nav-desktop d-flex flex-column h-100 pt-3">
    <div class="settings-nav-desktop__header">
      <h2 class="h5 fw-bold text-reactive-primary mb-1">{{ $t('settings.title') }}</h2>
      <p class="small text-reactive-secondary mb-0 text-nowrap">{{ $t('settings.subtitle') }}</p>
    </div>

    <nav class="settings-nav-desktop__nav d-flex flex-column overflow-y-auto overflow-x-hidden h-100">
      <ul class="list-unstyled mb-0 d-flex flex-column gap-1">
        <li v-for="item in navItems" :key="item.to">
          <NuxtLinkLocale
            :to="item.to"
            class="settings-nav__item"
            exact-active-class="active"
          >
            <span class="settings-nav__item-left">
              <i :class="item.icon"></i>
              <span>{{ $t(item.labelKey) }}</span>
            </span>
            <i class="bi bi-chevron-right settings-nav__chevron"></i>
          </NuxtLinkLocale>
        </li>
      </ul>
    </nav>
  </div>
</template>

<style scoped>
.settings-nav-desktop__header {
  padding: 16px 20px;
  margin-bottom: 0;
  border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.25);
}

.settings-nav-desktop__nav {
  padding-top: 8px;
}

.settings-nav__item {
  min-height: 56px;
  padding: 12px 20px;
  border-radius: 8px;
  color: var(--text-reactive-primary);
  text-decoration: none;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: background-color 0.2s ease, color 0.2s ease;
}

.settings-nav__item-left {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 0.95rem;
  font-weight: 600;
}

.settings-nav__item-left > i,
.settings-nav__chevron {
  color: #8e8e8e;
  font-size: 1.1rem;
}

.settings-nav__item:hover {
  background-color: rgba(var(--bs-secondary-rgb), 0.14);
}

.settings-nav__item.active {
  background-color: rgba(0, 188, 212, 0.14);
}

.settings-nav__item.active .settings-nav__item-left,
.settings-nav__item.active .settings-nav__item-left > i,
.settings-nav__item.active .settings-nav__chevron {
  color: var(--settings-accent);
}
</style>
