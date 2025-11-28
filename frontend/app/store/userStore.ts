import { defineStore } from "pinia";

export const useUserStore = defineStore('user', () => {
  const user = ref<User>({
    id: '',
    roleId: -1,
    username: '',
    email: ''
  });

  const isLoggedIn = computed(() => user.value.roleId < 0);

  async function login() {

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