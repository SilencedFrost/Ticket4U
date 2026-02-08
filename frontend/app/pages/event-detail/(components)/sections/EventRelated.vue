<script setup lang="ts">
import type { RelatedEvent } from '@/pages/event-detail/types/event-detail';
const { vFallback: vImgFallback } = useImagePlaceholder();
defineProps<{
  eventData: RelatedEvent[];
}>();
</script>

<template>
  <section class="py-5">
    <div class="container-xxl">
      <h2 class="text-reactive-primary text-center fw-bold mb-5 related-title">
        {{ $t('event_detail.section.related') }}
      </h2>

      <div class="row g-3 mb-5 d-flex">
        <div v-for="event in eventData" :key="event.id" class="col-6 col-lg-3">
          <div class="card h-100 overflow-hidden">
            <img
              v-img-fallback="[400, 220]"
              alt="Event"
              :src="event.image"
              class="card-img-top object-fit-cover h-auto"
            />
            <div class="d-flex flex-column p-3">
              <p class="text-reactive-primary fw-semibold mb-2 fs-6 lh-sm">{{ event.title }}</p>
              <div class="mt-auto">
                <p class="text-info fw-bold mb-0 fs-6">
                  {{ $t('event_detail.label.price_from') }} {{ event.price }} đ
                </p>
                <div class="d-flex align-items-center gap-2 mb-2">
                  <i class="bi bi-calendar text-info small" />
                  <span class="text-reactive-primary small">{{
                    $d(new Date(event.date), 'short')
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="text-center">
        <button class="btn btn-primary fw-bold px-4 py-2">
          {{ $t('common.see.more') }}
        </button>
      </div>
    </div>
  </section>
</template>
