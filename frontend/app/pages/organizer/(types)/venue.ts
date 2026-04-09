// TODO: refactor to features/organizer/

// ── Venue Layout JSONB shape ──────────────────────────────────
// Mirrors the layout JSONB stored in the venues table

export interface VenueLayoutSeat {
  seat_name:     string
  seat_id:       string
  seat_pos:      { x: number; y: number }
  seat_rotation: number
}

export interface VenueLayoutZone {
  zone_name:    string
  zone_type:    'sitting' | 'standing'
  color:        string
  corner1:      { x: number; y: number }
  corner2:      { x: number; y: number }
  corner3:      { x: number; y: number }
  corner4:      { x: number; y: number }
  seats:        VenueLayoutSeat[]
  /** frontend-only — used by LayoutPreview to skip decorative zones */
  accessible?:  boolean
  seat_size?:   number
}

export interface VenueLayoutStage {
  x1: number
  y1: number
  x2: number
  y2: number
}

export interface VenueLayout {
  stage:      VenueLayoutStage
  zones:      VenueLayoutZone[]
  seat_size?: number
  /** multi-floor format — optional */
  floors?:    VenueLayoutFloor[]
}

export interface VenueLayoutFloor {
  floor_name?:       string
  stage?:            VenueLayoutStage
  stage_shapes?:     any[]
  zones:             VenueLayoutZone[]
  global_seat_size?: number
}

// ── Venue ─────────────────────────────────────────────────────

export interface Venue {
  id:         string
  name:       string
  addressLine: string
  longitude:  number
  latitude:   number
  /** Parsed from JSONB — null if no layout configured yet */
  layout:     VenueLayout | null
  imageUrl:   string
  createdAt:  string
  updatedAt?: string | null
}