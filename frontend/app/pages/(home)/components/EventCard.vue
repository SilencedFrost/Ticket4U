<template>
  <div class="event-card">
    <div class="event-card-img mb-3 position-relative overflow-hidden rounded-4">
      <img
        v-img-fallback="[350, 150]"
        :src="event.bannerUrl"
        :alt="event.name"
        class="w-100 h-100 object-fit-cover"
      />
    </div>

    <div v-if="showDetails">
      <h3 class="fs-5 fw-bold text-reactive-primary mb-2 text-ellipsis-2">
        {{ event.name }}
      </h3>

      <p class="text-primary fw-medium mb-1">
        {{ formatPrice(event.minPrice) }}
      </p>

      <p class="text-reactive-secondary mb-0">
        {{ formatDate(event.startDate) }}
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { Event } from '~/pages/(home)/types/home'
  
const { vFallback: vImgFallback } = useImagePlaceholder();

interface Props {
  event: Event
  showDetails?: boolean
}

withDefaults(defineProps<Props>(), {
  showDetails: true,
})

const formatPrice = (price: number) => {
  return (price !== null) ? `Từ ${price.toLocaleString('vi-VN')}đ` : "Chưa cập nhật giá";
}

const formatDate = (isoDate: string) => {
  const date = new Date(isoDate)
  const day = date.getDate()
  const month = date.getMonth() + 1
  const year = date.getFullYear()
  return `${day} tháng ${month}, ${year}` //07 tháng 02, 2026
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
