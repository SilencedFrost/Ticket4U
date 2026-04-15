<script setup lang="ts">
import { onClickOutside } from '@vueuse/core';
import AccountDropDown from './menus/AccountDropDown.vue';
import BurgerDropDown from './menus/BurgerDropDown.vue';
import LanguageSwitcherDropDown from './menus/LanguageSwitcherDropDown.vue';

const { locale } = useI18n();
const currentMenuKey = ref<string>('none');
const searchInput = ref<HTMLInputElement | null>(null);
const menuContainer = ref<HTMLElement | null>(null);
const useUser = useUserStore();
const searchQuery = ref('');
const localePath = useLocalePath();
const route = useRoute();

watch(
  () => route.query.q,
  (newQuery) => {
    if (newQuery === undefined) {
      searchQuery.value = '';
    } else {
      // Ngược lại nếu có dữ liệu thì mới gán vào
      searchQuery.value = String(newQuery);
    }
  },
  { immediate: true },
);

const { currentTheme } = useTheme();

function focusSearch() {
  searchInput.value?.focus();
}

function toggleMenu(targetKey = 'none') {
  currentMenuKey.value = currentMenuKey.value === targetKey ? 'none' : targetKey;
}

function handleSearch() {
  const query = searchQuery.value.trim();
  if (query) {
    const targetPath = localePath('/dev/search');

    navigateTo({
      path: targetPath,
      query: { q: query },
    });

    searchInput.value?.blur();
    currentMenuKey.value = 'none';
  }
}

onClickOutside(menuContainer, () => {
  toggleMenu();
});
</script>
<template>
  <div ref="menuContainer">
    <nav
      :class="['bg-reactive-primary', 'shadow-sm', { 'border-bottom': currentTheme == 'dark' }]"
      @click="toggleMenu()"
    >
      <div class="container-fluid p-0 position-relative d-flex">
        <!-- Logo -->
        <div class="nav-container">
          <nuxt-link-locale :to="'/'">
            <img src="/logo-primary-128.png" class="h-100" alt="brand-logo" />
          </nuxt-link-locale>
        </div>
        <!-- Hover buttons -->
        <div class="d-none d-md-flex ms-lg-5">
          <div class="nav-item">
            <span>{{ $t('common.events') }}</span>
          </div>
          <div class="nav-item">
            <span>{{ $t('common.contact') }}</span>
          </div>
        </div>
        <!-- Search bar -->
        <div class="nav-container position-absolute start-50 translate-middle-x">
          <i class="bi bi-search text-clickable me-2" @click="focusSearch()" /><input
            ref="searchInput"
            v-model="searchQuery"
            type="text"
            class="search-field text-reactive-primary input-underline"
            :placeholder="$t('placeholder.search')"
            @keyup.enter="handleSearch()"
          />
        </div>
        <!-- Function buttons -->
        <div class="d-flex align-items-center ms-auto">
          <!-- Ticket button -->
          <div
            v-if="useUser.isLoggedIn === true"
            class="d-flex text-clickable pe-2 border-end border-2"
          >
            <i class="bi bi-ticket" />
            <span class="d-none d-sm-flex ms-2">{{ $t('common.tickets') }}</span>
          </div>
          <!-- Language switching -->
          <div class="d-none d-md-flex ms-2 pe-2 border-end border-2 position-relative">
            <div class="d-flex flex-column">
              <div class="d-flex text-clickable" @click.stop="toggleMenu('language')">
                <i class="bi bi-globe2" />
                <span class="ms-2" style="width: 20px">{{ locale.toUpperCase() }}</span>
              </div>
              <!-- Language drop down -->
              <div
                v-if="currentMenuKey === 'language'"
                class="position-absolute top-100 start-50 translate-middle-x mt-3"
              >
                <language-switcher-drop-down @click="toggleMenu()" />
              </div>
            </div>
          </div>
          <!-- Account button -->
          <div class="d-none d-md-flex ms-2 pe-3 position-relative">
            <div class="d-flex flex-column">
              <i class="bi bi-person-circle text-clickable" @click.stop="toggleMenu('account')" />
              <!-- Account drop down -->
              <div v-if="currentMenuKey === 'account'" class="position-absolute top-100 end-0 mt-3">
                <account-drop-down />
              </div>
            </div>
          </div>
          <!-- Burger button -->
          <div class="d-flex d-md-none ms-2 pe-3">
            <i class="bi bi-list text-clickable" @click.stop="toggleMenu('burger')" />
          </div>
        </div>
      </div>
    </nav>
    <!-- Burger collapsible menu -->
    <div
      :class="[
        'position-absolute',
        'w-100',
        'dropdown-content',
        { open: currentMenuKey === 'burger' },
      ]"
    >
      <burger-drop-down
        :parent-menu-open="currentMenuKey === 'burger'"
        @switched-lang="toggleMenu()"
      />
    </div>
  </div>
</template>

<style scoped>
.nav-container {
  display: flex;
  align-items: center;
  padding: 0.75rem;

  > * {
    height: 40px;
    align-items: center;
  }
}

.search-field {
  background-color: transparent;
  width: 35vw;
  min-width: 190px;
  font-size: 15px;
}

/* Icon size */
.bi {
  display: flex;
  font-size: 25px;
  width: 25px;
  align-items: center;
  justify-content: center;
}
</style>
