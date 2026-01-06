<script setup lang="ts">
defineProps<{
  sideBarImage?: string;
}>();
</script>

<template>
  <div class="col-lg-3 mt-3 d-none d-lg-block px-2">
    <img :src="sideBarImage" alt="" class="rounded-2 img-fluid object-fit-cover" />
  </div>
</template>
