import type { EventSummary } from './Event';

export interface CategorySummary {
  id: number;
  name: string;
}

export interface CategoryWithEvent extends CategorySummary {
  events: EventSummary[];
}
