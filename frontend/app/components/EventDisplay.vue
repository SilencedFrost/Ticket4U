<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';

// Interface for Event data
interface Event {
  id: number;
  image: string;
  title: string;
  price: string;
  date: string;
}

// Props
interface Props {
  events?: Event[];
  searchQuery?: string;
  dateRange?: string;
}

const props = withDefaults(defineProps<Props>(), {
  events: () => [],
  searchQuery: '',
  dateRange: '26/11/2025 - 30/12/2025',
});

// Sample event data - replace with actual API data
const sampleEvents: Event[] = [
  {
    id: 1,
    image: 'https://www.figma.com/api/mcp/asset/edb2cfba-4691-409f-8f0a-65a097f02bf2',
    title: 'SOOBIN LIVE CONCERT: ALL-ROUNDER THE FINAL',
    price: 'Từ 100.000đ',
    date: '26 tháng 11,2025',
  },
  {
    id: 2,
    image: 'https://www.figma.com/api/mcp/asset/fbc994a8-42af-47c2-b661-c76212673c6a',
    title: '[TP.HCM] Những Thành Phố Mơ Màng Year End 2025',
    price: 'Từ 900.000đ',
    date: '31 tháng 12,2025',
  },
  {
    id: 3,
    image: 'https://www.figma.com/api/mcp/asset/a23b6bd6-8e7b-488c-a54c-6da30b4e621a',
    title: 'ANH TRAI "SAY HI" 2025 CONCERT',
    price: 'Từ 1.040.000đ',
    date: '25 tháng 11,2025',
  },
  {
    id: 4,
    image: 'https://www.figma.com/api/mcp/asset/46a24091-0360-4f43-9a31-6d86212cc7a8',
    title: '[CAT&MOUSE] CA SĨ VICKY NHUNG + CA SĨ PHAN DUY ANH',
    price: 'Từ 375.000đ',
    date: '25 Tháng 11,2025',
  },
  {
    id: 5,
    image: 'https://www.figma.com/api/mcp/asset/edb2cfba-4691-409f-8f0a-65a097f02bf2',
    title: 'SOOBIN LIVE CONCERT: ALL-ROUNDER THE FINAL',
    price: 'Từ 100.000đ',
    date: '26 tháng 11,2025',
  },
  {
    id: 6,
    image: 'https://www.figma.com/api/mcp/asset/fbc994a8-42af-47c2-b661-c76212673c6a',
    title: '[TP.HCM] Những Thành Phố Mơ Màng Year End 2025',
    price: 'Từ 900.000đ',
    date: '31 tháng 12,2025',
  },
  {
    id: 7,
    image: 'https://www.figma.com/api/mcp/asset/a23b6bd6-8e7b-488c-a54c-6da30b4e621a',
    title: 'ANH TRAI "SAY HI" 2025 CONCERT',
    price: 'Từ 1.040.000đ',
    date: '25 tháng 11,2025',
  },
  {
    id: 8,
    image: 'https://www.figma.com/api/mcp/asset/46a24091-0360-4f43-9a31-6d86212cc7a8',
    title: '[CAT&MOUSE] CA SĨ VICKY NHUNG + CA SĨ PHAN DUY ANH',
    price: 'Từ 375.000đ',
    date: '25 Tháng 11,2025',
  },
  {
    id: 9,
    image: 'https://www.figma.com/api/mcp/asset/edb2cfba-4691-409f-8f0a-65a097f02bf2',
    title: 'SOOBIN LIVE CONCERT: ALL-ROUNDER THE FINAL',
    price: 'Từ 100.000đ',
    date: '26 tháng 11,2025',
  },
  {
    id: 10,
    image: 'https://www.figma.com/api/mcp/asset/fbc994a8-42af-47c2-b661-c76212673c6a',
    title: '[TP.HCM] Những Thành Phố Mơ Màng Year End 2025',
    price: 'Từ 900.000đ',
    date: '31 tháng 12,2025',
  },
  {
    id: 11,
    image: 'https://www.figma.com/api/mcp/asset/a23b6bd6-8e7b-488c-a54c-6da30b4e621a',
    title: 'ANH TRAI "SAY HI" 2025 CONCERT',
    price: 'Từ 1.040.000đ',
    date: '25 tháng 11,2025',
  },
  {
    id: 12,
    image: 'https://www.figma.com/api/mcp/asset/46a24091-0360-4f43-9a31-6d86212cc7a8',
    title: '[CAT&MOUSE] CA SĨ VICKY NHUNG + CA SĨ PHAN DUY ANH',
    price: 'Từ 375.000đ',
    date: '25 Tháng 11,2025',
  },
  {
    id: 13,
    image: 'https://www.figma.com/api/mcp/asset/edb2cfba-4691-409f-8f0a-65a097f02bf2',
    title: 'SOOBIN LIVE CONCERT: ALL-ROUNDER THE FINAL',
    price: 'Từ 100.000đ',
    date: '26 tháng 11,2025',
  },
  {
    id: 14,
    image: 'https://www.figma.com/api/mcp/asset/fbc994a8-42af-47c2-b661-c76212673c6a',
    title: '[TP.HCM] Những Thành Phố Mơ Màng Year End 2025',
    price: 'Từ 900.000đ',
    date: '31 tháng 12,2025',
  },
  {
    id: 15,
    image: 'https://www.figma.com/api/mcp/asset/a23b6bd6-8e7b-488c-a54c-6da30b4e621a',
    title: 'ANH TRAI "SAY HI" 2025 CONCERT',
    price: 'Từ 1.040.000đ',
    date: '25 tháng 11,2025',
  },
  {
    id: 16,
    image: 'https://www.figma.com/api/mcp/asset/46a24091-0360-4f43-9a31-6d86212cc7a8',
    title: '[CAT&MOUSE] CA SĨ VICKY NHUNG + CA SĨ PHAN DUY ANH',
    price: 'Từ 375.000đ',
    date: '25 Tháng 11,2025',
  },
];

