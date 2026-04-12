<script setup lang="ts">
import ThemePreview from '~/features/settings/preferences/components/ThemePreview.vue';
</script>

<template>
  <div class="container-xxl p-2">
    <div class="card p-3">
      <div class="row g-2">
        <div class="col"><ThemePreview theme="light" /></div>
        <div class="col"><ThemePreview theme="dark" /></div>
      </div>
    </div>
  </div>
</template>
