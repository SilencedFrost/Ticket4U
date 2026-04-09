<script setup lang="ts">
import CarouselWrapper from '~/features/components/CarouselWrapper.vue';
import type { EventSummary } from '~/features/event/types/Event';
import EventCard from '~/features/event/components/core/EventCard.vue';

const config = useRuntimeConfig();
const eventList = ref<EventSummary[]>([]);
const visibleCount = ref<number>(4);

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

const modes = ['carousel', 'page'];
const selectedMode = ref<'carousel' | 'page'>('carousel');
const wrapAround = ref<boolean>(true);

onMounted(() => getFeaturedEvents());
</script>

<template>
  <div>
    <input v-model="visibleCount" type="number" />
    <select v-model="selectedMode" name="mode-selector">
      <option v-for="mode in modes" :key="mode" :value="mode">{{ mode }}</option>
    </select>
    <input v-model="wrapAround" type="checkbox" />Wrap around?
    <carousel-wrapper
      :items="eventList"
      :visible-count="visibleCount"
      :mode="selectedMode"
      :wrap-around="wrapAround"
    >
      <template #item="{ item }">
        <event-card :event="item" />
      </template>
    </carousel-wrapper>
  </div>
</template>
