-- data truncation
TRUNCATE TABLE public.categories CASCADE;
TRUNCATE TABLE public.venues CASCADE;

-- inserts
INSERT INTO public.categories (id, name) VALUES

--1. Table: Categories
(0,  'categories.concert'),
(1,  'categories.festival'),
(2,  'categories.theater'),
(3,  'categories.comedy'),
(4,  'categories.sports'),
(5,  'categories.conference'),
(6,  'categories.workshop'),
(7,  'categories.seminar'),
(8,  'categories.networking'),
(9,  'categories.exhibition'),
(10, 'categories.nightlife'),
(11, 'categories.wellness'),
(12, 'categories.food'),
(13, 'categories.drinks'),
(14, 'categories.dance'),
(15, 'categories.film'),
(16, 'categories.gaming'),
(17, 'categories.outdoor'),
(18, 'categories.charity'),
(19, 'categories.family'),
(20, 'categories.cultural'),
(21, 'categories.fashion'),
(22, 'categories.tech'),
(23, 'categories.art'),
(24, 'categories.literature'),
(25, 'categories.science'),
(26, 'categories.religion'),
(27, 'categories.politics'),
(28, 'categories.travel'),
(29, 'categories.virtual');


-- 2. Venues
INSERT INTO public.venues (id, name, address_line, latitude, longitude, layout, image_url, created_at, updated_at) VALUES
(uuidv7(), 'CIS Arena',                        '7, Đường số 23, Phú Mỹ Hưng, Phường Tân Mỹ, TP.Hồ Chí Minh',  10.713750, 106.728750, NULL, 'https://s.inyourpocket.com/gallery/178413.jpg', NOW(), NULL),
(uuidv7(), 'Nhà Hát Bến Thành',                '6 Đường Mạc Đĩnh Chi, Phường Sài Gòn, TP.Hồ Chí Minh',        10.784246, 106.700877, NULL, 'http://vietlandmarks.com/upload/142582138454fc4ec85a5d2.jpg', NOW(), NULL),
(uuidv7(), 'Mây in The Nest',                  '519 Thôn Măng Lin, P. Langbiang, Đà Lạt, Tỉnh Lâm Đồng',      11.978842, 108.393794, NULL, 'https://media2.gody.vn/public/images/place/may-lang-thang-da-lat/614c498b500ff-1632389515.jpeg', NOW(), NULL),
(uuidv7(), 'GEM Center',                       '8 Đường Nguyễn Bỉnh Khiêm, Phường Sài Gòn, TP.Hồ Chí Minh',   10.790146, 106.702379, NULL, 'https://images2.thanhnien.vn/528068263637045248/2023/2/22/1-gem-center-16770590914701989789155.jpg', NOW(), NULL),uuidv7(), 'Tinh Tế Cafe',                     '351/56 Đ. Lê Văn Sỹ, Phường Nhiêu Lộc, TP.Hồ Chí Minh',       10.785960, 106.676488, NULL, 'https://ik.imagekit.io/tvlk/blog/2022/10/quan-cafe-nhieu-cay-xanh-tphcm-1.jpeg?tr=q-70,c-at_max,w-500,h-300,dpr-2', NOW(), NULL),
                                                                                                                       (uuidv7(), 'Dreamplex Điện Biên Phủ',          '195 Đường Điện Biên Phủ, Phường Gia Định, TP.Hồ Chí Minh',    10.799603, 106.705507, NULL, 'https://maisonoffice.vn/wp-content/uploads/2021/09/van-phong-cho-thue-dreamplex-195-dien-bien-phu.jpg', NOW(), NULL),
                                                                                                                       (uuidv7(), 'Gigamall Thủ Đức',                 '240-242 Phạm Văn Đồng, Phường Hiệp Bình, TP.Hồ Chí Minh',     10.828149, 106.721385, NULL, 'https://bidiland.vn/dataweb/images/tin-tuc/tong-hop-nhung-sieu-thi-va-cho-khu-vuc-quan-9-quan-2-quan-thu-duc-thanh-pho-thu-duc-coop-extra-gigamall-bidiland(1).jpg', NOW(), NULL),
                                                                                                                       (uuidv7(), 'Sân Lễ Hội Đền Vua Đinh - Vua Lê', 'Xã Trường Yên, Huyện Hoa Lư, Tỉnh Ninh Bình',                 20.284638, 105.905315, NULL, 'https://mtcs.1cdn.vn/2023/02/16/le-hoi-den-hung.jpg', NOW(), NULL),
                                                                                                                       (uuidv7(), 'Công viên Yên Sở',                 'QL1A, Gamuda Central, Thủ đô Hà Nội',                         20.964741, 105.854647, NULL, 'https://gamudagardens.vn/wp-content/uploads/2016/09/ho-yen-so.jpg', NOW(), NULL),
                                                                                                                       (uuidv7(), 'Khu đô thị Vạn Phúc',              '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',                    10.846066, 106.709149, NULL, 'https://khudothivanphuc.vn/wp-content/uploads/2021/07/cong-vien-ocean-world-van-phuc.jpg', NOW(), NULL);


-- Venue layouts
-- CIS Arena  –  zones: VIP Floor | Lower Bowl Left | Lower Bowl Right | GA Pit | Lower Bowl Back | Upper Bowl
UPDATE public.venues SET layout = '{
  "floors": [
    {
      "floor_name": "Floor Level", "floor_order": 1, "global_seat_size": 10,
      "stage": { "x1": -0.22, "y1": -0.96, "x2": 0.22, "y2": -0.8 }, "stage_shapes": [],
      "zones": [
        { "zone_name": "VIP Floor",        "zone_type": "standing", "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.28,"y":-0.78}, "corner2": {"x": 0.28,"y":-0.78}, "corner3": {"x": 0.28,"y":-0.3 }, "corner4": {"x":-0.28,"y":-0.3 } },
        { "zone_name": "Lower Bowl Left",  "zone_type": "standing", "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y":-0.78}, "corner2": {"x":-0.3, "y":-0.78}, "corner3": {"x":-0.3, "y": 0.3 }, "corner4": {"x":-0.97,"y": 0.3 } },
        { "zone_name": "Lower Bowl Right", "zone_type": "standing", "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x": 0.3, "y":-0.78}, "corner2": {"x": 0.97,"y":-0.78}, "corner3": {"x": 0.97,"y": 0.3 }, "corner4": {"x": 0.3, "y": 0.3 } },
        { "zone_name": "GA Pit",           "zone_type": "standing", "color": "#22c55e", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.28,"y":-0.28}, "corner2": {"x": 0.28,"y":-0.28}, "corner3": {"x": 0.28,"y": 0.3 }, "corner4": {"x":-0.28,"y": 0.3 } },
        { "zone_name": "Lower Bowl Back",  "zone_type": "standing", "color": "#3b82f6", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y": 0.32}, "corner2": {"x": 0.97,"y": 0.32}, "corner3": {"x": 0.97,"y": 0.72}, "corner4": {"x":-0.97,"y": 0.72} }
      ]
    },
    {
      "floor_name": "Upper Bowl", "floor_order": 2, "global_seat_size": 9,
      "stage": { "x1": -0.22, "y1": -0.96, "x2": 0.22, "y2": -0.8 }, "stage_shapes": [],
      "zones": [
        { "zone_name": "Upper Bowl", "zone_type": "standing", "color": "#8b5cf6", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y":-0.6}, "corner2": {"x": 0.97,"y":-0.6}, "corner3": {"x": 0.97,"y": 0.6}, "corner4": {"x":-0.97,"y": 0.6} }
      ]
    }
  ]
}'::jsonb WHERE name = 'CIS Arena';

-- Nhà Hát Bến Thành  –  zones (F1): Orchestra | Box Left | Box Right | Mezzanine  (F2): Balcony
UPDATE public.venues SET layout = '{
  "floors": [
    {
      "floor_name": "Orchestra", "floor_order": 1, "global_seat_size": 13,
      "stage": { "x1": -0.6, "y1": -0.96, "x2": 0.6, "y2": -0.72 }, "stage_shapes": [],
      "zones": [
        { "zone_name": "Orchestra", "zone_type": "sitting", "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.7, "y":-0.7 }, "corner2": {"x": 0.7, "y":-0.7 }, "corner3": {"x": 0.7, "y": 0.3 }, "corner4": {"x":-0.7, "y": 0.3 } },
        { "zone_name": "Box Left",  "zone_type": "sitting", "color": "#ec4899", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y":-0.7 }, "corner2": {"x":-0.72,"y":-0.7 }, "corner3": {"x":-0.72,"y": 0.1 }, "corner4": {"x":-0.97,"y": 0.1 } },
        { "zone_name": "Box Right", "zone_type": "sitting", "color": "#ec4899", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x": 0.72,"y":-0.7 }, "corner2": {"x": 0.97,"y":-0.7 }, "corner3": {"x": 0.97,"y": 0.1 }, "corner4": {"x": 0.72,"y": 0.1 } },
        { "zone_name": "Mezzanine", "zone_type": "sitting", "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.7, "y": 0.32}, "corner2": {"x": 0.7, "y": 0.32}, "corner3": {"x": 0.7, "y": 0.72}, "corner4": {"x":-0.7, "y": 0.72} }
      ]
    },
    {
      "floor_name": "Balcony", "floor_order": 2, "global_seat_size": 11,
      "stage": { "x1": -0.6, "y1": -0.96, "x2": 0.6, "y2": -0.72 }, "stage_shapes": [],
      "zones": [
        { "zone_name": "Balcony", "zone_type": "sitting", "color": "#3b82f6", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.85,"y":-0.5}, "corner2": {"x": 0.85,"y":-0.5}, "corner3": {"x": 0.85,"y": 0.5}, "corner4": {"x":-0.85,"y": 0.5} }
      ]
    }
  ]
}'::jsonb WHERE name = 'Nhà Hát Bến Thành';

-- Mây in The Nest  –  zones: VIP | Zone A | Zone B | Standing
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Main Area", "floor_order": 1, "global_seat_size": 16,
    "stage": { "x1": -0.4, "y1": -0.96, "x2": 0.4, "y2": -0.74 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "VIP",      "zone_type": "sitting",  "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.4, "y":-0.72}, "corner2": {"x": 0.4, "y":-0.72}, "corner3": {"x": 0.4, "y":-0.1 }, "corner4": {"x":-0.4, "y":-0.1 } },
      { "zone_name": "Zone A",   "zone_type": "standing", "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y":-0.72}, "corner2": {"x":-0.42,"y":-0.72}, "corner3": {"x":-0.42,"y": 0.4 }, "corner4": {"x":-0.97,"y": 0.4 } },
      { "zone_name": "Zone B",   "zone_type": "standing", "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x": 0.42,"y":-0.72}, "corner2": {"x": 0.97,"y":-0.72}, "corner3": {"x": 0.97,"y": 0.4 }, "corner4": {"x": 0.42,"y": 0.4 } },
      { "zone_name": "Standing", "zone_type": "standing", "color": "#22c55e", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y": 0.42}, "corner2": {"x": 0.97,"y": 0.42}, "corner3": {"x": 0.97,"y": 0.95}, "corner4": {"x":-0.97,"y": 0.95} }
    ]
  }]
}'::jsonb WHERE name = 'Mây in The Nest';

