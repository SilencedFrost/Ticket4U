<script setup lang="ts">
const { locales, setLocale } = useI18n();
</script>
<template>
  <div class="card bg-reactive-primary overflow-hidden border" style="width: 70px">
    <div
      v-for="item in locales"
      :key="item.code"
      class="menu-item-right"
      @click="setLocale(item.code)"
    >
      <span>{{ item.code.toUpperCase() }}</span>
    </div>
  </div>
</template>
