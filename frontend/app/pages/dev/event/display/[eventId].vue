<script setup lang="ts">
import type { Event } from './type/event-summary';
import EventDisplay from '~/pages/event-display/(components)/EventDisplay.vue';

const { formatLongDate } = useFormatter();

const mockEvents = computed<Event[]>(() => {
  const formattedDate = formatLongDate(new Date().toISOString()) || '01 tháng 01, 2026';

  return [
    {
      id: 'demo-event-1',
      title: 'Ticket4U Mock Event Display Preview',
      imageUrl: 'https://picsum.photos/seed/ticket4u-dev-preview/1200/675',
      price: 149000,
      date: formattedDate,
    },
    {
      id: 'demo-event-2-alt',
      title: 'Ticket4U Mock Event Alternative',
      imageUrl: 'https://picsum.photos/seed/ticket4u-dev-preview-2/1200/675',
      price: 99000,
      date: formattedDate,
    },
  ];
});
</script>

<template>
  <EventDisplay :events="mockEvents" />
</template>
