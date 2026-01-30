import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', () => {
  const config = useRuntimeConfig();

  const emptyUser: User = {
    id: '',
    roleId: -1,
    username: '',
    email: '',
  };

  const _user = ref<User>({ ...emptyUser });

  const user = computed(() => _user.value);
  const isLoggedIn = computed(() => _user.value.roleId >= 0);

  async function login(email: string, password: string, rememberMe: boolean) {
    _user.value = await $fetch(`${config.public.authUrl}/login`, {
      credentials: 'include',
      method: 'POST',
      body: { email, password, rememberMe },
    });
  }

  async function refresh() {
    try {
      _user.value = await $fetch(`${config.public.authUrl}/refresh`, {
        credentials: 'include',
        method: 'POST',
      });
    } catch {
      // No session - ignore
    }
  }

  async function logout() {
    try {
      await $fetch(`${config.public.authUrl}/logout`, {
        credentials: 'include',
        method: 'POST',
      });
    } catch {
      // Ignore logout errors
    }
    _user.value = { ...emptyUser };
  }

  return {
    user,
    isLoggedIn,
    login,
    refresh,
    logout,
  };
});
