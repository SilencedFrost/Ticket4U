<script setup lang="ts">
const { isLoggedIn } = storeToRefs(useUserStore());

type SettingsNavItem = {
  to: string;
  icon: string;
  labelKey: string;
  guestRestricted?: boolean;
};

const navItems = computed(() => {
  // TODO: Không disable tab account/security cho khách. Khi người dùng bấm vào,
  // hiển thị modal "Để sử dụng tính năng này bạn cần đăng nhập" với 2 nút
  // "Quay lại" và "Đăng nhập"; nếu chọn "Đăng nhập" thì chuyển tới trang login
  // và sau khi đăng nhập xong quay lại đúng tab đã chọn.
  const items: SettingsNavItem[] = [
    {
      to: '/settings/account',
      icon: 'bi bi-person-fill',
      labelKey: 'settings.nav.personal_information',
      guestRestricted: true,
    },
    {
      to: '/settings/security',
      icon: 'bi bi-shield-fill',
      labelKey: 'settings.nav.security',
      guestRestricted: true,
    },
    {
      to: '/settings/preferences',
      icon: 'bi bi-palette-fill',
      labelKey: 'settings.nav.preferences',
    },
  ];

  return items.map((item) => ({
    ...item,
    disabled: Boolean(item.guestRestricted && !isLoggedIn.value),
  }));
});
</script>

<template>
  <div class="settings-nav-desktop d-flex flex-column h-100">
    <div class="settings-nav-desktop__header">
      <h2 class="h5 fw-bold text-reactive-primary mb-1">{{ $t('settings.title') }}</h2>
      <p class="small text-reactive-secondary mb-0 text-nowrap">{{ $t('settings.subtitle') }}</p>
    </div>

    <nav class="settings-nav-desktop__nav d-flex flex-column overflow-y-auto overflow-x-hidden h-100">
      <ul class="list-unstyled mb-0 d-flex flex-column gap-1">
        <li v-for="item in navItems" :key="item.to">
          <button v-if="item.disabled" type="button" class="settings-nav__item is-disabled" disabled aria-disabled="true">
            <span class="settings-nav__item-left d-inline-flex align-items-center">
              <i :class="[item.icon, 'text-reactive-secondary']"></i>
              <span>{{ $t(item.labelKey) }}</span>
            </span>
            <i class="bi bi-chevron-right text-reactive-secondary"></i>
          </button>
          <nuxt-link-locale v-else :to="item.to" class="settings-nav__item" exact-active-class="active">
            <span class="settings-nav__item-left d-inline-flex align-items-center">
              <i :class="[item.icon, 'text-reactive-secondary']"></i>
              <span>{{ $t(item.labelKey) }}</span>
            </span>
            <i class="bi bi-chevron-right text-reactive-secondary"></i>
          </nuxt-link-locale>
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
  padding: 8px;
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
  transition:
    background-color 0.2s ease,
    color 0.2s ease;
}

.settings-nav__item-left {
  gap: 10px;
  font-size: 0.95rem;
  font-weight: 600;
}

.settings-nav__item:hover {
  background-color: rgba(var(--bs-secondary-rgb), 0.14);
}

.settings-nav__item.active {
  background-color: rgba(0, 188, 212, 0.14);
}

.settings-nav__item.active .settings-nav__item-left,
.settings-nav__item.active .settings-nav__item-left>i,
.settings-nav__item.active .settings-nav__chevron {
  color: var(--bs-primary);
}

.settings-nav__item.is-disabled {
  width: 100%;
  border: 0;
  text-align: left;
  background-color: transparent;
  opacity: 0.5;
  cursor: not-allowed;
}

.settings-nav__item.is-disabled:hover {
  background-color: transparent;
}
</style>
