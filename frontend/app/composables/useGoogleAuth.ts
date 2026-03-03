type GoogleButtonTheme = 'outline' | 'filled_black';
type GoogleButtonText = 'signin_with' | 'signup_with' | 'continue_with';

interface GoogleAuthOptions {
  buttonRef: Ref<HTMLElement | null>;
  onCredential: (idToken: string) => void;
  buttonText?: GoogleButtonText;
  buttonWidth?: number;
}

export function useGoogleAuth(options: GoogleAuthOptions) {
  const config = useRuntimeConfig();
  const { currentTheme } = useTheme();
  const loaded = ref(false);
  const scriptError = ref(false);
  const buttonTheme = computed<GoogleButtonTheme>(() =>
    currentTheme.value === 'dark' ? 'filled_black' : 'outline',
  );

  function loadScript(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (window.google?.accounts?.id) {
        loaded.value = true;
        resolve();
        return;
      }

      const script = document.createElement('script');
      script.src = 'https://accounts.google.com/gsi/client';
      script.async = true;
      script.defer = true;
      script.onload = () => {
        loaded.value = true;
        resolve();
      };
      script.onerror = () => reject(new Error('Failed to load Google Identity Services'));
      document.head.appendChild(script);
    });
  }

  function initialize() {
    google.accounts.id.initialize({
      client_id: config.public.googleClientId,
      callback: (response: google.accounts.id.CredentialResponse) => {
        options.onCredential(response.credential);
      },
    });
  }

  function renderButton(element: HTMLElement) {
    google.accounts.id.renderButton(element, {
      type: 'standard',
      theme: buttonTheme.value,
      size: 'large',
      text: options.buttonText ?? 'signup_with',
      width: options.buttonWidth ?? 320,
    });
  }

  function reRenderButton(el: HTMLElement | null) {
    if (!loaded.value || !el) return;
    el.innerHTML = '';
    renderButton(el);
  }

  function clickHiddenButton() {
    const container = options.buttonRef.value;
    if (!container) return;
    const btn =
      container.querySelector<HTMLElement>('[role="button"]') ||
      container.querySelector<HTMLElement>('div[aria-labelledby]') ||
      container.querySelector<HTMLElement>('iframe');
    btn?.click();
  }

  onMounted(async () => {
    try {
      await loadScript();
      initialize();
      if (options.buttonRef.value) renderButton(options.buttonRef.value);
    } catch {
      scriptError.value = true;
    }
  });

  watch(buttonTheme, () => reRenderButton(options.buttonRef.value));

  return {
    loaded,
    scriptError,
    clickHiddenButton,
  };
}
