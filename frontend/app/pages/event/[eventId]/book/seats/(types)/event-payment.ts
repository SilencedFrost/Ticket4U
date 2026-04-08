// TODO: refactor to features/
import type { SelectedSeat } from './ticket'

export interface CartItem {
  zoneId:     string
  name:       string
  quantity:   number
  price:      number
  isStanding: boolean
  seats?:     SelectedSeat[]
}
