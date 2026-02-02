<template>
  <div class="mb-4">
    <h4 class="mb-3 text-reactive-primary">{{ event.title }}</h4>
    
    <div class="text-reactive-secondary">
      <div class="d-flex align-items-center mb-2">
        <i class="bi bi-calendar3 me-2"></i>
        <small>{{ event.time }}, {{ event.date }}</small>
      </div>
      <div class="d-flex align-items-center">
        <i class="bi bi-geo-alt me-2"></i>
        <small>{{ event.venue }}</small>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { EventDetails } from '../types/ticket'

interface Props {
  event: EventDetails
}

defineProps<Props>()
</script>