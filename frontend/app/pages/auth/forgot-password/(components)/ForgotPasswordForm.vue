<script setup lang="ts">
import type { FetchError } from 'ofetch';
const { isEmailFormatValid } = useEmailValidation();

const config = useRuntimeConfig();
const localePath = useLocalePath();

const loading = ref(false);
const email = ref('');
const touched = reactive({ email: false });
const error = reactive({ email: '', generic: '' });

function validateEmail(): boolean {
  const trimmed = email.value.trim();

  if (!trimmed) {
    error.email = 'auth.error.blank.email';
    return false;
  }

  if (!isEmailFormatValid(trimmed)) {
    error.email = 'auth.error.format.email';
    return false;
  }

  error.email = '';
  return true;
}

function onEmailBlur() {
  if (touched.email) return;
  touched.email = true;
  validateEmail();
}

watch(
  () => (email.value.trim().length > 0 ? email.value : null),
  () => {
    touched.email = true;
    validateEmail();
  },
);

async function submit() {
  touched.email = true;
  error.generic = '';

  if (!validateEmail()) return;

  loading.value = true;
  try {
    await $fetch(`${config.public.authUrl}/forgot-password`, {
      method: 'POST',
      body: { email: email.value.trim() },
    });
    await navigateTo(
      { path: localePath('/auth/login'), query: { passwordResetRequested: 'true' } },
      { replace: true },
    );
  } catch (err) {
    const fetchError = err as FetchError;
    if (!fetchError.statusCode) {
      error.generic = 'auth.error.network';
      return;
    }
    if (fetchError.statusCode === 400) {
      error.email = fetchError.data?.email || 'auth.error.format.email';
    } else {
      error.generic = 'auth.error.unknown';
    }
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="form-width">
    <h3 class="text-center text-reactive-primary">{{ $t('auth.forgot_password') }}</h3>
    <hr class="my-2" />

    <p class="text-reactive-secondary mb-3">{{ $t('auth.password_reset.instruction') }}</p>
    <form novalidate @submit.prevent="submit">
      <div class="mb-3">
        <label for="email" class="form-label text-reactive-primary user-select-none">
          {{ $t('common.email') }}:
        </label>
        <input
          id="email"
          v-model="email"
          type="email"
          :class="[
            'form-control',
            'bg-reactive-primary',
            'text-reactive-primary',
            { 'is-invalid': error.email },
          ]"
          @blur="onEmailBlur"
        />
        <div v-if="error.email" class="invalid-feedback">
          {{ $t(error.email) }}
        </div>
      </div>
      <div v-if="error.generic" class="invalid-feedback d-block mb-2">
        {{ $t(error.generic) }}
      </div>
      <button class="btn btn-primary w-100" :disabled="loading">
        <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" />
        {{ $t('auth.password_reset.send_action') }}
      </button>
    </form>

    <hr class="my-2" />
    <div class="text-center form-text">
      <NuxtLink
        :to="localePath('/auth/register')"
        class="text-decoration-none text-reactive-secondary"
      >
        {{ $t('auth.create_account') }}
      </NuxtLink>
      |
      <NuxtLink
        :to="localePath('/auth/login')"
        class="text-decoration-none text-reactive-secondary"
      >
        {{ $t('auth.login.action') }}
      </NuxtLink>
    </div>
  </div>
</template>
