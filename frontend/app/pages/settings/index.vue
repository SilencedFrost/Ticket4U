<script setup lang="ts">
import SettingsLayout from './components/SettingsLayout.vue';

const localePath = useLocalePath();

onMounted(() => {
  if (window.matchMedia('(min-width: 768px)').matches) {
    navigateTo(localePath('/settings/account'), { replace: true });
  }
});
</script>

<template>
  <SettingsLayout>
    <Title>{{ $t('settings.title') }} | Ticket4U</Title>
  </SettingsLayout>
</template>
