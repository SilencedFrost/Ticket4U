<script setup lang="ts">
import type { OrganizerSummary } from '@/features/auth/types/Organizer';
defineProps<{
  organizerData: OrganizerSummary;
}>();
</script>

<template>
  <section
    id="organizer-section"
    class="card bg-reactive-secondary overflow-hidden m-3 mx-auto mw-100"
  >
    <div class="bg-reactive-gray w-100 p-3 d-flex justify-content-between align-items-center">
      <h5 class="text-reactive-primary fw-bold mb-0">
        {{ $t('event_detail.label.organizer') }}
      </h5>
    </div>
    <div class="p-3 d-flex flex-column flex-md-row align-items-center align-items-md-start gap-4">
      <div class="flex-shrink-0 text-center">
        <img
          v-img-fallback="[400, 400]"
          :src="organizerData.logo_url"
          alt="Organizer"
          class="img-fluid w-75 rounded-3 border shadow-sm"
        />
      </div>

      <div class="flex-grow-1 text-center text-md-start">
        <h4 class="text-reactive-primary fw-bold mb-2 fs-5">
          {{ organizerData.name }}
        </h4>
        <p class="text-reactive-primary lh-base small mb-0">
          {{ organizerData.description }}
        </p>
      </div>
    </div>
  </section>
</template>