-- GEM Center  –  zones: VIP | Zone A | Zone B | Zone C | Standing
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Main Hall", "floor_order": 1, "global_seat_size": 12,
    "stage": { "x1": -0.28, "y1": -0.96, "x2": 0.28, "y2": -0.78 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "VIP",      "zone_type": "sitting",  "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.32,"y":-0.76}, "corner2": {"x": 0.32,"y":-0.76}, "corner3": {"x": 0.32,"y":-0.38}, "corner4": {"x":-0.32,"y":-0.38} },
      { "zone_name": "Zone A",   "zone_type": "sitting",  "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.95,"y":-0.76}, "corner2": {"x":-0.34,"y":-0.76}, "corner3": {"x":-0.34,"y": 0.2 }, "corner4": {"x":-0.95,"y": 0.2 } },
      { "zone_name": "Zone B",   "zone_type": "sitting",  "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x": 0.34,"y":-0.76}, "corner2": {"x": 0.95,"y":-0.76}, "corner3": {"x": 0.95,"y": 0.2 }, "corner4": {"x": 0.34,"y": 0.2 } },
      { "zone_name": "Zone C",   "zone_type": "sitting",  "color": "#3b82f6", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.95,"y": 0.22}, "corner2": {"x": 0.95,"y": 0.22}, "corner3": {"x": 0.95,"y": 0.62}, "corner4": {"x":-0.95,"y": 0.62} },
      { "zone_name": "Standing", "zone_type": "standing", "color": "#22c55e", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.95,"y": 0.64}, "corner2": {"x": 0.95,"y": 0.64}, "corner3": {"x": 0.95,"y": 0.95}, "corner4": {"x":-0.95,"y": 0.95} }
    ]
  }]
}'::jsonb WHERE name = 'GEM Center';

-- Tinh Tế Cafe  –  zones: Seated | Standing
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Main Floor", "floor_order": 1, "global_seat_size": 18,
    "stage": { "x1": -0.5, "y1": -0.96, "x2": 0.5, "y2": -0.68 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "Seated",   "zone_type": "sitting",  "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.85,"y":-0.65}, "corner2": {"x": 0.85,"y":-0.65}, "corner3": {"x": 0.85,"y": 0.3 }, "corner4": {"x":-0.85,"y": 0.3 } },
      { "zone_name": "Standing", "zone_type": "standing", "color": "#22c55e", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.85,"y": 0.35}, "corner2": {"x": 0.85,"y": 0.35}, "corner3": {"x": 0.85,"y": 0.92}, "corner4": {"x":-0.85,"y": 0.92} }
    ]
  }]
}'::jsonb WHERE name = 'Tinh Tế Cafe';

-- Dreamplex Điện Biên Phủ  –  zones: Zone A | Zone B | Standing
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Event Space", "floor_order": 1, "global_seat_size": 14,
    "stage": { "x1": -0.35, "y1": -0.96, "x2": 0.35, "y2": -0.76 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "Zone A",   "zone_type": "sitting",  "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.85,"y":-0.74}, "corner2": {"x": 0.85,"y":-0.74}, "corner3": {"x": 0.85,"y":-0.08}, "corner4": {"x":-0.85,"y":-0.08} },
      { "zone_name": "Zone B",   "zone_type": "sitting",  "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.85,"y":-0.06}, "corner2": {"x": 0.85,"y":-0.06}, "corner3": {"x": 0.85,"y": 0.55}, "corner4": {"x":-0.85,"y": 0.55} },
      { "zone_name": "Standing", "zone_type": "standing", "color": "#22c55e", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.85,"y": 0.57}, "corner2": {"x": 0.85,"y": 0.57}, "corner3": {"x": 0.85,"y": 0.95}, "corner4": {"x":-0.85,"y": 0.95} }
    ]
  }]
}'::jsonb WHERE name = 'Dreamplex Điện Biên Phủ';

-- Gigamall Thủ Đức  –  zones: VIP | GA
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Exhibition Space", "floor_order": 1, "global_seat_size": 20,
    "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "VIP", "zone_type": "standing", "color": "#7c3aed", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.9,"y":-0.8 }, "corner2": {"x": 0.9,"y":-0.8 }, "corner3": {"x": 0.9,"y":-0.2 }, "corner4": {"x":-0.9,"y":-0.2 } },
      { "zone_name": "GA",  "zone_type": "standing", "color": "#5b21b6", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.9,"y":-0.15}, "corner2": {"x": 0.9,"y":-0.15}, "corner3": {"x": 0.9,"y": 0.9 }, "corner4": {"x":-0.9,"y": 0.9 } }
    ]
  }]
}'::jsonb WHERE name = 'Gigamall Thủ Đức';

-- Sân Lễ Hội Đền Vua Đinh - Vua Lê  –  zones: VVIP | VIP | GA Standing
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Grounds", "floor_order": 1, "global_seat_size": 14,
    "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "VVIP",       "zone_type": "standing", "color": "#dc2626", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.9,"y":-0.8 }, "corner2": {"x": 0.9,"y":-0.8 }, "corner3": {"x": 0.9,"y":-0.35}, "corner4": {"x":-0.9,"y":-0.35} },
      { "zone_name": "VIP",        "zone_type": "standing", "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.9,"y":-0.3 }, "corner2": {"x": 0.9,"y":-0.3 }, "corner3": {"x": 0.9,"y": 0.2 }, "corner4": {"x":-0.9,"y": 0.2 } },
      { "zone_name": "GA Standing","zone_type": "standing", "color": "#2563eb", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.9,"y": 0.25}, "corner2": {"x": 0.9,"y": 0.25}, "corner3": {"x": 0.9,"y": 0.9 }, "corner4": {"x":-0.9,"y": 0.9 } }
    ]
  }]
}'::jsonb WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê';

-- Công viên Yên Sở  –  zones: VIP | Zone A | Zone B | GA
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Main Stage Area", "floor_order": 1, "global_seat_size": 12,
    "stage": { "x1": -0.35, "y1": -1.0, "x2": 0.35, "y2": -0.76 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "VIP",    "zone_type": "standing", "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.4, "y":-0.74}, "corner2": {"x": 0.4, "y":-0.74}, "corner3": {"x": 0.4, "y":-0.2 }, "corner4": {"x":-0.4, "y":-0.2 } },
      { "zone_name": "Zone A", "zone_type": "standing", "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y":-0.74}, "corner2": {"x":-0.42,"y":-0.74}, "corner3": {"x":-0.42,"y": 0.35}, "corner4": {"x":-0.97,"y": 0.35} },
      { "zone_name": "Zone B", "zone_type": "standing", "color": "#6366f1", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x": 0.42,"y":-0.74}, "corner2": {"x": 0.97,"y":-0.74}, "corner3": {"x": 0.97,"y": 0.35}, "corner4": {"x": 0.42,"y": 0.35} },
      { "zone_name": "GA",     "zone_type": "standing", "color": "#22c55e", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y": 0.37}, "corner2": {"x": 0.97,"y": 0.37}, "corner3": {"x": 0.97,"y": 0.97}, "corner4": {"x":-0.97,"y": 0.97} }
    ]
  }]
}'::jsonb WHERE name = 'Công viên Yên Sở';

-- Khu đô thị Vạn Phúc  –  zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
UPDATE public.venues SET layout = '{
  "floors": [{
    "floor_name": "Main Stage", "floor_order": 1, "global_seat_size": 12,
    "stage": { "x1": -0.32, "y1": -1.0, "x2": 0.32, "y2": -0.76 }, "stage_shapes": [],
    "zones": [
      { "zone_name": "SVIP",          "zone_type": "standing", "color": "#dc2626", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.4, "y":-0.74}, "corner2": {"x": 0.4, "y":-0.74}, "corner3": {"x": 0.4, "y":-0.18}, "corner4": {"x":-0.4, "y":-0.18} },
      { "zone_name": "VIP Left",      "zone_type": "standing", "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y":-0.74}, "corner2": {"x":-0.42,"y":-0.74}, "corner3": {"x":-0.42,"y":-0.18}, "corner4": {"x":-0.97,"y":-0.18} },
      { "zone_name": "VIP Right",     "zone_type": "standing", "color": "#f59e0b", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x": 0.42,"y":-0.74}, "corner2": {"x": 0.97,"y":-0.74}, "corner3": {"x": 0.97,"y":-0.18}, "corner4": {"x": 0.42,"y":-0.18} },
      { "zone_name": "GA Left",       "zone_type": "standing", "color": "#2563eb", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x":-0.97,"y":-0.15}, "corner2": {"x":-0.05,"y":-0.15}, "corner3": {"x":-0.05,"y": 0.9 }, "corner4": {"x":-0.97,"y": 0.9 } },
      { "zone_name": "GA Early Bird", "zone_type": "standing", "color": "#1d4ed8", "rotation": 0, "accessible": true, "shape_type": "rect", "corner1": {"x": 0.05,"y":-0.15}, "corner2": {"x": 0.97,"y":-0.15}, "corner3": {"x": 0.97,"y": 0.9 }, "corner4": {"x": 0.05,"y": 0.9 } }
    ]
  }]
}'::jsonb WHERE name = 'Khu đô thị Vạn Phúc';

-- 4. Events
-- TODO: update organizer and status
INSERT INTO public.events (id, organizer_id, status, name, banner_url, address_line, created_at) VALUES
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'V-Glow: The Cyber-Heritage Night',            '{}'::jsonb, 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh',              now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'The Echo of An Nam',                          '{}'::jsonb, 'Sân Lễ Hội Đền Vua Đinh - Vua Lê, Huyện Hoa Lư, Tỉnh Ninh Bình',     now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026',    '{}'::jsonb, 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh',              now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'CỔ NGHỆ KIÊU HÙNG',                          '{}'::jsonb, 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội',                              now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'NEO-LUMINANCE: The Echo of Indochine',       '{}'::jsonb, 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh',              now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Sắc Lam: The Indigo Echo',                   '{}'::jsonb, 'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh',                 now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'CRYSTAL REALM: The Neon Garden',             '{}'::jsonb, 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội',                              now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'SẮT & SON',                                  '{}'::jsonb, 'Sân Lễ Hội Đền Vua Đinh - Vua Lê, Huyện Hoa Lư, Tỉnh Ninh Bình',     now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Techno-Sorcery: The Zenith of AI & Robotics','{}'::jsonb, 'GEM Center, Quận 1, TP. Hồ Chí Minh',                                   now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Vũ Trụ Cận Thị',                             '{}'::jsonb, 'Mây in The Nest, Phường Langbiang, Đà Lạt, Tỉnh Lâm Đồng',            now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'THE DREAMCATCHER ARCHIPELAGO',               '{}'::jsonb, 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh',              now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'KAIZEN: The Art of Precision',               '{}'::jsonb, 'Tinh Tế Cafe, Quận 3, TP. Hồ Chí Minh',                                 now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'LUVIA: The Echo of Highlands',               '{}'::jsonb, 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội',                              now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Scent of the Soul: The Echoes of Vietnam',   '{}'::jsonb, 'GEM Center, Quận 1, TP. Hồ Chí Minh',                                   now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Âm Sắc Việt - THE RESONANCE',                '{}'::jsonb, 'CIS Arena, Quận 7, TP. Hồ Chí Minh',                                    now());

