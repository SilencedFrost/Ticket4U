<script setup lang="ts">
const props = defineProps<{
  aboutVi: string;
  aboutEn: string;
}>();

const { locale } = useI18n();
const expandAbout = ref(false);
const PREVIEW_LENGTH = 420;

const currentDescription = computed(
  () => (locale.value === 'vi' ? props.aboutVi : props.aboutEn) || '',
);

const isLongDescription = computed(() => currentDescription.value.length > PREVIEW_LENGTH);

const displayedDescription = computed(() => {
  if (expandAbout.value || !isLongDescription.value) return currentDescription.value;

  return `${currentDescription.value.slice(0, PREVIEW_LENGTH).trimEnd()}...`;
});

watch(locale, () => {
  expandAbout.value = false;
});
</script>
<template>
  <section id="about-section" class="card m-3 mx-auto overflow-hidden mw-100 pb-4">
    <div class="bg-reactive-gray w-100 p-2 d-flex justify-content-center align-items-center">
      <h5 class="text-reactive-primary fw-bold mb-0">
        {{ $t('event_detail.section.about') }}
      </h5>
    </div>
    <div class="mb-3 p-3 p-md-4">
      <p class="mb-0 text-break lh-lg">
        {{ displayedDescription }}
      </p>
    </div>
    <button
      v-if="isLongDescription"
      class="btn btn-outline-primary fw-bold mx-auto d-block"
      @click="expandAbout = !expandAbout"
    >
      {{ expandAbout ? $t('common.see.less') : $t('common.see.more') }}
    </button>
  </section>
</template>
