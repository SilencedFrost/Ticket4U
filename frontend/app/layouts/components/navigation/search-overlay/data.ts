import type { BrowseCardSeed, SearchResultItem } from './types';

export const CATEGORY_CARD_SEEDS: BrowseCardSeed[] = [
  {
    id: 'music',
    labelKey: 'navbar.searchOverlay.categories.music',
    imageUrl:
      'https://images.unsplash.com/photo-1546708770-589dab7b22c7?q=80&w=1112&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  },
  {
    id: 'theaters-art',
    labelKey: 'navbar.searchOverlay.categories.theatersAndArt',
    imageUrl:
      'https://images.unsplash.com/photo-1514306191717-452ec28c7814?q=80&w=1169&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  },
  {
    id: 'sport',
    labelKey: 'navbar.searchOverlay.categories.sport',
    imageUrl:
      'https://images.unsplash.com/photo-1589487391730-58f20eb2c308?q=80&w=1174&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  },
  {
    id: 'workshops',
    labelKey: 'navbar.searchOverlay.categories.workshops',
    imageUrl:
      'https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  },
];

export const CITY_CARD_SEEDS: BrowseCardSeed[] = [
  {
    id: 'hcmc',
    labelKey: 'navbar.searchOverlay.cities.hcmc',
    imageUrl:
      'https://images.unsplash.com/photo-1583417319070-4a69db38a482?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  },
  {
    id: 'hanoi',
    labelKey: 'navbar.searchOverlay.cities.hanoi',
    imageUrl:
      'https://plus.unsplash.com/premium_photo-1691960159290-6f4ace6e6c4c?q=80&w=1742&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  },
  {
    id: 'dalat',
    labelKey: 'navbar.searchOverlay.cities.dalat',
    imageUrl:
      'https://images.unsplash.com/photo-1586595276832-b6840c79bdfc?q=80&w=1548&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
  },
  {
    id: 'other-location',
    labelKey: 'navbar.searchOverlay.cities.other',
    imageUrl: 'https://salt.tkbcdn.com/ts/ds/21/a9/0d/66d54c33c3d6d7943c4d230e08270111.png',
  },
];

export const SEARCH_RESULT_SEEDS: SearchResultItem[] = [
  {
    id: 'f1-the-movie',
    title: 'F1: The Movie',
    releaseDate: '2025-06-27',
    price: 180000,
    imageUrl:
      'https://images.unsplash.com/photo-1440404653325-ab127d49abc1?q=80&w=1170&auto=format&fit=crop',
    tags: ['movie', 'f1', 'action'],
  },
  {
    id: 'scary-movie-2026',
    title: 'Scary Movie',
    releaseDate: '2026-10-16',
    price: 220000,
    imageUrl:
      'https://images.unsplash.com/photo-1489599511986-cfef37d77d04?q=80&w=1170&auto=format&fit=crop',
    tags: ['movie', 'horror', 'comedy'],
  },
  {
    id: 'lee-cronins-the-mummy',
    title: "Lee Cronin's the Mummy",
    releaseDate: '2026-04-17',
    price: 260000,
    imageUrl:
      'https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?q=80&w=1170&auto=format&fit=crop',
    tags: ['movie', 'horror', 'fantasy'],
  },
  {
    id: 'the-studio',
    title: 'The Studio',
    releaseDate: '2025-09-12',
    price: 190000,
    imageUrl:
      'https://images.unsplash.com/photo-1478720568477-152d9b164e26?q=80&w=1169&auto=format&fit=crop',
    tags: ['comedy', 'series', 'studio'],
  },
  {
    id: 'a-minecraft-movie',
    title: 'A Minecraft Movie',
    releaseDate: '2025-04-04',
    price: 210000,
    imageUrl:
      'https://images.unsplash.com/photo-1460881680858-30d872d5b530?q=80&w=1170&auto=format&fit=crop',
    tags: ['movie', 'adventure', 'family'],
  },
  {
    id: 'anh-trai-say-hi-concert',
    title: 'Anh Trai Say Hi Concert',
    releaseDate: '2025-12-20',
    price: 1200000,
    imageUrl:
      'https://images.unsplash.com/photo-1506157786151-b8491531f063?q=80&w=1170&auto=format&fit=crop',
    tags: ['concert', 'music', 'anh trai say hi'],
  },
];
