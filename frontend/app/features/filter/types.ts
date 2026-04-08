import type { EventStatus } from '../event/types/Event';

export type FilterMode = 'mobile' | 'desktop';

export type FilterBarItem = 'date' | 'main';

export type MainFilterSection = 'price' | 'category' | 'status';

export interface FilterStatusOption {
  value: EventStatus;
  label: string;
}
