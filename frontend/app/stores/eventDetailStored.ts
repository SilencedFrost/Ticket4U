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
        aboutVi: string;
        aboutEn: string;
        minPrice: number;
        maxPrice: number;
        categoryId: number;
        bannerUrl: string;
        seatingPlanImageUrl: string;
        organizerId?: string;
        sessions: Array<{
          id: string;
          startDate: string;
          zones: Array<{
            id: string;
            name: string;
            price: number;
            available: number;
            descriptionVi?: string;
            descriptionEn?: string;
            giftImageUrl?: string;
            perks?: string[] | null;
          }>;
        }>;
      }>(`${config.public.eventDetailUrl}/${eventId}`);

      const { date, time } = formatDateTime(data.startDate);

      currentEvent.value = {
        eventId: data.id,
        eventTitle: data.name,
        address: data.addressLine,
        aboutVi: data.aboutVi,
        aboutEn: data.aboutEn,
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
        sessions: data.sessions?.map((session) => {
          const { date: sDate, time: sTime } = formatDateTime(session.startDate);
          return {
            id: session.id,
            date: sDate,
            time: sTime,
            zones: session.zones?.map((zone) => ({
              id: zone.id,
              name: zone.name,
              price: zone.price,
              available: zone.available,
              descriptionVi: zone.descriptionVi,
              descriptionEn: zone.descriptionEn,
              giftImageUrl: zone.giftImageUrl,
              perks: zone.perks,
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
