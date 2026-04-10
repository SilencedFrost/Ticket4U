<script setup lang="ts" generic="T">
import { useSwipe } from '@vueuse/core';

const props = withDefaults(
  defineProps<{
    items: T[];
    mode?: 'page' | 'carousel';
    wrapAround?: boolean;
    visibleCount?: number;
    chevronOffset?: number;
    chevronSize?: number;
    chevronInset?: number;
    animationDuration?: number;
  }>(),
  {
    visibleCount: 1,
    mode: 'carousel',
    wrapAround: true,
    chevronOffset: 100,
    chevronSize: 27,
    chevronInset: 10,
    animationDuration: 150,
  },
);

defineSlots<{
  item(props: { item: T }): VNode[];
}>();

const currentIndex = ref<number>(0);
const displayedIndex = ref<number>(0);
const container = ref(null);

const maxIndex = computed<number>(() => {
  if (props.mode === 'page') {
    return Math.ceil(props.items.length / props.visibleCount) - 1;
  } else {
    return props.items.length - props.visibleCount;
  }
});

const direction = ref<'next' | 'prev' | null>(null);
const isAnimating = ref<boolean>(false);

const activeArray = computed<T[]>(() => {
  if (props.mode === 'page') {
    const offset = displayedIndex.value * props.visibleCount;
    return props.items.slice(offset, offset + props.visibleCount);
  } else {
    const start = displayedIndex.value;
    const end = start + props.visibleCount;

    if (end <= props.items.length) {
      return props.items.slice(start, end);
    } else if (props.wrapAround) {
      const tail = props.items.slice(start);
      const head = props.items.slice(0, end - props.items.length);
      return [...tail, ...head];
    } else {
      return props.items.slice(start);
    }
  }
});

const prevItems = computed<T[]>(() => {
  if (props.mode !== 'carousel') return [];

  const result: T[] = [];
  const len = props.items.length;

  for (let i = 1; i <= 2; i++) {
    const idx = displayedIndex.value - i;

    let item;
    if (idx < 0) {
      if (props.wrapAround) {
        item = props.items[(idx + len) % len];
      } else {
        break;
      }
    } else {
      item = props.items[idx];
    }
    if (item !== undefined) result.unshift(item);
  }

  return result;
});

const nextItems = computed<T[]>(() => {
  if (props.mode !== 'carousel') return [];

  const result: T[] = [];
  const len = props.items.length;

  for (let i = 1; i <= 2; i++) {
    const idx = displayedIndex.value + props.visibleCount + (i - 1);

    let item;
    if (idx >= len) {
      if (props.wrapAround) {
        item = props.items[idx % len];
      } else {
        break;
      }
    } else {
      item = props.items[idx];
    }
    if (item !== undefined) result.push(item);
  }

  return result;
});

function next() {
  if (isAnimating.value) return;

  if (props.mode === 'carousel') {
    direction.value = 'next';
    isAnimating.value = true;
  }

  if (props.mode === 'page') {
    const targetIndex = Math.min(currentIndex.value + 1, maxIndex.value);
    currentIndex.value = targetIndex;
    displayedIndex.value = targetIndex;
  } else if (props.wrapAround) {
    currentIndex.value = (currentIndex.value + 1) % props.items.length;
  } else {
    currentIndex.value = Math.min(currentIndex.value + 1, maxIndex.value);
  }
}

function prev() {
  if (isAnimating.value) return;

  if (props.mode === 'carousel') {
    direction.value = 'prev';
    isAnimating.value = true;
  }

  if (props.mode === 'page') {
    const targetIndex = Math.max(currentIndex.value - 1, 0);
    currentIndex.value = targetIndex;
    displayedIndex.value = targetIndex;
  } else if (props.wrapAround) {
    currentIndex.value = (currentIndex.value - 1 + props.items.length) % props.items.length;
  } else {
    currentIndex.value = Math.max(currentIndex.value - 1, 0);
  }
}

const { direction: swipeDir } = useSwipe(container, {
  onSwipeEnd() {
    if (swipeDir.value === 'left') next();
    if (swipeDir.value === 'right') prev();
  },
});

defineExpose({ next, prev, currentIndex, maxIndex });

const gridStyle = computed(() => ({
  display: 'grid',
  gridTemplateColumns: `repeat(${props.visibleCount}, 1fr)`,
  gap: '0.5rem',
}));

const trackStyle = computed(() => ({
  '--animation-duration': `${props.animationDuration}ms`,
  '--step': `${33.333 / props.visibleCount}%`,
}));
</script>

