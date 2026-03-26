export interface Ticket {
  id:             string
  name:           string
  price:          number
  color:          string
  zone:           string
  available:      number
  soldOut:        boolean
  maxPerAccount:  number | null
  isStanding:     boolean
  descriptionVi?: string
  descriptionEn?: string
  giftImageUrl?:  string
  perks?:         string[]
}

export interface SelectedSeat {
  seatId:   string    // seatCode e.g. "A1" — matches DB seatCode field
  seatName: string    // display name e.g. "Seat A1"
  seatUuid: string    // actual DB UUID — used for booking
}
