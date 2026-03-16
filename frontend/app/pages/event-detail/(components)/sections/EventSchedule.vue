<script setup lang="ts">
import type { EventDetailResponse } from '@/pages/event-detail/types/event-detail';

const { formatDateTime } = useFormatter();

defineProps<{
  event: EventDetailResponse;
}>();
</script>

<template>
  <section id="schedule-section" class="p-3 p-md-4 card bg-reactive-secondary m-3 mx-auto mw-100">
    <div class="container-xxl">
      <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 h4">
        {{ $t('event_detail.section.schedule') }}
      </h3>

      <div class="d-flex flex-column gap-2 mb-3">
        <h6 class="text-reactive-primary mb-1">{{ event.eventTitle }}</h6>
        <p class="text-reactive-secondary mb-2 small">{{ event.address }}</p>
      </div>

      <div class="mb-4">
        <img
          :src="event.imgEvent.seatMapUrl"
          alt=""
          class="w-auto rounded-2 object-fit-cover img-fluid mh-100"
        />
      </div>
      <div class="border-bottom my-4" />
      <div class="row w-100">
        <div class="col-12">
          <p class="mb-0">
            <i class="bi bi-calendar-event text-primary me-2"></i>
            <strong>{{ formatDateTime(event.startDate).dateTime }}</strong>
          </p>
        </div>
      </div>
    </div>
  </section>
</template>
