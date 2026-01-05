interface LocalizedContent {
  vi: string;
  en: string;
}

interface Organizer {
  name: string;
  description: string;
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
}

interface Showtime {
  id: string;
  date: string;
  time: string;
  seatTypes: SeatType[];
}

interface EventDetail {
  title: string;
  date: string;
  time: string;
  venue: LocalizedContent;
  address: LocalizedContent;
  startPrice: string;
  svipPrice: string;
  svipBenefits: string;
  organizer: Organizer;
  relatedEvents: RelatedEvent[];
}

export type { LocalizedContent, Organizer, RelatedEvent, SeatType, Showtime, EventDetail };
