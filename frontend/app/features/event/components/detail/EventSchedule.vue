<script setup lang="ts">
import type { Event } from '@/features/event/types/Event';
import ShimmerImg from '~/components/ShimmerImg.vue';

defineProps<{
  event: Event;
}>();
</script>

<template>
  <section id="schedule-section" class="card m-3 mx-auto overflow-hidden mw-100 pb-4">
    <div class="d-flex flex-column p-3 gap-2 mb-3 p-md-4">
      <h5 class="text-primary fw-bold mb-0 pb-2 border-bottom">
        {{ $t('event_detail.section.schedule') }}
      </h5>
      <h6 class="text-reactive-primary mb-1 pt-2">{{ event.name }}</h6>
      <p class="text-reactive-secondary mb-2 small">{{ event.addressLine }}</p>
    </div>

    <div class="mb-4 w-100 d-flex justify-content-center">
      <template v-if="event.seatingPlanImageUrl">
        <div class="w-75 rounded-2 overflow-hidden mh-100">
          <shimmer-img :src="event.seatingPlanImageUrl" alt="Seating chart" class="w-100 h-100" />
        </div>
      </template>

      <template v-else>
        <div class="placeholder-seatmap w-75 rounded-2 text-center py-5">
          <i class="bi bi-geo-alt-fill fs-1 text-reactive-secondary"></i>
          <p class="text-reactive-secondary lh-sm mb-0">Seat map currently unavailable</p>
        </div>
      </template>
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
