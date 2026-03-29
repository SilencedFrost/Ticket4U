<script setup lang="ts">
import SettingsSaveButton from '../../components/SettingsSaveButton.vue';

const currentPassword = ref('');
const newPassword = ref('');
const isViewingCurrentPassword = ref(false);
const isViewingNewPassword = ref(false);

function toggleCurrentPasswordVisibility() {
  isViewingCurrentPassword.value = !isViewingCurrentPassword.value;
}

function toggleNewPasswordVisibility() {
  isViewingNewPassword.value = !isViewingNewPassword.value;
}

function submitChangePassword() {
  // TODO: wire up API submit.
}
</script>

<template>
  <div class="change-password-form">
    <div class="card p-4 rounded-3 shadow-none border">
      <h2 class="h6 fw-bold mb-4 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-shield-lock-fill"></i>
        {{ $t('settings.security.change_password.title') }}
      </h2>

      <form @submit.prevent="submitChangePassword">
        <div class="row g-3">
          <div class="col-12">
            <label for="current-password" class="form-label settings-label text-reactive-secondary">
              {{ $t('settings.security.change_password.current_password') }}
            </label>
            <div class="input-group">
              <input
                id="current-password"
                v-model="currentPassword"
                :type="isViewingCurrentPassword ? 'text' : 'password'"
                class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-start-3 py-2 px-3"
                autocomplete="current-password"
              />
              <button
                class="btn btn-outline-secondary bg-reactive-primary"
                type="button"
                @mousedown.prevent="toggleCurrentPasswordVisibility"
              >
                <i :class="isViewingCurrentPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
              </button>
            </div>
          </div>

          <div class="col-12">
            <label for="new-password" class="form-label settings-label text-reactive-secondary">
              {{ $t('settings.security.change_password.new_password') }}
            </label>
            <div class="input-group">
              <input
                id="new-password"
                v-model="newPassword"
                :type="isViewingNewPassword ? 'text' : 'password'"
                class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-start-3 py-2 px-3"
                autocomplete="new-password"
              />
              <button
                class="btn btn-outline-secondary bg-reactive-primary"
                type="button"
                @mousedown.prevent="toggleNewPasswordVisibility"
              >
                <i :class="isViewingNewPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
              </button>
            </div>
            <span class="small text-reactive-secondary mt-1 px-1 d-block">
              {{ $t('settings.security.change_password.new_password_hint') }}
            </span>
          </div>
        </div>

        <div class="settings-form-actions d-flex justify-content-end mt-3">
          <SettingsSaveButton />
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
</style>
