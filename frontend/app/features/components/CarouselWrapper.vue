<script setup lang="ts" generic="T">
import { computed } from 'vue';

const props = withDefaults(
  defineProps<{
    items: T[];
    mode?: 'page' | 'carousel';
    wrapAround?: boolean;
    visibleCount?: number;
  }>(),
  {
    visibleCount: 1,
    mode: 'carousel',
    wrapAround: true,
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
  <div v-if="items.length" class="carousel-container">
    <button @click="prev()">prv</button>
    <div class="carousel-content" :style="gridStyle">
      <div v-for="item in activeArray" :key="JSON.stringify(item)">
        <slot name="item" :item="item" />
      </div>
    </div>
    <button @click="next()">nxt</button>
  </div>
  <div v-else class="alternate-message"></div>
</template>
