// Backend response types
// Note: EventCardResponse matches the Event interface in home.d.ts
// No mapping needed - frontend uses backend field names directly

export interface CategoryResponse {
  id: number
  name: string
}

export interface EventCardResponse {
  id: string // UUID as string
  name: string
  bannerUrl: string
  addressLine: string
  startDate: string // ISO timestamp
  endDate: string
  minPrice: number
  categoryName?: string
}

// Backend response type for places
export interface PlaceResponse {
  id: string
  name: string
  imageUrl: string
}

// Backend response type for categories with events
export interface CategoryWithEventsResponse {
  id: number
  name: string
  events: EventCardResponse[]
}
