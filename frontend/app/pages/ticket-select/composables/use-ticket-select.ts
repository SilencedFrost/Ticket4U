import { ref } from 'vue'
import type { Ticket } from '../(types)/ticket.type'
import type { Event } from '../(types)/event.type'
import type { Floor, LayoutZone, LayoutSeat } from '../(types)/seating-layout.type'

// ── Backend response shape ─────────────────────────────────
interface SeatResponse {
  id:            string
  zoneId:        string
  seatCode:      string
  rowName:       string
  colName:       string
  name:          string
  status:        string
  priceOverride: number | null
}

interface ZoneResponse {
  id:             string
  name:           string
  price:          number
  available:      number
  isStanding:     boolean
  descriptionVi?: string
  descriptionEn?: string
  giftImageUrl?:  string
  perks?:         string[]
  seats:          SeatResponse[]
}

// Matches SeatingPlanResponse on backend
interface SeatingPlanResponse {
  layout:  string | null
  zones:   ZoneResponse[]
  seats:   SeatResponse[]
}

// Minimal event info for the header
interface EventInfoResponse {
  id:          string
  name:        string
  addressLine: string
  startDate:   string
  endDate:     string
}

// ── Parsed layout floor shape ──────────────────────────────
interface RawLayoutFloor {
  floor_order?:      number
  floor_name?:       string
  global_seat_size?: number
  stage?:            { x1: number; y1: number; x2: number; y2: number }
  zones?:            RawLayoutZone[]
}

interface RawLayoutZone {
  zone_id?:    string | null
  zone_name?:  string
  accessible?: boolean
  color?:      string
  rotation?:   number
  corner1:     { x: number; y: number }
  corner2:     { x: number; y: number }
  corner3:     { x: number; y: number }
  corner4:     { x: number; y: number }
}

// ── Fallback colors ────────────────────────────────────────
const FALLBACK_COLORS = [
  '#E53E3E', '#06B6D4', '#22D3EE', '#D69E2E',
  '#805AD5', '#38A169', '#DD6B20', '#3182CE',
]

// ── Build floors from layout JSON ──────────────────────────
const buildFloors = (
    layoutJson: string | null,
    zones: ZoneResponse[]
): Floor[] => {
  if (!layoutJson) return []

  let parsed: { venueMode?: boolean; floors?: RawLayoutFloor[]; stage?: RawLayoutFloor['stage']; zones?: RawLayoutZone[] }
  try { parsed = JSON.parse(layoutJson) as typeof parsed }
  catch { return [] }

  if (parsed.venueMode) return []

  let rawFloors: RawLayoutFloor[]
  if (parsed.floors && parsed.floors.length > 0) {
    rawFloors = parsed.floors
  } else if (parsed.stage || parsed.zones) {
    rawFloors = [{ floor_order: 1, floor_name: 'Main Floor', stage: parsed.stage as RawLayoutFloor['stage'], zones: parsed.zones }]
  } else {
    return []
  }

  // Zone lookup by id — seats live inside each zone
  const zoneById = new Map(zones.map(z => [z.id, z]))

  return rawFloors
      .sort((a, b) => (a.floor_order ?? 0) - (b.floor_order ?? 0))
      .map((fl): Floor => {
        const layoutZones: LayoutZone[] = (fl.zones ?? []).map((z): LayoutZone => {
          const zoneId   = z.zone_id ?? null
          const zoneData = zoneId ? zoneById.get(zoneId) : undefined
          const zoneSeats = zoneData?.seats ?? []

          const layoutSeats: LayoutSeat[] = zoneSeats.map((s): LayoutSeat => ({
            seat_name:     s.name,
            seat_id:       s.seatCode,
            seat_uuid:     s.id,
            seat_pos:      { x: 0, y: 0 },
            seat_rotation: 0,
            status:        (s.status as 'AVAILABLE' | 'BOOKED' | 'HOLD') ?? 'AVAILABLE',
            priceOverride: s.priceOverride,
          }))

          return {
            zone_name:    z.zone_name ?? 'Zone',
            zone_type:    z.accessible !== false ? 'sitting' : 'standing',
            color:        z.color ?? FALLBACK_COLORS[0]!,
            corner1:      z.corner1,
            corner2:      z.corner2,
            corner3:      z.corner3,
            corner4:      z.corner4,
            seats:        layoutSeats,
            zone_uuid:    zoneId ?? undefined,
            display_name: z.zone_name,
            rotation:     z.rotation ?? 0,
          }
        })

        return {
          id:          String(fl.floor_order ?? 1),
          floor_name:  fl.floor_name ?? 'Floor',
          floor_order: fl.floor_order ?? 1,
          layout: {
            seat_size: fl.global_seat_size ?? 14,
            stage:     fl.stage ?? { x1: -0.3, y1: -1.0, x2: 0.3, y2: -0.85 },
            zones:     layoutZones,
          },
        }
      })
}

