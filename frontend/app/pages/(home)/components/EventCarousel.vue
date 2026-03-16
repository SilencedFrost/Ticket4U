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

const buttonTopOffset = ref<number | null>(null);

// Calculate button position based on carousel height
const updateButtonPosition = () => {
  if (!trackRef.value) return;

  // Get the parent element of the track (the section) to calculate relative position
  const sectionEl = trackRef.value.parentElement;
  // Find the first image element
  const firstImage = trackRef.value.querySelector('img');

  if (sectionEl && firstImage) {
    // Lấy tọa độ thực tế của thẻ section và thẻ ảnh trên màn hình
    const sectionRect = sectionEl.getBoundingClientRect();
    const imgRect = firstImage.getBoundingClientRect();

    // Công thức: (Khoảng cách từ đỉnh section đến đỉnh ảnh) + (Một nửa chiều cao ảnh)
    const exactCenterOffset = imgRect.top - sectionRect.top + imgRect.height / 2;

    if (exactCenterOffset > 0) {
      buttonTopOffset.value = exactCenterOffset;
    }
  }
};
const updateViewport = () => {
  if (typeof window !== 'undefined') {
    isMobile.value = window.innerWidth < 768;
    updateScrollState();
    updateButtonPosition();
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
    setTimeout(() => {
      updateScrollState();
      updateButtonPosition();
    }, 150); // Delay to allow DOM to update with new items
  },
  { deep: true },
);

onMounted(() => {
  updateViewport();
  setTimeout(updateScrollState, 100);
  setTimeout(updateButtonPosition, 500); // Đảm bảo ảnh đã kịp render
  window.addEventListener('resize', updateViewport);
});

onUnmounted(() => {
  if (typeof window !== 'undefined') {
    window.removeEventListener('resize', updateViewport);
  }
});
</script>

<template>
  <section class="position-relative">
    <div class="carousel-track row g-3 flex-nowrap" ref="trackRef" @scroll="handleScroll">
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
      :style="buttonTopOffset ? { top: `${buttonTopOffset}px` } : {}"
      @click="goPrev"
      aria-label="Previous"
    >
      &#10094;
    </button>

    <button
      v-if="canGoNext && !isMobile"
      class="carousel-nav-btn next"
      :style="buttonTopOffset ? { top: `${buttonTopOffset}px` } : {}"
      @click="goNext"
      aria-label="Next"
    >
      &#10095;
    </button>
  </section>
</template>

<style scoped>
@import '../styles/carousel.css';

section {
  position: relative;
}

.carousel-track {
  padding-bottom: 10px; /* Prevent box-shadow or content clipping */
}

/* Include negative margins of bootstrap's row to avoid horizontal scrollbar on body
   Wait, if carousel-track is overflow-x: auto, the negative margin of `row` might cause issue.
   To fix, we can ensure the negative margins are handled, but we use them so grid col sizes work correctly.
 */
/* .carousel-track {
  margin-left: 0;
  margin-right: 0;
} */

.carousel-slide {
  scroll-snap-align: start;
  scroll-snap-stop: always;
}

.carousel-indicators-dots {
  bottom: 15px;
}

.carousel-indicators-dots .dot {
  background-color: rgba(255, 255, 255, 0.5);
}

.carousel-indicators-dots .dot:hover {
  background-color: rgba(255, 255, 255, 0.8);
}

.carousel-indicators-dots .dot.active {
  background-color: rgba(255, 255, 255, 1);
}
</style>
