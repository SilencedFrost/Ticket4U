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

useHead({
  title: () => eventStore.currentEvent?.eventTitle || 'Loading Event...',
  titleTemplate: (title) => `${title}`,
});
</script>

<template>
  <div v-if="eventStore.currentEvent" class="h-auto overflow-x-hidden mw-100">
    <event-hero
      :title="eventStore.currentEvent!.eventTitle"
      :date="eventStore.currentEvent!.date"
      :time="eventStore.currentEvent!.time"
      :address="eventStore.currentEvent!.address"
      :min-price="eventStore.currentEvent!.minPrice"
      :hero-image="eventStore.currentEvent!.imgEvent.heroUrl"
    />
    <event-nav />
    <div class="row m-0 container-xxl mx-auto flex-column flex-lg-row">
      <div class="col-lg-9">
        <event-schedule
          :title="eventStore.currentEvent!.eventTitle"
          :address="eventStore.currentEvent!.address"
          :date="eventStore.currentEvent!.date"
          :time="eventStore.currentEvent!.time"
          :event-thumb-image="eventStore.currentEvent!.imgEvent.seatMapUrl"
        />
        <event-about :description="eventStore.currentEvent!.description" />
        <event-tickets :show-time="eventStore.currentEvent!.showtimes" />
        <event-organizer :event-data="eventStore.currentEvent!.organizer" />
      </div>
      <event-ads />
    </div>
    <event-related :event-data="eventStore.relatedEvents" />
  </div>
  <div v-else>Loading event...</div>
</template>
