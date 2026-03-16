export {};

declare global {
  interface Zone {
    id: string;
    name: string;
    price: number;
    available: number;
    descriptionVi: string;
    descriptionEn: string;
    giftImageUrl: string;
    perks: string[];
  }
}
