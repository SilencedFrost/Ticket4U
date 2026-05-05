<script setup lang="ts">
import type { EventSummary } from '@/features/event/types/Event';
import EventGrid from '@/features/event/components/layout/EventGrid.vue';

const route = useRoute();
const config = useRuntimeConfig();
const localePath = useLocalePath();

const { data: eventList } = await useFetch<EventSummary[]>(
  () => `/public/events/${route.params.id}/related`,
  {
    baseURL: config.public.eventServiceUrl,
    key: `related-events-${route.params.id}`,
    default: () => [],
    onResponseError({ response }) {
      if (response?.status === 403) {
        return;
      }

      console.error('Error loading related events:', response?.statusText ?? 'unknown error');
    },
  },
);

const handleEventClick = (eventId: string) => {
  navigateTo(localePath(`/event/${eventId}`));
};
</script>

<template>
  <section class="py-5">
    <div class="container-xxl">
      <h4 class="text-reactive-primary text-center fw-bold mb-5">
        {{ $t('event_detail.section.related') }}
      </h4>

      <EventGrid :events="eventList" take="{8}" @event-click="handleEventClick" />
    </div>
  </section>
</template>
