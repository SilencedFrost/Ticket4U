<script setup lang="ts">
import type { OrganizerSummary } from '~/types/organizer';

const props = defineProps<{
  organizerId: string | null;
}>();

const config = useRuntimeConfig();

const { data: organizer } = await useFetch<OrganizerSummary>(
  () => `/public/organizers/${props.organizerId}`,
  {
    baseURL: config.public.userServiceUrl,
    key: `organizer-${props.organizerId}`,
  },
);
</script>

<template>
  <section
    v-if="organizer"
    id="organizer-section"
    class="card p-3 bg-reactive-secondary overflow-hidden mx-auto mw-100"
  >
    <div class="mb-3 p-3">
      <h5 class="text-primary fw-bold mb-0 pb-2 border-bottom">
        {{ $t('common.organizer') }}
      </h5>
      <div
        class="d-flex flex-column flex-md-row align-items-center pt-3 align-items-md-start gap-3 w-100"
      >
        <div class="flex-shrink-0 text-center w-md-auto">
          <img
            v-img-fallback="[400, 400]"
            :src="organizer?.logo_url"
            alt="Organizer"
            class="img-fluid rounded-3 border shadow-sm w-100"
          />
        </div>

        <div class="flex-grow-1 text-md-start">
          <h4 class="text-reactive-primary fw-bold mb-2 fs-5">
            {{ organizer?.name }}
          </h4>
          <p class="text-reactive-primary lh-base small mb-0">
            {{ organizer?.description }}
          </p>
        </div>
      </div>
    </div>
  </section>
</template>
