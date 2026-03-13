<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import type { DatePreset } from '../types/event-display';

const { locale } = useI18n();

// Map i18n locale code to BCP-47 locale for Intl/Date formatting
const localeMap: Record<string, string> = { vi: 'vi-VN', en: 'en-US' };
const dateLocale = computed(() => localeMap[locale.value] ?? 'en-US');

interface Props {
  show: boolean;
  startDate: string;
  endDate: string;
  selectedPreset: string;
  dateRangeLabel: string;
  presets: DatePreset[];
  isMobile: boolean;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  toggle: [];
  'select-preset': [value: string];
  'update:startDate': [value: string];
  'update:endDate': [value: string];
  reset: [];
  apply: [];
}>();

// Internal state
const hoveredDate = ref('');
const pickingEnd = ref(false);

// Calendar navigation — track the "left month" as { year, month (0-indexed) }
const leftMonth = ref({ year: new Date().getFullYear(), month: new Date().getMonth() });

const rightMonth = computed(() => {
  const m = leftMonth.value.month + 1;
  return {
    year: leftMonth.value.year + Math.floor(m / 12),
    month: m % 12,
  };
});

const weekDays = computed(() => {
  // Generate weekday names starting from Monday
  const formatter = new Intl.DateTimeFormat(dateLocale.value, { weekday: 'short' });
  // Jan 5, 2026 is a Monday
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(2026, 0, 5 + i);
    const name = formatter.format(d);
    return name.charAt(0).toUpperCase() + name.slice(1).replace('.', '');
  });
});

// Reset calendar view to startDate month when dropdown opens
watch(
  () => props.show,
  (open) => {
    if (open) {
      pickingEnd.value = false;
      hoveredDate.value = '';
      if (props.startDate) {
        const [y, m] = props.startDate.split('-').map(Number);
        leftMonth.value = { year: y!, month: m! - 1 };
      } else {
        leftMonth.value = { year: new Date().getFullYear(), month: new Date().getMonth() };
      }
    }
  },
);

// Sync calendar view when preset updates dates
watch(
  () => props.startDate,
  (val) => {
    if (val && props.show) {
      const [y, m] = val.split('-').map(Number);
      leftMonth.value = { year: y!, month: m! - 1 };
    }
  },
);

// --- Helpers ---
function pad(n: number): string {
  return String(n).padStart(2, '0');
}

function toDateStr(year: number, month: number, day: number): string {
  return `${year}-${pad(month + 1)}-${pad(day)}`;
}

function todayStr(): string {
  const d = new Date();
  return toDateStr(d.getFullYear(), d.getMonth(), d.getDate());
}

function formatMonthYear(m: { year: number; month: number }): string {
  const date = new Date(m.year, m.month, 1);
  return date.toLocaleDateString(dateLocale.value, { month: 'long', year: 'numeric' });
}

function formatMonthOnly(m: { year: number; month: number }): string {
  const date = new Date(m.year, m.month, 1);
  const str = date.toLocaleDateString(dateLocale.value, { month: 'long' });
  return str.charAt(0).toUpperCase() + str.slice(1);
}

const sharedYearLabel = computed(() => {
  if (leftMonth.value.year === rightMonth.value.year) {
    return `${leftMonth.value.year}`;
  }
  return `${leftMonth.value.year} — ${rightMonth.value.year}`;
});

function formatDisplayDate(dateStr: string): string {
  const parts = dateStr.split('-');
  return `${parts[2]}/${parts[1]}/${parts[0]}`;
}

// --- Calendar cell generation ---
interface CalendarCell {
  day: number;
  date: string;
  currentMonth: boolean;
  isToday: boolean;
}

function generateMonthCells(year: number, month: number): CalendarCell[] {
  const cells: CalendarCell[] = [];
  const firstDay = new Date(year, month, 1);
  // Monday = 0, Sunday = 6
  let startDow = firstDay.getDay() - 1;
  if (startDow < 0) startDow = 6;

  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const today = todayStr();

  // Previous month padding
  const prevMonthDays = new Date(year, month, 0).getDate();
  for (let i = startDow - 1; i >= 0; i--) {
    const d = prevMonthDays - i;
    const pm = month === 0 ? 11 : month - 1;
    const py = month === 0 ? year - 1 : year;
    cells.push({
      day: d,
      date: toDateStr(py, pm, d),
      currentMonth: false,
      isToday: false,
    });
  }

  // Current month days
  for (let d = 1; d <= daysInMonth; d++) {
    const dateStr = toDateStr(year, month, d);
    cells.push({
      day: d,
      date: dateStr,
      currentMonth: true,
      isToday: dateStr === today,
    });
  }

  // Next month padding (fill to 42 cells = 6 rows)
  const remaining = 42 - cells.length;
  for (let d = 1; d <= remaining; d++) {
    const nm = month === 11 ? 0 : month + 1;
    const ny = month === 11 ? year + 1 : year;
    cells.push({
      day: d,
      date: toDateStr(ny, nm, d),
      currentMonth: false,
      isToday: false,
    });
  }

  return cells;
}

