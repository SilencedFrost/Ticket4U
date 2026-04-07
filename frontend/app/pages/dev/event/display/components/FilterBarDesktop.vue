<script setup lang="ts">
import DateRangeFilter from './DateRangeFilter.vue';
import MainFilter from './MainFilter.vue';
</script>

<template>
  <div class="d-flex w-100 justify-content-start gap-2 mb-3">
    <DateRangeFilter mode="desktop" />
    <MainFilter mode="desktop" />
  </div>
</template>
