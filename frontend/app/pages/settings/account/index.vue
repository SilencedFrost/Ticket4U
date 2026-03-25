<script setup lang="ts">
import SettingsLayout from '../components/SettingsLayout.vue';
import AvatarUpload from './components/AvatarUpload.vue';
import PersonalInfoForm from './components/PersonalInfoForm.vue';

// <!-- TODO: wire up script -->
</script>

<template>
  <SettingsLayout>
    <Title>{{ $t('settings.title') }}</Title>
    <!-- Page header: title + subtitle, full width -->
    <div class="page-header mb-4">
      <h1 class="h3 fw-bold text-reactive-primary mb-1">{{ $t('settings.personal_information.title') }}</h1>
      <p class="text-reactive-secondary mb-0">
        {{ $t('settings.personal_information.subtitle') }}
      </p>
    </div>

    <!-- Main content: avatar LEFT + form RIGHT, side by side -->
    <div class="personal-info-content d-flex flex-column flex-lg-row align-items-lg-start gap-4">
      <!-- LEFT column: fixed ~180px -->
      <div class="personal-info-avatar-col card p-3 rounded-4 shadow-sm border-0 bg-reactive-secondary">
        <AvatarUpload />
      </div>

      <!-- RIGHT column: flex-1, takes remaining width -->
      <div class="personal-info-form-col">
        <!-- PersonalInfoForm renders form content + save button -->
        <PersonalInfoForm />
      </div>
    </div>
  </SettingsLayout>
</template>

<style scoped>
.personal-info-avatar-col {
  width: 240px;
  flex-shrink: 0;
}

.personal-info-form-col {
  flex: 1;
  min-width: 0;
}
</style>
