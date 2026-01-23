import { ref, computed, onMounted } from 'vue';

type Theme = 'light' | 'dark';

const currentTheme = ref<Theme>('light');

export const useTheme = () => {
  const nextTheme = computed<Theme>(() => (currentTheme.value === 'light' ? 'dark' : 'light'));

  const setTheme = (theme: Theme) => {
    currentTheme.value = theme;
    if (import.meta.client) {
      document.documentElement.setAttribute('data-bs-theme', theme);
      localStorage.setItem('theme', theme);
    }
  };

  const toggleTheme = () => {
    setTheme(nextTheme.value);
  };

  onMounted(() => {
    if (import.meta.client) {
      const savedTheme = localStorage.getItem('theme') as Theme | null;
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      const initialTheme = savedTheme || (prefersDark ? 'dark' : 'light');
      setTheme(initialTheme);
    }
  });

  return {
    currentTheme,
    nextTheme,
    toggleTheme,
    setTheme,
  };
};
