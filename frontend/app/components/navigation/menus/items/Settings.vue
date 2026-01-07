<script setup lang="ts">
const localePath = useLocalePath();
</script>
<template>
  <nuxt-link-locale :to="localePath('/settings')">{{ $t('common.settings') }}</nuxt-link-locale>
</template>
