const PASSWORD_SPECIAL_CHAR_REGEX = /[!@$^*()_=+[\]{}\\|;:",./?~`-]+/;
const PASSWORD_LOWERCASE_REGEX = /[a-z]+/;
const PASSWORD_UPPERCASE_REGEX = /[A-Z]+/;
const PASSWORD_DIGIT_REGEX = /\d+/;

export function usePasswordValidation() {
  function validatePasswordValue(value: string): string[] {
    const errors: string[] = [];

    if (value.length < 8) errors.push('auth.error.format.password.length.short');
    if (value.length > 32) errors.push('auth.error.format.password.length.long');
    if (!PASSWORD_LOWERCASE_REGEX.test(value)) errors.push('auth.error.format.password.lowercase');
    if (!PASSWORD_UPPERCASE_REGEX.test(value)) errors.push('auth.error.format.password.uppercase');
    if (!PASSWORD_DIGIT_REGEX.test(value)) errors.push('auth.error.format.password.digit');
    if (!PASSWORD_SPECIAL_CHAR_REGEX.test(value))
      errors.push('auth.error.format.password.special_char');

    return errors;
  }

  return { validatePasswordValue };
}
