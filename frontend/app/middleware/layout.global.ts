export default defineNuxtRouteMiddleware((to) => {
  const pathWithoutLocale = to.fullPath.replace(/^\/(en|vi)/, '');

  if (pathWithoutLocale.startsWith('/auth')) {
    setPageLayout('auth');
  }  else if (pathWithoutLocale.startsWith('/select-ticket')) {
  setPageLayout('blank')
  } else {
    setPageLayout('default');
  }
});
