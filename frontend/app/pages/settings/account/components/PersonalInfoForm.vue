<script setup lang="ts">
import type { ProfileForm } from '../../types/settings';
import PersonalInfoFields from './PersonalInfoFields.vue';

interface FieldErrors {
  firstName?: string;
  lastName?: string;
  birthday?: string;
  phoneNumber?: string;
}

const props = withDefaults(
  defineProps<{
    modelValue: ProfileForm;
    errors?: FieldErrors;
    loading?: boolean;
    loadingUser?: boolean;
    genericError?: string;
    successMessage?: string;
    hasChanges?: boolean;
  }>(),
  {
    errors: () => ({}),
    loading: false,
    loadingUser: false,
    genericError: '',
    successMessage: '',
    hasChanges: false,
  },
);

const emit = defineEmits<{
  (e: 'update:modelValue', value: ProfileForm): void;
  (e: 'submit'): void;
}>();

const formData = computed<ProfileForm>({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
});

function handleSubmit() {
  emit('submit');
}
</script>

<template>
  <form class="personal-info-form" novalidate @submit.prevent="handleSubmit">
    <!-- Single Section — always present -->
    <div class="card p-3 shadow-sm">
      <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-person-vcard"></i>
        {{ $t('settings.personal_information.basic_info.section_title') }}
      </h2>

      <div v-if="props.genericError" class="alert alert-danger py-2 mb-3" role="alert">
        {{ $t(props.genericError) }}
      </div>

      <div v-if="props.successMessage" class="alert alert-success py-2 mb-3" role="status">
        {{ $t(props.successMessage) }}
      </div>

      <personal-info-fields
        v-model="formData"
        :errors="props.errors"
        :disabled="props.loading || props.loadingUser"
      />

      <!-- Save button inside the card, bottom right -->
      <button
        type="submit"
        class="btn btn-primary ms-auto mt-3 d-inline-flex align-items-center"
        :disabled="props.loading || props.loadingUser || !props.hasChanges"
      >
        <i class="bi bi-floppy-fill text-white"></i>
      </button>
    </div>
  </form>
</template>
