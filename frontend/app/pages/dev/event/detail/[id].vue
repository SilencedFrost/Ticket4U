<script setup lang="ts">
import type { EventResponse } from '@/features/event/types/Event';
import EventHero from './(components)/EventHero.vue';
const config = useRuntimeConfig();
const route = useRoute();

const event = ref<EventResponse | null>(null);

async function getEvent() {
  try {
    event.value = await $fetch(
      `${config.public.eventServiceUrl}/public/events/${route.params.id}`,
      {
        method: 'GET',
      },
    );
  } catch (e) {
    console.log(e);
    event.value = null;
  }
}

onMounted(() => getEvent());
</script>

<template>
  <div v-if="event" class="h-auto overflow-x-hidden mw-100">
    <event-hero :event="event" />
  </div>
  <div v-else>Loading event...</div>
</template>
