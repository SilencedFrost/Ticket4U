<script setup lang="ts">
import DateRangeFilter from './DateRangeFilter.vue';
import MainFilter from './MainFilter.vue';
</script>

<template>
  <div class="d-flex w-100 justify-content-end gap-2 mb-3">
    <MainFilter mode="mobile" />
    <DateRangeFilter mode="mobile" />
  </div>
</template>
