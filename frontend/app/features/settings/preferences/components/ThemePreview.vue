<script setup lang="ts">
const props = defineProps<{
  theme: 'dark' | 'light';
}>();

function isLight() {
  return props.theme === 'light';
}

const activeTheme = computed(() => {
  return {
    bgPrimary: `var(--bg-primary-${isLight() ? 'light' : 'dark'})`,
    bgReactive: `var(--bg-${isLight() ? 'secondary-light' : 'primary-dark'})`,
    textPrimary: `var(--text-primary-${isLight() ? 'light' : 'dark'})`,
    textSecondary: `var(--text-secondary-${isLight() ? 'light' : 'dark'})`,
    cardBackground: `var(--bg-${isLight() ? 'primary-light' : 'secondary-dark'})`,
    borderSubtle: `1px solid color-mix(in srgb, var(--text-reactive-secondary) 50%, transparent)`,
    border: `1px solid var(--text-reactive-secondary)`,
  };
});

const textPreviews = [
  { width: '45%', background: 'var(--bs-primary)' },
  { width: '30%', background: activeTheme.value.textPrimary },
  { width: '40%', background: activeTheme.value.textSecondary },
  { width: '20%', background: activeTheme.value.textSecondary },
];
</script>

<template>
  <div
    class="theme-preview rounded-3 overflow-hidden"
    :style="{ backgroundColor: activeTheme.bgReactive, border: activeTheme.borderSubtle }"
  >
    <div
      :class="['preview-navbar', 'p-1', isLight() ? 'shadow-sm' : '']"
      :style="{
        backgroundColor: activeTheme.bgPrimary,
        height: `14%`,
        borderBottom: isLight() ? 'none' : activeTheme.border,
      }"
    >
      <div class="bg-primary rounded-1" style="aspect-ratio: 1; height: 100%"></div>
    </div>
    <div class="preview-body p-2">
      <div class="rounded-3 p-2 shadow-sm" :style="{ backgroundColor: activeTheme.cardBackground }">
        <template v-for="preset in textPreviews" :key="preset.width">
          <div class="text-preview">
            <div :style="{ width: preset.width, backgroundColor: preset.background }" />
          </div>
          <div class="artificial-padding" />
        </template>
      </div>
      <div class="artificial-padding" />
      <div class="row g-0">
        <div class="col-8">
          <div
            class="rounded-3 p-2 shadow-sm"
            :style="{ backgroundColor: activeTheme.cardBackground, aspectRatio: 4 }"
          />
        </div>
        <div class="col-4" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.theme-preview {
  aspect-ratio: 16/9;
}

.text-preview {
  aspect-ratio: 50/1;

  div {
    border-radius: 100px;
    height: 100%;
  }
}

.artificial-padding {
  aspect-ratio: 50/1;
}
</style>
