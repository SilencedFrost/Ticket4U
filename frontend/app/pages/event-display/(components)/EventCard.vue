<template>
  <div
    class="cursor-pointer h-100"
    role="button"
    tabindex="0"
    @click="$emit('event-click', event.id)"
  >
    <div class="ratio ratio-16x9 rounded-3 overflow-hidden mb-3">
      <img
        :src="event.imageUrl"
        :alt="event.title"
        class="w-100 h-100 object-fit-cover"
        loading="lazy"
      />
    </div>

    <div>
      <h3 class="fw-bold mb-2 text-reactive-primary fs-5 lh-sm">
        {{ event.title }}
      </h3>
      <p class="text-primary fw-bold mb-1">
        {{ formatPrice(event.price) }}
      </p>
      <p class="text-reactive-secondary mb-0 d-flex align-items-center gap-1 small">
        <i class="bi bi-calendar-event"></i>
        <span>{{ event.date }}</span>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Event } from '../types/event-display';

interface Props {
  event: Event;
}

defineProps<Props>();

defineEmits<{
  'event-click': [id: string];
}>();

const formatPrice = (price: number): string => {
  return `Từ ${price.toLocaleString('vi-VN')}đ`;
};
</script>
