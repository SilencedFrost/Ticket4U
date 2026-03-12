<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import type { Event } from '~/pages/(home)/types/home';

interface Props {
  events: Event[];
}

const props = defineProps<Props>();
const currentSlide = ref(0);
const isMobile = ref(false);
const trackRef = ref<HTMLElement | null>(null);

const itemsPerSlide = computed(() => (isMobile.value ? 1 : 2));
const totalSlides = computed(() => Math.ceil(props.events.length / itemsPerSlide.value));

// Create cloned elements to fake the infinite loop
const loopPadding = computed(() => Math.max(1, itemsPerSlide.value));

const loopedEvents = computed(() => {
  if (props.events.length === 0) return [];
  const startClones = props.events.slice(-loopPadding.value);
  const endClones = props.events.slice(0, loopPadding.value);
  return [...startClones, ...props.events, ...endClones];
});

const isTouching = ref(false);

const updateViewport = () => {
  if (typeof window !== 'undefined') {
    isMobile.value = window.innerWidth < 768;
    // Debounce resetting to ensure elements are ready
    setTimeout(() => {
      resetScrollToRealIndex(currentSlide.value);
    }, 100);
  }
};

const resetScrollToRealIndex = (index: number) => {
  if (!trackRef.value) return;
  const childIndex = index * itemsPerSlide.value + loopPadding.value;
  const child = trackRef.value.children[childIndex] as HTMLElement;
  if (child) {
    trackRef.value.scrollTo({
      left: child.offsetLeft,
      behavior: 'instant' as ScrollBehavior, // Instantly jump behind the scenes
    });
  }
};

const scrollToSlide = (index: number) => {
  if (!trackRef.value) return;
  // Calculate which physical DOM child to scroll to (accounting for padding)
  let virtualIndex = index * itemsPerSlide.value + loopPadding.value;

  // If moving out of bounds via Next/Prev buttons, temporarily go into the clones
  if (index >= totalSlides.value) {
    virtualIndex = props.events.length + loopPadding.value;
  } else if (index < 0) {
    virtualIndex = loopPadding.value - itemsPerSlide.value;
  }

  const child = trackRef.value.children[virtualIndex] as HTMLElement;
  if (child) {
    trackRef.value.scrollTo({
      left: child.offsetLeft,
      behavior: 'smooth',
    });
  }
};

const goPrev = () => {
  let tempIndex = currentSlide.value - 1;
  scrollToSlide(tempIndex);

  if (tempIndex < 0) {
    setTimeout(() => {
      currentSlide.value = totalSlides.value - 1;
      resetScrollToRealIndex(currentSlide.value);
    }, 500); // 500ms allows the smooth CSS scroll animation to finish before jumping
  } else {
    currentSlide.value = tempIndex;
  }
};

const goNext = () => {
  let tempIndex = currentSlide.value + 1;
  scrollToSlide(tempIndex);

  if (tempIndex >= totalSlides.value) {
    setTimeout(() => {
      currentSlide.value = 0;
      resetScrollToRealIndex(currentSlide.value);
    }, 500);
  } else {
    currentSlide.value = tempIndex;
  }
};

const goToSlide = (index: number) => {
  currentSlide.value = index;
  scrollToSlide(index);
};

const handleTouchStart = () => {
  isTouching.value = true;
};

const handleTouchEnd = () => {
  isTouching.value = false;
  // Check if we need to snap back to real content after scrolling into clones
  checkAndResetBoundary();
};