// Helper function to parse Vietnamese date format
const parseEventDate = (dateStr: string): Date | null => {
  // Format: "26 tháng 11,2025" or "25 Tháng 11,2025"
  const match = dateStr.match(/(\d+)\s+[tT]háng\s+(\d+)[,\s]+(\d+)/);
  if (match) {
    const day = parseInt(match[1]!);
    const month = parseInt(match[2]!) - 1; // JavaScript months are 0-indexed
    const year = parseInt(match[3]!);
    return new Date(year, month, day);
  }
  return null;
};

// Computed property to filter events by date range
const displayEvents = computed(() => {
  const events = props.events.length > 0 ? props.events : sampleEvents;
  
  // If no date range is selected, return all events
  if (!startDate.value || !endDate.value) {
    return events;
  }
  
  // Parse selected date range
  const start = new Date(startDate.value);
  const end = new Date(endDate.value);
  
  // Filter events by date range
  return events.filter(event => {
    if (!event.date) return false;
    const eventDate = parseEventDate(event.date);
    if (!eventDate) return false;
    
    // Check if event date is within range (inclusive)
    return eventDate >= start && eventDate <= end;
  });
});

// Filter state
const showDateFilter = ref(false);
const showMainFilter = ref(false);

// Date range state
const startDate = ref<string>('');
const endDate = ref<string>('');
const selectedPreset = ref<string>('');

// Date presets
const datePresets = [
  { label: 'Tất cả các ngày', value: 'all' },
  { label: 'Hôm nay', value: 'today' },
  { label: 'Ngày mai', value: 'tomorrow' },
  { label: 'Cuối tuần này', value: 'this-weekend' },
  { label: 'Tháng này', value: 'this-month' },
];

// Helper function to format date to yyyy-MM-dd for input
const toInputFormat = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// Filter handlers
const toggleDateFilter = () => {
  showDateFilter.value = !showDateFilter.value;
};

const toggleMainFilter = () => {
  showMainFilter.value = !showMainFilter.value;
};

// Date preset handlers
const selectPreset = (preset: string) => {
  selectedPreset.value = preset;
  const today = new Date();
  
  switch (preset) {
    case 'all':
      startDate.value = '';
      endDate.value = '';
      break;
    case 'today':
      startDate.value = toInputFormat(today);
      endDate.value = toInputFormat(today);
      break;
    case 'tomorrow':
      const tomorrow = new Date(today);
      tomorrow.setDate(today.getDate() + 1);
      startDate.value = toInputFormat(tomorrow);
      endDate.value = toInputFormat(tomorrow);
      break;
    case 'this-weekend':
      const dayOfWeek = today.getDay();
      const daysUntilFriday = (5 - dayOfWeek + 7) % 7;
      const friday = new Date(today);
      friday.setDate(today.getDate() + daysUntilFriday);
      const sunday = new Date(friday);
      sunday.setDate(friday.getDate() + 2);
      startDate.value = toInputFormat(friday);
      endDate.value = toInputFormat(sunday);
      break;
    case 'this-month':
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
      const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
      startDate.value = toInputFormat(firstDay);
      endDate.value = toInputFormat(lastDay);
      break;
  }
};

