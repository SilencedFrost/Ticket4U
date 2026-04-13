<script setup lang="ts">
import type { EventSummary } from '~/features/event/types/Event';
import EventCarousel from '~/features/event/components/layout/EventCarousel.vue';

const config = useRuntimeConfig();
const eventList = ref<EventSummary[]>([]);

async function getFeaturedEvents() {
  try {
    eventList.value = await $fetch(`${config.public.eventServiceUrl}/public/events/featured`, {
      method: 'GET',
    });
  } catch (e) {
    console.log(e);
    eventList.value = [];
  }
}

const wrapAround = ref<boolean>(true);

onMounted(() => getFeaturedEvents());
</script>

<template>
  <div>
    <input v-model="wrapAround" type="checkbox" />Wrap around?
    <div class="p-3">
      <event-carousel :events="eventList" :wrap-around="wrapAround" />
    </div>
  </div>
</template>