-- Banner URLs
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775381970/vglow-wide_t6cooz.png","square":"https://cdn.ticket4u.uk/v1775381967/vglow-sqre_nmypgq.png","tall":"https://cdn.ticket4u.uk/v1775381974/vglow-tall_wgekmk.png"}'::jsonb WHERE name = 'V-Glow: The Cyber-Heritage Night';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775381964/aecho-wide_cdgl6u.png","square":"https://cdn.ticket4u.uk/v1775381950/aecho-sqre_efweqg.png","tall":"https://cdn.ticket4u.uk/v1775381948/aecho-tall_xkm5ic.png"}'::jsonb WHERE name = 'The Echo of An Nam';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775382276/beats-wide_kq6cve.png","square":"https://cdn.ticket4u.uk/v1775382269/beats-sqre_kf8szt.png","tall":"https://cdn.ticket4u.uk/v1775382271/beats-tall_nzkxux.png"}'::jsonb WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775382586/tloom-wide_nnsqej.png","square":"https://cdn.ticket4u.uk/v1775382590/tloom-sqre_zqnaam.png","tall":"https://cdn.ticket4u.uk/v1775382588/tloom-tall_wlhtt2.png"}'::jsonb WHERE name = 'CỔ NGHỆ KIÊU HÙNG';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775382880/neolu-wide_wiwf4z.png","square":"https://cdn.ticket4u.uk/v1775382884/neolu-sqre_y2ofzz.png","tall":"https://cdn.ticket4u.uk/v1775382886/neolu-tall_spbtwy.png"}'::jsonb WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775383130/sclam-wide_ymvfhg.png","square":"https://cdn.ticket4u.uk/v1775383104/sclam-sqre_uaxxnl.png","tall":"https://cdn.ticket4u.uk/v1775383057/sclam-tall_froaue.png"}'::jsonb WHERE name = 'Sắc Lam: The Indigo Echo';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775383196/cryst-wide_ccxmzc.png","square":"https://cdn.ticket4u.uk/v1775383192/cryst-sqre_e76ssg.png","tall":"https://cdn.ticket4u.uk/v1775383192/cryst-tall_h04ljf.png"}'::jsonb WHERE name = 'CRYSTAL REALM: The Neon Garden';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775383391/satso-wide_vlujh3.png","square":"https://cdn.ticket4u.uk/v1775383341/satso-sqre_vu6tau.png","tall":"https://cdn.ticket4u.uk/v1775383338/satso-tall_dgbelz.png"}'::jsonb WHERE name = 'SẮT & SON';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775383504/techs-wide_ekyhab.png","square":"https://cdn.ticket4u.uk/v1775383502/techs-sqre_ah00iv.png","tall":"https://cdn.ticket4u.uk/v1775383508/techs-tall_q41wkb.png"}'::jsonb WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775383818/canth-wide_m4gbop.png","square":"https://cdn.ticket4u.uk/v1775383821/canth-sqre_im0awy.png","tall":"https://cdn.ticket4u.uk/v1775383941/dream-tall_fkwtkk.png"}'::jsonb WHERE name = 'Vũ Trụ Cận Thị';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775383940/dream-wide_knggr2.png","square":"https://cdn.ticket4u.uk/v1775383942/dream-sqre_zlwxep.png","tall":"https://cdn.ticket4u.uk/v1775383941/dream-tall_fkwtkk.png"}'::jsonb WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775384284/kzart-wide_qvhiub.png","square":"https://cdn.ticket4u.uk/v1775384345/kzart-sqre_v3harc.png","tall":"https://cdn.ticket4u.uk/v1775384294/kzart-tall_lphcur.png"}'::jsonb WHERE name = 'KAIZEN: The Art of Precision';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775384450/luvia-wide_gyizyq.png","square":"https://cdn.ticket4u.uk/v1775384446/luvia-sqre_lqtogb.png","tall":"https://cdn.ticket4u.uk/v1775384451/luvia-tall_dzcyjo.png"}'::jsonb WHERE name = 'LUVIA: The Echo of Highlands';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775390582/echov-wide_paluak.png","square":"https://cdn.ticket4u.uk/v1775390593/echov-sqre_huj7ep.png","tall":"https://cdn.ticket4u.uk/v1775390592/echov-tall_tyfbot.png"}'::jsonb WHERE name = 'Scent of the Soul: The Echoes of Vietnam';
UPDATE public.events SET banner_url = '{"wide":"https://cdn.ticket4u.uk/v1775390591/reson-wide_aossid.png","square":"https://cdn.ticket4u.uk/v1775390583/reson-sqre_igosvf.png","tall":"https://cdn.ticket4u.uk/v1775390583/reson-tall_segvto.png"}'::jsonb WHERE name = 'Âm Sắc Việt - THE RESONANCE';

-- Venue links (Vũ Trụ Cận Thị and KAIZEN use custom layout – venue_id left NULL)
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc')          WHERE name = 'V-Glow: The Cyber-Heritage Night';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê') WHERE name = 'The Echo of An Nam';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc')          WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở')             WHERE name = 'CỔ NGHỆ KIÊU HÙNG';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc')          WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Gigamall Thủ Đức')             WHERE name = 'Sắc Lam: The Indigo Echo';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở')             WHERE name = 'CRYSTAL REALM: The Neon Garden';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê') WHERE name = 'SẮT & SON';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'GEM Center')                   WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc')          WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở')             WHERE name = 'LUVIA: The Echo of Highlands';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'GEM Center')                   WHERE name = 'Scent of the Soul: The Echoes of Vietnam';
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'CIS Arena')                    WHERE name = 'Âm Sắc Việt - THE RESONANCE';

-- Descriptions + terms + refund policy
UPDATE public.events SET
                         about_vi='V-Glow 2026 là lễ hội âm nhạc và công nghệ đột phá tại TP.HCM, kết hợp giữa nét đẹp Trung Thu truyền thống và phong cách Cyberpunk tương lai. Với sự góp mặt của các ngôi sao V-Pop hàng đầu, triển lãm nghệ thuật AR và diễu hành lồng đèn số, đây là điểm đến không thể bỏ qua cho cộng đồng fan Gen Z.',
                         about_en='V-Glow 2026 is a revolutionary fan-fest in Ho Chi Minh City, merging Vietnamese traditional mid-autumn heritage with futuristic Cyberpunk aesthetics. Featuring top V-Pop idols, immersive AR art, and a digital lantern parade, it''s the ultimate cultural-tech experience for the Gen Z fanbase.',
                         terms_and_conditions='<p>Cấm trẻ em dưới 16 tuổi. Không mang chất cấm, vũ khí vào khu vực. Trang phục tự do.</p>',
                         policy_refund='<p>Hoàn tiền 50% nếu hủy trước 7 ngày diễn ra sự kiện.</p>'
WHERE name='V-Glow: The Cyber-Heritage Night';

UPDATE public.events SET
                         about_vi='"Tiếng Vọng An Nam" là lễ hội đêm quy mô lớn năm 2026, kết hợp giữa di sản truyền thống và công nghệ tương lai. Với sự góp mặt của các nghệ sĩ hàng đầu như Gemini Hùng Huỳnh và nghệ thuật số đa giác quan, đây là sự kiện văn hóa - công nghệ không thể bỏ lỡ.',
                         about_en='"The Echo of An Nam" is a premier 2026 night festival blending traditional Vietnamese heritage with futuristic tech. Featuring top artists like Gemini Hung Huynh and immersive digital art, it''s the ultimate cultural-tech crossover for the modern fanbase.',
                         terms_and_conditions='<p>Vui lòng mang theo CCCD khi check-in. Không gây rối trật tự.</p>',
                         policy_refund='<p>Không hoàn tiền sau khi mua vé.</p>'
WHERE name='The Echo of An Nam';

UPDATE public.events SET
                         about_vi='Trải nghiệm lễ hội âm nhạc và công nghệ đỉnh cao tại Sài Gòn Neon Beats 2026. Một sự kiện bùng nổ kết hợp giữa âm thanh EDM sôi động, trình diễn Drone Light Show và không gian tương tác ảo (AR) tại Global City.',
                         about_en='Experience the ultimate music and technology festival at Saigon Neon Beats 2026. An explosive event blending high-energy EDM, Drone Light Shows, and AR interactive zones at Global City. Don''t miss the chance to meet top artists and immerse yourself in the digital era.',
                         terms_and_conditions='<p>Cấm trẻ em dưới 16 tuổi. Không mang chất cấm. Trang phục tự do.</p>',
                         policy_refund='<p>Hoàn tiền 50% trước 7 ngày diễn ra.</p>'
WHERE name='SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

UPDATE public.events SET
                         about_vi='Đắm mình trong sự giao thoa giữa di sản nghìn năm và tư duy sáng tạo hiện đại tại "Cổ Nghệ Kiêu Hùng." Sự kiện mang đến những trải nghiệm trình diễn nghề thủ công trực tiếp, nhạc indie-folk và sàn diễn "Cổ phục" độc bản.',
                         about_en='Experience the fusion of Vietnam''s 1,000-year-old heritage and modern creative flair at "The Antique Alchemy." A night of live traditional craftsmanship, indie-folk music, and an exclusive "Antique Runway" featuring top artisans and Gen-Z designers.',
                         terms_and_conditions='<p>Vui lòng mang theo CCCD. Trang phục cổ phục được khuyến khích.</p>',
                         policy_refund='<p>Không hoàn tiền sau khi mua vé.</p>'
WHERE name='CỔ NGHỆ KIÊU HÙNG';

UPDATE public.events SET
                         about_vi='Khám phá NEO-LUMINANCE 2026, lễ hội âm nhạc điện tử và đa dòng nhạc ngoài trời quy mô lớn nhất tại TP.HCM. Sự kiện là sự giao thoa độc đáo giữa phong cách tương lai Cyberpunk và họa tiết Đông Dương truyền thống.',
                         about_en='Experience NEO-LUMINANCE 2026, Vietnam''s premier outdoor electronic and fusion music festival in Ho Chi Minh City. This event blends futuristic cyberpunk aesthetics with traditional Indochine motifs. Join 25,000 fans for a night of high-tech light shows and explosive music.',
                         terms_and_conditions='<p>Cấm trẻ em dưới 16 tuổi. Không mang chất cấm, vũ khí vào khu vực.</p>',
                         policy_refund='<p>Hoàn tiền 50% trước 7 ngày diễn ra.</p>'
WHERE name='NEO-LUMINANCE: The Echo of Indochine';