// ── Map seating plan ───────────────────────────────────────
const mapSeatingPlan = (data: SeatingPlanResponse): {
  tickets: Ticket[]; floors: Floor[]
} => {
  const tickets: Ticket[] = data.zones.map((zone, i): Ticket => ({
    id:             zone.id,
    name:           zone.name,
    price:          zone.price,
    color:          FALLBACK_COLORS[i % FALLBACK_COLORS.length] ?? '#6366f1',
    zone:           zone.name,
    available:      zone.available,
    soldOut:        zone.available <= 0,
    maxPerAccount:  null,
    isStanding:     zone.isStanding ?? false,
    descriptionVi:  zone.descriptionVi,
    descriptionEn:  zone.descriptionEn,
    giftImageUrl:   zone.giftImageUrl,
    perks:          zone.perks,
  }))

  const floors = buildFloors(data.layout, data.zones)

  // Back-fill color from layout zone data
  if (floors.length > 0) {
    for (const ticket of tickets) {
      const layoutZone = floors[0]?.layout.zones.find(z => z.zone_uuid === ticket.id)
      if (layoutZone) {
        ticket.color      = layoutZone.color
        ticket.isStanding = layoutZone.zone_type === 'standing'
      }
    }
  }

  return { tickets, floors }
}

// ── Composable ─────────────────────────────────────────────
export const useTicketSelect = () => {
  const event   = ref<Event | null>(null)
  const tickets = ref<Ticket[]>([])
  const floors  = ref<Floor[]>([])
  const loading = ref(false)
  const error   = ref<string | null>(null)

  const config = useRuntimeConfig()
  const { locale } = useI18n()

  const fetchTicketSelect = async (eventId: string) => {
    loading.value = true
    error.value   = null
    try {
      // Fetch event info and seating plan in parallel
      const [eventData, seatingData] = await Promise.all([
        $fetch<EventInfoResponse>(`${config.public.apiUrl}/public/events/${eventId}`),
        $fetch<SeatingPlanResponse>(`${config.public.apiUrl}/public/events/${eventId}/layout`),
      ])

      const start = new Date(eventData.startDate)
      const end   = new Date(eventData.endDate)
      event.value = {
        id:    eventData.id,
        title: eventData.name,
        date:  start.toLocaleDateString(locale.value, { day: 'numeric', month: 'long', year: 'numeric' }),
        time:  `${start.toLocaleTimeString(locale.value, { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString(locale.value, { hour: '2-digit', minute: '2-digit' })}`,
        venue: eventData.addressLine,
      }

      const mapped  = mapSeatingPlan(seatingData)
      tickets.value = mapped.tickets
      floors.value  = mapped.floors
    } catch (err: unknown) {
      const e = err as { data?: { message?: string }; message?: string }
      error.value = e?.data?.message ?? e?.message ?? 'Failed to load event data'
    } finally {
      loading.value = false
    }
  }

  return { event, tickets, floors, loading, error, fetchTicketSelect }
}