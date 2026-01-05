<script setup lang="ts">
import type { Organizer } from '@/types/event-detail';
defineProps<{
  eventData: Organizer;
  organizerImage: string;
}>();
</script>

<template>
  <section id="organizer-section" class="p-3 p-md-4 card bg-reactive-secondary m-3 mx-auto mw-100">
    <div class="container-xxl organizer-card">
      <h3 class="text-reactive-primary fw-bold mb-4 h4">{{ $t('event-detail.organizer') }}</h3>
      <div class="row g-4 align-items-start">
        <div class="col-md-4">
          <img
            :src="organizerImage"
            alt="Organizer"
            class="img-fluid rounded-2 ratio ratio-1x1 object-fit-cover"
          />
        </div>
        <div class="col-md-8">
          <h4 class="text-reactive-primary fw-bold mb-3 fs-5">{{ eventData.name }}</h4>
          <p class="text-reactive-primary-50 lh-lg small">
            {{ eventData.description }}
          </p>
        </div>
      </div>
    </div>
  </section>
</template>
