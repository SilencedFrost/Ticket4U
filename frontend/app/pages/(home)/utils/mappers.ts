/**
 * @deprecated This file is no longer used.
 * The Event interface now directly matches EventCardDTO from backend.
 * No mapping is needed - frontend uses backend field names directly.
 */

import type { EventCardDTO } from '../types/api'
import type { Event, TrendingEvent } from '../types/home'

/**
 * @deprecated Event interface now matches EventCardDTO directly
 * This function is kept for reference but should not be used
 */
export function mapEventDtoToEvent(dto: EventCardDTO): Event {
  // Event interface now matches EventCardDTO, so no mapping needed
  return dto
}

/**
 * @deprecated Use direct object spread with rank property instead
 * Example: { ...event, rank: 1 as 1 | 2 | 3 }
 */
export function mapEventDtoToTrendingEvent(
  dto: EventCardDTO,
  rank: 1 | 2 | 3
): TrendingEvent {
  return {
    ...dto,
    rank,
  }
}
