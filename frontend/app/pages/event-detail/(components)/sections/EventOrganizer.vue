<script setup lang="ts">
import type { Organizer } from '@/pages/event-detail/types/event-detail';
const { vFallback: vImgFallback } = useImagePlaceholder();
defineProps<{
  eventData: Organizer;
}>();
</script>

<template>
  <section id="organizer-section" class="p-3 p-md-4 card bg-reactive-secondary m-3 mx-auto mw-100">
    <div class="container-xxl organizer-card">
      <h3 class="text-reactive-primary fw-bold mb-4 h4">
        {{ $t('event_detail.label.organizer') }}
      </h3>
      <div class="row g-4 align-items-start">
        <div class="col-6 col-md-4">
          <img
            v-img-fallback="[400, 400]"
            :src="eventData.logo_url"
            alt="Organizer"
            class="img-fluid rounded-2 ratio ratio-1x1 object-fit-cover"
          />
        </div>
        <div class="col-12 col-md-8">
          <h4 class="text-reactive-primary fw-bold mb-3 fs-5">{{ eventData.name }}</h4>
          <p class="text-reactive-primary lh-lg medium">
            {{ eventData.description }}
          </p>
        </div>
      </div>
    </div>
  </section>
</template>
