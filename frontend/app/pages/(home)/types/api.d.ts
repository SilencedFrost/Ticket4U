// Backend response types
// Note: EventCardDTO matches the Event interface in home.d.ts
// No mapping needed - frontend uses backend field names directly
export interface EventCardDTO {
  id: string // UUID as string
  name: string
  bannerUrl: string
  addressLine: string
  startDate: string // ISO timestamp
  endDate: string
  minPrice: number
  categoryName?: string
  status?: string
}

// Backend response type for places
export interface PlaceDTO {
  id: string
  name: string
  imageUrl: string
}