UPDATE public.events SET
                         about_vi='Sắc Lam: The Indigo Echo là triển lãm nghệ thuật đa giác quan kết hợp giữa kỹ thuật nhuộm chàm truyền thống của Việt Nam và công nghệ trình chiếu ánh sáng hiện đại. Cùng khám phá linh hồn của vùng cao qua lăng kính đương đại ngay giữa lòng Sài Gòn.',
                         about_en='Sắc Lam: The Indigo Echo is a premier immersive art expo blending traditional Vietnamese natural dyeing techniques with cutting-edge digital projection. Experience the soul of the highlands through a modern lens in the heart of Saigon.',
                         terms_and_conditions='<p>Mỗi lượt tham quan tối đa 90 phút. Không chụp ảnh flash.</p>',
                         policy_refund='<p>Không hỗ trợ hoàn tiền.</p>'
WHERE name='Sắc Lam: The Indigo Echo';

UPDATE public.events SET
                         about_vi='Crystal Realm: Vườn Neon Huyền Ảo là lễ hội cosplay ngoài trời cao cấp tại Công viên Yên Sở, Hà Nội. Với sân khấu LED khổng lồ, các khu vực chụp ảnh chuyên nghiệp và dàn khách mời là những cosplayer quốc tế hàng đầu.',
                         about_en='Crystal Realm: The Neon Garden is a premium outdoor cosplay festival at Yen So Park, Hanoi. Featuring a massive LED-integrated stage, professional photo zones, and a guest lineup of top-tier international cosplayers blending nature with futuristic cyberpunk aesthetics.',
                         terms_and_conditions='<p>Trang phục cosplay được khuyến khích. Cấm trẻ em dưới 12 tuổi vào ban đêm.</p>',
                         policy_refund='<p>Hoàn tiền 50% trước 5 ngày diễn ra.</p>'
WHERE name='CRYSTAL REALM: The Neon Garden';

UPDATE public.events SET
                         about_vi='SẮT & SON là lễ hội âm nhạc Rock lớn nhất năm 2026 tại Ninh Bình, quy tụ những biểu tượng Rock huyền thoại và các ban nhạc Indie đầy nổi loạn. Không gian công nghiệp gai góc hòa quyện cùng nét văn hóa truyền thống.',
                         about_en='SẮT & SON is the premier Rock festival of 2026 in Ninh Binh, bringing together legendary rock icons and rebellious indie bands. Set in a gritty industrial atmosphere infused with traditional cultural motifs, the event promises high-octane performances.',
                         terms_and_conditions='<p>Cấm trẻ em dưới 16 tuổi. Không mang chất cấm. Trang phục tự do.</p>',
                         policy_refund='<p>Không hoàn tiền sau khi mua vé.</p>'
WHERE name='SẮT & SON';

UPDATE public.events SET
                         about_vi='Khám phá sự giao thoa giữa huyền bí cổ xưa và đổi mới tương lai tại Techno-Sorcery. Lễ hội duy nhất trong đêm hội tụ "phép thuật đen" của AI tiên tiến và robot hiệu suất cao, đặt trong không gian GEM Center.',
                         about_en='Experience the fusion of ancient mysticism and futuristic innovation at Techno-Sorcery. This one-night-only festival showcases the "dark magic" of advanced AI and high-performance robotics at GEM Center. Witness machines that think and steel that moves with a soul.',
                         terms_and_conditions='<p>Khuyến khích mang theo laptop. Phù hợp mọi lứa tuổi.</p>',
                         policy_refund='<p>Hoàn tiền 50% nếu hủy trước 5 ngày.</p>'
WHERE name='Techno-Sorcery: The Zenith of AI & Robotics';

UPDATE public.events SET
                         about_vi='Buổi ra mắt thân mật của "Lặng" – ban nhạc Indie-Dream Pop mới nổi. Một hành trình đa giác quan kết hợp giữa âm nhạc và nghệ thuật thị giác trừu tượng trong không gian lãng mạn của Đà Lạt.',
                         about_en='An intimate debut showcase for "Lặng," an emerging indie-dream pop band. Experience a sensory journey where music meets abstract visual art in the romantic setting of Da Lat.',
                         terms_and_conditions='<p>Sức chứa có hạn. Vui lòng đến đúng giờ. Không quay phim trong buổi diễn.</p>',
                         policy_refund='<p>Không hoàn tiền vé.</p>'
WHERE name='Vũ Trụ Cận Thị';

UPDATE public.events SET
                         about_vi='Đắm chìm trong Đảo Quốc Mộng Mơ, lễ hội âm nhạc và nghệ thuật kỳ ảo tại Khu đô thị Vạn Phúc, TP.HCM. Trải nghiệm không gian thần tiên với sen neon khổng lồ, sân khấu mây bồng bềnh và những nghệ sĩ hàng đầu.',
                         about_en='Step into The Dreamcatcher Archipelago, an immersive whimsical festival at Van Phuc Urban Area, HCMC. Experience magic reimagined with Vietnamese soul, featuring floating neon lotuses, cloud-shrouded stages, and top-tier musical acts.',
                         terms_and_conditions='<p>Phù hợp mọi lứa tuổi. Trẻ em dưới 12 tuổi cần có người lớn đi kèm.</p>',
                         policy_refund='<p>Hoàn tiền 50% trước 7 ngày diễn ra.</p>'
WHERE name='THE DREAMCATCHER ARCHIPELAGO';

UPDATE public.events SET
                         about_vi='Trải nghiệm Omakase cao cấp độc bản, kết hợp giữa kỹ nghệ truyền thống Nhật Bản và tinh hoa nguyên liệu mùa hè Việt Nam. Sự kiện giới hạn chỉ 24 khách cho một hành trình ẩm thực đầy cảm xúc.',
                         about_en='An ultra-exclusive, high-end Omakase experience merging traditional Japanese craftsmanship with local Vietnamese seasonal treasures. Limited to 24 seats for an intimate journey of culinary storytelling.',
                         terms_and_conditions='<p>Sự kiện giới hạn 24 khách. Dress code: smart casual. Đúng giờ tuyệt đối.</p>',
                         policy_refund='<p>Không hoàn tiền sau khi xác nhận đặt chỗ.</p>'
WHERE name='KAIZEN: The Art of Precision';

UPDATE public.events SET
                         about_vi='LUVIA: The Echo of Highlands là một trải nghiệm âm thanh đa giác quan độc bản, kết hợp giữa âm nhạc ambient, tiếng vang của đại ngàn và nghệ thuật ánh sáng tại Công viên Yên Sở. Âm thanh 360 độ mang lại cảm giác thư thái và kết nối tâm hồn tuyệt đối.',
                         about_en='LUVIA: The Echo of Highlands is a unique multi-sensory auditory experience blending ambient music, forest echoes, and light art at Yen So Park. 360-degree sound technology offers ultimate relaxation and spiritual reconnection.',
                         terms_and_conditions='<p>Trang phục thoải mái. Khuyến khích mang theo áo khoác.</p>',
                         policy_refund='<p>Không hoàn tiền sau khi mua vé.</p>'
WHERE name='LUVIA: The Echo of Highlands';

UPDATE public.events SET
                         about_vi='Trải nghiệm "Hương Sắc Linh Hồn," buổi hòa nhạc 6D đa giác quan đầu tiên tại Việt Nam. Sự kết hợp giữa âm thanh vòm sống động và công nghệ kích hoạt mùi hương đồng bộ sẽ đưa người hâm mộ hành trình qua những vùng ký ức đặc trưng của Việt Nam.',
                         about_en='Experience "Scent of the Soul," Vietnam''s first 6D olfactory-immersive indoor concert. Melding high-fidelity spatial audio with synchronized scent-triggering technology, this event transports fans through the aromatic landscapes of Vietnam.',
                         terms_and_conditions='<p>Phù hợp mọi lứa tuổi. Không phù hợp với người dị ứng mùi hương mạnh.</p>',
                         policy_refund='<p>Hoàn tiền 50% nếu hủy trước 5 ngày.</p>'
WHERE name='Scent of the Soul: The Echoes of Vietnam';

UPDATE public.events SET
                         about_vi='Khám phá sức mạnh của tiếng Việt thông qua âm nhạc tại Âm Sắc Việt - THE RESONANCE. Một đêm nhạc tương tác đa giác quan, nơi ranh giới giữa nghệ sĩ và khán giả bị xóa nhòa bởi những thử thách ngôn ngữ và ứng biến giai điệu.',
                         about_en='Experience the power of the Vietnamese language through music at Âm Sắc Việt - THE RESONANCE. An interactive, multi-sensory concert where the line between performer and audience fades through linguistic challenges, melodic improvisation, and cutting-edge light shows.',
                         terms_and_conditions='<p>Vui lòng mang theo CCCD khi check-in. Không ảnh hưởng đến khán giả xung quanh.</p>',
                         policy_refund='<p>Không hoàn tiền sau khi mua vé.</p>'
WHERE name='Âm Sắc Việt - THE RESONANCE';


-- 5. event_categories
INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  1 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night' UNION ALL
SELECT id,  0 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night' UNION ALL
SELECT id, 20 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night' UNION ALL
SELECT id, 22 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  1 FROM public.events WHERE name = 'The Echo of An Nam' UNION ALL
SELECT id, 20 FROM public.events WHERE name = 'The Echo of An Nam' UNION ALL
SELECT id, 23 FROM public.events WHERE name = 'The Echo of An Nam' UNION ALL
SELECT id, 10 FROM public.events WHERE name = 'The Echo of An Nam';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  1 FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026' UNION ALL
SELECT id, 22 FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026' UNION ALL
SELECT id, 10 FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 20 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG' UNION ALL
SELECT id, 23 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG' UNION ALL
SELECT id, 21 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG' UNION ALL
SELECT id, 24 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  1 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine' UNION ALL
SELECT id, 10 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine' UNION ALL
SELECT id, 22 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine' UNION ALL
SELECT id, 20 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 23 FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo' UNION ALL
SELECT id,  9 FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo' UNION ALL
SELECT id, 20 FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  1 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden' UNION ALL
SELECT id, 16 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden' UNION ALL
SELECT id, 21 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden' UNION ALL
SELECT id, 17 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  0 FROM public.events WHERE name = 'SẮT & SON' UNION ALL
SELECT id,  1 FROM public.events WHERE name = 'SẮT & SON' UNION ALL
SELECT id, 10 FROM public.events WHERE name = 'SẮT & SON';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 22 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics' UNION ALL
SELECT id,  9 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics' UNION ALL
SELECT id,  5 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics' UNION ALL
SELECT id, 25 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  0 FROM public.events WHERE name = 'Vũ Trụ Cận Thị' UNION ALL
SELECT id,  1 FROM public.events WHERE name = 'Vũ Trụ Cận Thị' UNION ALL
SELECT id, 23 FROM public.events WHERE name = 'Vũ Trụ Cận Thị';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  1 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO' UNION ALL
SELECT id, 23 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO' UNION ALL
SELECT id, 10 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO' UNION ALL
SELECT id, 19 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 12 FROM public.events WHERE name = 'KAIZEN: The Art of Precision' UNION ALL
SELECT id, 13 FROM public.events WHERE name = 'KAIZEN: The Art of Precision' UNION ALL
SELECT id,  9 FROM public.events WHERE name = 'KAIZEN: The Art of Precision';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  0 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands' UNION ALL
SELECT id, 23 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands' UNION ALL
SELECT id, 17 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands' UNION ALL
SELECT id, 11 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  0 FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam' UNION ALL
SELECT id, 23 FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam' UNION ALL
SELECT id,  9 FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id,  0 FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE' UNION ALL
SELECT id,  1 FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE' UNION ALL
SELECT id, 10 FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE';

