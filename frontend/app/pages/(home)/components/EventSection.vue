<template>
  <section class="mb-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2 class="text-reactive-primary fs-4 fw-bold mb-0">{{ title }}</h2>
      <NuxtLink
        v-if="showViewAll"
        :to="viewAllLink"
        class="text-reactive-primary text-decoration-none fw-light"
      >
        Xem thêm &gt;
      </NuxtLink>
    </div>
    <EventCarousel
      :items="events"
      :items-per-page="4"
      col-class="col-lg-3 col-md-6 col-sm-12"
    >
      <template #default="{ item }">
        <div class="event-card-wrapper">
          <EventCard :event="item" />
        </div>
      </template>
    </EventCarousel>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Event } from '~/pages/(home)/types/home'
import EventCarousel from './EventCarousel.vue'
import EventCard from './EventCard.vue'

interface Props {
  title: string
  events: Event[]
  showViewAll?: boolean
  categoryId?: number
}

const props = withDefaults(defineProps<Props>(), {
  showViewAll: false,
})

// Generate link with category filter if categoryId is provided
const viewAllLink = computed(() => {
  if (props.categoryId) {
    return `/event-display?categoryIds=${props.categoryId}`
  }
  return '/event-display'
})
</script>

<style scoped>
.event-card-wrapper {
  height: 320px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.event-card-wrapper:hover {
  transform: translateY(-4px);
}
</style>
