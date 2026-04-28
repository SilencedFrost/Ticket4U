export default defineNuxtRouteMiddleware((to) => {
  const routeName = String(to.name ?? '').replace(/___\w+$/, '');

  if (routeName.startsWith('auth')) setPageLayout('auth');
  if (routeName.startsWith('settings')) to.meta.hideFooter = true;
  if (routeName.startsWith('manage')) setPageLayout('manage');
});
