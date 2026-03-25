<template>
  <div class="personal-info-form">
    <!-- Single Section — always present -->
    <div class="card p-3 rounded-4 shadow-sm border-0 bg-reactive-secondary">
      <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-person-vcard"></i> Thông tin cơ bản
      </h2>
      <PersonalInfoFields />

      <!-- Save button inside the card, bottom right -->
      <div class="form-actions d-flex justify-content-end mt-4">
        <!-- TODO: wire up save handler -->
        <button class="btn btn-primary fw-bold px-4 py-2 rounded-3 shadow-sm d-flex align-items-center gap-2 text-white">
          <i class="bi bi-floppy2-fill"></i> Lưu thay đổi
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import PersonalInfoFields from './PersonalInfoFields.vue'

// <!-- TODO: wire up script -->
</script>

<style scoped>
/* Ensure child components match input-underline behavior */
:deep(.input-underline) {
  border-bottom: 2px solid transparent !important;
}
:deep(.input-underline:focus) {
  box-shadow: none;
  border-bottom-color: var(--bs-primary) !important;
}
</style>