<template>
  <div
    v-if="items.length"
    ref="container"
    class="carousel-container d-flex"
    style="overflow: hidden"
  >
    <div
      class="arrow-container"
      :style="{
        marginRight: `-${chevronInset}px`,
        paddingBottom: chevronOffset > 0 ? `${chevronOffset}px` : 0,
        paddingTop: chevronOffset < 0 ? `${chevronOffset * -1}px` : '',
        visibility: currentIndex <= 0 && (!wrapAround || mode === 'page') ? 'hidden' : 'visible',
      }"
    >
      <i
        class="bi bi-chevron-left shadow-sm bg-reactive-primary rounded-pill text-clickable text-reactive-primary"
        :style="{ fontSize: `${chevronSize}pt` }"
        @click="prev()"
      />
    </div>

    <div v-if="mode === 'carousel'" class="carousel-viewport flex-grow-1">
      <div class="carousel-mask-container">
        <div
          class="carousel-track"
          :class="[
            mode === 'carousel'
              ? direction === 'next'
                ? 'slide-next'
                : direction === 'prev'
                  ? 'slide-prev'
                  : ''
              : '',
          ]"
          :style="trackStyle"
          @animationend="
            displayedIndex = currentIndex;
            isAnimating = false;
            direction = null;
          "
        >
          <!-- prev preload (carousel only) -->
          <div
            v-if="mode === 'carousel'"
            class="carousel-preload carousel-preload--prev"
            :style="gridStyle"
          >
            <div v-for="i in visibleCount - prevItems.length" :key="i"></div>
            <div v-for="(item, index) in prevItems" :key="index">
              <slot v-if="item" name="item" :item="item" />
            </div>
          </div>

          <!-- active -->
          <div class="carousel-active" :style="gridStyle">
            <div v-for="(item, index) in activeArray" :key="index">
              <slot v-if="item" name="item" :item="item" />
            </div>
          </div>

          <!-- next preload (carousel only) -->
          <div
            v-if="mode === 'carousel'"
            class="carousel-preload carousel-preload--next"
            :style="gridStyle"
          >
            <div v-for="(item, index) in nextItems" :key="index">
              <slot v-if="item" name="item" :item="item" />
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="page-viewport flex-grow-1">
      <div :style="gridStyle">
        <div v-for="(item, index) in activeArray" :key="index">
          <slot v-if="item" name="item" :item="item" />
        </div>
      </div>
    </div>

    <div
      class="arrow-container"
      :style="{
        marginLeft: `-${chevronInset}px`,
        paddingBottom: chevronOffset > 0 ? `${chevronOffset}px` : 0,
        paddingTop: chevronOffset < 0 ? `${chevronOffset * -1}px` : '',
        visibility:
          currentIndex >= maxIndex && (!wrapAround || mode === 'page') ? 'hidden' : 'visible',
      }"
    >
      <i
        class="bi bi-chevron-right shadow-sm bg-reactive-primary rounded-pill text-clickable text-reactive-primary"
        :style="{ fontSize: `${chevronSize}pt` }"
        @click="next()"
      />
    </div>
  </div>

  <div v-else class="alternate-message"></div>
</template>

<style scoped>
.arrow-container {
  display: flex;
  align-items: center;
  position: relative;
  z-index: 2;
}

.carousel-viewport {
  overflow: visible;
  position: relative;
  min-width: 0;
}

.carousel-mask-container {
  margin-left: -20px;
  margin-right: -20px;

  padding-left: 20px;
  padding-right: 20px;

  -webkit-mask-image: linear-gradient(
    to right,
    transparent 0%,
    black 30px,
    black calc(100% - 30px),
    transparent 100%
  );
  mask-image: linear-gradient(
    to right,
    transparent 0%,
    black 30px,
    black calc(100% - 30px),
    transparent 100%
  );
}

.carousel-track {
  display: flex;
  flex-direction: row;
  width: 300%;
  transform: translateX(-33.333%);
  margin-left: -0.75rem;
}

.carousel-preload--prev,
.carousel-active,
.carousel-preload--next {
  width: 33.333%;
  flex-shrink: 0;
  margin-left: 0.25rem;
  margin-right: 0.25rem;
}

.carousel-track.slide-next {
  animation: slide-to-next var(--animation-duration) ease-out forwards;
}

.carousel-track.slide-prev {
  animation: slide-to-prev var(--animation-duration) ease-out forwards;
}

@keyframes slide-to-next {
  from {
    transform: translateX(-33.333%);
  }
  to {
    transform: translateX(calc(-33.333% - var(--step)));
  }
}

@keyframes slide-to-prev {
  from {
    transform: translateX(-33.333%);
  }
  to {
    transform: translateX(calc(-33.333% + var(--step)));
  }
}
</style>
