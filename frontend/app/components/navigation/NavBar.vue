<script setup lang="ts">
import { onClickOutside } from '@vueuse/core';
import AccountDropDown from './menus/AccountDropDown.vue';

const menuList = reactive({
  none: null,
  account: AccountDropDown,
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
      <div class="container-fluid p-0 d-flex justify-content-between position-relative">
        <div class="nav-container-left">
          <div>
            <nuxt-link to="/">
              <img src="/logo-primary.png" style="height: 100%" />
            </nuxt-link>
          </div>
        </div>
        <div
          class="nav-container-middle"
          style="position: absolute; left: 50%; transform: translateX(-50%)"
        >
          <i class="bi bi-search text-clickable me-2" @click="focusSearch()" /><input
            ref="searchInput"
            type="text"
            class="search-field text-reactive-primary input-underline"
            :placeholder="$t('placeholder.search')"
            @blur="clearSearch()"
          />
        </div>
        <div class="nav-container-right">
          <i v-if="useUser.isLoggedIn === true" class="bi bi-ticket me-2 text-clickable" />
          <div class="d-none d-sm-flex">
            <i class="bi bi-person-circle text-clickable" @click="toggleMenu('account')" />
          </div>
          <div class="d-flex d-sm-none"><i class="bi bi-list text-clickable" /></div>
        </div>
      </div>
    </nav>
    <div v-if="currentMenu" class="position-absolute w-100 d-flex">
      <component :is="currentMenu" />
    </div>
  </div>
</template>

<style scoped>
[class^='nav-container-'] {
  display: flex;
  align-items: center;
  padding: 0.75rem;
}

[class^='nav-container-'] > * {
  height: 40px;
  align-items: center;
}

[class^='nav-container-'] > i {
  display: flex;
}

.search-field {
  background-color: transparent;
  width: 40vw;
  min-width: 200px;
  font-size: 15px;
}

.bi {
  font-size: 25px;
}
</style>
