<script setup lang="ts">
import { ref, reactive } from 'vue';
import type { FetchError } from 'ofetch';

const localePath = useLocalePath();
const router = useRouter();
const loading = ref<boolean>(false);
const useUser = useUserStore();
const error = reactive({ email: '', password: '', generic: '' });
const isViewingPassword = ref<boolean>(false);
const formData = reactive({
  email: '',
  password: '',
  rememberMe: false,
});

async function login() {
  loading.value = true;
  Object.assign(error, { email: '', password: '', generic: '' });
  try {
    await useUser.login(formData.email, formData.password, formData.rememberMe);

    router.push(localePath('/'));
  } catch (err) {
    const fetchError = err as FetchError;
    console.log(fetchError);
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
      default:
        error.generic = 'auth.error.unknown';
        break;
    }
  } finally {
    loading.value = false;
  }
}

function viewPassword() {
  isViewingPassword.value = true;

  setTimeout(() => {
    isViewingPassword.value = false;
  }, 500);
}
</script>

<template>
  <div class="form-width">
    <h3 class="text-center text-reactive-primary">{{ $t('auth.login.title') }}:</h3>
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
            :disabled="isViewingPassword"
            @click="viewPassword"
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
        <button class="btn btn-reactive-gray">
          <i class="bi bi-google me-2" />
          <span>{{ $t('auth.login.google') }}</span>
        </button>
      </div>
    </form>
    <hr class="my-2" />
    <div class="text-center form-text">
      <a href="" class="text-decoration-none text-reactive-secondary">{{
        $t('auth.create_account')
      }}</a>
      |
      <a href="" class="text-decoration-none text-reactive-secondary">{{
        $t('auth.forgot_password')
      }}</a>
    </div>
  </div>
</template>
