import type { Ticket } from '../types/ticket'

export const ticketData: Ticket[] = [
  { id: '1', name: 'SVIP - Dừa Lòng', price: 3000000, color: '#E53E3E', zone: 'SVIP', available: 100, soldOut: false },
  { id: '2', name: 'Vé Phổ Thông (Zone B)', price: 500000, color: '#DD6B20', zone: 'REGULAR', available: 300, soldOut: false },
  { id: '3', name: 'Vé Tiết Kiệm', price: 300000, color: '#D69E2E', zone: 'BUDGET', available: 0, soldOut: true },
]