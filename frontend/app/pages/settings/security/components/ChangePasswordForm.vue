<script setup lang="ts">
const { validatePasswordValue } = usePasswordValidation();

/**===========
 * Interfaces
 ============*/

interface ChangepassError {
  oldPass: string;
  newPass: string[];
}

interface ChangepassForm {
  oldPass: string;
  newPass: string;
}

interface ChangepassTouched {
  oldPass: boolean;
  newPass: boolean;
}

/**======================
 * State constants
 =======================*/

const emptyError: ChangepassError = {
  oldPass: '',
  newPass: [],
};

const emptyForm: ChangepassForm = {
  oldPass: '',
  newPass: '',
};

const defaultTouched: ChangepassTouched = {
  oldPass: false,
  newPass: false,
};

/**======================
 * Form reactive objects
 =======================*/

const loading = ref<boolean>(false);
const isViewingCurrentPassword = ref(false);
const isViewingNewPassword = ref(false);

const error = reactive<ChangepassError>(emptyError);
const formData = reactive<ChangepassForm>(emptyForm);
const touched = reactive<ChangepassTouched>(defaultTouched);

/**==========
 * Functions
 ===========*/

// Onblur function to do validation
function onBlur(field: keyof ChangepassForm) {
  if (touched[field] === false) {
    touched[field] = true;
    const validators: Record<keyof ChangepassForm, () => boolean> = {
      oldPass: validateOldPass,
      newPass: validateNewPass,
    };

    validators[field]?.();
  }
}

// Validation functions
function validateOldPass(): boolean {
  const val = formData.oldPass.trim();
  if (!val) {
    error.oldPass = 'auth.error.blank.password';
    return false;
  }

  error.oldPass = '';
  return true;
}

function validateNewPass(): boolean {
  const val = formData.newPass.trim();
  if (!val) {
    error.newPass = ['auth.error.blank.password'];
    return false;
  }
  error.newPass = validatePasswordValue(val);
  return error.newPass.length === 0;
}

// Validate the form, return status
function validateForm(): boolean {
  return validateNewPass() && validateOldPass();
}

// Helper function to view password
function toggleCurrentPasswordVisibility() {
  isViewingCurrentPassword.value = !isViewingCurrentPassword.value;
}

function toggleNewPasswordVisibility() {
  isViewingNewPassword.value = !isViewingNewPassword.value;
}

/**===================
 * Computed & watches
 ====================*/

const isFormValid = computed(() => {
  const allTouched = touched.newPass && touched.oldPass;

  const noErrors = error.oldPass === '' && error.newPass.length === 0;

  return allTouched && noErrors;
});

watch(
  () => (formData.oldPass.trim().length > 0 ? formData.oldPass : null),
  () => {
    touched.oldPass = true;
    validateOldPass();
  },
);

watch(
  () => (formData.newPass.trim().length > 0 ? formData.newPass : null),
  () => {
    touched.newPass = true;
    validateNewPass();
  },
);

function submitChangePassword() {
  if (!validateForm()) return;
  // TODO: wire up API submit.
}
</script>

<template>
  <div class="change-password-form">
    <div class="card p-3 shadow-sm">
      <h2 class="h6 fw-bold mb-3 text-primary">
        <i class="bi bi-key-fill"></i>
        {{ $t('settings.security.change_password.title') }}
      </h2>

      <div class="row g-2">
        <div class="col-12 col-lg-6">
          <label for="current-password" class="form-label">
            {{ $t('settings.security.change_password.current_password') }}
          </label>
          <div class="input-group">
            <input
              id="current-password"
              v-model="formData.oldPass"
              :type="isViewingCurrentPassword ? 'text' : 'password'"
              :class="['form-control', { 'is-invalid': error.oldPass }]"
              @blur="onBlur('oldPass')"
            />
            <button
              class="btn btn-outline-secondary bg-reactive-primary"
              type="button"
              @mousedown.prevent="toggleCurrentPasswordVisibility"
            >
              <i :class="isViewingCurrentPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
            </button>
          </div>
          <div v-if="error.oldPass" id="old-password-error" class="text-danger small">
            {{ $t(error.oldPass) }}
          </div>
        </div>

        <div class="col-12 col-lg-6">
          <label for="new-password" class="form-label">
            {{ $t('settings.security.change_password.new_password') }}
          </label>
          <div class="input-group">
            <input
              id="new-password"
              v-model="formData.newPass"
              :type="isViewingNewPassword ? 'text' : 'password'"
              :class="['form-control', { 'is-invalid': error.newPass.length > 0 }]"
              @blur="onBlur('newPass')"
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
            v-if="error.newPass.length > 0"
            id="reg-password-error"
            :class="[
              'text-danger',
              'small',
              'mt-1',
              'mb-0',
              { 'ps-3': error.newPass.length !== 1 },
              { 'list-unstyled': error.newPass.length === 1 },
            ]"
            aria-live="assertive"
          >
            <li v-for="(err, index) in error.newPass" :key="index">
              {{ $t(err) }}
            </li>
          </ul>
        </div>
      </div>

      <button class="btn btn-primary ms-auto mt-3" :disabled="loading || !isFormValid">
        <i class="bi bi-floppy-fill text-white" @click="submitChangePassword()"></i>
      </button>
    </div>
  </div>
</template>

<style scoped></style>
