export default defineNuxtRouteMiddleware((to) => {
  const routeName = String(to.name ?? '').replace(/___\w+$/, '');

  if (routeName.startsWith('auth')) setPageLayout('auth');
  if (routeName.includes('event') && routeName.includes('book-seats')) setPageLayout('blank');
  if (routeName.startsWith('settings')) to.meta.hideFooter = true;

  const protectedRoutes = ['settings-account', 'settings-security'];
  if (protectedRoutes.includes(routeName)) {
    // TODO(vi): Khi đã có modal xác nhận ở menu settings, bỏ redirect cưỡng bức này.
    // Flow mong muốn: người dùng bấm tab hạn chế -> modal "cần đăng nhập" -> chọn đăng nhập
    // mới chuyển trang, và sau khi login thì quay lại đúng tab đã chọn.
    const { isLoggedIn } = storeToRefs(useUserStore());
    if (!isLoggedIn.value) {
      const localePath = useLocalePath();
      return navigateTo(localePath('/settings/preferences'));
    }
  }
});
