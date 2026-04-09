<script setup lang="ts">
type ThemePreference = 'light' | 'dark';

interface AppearanceOption {
  value: ThemePreference;
  icon: string;
  titleKey: string;
  subtitleKey: string;
}

const model = defineModel<ThemePreference>({ required: true });

const options: AppearanceOption[] = [
  {
    value: 'dark',
    icon: 'bi bi-moon-stars-fill',
    titleKey: 'settings.preferences.appearance.dark_mode.title',
    subtitleKey: 'settings.preferences.appearance.dark_mode.subtitle',
  },
  {
    value: 'light',
    icon: 'bi bi-brightness-high-fill',
    titleKey: 'settings.preferences.appearance.light_mode.title',
    subtitleKey: 'settings.preferences.appearance.light_mode.subtitle',
  },
];
</script>

<template>
  <section class="d-flex flex-column gap-3">
    <div>
      <h2 class="h6 fw-bold text-primary mb-1 d-flex align-items-center gap-2">
        <i class="bi bi-palette-fill" aria-hidden="true"></i>
        {{ $t('settings.preferences.appearance.title') }}
      </h2>
      <p class="small text-reactive-secondary mb-0">
        {{ $t('settings.preferences.appearance.subtitle') }}
      </p>
    </div>

    <div class="row g-2 g-lg-3">
      <div v-for="option in options" :key="option.value" class="col-12 col-lg-6">
        <button
          type="button"
          class="preferences-theme-option w-100 d-flex align-items-center justify-content-between text-start border rounded px-3 py-2"
          :class="model === option.value ? 'preferences-theme-option--active border-primary' : 'border-transparent'"
          @click="model = option.value"
        >
          <span class="d-flex align-items-center gap-2">
            <i :class="[option.icon, model === option.value ? 'text-primary' : 'text-reactive-secondary']" />
            <span class="d-flex flex-column">
              <span class="fw-bold text-reactive-primary">{{ $t(option.titleKey) }}</span>
              <span class="small text-reactive-secondary">{{ $t(option.subtitleKey) }}</span>
            </span>
          </span>

          <i :class="model === option.value ? 'bi bi-check-circle-fill text-primary' : 'bi bi-circle text-reactive-secondary'" />
        </button>
      </div>
    </div>
  </section>
</template>

<style scoped>
.preferences-theme-option {
  min-height: 72px;
  background-color: var(--bg-reactive-primary);
}

.preferences-theme-option--active {
  background-color: var(--bg-reactive-secondary);
}
</style>