const checkAndResetBoundary = () => {
  if (!trackRef.value) return;
  const scrollLeft = trackRef.value.scrollLeft;
  const trackWidth = trackRef.value.clientWidth;
  // Virtual slide based on physical position
  const newVirtualSlide = Math.round(scrollLeft / trackWidth);

  // Total physical slides rendered
  const totalVirtualSlides = Math.ceil(loopedEvents.value.length / itemsPerSlide.value);

  // Bounds checking
  if (newVirtualSlide <= 0 && scrollLeft <= 0) {
    // Scrolled into left clones
    currentSlide.value = totalSlides.value - 1;
    resetScrollToRealIndex(currentSlide.value);
  } else if (newVirtualSlide >= totalVirtualSlides - 1) {
    // Scrolled into right clones
    currentSlide.value = 0;
    resetScrollToRealIndex(currentSlide.value);
  } else {
    // Safe middle area
    currentSlide.value = newVirtualSlide - Math.ceil(loopPadding.value / itemsPerSlide.value);
  }
};

let scrollTimeout: ReturnType<typeof setTimeout> | null = null;
const handleScroll = () => {
  if (!trackRef.value || isTouching.value) return;
  if (scrollTimeout) clearTimeout(scrollTimeout);

  scrollTimeout = setTimeout(() => {
    checkAndResetBoundary();
  }, 100);
};

onMounted(() => {
  updateViewport();
  window.addEventListener('resize', updateViewport);
  // Initial scroll positioning
  setTimeout(() => resetScrollToRealIndex(0), 50);
});

onUnmounted(() => {
  if (typeof window !== 'undefined') {
    window.removeEventListener('resize', updateViewport);
  }
});
</script>

<template>
  <section class="mb-5 position-relative">
    <div
      class="carousel-track d-flex"
      ref="trackRef"
      @scroll="handleScroll"
      @touchstart="handleTouchStart"
      @touchend="handleTouchEnd"
    >
      <div
        v-for="(event, idx) in loopedEvents"
        :key="`event-${event.id}-${idx}`"
        class="carousel-slide flex-shrink-0"
      >
        <NuxtLinkLocale
          :to="`/event-detail/${event.id}`"
          class="event-card-large position-relative rounded-4 overflow-hidden d-block w-100"
        >
          <img :src="event.bannerUrl" :alt="event.name" class="w-100 h-100 object-fit-cover" />
        </NuxtLinkLocale>
      </div>
    </div>

    <button
      v-if="totalSlides > 1 && !isMobile"
      class="carousel-nav-btn prev"
      aria-label="Previous"
      @click="goPrev"
    >
      &#10094;
    </button>

    <button
      v-if="totalSlides > 1 && !isMobile"
      class="carousel-nav-btn next"
      aria-label="Next"
      @click="goNext"
    >
      &#10095;
    </button>

    <div v-if="totalSlides > 1" class="carousel-indicators-dots">
      <button
        v-for="(_, index) in totalSlides"
        :key="index"
        class="dot"
        :class="{ active: currentSlide === index }"
        :aria-label="`Go to slide ${index + 1}`"
        @click="goToSlide(index)"
      />
    </div>
  </section>
</template>

<style scoped>
.carousel-track {
  gap: 1rem;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
  position: relative;
  scroll-behavior: smooth;
  -webkit-overflow-scrolling: touch; /* Momentum scrolling on iOS */
  padding-bottom: 5px; /* Prevent box-shadow or content clipping on mobile */
}

/* Ensure completely invisible scrollbar across all webkit browsers */
.carousel-track::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

.carousel-slide {
  flex: 0 0 100%;
  scroll-snap-align: center; /* Center snap feels much more natural on mobile than start snap */
  scroll-snap-stop: always; /* Force snap to stop at each item, preventing "flying over" multiple items */
}

@media (min-width: 768px) {
  .carousel-slide {
    flex: 0 0 calc(50% - 0.5rem);
    scroll-snap-align: start; /* Revert back to start snap on desktop where we show 2 items */
  }
}

.event-card-large {
  width: 100%;
  aspect-ratio: 807 / 460;
  cursor: pointer;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

/* Active touch feedback for mobile users */
.event-card-large:active {
  transform: scale(0.98);
}

@media (max-width: 767.98px) {
  .event-card-large {
    aspect-ratio: 4 / 3;
  }
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
  background-color: #07b3df;
}
</style>
