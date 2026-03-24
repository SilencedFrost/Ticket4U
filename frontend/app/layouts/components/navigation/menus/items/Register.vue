<script setup lang="ts">
const localePath = useLocalePath();
</script>
<template>
    <nuxt-link-locale :to="localePath('/auth/register')">{{ $t('auth.register.title') }}</nuxt-link-locale>
</template>
