<script setup lang="ts">
import { computed } from 'vue';
import type { Event } from '~/pages/(home)/types/home';
import EventCarousel from './EventCarousel.vue';
import EventCard from './EventCard.vue';

interface Props {
  title: string;
  events: Event[];
  showViewAll?: boolean;
  categoryId?: number;
}

const props = withDefaults(defineProps<Props>(), {
  showViewAll: false,
});

// Generate link with category filter if categoryId is provided
const viewAllLink = computed(() => {
  if (props.categoryId) {
    return `/event-display?categoryIds=${props.categoryId}`;
  }
  return '/event-display';
});
</script>

<template>
  <section class="mb-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2 class="text-reactive-primary fs-4 fw-bold mb-0">{{ title }}</h2>
      <NuxtLinkLocale
        v-if="showViewAll"
        :to="viewAllLink"
        class="text-reactive-primary text-decoration-none fw-light"
      >
        {{ $t('common.see.more') }} &gt;
      </NuxtLinkLocale>
    </div>
    <EventCarousel :items="events" :items-per-page="4" col-class="col-lg-3 col-md-6 col-sm-12">
      <template #default="{ item }">
        <div class="event-card-wrapper">
          <EventCard :event="item" />
        </div>
      </template>
    </EventCarousel>
  </section>
</template>

<style scoped>
.event-card-wrapper {
  height: 100%; /* Change fixed height to 100% so it lets children dictate height or stretches flexibly */
  cursor: pointer;
  transition: transform 0.3s ease;
}

/* Ensure child card takes full height to align items well in rows */
.event-card-wrapper > :deep(.event-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.event-card-wrapper > :deep(.event-card) > div:last-child {
  flex-grow: 1;
}

.event-card-wrapper:hover {
  transform: translateY(-4px);
}
</style>
