<script setup lang="ts">
import type { EventSummary } from '@/features/event/types/Event';
import EventGrid from '@/features/event/components/layout/EventGrid.vue';

const route = useRoute();
const config = useRuntimeConfig();

const eventList = ref<EventSummary[]>([]);

async function getFeaturedEvents() {
  try {
    eventList.value = await $fetch(
      `${config.public.eventServiceUrl}/public/events/${route.params.id}/related`,
      {
        method: 'GET',
      },
    );
  } catch (e) {
    console.log(e);
    eventList.value = [];
  }
}

onMounted(() => getFeaturedEvents());

const localePath = useLocalePath();

const handleEventClick = (eventId: string) => {
  navigateTo(localePath(`/dev/event/detail/${eventId}`));
};
</script>

<template>
  <section class="py-5">
    <div class="container-xxl">
      <h2 class="text-reactive-primary text-center fw-bold mb-5">
        {{ $t('event_detail.section.related') }}
      </h2>

      <EventGrid :events="eventList" take="{8}" @event-click="handleEventClick" />
    </div>
  </section>
</template>
