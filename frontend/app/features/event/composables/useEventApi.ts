import type { EventSummary } from '../types/Event';
import type { UseFetchOptions } from '#app';
import type { HttpMethod } from '~/types/http-method';

export const useEventApi = () => {
  const config = useRuntimeConfig();
  const logger = useLogger();
  const api = config.public.eventServiceUrl;

  async function getEvent(
    url: string,
    query: UseFetchOptions<EventSummary[]>['query'],
    method: HttpMethod = 'GET',
  ): Promise<EventSummary[]> {
    try {
      return await $fetch(`${api}/public/events${url ? '/' + url : ''}`, {
        query,
        method,
      });
    } catch (e) {
      logger.error(e);
    }
    return [];
  }

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

  async function getLocationEvents(
    longitude: number | null,
    latitude: number | null,
  ): Promise<EventSummary[]> {
    if (longitude && latitude) return await getEvent('locational', { longitude, latitude }, 'GET');
    return [];
  }

  return { useFeaturedEvents, getLocationEvents };
};
