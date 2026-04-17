const THEME_COOKIE_KEY = 'user-theme-preference';
type Theme = 'light' | 'dark';
// TODO: Extend Theme to include an "auto" mode synced with system preference.

export function useTheme() {
  const themeCookie = useCookie<Theme | null>(THEME_COOKIE_KEY, {
    maxAge: 60 * 60 * 24 * 365,
    sameSite: 'lax',
    path: '/',
  });

  const currentTheme = useState<Theme>('app-theme', () => {
    return themeCookie.value === 'dark' ? 'dark' : 'light';
  });

  // Roll forward expiry by creating a fresh useCookie instance
  if (themeCookie.value) {
    const currentValue = themeCookie.value;
    // Force a new Set-Cookie by reassigning with fresh options
    themeCookie.value = null;
    nextTick(() => {
      themeCookie.value = currentValue;
    });
  }

  useHead({
    htmlAttrs: {
      'data-bs-theme': () => currentTheme.value,
    },
  });

  watch(currentTheme, (newTheme) => {
    themeCookie.value = newTheme;
    if (import.meta.client) {
      document.documentElement.setAttribute('data-bs-theme', newTheme);
    }
  });

  const toggleTheme = () => {
    // TODO: When "auto" is supported, update toggle logic and provide explicit mode selection.
    currentTheme.value = currentTheme.value === 'light' ? 'dark' : 'light';
  };

  // TODO: Add a future Appearance UI option/menu item for selecting "auto" mode.
  const nextTheme = computed(() => (currentTheme.value === 'light' ? 'dark' : 'light'));

  return {
    currentTheme,
    toggleTheme,
    nextTheme,
  };
}
