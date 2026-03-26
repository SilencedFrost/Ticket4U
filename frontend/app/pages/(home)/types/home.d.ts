export interface Event {
  id: string;
  name: string;
  bannerUrl: string;
  addressLine: string;
  startDate: string;
  endDate: string;
  minPrice: number;
  categories?: CategorySummary[];
}

export interface Place {
  id: string;
  name: string;
  imageUrl: string;
}

export interface TrendingEvent extends Event {
  rank: 1 | 2 | 3;
}

export interface CategoryWithEvents {
  id: number;
  name: string;
  events: Event[];
}
