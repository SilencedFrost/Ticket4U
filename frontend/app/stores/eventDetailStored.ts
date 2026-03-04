import { defineStore } from 'pinia';
import type { EventDetailResponse, Organizer } from '@/pages/event-detail/types/event-detail';
import type { EventCardResponse } from '~/pages/(home)/types/api';

export const useEventStore = defineStore('event', () => {
  const config = useRuntimeConfig();
  const { formatDateTime } = useFormatter();

  const currentEvent = ref<EventDetailResponse | null>(null);
  const relatedEvents = ref<EventCardResponse[]>([]);

  async function fetchEventDetail(eventId: string) {
    try {
      const data = await $fetch<{
        id: string;
        name: string;
        startDate: string;
        addressLine: string;
        description: string;
        minPrice: number;
        maxPrice: number;
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
            price: number;
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
        minPrice: data.minPrice,
        maxPrice: data.maxPrice,
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
              price: seatType.price,
              available: seatType.available,
              description: seatType.description,
              image: seatType.image,
              benefits: seatType.benefits,
            })),
          };
        }),
      } as EventDetailResponse;

      // Explicitly fetch related data after main event loaded
      await Promise.allSettled([
        data.organizerId ? fetchOrganizer(data.organizerId) : Promise.resolve(),
        fetchRelatedEvents(data.id),
      ]);

      return currentEvent.value;
    } catch (err) {
      currentEvent.value = null;
      console.error('[EventStore] Failed to fetch event detail:', err);
      throw err;
    }
  }

  async function fetchRelatedEvents(eventId: string) {
    try {
      const data = await $fetch<EventCardResponse[]>(
        `${config.public.eventDetailUrl}/${eventId}/related`,
      );
      relatedEvents.value = data;
    } catch (err) {
      console.error('[EventStore] Failed to fetch related events:', err);
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
    } catch (err) {
      console.error('[EventStore] Failed to fetch organizer:', err);
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
