export interface Ticket {
  id:             string
  name:           string
  price:          number
  color:          string
  zone:           string
  capacity:      number
  soldOut:        boolean
  maxPerAccount:  number | null
  isStanding:     boolean
  descriptionVi?: string
  descriptionEn?: string
  giftImageUrl?:  string
  perks?:         string[]
}

export interface SelectedSeat {
  seatId:    string
  seatName:  string
  seatUuid:  string
  zoneUuid?: string
  zoneName:  string
  zoneColor: string
  price:     number
}