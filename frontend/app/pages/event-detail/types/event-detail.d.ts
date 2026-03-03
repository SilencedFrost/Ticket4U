interface Organizer {
  id: string;
  name: string;
  description: string;
  avatar: string;
}

interface SeatType {
  id: string;
  name: string;
  price: string;
  available: number;
  description?: string;
  image?: string;
  benefits?: string[] | null;
}

interface Showtime {
  id: string;
  date: string;
  time: string;
  seatTypes: SeatType[];
}

interface EventImages {
  heroUrl: string;
  seatMapUrl: string;
}

export interface EventDetailResponse {
  eventId: string;
  eventTitle: string;
  address: string;
  minPrice: string;
  maxPrice: string;
  categoryId: number;
  description: string;
  imgEvent: EventImages;
  organizer: Organizer;
  showtimes: Showtime[];
  date: string;
  time: string;
}
