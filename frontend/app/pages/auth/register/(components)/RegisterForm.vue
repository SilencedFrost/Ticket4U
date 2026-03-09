<script setup lang="ts">
import type { FetchError } from 'ofetch';

const config = useRuntimeConfig();
const localePath = useLocalePath();
const router = useRouter();
const userStore = useUserStore();
const loading = ref<boolean>(false);
const isViewingPassword = ref<boolean>(false);
const registerSuccess = ref<boolean>(false);
const hiddenGoogleBtn = ref<HTMLElement | null>(null);

const {
  loaded: googleLoaded,
  scriptError: googleScriptError,
  clickHiddenButton,
} = useGoogleAuth({
  buttonRef: hiddenGoogleBtn,
  onCredential: handleGoogleCredential,
  buttonText: 'signup_with',
});
async function handleGoogleCredential(idToken: string) {
  loading.value = true;
  Object.assign(error, { ...emptyError, password: [] });
  try {
    await userStore.loginWithGoogle(idToken);
    router.push(localePath('/'));
  } catch (err) {
    handleError(err as FetchError);
  } finally {
    loading.value = false;
  }
}

/**======================
 * Reused constants
 =======================*/

const PHONE_REGEX = /^(0)?(3|5|7|8|9)\d{8}$/;
const EMAIL_FORMAT_REGEX = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const PASSWORD_SPECIAL_CHAR_REGEX = /[!@$^*()_=+[\]{}\\|;:",./?~`-]+/;
const PASSWORD_LOWERCASE_REGEX = /[a-z]+/;
const PASSWORD_UPPERCASE_REGEX = /[A-Z]+/;
const PASSWORD_DIGIT_REGEX = /\d+/;

/**===========
 * Interfaces
 ============*/

interface registerError {
  fullName: string;
  email: string;
  phoneNumber: string;
  password: string[];
  generic: string;
}

interface registerForm {
  fullName: string;
  email: string;
  phoneNumber: string;
  password: string;
}

interface registerTouched {
  fullName: boolean;
  email: boolean;
  phoneNumber: boolean;
  password: boolean;
}

/**======================
 * State constants
 =======================*/

const emptyError: registerError = {
  fullName: '',
  email: '',
  phoneNumber: '',
  password: [] as string[],
  generic: '',
};

const emptyForm: registerForm = {
  fullName: '',
  email: '',
  phoneNumber: '',
  password: '',
};

const defaultTouched: registerTouched = {
  fullName: false,
  email: false,
  phoneNumber: false,
  password: false,
};

/**======================
 * Form reactive objects
 =======================*/

const error = reactive<registerError>(emptyError);
const formData = reactive<registerForm>(emptyForm);
const touched = reactive<registerTouched>(defaultTouched);

/**==========
 * Functions
 ===========*/

// Onblur function to do validation
function onBlur(field: keyof registerForm) {
  if (touched[field] === false) {
    touched[field] = true;
    const validators: Record<keyof registerForm, () => boolean> = {
      fullName: validateFullName,
      email: validateEmail,
      phoneNumber: validatePhone,
      password: validatePassword,
    };

    validators[field]?.();
  }
}

// Validation functions
function validateFullName(): boolean {
  const val = formData.fullName.trim();
  if (!val) {
    error.fullName = 'auth.error.blank.full_name';
    return false;
  }

  error.fullName = '';
  return true;
}

function validateEmail(): boolean {
  const val = formData.email.trim();
  if (!val) {
    error.email = 'auth.error.blank.email';
    return false;
  }

  if (!EMAIL_FORMAT_REGEX.test(val)) {
    error.email = 'auth.error.format.email';
    return false;
  }

  error.email = '';
  return true;
}

function validatePhone(): boolean {
  const val = formData.phoneNumber.trim();
  if (!val) {
    error.phoneNumber = 'auth.error.blank.phone';
    return false;
  }
  if (!PHONE_REGEX.test(val)) {
    error.phoneNumber = 'auth.error.format.phone';
    return false;
  }

  error.phoneNumber = '';
  return true;
}

function validatePassword(): boolean {
  const val = formData.password.trim();
  if (!val) {
    error.password = ['auth.error.blank.password'];
    return false;
  }
  const errors: string[] = [];
  if (val.length < 8) errors.push('auth.error.format.password.length.short');
  if (val.length > 32) errors.push('auth.error.format.password.length.long');
  if (!PASSWORD_LOWERCASE_REGEX.test(val)) errors.push('auth.error.format.password.lowercase');
  if (!PASSWORD_UPPERCASE_REGEX.test(val)) errors.push('auth.error.format.password.uppercase');
  if (!PASSWORD_SPECIAL_CHAR_REGEX.test(val))
    errors.push('auth.error.format.password.special_char');
  if (!PASSWORD_DIGIT_REGEX.test(val)) errors.push('auth.error.format.password.digit');
  error.password = errors;
  return errors.length === 0;
}

// Validate the form, return status
function validateForm(): boolean {
  return validateFullName() && validateEmail() && validatePhone() && validatePassword();
}

// Function to register
async function register() {
  if (!validateForm()) return;

  loading.value = true;

  try {
    const response = await $fetch<{ userId: string | null; message: string }>(
      `${config.public.authUrl}/register`,
      {
        method: 'POST',
        body: {
          email: formData.email.trim(),
          password: formData.password.trim(),
          phoneNumber: formData.phoneNumber.trim(),
          fullName: formData.fullName.trim(),
        },
      },
    );

    registerSuccess.value = true;
    Object.assign(formData, {
      email: '',
      password: '',
      phoneNumber: '',
      fullName: '',
    });
    Object.assign(touched, { ...defaultTouched });
    Object.assign(error, { ...emptyError, password: [] });
  } catch (err) {
    handleError(err as FetchError);
  } finally {
    loading.value = false;
  }
}

// Handle errors returned by backend I suppose
function handleError(fetchError: FetchError) {
  if (!fetchError.statusCode) {
    error.generic = 'auth.error.network';
    return;
  }
  switch (fetchError.statusCode) {
    case 400: {
      const errorMessage = String(fetchError.data?.message ?? fetchError.data?.error ?? '');

      if (
        errorMessage.toLowerCase().includes('email') &&
        (errorMessage.toLowerCase().includes('exists') ||
          errorMessage.toLowerCase().includes('already') ||
          errorMessage.toLowerCase().includes('taken'))
      ) {
        error.email = 'auth.error.registrationFailed';
      } else {
        error.email = fetchError.data?.email || '';
        error.password = fetchError.data?.password ? [fetchError.data.password] : [];
        error.phoneNumber = fetchError.data?.phoneNumber || '';
        error.fullName = fetchError.data?.fullName || '';

        if (!error.email && error.password.length === 0 && !error.phoneNumber && !error.fullName) {
          error.generic = errorMessage || 'auth.error.validation';
        }
      }
      break;
    }
    default:
      error.generic = String(fetchError.data?.message || 'auth.error.unknown');
  }
}

// Helper function to view password
function viewPassword() {
  isViewingPassword.value = !isViewingPassword.value;
}

// Helper function to go to login
function goToLogin() {
  navigateTo(localePath('/auth/login'));
}

/**===================
 * Computed & watches
 ====================*/

const isFormValid = computed(() => {
  const allTouched = touched.fullName && touched.email && touched.phoneNumber && touched.password;

  const noErrors =
    error.fullName === '' &&
    error.email === '' &&
    error.phoneNumber === '' &&
    error.password.length === 0;

  return allTouched && noErrors;
});

watch(
  () => (formData.fullName.trim().length > 0 ? formData.fullName : null),
  () => {
    touched.fullName = true;
    validateFullName();
  },
);

watch(
  () => (formData.email.trim().length > 0 ? formData.email : null),
  () => {
    touched.email = true;
    validateEmail();
  },
);

watch(
  () => (formData.phoneNumber.trim().length > 0 ? formData.phoneNumber : null),
  () => {
    touched.phoneNumber = true;
    validatePhone();
  },
);

watch(
  () => (formData.password.trim().length > 0 ? formData.password : null),
  () => {
    touched.password = true;
    validatePassword();
  },
);
</script>

<template>
  <div class="form-width">
    <h3 class="text-center text-reactive-primary">{{ $t('auth.register.title') }}</h3>
    <hr class="my-2" />

    <div v-if="registerSuccess" class="alert alert-success text-center mb-3">
      <i class="bi bi-check-circle-fill me-2" />
      {{ $t('auth.register.success') }}
    </div>

    <form v-if="!registerSuccess" novalidate @submit.prevent="register">
      <div class="mb-2">
        <label for="reg-fullname" class="form-label text-reactive-primary user-select-none">
          {{ $t('common.full_name') }}<span class="text-danger" aria-hidden="true"> *</span>
        </label>
        <input
          id="reg-fullname"
          v-model="formData.fullName"
          type="text"
          autocomplete="name"
          :aria-invalid="!!error.fullName"
          :aria-describedby="error.fullName ? 'reg-fullname-error' : undefined"
          :disabled="loading"
          :class="[
            'form-control',
            'bg-reactive-primary',
            'text-reactive-primary',
            { 'is-invalid': error.fullName },
          ]"
          @blur="onBlur('fullName')"
        />
        <div
          v-if="error.fullName"
          id="reg-fullname-error"
          class="invalid-feedback"
          aria-live="assertive"
        >
          {{ $t(error.fullName) }}
        </div>
      </div>
      <div class="mb-2">
        <label for="reg-email" class="form-label text-reactive-primary user-select-none">
          {{ $t('common.email') }}<span class="text-danger" aria-hidden="true"> *</span>
        </label>
        <input
          id="reg-email"
          v-model="formData.email"
          type="email"
          autocomplete="email"
          aria-required="true"
          :aria-invalid="!!error.email"
          :aria-describedby="error.email ? 'reg-email-error' : undefined"
          :disabled="loading"
          :class="[
            'form-control',
            'bg-reactive-primary',
            'text-reactive-primary',
            { 'is-invalid': error.email },
          ]"
          @blur="onBlur('email')"
        />
        <div v-if="error.email" id="reg-email-error" class="invalid-feedback" aria-live="assertive">
          {{ $t(error.email) }}
        </div>
      </div>
      <div class="mb-2">
        <label for="reg-phone" class="form-label text-reactive-primary user-select-none">
          {{ $t('common.phone') }}<span class="text-danger" aria-hidden="true"> *</span>
        </label>
        <input
          id="reg-phone"
          v-model="formData.phoneNumber"
          type="tel"
          autocomplete="tel"
          aria-required="true"
          :aria-invalid="!!error.phoneNumber"
          :aria-describedby="error.phoneNumber ? 'reg-phone-error' : undefined"
          :disabled="loading"
          :class="[
            'form-control',
            'bg-reactive-primary',
            'text-reactive-primary',
            { 'is-invalid': error.phoneNumber },
          ]"
          @blur="onBlur('phoneNumber')"
        />
        <div
          v-if="error.phoneNumber"
          id="reg-phone-error"
          class="invalid-feedback"
          aria-live="assertive"
        >
          {{ $t(error.phoneNumber) }}
        </div>
      </div>
      <div class="mb-2">
        <label for="reg-password" class="form-label text-reactive-primary user-select-none">
          {{ $t('auth.password') }}<span class="text-danger" aria-hidden="true"> *</span>
        </label>
        <div class="input-group">
          <input
            id="reg-password"
            v-model="formData.password"
            :type="isViewingPassword ? 'text' : 'password'"
            autocomplete="new-password"
            aria-required="true"
            :aria-invalid="error.password.length > 0"
            :aria-describedby="
              [error.password.length > 0 ? 'reg-password-error' : '', 'reg-password-hint']
                .filter(Boolean)
                .join(' ') || undefined
            "
            :disabled="loading"
            :class="[
              'form-control',
              'bg-reactive-primary',
              'text-reactive-primary',
              { 'is-invalid': error.password.length > 0 },
            ]"
            @blur="onBlur('password')"
          />
          <button
            class="btn btn-outline-secondary bg-reactive-primary"
            type="button"
            :disabled="loading"
            @mousedown.prevent="viewPassword"
          >
            <i :class="isViewingPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
          </button>
        </div>
        <ul
          v-if="error.password.length > 0"
          id="reg-password-error"
          :class="[
            'text-danger',
            'small',
            'mt-1',
            'mb-0',
            { 'ps-3': error.password.length !== 1 },
            { 'list-unstyled': error.password.length === 1 },
          ]"
          aria-live="assertive"
        >
          <li v-for="(err, index) in error.password" :key="index">
            {{ $t(err) }}
          </li>
        </ul>
        <small id="reg-password-hint" class="form-text text-muted">
          {{ $t('auth.register.password_hint') }}
        </small>
      </div>
      <div v-if="error.generic" class="invalid-feedback d-block mb-2" aria-live="assertive">
        {{ $t(error.generic) }}
      </div>
      <div class="d-flex flex-column">
        <button
          class="btn btn-primary text-center mb-2"
          type="submit"
          :disabled="loading || !isFormValid"
        >
          <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" />
          {{ $t('auth.register.action') }}
        </button>
        <div ref="hiddenGoogleBtn" class="d-none" />
        <button
          type="button"
          class="btn btn-reactive-gray"
          :disabled="loading || !googleLoaded"
          @click="clickHiddenButton()"
        >
          <i class="bi bi-google me-2" />
          <span>{{ $t('auth.register.google') }}</span>
        </button>
        <small v-if="googleScriptError" class="text-warning mt-1">
          {{ $t('auth.error.google_unavailable') }}
        </small>
      </div>
    </form>
    <hr class="my-2" />
    <div class="text-center form-text">
      <a href="#" class="text-decoration-none text-reactive-secondary" @click.prevent="goToLogin">
        {{ $t('auth.register.has_account') }}
      </a>
    </div>
  </div>
</template>
