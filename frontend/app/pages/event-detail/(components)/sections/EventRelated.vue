<script setup lang="ts">
import type { Event } from '~/pages/(home)/types/home';
import EventGrid from '@/pages/event-display/(components)/EventGrid.vue';
import { computed } from 'vue';

const props = defineProps<{
  eventData: Event[];
}>();

const limitedEvents = computed(() => {
  return props.eventData?.slice(0, 8) || [];
});

const handleEventClick = (eventId: string) => {
  navigateTo(`/event-detail/${eventId}`);
};
</script>

<template>
  <section class="py-5">
    <div class="container-xxl">
      <h2 class="text-reactive-primary text-center fw-bold mb-5">
        {{ $t('event_detail.section.related') }}
      </h2>

      <EventGrid :events="limitedEvents" @event-click="handleEventClick" />
    </div>
  </section>
</template>
