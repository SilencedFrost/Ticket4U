<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import type { Event } from '~/pages/(home)/types/home';
import EventGrid from '~/pages/event-display/(components)/EventGrid.vue';

const route = useRoute();
const config = useRuntimeConfig();
const { formatLongDate } = useFormatter();

const loading = ref(true);
const error = ref<string | null>(null);
const eventData = ref<Event | null>(null);

const routeEventId = computed<string>(() => {
  const rawParam = route.params.eventid;
  if (Array.isArray(rawParam)) return rawParam[0] || '';
  return rawParam ? String(rawParam) : '';
});

const displayEvents = computed<Event[]>(() => (eventData.value ? [eventData.value] : []));

onMounted(async () => {
  if (!routeEventId.value) {
    error.value = 'Event ID was not found in the URL.';
    loading.value = false;
    return;
  }

  try {
    const response = await $fetch<{
      id?: string;
      name?: string;
      bannerUrl?: string;
      minPrice?: number | string;
      startDate?: string;
    }>(`${config.public.eventServiceUrl}/public/events/${routeEventId.value}`);

    eventData.value = {
      id: String(response.id ?? routeEventId.value),
      title: response.name || 'Event',
      imageUrl: response.bannerUrl || 'https://picsum.photos/seed/ticket4u-event-fallback/1200/675',
      price: Number(response.minPrice ?? 0),
      date: formatLongDate(response.startDate ?? null) || '',
    };
  } catch {
    error.value = 'Failed to load event data.';
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="container py-4">
    <div v-if="loading" class="text-reactive-secondary">Loading event data...</div>
    <div v-else-if="error" class="alert alert-danger mb-0">{{ error }}</div>
    <EventGrid v-else :events="displayEvents" />
  </div>
</template>
