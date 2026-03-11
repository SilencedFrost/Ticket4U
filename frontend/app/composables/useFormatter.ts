import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';

dayjs.extend(utc);
dayjs.extend(timezone);

export function useFormatter() {
  const { locale } = useI18n();

  function formatPrice(price: number, currency: string = 'VND'): string {
    return new Intl.NumberFormat(locale.value || 'vi-VN', {
      style: 'currency',
      currency,
    }).format(price);
  }

  function formatDateTime(isoString: string) {
    if (!isoString) return { date: '', time: '' };

    const d = dayjs(isoString).tz('Asia/Ho_Chi_Minh');

    if (!d.isValid()) return { date: 'Wrong data', time: '' };

    const isVietnamese = locale.value?.startsWith('vi');
    const dateFormat = isVietnamese ? 'DD/MM/YYYY' : 'MM/DD/YYYY';

    return {
      date: d.format(dateFormat),
      time: d.format('HH:mm'),
    };
  }

  return {
    formatPrice,
    formatDateTime,
  };
}
