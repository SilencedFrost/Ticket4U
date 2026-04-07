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
import type { Event, EventStatus, EventFormState, EventContentState } from './(types)/event'
import type { Profile }            from './(types)/profile'
import type { Seat }               from './(types)/seat'
import type { OrganizerStaff, AssignableRole } from './(types)/staff'

// Re-export types so existing imports still work
export type {
  Category, Venue, VenueLayout, Zone, Session,
  Event, EventStatus, EventFormState, EventContentState,
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
  { id: 1, name: 'Âm nhạc' },
  { id: 2, name: 'Hội thảo' },
  { id: 3, name: 'Sân khấu - Nghệ thuật' },
  { id: 4, name: 'Thể thao' },
  { id: 5, name: 'Giải trí về đêm' },
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
      stage: { x1: -0.3, y1: -0.3, x2: 0.3, y2: 0.3 },
      seat_size: 20,
      zones: [
        {
          zone_name: 'Courtside Left', zone_type: 'sitting', color: '#F59E0B',
          corner1: { x: -0.9, y: -0.5 }, corner2: { x: -0.35, y: -0.5 },
          corner3: { x: -0.35, y: 0.5 }, corner4: { x: -0.9, y: 0.5 },
          seats: [
            { seat_name: 'L-A1', seat_id: 'L-A1', seat_pos: { x: -0.8, y: -0.35 }, seat_rotation: 90 },
            { seat_name: 'L-A2', seat_id: 'L-A2', seat_pos: { x: -0.8, y: -0.1  }, seat_rotation: 90 },
            { seat_name: 'L-A3', seat_id: 'L-A3', seat_pos: { x: -0.8, y:  0.15 }, seat_rotation: 90 },
            { seat_name: 'L-A4', seat_id: 'L-A4', seat_pos: { x: -0.8, y:  0.4  }, seat_rotation: 90 },
            { seat_name: 'L-B1', seat_id: 'L-B1', seat_pos: { x: -0.6, y: -0.35 }, seat_rotation: 90 },
            { seat_name: 'L-B2', seat_id: 'L-B2', seat_pos: { x: -0.6, y: -0.1  }, seat_rotation: 90 },
            { seat_name: 'L-B3', seat_id: 'L-B3', seat_pos: { x: -0.6, y:  0.15 }, seat_rotation: 90 },
            { seat_name: 'L-B4', seat_id: 'L-B4', seat_pos: { x: -0.6, y:  0.4  }, seat_rotation: 90 },
          ],
        },
        {
          zone_name: 'Courtside Right', zone_type: 'sitting', color: '#F59E0B',
          corner1: { x: 0.35, y: -0.5 }, corner2: { x: 0.9, y: -0.5 },
          corner3: { x: 0.9, y: 0.5 },   corner4: { x: 0.35, y: 0.5 },
          seats: [
            { seat_name: 'R-A1', seat_id: 'R-A1', seat_pos: { x:  0.6, y: -0.35 }, seat_rotation: -90 },
            { seat_name: 'R-A2', seat_id: 'R-A2', seat_pos: { x:  0.6, y: -0.1  }, seat_rotation: -90 },
            { seat_name: 'R-A3', seat_id: 'R-A3', seat_pos: { x:  0.6, y:  0.15 }, seat_rotation: -90 },
            { seat_name: 'R-A4', seat_id: 'R-A4', seat_pos: { x:  0.6, y:  0.4  }, seat_rotation: -90 },
            { seat_name: 'R-B1', seat_id: 'R-B1', seat_pos: { x:  0.8, y: -0.35 }, seat_rotation: -90 },
            { seat_name: 'R-B2', seat_id: 'R-B2', seat_pos: { x:  0.8, y: -0.1  }, seat_rotation: -90 },
            { seat_name: 'R-B3', seat_id: 'R-B3', seat_pos: { x:  0.8, y:  0.15 }, seat_rotation: -90 },
            { seat_name: 'R-B4', seat_id: 'R-B4', seat_pos: { x:  0.8, y:  0.4  }, seat_rotation: -90 },
          ],
        },
        {
          zone_name: 'Bleachers', zone_type: 'sitting', color: '#64748B',
          corner1: { x: -0.9, y: 0.55 }, corner2: { x: 0.9, y: 0.55 },
          corner3: { x: 0.9, y: 0.95 },  corner4: { x: -0.9, y: 0.95 },
          seats: [
            { seat_name: 'BL-A1', seat_id: 'BL-A1', seat_pos: { x: -0.72, y: 0.68 }, seat_rotation: 0 },
            { seat_name: 'BL-A2', seat_id: 'BL-A2', seat_pos: { x: -0.48, y: 0.68 }, seat_rotation: 0 },
            { seat_name: 'BL-A3', seat_id: 'BL-A3', seat_pos: { x: -0.24, y: 0.68 }, seat_rotation: 0 },
            { seat_name: 'BL-A4', seat_id: 'BL-A4', seat_pos: { x:  0.0,  y: 0.68 }, seat_rotation: 0 },
            { seat_name: 'BL-A5', seat_id: 'BL-A5', seat_pos: { x:  0.24, y: 0.68 }, seat_rotation: 0 },
            { seat_name: 'BL-A6', seat_id: 'BL-A6', seat_pos: { x:  0.48, y: 0.68 }, seat_rotation: 0 },
            { seat_name: 'BL-A7', seat_id: 'BL-A7', seat_pos: { x:  0.72, y: 0.68 }, seat_rotation: 0 },
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
      stage: { x1: -0.5, y1: -1.0, x2: 0.5, y2: -0.88 },
      seat_size: 22,
      zones: [
        {
          zone_name: 'VIP', zone_type: 'sitting', color: '#DC2626',
          corner1: { x: -0.85, y: -0.85 }, corner2: { x: 0.85, y: -0.85 },
          corner3: { x: 0.85, y: -0.1 },   corner4: { x: -0.85, y: -0.1 },
          seats: [
            { seat_name: 'A1', seat_id: 'A1', seat_pos: { x: -0.65, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A2', seat_id: 'A2', seat_pos: { x: -0.4,  y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A3', seat_id: 'A3', seat_pos: { x: -0.15, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A4', seat_id: 'A4', seat_pos: { x:  0.15, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A5', seat_id: 'A5', seat_pos: { x:  0.4,  y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A6', seat_id: 'A6', seat_pos: { x:  0.65, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'B1', seat_id: 'B1', seat_pos: { x: -0.65, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B2', seat_id: 'B2', seat_pos: { x: -0.4,  y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B3', seat_id: 'B3', seat_pos: { x: -0.15, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B4', seat_id: 'B4', seat_pos: { x:  0.15, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B5', seat_id: 'B5', seat_pos: { x:  0.4,  y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B6', seat_id: 'B6', seat_pos: { x:  0.65, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'C1', seat_id: 'C1', seat_pos: { x: -0.65, y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C2', seat_id: 'C2', seat_pos: { x: -0.4,  y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C3', seat_id: 'C3', seat_pos: { x: -0.15, y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C4', seat_id: 'C4', seat_pos: { x:  0.15, y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C5', seat_id: 'C5', seat_pos: { x:  0.4,  y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C6', seat_id: 'C6', seat_pos: { x:  0.65, y: -0.25 }, seat_rotation: 0 },
          ],
        },
        {
          zone_name: 'Standard', zone_type: 'sitting', color: '#059669',
          corner1: { x: -0.85, y: -0.05 }, corner2: { x: 0.85, y: -0.05 },
          corner3: { x: 0.85, y: 0.9 },    corner4: { x: -0.85, y: 0.9 },
          seats: [
            { seat_name: 'D1', seat_id: 'D1', seat_pos: { x: -0.65, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D2', seat_id: 'D2', seat_pos: { x: -0.4,  y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D3', seat_id: 'D3', seat_pos: { x: -0.15, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D4', seat_id: 'D4', seat_pos: { x:  0.15, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D5', seat_id: 'D5', seat_pos: { x:  0.4,  y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D6', seat_id: 'D6', seat_pos: { x:  0.65, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'E1', seat_id: 'E1', seat_pos: { x: -0.65, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E2', seat_id: 'E2', seat_pos: { x: -0.4,  y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E3', seat_id: 'E3', seat_pos: { x: -0.15, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E4', seat_id: 'E4', seat_pos: { x:  0.15, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E5', seat_id: 'E5', seat_pos: { x:  0.4,  y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E6', seat_id: 'E6', seat_pos: { x:  0.65, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'F1', seat_id: 'F1', seat_pos: { x: -0.65, y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F2', seat_id: 'F2', seat_pos: { x: -0.4,  y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F3', seat_id: 'F3', seat_pos: { x: -0.15, y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F4', seat_id: 'F4', seat_pos: { x:  0.15, y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F5', seat_id: 'F5', seat_pos: { x:  0.4,  y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F6', seat_id: 'F6', seat_pos: { x:  0.65, y: 0.6  }, seat_rotation: 0 },
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
      stage: { x1: -0.4, y1: -1.0, x2: 0.4, y2: -0.85 },
      seat_size: 22,
      zones: [
        {
          zone_name: 'VIP', zone_type: 'sitting', color: '#DC2626',
          corner1: { x: -0.85, y: -0.82 }, corner2: { x: 0.85, y: -0.82 },
          corner3: { x: 0.85, y: -0.2 },   corner4: { x: -0.85, y: -0.2 },
          seats: [
            { seat_name: 'A1', seat_id: 'A1', seat_pos: { x: -0.65, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'A2', seat_id: 'A2', seat_pos: { x: -0.4,  y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'A3', seat_id: 'A3', seat_pos: { x: -0.15, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'A4', seat_id: 'A4', seat_pos: { x:  0.15, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'A5', seat_id: 'A5', seat_pos: { x:  0.4,  y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'A6', seat_id: 'A6', seat_pos: { x:  0.65, y: -0.7  }, seat_rotation: 0 },
            { seat_name: 'B1', seat_id: 'B1', seat_pos: { x: -0.65, y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'B2', seat_id: 'B2', seat_pos: { x: -0.4,  y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'B3', seat_id: 'B3', seat_pos: { x: -0.15, y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'B4', seat_id: 'B4', seat_pos: { x:  0.15, y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'B5', seat_id: 'B5', seat_pos: { x:  0.4,  y: -0.45 }, seat_rotation: 0 },
            { seat_name: 'B6', seat_id: 'B6', seat_pos: { x:  0.65, y: -0.45 }, seat_rotation: 0 },
          ],
        },
        {
          zone_name: 'GA', zone_type: 'standing', color: '#2563EB',
          corner1: { x: -0.85, y: -0.15 }, corner2: { x: 0.85, y: -0.15 },
          corner3: { x: 0.85, y: 0.9 },    corner4: { x: -0.85, y: 0.9 },
          seats: [],
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
      stage: { x1: -0.5, y1: -1.0, x2: 0.5, y2: -0.88 },
      seat_size: 22,
      zones: [
        {
          zone_name: 'VIP', zone_type: 'sitting', color: '#DC2626',
          corner1: { x: -0.85, y: -0.85 }, corner2: { x: 0.85, y: -0.85 },
          corner3: { x: 0.85, y: -0.1 },   corner4: { x: -0.85, y: -0.1 },
          seats: [
            { seat_name: 'A1', seat_id: 'A1', seat_pos: { x: -0.65, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A2', seat_id: 'A2', seat_pos: { x: -0.4,  y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A3', seat_id: 'A3', seat_pos: { x: -0.15, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A4', seat_id: 'A4', seat_pos: { x:  0.15, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A5', seat_id: 'A5', seat_pos: { x:  0.4,  y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A6', seat_id: 'A6', seat_pos: { x:  0.65, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'B1', seat_id: 'B1', seat_pos: { x: -0.65, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B2', seat_id: 'B2', seat_pos: { x: -0.4,  y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B3', seat_id: 'B3', seat_pos: { x: -0.15, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B4', seat_id: 'B4', seat_pos: { x:  0.15, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B5', seat_id: 'B5', seat_pos: { x:  0.4,  y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'B6', seat_id: 'B6', seat_pos: { x:  0.65, y: -0.55 }, seat_rotation: 0 },
            { seat_name: 'C1', seat_id: 'C1', seat_pos: { x: -0.65, y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C2', seat_id: 'C2', seat_pos: { x: -0.4,  y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C3', seat_id: 'C3', seat_pos: { x: -0.15, y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C4', seat_id: 'C4', seat_pos: { x:  0.15, y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C5', seat_id: 'C5', seat_pos: { x:  0.4,  y: -0.25 }, seat_rotation: 0 },
            { seat_name: 'C6', seat_id: 'C6', seat_pos: { x:  0.65, y: -0.25 }, seat_rotation: 0 },
          ],
        },
        {
          zone_name: 'Standard', zone_type: 'sitting', color: '#059669',
          corner1: { x: -0.85, y: -0.05 }, corner2: { x: 0.85, y: -0.05 },
          corner3: { x: 0.85, y: 0.9 },    corner4: { x: -0.85, y: 0.9 },
          seats: [
            { seat_name: 'D1', seat_id: 'D1', seat_pos: { x: -0.65, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D2', seat_id: 'D2', seat_pos: { x: -0.4,  y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D3', seat_id: 'D3', seat_pos: { x: -0.15, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D4', seat_id: 'D4', seat_pos: { x:  0.15, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D5', seat_id: 'D5', seat_pos: { x:  0.4,  y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'D6', seat_id: 'D6', seat_pos: { x:  0.65, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'E1', seat_id: 'E1', seat_pos: { x: -0.65, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E2', seat_id: 'E2', seat_pos: { x: -0.4,  y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E3', seat_id: 'E3', seat_pos: { x: -0.15, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E4', seat_id: 'E4', seat_pos: { x:  0.15, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E5', seat_id: 'E5', seat_pos: { x:  0.4,  y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'E6', seat_id: 'E6', seat_pos: { x:  0.65, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'F1', seat_id: 'F1', seat_pos: { x: -0.65, y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F2', seat_id: 'F2', seat_pos: { x: -0.4,  y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F3', seat_id: 'F3', seat_pos: { x: -0.15, y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F4', seat_id: 'F4', seat_pos: { x:  0.15, y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F5', seat_id: 'F5', seat_pos: { x:  0.4,  y: 0.6  }, seat_rotation: 0 },
            { seat_name: 'F6', seat_id: 'F6', seat_pos: { x:  0.65, y: 0.6  }, seat_rotation: 0 },
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
      stage: { x1: -0.15, y1: -1.0, x2: 0.15, y2: -0.9 },
      seat_size: 22,
      zones: [
        {
          zone_name: 'Window Section', zone_type: 'sitting', color: '#DC2626',
          corner1: { x: -0.9, y: -0.85 }, corner2: { x: -0.3, y: -0.85 },
          corner3: { x: -0.3, y: 0.9 },   corner4: { x: -0.9, y: 0.9 },
          seats: [
            { seat_name: 'W1', seat_id: 'W1', seat_pos: { x: -0.7, y: -0.65 }, seat_rotation:  90 },
            { seat_name: 'W2', seat_id: 'W2', seat_pos: { x: -0.7, y: -0.35 }, seat_rotation:  90 },
            { seat_name: 'W3', seat_id: 'W3', seat_pos: { x: -0.7, y: -0.05 }, seat_rotation:  90 },
            { seat_name: 'W4', seat_id: 'W4', seat_pos: { x: -0.7, y:  0.25 }, seat_rotation:  90 },
            { seat_name: 'W5', seat_id: 'W5', seat_pos: { x: -0.7, y:  0.55 }, seat_rotation:  90 },
          ],
        },
        {
          zone_name: 'Main Floor', zone_type: 'sitting', color: '#059669',
          corner1: { x: -0.25, y: -0.85 }, corner2: { x: 0.25, y: -0.85 },
          corner3: { x: 0.25, y: 0.9 },    corner4: { x: -0.25, y: 0.9 },
          seats: [
            { seat_name: 'M1', seat_id: 'M1', seat_pos: { x: 0.0, y: -0.65 }, seat_rotation: 0 },
            { seat_name: 'M2', seat_id: 'M2', seat_pos: { x: 0.0, y: -0.35 }, seat_rotation: 0 },
            { seat_name: 'M3', seat_id: 'M3', seat_pos: { x: 0.0, y: -0.05 }, seat_rotation: 0 },
            { seat_name: 'M4', seat_id: 'M4', seat_pos: { x: 0.0, y:  0.25 }, seat_rotation: 0 },
            { seat_name: 'M5', seat_id: 'M5', seat_pos: { x: 0.0, y:  0.55 }, seat_rotation: 0 },
          ],
        },
        {
          zone_name: 'Private Room', zone_type: 'sitting', color: '#7C3AED',
          corner1: { x: 0.3, y: -0.85 }, corner2: { x: 0.9, y: -0.85 },
          corner3: { x: 0.9, y: 0.9 },   corner4: { x: 0.3, y: 0.9 },
          seats: [
            { seat_name: 'P1', seat_id: 'P1', seat_pos: { x: 0.6, y: -0.65 }, seat_rotation: -90 },
            { seat_name: 'P2', seat_id: 'P2', seat_pos: { x: 0.6, y: -0.35 }, seat_rotation: -90 },
            { seat_name: 'P3', seat_id: 'P3', seat_pos: { x: 0.6, y: -0.05 }, seat_rotation: -90 },
            { seat_name: 'P4', seat_id: 'P4', seat_pos: { x: 0.6, y:  0.25 }, seat_rotation: -90 },
            { seat_name: 'P5', seat_id: 'P5', seat_pos: { x: 0.6, y:  0.55 }, seat_rotation: -90 },
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
      stage: { x1: -0.5, y1: -1.0, x2: 0.5, y2: -0.88 },
      seat_size: 22,
      zones: [
        {
          zone_name: 'VIP', zone_type: 'sitting', color: '#059669',
          corner1: { x: -0.85, y: -0.85 }, corner2: { x: 0.85, y: -0.85 },
          corner3: { x: 0.85, y: -0.1 },   corner4: { x: -0.85, y: -0.1 },
          seats: [
            { seat_name: 'A1', seat_id: 'A1', seat_pos: { x: -0.65, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A2', seat_id: 'A2', seat_pos: { x: -0.4,  y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A3', seat_id: 'A3', seat_pos: { x: -0.15, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A4', seat_id: 'A4', seat_pos: { x:  0.15, y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A5', seat_id: 'A5', seat_pos: { x:  0.4,  y: -0.75 }, seat_rotation: 0 },
            { seat_name: 'A6', seat_id: 'A6', seat_pos: { x:  0.65, y: -0.75 }, seat_rotation: 0 },
          ],
        },
        {
          zone_name: 'Standard', zone_type: 'sitting', color: '#0D9488',
          corner1: { x: -0.85, y: -0.05 }, corner2: { x: 0.85, y: -0.05 },
          corner3: { x: 0.85, y: 0.9 },    corner4: { x: -0.85, y: 0.9 },
          seats: [
            { seat_name: 'B1', seat_id: 'B1', seat_pos: { x: -0.65, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'B2', seat_id: 'B2', seat_pos: { x: -0.4,  y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'B3', seat_id: 'B3', seat_pos: { x: -0.15, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'B4', seat_id: 'B4', seat_pos: { x:  0.15, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'B5', seat_id: 'B5', seat_pos: { x:  0.4,  y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'B6', seat_id: 'B6', seat_pos: { x:  0.65, y: 0.1  }, seat_rotation: 0 },
            { seat_name: 'C1', seat_id: 'C1', seat_pos: { x: -0.65, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'C2', seat_id: 'C2', seat_pos: { x: -0.4,  y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'C3', seat_id: 'C3', seat_pos: { x: -0.15, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'C4', seat_id: 'C4', seat_pos: { x:  0.15, y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'C5', seat_id: 'C5', seat_pos: { x:  0.4,  y: 0.35 }, seat_rotation: 0 },
            { seat_name: 'C6', seat_id: 'C6', seat_pos: { x:  0.65, y: 0.35 }, seat_rotation: 0 },
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
      stage: { x1: -0.4, y1: -1.0, x2: 0.4, y2: -0.85 },
      seat_size: 20,
      zones: [
        {
          zone_name: 'VIP', zone_type: 'standing', color: '#7C3AED',
          corner1: { x: -0.9, y: -0.8 }, corner2: { x: 0.9, y: -0.8 },
          corner3: { x: 0.9, y: -0.2 },  corner4: { x: -0.9, y: -0.2 },
          seats: [],
        },
        {
          zone_name: 'GA', zone_type: 'standing', color: '#5B21B6',
          corner1: { x: -0.9, y: -0.15 }, corner2: { x: 0.9, y: -0.15 },
          corner3: { x: 0.9, y: 0.9 },    corner4: { x: -0.9, y: 0.9 },
          seats: [],
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
      stage: { x1: -0.4, y1: -1.0, x2: 0.4, y2: -0.85 },
      seat_size: 20,
      zones: [
        {
          zone_name: 'VVIP', zone_type: 'standing', color: '#DC2626',
          corner1: { x: -0.9, y: -0.8  }, corner2: { x: 0.9, y: -0.8  },
          corner3: { x: 0.9, y: -0.35 },  corner4: { x: -0.9, y: -0.35 },
          seats: [],
        },
        {
          zone_name: 'VIP', zone_type: 'standing', color: '#F59E0B',
          corner1: { x: -0.9, y: -0.3 }, corner2: { x: 0.9, y: -0.3 },
          corner3: { x: 0.9, y: 0.2 },   corner4: { x: -0.9, y: 0.2 },
          seats: [],
        },
        {
          zone_name: 'GA Standing', zone_type: 'standing', color: '#2563EB',
          corner1: { x: -0.9, y: 0.25 }, corner2: { x: 0.9, y: 0.25 },
          corner3: { x: 0.9, y: 0.9 },   corner4: { x: -0.9, y: 0.9 },
          seats: [],
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
      stage: { x1: -0.4, y1: -1.0, x2: 0.4, y2: -0.85 },
      seat_size: 20,
      zones: [
        {
          zone_name: 'VIP', zone_type: 'standing', color: '#F59E0B',
          corner1: { x: -0.9, y: -0.8 }, corner2: { x: 0.9, y: -0.8 },
          corner3: { x: 0.9, y: -0.2 },  corner4: { x: -0.9, y: -0.2 },
          seats: [],
        },
        {
          zone_name: 'GA', zone_type: 'standing', color: '#2563EB',
          corner1: { x: -0.9, y: -0.15 }, corner2: { x: 0.9, y: -0.15 },
          corner3: { x: 0.9, y: 0.9 },    corner4: { x: -0.9, y: 0.9 },
          seats: [],
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
      stage: { x1: -0.4, y1: -1.0, x2: 0.4, y2: -0.85 },
      seat_size: 20,
      zones: [
        {
          zone_name: 'SVIP', zone_type: 'standing', color: '#DC2626',
          corner1: { x: -0.4, y: -0.8 }, corner2: { x: 0.4, y: -0.8 },
          corner3: { x: 0.4, y: -0.2 },  corner4: { x: -0.4, y: -0.2 },
          seats: [],
        },
        {
          zone_name: 'VIP Left', zone_type: 'standing', color: '#F59E0B',
          corner1: { x: -0.9, y: -0.8  }, corner2: { x: -0.45, y: -0.8 },
          corner3: { x: -0.45, y: -0.2 }, corner4: { x: -0.9, y: -0.2 },
          seats: [],
        },
        {
          zone_name: 'VIP Right', zone_type: 'standing', color: '#F59E0B',
          corner1: { x: 0.45, y: -0.8 }, corner2: { x: 0.9, y: -0.8 },
          corner3: { x: 0.9, y: -0.2 },  corner4: { x: 0.45, y: -0.2 },
          seats: [],
        },
        {
          zone_name: 'GA Left', zone_type: 'standing', color: '#2563EB',
          corner1: { x: -0.9, y: -0.15  }, corner2: { x: -0.05, y: -0.15 },
          corner3: { x: -0.05, y: 0.9 },   corner4: { x: -0.9, y: 0.9 },
          seats: [],
        },
        {
          zone_name: 'GA Early Bird', zone_type: 'standing', color: '#1D4ED8',
          corner1: { x: 0.05, y: -0.15 }, corner2: { x: 0.9, y: -0.15 },
          corner3: { x: 0.9, y: 0.9 },    corner4: { x: 0.05, y: 0.9 },
          seats: [],
        },
      ],
    },
  },
]

// ── Helper — offset from now ──────────────────────────────────

function fromNow(days: number, hours = 0): string {
  return new Date(Date.now() + days * 86_400_000 + hours * 3_600_000).toISOString()
}

// ── Events ────────────────────────────────────────────────────
// Structure mirrors DB:
//   event → sessions[] → zones[]
// categoryIds mirrors event_categories junction table
// Statuses match seed (all PREMIERE except Saigon Heat = FINISHED)

export const mockEvents: Event[] = [
  {
    id: '1', name: 'Hà Anh Tuấn: Chân Trời Rực Rỡ',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1, 3],
    addressLine: 'Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình',
    status: 'PREMIERE',
    bannerUrl: 'https://salt.tkbcdn.com/ts/ds/25/e6/b4/d79786df1e38c39beabe33c462cc381e.jpg',
    createdAt: new Date().toISOString(),
    aboutVi: '<div style="max-width:800px;margin:auto;font-family:\'Open Sans\',sans-serif;text-align:center;"><h1 style="color:#d63384;">HÀ ANH TUẤN - CHÂN TRỜI RỰC RỠ</h1><p>Đêm nhạc huyền thoại với giọng ca đầy cảm xúc của Hà Anh Tuấn.</p></div>',
    aboutEn: '<div style="max-width:800px;margin:auto;font-family:\'Open Sans\',sans-serif;text-align:center;"><h1 style="color:#d63384;">HÀ ANH TUẤN - BRILLIANT HORIZON</h1><p>A legendary music night featuring the emotional voice of Ha Anh Tuan.</p></div>',
    termsAndConditions: '<p>Cấm trẻ em dưới 6 tuổi. Không ảnh hưởng đến khán giả khác.</p>',
    policyRefund: '<p>Không hoàn tiền sau khi mua. Chỉ hỗ trợ đổi vé trong trường hợp đặc biệt.</p>',
    seatingPlanImageUrl: 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a',
    venueId: 'v8', longitude: 105.945023, latitude: 20.317891,
    sessions: [
      {
        id: 's1', eventId: '1', name: 'Show chính',
        startDate: fromNow(30), endDate: fromNow(30, 4),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z1-1', sessionId: 's1', name: 'VVIP',
            isStanding: false, capacity: 50, price: 2_500_000,
            purchaseLimit: 4, quantitySold: 18, gridRows: 5, gridCols: 10,
            giftImageUrl: 'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
            perks: ['Vòng tay check-in VIP', 'Goodie bag', 'Meet & Greet'],
            descriptionVi: 'Khu vực VIP cao cấp với vị trí đẹp nhất, dịch vụ đặc biệt và nhiều ưu đãi độc quyền.',
            descriptionEn: 'Premium VIP area with the best location, exclusive services and special privileges.',
            createdAt: new Date().toISOString(),
          },
          {
            id: 'z1-2', sessionId: 's1', name: 'GA Standing',
            isStanding: true, capacity: 800, price: 650_000,
            purchaseLimit: null, quantitySold: 294,
            perks: ['Vòng tay check-in', 'Nước suối'],
            descriptionVi: 'Khu vực đứng tự do gần sân khấu, trải nghiệm âm nhạc bùng nổ cùng đám đông.',
            descriptionEn: 'Standing area near the stage for an explosive music experience with the crowd.',
            createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '2', name: 'VBA 2025: Saigon Heat vs Hanoi Buffaloes',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
    categoryIds: [4],
    addressLine: 'CIS Arena, Quận 7, TP. Hồ Chí Minh',
    status: 'FINISHED',
    bannerUrl: 'https://cdn.nextix.cloud/nextix/81/saigonheat_1_2996fb89aa.png?updated_at=2024-06-20T06:55:12.533Z',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Chào đón người hâm mộ quay trở lại chảo lửa CIS.</p>',
    aboutEn: '<p>Welcome fans back to the CIS arena.</p>',
    termsAndConditions: '<p>Không gây rối, không ảnh hưởng đến người khác.</p>',
    policyRefund: '<p>Hoàn vé theo quy định của BTC trong trường hợp trận đấu bị hủy.</p>',
    seatingPlanImageUrl: 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg',
    venueId: 'v1', longitude: 106.719368, latitude: 10.731490,
    sessions: [
      {
        id: 's2', eventId: '2', name: 'Trận chính',
        startDate: fromNow(-2), endDate: fromNow(-2, 2),
        status: 'FINISHED', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z2-1', sessionId: 's2', name: 'Courtside VIP',
            isStanding: false, capacity: 50, price: 2_500_000,
            purchaseLimit: 2, quantitySold: 50, gridRows: 5, gridCols: 10,
            giftImageUrl: 'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
            perks: ['Áo đấu phiên bản giới hạn', 'F&B phục vụ tại chỗ'],
            descriptionVi: 'Ghế ngồi sát sàn đấu, cảm nhận từng bước chạy của cầu thủ.',
            descriptionEn: 'Courtside seats to feel every move of the players.',
            createdAt: new Date().toISOString(),
          },
          {
            id: 'z2-2', sessionId: 's2', name: 'Standard Bleachers',
            isStanding: false, capacity: 800, price: 150_000,
            purchaseLimit: null, quantitySold: 800, gridRows: 20, gridCols: 40,
            perks: [],
            descriptionVi: 'Khu vực khán đài tiêu chuẩn với tầm nhìn tốt.',
            descriptionEn: 'Standard bleachers area with good view.',
            createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '3', name: 'Những Thành Phố Mơ Màng - Summer Tour',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1],
    addressLine: 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội',
    status: 'PREMIERE',
    bannerUrl: 'https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Hành trình âm nhạc indie đầy mộng mơ với sự góp mặt của Đen Vâu, Chillies, và Vũ.</p>',
    aboutEn: '<p>A dreamy indie music journey featuring Den Vau, Chillies, and Vu.</p>',
    termsAndConditions: '<p>Vui lòng mang theo CCCD khi check-in. Trang phục thoải mái.</p>',
    policyRefund: '<p>Không hoàn trả vé sau khi mua.</p>',
    seatingPlanImageUrl: 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
    venueId: 'v9', longitude: 105.850234, latitude: 20.976543,
    sessions: [
      {
        id: 's3', eventId: '3', name: 'Đêm diễn Summer Tour',
        startDate: fromNow(45), endDate: fromNow(45, 6),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z3-1', sessionId: 's3', name: 'Cư Dân VIP',
            isStanding: false, capacity: 200, price: 1_500_000,
            purchaseLimit: null, quantitySold: 120, gridRows: 10, gridCols: 20,
            giftImageUrl: 'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
            perks: ['Bộ quà tặng Cư Dân', 'Lối đi ưu tiên', 'Nước uống miễn phí'],
            descriptionVi: 'Đặc quyền cư dân VIP với khu vực nghỉ ngơi riêng, quà tặng độc quyền.',
            descriptionEn: 'VIP resident privileges with private lounge, exclusive gifts.',
            createdAt: new Date().toISOString(),
          },
          {
            id: 'z3-2', sessionId: 's3', name: 'GA Thường',
            isStanding: true, capacity: 2000, price: 650_000,
            purchaseLimit: null, quantitySold: 660,
            perks: ['Vòng tay vải', 'Sticker'],
            descriptionVi: 'Khu vực tự do dành cho các cư dân yêu âm nhạc.',
            descriptionEn: 'General admission area for music-loving residents.',
            createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '4', name: 'Mây Lang Thang: Đêm Nhạc Trịnh',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [1],
    addressLine: 'Mây Lang Thang, Phường 4, Thành phố Đà Lạt, Tỉnh Lâm Đồng',
    status: 'PREMIERE',
    bannerUrl: 'https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Thưởng thức những giai điệu nhạc Trịnh bất hủ giữa khung cảnh đồi thông mộng mơ của Đà Lạt.</p>',
    aboutEn: '<p>Enjoy timeless Trinh melodies amidst the dreamy pine hills of Dalat.</p>',
    termsAndConditions: '<p>Nên mang theo áo khoác ấm do thời tiết Đà Lạt se lạnh.</p>',
    policyRefund: '<p>Không hoàn tiền vé.</p>',
    seatingPlanImageUrl: 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
    venueId: 'v3', longitude: 108.436867, latitude: 11.934155,
    sessions: [
      {
        id: 's4', eventId: '4', name: 'Đêm nhạc Trịnh',
        startDate: fromNow(25), endDate: fromNow(25, 3),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z4-1', sessionId: 's4', name: 'Khu VVIP (Gần ca sĩ)',
            isStanding: false, capacity: 50, price: 1_800_000,
            purchaseLimit: null, quantitySold: 22, gridRows: 5, gridCols: 10,
            perks: ['Một phần nước tự chọn', 'Đĩa CD nhạc Trịnh'],
            descriptionVi: 'Vị trí đẹp nhất để tương tác cùng nghệ sĩ.',
            descriptionEn: 'The best spot to interact with the artist.',
            createdAt: new Date().toISOString(),
          },
          {
            id: 'z4-2', sessionId: 's4', name: 'Khu Khán Đài',
            isStanding: false, capacity: 200, price: 800_000,
            purchaseLimit: null, quantitySold: 73, gridRows: 10, gridCols: 20,
            perks: ['Nước uống', 'Chăn len'],
            descriptionVi: 'Khu vực ngồi thoải mái với tầm nhìn đẹp.',
            descriptionEn: 'Comfortable seating area with beautiful view.',
            createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '5', name: 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
    categoryIds: [3],
    addressLine: 'Nhà Hát Bến Thành, Quận 1, TP. Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: 'https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Chương trình kịch thiếu nhi được mong chờ nhất hè này.</p>',
    aboutEn: '<p>The most anticipated children\'s theater show this summer.</p>',
    termsAndConditions: '<p>Mỗi vé dành cho một người, trẻ em cần có người lớn đi kèm.</p>',
    policyRefund: '<p>Không hoàn tiền.</p>',
    seatingPlanImageUrl: 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
    venueId: 'v2', longitude: 106.690864, latitude: 10.769444,
    sessions: [
      {
        id: 's5', eventId: '5', name: 'Buổi chiều Chủ Nhật',
        startDate: fromNow(5), endDate: fromNow(5, 3),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z5-1', sessionId: 's5', name: 'Khu A (Lầu 1)',
            isStanding: false, capacity: 300, price: 350_000,
            purchaseLimit: null, quantitySold: 180, gridRows: 15, gridCols: 20,
            perks: [],
            descriptionVi: 'Khu vực lầu 1 gần sân khấu nhất.',
            descriptionEn: 'Floor 1 area closest to the stage.',
            createdAt: new Date().toISOString(),
          },
          {
            id: 'z5-2', sessionId: 's5', name: 'Khu B (Lầu 2)',
            isStanding: false, capacity: 200, price: 250_000,
            purchaseLimit: null, quantitySold: 40, gridRows: 10, gridCols: 20,
            perks: [],
            descriptionVi: 'Khu vực lầu 2 với tầm nhìn toàn cảnh.',
            descriptionEn: 'Floor 2 area with panoramic view.',
            createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '6', name: 'Ravolution Music Festival: Unite',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
    categoryIds: [1],
    addressLine: 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: 'https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Lễ hội âm nhạc điện tử quốc tế lớn nhất khu vực.</p>',
    aboutEn: '<p>The biggest international EDM festival in the region.</p>',
    termsAndConditions: '<p>Trang phục tự do, không mang chất cấm. Cấm trẻ em dưới 16 tuổi.</p>',
    policyRefund: '<p>Hoàn tiền 50% trước 7 ngày diễn ra.</p>',
    seatingPlanImageUrl: 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg',
    venueId: 'v10', longitude: 106.781234, latitude: 10.853456,
    sessions: [
      {
        id: 's6', eventId: '6', name: 'Festival Day 1',
        startDate: fromNow(60), endDate: fromNow(61),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z6-1', sessionId: 's6', name: 'SVIP Deck',
            isStanding: false, capacity: 100, price: 4_500_000,
            purchaseLimit: 4, quantitySold: 38, gridRows: 5, gridCols: 20,
            giftImageUrl: 'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
            perks: ['Private Bar', 'Quà tặng từ nhà tài trợ', 'Lối đi VIP'],
            descriptionVi: 'Tận hưởng lễ hội trên khán đài cao cấp với không gian riêng tư.',
            descriptionEn: 'Enjoy the festival from premium deck with private space.',
            createdAt: new Date().toISOString(),
          },
          {
            id: 'z6-2', sessionId: 's6', name: 'GA Early Bird',
            isStanding: true, capacity: 1500, price: 850_000,
            purchaseLimit: null, quantitySold: 382,
            perks: ['Vòng tay vải'],
            descriptionVi: 'Khu vực tự do cho những người đến sớm.',
            descriptionEn: 'General admission area for early arrivals.',
            createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '7', name: 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
    categoryIds: [3],
    addressLine: 'Cà phê Tinh Tế, Quận 3, TP. Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: 'https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Đêm hài độc thoại giải tỏa căng thẳng cuối tuần.</p>',
    aboutEn: '<p>A stand-up comedy night to unwind your weekend stress.</p>',
    termsAndConditions: '<p>Vui lòng không quay phim trong buổi diễn.</p>',
    policyRefund: '<p>Không hoàn trả vé.</p>',
    seatingPlanImageUrl: 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
    venueId: 'v5', longitude: 106.686500, latitude: 10.786200,
    sessions: [
      {
        id: 's7', eventId: '7', name: 'Đêm hài độc thoại',
        startDate: fromNow(7), endDate: fromNow(7, 2),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z7-1', sessionId: 's7', name: 'Khu ngồi chính',
            isStanding: false, capacity: 200, price: 0,
            purchaseLimit: null, quantitySold: 0, gridRows: 20, gridCols: 10,
            perks: [], createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '8', name: 'Vietnam Tech Summit 2025',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
    categoryIds: [2],
    addressLine: 'GEM Center, Quận 1, TP. Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: 'https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Hội thảo công nghệ lớn nhất năm với các chuyên gia AI, Cloud, Blockchain.</p>',
    aboutEn: '<p>The biggest technology conference of the year.</p>',
    termsAndConditions: '<p>Khuyến khích mang theo laptop cá nhân.</p>',
    policyRefund: '<p>Hoàn tiền 50% nếu hủy trước 5 ngày.</p>',
    seatingPlanImageUrl: 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a',
    venueId: 'v4', longitude: 106.697150, latitude: 10.772510,
    sessions: [
      {
        id: 's8', eventId: '8', name: 'Tech Summit Day 1',
        startDate: fromNow(10), endDate: fromNow(10, 8),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z8-1', sessionId: 's8', name: 'Hội trường chính',
            isStanding: false, capacity: 500, price: 0,
            purchaseLimit: null, quantitySold: 0, gridRows: 25, gridCols: 20,
            perks: [], createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '9', name: 'Workshop: Marketing 0 Đồng cho Startup',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [2],
    addressLine: 'Dreamplex Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: 'https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Chia sẻ bí quyết tăng trưởng không cần ngân sách lớn.</p>',
    aboutEn: '<p>Sharing growth secrets without a big budget.</p>',
    termsAndConditions: '<p>Khuyến khích mang theo laptop cá nhân.</p>',
    policyRefund: '<p>Hoàn tiền 50% nếu hủy trước 3 ngày.</p>',
    seatingPlanImageUrl: 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a',
    venueId: 'v6', longitude: 106.707300, latitude: 10.799800,
    sessions: [
      {
        id: 's9', eventId: '9', name: 'Workshop Session',
        startDate: fromNow(14), endDate: fromNow(14, 4),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z9-1', sessionId: 's9', name: 'Khu học viên',
            isStanding: false, capacity: 100, price: 0,
            purchaseLimit: null, quantitySold: 0, gridRows: 10, gridCols: 10,
            perks: [], createdAt: new Date().toISOString(),
          },
        ],
      },
    ],
  },

  {
    id: '10', name: 'Van Gogh Art Lighting Experience',
    organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    categoryIds: [3],
    addressLine: 'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh',
    status: 'PREMIERE',
    bannerUrl: 'https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990',
    createdAt: new Date().toISOString(),
    aboutVi: '<p>Triển lãm nghệ thuật tương tác đa giác quan với công nghệ ánh sáng hiện đại.</p>',
    aboutEn: '<p>Multi-sensory interactive art exhibition with modern lighting technology.</p>',
    termsAndConditions: '<p>Mỗi lượt tham quan tối đa 60 phút.</p>',
    policyRefund: '<p>Không hỗ trợ hoàn tiền.</p>',
    seatingPlanImageUrl: 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg',
    venueId: 'v7', longitude: 106.761789, latitude: 10.847910,
    sessions: [
      {
        id: 's10', eventId: '10', name: 'Exhibition Period',
        startDate: fromNow(-10), endDate: fromNow(20),
        status: 'ONGOING', createdAt: new Date().toISOString(),
        zones: [
          {
            id: 'z10-1', sessionId: 's10', name: 'VIP',
            isStanding: true, capacity: 200, price: 350_000,
            purchaseLimit: null, quantitySold: 890,
            perks: ['Lối vào ưu tiên'], createdAt: new Date().toISOString(),
          },
          {
            id: 'z10-2', sessionId: 's10', name: 'GA',
            isStanding: true, capacity: 9999, price: 200_000,
            purchaseLimit: null, quantitySold: 350,
            perks: [], createdAt: new Date().toISOString(),
          },
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
    EDITING:   'organizer.events.status.editing',
    SCHEDULED: 'organizer.events.status.premier',
    PREMIERE:  'organizer.events.status.premier',
    SELLING:   'organizer.events.status.selling',
    PAUSED:    'organizer.events.status.paused',
    ONGOING:   'organizer.events.status.ongoing',
    FINISHED:  'organizer.events.status.finished',
    CANCELLED: 'organizer.events.status.cancelled',
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
  return venue.layout?.zones.map(z => z.zone_name) ?? []
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