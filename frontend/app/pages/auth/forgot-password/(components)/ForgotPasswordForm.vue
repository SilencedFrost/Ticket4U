<script setup lang="ts">
import type { FetchError } from 'ofetch';

const EMAIL_FORMAT_REGEX = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

const config = useRuntimeConfig();
const localePath = useLocalePath();

const loading = ref(false);
const submitted = ref(false);
const email = ref('');
const error = reactive({ email: '', generic: '' });

async function submit() {
  Object.assign(error, { email: '', generic: '' });

  const trimmed = email.value.trim();
  if (!trimmed) {
    error.email = 'auth.error.blank.email';
    return;
  }
  if (!EMAIL_FORMAT_REGEX.test(trimmed)) {
    error.email = 'auth.error.format.email';
    return;
  }

  loading.value = true;
  try {
    await $fetch(`${config.public.authUrl}/forgot-password`, {
      method: 'POST',
      body: { email: email.value.trim() },
    });
    submitted.value = true;
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
  <div class="d-flex justify-content-center align-items-center" style="min-height: 80vh">
    <div class="form-width">
      <h3 class="text-center text-reactive-primary">{{ $t('auth.forgot_password') }}</h3>
      <hr class="my-2" />

      <template v-if="!submitted">
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
      </template>

      <template v-else>
        <div class="text-center">
          <i
            class="bi bi-envelope-check text-success"
            style="font-size: 3.5rem; display: block; margin-bottom: 1rem"
          />
          <p class="text-reactive-secondary">{{ $t('auth.password_reset.check_email') }}</p>
        </div>
      </template>

      <hr class="my-2" />
      <div class="text-center form-text">
        <NuxtLink
          :to="localePath('/auth/login')"
          class="text-decoration-none text-reactive-secondary"
        >
          {{ $t('auth.login.action') }}
        </NuxtLink>
      </div>
    </div>
  </div>
</template>
