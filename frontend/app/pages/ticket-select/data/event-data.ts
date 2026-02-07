import type { Ticket } from '../types/ticket.type'
import type { Event } from '../types/event.type'

export const ticketData: Ticket[] = [
  { 
    id: 'svip', 
    name: 'SVIP - Dứa Lòng', 
    price: 3000000, 
    color: '#E53E3E', 
    zone: 'SVIP', 
    available: 100, 
    soldOut: false 
  },
  { 
    id: 'ga-a', 
    name: 'Vé Phổ Thông (Zone A)', 
    price: 500000, 
    color: '#06B6D4', 
    zone: 'GA', 
    available: 150, 
    soldOut: false 
  },
  { 
    id: 'ga-b', 
    name: 'Vé Phổ Thông (Zone B)', 
    price: 500000, 
    color: '#22D3EE', 
    zone: 'GA', 
    available: 150, 
    soldOut: false 
  },
  { 
    id: 'budget-left', 
    name: 'Vé Tiết Kiệm (Trái)', 
    price: 300000, 
    color: '#D69E2E', 
    zone: 'BUDGET', 
    available: 0, 
    soldOut: true 
  },
  { 
    id: 'budget-right', 
    name: 'Vé Tiết Kiệm (Phải)', 
    price: 300000, 
    color: '#D69E2E', 
    zone: 'BUDGET', 
    available: 0, 
    soldOut: true 
  },
]

export const eventData: Event = {
  id: '1',
  title: 'ANH TRAI "SAY HI" 2025 CONCERT - ĐÊM 2',
  date: '14 Tháng 03, 2026',
  time: '12:00 - 23:00',
  venue: 'Sân Vận Động Quốc Gia Mỹ Đình'
}