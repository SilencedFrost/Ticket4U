import type { LocationOption, DatePreset } from '../types/event-display'

export const locations: LocationOption[] = [
  { label: 'Toàn quốc', value: '' },
  { label: 'Hồ Chí Minh', value: 'hcm' },
  { label: 'Hà Nội', value: 'hanoi' },
  { label: 'Đà Lạt', value: 'dalat' },
  { label: 'Vị trí khác', value: 'other' },
]

// Categories are now fetched dynamically from backend
// See eventDisplayStore.fetchCategories()

export const datePresets: DatePreset[] = [
  { label: 'all_date', value: 'all' },
  { label: 'today', value: 'today' },
  { label: 'tomorrow', value: 'tomorrow' },
  { label: 'this_weekend', value: 'this-weekend' },
  { label: 'this_month', value: 'this-month' },
]
