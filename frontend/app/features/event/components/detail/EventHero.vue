<script setup lang="ts">
import type { Event } from '@/features/event/types/Event';
const { formatPrice } = useFormatter();

defineProps<{
  event: Event;
}>();

const emit = defineEmits(['buyClick']);
</script>

<template>
  <section class="pt-4 position-relative overflow-hidden min-vh-50">
    <div class="position-absolute top-0 h-100 start-0 end-0 overflow-hidden">
      <img
        :src="event.bannerUrl.wide"
        :alt="event.name"
        class="w-100 h-100 object-fit-cover hero-bg-blur"
      />
    </div>
    <div class="position-relative z-1">
      <div class="container-xxl pb-4">
        <div class="g-3 align-items-start hero-grid">
          <div class="col-12 h-auto">
            <div class="card d-flex flex-column flex-lg-row shadow-lg mx-auto overflow-hidden">
              <div class="card-body p-4 d-flex flex-column order-2 order-lg-1 col-12 col-lg-3">
                <h5 class="text-reactive-primary fw-bold lh-sm mb-3">
                  {{ event.name }}
                </h5>
                <div class="d-flex mb-3 small">
                  <i class="bi bi-calendar-event text-reactive-primary me-1" />
                  <div>
                    <span class="text-primary fw-semibold">
                      {{ $d(new Date(event.startDate), { hour: '2-digit', minute: '2-digit' }) }},
                      {{ $d(new Date(event.startDate), 'short') }}
                    </span>
                  </div>
                </div>
                <div class="d-flex mb-3 text-reactive-secondary small">
                  <i class="bi bi-geo-alt-fill text-reactive-primary me-1" />

                  <div class="d-flex flex-column">
                    <template v-if="event.venue?.name || event.addressLine">
                      <span v-if="event.venue?.name" class="text-primary fw-semibold mb-1">
                        {{ event.venue.name }}
                      </span>
                      <p v-if="event.addressLine" class="mb-0 text-muted extra-small">
                        {{ event.addressLine }}
                      </p>
                    </template>

                    <template v-else>
                      <p class="mb-0 text-primary extra-small italic fw-semibold">
                        Contact organizer for details
                      </p>
                    </template>
                  </div>
                </div>
                <div class="mt-auto">
                  <hr class="bg-reactive-secondary my-2" />
                  <p class="text-reactive-primary fw-semibold mb-1 text-xs">
                    {{ $t('common.currency.from') }}
                  </p>
                  <p class="text-primary fw-bold mb-2 fs-3">{{ formatPrice(event.minPrice) }}</p>
                  <button
                    class="btn btn-primary fw-bold w-100 py-1 small"
                    @click="emit('buyClick')"
                  >
                    {{ $t('common.action.buy') }}
                  </button>
                </div>
              </div>
              <img
                :src="event.bannerUrl.wide"
                :alt="event.name"
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
