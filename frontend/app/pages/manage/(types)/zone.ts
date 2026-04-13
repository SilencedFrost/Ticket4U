export interface Zone {
  id:          string
  sessionId:   string
  name:        string
  isStanding:  boolean
  capacity:    number
  price:       number
  purchaseLimit?:  number | null
  descriptionVi?:  string
  descriptionEn?:  string
  giftImageUrl?:   string | null
  perks?:          string[]
  createdAt?:      string
  updatedAt?:      string | null

  /** frontend-only UI fields — not stored in DB */
  gridRows?:    number
  gridCols?:    number

  /** computed/derived from tickets — not in DB */
  quantitySold?: number
  seatCount?:    number
}

export interface ZoneFormState {
  name:          string
  isStanding:    boolean
  capacity:      number
  price:         number
  purchaseLimit: number | null
  descriptionVi: string
  descriptionEn: string
  giftImageUrl:  string | null
  perks:         string[]
  gridRows:      number
  gridCols:      number
}