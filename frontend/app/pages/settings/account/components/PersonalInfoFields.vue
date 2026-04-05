<script setup lang="ts">
import { useI18nErrorKey } from '../../../../composables/useI18nErrorKey';
import type { FieldErrors, ProfileForm } from '../../types/settings';

const { toValidationErrorI18nKey } = useI18nErrorKey();

const props = withDefaults(
  defineProps<{
    modelValue: ProfileForm;
    errors?: FieldErrors;
    disabled?: boolean;
  }>(),
  {
    errors: () => ({}),
    disabled: false,
  },
);

const emit = defineEmits<{
  (e: 'update:modelValue', value: ProfileForm): void;
}>();

function updateField(field: keyof ProfileForm, value: string) {
  emit('update:modelValue', {
    ...props.modelValue,
    [field]: value,
  });
}
</script>

<template>
  <div class="row g-2">
    <!-- Họ & Tên -->

    <div class="col-lg-6">
      <label for="first-name" class="form-label">{{ $t('common.first_name') }}</label>
      <input
        id="first-name"
        :value="modelValue.firstName"
        type="text"
        class="form-control"
        :class="{ 'is-invalid': !!errors.firstName }"
        maxlength="32"
        :disabled="disabled"
        @input="updateField('firstName', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.firstName" class="invalid-feedback d-block">
        {{ $t(toValidationErrorI18nKey(errors.firstName)) }}
      </div>
    </div>

    <div class="col-lg-6">
      <label for="last-name" class="form-label">{{ $t('common.last_name') }}</label>
      <input
        id="last-name"
        :value="modelValue.lastName"
        type="text"
        class="form-control"
        :class="{ 'is-invalid': !!errors.lastName }"
        maxlength="32"
        :disabled="disabled"
        @input="updateField('lastName', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.lastName" class="invalid-feedback d-block">
        {{ $t(toValidationErrorI18nKey(errors.lastName)) }}
      </div>
    </div>

    <!-- Email -->
    <div class="col-12">
      <label for="email" class="form-label">{{ $t('common.email') }}</label>
      <input id="email" type="email" class="form-control" :value="modelValue.email" readonly />
    </div>

    <!-- Ngày sinh -->
    <div class="col-lg-6">
      <label for="birthday" class="form-label">{{ $t('common.birthday') }}</label>
      <input
        id="birthday"
        :value="modelValue.birthday"
        type="date"
        class="form-control"
        :class="{ 'is-invalid': !!errors.birthday }"
        :disabled="disabled"
        @input="updateField('birthday', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.birthday" class="invalid-feedback d-block">
        {{ $t(toValidationErrorI18nKey(errors.birthday)) }}
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
        :value="modelValue.phoneNumber"
        pattern="0[35789][0-9]{8}"
        maxlength="10"
        :disabled="disabled"
        @input="updateField('phoneNumber', ($event.target as HTMLInputElement).value)"
      />
      <div v-if="errors.phoneNumber" class="invalid-feedback d-block">
        {{ $t(toValidationErrorI18nKey(errors.phoneNumber)) }}
      </div>
    </div>
  </div>
</template>
