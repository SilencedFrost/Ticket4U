export default defineNuxtRouteMiddleware(async () => {
  // Skip middleware on server side - cookies aren't available during SSR
  if (import.meta.server) return

  const userStore = useUserStore()

  if (!userStore.isLoggedIn) {
    await userStore.refresh()
  }

  if (!userStore.isLoggedIn) {
    return navigateTo('/auth/login')
  }

  const roleId = userStore.user.roleId
  if (roleId !== 1 && roleId !== 2) {
    return navigateTo('/')
  }
})