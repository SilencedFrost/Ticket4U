export default defineNuxtRouteMiddleware((to) => {
  const pathWithoutLocale = to.fullPath.replace(/^\/(en|vi)/, '');
  const routeName = String(to.name ?? '').replace(/___\w+$/, '');

  if (pathWithoutLocale.startsWith('/auth')) {
    setPageLayout('auth');
  } else if (pathWithoutLocale.startsWith('/event/') && pathWithoutLocale.includes('/book/seats/')) {
    setPageLayout('blank');
  } else {
    setPageLayout('default');
  }

  if (routeName.startsWith('settings')) to.meta.hideFooter = true;
});
