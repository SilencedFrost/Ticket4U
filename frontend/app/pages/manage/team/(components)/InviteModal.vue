<script setup lang="ts">
import { ref, watch } from 'vue'
import { ASSIGNABLE_ROLES } from '../../mock.data'

const props = defineProps({
  show: { type: Boolean, required: true },
})

const emit = defineEmits(['close', 'invite'])

const email  = ref('')
const roleId = ref('')
const errors = ref({ email: '', roleId: '' })

watch(() => props.show, function (val) {
  if (!val) return
  email.value  = ''
  roleId.value = ''
  errors.value = { email: '', roleId: '' }
})

function validate() {
  errors.value = { email: '', roleId: '' }
  if (!email.value.trim()) {
    errors.value.email = 'Email is required'
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value.trim())) {
    errors.value.email = 'Enter a valid email address'
  }
  if (!roleId.value) {
    errors.value.roleId = 'Please select a role'
  }
  return !errors.value.email && !errors.value.roleId
}

function submit() {
  if (!validate()) return
  emit('invite', { email: email.value.trim(), roleId: Number(roleId.value) })
  emit('close')
}
</script>

<template>
  <Teleport to="body">
    <div v-if="show" class="modal-backdrop-custom" @click.self="$emit('close')">
      <div class="modal-box card bg-reactive-secondary border-0 p-4 shadow-lg">

        <div class="d-flex align-items-center justify-content-between mb-4">
          <h5 class="fw-bold text-reactive-primary mb-0">
            <i class="bi bi-person-plus me-2 text-primary"/>{{ $t('organizer.team.invite_modal.title') }}
          </h5>
          <button class="btn-close btn-close-white opacity-50" @click="$emit('close')"/>
        </div>

        <!-- Email -->
        <div class="mb-3">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.team.invite_modal.email') }} <span class="text-danger">*</span>
          </label>
          <input
            v-model="email"
            type="email"
            class="form-control bg-reactive-primary border-0 text-reactive-primary"
            :class="{ 'is-invalid': errors.email }"
            :placeholder="$t('organizer.team.invite_modal.email_placeholder')"
          />
          <div v-if="errors.email" class="invalid-feedback">{{ errors.email }}</div>
        </div>

        <!-- Role -->
        <div class="mb-4">
          <label class="form-label small fw-semibold text-reactive-secondary">
            {{ $t('organizer.team.invite_modal.role') }} <span class="text-danger">*</span>
          </label>
          <select
            v-model="roleId"
            class="form-select bg-reactive-primary border-0 text-reactive-primary"
            :class="{ 'is-invalid': errors.roleId }"
          >
            <option value="">{{ $t('organizer.team.invite_modal.select_role') }}</option>
            <option v-for="r in ASSIGNABLE_ROLES" :key="r.id" :value="r.id">
              {{ $t('organizer.team.roles.' + r.key) }}
            </option>
          </select>
          <div v-if="errors.roleId" class="invalid-feedback">{{ errors.roleId }}</div>
        </div>

        <!-- Role hint -->
        <div v-if="roleId" class="alert alert-info py-2 small mb-4">
          <i class="bi bi-info-circle me-1"/>
          {{ $t('organizer.team.roles.' + (ASSIGNABLE_ROLES.find(r => r.id === Number(roleId))?.key ?? '')) }}
          — {{ $t('organizer.team.role_hint.' + (ASSIGNABLE_ROLES.find(r => r.id === Number(roleId))?.key ?? '')) }}
        </div>

        <div class="d-flex justify-content-end gap-2">
          <button class="btn btn-outline-secondary px-4" @click="$emit('close')">
            {{ $t('organizer.team.invite_modal.cancel') }}
          </button>
          <button class="btn btn-primary px-4" @click="submit">
            <i class="bi bi-send me-2"/>{{ $t('organizer.team.invite_modal.submit') }}
          </button>
        </div>

      </div>
    </div>
  </Teleport>
</template>

<style scoped>
.modal-backdrop-custom {
  position: fixed; inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex; align-items: center; justify-content: center;
  z-index: 1050;
}
.modal-box {
  width: 100%; max-width: 480px;
  border-radius: 12px;
}
</style>
