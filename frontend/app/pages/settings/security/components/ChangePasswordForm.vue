<script setup lang="ts">
import type { FetchError } from 'ofetch';
import { useI18nErrorKey } from '../../../../composables/useI18nErrorKey';
import { useSettingsApi } from '../../composables/useSettingsApi';

const { validatePasswordValue } = usePasswordValidation();
const { changePassword, extractFieldErrors, extractMessage } = useSettingsApi();
const { toValidationErrorI18nKey, toGenericErrorI18nKey } = useI18nErrorKey();


interface ChangePasswordError {
  currentPassword: string;
  newPassword: string[];
}

interface ChangePasswordForm {
  currentPassword: string;
  newPassword: string;
}

interface ChangePasswordTouched {
  currentPassword: boolean;
  newPassword: boolean;
}


const emptyError: ChangePasswordError = {
  currentPassword: '',
  newPassword: [],
};

const emptyForm: ChangePasswordForm = {
  currentPassword: '',
  newPassword: '',
};

const defaultTouched: ChangePasswordTouched = {
  currentPassword: false,
  newPassword: false,
};

const loading = ref<boolean>(false);
const isViewingCurrentPassword = ref(false);
const isViewingNewPassword = ref(false);
const genericError = ref('');
const successMessage = ref('');

const error = reactive<ChangePasswordError>(emptyError);
const formData = reactive<ChangePasswordForm>(emptyForm);
const touched = reactive<ChangePasswordTouched>(defaultTouched);


// Onblur function to do validation
function onBlur(field: keyof ChangePasswordForm) {
  if (touched[field]) {
    return;
  }

  touched[field] = true;

  if (field === 'currentPassword') {
    validateCurrentPassword();
    return;
  }

  validateNewPassword();
}

// Validation functions
function validateCurrentPassword(): boolean {
  const val = formData.currentPassword.trim();
  if (!val) {
    error.currentPassword = 'auth.error.blank.password';
    return false;
  }

  error.currentPassword = '';
  return true;
}

function validateNewPassword(): boolean {
  const val = formData.newPassword.trim();
  if (!val) {
    error.newPassword = ['auth.error.blank.password'];
    return false;
  }

  error.newPassword = validatePasswordValue(val);
  return error.newPassword.length === 0;
}

// Validate the form, return status
function validateForm(): boolean {
  return validateCurrentPassword() && validateNewPassword();
}

// Helper function to view password
function toggleCurrentPasswordVisibility() {
  isViewingCurrentPassword.value = !isViewingCurrentPassword.value;
}

function toggleNewPasswordVisibility() {
  isViewingNewPassword.value = !isViewingNewPassword.value;
}

const isFormValid = computed(() => {
  const allTouched = touched.newPassword && touched.currentPassword;

  const noErrors = error.currentPassword === '' && error.newPassword.length === 0;

  return allTouched && noErrors;
});

watch(
  () => (formData.currentPassword.trim().length > 0 ? formData.currentPassword : null),
  (value) => {
    if (value === null) {
      return;
    }

    touched.currentPassword = true;
    validateCurrentPassword();
  },
);

watch(
  () => (formData.newPassword.trim().length > 0 ? formData.newPassword : null),
  (value) => {
    if (value === null) {
      return;
    }

    touched.newPassword = true;
    validateNewPassword();
  },
);

function submitChangePassword() {
  void submitChangePasswordAsync();
}

function resetFormState() {
  formData.currentPassword = '';
  formData.newPassword = '';
  touched.currentPassword = false;
  touched.newPassword = false;
  error.currentPassword = '';
  error.newPassword = [];
}

function beginSubmit() {
  loading.value = true;
  genericError.value = '';
  successMessage.value = '';
}

function applyPasswordChangeSuccess() {
  resetFormState();
  successMessage.value = 'settings.security.change_password.messages.success';
}

function applyPasswordFieldErrors(fieldErrors: Record<string, string>): boolean {
  let hasFieldErrors = false;

  if (fieldErrors.currentPassword) {
    error.currentPassword = toValidationErrorI18nKey(fieldErrors.currentPassword);
    hasFieldErrors = true;
  }

  if (fieldErrors.newPassword) {
    error.newPassword = [toValidationErrorI18nKey(fieldErrors.newPassword)];
    hasFieldErrors = true;
  }

  return hasFieldErrors;
}

