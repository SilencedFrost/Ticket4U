<script setup lang="ts">
import PersonalInfoFields from './PersonalInfoFields.vue';
import SettingsSaveButton from '../../components/SettingsSaveButton.vue';

// <!-- TODO: wire up script -->
</script>

<template>
  <div class="personal-info-form">
    <!-- Single Section — always present -->
    <div class="card p-4 rounded-3 shadow-none border">
      <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
        <i class="bi bi-person-vcard"></i>
        {{ $t('settings.personal_information.basic_info.section_title') }}
      </h2>
      <PersonalInfoFields />

      <!-- Save button inside the card, bottom right -->
      <div class="form-actions d-flex justify-content-end mt-3">
        <!-- TODO: wire up save handler -->
        <SettingsSaveButton />
      </div>
    </div>
  </div>
</template>
