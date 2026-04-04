export default defineNuxtRouteMiddleware((to) => {
  const routeName = String(to.name ?? '').replace(/___\w+$/, '');

  if (routeName.startsWith('auth')) setPageLayout('auth');
  if (routeName.startsWith('event') && routeName.includes('book-seats')) setPageLayout('blank');
  if (routeName.startsWith('settings')) to.meta.hideFooter = true;
});
