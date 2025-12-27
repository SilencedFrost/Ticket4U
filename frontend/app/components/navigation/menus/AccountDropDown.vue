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
  <div
    class="card ms-auto d-flex flex-column bg-reactive-primary overflow-hidden border"
    style="width: 150px"
  >
    <div v-if="!useUser.isLoggedIn">
      <div class="menu-item-right"><log-in /><i class="bi bi-box-arrow-in-left" /></div>
      <div class="menu-item-right"><register /><i class="bi bi-person-plus-fill" /></div>
    </div>
    <div v-else>
      <div class="menu-item-right"><log-out /><i class="bi bi-box-arrow-in-right" /></div>
    </div>
    <div>
      <div class="menu-item-right">
        <change-theme />
        <i v-if="nextTheme === 'light'" class="bi bi-brightness-high-fill" />
        <i v-if="nextTheme === 'dark'" class="bi bi-moon-fill" />
      </div>
      <div class="menu-item-right"><settings /><i class="bi bi-gear-fill" /></div>
    </div>
  </div>
</template>
