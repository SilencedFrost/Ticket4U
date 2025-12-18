<script setup>
import { ref } from 'vue';

const showLangDropdown = ref(false);
const selectedLang = ref('vi');

const selectLanguage = (lang) => {
  selectedLang.value = lang;
  showLangDropdown.value = false;
};
</script>

<template>
    <!-- Header with Search -->
    <header class="bg-primary position-sticky header-container">
      <div class="container-xxl d-flex align-items-center gap-3 py-3">
        <!-- Logo -->
        <div class="fw-bold fs-5 text-white flex-shrink-0">
            <img src="/logo-gray.png" alt="" class="header-logo"/>
        </div>

        <!-- Search Bar -->
        <div class="d-sm-none flex-shrink-0">
         <i class="bi bi-search text-white me-2 fs-5"></i>
        </div>
        <div class="flex-grow-1 d-flex gap-2 d-none d-sm-flex header-search-container">
          <div class="d-flex align-items-center bg-white rounded-2 px-3 py-2 flex-grow-1">
            <i class="bi bi-search text-dark me-2 fs-6"></i>
            <input
              type="text"
              :placeholder="$t('header.search_placeholder')"
              class="border-0 bg-transparent flex-grow-1 text-dark small"
            />
          </div>
          <button class="btn btn-outline-light btn-sm rounded-pill d-none d-sm-inline">{{ $t('header.button_create_event') }}</button>
        </div>

        <!-- Right Actions -->
        <div class="d-flex gap-4 align-items-center flex-shrink-0 ms-auto">
          
          <span class="text-white small">{{ $t('header.button_sign_in') }} | {{ $t('header.button_sign_up') }}</span>
          
          <!-- Language Selector Dropdown -->
          <div class="position-relative">
            <button 
              @click="showLangDropdown = !showLangDropdown" 
              class="btn btn-link p-0 d-flex align-items-center gap-2 text-decoration-none lang-button" 
              type="button"
            >
              <img 
                v-if="selectedLang === 'vi'"
                src="../assets/img/vn.svg" 
                alt="VN" 
                class="flag-img"
              />
              <img 
                v-else
                src="../assets/img/en.svg" 
                alt="EN" 
                class="flag-img"
              />
              <i 
                class="bi text-white" 
                :class="showLangDropdown ? 'bi-chevron-up' : 'bi-chevron-down'"
              ></i>
            </button>
            
            <!-- Dropdown Menu -->
            <div 
              v-if="showLangDropdown"
              class="position-absolute top-100 end-0 bg-white rounded-3 shadow mt-2 lang-dropdown"
            >
              <button 
                @click="selectLanguage('vi')"
                class="w-100 text-start d-flex align-items-center gap-2 border-0 bg-white px-3 py-2 lang-dropdown-item"
              >
                <img src="../assets/img/vn.svg" alt="VN" class="flag-dropdown-img" />
                <span class="small text-dark">Tiếng Việt</span>
              </button>
              <button 
                @click="selectLanguage('en')"
                class="w-100 text-start d-flex align-items-center gap-2 border-0 bg-white px-3 py-2 rounded-bottom lang-dropdown-item"
              >
                <img src="../assets/img/en.svg" alt="EN" class="flag-dropdown-img" />
                <span class="small text-dark">English</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Categories Navigation -->
      <nav class="overflow-x-auto bg-reactive-primary py-3">
        <div class="container-xxl d-flex gap-3 flex-wrap">
          <span class="text-reactive-primary fw-semibold text-nowrap small category-link">{{ $t('header.categories.live_music') }}</span>
          <span class="text-reactive-primary fw-semibold text-nowrap small category-link">{{ $t('header.categories.theater_art') }}</span>
          <span class="text-reactive-primary fw-semibold text-nowrap small category-link">{{ $t('header.categories.sports') }}</span>
          <span class="text-reactive-primary fw-semibold text-nowrap small category-link">{{ $t('header.categories.others') }}</span>
        </div>
      </nav>
    </header>
</template>

<style scoped>
/* Heder container */
.header-container {
  z-index: 100;
  top: 0;
}

/* Logo */
.header-logo {
  width: 50px;
}

/* Search */
.header-search-container {
  min-width: 0;
  max-width: 700px;
}

/* Language Dropdown */
.flag-img {
  width: 20px;
  height: 20px;
}

.lang-dropdown {
  min-width: 160px;
  z-index: 1000;
}

.lang-dropdown-item {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.lang-dropdown-item:hover {
  background-color: #f8f9fa;
}

.flag-dropdown-img {
  width: 24px;
  height: 20px;
}

/* Categories */
.category-link {
  cursor: pointer;
}
</style>