<script setup lang="ts">
import type { FetchError } from 'ofetch';
import type { ChangeInfo, ProfileForm, UserSummary } from '../../types/settings';
import { useSettingsApi } from '../../composables/useSettingsApi';
import PersonalInfoFields from './PersonalInfoFields.vue';

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

function toI18nKeyOrFallback(message?: string, fallback = 'auth.error.validation'): string {
  if (!message) {
    return fallback;
  }
  return /^[a-z]+(\.[a-z0-9_]+)+$/i.test(message) ? message : fallback;
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
      : toI18nKeyOrFallback(extractMessage(fetchError) ?? undefined, 'auth.error.unknown');
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
      ? toI18nKeyOrFallback(apiFieldErrors.firstName)
      : undefined;
    fieldErrors.lastName = apiFieldErrors.lastName
      ? toI18nKeyOrFallback(apiFieldErrors.lastName)
      : undefined;
    fieldErrors.birthday = apiFieldErrors.birthday
      ? toI18nKeyOrFallback(apiFieldErrors.birthday)
      : undefined;
    fieldErrors.phoneNumber = apiFieldErrors.phoneNumber
      ? toI18nKeyOrFallback(apiFieldErrors.phoneNumber)
      : undefined;

    const hasFieldError = Object.values(fieldErrors).some((message) => !!message);

    if (!hasFieldError) {
      genericError.value = toI18nKeyOrFallback(
        extractMessage(fetchError) ?? undefined,
        'auth.error.unknown',
      );
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
  <form class="personal-info-form" novalidate @submit.prevent="saveInfo">
    <!-- Single Section — always present -->
    <div class="card p-3 shadow-sm">
      <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-person-vcard"></i>
        {{ $t('settings.personal_information.basic_info.section_title') }}
      </h2>

      <div v-if="genericError" class="alert alert-danger py-2 mb-3" role="alert">
        {{ $t(genericError) }}
      </div>

      <div v-if="successMessage" class="alert alert-success py-2 mb-3" role="status">
        {{ $t(successMessage) }}
      </div>

      <personal-info-fields
        v-model="formData"
        :errors="fieldErrors"
        :disabled="loading || loadingUser"
      />

      <!-- Save button inside the card, bottom right -->
      <button
        type="submit"
        class="btn btn-primary ms-auto mt-3 d-inline-flex align-items-center"
        :disabled="loading || loadingUser || !hasChanges"
      >
        <i class="bi bi-floppy-fill text-white"></i>
      </button>
    </div>
  </form>
</template>
