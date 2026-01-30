<script setup lang="ts">
import { ref, reactive } from 'vue';
import type { FetchError } from 'ofetch';

definePageMeta({
    layout: 'auth',
});

const config = useRuntimeConfig();
const localePath = useLocalePath();
const loading = ref<boolean>(false);
const isViewingPassword = ref<boolean>(false);

const error = reactive({
    email: '',
    password: '',
    phoneNumber: '',
    fullName: '',
    generic: '',
});

const formData = reactive({
    email: '',
    password: '',
    phoneNumber: '',
    fullName: '',
});

async function register() {
    loading.value = true;
    resetErrors();

    try {
        await $fetch(`${config.public.authUrl}/register`, {
            method: 'POST',
            body: {
                email: formData.email,
                password: formData.password,
                phoneNumber: formData.phoneNumber || null,
                fullName: formData.fullName || null,
            },
        });
        goToLogin();
    } catch (err) {
        handleError(err as FetchError);
    } finally {
        loading.value = false;
    }
}

function resetErrors() {
    Object.assign(error, {
        email: '',
        password: '',
        phoneNumber: '',
        fullName: '',
        generic: '',
    });
}

function handleError(fetchError: FetchError) {
    if (!fetchError.statusCode) {
        error.generic = 'auth.error.network';
        return;
    }
    switch (fetchError.statusCode) {
        case 400:
            error.email = fetchError.data?.email || '';
            error.password = fetchError.data?.password || '';
            error.phoneNumber = fetchError.data?.phoneNumber || '';
            error.fullName = fetchError.data?.fullName || '';
            break;
        case 409:
            error.generic = 'auth.register.error.exists';
            break;
        default:
            error.generic = 'auth.error.unknown';
    }
}

function viewPassword() {
    isViewingPassword.value = true;
    setTimeout(() => {
        isViewingPassword.value = false;
    }, 500);
}

function goToLogin() {
    navigateTo(localePath('/auth/login'));
}
</script>

<template>
    <div class="form-width">
        <h3 class="text-center text-reactive-primary">{{ $t('auth.register.title') }}</h3>
        <hr class="my-2" />
        <form novalidate @submit.prevent="register">
            <div class="mb-2">
                <label for="reg-email" class="form-label text-reactive-primary user-select-none">
                    {{ $t('common.email') }}:<span class="text-danger">*</span>
                </label>
                <input id="reg-email" v-model="formData.email" type="email" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.email },
                ]" />
                <div v-if="error.email" class="invalid-feedback">{{ $t(error.email) }}</div>
            </div>

            <div class="mb-2">
                <label for="reg-password" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.password') }}:<span class="text-danger">*</span>
                </label>
                <div class="input-group">
                    <input id="reg-password" v-model="formData.password" :type="isViewingPassword ? 'text' : 'password'" :class="[
                        'form-control',
                        'bg-reactive-primary',
                        'text-reactive-primary',
                        { 'is-invalid': error.password },
                    ]" />
                    <button class="btn btn-outline-secondary bg-reactive-primary" type="button" :disabled="isViewingPassword" @click="viewPassword">
                        <i :class="isViewingPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
                    </button>
                    <div v-if="error.password" class="invalid-feedback">{{ $t(error.password) }}</div>
                </div>
            </div>

            <div class="mb-2">
                <label for="reg-fullname" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.fullName') }}:
                </label>
                <input id="reg-fullname" v-model="formData.fullName" type="text" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.fullName },
                ]" />
                <div v-if="error.fullName" class="invalid-feedback">{{ $t(error.fullName) }}</div>
            </div>

            <div class="mb-2">
                <label for="reg-phone" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.phone') }}:
                </label>
                <input id="reg-phone" v-model="formData.phoneNumber" type="tel" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.phoneNumber },
                ]" />
                <div v-if="error.phoneNumber" class="invalid-feedback">{{ $t(error.phoneNumber) }}</div>
            </div>

            <div v-if="error.generic" class="invalid-feedback d-block mb-2">{{ $t(error.generic) }}</div>

            <div class="d-flex flex-column">
                <button class="btn btn-primary text-center mb-2" type="submit" :disabled="loading">
                    <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" />
                    {{ $t('auth.register.action') }}
                </button>
                <button type="button" class="btn btn-reactive-gray">
                    <i class="bi bi-google me-2" />
                    <span>{{ $t('auth.register.google') }}</span>
                </button>
            </div>
        </form>
        <hr class="my-2" />
        <div class="text-center form-text">
            <a href="#" class="text-decoration-none text-reactive-secondary" @click.prevent="goToLogin">
                {{ $t('auth.register.hasAccount') }}
            </a>
        </div>
    </div>
</template>
