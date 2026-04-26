import type { EventSummary } from '../types/Event';
import type { UseFetchOptions } from '#app';
import type { HttpMethod } from '~/types/http-method';

export const useEventApi = () => {
  const config = useRuntimeConfig();
  const api = config.public.eventServiceUrl;

  function useEvent(
    url: string,
    key: string,
    query: UseFetchOptions<EventSummary[]>['query'],
    method: HttpMethod = 'GET',
  ) {
    return useFetch<EventSummary[]>(`${api}/public/events${url ? '/' + url : ''}`, {
      default: () => [],
      key,
      query,
      method,
    });
  }

  function useFeaturedEvents(limit: number = 5) {
    return useEvent('featured', `events-featured-${limit}`, { limit });
  }

  return { useFeaturedEvents };
};
