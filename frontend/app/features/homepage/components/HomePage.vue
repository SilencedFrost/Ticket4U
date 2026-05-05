<script setup lang="ts">
import EventHeroCarousel from '~/features/event/components/layout/EventHeroCarousel.vue';
import EventSection from './layout/EventSection.vue';
import EventGrid from '~/features/event/components/layout/EventGrid.vue';
import LocationPermissionPopup from '~/components/LocationPermissionPopup.vue';
import { useEventApi } from '~/features/event/composables/useEventApi';
import { useCategoryApi } from '~/features/event/composables/useCategoryApi';
import type { EventSummary } from '~/features/event/types/Event';

const localePath = useLocalePath();
const { useFeaturedEvents, getLocationEvents } = useEventApi();
const { data: eventList } = useFeaturedEvents(15);
const { data: recommendedCategories } = useCategoryApi().useRecommendedCategories(3);
const { longitude, latitude, initializeLocation } = useLocation();

// TODO: add hero carousel displaying featured
// TODO: add section displaying hot events
// TODO: add section display events near user
// TODO: add section to display other events

// TODO: dynamic sectioning (get from backend)
const sectionList = computed(() => {
  return [
    { key: 'homepage.section.popular', eventList: eventList.value.slice(1, 5), wrapAround: true },
  ];
});

const locationalEvents = ref<EventSummary[]>([]);

const handleEventClick = (eventId: string) => {
  navigateTo(localePath(`/event/${eventId}`));
};

onMounted(async () => {
  if (import.meta.client) {
    await initializeLocation();
  }
});

watch(
  [longitude, latitude],
  async ([lon, lat]) => {
    locationalEvents.value = await getLocationEvents(lon, lat);
  },
  { immediate: true },
);
</script>

<template>
  <location-permission-popup />
  <div id="homepage-container" class="d-flex flex-column" style="gap: 0.5rem">
    <div id="hero" class=""><!-- Todo: add hero --></div>
    <div id="event-display" class="container-xl">
      <event-hero-carousel
        :events="eventList.slice(0, 1)"
        :wrap-around="true"
        @event-click="handleEventClick"
      />

      <template v-for="section in sectionList" :key="section.key">
        <event-section
          :events="section.eventList"
          :title="$t(section.key)"
          :wrap-around="section.wrapAround"
          @event-click="handleEventClick"
        />
      </template>
      <template v-if="locationalEvents">
        <event-section
          :events="locationalEvents"
          :title="$t('homepage.section.near_you')"
          :wrap-around="true"
          @event-click="handleEventClick"
        />
      </template>
      <template v-for="category in recommendedCategories" :key="category.name">
        <event-section
          :events="category.events"
          :title="$t(category.name)"
          :wrap-around="true"
          @event-click="handleEventClick"
        />
      </template>
      <hr />
      <h3 class="fw-bold mb-3">Other events</h3>
      <event-grid :events="eventList.slice(9, 15)" @event-click="handleEventClick" />
    </div>
  </div>
</template>
