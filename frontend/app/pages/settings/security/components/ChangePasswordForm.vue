<script setup lang="ts">
// <!-- TODO: wire up script -->
</script>

<template>
  <div class="change-password-form">
    <div class="card p-3 rounded-4 shadow-sm border-0 bg-reactive-secondary">
      <h2 class="h6 fw-bold mb-4 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-shield-lock-fill"></i>
        {{ $t('settings.security.change_password.title') }}
      </h2>

      <div class="row g-3">
        <!-- Mật khẩu hiện tại -->
        <div class="col-12 col-md-8 col-lg-6">
          <label for="current-password" class="form-label settings-label">{{ $t('settings.security.change_password.current_password') }}</label>
          <input
            id="current-password"
            type="password"
            class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-3 py-2 px-3"
            :placeholder="$t('settings.security.change_password.current_password_placeholder')"
          />
        </div>

        <div class="w-100 d-none d-md-block m-0"></div>

        <!-- Mật khẩu mới -->
        <div class="col-12 col-md-8 col-lg-6">
          <label for="new-password" class="form-label settings-label">{{ $t('settings.security.change_password.new_password') }}</label>
          <input
            id="new-password"
            type="password"
            class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-3 py-2 px-3"
            :placeholder="$t('settings.security.change_password.new_password_placeholder')"
          />
          <span class="small text-reactive-secondary mt-1 px-1 d-block">{{ $t('settings.security.change_password.new_password_hint') }}</span>
        </div>

        <div class="w-100 d-none d-md-block m-0"></div>

        <!-- Xác nhận mật khẩu mới -->
        <div class="col-12 col-md-8 col-lg-6">
          <label for="confirm-password" class="form-label settings-label">{{ $t('settings.security.change_password.confirm_password') }}</label>
          <input
            id="confirm-password"
            type="password"
            class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-3 py-2 px-3"
            :placeholder="$t('settings.security.change_password.confirm_password_placeholder')"
          />
        </div>
      </div>

      <!-- Save button -->
      <div class="settings-form-actions d-flex justify-content-end mt-4">
        <button
          class="btn btn-primary fw-bold px-4 py-2 rounded-3 shadow-sm d-flex align-items-center gap-2 text-white"
        >
          <i class="bi bi-floppy2-fill"></i> {{ $t('settings.personal_information.actions.save') }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>
