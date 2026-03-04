<script setup lang="ts">
import DOMPurify from 'isomorphic-dompurify';

const props = defineProps<{
  description: string;
}>();

const expandAbout = ref(false);

const sanitizedDescription = computed(() => {
  return DOMPurify.sanitize(props.description, {
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
    ],
    ALLOWED_ATTR: ['href', 'target', 'rel', 'src', 'alt', 'class', 'style'],
  });
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
        :class="{ expanded: expandAbout }"
      >
        <div class="p-3 p-md-4">
          <div
            class="description-content overflow-auto text-break w-100"
            v-html="sanitizedDescription"
          />
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
  transition: max-height 2s;
}

.about-expandable.expanded {
  max-height: 5000px;
}
</style>
