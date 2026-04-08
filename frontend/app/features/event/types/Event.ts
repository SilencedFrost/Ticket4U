import type { CategorySummary } from './Category';

export interface BannerImageGroup {
  wide: string;
  square: string;
  tall: string;
}

export interface EventSummary {
  id: string;
  name: string;
  organizerId: string;
  categories: CategorySummary[];
  bannerUrl: BannerImageGroup;
  venueName: string;
  startDate: string;
  endDate: string;
  minPrice: number;
}

export enum EventStatus {
  PREMIERE = 'PREMIERE',
  SELLING = 'SELLING',
  ONGOING = 'ONGOING',
  FINISHED = 'FINISHED',
  CANCELLED = 'CANCELLED',
  EDITING = 'EDITING',
  PAUSED = 'PAUSED',
  SOLD = 'SOLD',
}
