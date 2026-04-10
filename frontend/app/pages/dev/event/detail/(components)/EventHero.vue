<script setup lang="ts">
import type { EventResponse } from '@/features/event/types/Event';
const { formatPrice } = useFormatter();

defineProps<{
  event: EventResponse;
}>();

const emit = defineEmits(['buyClick']);
</script>

<template>
  <section class="pt-4 position-relative overflow-hidden min-vh-50">
    <div class="position-absolute top-0 h-100 start-0 end-0 overflow-hidden">
      <img
        v-img-fallback="[1200, 600]"
        :src="event.bannerUrl.wide"
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
                  {{ event.name }}
                </h5>
                <div class="mb-2 small">
                  <i class="bi bi-calendar-event text-reactive-primary me-1" />
                  <span class="text-primary fw-semibold">
                    {{ event.startDate }},
                    <!-- {{ $d(new Date(event.date), 'short') }} -->
                  </span>
                </div>
                <div class="mb-2 text-reactive-secondary small">
                  <i class="bi bi-geo-alt-fill text-reactive-primary me-1" />
                  <span class="text-primary fw-semibold">{{ event.venue }}</span>
                  <!-- <p class="mb-0 small">{{  }}</p> -->
                </div>
                <div class="mt-auto">
                  <hr class="bg-reactive-secondary my-2" />
                  <p class="text-reactive-primary fw-semibold mb-1 text-xs">
                    {{ $t('common.price.from') }}
                  </p>
                  <p class="text-primary fw-bold mb-2 fs-3">{{ formatPrice(event.minPrice) }}</p>
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
                :src="event.bannerUrl.wide"
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
