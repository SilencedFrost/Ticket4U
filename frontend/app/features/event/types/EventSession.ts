import type { Zone } from './Zone';

export interface EventSessionSummary {
  id: string;
  startDate: string;
  endDate: string;
  status: SessionStatus;
  name: string;
  zones: Zone[];
}

export enum SessionStatus {
  EDITING = 'EDITING',
  SCHEDULED = 'SCHEDULED',
  PREMIERE = 'PREMIERE',
  SELLING = 'SELLING',
  PAUSED = 'PAUSED',
  ONGOING = 'ONGOING',
  FINISHED = 'FINISHED',
  CANCELLED = 'CANCELLED',
}
