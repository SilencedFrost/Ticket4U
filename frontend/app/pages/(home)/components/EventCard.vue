<template>
  <div class="event-card">
    <div class="event-card-img mb-3 position-relative overflow-hidden rounded-4">
      <img
        :src="event.imageUrl"
        :alt="event.title"
        class="w-100 h-100 object-fit-cover"
      />
    </div>

    <div v-if="showDetails">
      <h3 class="fs-5 fw-bold text-reactive-primary mb-2 text-ellipsis-2">
        {{ event.title }}
      </h3>

      <p class="text-primary fw-medium mb-1">
        {{ formatPrice(event.price) }}
      </p>

      <p class="text-reactive-secondary mb-0">
        {{ event.date }}
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { Event } from '~/types/home'

interface Props {
  event: Event
  showDetails?: boolean
}

withDefaults(defineProps<Props>(), {
  showDetails: true,
})

const formatPrice = (price: number) => {
  return `Từ ${price.toLocaleString('vi-VN')}đ`
}
</script>

<style scoped>
.event-card-img {
  width: 100%;
  aspect-ratio: 16 / 9;
  background-color: var(--bg-reactive-secondary);
}

.event-card-img img {
  transition: transform 0.3s ease;
}

.event-card-img:hover img {
  transform: scale(1.05);
}

.text-ellipsis-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.3;
  min-height: 2.6em;
}
</style>
