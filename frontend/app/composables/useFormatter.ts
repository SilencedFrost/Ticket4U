import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
import type { Dayjs } from 'dayjs';
import 'dayjs/locale/vi';
import 'dayjs/locale/en';
dayjs.extend(utc);
dayjs.extend(timezone);

export function useFormatter() {
  const i18n = useI18n();
  const locale = i18n?.locale;

  function resolveLocaleTag() {
    return locale?.value?.startsWith('vi') ? 'vi-VN' : 'en-US';
  }

  function resolveDayjsLocale() {
    return locale?.value?.startsWith('vi') ? 'vi' : 'en';
  }

  function toUserZonedDate(isoString: string | null | undefined): Dayjs | null {
    if (!isoString || typeof isoString !== 'string' || isoString.trim() === '') {
      return null;
    }

    const userTimezone = dayjs.tz.guess();
    const d = dayjs(isoString.trim()).tz(userTimezone).locale(resolveDayjsLocale());

    return d.isValid() ? d : null;
  }

  function formatPrice(price: number, currency: string = 'VND'): string {
    const safePrice = price || 0;
    return new Intl.NumberFormat(resolveLocaleTag(), {
      style: 'currency',
      currency,
    }).format(safePrice);
  }

  function formatLongDate(isoString: string | null | undefined): string {
    try {
      const d = toUserZonedDate(isoString);

      if (!d) {
        return '';
      }

      return new Intl.DateTimeFormat(resolveLocaleTag(), {
        day: 'numeric',
        month: 'long',
        year: 'numeric',
      }).format(d.toDate());
    } catch {
      return '';
    }
  }

  function formatDateTime(isoString: string | null | undefined): {
    date: string;
    time: string;
    datetime: string;
    datetimeWithWeekday: string;
  } {
    const outputDate = {
      date: '',
      time: '',
      datetime: '',
      datetimeWithWeekday: '',
    };

    if (!isoString || typeof isoString !== 'string' || isoString.trim() === '') return outputDate;

    try {
      const isVietnamese = locale?.value?.startsWith('vi');
      const d = toUserZonedDate(isoString);

      if (!d) {
        console.warn('Invalid date:', isoString);
        return outputDate;
      }

      const dateFormat = isVietnamese ? 'DD/MM/YYYY' : 'MM/DD/YYYY';

      const dateText = isVietnamese ? d.format('D MMMM YYYY') : d.format('MMMM D, YYYY');
      const weekday = d.format('dddd');

      outputDate.time = d.format('HH:mm');
      outputDate.date = d.format(dateFormat);
      outputDate.datetime = `${outputDate.time}, ${dateText}`;
      outputDate.datetimeWithWeekday = `${outputDate.time}, ${weekday}, ${dateText}`;
    } catch (error) {
      console.error('Error formatting date time:', error, 'Input:', isoString);
    }
    return outputDate;
  }

  return {
    formatPrice,
    formatLongDate,
    formatDateTime,
  };
}
