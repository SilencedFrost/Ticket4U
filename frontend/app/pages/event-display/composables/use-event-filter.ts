import { ref } from 'vue';

export function useEventFilter() {
  const selectedLocation = ref<string>('');
  const isFreeEvent = ref(false);
  const selectedCategories = ref<string[]>([]);

  const toggleCategory = (categoryValue: string) => {
    const index = selectedCategories.value.indexOf(categoryValue);
    if (index > -1) {
      selectedCategories.value.splice(index, 1);
    } else {
      selectedCategories.value.push(categoryValue);
    }
  };

  const reset = () => {
    selectedLocation.value = '';
    isFreeEvent.value = false;
    selectedCategories.value = [];
  };

  const apply = () => {
    // Return filter values for parent to handle
    return {
      location: selectedLocation.value,
      isFree: isFreeEvent.value,
      categories: selectedCategories.value,
    };
  };

  return {
    selectedLocation,
    isFreeEvent,
    selectedCategories,
    toggleCategory,
    reset,
    apply,
  };
}
