<script setup lang="ts">
import type { FetchError } from 'ofetch';
import type { LocationQueryValue } from 'vue-router';

const localePath = useLocalePath();
const router = useRouter();
const route = useRoute();
const loading = ref<boolean>(false);
const useUser = useUserStore();
const error = reactive({ email: '', password: '', generic: '' });
const isViewingPassword = ref<boolean>(false);
const hiddenGoogleBtn = ref<HTMLElement | null>(null);
const formData = reactive({
  email: '',
  password: '',
  rememberMe: false,
});

const {
  loaded: googleLoaded,
  scriptError: googleScriptError,
  clickHiddenButton,
} = useGoogleAuth({
  buttonRef: hiddenGoogleBtn,
  onCredential: handleGoogleCredential,
  buttonText: 'signin_with',
});

function isTrueQueryFlag(value: LocationQueryValue | LocationQueryValue[] | undefined): boolean {
  if (!value) return false;
  const resolvedValue = Array.isArray(value) ? value[0] : value;
  return resolvedValue === 'true';
}

function resolveNoticeMessage(): string {
  if (isTrueQueryFlag(route.query.verified)) return 'auth.verification.success';
  if (isTrueQueryFlag(route.query.registered)) return 'auth.register.success';
  if (isTrueQueryFlag(route.query.passwordResetRequested)) return 'auth.password_reset.check_email';
  if (isTrueQueryFlag(route.query.passwordReset)) return 'auth.password_reset.success';
  return '';
}

const noticeMessage = ref<string>(resolveNoticeMessage());

async function handleGoogleCredential(idToken: string) {
  loading.value = true;
  Object.assign(error, { email: '', password: '', generic: '' });
  try {
    await useUser.loginWithGoogle(idToken);
    router.push(localePath('/'));
  } catch (err) {
    const fetchError = err as FetchError;
    if (!fetchError.statusCode) {
      error.generic = 'auth.error.network';
      return;
    }
    error.generic = 'auth.error.unauthorized';
  } finally {
    loading.value = false;
  }
}

async function login() {
  loading.value = true;
  noticeMessage.value = '';
  Object.assign(error, { email: '', password: '', generic: '' });
  try {
    await useUser.login(formData.email, formData.password, formData.rememberMe);
    router.push(localePath('/'));
  } catch (err) {
    const fetchError = err as FetchError;

    if (!fetchError.statusCode) {
      error.generic = 'auth.error.network';
      return;
    }

    switch (fetchError.statusCode) {
      case 400:
        error.email = fetchError.data?.email || '';
        error.password = fetchError.data?.password || '';
        break;
      case 401:
        error.generic = 'auth.error.unauthorized';
        break;
      case 403:
        error.generic = 'auth.error.account_not_verified';
        break;
      default:
        error.generic = 'auth.error.unknown';
        break;
    }
  } finally {
    loading.value = false;
  }
}

function togglePassword() {
  isViewingPassword.value = !isViewingPassword.value;
}
</script>

<template>
  <div class="form-width">
    <h3 class="text-center text-reactive-primary">{{ $t('auth.login.title') }}</h3>
    <div
      v-if="noticeMessage"
      class="alert alert-success d-flex align-items-center mt-2 mb-0"
      role="alert"
    >
      <i class="bi bi-check-circle-fill me-2" />
      <span>{{ $t(noticeMessage) }}</span>
    </div>
    <hr class="my-2" />
    <form novalidate>
      <div class="mb-2">
        <label for="email" class="form-label text-reactive-primary user-select-none"
          >{{ $t('common.email') }}:</label
        >
        <input
          id="email"
          v-model="formData.email"
          type="email"
          :class="[
            'form-control',
            'bg-reactive-primary',
            'text-reactive-primary',
            { 'is-invalid': error.email },
          ]"
        />
        <div v-if="error.email" id="error-email" class="invalid-feedback">
          {{ $t(error.email) }}
        </div>
      </div>
      <div class="mb-2">
        <label for="password" class="form-label text-reactive-primary user-select-none"
          >{{ $t('auth.password') }}:</label
        >
        <div class="input-group">
          <input
            id="password"
            v-model="formData.password"
            :type="isViewingPassword ? 'text' : 'password'"
            :class="[
              'form-control',
              'bg-reactive-primary',
              'text-reactive-primary',
              { 'is-invalid': error.password },
            ]"
          />
          <button
            class="btn btn-outline-secondary bg-reactive-primary"
            type="button"
            @mousedown.prevent="togglePassword"
          >
            <i :class="isViewingPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
          </button>
        </div>
        <div v-if="error.password" id="error-password" class="invalid-feedback d-block">
          {{ $t(error.password) }}
        </div>
      </div>
      <div class="form-check mb-2">
        <input
          id="rememberMe"
          v-model="formData.rememberMe"
          type="checkbox"
          class="form-check-input"
        />
        <label for="rememberMe" class="form-check-label text-reactive-primary user-select-none">{{
          $t('auth.remember_me')
        }}</label>
      </div>
      <div v-if="error.generic" id="error-generic" class="invalid-feedback d-block mb-2">
        {{ $t(error.generic) }}
      </div>
      <div class="d-flex flex-column">
        <button
          id="submit-btn"
          class="btn btn-primary text-center mb-2"
          :disabled="loading"
          @click.prevent.stop="login()"
        >
          <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" />
          {{ $t('auth.login.action') }}
        </button>
        <div ref="hiddenGoogleBtn" class="d-none" />
        <button
          type="button"
          class="btn btn-reactive-gray"
          :disabled="loading || !googleLoaded"
          @click="clickHiddenButton()"
        >
          <i class="bi bi-google me-2" />
          <span>{{ $t('auth.login.google') }}</span>
        </button>
        <small v-if="googleScriptError" class="text-warning mt-1">
          {{ $t('auth.error.google_unavailable') }}
        </small>
      </div>
    </form>
    <hr class="my-2" />
    <div class="text-center form-text">
      <NuxtLink
        :to="localePath('/auth/register')"
        class="text-decoration-none text-reactive-secondary"
        >{{ $t('auth.create_account') }}</NuxtLink
      >
      |
      <NuxtLink
        :to="localePath('/auth/forgot-password')"
        class="text-decoration-none text-reactive-secondary"
        >{{ $t('auth.forgot_password') }}</NuxtLink
      >
    </div>
  </div>
</template>
