<script setup lang="ts">
import SettingsLayout from '../../../features/settings/components/SettingsLayout.vue';
import ChangePasswordForm from '../../../features/settings/security/components/ChangePasswordForm.vue';
import SessionList from '../../../features/settings/security/components/SessionList.vue';

// TODO: parse & format user agent string thành dạng dễ đọc
// (vd: "Chrome 120 trên Windows 10" thay vì raw UA string) - cần nghiên cứu thêm
</script>

<template>
  <settings-layout>
    <Title>{{ $t('settings.security.title') }} | Ticket4U</Title>
    <!-- Page header: title + subtitle, full width -->
    <div class="page-header card shadow-sm p-3 mb-2">
      <h1 class="h3 fw-bold text-reactive-primary mb-1">{{ $t('settings.security.title') }}</h1>
      <p class="text-reactive-secondary mb-0">
        {{ $t('settings.security.subtitle') }}
      </p>
    </div>

    <!-- Main content: full width form -->
    <div>
      <div class="d-flex flex-column gap-2">
        <change-password-form />
        <session-list />
      </div>
    </div>
  </settings-layout>
</template>
