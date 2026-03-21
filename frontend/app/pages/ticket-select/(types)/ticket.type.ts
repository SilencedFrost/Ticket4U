export interface Ticket {
  id: string
  name: string
  price: number
  color: string
  zone: string
  available: number
  soldOut: boolean
  maxPerAccount: number | null
  isStanding: boolean
}

export interface SelectedSeat {
  seatId:   string    // seatCode e.g. "A1"
  seatName: string
  seatUuid: string    // actual DB UUID
}

export interface SelectedTicket {
  ticket:   Ticket
  quantity: number
}