const leftMonthCells = computed(() =>
  generateMonthCells(leftMonth.value.year, leftMonth.value.month),
);

const rightMonthCells = computed(() =>
  generateMonthCells(rightMonth.value.year, rightMonth.value.month),
);

// --- Navigation ---
function navigateMonth(delta: number) {
  let m = leftMonth.value.month + delta;
  let y = leftMonth.value.year;
  if (m < 0) {
    m = 11;
    y--;
  } else if (m > 11) {
    m = 0;
    y++;
  }
  leftMonth.value = { year: y, month: m };
}

// --- Selection logic ---
function handleDayClick(dateStr: string) {
  if (!pickingEnd.value || !props.startDate) {
    // First click — set start date
    emit('update:startDate', dateStr);
    emit('update:endDate', '');
    pickingEnd.value = true;
  } else {
    // Second click — set end date
    if (dateStr < props.startDate) {
      // Swap if picked earlier date
      emit('update:endDate', props.startDate);
      emit('update:startDate', dateStr);
    } else {
      emit('update:endDate', dateStr);
    }
    pickingEnd.value = false;
  }
}

function handleDayHover(dateStr: string) {
  if (pickingEnd.value && props.startDate) {
    hoveredDate.value = dateStr;
  }
}

// --- Cell CSS classes ---
function getCellClasses(cell: CalendarCell): string[] {
  const cls = ['calendar-cell'];

  if (!cell.currentMonth) {
    cls.push('outside-month');
    return cls;
  }

  const date = cell.date;
  const start = props.startDate;
  const end = props.endDate;

  // Start or end anchor
  if (date === start && date === end) {
    cls.push('range-single');
  } else if (date === start) {
    cls.push('range-start');
  } else if (date === end) {
    cls.push('range-end');
  }

  // In confirmed range
  if (start && end && date > start && date < end) {
    cls.push('in-range');
  }

  // Hover preview range (when picking end date)
  if (pickingEnd.value && start && !end && hoveredDate.value) {
    const hoverStart = hoveredDate.value < start ? hoveredDate.value : start;
    const hoverEnd = hoveredDate.value < start ? start : hoveredDate.value;

    if (date === hoveredDate.value) {
      cls.push('hover-anchor');
    } else if (date > hoverStart && date < hoverEnd) {
      cls.push('hover-range');
    }
  }

  if (cell.isToday) {
    cls.push('is-today');
  }

  return cls;
}
</script>

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

    <Transition name="dropdown">
      <div
        v-if="show"
        class="dropdown-popup position-absolute bg-reactive-secondary border border-1 rounded-3 shadow"
      >
        <div class="p-4">
          <!-- Preset Options (Chips) -->
          <div class="preset-chips d-flex flex-wrap gap-2 pb-3 mb-3 border-bottom border-secondary">
            <button
              v-for="preset in presets"
              :key="preset.value"
              :class="['chip', selectedPreset === preset.value ? 'chip--active' : '']"
              @click="$emit('select-preset', preset.value)"
            >
              {{ $t(`event_display.label.${preset.label}`) }}
            </button>
          </div>

          <!-- Shared Year Header -->
          <div v-if="!isMobile" class="shared-year-header">
            <span>{{ sharedYearLabel }}</span>
          </div>

          <!-- Custom Calendar -->
          <div class="calendar-container" :class="{ 'calendar-dual': !isMobile }">
            <!-- Left Month -->
            <div class="calendar-month">
              <div class="calendar-header">
                <button class="btn-nav-circle" @click="navigateMonth(-1)">
                  <i class="bi bi-chevron-left"></i>
                </button>
                <span class="calendar-title">{{ formatMonthOnly(leftMonth) }}</span>
                <button v-if="isMobile" class="btn-nav-circle" @click="navigateMonth(1)">
                  <i class="bi bi-chevron-right"></i>
                </button>
                <span v-else class="btn-nav-placeholder"></span>
              </div>
              <div class="calendar-weekdays">
                <span v-for="day in weekDays" :key="day" class="weekday">{{ day }}</span>
              </div>
              <div class="calendar-grid">
                <button
                  v-for="(cell, idx) in leftMonthCells"
                  :key="'l' + idx"
                  :class="getCellClasses(cell)"
                  :disabled="!cell.currentMonth"
                  @click="cell.currentMonth && handleDayClick(cell.date)"
                  @mouseenter="cell.currentMonth && handleDayHover(cell.date)"
                  @mouseleave="hoveredDate = ''"
                >
                  <span class="day-number">{{ cell.day }}</span>
                  <span v-if="cell.isToday" class="today-dot"></span>
                </button>
              </div>
            </div>

            <!-- Right Month (desktop only) -->
            <div v-if="!isMobile" class="calendar-month calendar-month--right">
              <div class="calendar-header">
                <span class="btn-nav-placeholder"></span>
                <span class="calendar-title">{{ formatMonthOnly(rightMonth) }}</span>
                <button class="btn-nav-circle" @click="navigateMonth(1)">
                  <i class="bi bi-chevron-right"></i>
                </button>
              </div>
              <div class="calendar-weekdays">
                <span v-for="day in weekDays" :key="'r' + day" class="weekday">{{ day }}</span>
              </div>
              <div class="calendar-grid">
                <button
                  v-for="(cell, idx) in rightMonthCells"
                  :key="'r' + idx"
                  :class="getCellClasses(cell)"
                  :disabled="!cell.currentMonth"
                  @click="cell.currentMonth && handleDayClick(cell.date)"
                  @mouseenter="cell.currentMonth && handleDayHover(cell.date)"
                  @mouseleave="hoveredDate = ''"
                >
                  <span class="day-number">{{ cell.day }}</span>
                  <span v-if="cell.isToday" class="today-dot"></span>
                </button>
              </div>
            </div>
          </div>

          <!-- Selected Range Display -->
          <div v-if="startDate || endDate" class="selected-range-display mt-3 mb-2">
            <div class="d-flex align-items-center justify-content-center gap-2">
              <div class="range-badge" :class="{ active: startDate }">
                {{ startDate ? formatDisplayDate(startDate) : '—' }}
              </div>
              <i class="bi bi-arrow-right text-reactive-primary" style="font-size: 12px"></i>
              <div class="range-badge" :class="{ active: endDate }">
                {{ endDate ? formatDisplayDate(endDate) : '—' }}
              </div>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="d-flex gap-3 pt-3">
            <button class="btn btn-action-reset flex-fill" @click="$emit('reset')">
              {{ $t('event_display.button.reset') }}
            </button>
            <button class="btn btn-action-apply flex-fill" @click="$emit('apply')">
              {{ $t('event_display.button.apply') }}
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

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

