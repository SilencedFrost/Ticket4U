<script setup lang="ts">
defineProps<{
  eventData: any
}>();
</script>

<template>
    <section class="py-5">
      <div class="container-xxl">
        <h2 class="text-reactive-primary text-center fw-bold mb-5 related-title">{{ $t('event-detail.maybe_you_like') }}</h2>

        <div class="row g-3 mb-5 d-flex">
          <div v-for="event in eventData.relatedEvents" :key="event.id" class="col-6 col-lg-3">
            <div class="card bg-reactive-secondary rounded-3 h-100 overflow-hidden">
              <img :src="event.image" alt="Event" class="card-img-top object-fit-cover h-auto" />
              <div class="d-flex flex-column p-3">
                <p class="text-reactive-primary fw-semibold mb-2 fs-6 lh-sm">{{ event.title }}</p>
                <div class="mt-auto">
                  <p class="text-info fw-bold mb-0 fs-6">{{ $t('event-detail.price_from') }} {{ event.price }} đ</p>
                  <div class="d-flex align-items-center gap-2 mb-2">
                    <i class="bi bi-calendar text-info small"></i>
                    <span class="text-reactive-primary small">{{ $d(new Date(event.date), 'short') }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="text-center">
          <button class="btn btn-info fw-bold px-4 py-2">{{ $t('event-detail.view_more_events') }}</button>
        </div>
      </div>
    </section>
</template>