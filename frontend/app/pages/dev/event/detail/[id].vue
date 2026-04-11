<script setup lang="ts">
import type { Event } from '@/features/event/types/Event';
import type { OrganizerSummary } from '~/features/Organizer';
import EventHero from '../../../../features/event/components/detail/EventHero.vue';
import EventNav from '../../../../features/event/components/detail/EventNav.vue';
import EventAbout from '../../../../features/event/components/detail/EventAbout.vue';
import EventSchedule from '../../../../features/event/components/detail/EventSchedule.vue';
import EventOrganizer from '../../../../features/event/components/detail/EventOrganizer.vue';
import EventRelated from '../../../../features/event/components/detail/EventRelated.vue';
const config = useRuntimeConfig();
const route = useRoute();

const event = ref<Event | null>(null);
const organizer = ref<OrganizerSummary | null>(null);

async function getEvent() {
  try {
    event.value = await $fetch(
      `${config.public.eventServiceUrl}/public/events/${route.params.id}`,
      {
        method: 'GET',
      },
    );

    if (event.value?.organizerId) {
      fetchOrganizer(event.value.organizerId);
    }
  } catch (e) {
    console.log(e);
    event.value = null;
    organizer.value = null;
  }
}

async function fetchOrganizer(id: string) {
  try {
    organizer.value = await $fetch<OrganizerSummary>(
      `${config.public.userServiceUrl}/public/organizers/${id}`,
    );
  } catch (err) {
    console.warn(err);
    organizer.value = null;
  }
}

onMounted(() => getEvent());
</script>

<template>
  <div v-if="event" class="h-auto overflow-x-hidden mw-100">
    <event-hero :event="event" />
    <event-nav />
    <div class="rowz m-0 container-xxl mx-auto flex-column flex-lg-row">
      <div class="col-lg-12">
        <event-schedule :event="event" />
        <event-about :about-vi="event.aboutVi" :about-en="event.aboutEn" />
        <!-- To do: Add event ticket section here -->
        <event-organizer v-if="organizer" :organizer-data="organizer" />
      </div>
    </div>
    <event-related />
  </div>
  <div v-else>Loading event...</div>
</template>
