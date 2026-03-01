import { defineStore } from 'pinia';
import type { EventDetailResponse } from '@/pages/event-detail/types/event-detail';
import type { EventCardResponse } from '~/pages/(home)/types/api';

export const useEventStore = defineStore('event', () => {
  const config = useRuntimeConfig();

  const currentEvent = ref<EventDetailResponse | null>(null);
  const relatedEvents = ref<EventCardResponse[]>([]);

  const formatPrice = (price: string | number) =>
    new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(
      typeof price === 'string' ? Number.parseFloat(price) : price,
    );

  const formatDateTime = (isoString: string) => {
    const d = new Date(isoString);
    return {
      date: d.toISOString().split('T')[0],
      time: d.toTimeString().slice(0, 5),
    };
  };

  async function fetchEventDetail(eventId: string) {
    try {
      const { startDate, minPrice, maxPrice, showtimes, ...rest } = await $fetch<{
        startDate: string;
        minPrice: string;
        maxPrice: string;
        showtimes: Array<{
          date: string;
          seatTypes: Array<{ price: string; [key: string]: unknown }>;
          [key: string]: unknown;
        }>;
        [key: string]: unknown;
      }>(`${config.public.eventDetailUrl}/${eventId}`);

      const { date, time } = formatDateTime(startDate);

      currentEvent.value = {
        ...rest,
        date,
        time,
        minPrice: formatPrice(minPrice),
        maxPrice: formatPrice(maxPrice),
        showtimes: showtimes?.map(({ date: stDate, seatTypes, ...stRest }) => {
          const stDateTime = formatDateTime(stDate);
          return {
            ...stRest,
            date: stDateTime.date,
            time: stDateTime.time,
            seatTypes: seatTypes?.map(({ price, ...seatRest }) => ({
              ...seatRest,
              price: formatPrice(price),
            })),
          };
        }),
      } as EventDetailResponse;

      await fetchRelatedEvents(rest.eventId as string, rest.categoryId as number, rest.address as string);
      return currentEvent.value;
    } catch (error) {
      console.error('Lỗi lấy chi tiết sự kiện:', error);
      currentEvent.value = null;
      throw error;
    }
  }

  async function fetchRelatedEvents(eventId: string, categoryId: number, address: string) {
    try {
      const data = await $fetch<EventCardResponse[]>(
        `${config.public.eventDetailUrl}/${eventId}/related`,
        {
          method: 'GET',
          params: {
            categoryId: categoryId,
            address: address,
          },
        },
      );
      relatedEvents.value = data;
    } catch (error) {
      console.error('Error fetching related events:', error);
      relatedEvents.value = [];
    }
  }

  return {
    currentEvent,
    relatedEvents,
    fetchEventDetail,
    fetchRelatedEvents,
  };
});
