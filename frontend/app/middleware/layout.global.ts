export default defineNuxtRouteMiddleware((to) => {
  const routeName = String(to.name ?? '').replace(/___\w+$/, '');

  if (routeName.startsWith('auth')) setPageLayout('auth');
  if (routeName.includes('event') && routeName.includes('book-seats')) setPageLayout('blank');
  if (routeName.startsWith('settings')) to.meta.hideFooter = true;

  const protectedRoutes = ['settings-account', 'settings-security'];
  if (protectedRoutes.includes(routeName)) {
    const { isLoggedIn } = storeToRefs(useUserStore());
    if (!isLoggedIn.value) {
      const localePath = useLocalePath();
      // Giữ lại path gốc để modal biết cần redirect về đâu sau login
      return navigateTo(localePath(`/settings/preferences?loginRequired=${to.path}`));
    }
  }

  if (routeName === 'payment' || to.path.includes('/book/checkout')) {
    const { isLoggedIn } = storeToRefs(useUserStore());
    if (!isLoggedIn.value) {
      const localePath = useLocalePath();
      return navigateTo(localePath(`/auth/login?loginRequired=${to.path}`));
    }
  }
});
