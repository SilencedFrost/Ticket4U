export interface Ticket {
  id: string
  name: string
  price: number
  color: string
  zone: string
  available: number
  soldOut: boolean
  maxPerAccount: number | null
}

export interface SelectedTicket {
  ticket: Ticket
  quantity: number
}