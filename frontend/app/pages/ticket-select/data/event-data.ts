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
  title: 'GIAO HƯỞNG MÙA YÊU - LIVE CONCERT ĐẶC BIỆT TẠI NHÀ HÁT HỒ GƯƠM',
  date: 'Feb 20, 2025',
  time: '20:00 - 22:30',
  venue: 'Sân Vận Động Quốc Gia Mỹ Đình'
}