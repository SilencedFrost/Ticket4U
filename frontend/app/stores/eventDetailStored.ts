import { defineStore } from 'pinia';
import type { EventDetailResponse, Organizer } from '@/pages/event-detail/types/event-detail';
import type { EventCardResponse } from '~/pages/(home)/types/api';

interface EventApiResponse {
  id: string;
  name: string;
  startDate: string;
  addressLine: string;
  aboutVi: string;
  aboutEn: string;
  minPrice: number;
  maxPrice: number;
  categories: CategorySummary[];
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
}

export const useEventStore = defineStore('event', () => {
  const config = useRuntimeConfig();

  const currentEvent = ref<EventDetailResponse | null>(null);
  const relatedEvents = ref<EventCardResponse[]>([]);

  async function fetchEventDetail(eventId: string) {
    try {
      const data = await $fetch<EventApiResponse>(
        `${config.public.eventServiceUrl}/public/events/${eventId}`,
      );

      currentEvent.value = mapEventResponse(data);

      if (data.organizerId) {
        void fetchOrganizer(data.organizerId, data.id);
      }

      await fetchRelatedEvents(data.id);

      return currentEvent.value;
    } catch (err) {
      currentEvent.value = null;
      console.error('Failed to fetch event detail:', err);
      throw err;
    }
  }

  async function fetchRelatedEvents(eventId: string) {
    try {
      const data = await $fetch<EventCardResponse[]>(
        `${config.public.eventServiceUrl}/public/events/${eventId}/related`,
      );

      if (currentEvent.value && currentEvent.value.eventId === eventId) {
        relatedEvents.value = data;
      }
    } catch (err) {
      console.error('Failed to fetch related events:', err);
      if (currentEvent.value && currentEvent.value.eventId === eventId) {
        relatedEvents.value = [];
      }
    }
  }

  async function fetchOrganizer(organizerId: string, eventId: string) {
    try {
      const organizerData = await $fetch<Organizer>(
        `${config.public.userServiceUrl}/public/organizers/${organizerId}`,
      );
      if (currentEvent.value && currentEvent.value.eventId === eventId) {
        currentEvent.value.organizer = organizerData;
      }
    } catch (err) {
      console.error('Failed to fetch organizer:', err);
      if (currentEvent.value && currentEvent.value.eventId === eventId)
        currentEvent.value.organizer = null;
    }
  }

  function mapEventResponse(data: EventApiResponse): EventDetailResponse {
    return {
      eventId: data.id,
      eventTitle: data.name,
      address: data.addressLine,
      aboutVi: data.aboutVi,
      aboutEn: data.aboutEn,
      categories: data.categories || [],
      organizerId: data.organizerId,
      startDate: data.startDate || '',
      minPrice: data.minPrice,
      maxPrice: data.maxPrice,
      imgEvent: {
        heroUrl: data.bannerUrl,
        seatMapUrl: data.seatingPlanImageUrl,
      },
      sessions:
        data.sessions?.map((s) => ({
          id: s.id,
          startDate: s.startDate || '',
          zones: s.zones || [],
        })) || [],
    } as EventDetailResponse;
  }

  return {
    currentEvent,
    relatedEvents,
    fetchEventDetail,
    fetchRelatedEvents,
    fetchOrganizer,
  };
});
