<script setup lang="ts">
import type { EventDetailResponse } from '@/pages/event-detail/types/event-detail';
import { useFormatter } from '@/composables/useFormatter';

const { vFallback: vImgFallback } = useImagePlaceholder();
const { formatPrice, formatDateTime } = useFormatter();

defineProps<{
  event: EventDetailResponse;
}>();

const emit = defineEmits(['buyClick']);
</script>

<template>
  <section class="pt-4 position-relative overflow-hidden min-vh-50">
    <div class="position-absolute top-0 h-100 start-0 end-0 overflow-hidden">
      <img
        v-img-fallback="[1200, 600]"
        :src="event.imgEvent.heroUrl"
        class="w-100 h-100 object-fit-cover hero-bg-blur"
      />
    </div>
    <div class="position-relative z-1">
      <div class="container-xxl pb-4">
        <div class="g-3 align-items-start hero-grid">
          <div class="col-12 h-auto">
            <div class="card d-flex flex-column flex-lg-row shadow-lg mx-auto overflow-hidden">
              <div class="card-body p-4 d-flex flex-column order-2 order-lg-1 col-12 col-lg-3">
                <h5 class="text-reactive-primary fw-bold lh-sm mb-2">
                  {{ event.eventTitle }}
                </h5>
                <div class="mb-2 small">
                  <i class="bi bi-calendar-event text-reactive-primary me-1" />
                  <span class="text-primary fw-semibold">
                    {{ formatDateTime(event.startDate).dateTime }}
                  </span>
                </div>
                <div class="mb-2 text-reactive-secondary small">
                  <i class="bi bi-geo-alt-fill text-reactive-primary me-1" />
                  <span class="text-primary fw-semibold">{{ event.address }}</span>
                  <!-- <p class="mb-0 small">{{  }}</p> -->
                </div>
                <div class="mt-auto">
                  <hr class="bg-reactive-secondary my-2" />
                  <p class="text-reactive-primary fw-semibold mb-1 text-xs">
                    {{ event.minPrice === 0 ? '' : $t('common.price.from') }}
                  </p>
                  <p
                    class="fw-bold mb-2 fs-3"
                    :class="event.minPrice === 0 ? 'text-success' : 'text-primary'"
                  >
                    {{ event.minPrice === 0 ? 'Free' : formatPrice(event.minPrice) }}
                  </p>
                  <button
                    class="btn btn-primary text-reactive-primary fw-bold w-100 py-1 small"
                    @click="emit('buyClick')"
                  >
                    {{ $t('common.action.buy') }}
                  </button>
                </div>
              </div>
              <img
                v-img-fallback="[1200, 600]"
                :src="event.imgEvent.heroUrl"
                class="order-1 order-lg-2 col-12 col-lg-9 object-fit-cover shadow-lg my-dashed-line"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.hero-bg-blur {
  filter: blur(8px);
}

.my-dashed-line {
  border-left: 2px solid transparent;
  border-image: repeating-linear-gradient(
      to bottom,
      #5e5e5e,
      20px,
      transparent 20px,
      transparent 40px
    )
    50;
}

@media (max-width: 991px) {
  .my-dashed-line {
    border-left: none;
  }
}
</style>
