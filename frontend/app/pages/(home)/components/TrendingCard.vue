<template>
  <div class="trendy-event-wrapper d-flex align-items-end">
    <div class="trendy-badge flex-shrink-0">
      <component :is="badgeComponent" />
    </div>

    <div class="trendy-card flex-grow-1 rounded-3 overflow-hidden">
      <img :src="event.imageUrl" :alt="event.title" class="w-100 h-100 object-fit-cover" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { TrendBadgeOne, TrendBadgeTwo, TrendBadgeThree } from '~/components/icons';
import type { TrendingEvent } from '~/types/home';

interface Props {
  event: TrendingEvent;
}

const props = defineProps<Props>();

const badgeComponent = computed(() => {
  switch (props.event.rank) {
    case 1:
      return TrendBadgeOne;
    case 2:
      return TrendBadgeTwo;
    case 3:
      return TrendBadgeThree;
    default:
      return TrendBadgeOne;
  }
});
</script>

<style scoped>
.trendy-badge {
  width: 53px;
  height: 75px;
  flex-shrink: 0;
  display: flex;
  align-items: flex-start;
  justify-content: center;
}

.trendy-badge :deep(svg) {
  width: 53px;
  height: 75px;
  display: block;
}

.trendy-card {
  aspect-ratio: 16 / 9;
}
</style>
