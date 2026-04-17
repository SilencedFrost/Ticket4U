import type { EventSummary } from '../types/Event';

export const useEventApi = () => {
  const config = useRuntimeConfig();
  const api = config.public.eventServiceUrl;

  const useFeaturedEvents = (limit: number = 5) => {
    return useFetch<EventSummary[]>(`${api}/public/events/featured`, {
      default: () => [],
      key: `events-featured-${limit}`,
      query: { limit },
      onRequest() {
        console.log('fetching featured events');
      },
    });
  };

  return { useFeaturedEvents };
};
