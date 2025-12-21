<script setup lang="ts">
import LogIn from './items/LogIn.vue';
import LogOut from './items/LogOut.vue';
import Register from './items/Register.vue';
import ChangeTheme from './items/ChangeTheme.vue';
import Settings from './items/Settings.vue';
import { useTheme } from '#imports';

const { nextTheme } = useTheme();
const useUser = useUserStore();
</script>

<template>
  <div class="d-flex flex-column bg-reactive-primary flex-fill">
    <div v-if="!useUser.isLoggedIn">
      <div class="menu-item-left"><i class="bi bi-box-arrow-in-left" /><log-in /></div>
      <div class="menu-item-left"><i class="bi bi-person-plus-fill" /><register /></div>
    </div>
    <div v-else>
      <div class="menu-item-left"><i class="bi bi-box-arrow-in-right" /><log-out /></div>
    </div>
    <div>
      <div class="menu-item-left">
        <i v-if="nextTheme === 'light'" class="bi bi-brightness-high-fill" />
        <i v-if="nextTheme === 'dark'" class="bi bi-moon-fill" />
        <change-theme />
      </div>
      <div class="menu-item-left"><i class="bi bi-gear-fill" /><settings /></div>
    </div>
  </div>
</template>
