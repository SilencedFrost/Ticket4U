<script setup lang="ts">
import type { FetchError } from 'ofetch';

const config = useRuntimeConfig();
const localePath = useLocalePath();
const { t } = useI18n();
const route = useRoute();

const status = ref<'loading' | 'expired' | 'error'>('loading');
const message = ref('');

onMounted(async () => {
  const token = route.query.token as string;

  if (!token) {
    status.value = 'error';
    message.value = t('auth.verification.token_invalid');
    return;
  }

  try {
    await $fetch(`${config.public.authUrl}/verify-email`, {
      method: 'GET',
      params: { token },
    });
    navigateTo(localePath('/auth/login') + '?verified=true', { replace: true });
  } catch (err) {
    const fetchError = err as FetchError;
    const serverMessage = fetchError.data?.message ?? '';

    if (fetchError.statusCode === 410 || serverMessage === 'auth.verification.token_expired') {
      status.value = 'expired';
      message.value = t('auth.verification.token_expired');
    } else {
      status.value = 'error';
      message.value = t(serverMessage || 'auth.verification.token_invalid');
    }
  }
});
</script>

<template>
  <div class="verify-email-wrapper">
    <div class="verify-card bg-reactive-secondary shadow-sm">
      <div v-if="status === 'loading'" class="text-center py-4">
        <div class="spinner-border text-primary mb-3" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <p class="text-reactive-primary">{{ $t('auth.verification.verifying') }}</p>
      </div>

      <div v-else-if="status === 'expired'" class="text-center">
        <i class="bi bi-clock-history text-warning icon-large" />
        <h3 class="text-reactive-primary">{{ $t('auth.verification.expired_title') }}</h3>
        <p class="text-reactive-secondary">{{ message }}</p>
        <NuxtLink :to="localePath('/auth/register')" class="btn btn-outline-primary mt-3">
          {{ $t('auth.register.action') }}
        </NuxtLink>
      </div>

      <div v-else-if="status === 'error'" class="text-center">
        <i class="bi bi-x-circle-fill text-danger icon-large" />
        <h3 class="text-reactive-primary">{{ $t('auth.verification.error_title') }}</h3>
        <p class="text-reactive-secondary">{{ message }}</p>
        <NuxtLink :to="localePath('/auth/login')" class="btn btn-outline-primary mt-3">
          {{ $t('auth.login.action') }}
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
