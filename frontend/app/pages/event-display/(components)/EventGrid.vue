<template>
  <div>
    <div v-if="events.length > 0" class="row g-3 g-md-4">
      <div v-for="event in events" :key="event.id" class="col-6 col-md-4 col-lg-3">
        <EventCard :event="event" @event-click="$emit('event-click', $event)" />
      </div>
    </div>

    <div v-else class="text-center py-5">
      <p class="text-reactive-secondary mb-0">No finding events</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import EventCard from './EventCard.vue';
import type { Event } from '../types/event-display';

defineProps<{ events: Event[] }>();
defineEmits(['event-click']);
</script>
