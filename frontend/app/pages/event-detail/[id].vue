<template>
  <div v-if="pending">{{ $t('common.loading') }}</div>
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

function resolveStatusCode(err: unknown): number {
  if (typeof err === 'object' && err !== null) {
    const errorLike = err as { statusCode?: number; status?: number };

    if (typeof errorLike.statusCode === 'number') {
      return errorLike.statusCode;
    }

    if (typeof errorLike.status === 'number') {
      return errorLike.status;
    }
  }

  return 500;
}

const { pending } = await useAsyncData(
  `event-${eventId}`,
  async () => {
    try {
      return await eventStore.fetchEventDetail(eventId);
    } catch (err: unknown) {
      const statusCode = resolveStatusCode(err);

      if (statusCode === 404) {
        throw createError({
          statusCode: 404,
          statusMessage: 'EVENT_NOT_FOUND',
        });
      }

      throw createError({
        statusCode: statusCode >= 400 ? statusCode : 500,
        statusMessage: 'EVENT_FETCH_FAILED',
      });
    }
  },
  {
    server: true,
    lazy: false,
  },
);
</script>
