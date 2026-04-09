<script setup lang="ts" generic="T">
import { computed } from 'vue';

const props = withDefaults(
  defineProps<{
    items: T[];
    mode?: 'page' | 'carousel';
    wrapAround?: boolean;
    visibleCount?: number;
    chevronOffset?: number;
    chevronSize?: number;
    chevronInset?: number;
  }>(),
  {
    visibleCount: 1,
    mode: 'carousel',
    wrapAround: true,
    chevronOffset: 100,
    chevronSize: 30,
    chevronInset: 18,
  },
);

defineSlots<{
  item(props: { item: T }): VNode[];
}>();

const currentIndex = ref<number>(0);

const maxIndex = computed<number>(() => {
  if (props.mode === 'page') {
    return Math.ceil(props.items.length / props.visibleCount) - 1;
  } else {
    return props.items.length - props.visibleCount;
  }
});

const activeArray = computed<T[]>(() => {
  if (props.mode === 'page') {
    const offset = currentIndex.value * props.visibleCount;
    return props.items.slice(offset, offset + props.visibleCount);
  } else {
    const start = currentIndex.value;
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

function next() {
  if (props.mode === 'page') {
    currentIndex.value = Math.min(currentIndex.value + 1, maxIndex.value);
  } else if (props.wrapAround) {
    currentIndex.value = (currentIndex.value + 1) % props.items.length;
  } else {
    currentIndex.value = Math.min(currentIndex.value + 1, maxIndex.value);
  }
}

function prev() {
  if (props.mode === 'page') {
    currentIndex.value = Math.max(currentIndex.value - 1, 0);
  } else if (props.wrapAround) {
    currentIndex.value = (currentIndex.value - 1 + props.items.length) % props.items.length;
  } else {
    currentIndex.value = Math.max(currentIndex.value - 1, 0);
  }
}

defineExpose({ next, prev, currentIndex, maxIndex });

const gridStyle = computed(() => ({
  display: 'grid',
  gridTemplateColumns: `repeat(${props.visibleCount}, 1fr)`,
  gap: '0.5rem',
}));
</script>

<template>
  <div v-if="items.length" class="carousel-container d-flex">
    <div
      class="arrow-container"
      :style="{
        marginRight: `-${chevronInset}px`,
        paddingBottom: chevronOffset > 0 ? `${chevronOffset}px` : 0,
        paddingTop: chevronOffset < 0 ? `${chevronOffset * -1}px` : '',
        visibility: currentIndex === 0 && (!wrapAround || mode === 'page') ? 'hidden' : 'visible',
      }"
    >
      <i
        class="bi bi-chevron-left shadow-sm bg-reactive-primary rounded-pill text-clickable text-reactive-primary"
        :style="{ fontSize: `${chevronSize}pt` }"
        @click="prev()"
      />
    </div>

    <div class="carousel-content flex-grow-1" :style="gridStyle">
      <div v-for="item in activeArray" :key="JSON.stringify(item)">
        <slot name="item" :item="item" />
      </div>
    </div>

    <div
      class="arrow-container"
      :style="{
        marginLeft: `-${chevronInset}px`,
        paddingBottom: chevronOffset > 0 ? `${chevronOffset}px` : 0,
        paddingTop: chevronOffset < 0 ? `${chevronOffset * -1}px` : '',
        visibility:
          currentIndex === maxIndex && (!wrapAround || mode === 'page') ? 'hidden' : 'visible',
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
</style>
