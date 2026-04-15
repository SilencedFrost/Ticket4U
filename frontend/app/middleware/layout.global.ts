export default defineNuxtRouteMiddleware((to) => {
  const routeName = String(to.name ?? '').replace(/___\w+$/, '');

  if (routeName.startsWith('auth')) setPageLayout('auth');
  if (routeName.startsWith('settings')) to.meta.hideFooter = true;

  const protectedRoutes = ['settings-account', 'settings-security'];
  if (protectedRoutes.includes(routeName)) {
    const { isLoggedIn } = storeToRefs(useUserStore());
    if (!isLoggedIn.value) {
      const localePath = useLocalePath();
      return navigateTo(localePath('/settings/preferences'));
    }
  }
});
