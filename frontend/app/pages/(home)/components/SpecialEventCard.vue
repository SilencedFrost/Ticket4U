<template>
  <div class="special-event-card rounded-4 overflow-hidden">
    <img
      v-img-fallback="[350, 200]"
      :src="event.bannerUrl"
      :alt="event.name"
      class="w-100 h-100 object-fit-cover"
    />
  </div>
</template>

<script setup lang="ts">
import type { Event } from '~/pages/(home)/types/home'
const { vFallback: vImgFallback } = useImagePlaceholder();
interface Props {
  event: Event
}

defineProps<Props>()
</script>

<style scoped>
.special-event-card {
  height: 483px;
}

@media (max-width: 767.98px) {
  .special-event-card {
    height: 320px;
  }
}
</style>
