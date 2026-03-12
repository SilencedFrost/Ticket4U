<script setup lang="ts">
import type { FetchError } from 'ofetch';

const { validatePasswordValue } = usePasswordValidation();
const config = useRuntimeConfig();
const localePath = useLocalePath();
const { t } = useI18n();
const route = useRoute();

const status = ref<'form' | 'expired' | 'invalid' | 'success'>('form');
const message = ref('');
const loading = ref(false);
const isViewingPassword = ref(false);
const password = ref('');
const touched = ref(false);
const error = reactive({ password: [] as string[], generic: '' });

const token = computed(() => {
  const rawToken = route.query.token;
  return Array.isArray(rawToken) ? rawToken[0] : rawToken;
});

onMounted(() => {
  if (!token.value) {
    status.value = 'invalid';
    message.value = t('auth.password_reset.token_invalid');
  }
});

function validatePassword(): boolean {
  error.password = validatePasswordValue(password.value);
  return error.password.length === 0;
}

watch(password, () => {
  if (touched.value) validatePassword();
});

async function submit() {
  touched.value = true;
  error.generic = '';

  if (!validatePassword()) return;

  loading.value = true;
  try {
    await $fetch(`${config.public.authUrl}/reset-password`, {
      method: 'POST',
      body: { token: token.value, password: password.value },
    });
    status.value = 'success';
  } catch (err) {
    const fetchError = err as FetchError;
    const serverMessage = fetchError.data?.message ?? '';

    if (fetchError.statusCode === 410 || serverMessage === 'auth.password_reset.token_expired') {
      status.value = 'expired';
      message.value = t('auth.password_reset.token_expired');
    } else if (
      fetchError.statusCode === 400 ||
      serverMessage === 'auth.password_reset.token_invalid'
    ) {
      status.value = 'invalid';
      message.value = t('auth.password_reset.token_invalid');
    } else if (!fetchError.statusCode) {
      error.generic = 'auth.error.network';
    } else {
      error.generic = 'auth.error.unknown';
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
  <div class="verify-email-wrapper">
    <div class="verify-card bg-reactive-secondary shadow-sm">
      <template v-if="status === 'form'">
        <h3 class="text-center text-reactive-primary">{{ $t('auth.password_reset.title') }}</h3>
        <p class="text-reactive-secondary mb-3">
          {{ $t('auth.password_reset.new_password_hint') }}
        </p>
        <form novalidate @submit.prevent="submit">
          <div class="mb-3">
            <label for="password" class="form-label text-reactive-primary user-select-none">
              {{ $t('auth.password') }}:
            </label>
            <div class="input-group">
              <input
                id="password"
                v-model="password"
                :type="isViewingPassword ? 'text' : 'password'"
                :class="[
                  'form-control',
                  'bg-reactive-primary',
                  'text-reactive-primary',
                  { 'is-invalid': touched && error.password.length > 0 },
                ]"
                @blur="
                  touched = true;
                  validatePassword();
                "
              />
              <button
                class="btn btn-outline-secondary bg-reactive-primary"
                type="button"
                @mousedown.prevent="togglePassword"
              >
                <i :class="isViewingPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
              </button>
            </div>
            <ul
              v-if="touched && error.password.length > 0"
              class="text-danger small mt-1 mb-0 ps-3"
            >
              <li v-for="err in error.password" :key="err">{{ $t(err) }}</li>
            </ul>
            <small class="form-text text-reactive-secondary">
              {{ $t('auth.register.password_hint') }}
            </small>
          </div>
          <div v-if="error.generic" class="invalid-feedback d-block mb-2">
            {{ $t(error.generic) }}
          </div>
          <button class="btn btn-primary w-100" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" />
            {{ $t('auth.password_reset.action') }}
          </button>
        </form>
      </template>

      <div v-else-if="status === 'success'" class="text-center">
        <i class="bi bi-check-circle-fill text-success icon-large" />
        <h3 class="text-reactive-primary">{{ $t('auth.password_reset.success_title') }}</h3>
        <p class="text-reactive-secondary">{{ $t('auth.password_reset.success') }}</p>
        <NuxtLink :to="localePath('/auth/login')" class="btn btn-primary mt-3">
          {{ $t('auth.login.action') }}
        </NuxtLink>
      </div>

      <div v-else-if="status === 'expired'" class="text-center">
        <i class="bi bi-clock-history text-warning icon-large" />
        <h3 class="text-reactive-primary">{{ $t('auth.password_reset.expired_title') }}</h3>
        <p class="text-reactive-secondary">{{ message }}</p>
        <NuxtLink :to="localePath('/auth/forgot-password')" class="btn btn-primary mt-3">
          {{ $t('auth.password_reset.request_again') }}
        </NuxtLink>
      </div>

      <div v-else-if="status === 'invalid'" class="text-center">
        <i class="bi bi-x-circle-fill text-danger icon-large" />
        <h3 class="text-reactive-primary">{{ $t('auth.password_reset.error_title') }}</h3>
        <p class="text-reactive-secondary">{{ message }}</p>
        <NuxtLink :to="localePath('/auth/forgot-password')" class="btn btn-outline-primary mt-3">
          {{ $t('auth.password_reset.request_again') }}
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
.verify-email-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  padding: 1rem;
}

.verify-card {
  max-width: 480px;
  width: 100%;
  padding: 2.5rem;
  border-radius: 12px;
}

.icon-large {
  font-size: 3.5rem;
  display: block;
  margin-bottom: 1rem;
}
</style>
