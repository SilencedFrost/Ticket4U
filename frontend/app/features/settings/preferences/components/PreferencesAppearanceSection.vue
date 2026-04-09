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
          :class="model === option.value ? 'border-primary shadow-sm bg-reactive-secondary' : 'border-reactive-subtle'"
          :aria-pressed="model === option.value"
          :aria-label="$t(option.titleKey)"
          @click="model = option.value"
        >
          <span class="appearance-card-preview border-bottom border-reactive-subtle p-2 bg-reactive-secondary" :data-bs-theme="option.value">
            <span class="appearance-card-preview-frame d-block mx-auto">
              <span class="ratio ratio-16x9">
                <span class="appearance-card-preview-window d-block rounded-2 overflow-hidden border border-reactive-subtle bg-reactive-primary">
                  <span class="appearance-card-preview-toolbar d-flex align-items-center gap-1 px-2 py-1 border-bottom border-reactive-subtle bg-reactive-secondary">
                    <i class="bi bi-dot text-reactive-secondary"></i>
                    <i class="bi bi-dot text-reactive-secondary"></i>
                    <i class="bi bi-dot text-reactive-secondary"></i>
                  </span>
                  <span class="d-flex flex-column gap-1 px-2 py-2 bg-reactive-primary">
                    <span class="appearance-card-line d-block rounded-pill w-75 bg-primary opacity-75"></span>
                    <span class="appearance-card-line d-block rounded-pill w-50 bg-reactive-gray"></span>
                    <span class="appearance-card-line d-block rounded-pill w-100 bg-reactive-gray"></span>
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
