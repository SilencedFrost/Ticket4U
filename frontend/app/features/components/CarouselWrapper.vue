<script setup lang="ts" generic="T">
import { useSwipe } from '@vueuse/core';

interface ChevronConfig {
  height?: number;
  offset?: number;
  inset?: number;
  opacity?: number;
}

interface Props {
  items: T[];
  mode?: 'page' | 'carousel';
  wrapAround?: boolean;
  visibleCount?: number;
  animationDuration?: number;
  defaultHeight?: number;
  chevronOptions?: ChevronConfig;
}

const props = withDefaults(defineProps<Props>(), {
  visibleCount: 1,
  mode: 'carousel',
  wrapAround: true,
  animationDuration: 150,
  defaultHeight: 200,
  chevronOptions: () => ({}),
});

const chevronDefaults = {
  height: 55,
  offset: 0,
  inset: 10,
  opacity: 50,
};

// props.chevronOptions is the external props name, chevronConfig is the sanitized internal use name
const chevronConfig = computed(() => {
  return {
    height: props.chevronOptions?.height ?? chevronDefaults.height,
    offset: Math.min(100, Math.max(-100, props.chevronOptions?.offset ?? chevronDefaults.offset)),
    inset: props.chevronOptions?.inset ?? chevronDefaults.inset,
    opacity: props.chevronOptions?.opacity ?? chevronDefaults.opacity,
  };
});

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

watch(maxIndex, (newMax) => {
  if (!props.wrapAround && currentIndex.value > newMax) {
    const safeIndex = Math.max(0, newMax);
    currentIndex.value = safeIndex;
    displayedIndex.value = safeIndex;
  }
});
</script>

<template>
  <div
    v-if="items && items.length > 0"
    ref="container"
    class="carousel-container d-flex"
    style="overflow: hidden"
  >
    <div
      class="arrow-container"
      :style="{
        marginRight: `-${chevronConfig.inset}px`,
        visibility: currentIndex <= 0 && (!wrapAround || mode === 'page') ? 'hidden' : 'visible',
        opacity: `${chevronConfig.opacity}%`,
      }"
    >
      <i
        class="bi bi-chevron-left shadow-sm bg-reactive-primary rounded-pill text-clickable text-reactive-primary d-flex align-items-center"
        :style="{
          fontSize: `${chevronConfig.height - 22}px`,
          position: `relative`,
          height: `${chevronConfig.height}px`,
          top: `calc(${(100 - chevronConfig.offset) / 2}% + ${(chevronConfig.height * chevronConfig.offset) / 200}px)`,
        }"
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
        marginLeft: `-${chevronConfig.inset}px`,
        visibility:
          currentIndex >= maxIndex && (!wrapAround || mode === 'page') ? 'hidden' : 'visible',
        opacity: `${chevronConfig.opacity}%`,
      }"
    >
      <i
        class="bi bi-chevron-right shadow-sm bg-reactive-primary rounded-pill text-clickable text-reactive-primary d-flex align-items-center"
        :style="{
          fontSize: `${chevronConfig.height - 22}px`,
          position: `relative`,
          height: `${chevronConfig.height}px`,
          top: `calc(${(100 - chevronConfig.offset) / 2}% + ${(chevronConfig.height * chevronConfig.offset) / 200}px)`,
        }"
        @click="next()"
      />
    </div>
  </div>

  <div
    v-else
    class="alternate-message text-center text-reactive-secondary pt-5"
    :style="{ minHeight: `${defaultHeight}px` }"
  >
    <i class="bi bi-box2-fill" style="font-size: 80px" />
    <h2>Nothing here</h2>
  </div>
</template>

<style scoped>
.arrow-container {
  position: relative;
  z-index: 2;
  transition: opacity 0.3s ease;

  i {
    transform: translateY(-50%);
  }

  &:hover {
    opacity: 100% !important;
  }
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
