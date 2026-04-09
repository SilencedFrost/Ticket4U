// TODO: refactor to features/organizer/
import type { Zone } from './zone'

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