<script setup lang="ts">
import PersonalInfoFields from './PersonalInfoFields.vue'

// <!-- TODO: wire up script -->
</script>

<template>
  <div class="personal-info-form">
    <!-- Single Section — always present -->
    <div class="card p-3 rounded-4 shadow-sm border-0 bg-reactive-secondary">
      <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-person-vcard"></i> {{ $t('settings.personal_information.basic_info.section_title') }}
      </h2>
      <PersonalInfoFields />

      <!-- Save button inside the card, bottom right -->
      <div class="form-actions d-flex justify-content-end mt-4">
        <!-- TODO: wire up save handler -->
        <button class="btn btn-primary fw-bold px-4 py-2 rounded-3 shadow-sm d-flex align-items-center gap-2 text-white">
          <i class="bi bi-floppy2-fill"></i> {{ $t('settings.personal_information.actions.save') }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Scoped styles reserved for inner layout components */
</style>
