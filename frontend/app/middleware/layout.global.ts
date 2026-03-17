export default defineNuxtRouteMiddleware((to) => {
  const pathWithoutLocale = to.fullPath.replace(/^\/(en|vi)/, '');

  if (pathWithoutLocale.startsWith('/auth')) {
    setPageLayout('auth');
  } else if (pathWithoutLocale.startsWith('/organizer')) {
    setPageLayout('organizer');
  } else {
    setPageLayout('default');
  }
});
