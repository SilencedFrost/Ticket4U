<script setup lang="ts">
const useUser = useUserStore();

function handleLogout() {
  useUser.logout();
  navigateTo('/auth/login');
}
</script>
<template>
  <span @click="handleLogout()">{{ $t('auth.logout.action') }}</span>
</template>
