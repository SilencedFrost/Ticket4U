<template>
  <div class="event-display bg-reactive-primary" style="min-height: 100vh">
    <div class="container-xxl py-5">
      <!-- Header section with filters -->
      <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
        <h2 class="text-primary fw-normal mb-0 d-none d-md-block" style="font-size: 20px">
          Kết quả tìm kiếm
        </h2>

        <div class="d-flex gap-2 gap-md-3 ms-auto">
          <DateRangeFilter
            :show="showDateFilter"
            :start-date="startDate"
            :end-date="endDate"
            :selected-preset="selectedPreset"
            :date-range-label="formatDateRange()"
            :presets="datePresets"
            :is-mobile="isMobile"
            @toggle="toggleDateFilter"
            @select-preset="selectPreset"
            @update:start-date="startDate = $event"
            @update:end-date="endDate = $event"
            @reset="resetDateFilter"
            @apply="applyDateFilter"
          />

          <MainFilter
            :show="showMainFilter"
            :selected-location="selectedLocation"
            :is-free-event="isFreeEvent"
            :selected-categories="selectedCategories"
            :locations="locations"
            :categories="categories"
            :is-mobile="isMobile"
            :popup-style="mainFilterPopupStyle"
            @toggle="toggleMainFilter"
            @update:selected-location="selectedLocation = $event"
            @update:is-free-event="isFreeEvent = $event"
            @toggle-category="toggleCategory"
            @reset="resetMainFilter"
            @apply="applyMainFilter"
          />
        </div>
      </div>

      <!-- Events Grid -->
      <EventGrid :events="displayEvents" @event-click="handleEventClick" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import DateRangeFilter from './event-display/DateRangeFilter.vue'
import MainFilter from './event-display/MainFilter.vue'
import EventGrid from './event-display/EventGrid.vue'
import { useDateRange } from '~/composables/useDateRange'
import { useEventFilter } from '~/composables/useEventFilter'
import { locations, categories, datePresets } from '~/data/filters'
import type { Event } from '~/types/home'

interface Props {
  events?: Event[]
}

const props = withDefaults(defineProps<Props>(), {
  events: () => [],
})

// Sample events data
const sampleEvents: Event[] = [
  {
    id: '1',
    imageUrl: 'https://www.figma.com/api/mcp/asset/a25dbc73-4601-4464-974d-5b26b1fa53e1',
    title: 'SOOBIN LIVE CONCERT: ALL-ROUNDER THE FINAL',
    price: 100000,
    date: '26 tháng 11,2025',
  },
  {
    id: '2',
    imageUrl: 'https://www.figma.com/api/mcp/asset/ab959769-fc96-47d3-8f3c-23e614caeba7',
    title: '[TP.HCM] Những Thành Phố Mơ Màng Year End 2025',
    price: 900000,
    date: '31 tháng 12,2025',
  },
  {
    id: '3',
    imageUrl: 'https://www.figma.com/api/mcp/asset/2591db95-936f-45e0-81fa-914d51f89315',
    title: 'ANH TRAI "SAY HI" 2025 CONCERT',
    price: 1040000,
    date: '25 tháng 11,2025',
  },
  {
    id: '4',
    imageUrl: 'https://www.figma.com/api/mcp/asset/cc53c02f-5fc1-4d8d-bed6-0e18aae2bd23',
    title: '[CAT&MOUSE] CA SĨ VICKY NHUNG + CA SĨ PHAN DUY ANH',
    price: 375000,
    date: '25 Tháng 11,2025',
  },
]

// Duplicate sample events to have more items
const allSampleEvents = Array.from({ length: 4 }, (_, i) =>
  sampleEvents.map((e) => ({ ...e, id: e.id + i * sampleEvents.length }))
).flat()

// Mobile state
const isMobile = ref(false)

// Date range composable
const {
  startDate,
  endDate,
  selectedPreset,
  selectPreset,
  formatDateRange,
  filterEventsByDate,
  reset: resetDateRange,
} = useDateRange()

// Event filter composable
const {
  selectedLocation,
  isFreeEvent,
  selectedCategories,
  toggleCategory,
  reset: resetEventFilter,
  apply: applyEventFilter,
} = useEventFilter()

// Filter visibility
const showDateFilter = ref(false)
const showMainFilter = ref(false)

// Popup style for main filter
const mainFilterPopupStyle = ref<Record<string, string>>({})

// Computed filtered events
const displayEvents = computed(() => {
  const events = props.events.length > 0 ? props.events : allSampleEvents
  return filterEventsByDate(events)
})

// Toggle handlers
const toggleDateFilter = () => {
  showDateFilter.value = !showDateFilter.value
  if (showMainFilter.value) showMainFilter.value = false
}

const toggleMainFilter = () => {
  showMainFilter.value = !showMainFilter.value
  if (showDateFilter.value) showDateFilter.value = false
  nextTick(() => {
    adjustPopupPosition()
  })
}

// Filter handlers
const resetDateFilter = () => {
  resetDateRange()
}

const applyDateFilter = () => {
  showDateFilter.value = false
  console.log('Apply date filter:', startDate.value, endDate.value)
}

const resetMainFilter = () => {
  resetEventFilter()
}

const applyMainFilter = () => {
  showMainFilter.value = false
  const filters = applyEventFilter()
  console.log('Apply filters:', filters)
}

// Event click handler
const handleEventClick = (eventId: string) => {
  console.log('Navigate to event:', eventId)
  // navigateTo(`/event/${eventId}`)
}

// Close popup when clicking outside
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  if (
    showDateFilter.value &&
    !target.closest('.dropdown-popup') &&
    !target.closest('.btn-filter-primary')
  ) {
    showDateFilter.value = false
  }
  if (
    showMainFilter.value &&
    !target.closest('.dropdown-popup') &&
    !target.closest('.btn-filter-secondary')
  ) {
    showMainFilter.value = false
  }
}

// Adjust popup position for mobile
const adjustPopupPosition = () => {
  if (!isMobile.value || !showMainFilter.value) return

  const button = document.querySelector('.btn-filter-secondary') as HTMLElement
  const popup = document.querySelector('.dropdown-popup') as HTMLElement

  if (!button || !popup) return

  const buttonRect = button.getBoundingClientRect()
  const viewportHeight = window.innerHeight

  const spaceBelow = viewportHeight - buttonRect.bottom
  const spaceAbove = buttonRect.top

  mainFilterPopupStyle.value = {}

  if (spaceBelow < 400 && spaceAbove > 400) {
    mainFilterPopupStyle.value = {
      top: 'auto',
      bottom: 'calc(100% + 8px)',
      maxHeight: `${spaceAbove - 16}px`,
    }
  } else {
    mainFilterPopupStyle.value = {
      maxHeight: `${spaceBelow - 16}px`,
    }
  }
}

// Viewport detection
const updateMobileState = () => {
  isMobile.value = window.innerWidth < 768
  if (showMainFilter.value) {
    nextTick(() => adjustPopupPosition())
  }
}

onMounted(() => {
  updateMobileState()
  window.addEventListener('resize', updateMobileState)
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  window.removeEventListener('resize', updateMobileState)
  document.removeEventListener('click', handleClickOutside)
})
</script>
