<script setup lang="ts">
import type { FetchError } from 'ofetch';

const config = useRuntimeConfig();
const localePath = useLocalePath();
const { t } = useI18n();
const route = useRoute();
const router = useRouter();

const status = ref<'loading' | 'expired' | 'already_active' | 'error'>('loading');
const message = ref('');
const resendEmail = ref('');
const resending = ref(false);
const resendSuccess = ref(false);
const resendError = ref('');
const verifyRequestController = ref<AbortController | null>(null);
const isActive = ref(true);

function resolveToken(): string | undefined {
  const rawToken = route.query.token;
  const queryToken = Array.isArray(rawToken) ? rawToken[0] : rawToken;

  if (queryToken) return queryToken;

  if (import.meta.client) {
    const tokenFromSearch = new URLSearchParams(window.location.search).get('token');
    if (tokenFromSearch) return tokenFromSearch;
  }

  return undefined;
}

onMounted(async () => {
  await router.isReady();

  if (!isActive.value) return;

  const token = resolveToken();

  if (!token) {
    status.value = 'error';
    message.value = t('auth.verification.token_invalid');
    return;
  }

  try {
    verifyRequestController.value = new AbortController();
    await $fetch(`${config.public.authUrl}/verify-email`, {
      method: 'GET',
      params: { token },
      signal: verifyRequestController.value.signal,
    });
    if (!isActive.value) return;
    navigateTo({ path: localePath('/auth/login'), query: { verified: 'true' } }, { replace: true });
  } catch (err) {
    const fetchError = err as FetchError;

    if (fetchError.name === 'AbortError') return;
    if (!isActive.value) return;

    const serverMessage = fetchError.data?.message ?? '';

    if (fetchError.statusCode === 410 || serverMessage === 'auth.verification.token_expired') {
      status.value = 'expired';
      message.value = t('auth.verification.token_expired');
    } else if (
      fetchError.statusCode === 409 ||
      serverMessage === 'auth.verification.already_active'
    ) {
      status.value = 'already_active';
      message.value = t('auth.verification.already_active');
    } else {
      status.value = 'error';
      const knownKeys = [
        'auth.verification.token_invalid',
        'auth.verification.token_expired',
        'auth.verification.already_active',
      ];
      const safeKey = knownKeys.includes(serverMessage)
        ? serverMessage
        : 'auth.verification.token_invalid';
      message.value = t(safeKey);
    }
  } finally {
    verifyRequestController.value = null;
  }
});

onBeforeUnmount(() => {
  isActive.value = false;
  verifyRequestController.value?.abort();
});

async function resendVerification() {
  if (!resendEmail.value.trim()) return;

  resending.value = true;
  resendSuccess.value = false;
  resendError.value = '';

  try {
    await $fetch(`${config.public.authUrl}/resend-verification`, {
      method: 'POST',
      body: { email: resendEmail.value.trim() },
    });
    resendSuccess.value = true;
  } catch (err) {
    const fetchError = err as FetchError;
    if (!fetchError.statusCode || fetchError.statusCode >= 500) {
      resendError.value = 'auth.error.network';
      return;
    }

    if (fetchError.statusCode === 400) {
      resendError.value = 'auth.error.format.email';
      return;
    }

    resendSuccess.value = true;
  } finally {
    resending.value = false;
  }
}
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

        <template v-if="!resendSuccess">
          <p class="text-reactive-secondary mt-3">{{ $t('auth.verification.resend_prompt') }}</p>
          <div class="d-flex gap-2 mt-2">
            <input
              v-model="resendEmail"
              type="email"
              class="form-control bg-reactive-primary text-reactive-primary"
              :placeholder="$t('common.email')"
              :disabled="resending"
            />
            <button
              class="btn btn-primary flex-shrink-0"
              :disabled="resending || !resendEmail.trim()"
              @click="resendVerification"
            >
              <span v-if="resending" class="spinner-border spinner-border-sm me-1" role="status" />
              {{ $t('auth.verification.resend_action') }}
            </button>
          </div>
          <div v-if="resendError" class="invalid-feedback d-block mt-2">
            {{ $t(resendError) }}
          </div>
        </template>

        <div v-else class="alert alert-success mt-3 mb-0">
          {{ $t('auth.verification.resend_success') }}
        </div>
      </div>

      <div v-else-if="status === 'already_active'" class="text-center">
        <i class="bi bi-check-circle-fill text-success icon-large" />
        <h3 class="text-reactive-primary">{{ $t('auth.verification.already_active_title') }}</h3>
        <p class="text-reactive-secondary">{{ message }}</p>
        <NuxtLink :to="localePath('/auth/login')" class="btn btn-primary mt-3">
          {{ $t('auth.login.action') }}
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
