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
    _user.value = await $fetch(`${config.public.userServiceUrl}/auth/login`, {
      credentials: 'include',
      method: 'POST',
      body: { email, password, rememberMe },
    });
  }

  async function loginWithGoogle(idToken: string) {
    _user.value = await $fetch(`${config.public.userServiceUrl}/auth/google`, {
      credentials: 'include',
      method: 'POST',
      body: { idToken },
    });
  }

  async function refresh() {
    try {
      _user.value = await $fetch(`${config.public.userServiceUrl}/auth/refresh`, {
        credentials: 'include',
        method: 'POST',
      });
    } catch {
      // No session - ignore
    }
  }

  async function logout() {
    try {
      await $fetch(`${config.public.userServiceUrl}/auth/logout`, {
        credentials: 'include',
        method: 'POST',
      });
      _user.value = { ...emptyUser };
    } catch {
      const { $i18n } = useNuxtApp();
      alert($i18n.t('auth.session.invalid'));
    }
  }

  return {
    user,
    isLoggedIn,
    login,
    loginWithGoogle,
    refresh,
    logout,
  };
});
