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
    <header style="background-color: #07b3df; z-index: 100; position: sticky;box-sizing: border-box; ">
      <div class="container-xxl d-flex align-items-center gap-3 py-3">
        <!-- Logo -->
        <div class="fw-bold fs-5 text-white flex-shrink-0">
            <img src="/logo-gray.png" alt="" style="width: 50px;"/>
        </div>

        <!-- Search Bar -->
        <div class="d-sm-none flex-shrink-0">
         <i class="bi bi-search " style="color: white; margin-right: 0.5rem; font-size: 1.2rem"></i>
        </div>
        <div class="flex-grow-1 d-flex gap-2 d-none d-sm-flex" style="min-width: 0; max-width: 700px">
          <div class="d-flex align-items-center bg-white rounded-2 px-3 py-2 flex-grow-1">
            <i class="bi bi-search " style="color: #111111; margin-right: 0.5rem; font-size: 0.9rem"></i>
            <input
              type="text"
              :placeholder="$t('header.search_placeholder')"
              class="border-0 bg-transparent flex-grow-1"
              style="outline: none; color: #666; font-size: 0.85rem"
            />
          </div>
          <button class="btn btn-outline-light btn-sm rounded-pill d-none d-sm-inline">{{ $t('header.button_create_event') }}</button>
        </div>

        <!-- Right Actions -->
        <div class="d-flex gap-4 align-items-center flex-shrink-0 ms-auto">
          
          <span class="text-white" style="font-size: 0.85rem">{{ $t('header.button_sign_in') }} | {{ $t('header.button_sign_up') }}</span>
          
          <!-- Language Selector Dropdown -->
          <div style="position: relative">
            <button 
              @click="showLangDropdown = !showLangDropdown" 
              class="btn btn-link p-0 d-flex align-items-center gap-2 text-decoration-none" 
              type="button"
              style="border: none; background: none"
            >
              <img 
                v-if="selectedLang === 'vi'"
                src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 20'%3E%3Crect fill='%23DA251D' width='30' height='20'/%3E%3Cpolygon fill='%23FFFF00' points='15,4 16.5,9 21.5,9 17.5,12 19,17 15,14 11,17 12.5,12 8.5,9 13.5,9'/%3E%3C/svg%3E" 
                alt="VN" 
                style="width: 24px; height: 16px; border-radius: 2px; border: 1px solid rgba(255,255,255,0.2)" 
              />
              <img 
                v-else
                src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 60 30'%3E%3CclipPath id='s'%3E%3Cpath d='M0,0 v30 h60 v-30 z'/%3E%3C/clipPath%3E%3CclipPath id='t'%3E%3Cpath d='M30,15 h30 v15 z v15 h-30 z h-30 v-15 z v-15 h30 z'/%3E%3C/clipPath%3E%3Cg clip-path='url(%23s)'%3E%3Cpath d='M0,0 v30 h60 v-30 z' fill='%23012169'/%3E%3Cpath d='M0,0 L60,30 M60,0 L0,30' stroke='%23fff' stroke-width='6'/%3E%3Cpath d='M0,0 L60,30 M60,0 L0,30' clip-path='url(%23t)' stroke='%23C8102E' stroke-width='4'/%3E%3Cpath d='M30,0 v30 M0,15 h60' stroke='%23fff' stroke-width='10'/%3E%3Cpath d='M30,0 v30 M0,15 h60' stroke='%23C8102E' stroke-width='6'/%3E%3C/g%3E%3C/svg%3E" 
                alt="EN" 
                style="width: 24px; height: 16px; border-radius: 2px; border: 1px solid rgba(255,255,255,0.2)" 
              />
              <i 
                class="bi text-white" 
                :class="showLangDropdown ? 'bi-chevron-up' : 'bi-chevron-down'" 
                style="font-size: 0.75rem"
              ></i>
            </button>
            
            <!-- Dropdown Menu -->
            <div 
              v-if="showLangDropdown"
              style="position: absolute; right: 0; top: 100%; margin-top: 0.5rem; background: white; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); min-width: 160px; z-index: 1000"
            >
              <button 
                @click="selectLanguage('vi')"
                class="w-100 text-start d-flex align-items-center gap-2 border-0 bg-white"
                style="padding: 0.75rem 1rem; cursor: pointer; transition: background-color 0.2s"
                @mouseover="$event.target.style.backgroundColor = '#f8f9fa'"
                @mouseout="$event.target.style.backgroundColor = 'white'"
              >
                <img src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 20'%3E%3Crect fill='%23DA251D' width='30' height='20'/%3E%3Cpolygon fill='%23FFFF00' points='15,4 16.5,9 21.5,9 17.5,12 19,17 15,14 11,17 12.5,12 8.5,9 13.5,9'/%3E%3C/svg%3E" alt="VN" style="width: 24px; height: 16px; border-radius: 2px; border: 1px solid rgba(0,0,0,0.1)" />
                <span style="color: #333; font-size: 0.9rem">Tiếng Việt</span>
              </button>
              <button 
                @click="selectLanguage('en')"
                class="w-100 text-start d-flex align-items-center gap-2 border-0 bg-white"
                style="padding: 0.75rem 1rem; cursor: pointer; transition: background-color 0.2s; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px"
                @mouseover="$event.target.style.backgroundColor = '#f8f9fa'"
                @mouseout="$event.target.style.backgroundColor = 'white'"
              >
                <img src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 60 30'%3E%3CclipPath id='s'%3E%3Cpath d='M0,0 v30 h60 v-30 z'/%3E%3C/clipPath%3E%3CclipPath id='t'%3E%3Cpath d='M30,15 h30 v15 z v15 h-30 z h-30 v-15 z v-15 h30 z'/%3E%3C/clipPath%3E%3Cg clip-path='url(%23s)'%3E%3Cpath d='M0,0 v30 h60 v-30 z' fill='%23012169'/%3E%3Cpath d='M0,0 L60,30 M60,0 L0,30' stroke='%23fff' stroke-width='6'/%3E%3Cpath d='M0,0 L60,30 M60,0 L0,30' clip-path='url(%23t)' stroke='%23C8102E' stroke-width='4'/%3E%3Cpath d='M30,0 v30 M0,15 h60' stroke='%23fff' stroke-width='10'/%3E%3Cpath d='M30,0 v30 M0,15 h60' stroke='%23C8102E' stroke-width='6'/%3E%3C/g%3E%3C/svg%3E" alt="EN" style="width: 24px; height: 16px; border-radius: 2px; border: 1px solid rgba(0,0,0,0.1)" />
                <span style="color: #333; font-size: 0.9rem">English</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Categories Navigation -->
      <nav class="overflow-x-auto bg-reactive-primary" style="padding: 1rem 0">
        <div class="container-xxl d-flex gap-3 flex-wrap">
          <span class="text-reactive-primary fw-semibold" style="cursor: pointer; font-size: clamp(0.75rem, 2vw, 0.9rem); white-space: nowrap">{{ $t('header.categories.live_music') }}</span>
          <span class="text-reactive-primary fw-semibold" style="cursor: pointer; font-size: clamp(0.75rem, 2vw, 0.9rem); white-space: nowrap">{{ $t('header.categories.theater_art') }}</span>
          <span class="text-reactive-primary fw-semibold" style="cursor: pointer; font-size: clamp(0.75rem, 2vw, 0.9rem); white-space: nowrap">{{ $t('header.categories.sports') }}</span>
          <span class="text-reactive-primary fw-semibold" style="cursor: pointer; font-size: clamp(0.75rem, 2vw, 0.9rem); white-space: nowrap">{{ $t('header.categories.others') }}</span>
        </div>
      </nav>
    </header>
</template>