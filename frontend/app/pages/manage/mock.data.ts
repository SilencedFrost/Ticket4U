// ─────────────────────────────────────────────────────────────
//  organizer/mock.data.ts
//  Mirrors the DB seed exactly:
//    categories → venues (with layout JSONB) → events
//    → event_categories (N-N) → event_sessions → zones
// ─────────────────────────────────────────────────────────────

import type { Category }          from './(types)/category'
import type { Venue, VenueLayout } from './(types)/venue'
import type { Zone }               from './(types)/zone'
import type { Session }            from './(types)/session'
import type { Event, EventStatus, EventFormState, EventContentState, BannerUrl } from './(types)/event'
import type { Profile }            from './(types)/profile'
import type { Seat }               from './(types)/seat'
import type { OrganizerStaff, AssignableRole } from './(types)/staff'

// Re-export types so existing imports still work
export type {
  Category, Venue, VenueLayout, Zone, Session,
  Event, EventStatus, EventFormState, EventContentState, BannerUrl,
  Profile, Seat, OrganizerStaff, AssignableRole,
}

// ── Profile ───────────────────────────────────────────────────

export const mockProfile: Profile = {
  id:        '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
  name:      'Nguyễn Văn A',
  email:     'organizer@example.com',
  avatarUrl: undefined,
  roleId:    2,
}

// ── Categories ────────────────────────────────────────────────

export const mockCategories: Category[] = [
  { id: 0,  name: 'categories.concert'    },
  { id: 1,  name: 'categories.festival'   },
  { id: 2,  name: 'categories.theater'    },
  { id: 3,  name: 'categories.comedy'     },
  { id: 4,  name: 'categories.sports'     },
  { id: 5,  name: 'categories.conference' },
  { id: 6,  name: 'categories.workshop'   },
  { id: 7,  name: 'categories.seminar'    },
  { id: 8,  name: 'categories.networking' },
  { id: 9,  name: 'categories.exhibition' },
  { id: 10, name: 'categories.nightlife'  },
  { id: 11, name: 'categories.wellness'   },
  { id: 12, name: 'categories.food'       },
  { id: 13, name: 'categories.drinks'     },
  { id: 14, name: 'categories.dance'      },
  { id: 15, name: 'categories.film'       },
  { id: 16, name: 'categories.gaming'     },
  { id: 17, name: 'categories.outdoor'    },
  { id: 18, name: 'categories.charity'    },
  { id: 19, name: 'categories.family'     },
  { id: 20, name: 'categories.cultural'   },
  { id: 21, name: 'categories.fashion'    },
  { id: 22, name: 'categories.tech'       },
  { id: 23, name: 'categories.art'        },
  { id: 24, name: 'categories.literature' },
  { id: 25, name: 'categories.science'    },
  { id: 26, name: 'categories.religion'   },
  { id: 27, name: 'categories.politics'   },
  { id: 28, name: 'categories.travel'     },
  { id: 29, name: 'categories.virtual'    },
]

// ── Venues ────────────────────────────────────────────────────
// layout mirrors the JSONB set in the seed UPDATE statements.
// zone_name in layout.zones links to zone.name by name matching.

