import type { LocationOption, CategoryOption, DatePreset } from '../types'

export const locations: LocationOption[] = [
  { label: 'Toàn quốc', value: '' },
  { label: 'Hồ Chí Minh', value: 'hcm' },
  { label: 'Hà Nội', value: 'hanoi' },
  { label: 'Đà Lạt', value: 'dalat' },
  { label: 'Vị trí khác', value: 'other' },
]

export const categories: CategoryOption[] = [
  { label: 'Nhạc sống', value: 'music' },
  { label: 'Sân khấu & Nghệ thuật', value: 'theatersandart' },
  { label: 'Thể Thao', value: 'sport' },
  { label: 'Khác', value: 'others' },
]

export const datePresets: DatePreset[] = [
  { label: 'Tất cả các ngày', value: 'all' },
  { label: 'Hôm nay', value: 'today' },
  { label: 'Ngày mai', value: 'tomorrow' },
  { label: 'Cuối tuần này', value: 'this-weekend' },
  { label: 'Tháng này', value: 'this-month' },
]
