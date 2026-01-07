<script setup lang="ts">
const localePath = useLocalePath();
</script>
<template>
  <nuxt-link-locale :to="localePath('/auth/register')">{{ $t('auth.register') }}</nuxt-link-locale>
</template>
