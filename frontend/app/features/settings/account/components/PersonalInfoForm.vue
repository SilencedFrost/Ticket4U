<script setup lang="ts">
import type { FieldErrors } from '../../types/fieldErrors';
import type { ProfileForm } from '../../types/profileForm';
import PersonalInfoFields from './PersonalInfoFields.vue';

withDefaults(
  defineProps<{
    errors?: FieldErrors;
    loading?: boolean;
    loadingUser?: boolean;
    genericError?: string;
    successMessage?: string;
    hasChanges?: boolean;
    emailLoading?: boolean;
    emailErrors?: {
      newEmail?: string;
      password?: string;
      generic?: string;
    };
  }>(),
  {
    errors: () => ({}),
    loading: false,
    loadingUser: false,
    genericError: '',
    successMessage: '',
    hasChanges: false,
    emailLoading: false,
    emailErrors: () => ({}),
  },
);

const model = defineModel<ProfileForm>({ required: true });

const emit = defineEmits<{
  (e: 'submit'): void;
  (e: 'change-email', payload: { newEmail: string; password: string }): void;
}>();

const personalInfoFieldsRef = ref<InstanceType<typeof PersonalInfoFields> | null>(null);

function handleSubmit() {
  emit('submit');
}

function handleChangeEmail(payload: { newEmail: string; password: string }) {
  emit('change-email', payload);
}

function closeEmailModal() {
  personalInfoFieldsRef.value?.closeEmailModal();
}

defineExpose({ closeEmailModal });

</script>

<template>
  <form class="personal-info-form" novalidate @submit.prevent="handleSubmit">
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
        ref="personalInfoFieldsRef"
        v-model="model"
        :errors="errors"
        :disabled="loading || loadingUser"
        :email-loading="emailLoading"
        :email-errors="emailErrors"
        @change-email="handleChangeEmail"
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
