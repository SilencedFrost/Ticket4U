<script setup lang="ts">
import SettingsLayout from './components/SettingsLayout.vue';

const localePath = useLocalePath();
let desktopMediaQuery: MediaQueryList | null = null;

const handleViewportChange = (event: MediaQueryListEvent) => {
  if (event.matches) {
    redirectToDefaultDesktopTab();
  }
};

function redirectToDefaultDesktopTab() {
  navigateTo(localePath('/settings/account'), { replace: true });
}

onMounted(() => {
  desktopMediaQuery = window.matchMedia('(min-width: 768px)');

  if (desktopMediaQuery.matches) {
    redirectToDefaultDesktopTab();
  }

  desktopMediaQuery.addEventListener('change', handleViewportChange);
});

onBeforeUnmount(() => {
  if (desktopMediaQuery) {
    desktopMediaQuery.removeEventListener('change', handleViewportChange);
  }
});
</script>

<template>
  <SettingsLayout>
    <Title>{{ $t('settings.title') }} | Ticket4U</Title>
  </SettingsLayout>
</template>
