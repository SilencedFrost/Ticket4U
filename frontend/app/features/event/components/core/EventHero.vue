<script setup lang="ts">
import type { EventSummary } from '~/features/event/types/Event';
import ShimmerImg from '~/components/ShimmerImg.vue';

defineProps<{
  event: EventSummary;
}>();

defineEmits<{
  'event-click': [id: string];
}>();

const isMounted = ref(false);
</script>

<template>
  <div class="cursor-pointer" @click="$emit('event-click', event.id)">
    <template v-if="!isMounted">
      <div class="ratio ratio-16x9 rounded-3 overflow-hidden mb-2 shimmer-effect" />
    </template>

    <client-only @vue:mounted="isMounted = true">
      <div class="ratio ratio-16x9 rounded-3 overflow-hidden mb-2">
        <shimmer-img :src="event.bannerUrl.wide" :alt="event.name" />
      </div>
    </client-only>
  </div>
</template>
