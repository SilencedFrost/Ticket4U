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
  seatId: string    // e.g. "A1"
  seatName: string  // e.g. "Ghế A1"
  seatUuid: string  // actual UUID from seats table
}

export interface SelectedTicket {
  ticket: Ticket
  quantity: number
}