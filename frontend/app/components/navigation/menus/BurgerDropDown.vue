<script setup lang="ts">
import LogIn from './items/LogIn.vue';
import LogOut from './items/LogOut.vue';
import Register from './items/Register.vue';
import ChangeTheme from './items/ChangeTheme.vue';
import Settings from './items/Settings.vue';
import { useTheme } from '#imports';

const emit = defineEmits(['switchedLang']);

const props = defineProps<{
  parentMenuOpen: boolean;
}>();

const { locale, locales, setLocale } = useI18n();
const { nextTheme } = useTheme();
const useUser = useUserStore();
const switchLang = ref<boolean>(false);

watch(
  () => props.parentMenuOpen,
  (newValue) => {
    if (newValue === false) {
      switchLang.value = false;
    }
  },
);
</script>

<template>
  <div class="d-flex flex-column bg-reactive-primary flex-fill shadow-sm">
    <!-- Lang switch button -->
    <div class="menu-item-left" @click="switchLang = !switchLang">
      <i class="bi bi-globe2" />
      <span style="width: 20px">{{ locale.toUpperCase() }}</span>
    </div>
    <!-- Lang switch menu -->
    <div :class="['w-100', 'dropdown-content', { open: switchLang }]">
      <div class="ms-2" @click="emit('switchedLang')">
        <div
          v-for="item in locales"
          :key="item.code"
          class="menu-item"
          @click="setLocale(item.code)"
        >
          {{ item.code.toUpperCase() }}
        </div>
      </div>
    </div>
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
