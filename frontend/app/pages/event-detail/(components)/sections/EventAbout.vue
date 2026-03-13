<script setup lang="ts">
import DOMPurify from 'isomorphic-dompurify';

const props = defineProps<{
  aboutVi: string;
  aboutEn: string;
}>();

const { locale } = useI18n();
const expandAbout = ref(false);
const COLLAPSED_HEIGHT = 250;
const aboutContainerRef = ref<HTMLElement | null>(null);
const animatedMaxHeight = ref(`${COLLAPSED_HEIGHT}px`);

const currentDescription = computed(() => {
  return locale.value === 'vi' ? props.aboutVi : props.aboutEn;
});

const sanitizedDescription = computed(() => {
  return DOMPurify.sanitize(currentDescription.value, {
    ALLOWED_TAGS: [
      'p',
      'br',
      'strong',
      'em',
      'u',
      's',
      'h1',
      'h2',
      'h3',
      'h4',
      'h5',
      'h6',
      'ul',
      'ol',
      'li',
      'blockquote',
      'code',
      'pre',
      'a',
      'img',
      'div',
    ],
    ALLOWED_ATTR: ['href', 'target', 'rel', 'src', 'alt', 'class', 'style'],
  });
});

const updateAnimatedMaxHeight = () => {
  if (!aboutContainerRef.value || !expandAbout.value) {
    animatedMaxHeight.value = `${COLLAPSED_HEIGHT}px`;
    return;
  }
  animatedMaxHeight.value = `${Math.max(aboutContainerRef.value.scrollHeight, COLLAPSED_HEIGHT)}px`;
};

watch([expandAbout, sanitizedDescription], async () => {
  await nextTick();
  updateAnimatedMaxHeight();
});

onMounted(() => {
  updateAnimatedMaxHeight();
});
</script>
<template>
  <section id="about-section" class="p-3 p-md-4 card m-3 mx-auto mw-100">
    <div class="container-xxl">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="text-reactive-primary fw-bold mb-0 h4">
          {{ $t('event_detail.section.about') }}
        </h3>
      </div>
      <div
        class="border-dark overflow-hidden mb-4 about-expandable rounded-2"
        :style="{ maxHeight: animatedMaxHeight }"
      >
        <div ref="aboutContainerRef" class="p-3 p-md-4">
          <div class="text-break w-100" v-html="sanitizedDescription" />
        </div>
      </div>
      <button
        class="btn btn-outline-primary fw-bold mx-auto d-block"
        @click="expandAbout = !expandAbout"
      >
        {{ expandAbout ? $t('common.see.less') : $t('common.see.more') }}
      </button>
    </div>
  </section>
</template>

<style scoped>
.about-expandable {
  max-height: 250px;
  transition: max-height 0.45s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: max-height;
}
</style>
