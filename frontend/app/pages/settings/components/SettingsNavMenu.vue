<template>
  <div class="d-flex flex-column h-100 pt-3">
    <!-- Header -->
    <div class="px-3 mb-4 border-bottom border-light pb-3">
      <h2 class="h5 fw-bold text-reactive-primary mb-1">Settings</h2>
      <p class="small text-reactive-secondary mb-0 text-nowrap">Quản lý tài khoản</p>
    </div>

    <!-- Tab Navigation List -->
    <nav class="d-flex flex-column overflow-auto h-100">
      <!-- Tabbing Structure -->
      <ul class="list-unstyled mb-0 d-flex flex-column gap-1">
        <!-- Personal Info Tab (Active by default per instructions) -->
        <li>
          <NuxtLink
            to="/settings/personal-information"
            class="nav-item d-flex align-items-center px-3 py-3 w-100 rounded text-decoration-none settings-nav__item active"
            style="gap: 8px"
          >
            <i class="bi bi-person fw-bold fs-5"></i>
            <span class="fw-medium small fw-bold text-nowrap">Thông tin cá nhân</span>
          </NuxtLink>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
// <!-- TODO: wire up script -->
</script>

<style scoped>
.settings-nav__item {
  color: var(--text-reactive-secondary);
  background-color: transparent;
  border-left: 4px solid transparent;
  transition: all 0.2s ease;
}

.settings-nav__item:hover {
  background-color: var(--bg-reactive-gray);
  color: var(--text-reactive-primary);
}

.settings-nav__item.active,
.settings-nav__item.router-link-exact-active {
  background-color: var(--bg-reactive-gray);
  border-left-color: var(--bs-primary);
  color: var(--bs-primary) !important;
}

.settings-nav__item .bi {
  width: 24px;
}

.settings-nav__vip-btn {
  background: linear-gradient(135deg, #af88ff, #6514d6);
  color: #fff;
  border: none;
  transition: transform 0.2s ease;
}

.settings-nav__vip-btn:hover,
.settings-nav__vip-btn:active {
  transform: scale(1.05);
  color: #fff;
}
</style>
