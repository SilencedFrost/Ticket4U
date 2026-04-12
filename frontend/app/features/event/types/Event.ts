import type { CategorySummary } from './Category';
import type { VenueSummary } from './Venue';
import type { EventSessionSummary } from './EventSession';

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

export interface Event {
  id: string;
  name: string;
  organizerId: string;
  categories: CategorySummary[];
  addressLine: string;
  status: EventStatus;
  bannerUrl: BannerImageGroup;
  createdAt: string;
  updatedAt: string;
  aboutVi: string;
  aboutEn: string;
  termsAndConditions: string;
  policyRefund: string;
  seatingPlanImageUrl: string;
  venue: VenueSummary;
  longitude: number;
  latitude: number;
  sessions: EventSessionSummary[];
  startDate: string;
  endDate: string;
  minPrice: number;
  maxPrice: number;
}

export enum EventStatus {
  PREMIERE = 'PREMIERE',
  SCHEDULED = 'SCHEDULED',
  SELLING = 'SELLING',
  ONGOING = 'ONGOING',
  FINISHED = 'FINISHED',
  CANCELLED = 'CANCELLED',
  EDITING = 'EDITING',
  PAUSED = 'PAUSED',
  SOLD = 'SOLD',
}
