<script setup lang="ts">
import type { EventResponse } from '@/features/event/types/Event';

defineProps<{
  event: EventResponse;
}>();
</script>

<template>
  <section id="schedule-section" class="card m-3 mx-auto overflow-hidden mw-100 pb-4">
    <div class="bg-reactive-gray w-100 p-3 d-flex justify-content-between align-items-center">
      <h5 class="text-reactive-primary fw-bold mb-0">
        {{ $t('event_detail.section.schedule') }}
      </h5>
    </div>

    <div class="d-flex flex-column p-3 gap-2 mb-3 p-md-4">
      <h6 class="text-reactive-primary mb-1">{{ event.name }}</h6>
      <p class="text-reactive-secondary mb-2 small">{{ event.addressLine }}</p>
    </div>

    <div class="mb-4 w-100 d-flex justify-content-center">
      <img
        v-img-fallback="[900, 1000]"
        :src="event.seatingPlanImageUrl"
        alt=""
        class="w-75 rounded-2 object-fit-cover img-fluid mh-100"
      />
    </div>
    <div class="border-bottom my-4" />
    <div class="d-flex justify-content-between align-items-center w-100 px-3">
      <div class="date">
        <p class="mb-0 text-primary fw-semibold">
          {{ $d(new Date(event.startDate), 'long') }}
        </p>
      </div>
      <div class="time">
        <button class="btn btn-primary text-reactive-primary">
          {{ $d(new Date(event.startDate), { hour: '2-digit', minute: '2-digit' }) }}
        </button>
      </div>
    </div>
  </section>
</template>
