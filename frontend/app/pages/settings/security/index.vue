<script setup lang="ts">
import SettingsLayout from '../components/SettingsLayout.vue';
import ChangePasswordForm from './components/ChangePasswordForm.vue';
import SessionList from './components/SessionList.vue';

// <!-- TODO: wire up script -->
</script>

<template>
  <SettingsLayout>
    <Title>{{ $t('settings.security.title') }} | Ticket4U</Title>
    <!-- Page header: title + subtitle, full width -->
    <div class="page-header card rounded-3 border p-4 mb-3">
      <h1 class="h3 fw-bold text-reactive-primary mb-1">{{ $t('settings.security.title') }}</h1>
      <p class="text-reactive-secondary mb-0">
        {{ $t('settings.security.subtitle') }}
      </p>
    </div>

    <!-- Main content: full width form -->
    <div class="security-content">
      <div class="d-flex flex-column gap-3">
        <ChangePasswordForm />
        <SessionList />
      </div>
    </div>
  </SettingsLayout>
</template>
