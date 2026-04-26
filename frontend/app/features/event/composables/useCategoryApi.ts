import type { UseFetchOptions } from '#app';
import type { HttpMethod } from '~/types/http-method';
import type { CategoryWithEvent } from '../types/Category';

export const useCategoryApi = () => {
  const config = useRuntimeConfig();
  const api = config.public.eventServiceUrl;
  const userStore = useUserStore();

  function useCategoriesWithEvents(
    url: string,
    key: string,
    query: UseFetchOptions<CategoryWithEvent[]>['query'],
    method: HttpMethod = 'GET',
  ) {
    return useFetch<CategoryWithEvent[]>(`${api}/public/categories${url ? '/' + url : ''}`, {
      default: () => [],
      key,
      query,
      method,
    });
  }

  function useRecommendedCategories(limit: number = 3) {
    return useCategoriesWithEvents(
      'recommended',
      `categories-recommended-${userStore.user.id ?? null}-${limit}`,
      { limit },
    );
  }

  return { useRecommendedCategories };
};
