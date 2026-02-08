<template>
  <div class="position-relative place-card">
    <img
      :src="place.imageUrl"
      :alt="place.name"
      class="w-100 h-100 object-fit-cover rounded-4"
    />
    <div class="place-overlay">
      <h3 class="text-white fw-bold text-center">
        {{ place.name }}
      </h3>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Place } from '~/pages/(home)/types/home'

interface Props {
  place: Place
}

defineProps<Props>()
</script>

<style scoped>
.place-card {
  height: 470px;
  overflow: hidden;
}

.place-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 2rem;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
}
</style>
