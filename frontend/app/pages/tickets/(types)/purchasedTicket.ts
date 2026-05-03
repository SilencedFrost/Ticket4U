export type TicketStatus = 'SOLD' | 'USED' | 'CANCELLED'

export interface PurchasedTicket {
  id: string
  orderId: string
  eventId: string
  eventName: string
  eventBannerUrl: {
    wide: string
    square: string
    tall: string
  }
  eventDate: string
  eventStartTime: string
  eventEndTime: string
  eventVenue: string
  seatName: string
  zoneName: string
  ticketType: string
  basePrice: number
  status: TicketStatus
  purchasedAt: string
}