SELECT setval(pg_get_serial_sequence('public.categories', 'id'), 29);


-- 4. Event Sessions
-- V-Glow: The Cyber-Heritage Night (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '30 days', NOW() + INTERVAL '30 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '31 days', NOW() + INTERVAL '31 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night';

-- The Echo of An Nam (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '45 days', NOW() + INTERVAL '45 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'The Echo of An Nam';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '46 days', NOW() + INTERVAL '46 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'The Echo of An Nam';

-- SÀI GÒN NEON BEATS: THE CYBER-FEST 2026 (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '60 days', NOW() + INTERVAL '60 days 8 hours', 'SCHEDULED', 'Ngày 1', NOW()
FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '61 days', NOW() + INTERVAL '61 days 8 hours', 'SCHEDULED', 'Ngày 2', NOW()
FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

-- CỔ NGHỆ KIÊU HÙNG (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '50 days', NOW() + INTERVAL '50 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '51 days', NOW() + INTERVAL '51 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG';

-- NEO-LUMINANCE: The Echo of Indochine (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '75 days', NOW() + INTERVAL '75 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '76 days', NOW() + INTERVAL '76 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';

-- Sắc Lam: The Indigo Echo (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '35 days', NOW() + INTERVAL '35 days 5 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '36 days', NOW() + INTERVAL '36 days 5 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo';

-- CRYSTAL REALM: The Neon Garden (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '55 days', NOW() + INTERVAL '55 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '56 days', NOW() + INTERVAL '56 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden';

-- SẮT & SON (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '40 days', NOW() + INTERVAL '40 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'SẮT & SON';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '41 days', NOW() + INTERVAL '41 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'SẮT & SON';

-- Techno-Sorcery: The Zenith of AI & Robotics (1 session)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '25 days', NOW() + INTERVAL '25 days 8 hours', 'SCHEDULED', 'Hội thảo chính', NOW()
FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics';

-- Vũ Trụ Cận Thị (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '38 days', NOW() + INTERVAL '38 days 5 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'Vũ Trụ Cận Thị';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '39 days', NOW() + INTERVAL '39 days 5 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'Vũ Trụ Cận Thị';

-- THE DREAMCATCHER ARCHIPELAGO (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '65 days', NOW() + INTERVAL '65 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '66 days', NOW() + INTERVAL '66 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';

-- KAIZEN: The Art of Precision (1 session – intimate)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '28 days', NOW() + INTERVAL '28 days 3 hours', 'SCHEDULED', 'Buổi chiều', NOW()
FROM public.events WHERE name = 'KAIZEN: The Art of Precision';

-- LUVIA: The Echo of Highlands (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '70 days', NOW() + INTERVAL '70 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'LUVIA: The Echo of Highlands';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '71 days', NOW() + INTERVAL '71 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'LUVIA: The Echo of Highlands';

-- Scent of the Soul: The Echoes of Vietnam (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '48 days', NOW() + INTERVAL '48 days 5 hours', 'SCHEDULED', 'Ngày 1', NOW()
FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '49 days', NOW() + INTERVAL '49 days 5 hours', 'SCHEDULED', 'Ngày 2', NOW()
FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam';

-- Âm Sắc Việt - THE RESONANCE (2 sessions)
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '20 days', NOW() + INTERVAL '20 days 6 hours', 'SCHEDULED', 'Đêm 1', NOW()
FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE';

INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '21 days', NOW() + INTERVAL '21 days 6 hours', 'SCHEDULED', 'Đêm 2', NOW()
FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE';


-- 5. Zones
-- =============================================================================
-- ZONES  –  all 15 events
-- Pattern: one INSERT per zone per event (applies to ALL sessions via JOIN)
-- Venue zone → event zone mapping mirrors old data's zoneLinks logic
-- =============================================================================

-- =============================================================================
-- V-Glow: The Cyber-Heritage Night  (Khu đô thị Vạn Phúc)
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'SVIP Lounge', false, 80, 2, 3500000,
       'Khu vực SVIP cao cấp với không gian riêng tư, open bar và gói quà tặng độc quyền. Trải nghiệm V-Glow từ góc nhìn đẹp nhất.',
       'Exclusive SVIP lounge with private space, open bar and premium gift package. Experience V-Glow from the best vantage point.',
       NULL, '["Open bar","Goodie bag","Lối vào ưu tiên"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'V-Glow: The Cyber-Heritage Night';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP', true, 300, 4, 1500000,
       'Khu vực VIP đứng gần sân khấu với tầm nhìn tuyệt vời và vòng tay check-in đặc biệt.',
       'VIP standing area near the stage with great views and special check-in wristband.',
       NULL, '["Vòng tay VIP","Poster sự kiện"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'V-Glow: The Cyber-Heritage Night';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Early Bird', true, 600, 4, 750000,
       'Khu GA Early Bird với giá ưu đãi cho người đặt sớm. Tham gia không khí lễ hội sôi động.',
       'GA Early Bird with special early-booking price. Join the vibrant festival atmosphere.',
       NULL, '["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'V-Glow: The Cyber-Heritage Night';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 800, 4, 550000,
       'Khu GA tự do hòa mình vào không khí lễ hội cùng hàng nghìn khán giả.',
       'General admission to immerse in the festival atmosphere with thousands of fans.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'V-Glow: The Cyber-Heritage Night';

-- =============================================================================
-- The Echo of An Nam  (Sân Lễ Hội Đền Vua Đinh - Vua Lê)
-- Venue zones: VVIP | VIP | GA Standing
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VVIP', true, 100, 2, 3000000,
       'Khu VVIP đứng sát sân khấu, trải nghiệm âm nhạc và ánh sáng tối đỉnh. Bao gồm quà tặng và nước uống VIP.',
       'VVIP standing directly at the stage for the ultimate music and light experience. Includes gifts and VIP beverages.',
       NULL, '["Vòng tay VVIP","Goodie bag","Nước uống VIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'The Echo of An Nam';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP', true, 400, 4, 1200000,
       'Khu VIP đứng tự do với tầm nhìn tốt và không gian rộng rãi hơn GA.',
       'VIP free standing with good views and more spacious than GA.',
       NULL, '["Vòng tay VIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'The Echo of An Nam';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Standing', true, 1500, 4, 500000,
       'Khu GA đứng tự do, hòa mình vào không khí lễ hội sôi động.',
       'General standing area, immerse in the vibrant festival atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'The Echo of An Nam';

-- =============================================================================
-- SÀI GÒN NEON BEATS: THE CYBER-FEST 2026  (Khu đô thị Vạn Phúc)
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'SVIP', true, 100, 2, 4000000,
       'Khu SVIP cao cấp ngay trước sân khấu với open bar, khu nghỉ riêng và quà tặng premium.',
       'Premium SVIP area directly in front of the stage with open bar, private lounge and premium gifts.',
       NULL, '["Open bar","Private lounge","Premium goodie bag"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Left', true, 350, 4, 1800000,
       'Khu VIP trái sân khấu, góc nhìn tuyệt vời và không khí EDM bùng nổ.',
       'VIP left of stage with great view and explosive EDM atmosphere.',
       NULL, '["Vòng tay VIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Right', true, 350, 4, 1800000,
       'Khu VIP phải sân khấu, góc nhìn tuyệt vời và không khí EDM bùng nổ.',
       'VIP right of stage with great view and explosive EDM atmosphere.',
       NULL, '["Vòng tay VIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 800, 4, 700000,
       'Khu GA tự do hòa mình vào không khí Cyber-Fest bùng nổ.',
       'General admission to immerse in the explosive Cyber-Fest atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Early Bird', true, 600, 4, 550000,
       'Khu GA Early Bird với giá ưu đãi cho người đặt sớm.',
       'GA Early Bird at special price for early bookers.',
       NULL, '["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

-- =============================================================================
-- CỔ NGHỆ KIÊU HÙNG  (Công viên Yên Sở)
-- Venue zones: VIP | Zone A | Zone B | GA
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP', true, 200, 2, 1500000,
       'Khu VIP ngay trước sân khấu với tầm nhìn tuyệt vời, gói quà tặng độc quyền và nước uống miễn phí.',
       'VIP area directly in front of stage with great views, exclusive gifts and complimentary beverages.',
       NULL, '["Bộ quà tặng cổ phục","Nước uống miễn phí","Lối vào ưu tiên"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone A', true, 500, 4, 700000,
       'Khu Zone A bên trái sân khấu, góc nhìn tốt và không khí sôi động.',
       'Zone A left of stage with good views and vibrant atmosphere.',
       NULL, '["Vòng tay vải","Sticker cổ phục"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone B', true, 500, 4, 700000,
       'Khu Zone B bên phải sân khấu, góc nhìn tốt và không khí sôi động.',
       'Zone B right of stage with good views and vibrant atmosphere.',
       NULL, '["Vòng tay vải","Sticker cổ phục"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 1000, 4, 400000,
       'Khu GA tự do phía sau, tham gia không khí lễ hội văn hóa.',
       'General admission rear section, join the cultural festival atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG';

-- =============================================================================
-- NEO-LUMINANCE: The Echo of Indochine  (Khu đô thị Vạn Phúc)
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'SVIP Deck', false, 120, 2, 5000000,
       'Khán đài SVIP cao cấp với không gian riêng tư, private bar và tầm nhìn bao quát toàn bộ lễ hội.',
       'Premium SVIP deck with private space, private bar and panoramic view of the entire festival.',
       NULL, '["Private bar","Goodie bag cao cấp","Lối đi VIP riêng"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Left', true, 400, 4, 2000000,
       'Khu VIP trái sân khấu, tầm nhìn tuyệt vời và không khí bùng nổ.',
       'VIP left of stage with amazing views and explosive atmosphere.',
       NULL, '["Vòng tay VIP","Poster"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Right', true, 400, 4, 2000000,
       'Khu VIP phải sân khấu, tầm nhìn tuyệt vời và không khí bùng nổ.',
       'VIP right of stage with amazing views and explosive atmosphere.',
       NULL, '["Vòng tay VIP","Poster"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 800, 4, 850000,
       'Khu GA tự do hòa mình vào không khí EDM và ánh sáng Cyberpunk.',
       'General admission to immerse in the EDM and Cyberpunk light atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Early Bird', true, 600, 4, 650000,
       'GA Early Bird ưu đãi cho người đặt sớm.',
       'GA Early Bird at special price for early bookers.',
       NULL, '["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine';

-- =============================================================================
-- Sắc Lam: The Indigo Echo  (Gigamall Thủ Đức)
-- Venue zones: VIP | GA
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP (Kèm quà tặng)', true, 150, 2, 800000,
       'Vé VIP bao gồm quà tặng nghệ thuật độc quyền: túi vải nhuộm chàm thủ công và sách ảnh giới hạn.',
       'VIP ticket includes exclusive art gifts: handmade indigo-dyed tote bag and limited photo book.',
       NULL, '["Túi vải nhuộm chàm","Sách ảnh giới hạn","Lối vào sớm"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Sắc Lam: The Indigo Echo';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Standard', true, 500, 4, 350000,
       'Vé tham quan tiêu chuẩn, khám phá toàn bộ không gian triển lãm.',
       'Standard admission ticket, explore the entire exhibition space.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Sắc Lam: The Indigo Echo';

-- =============================================================================
-- CRYSTAL REALM: The Neon Garden  (Công viên Yên Sở)
-- Venue zones: VIP | Zone A | Zone B | GA
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Crystal', true, 150, 2, 1800000,
       'Khu VIP Crystal cao cấp với khu vực chụp ảnh riêng, nước uống và cơ hội gặp gỡ cosplayer khách mời.',
       'Premium VIP Crystal area with private photo zone, beverages and meet-and-greet with guest cosplayers.',
       NULL, '["Khu ảnh riêng","Meet & Greet","Nước uống"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CRYSTAL REALM: The Neon Garden';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone A', true, 500, 4, 800000,
       'Khu Zone A bên trái sân khấu với góc nhìn tốt.',
       'Zone A left of stage with good views.',
       NULL, '["Vòng tay vải","Sticker"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CRYSTAL REALM: The Neon Garden';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone B', true, 500, 4, 800000,
       'Khu Zone B bên phải sân khấu với góc nhìn tốt.',
       'Zone B right of stage with good views.',
       NULL, '["Vòng tay vải","Sticker"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CRYSTAL REALM: The Neon Garden';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 1000, 4, 450000,
       'Khu GA tự do tham gia lễ hội cosplay.',
       'General admission to join the cosplay festival.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'CRYSTAL REALM: The Neon Garden';

-- =============================================================================
-- SẮT & SON  (Sân Lễ Hội Đền Vua Đinh - Vua Lê)
-- Venue zones: VVIP | VIP | GA Standing
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VVIP Pit', true, 100, 2, 3000000,
       'Khu VVIP Pit ngay sát sân khấu, cảm nhận năng lượng Rock mạnh nhất. Bao gồm áo phông sự kiện và meet & greet.',
       'VVIP Pit directly at the stage for the most intense Rock energy. Includes event t-shirt and meet & greet.',
       NULL, '["Áo phông sự kiện","Meet & Greet","Vòng tay VVIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SẮT & SON';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP', true, 400, 4, 1200000,
       'Khu VIP đứng tự do với tầm nhìn tốt và vòng tay đặc biệt.',
       'VIP free standing with good views and special wristband.',
       NULL, '["Vòng tay VIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SẮT & SON';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 2000, 4, 500000,
       'Khu GA tự do hòa mình vào không khí Rock bùng cháy.',
       'General admission to immerse in the blazing Rock atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'SẮT & SON';

-- =============================================================================
-- Techno-Sorcery: The Zenith of AI & Robotics  (GEM Center)
-- Venue zones: VIP | Zone A | Zone B | Zone C | Standing
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Scholars', false, 80, 2, 2500000,
       'Khu VIP dành cho các học giả và chuyên gia, hàng ghế đầu với tầm nhìn tốt nhất và túi quà tặng cao cấp.',
       'VIP section for scholars and experts, front row seats with the best view and premium gift bag.',
       NULL, '["Túi quà tặng cao cấp","Lối vào sớm","Chương trình in kỷ niệm"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone A', false, 120, 4, 900000,
       'Khu Zone A bên trái hội trường với tầm nhìn tốt.',
       'Zone A left side of the hall with good views.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone B', false, 120, 4, 900000,
       'Khu Zone B bên phải hội trường với tầm nhìn tốt.',
       'Zone B right side of the hall with good views.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone C', false, 150, 4, 650000,
       'Khu Zone C phía sau hội trường, tầm nhìn toàn cảnh.',
       'Zone C rear of the hall with panoramic view.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Standing', true, 100, 4, 400000,
       'Khu đứng phía sau hội trường cho những ai muốn không gian tự do hơn.',
       'Standing area at the rear for those who prefer more freedom of movement.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

-- =============================================================================
-- Vũ Trụ Cận Thị  (CUSTOM LAYOUT – no venue, Mây in The Nest address only)
-- No venue zone mapping needed – zones go directly into custom layout
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VVIP Intimate', false, 20, 2, 1500000,
       'Hàng ghế đầu thân mật ngay cạnh nghệ sĩ. Bao gồm đĩa vinyl giới hạn và cơ hội chụp ảnh sau show.',
       'Intimate front row seats next to the artist. Includes limited vinyl record and post-show photo opportunity.',
       NULL, '["Đĩa vinyl giới hạn","Chụp ảnh sau show","Nước uống"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Vũ Trụ Cận Thị';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Seated Standard', false, 80, 4, 600000,
       'Ghế ngồi tiêu chuẩn thưởng thức âm nhạc indie trong không gian ấm cúng của Đà Lạt.',
       'Standard seated area to enjoy indie music in a cozy Da Lat atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Vũ Trụ Cận Thị';

-- =============================================================================
-- THE DREAMCATCHER ARCHIPELAGO  (Khu đô thị Vạn Phúc)
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'SVIP Dream', false, 100, 2, 4000000,
       'Khu SVIP Dream với không gian VIP riêng tư, open bar và gói quà tặng thần tiên độc quyền.',
       'SVIP Dream area with private VIP space, open bar and exclusive dreamy gift package.',
       NULL, '["Open bar","Dreamy goodie bag","Lối vào ưu tiên","Meet & Greet"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP', true, 500, 4, 1500000,
       'Khu VIP đứng với tầm nhìn tốt và vòng tay phát sáng đặc biệt.',
       'VIP standing with great views and special glow wristband.',
       NULL, '["Vòng tay phát sáng","Poster sự kiện"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Early Bird', true, 600, 4, 700000,
       'GA Early Bird với giá ưu đãi và vòng tay vải đặc biệt.',
       'GA Early Bird at special price with exclusive fabric wristband.',
       NULL, '["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 1000, 4, 550000,
       'Khu GA tự do hòa mình vào thế giới mộng mơ của Dreamcatcher.',
       'General admission to immerse in the dreamy world of Dreamcatcher.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO';

-- =============================================================================
-- KAIZEN: The Art of Precision  (CUSTOM LAYOUT – Tinh Tế Cafe)
-- Intimate omakase: 2 zones embedded directly into custom layout
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Chef''s Table', false, 8, 1, 3500000,
       'Bàn Chef''s Table ngay trước quầy bếp mở, tương tác trực tiếp với Bếp trưởng trong suốt bữa ăn.',
       'Chef''s Table directly in front of the open kitchen, interact directly with the Head Chef throughout the meal.',
       NULL, '["Tương tác trực tiếp với bếp trưởng","Rượu vang đặc biệt","Thực đơn in kỷ niệm có chữ ký"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'KAIZEN: The Art of Precision';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Dining Room', false, 16, 2, 2500000,
       'Bàn ăn trong phòng ấm cúng với trải nghiệm Omakase đầy đủ, thực đơn 12 món theo mùa.',
       'Dining room seating with full Omakase experience, 12-course seasonal menu.',
       NULL, '["Thực đơn 12 món","Rượu sake chọn lọc","Thực đơn in kỷ niệm"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'KAIZEN: The Art of Precision';

-- =============================================================================
-- LUVIA: The Echo of Highlands  (Công viên Yên Sở)
-- Venue zones: VIP | Zone A | Zone B | GA
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Hammock', false, 80, 2, 1200000,
       'Khu VIP với võng và ghế nằm thư giãn, trải nghiệm âm nhạc ambient trong không gian thiên nhiên tuyệt vời nhất.',
       'VIP hammock and lounge chair area, experience ambient music in the most beautiful natural setting.',
       NULL, '["Võng/ghế nằm riêng","Nước uống tự nhiên","Chăn len"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'LUVIA: The Echo of Highlands';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone A', true, 300, 4, 600000,
       'Khu Zone A bên trái, nằm nghe nhạc ambient giữa thiên nhiên.',
       'Zone A left side, lie back and enjoy ambient music surrounded by nature.',
       NULL, '["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'LUVIA: The Echo of Highlands';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone B', true, 300, 4, 600000,
       'Khu Zone B bên phải, nằm nghe nhạc ambient giữa thiên nhiên.',
       'Zone B right side, lie back and enjoy ambient music surrounded by nature.',
       NULL, '["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'LUVIA: The Echo of Highlands';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA', true, 600, 4, 350000,
       'Khu GA mở rộng phía sau để thưởng thức âm nhạc và thiên nhiên.',
       'Extended GA rear section to enjoy music and nature.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'LUVIA: The Echo of Highlands';

-- =============================================================================
-- Scent of the Soul: The Echoes of Vietnam  (GEM Center)
-- Venue zones: VIP | Zone A | Zone B | Zone C | Standing
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Sensory', false, 80, 2, 2800000,
       'Ghế VIP hàng đầu với bộ khuếch tán hương thơm cá nhân, tai nghe 3D và trải nghiệm 6D đầy đủ nhất.',
       'Front VIP seats with personal scent diffuser, 3D headphones and the fullest 6D experience.',
       NULL, '["Bộ khuếch tán hương thơm","Tai nghe 3D","Goodie bag"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone A', false, 120, 4, 1200000,
       'Khu Zone A bên trái với trải nghiệm âm thanh vòm và hương thơm đồng bộ.',
       'Zone A left side with spatial audio and synchronized scent experience.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone B', false, 120, 4, 1200000,
       'Khu Zone B bên phải với trải nghiệm âm thanh vòm và hương thơm đồng bộ.',
       'Zone B right side with spatial audio and synchronized scent experience.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Zone C', false, 150, 4, 900000,
       'Khu Zone C phía sau với tầm nhìn toàn cảnh và trải nghiệm hương thơm.',
       'Zone C rear with panoramic view and scent experience.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Standing', true, 80, 4, 600000,
       'Khu đứng phía sau cho những ai muốn tự do di chuyển.',
       'Standing area at the rear for those who prefer freedom of movement.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam';

-- =============================================================================
-- Âm Sắc Việt - THE RESONANCE  (CIS Arena)
-- Venue zones (floor 1): VIP Floor | Lower Bowl Left | Lower Bowl Right | GA Pit | Lower Bowl Back
-- Venue zones (floor 2): Upper Bowl
-- =============================================================================
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VIP Floor', false, 150, 2, 2500000,
       'Khu VIP Floor sát sân khấu, trải nghiệm âm nhạc tương tác gần nhất với nghệ sĩ và ghế ngồi cao cấp.',
       'VIP Floor closest to stage for the most interactive music experience with artists, premium seating.',
       NULL, '["Ghế ngồi cao cấp","Vòng tay VIP","Goodie bag"]'::jsonb, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Âm Sắc Việt - THE RESONANCE';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Lower Bowl Left', false, 300, 4, 900000,
       'Khán đài trái phía dưới với tầm nhìn tốt và không khí sôi động.',
       'Lower left bleachers with good views and vibrant atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Âm Sắc Việt - THE RESONANCE';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Lower Bowl Right', false, 300, 4, 900000,
       'Khán đài phải phía dưới với tầm nhìn tốt và không khí sôi động.',
       'Lower right bleachers with good views and vibrant atmosphere.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Âm Sắc Việt - THE RESONANCE';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Pit', false, 300, 4, 700000,
       'Khu GA Pit giữa sân với tầm nhìn tốt.',
       'GA Pit center court with good views.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Âm Sắc Việt - THE RESONANCE';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Lower Bowl Back', false, 400, 4, 550000,
       'Khán đài phía sau dưới, tầm nhìn toàn cảnh sân khấu.',
       'Lower back bleachers with panoramic stage view.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Âm Sắc Việt - THE RESONANCE';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Upper Bowl', false, 500, 4, 350000,
       'Khán đài tầng trên với tầm nhìn toàn cảnh và giá cả phải chăng.',
       'Upper bowl with panoramic view at an affordable price.',
       NULL, NULL, NOW(), NULL
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Âm Sắc Việt - THE RESONANCE';
-- 6.Seats
-- V-Glow: SVIP  (8 rows × 10 cols = 80)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,8)  r
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'SVIP Lounge'
  AND e.name = 'V-Glow: The Cyber-Heritage Night';

-- NEO-LUMINANCE: SVIP  (10 rows × 12 cols = 120)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,10) r
         CROSS JOIN generate_series(1,12) c
WHERE z.name = 'SVIP Deck'
  AND e.name = 'NEO-LUMINANCE: The Echo of Indochine';

-- Techno-Sorcery: VIP  (8 rows × 10 cols = 80)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,8)  r
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'VIP Scholars'
  AND e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

-- Techno-Sorcery: Zone A  (6 rows × 20 cols = 120)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,6)  r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Zone A'
  AND e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

-- Techno-Sorcery: Zone B  (6 rows × 20 cols = 120)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,6)  r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Zone B'
  AND e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

-- Techno-Sorcery: Zone C  (6 rows × 25 cols = 150)
-- Row A gets price_override (premium front row), rows B-F standard
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code, price_override)
SELECT uuidv7(), z.id, 'Ghế A' || c, 'A', c::text, 'A' || c, 1200000
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,25) c
WHERE z.name = 'Zone C'
  AND e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(2,6)  r
         CROSS JOIN generate_series(1,25) c
WHERE z.name = 'Zone C'
  AND e.name = 'Techno-Sorcery: The Zenith of AI & Robotics';

-- Vũ Trụ Cận Thị: VVIP Intimate  (4 rows × 5 cols = 20)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,4) r
         CROSS JOIN generate_series(1,5) c
WHERE z.name = 'VVIP Intimate'
  AND e.name = 'Vũ Trụ Cận Thị';

-- Vũ Trụ Cận Thị: Seated Standard  (8 rows × 10 cols = 80)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,8)  r
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'Seated Standard'
  AND e.name = 'Vũ Trụ Cận Thị';

-- THE DREAMCATCHER ARCHIPELAGO: SVIP  (10 rows × 10 cols = 100)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,10) r
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'SVIP Dream'
  AND e.name = 'THE DREAMCATCHER ARCHIPELAGO';

-- KAIZEN: Chef's Table  (2 rows × 4 cols = 8)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,2) r
         CROSS JOIN generate_series(1,4) c
WHERE z.name = 'Chef''s Table'
  AND e.name = 'KAIZEN: The Art of Precision';

-- KAIZEN: Dining Room  (4 rows × 4 cols = 16)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,4) r
         CROSS JOIN generate_series(1,4) c
WHERE z.name = 'Dining Room'
  AND e.name = 'KAIZEN: The Art of Precision';

-- LUVIA: VIP  (8 rows × 10 cols = 80)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,8)  r
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'VIP Hammock'
  AND e.name = 'LUVIA: The Echo of Highlands';

