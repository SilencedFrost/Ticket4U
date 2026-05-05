<script setup lang="ts">
import EventCard from '~/features/event/components/core/EventCard.vue';
import type { EventSummary } from '~/features/event/types/Event';

const config = useRuntimeConfig();

const eventList = ref<EventSummary[]>([]);
const colCount = ref<number>(1);
const logger = useLogger();

async function getFeaturedEvents() {
  try {
    eventList.value = await $fetch(`${config.public.eventServiceUrl}/public/events/featured`, {
      method: 'GET',
    });
  } catch (e) {
    logger.error(e);
    eventList.value = [];
  }
}

onMounted(() => getFeaturedEvents());
</script>

<template>
  <div class="d-flex flex-column">
    <div class="p-3"><input v-model="colCount" type="number" class="form-control" /></div>
    <div class="p-3">
      <div class="row g-2">
        <div class="col">
          <event-card v-if="eventList[0]" :event="eventList[0]" />
        </div>
        <div v-for="n in colCount - 1" :key="n" class="col bg-warning">placeholder col</div>
      </div>
    </div>
  </div>
</template>
