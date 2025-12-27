<script setup lang="ts">
const localePath = useLocalePath();
</script>
<template>
  <nuxt-link-locale :to="localePath('/auth/login')">{{ $t('auth.login.action') }}</nuxt-link-locale>
</template>
