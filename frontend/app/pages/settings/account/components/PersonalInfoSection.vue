<script setup lang="ts">
import type { FetchError } from 'ofetch';
import type { ChangeInfo, ProfileForm, UserSummary } from '../../types/settings';
import { useSettingsApi } from '../../composables/useSettingsApi';
import AvatarUpload from './AvatarUpload.vue';
import PersonalInfoForm from './PersonalInfoForm.vue';

const { currentUser, fetchCurrentUser, updateCurrentUser, extractFieldErrors, extractMessage } =
  useSettingsApi();

const loading = ref(false);
const loadingUser = ref(false);
const genericError = ref('');
const successMessage = ref('');

const fieldErrors = reactive<{
  firstName?: string;
  lastName?: string;
  birthday?: string;
  phoneNumber?: string;
}>({});

const formData = ref<ProfileForm>({
  firstName: '',
  lastName: '',
  email: '',
  birthday: '',
  phoneNumber: '',
});

const initialSnapshot = ref<ProfileForm | null>(null);

function normalizeToNullable(value: string): string | null {
  const trimmed = value.trim();
  return trimmed.length > 0 ? trimmed : null;
}

function toSafeI18nKey(message: string | undefined, fallback: string): string {
  if (!message) {
    return fallback;
  }
  return /^[a-z]+(\.[a-z0-9_]+)+$/i.test(message) ? message : fallback;
}

function toValidationErrorI18nKey(message?: string): string {
  return toSafeI18nKey(message, 'auth.error.validation');
}

function toGenericErrorI18nKey(message?: string): string {
  return toSafeI18nKey(message, 'auth.error.unknown');
}

function resetFieldErrors() {
  fieldErrors.firstName = undefined;
  fieldErrors.lastName = undefined;
  fieldErrors.birthday = undefined;
  fieldErrors.phoneNumber = undefined;
}

function applyUserToForm(user: UserSummary) {
  const nextValue: ProfileForm = {
    firstName: user.firstName ?? '',
    lastName: user.lastName ?? '',
    email: user.email,
    birthday: user.birthday ?? '',
    phoneNumber: user.phoneNumber ?? '',
  };

  formData.value = nextValue;
  initialSnapshot.value = { ...nextValue };
}

const hasChanges = computed(() => {
  if (!initialSnapshot.value) {
    return false;
  }

  const current = formData.value;
  const initial = initialSnapshot.value;

  return (
    current.firstName !== initial.firstName ||
    current.lastName !== initial.lastName ||
    current.birthday !== initial.birthday ||
    current.phoneNumber !== initial.phoneNumber
  );
});

const fullName = computed(() => {
  const user = currentUser.value;
  if (!user) {
    return '';
  }

  const parts = [user.lastName, user.firstName]
    .map((part) => part?.trim() ?? '')
    .filter((part) => part.length > 0);

  if (parts.length > 0) {
    return parts.join(' ');
  }

  return user.username || user.email;
});

const username = computed(() => {
  const value = currentUser.value?.username?.trim();
  return value ? `@${value}` : '';
});

const createdAtDate = computed(() => {
  const value = currentUser.value?.createdAt;
  return value ? new Date(value) : null;
});

async function loadCurrentUser(force = false) {
  loadingUser.value = true;
  genericError.value = '';

  try {
    const user = await fetchCurrentUser(force);
    applyUserToForm(user);
  } catch (err) {
    const fetchError = err as FetchError;
    genericError.value = !fetchError.statusCode
      ? 'auth.error.network'
      : toGenericErrorI18nKey(extractMessage(fetchError) ?? undefined);
  } finally {
    loadingUser.value = false;
  }
}

async function saveInfo() {
  if (!hasChanges.value || loading.value) {
    return;
  }

  loading.value = true;
  successMessage.value = '';
  genericError.value = '';
  resetFieldErrors();

  const payload: ChangeInfo = {
    firstName: normalizeToNullable(formData.value.firstName),
    lastName: normalizeToNullable(formData.value.lastName),
    birthday: normalizeToNullable(formData.value.birthday),
    phoneNumber: normalizeToNullable(formData.value.phoneNumber),
  };

  try {
    const updatedUser = await updateCurrentUser(payload);
    applyUserToForm(updatedUser);
    successMessage.value = 'settings.personal_information.messages.update_success';
  } catch (err) {
    const fetchError = err as FetchError;

    if (!fetchError.statusCode) {
      genericError.value = 'auth.error.network';
      return;
    }

    const apiFieldErrors = extractFieldErrors(fetchError);

    fieldErrors.firstName = apiFieldErrors.firstName
      ? toValidationErrorI18nKey(apiFieldErrors.firstName)
      : undefined;
    fieldErrors.lastName = apiFieldErrors.lastName
      ? toValidationErrorI18nKey(apiFieldErrors.lastName)
      : undefined;
    fieldErrors.birthday = apiFieldErrors.birthday
      ? toValidationErrorI18nKey(apiFieldErrors.birthday)
      : undefined;
    fieldErrors.phoneNumber = apiFieldErrors.phoneNumber
      ? toValidationErrorI18nKey(apiFieldErrors.phoneNumber)
      : undefined;

    const hasFieldError = Object.values(fieldErrors).some((message) => !!message);

    if (!hasFieldError) {
      genericError.value = toGenericErrorI18nKey(extractMessage(fetchError) ?? undefined);
    }
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  if (currentUser.value) {
    applyUserToForm(currentUser.value);
    return;
  }

  void loadCurrentUser();
});
</script>

<template>
  <div>
    <div class="page-header card shadow-sm p-3 mb-2">
      <h1 class="h3 fw-bold text-reactive-primary mb-1">
        {{ $t('settings.personal_information.title') }}
      </h1>
      <p class="text-reactive-secondary mb-0">
        {{ $t('settings.personal_information.subtitle') }}
      </p>
    </div>

    <div class="row g-2 overflow-visible">
      <div class="col-12 col-lg-auto">
        <div class="personal-info-avatar-col card p-3 shadow-sm d-flex flex-column h-100">
          <avatar-upload :full-name="fullName" :username="username" :created-at="createdAtDate" />
        </div>
      </div>

      <div class="col-12 personal-info-form-col">
        <personal-info-form
          v-model="formData"
          :errors="fieldErrors"
          :loading="loading"
          :loading-user="loadingUser"
          :generic-error="genericError"
          :success-message="successMessage"
          :has-changes="hasChanges"
          @submit="saveInfo"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.personal-info-avatar-col {
  width: 100%;
}

@media (min-width: 992px) {
  .personal-info-avatar-col {
    width: 240px;
  }
}

.personal-info-form-col {
  flex: 1;
  min-width: 0;
}
</style>