const formatDateRange = () => {
  if (!startDate.value || !endDate.value) return 'Tất cả các ngày';
  
  const formatDate = (dateStr: string) => {
    const parts = dateStr.split('-');
    const year = parts[0] || '';
    const month = parts[1] || '';
    const day = parts[2] || '';
    return `${day}/${month}/${year}`;
  };
  
  return `${formatDate(startDate.value)} - ${formatDate(endDate.value)}`;
};

const applyDateFilter = () => {
  showDateFilter.value = false;
  // Emit event or call API with date range
  console.log('Apply date filter:', startDate.value, endDate.value);
};

const resetDateFilter = () => {
  startDate.value = '';
  endDate.value = '';
  selectedPreset.value = '';
};

// Event click handler
const handleEventClick = (eventId: number) => {
  // Navigate to event detail page
  console.log('Navigate to event:', eventId);
  // You can use: navigateTo(`/event/${eventId}`)
};

// Close popup when clicking outside
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (showDateFilter.value && !target.closest('.date-filter-popup') && !target.closest('.btn-filter-date')) {
    showDateFilter.value = false;
  }
  if (showMainFilter.value && !target.closest('.main-filter-popup') && !target.closest('.btn-filter')) {
    showMainFilter.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<template>
  <div class="event-display" style="background-color: #111111">
    <div class="container-xxl py-5">
      <!-- Header section with filters -->
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary fw-normal mb-0" style="font-size: 20px">
          Kết quả tìm kiếm
        </h2>

        <div class="d-flex gap-3">
          <!-- Date Range Filter Button -->
          <div class="position-relative">
            <button
              class="btn btn-filter-date d-flex align-items-center gap-2"
              @click="toggleDateFilter"
            >
              <i class="bi bi-calendar-event"></i>
              <span>{{ formatDateRange() }}</span>
              <i class="bi bi-chevron-down ms-auto"></i>
            </button>

            <!-- Date Range Picker Popup -->
            <div v-if="showDateFilter" class="date-filter-popup">
              <div class="date-filter-content">
                <!-- Preset Options -->
                <div class="preset-options">
                  <button
                    v-for="preset in datePresets"
                    :key="preset.value"
                    :class="['preset-btn', { active: selectedPreset === preset.value }]"
                    @click="selectPreset(preset.value)"
                  >
                    {{ preset.label }}
                  </button>
                </div>

                <!-- Date Pickers -->
                <div class="date-pickers">
                  <div class="date-input-group">
                    <label>Từ ngày</label>
                    <input
                      v-model="startDate"
                      type="date"
                      class="form-control"
                    />
                  </div>
                  <div class="date-input-group">
                    <label>Đến ngày</label>
                    <input
                      v-model="endDate"
                      type="date"
                      class="form-control"
                    />
                  </div>
                </div>

                <!-- Action Buttons -->
                <div class="date-filter-actions">
                  <button class="btn btn-reset" @click="resetDateFilter">
                    Thiết lập lại
                  </button>
                  <button class="btn btn-apply" @click="applyDateFilter">
                    Áp dụng
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Main Filter Button -->
          <button
            class="btn btn-filter d-flex align-items-center gap-2"
            @click="toggleMainFilter"
          >
            <i class="bi bi-funnel"></i>
            <span>Bộ lọc</span>
            <i class="bi bi-chevron-down ms-auto"></i>
          </button>
        </div>
      </div>

      <!-- Events Grid -->
      <div class="row g-3 g-md-4">
        <div
          v-for="event in displayEvents"
          :key="event.id"
          class="col-6 col-md-4 col-lg-3"
        >
          <div
            class="event-card"
            role="button"
            tabindex="0"
            @click="handleEventClick(event.id)"
            @keypress.enter="handleEventClick(event.id)"
          >
            <!-- Event Image -->
            <div class="event-card-image mb-3">
              <img
                :src="event.image"
                :alt="event.title"
                class="w-100 h-100 object-fit-cover"
                loading="lazy"
              />
            </div>

            <!-- Event Info -->
            <div class="event-card-content">
              <h3 class="event-card-title text-white fw-bold mb-2">
                {{ event.title }}
              </h3>
              <p class="event-card-price text-primary fw-bold mb-1">
                {{ event.price }}
              </p>
              <p class="event-card-date text-white mb-0 d-flex align-items-center gap-1">
                <i class="bi bi-calendar-event"></i>
                <span>{{ event.date }}</span>
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div
        v-if="displayEvents.length === 0"
        class="text-center py-5"
      >
        <p class="text-white-50 mb-0">Không tìm thấy sự kiện nào</p>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.event-display {
  min-height: 100vh;
}

// Filter Buttons
.btn-filter-date {
  background-color: rgba(7, 179, 223, 0.8);
  border: none;
  border-radius: 30px;
  color: white;
  font-weight: 700;
  font-size: 16px;
  padding: 8px 24px;
  min-width: 336px;
  transition: all 0.3s ease;

  &:hover {
    background-color: rgba(7, 179, 223, 1);
    color: white;
  }

  &:active,
  &:focus {
    background-color: rgba(7, 179, 223, 1);
    color: white;
    box-shadow: 0 0 0 0.25rem rgba(7, 179, 223, 0.25);
  }

  i {
    font-size: 20px;
  }
}

.btn-filter {
  background-color: rgba(255, 255, 255, 0.3);
  border: none;
  border-radius: 30px;
  color: white;
  font-weight: 700;
  font-size: 16px;
  padding: 8px 24px;
  min-width: 140px;
  transition: all 0.3s ease;

  &:hover {
    background-color: rgba(255, 255, 255, 0.4);
    color: white;
  }

  &:active,
  &:focus {
    background-color: rgba(255, 255, 255, 0.4);
    color: white;
    box-shadow: 0 0 0 0.25rem rgba(255, 255, 255, 0.1);
  }

  i {
    font-size: 20px;
  }
}

// Event Card Styles
.event-card {
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-4px);

    .event-card-image img {
      transform: scale(1.05);
    }
  }

  &:focus {
    outline: 2px solid #07b3df;
    outline-offset: 4px;
    border-radius: 20px;
  }
}

