<script setup lang="ts">
import type { Event } from '~/pages/(home)/types/home';

interface Props {
  event: Event;
  showDetails?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  showDetails: true,
});

const localePath = useLocalePath();
const { formatPrice, formatLongDate } = useFormatter();

const handleClick = () => {
  navigateTo(localePath(`/event-detail/${props.event.id}`));
};

const formatEventPrice = (price: number | null | undefined) => {
  if (price === null || price === undefined) {
    return $t('common.price.not_updated');
  }

  return `${$t('common.price.from')} ${formatPrice(price)}`;
};

const formatDate = (isoDate: string) => formatLongDate(isoDate);
</script>

<template>
  <div
    class="event-card cursor-pointer"
    role="button"
    tabindex="0"
    @click="handleClick"
    @keydown.enter="handleClick"
  >
    <div class="event-card-img mb-3 position-relative overflow-hidden rounded-4">
      <img :src="event.bannerUrl" :alt="event.name" class="w-100 h-100 object-fit-cover" />
    </div>

    <div v-if="showDetails">
      <h3 class="fs-5 fw-bold text-reactive-primary mb-2 text-ellipsis-2">
        {{ event.name }}
      </h3>

      <p class="text-primary fw-medium mb-1">
        {{ formatEventPrice(event.minPrice) }}
      </p>

      <p class="text-reactive-secondary mb-0">
        {{ formatDate(event.startDate) }}
      </p>
    </div>
  </div>
</template>

<style scoped>
.event-card-img {
  width: 100%;
  aspect-ratio: 16 / 9;
  background-color: var(--bg-reactive-secondary);
}

.event-card-img img {
  transition: transform 0.3s ease;
}

.event-card-img:hover img {
  transform: scale(1.05);
}

.event-card:hover {
  opacity: 0.9;
}

.text-ellipsis-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.3;
  min-height: 2.6em;
}
</style>
