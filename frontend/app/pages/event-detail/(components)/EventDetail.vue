<script setup lang="ts">
import EventHero from './sections/EventHero.vue';
import EventSchedule from './sections/EventSchedule.vue';
import EventAbout from './sections/EventAbout.vue';
import EventTickets from './sections/EventTickets.vue';
import EventOrganizer from './sections/EventOrganizer.vue';
import EventNav from './sections/EventNav.vue';
import EventRelated from './sections/EventRelated.vue';
import EventAds from './sections/EventAds.vue';

const eventStore = useEventStore();
const event = computed(() => eventStore.currentEvent);

useHead({
  title: () => event.value?.eventTitle || 'Loading Event...',
  titleTemplate: (title) => `${title}`,
});
</script>

<template>
  <div v-if="event" class="h-auto overflow-x-hidden mw-100">
    <event-hero :event="event" />
    <event-nav />
    <div class="row m-0 container-xxl mx-auto flex-column flex-lg-row">
      <div class="col-lg-9">
        <event-schedule :event="event" />
        <event-about :about-vi="event.aboutVi" :about-en="event.aboutEn" />
        <event-tickets :show-time="event.showtimes" />
        <event-organizer v-if="event.organizer?.id" :event-data="event.organizer" />
      </div>
      <event-ads />
    </div>
    <event-related :event-data="eventStore.relatedEvents" />
  </div>
  <div v-else>Loading event...</div>
</template>
