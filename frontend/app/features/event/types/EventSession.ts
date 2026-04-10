export interface EventSessionSummary {
  id: number;
  startDate: string;
  endDate: string;
  status: SessionStatus;
  name: string;
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
