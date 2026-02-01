<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import type { FetchError } from 'ofetch';

definePageMeta({
    layout: 'auth',
});

const config = useRuntimeConfig();
const localePath = useLocalePath();
const loading = ref<boolean>(false);
const isViewingPassword = ref<boolean>(false);
const isViewingConfirmPassword = ref<boolean>(false);
const registerSuccess = ref<boolean>(false);

const error = reactive({
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    fullName: '',
    generic: '',
});

const formData = reactive({
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    fullName: '',
});

// Regex từ backend
const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$/;
const PHONE_REGEX = /^(0\d{9}|[1-9]\d{8})$/;
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const isFormValid = computed(() => {
    return (
        formData.email &&
        formData.password &&
        formData.confirmPassword &&
        formData.phoneNumber &&
        formData.password === formData.confirmPassword &&
        PASSWORD_REGEX.test(formData.password) &&
        EMAIL_REGEX.test(formData.email) &&
        PHONE_REGEX.test(formData.phoneNumber)
    );
});

function validateForm(): boolean {
    resetErrors();
    let valid = true;

    if (!formData.email) {
        error.email = 'auth.error.blank.email';
        valid = false;
    } else if (!EMAIL_REGEX.test(formData.email)) {
        error.email = 'auth.error.format.email';
        valid = false;
    }

    if (!formData.password) {
        error.password = 'auth.error.blank.password';
        valid = false;
    } else if (!PASSWORD_REGEX.test(formData.password)) {
        error.password = 'auth.error.format.password';
        valid = false;
    }

    if (!formData.confirmPassword) {
        error.confirmPassword = 'auth.register.error.confirmRequired';
        valid = false;
    } else if (formData.password !== formData.confirmPassword) {
        error.confirmPassword = 'auth.register.error.passwordMismatch';
        valid = false;
    }

    if (!formData.phoneNumber) {
        error.phoneNumber = 'auth.error.blank.phone';
        valid = false;
    } else if (!PHONE_REGEX.test(formData.phoneNumber)) {
        error.phoneNumber = 'auth.error.format.phone';
        valid = false;
    }

    return valid;
}

async function register() {
    if (!validateForm()) return;

    loading.value = true;

    try {
        const response = await $fetch<{ userId: string | null; message: string }>(
            `${config.public.authUrl}/register`,
            {
                method: 'POST',
                body: {
                    email: formData.email,
                    password: formData.password,
                    phoneNumber: formData.phoneNumber,
                    fullName: formData.fullName || null,
                },
            },
        );

        if (response.userId) {
            registerSuccess.value = true;
            setTimeout(() => goToLogin(), 2000);
        } else {
            error.generic = response.message;
        }
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
        confirmPassword: '',
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

function viewConfirmPassword() {
    isViewingConfirmPassword.value = true;
    setTimeout(() => {
        isViewingConfirmPassword.value = false;
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

        <!-- Success message -->
        <div v-if="registerSuccess" class="alert alert-success text-center">
            <i class="bi bi-check-circle me-2" />
            {{ $t('auth.register.success') }}
        </div>

        <form v-else novalidate @submit.prevent="register">
            <!-- Email -->
            <div class="mb-2">
                <label for="reg-email" class="form-label text-reactive-primary user-select-none">
                    {{ $t('common.email') }}:<span class="text-danger">*</span>
                </label>
                <input id="reg-email" v-model="formData.email" type="email" :disabled="loading" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.email },
                ]" />
                <div v-if="error.email" class="invalid-feedback">{{ $t(error.email) }}</div>
            </div>

            <!-- Password -->
            <div class="mb-2">
                <label for="reg-password" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.password') }}:<span class="text-danger">*</span>
                </label>
                <div class="input-group">
                    <input id="reg-password" v-model="formData.password" :type="isViewingPassword ? 'text' : 'password'" :disabled="loading" :class="[
                        'form-control',
                        'bg-reactive-primary',
                        'text-reactive-primary',
                        { 'is-invalid': error.password },
                    ]" />
                    <button class="btn btn-outline-secondary bg-reactive-primary" type="button" :disabled="isViewingPassword || loading" @click="viewPassword">
                        <i :class="isViewingPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
                    </button>
                    <div v-if="error.password" class="invalid-feedback">{{ $t(error.password) }}</div>
                </div>
                <small class="form-text text-muted">{{ $t('auth.register.passwordHint') }}</small>
            </div>

            <!-- Confirm Password -->
            <div class="mb-2">
                <label for="reg-confirm-password" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.confirmPassword') }}:<span class="text-danger">*</span>
                </label>
                <div class="input-group">
                    <input id="reg-confirm-password" v-model="formData.confirmPassword" :type="isViewingConfirmPassword ? 'text' : 'password'" :disabled="loading" :class="[
                        'form-control',
                        'bg-reactive-primary',
                        'text-reactive-primary',
                        { 'is-invalid': error.confirmPassword },
                    ]" />
                    <button class="btn btn-outline-secondary bg-reactive-primary" type="button" :disabled="isViewingConfirmPassword || loading" @click="viewConfirmPassword">
                        <i :class="isViewingConfirmPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
                    </button>
                    <div v-if="error.confirmPassword" class="invalid-feedback">{{ $t(error.confirmPassword) }}</div>
                </div>
            </div>

            <!-- Full Name -->
            <div class="mb-2">
                <label for="reg-fullname" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.fullName') }}:
                </label>
                <input id="reg-fullname" v-model="formData.fullName" type="text" :disabled="loading" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.fullName },
                ]" />
                <div v-if="error.fullName" class="invalid-feedback">{{ $t(error.fullName) }}</div>
            </div>

            <!-- Phone Number -->
            <div class="mb-2">
                <label for="reg-phone" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.phone') }}:<span class="text-danger">*</span>
                </label>
                <input id="reg-phone" v-model="formData.phoneNumber" type="tel" :disabled="loading" placeholder="0xxxxxxxxx" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.phoneNumber },
                ]" />
                <div v-if="error.phoneNumber" class="invalid-feedback">{{ $t(error.phoneNumber) }}</div>
            </div>

            <div v-if="error.generic" class="invalid-feedback d-block mb-2">{{ $t(error.generic) }}</div>

            <div class="d-flex flex-column">
                <button class="btn btn-primary text-center mb-2" type="submit" :disabled="loading || !isFormValid">
                    <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" />
                    {{ $t('auth.register.action') }}
                </button>
                <button type="button" class="btn btn-reactive-gray" :disabled="loading">
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
