<script setup lang="ts">
import SettingsLayout from '../../../features/settings/components/SettingsLayout.vue';
import PreferencesSection from '../../../features/settings/preferences/components/PreferencesSection.vue';
</script>

<template>
  <settings-layout>
    <Title>{{ $t('settings.nav.preferences') }} | Ticket4U</Title>
    <preferences-section />
  </settings-layout>
</template>
