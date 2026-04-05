import type { CategorySummary } from './Category';

export interface BannerImageGroup {
  wide: string;
  square: string;
  tall: string;
}

// TODO: refactor this bannerUrl to support new format
export interface EventSummary {
  id: string;
  name: string;
  organizerId: string;
  categories: CategorySummary[];
  bannerUrl: string;
  venueName: string;
  startDate: string;
  endDate: string;
  minPrice: number;
}
