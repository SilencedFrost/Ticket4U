<template>
  <div class="position-relative">
    <button
      class="btn btn-filter-secondary d-flex align-items-center gap-2"
      :class="isMobile ? 'btn-filter-mobile' : 'btn-filter-desktop'"
      @click="$emit('toggle')"
    >
      <i class="bi bi-funnel fs-5"></i>
      <span class="d-none d-sm-inline">{{ $t('event_display.label.filter') }}</span>
      <span class="d-sm-none">Lọc</span>
      <i class="bi bi-chevron-down ms-auto"></i>
    </button>

    <Transition name="dropdown">
      <div
        v-if="show"
        class="dropdown-popup position-absolute bg-reactive-secondary border border-1 rounded-3 shadow"
        :style="popupStyle"
      >
        <div class="p-4">
          <!-- Location Filter -->
          <div class="mb-3">
            <h6 class="fw-bold text-reactive-primary mb-3 small">
              {{ $t('event_display.title.place') }}
            </h6>
            <div class="d-flex flex-column gap-2">
              <label
                v-for="location in locations"
                :key="location.value"
                class="d-flex align-items-center gap-2 cursor-pointer user-select-none"
              >
                <input
                  :checked="selectedLocation === location.value"
                  type="radio"
                  :value="location.value"
                  name="location"
                  class="form-check-input m-0"
                  @change="$emit('update:selectedLocation', location.value)"
                />
                <span class="text-reactive-primary small">{{ location.label }}</span>
              </label>
            </div>
          </div>

          <hr class="border-secondary border-opacity-50" />

          <!-- Price Filter -->
          <div class="mb-3">
            <h6 class="fw-bold text-reactive-primary mb-3 small">
              {{ $t('event_display.title.price') }}
            </h6>
            <div class="d-flex justify-content-between align-items-center">
              <span class="text-reactive-primary small">{{ $t('event_display.label.free') }}</span>
              <div class="form-check form-switch">
                <input
                  :checked="isFreeEvent"
                  type="checkbox"
                  class="form-check-input cursor-pointer"
                  role="switch"
                  @change="$emit('update:isFreeEvent', ($event.target as HTMLInputElement).checked)"
                />
              </div>
            </div>
          </div>

          <hr class="border-secondary border-opacity-50" />

          <!-- Category Filter -->
          <div class="mb-3">
            <h6 class="fw-bold text-reactive-primary mb-3 small">
              {{ $t('event_display.title.category') }}
            </h6>
            <div class="d-flex flex-wrap gap-2">
              <button
                v-for="category in categories"
                :key="category.value"
                :class="[
                  'btn btn-sm rounded-pill border',
                  selectedCategories.includes(category.value)
                    ? 'btn-primary text-white border-primary'
                    : 'btn-outline-secondary text-reactive-primary',
                ]"
                @click="$emit('toggle-category', category.value)"
              >
                {{ $t(`common.category.${category.value}`, category.label) }}
              </button>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="d-flex gap-2 mt-3">
            <button class="btn btn-outline-secondary flex-fill rounded-2" @click="$emit('reset')">
              {{ $t('event_display.button.reset') }}
            </button>
            <button class="btn btn-primary flex-fill rounded-2" @click="$emit('apply')">
              {{ $t('event_display.button.apply') }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import type { LocationOption, CategoryOption } from '../types/event-display';

interface Props {
  show: boolean;
  selectedLocation: string;
  isFreeEvent: boolean;
  selectedCategories: string[];
  locations: LocationOption[];
  categories: CategoryOption[];
  isMobile: boolean;
  popupStyle?: Record<string, string>;
}

withDefaults(defineProps<Props>(), {
  popupStyle: () => ({}),
});

defineEmits<{
  toggle: [];
  'update:selectedLocation': [value: string];
  'update:isFreeEvent': [value: boolean];
  'toggle-category': [value: string];
  reset: [];
  apply: [];
}>();
</script>

<style scoped>
.btn-filter-secondary {
  background-color: var(--bg-reactive-gray);
  border: none;
  border-radius: 30px;
  color: var(--text-reactive-primary);
  font-weight: 700;
  padding: 8px 24px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.btn-filter-secondary:hover,
.btn-filter-secondary:focus {
  background-color: var(--bg-reactive-gray-hover);
  color: var(--text-reactive-primary);
}

.btn-filter-mobile {
  min-width: 100px;
}

.btn-filter-desktop {
  min-width: 140px;
}

.dropdown-popup {
  top: calc(100% + 8px);
  right: 0;
  min-width: 350px;
  z-index: 1000;
  max-height: 80vh;
  overflow-y: auto;
  transform-origin: top right;
}

.dropdown-enter-active {
  animation: scaleIn 0.1s ease-out forwards;
}

.dropdown-leave-active {
  animation: scaleOut 0.1s ease-in forwards;
}
@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.75);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
@keyframes scaleOut {
  from {
    opacity: 1;
    transform: scale(1);
  }
  to {
    opacity: 0;
    transform: scale(0.75);
  }
}
.cursor-pointer {
  cursor: pointer;
}

.user-select-none {
  user-select: none;
}

@media (max-width: 767.98px) {
  @keyframes scaleInMobile {
    from {
      opacity: 0;
      transform: translateX(-70%) scale(0.95);
    }
    to {
      opacity: 1;
      transform: translateX(-70%) scale(1);
    }
  }

  @keyframes scaleOutMobile {
    from {
      opacity: 1;
      transform: translateX(-70%) scale(1);
    }
    to {
      opacity: 0;
      transform: translateX(-70%) scale(0.95);
    }
  }

  .dropdown-popup {
    min-width: 70vw !important;
    left: 30% !important;
    transform: translateX(-70%) !important;
    right: auto !important;
    transform-origin: top center;
  }

  .dropdown-enter-active {
    animation: scaleInMobile 0.15s ease-out forwards;
  }

  .dropdown-leave-active {
    animation: scaleOutMobile 0.1s ease-in forwards;
  }
}
</style>
