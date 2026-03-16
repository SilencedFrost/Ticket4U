/**
 * @deprecated This file is no longer used.
 * The Event interface now directly matches EventCardResponse from backend.
 * No mapping is needed - frontend uses backend field names directly.
 */

import type { EventCardResponse } from '../types/api';
import type { Event, TrendingEvent } from '../types/home';

/**
 * @deprecated Event interface now matches EventCardResponse directly
 * This function is kept for reference but should not be used
 */
export function mapEventDtoToEvent(dto: EventCardResponse): Event {
  // Event interface now matches EventCardResponse, so no mapping needed
  return dto;
}

/**
 * @deprecated Use direct object spread with rank property instead
 * Example: { ...event, rank: 1 as 1 | 2 | 3 }
 */
export function mapEventDtoToTrendingEvent(dto: EventCardResponse, rank: 1 | 2 | 3): TrendingEvent {
  return {
    ...dto,
    rank,
  };
}
