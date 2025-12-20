<script setup lang="ts">
import LogIn from './items/LogIn.vue';
import LogOut from './items/LogOut.vue';
import ChangeTheme from './items/ChangeTheme.vue';
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
      <div class="menu-item-right">
        <component :is="LogIn" /><i class="bi bi-box-arrow-in-left" />
      </div>
    </div>
    <div v-else>
      <div class="menu-item-right">
        <component :is="LogOut" /><i class="bi bi-box-arrow-in-right" />
      </div>
    </div>
    <div>
      <div class="menu-item-right">
        <component :is="ChangeTheme" />
        <i v-if="nextTheme === 'light'" class="bi bi-brightness-high-fill" />
        <i v-if="nextTheme === 'dark'" class="bi bi-moon-fill" />
      </div>
    </div>
  </div>
</template>
