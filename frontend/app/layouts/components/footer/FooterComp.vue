<script setup lang="ts">
import FooterDesktop from './FooterDesktop.vue';
import FooterMobile from './FooterMobile.vue';
import { FOOTER_BOTTOM_LINKS } from './footerData';
</script>

<template>
  <footer class="bg-reactive-dark text-white pt-5 pb-4">
    <div class="container-lg">
      <div class="d-md-none">
        <footer-mobile />
      </div>

      <div class="d-none d-md-block">
        <footer-desktop />
      </div>

      <hr class="text-white-50 opacity-25" />

      <div class="d-flex justify-content-end gap-4">
        <a
          v-for="link in FOOTER_BOTTOM_LINKS"
          :key="link.labelKey"
          :href="link.href"
          class="text-clickable user-select-auto text-decoration-none"
        >
          {{ $t(link.labelKey) }}
        </a>
      </div>
    </div>
  </footer>
</template>
