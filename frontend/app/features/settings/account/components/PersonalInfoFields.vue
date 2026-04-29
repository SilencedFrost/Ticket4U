<script setup lang="ts">
import type { FieldErrors } from '../../types/fieldErrors';
import type { ProfileForm } from '../../types/profileForm';
import ChangeEmailModal from './ChangeEmailModal.vue';
import { usePasswordValidation } from '~/composables/usePasswordValidation';

const model = defineModel<ProfileForm>({ required: true });

// TODO: add i18n key for birthday errors
// TODO: add proper validation for old password field
// TODO: add [i] that when user hovers mouse over or click "blank fields are unsaved"
// TODO: allow nullable phone number
const props = withDefaults(
  defineProps<{
    errors?: FieldErrors;
    disabled?: boolean;
    emailLoading?: boolean;
    emailErrors?: {
      newEmail?: string;
      password?: string;
      generic?: string;
    };
  }>(),
  {
    errors: () => ({}),
    disabled: false,
    emailLoading: false,
    emailErrors: () => ({}),
  },
);
const emit = defineEmits<{
  (e: 'change-email', payload: { newEmail: string; password: string }): void;
}>();

const { validatePasswordValue } = usePasswordValidation();
const loadPasswordError = ref<string | undefined>(undefined);

const mergedEmailErrors = computed(() => ({
  newEmail: props.emailErrors?.newEmail,
  password: loadPasswordError.value ?? props.emailErrors?.password,
  generic: props.emailErrors?.generic,
}));

function updateField(field: keyof ProfileForm, value: string) {
  model.value = {
    ...model.value,
    [field]: value,
  };
}

const emailModalRef = ref<InstanceType<typeof ChangeEmailModal> | null>(null);

function openEmailChangeModal() {
  emailModalRef.value?.open();
}

// Gọi từ parent khi đổi email thành công
function closeEmailModal() {
  emailModalRef.value?.close();
}

defineExpose({ closeEmailModal });

function handleChangeEmailSubmit(payload: { newEmail: string; password: string }) {
  const formatErrors = validatePasswordValue(payload.password);
  if (formatErrors.length > 0) {
    loadPasswordError.value = formatErrors[0];
    return;
  }

  loadPasswordError.value = undefined;
  emit('change-email', payload);
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
      <!-- <input id="email" type="email" class="form-control" :value="model.email" readonly /> -->
      <div class="input-group">
        <input id="email" type="email" class="form-control" :value="model.email" readonly />
        <button type="button" class="btn btn-outline-primary" @click="openEmailChangeModal">
          {{ $t('common.action.change_email') }}
        </button>
      </div>
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
        :max="new Date().toISOString().split('T')[0]"
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

    <ChangeEmailModal
      ref="emailModalRef"
      :current-email="model.email"
      :loading="emailLoading"
      :errors="mergedEmailErrors"
      @submit="handleChangeEmailSubmit"
    />
  </div>
</template>
