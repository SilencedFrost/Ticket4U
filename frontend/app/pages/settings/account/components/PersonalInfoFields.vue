<script setup lang="ts">
// <!-- TODO: wire up script -->
</script>

<template>
  <div class="row g-3">
    <!-- Họ & Tên -->
    <div class="col-md-6">
      <label for="last-name" class="form-label settings-label">{{
        $t('common.last_name')
      }}</label>
      <input
        id="last-name"
        type="text"
        class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-3 py-2 px-3"
        value="Nguyễn"
        maxlength="32"
      />
    </div>
    <div class="col-md-6">
      <label for="first-name" class="form-label settings-label">{{
        $t('common.first_name')
      }}</label>
      <input
        id="first-name"
        type="text"
        class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-3 py-2 px-3"
        value="Minh Quang"
        maxlength="32"
      />
    </div>

    <!-- Email -->
    <div class="col-12">
      <label for="email" class="form-label settings-label">{{ $t('common.email') }}</label>
      <input
        id="email"
        type="email"
        class="form-control settings-email-field text-reactive-secondary border border-secondary-subtle rounded-3 py-2 px-3"
        value="minhquangdev@gmail.com"
        readonly
      />
    </div>

    <!-- Ngày sinh -->
    <div class="col-md-6">
      <label for="birthday" class="form-label settings-label">{{
        $t('settings.personal_information.basic_info.birthday')
      }}</label>
      <input
        id="birthday"
        type="date"
        class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-3 py-2 px-3"
        value="1995-08-15"
      />
    </div>

    <!-- Số điện thoại -->
    <div class="col-md-6">
      <label for="phone" class="form-label settings-label">{{
        $t('settings.personal_information.basic_info.phone')
      }}</label>
      <input
        id="phone"
        type="tel"
        class="form-control bg-reactive-primary text-reactive-primary border border-secondary-subtle rounded-3 py-2 px-3"
        placeholder="VD: 0912 345 678"
        value="0901234567"
        pattern="^(0[3|5|7|8|9])+([0-9]{8})$"
        title="Số điện thoại không hợp lệ. VD: 0912345678"
        maxlength="12"
      />
    </div>
  </div>
</template>

<style scoped>
</style>
