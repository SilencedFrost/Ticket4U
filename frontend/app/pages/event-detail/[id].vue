<template>
  <div v-if="pending">Loading...</div>
  <div v-else-if="error">
    <h3>Error:</h3>
    <pre>{{ error }}</pre>
  </div>
  <event-detail v-else />
</template>

<script setup lang="ts">
import EventDetail from './(components)/EventDetail.vue';

definePageMeta({
  ssr: true,
});

const route = useRoute();
const eventId = route.params.id as string;

const eventStore = useEventStore();

const { pending, error } = await useAsyncData(
  `event-${eventId}`,
  () => eventStore.fetchEventDetail(eventId),
  {
    server: true,
    lazy: false,
  },
);
</script>
