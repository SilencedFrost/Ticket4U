<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue';

// Props
interface Props {
  events?: EventData[];
  searchQuery?: string;
  dateRange?: string;
}

const props = withDefaults(defineProps<Props>(), {
  events: () => [],
  searchQuery: '',
  dateRange: '26/11/2025 - 30/12/2025',
});

// Sample event data - replace with actual API data
const sampleEvents: EventData[] = [
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

// Main filter state
const selectedLocation = ref<string>('');
const isFreeEvent = ref(false);
const selectedCategories = ref<string[]>([]);

// Location options
const locations = [
  { label: 'Toàn quốc', value: '' },
  { label: 'Hồ Chí Minh', value: 'hcm' },
  { label: 'Hà Nội', value: 'hanoi' },
  { label: 'Đà Lạt', value: 'dalat' },
  { label: 'Vị trí khác', value: 'other' },
];

// Category options
const categories = [
  { label: 'Nhạc sống', value: 'music' },
  { label: 'Sân khấu & Nghệ thuật', value: 'theatersandart' },
  { label: 'Thể Thao', value: 'sport' },
  { label: 'Khác', value: 'others' },
];

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
  // Force DOM update to calculate position
  nextTick(() => {
    adjustPopupPosition();
  });
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

// Main filter handlers
const toggleCategory = (categoryValue: string) => {
  const index = selectedCategories.value.indexOf(categoryValue);
  if (index > -1) {
    selectedCategories.value.splice(index, 1);
  } else {
    selectedCategories.value.push(categoryValue);
  }
};

const applyMainFilter = () => {
  showMainFilter.value = false;
  // Apply filters logic here
  console.log('Apply filters:', {
    location: selectedLocation.value,
    isFree: isFreeEvent.value,
    categories: selectedCategories.value,
  });
};

const resetMainFilter = () => {
  selectedLocation.value = '';
  isFreeEvent.value = false;
  selectedCategories.value = [];
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
  if (showDateFilter.value && !target.closest('.dropdown-popup-dark') && !target.closest('.btn-filter-primary')) {
    showDateFilter.value = false;
  }
  if (showMainFilter.value && !target.closest('.dropdown-popup-dark') && !target.closest('.btn-filter-secondary')) {
    showMainFilter.value = false;
  }
};

// Adjust popup position for mobile
const adjustPopupPosition = () => {
  if (!isMobile.value || !showMainFilter.value) return;

  const button = document.querySelector('.btn-filter-secondary') as HTMLElement;
  const popup = document.querySelector('.dropdown-popup-dark') as HTMLElement;

  if (!button || !popup) return;

  const buttonRect = button.getBoundingClientRect();
  const popupRect = popup.getBoundingClientRect();
  const viewportHeight = window.innerHeight;
  const viewportWidth = window.innerWidth;

  // Calculate available space
  const spaceBelow = viewportHeight - buttonRect.bottom;
  const spaceAbove = buttonRect.top;
  const spaceRight = viewportWidth - buttonRect.left;
  const spaceLeft = buttonRect.left;

  // Reset styles
  mainFilterPopupStyle.value = {};

  // If not enough space below, position above the button
  if (spaceBelow < popupRect.height && spaceAbove > popupRect.height) {
    mainFilterPopupStyle.value = {
      top: 'auto',
      bottom: 'calc(100% + 8px)',
      left: '0',
      right: 'auto',
      transform: 'none',
      maxHeight: `${spaceAbove - 16}px`,
      overflowY: 'auto'
    };
  } else {
    // Position below with constrained width
    const maxWidth = Math.min(spaceRight, viewportWidth - 32); // Leave some margin
    mainFilterPopupStyle.value = {
      top: 'calc(100% + 8px)',
      bottom: 'auto',
      left: '0',
      right: 'auto',
      transform: 'none',
      maxWidth: `${maxWidth}px`,
      maxHeight: `${spaceBelow - 16}px`,
      overflowY: 'auto'
    };
  }
};

// Mobile detection
const isMobile = ref(false);

// Popup positioning
const mainFilterPopupStyle = ref({});

const updateMobileState = () => {
  isMobile.value = window.innerWidth < 768;
  if (showMainFilter.value) {
    nextTick(() => adjustPopupPosition());
  }
};

onMounted(() => {
  updateMobileState();
  window.addEventListener('resize', updateMobileState);
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  window.removeEventListener('resize', updateMobileState);
  document.removeEventListener('click', handleClickOutside);
});
</script>

<template>
  <div class="event-display bg-reactive-primary" style="min-height: 100vh">
    <div class="container-xxl py-5">
      <!-- Header section with filters -->
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="text-primary fw-normal mb-0 d-none d-md-block" style="font-size: 20px">
          Kết quả tìm kiếm
        </h2>

        <div class="d-flex gap-2 gap-md-3">
          <!-- Date Range Filter Button -->
          <div class="position-relative">
            <button
              class="btn btn-filter-primary d-flex align-items-center gap-2"
              style="min-width: 120px; font-size: 14px"
              :style="{ 'min-width': isMobile ? '120px' : '336px' }"
              @click="toggleDateFilter"
            >
              <i class="bi bi-calendar-event fs-5"></i>
              <span class="d-none d-sm-inline">{{ formatDateRange() }}</span>
              <span class="d-sm-none">Ngày</span>
              <i class="bi bi-chevron-down ms-auto"></i>
            </button>

            <!-- Date Range Picker Popup -->
            <div v-if="showDateFilter" class="dropdown-popup-dark" style="min-width: 600px">
              <div class="p-4">
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
          <div class="position-relative">
            <button
              class="btn btn-filter-secondary d-flex align-items-center gap-2"
              style="min-width: 100px; font-size: 14px"
              :style="{ 'min-width': isMobile ? '100px' : '140px' }"
              @click="toggleMainFilter"
            >
              <i class="bi bi-funnel fs-5"></i>
              <span class="d-none d-sm-inline">Bộ lọc</span>
              <span class="d-sm-none">Lọc</span>
              <i class="bi bi-chevron-down ms-auto"></i>
            </button>

            <!-- Main Filter Popup -->
            <div v-if="showMainFilter" class="dropdown-popup-dark" style="min-width: 350px" :style="mainFilterPopupStyle">
              <div class="p-4">
                <!-- Location Filter -->
                <div class="mb-3">
                  <h6 class="fw-bold text-reactive-primary mb-3" style="font-size: 14px">Vị trí</h6>
                  <div class="filter-radio-group">
                    <label
                      v-for="location in locations"
                      :key="location.value"
                      class="filter-radio-label"
                    >
                      <input
                        v-model="selectedLocation"
                        type="radio"
                        :value="location.value"
                        class="filter-radio-input"
                      />
                      <span class="filter-radio-custom"></span>
                      <span class="filter-radio-text">{{ location.label }}</span>
                    </label>
                  </div>
                </div>

                <div class="filter-divider"></div>

                <!-- Price Filter -->
                <div class="mb-3">
                  <h6 class="fw-bold text-reactive-primary mb-3" style="font-size: 14px">Giá tiền</h6>
                  <div class="d-flex justify-content-between align-items-center">
                    <span class="text-reactive-primary" style="font-size: 14px">Miễn phí</span>
                    <label class="filter-switch">
                      <input v-model="isFreeEvent" type="checkbox" />
                      <span class="filter-switch-slider"></span>
                    </label>
                  </div>
                </div>

                <div class="filter-divider"></div>

                <!-- Category Filter -->
                <div class="mb-3">
                  <h6 class="fw-bold text-reactive-primary mb-3" style="font-size: 14px">Thể loại</h6>
                  <div class="filter-tags">
                    <button
                      v-for="category in categories"
                      :key="category.value"
                      :class="['filter-tag', { active: selectedCategories.includes(category.value) }]"
                      @click="toggleCategory(category.value)"
                    >
                      {{ category.label }}
                    </button>
                  </div>
                </div>

                <!-- Action Buttons -->
                <div class="filter-actions">
                  <button class="btn btn-reset" @click="resetMainFilter">
                    Thiết lập lại
                  </button>
                  <button class="btn btn-apply" @click="applyMainFilter">
                    Áp dụng
                  </button>
                </div>
              </div>
            </div>
          </div>
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
            class="card-interactive"
            role="button"
            tabindex="0"
            @click="handleEventClick(event.id)"
            @keypress.enter="handleEventClick(event.id)"
          >
            <!-- Event Image -->
            <div class="event-card-img mb-3">
              <img
                :src="event.image"
                :alt="event.title"
                loading="lazy"
              />
            </div>

            <!-- Event Info -->
            <div>
              <h3 class="fw-bold mb-2 text-reactive-primary text-ellipsis-2" style="font-size: 20px; min-height: 52px">
                {{ event.title }}
              </h3>
              <p class="text-primary fw-bold mb-1" style="font-size: 16px">
                {{ event.price }}
              </p>
              <p class="text-reactive-secondary mb-0 d-flex align-items-center gap-1" style="font-size: 16px">
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
        <p class="text-reactive-secondary mb-0">Không tìm thấy sự kiện nào</p>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
/* Component-specific styles that can't be replaced with utilities */

// Preset options layout - specific to date picker
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
  color: var(--text-reactive-primary);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;

  &:hover {
    background-color: var(--bg-reactive-secondary);
  }

  &.active {
    background-color: #07b3df;
    color: white;
  }
}

