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
    value: 'light',
    icon: 'bi bi-brightness-high-fill',
    titleKey: 'settings.preferences.appearance.light_mode.title',
    subtitleKey: 'settings.preferences.appearance.light_mode.subtitle',
  },
  {
    value: 'dark',
    icon: 'bi bi-moon-fill',
    titleKey: 'settings.preferences.appearance.dark_mode.title',
    subtitleKey: 'settings.preferences.appearance.dark_mode.subtitle',
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

    <div class="row row-cols-1 row-cols-lg-2 g-3">
      <div v-for="option in options" :key="option.value" class="col">
        <button
          type="button"
          class="appearance-card card w-100 h-100 text-start border p-0 overflow-hidden bg-reactive-primary"
          :class="model === option.value ? 'border-primary shadow-sm bg-reactive-secondary' : 'border-secondary-subtle'"
          :aria-pressed="model === option.value"
          :aria-label="$t(option.titleKey)"
          @click="model = option.value"
        >
          <span class="appearance-card-preview border-bottom p-2" :data-bs-theme="option.value">
            <span class="appearance-card-preview-frame d-block mx-auto">
              <span class="ratio ratio-16x9">
                <span class="appearance-card-preview-window d-block rounded-2 overflow-hidden border">
                  <span class="appearance-card-preview-toolbar d-flex align-items-center gap-1 px-2 py-1 border-bottom">
                    <i class="appearance-card-preview-dot bi bi-dot"></i>
                    <i class="appearance-card-preview-dot bi bi-dot"></i>
                    <i class="appearance-card-preview-dot bi bi-dot"></i>
                  </span>
                  <span class="appearance-card-preview-content d-flex flex-column gap-1 px-2 py-2">
                    <span class="appearance-card-line appearance-card-line-primary d-block rounded-pill w-75"></span>
                    <span class="appearance-card-line appearance-card-line-secondary d-block rounded-pill w-50"></span>
                    <span class="appearance-card-line appearance-card-line-secondary d-block rounded-pill w-100"></span>
                  </span>
                </span>
              </span>
            </span>
          </span>

          <span class="d-flex align-items-start justify-content-between gap-2 px-3 py-2">
            <span class="d-flex align-items-start gap-2">
              <i :class="[option.icon, model === option.value ? 'text-primary' : 'text-reactive-secondary']" />
              <span class="d-flex flex-column">
                <span class="fw-bold text-reactive-primary">{{ $t(option.titleKey) }}</span>
                <span class="small text-reactive-secondary">{{ $t(option.subtitleKey) }}</span>
              </span>
            </span>

            <i :class="model === option.value ? 'bi bi-check-circle-fill text-primary' : 'bi bi-circle text-reactive-secondary'" />
          </span>
        </button>
      </div>
    </div>
  </section>
</template>

<style scoped>
.appearance-card {
  min-height: 170px;
  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease,
    transform 0.2s ease;
}

.appearance-card:hover {
  transform: translateY(-1px);
}

.appearance-card-preview {
  --appearance-preview-bg: #f3f4f6;
  --appearance-preview-surface: #ffffff;
  --appearance-preview-toolbar: #f5f6f8;
  --appearance-preview-border: rgba(15, 23, 42, 0.18);
  --appearance-preview-dot: #9ca3af;
  --appearance-preview-line-primary: var(--bs-primary, #20c2ea);
  --appearance-preview-line-secondary: #c4cad3;
  background-color: var(--appearance-preview-bg);
  border-bottom-color: var(--appearance-preview-border) !important;
}

.appearance-card-preview[data-bs-theme='light'] {
  --appearance-preview-bg: #ececec;
  --appearance-preview-surface: #ffffff;
  --appearance-preview-toolbar: #f5f6f8;
  --appearance-preview-border: rgba(15, 23, 42, 0.18);
  --appearance-preview-dot: #9ca3af;
  --appearance-preview-line-secondary: #c4cad3;
}

.appearance-card-preview[data-bs-theme='dark'] {
  --appearance-preview-bg: #161a20;
  --appearance-preview-surface: #111111;
  --appearance-preview-toolbar: #171d26;
  --appearance-preview-border: rgba(148, 163, 184, 0.32);
  --appearance-preview-dot: #8f9bad;
  --appearance-preview-line-secondary: #616b7a;
}

.appearance-card-preview-window {
  background-color: var(--appearance-preview-surface);
  border-color: var(--appearance-preview-border) !important;
}

.appearance-card-preview-toolbar {
  background-color: var(--appearance-preview-toolbar);
  border-bottom-color: var(--appearance-preview-border) !important;
}

.appearance-card-preview-dot {
  color: var(--appearance-preview-dot);
}

.appearance-card-preview-content {
  background-color: var(--appearance-preview-surface);
}

.appearance-card-line-primary {
  background-color: var(--appearance-preview-line-primary);
  opacity: 0.75;
}

.appearance-card-line-secondary {
  background-color: var(--appearance-preview-line-secondary);
}

.appearance-card-preview-frame {
  width: 100%;
}

@media (min-width: 992px) {
  .appearance-card-preview-frame {
    --appearance-preview-max-height: 152px;
    width: min(100%, calc(var(--appearance-preview-max-height) * 16 / 9));
  }
}

.appearance-card-line {
  height: 6px;
}
</style>
