import { defineStore } from 'pinia';
import type { EventDetailResponse } from '@/pages/event-detail/types/event-detail';
import type { EventCardDTO } from '~/pages/(home)/types/api';

export const useEventStore = defineStore('event', () => {
  const config = useRuntimeConfig();

  const currentEvent = ref<EventDetailResponse | null>(null);
  const relatedEvents = ref<EventCardDTO[]>([]);
  const selectedEventId = ref<string | null>(null);  // ← new

  async function fetchEventDetail(eventId: string) {
    try {
      const data = await $fetch<EventDetailResponse>(`${config.public.eventDetailUrl}/${eventId}`, {
        method: 'GET',
      });

      if (data) {
        await fetchRelatedEvents(data.eventId, data.categoryId, data.address);
      }

      currentEvent.value = data;
      return currentEvent.value;
    } catch (error) {
      console.error('Lỗi lấy chi tiết sự kiện:', error);
      currentEvent.value = null;
      throw error;
    }
  }

  async function fetchRelatedEvents(eventId: string, categoryId: number, address: string) {
    try {
      const data = await $fetch<EventCardDTO[]>(
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

  function selectEvent(eventId: string) {   // ← new
    selectedEventId.value = eventId;
  }

  return {
    currentEvent,
    relatedEvents,
    selectedEventId,
    fetchEventDetail,
    fetchRelatedEvents,
    selectEvent,
  };
});