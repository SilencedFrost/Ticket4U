export type FilterMode = 'mobile' | 'desktop';

export type FilterBarItem = 'date' | 'main';

export type MainFilterSection = 'price' | 'category' | 'status';

export interface FilterStatusOption {
  value: string;
  label: string;
}
