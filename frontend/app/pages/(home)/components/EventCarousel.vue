<template>
  <section class="position-relative">
    <div class="carousel-track row g-3 flex-nowrap m-0" ref="trackRef" @scroll="handleScroll">
      <!-- We add padding-right directly here or just let the track scroll.  g-3 adds margins so we should add padding or let padding-bottom handle overflow. Note that `m-0` cancels negative margins if we don't want the track to overflow parent sideways. Actually `row g-3` has negative margins. Let's keep `row g-3` but inside a wrapper or just allow the scroll container to be the track. -->
      <div
        v-for="(item, index) in items"
        :key="item.id || index"
        :class="colClass"
        class="carousel-slide flex-shrink-0"
      >
        <slot :item="item" />
      </div>
    </div>

    <button
      v-if="canGoPrev && !isMobile"
      class="carousel-nav-btn prev"
      @click="goPrev"
      aria-label="Previous"
    >
      &lt;
    </button>

    <button
      v-if="canGoNext && !isMobile"
      class="carousel-nav-btn next"
      @click="goNext"
      aria-label="Next"
    >
      &gt;
    </button>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';

interface Props {
  items: any[];
  itemsPerPage?: number;
  colClass?: string;
  showDots?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  itemsPerPage: 4,
  colClass: 'col-lg-3 col-md-4 col-sm-6',
  showDots: false,
});

const isMobile = ref(false);
const trackRef = ref<HTMLElement | null>(null);

const maxScrollLeft = ref(0);
const scrollLeft = ref(0);

const canGoPrev = computed(() => scrollLeft.value > 0);
// Small threshold for visual rounding on varying pixel densities
const canGoNext = computed(() => scrollLeft.value < maxScrollLeft.value - 2);

const updateViewport = () => {
  if (typeof window !== 'undefined') {
    isMobile.value = window.innerWidth < 768;
    updateScrollState();
  }
};

const updateScrollState = () => {
  if (trackRef.value) {
    scrollLeft.value = trackRef.value.scrollLeft;
    // max scroll left is scrollWidth minus clientWidth
    maxScrollLeft.value = trackRef.value.scrollWidth - trackRef.value.clientWidth;
  }
};

const handleScroll = () => {
  updateScrollState();
};

const scrollByAmount = () => {
  if (!trackRef.value) return 0;
  return trackRef.value.clientWidth;
};

const goPrev = () => {
  if (!trackRef.value) return;
  trackRef.value.scrollBy({ left: -scrollByAmount(), behavior: 'smooth' });
};

const goNext = () => {
  if (!trackRef.value) return;
  trackRef.value.scrollBy({ left: scrollByAmount(), behavior: 'smooth' });
};

// If items change, we need to update maxScrollState
watch(
  () => props.items,
  () => {
    setTimeout(updateScrollState, 150);
  },
  { deep: true },
);

onMounted(() => {
  updateViewport();
  setTimeout(updateScrollState, 100);
  window.addEventListener('resize', updateViewport);
});

onUnmounted(() => {
  if (typeof window !== 'undefined') {
    window.removeEventListener('resize', updateViewport);
  }
});
</script>

<style scoped>
section {
  position: relative;
}

.carousel-track {
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
  scroll-behavior: smooth;
  -webkit-overflow-scrolling: touch; /* Momentum scrolling on iOS */
  padding-bottom: 10px; /* Prevent box-shadow or content clipping */
}

/* Include negative margins of bootstrap's row to avoid horizontal scrollbar on body
   Wait, if carousel-track is overflow-x: auto, the negative margin of `row` might cause issue.
   To fix, we can ensure the negative margins are handled, but we use them so grid col sizes work correctly.
 */
.carousel-track {
  margin-left: 0;
  margin-right: 0;
}

.carousel-track::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

.carousel-slide {
  scroll-snap-align: start;
  scroll-snap-stop: always;
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
  left: -20px;
}

.carousel-nav-btn.next {
  right: -20px;
}

/* Adjust button placement for smaller screens or normal container */
@media (max-width: 1200px) {
  .carousel-nav-btn.prev {
    left: 0;
  }
  .carousel-nav-btn.next {
    right: 0;
  }
}

.carousel-indicators-dots {
  position: absolute;
  bottom: 15px;
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
  background-color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: background-color 0.3s;
  border: none;
  padding: 0;
}

.carousel-indicators-dots .dot:hover {
  background-color: rgba(255, 255, 255, 0.8);
}

.carousel-indicators-dots .dot.active {
  background-color: rgba(255, 255, 255, 1);
}
</style>
