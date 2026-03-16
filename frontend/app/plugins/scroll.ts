import { defineNuxtPlugin, useRouter } from '#app';

export default defineNuxtPlugin(() => {
  if (!import.meta.client) return;

  const router = useRouter();

  router.afterEach(async () => {
    // Await vue DOM update
    await nextTick();

    const component = document.querySelector('.auto-scrollable');
    if (component instanceof HTMLElement) {
      component.scrollTo({ top: 0, behavior: 'smooth' });
    }
  });
});
