import type { SelectedSeat } from './ticket.type'

export interface CartItem {
  zoneId: string
  name: string
  quantity: number
  price: number
  isStanding: boolean
  seats?: SelectedSeat[]  // only for seated zones
}