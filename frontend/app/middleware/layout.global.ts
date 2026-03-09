export default defineNuxtRouteMiddleware((to) => {
  const pathWithoutLocale = to.fullPath.replace(/^\/(en|vi)/, '');

  if (pathWithoutLocale.startsWith('/auth') || pathWithoutLocale.startsWith('/verify-email')) {
    setPageLayout('auth');
  } else {
    setPageLayout('default');
  }
});
