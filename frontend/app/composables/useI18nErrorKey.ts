const I18N_KEY_PATTERN = /^[a-z]+(\.[a-z0-9_]+)+$/i;

function toSafeI18nKey(message: string | undefined, fallback: string): string {
  if (!message) {
    return fallback;
  }

  return I18N_KEY_PATTERN.test(message) ? message : fallback;
}

export function useI18nErrorKey() {
  function toValidationErrorI18nKey(message?: string): string {
    return toSafeI18nKey(message, 'auth.error.validation');
  }

  function toGenericErrorI18nKey(message?: string): string {
    return toSafeI18nKey(message, 'auth.error.unknown');
  }

  return {
    toSafeI18nKey,
    toValidationErrorI18nKey,
    toGenericErrorI18nKey,
  };
}