-- Scent of the Soul: VIP  (8 rows × 10 cols = 80)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,8)  r
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'VIP Sensory'
  AND e.name = 'Scent of the Soul: The Echoes of Vietnam';

-- Scent of the Soul: Zone A  (6 rows × 20 cols = 120)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,6)  r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Zone A'
  AND e.name = 'Scent of the Soul: The Echoes of Vietnam';

-- Scent of the Soul: Zone B  (6 rows × 20 cols = 120)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,6)  r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Zone B'
  AND e.name = 'Scent of the Soul: The Echoes of Vietnam';

-- Scent of the Soul: Zone C  (6 rows × 25 cols = 150)
-- Row A gets price_override, rows B-F standard
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code, price_override)
SELECT uuidv7(), z.id, 'Ghế A' || c, 'A', c::text, 'A' || c, 1500000
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,25) c
WHERE z.name = 'Zone C'
  AND e.name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(2,6)  r
         CROSS JOIN generate_series(1,25) c
WHERE z.name = 'Zone C'
  AND e.name = 'Scent of the Soul: The Echoes of Vietnam';

-- Âm Sắc Việt: VIP Floor  (15 rows × 10 cols = 150)
-- Row A gets price_override (best seats), rows B-O standard
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code, price_override)
SELECT uuidv7(), z.id, 'Ghế A' || c, 'A', c::text, 'A' || c, 3500000
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'VIP Floor'
  AND e.name = 'Âm Sắc Việt - THE RESONANCE';

INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(2,15) r
         CROSS JOIN generate_series(1,10) c
WHERE z.name = 'VIP Floor'
  AND e.name = 'Âm Sắc Việt - THE RESONANCE';

-- Âm Sắc Việt: Lower Bowl Left  (15 rows × 20 cols = 300)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,15) r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Lower Bowl Left'
  AND e.name = 'Âm Sắc Việt - THE RESONANCE';

-- Âm Sắc Việt: Lower Bowl Right  (15 rows × 20 cols = 300)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,15) r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Lower Bowl Right'
  AND e.name = 'Âm Sắc Việt - THE RESONANCE';

-- Âm Sắc Việt: GA Pit  (15 rows × 20 cols = 300)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,15) r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'GA Pit'
  AND e.name = 'Âm Sắc Việt - THE RESONANCE';

-- Âm Sắc Việt: Lower Bowl Back  (20 rows × 20 cols = 400)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,20) r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Lower Bowl Back'
  AND e.name = 'Âm Sắc Việt - THE RESONANCE';

-- Âm Sắc Việt: Upper Bowl  (25 rows × 20 cols = 500)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64+r) || c, chr(64+r), c::text, chr(64+r) || c
FROM public.zones z
         JOIN public.event_sessions es ON z.session_id = es.id
         JOIN public.events e ON es.event_id = e.id
         CROSS JOIN generate_series(1,25) r
         CROSS JOIN generate_series(1,20) c
WHERE z.name = 'Upper Bowl'
  AND e.name = 'Âm Sắc Việt - THE RESONANCE';


-- 7. Layout zone links (venueMode)

-- V-Glow: The Cyber-Heritage Night
-- Venue: Khu đô thị Vạn Phúc
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- Event zones: SVIP Lounge, VIP, GA Early Bird, GA
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'SVIP',          (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'V-Glow: The Cyber-Heritage Night' AND z.name = 'SVIP Lounge'   LIMIT 1),
                'VIP Left',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'V-Glow: The Cyber-Heritage Night' AND z.name = 'VIP'          LIMIT 1),
                'VIP Right',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'V-Glow: The Cyber-Heritage Night' AND z.name = 'VIP'          LIMIT 1),
                'GA Left',       (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'V-Glow: The Cyber-Heritage Night' AND z.name = 'GA'           LIMIT 1),
                'GA Early Bird', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'V-Glow: The Cyber-Heritage Night' AND z.name = 'GA Early Bird' LIMIT 1)
                     )
             )
WHERE name = 'V-Glow: The Cyber-Heritage Night';

-- The Echo of An Nam
-- Venue: Sân Lễ Hội Đền Vua Đinh - Vua Lê
-- Venue zones: VVIP | VIP | GA Standing
-- Event zones: VVIP, VIP, GA Standing
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VVIP',       (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'The Echo of An Nam' AND z.name = 'VVIP'       LIMIT 1),
                'VIP',        (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'The Echo of An Nam' AND z.name = 'VIP'        LIMIT 1),
                'GA Standing',(SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'The Echo of An Nam' AND z.name = 'GA Standing' LIMIT 1)
                     )
             )
WHERE name = 'The Echo of An Nam';

-- SÀI GÒN NEON BEATS: THE CYBER-FEST 2026
-- Venue: Khu đô thị Vạn Phúc
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- Event zones: SVIP, VIP Left, VIP Right, GA, GA Early Bird
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'SVIP',          (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026' AND z.name = 'SVIP'          LIMIT 1),
                'VIP Left',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026' AND z.name = 'VIP Left'      LIMIT 1),
                'VIP Right',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026' AND z.name = 'VIP Right'     LIMIT 1),
                'GA Left',       (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026' AND z.name = 'GA'           LIMIT 1),
                'GA Early Bird', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026' AND z.name = 'GA Early Bird' LIMIT 1)
                     )
             )
WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

-- CỔ NGHỆ KIÊU HÙNG
-- Venue: Công viên Yên Sở
-- Venue zones: VIP | Zone A | Zone B | GA
-- Event zones: VIP, Zone A, Zone B, GA
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG' AND z.name = 'VIP'    LIMIT 1),
                'Zone A', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG' AND z.name = 'Zone A' LIMIT 1),
                'Zone B', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG' AND z.name = 'Zone B' LIMIT 1),
                'GA',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CỔ NGHỆ KIÊU HÙNG' AND z.name = 'GA'     LIMIT 1)
                     )
             )
WHERE name = 'CỔ NGHỆ KIÊU HÙNG';

-- NEO-LUMINANCE: The Echo of Indochine
-- Venue: Khu đô thị Vạn Phúc
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- Event zones: SVIP Deck, VIP Left, VIP Right, GA, GA Early Bird
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'SVIP',          (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine' AND z.name = 'SVIP Deck'      LIMIT 1),
                'VIP Left',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine' AND z.name = 'VIP Left'      LIMIT 1),
                'VIP Right',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine' AND z.name = 'VIP Right'     LIMIT 1),
                'GA Left',       (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine' AND z.name = 'GA'           LIMIT 1),
                'GA Early Bird', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'NEO-LUMINANCE: The Echo of Indochine' AND z.name = 'GA Early Bird' LIMIT 1)
                     )
             )
WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';

-- Sắc Lam: The Indigo Echo
-- Venue: Gigamall Thủ Đức
-- Venue zones: VIP | GA
-- Event zones: VIP (Kèm quà tặng), Standard
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Gigamall Thủ Đức'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Sắc Lam: The Indigo Echo' AND z.name = 'VIP (Kèm quà tặng)' LIMIT 1),
                'GA',  (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Sắc Lam: The Indigo Echo' AND z.name = 'Standard'          LIMIT 1)
                     )
             )
WHERE name = 'Sắc Lam: The Indigo Echo';

-- CRYSTAL REALM: The Neon Garden
-- Venue: Công viên Yên Sở
-- Venue zones: VIP | Zone A | Zone B | GA
-- Event zones: VIP Crystal, Zone A, Zone B, GA
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CRYSTAL REALM: The Neon Garden' AND z.name = 'VIP Crystal' LIMIT 1),
                'Zone A', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CRYSTAL REALM: The Neon Garden' AND z.name = 'Zone A'     LIMIT 1),
                'Zone B', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CRYSTAL REALM: The Neon Garden' AND z.name = 'Zone B'     LIMIT 1),
                'GA',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'CRYSTAL REALM: The Neon Garden' AND z.name = 'GA'         LIMIT 1)
                     )
             )
WHERE name = 'CRYSTAL REALM: The Neon Garden';

-- SẮT & SON
-- Venue: Sân Lễ Hội Đền Vua Đinh - Vua Lê
-- Venue zones: VVIP | VIP | GA Standing
-- Event zones: VVIP Pit, VIP, GA
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VVIP',       (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SẮT & SON' AND z.name = 'VVIP Pit' LIMIT 1),
                'VIP',        (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SẮT & SON' AND z.name = 'VIP'      LIMIT 1),
                'GA Standing',(SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'SẮT & SON' AND z.name = 'GA'       LIMIT 1)
                     )
             )
WHERE name = 'SẮT & SON';

-- Techno-Sorcery: The Zenith of AI & Robotics
-- Venue: GEM Center
-- Venue zones: VIP | Zone A | Zone B | Zone C | Standing
-- Event zones: VIP Scholars, Zone A, Zone B, Zone C, Standing
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'GEM Center'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics' AND z.name = 'VIP Scholars' LIMIT 1),
                'Zone A',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics' AND z.name = 'Zone A'       LIMIT 1),
                'Zone B',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics' AND z.name = 'Zone B'       LIMIT 1),
                'Zone C',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics' AND z.name = 'Zone C'       LIMIT 1),
                'Standing', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Techno-Sorcery: The Zenith of AI & Robotics' AND z.name = 'Standing'     LIMIT 1)
                     )
             )
WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics';

-- THE DREAMCATCHER ARCHIPELAGO
-- Venue: Khu đô thị Vạn Phúc
-- Venue zones: SVIP | VIP Left | VIP Right | GA Left | GA Early Bird
-- Event zones: SVIP Dream, VIP, GA Early Bird, GA
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'SVIP',          (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO' AND z.name = 'SVIP Dream'    LIMIT 1),
                'VIP Left',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO' AND z.name = 'VIP'          LIMIT 1),
                'VIP Right',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO' AND z.name = 'VIP'          LIMIT 1),
                'GA Left',       (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO' AND z.name = 'GA'           LIMIT 1),
                'GA Early Bird', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'THE DREAMCATCHER ARCHIPELAGO' AND z.name = 'GA Early Bird' LIMIT 1)
                     )
             )
WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';

-- LUVIA: The Echo of Highlands
-- Venue: Công viên Yên Sở
-- Venue zones: VIP | Zone A | Zone B | GA
-- Event zones: VIP Hammock, Zone A, Zone B, GA
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'LUVIA: The Echo of Highlands' AND z.name = 'VIP Hammock' LIMIT 1),
                'Zone A', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'LUVIA: The Echo of Highlands' AND z.name = 'Zone A'      LIMIT 1),
                'Zone B', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'LUVIA: The Echo of Highlands' AND z.name = 'Zone B'      LIMIT 1),
                'GA',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'LUVIA: The Echo of Highlands' AND z.name = 'GA'          LIMIT 1)
                     )
             )
WHERE name = 'LUVIA: The Echo of Highlands';

-- Scent of the Soul: The Echoes of Vietnam
-- Venue: GEM Center
-- Venue zones: VIP | Zone A | Zone B | Zone C | Standing
-- Event zones: VIP Sensory, Zone A, Zone B, Zone C, Standing
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'GEM Center'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam' AND z.name = 'VIP Sensory' LIMIT 1),
                'Zone A',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam' AND z.name = 'Zone A'      LIMIT 1),
                'Zone B',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam' AND z.name = 'Zone B'      LIMIT 1),
                'Zone C',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam' AND z.name = 'Zone C'      LIMIT 1),
                'Standing', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Scent of the Soul: The Echoes of Vietnam' AND z.name = 'Standing'    LIMIT 1)
                     )
             )
WHERE name = 'Scent of the Soul: The Echoes of Vietnam';

-- Âm Sắc Việt - THE RESONANCE
-- Venue: CIS Arena
-- Venue zones (floor 1): VIP Floor | Lower Bowl Left | Lower Bowl Right | GA Pit | Lower Bowl Back
-- Venue zones (floor 2): Upper Bowl
-- Event zones: VIP Floor, Lower Bowl Left, Lower Bowl Right, GA Pit, Lower Bowl Back, Upper Bowl
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'CIS Arena'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP Floor',        (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Âm Sắc Việt - THE RESONANCE' AND z.name = 'VIP Floor'        LIMIT 1),
                'Lower Bowl Left',  (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Âm Sắc Việt - THE RESONANCE' AND z.name = 'Lower Bowl Left'  LIMIT 1),
                'Lower Bowl Right', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Âm Sắc Việt - THE RESONANCE' AND z.name = 'Lower Bowl Right' LIMIT 1),
                'GA Pit',           (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Âm Sắc Việt - THE RESONANCE' AND z.name = 'GA Pit'           LIMIT 1),
                'Lower Bowl Back',  (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Âm Sắc Việt - THE RESONANCE' AND z.name = 'Lower Bowl Back'  LIMIT 1),
                'Upper Bowl',       (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Âm Sắc Việt - THE RESONANCE' AND z.name = 'Upper Bowl'       LIMIT 1)
                     )
             )
WHERE name = 'Âm Sắc Việt - THE RESONANCE';


-- 8. Custom Layout

-- Vũ Trụ Cận Thị  (Mây in The Nest – intimate indie concert, Đà Lạt)
-- Zones: VVIP Intimate (front, sitting, 20), Seated Standard (back, sitting, 80)
UPDATE public.events
SET layout = jsonb_build_object(
        'floors', jsonb_build_array(
                jsonb_build_object(
                        'floor_name',       'Main Area',
                        'floor_order',      1,
                        'global_seat_size', 16,
                        'stage',            jsonb_build_object('x1', -0.35, 'y1', -0.96, 'x2', 0.35, 'y2', -0.76),
                        'stage_shapes',     jsonb_build_array(),
                        'zones',            jsonb_build_array(
                                jsonb_build_object(
                                        'zone_id',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Vũ Trụ Cận Thị' AND z.name = 'VVIP Intimate'   LIMIT 1),
                                        'zone_name',  'VVIP Intimate',
                                        'zone_type',  'sitting',
                                        'accessible', true,
                                        'shape_type', 'rect',
                                        'color',      '#f59e0b',
                                        'rotation',   0,
                                        'corner1',    jsonb_build_object('x', -0.45, 'y', -0.74),
                                        'corner2',    jsonb_build_object('x',  0.45, 'y', -0.74),
                                        'corner3',    jsonb_build_object('x',  0.45, 'y', -0.18),
                                        'corner4',    jsonb_build_object('x', -0.45, 'y', -0.18)
                                ),
                                jsonb_build_object(
                                        'zone_id',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Vũ Trụ Cận Thị' AND z.name = 'Seated Standard' LIMIT 1),
                                        'zone_name',  'Seated Standard',
                                        'zone_type',  'sitting',
                                        'accessible', true,
                                        'shape_type', 'rect',
                                        'color',      '#6366f1',
                                        'rotation',   0,
                                        'corner1',    jsonb_build_object('x', -0.85, 'y', -0.16),
                                        'corner2',    jsonb_build_object('x',  0.85, 'y', -0.16),
                                        'corner3',    jsonb_build_object('x',  0.85, 'y',  0.72),
                                        'corner4',    jsonb_build_object('x', -0.85, 'y',  0.72)
                                )
                                            )
                )
                  )
             )
WHERE name = 'Vũ Trụ Cận Thị';


-- KAIZEN: The Art of Precision  (Tinh Tế Cafe – omakase dining, Quận 3)
-- Zones: Chef's Table (front, sitting, 8), Dining Room (back, sitting, 16)
UPDATE public.events
SET layout = jsonb_build_object(
        'floors', jsonb_build_array(
                jsonb_build_object(
                        'floor_name',       'Dining Area',
                        'floor_order',      1,
                        'global_seat_size', 18,
                        'stage',            jsonb_build_object('x1', -0.6, 'y1', -0.96, 'x2', 0.6, 'y2', -0.76),
                        'stage_shapes',     jsonb_build_array(),
                        'zones',            jsonb_build_array(
                                jsonb_build_object(
                                        'zone_id',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'KAIZEN: The Art of Precision' AND z.name = 'Chef''s Table' LIMIT 1),
                                        'zone_name',  'Chef''s Table',
                                        'zone_type',  'sitting',
                                        'accessible', true,
                                        'shape_type', 'rect',
                                        'color',      '#f59e0b',
                                        'rotation',   0,
                                        'corner1',    jsonb_build_object('x', -0.65, 'y', -0.74),
                                        'corner2',    jsonb_build_object('x',  0.65, 'y', -0.74),
                                        'corner3',    jsonb_build_object('x',  0.65, 'y', -0.25),
                                        'corner4',    jsonb_build_object('x', -0.65, 'y', -0.25)
                                ),
                                jsonb_build_object(
                                        'zone_id',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'KAIZEN: The Art of Precision' AND z.name = 'Dining Room' LIMIT 1),
                                        'zone_name',  'Dining Room',
                                        'zone_type',  'sitting',
                                        'accessible', true,
                                        'shape_type', 'rect',
                                        'color',      '#6366f1',
                                        'rotation',   0,
                                        'corner1',    jsonb_build_object('x', -0.85, 'y', -0.23),
                                        'corner2',    jsonb_build_object('x',  0.85, 'y', -0.23),
                                        'corner3',    jsonb_build_object('x',  0.85, 'y',  0.70),
                                        'corner4',    jsonb_build_object('x', -0.85, 'y',  0.70)
                                )
                                            )
                )
                  )
             )
WHERE name = 'KAIZEN: The Art of Precision';


