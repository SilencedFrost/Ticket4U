export default defineNuxtRouteMiddleware((to) => {
  const pathWithoutLocale = to.fullPath.replace(/^\/(en|vi)/, '');

  if (pathWithoutLocale.startsWith('/auth')) {
    setPageLayout('auth');
  } else if (pathWithoutLocale.startsWith('/event/') && pathWithoutLocale.includes('/book/seats/')) {
    setPageLayout('blank')
  } else {
    setPageLayout('default');
  }
});