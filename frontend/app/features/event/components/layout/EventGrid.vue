<script setup lang="ts">
import type { EventSummary } from '~/features/event/types/Event';
import EventCard from '../core/EventCard.vue';

defineProps<{ events: EventSummary[] }>();
</script>

<template>
  <div v-if="!events || events.length === 0" class="text-center text-reactive-secondary pt-5">
    <i class="bi bi-box2-fill" style="font-size: 80px" />
    <h2>Nothing here</h2>
  </div>
  <div v-else class="row g-2">
    <div v-for="event in events" :key="event.id" class="col-6 col-md-4 col-xxl-3">
      <event-card :event="event" />
    </div>
  </div>
</template>
