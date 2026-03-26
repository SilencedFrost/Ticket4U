export interface Organizer {
  id: string;
  name: string;
  description: string;
  logo_url: string;
}

interface Zone {
  id: string;
  name: string;
  price: number;
  available: number;
  descriptionVi?: string;
  descriptionEn?: string;
  giftImageUrl?: string;
  perks?: string[] | null;
}

interface Session {
  id: string;
  startDate: string;
  zones: Zone[];
}

interface EventImages {
  heroUrl: string;
  seatMapUrl: string;
}

export interface EventDetailResponse {
  eventId: string;
  eventTitle: string;
  address: string;
  minPrice: number;
  maxPrice: number;
  categories: CategorySummary[];
  aboutVi: string;
  aboutEn: string;
  imgEvent: EventImages;
  organizerId?: string;
  organizer?: Organizer | null;
  sessions: Session[];
  startDate: string;
}
