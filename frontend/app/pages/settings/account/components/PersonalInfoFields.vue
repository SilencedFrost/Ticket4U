<script setup lang="ts">
import type { ProfileForm } from '../../../../features/settings/types/profileForm';
import type { FieldErrors } from '../../types/settings';

const model = defineModel<ProfileForm>({ required: true });

// TODO: add i18n key for birthday errors
// TODO: add proper validation for old password field
// TODO: add [i] that when user hovers mouse over or click "blank fields are unsaved"
// TODO: allow nullable phone number
withDefaults(
  defineProps<{
    errors?: FieldErrors;
    disabled?: boolean;
  }>(),
  {
    errors: () => ({}),
    disabled: false,
  },
);

function updateField(field: keyof ProfileForm, value: string) {
  model.value = {
    ...model.value,
    [field]: value,
  };
}
</script>

<template>
  <div class="row g-2">
    <!-- Họ & Tên -->

    <div class="col-lg-6">
      <label for="first-name" class="form-label">{{ $t('common.first_name') }}</label>
      <input
        id="first-name"
        :value="model.firstName"
        type="text"
        class="form-control"
        :class="{ 'is-invalid': !!errors.firstName }"
        maxlength="32"
        :disabled="disabled"
        @input="updateField('firstName', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.firstName" class="invalid-feedback d-block">
        {{ $t(errors.firstName) }}
      </div>
    </div>

    <div class="col-lg-6">
      <label for="last-name" class="form-label">{{ $t('common.last_name') }}</label>
      <input
        id="last-name"
        :value="model.lastName"
        type="text"
        class="form-control"
        :class="{ 'is-invalid': !!errors.lastName }"
        maxlength="32"
        :disabled="disabled"
        @input="updateField('lastName', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.lastName" class="invalid-feedback d-block">
        {{ $t(errors.lastName) }}
      </div>
    </div>

    <!-- Email -->
    <div class="col-12">
      <label for="email" class="form-label">{{ $t('common.email') }}</label>
      <input id="email" type="email" class="form-control" :value="model.email" readonly />
    </div>

    <!-- Ngày sinh -->
    <div class="col-lg-6">
      <label for="birthday" class="form-label">{{ $t('common.birthday') }}</label>
      <input
        id="birthday"
        :value="model.birthday"
        type="date"
        class="form-control"
        :class="{ 'is-invalid': !!errors.birthday }"
        :disabled="disabled"
        @input="updateField('birthday', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.birthday" class="invalid-feedback d-block">
        {{ $t(errors.birthday) }}
      </div>
    </div>

    <!-- Số điện thoại -->
    <div class="col-lg-6">
      <label for="phone" class="form-label">{{ $t('common.phone') }}</label>
      <input
        id="phone"
        type="tel"
        class="form-control"
        :class="{ 'is-invalid': !!errors.phoneNumber }"
        :value="model.phoneNumber"
        pattern="0[35789][0-9]{8}"
        maxlength="10"
        :disabled="disabled"
        @input="updateField('phoneNumber', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.phoneNumber" class="invalid-feedback d-block">
        {{ $t(errors.phoneNumber) }}
      </div>
    </div>
  </div>
</template>
