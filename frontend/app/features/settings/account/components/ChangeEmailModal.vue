<script setup lang="ts">
import type { Modal } from 'bootstrap';

withDefaults(
  defineProps<{
    currentEmail: string;
    loading?: boolean;
    errors?: {
      newEmail?: string;
      password?: string;
      generic?: string;
    };
  }>(),
  {
    loading: false,
    errors: () => ({}),
  },
);

const emit = defineEmits<{
  (e: 'submit', payload: { newEmail: string; password: string }): void;
  (e: 'close'): void;
}>();


interface WindowWithBootstrap extends Window {
  bootstrap?: {
    Modal?: new (element: HTMLElement) => Modal;
  };
}

const modalRef = ref<HTMLElement | null>(null);
let modalInstance: Modal | null = null;

const newEmail = ref('');
const currentPassword = ref('');

type ModalConstructor = new (element: HTMLElement) => Modal;

async function resolveModalConstructor(): Promise<ModalConstructor | null> {
  if (typeof window === 'undefined') {
    return null;
  }

  const windowWithBootstrap = window as WindowWithBootstrap;
  if (windowWithBootstrap.bootstrap?.Modal) {
    return windowWithBootstrap.bootstrap.Modal;
  }

  try {
    const modalModule = await import('bootstrap/js/dist/modal');
    return modalModule.default as ModalConstructor;
  }
  catch {
    return null;
  }
}

function open() {
  newEmail.value = '';
  currentPassword.value = '';

  void openModal();
}

async function openModal() {
  const element = modalRef.value;
  if (!element) {
    return;
  }

  if (!modalInstance) {
    const ModalConstructor = await resolveModalConstructor();
    if (!ModalConstructor) {
      return;
    }
    modalInstance = new ModalConstructor(element);
  }

  modalInstance.show();
}

function close() {
  modalInstance?.hide();
  emit('close');
}

function submit() {
  emit('submit', {
    newEmail: newEmail.value.trim(),
    password: currentPassword.value,
  });
}

defineExpose({ open, close });
</script>

<template>
  <div ref="modalRef" class="modal fade" tabindex="-1" aria-labelledby="changeEmailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">

        <div class="modal-header">
          <h5 id="changeEmailModalLabel" class="modal-title">
            {{ $t('settings.personal_information.change_email.title') }}
          </h5>
          <button type="button" class="btn-close" aria-label="Close" @click="close" />
        </div>

        <div class="modal-body">
          <div v-if="errors?.generic" class="alert alert-danger py-2 mb-3" role="alert">
            {{ $t(errors.generic) }}
          </div>

          <div class="mb-3">
            <label class="form-label">{{ $t('settings.personal_information.change_email.current_email') }}</label>
            <input type="email" class="form-control" :value="currentEmail" readonly />
          </div>

          <div class="mb-3">
            <label for="new-email" class="form-label">
              {{ $t('settings.personal_information.change_email.new_email') }}
            </label>
            <input
              id="new-email"
              v-model="newEmail"
              type="email"
              class="form-control"
              :class="{ 'is-invalid': !!errors?.newEmail }"
              :disabled="loading"
              autocomplete="email"
            />
            <div v-if="errors?.newEmail" class="invalid-feedback d-block">
              {{ $t(errors.newEmail) }}
            </div>
          </div>

          <div class="mb-1">
            <label for="confirm-password" class="form-label">
              {{ $t('settings.personal_information.change_email.current_password') }}
            </label>
            <input
              id="confirm-password"
              v-model="currentPassword"
              type="password"
              class="form-control"
              :class="{ 'is-invalid': !!errors?.password }"
              :disabled="loading"
              autocomplete="current-password"
            />
            <div v-if="errors?.password" class="invalid-feedback d-block">
              {{ $t(errors.password) }}
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" :disabled="loading" @click="close">
            {{ $t('common.action.cancel') }}
          </button>
          <button
            type="button"
            class="btn btn-primary d-inline-flex align-items-center gap-2"
            :disabled="loading || !newEmail || !currentPassword"
            @click="submit"
          >
            <span v-if="loading" class="spinner-border spinner-border-sm text-white" role="status" />
            <i class="bi bi-floppy-fill text-white"></i>
          </button>
        </div>

      </div>
    </div>
  </div>
</template>