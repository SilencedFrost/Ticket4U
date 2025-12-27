import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', () => {
  const config = useRuntimeConfig()
  const user = ref<User>({
    id: '',
    roleId: -1,
    username: '',
    email: ''
  });

  const isLoggedIn = computed(() => user.value.roleId >= 0);

  async function login(email: string, password: string, rememberMe: boolean) {
      user.value = await $fetch(`${config.public.authUrl}/login`, {
      credentials: 'include',
      method: 'POST',
      body: {
        email: email,
        password: password,
        rememberMe: rememberMe
      }
    })
  }

  async function logout() {

  }
  
  return {
    user: readonly(user),
    isLoggedIn,
    login,
    logout
  }
})