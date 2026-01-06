interface LocalizedContent {
  vi: string;
  en: string;
}

interface Organizer {
  name: string;
  description: string;
  logo: string;
}

interface RelatedEvent {
  id: number;
  image: string;
  title: string;
  date: string;
  price: string;
}

interface SeatType {
  name: string;
  price: string;
  available: number;
  description?: string;
  image?: string;
  benefits?: string[];
}

interface Showtime {
  id: string;
  date: string;
  time: string;
  seatTypes: SeatType[];
}

export interface EventImages {
  hero: string;
  location: string;
  about: string;
  sidebar?: string;
}

interface EventDetail {
  title: string;
  date: string;
  time: string;
  venue: LocalizedContent;
  address: LocalizedContent;
  minPrice: string;
  maxPrice: string;
  images: EventImages;
  organizer: Organizer;
  relatedEvents: RelatedEvent[];
}

export type { LocalizedContent, Organizer, RelatedEvent, SeatType, Showtime, EventDetail };
