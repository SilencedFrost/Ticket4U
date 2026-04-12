<script lang="ts" setup>
const props = defineProps<{
  src: string;
  alt?: string;
}>();
</script>

<template>
  <nuxt-img v-slot="{ isLoaded, imgAttrs }" :src="props.src" :custom="true">
    <div class="h-100 w-100">
      <div v-if="!isLoaded" class="shimmer-effect h-100 w-100" />
      <img
        v-else
        v-bind="imgAttrs"
        class="w-100 h-100"
        style="object-fit: cover"
        :alt="props.alt"
      />
    </div>
  </nuxt-img>
</template>
