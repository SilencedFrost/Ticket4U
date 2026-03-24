<template>
    <section class="position-relative" @touchstart="handleTouchStart" @touchmove="handleTouchMove" @touchend="handleTouchEnd">
        <div class="row g-3">
            <div v-for="item in visibleItems" :key="item.id" :class="colClass">
                <slot :item="item" />
            </div>
        </div>

        <button v-if="canGoPrev && !isMobile" class="carousel-nav-btn prev" @click="goPrev" aria-label="Previous">
            &lt;
        </button>

        <button v-if="canGoNext && !isMobile" class="carousel-nav-btn next" @click="goNext" aria-label="Next">
            &gt;
        </button>

        <div v-if="showDots" class="carousel-indicators-dots">
            <button v-for="(_, index) in totalPages" :key="index" class="dot" :class="{ active: currentIndex === index }" @click="goToPage(index)" :aria-label="`Go to page ${index + 1}`" />
        </div>
    </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useCarousel } from '../composables/use-carousel'

interface Props {
    items: any[]
    itemsPerPage?: number
    colClass?: string
    showDots?: boolean
}

const props = withDefaults(defineProps<Props>(), {
    itemsPerPage: 4,
    colClass: 'col-lg-3 col-md-4 col-sm-6',
    showDots: false,
})

const isMobile = ref(false)
const touchStartX = ref(0)
const touchEndX = ref(0)
const minSwipeDistance = 50

const {
    currentIndex,
    totalPages,
    canGoPrev,
    canGoNext,
    visibleItems,
    goNext,
    goPrev,
    goToPage,
} = useCarousel(props.items, props.itemsPerPage)

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

    if (isLeftSwipe && canGoNext.value) {
        goNext()
    } else if (isRightSwipe && canGoPrev.value) {
        goPrev()
    }
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
