export function useCategoryApi() {
  async function fetchPublicCategories(eventServiceUrl: string): Promise<CategorySummary[]> {
    return await $fetch<CategorySummary[]>(`${eventServiceUrl}/public/categories`, {
      credentials: 'include',
    });
  }

  return {
    fetchPublicCategories,
  };
}
