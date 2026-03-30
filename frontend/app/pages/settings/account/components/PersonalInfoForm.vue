<script setup lang="ts">
import PersonalInfoFields from './PersonalInfoFields.vue';

function saveInfo() {
  // placeholder function
}
// <!-- TODO: wire up script -->
</script>

<template>
  <div class="personal-info-form">
    <!-- Single Section — always present -->
    <div class="card p-3 shadow-sm">
      <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-person-vcard"></i>
        {{ $t('settings.personal_information.basic_info.section_title') }}
      </h2>
      <personal-info-fields />

      <!-- Save button inside the card, bottom right -->
      <button class="btn btn-primary ms-auto mt-3">
        <i class="bi bi-floppy-fill text-white" @click="saveInfo()"></i>
      </button>
    </div>
  </div>
</template>
