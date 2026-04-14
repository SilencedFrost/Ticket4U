import type { Seat } from './Seat';

export interface Zone {
  id: string;
  name: string;
  isStanding: boolean;
  capacity: number;
  purchaseLimit: number;
  price: number;
  descriptionVi: string;
  descriptionEn: string;
  giftImageUrl: string;
  perks: string[];
  seats: Seat[];
}
