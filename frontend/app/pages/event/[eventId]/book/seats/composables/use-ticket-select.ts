import { ref } from 'vue'
import type { Ticket } from '../(types)/ticket'
import type { Event } from '../(types)/event'
import type { Floor, LayoutZone, LayoutSeat } from '../(types)/seating-layout'

// Backend response shapes
interface Seat {
  id:            string
  zoneId:        string
  seatCode:      string
  rowName:       string
  colName:       string
  name:          string
  status:        string
  priceOverride: number | null
}

interface Zone {
  id:             string
  name:           string
  price:          number
  isStanding:     boolean
  capacity:       number
  purchaseLimit:  number | null
  descriptionVi?: string
  descriptionEn?: string
  giftImageUrl?:  string
  perks?:         string[]
  seats:          Seat[]
}

interface SeatingPlan {
  layout: string | null
  zones:  Zone[]
}

interface EventInfo {
  id:          string
  name:        string
  addressLine: string
  startDate:   string
  endDate:     string
  sessions:    { id: string }[]
}

// Layout parsing shapes
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
  zone_type?:  'sitting' | 'standing'
  accessible?: boolean
  color?:      string
  rotation?:   number
  corner1:     { x: number; y: number }
  corner2:     { x: number; y: number }
  corner3:     { x: number; y: number }
  corner4:     { x: number; y: number }
}

// Constants
const FALLBACK_COLORS = [
  '#E53E3E', '#06B6D4', '#22D3EE', '#D69E2E',
  '#805AD5', '#38A169', '#DD6B20', '#3182CE',
]

// Layout builders
function buildLayoutSeats(zoneSeats: Seat[]): LayoutSeat[] {
  return zoneSeats.map((s): LayoutSeat => ({
    seat_name:     s.name,
    seat_id:       s.seatCode,
    seat_uuid:     s.id,
    seat_pos:      { x: 0, y: 0 },
    seat_rotation: 0,
    status:        (s.status as 'AVAILABLE' | 'BOOKED' | 'HOLD') ?? 'AVAILABLE',
    priceOverride: s.priceOverride,
  }))
}

function buildLayoutZone(z: RawLayoutZone, zoneById: Map<string, Zone>): LayoutZone {
  const zoneId   = z.zone_id ?? null
  const zoneData = zoneId ? zoneById.get(zoneId) : undefined

  // Use isStanding from the DB zone as source of truth — the layout JSON's
  // zone_type only describes polygon display style and can be wrong
  // (e.g. CIS Arena marks all zones as "standing" even for seated bleachers).
  // Fall back to z.zone_type only when no DB zone is linked.
  const zoneType: 'sitting' | 'standing' =
      zoneData != null
          ? (zoneData.isStanding ? 'standing' : 'sitting')
          : (z.zone_type ?? 'standing')

  return {
    ...z,
    zone_name:    z.zone_name ?? 'Zone',
    zone_type:    zoneType,
    color:        z.color ?? FALLBACK_COLORS[0]!,
    seats:        buildLayoutSeats(zoneData?.seats ?? []),
    zone_uuid:    zoneId ?? undefined,
    display_name: z.zone_name,
    rotation:     z.rotation ?? 0,
  }
}

function buildFloor(fl: RawLayoutFloor, zoneById: Map<string, Zone>): Floor {
  return {
    id:          String(fl.floor_order ?? 1),
    floor_name:  fl.floor_name ?? 'Floor',
    floor_order: fl.floor_order ?? 1,
    layout: {
      seat_size: fl.global_seat_size ?? 14,
      stage:     fl.stage ?? { x1: -0.3, y1: -1.0, x2: 0.3, y2: -0.85 },
      zones:     (fl.zones ?? []).map(z => buildLayoutZone(z, zoneById)),
    },
  }
}

function buildFloors(layoutJson: string | null, zones: Zone[]): Floor[] {
  if (!layoutJson) return []

  let parsed: { venueMode?: boolean; floors?: RawLayoutFloor[]; stage?: RawLayoutFloor['stage']; zones?: RawLayoutZone[] }
  try { parsed = JSON.parse(layoutJson) as typeof parsed }
  catch { return [] }

  if (parsed.venueMode) return []

  let rawFloors: RawLayoutFloor[]
  if (parsed.floors && parsed.floors.length > 0) {
    rawFloors = parsed.floors
  } else if (parsed.stage || parsed.zones) {
    rawFloors = [{ floor_order: 1, floor_name: 'Main Floor', stage: parsed.stage, zones: parsed.zones }]
  } else {
    return []
  }

  const zoneById = new Map(zones.map(zone => [zone.id, zone]))

  return rawFloors
      .sort((a, b) => (a.floor_order ?? 0) - (b.floor_order ?? 0))
      .map(fl => buildFloor(fl, zoneById))
}

function mapSeatingPlan(data: SeatingPlan): { tickets: Ticket[]; floors: Floor[] } {
  const tickets: Ticket[] = data.zones.map((zone, i): Ticket => ({
    ...zone,
    color:         FALLBACK_COLORS[i % FALLBACK_COLORS.length] ?? '#6366f1',
    zone:          zone.name,
    soldOut:       zone.capacity <= 0,
    maxPerAccount: zone.purchaseLimit ?? null,
    isStanding:    zone.isStanding ?? false,
  }))

  const floors = buildFloors(data.layout, data.zones)

  // Back-fill color from layout zone data (search all floors, not just floor 0).
  // Do NOT back-fill isStanding — it is already correct from DB zone.isStanding.
  // layout JSON zone_type is a display hint and can differ from the ticket type
  // (e.g. all CIS Arena zones say zone_type=standing even for seated bleachers).
  for (const ticket of tickets) {
    for (const floor of floors) {
      const layoutZone = floor.layout.zones.find(z => z.zone_uuid === ticket.id)
      if (layoutZone) { ticket.color = layoutZone.color; break }
    }
  }

  return { tickets, floors }
}

// Composable
export const useTicketSelect = () => {
  const event   = ref<Event | null>(null)
  const tickets = ref<Ticket[]>([])
  const floors  = ref<Floor[]>([])
  const loading = ref(false)
  const error   = ref<string | null>(null)

  const config     = useRuntimeConfig()
  const { locale } = useI18n()

  async function fetchTicketSelect(eventId: string, sessionId: string) {
    loading.value = true
    error.value   = null
    try {
      const [eventData, seatingData] = await Promise.all([
        $fetch<EventInfo>(`${config.public.eventServiceUrl}/public/events/${eventId}`),
        $fetch<SeatingPlan>(`${config.public.eventServiceUrl}/public/sessions/${sessionId}/layout`),
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