<script setup lang="ts">
import { ref, reactive } from 'vue';
import type { FetchError } from 'ofetch';

const loading = ref<boolean>(false);
const useUser = useUserStore();
const error = ref<string>();
const isViewingPassword = ref<boolean>(false);
const formData = reactive({
  email: '',
  password: '',
  rememberMe: false,
});

async function login() {
  loading.value = true;
  error.value = '';
  try {
    await useUser.login(formData.email, formData.password, formData.rememberMe);
  } catch (err) {
    const fetchError = err as FetchError;
    console.log(fetchError);
    if (!fetchError.statusCode) {
      error.value = 'auth.error.network';
      return;
    }

    if (fetchError.statusCode === 401) {
      error.value = 'auth.error.unauthorized';
    } else {
      error.value = 'auth.error.unknown';
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
  <div class="card bg-reactive-secondary p-3 form-width">
    <h3 class="text-center text-reactive-primary">{{ $t('auth.login_title') }}:</h3>
    <hr class="my-2" />
    <form action="#">
      <div class="mb-2">
        <label for="email" class="form-label text-reactive-primary"
          >{{ $t('common.email') }}:</label
        >
        <input
          id="email"
          v-model="formData.email"
          type="email"
          class="form-control bg-reactive-primary text-reactive-primary"
        />
      </div>
      <div class="mb-2">
        <label for="password" class="form-label text-reactive-primary"
          >{{ $t('auth.password') }}:</label
        >
        <div class="input-group">
          <input
            id="password"
            v-model="formData.password"
            :type="isViewingPassword ? 'text' : 'password'"
            class="form-control bg-reactive-primary text-reactive-primary"
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
      </div>
      <div class="form-check mb-2">
        <input
          id="rememberMe"
          v-model="formData.rememberMe"
          type="checkbox"
          class="form-check-input"
        />
        <label for="rememberMe" class="form-check-label">{{ $t('auth.remember_me') }}</label>
      </div>
      <div class="d-flex flex-column">
        <button
          class="btn btn-primary text-center mb-2"
          :disabled="loading"
          @click.prevent.stop="login()"
        >
          {{ $t('auth.login_action._') }}
        </button>
        <button class="btn btn-reactive-gray">
          <i class="bi bi-google me-2" />
          <span>{{ $t('auth.login_action.google') }}</span>
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
