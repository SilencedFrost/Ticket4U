<script setup lang="ts">
import type { NuxtError } from '#app';
import type { PropType } from 'vue';

const props = defineProps({
  error: {
    type: Object as PropType<NuxtError>,
    required: true,
  },
});

const localePath = useLocalePath();

const isNotFound = computed(() => props.error?.statusCode === 404);

const titleKey = computed(() =>
  isNotFound.value ? 'common.error.not_found_title' : 'common.error.generic_title',
);

const descriptionKey = computed(() =>
  isNotFound.value ? 'common.error.not_found_description' : 'common.error.generic_description',
);

const handleBackHome = () => {
  clearError({ redirect: localePath('/') });
};
</script>

<template>
  <main class="min-vh-100 d-flex align-items-center justify-content-center bg-reactive-primary p-3">
    <div
      class="card bg-reactive-secondary border-0 shadow-sm p-4 p-md-5 text-center"
      style="max-width: 620px; width: 100%"
    >
      <h1 class="text-reactive-primary mb-2">{{ $t(titleKey) }}</h1>
      <p class="text-reactive-secondary mb-4">{{ $t(descriptionKey) }}</p>
      <div class="d-flex justify-content-center gap-2">
        <button type="button" class="btn btn-primary" @click="handleBackHome">
          {{ $t('common.error.back_home') }}
        </button>
      </div>
    </div>
  </main>
</template>
