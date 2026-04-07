export default defineNuxtRouteMiddleware((to) => {
  const routeName = String(to.name ?? '').replace(/___\w+$/, '');

  if (routeName.startsWith('auth')) setPageLayout('auth');
  if (routeName.startsWith('settings')) to.meta.hideFooter = true;
  if (routeName.startsWith('organizer')) setPageLayout('organizer');

  /** TODO: thêm route guard cho /settings/account và /settings/security
   * redirect về login nếu chưa authenticated
   * KHÔNG chặn /settings/preferences vì chưa auth thì lưu bằng cookie */

});
