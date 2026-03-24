import { ref } from 'vue'
import type { Ticket } from '../(types)/ticket.type'
import type { Event } from '../(types)/event.type'
import type { Floor, LayoutZone, LayoutSeat } from '../(types)/seating-layout.type'

// ── Backend response shape ─────────────────────────────────
interface EventResponse {
  id: string
  name: string
  addressLine: string
  bannerUrl: string
  startDate: string
  endDate: string
  aboutVi?: string
  aboutEn?: string
}
interface ZoneResponse {
  id:             string
  name:           string
  price:          number
  available:      number
  descriptionVi?: string
  descriptionEn?: string
  giftImageUrl?:  string
  perks?:         string[]
}

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

interface SeatingPlanResponse {
  layout: string | null
  zones: ZoneResponse[]
  seats: SeatResponse[]
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

// ── Build floors from layout JSON + seats ──────────────────
const buildFloors = (
    layoutJson: string | null,
    zones: ZoneResponse[],
    seats: SeatResponse[]
): Floor[] => {
  if (!layoutJson) return []

  let parsed: { venueMode?: boolean; floors?: RawLayoutFloor[]; stage?: RawLayoutFloor['stage']; zones?: RawLayoutZone[] }
  try { parsed = JSON.parse(layoutJson) as typeof parsed }
  catch { return [] }

  // Venue marker without floors — backend should have resolved but guard anyway
  if (parsed.venueMode) return []

  // Handle both multi-floor { floors: [...] } and single-floor { stage, zones } shapes
  let rawFloors: RawLayoutFloor[]
  if (parsed.floors && parsed.floors.length > 0) {
    rawFloors = parsed.floors
  } else if (parsed.stage || parsed.zones) {
    // Flat single-floor venue layout
    rawFloors = [{ floor_order: 1, floor_name: 'Main Floor', stage: parsed.stage as RawLayoutFloor['stage'], zones: parsed.zones }]
  } else {
    return []
  }

  // Seat lookup by zoneId
  const seatsByZone = new Map<string, SeatResponse[]>()
  for (const seat of seats) {
    if (!seatsByZone.has(seat.zoneId)) seatsByZone.set(seat.zoneId, [])
    seatsByZone.get(seat.zoneId)!.push(seat)
  }

  // Zone lookup by id
  const zoneById = new Map(zones.map(z => [z.id, z]))

  return rawFloors
      .sort((a, b) => (a.floor_order ?? 0) - (b.floor_order ?? 0))
      .map((fl): Floor => {
        const layoutZones: LayoutZone[] = (fl.zones ?? []).map((z): LayoutZone => {
          const zoneId    = z.zone_id ?? null
          const _zoneData = zoneId ? zoneById.get(zoneId) : undefined  // reserved for future use
          void _zoneData
          const zoneSeats = zoneId ? (seatsByZone.get(zoneId) ?? []) : []

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

// ── Map response ───────────────────────────────────────────
const mapResponse = (
  eventData: EventResponse,
  seatingData: SeatingPlanResponse,
  locale: string
): {
  event: Event; tickets: Ticket[]; floors: Floor[]
} => {
  const start = new Date(eventData.startDate)
  const end   = new Date(eventData.endDate)

  const event: Event = {
    id:    eventData.id,
    title: eventData.name,
    date:  start.toLocaleDateString(locale, { day: 'numeric', month: 'long', year: 'numeric' }),
    time:  `${start.toLocaleTimeString(locale, { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString(locale, { hour: '2-digit', minute: '2-digit' })}`,
    venue: eventData.addressLine,
  }

  const tickets: Ticket[] = seatingData.zones.map((zone, i) => ({
    id: zone.id,
    name: zone.name,
    price: zone.price,
    color: FALLBACK_COLORS[i % FALLBACK_COLORS.length] ?? '#6366f1',
    zone: zone.name,
    available: zone.available,
    soldOut: zone.available <= 0,
    maxPerAccount: null,
    isStanding: false,
    descriptionVi: zone.descriptionVi,
    descriptionEn: zone.descriptionEn,
    giftImageUrl: zone.giftImageUrl,
    perks: zone.perks,
  }))

  const floors = buildFloors(
    seatingData.layout,
    seatingData.zones,
    seatingData.seats
  )

  // Back-fill isStanding + color from layout zone data
  if (floors.length > 0) {
    for (const ticket of tickets) {
      const layoutZone = floors[0]?.layout.zones.find(z => z.zone_uuid === ticket.id)
      if (layoutZone) {
        ticket.isStanding = layoutZone.zone_type === 'standing'
        ticket.color      = layoutZone.color
      }
    }
  }

  return { event, tickets, floors }
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
      const [eventRes, seatingRes] = await Promise.all([
        $fetch<EventResponse>(`${config.public.apiUrl}/public/events/${eventId}`),
        $fetch<SeatingPlanResponse>(`${config.public.apiUrl}/public/events/${eventId}/seating-plan`)
      ])

      const mapped = mapResponse(eventRes, seatingRes, locale.value)

      event.value   = mapped.event
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