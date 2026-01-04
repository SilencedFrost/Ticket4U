import { ref, computed } from 'vue'

export function useCarousel<T>(items: T[], itemsPerPage = 4) {
  const currentIndex = ref(0)

  const totalPages = computed(() => Math.ceil(items.length / itemsPerPage))

  const canGoPrev = computed(() => currentIndex.value > 0)

  const canGoNext = computed(() => currentIndex.value < totalPages.value - 1)

  const visibleItems = computed(() => {
    const start = currentIndex.value * itemsPerPage
    return items.slice(start, start + itemsPerPage)
  })

  const goNext = () => {
    if (canGoNext.value) currentIndex.value++
  }

  const goPrev = () => {
    if (canGoPrev.value) currentIndex.value--
  }

  const goToPage = (index: number) => {
    if (index >= 0 && index < totalPages.value) {
      currentIndex.value = index
    }
  }

  return {
    currentIndex,
    totalPages,
    canGoPrev,
    canGoNext,
    visibleItems,
    goNext,
    goPrev,
    goToPage,
  }
}
