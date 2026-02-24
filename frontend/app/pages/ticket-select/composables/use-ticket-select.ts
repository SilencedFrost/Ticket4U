import { ref } from 'vue'
import axios from 'axios'
import type { Ticket } from '../types/ticket.type'
import type { Event } from '../types/event.type'
import type { SeatingLayout } from '../types/seating-layout.type.ts'

// Shape returned by GET /ticket-select?eventId=xxx
interface TicketSelectResponse {
  event: {
    id: string
    name: string
    startDate: string
    endDate: string
    addressLine: string
    seatingLayout: SeatingLayout | null
  }
  zones: {
    id: string
    name: string
    price: number
    capacity: number
    quantitySold: number
    purchaseLimit: number | null
    color?: string
  }[]
}

// Fallback zone colors if backend doesn't provide them
const FALLBACK_COLORS = [
  '#E53E3E', '#06B6D4', '#22D3EE', '#D69E2E',
  '#805AD5', '#38A169', '#DD6B20', '#3182CE',
]

const mapResponse = (data: TicketSelectResponse): {
  event: Event
  tickets: Ticket[]
  seatingLayout: SeatingLayout | null
} => {
  const start = new Date(data.event.startDate)
  const end = new Date(data.event.endDate)

  const event: Event = {
    id: data.event.id,
    title: data.event.name,
    date: start.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
    time: `${start.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}`,
    venue: data.event.addressLine,
  }

  const tickets: Ticket[] = data.zones.map((zone, i) => {
    const available = zone.capacity - zone.quantitySold
    return {
        id: zone.id,
        name: zone.name,
        price: zone.price,
        color: zone.color ?? FALLBACK_COLORS[i % FALLBACK_COLORS.length] ?? '#000000', // Add fallback to ensure string
        zone: zone.name,
        available,
        soldOut: available <= 0,
        maxPerAccount: zone.purchaseLimit,
    }
    })

  return { event, tickets, seatingLayout: data.event.seatingLayout ?? null }
}

export const useTicketSelect = () => {
  const event = ref<Event | null>(null)
  const tickets = ref<Ticket[]>([])
  const seatingLayout = ref<SeatingLayout | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const fetchTicketSelect = async (eventId: string) => {
    loading.value = true
    error.value = null
    try {
      const { data } = await axios.get<TicketSelectResponse>(
        'http://localhost:8080/ticket-select',
        { params: { eventId } }
      )
      const mapped = mapResponse(data)
      event.value = mapped.event
      tickets.value = mapped.tickets
      seatingLayout.value = mapped.seatingLayout
    } catch (err: any) {
      error.value = err?.response?.data?.message ?? err?.message ?? 'Failed to load event data'
    } finally {
      loading.value = false
    }
  }

  return { event, tickets, seatingLayout, loading, error, fetchTicketSelect }
}