export const mockVenues: Venue[] = [
  {
    id: 'v1', name: 'CIS Arena',
    addressLine: '7, Đường số 23, Phú Mỹ Hưng, Phường Tân Mỹ, TP.Hồ Chí Minh',
    latitude: 10.713750, longitude: 106.728750,
    imageUrl: 'https://s.inyourpocket.com/gallery/178413.jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Floor Level', floor_order: 1, global_seat_size: 18,
          stage: { x1: -0.35, y1: -0.96, x2: 0.35, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'VIP Floor',       zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: -0.20 }, corner4: { x: -0.85, y: -0.20 }, seats: [] },
            { zone_name: 'Lower Bowl Left', zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: -0.85, y: -0.18 }, corner2: { x: -0.05, y: -0.18 }, corner3: { x: -0.05, y: 0.70 }, corner4: { x: -0.85, y: 0.70 }, seats: [] },
            { zone_name: 'Lower Bowl Right',zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: 0.05,  y: -0.18 }, corner2: { x: 0.85, y: -0.18 }, corner3: { x: 0.85, y: 0.70 },  corner4: { x: 0.05, y: 0.70 },  seats: [] },
            { zone_name: 'GA Pit',          zone_type: 'sitting',  accessible: false, shape_type: 'rect', color: '#22c55e', rotation: 0, corner1: { x: -0.04, y: -0.18 }, corner2: { x: 0.04, y: -0.18 }, corner3: { x: 0.04, y: 0.70 },  corner4: { x: -0.04, y: 0.70 }, seats: [] },
            { zone_name: 'Lower Bowl Back', zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#3b82f6', rotation: 0, corner1: { x: -0.85, y: 0.72 },  corner2: { x: 0.85, y: 0.72 },  corner3: { x: 0.85, y: 0.96 },  corner4: { x: -0.85, y: 0.96 }, seats: [] },
          ],
        },
        {
          floor_name: 'Upper Bowl', floor_order: 2, global_seat_size: 16,
          stage: { x1: -0.35, y1: -0.96, x2: 0.35, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'Upper Bowl', zone_type: 'sitting', accessible: true, shape_type: 'rect', color: '#8b5cf6', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: 0.96 }, corner4: { x: -0.85, y: 0.96 }, seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v2', name: 'Nhà Hát Bến Thành',
    addressLine: '6 Đường Mạc Đĩnh Chi, Phường Sài Gòn, TP.Hồ Chí Minh',
    latitude: 10.784246, longitude: 106.700877,
    imageUrl: 'http://vietlandmarks.com/upload/142582138454fc4ec85a5d2.jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Orchestra', floor_order: 1, global_seat_size: 20,
          stage: { x1: -0.5, y1: -0.96, x2: 0.5, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'Orchestra',  zone_type: 'sitting', accessible: true,  shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.60, y: -0.74 }, corner2: { x: 0.60, y: -0.74 }, corner3: { x: 0.60, y: 0.10 },  corner4: { x: -0.60, y: 0.10 },  seats: [] },
            { zone_name: 'Box Left',   zone_type: 'sitting', accessible: false, shape_type: 'rect', color: '#ec4899', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: -0.62, y: -0.74 }, corner3: { x: -0.62, y: 0.10 }, corner4: { x: -0.85, y: 0.10 }, seats: [] },
            { zone_name: 'Box Right',  zone_type: 'sitting', accessible: false, shape_type: 'rect', color: '#ec4899', rotation: 0, corner1: { x: 0.62,  y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: 0.10 },  corner4: { x: 0.62, y: 0.10 },  seats: [] },
            { zone_name: 'Mezzanine',  zone_type: 'sitting', accessible: true,  shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: -0.85, y: 0.12 },  corner2: { x: 0.85, y: 0.12 },  corner3: { x: 0.85, y: 0.72 },  corner4: { x: -0.85, y: 0.72 },  seats: [] },
          ],
        },
        {
          floor_name: 'Balcony', floor_order: 2, global_seat_size: 18,
          stage: { x1: -0.5, y1: -0.96, x2: 0.5, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'Balcony', zone_type: 'sitting', accessible: true, shape_type: 'rect', color: '#3b82f6', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: 0.72 }, corner4: { x: -0.85, y: 0.72 }, seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v3', name: 'Mây in The Nest',
    addressLine: '519 Thôn Măng Lin, P. Langbiang, Đà Lạt, Tỉnh Lâm Đồng',
    latitude: 11.978842, longitude: 108.393794,
    imageUrl: 'https://media2.gody.vn/public/images/place/may-lang-thang-da-lat/614c498b500ff-1632389515.jpeg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Main Floor', floor_order: 1, global_seat_size: 18,
          stage: { x1: -0.35, y1: -0.96, x2: 0.35, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'VIP',      zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: -0.18 }, corner4: { x: -0.85, y: -0.18 }, seats: [] },
            { zone_name: 'Zone A',   zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: -0.85, y: -0.16 }, corner2: { x: -0.05, y: -0.16 }, corner3: { x: -0.05, y: 0.72 },  corner4: { x: -0.85, y: 0.72 },  seats: [] },
            { zone_name: 'Zone B',   zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: 0.05,  y: -0.16 }, corner2: { x: 0.85, y: -0.16 }, corner3: { x: 0.85, y: 0.72 },   corner4: { x: 0.05, y: 0.72 },   seats: [] },
            { zone_name: 'Standing', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#22c55e', rotation: 0, corner1: { x: -0.85, y: 0.74 },  corner2: { x: 0.85, y: 0.74 },  corner3: { x: 0.85, y: 0.96 },   corner4: { x: -0.85, y: 0.96 },  seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v4', name: 'GEM Center',
    addressLine: '8 Đường Nguyễn Bỉnh Khiêm, Phường Sài Gòn, TP.Hồ Chí Minh',
    latitude: 10.790146, longitude: 106.702379,
    imageUrl: 'https://images2.thanhnien.vn/528068263637045248/2023/2/22/1-gem-center-16770590914701989789155.jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Main Hall', floor_order: 1, global_seat_size: 18,
          stage: { x1: -0.4, y1: -0.96, x2: 0.4, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'VIP',      zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: -0.18 }, corner4: { x: -0.85, y: -0.18 }, seats: [] },
            { zone_name: 'Zone A',   zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: -0.85, y: -0.16 }, corner2: { x: -0.05, y: -0.16 }, corner3: { x: -0.05, y: 0.36 },  corner4: { x: -0.85, y: 0.36 },  seats: [] },
            { zone_name: 'Zone B',   zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: 0.05,  y: -0.16 }, corner2: { x: 0.85, y: -0.16 }, corner3: { x: 0.85, y: 0.36 },   corner4: { x: 0.05, y: 0.36 },   seats: [] },
            { zone_name: 'Zone C',   zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#3b82f6', rotation: 0, corner1: { x: -0.85, y: 0.38 },  corner2: { x: 0.85, y: 0.38 },  corner3: { x: 0.85, y: 0.72 },   corner4: { x: -0.85, y: 0.72 },  seats: [] },
            { zone_name: 'Standing', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#22c55e', rotation: 0, corner1: { x: -0.85, y: 0.74 },  corner2: { x: 0.85, y: 0.74 },  corner3: { x: 0.85, y: 0.96 },   corner4: { x: -0.85, y: 0.96 },  seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v5', name: 'Tinh Tế Cafe',
    addressLine: '351/56 Đ. Lê Văn Sỹ, Phường Nhiêu Lộc, TP.Hồ Chí Minh',
    latitude: 10.785960, longitude: 106.676488,
    imageUrl: 'https://ik.imagekit.io/tvlk/blog/2022/10/quan-cafe-nhieu-cay-xanh-tphcm-1.jpeg?tr=q-70,c-at_max,w-500,h-300,dpr-2',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Cafe Floor', floor_order: 1, global_seat_size: 20,
          stage: { x1: -0.2, y1: -0.96, x2: 0.2, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'Seated',   zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: 0.36 }, corner4: { x: -0.85, y: 0.36 }, seats: [] },
            { zone_name: 'Standing', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#22c55e', rotation: 0, corner1: { x: -0.85, y: 0.38 },  corner2: { x: 0.85, y: 0.38 },  corner3: { x: 0.85, y: 0.96 }, corner4: { x: -0.85, y: 0.96 }, seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v6', name: 'Dreamplex Điện Biên Phủ',
    addressLine: '195 Đường Điện Biên Phủ, Phường Gia Định, TP.Hồ Chí Minh',
    latitude: 10.799603, longitude: 106.705507,
    imageUrl: 'https://maisonoffice.vn/wp-content/uploads/2021/09/van-phong-cho-thue-dreamplex-195-dien-bien-phu.jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Event Hall', floor_order: 1, global_seat_size: 20,
          stage: { x1: -0.4, y1: -0.96, x2: 0.4, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'Zone A',   zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: -0.02 }, corner4: { x: -0.85, y: -0.02 }, seats: [] },
            { zone_name: 'Zone B',   zone_type: 'sitting',  accessible: true,  shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: -0.85, y: 0.00 },  corner2: { x: 0.85, y: 0.00 },  corner3: { x: 0.85, y: 0.60 },  corner4: { x: -0.85, y: 0.60 },  seats: [] },
            { zone_name: 'Standing', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#22c55e', rotation: 0, corner1: { x: -0.85, y: 0.62 },  corner2: { x: 0.85, y: 0.62 },  corner3: { x: 0.85, y: 0.96 },  corner4: { x: -0.85, y: 0.96 },  seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v7', name: 'Gigamall Thủ Đức',
    addressLine: '240-242 Phạm Văn Đồng, Phường Hiệp Bình, TP.Hồ Chí Minh',
    latitude: 10.828149, longitude: 106.721385,
    imageUrl: 'https://bidiland.vn/dataweb/images/tin-tuc/tong-hop-nhung-sieu-thi-va-cho-khu-vuc-quan-9-quan-2-quan-thu-duc-thanh-pho-thu-duc-coop-extra-gigamall-bidiland(1).jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Main Stage', floor_order: 1, global_seat_size: 16,
          stage: { x1: -0.4, y1: -0.96, x2: 0.4, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'VIP', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#7c3aed', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: -0.10 }, corner4: { x: -0.85, y: -0.10 }, seats: [] },
            { zone_name: 'GA',  zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#5b21b6', rotation: 0, corner1: { x: -0.85, y: -0.08 }, corner2: { x: 0.85, y: -0.08 }, corner3: { x: 0.85, y: 0.96 },  corner4: { x: -0.85, y: 0.96 },  seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v8', name: 'Sân Lễ Hội Đền Vua Đinh - Vua Lê',
    addressLine: 'Xã Trường Yên, Huyện Hoa Lư, Tỉnh Ninh Bình',
    latitude: 20.284638, longitude: 105.905315,
    imageUrl: 'https://mtcs.1cdn.vn/2023/02/16/le-hoi-den-hung.jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Festival Grounds', floor_order: 1, global_seat_size: 16,
          stage: { x1: -0.5, y1: -0.96, x2: 0.5, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'VVIP',        zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#dc2626', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: -0.28 }, corner4: { x: -0.85, y: -0.28 }, seats: [] },
            { zone_name: 'VIP',         zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.85, y: -0.26 }, corner2: { x: 0.85, y: -0.26 }, corner3: { x: 0.85, y: 0.22 },  corner4: { x: -0.85, y: 0.22 },  seats: [] },
            { zone_name: 'GA Standing', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#2563eb', rotation: 0, corner1: { x: -0.85, y: 0.24 },  corner2: { x: 0.85, y: 0.24 },  corner3: { x: 0.85, y: 0.96 },  corner4: { x: -0.85, y: 0.96 },  seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v9', name: 'Công viên Yên Sở',
    addressLine: 'QL1A, Gamuda Central, Thủ đô Hà Nội',
    latitude: 20.964741, longitude: 105.854647,
    imageUrl: 'https://gamudagardens.vn/wp-content/uploads/2016/09/ho-yen-so.jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Park Stage', floor_order: 1, global_seat_size: 16,
          stage: { x1: -0.4, y1: -0.96, x2: 0.4, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'VIP',    zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: 0.85, y: -0.74 }, corner3: { x: 0.85, y: -0.20 }, corner4: { x: -0.85, y: -0.20 }, seats: [] },
            { zone_name: 'Zone A', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: -0.85, y: -0.18 }, corner2: { x: -0.05, y: -0.18 }, corner3: { x: -0.05, y: 0.44 },  corner4: { x: -0.85, y: 0.44 },  seats: [] },
            { zone_name: 'Zone B', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#6366f1', rotation: 0, corner1: { x: 0.05,  y: -0.18 }, corner2: { x: 0.85, y: -0.18 }, corner3: { x: 0.85, y: 0.44 },   corner4: { x: 0.05, y: 0.44 },   seats: [] },
            { zone_name: 'GA',     zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#22c55e', rotation: 0, corner1: { x: -0.85, y: 0.46 },  corner2: { x: 0.85, y: 0.46 },  corner3: { x: 0.85, y: 0.96 },   corner4: { x: -0.85, y: 0.96 },  seats: [] },
          ],
        },
      ],
    },
  },
  {
    id: 'v10', name: 'Khu đô thị Vạn Phúc',
    addressLine: '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',
    latitude: 10.846066, longitude: 106.709149,
    imageUrl: 'https://khudothivanphuc.vn/wp-content/uploads/2021/07/cong-vien-ocean-world-van-phuc.jpg',
    createdAt: new Date().toISOString(),
    layout: {
      floors: [
        {
          floor_name: 'Main Stage', floor_order: 1, global_seat_size: 16,
          stage: { x1: -0.35, y1: -0.96, x2: 0.35, y2: -0.76 }, stage_shapes: [],
          zones: [
            { zone_name: 'SVIP',          zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#dc2626', rotation: 0, corner1: { x: -0.40, y: -0.74 }, corner2: { x: 0.40,  y: -0.74 }, corner3: { x: 0.40,  y: -0.20 }, corner4: { x: -0.40, y: -0.20 }, seats: [] },
            { zone_name: 'VIP Left',      zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: -0.85, y: -0.74 }, corner2: { x: -0.42, y: -0.74 }, corner3: { x: -0.42, y: -0.20 }, corner4: { x: -0.85, y: -0.20 }, seats: [] },
            { zone_name: 'VIP Right',     zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#f59e0b', rotation: 0, corner1: { x: 0.42,  y: -0.74 }, corner2: { x: 0.85,  y: -0.74 }, corner3: { x: 0.85,  y: -0.20 }, corner4: { x: 0.42,  y: -0.20 }, seats: [] },
            { zone_name: 'GA Left',       zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#2563eb', rotation: 0, corner1: { x: -0.85, y: -0.18 }, corner2: { x: -0.05, y: -0.18 }, corner3: { x: -0.05, y: 0.96 },  corner4: { x: -0.85, y: 0.96 },  seats: [] },
            { zone_name: 'GA Early Bird', zone_type: 'standing', accessible: false, shape_type: 'rect', color: '#1d4ed8', rotation: 0, corner1: { x: 0.05,  y: -0.18 }, corner2: { x: 0.85,  y: -0.18 }, corner3: { x: 0.85,  y: 0.96 },  corner4: { x: 0.05,  y: 0.96 },  seats: [] },
          ],
        },
      ],
    },
  },
]

// ── Events ────────────────────────────────────────────────────
// Structure mirrors DB:
//   event → sessions[] → zones[]
// categoryIds mirrors event_categories junction table
// All 15 events from Ticket4U-Event_Data_new.sql, status PREMIERE

export const mockEvents: Event[] = [
  {
    id: '1', name: 'V-Glow: The Cyber-Heritage Night',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1, 0, 20, 22],
    addressLine: '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',
    status: 'EDITING',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775381970/vglow-wide_t6cooz.png', square: 'https://cdn.ticket4u.uk/v1775381967/vglow-sqre_nmypgq.png', tall: 'https://cdn.ticket4u.uk/v1775381974/vglow-tall_wgekmk.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'V-Glow 2026 là lễ hội âm nhạc và công nghệ đột phá tại TP.HCM, kết hợp giữa nét đẹp Trung Thu truyền thống và phong cách Cyberpunk tương lai. Với sự góp mặt của các ngôi sao V-Pop hàng đầu, triển lãm nghệ thuật AR và diễu hành lồng đèn số, đây là điểm đến không thể bỏ qua cho cộng đồng fan Gen Z.',
    aboutEn: 'V-Glow 2026 is a revolutionary fan-fest in Ho Chi Minh City, merging Vietnamese traditional mid-autumn heritage with futuristic Cyberpunk aesthetics. Featuring top V-Pop idols, immersive AR art, and a digital lantern parade, it\'s the ultimate cultural-tech experience for the Gen Z fanbase.',
    termsAndConditions: 'Cấm trẻ em dưới 16 tuổi. Không mang chất cấm, vũ khí vào khu vực. Trang phục tự do.',
    policyRefund: 'Hoàn tiền 50% nếu hủy trước 7 ngày diễn ra sự kiện.',
    venueId: 'v10', latitude: 10.846066, longitude: 106.709149,
    layout: '{"venueId":"v10","venueMode":true,"zoneLinks":{"SVIP":"z1-1","VIP Left":"z1-2","VIP Right":"z1-3","GA Left":"z1-5","GA Early Bird":"z1-4"}}',
    sessions: [
      {
        id: 's1', eventId: '1', name: 'session',
        startDate: '2026-04-15T18:00:00+07:00', endDate: '2026-04-15T23:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z1-1', sessionId: 's1', name: 'SVIP Lounge', isStanding: false, capacity: 80, price: 3_500_000, purchaseLimit: 2, quantitySold: 45, perks: ['Open bar', 'Goodie bag', 'Lối vào ưu tiên'], descriptionVi: 'Khu vực SVIP cao cấp với không gian riêng tư, open bar và gói quà tặng độc quyền.', descriptionEn: 'Exclusive SVIP lounge with private space, open bar and premium gift package.', createdAt: new Date().toISOString() },
          { id: 'z1-2', sessionId: 's1', name: 'VIP Left',    isStanding: true,  capacity: 150, price: 1_500_000, purchaseLimit: 4, quantitySold: 80, perks: ['Vòng tay VIP', 'Poster sự kiện'], descriptionVi: 'Khu vực VIP trái đứng gần sân khấu với tầm nhìn tuyệt vời.', descriptionEn: 'VIP Left standing area near the stage with great views.', createdAt: new Date().toISOString() },
          { id: 'z1-3', sessionId: 's1', name: 'VIP Right',   isStanding: true,  capacity: 150, price: 1_500_000, purchaseLimit: 4, quantitySold: 75, perks: ['Vòng tay VIP', 'Poster sự kiện'], descriptionVi: 'Khu vực VIP phải đứng gần sân khấu với tầm nhìn tuyệt vời.', descriptionEn: 'VIP Right standing area near the stage with great views.', createdAt: new Date().toISOString() },
          { id: 'z1-4', sessionId: 's1', name: 'GA Early Bird', isStanding: true, capacity: 600, price: 750_000, purchaseLimit: 4, quantitySold: 350, perks: ['Vòng tay vải'], descriptionVi: 'Khu GA Early Bird với giá ưu đãi cho người đặt sớm.', descriptionEn: 'GA Early Bird with special early-booking price.', createdAt: new Date().toISOString() },
          { id: 'z1-5', sessionId: 's1', name: 'GA',          isStanding: true,  capacity: 800, price: 550_000, purchaseLimit: 4, quantitySold: 400, descriptionVi: 'Khu GA tự do hòa mình vào không khí lễ hội.', descriptionEn: 'General admission to immerse in the festival atmosphere.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '2', name: 'The Echo of An Nam',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1, 20, 23, 10],
    addressLine: 'Xã Trường Yên, Huyện Hoa Lư, Tỉnh Ninh Bình',
    status: 'EDITING',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775381964/aecho-wide_cdgl6u.png', square: 'https://cdn.ticket4u.uk/v1775381950/aecho-sqre_efweqg.png', tall: 'https://cdn.ticket4u.uk/v1775381948/aecho-tall_xkm5ic.png' },
    createdAt: new Date().toISOString(),
    aboutVi: '"Tiếng Vọng An Nam" là lễ hội đêm quy mô lớn tại TP.HCM năm 2026, kết hợp giữa di sản truyền thống và công nghệ tương lai. Với sự góp mặt của các nghệ sĩ hàng đầu như Gemini Hùng Huỳnh và nghệ thuật số đa giác quan, đây là sự kiện văn hóa - công nghệ không thể bỏ lỡ.',
    aboutEn: '"The Echo of An Nam" is a premier 2026 night festival in Ho Chi Minh City blending traditional Vietnamese heritage with futuristic tech. Featuring top artists like Gemini Hung Huynh and immersive digital art, it\'s the ultimate cultural-tech crossover for the modern fanbase.',
    termsAndConditions: 'Vui lòng mang theo CCCD khi check-in. Không gây rối trật tự.',
    policyRefund: 'Không hoàn tiền sau khi mua vé.',
    venueId: 'v8', latitude: 20.284638, longitude: 105.905315,
    layout: '{"venueId":"v8","venueMode":true,"zoneLinks":{"VVIP":"z2-1","VIP":"z2-2","GA Standing":"z2-3"}}',
    sessions: [
      {
        id: 's2', eventId: '2', name: 'session',
        startDate: '2026-04-18T17:00:00+07:00', endDate: '2026-04-18T22:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z2-1', sessionId: 's2', name: 'VVIP',       isStanding: true, capacity: 100,  price: 3_000_000, purchaseLimit: 2, quantitySold: 60,  perks: ['Vòng tay VVIP', 'Goodie bag', 'Nước uống VIP'], descriptionVi: 'Khu VVIP đứng sát sân khấu, trải nghiệm âm nhạc và ánh sáng tối đỉnh.', descriptionEn: 'VVIP standing directly at the stage for the ultimate music and light experience.', createdAt: new Date().toISOString() },
          { id: 'z2-2', sessionId: 's2', name: 'VIP',        isStanding: true, capacity: 400,  price: 1_200_000, purchaseLimit: 4, quantitySold: 200, perks: ['Vòng tay VIP'], descriptionVi: 'Khu VIP đứng tự do với tầm nhìn tốt.', descriptionEn: 'VIP free standing with good views.', createdAt: new Date().toISOString() },
          { id: 'z2-3', sessionId: 's2', name: 'GA Standing', isStanding: true, capacity: 1500, price: 500_000,  purchaseLimit: 4, quantitySold: 700, descriptionVi: 'Khu GA đứng tự do, hòa mình vào không khí lễ hội sôi động.', descriptionEn: 'General standing area, immerse in the vibrant festival atmosphere.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '3', name: 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1, 22, 10],
    addressLine: '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',
    status: 'EDITING',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775382276/beats-wide_kq6cve.png', square: 'https://cdn.ticket4u.uk/v1775382269/beats-sqre_kf8szt.png', tall: 'https://cdn.ticket4u.uk/v1775382271/beats-tall_nzkxux.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Trải nghiệm lễ hội âm nhạc và công nghệ đỉnh cao tại Sài Gòn Neon Beats 2026. Một sự kiện bùng nổ kết hợp giữa âm thanh EDM sôi động, trình diễn Drone Light Show và không gian tương tác ảo (AR) tại "Trung tâm mới" Global City.',
    aboutEn: 'Experience the ultimate music and technology festival at Saigon Neon Beats 2026. An explosive event blending high-energy EDM, Drone Light Shows, and AR interactive zones at the "New City Center" - Global City.',
    termsAndConditions: 'Cấm trẻ em dưới 16 tuổi. Không mang chất cấm. Trang phục tự do.',
    policyRefund: 'Hoàn tiền 50% trước 7 ngày diễn ra.',
    venueId: 'v10', latitude: 10.846066, longitude: 106.709149,
    layout: '{"venueId":"v10","venueMode":true,"zoneLinks":{"SVIP":"z3-1","VIP Left":"z3-2","VIP Right":"z3-3","GA Left":"z3-4","GA Early Bird":"z3-5"}}',
    sessions: [
      {
        id: 's3', eventId: '3', name: 'session',
        startDate: '2026-04-19T14:00:00+07:00', endDate: '2026-04-20T02:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z3-1', sessionId: 's3', name: 'SVIP',         isStanding: true, capacity: 100,  price: 4_000_000, purchaseLimit: 2, quantitySold: 55,  perks: ['Open bar', 'Private lounge', 'Premium goodie bag'], descriptionVi: 'Khu SVIP cao cấp ngay trước sân khấu với open bar, khu nghỉ riêng và quà tặng premium.', descriptionEn: 'Premium SVIP area directly in front of the stage with open bar, private lounge and premium gifts.', createdAt: new Date().toISOString() },
          { id: 'z3-2', sessionId: 's3', name: 'VIP Left',     isStanding: true, capacity: 350,  price: 1_800_000, purchaseLimit: 4, quantitySold: 180, perks: ['Vòng tay VIP'], descriptionVi: 'Khu VIP trái sân khấu, góc nhìn tuyệt vời.', descriptionEn: 'VIP left of stage with great view and explosive EDM atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z3-3', sessionId: 's3', name: 'VIP Right',    isStanding: true, capacity: 350,  price: 1_800_000, purchaseLimit: 4, quantitySold: 170, perks: ['Vòng tay VIP'], descriptionVi: 'Khu VIP phải sân khấu, góc nhìn tuyệt vời.', descriptionEn: 'VIP right of stage with great view and explosive EDM atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z3-4', sessionId: 's3', name: 'GA',           isStanding: true, capacity: 800,  price: 700_000,  purchaseLimit: 4, quantitySold: 400, descriptionVi: 'Khu GA tự do hòa mình vào không khí Cyber-Fest bùng nổ.', descriptionEn: 'General admission to immerse in the explosive Cyber-Fest atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z3-5', sessionId: 's3', name: 'GA Early Bird', isStanding: true, capacity: 600, price: 550_000,  purchaseLimit: 4, quantitySold: 380, perks: ['Vòng tay vải'], descriptionVi: 'Khu GA Early Bird với giá ưu đãi cho người đặt sớm.', descriptionEn: 'GA Early Bird at special price for early bookers.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '4', name: 'CỔ NGHỆ KIÊU HÙNG',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [20, 23, 21, 24],
    addressLine: 'QL1A, Gamuda Central, Thủ đô Hà Nội',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775382586/tloom-wide_nnsqej.png', square: 'https://cdn.ticket4u.uk/v1775382590/tloom-sqre_zqnaam.png', tall: 'https://cdn.ticket4u.uk/v1775382588/tloom-tall_wlhtt2.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Đắm mình trong sự giao thoa giữa di sản nghìn năm và tư duy sáng tạo hiện đại tại "Cổ Nghệ Kiêu Hùng." Diễn ra tại Văn Miếu - Quốc Tử Giám, sự kiện mang đến những trải nghiệm trình diễn nghề thủ công trực tiếp, nhạc indie-folk và sàn diễn "Cổ phục" độc bản.',
    aboutEn: 'Experience the fusion of Vietnam\'s 1,000-year-old heritage and modern creative flair at "The Antique Alchemy." Join us at Hanoi\'s historic Temple of Literature for a night of live traditional craftsmanship, indie-folk music, and an exclusive "Antique Runway."',
    termsAndConditions: 'Vui lòng mang theo CCCD. Trang phục cổ phục được khuyến khích.',
    policyRefund: 'Không hoàn tiền sau khi mua vé.',
    venueId: 'v9', latitude: 20.964741, longitude: 105.854647,
    layout: '{"venueId":"v9","venueMode":true,"zoneLinks":{"VIP":"z4-1","Zone A":"z4-2","Zone B":"z4-3","GA":"z4-4"}}',
    sessions: [
      {
        id: 's4', eventId: '4', name: 'session',
        startDate: '2026-04-25T16:00:00+07:00', endDate: '2026-04-25T22:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z4-1', sessionId: 's4', name: 'VIP',    isStanding: true, capacity: 200,  price: 1_500_000, purchaseLimit: 2, quantitySold: 100, perks: ['Bộ quà tặng cổ phục', 'Nước uống miễn phí', 'Lối vào ưu tiên'], descriptionVi: 'Khu VIP ngay trước sân khấu với tầm nhìn tuyệt vời và quà tặng độc quyền.', descriptionEn: 'VIP area directly in front of stage with great views and exclusive gifts.', createdAt: new Date().toISOString() },
          { id: 'z4-2', sessionId: 's4', name: 'Zone A', isStanding: true, capacity: 500,  price: 700_000,  purchaseLimit: 4, quantitySold: 250, perks: ['Vòng tay vải', 'Sticker cổ phục'], descriptionVi: 'Khu Zone A bên trái sân khấu, góc nhìn tốt.', descriptionEn: 'Zone A left of stage with good views and vibrant atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z4-3', sessionId: 's4', name: 'Zone B', isStanding: true, capacity: 500,  price: 700_000,  purchaseLimit: 4, quantitySold: 240, perks: ['Vòng tay vải', 'Sticker cổ phục'], descriptionVi: 'Khu Zone B bên phải sân khấu, góc nhìn tốt.', descriptionEn: 'Zone B right of stage with good views and vibrant atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z4-4', sessionId: 's4', name: 'GA',     isStanding: true, capacity: 1000, price: 400_000,  purchaseLimit: 4, quantitySold: 450, descriptionVi: 'Khu GA tự do phía sau, tham gia không khí lễ hội văn hóa.', descriptionEn: 'General admission rear section, join the cultural festival atmosphere.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '5', name: 'NEO-LUMINANCE: The Echo of Indochine',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1, 10, 22, 20],
    addressLine: '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775382880/neolu-wide_wiwf4z.png', square: 'https://cdn.ticket4u.uk/v1775382884/neolu-sqre_y2ofzz.png', tall: 'https://cdn.ticket4u.uk/v1775382886/neolu-tall_spbtwy.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Khám phá NEO-LUMINANCE 2026, lễ hội âm nhạc điện tử và đa dòng nhạc ngoài trời quy mô lớn nhất tại TP.HCM. Với sự góp mặt của DJ/Nhà sản xuất nổi tiếng thế giới cùng các nghệ sĩ hàng đầu Việt Nam, sự kiện là sự giao thoa độc đáo giữa phong cách tương lai Cyberpunk và họa tiết Đông Dương truyền thống.',
    aboutEn: 'Experience NEO-LUMINANCE 2026, Vietnam\'s premier outdoor electronic and fusion music festival in Ho Chi Minh City. Featuring a headline performance by a world-renowned European DJ/Producer and top-tier local artists, this event blends futuristic cyberpunk aesthetics with traditional Indochine motifs.',
    termsAndConditions: 'Cấm trẻ em dưới 16 tuổi. Không mang chất cấm, vũ khí vào khu vực.',
    policyRefund: 'Hoàn tiền 50% trước 7 ngày diễn ra.',
    venueId: 'v10', latitude: 10.846066, longitude: 106.709149,
    layout: '{"venueId":"v10","venueMode":true,"zoneLinks":{"SVIP":"z5-1","VIP Left":"z5-2","VIP Right":"z5-3","GA Left":"z5-4","GA Early Bird":"z5-5"}}',
    sessions: [
      {
        id: 's5', eventId: '5', name: 'session',
        startDate: '2026-04-26T18:00:00+07:00', endDate: '2026-04-27T02:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z5-1', sessionId: 's5', name: 'SVIP Deck',    isStanding: false, capacity: 120,  price: 5_000_000, purchaseLimit: 2, quantitySold: 70,  perks: ['Private bar', 'Goodie bag cao cấp', 'Lối đi VIP riêng'], descriptionVi: 'Khán đài SVIP cao cấp với không gian riêng tư, private bar và tầm nhìn bao quát toàn bộ lễ hội.', descriptionEn: 'Premium SVIP deck with private space, private bar and panoramic view of the entire festival.', createdAt: new Date().toISOString() },
          { id: 'z5-2', sessionId: 's5', name: 'VIP Left',     isStanding: true,  capacity: 400,  price: 2_000_000, purchaseLimit: 4, quantitySold: 200, perks: ['Vòng tay VIP', 'Poster'], descriptionVi: 'Khu VIP trái sân khấu, tầm nhìn tuyệt vời.', descriptionEn: 'VIP left of stage with amazing views and explosive atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z5-3', sessionId: 's5', name: 'VIP Right',    isStanding: true,  capacity: 400,  price: 2_000_000, purchaseLimit: 4, quantitySold: 190, perks: ['Vòng tay VIP', 'Poster'], descriptionVi: 'Khu VIP phải sân khấu, tầm nhìn tuyệt vời.', descriptionEn: 'VIP right of stage with amazing views and explosive atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z5-4', sessionId: 's5', name: 'GA',           isStanding: true,  capacity: 800,  price: 850_000,  purchaseLimit: 4, quantitySold: 380, descriptionVi: 'Khu GA tự do hòa mình vào không khí EDM và ánh sáng Cyberpunk.', descriptionEn: 'General admission to immerse in the EDM and Cyberpunk light atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z5-5', sessionId: 's5', name: 'GA Early Bird', isStanding: true, capacity: 600,  price: 650_000,  purchaseLimit: 4, quantitySold: 420, perks: ['Vòng tay vải'], descriptionVi: 'GA Early Bird ưu đãi cho người đặt sớm.', descriptionEn: 'GA Early Bird at special price for early bookers.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '6', name: 'Sắc Lam: The Indigo Echo',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [23, 9, 20],
    addressLine: '240-242 Phạm Văn Đồng, Phường Hiệp Bình, TP.Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775383130/sclam-wide_ymvfhg.png', square: 'https://cdn.ticket4u.uk/v1775383104/sclam-sqre_uaxxnl.png', tall: 'https://cdn.ticket4u.uk/v1775383057/sclam-tall_froaue.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Sắc Lam: The Indigo Echo là triển lãm nghệ thuật đa giác quan kết hợp giữa kỹ thuật nhuộm chàm truyền thống của Việt Nam và công nghệ trình chiếu ánh sáng hiện đại. Cùng khám phá linh hồn của vùng cao qua lăng kính đương đại ngay giữa lòng Sài Gòn.',
    aboutEn: 'Sắc Lam: The Indigo Echo is a premier immersive art expo blending traditional Vietnamese natural dyeing techniques with cutting-edge digital projection. Experience the soul of the highlands through a modern lens in the heart of Saigon.',
    termsAndConditions: 'Mỗi lượt tham quan tối đa 90 phút. Không chụp ảnh flash.',
    policyRefund: 'Không hỗ trợ hoàn tiền.',
    venueId: 'v7', latitude: 10.828149, longitude: 106.721385,
    layout: '{"venueId":"v7","venueMode":true,"zoneLinks":{"VIP":"z6-1","GA":"z6-2"}}',
    sessions: [
      {
        id: 's6', eventId: '6', name: 'session',
        startDate: '2026-05-02T09:00:00+07:00', endDate: '2026-05-03T21:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z6-1', sessionId: 's6', name: 'VIP (Kèm quà tặng)', isStanding: true, capacity: 150, price: 800_000, purchaseLimit: 2, quantitySold: 90, perks: ['Túi vải nhuộm chàm', 'Sách ảnh giới hạn', 'Lối vào sớm'], descriptionVi: 'Vé VIP bao gồm quà tặng nghệ thuật độc quyền.', descriptionEn: 'VIP ticket includes exclusive art gifts: handmade indigo-dyed tote bag and limited photo book.', createdAt: new Date().toISOString() },
          { id: 'z6-2', sessionId: 's6', name: 'Standard',          isStanding: true, capacity: 500, price: 350_000, purchaseLimit: 4, quantitySold: 220, descriptionVi: 'Vé tham quan tiêu chuẩn, khám phá toàn bộ không gian triển lãm.', descriptionEn: 'Standard admission ticket, explore the entire exhibition space.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '7', name: 'CRYSTAL REALM: The Neon Garden',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1, 16, 21, 17],
    addressLine: 'QL1A, Gamuda Central, Thủ đô Hà Nội',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775383196/cryst-wide_ccxmzc.png', square: 'https://cdn.ticket4u.uk/v1775383192/cryst-sqre_e76ssg.png', tall: 'https://cdn.ticket4u.uk/v1775383192/cryst-tall_h04ljf.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Crystal Realm: Vườn Neon Huyền Ảo là lễ hội cosplay ngoài trời cao cấp ngay tại trung tâm TP.HCM. Với sân khấu LED khổng lồ, các khu vực chụp ảnh chuyên nghiệp và dàn khách mời là những cosplayer quốc tế hàng đầu.',
    aboutEn: 'Crystal Realm: The Neon Garden is a premium outdoor cosplay festival in the heart of Ho Chi Minh City. Featuring a massive LED-integrated stage, professional photo zones, and a guest lineup of top-tier international cosplayers.',
    termsAndConditions: 'Trang phục cosplay được khuyến khích. Cấm trẻ em dưới 12 tuổi vào ban đêm.',
    policyRefund: 'Hoàn tiền 50% trước 5 ngày diễn ra.',
    venueId: 'v9', latitude: 20.964741, longitude: 105.854647,
    layout: '{"venueId":"v9","venueMode":true,"zoneLinks":{"VIP":"z7-1","Zone A":"z7-2","Zone B":"z7-3","GA":"z7-4"}}',
    sessions: [
      {
        id: 's7', eventId: '7', name: 'session',
        startDate: '2026-05-09T15:00:00+07:00', endDate: '2026-05-09T23:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z7-1', sessionId: 's7', name: 'VIP Crystal', isStanding: true, capacity: 150,  price: 1_800_000, purchaseLimit: 2, quantitySold: 80,  perks: ['Khu ảnh riêng', 'Meet & Greet', 'Nước uống'], descriptionVi: 'Khu VIP Crystal cao cấp với khu vực chụp ảnh riêng và cơ hội gặp gỡ cosplayer khách mời.', descriptionEn: 'Premium VIP Crystal area with private photo zone, beverages and meet-and-greet.', createdAt: new Date().toISOString() },
          { id: 'z7-2', sessionId: 's7', name: 'Zone A',      isStanding: true, capacity: 500,  price: 800_000,  purchaseLimit: 4, quantitySold: 260, perks: ['Vòng tay vải', 'Sticker'], descriptionVi: 'Khu Zone A bên trái sân khấu với góc nhìn tốt.', descriptionEn: 'Zone A left of stage with good views.', createdAt: new Date().toISOString() },
          { id: 'z7-3', sessionId: 's7', name: 'Zone B',      isStanding: true, capacity: 500,  price: 800_000,  purchaseLimit: 4, quantitySold: 240, perks: ['Vòng tay vải', 'Sticker'], descriptionVi: 'Khu Zone B bên phải sân khấu với góc nhìn tốt.', descriptionEn: 'Zone B right of stage with good views.', createdAt: new Date().toISOString() },
          { id: 'z7-4', sessionId: 's7', name: 'GA',          isStanding: true, capacity: 1000, price: 450_000,  purchaseLimit: 4, quantitySold: 520, descriptionVi: 'Khu GA tự do tham gia lễ hội cosplay.', descriptionEn: 'General admission to join the cosplay festival.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '8', name: 'SẮT & SON',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [0, 1, 10],
    addressLine: 'Xã Trường Yên, Huyện Hoa Lư, Tỉnh Ninh Bình',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775383391/satso-wide_vlujh3.png', square: 'https://cdn.ticket4u.uk/v1775383341/satso-sqre_vu6tau.png', tall: 'https://cdn.ticket4u.uk/v1775383338/satso-tall_dgbelz.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'SẮT & SON là lễ hội âm nhạc Rock lớn nhất năm 2026 tại Sài Gòn, quy tụ những biểu tượng Rock huyền thoại và các ban nhạc Indie đầy nổi loạn. Với không gian công nghiệp gai góc hòa quyện cùng nét văn hóa truyền thống.',
    aboutEn: 'SẮT & SON is the premier Rock festival of 2026 in Saigon, bringing together legendary rock icons and rebellious indie bands. Set in a gritty industrial atmosphere infused with traditional cultural motifs.',
    termsAndConditions: 'Cấm trẻ em dưới 16 tuổi. Không mang chất cấm. Trang phục tự do.',
    policyRefund: 'Không hoàn tiền sau khi mua vé.',
    venueId: 'v8', latitude: 20.284638, longitude: 105.905315,
    layout: '{"venueId":"v8","venueMode":true,"zoneLinks":{"VVIP":"z8-1","VIP":"z8-2","GA Standing":"z8-3"}}',
    sessions: [
      {
        id: 's8', eventId: '8', name: 'session',
        startDate: '2026-05-10T17:00:00+07:00', endDate: '2026-05-10T23:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z8-1', sessionId: 's8', name: 'VVIP Pit', isStanding: true, capacity: 100,  price: 3_000_000, purchaseLimit: 2, quantitySold: 65,  perks: ['Áo phông sự kiện', 'Meet & Greet', 'Vòng tay VVIP'], descriptionVi: 'Khu VVIP Pit ngay sát sân khấu, cảm nhận năng lượng Rock mạnh nhất.', descriptionEn: 'VVIP Pit directly at the stage for the most intense Rock energy.', createdAt: new Date().toISOString() },
          { id: 'z8-2', sessionId: 's8', name: 'VIP',      isStanding: true, capacity: 400,  price: 1_200_000, purchaseLimit: 4, quantitySold: 210, perks: ['Vòng tay VIP'], descriptionVi: 'Khu VIP đứng tự do với tầm nhìn tốt.', descriptionEn: 'VIP free standing with good views and special wristband.', createdAt: new Date().toISOString() },
          { id: 'z8-3', sessionId: 's8', name: 'GA',       isStanding: true, capacity: 2000, price: 500_000,  purchaseLimit: 4, quantitySold: 900, descriptionVi: 'Khu GA tự do hòa mình vào không khí Rock bùng cháy.', descriptionEn: 'General admission to immerse in the blazing Rock atmosphere.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '9', name: 'Techno-Sorcery: The Zenith of AI & Robotics',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [22, 9, 5, 25],
    addressLine: '8 Đường Nguyễn Bỉnh Khiêm, Phường Sài Gòn, TP.Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775383504/techs-wide_ekyhab.png', square: 'https://cdn.ticket4u.uk/v1775383502/techs-sqre_ah00iv.png', tall: 'https://cdn.ticket4u.uk/v1775383508/techs-tall_q41wkb.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Khám phá sự giao thoa giữa huyền bí cổ xưa và đổi mới tương lai tại Techno-Sorcery. Lễ hội duy nhất trong đêm hội tụ "phép thuật đen" của AI tiên tiến và robot hiệu suất cao, đặt trong không gian kiến trúc độc đáo của Bảo tàng Hà Nội.',
    aboutEn: 'Experience the fusion of ancient mysticism and futuristic innovation at Techno-Sorcery. This one-night-only festival showcases the "dark magic" of advanced AI and high-performance robotics, set against the iconic architecture of the Hanoi Museum.',
    termsAndConditions: 'Khuyến khích mang theo laptop. Phù hợp mọi lứa tuổi.',
    policyRefund: 'Hoàn tiền 50% nếu hủy trước 5 ngày.',
    venueId: 'v4', latitude: 10.790146, longitude: 106.702379,
    layout: '{"venueId":"v4","venueMode":true,"zoneLinks":{"VIP":"z9-1","Zone A":"z9-2","Zone B":"z9-3","Zone C":"z9-4","Standing":"z9-5"}}',
    sessions: [
      {
        id: 's9', eventId: '9', name: 'session',
        startDate: '2026-05-16T09:00:00+07:00', endDate: '2026-05-16T18:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z9-1', sessionId: 's9', name: 'VIP Scholars', isStanding: false, capacity: 80,  price: 2_500_000, purchaseLimit: 2, quantitySold: 50, perks: ['Túi quà tặng cao cấp', 'Lối vào sớm', 'Chương trình in kỷ niệm'], descriptionVi: 'Khu VIP dành cho các học giả và chuyên gia, hàng ghế đầu.', descriptionEn: 'VIP section for scholars and experts, front row seats with the best view.', createdAt: new Date().toISOString() },
          { id: 'z9-2', sessionId: 's9', name: 'Zone A',       isStanding: false, capacity: 120, price: 900_000,  purchaseLimit: 4, quantitySold: 70, descriptionVi: 'Khu Zone A bên trái hội trường.', descriptionEn: 'Zone A left side of the hall with good views.', createdAt: new Date().toISOString() },
          { id: 'z9-3', sessionId: 's9', name: 'Zone B',       isStanding: false, capacity: 120, price: 900_000,  purchaseLimit: 4, quantitySold: 65, descriptionVi: 'Khu Zone B bên phải hội trường.', descriptionEn: 'Zone B right side of the hall with good views.', createdAt: new Date().toISOString() },
          { id: 'z9-4', sessionId: 's9', name: 'Zone C',       isStanding: false, capacity: 150, price: 650_000,  purchaseLimit: 4, quantitySold: 75, descriptionVi: 'Khu Zone C phía sau hội trường, tầm nhìn toàn cảnh.', descriptionEn: 'Zone C rear of the hall with panoramic view.', createdAt: new Date().toISOString() },
          { id: 'z9-5', sessionId: 's9', name: 'Standing',     isStanding: true,  capacity: 100, price: 400_000,  purchaseLimit: 4, quantitySold: 40, descriptionVi: 'Khu đứng phía sau hội trường.', descriptionEn: 'Standing area at the rear for those who prefer more freedom.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '10', name: 'Vũ Trụ Cận Thị',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [0, 23, 15],
    addressLine: '519 Thôn Măng Lin, P. Langbiang, Đà Lạt, Tỉnh Lâm Đồng',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775383818/canth-wide_m4gbop.png', square: 'https://cdn.ticket4u.uk/v1775383821/canth-sqre_im0awy.png', tall: 'https://cdn.ticket4u.uk/v1775383941/dream-tall_fkwtkk.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Buổi ra mắt thân mật của "Lặng" – ban nhạc Indie-Dream Pop mới nổi. Một hành trình đa giác quan kết hợp giữa âm nhạc và nghệ thuật thị giác trừu tượng trong không gian biệt thự cổ kính giữa lòng Hà Nội.',
    aboutEn: 'An intimate, low-fidelity debut showcase for "Lặng," an emerging indie-dream pop band. Experience a sensory journey where music meets abstract visual art in one of Hanoi\'s most iconic colonial villas.',
    termsAndConditions: 'Sức chứa có hạn. Vui lòng đến đúng giờ. Không quay phim trong buổi diễn.',
    policyRefund: 'Không hoàn tiền vé.',
    venueId: 'v3', latitude: 11.978842, longitude: 108.393794,
    layout: '{"floors":[{"floor_name":"Main Area","floor_order":1,"global_seat_size":16,"stage":{"x1":-0.35,"y1":-0.96,"x2":0.35,"y2":-0.76},"stage_shapes":[],"zones":[{"zone_id":"z10-1","zone_name":"VVIP Intimate","zone_type":"sitting","accessible":true,"shape_type":"rect","color":"#f59e0b","rotation":0,"corner1":{"x":-0.45,"y":-0.74},"corner2":{"x":0.45,"y":-0.74},"corner3":{"x":0.45,"y":-0.18},"corner4":{"x":-0.45,"y":-0.18}},{"zone_id":"z10-2","zone_name":"Seated Standard","zone_type":"sitting","accessible":true,"shape_type":"rect","color":"#6366f1","rotation":0,"corner1":{"x":-0.85,"y":-0.16},"corner2":{"x":0.85,"y":-0.16},"corner3":{"x":0.85,"y":0.72},"corner4":{"x":-0.85,"y":0.72}}]}]}',
    sessions: [
      {
        id: 's10', eventId: '10', name: 'session',
        startDate: '2026-05-17T19:30:00+07:00', endDate: '2026-05-17T22:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z10-1', sessionId: 's10', name: 'VVIP Intimate',  isStanding: false, capacity: 20, price: 1_500_000, purchaseLimit: 2, quantitySold: 15, perks: ['Đĩa vinyl giới hạn', 'Chụp ảnh sau show', 'Nước uống'], descriptionVi: 'Hàng ghế đầu thân mật ngay cạnh nghệ sĩ.', descriptionEn: 'Intimate front row seats next to the artist. Includes limited vinyl record.', createdAt: new Date().toISOString() },
          { id: 'z10-2', sessionId: 's10', name: 'Seated Standard', isStanding: false, capacity: 80, price: 600_000,  purchaseLimit: 4, quantitySold: 45, descriptionVi: 'Ghế ngồi tiêu chuẩn thưởng thức âm nhạc indie trong không gian ấm cúng.', descriptionEn: 'Standard seated area to enjoy indie music in a cozy atmosphere.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '11', name: 'THE DREAMCATCHER ARCHIPELAGO',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1, 23, 19, 20],
    addressLine: '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775383940/dream-wide_knggr2.png', square: 'https://cdn.ticket4u.uk/v1775383942/dream-sqre_zlwxep.png', tall: 'https://cdn.ticket4u.uk/v1775383941/dream-tall_fkwtkk.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Đắm chìm trong Đảo Quốc Mộng Mơ, lễ hội âm nhạc và nghệ thuật kỳ ảo tại The Global City, TP.HCM. Trải nghiệm không gian thần tiên mang phong cách Disneyland nhưng đậm chất tâm hồn Việt với sen neon khổng lồ, sân khấu mây bồng bềnh.',
    aboutEn: 'Step into The Dreamcatcher Archipelago, an immersive whimsical festival at The Global City, HCMC. Experience a "Disneyland-style" magic reimagined with Vietnamese soul, featuring floating neon lotuses, cloud-shrouded stages.',
    termsAndConditions: 'Phù hợp mọi lứa tuổi. Trẻ em dưới 12 tuổi cần có người lớn đi kèm.',
    policyRefund: 'Hoàn tiền 50% trước 7 ngày diễn ra.',
    venueId: 'v10', latitude: 10.846066, longitude: 106.709149,
    layout: '{"venueId":"v10","venueMode":true,"zoneLinks":{"SVIP":"z11-1","VIP Left":"z11-2","VIP Right":"z11-3","GA Left":"z11-5","GA Early Bird":"z11-4"}}',
    sessions: [
      {
        id: 's11', eventId: '11', name: 'session',
        startDate: '2026-05-23T16:00:00+07:00', endDate: '2026-05-23T23:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z11-1', sessionId: 's11', name: 'SVIP Dream',    isStanding: false, capacity: 100,  price: 4_000_000, purchaseLimit: 2, quantitySold: 60,  perks: ['Open bar', 'Dreamy goodie bag', 'Lối vào ưu tiên', 'Meet & Greet'], descriptionVi: 'Khu SVIP Dream với không gian VIP riêng tư, open bar và gói quà tặng thần tiên độc quyền.', descriptionEn: 'SVIP Dream area with private VIP space, open bar and exclusive dreamy gift package.', createdAt: new Date().toISOString() },
          { id: 'z11-2', sessionId: 's11', name: 'VIP Left',      isStanding: true,  capacity: 250,  price: 1_500_000, purchaseLimit: 4, quantitySold: 130, perks: ['Vòng tay phát sáng', 'Poster sự kiện'], descriptionVi: 'Khu VIP trái đứng với tầm nhìn tốt và vòng tay phát sáng đặc biệt.', descriptionEn: 'VIP Left standing with great views and special glow wristband.', createdAt: new Date().toISOString() },
          { id: 'z11-3', sessionId: 's11', name: 'VIP Right',     isStanding: true,  capacity: 250,  price: 1_500_000, purchaseLimit: 4, quantitySold: 125, perks: ['Vòng tay phát sáng', 'Poster sự kiện'], descriptionVi: 'Khu VIP phải đứng với tầm nhìn tốt và vòng tay phát sáng đặc biệt.', descriptionEn: 'VIP Right standing with great views and special glow wristband.', createdAt: new Date().toISOString() },
          { id: 'z11-4', sessionId: 's11', name: 'GA Early Bird', isStanding: true,  capacity: 600,  price: 700_000,  purchaseLimit: 4, quantitySold: 370, perks: ['Vòng tay vải'], descriptionVi: 'GA Early Bird với giá ưu đãi và vòng tay vải đặc biệt.', descriptionEn: 'GA Early Bird at special price with exclusive fabric wristband.', createdAt: new Date().toISOString() },
          { id: 'z11-5', sessionId: 's11', name: 'GA',            isStanding: true,  capacity: 1000, price: 550_000,  purchaseLimit: 4, quantitySold: 500, descriptionVi: 'Khu GA tự do hòa mình vào thế giới mộng mơ của Dreamcatcher.', descriptionEn: 'General admission to immerse in the dreamy world of Dreamcatcher.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '12', name: 'KAIZEN: The Art of Precision',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [12, 20, 23],
    addressLine: '351/56 Đ. Lê Văn Sỹ, Phường Nhiêu Lộc, TP.Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775384284/kzart-wide_qvhiub.png', square: 'https://cdn.ticket4u.uk/v1775384345/kzart-sqre_v3harc.png', tall: 'https://cdn.ticket4u.uk/v1775384294/kzart-tall_lphcur.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Trải nghiệm Omakase cao cấp độc bản, kết hợp giữa kỹ nghệ truyền thống Nhật Bản và tinh hoa nguyên liệu mùa hè Việt Nam. Sự kiện giới hạn chỉ 24 khách cho một hành trình ẩm thực đầy cảm xúc.',
    aboutEn: 'An ultra-exclusive, high-end Omakase experience merging traditional Japanese craftsmanship with local Vietnamese seasonal treasures. Limited to 24 seats for an intimate journey of culinary storytelling.',
    termsAndConditions: 'Sự kiện giới hạn 24 khách. Dress code: smart casual. Đúng giờ tuyệt đối.',
    policyRefund: 'Không hoàn tiền sau khi xác nhận đặt chỗ.',
    venueId: 'v5', latitude: 10.785960, longitude: 106.676488,
    layout: '{"floors":[{"floor_name":"Dining Area","floor_order":1,"global_seat_size":18,"stage":{"x1":-0.6,"y1":-0.96,"x2":0.6,"y2":-0.76},"stage_shapes":[],"zones":[{"zone_id":"z12-1","zone_name":"Chef\'s Table","zone_type":"sitting","accessible":true,"shape_type":"rect","color":"#f59e0b","rotation":0,"corner1":{"x":-0.65,"y":-0.74},"corner2":{"x":0.65,"y":-0.74},"corner3":{"x":0.65,"y":-0.25},"corner4":{"x":-0.65,"y":-0.25}},{"zone_id":"z12-2","zone_name":"Dining Room","zone_type":"sitting","accessible":true,"shape_type":"rect","color":"#6366f1","rotation":0,"corner1":{"x":-0.85,"y":-0.23},"corner2":{"x":0.85,"y":-0.23},"corner3":{"x":0.85,"y":0.70},"corner4":{"x":-0.85,"y":0.70}}]}]}',
    sessions: [
      {
        id: 's12', eventId: '12', name: 'session',
        startDate: '2026-05-24T18:00:00+07:00', endDate: '2026-05-24T22:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z12-1', sessionId: 's12', name: "Chef's Table", isStanding: false, capacity: 8,  price: 3_500_000, purchaseLimit: 1, quantitySold: 8,  perks: ['Tương tác trực tiếp với bếp trưởng', 'Rượu vang đặc biệt', 'Thực đơn in kỷ niệm có chữ ký'], descriptionVi: "Bàn Chef's Table ngay trước quầy bếp mở, tương tác trực tiếp với Bếp trưởng.", descriptionEn: "Chef's Table directly in front of the open kitchen, interact directly with the Head Chef.", createdAt: new Date().toISOString() },
          { id: 'z12-2', sessionId: 's12', name: 'Dining Room',   isStanding: false, capacity: 16, price: 2_500_000, purchaseLimit: 2, quantitySold: 12, perks: ['Thực đơn 12 món', 'Rượu sake chọn lọc', 'Thực đơn in kỷ niệm'], descriptionVi: 'Bàn ăn trong phòng ấm cúng với trải nghiệm Omakase đầy đủ, thực đơn 12 món theo mùa.', descriptionEn: 'Dining room seating with full Omakase experience, 12-course seasonal menu.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '13', name: 'LUVIA: The Echo of Highlands',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [0, 11, 17, 28],
    addressLine: 'QL1A, Gamuda Central, Thủ đô Hà Nội',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775384450/luvia-wide_gyizyq.png', square: 'https://cdn.ticket4u.uk/v1775384446/luvia-sqre_lqtogb.png', tall: 'https://cdn.ticket4u.uk/v1775384451/luvia-tall_dzcyjo.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'LUVIA: The Echo of Highlands là một trải nghiệm âm thanh đa giác quan độc bản, kết hợp giữa âm nhạc ambient, tiếng vang của đại ngàn và nghệ thuật ánh sáng tại cao nguyên Mộc Châu. Diễn ra tại cây cầu kính Bạch Long kỷ lục.',
    aboutEn: 'LUVIA: The Echo of Highlands is a unique multi-sensory auditory experience blending ambient music, forest echoes, and light art in the heart of Moc Chau. Set against the backdrop of the record-breaking Bach Long Glass Bridge.',
    termsAndConditions: 'Trang phục thoải mái. Khuyến khích mang theo áo khoác.',
    policyRefund: 'Không hoàn tiền sau khi mua vé.',
    venueId: 'v9', latitude: 20.964741, longitude: 105.854647,
    layout: '{"venueId":"v9","venueMode":true,"zoneLinks":{"VIP":"z13-1","Zone A":"z13-2","Zone B":"z13-3","GA":"z13-4"}}',
    sessions: [
      {
        id: 's13', eventId: '13', name: 'session',
        startDate: '2026-05-30T17:00:00+07:00', endDate: '2026-05-30T21:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z13-1', sessionId: 's13', name: 'VIP Hammock', isStanding: false, capacity: 80,  price: 1_200_000, purchaseLimit: 2, quantitySold: 50,  perks: ['Võng/ghế nằm riêng', 'Nước uống tự nhiên', 'Chăn len'], descriptionVi: 'Khu VIP với võng và ghế nằm thư giãn.', descriptionEn: 'VIP hammock and lounge chair area, experience ambient music in the most beautiful natural setting.', createdAt: new Date().toISOString() },
          { id: 'z13-2', sessionId: 's13', name: 'Zone A',      isStanding: true,  capacity: 300, price: 600_000,  purchaseLimit: 4, quantitySold: 150, perks: ['Vòng tay vải'], descriptionVi: 'Khu Zone A bên trái, nghe nhạc ambient giữa thiên nhiên.', descriptionEn: 'Zone A left side, lie back and enjoy ambient music surrounded by nature.', createdAt: new Date().toISOString() },
          { id: 'z13-3', sessionId: 's13', name: 'Zone B',      isStanding: true,  capacity: 300, price: 600_000,  purchaseLimit: 4, quantitySold: 145, perks: ['Vòng tay vải'], descriptionVi: 'Khu Zone B bên phải, nghe nhạc ambient giữa thiên nhiên.', descriptionEn: 'Zone B right side, lie back and enjoy ambient music surrounded by nature.', createdAt: new Date().toISOString() },
          { id: 'z13-4', sessionId: 's13', name: 'GA',          isStanding: true,  capacity: 600, price: 350_000,  purchaseLimit: 4, quantitySold: 290, descriptionVi: 'Khu GA mở rộng phía sau để thưởng thức âm nhạc và thiên nhiên.', descriptionEn: 'Extended GA rear section to enjoy music and nature.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '14', name: 'Scent of the Soul: The Echoes of Vietnam',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [0, 20, 23, 11],
    addressLine: '8 Đường Nguyễn Bỉnh Khiêm, Phường Sài Gòn, TP.Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775390582/echov-wide_paluak.png', square: 'https://cdn.ticket4u.uk/v1775390593/echov-sqre_huj7ep.png', tall: 'https://cdn.ticket4u.uk/v1775390592/echov-tall_tyfbot.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Trải nghiệm "Hương Sắc Linh Hồn," buổi hòa nhạc 6D đa giác quan đầu tiên tại Việt Nam. Sự kết hợp giữa âm thanh vòm sống động và công nghệ kích hoạt mùi hương đồng bộ sẽ đưa người hâm mộ hành trình qua những vùng ký ức đặc trưng.',
    aboutEn: 'Experience "Scent of the Soul," Vietnam\'s first 6D olfactory-immersive indoor concert. Melding high-fidelity spatial audio with synchronized scent-triggering technology, this event transports fans through the aromatic landscapes of Vietnam.',
    termsAndConditions: 'Phù hợp mọi lứa tuổi. Không phù hợp với người dị ứng mùi hương mạnh.',
    policyRefund: 'Hoàn tiền 50% nếu hủy trước 5 ngày.',
    venueId: 'v4', latitude: 10.790146, longitude: 106.702379,
    layout: '{"venueId":"v4","venueMode":true,"zoneLinks":{"VIP":"z14-1","Zone A":"z14-2","Zone B":"z14-3","Zone C":"z14-4","Standing":"z14-5"}}',
    sessions: [
      {
        id: 's14', eventId: '14', name: 'session',
        startDate: '2026-05-31T19:00:00+07:00', endDate: '2026-05-31T22:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z14-1', sessionId: 's14', name: 'VIP Sensory', isStanding: false, capacity: 80,  price: 2_800_000, purchaseLimit: 2, quantitySold: 55, perks: ['Bộ khuếch tán hương thơm', 'Tai nghe 3D', 'Goodie bag'], descriptionVi: 'Ghế VIP hàng đầu với bộ khuếch tán hương thơm cá nhân và tai nghe 3D.', descriptionEn: 'Front VIP seats with personal scent diffuser, 3D headphones and the fullest 6D experience.', createdAt: new Date().toISOString() },
          { id: 'z14-2', sessionId: 's14', name: 'Zone A',       isStanding: false, capacity: 120, price: 1_200_000, purchaseLimit: 4, quantitySold: 65, descriptionVi: 'Khu Zone A bên trái với trải nghiệm âm thanh vòm và hương thơm.', descriptionEn: 'Zone A left side with spatial audio and synchronized scent experience.', createdAt: new Date().toISOString() },
          { id: 'z14-3', sessionId: 's14', name: 'Zone B',       isStanding: false, capacity: 120, price: 1_200_000, purchaseLimit: 4, quantitySold: 60, descriptionVi: 'Khu Zone B bên phải với trải nghiệm âm thanh vòm và hương thơm.', descriptionEn: 'Zone B right side with spatial audio and synchronized scent experience.', createdAt: new Date().toISOString() },
          { id: 'z14-4', sessionId: 's14', name: 'Zone C',       isStanding: false, capacity: 150, price: 900_000,  purchaseLimit: 4, quantitySold: 70, descriptionVi: 'Khu Zone C phía sau với tầm nhìn toàn cảnh.', descriptionEn: 'Zone C rear with panoramic view and scent experience.', createdAt: new Date().toISOString() },
          { id: 'z14-5', sessionId: 's14', name: 'Standing',     isStanding: true,  capacity: 80,  price: 600_000,  purchaseLimit: 4, quantitySold: 40, descriptionVi: 'Khu đứng phía sau cho những ai muốn tự do di chuyển.', descriptionEn: 'Standing area at the rear for those who prefer freedom of movement.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },

  {
    id: '15', name: 'Âm Sắc Việt - THE RESONANCE',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [0, 20, 24],
    addressLine: '7, Đường số 23, Phú Mỹ Hưng, Phường Tân Mỹ, TP.Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: { wide: 'https://cdn.ticket4u.uk/v1775390591/reson-wide_aossid.png', square: 'https://cdn.ticket4u.uk/v1775390583/reson-sqre_igosvf.png', tall: 'https://cdn.ticket4u.uk/v1775390583/reson-tall_segvto.png' },
    createdAt: new Date().toISOString(),
    aboutVi: 'Khám phá sức mạnh của tiếng Việt thông qua âm nhạc tại Âm Sắc Việt - THE RESONANCE. Một đêm nhạc tương tác đa giác quan, nơi ranh giới giữa nghệ sĩ và khán giả bị xóa nhòa bởi những thử thách ngôn ngữ.',
    aboutEn: 'Experience the power of the Vietnamese language through music at Âm Sắc Việt - THE RESONANCE. An interactive, multi-sensory concert where the line between performer and audience fades through linguistic challenges and cutting-edge light shows.',
    termsAndConditions: 'Vui lòng mang theo CCCD khi check-in. Không ảnh hưởng đến khán giả xung quanh.',
    policyRefund: 'Không hoàn tiền sau khi mua vé.',
    venueId: 'v1', latitude: 10.713750, longitude: 106.728750,
    layout: '{"venueId":"v1","venueMode":true,"zoneLinks":{"VIP Floor":"z15-1","Lower Bowl Left":"z15-2","Lower Bowl Right":"z15-3","GA Pit":"z15-4","Lower Bowl Back":"z15-5","Upper Bowl":"z15-6"}}',
    sessions: [
      {
        id: 's15', eventId: '15', name: 'session',
        startDate: '2026-06-06T18:00:00+07:00', endDate: '2026-06-06T23:00:00+07:00',
        status: 'SELLING', createdAt: new Date().toISOString(),
        zones: [
          { id: 'z15-1', sessionId: 's15', name: 'VIP Floor',       isStanding: false, capacity: 150, price: 2_500_000, purchaseLimit: 2, quantitySold: 90,  perks: ['Ghế ngồi cao cấp', 'Vòng tay VIP', 'Goodie bag'], descriptionVi: 'Khu VIP Floor sát sân khấu, ghế ngồi cao cấp gần nghệ sĩ nhất.', descriptionEn: 'VIP Floor closest to stage, premium seating for the most interactive experience.', createdAt: new Date().toISOString() },
          { id: 'z15-2', sessionId: 's15', name: 'Lower Bowl Left',  isStanding: false, capacity: 300, price: 900_000,  purchaseLimit: 4, quantitySold: 150, descriptionVi: 'Khán đài trái phía dưới với tầm nhìn tốt.', descriptionEn: 'Lower left bleachers with good views and vibrant atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z15-3', sessionId: 's15', name: 'Lower Bowl Right', isStanding: false, capacity: 300, price: 900_000,  purchaseLimit: 4, quantitySold: 145, descriptionVi: 'Khán đài phải phía dưới với tầm nhìn tốt.', descriptionEn: 'Lower right bleachers with good views and vibrant atmosphere.', createdAt: new Date().toISOString() },
          { id: 'z15-4', sessionId: 's15', name: 'GA Pit',           isStanding: false, capacity: 300, price: 700_000,  purchaseLimit: 4, quantitySold: 160, descriptionVi: 'Khu GA Pit giữa sân với tầm nhìn tốt.', descriptionEn: 'GA Pit center court with good views.', createdAt: new Date().toISOString() },
          { id: 'z15-5', sessionId: 's15', name: 'Lower Bowl Back',  isStanding: false, capacity: 400, price: 550_000,  purchaseLimit: 4, quantitySold: 180, descriptionVi: 'Khán đài phía sau dưới, tầm nhìn toàn cảnh sân khấu.', descriptionEn: 'Lower back bleachers with panoramic stage view.', createdAt: new Date().toISOString() },
          { id: 'z15-6', sessionId: 's15', name: 'Upper Bowl',       isStanding: false, capacity: 500, price: 350_000,  purchaseLimit: 4, quantitySold: 220, descriptionVi: 'Khán đài tầng trên với tầm nhìn toàn cảnh và giá cả phải chăng.', descriptionEn: 'Upper bowl with panoramic view at an affordable price.', createdAt: new Date().toISOString() },
        ],
      },
    ],
  },
]

// ── Derived helpers ───────────────────────────────────────────
// All zone access now goes through sessions

export function getAllZones(event: Event): Zone[] {
  return event.sessions.flatMap(s => s.zones)
}

export function getTicketsSold(event: Event): number {
  return getAllZones(event).reduce((s, z) => s + (z.quantitySold ?? 0), 0)
}

export function getTotalCapacity(event: Event): number {
  return getAllZones(event).reduce((s, z) => s + z.capacity, 0)
}

export function getRevenue(event: Event): number {
  return getAllZones(event).reduce((s, z) => s + (z.quantitySold ?? 0) * z.price, 0)
}

/** Returns start date of the first session */
export function getSessionStart(event: Event): string {
  return event.sessions[0]?.startDate ?? ''
}

export function getStatusClass(status: string): string {
  return ({
    EDITING:   'bg-secondary bg-opacity-10 text-secondary',
    SCHEDULED: 'bg-info bg-opacity-10 text-info',
    PREMIERE:  'bg-info bg-opacity-10 text-info',
    SELLING:   'bg-primary bg-opacity-10 text-primary',
    PAUSED:    'bg-warning bg-opacity-10 text-warning',
    ONGOING:   'bg-success bg-opacity-10 text-success',
    FINISHED:  'bg-dark bg-opacity-10 text-secondary',
    CANCELLED: 'bg-danger bg-opacity-10 text-danger',
  } as Record<string, string>)[status] ?? 'bg-secondary bg-opacity-10 text-secondary'
}

export function getStatusI18nKey(status: string): string {
  return ({
    EDITING:   'manage.events.status.editing',
    SCHEDULED: 'manage.events.status.premier',
    PREMIERE:  'manage.events.status.premier',
    SELLING:   'manage.events.status.selling',
    PAUSED:    'manage.events.status.paused',
    ONGOING:   'manage.events.status.ongoing',
    FINISHED:  'manage.events.status.finished',
    CANCELLED: 'manage.events.status.cancelled',
  } as Record<string, string>)[status] ?? status
}

export function formatPrice(p: number): string {
  return new Intl.NumberFormat('vi-VN').format(p) + ' ₫'
}

export function formatDate(d: string): string {
  return new Date(d).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' })
}

export function formatTime(d: string): string {
  return new Date(d).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
}

/** Returns venue layout zone names — used for zone linking in Step4 */
export function getVenueZoneNames(venue: Venue): string[] {
  if (venue.layout?.floors?.length) {
    return venue.layout.floors.flatMap(f => f.zones.map(z => z.zone_name))
  }
  return venue.layout?.zones?.map(z => z.zone_name) ?? []
}

// ── Staff ─────────────────────────────────────────────────────

export const ASSIGNABLE_ROLES: AssignableRole[] = [
  { id: 1,  key: 'event_manager',   icon: 'bi-calendar-event', color: '#3b82f6' },
  { id: 10, key: 'gatekeeper',      icon: 'bi-door-open-fill', color: '#22c55e' },
  { id: 11, key: 'support_agent',   icon: 'bi-headset',        color: '#f59e0b' },
  { id: 12, key: 'finance_manager', icon: 'bi-cash-stack',     color: '#ec4899' },
]

export const mockStaff: OrganizerStaff[] = [
  {
    id: 'staff-1', email: 'nguyen.van.b@example.com', username: 'nvb',
    firstName: 'Văn B', lastName: 'Nguyễn',
    roleId: 1, invitedAt: '2025-01-15T08:00:00Z', isActive: true,
  },
  {
    id: 'staff-2', email: 'tran.thi.c@example.com', username: 'ttc',
    firstName: 'Thị C', lastName: 'Trần',
    roleId: 10, invitedAt: '2025-02-10T08:00:00Z', isActive: true,
  },
  {
    id: 'staff-3', email: 'le.van.d@example.com', username: 'lvd',
    firstName: 'Văn D', lastName: 'Lê',
    roleId: 10, invitedAt: '2025-02-18T08:00:00Z', isActive: true,
  },
  {
    id: 'staff-4', email: 'pham.finance@example.com', username: 'pfinance',
    firstName: 'Tài Chính', lastName: 'Phạm',
    roleId: 12, invitedAt: '2025-03-01T08:00:00Z', isActive: false,
  },
  {
    id: 'staff-5', email: 'support.agent@example.com', username: 'support1',
    firstName: 'Hỗ Trợ', lastName: 'Hồ',
    roleId: 11, invitedAt: '2025-03-20T08:00:00Z', isActive: true,
  },
]

export function getStaffDisplayName(s: OrganizerStaff): string {
  if (s.firstName || s.lastName) return [s.firstName, s.lastName].filter(Boolean).join(' ')
  return s.username
}

export function getAssignableRole(roleId: number): AssignableRole | undefined {
  return ASSIGNABLE_ROLES.find(function (r) { return r.id === roleId })
}