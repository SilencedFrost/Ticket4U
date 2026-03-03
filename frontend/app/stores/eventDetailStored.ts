import { defineStore } from 'pinia';
import type { EventDetailResponse, Organizer } from '@/pages/event-detail/types/event-detail';
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
      const data = await $fetch<{
        id: string;
        name: string;
        startDate: string;
        addressLine: string;
        description: string;
        minPrice: string;
        maxPrice: string;
        categoryId: number;
        bannerUrl: string;
        seatingPlanImageUrl: string;
        organizerId?: string;
        showtimes: Array<{
          id: string;
          startDate: string;
          seatTypes: Array<{
            id: string;
            name: string;
            price: string;
            available: number;
            description?: string;
            image?: string;
            benefits?: string[] | null;
          }>;
        }>;
      }>(`${config.public.eventDetailUrl}/${eventId}`);

      const { date, time } = formatDateTime(data.startDate);

      currentEvent.value = {
        eventId: data.id,
        eventTitle: data.name,
        address: data.addressLine,
        description: data.description,
        categoryId: data.categoryId,
        organizerId: data.organizerId,
        date,
        time,
        minPrice: formatPrice(data.minPrice),
        maxPrice: formatPrice(data.maxPrice),
        imgEvent: {
          heroUrl: data.bannerUrl,
          seatMapUrl: data.seatingPlanImageUrl,
        },
        showtimes: data.showtimes?.map((showtime) => {
          const { date: stDate, time: stTime } = formatDateTime(showtime.startDate);
          return {
            id: showtime.id,
            date: stDate,
            time: stTime,
            seatTypes: showtime.seatTypes?.map((seatType) => ({
              id: seatType.id,
              name: seatType.name,
              price: formatPrice(seatType.price),
              available: seatType.available,
              description: seatType.description,
              image: seatType.image,
              benefits: seatType.benefits,
            })),
          };
        }),
      } as EventDetailResponse;

      if (data.organizerId) {
        fetchOrganizer(data.organizerId);
      }

      fetchRelatedEvents(data.id);

      return currentEvent.value;
    } catch (error) {
      console.error('Error fetching event detail:', error);
      currentEvent.value = null;
      throw error;
    }
  }

  async function fetchRelatedEvents(eventId: string) {
    try {
      const data = await $fetch<EventCardResponse[]>(
        `${config.public.eventDetailUrl}/${eventId}/related`,
        {
          method: 'GET',
        },
      );
      relatedEvents.value = data;
    } catch (error) {
      console.error('Error fetching related events:', error);
      relatedEvents.value = [];
    }
  }

  async function fetchOrganizer(organizerId: string) {
    try {
      const organizerData = await $fetch<Organizer>(
        `${config.public.userServiceUrl}/public/organizers/${organizerId}`,
      );
      if (currentEvent.value) {
        currentEvent.value.organizer = organizerData;
      }
    } catch (error) {
      console.error('Error fetching organizer:', error);
      if (currentEvent.value) {
        currentEvent.value.organizer = null;
      }
    }
  }

  return {
    currentEvent,
    relatedEvents,
    fetchEventDetail,
    fetchRelatedEvents,
    fetchOrganizer,
  };
});
