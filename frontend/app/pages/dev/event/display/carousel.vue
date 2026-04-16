<script setup lang="ts">
import type { EventSummary } from '~/features/event/types/Event';
import EventCarousel from '~/features/event/components/layout/EventCarousel.vue';

const config = useRuntimeConfig();
const eventList = ref<EventSummary[]>([]);
const width = ref<number>(100);

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

onMounted(() => {
  getFeaturedEvents();
  width.value = window.innerWidth;
});
</script>

<template>
  <div>
    <input v-model="wrapAround" type="checkbox" />Wrap around?
    <input v-model.number="width" type="number" min="0" step="10" />

    <div :style="{ width: width + 'px' }">
      <event-carousel :events="eventList" :wrap-around="wrapAround" />
    </div>
  </div>
</template>
