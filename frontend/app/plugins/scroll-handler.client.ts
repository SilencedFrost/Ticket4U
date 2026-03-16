import { defineNuxtPlugin, useRouter } from '#app';
import { nextTick } from 'vue';

export default defineNuxtPlugin((_nuxtApp) => {
  const router = useRouter();

  router.afterEach(async (to, from) => {
    // 1. Logic to ignore minor route changes (like i18n or query params)
    const toBaseName = to.name?.toString().split('___')[0];
    const fromBaseName = from.name?.toString().split('___')[0];

    if (toBaseName === fromBaseName && toBaseName !== undefined) {
      return;
    }

    // 2. Wait for Vue to swap the page components in the DOM
    await nextTick();

    // 3. Find your specific container
    const container = document.querySelector('.auto-scrollable');

    if (container instanceof HTMLElement) {
      // Use a double-frame approach to ensure the browser's
      // default 'popstate' scroll restoration has already fired.
      requestAnimationFrame(() => {
        requestAnimationFrame(() => {
          container.scrollTo({
            top: 0,
            left: 0,
            behavior: 'smooth',
          });
        });
      });
    }
  });
});
