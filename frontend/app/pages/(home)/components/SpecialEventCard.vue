<script setup lang="ts">
import type { Event } from '~/pages/(home)/types/home';
const { vFallback: vImgFallback } = useImagePlaceholder();
interface Props {
  event: Event;
}

const props = defineProps<Props>();

const localePath = useLocalePath();

const handleClick = () => {
  navigateTo(localePath(`/event-detail/${props.event.id}`));
};
</script>

<template>
  <div
    class="special-event-card rounded-4 overflow-hidden cursor-pointer"
    role="button"
    tabindex="0"
    @click="handleClick"
    @keydown.enter="handleClick"
  >
    <img
      v-img-fallback="[350, 200]"
      :src="event.bannerUrl"
      :alt="event.name"
      class="w-100 h-100 object-fit-cover"
    />
  </div>
</template>

<style scoped>
.special-event-card {
  height: 483px;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.special-event-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.cursor-pointer {
  cursor: pointer;
}

@media (max-width: 767.98px) {
  .special-event-card {
    height: 320px;
  }
}
</style>