.dropdown-popup {
  top: calc(100% + 8px);
  right: 0;
  z-index: 1000;
  min-width: 610px;
  transform-origin: top right;
}

.dropdown-enter-active {
  animation: scaleIn 0.1s ease-out forwards;
}

.dropdown-leave-active {
  animation: scaleOut 0.1s ease-in forwards;
}

/* ===== Chip / Tag Preset Filters ===== */
.preset-chips {
  gap: 8px;
}

.chip {
  display: inline-flex;
  align-items: center;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  border: 1.5px solid var(--text-reactive-secondary, rgba(128, 128, 128, 0.3));
  background: transparent;
  color: var(--text-reactive-primary);
  cursor: pointer;
  transition: all 0.2s ease;
  user-select: none;
}

.chip:hover {
  border-color: #07b3df;
  color: #07b3df;
  background-color: rgba(7, 179, 223, 0.06);
}

.chip--active {
  background-color: #07b3df;
  color: white;
  border-color: #07b3df;
  font-weight: 600;
}

.chip--active:hover {
  background-color: #069ac0;
  border-color: #069ac0;
  color: white;
}

/* ===== Shared Year Header ===== */
.shared-year-header {
  text-align: center;
  margin-bottom: 8px;
}

.shared-year-header span {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-reactive-primary);
  letter-spacing: 0.5px;
}

/* ===== Calendar Container ===== */
.calendar-container {
  display: flex;
  gap: 32px;
}

.calendar-container:not(.calendar-dual) {
  flex-direction: column;
}

.calendar-month {
  flex: 1;
  min-width: 0;
}

.calendar-month--right {
  padding-left: 30px;
  border-left: 1px solid var(--text-reactive-secondary, rgba(128, 128, 128, 0.15));
}

/* ===== Calendar Header ===== */
.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  padding: 0 4px;
}

.calendar-title {
  font-weight: 700;
  font-size: 15px;
  color: var(--text-reactive-primary);
  text-transform: capitalize;
  user-select: none;
}

