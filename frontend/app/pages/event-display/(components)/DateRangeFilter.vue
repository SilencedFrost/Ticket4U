<template>
  <div class="position-relative">
    <button
      class="btn btn-filter-primary d-flex align-items-center gap-2"
      :class="isMobile ? 'btn-filter-mobile' : 'btn-filter-desktop'"
      @click="$emit('toggle')"
    >
      <i class="bi bi-calendar-event fs-5"></i>
      <span class="d-none d-sm-inline">{{ dateRangeLabel }}</span>
      <span class="d-sm-none">Ngày</span>
      <i class="bi bi-chevron-down ms-auto"></i>
    </button>

    <div
      v-if="show"
      class="dropdown-popup position-absolute bg-reactive-secondary border border-1 rounded-3 shadow"
      style="min-width: 600px"
    >
      <div class="p-4">
        <!-- Preset Options -->
        <div class="preset-options d-flex flex-wrap gap-2 pb-3 mb-3 border-bottom border-secondary">
          <button
            v-for="preset in presets"
            :key="preset.value"
            :class="[
              'btn btn-sm rounded-2 text-reactive-primary',
              selectedPreset === preset.value ? 'btn-primary text-white' : 'btn-transparent',
            ]"
            @click="$emit('select-preset', preset.value)"
          >
            {{ preset.label }}
          </button>
        </div>

        <!-- Date Pickers -->
        <div class="row g-3 mb-3">
          <div class="col-12 col-sm-6">
            <label class="form-label text-reactive-primary small">Từ ngày</label>
            <input
              :value="startDate"
              type="date"
              class="form-control bg-reactive-primary text-reactive-primary border-secondary"
              @input="$emit('update:startDate', ($event.target as HTMLInputElement).value)"
            />
          </div>
          <div class="col-12 col-sm-6">
            <label class="form-label text-reactive-primary small">Đến ngày</label>
            <input
              :value="endDate"
              type="date"
              class="form-control bg-reactive-primary text-reactive-primary border-secondary"
              @input="$emit('update:endDate', ($event.target as HTMLInputElement).value)"
            />
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="d-flex gap-3 pt-3">
          <button class="btn btn-outline-secondary flex-fill rounded-2" @click="$emit('reset')">
            Thiết lập lại
          </button>
          <button class="btn btn-primary flex-fill rounded-2" @click="$emit('apply')">
            Áp dụng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { DatePreset } from '../types/event-display';

interface Props {
  show: boolean;
  startDate: string;
  endDate: string;
  selectedPreset: string;
  dateRangeLabel: string;
  presets: DatePreset[];
  isMobile: boolean;
}

defineProps<Props>();

defineEmits<{
  toggle: [];
  'select-preset': [value: string];
  'update:startDate': [value: string];
  'update:endDate': [value: string];
  reset: [];
  apply: [];
}>();
</script>

<style scoped>
.btn-filter-primary {
  background-color: rgba(7, 179, 223, 0.8);
  border: none;
  border-radius: 30px;
  color: white;
  font-weight: 700;
  padding: 8px 24px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.btn-filter-primary:hover,
.btn-filter-primary:focus {
  background-color: rgb(7, 179, 223);
  color: white;
  box-shadow: 0 4px 12px rgba(7, 179, 223, 0.3);
}

.btn-filter-mobile {
  min-width: 120px;
}

.btn-filter-desktop {
  min-width: 336px;
}

.dropdown-popup {
  top: calc(100% + 8px);
  right: 0;
  z-index: 1000;
}

.btn-transparent {
  background: transparent;
}

.btn-transparent:hover {
  background-color: var(--bg-reactive-secondary);
}

[data-bs-theme='dark'] input[type='date']::-webkit-calendar-picker-indicator {
  filter: invert(1);
}

@media (max-width: 767.98px) {
  .dropdown-popup {
    min-width: 90vw !important;
    left: 0;
    right: auto;
  }
}
</style>
