<script setup lang="ts">
import SettingsLayout from '../components/SettingsLayout.vue';
import AvatarUpload from './components/AvatarUpload.vue';
import PersonalInfoForm from './components/PersonalInfoForm.vue';

// <!-- TODO: wire up script -->
</script>

<template>
  <settings-layout>
    <Title>{{ $t('settings.personal_information.title') }}</Title>
    <!-- Page header: title + subtitle, full width -->
    <div class="page-header card shadow-sm p-3 mb-2">
      <h1 class="h3 fw-bold text-reactive-primary mb-1">
        {{ $t('settings.personal_information.title') }}
      </h1>
      <p class="text-reactive-secondary mb-0">
        {{ $t('settings.personal_information.subtitle') }}
      </p>
    </div>

    <!-- Main content: avatar LEFT + form RIGHT, side by side -->
    <div class="row g-2 overflow-visible">
      <!-- LEFT column: fixed ~180px -->
      <div class="col-12 col-lg-auto">
        <div class="personal-info-avatar-col card p-3 shadow-sm d-flex flex-column h-100">
          <avatar-upload />
        </div>
      </div>

      <!-- RIGHT column: flex-1, takes remaining width -->
      <div class="col-12 personal-info-form-col">
        <!-- PersonalInfoForm renders form content + save button -->
        <personal-info-form />
      </div>
    </div>
  </settings-layout>
</template>

<style scoped>
.personal-info-avatar-col {
  width: 100%;
}

@media (min-width: 992px) {
  .personal-info-avatar-col {
    width: 240px;
  }
}

.personal-info-form-col {
  flex: 1;
  min-width: 0;
}
</style>
