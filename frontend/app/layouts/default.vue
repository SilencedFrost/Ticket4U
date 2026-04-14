<script setup lang="ts">
import NavBar from './components/navigation/NavBar.vue';
import FooterComp from './components/footer/FooterComp.vue';

const { currentTheme } = useTheme();
const route = useRoute();
const scrollContainer = ref<HTMLElement | null>(null);

watch(
  () => route.fullPath,
  async () => {
    await nextTick();
    scrollContainer.value?.scrollTo({ top: 0, left: 0, behavior: 'auto' });
  },
);
</script>

<template>
  <div class="d-flex flex-column overflow-hidden h-100">
    <header class="sticky-top">
      <nav-bar />
    </header>
    <div ref="scrollContainer" class="overflow-auto flex-fill">
      <main
        :class="[
          { 'bg-reactive-primary': currentTheme == 'dark' },
          { 'bg-reactive-secondary': currentTheme == 'light' },
        ]"
        style="min-height: 100%"
      >
        <slot />
      </main>
      <footer v-if="!route.meta.hideFooter"><footer-comp /></footer>
    </div>
  </div>
</template>
