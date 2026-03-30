<script lang="ts" setup>
const props = defineProps<{
  src: string;
  alt?: string;
}>();
</script>

<template>
  <nuxt-img v-slot="{ isLoaded, imgAttrs }" :src="props.src" :custom="true" :alt="props.alt">
    <div v-if="!isLoaded" class="shimmer-effect h-100 w-100" />

    <img v-else v-bind="imgAttrs" class="w-100 h-100" style="object-fit: cover" :alt="props.alt" />
  </nuxt-img>
</template>

<style scoped>
.shimmer-effect {
  background: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.4) 50%,
    rgba(255, 255, 255, 0) 100%
  );
  background-size: 200% 100%;
  background-color: var(--bs-gray-400);

  animation: shimmer 1.5s infinite;
}

@keyframes shimmer {
  0% {
    background-position: -200% 0;
  }
  100% {
    background-position: 200% 0;
  }
}
</style>
