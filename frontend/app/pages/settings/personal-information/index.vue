<template>
  <SettingsLayout>
    <!-- Page header: title + subtitle, full width -->
    <div class="page-header mb-4">
      <h1 class="h3 fw-bold text-reactive-primary mb-1">Thông tin cá nhân</h1>
      <p class="text-reactive-secondary mb-0">
        Cập nhật hồ sơ của bạn để có trải nghiệm đặt vé mượt mà hơn.
      </p>
    </div>

    <!-- Main content: avatar LEFT + form RIGHT, side by side -->
    <div class="personal-info-content d-flex flex-column flex-lg-row align-items-lg-start gap-4">
      <!-- LEFT column: fixed ~180px -->
      <div
        class="personal-info-avatar-col card p-3 rounded-4 shadow-sm border-0 bg-reactive-secondary"
      >
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

<script setup>
import SettingsLayout from '../components/SettingsLayout.vue';
import AvatarUpload from './components/AvatarUpload.vue';
import PersonalInfoForm from './components/PersonalInfoForm.vue';

useHead({
  title: 'Settings',
});
// <!-- TODO: wire up script -->
</script>

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