/* ===== Navigation Circle Buttons ===== */
.btn-nav-circle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border: 1.5px solid var(--text-reactive-secondary, rgba(128, 128, 128, 0.3));
  background: transparent;
  border-radius: 50%;
  color: var(--text-reactive-primary);
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-nav-circle:hover {
  background-color: rgba(7, 179, 223, 0.08);
  border-color: #07b3df;
  color: #07b3df;
}

.btn-nav-circle:active {
  background-color: rgba(7, 179, 223, 0.15);
}

.btn-nav-circle i {
  font-size: 13px;
  line-height: 1;
}

.btn-nav-placeholder {
  width: 32px;
  height: 32px;
}

/* ===== Weekday Headers ===== */
.calendar-weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  margin-bottom: 6px;
}

.weekday {
  text-align: center;
  font-size: 12px;
  font-weight: 700;
  color: var(--text-reactive-primary, #555);
  padding: 4px 0;
  user-select: none;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* ===== Calendar Grid ===== */
.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 2px;
}

/* ===== Calendar Cell ===== */
.calendar-cell {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  aspect-ratio: 1;
  border: none;
  background: transparent;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.15s ease;
  font-size: 13px;
  color: var(--text-reactive-primary);
  padding: 0;
}

.calendar-cell:hover:not(.outside-month):not(.range-start):not(.range-end):not(.range-single) {
  background-color: var(--bg-reactive-gray, rgba(128, 128, 128, 0.1));
}

.calendar-cell:disabled {
  cursor: default;
}

/* Outside month */
.outside-month {
  opacity: 0.25;
  pointer-events: none;
}

/* Day number */
.day-number {
  line-height: 1;
  font-weight: 500;
  z-index: 1;
}

/* Today indicator */
.today-dot {
  position: absolute;
  bottom: 3px;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background-color: #07b3df;
}

.is-today .day-number {
  font-weight: 700;
}

/* ===== Selection States ===== */
.range-start,
.range-end,
.range-single {
  background-color: #07b3df !important;
  color: white !important;
  border-radius: 50%;
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(7, 179, 223, 0.35);
}

.range-start .today-dot,
.range-end .today-dot,
.range-single .today-dot {
  background-color: white;
}

/* Range between start and end */
.in-range {
  background-color: rgba(7, 179, 223, 0.1);
  border-radius: 0;
}

/* Hover preview */
.hover-anchor {
  background-color: rgba(7, 179, 223, 0.3) !important;
  border-radius: 50%;
}

.hover-range {
  background-color: rgba(7, 179, 223, 0.08);
  border-radius: 0;
}

/* ===== Selected Range Display ===== */
.selected-range-display {
  text-align: center;
}

.range-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 14px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  background-color: var(--bg-reactive-gray, rgba(128, 128, 128, 0.08));
  color: var(--text-reactive-secondary, #999);
  transition: all 0.2s ease;
}

.range-badge.active {
  background-color: rgba(7, 179, 223, 0.12);
  color: #07b3df;
  font-weight: 600;
}

/* ===== Action Buttons ===== */
.btn-action-reset {
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-reactive-primary, #333);
  background: transparent;
  border: 1.5px solid var(--text-reactive-secondary, rgba(128, 128, 128, 0.35));
  transition: all 0.2s ease;
}

.btn-action-reset:hover {
  border-color: var(--text-reactive-primary, #555);
  background-color: var(--bg-reactive-gray, rgba(128, 128, 128, 0.06));
}

.btn-action-apply {
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  color: white;
  background-color: #07b3df;
  border: 1.5px solid #07b3df;
  transition: all 0.2s ease;
}

.btn-action-apply:hover {
  background-color: #069ac0;
  border-color: #069ac0;
  box-shadow: 0 2px 8px rgba(7, 179, 223, 0.3);
}

/* ===== Responsive ===== */
@media (max-width: 767.98px) {
  @keyframes scaleInMobile {
    from {
      opacity: 0;
      transform: translateX(-50%) scale(0.95);
    }
    to {
      opacity: 1;
      transform: translateX(-50%) scale(1);
    }
  }

  @keyframes scaleOutMobile {
    from {
      opacity: 1;
      transform: translateX(-50%) scale(1);
    }
    to {
      opacity: 0;
      transform: translateX(-50%) scale(0.95);
    }
  }

  .dropdown-popup {
    min-width: 90vw !important;
    left: 50% !important;
    transform: translateX(-50%) !important;
    right: auto;
  }

  .dropdown-enter-active {
    animation: scaleInMobile 0.15s ease-out forwards;
  }

  .dropdown-leave-active {
    animation: scaleOutMobile 0.1s ease-in forwards;
  }

  .calendar-cell {
    font-size: 14px;
  }
}
</style>
