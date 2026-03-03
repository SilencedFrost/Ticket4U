export interface SeatPos {
  x: number
  y: number
}

export interface LayoutSeat {
  seat_name: string
  seat_id: string     // e.g. "A1"
  seat_uuid?: string  // resolved from modifications
  seat_pos: SeatPos
  seat_rotation: number
  status?: 'AVAILABLE' | 'BOOKED' | 'HOLD'
}

export interface LayoutZone {
  zone_name: string           // from template (e.g. "VIP")
  zone_type: 'standing' | 'sitting'
  color: string
  corner1: SeatPos
  corner2: SeatPos
  corner3: SeatPos
  corner4: SeatPos
  seats: LayoutSeat[]
  // resolved from modifications:
  zone_uuid?: string          // actual zone UUID
  display_name?: string       // organizer's name override
}

export interface LayoutStage {
  x1: number
  y1: number
  x2: number
  y2: number
}

export interface SeatingLayout {
  seat_size?: number
  stage: LayoutStage
  zones: LayoutZone[]
}

export interface Floor {
  id: string
  floor_name: string
  floor_order: number
  layout: SeatingLayout
}