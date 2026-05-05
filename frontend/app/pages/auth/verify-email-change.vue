<script setup lang="ts">
import type { FetchError } from 'ofetch';

// Layout auth (cùng pattern với verify-email.vue)
definePageMeta({ layout: 'auth' });

const config = useRuntimeConfig();
const route = useRoute();
const { t } = useI18n();

type Status = 'loading' | 'success' | 'error';

const status = ref<Status>('loading');
const errorMessageKey = ref('auth.error.token.invalid');

const TOKEN_ERROR_KEYS = new Set([
  'auth.error.token.invalid',
  'auth.error.token.expired',
]);

const VERIFIED_TOKEN_SESSION_PREFIX = 'verify-email-change:';

useHead({ title: t('auth.verify_email_change.page_title') });

function getVerifiedTokenKey(token: string): string {
  return `${VERIFIED_TOKEN_SESSION_PREFIX}${token}`;
}

function wasVerifiedInSession(token: string): boolean {
  if (!import.meta.client) return false;
  return sessionStorage.getItem(getVerifiedTokenKey(token)) === 'success';
}

function markVerifiedInSession(token: string): void {
  if (!import.meta.client) return;
  sessionStorage.setItem(getVerifiedTokenKey(token), 'success');
}

function resolveTokenErrorKey(err: unknown): string {
  const fetchError = err as FetchError<{ message?: unknown }>;
  const serverMessage = fetchError.data?.message;

  if (typeof serverMessage === 'string' && TOKEN_ERROR_KEYS.has(serverMessage)) {
    return serverMessage;
  }

  if (fetchError.statusCode === 410) {
    return 'auth.error.token.expired';
  }

  return 'auth.error.token.invalid';
}

async function verifyEmailChange(token: string) {
  try {
    await $fetch(`${config.public.userServiceUrl}/auth/verify-email-change`, {
      method: 'GET',
      credentials: 'include',
      params: { token },
    });
    markVerifiedInSession(token);
    status.value = 'success';
  } catch (err: unknown) {
    if (wasVerifiedInSession(token)) {
      status.value = 'success';
      return;
    }

    status.value = 'error';
    errorMessageKey.value = resolveTokenErrorKey(err);
  }
}

onMounted(async () => {
  const token = route.query.token;
  if (typeof token !== 'string' || !token.trim()) {
    status.value = 'error';
    errorMessageKey.value = 'auth.error.token.invalid';
    return;
  }

  if (wasVerifiedInSession(token)) {
    status.value = 'success';
    return;
  }

  await verifyEmailChange(token);
});
</script>

<template>
  <div class="d-flex flex-column align-items-center justify-content-center min-vh-100 p-3">
    <div class="card shadow-sm p-4 text-center" style="max-width: 420px; width: 100%">

      <!-- Loading -->
      <div v-if="status === 'loading'" class="py-4">
        <div class="spinner-border text-primary mb-3" role="status"></div>
        <p class="text-reactive-secondary mb-0">
          {{ $t('auth.verify_email_change.verifying') }}
        </p>
      </div>

      <!-- Success -->
      <div v-else-if="status === 'success'" class="py-2">
        <i class="bi bi-check-circle-fill text-success mb-3 d-block" style="font-size: 3rem"></i>
        <h1 class="h5 fw-bold mb-2">
          {{ $t('auth.verify_email_change.success_title') }}
        </h1>
        <p class="text-reactive-secondary mb-4">
          {{ $t('auth.verify_email_change.success_description') }}
        </p>
        <!--
          Redirect về settings/account để user thấy email mới.
          useUserStore().refresh() nếu bạn có global user store —
          hoặc để trang account tự gọi fetchCurrentUser() khi mount.
        -->
        <NuxtLink to="/settings/account" class="btn btn-primary w-100">
          {{ $t('auth.verify_email_change.go_to_settings') }}
        </NuxtLink>
      </div>

      <!-- Error -->
      <div v-else class="py-2">
        <i class="bi bi-x-circle-fill text-danger mb-3 d-block" style="font-size: 3rem"></i>
        <h1 class="h5 fw-bold mb-2">
          {{ $t('auth.verify_email_change.error_title') }}
        </h1>
        <p class="text-reactive-secondary mb-4">
          {{ $t(errorMessageKey) }}
        </p>
        <NuxtLink to="/settings/security" class="btn btn-outline-primary w-100">
          {{ $t('auth.verify_email_change.try_again') }}
        </NuxtLink>
      </div>

    </div>
  </div>
</template>