.event-card-image {
  width: 100%;
  aspect-ratio: 16 / 9;
  border-radius: 12px;
  overflow: hidden;
  background-color: var(--bg-reactive-secondary);
  position: relative;

  img {
    transition: transform 0.3s ease;
    object-position: center;
  }
}

.event-card-content {
  padding: 0;
}

.event-card-title {
  font-size: 20px;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  min-height: 52px;

  // Responsive font sizes
  @media (max-width: 991.98px) {
    font-size: 18px;
    min-height: 47px;
  }

  @media (max-width: 767.98px) {
    font-size: 16px;
    min-height: 42px;
  }
}

.event-card-price {
  font-size: 16px;
  color: #07b3df;

  @media (max-width: 767.98px) {
    font-size: 14px;
  }
}

.event-card-date {
  font-size: 16px;
  font-weight: normal;

  i {
    font-size: 16px;
  }

  @media (max-width: 767.98px) {
    font-size: 14px;

    i {
      font-size: 14px;
    }
  }
}

// Grid spacing adjustments
.row.g-3 {
  @media (min-width: 768px) {
    --bs-gutter-x: 1.5rem;
    --bs-gutter-y: 1.5rem;
  }
}

.row.g-md-4 {
  @media (min-width: 768px) {
    --bs-gutter-x: 2rem;
    --bs-gutter-y: 2rem;
  }
}

// Date Filter Popup
.date-filter-popup {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background-color: #181818;
  border: 1px solid #333;
  border-radius: 12px;
  padding: 0;
  min-width: 600px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  z-index: 1000;

  @media (max-width: 767.98px) {
    min-width: 90vw;
    right: -20px;
  }
}

.date-filter-content {
  padding: 1.5rem;
}

.preset-options {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding-bottom: 1rem;
  margin-bottom: 1rem;
  border-bottom: 1px dashed #a6a6b0;
}

.preset-btn {
  background: transparent;
  border: none;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;

  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }

  &.active {
    background-color: #07b3df;
    color: white;
  }
}

.date-pickers {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;

  @media (max-width: 575.98px) {
    flex-direction: column;
  }
}

.date-input-group {
  flex: 1;

  label {
    display: block;
    color: white;
    font-size: 14px;
    margin-bottom: 0.5rem;
  }

  input[type='date'] {
    width: 100%;
    background-color: #111111;
    border: 1px solid #333;
    color: white;
    padding: 0.5rem;
    border-radius: 8px;
    font-size: 14px;

    &::-webkit-calendar-picker-indicator {
      filter: invert(1);
      cursor: pointer;
    }

    &:focus {
      outline: none;
      border-color: #07b3df;
      box-shadow: 0 0 0 2px rgba(7, 179, 223, 0.2);
    }
  }
}

.date-filter-actions {
  display: flex;
  gap: 1rem;
  padding-top: 1rem;

  .btn {
    flex: 1;
    padding: 0.5rem 1rem;
    border-radius: 8px;
    border: none;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.2s ease;
  }

  .btn-reset {
    background-color: transparent;
    border: 1px solid #333;
    color: white;

    &:hover {
      background-color: rgba(255, 255, 255, 0.05);
    }
  }

  .btn-apply {
    background-color: #07b3df;
    color: white;

    &:hover {
      background-color: #0599bf;
    }
  }
}
</style>
