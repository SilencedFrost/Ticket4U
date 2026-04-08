<script setup lang="ts">
import type { FetchError } from 'ofetch';
import type { ChangeInfo } from '../../../../features/settings/types/changeInfo';
import type { ProfileForm } from '../../../../features/settings/types/profileForm';
import type { FieldErrors } from '../../types/settings';
import { useSettingsApi } from '../../../../features/settings/composables/useSettingsApi';
import AvatarUpload from './AvatarUpload.vue';
import PersonalInfoForm from './PersonalInfoForm.vue';

const { fetchCurrentUser, updateCurrentUser, extractFieldErrors, extractMessage } = useSettingsApi();

const FIELD_ERROR_KEYS: Array<keyof FieldErrors> = [
  'firstName',
  'lastName',
  'birthday',
  'phoneNumber',
];

const loading = ref(false);
const loadingUser = ref(false);
const genericError = ref('');
const successMessage = ref('');
const cachedUser = ref<UserSummary | null>(null);
const fieldErrors = reactive<FieldErrors>({});

const formData = ref<ProfileForm>({
  firstName: '',
  lastName: '',
  email: '',
  birthday: '',
  phoneNumber: '',
});

const initialSnapshot = ref<ProfileForm | null>(null);

function emptyStringToNull(value: string): string | null {
  const trimmed = value.trim();
  return trimmed.length > 0 ? trimmed : null;
}

function createProfileFormFromUser(user: UserSummary): ProfileForm {
  return {
    firstName: user.firstName ?? '',
    lastName: user.lastName ?? '',
    email: user.email,
    birthday: user.birthday ?? '',
    phoneNumber: user.phoneNumber ?? '',
  };
}

function setCachedUser(user: UserSummary) {
  cachedUser.value = user;
}

function syncFormDataFromUser(user: UserSummary) {
  const nextValue = createProfileFormFromUser(user);
  formData.value = nextValue;
  initialSnapshot.value = { ...nextValue };
}

function cacheUserAndSyncForm(user: UserSummary) {
  setCachedUser(user);
  syncFormDataFromUser(user);
}

function resetFieldErrors() {
  for (const key of FIELD_ERROR_KEYS) {
    fieldErrors[key] = undefined;
  }
}

function formatFullName(user: UserSummary): string {
  const parts = [user.lastName, user.firstName]
    .map((part) => part?.trim() ?? '')
    .filter((part) => part.length > 0);

  if (parts.length > 0) {
    return parts.join(' ');
  }

  return user.username || user.email;
}

function beginSaveInfo() {
  loading.value = true;
  successMessage.value = '';
  genericError.value = '';
  resetFieldErrors();
}

function buildChangeInfoPayload(): ChangeInfo {
  return {
    firstName: emptyStringToNull(formData.value.firstName),
    lastName: emptyStringToNull(formData.value.lastName),
    birthday: emptyStringToNull(formData.value.birthday),
    phoneNumber: emptyStringToNull(formData.value.phoneNumber),
  };
}

function applyApiFieldErrors(apiFieldErrors: Record<string, string>): boolean {
  for (const key of FIELD_ERROR_KEYS) {
    fieldErrors[key] = apiFieldErrors[key] ? apiFieldErrors[key] : undefined;
  }
  return FIELD_ERROR_KEYS.some((key) => Boolean(fieldErrors[key]));
}

function handleSaveInfoSuccess(updatedUser: UserSummary) {
  cacheUserAndSyncForm(updatedUser);
  successMessage.value = 'settings.personal_information.messages.update_success';
}

function isNetworkError(fetchError: FetchError): boolean {
  return !fetchError.statusCode;
}

function extractAndApplyFieldErrors(fetchError: FetchError): boolean {
  const apiFieldErrors = extractFieldErrors(fetchError);
  return applyApiFieldErrors(apiFieldErrors);
}

function setGenericErrorFallback(fetchError: FetchError) {
  genericError.value = extractMessage(fetchError) ?? 'auth.error.unknown';
}

function handleSaveInfoError(fetchError: FetchError) {
  if (isNetworkError(fetchError)) {
    genericError.value = 'auth.error.network';
    return;
  }

  const hasFieldError = extractAndApplyFieldErrors(fetchError);

  if (!hasFieldError) {
    setGenericErrorFallback(fetchError);
  }
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
  const user = cachedUser.value;
  if (!user) {
    return '';
  }

  return formatFullName(user);
});

const formattedUsername = computed(() => {
  const value = cachedUser.value?.username?.trim();
  return value ? `@${value}` : '';
});

const createdAtDate = computed(() => {
  return cachedUser.value?.createdAt ?? null;
});

async function loadCurrentUser() {
  loadingUser.value = true;
  genericError.value = '';

  try {
    const user = await fetchCurrentUser();
    cacheUserAndSyncForm(user);
  } catch (err) {
    const fetchError = err as FetchError;
    genericError.value = !fetchError.statusCode
      ? 'auth.error.network'
      : extractMessage(fetchError) ?? 'auth.error.unknown';
  } finally {
    loadingUser.value = false;
  }
}

async function saveInfo() {
  if (!hasChanges.value || loading.value) {
    return;
  }

  beginSaveInfo();
  const payload = buildChangeInfoPayload();

  try {
    const updatedUser = await updateCurrentUser(payload);
    handleSaveInfoSuccess(updatedUser);
  } catch (err) {
    handleSaveInfoError(err as FetchError);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
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
          <avatar-upload :full-name="fullName" :username="formattedUsername" :created-at="createdAtDate" />
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