function isCurrentPasswordErrorMessage(message: string): boolean {
  // Fallback for legacy backend responses that do not include stable error codes.
  return message.toLowerCase().includes('current password');
}

function handlePasswordChangeError(fetchError: FetchError) {
  if (!fetchError.statusCode) {
    genericError.value = 'auth.error.network';
    return;
  }

  const fieldErrors = extractFieldErrors(fetchError);
  const hasFieldErrors = applyPasswordFieldErrors(fieldErrors);

  if (hasFieldErrors) {
    return;
  }

  const message = extractMessage(fetchError);

  if (message && isCurrentPasswordErrorMessage(message)) {
    error.currentPassword = 'settings.security.change_password.errors.current_password_incorrect';
    return;
  }

  genericError.value = toGenericErrorI18nKey(message ?? undefined);
}

async function submitChangePasswordAsync() {
  if (!validateForm() || loading.value) {
    return;
  }

  beginSubmit();

  try {
    await changePassword({
      currentPassword: formData.currentPassword.trim(),
      newPassword: formData.newPassword.trim(),
    });

    applyPasswordChangeSuccess();
  } catch (err) {
    handlePasswordChangeError(err as FetchError);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="change-password-form">
    <form class="card p-3 shadow-sm" novalidate @submit.prevent="submitChangePassword">
      <h2 class="h6 fw-bold mb-3 text-primary">
        <i class="bi bi-key-fill"></i>
        {{ $t('settings.security.change_password.title') }}
      </h2>

      <div v-if="genericError" class="alert alert-danger py-2" role="alert">
        {{ $t(genericError) }}
      </div>

      <div v-if="successMessage" class="alert alert-success py-2" role="status">
        {{ $t(successMessage) }}
      </div>

      <div class="row g-2">
        <div class="col-12 col-lg-6">
          <label for="current-password" class="form-label">
            {{ $t('settings.security.change_password.current_password') }}
          </label>
          <div class="input-group">
            <input
              id="current-password"
              v-model="formData.currentPassword"
              :type="isViewingCurrentPassword ? 'text' : 'password'"
              :class="['form-control', { 'is-invalid': error.currentPassword }]"
              @blur="onBlur('currentPassword')"
            />
            <button
              class="btn btn-outline-secondary bg-reactive-primary"
              type="button"
              @mousedown.prevent="toggleCurrentPasswordVisibility"
            >
              <i :class="isViewingCurrentPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
            </button>
          </div>
          <div v-if="error.currentPassword" id="current-password-error" class="text-danger small">
            {{ $t(error.currentPassword) }}
          </div>
        </div>

        <div class="col-12 col-lg-6">
          <label for="new-password" class="form-label">
            {{ $t('settings.security.change_password.new_password') }}
          </label>
          <div class="input-group">
            <input
              id="new-password"
              v-model="formData.newPassword"
              :type="isViewingNewPassword ? 'text' : 'password'"
              :class="['form-control', { 'is-invalid': error.newPassword.length > 0 }]"
              @blur="onBlur('newPassword')"
            />
            <button
              class="btn btn-outline-secondary bg-reactive-primary"
              type="button"
              @mousedown.prevent="toggleNewPasswordVisibility"
            >
              <i :class="isViewingNewPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
            </button>
          </div>
          <ul
            v-if="error.newPassword.length > 0"
            id="reg-password-error"
            :class="[
              'text-danger',
              'small',
              'mt-1',
              'mb-0',
              { 'ps-3': error.newPassword.length !== 1 },
              { 'list-unstyled': error.newPassword.length === 1 },
            ]"
            aria-live="assertive"
          >
            <li v-for="(err, index) in error.newPassword" :key="index">
              {{ $t(err) }}
            </li>
          </ul>
        </div>
      </div>

      <button type="submit" class="btn btn-primary ms-auto mt-3" :disabled="loading || !isFormValid">
        <i class="bi bi-floppy-fill text-white"></i>
      </button>
    </form>
  </div>
</template>
