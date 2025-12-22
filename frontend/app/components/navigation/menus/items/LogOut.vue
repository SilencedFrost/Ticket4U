<script setup lang="ts">
const useUser = useUserStore();
const localePath = useLocalePath();

function handleLogout() {
  useUser.logout();
  navigateTo(localePath('/auth/login'));
}
</script>
<template>
  <span @click="handleLogout()">{{ $t('auth.logout.action') }}</span>
</template>
