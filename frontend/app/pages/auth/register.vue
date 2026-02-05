<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue';
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
    password: [] as string[],
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

// Regex validate patterns
const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*_-]).{8,32}$/;
const PHONE_REGEX = /^(0)?(3|5|7|8|9)\d{8}$/;
const EMAIL_REGEX = /^[a-zA-Z0-9._-]+@(gmail\.com|outlook\.com|hotmail\.com|live\.com|yahoo\.com|icloud\.com|me\.com)$/;

// Track touched fields for showing errors
const touched = reactive({
    email: false,
    password: false,
    confirmPassword: false,
    phoneNumber: false,
    fullName: false,
});

const isFormValid = computed(() => {
    return (
        formData.fullName &&
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

// Mark field as touched on blur and trigger validation
function onBlur(field: keyof typeof touched) {
    touched[field] = true;
    // Trigger validation immediately on blur
    if (field === 'email') {
        const val = formData.email;
        if (!val) {
            error.email = '';
        } else if (!EMAIL_REGEX.test(val)) {
            error.email = 'auth.error.format.email';
        } else {
            error.email = '';
        }
    } else if (field === 'phoneNumber') {
        const val = formData.phoneNumber;
        if (!val) {
            error.phoneNumber = '';
        } else if (!PHONE_REGEX.test(val)) {
            error.phoneNumber = 'auth.error.format.phone';
        } else {
            error.phoneNumber = '';
        }
    } else if (field === 'password') {
        const val = formData.password;
        error.password = [];
        if (!val) {
            return;
        }
        const errors: string[] = [];
        if (!/^[\x20-\x7E]*$/.test(val)) errors.push('auth.error.password.noVietnamese');
        if (val.length < 8) errors.push('auth.error.password.tooShort');
        if (val.length > 32) errors.push('auth.error.password.tooLong');
        if (!/[a-z]/.test(val)) errors.push('auth.error.password.noLowercase');
        if (!/[A-Z]/.test(val)) errors.push('auth.error.password.noUppercase');
        if (!/[!@#$%^&*_-]/.test(val)) errors.push('auth.error.password.noSpecialChar');
        error.password = errors;
    } else if (field === 'confirmPassword') {
        const val = formData.confirmPassword;
        if (!val) {
            error.confirmPassword = 'auth.register.error.confirmRequired';
        } else if (formData.password !== val) {
            error.confirmPassword = 'auth.register.error.passwordMismatch';
        } else {
            error.confirmPassword = '';
        }
    } else if (field === 'fullName') {
        const val = formData.fullName;
        if (!val) {
            error.fullName = '';
        } else {
            error.fullName = '';
        }
    }
}

// Realtime watchers for validation
watch(() => formData.email, (val) => {
    if (!touched.email) return;
    if (!val) {
        error.email = '';
    } else if (!EMAIL_REGEX.test(val)) {
        error.email = 'auth.error.format.email';
    } else {
        error.email = '';
    }
});

watch(() => formData.phoneNumber, (val) => {
    if (!touched.phoneNumber) return;
    if (!val) {
        error.phoneNumber = '';
    } else if (!PHONE_REGEX.test(val)) {
        error.phoneNumber = 'auth.error.format.phone';
    } else {
        error.phoneNumber = '';
    }
});

watch(() => formData.password, (val) => {
    if (!touched.password) return;
    error.password = [];
    if (!val) {
        return;
    }
    const errors: string[] = [];
    if (!/^[\x20-\x7E]*$/.test(val)) errors.push('auth.error.password.noVietnamese');
    if (val.length < 8) errors.push('auth.error.password.tooShort');
    if (val.length > 32) errors.push('auth.error.password.tooLong');
    if (!/[a-z]/.test(val)) errors.push('auth.error.password.noLowercase');
    if (!/[A-Z]/.test(val)) errors.push('auth.error.password.noUppercase');
    if (!/[!@#$%^&*_-]/.test(val)) errors.push('auth.error.password.noSpecialChar');
    error.password = errors;
    // Re-validate confirm password if already touched
    if (touched.confirmPassword && formData.confirmPassword) {
        error.confirmPassword = val !== formData.confirmPassword ? 'auth.register.error.passwordMismatch' : '';
    }
});

watch(() => formData.confirmPassword, (val) => {
    if (!touched.confirmPassword) return;
    if (!val) {
        error.confirmPassword = 'auth.register.error.confirmRequired';
    } else if (formData.password !== val) {
        error.confirmPassword = 'auth.register.error.passwordMismatch';
    } else {
        error.confirmPassword = '';
    }
});

watch(() => formData.fullName, (val) => {
    if (!touched.fullName) return;
    if (!val) {
        error.fullName = '';
    } else {
        error.fullName = '';
    }
});

function validateForm(): boolean {
    resetErrors();
    let valid = true;

    if (!formData.fullName) {
        valid = false;
    }

    if (!formData.email) {
        valid = false;
    } else if (!EMAIL_REGEX.test(formData.email)) {
        error.email = 'auth.error.format.email';
        valid = false;
    }

    if (!formData.password) {
        valid = false;
    } else if (!PASSWORD_REGEX.test(formData.password)) {
        const errors: string[] = [];
        if (!/^[\x20-\x7E]*$/.test(formData.password)) errors.push('auth.error.password.noVietnamese');
        if (formData.password.length < 8) errors.push('auth.error.password.tooShort');
        if (formData.password.length > 32) errors.push('auth.error.password.tooLong');
        if (!/[a-z]/.test(formData.password)) errors.push('auth.error.password.noLowercase');
        if (!/[A-Z]/.test(formData.password)) errors.push('auth.error.password.noUppercase');
        if (!/[!@#$%^&*_-]/.test(formData.password)) errors.push('auth.error.password.noSpecialChar');
        error.password = errors;
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
            Object.assign(formData, {
                email: '',
                password: '',
                confirmPassword: '',
                phoneNumber: '',
                fullName: '',
            });
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
        password: [],
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
        case 400: {
            const errorMessage = String(fetchError.data?.message ?? fetchError.data?.error ?? '');

            if (errorMessage.toLowerCase().includes('email') &&
                (errorMessage.toLowerCase().includes('exists') ||
                    errorMessage.toLowerCase().includes('already') ||
                    errorMessage.toLowerCase().includes('taken'))) {
                error.email = 'auth.error.registrationFailed';
            } else {
                error.email = fetchError.data?.email || '';
                error.password = fetchError.data?.password ? [fetchError.data.password] : [];
                error.phoneNumber = fetchError.data?.phoneNumber || '';
                error.fullName = fetchError.data?.fullName || '';

                if (!error.email && error.password.length === 0 && !error.phoneNumber && !error.fullName) {
                    error.generic = errorMessage || 'auth.error.validation';
                }
            }
            break;
        }
        default:
            error.generic = String(fetchError.data?.message || 'auth.error.unknown');
    }
}

function viewPassword() {
    isViewingPassword.value = !isViewingPassword.value;
}

function viewConfirmPassword() {
    isViewingConfirmPassword.value = !isViewingConfirmPassword.value;
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
            <!-- Full Name -->
            <div class="mb-2">
                <label for="reg-fullname" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.fullName') }}<span class="text-danger">*</span>
                </label>
                <input id="reg-fullname" v-model="formData.fullName" type="text" :disabled="loading" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.fullName },
                ]" @blur="onBlur('fullName')" />
                <div v-if="error.fullName" class="invalid-feedback">{{ $t(error.fullName) }}</div>
            </div>

            <!-- Email -->
            <div class="mb-2">
                <label for="reg-email" class="form-label text-reactive-primary user-select-none">
                    {{ $t('common.email') }}<span class="text-danger">*</span>
                </label>
                <input id="reg-email" v-model="formData.email" type="email" :disabled="loading" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.email },
                ]" @blur="onBlur('email')" />
                <div v-if="error.email" class="invalid-feedback">{{ $t(error.email) }}</div>
            </div>

            <!-- Phone Number -->
            <div class="mb-2">
                <label for="reg-phone" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.phone') }}<span class="text-danger">*</span>
                </label>
                <input id="reg-phone" v-model="formData.phoneNumber" type="tel" :disabled="loading" placeholder="0xxxxxxxxx hoặc xxxxxxxxx" :class="[
                    'form-control',
                    'bg-reactive-primary',
                    'text-reactive-primary',
                    { 'is-invalid': error.phoneNumber },
                ]" @blur="onBlur('phoneNumber')" />
                <div v-if="error.phoneNumber" class="invalid-feedback">{{ $t(error.phoneNumber) }}</div>
            </div>

            <!-- Password -->
            <div class="mb-2">
                <label for="reg-password" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.password') }}<span class="text-danger">*</span>
                </label>
                <div class="input-group">
                    <input id="reg-password" v-model="formData.password" :type="isViewingPassword ? 'text' : 'password'" :disabled="loading" :class="[
                        'form-control',
                        'bg-reactive-primary',
                        'text-reactive-primary',
                        { 'is-invalid': error.password.length > 0 },
                    ]" @blur="onBlur('password')" />
                    <button class="btn btn-outline-secondary bg-reactive-primary" type="button" :disabled="loading" @click="viewPassword">
                        <i :class="isViewingPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
                    </button>
                </div>
                <ul v-if="error.password.length > 0" class="text-danger small mt-1 mb-0 ps-3">
                    <li v-for="(err, index) in error.password" :key="index">{{ $t(err) }}</li>
                </ul>
                <small class="form-text text-muted">{{ $t('auth.register.passwordHint') }}</small>
            </div>

            <!-- Confirm Password -->
            <div class="mb-2">
                <label for="reg-confirm-password" class="form-label text-reactive-primary user-select-none">
                    {{ $t('auth.register.confirmPassword') }}<span class="text-danger">*</span>
                </label>
                <div class="input-group">
                    <input id="reg-confirm-password" v-model="formData.confirmPassword" :type="isViewingConfirmPassword ? 'text' : 'password'" :disabled="loading" :class="[
                        'form-control',
                        'bg-reactive-primary',
                        'text-reactive-primary',
                        { 'is-invalid': error.confirmPassword },
                    ]" @blur="onBlur('confirmPassword')" />
                    <button class="btn btn-outline-secondary bg-reactive-primary" type="button" :disabled="loading" @click="viewConfirmPassword">
                        <i :class="isViewingConfirmPassword ? 'bi bi-eye-slash-fill' : 'bi bi-eye-fill'" />
                    </button>
                </div>
                <div v-if="error.confirmPassword" class="invalid-feedback d-block">{{ $t(error.confirmPassword) }}</div>
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
