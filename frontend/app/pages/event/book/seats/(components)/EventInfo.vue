<script setup lang="ts">
import type { Event } from '~/pages/event/book/seats/(types)/event.type'
defineProps<{ event: Event }>()
</script>
<template>
  <div class="event-info-wrapper mb-4">
    <h4 class="mb-3 text-reactive-primary">{{ event.title }}</h4>
    <div class="text-reactive-secondary">
      <div class="d-flex align-items-center mb-2">
        <i class="bi bi-calendar3 me-2"/>
        <small>{{ event.time }}, {{ event.date }}</small>
      </div>
      <div class="d-flex align-items-center">
        <i class="bi bi-geo-alt me-2"/>
        <small>{{ event.venue }}</small>
      </div>
    </div>
  </div>
</template>
