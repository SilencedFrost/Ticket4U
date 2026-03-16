export {};

declare global {
  interface CategorySummary {
    id: number;
    name: string;
  }

  interface Category extends CategorySummary {
    events: EventSummary[];
  }
}
