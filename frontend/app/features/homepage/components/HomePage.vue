<script setup lang="ts">
import EventHeroCarousel from '~/features/event/components/layout/EventHeroCarousel.vue';
import EventSection from './layout/EventSection.vue';
import EventGrid from '~/features/event/components/layout/EventGrid.vue';
import { useEventApi } from '~/features/event/composables/useEventApi';

const localePath = useLocalePath();
const { useFeaturedEvents } = useEventApi();
const { data: eventList } = useFeaturedEvents(15);

// TODO: add hero carousel displaying featured
// TODO: add section displaying hot events
// TODO: add section display events near user
// TODO: add section displaying top 3 categories that the user have bought into
// TODO: add section to display other events

const sectionList = ref([
  { key: 'homepage.section.popular', eventList: eventList.value.slice(1, 5), wrapAround: true },
]);

const handleEventClick = (eventId: string) => {
  navigateTo(localePath(`/event/${eventId}`));
};
</script>

<template>
  <div id="homepage-container" class="d-flex flex-column" style="gap: 0.5rem">
    <div id="hero" class=""><!-- Todo: add hero --></div>
    <div id="event-display" class="container-xl">
      <event-hero-carousel
        :events="eventList.slice(0, 1)"
        :wrap-around="true"
        @event-click="handleEventClick"
      />
      <event-section
        v-for="section in sectionList"
        :key="section.key"
        :events="section.eventList"
        :title="$t(section.key)"
        :wrap-around="section.wrapAround"
        @event-click="handleEventClick"
      />
      <hr />
      <h3 class="fw-bold mb-3">Other events</h3>
      <event-grid :events="eventList.slice(9, 15)" @event-click="handleEventClick" />
    </div>
  </div>
</template>
