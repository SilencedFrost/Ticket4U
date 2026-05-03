import type { PurchasedTicket } from '../(types)/purchasedTicket'

const mockTickets: PurchasedTicket[] = [
  {
    id: 'tkt-001',
    orderId: 'ord-001',
    eventId: 'evt-001',
    eventName: 'V-Glow: The Cyber-Heritage Night',
    eventBannerUrl: {
      wide: 'https://cdn.ticket4u.uk/v1775381970/vglow-wide_t6cooz.png',
      square: 'https://cdn.ticket4u.uk/v1775381967/vglow-sqre_nmypgq.png',
      tall: 'https://cdn.ticket4u.uk/v1775381974/vglow-tall_wgekmk.png',
    },
    eventDate: '2026-04-15',
    eventStartTime: '18:00',
    eventEndTime: '23:00',
    eventVenue: 'Khu đô thị Vạn Phúc, 375 Quốc lộ 13, TP.Hồ Chí Minh',
    seatName: 'A12',
    zoneName: 'VIP Left',
    ticketType: 'VIP',
    basePrice: 1500000,
    status: 'USED',
    purchasedAt: '2026-03-20T10:30:00Z',
  },
  {
    id: 'tkt-002',
    orderId: 'ord-001',
    eventId: 'evt-001',
    eventName: 'V-Glow: The Cyber-Heritage Night',
    eventBannerUrl: {
      wide: 'https://cdn.ticket4u.uk/v1775381970/vglow-wide_t6cooz.png',
      square: 'https://cdn.ticket4u.uk/v1775381967/vglow-sqre_nmypgq.png',
      tall: 'https://cdn.ticket4u.uk/v1775381974/vglow-tall_wgekmk.png',
    },
    eventDate: '2026-04-15',
    eventStartTime: '18:00',
    eventEndTime: '23:00',
    eventVenue: 'Khu đô thị Vạn Phúc, 375 Quốc lộ 13, TP.Hồ Chí Minh',
    seatName: 'S45',
    zoneName: 'GA Early Bird',
    ticketType: 'GA',
    basePrice: 750000,
    status: 'USED',
    purchasedAt: '2026-03-20T10:30:00Z',
  },
  {
    id: 'tkt-003',
    orderId: 'ord-002',
    eventId: 'evt-003',
    eventName: 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026',
    eventBannerUrl: {
      wide: 'https://cdn.ticket4u.uk/v1775382276/beats-wide_kq6cve.png',
      square: 'https://cdn.ticket4u.uk/v1775382269/beats-sqre_kf8szt.png',
      tall: 'https://cdn.ticket4u.uk/v1775382271/beats-tall_nzkxux.png',
    },
    eventDate: '2026-04-19',
    eventStartTime: '19:00',
    eventEndTime: '00:00',
    eventVenue: 'Khu đô thị Vạn Phúc, 375 Quốc lộ 13, TP.Hồ Chí Minh',
    seatName: 'B3',
    zoneName: 'SVIP',
    ticketType: 'SVIP',
    basePrice: 4000000,
    status: 'USED',
    purchasedAt: '2026-03-25T14:00:00Z',
  },
  {
    id: 'tkt-004',
    orderId: 'ord-003',
    eventId: 'evt-006',
    eventName: 'Sắc Lam: The Indigo Echo',
    eventBannerUrl: {
      wide: 'https://cdn.ticket4u.uk/v1775383130/sclam-wide_ymvfhg.png',
      square: 'https://cdn.ticket4u.uk/v1775383104/sclam-sqre_uaxxnl.png',
      tall: 'https://cdn.ticket4u.uk/v1775383057/sclam-tall_froaue.png',
    },
    eventDate: '2026-05-02',
    eventStartTime: '17:00',
    eventEndTime: '21:00',
    eventVenue: 'Gigamall Thủ Đức, 240-242 Phạm Văn Đồng, TP.Hồ Chí Minh',
    seatName: 'C8',
    zoneName: 'Standard',
    ticketType: 'Standard',
    basePrice: 350000,
    status: 'USED',
    purchasedAt: '2026-04-10T09:15:00Z',
  },
  {
    id: 'tkt-005',
    orderId: 'ord-004',
    eventId: 'evt-007',
    eventName: 'CRYSTAL REALM: The Neon Garden',
    eventBannerUrl: {
      wide: 'https://cdn.ticket4u.uk/v1775383196/cryst-wide_ccxmzc.png',
      square: 'https://cdn.ticket4u.uk/v1775383192/cryst-sqre_e76ssg.png',
      tall: 'https://cdn.ticket4u.uk/v1775383192/cryst-tall_h04ljf.png',
    },
    eventDate: '2026-05-09',
    eventStartTime: '16:00',
    eventEndTime: '22:00',
    eventVenue: 'Công viên Yên Sở, QL1A, Gamuda Central, Hà Nội',
    seatName: 'D2',
    zoneName: 'VIP Crystal',
    ticketType: 'VIP',
    basePrice: 1800000,
    status: 'CANCELLED',
    purchasedAt: '2026-04-15T11:00:00Z',
  },
  {
    id: 'tkt-006',
    orderId: 'ord-005',
    eventId: 'evt-011',
    eventName: 'THE DREAMCATCHER ARCHIPELAGO',
    eventBannerUrl: {
      wide: 'https://cdn.ticket4u.uk/v1775383940/dream-wide_knggr2.png',
      square: 'https://cdn.ticket4u.uk/v1775383942/dream-sqre_zlwxep.png',
      tall: 'https://cdn.ticket4u.uk/v1775383941/dream-tall_fkwtkk.png',
    },
    eventDate: '2026-05-23',
    eventStartTime: '18:00',
    eventEndTime: '23:00',
    eventVenue: 'The Global City, TP.Hồ Chí Minh',
    seatName: 'GA',
    zoneName: 'General Admission',
    ticketType: 'GA',
    basePrice: 600000,
    status: 'SOLD',
    purchasedAt: '2026-04-28T16:45:00Z',
  },
]

export function useMyTickets() {
  const tickets = ref<PurchasedTicket[]>(mockTickets)
  const loading = ref(false)

  const activeTickets = computed(() =>
    tickets.value.filter((t) => t.status === 'SOLD'),
  )
  const pastTickets = computed(() =>
    tickets.value.filter((t) => t.status === 'USED'),
  )
  const cancelledTickets = computed(() =>
    tickets.value.filter((t) => t.status === 'CANCELLED'),
  )

  return {
    tickets,
    loading,
    activeTickets,
    pastTickets,
    cancelledTickets,
  }
}