// Date pickers layout
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
    color: var(--text-reactive-primary);
    font-size: 14px;
    margin-bottom: 0.5rem;
  }

  input[type='date'] {
    width: 100%;
    background-color: var(--bg-reactive-primary);
    border: 1px solid var(--bg-reactive-gray);
    color: var(--text-reactive-primary);
    padding: 0.5rem;
    border-radius: 8px;
    font-size: 14px;

    &::-webkit-calendar-picker-indicator {
      cursor: pointer;
    }

    &:focus {
      outline: none;
      border-color: #07b3df;
      box-shadow: 0 0 0 2px rgba(7, 179, 223, 0.2);
    }
  }
}

[data-bs-theme='dark'] .date-input-group input[type='date']::-webkit-calendar-picker-indicator {
  filter: invert(1);
}

// Date filter action buttons
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
    border: 1px solid var(--bg-reactive-gray);
    color: var(--text-reactive-primary);

    &:hover {
      background-color: var(--bg-reactive-secondary);
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

// Filter divider
.filter-divider {
  border-top: 1px dashed var(--bg-reactive-gray);
  margin: 1rem 0;
}

// Radio button group
.filter-radio-group {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.filter-radio-label {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  user-select: none;
}

.filter-radio-input {
  position: absolute;
  opacity: 0;
  pointer-events: none;
}

.filter-radio-custom {
  width: 18px;
  height: 18px;
  border: 2px solid var(--bg-reactive-gray);
  border-radius: 50%;
  position: relative;
  transition: all 0.2s ease;

  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%) scale(0);
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: #07b3df;
    transition: transform 0.2s ease;
  }
}

