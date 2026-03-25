<script setup lang="ts">
// <!-- TODO: wire up script -->
</script>

<template>
  <div class="row g-3">
    <!-- Họ & Tên -->
    <div class="col-md-6">
      <label class="form-label text-reactive-secondary small fw-bold tracking-wide mb-1 px-1">{{
        $t('settings.personal_information.basic_info.last_name')
      }}</label>
      <input
        type="text"
        class="form-control settings-input text-reactive-primary border-0 rounded-3 py-2 px-3 input-underline"
        value="Nguyễn"
        maxlength="32"
      />
      <span class="small text-reactive-secondary mt-1 px-1 d-block" aria-live="polite">{{
        $t('settings.personal_information.basic_info.last_name_hint')
      }}</span>
    </div>
    <div class="col-md-6">
      <label class="form-label text-reactive-secondary small fw-bold tracking-wide mb-1 px-1">{{
        $t('settings.personal_information.basic_info.first_name')
      }}</label>
      <input
        type="text"
        class="form-control settings-input text-reactive-primary border-0 rounded-3 py-2 px-3 input-underline"
        value="Minh Quang"
        maxlength="32"
      />
      <span class="small text-reactive-secondary mt-1 px-1 d-block" aria-live="polite">{{
        $t('settings.personal_information.basic_info.first_name_hint')
      }}</span>
    </div>

    <!-- Tên người dùng -->
    <div class="col-12">
      <label class="form-label text-reactive-secondary small fw-bold tracking-wide mb-1 px-1">{{
        $t('settings.personal_information.basic_info.username')
      }}</label>
      <input
        type="text"
        class="form-control settings-input text-reactive-primary border-0 rounded-3 py-2 px-3 input-underline"
        value="minhquangdev"
        maxlength="64"
      />
      <span class="small text-reactive-secondary mt-1 px-1 d-block" aria-live="polite">{{
        $t('settings.personal_information.basic_info.username_hint')
      }}</span>
    </div>

    <!-- Ngày sinh -->
    <div class="col-md-6">
      <label class="form-label text-reactive-secondary small fw-bold tracking-wide mb-1 px-1">{{
        $t('settings.personal_information.basic_info.birthday')
      }}</label>
      <input
        type="date"
        class="form-control settings-input text-reactive-primary border-0 rounded-3 py-2 px-3 input-underline"
        value="1995-08-15"
      />
    </div>

    <!-- Số điện thoại -->
    <div class="col-md-6">
      <label class="form-label text-reactive-secondary small fw-bold tracking-wide mb-1 px-1">{{
        $t('settings.personal_information.basic_info.phone')
      }}</label>
      <input
        type="tel"
        class="form-control settings-input text-reactive-primary border-0 rounded-3 py-2 px-3 input-underline"
        placeholder="+84xxxxxxxxx"
        value="+84 901 234 567"
        maxlength="15"
      />
      <span class="small text-reactive-secondary mt-1 px-1 d-block" aria-live="polite">{{
        $t('settings.personal_information.basic_info.phone_hint')
      }}</span>
    </div>
  </div>
</template>

<style scoped>
.tracking-wide {
  letter-spacing: 0.05em;
}

.input-underline {
  border-bottom: 2px solid transparent !important;
}

.input-underline:focus {
  box-shadow: none;
  border-bottom-color: var(--bs-primary) !important;
}
</style>
