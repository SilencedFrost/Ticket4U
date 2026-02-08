<template>
  <section 
    class="mb-5 position-relative"
    @touchstart="handleTouchStart"
    @touchmove="handleTouchMove"
    @touchend="handleTouchEnd"
  >
    <div class="row g-3">
      <div
        v-for="(event, idx) in visibleSlides"
        :key="event.id"
        :class="isMobile ? 'col-12' : 'col-md-6'"
      >
        <div class="event-card-large position-relative rounded-4 overflow-hidden">
          <img
            v-img-fallback="[640, 365]"
            :src="event.bannerUrl"
            :alt="event.name"
            class="w-100 h-100 object-fit-cover"
          />
          <button
            class="btn btn-light position-absolute bottom-0 start-0 m-3 rounded-2"
          >
            Xem chi tiết
          </button>
        </div>
      </div>
    </div>

    <button
      v-if="totalSlides > 1 && !isMobile"
      class="carousel-nav-btn prev"
      @click="goPrev"
      aria-label="Previous"
    >
      &lt;
    </button>

    <button
      v-if="totalSlides > 1 && !isMobile"
      class="carousel-nav-btn next"
      @click="goNext"
      aria-label="Next"
    >
      &gt;
    </button>

    <div v-if="totalSlides > 1" class="carousel-indicators-dots">
      <button
        v-for="(_, index) in totalSlides"
        :key="index"
        class="dot"
        :class="{ active: currentSlide === index }"
        @click="goToSlide(index)"
        :aria-label="`Go to slide ${index + 1}`"
      />
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import type { Event } from '~/pages/(home)/types/home'

const { vFallback: vImgFallback } = useImagePlaceholder();

interface Props {
  events: Event[]
}

const props = defineProps<Props>()
const currentSlide = ref(0)
const isMobile = ref(false)

const touchStartX = ref(0)
const touchEndX = ref(0)
const minSwipeDistance = 50

const itemsPerSlide = computed(() => isMobile.value ? 1 : 2)

const totalSlides = computed(() =>
  Math.ceil(props.events.length / itemsPerSlide.value)
)

const visibleSlides = computed(() => {
  const start = currentSlide.value * itemsPerSlide.value
  return props.events.slice(start, start + itemsPerSlide.value)
})

const updateViewport = () => {
  if (typeof window !== 'undefined') {
    isMobile.value = window.innerWidth < 768
  }
}

const handleTouchStart = (e: TouchEvent) => {
  if (e.touches && e.touches[0]) {
    touchStartX.value = e.touches[0].clientX
  }
}

const handleTouchMove = (e: TouchEvent) => {
  if (e.touches && e.touches[0]) {
    touchEndX.value = e.touches[0].clientX
  }
}

const handleTouchEnd = () => {
  const distance = touchStartX.value - touchEndX.value
  const isLeftSwipe = distance > minSwipeDistance
  const isRightSwipe = distance < -minSwipeDistance

  if (isLeftSwipe) {
    goNext()
  } else if (isRightSwipe) {
    goPrev()
  }
}

const goPrev = () => {
  currentSlide.value =
    currentSlide.value === 0
      ? totalSlides.value - 1
      : currentSlide.value - 1
}

const goNext = () => {
  currentSlide.value =
    currentSlide.value === totalSlides.value - 1
      ? 0
      : currentSlide.value + 1
}

const goToSlide = (index: number) => {
  currentSlide.value = index
}

onMounted(() => {
  updateViewport()
  window.addEventListener('resize', updateViewport)
})

onUnmounted(() => {
  if (typeof window !== 'undefined') {
    window.removeEventListener('resize', updateViewport)
  }
})
</script>

<style scoped>
section {
  position: relative;
}

.event-card-large {
  width: 100%;
  aspect-ratio: 807 / 460;
}

@media (max-width: 767.98px) {
  .event-card-large {
    aspect-ratio: 4 / 3;
  }
}

.carousel-nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 70px;
  border-radius: 10px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  font-size: 1.5rem;
  font-weight: bold;
  z-index: 100;
  transition: background-color 0.3s;
  cursor: pointer;
}

.carousel-nav-btn:hover {
  background-color: rgba(0, 0, 0, 0.7);
}

.carousel-nav-btn.prev {
  left: 0;
}

.carousel-nav-btn.next {
  right: 0;
}

.carousel-indicators-dots {
  position: absolute;
  bottom: -25px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 100;
}

.carousel-indicators-dots .dot {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  background-color: var(--bg-reactive-gray);
  cursor: pointer;
  transition: background-color 0.3s;
  border: none;
  padding: 0;
}

.carousel-indicators-dots .dot:hover {
  background-color: var(--bg-reactive-gray-hover);
}

.carousel-indicators-dots .dot.active {
  background-color: #07B3DF;
}
</style>
