import path from "path";

export default defineNuxtRouteMiddleware((to) => {
  const pathWithoutLocale = to.fullPath.replace(/^\/(en|vi)/, '');

  if (pathWithoutLocale.startsWith('/auth')) {
    setPageLayout('auth');
  } else if (pathWithoutLocale.startsWith('/ticket-select')) {
    setPageLayout('minimal');
  } else {
    setPageLayout('default');
  }
});
