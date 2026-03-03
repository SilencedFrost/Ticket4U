import { ref } from 'vue'
import axios from 'axios'
import type { Ticket } from '../(types)/ticket.type'
import type { Event } from '../(types)/event.type'
import type { Floor, SeatingLayout } from '../(types)/seating-layout.type'

// ── Backend response shape ─────────────────────────────────
interface ZoneResponse {
  id: string
  name: string
  price: number
  capacity: number
  quantitySold: number
  purchaseLimit: number | null
  color?: string
  isStanding: boolean
}

interface TicketSelectResponse {
  eventId: string
  name: string
  startDate: string
  endDate: string
  addressLine: string
  zones: ZoneResponse[]
  floors: FloorResponse[]   // new — from event_layouts + venue_layouts
}

interface FloorResponse {
  id: string
  floorName: string
  floorOrder: number
  layoutJson: SeatingLayout        // venue_layouts.layout_json
  modifications: ModificationsJson // event_layout_modifications.modifications_json
}

interface ModificationsJson {
  zones: Record<string, {
    zone_id: string
    name?: { new: string }
    type?: { new: string }
    seats?: Record<string, string>  // seat_id → seat UUID
  }>
}

// ── Merge layout with modifications ───────────────────────
const resolveFloor = (floor: FloorResponse): Floor => {
  const layoutJson = typeof floor.layoutJson === 'string' ? JSON.parse(floor.layoutJson) : floor.layoutJson
  const modsJson = typeof floor.modifications === 'string' ? JSON.parse(floor.modifications) : floor.modifications
  const mods = modsJson?.zones ?? {}

  const resolvedZones = layoutJson.zones.map((zone: any) => {
    const mod = mods[zone.zone_name]
    return {
      ...zone,
      zone_uuid:    mod?.zone_id ?? undefined,
      display_name: mod?.name?.new ?? zone.zone_name,
      zone_type:    (mod?.type?.new as 'standing' | 'sitting') ?? zone.zone_type,
      seats: zone.seats.map(seat => ({
        ...seat,
        seat_uuid: mod?.seats?.[seat.seat_id] ?? undefined,
      }))
    }
  })

  return {
    id:          floor.id,
    floor_name:  floor.floorName,
    floor_order: floor.floorOrder,
    layout: {
      stage: layoutJson.stage,
      zones: resolvedZones,
    }
  }
}

// ── Fallback colors ────────────────────────────────────────
const FALLBACK_COLORS = [
  '#E53E3E', '#06B6D4', '#22D3EE', '#D69E2E',
  '#805AD5', '#38A169', '#DD6B20', '#3182CE',
]

// ── Mock floors for development (remove when backend is ready) ─
const MOCK_FLOORS: FloorResponse[] = [
  {
    id: 'mock-floor-1',
    floorName: 'Main Floor',
    floorOrder: 1,
    layoutJson: {
      stage: { x1: -0.4, y1: -1.0, x2: 0.4, y2: -0.85 },
      zones: [
        {
          zone_name: 'VIP',
          zone_type: 'sitting',
          color: '#DC2626',
          corner1: { x: -0.85, y: -0.82 },
          corner2: { x:  0.85, y: -0.82 },
          corner3: { x:  0.85, y: -0.2  },
          corner4: { x: -0.85, y: -0.2  },
          seats: [
            { seat_name: 'Ghế A1', seat_id: 'A1', seat_pos: { x: -0.65, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'Ghế A2', seat_id: 'A2', seat_pos: { x: -0.4,  y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'Ghế A3', seat_id: 'A3', seat_pos: { x: -0.15, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'Ghế A4', seat_id: 'A4', seat_pos: { x:  0.15, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'Ghế A5', seat_id: 'A5', seat_pos: { x:  0.4,  y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'Ghế A6', seat_id: 'A6', seat_pos: { x:  0.65, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'Ghế B1', seat_id: 'B1', seat_pos: { x: -0.65, y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'Ghế B2', seat_id: 'B2', seat_pos: { x: -0.4,  y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'Ghế B3', seat_id: 'B3', seat_pos: { x: -0.15, y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'Ghế B4', seat_id: 'B4', seat_pos: { x:  0.15, y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'Ghế B5', seat_id: 'B5', seat_pos: { x:  0.4,  y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'Ghế B6', seat_id: 'B6', seat_pos: { x:  0.65, y: -0.45 }, seat_rotation: 0 },
          ]
        },
        {
          zone_name: 'GA',
          zone_type: 'standing',
          color: '#2563EB',
          corner1: { x: -0.85, y: -0.15 },
          corner2: { x:  0.85, y: -0.15 },
          corner3: { x:  0.85, y:  0.9  },
          corner4: { x: -0.85, y:  0.9  },
          seats: []
        }
      ]
    },
    modifications: {
      zones: {
        'VIP': { zone_id: 'mock-vip-zone-uuid', name: { new: 'Khu VVIP (Gần ca sĩ)' } },
        'GA':  { zone_id: 'mock-ga-zone-uuid',  name: { new: 'Khu Khán Đài' } }
      }
    }
  }
]

// ── Map response ───────────────────────────────────────────
const mapResponse = (data: TicketSelectResponse): {
  event: Event
  tickets: Ticket[]
  floors: Floor[]
} => {
  const start = new Date(data.startDate)
  const end   = new Date(data.endDate)

  const event: Event = {
    id:    data.eventId,
    title: data.name,
    date:  start.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
    time:  `${start.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}`,
    venue: data.addressLine,
  }

  const tickets: Ticket[] = data.zones.map((zone, i) => {
    const available = zone.capacity - zone.quantitySold
    return {
      id:            zone.id,
      name:          zone.name,
      price:         zone.price,
      color:         zone.color ?? FALLBACK_COLORS[i % FALLBACK_COLORS.length] ?? '#000000',
      zone:          zone.name,
      available,
      soldOut:       available <= 0,
      maxPerAccount: zone.purchaseLimit,
      isStanding:    zone.isStanding,
    }
  })

  // Use real floors if backend returns them, otherwise use mock
  const rawFloors = (data.floors && data.floors.length > 0) ? data.floors : MOCK_FLOORS
  const floors = rawFloors
    .sort((a, b) => a.floorOrder - b.floorOrder)
    .map(resolveFloor)

  return { event, tickets, floors }
}

// ── Composable ─────────────────────────────────────────────
export const useTicketSelect = () => {
  const event   = ref<Event | null>(null)
  const tickets = ref<Ticket[]>([])
  const floors  = ref<Floor[]>([])
  const loading = ref(false)
  const error   = ref<string | null>(null)

  const fetchTicketSelect = async (eventId: string) => {
    loading.value = true
    error.value   = null
    try {
      const { data } = await axios.get<TicketSelectResponse>(
        `https://localhost:8081/ticket-select/${eventId}`
      )
      const mapped  = mapResponse(data)
      event.value   = mapped.event
      tickets.value = mapped.tickets
      floors.value  = mapped.floors
    } catch (err: any) {
      error.value = err?.response?.data?.message ?? err?.message ?? 'Failed to load event data'
    } finally {
      loading.value = false
    }
  }

  return { event, tickets, floors, loading, error, fetchTicketSelect }
}