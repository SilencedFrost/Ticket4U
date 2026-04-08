export function useFormatter() {
  const { locale } = useI18n();

  function formatPrice(price: number, currency: 'USD' | 'VND' = 'VND'): string {
    const isVi = locale.value === 'vi';

    if (currency === 'VND') {
      const formatted = price.toLocaleString(isVi ? 'vi-VN' : 'en-US');
      return isVi ? `${formatted}đ` : `đ${formatted}`;
    }

    if (currency === 'USD') {
      const formatted = price.toLocaleString(isVi ? 'vi-VN' : 'en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
      return isVi ? `${formatted}$` : `$${formatted}`;
    }

    return price.toString();
  }

  return {
    formatPrice,
  };
}
