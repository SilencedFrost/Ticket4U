const PHONE_FORMAT_REGEX = /^(0)?(3|5|7|8|9)\d{8}$/;

function isPhoneFormatValid(value: string): boolean {
  return PHONE_FORMAT_REGEX.test(value);
}

export function usePhoneValidation() {
  return { isPhoneFormatValid };
}