.filter-radio-input:checked + .filter-radio-custom {
  border-color: #07b3df;

  &::after {
    transform: translate(-50%, -50%) scale(1);
  }
}

.filter-radio-text {
  color: var(--text-reactive-primary);
  font-size: 14px;
}

// Toggle switch
.filter-switch {
  position: relative;
  display: inline-block;
  width: 44px;
  height: 24px;
  cursor: pointer;

  input {
    opacity: 0;
    width: 0;
    height: 0;
  }
}

.filter-switch-slider {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--bg-reactive-gray);
  border-radius: 24px;
  transition: all 0.3s ease;

  &::before {
    content: '';
    position: absolute;
    height: 18px;
    width: 18px;
    left: 3px;
    bottom: 3px;
    background-color: white;
    border-radius: 50%;
    transition: transform 0.3s ease;
  }
}

.filter-switch input:checked + .filter-switch-slider {
  background-color: #07b3df;

  &::before {
    transform: translateX(20px);
  }
}

// Category tags
.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.filter-tag {
  background-color: transparent;
  border: 1px solid var(--bg-reactive-gray);
  color: var(--text-reactive-primary);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background-color: var(--bg-reactive-secondary);
  }

  &.active {
    background-color: #07b3df;
    border-color: #07b3df;
    color: white;
  }
}

// Filter actions
.filter-actions {
  display: flex;
  gap: 0.75rem;
  margin-top: 1rem;

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
    border: 1px solid var(--bg-reactive-gray);
    color: var(--text-reactive-primary);

    &:hover {
      background-color: var(--bg-reactive-secondary);
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

// Responsive adjustments for popup width
@media (max-width: 767.98px) {
  .dropdown-popup-dark {
    min-width: 70vw !important;
    left: 0 !important;
    right: auto !important;
    transform: none !important;
  }
  
  // Responsive filter buttons
  .btn-filter-primary,
  .btn-filter-secondary {
    font-size: 14px !important;
    padding: 0.5rem 0.75rem !important;
    
    .bi {
      font-size: 16px !important;
    }
  }
}

// Responsive font sizes for event cards
@media (max-width: 991.98px) {
  .text-ellipsis-2 {
    font-size: 18px !important;
    min-height: 47px !important;
  }
  
  h3 + p {
    font-size: 14px !important;
  }
}

@media (max-width: 767.98px) {
  .text-ellipsis-2 {
    font-size: 16px !important;
    min-height: 42px !important;
  }
  
  h3 + p {
    font-size: 14px !important;
  }
}
</style>
