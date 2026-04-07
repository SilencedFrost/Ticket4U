import type { Zone } from './zone.type'

export interface Session {
  id:        string
  eventId:   string
  name:      string
  startDate: string   // ISO string
  endDate:   string   // ISO string
  status:    string
  createdAt: string
  /** loaded with session — zones belong to sessions, not events */
  zones:     Zone[]
}