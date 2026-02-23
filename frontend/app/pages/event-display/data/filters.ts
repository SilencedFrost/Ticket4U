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
  { label: 'Tất cả các ngày', value: 'all' },
  { label: 'Hôm nay', value: 'today' },
  { label: 'Ngày mai', value: 'tomorrow' },
  { label: 'Cuối tuần này', value: 'this-weekend' },
  { label: 'Tháng này', value: 'this-month' },
]
