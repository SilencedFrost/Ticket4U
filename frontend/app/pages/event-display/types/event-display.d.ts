export type { Event } from '~/features/home/types'

export interface LocationOption {
  label: string
  value: string
}

export interface CategoryOption {
  label: string
  value: string
}

export interface DatePreset {
  label: string
  value: string
}

export interface EventFilter {
  location: string
  isFree: boolean
  categories: string[]
  startDate: string
  endDate: string
}
