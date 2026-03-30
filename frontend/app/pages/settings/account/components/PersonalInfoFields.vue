<script setup lang="ts">
// TODO: wire up script
</script>

<template>
  <div class="row g-2">
    <!-- Họ & Tên -->

    <div class="col-lg-6">
      <label for="first-name" class="form-label">{{ $t('common.first_name') }}</label>
      <input id="first-name" type="text" class="form-control" value="Minh Quang" maxlength="32" />
    </div>

    <div class="col-lg-6">
      <label for="last-name" class="form-label">{{ $t('common.last_name') }}</label>
      <input id="last-name" type="text" class="form-control" value="Nguyễn" maxlength="32" />
    </div>

    <!-- Email -->
    <div class="col-12">
      <label for="email" class="form-label">{{ $t('common.email') }}</label>
      <input id="email" type="email" class="form-control" value="minhquangdev@gmail.com" readonly />
    </div>

    <!-- Ngày sinh -->
    <div class="col-lg-6">
      <label for="birthday" class="form-label">{{ $t('common.birthday') }}</label>
      <input id="birthday" type="date" class="form-control" value="1995-08-15" />
    </div>

    <!-- Số điện thoại -->
    <div class="col-lg-6">
      <label for="phone" class="form-label">{{ $t('common.phone') }}</label>
      <input
        id="phone"
        type="tel"
        class="form-control"
        value="0901234567"
        pattern="0[35789][0-9]{8}"
        maxlength="10"
      />
    </div>
  </div>
</template>
