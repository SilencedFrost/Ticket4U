export {};

declare global {
  interface EventSummary {
    id: string;
    name: string;
    organizerId: string;
    categoryId: number;
    categoryName: string;
    status: string;
    bannerUrl: string;
    venueName: string;
    startDate: string;
    endDate: string;
    minPrice: number;
  }

  interface Event extends Omit<EventSummary, 'venueName'> {
    addressLine: string;
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
    sessions: EventSession[];
    maxPrice: number;
  }

  interface EventSession {
    id: string;
    startDate: string;
    endDate: string;
    status: string;
    name: string;
    zones: Zone[];
  }
}
