// Khai báo type cho Google Identity Services

export {};

declare global {
  interface Window {
    google?: typeof google;
  }

  namespace google.accounts.id {
    interface CredentialResponse {
      credential: string;
      select_by: string;
    }

    interface GsiButtonConfiguration {
      type?: 'standard' | 'icon';
      theme?: 'outline' | 'filled_blue' | 'filled_black';
      size?: 'large' | 'medium' | 'small';
      text?: 'signin_with' | 'signup_with' | 'continue_with' | 'signin';
      shape?: 'rectangular' | 'pill' | 'circle' | 'square';
      logo_alignment?: 'left' | 'center';
      width?: number;
      locale?: string;
    }

    interface IdConfiguration {
      client_id: string;
      callback?: (response: CredentialResponse) => void;
      auto_select?: boolean;
      cancel_on_tap_outside?: boolean;
      context?: 'signin' | 'signup' | 'use';
    }

    function initialize(config: IdConfiguration): void;
    function prompt(callback?: (notification: unknown) => void): void;
    function renderButton(parent: HTMLElement, options: GsiButtonConfiguration): void;
    function disableAutoSelect(): void;
    function revoke(hint: string, callback?: (response: { successful: boolean }) => void): void;
  }
}
