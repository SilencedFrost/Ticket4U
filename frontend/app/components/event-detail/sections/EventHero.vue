<script setup lang="ts">
defineProps<{
  eventData: any,
  heroImage: string
}>();

const emit = defineEmits(['buyClick']); 
</script>

<template>
    <section class="pt-4 position-relative overflow-hidden min-vh-50">
      <div class="position-absolute top-0 h-100 start-0 end-0 overflow-hidden">
        <img :src="heroImage" class="w-100 h-100 object-fit-cover hero-bg-blur">
      </div>
      <div class="position-relative z-1">
        <div class="container-xxl pb-4">
            <div class="g-3 align-items-start hero-grid">
                <div class="col-12 h-auto">
                  <div class="card d-flex flex-column flex-md-row bg-reactive-secondary border-dark shadow-lg mx-auto overflow-hidden">
                      <div class="card-body p-4 d-flex flex-column order-2 order-md-1 col-12 col-md-3">
                           <h5 class="text-reactive-primary fw-bold lh-sm mb-2">
                                {{ eventData.title }}
                            </h5>
                            <div class="mb-2 small">
                                <i class="bi bi-calendar text-primary me-1"></i>
                                <span class="text-primary fw-semibold">{{ eventData.time }},
                                  {{ $d(new Date(eventData.date), 'short') }}
                                </span>
                            </div>
                            <div class="mb-2 text-secondary small">
                                <i class="bi bi-geo-alt text-primary me-1"></i>
                                <span class="text-primary fw-semibold">{{ eventData.venue[$i18n.locale] }}</span>
                                <p class="mb-0 small">{{ eventData.address[$i18n.locale] }}</p>
                            </div>
                            <div class="mt-auto"> 
                                <hr class="bg-secondary my-2" />
                                <p class="text-reactive-primary fw-semibold mb-1 text-xs">{{ $t('event-detail.price_from') }}</p>
                                <p class="text-info fw-bold mb-2 fs-6">{{ eventData.startPrice }}</p>
                                <button @click="emit('buyClick')" class="btn btn-info text-reactive-primary fw-bold w-100  py-1 small">{{ $t('event-detail.buy_tickets') }}</button>
                            </div>
                      </div>
                      <img :src="heroImage" class="order-1 order-md-2 col-12 col-md-9 object-fit-cover shadow-lg my-dashed-line" />
                      
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
    #5E5E5E,
    20px,
    transparent 20px,
    transparent 40px
  ) 50;
}

@media (max-width: 768px) {
  .my-dashed-line {
    border-left: none;
  }
}
</style>