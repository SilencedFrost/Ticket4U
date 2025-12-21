<script setup lang="ts">
import { onClickOutside } from '@vueuse/core';
import AccountDropDown from './menus/AccountDropDown.vue';
import BurgerDropDown from './menus/BurgerDropDown.vue';

const { locale } = useI18n();
const menuList = reactive({
  none: null,
  account: AccountDropDown,
  burger: BurgerDropDown,
});
const currentMenu = ref<Component | null>(null);
const searchInput = ref<HTMLInputElement | null>(null);
const menuContainer = ref<HTMLElement | null>(null);
const useUser = useUserStore();

function focusSearch() {
  searchInput.value?.focus();
}

function clearSearch() {
  if (searchInput.value) {
    searchInput.value.value = '';
  }
}

function toggleMenu(menuKey?: keyof typeof menuList) {
  const targetMenu = menuKey ? menuList[menuKey] : menuList.none;
  currentMenu.value = currentMenu.value === targetMenu ? menuList.none : targetMenu;
}

onClickOutside(menuContainer, () => {
  toggleMenu();
});
</script>
<template>
  <div ref="menuContainer">
    <nav class="bg-reactive-primary shadow-sm">
      <div class="container-fluid p-0 position-relative d-flex">
        <!-- Logo -->
        <div class="nav-container">
          <nuxt-link to="/">
            <img src="/logo-primary.png" class="h-100" />
          </nuxt-link>
        </div>
        <!-- Hover buttons -->
        <div class="d-none d-md-flex ms-lg-5">
          <div class="nav-item">
            <span>Events</span>
          </div>
          <div class="nav-item">
            <span>Contact us</span>
          </div>
        </div>
        <!-- Search bar -->
        <div class="nav-container position-absolute start-50 translate-middle-x">
          <i class="bi bi-search text-clickable me-2" @click="focusSearch()" /><input
            ref="searchInput"
            type="text"
            class="search-field text-reactive-primary input-underline"
            :placeholder="$t('placeholder.search')"
            @blur="clearSearch()"
          />
        </div>
        <!-- Function buttons -->
        <div class="nav-container ms-auto">
          <!-- Ticket button -->
          <div
            v-if="useUser.isLoggedIn === true"
            class="d-flex text-clickable me-2 pe-2 border-end border-2"
          >
            <span class="d-none d-sm-flex me-2">{{ $t('common.tickets') }}</span>
            <i class="bi bi-ticket" />
          </div>
          <!-- Language switching -->
          <div class="d-none d-md-flex text-clickable me-2 pe-2 border-end border-2">
            <span class="me-2">{{ locale.toUpperCase() }}</span>
            <i class="bi bi-globe2" />
          </div>
          <!-- Account button -->
          <div class="d-none d-md-flex">
            <i class="bi bi-person-circle text-clickable" @click="toggleMenu('account')" />
          </div>
          <!-- Burger button -->
          <div class="d-flex d-md-none">
            <i class="bi bi-list text-clickable" @click="toggleMenu('burger')" />
          </div>
        </div>
      </div>
    </nav>
    <div v-if="currentMenu" class="position-absolute w-100 d-flex">
      <component :is="currentMenu" />
    </div>
  </div>
</template>

<style scoped>
.nav-container {
  display: flex;
  align-items: center;
  padding: 0.75rem;
}

.nav-container > * {
  height: 40px;
  align-items: center;
}

.search-field {
  background-color: transparent;
  width: 35vw;
  min-width: 180px;
  font-size: 15px;
}

.bi {
  display: flex;
  font-size: 25px;
  width: 25px;
  align-items: center;
  justify-content: center;
}
</style>
