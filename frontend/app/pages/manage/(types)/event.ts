import type { Session } from './session'

export interface BannerUrl {
  wide:   string
  square: string
  tall:   string
}

export type EventStatus =
  | 'EDITING'
  | 'SCHEDULED'
  | 'PREMIERE'
  | 'SELLING'
  | 'PAUSED'
  | 'ONGOING'
  | 'FINISHED'
  | 'CANCELLED'

export interface Event {
  id:                  string
  name:                string
  organizerId:         string
  /** N-N via event_categories junction table */
  categoryIds:         number[]
  addressLine:         string
  status:              EventStatus
  bannerUrl:           BannerUrl
  createdAt:           string
  updatedAt?:          string | null
  cancelledAt?:        string | null

  aboutVi?:            string
  aboutEn?:            string
  termsAndConditions?: string
  policyRefund?:       string
  seatingPlanImageUrl?: string | null

  venueId?:            string | null
  /** event-level custom layout JSONB — separate from venue.layout */
  layout?:             string | null
  longitude?:          number | null
  latitude?:           number | null

  /** 1-to-many: one event can have multiple sessions */
  sessions:            Session[]
}

// ── Form state (used in Step1) ────────────────────────────────

export interface EventFormState {
  name:        string
  /** multi-select — maps to event_categories junction */
  categoryIds: number[]
  status:      EventStatus
  addressLine: string
  startDate:   string   // datetime-local string, for first session
  endDate:     string   // datetime-local string, for first session
  bannerUrl:   BannerUrl
  venueId:     string
  longitude?:  number | null
  latitude?:   number | null
  seatingPlanImageUrl?: string | null
  /** Serialised layout JSONB — venue-mode or custom floors */
  layout?:     string | null
}

// ── Content state (used in Step2) ────────────────────────────

export interface EventContentState {
  aboutVi:            string
  aboutEn:            string
  termsAndConditions: string
  policyRefund:       string
}