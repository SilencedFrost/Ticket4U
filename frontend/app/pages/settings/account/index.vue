<script setup lang="ts">
import SettingsLayout from '../components/SettingsLayout.vue';
import PersonalInfoSection from './components/PersonalInfoSection.vue';
</script>

<template>
  <settings-layout>
    <Title>{{ $t('settings.personal_information.title') }} | Ticket4U</Title>
    <personal-info-section />
  </settings-layout>
</template>
