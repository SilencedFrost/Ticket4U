export const useFormatter = () => {
  const { locale } = useI18n();

  const formatPrice = (price: number, currency: string = 'VND') => {
    return new Intl.NumberFormat(locale.value || 'vi-VN', {
      style: 'currency',
      currency,
    }).format(price);
  };

  const formatDateTime = (isoString: string) => {
    const d = new Date(isoString);
    return {
      date: d.toISOString().split('T')[0],
      time: d.toTimeString().slice(0, 5),
    };
  };

  return {
    formatPrice,
    formatDateTime,
  };
};
