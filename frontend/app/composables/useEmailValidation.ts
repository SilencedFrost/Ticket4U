const EMAIL_FORMAT_REGEX = /^[a-zA-Z0-9._+=-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

export function useEmailValidation() {
  function isEmailFormatValid(value: string): boolean {
    return EMAIL_FORMAT_REGEX.test(value);
  }

  return { isEmailFormatValid };
}
