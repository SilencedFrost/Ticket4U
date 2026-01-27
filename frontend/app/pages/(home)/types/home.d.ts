export interface Event {
  id: string
  title: string
  imageUrl: string
  price: number
  date: string
  location?: string
}

export interface Place {
  id: string
  name: string
  imageUrl: string
}

export interface TrendingEvent extends Event {
  rank: 1 | 2 | 3
}
