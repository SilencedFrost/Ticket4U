export type BrowseTab = 'category' | 'city';

export interface BrowseCardSeed {
  id: string;
  labelKey: string;
  imageUrl: string;
}

export interface BrowseCardItem {
  id: string;
  label: string;
  imageUrl: string;
}

export interface SearchResultItem {
  id: string;
  title: string;
  releaseDate: string;
  price: number;
  imageUrl: string;
  tags: string[];
}

export interface SearchResultViewItem {
  id: string;
  title: string;
  releaseDateLabel: string;
  priceLabel: string;
  imageUrl: string;
}
