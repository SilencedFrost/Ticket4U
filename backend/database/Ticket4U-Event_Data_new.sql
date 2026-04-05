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

--2. Table: Venues
INSERT INTO public.venues (id, name,           address_line,                                                 latitude,  longitude, layout, image_url, created_at, updated_at) VALUES
(uuidv7(), 'CIS Arena',                        '7, Đường số 23, Phú Mỹ Hưng, Phường Tân Mỹ, TP.Hồ Chí Minh', 10.713750, 106.728750, NULL, 'https://s.inyourpocket.com/gallery/178413.jpg', NOW(), NULL),
(uuidv7(), 'Nhà Hát Bến Thành',                '6 Đường Mạc Đĩnh Chi, Phường Sài Gòn, TP.Hồ Chí Minh',       10.784246, 106.700877, NULL, 'http://vietlandmarks.com/upload/142582138454fc4ec85a5d2.jpg', NOW(), NULL),
(uuidv7(), 'Mây in The Nest',                  '519 Thôn Măng Lin, P. Langbiang, Đà Lạt, Tỉnh Lâm Đồng',     11.978842, 108.393794, NULL, 'https://media2.gody.vn/public/images/place/may-lang-thang-da-lat/614c498b500ff-1632389515.jpeg', NOW(), NULL),
(uuidv7(), 'GEM Center',                       '8 Đường Nguyễn Bỉnh Khiêm, Phường Sài Gòn, TP.Hồ Chí Minh',  10.790146, 106.702379, NULL, 'https://images2.thanhnien.vn/528068263637045248/2023/2/22/1-gem-center-16770590914701989789155.jpg', NOW(), NULL),
(uuidv7(), 'Tinh Tế Cafe',                     '351/56 Đ. Lê Văn Sỹ, Phường Nhiêu Lộc, TP.Hồ Chí Minh',      10.785960, 106.676488, NULL, 'https://ik.imagekit.io/tvlk/blog/2022/10/quan-cafe-nhieu-cay-xanh-tphcm-1.jpeg?tr=q-70,c-at_max,w-500,h-300,dpr-2', NOW(), NULL),
(uuidv7(), 'Dreamplex Điện Biên Phủ',          '195 Đường Điện Biên Phủ, Phường Gia Định, TP.Hồ Chí Minh',   10.799603, 106.705507, NULL, 'https://maisonoffice.vn/wp-content/uploads/2021/09/van-phong-cho-thue-dreamplex-195-dien-bien-phu.jpg', NOW(), NULL),
(uuidv7(), 'Gigamall Thủ Đức',                 '240-242 Phạm Văn Đồng, Phường Hiệp Bình, TP.Hồ Chí Minh',    10.828149, 106.721385, NULL, 'https://bidiland.vn/dataweb/images/tin-tuc/tong-hop-nhung-sieu-thi-va-cho-khu-vuc-quan-9-quan-2-quan-thu-duc-thanh-pho-thu-duc-coop-extra-gigamall-bidiland(1).jpg', NOW(), NULL),
(uuidv7(), 'Sân Lễ Hội Đền Vua Đinh - Vua Lê', 'Xã Trường Yên, Huyện Hoa Lư, Tỉnh Ninh Bình',                20.284638, 105.905315, NULL, 'https://mtcs.1cdn.vn/2023/02/16/le-hoi-den-hung.jpg', NOW(), NULL),
(uuidv7(), 'Công viên Yên Sở',                 'QL1A, Gamuda Central, Thủ đô Hà Nội',                        20.964741, 105.854647, NULL, 'https://gamudagardens.vn/wp-content/uploads/2016/09/ho-yen-so.jpg', NOW(), NULL),
(uuidv7(), 'Khu đô thị Vạn Phúc',              '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',                   10.846066, 106.709149, NULL, 'https://khudothivanphuc.vn/wp-content/uploads/2021/07/cong-vien-ocean-world-van-phuc.jpg', NOW(), NULL);

--3. Table: Events

--TODO: update organizers and status
INSERT INTO public.events(id, organizer_id, status, name, banner_url, created_at) VALUES
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'V-Glow: The Cyber-Heritage Night'           , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'The Echo of An Nam'                         , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026'    , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'CỔ NGHỆ KIÊU HÙNG'                          , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'NEO-LUMINANCE: The Echo of Indochine'       , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Sắc Lam: The Indigo Echo'                   , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'CRYSTAL REALM: The Neon Garden'             , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'SẮT & SON'                                  , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Techno-Sorcery: The Zenith of AI & Robotics', '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Vũ Trụ Cận Thị'                             , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'THE DREAMCATCHER ARCHIPELAGO'               , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'KAIZEN: The Art of Precision'               , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'LUVIA: The Echo of Highlands'               , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Scent of the Soul: The Echoes of Vietnam'   , '', now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Âm Sắc Việt - THE RESONANCE'                , '', now());

--4. Table: event_categories
INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 1 FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ'
UNION ALL
SELECT id, 3 FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ'
UNION ALL
SELECT id, 4 FROM public.events WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes'
UNION ALL
SELECT id, 1 FROM public.events WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour'
UNION ALL
SELECT id, 1 FROM public.events WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh'
UNION ALL
SELECT id, 3 FROM public.events WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35'
UNION ALL
SELECT id, 1 FROM public.events WHERE name = 'Ravolution Music Festival: Unite'
UNION ALL
SELECT id, 3 FROM public.events WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi'
UNION ALL
SELECT id, 2 FROM public.events WHERE name = 'Vietnam Tech Summit 2025'
UNION ALL
SELECT id, 2 FROM public.events WHERE name = 'Workshop: Marketing 0 Đồng cho Startup'
UNION ALL
SELECT id, 3 FROM public.events WHERE name = 'Van Gogh Art Lighting Experience';

--3. Table: Event Sessions 
-- Session cho Hà Anh Tuấn
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '30 days', NOW() + INTERVAL '30 days 4 hours', 'ONGOING', 'Show chính', NOW()
FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- Session cho Saigon Heat
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days' + INTERVAL '2 hours', 'FINISHED', 'Trận chính', NOW()
FROM public.events WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Session cho Những Thành Phố Mơ Màng
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '45 days', NOW() + INTERVAL '45 days 6 hours', 'ONGOING', 'Đêm diễn Summer Tour', NOW()
FROM public.events WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';

-- Session cho Mây Lang Thang
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '25 days', NOW() + INTERVAL '25 days 3 hours', 'ONGOING', 'Đêm nhạc Trịnh', NOW()
FROM public.events WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Session cho Kịch Idecaf
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '5 days', NOW() + INTERVAL '5 days 3 hours', 'ONGOING', 'Buổi chiều Chủ Nhật', NOW()
FROM public.events WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- Session cho Ravolution
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '60 days', NOW() + INTERVAL '61 days', 'ONGOING', 'Festival Day 1', NOW()
FROM public.events WHERE name = 'Ravolution Music Festival: Unite';

-- Session cho Saigon Tếu
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '7 days', NOW() + INTERVAL '7 days 2 hours', 'ONGOING', 'Đêm hài độc thoại', NOW()
FROM public.events WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Session cho Vietnam Tech Summit
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '10 days', NOW() + INTERVAL '10 days 8 hours', 'ONGOING', 'Tech Summit Day 1', NOW()
FROM public.events WHERE name = 'Vietnam Tech Summit 2025';

-- Session cho Workshop Marketing
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() + INTERVAL '14 days', NOW() + INTERVAL '14 days 4 hours', 'ONGOING', 'Workshop Session', NOW()
FROM public.events WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';

-- Session cho Van Gogh Art Experience
INSERT INTO public.event_sessions (id, event_id, start_date, end_date, status, name, created_at)
SELECT uuidv7(), id, NOW() - INTERVAL '10 days', NOW() + INTERVAL '20 days', 'ONGOING', 'Exhibition Period', NOW()
FROM public.events WHERE name = 'Van Gogh Art Lighting Experience';

--4. Table: Zones 
-- Zones cho Hà Anh Tuấn
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VVIP', false, 50, 2500000, 
	'Khu vực VIP cao cấp với vị trí đẹp nhất, dịch vụ đặc biệt và nhiều ưu đãi độc quyền. Ghế ngồi hạng sang gần sân khấu, phục vụ đồ uống cao cấp, quà tặng đặc biệt.', 
	'Premium VIP area with the best location, exclusive services and special privileges. Luxury seats near the stage, premium beverage service, special gift package.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Vòng tay check-in VIP", "Goodie bag", "Meet & Greet"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Standing', true, 800, 650000, 
	'Khu vực đứng tự do gần sân khấu, trải nghiệm âm nhạc bùng nổ cùng đám đông.', 
	'Standing area near the stage for an explosive music experience with the crowd.',
	NULL,
	'["Vòng tay check-in", "Nước suối"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- Zones cho Saigon Heat
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Courtside VIP', false, 50, 2500000, 
	'Ghế ngồi sát sàn đấu, cảm nhận từng bước chạy của cầu thủ. Ghế ngồi VIP sát sân, áo đấu phiên bản giới hạn, F&B phục vụ tại chỗ.', 
	'Courtside seats to feel every move of the players. VIP courtside seats, limited edition jersey, F&B service at seat.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Áo đấu phiên bản giới hạn", "F&B phục vụ tại chỗ"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Standard Bleachers', false, 800, 150000, 
	'Khu vực khán đài tiêu chuẩn với tầm nhìn tốt. Giá cả phải chăng cho người hâm mộ.', 
	'Standard bleachers area with good view. Affordable price for fans.',
	NULL,
	NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Zones cho Những Thành Phố Mơ Màng
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Cư Dân VIP', false, 200, 1500000, 
	'Đặc quyền cư dân VIP với khu vực nghỉ ngơi riêng, quà tặng độc quyền, lối đi ưu tiên và nước uống miễn phí cả ngày.', 
	'VIP resident privileges with private lounge, exclusive gifts, priority access and unlimited beverages.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Bộ quà tặng Cư Dân", "Lối đi ưu tiên", "Nước uống miễn phí"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Thường', true, 2000, 650000, 
	'Khu vực tự do dành cho các cư dân yêu âm nhạc. Hòa mình vào không khí sôi động của festival.', 
	'General admission area for music-loving residents. Immerse in the vibrant festival atmosphere.',
	NULL,
	'["Vòng tay vải", "Sticker"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour';

-- Zones cho Mây Lang Thang
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu VVIP (Gần ca sĩ)', false, 50, 1800000, 
	'Vị trí đẹp nhất để tương tác cùng nghệ sĩ, thưởng thức nhạc Trịnh bất hủ. Một phần nước tự chọn cao cấp, đĩa CD nhạc Trịnh kỷ niệm, cơ hội chụp ảnh với nghệ sĩ.', 
	'The best spot to interact with the artist, enjoying timeless Trinh melodies. Premium beverage of choice, commemorative CD, photo opportunity with artist.',
	NULL,
	'["Một phần nước tự chọn", "Đĩa CD nhạc Trịnh"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu Khán Đài', false, 200, 800000, 
	'Khu vực ngồi thoải mái với tầm nhìn đẹp, thưởng thức đêm nhạc trong không gian lãng mạn của Đà Lạt.', 
	'Comfortable seating area with beautiful view, enjoy the music night in romantic Dalat atmosphere.',
	NULL,
	'["Nước uống", "Chăn len"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Zones cho Kịch Idecaf
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu A (Lầu 1)', false, 300, 350000, 
	'Khu vực lầu 1 gần sân khấu nhất, phù hợp cho trẻ em để có trải nghiệm tốt nhất.', 
	'Floor 1 area closest to the stage, perfect for children to have the best experience.',
	NULL,
	NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu B (Lầu 2)', false, 200, 250000, 
	'Khu vực lầu 2 với tầm nhìn toàn cảnh, giá cả phải chăng.', 
	'Floor 2 area with panoramic view, affordable price.',
	NULL,
	NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- Zones cho Ravolution
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'SVIP Deck', false, 100, 4500000, 
	'Tận hưởng lễ hội trên khán đài cao cấp với không gian riêng tư. Private Bar với bartender riêng, quà tặng từ nhà tài trợ cao cấp, lối đi VIP không xếp hàng, khu vực nghỉ ngơi riêng biệt.', 
	'Enjoy the festival from premium deck with private space. Private Bar with dedicated bartender, premium sponsor gifts, VIP access with no queuing, private lounge area.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Private Bar", "Quà tặng từ nhà tài trợ", "Lối đi VIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Ravolution Music Festival: Unite';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Early Bird', true, 1500, 850000, 
	'Khu vực tự do cho những người đến sớm, hòa mình vào không khí EDM bùng nổ cùng hàng nghìn fan hâm mộ.', 
	'General admission area for early arrivals, immerse in explosive EDM atmosphere with thousands of fans.',
	NULL,
	'["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Ravolution Music Festival: Unite';

-- Zones cho Saigon Tếu (FREE)
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu ngồi chính', false, 200, 0, 
	NULL, NULL, NULL, NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Zones cho Vietnam Tech Summit (FREE)
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Hội trường chính', false, 500, 0, 
	NULL, NULL, NULL, NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Vietnam Tech Summit 2025';

-- Zones cho Workshop Marketing (FREE)
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu học viên', false, 100, 0, 
	NULL, NULL, NULL, NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Workshop: Marketing 0 Đồng cho Startup';

--5. Table: Seats 
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT 
    uuidv7(), 
    z.id, 
    'Ghế ' || chr(64 + r) || c, 
    chr(64 + r), 
    c::text, 
    chr(64 + r) || c
FROM public.zones z
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 5) r 
CROSS JOIN generate_series(1, 10) c 
WHERE z.name = 'VVIP' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- Seats cho Saigon Tếu (FREE)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT 
    uuidv7(), 
    z.id, 
    'Ghế ' || chr(64 + r) || c, 
    chr(64 + r), 
    c::text, 
    chr(64 + r) || c
FROM public.zones z
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 20) r 
CROSS JOIN generate_series(1, 10) c 
WHERE z.name = 'Khu ngồi chính' AND e.name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Seats cho Vietnam Tech Summit (FREE)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT 
    uuidv7(), 
    z.id, 
    'Ghế ' || chr(64 + r) || c, 
    chr(64 + r), 
    c::text, 
    chr(64 + r) || c
FROM public.zones z
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 25) r 
CROSS JOIN generate_series(1, 20) c 
WHERE z.name = 'Hội trường chính' AND e.name = 'Vietnam Tech Summit 2025';

-- Seats cho Workshop Marketing (FREE)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT 
    uuidv7(), 
    z.id, 
    'Ghế ' || chr(64 + r) || c, 
    chr(64 + r), 
    c::text, 
    chr(64 + r) || c
FROM public.zones z
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 10) r 
CROSS JOIN generate_series(1, 10) c 
WHERE z.name = 'Khu học viên' AND e.name = 'Workshop: Marketing 0 Đồng cho Startup';

--6. Venues Layout
-- CIS Arena → Sports Arena layout
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.3, "y1": -0.3, "x2": 0.3, "y2": 0.3 },
  "zones": [
    {
      "zone_name": "Courtside Left",
      "zone_type": "sitting",
      "color": "#F59E0B",
      "corner1": { "x": -0.9,  "y": -0.5 },
      "corner2": { "x": -0.35, "y": -0.5 },
      "corner3": { "x": -0.35, "y":  0.5 },
      "corner4": { "x": -0.9,  "y":  0.5 },
      "seats": [
        { "seat_name": "L-A1", "seat_id": "L-A1", "seat_pos": { "x": -0.8, "y": -0.35 }, "seat_rotation":  90 },
        { "seat_name": "L-A2", "seat_id": "L-A2", "seat_pos": { "x": -0.8, "y": -0.1  }, "seat_rotation":  90 },
        { "seat_name": "L-A3", "seat_id": "L-A3", "seat_pos": { "x": -0.8, "y":  0.15 }, "seat_rotation":  90 },
        { "seat_name": "L-A4", "seat_id": "L-A4", "seat_pos": { "x": -0.8, "y":  0.4  }, "seat_rotation":  90 },
        { "seat_name": "L-B1", "seat_id": "L-B1", "seat_pos": { "x": -0.6, "y": -0.35 }, "seat_rotation":  90 },
        { "seat_name": "L-B2", "seat_id": "L-B2", "seat_pos": { "x": -0.6, "y": -0.1  }, "seat_rotation":  90 },
        { "seat_name": "L-B3", "seat_id": "L-B3", "seat_pos": { "x": -0.6, "y":  0.15 }, "seat_rotation":  90 },
        { "seat_name": "L-B4", "seat_id": "L-B4", "seat_pos": { "x": -0.6, "y":  0.4  }, "seat_rotation":  90 }
      ]
    },
    {
      "zone_name": "Courtside Right",
      "zone_type": "sitting",
      "color": "#F59E0B",
      "corner1": { "x":  0.35, "y": -0.5 },
      "corner2": { "x":  0.9,  "y": -0.5 },
      "corner3": { "x":  0.9,  "y":  0.5 },
      "corner4": { "x":  0.35, "y":  0.5 },
      "seats": [
        { "seat_name": "R-A1", "seat_id": "R-A1", "seat_pos": { "x":  0.6, "y": -0.35 }, "seat_rotation": -90 },
        { "seat_name": "R-A2", "seat_id": "R-A2", "seat_pos": { "x":  0.6, "y": -0.1  }, "seat_rotation": -90 },
        { "seat_name": "R-A3", "seat_id": "R-A3", "seat_pos": { "x":  0.6, "y":  0.15 }, "seat_rotation": -90 },
        { "seat_name": "R-A4", "seat_id": "R-A4", "seat_pos": { "x":  0.6, "y":  0.4  }, "seat_rotation": -90 },
        { "seat_name": "R-B1", "seat_id": "R-B1", "seat_pos": { "x":  0.8, "y": -0.35 }, "seat_rotation": -90 },
        { "seat_name": "R-B2", "seat_id": "R-B2", "seat_pos": { "x":  0.8, "y": -0.1  }, "seat_rotation": -90 },
        { "seat_name": "R-B3", "seat_id": "R-B3", "seat_pos": { "x":  0.8, "y":  0.15 }, "seat_rotation": -90 },
        { "seat_name": "R-B4", "seat_id": "R-B4", "seat_pos": { "x":  0.8, "y":  0.4  }, "seat_rotation": -90 }
      ]
    },
    {
      "zone_name": "Bleachers",
      "zone_type": "sitting",
      "color": "#64748B",
      "corner1": { "x": -0.9, "y":  0.55 },
      "corner2": { "x":  0.9, "y":  0.55 },
      "corner3": { "x":  0.9, "y":  0.95 },
      "corner4": { "x": -0.9, "y":  0.95 },
      "seats": [
        { "seat_name": "BL-A1", "seat_id": "BL-A1", "seat_pos": { "x": -0.72, "y": 0.68 }, "seat_rotation": 0 },
        { "seat_name": "BL-A2", "seat_id": "BL-A2", "seat_pos": { "x": -0.48, "y": 0.68 }, "seat_rotation": 0 },
        { "seat_name": "BL-A3", "seat_id": "BL-A3", "seat_pos": { "x": -0.24, "y": 0.68 }, "seat_rotation": 0 },
        { "seat_name": "BL-A4", "seat_id": "BL-A4", "seat_pos": { "x":  0.0,  "y": 0.68 }, "seat_rotation": 0 },
        { "seat_name": "BL-A5", "seat_id": "BL-A5", "seat_pos": { "x":  0.24, "y": 0.68 }, "seat_rotation": 0 },
        { "seat_name": "BL-A6", "seat_id": "BL-A6", "seat_pos": { "x":  0.48, "y": 0.68 }, "seat_rotation": 0 },
        { "seat_name": "BL-A7", "seat_id": "BL-A7", "seat_pos": { "x":  0.72, "y": 0.68 }, "seat_rotation": 0 }
      ]
    }
  ],
  "seat_size": 20
}'::jsonb
WHERE name = 'CIS Arena';


-- Nhà Hát Bến Thành → Theater - VIP + Standard
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.5, "y1": -1.0, "x2": 0.5, "y2": -0.88 },
  "zones": [
    {
      "zone_name": "VIP",
      "zone_type": "sitting",
      "color": "#DC2626",
      "corner1": { "x": -0.85, "y": -0.85 },
      "corner2": { "x":  0.85, "y": -0.85 },
      "corner3": { "x":  0.85, "y": -0.1  },
      "corner4": { "x": -0.85, "y": -0.1  },
      "seats": [
        { "seat_name": "A1", "seat_id": "A1", "seat_pos": { "x": -0.65, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A2", "seat_id": "A2", "seat_pos": { "x": -0.4,  "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A3", "seat_id": "A3", "seat_pos": { "x": -0.15, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A4", "seat_id": "A4", "seat_pos": { "x":  0.15, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A5", "seat_id": "A5", "seat_pos": { "x":  0.4,  "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A6", "seat_id": "A6", "seat_pos": { "x":  0.65, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "B1", "seat_id": "B1", "seat_pos": { "x": -0.65, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B2", "seat_id": "B2", "seat_pos": { "x": -0.4,  "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B3", "seat_id": "B3", "seat_pos": { "x": -0.15, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B4", "seat_id": "B4", "seat_pos": { "x":  0.15, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B5", "seat_id": "B5", "seat_pos": { "x":  0.4,  "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B6", "seat_id": "B6", "seat_pos": { "x":  0.65, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "C1", "seat_id": "C1", "seat_pos": { "x": -0.65, "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C2", "seat_id": "C2", "seat_pos": { "x": -0.4,  "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C3", "seat_id": "C3", "seat_pos": { "x": -0.15, "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C4", "seat_id": "C4", "seat_pos": { "x":  0.15, "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C5", "seat_id": "C5", "seat_pos": { "x":  0.4,  "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C6", "seat_id": "C6", "seat_pos": { "x":  0.65, "y": -0.25 }, "seat_rotation": 0 }
      ]
    },
    {
      "zone_name": "Standard",
      "zone_type": "sitting",
      "color": "#059669",
      "corner1": { "x": -0.85, "y": -0.05 },
      "corner2": { "x":  0.85, "y": -0.05 },
      "corner3": { "x":  0.85, "y":  0.9  },
      "corner4": { "x": -0.85, "y":  0.9  },
      "seats": [
        { "seat_name": "D1", "seat_id": "D1", "seat_pos": { "x": -0.65, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D2", "seat_id": "D2", "seat_pos": { "x": -0.4,  "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D3", "seat_id": "D3", "seat_pos": { "x": -0.15, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D4", "seat_id": "D4", "seat_pos": { "x":  0.15, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D5", "seat_id": "D5", "seat_pos": { "x":  0.4,  "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D6", "seat_id": "D6", "seat_pos": { "x":  0.65, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "E1", "seat_id": "E1", "seat_pos": { "x": -0.65, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E2", "seat_id": "E2", "seat_pos": { "x": -0.4,  "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E3", "seat_id": "E3", "seat_pos": { "x": -0.15, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E4", "seat_id": "E4", "seat_pos": { "x":  0.15, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E5", "seat_id": "E5", "seat_pos": { "x":  0.4,  "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E6", "seat_id": "E6", "seat_pos": { "x":  0.65, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "F1", "seat_id": "F1", "seat_pos": { "x": -0.65, "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F2", "seat_id": "F2", "seat_pos": { "x": -0.4,  "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F3", "seat_id": "F3", "seat_pos": { "x": -0.15, "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F4", "seat_id": "F4", "seat_pos": { "x":  0.15, "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F5", "seat_id": "F5", "seat_pos": { "x":  0.4,  "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F6", "seat_id": "F6", "seat_pos": { "x":  0.65, "y": 0.6  }, "seat_rotation": 0 }
      ]
    }
  ],
  "seat_size": 22
}'::jsonb
WHERE name = 'Nhà Hát Bến Thành';


-- Mây in The Nest → Concert Classic (VIP sitting + GA standing)
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
  "zones": [
    {
      "zone_name": "VIP",
      "zone_type": "sitting",
      "color": "#DC2626",
      "corner1": { "x": -0.85, "y": -0.82 },
      "corner2": { "x":  0.85, "y": -0.82 },
      "corner3": { "x":  0.85, "y": -0.2  },
      "corner4": { "x": -0.85, "y": -0.2  },
      "seats": [
        { "seat_name": "A1", "seat_id": "A1", "seat_pos": { "x": -0.65, "y": -0.7  }, "seat_rotation": 0 },
        { "seat_name": "A2", "seat_id": "A2", "seat_pos": { "x": -0.4,  "y": -0.7  }, "seat_rotation": 0 },
        { "seat_name": "A3", "seat_id": "A3", "seat_pos": { "x": -0.15, "y": -0.7  }, "seat_rotation": 0 },
        { "seat_name": "A4", "seat_id": "A4", "seat_pos": { "x":  0.15, "y": -0.7  }, "seat_rotation": 0 },
        { "seat_name": "A5", "seat_id": "A5", "seat_pos": { "x":  0.4,  "y": -0.7  }, "seat_rotation": 0 },
        { "seat_name": "A6", "seat_id": "A6", "seat_pos": { "x":  0.65, "y": -0.7  }, "seat_rotation": 0 },
        { "seat_name": "B1", "seat_id": "B1", "seat_pos": { "x": -0.65, "y": -0.45 }, "seat_rotation": 0 },
        { "seat_name": "B2", "seat_id": "B2", "seat_pos": { "x": -0.4,  "y": -0.45 }, "seat_rotation": 0 },
        { "seat_name": "B3", "seat_id": "B3", "seat_pos": { "x": -0.15, "y": -0.45 }, "seat_rotation": 0 },
        { "seat_name": "B4", "seat_id": "B4", "seat_pos": { "x":  0.15, "y": -0.45 }, "seat_rotation": 0 },
        { "seat_name": "B5", "seat_id": "B5", "seat_pos": { "x":  0.4,  "y": -0.45 }, "seat_rotation": 0 },
        { "seat_name": "B6", "seat_id": "B6", "seat_pos": { "x":  0.65, "y": -0.45 }, "seat_rotation": 0 }
      ]
    },
    {
      "zone_name": "GA",
      "zone_type": "standing",
      "color": "#2563EB",
      "corner1": { "x": -0.85, "y": -0.15 },
      "corner2": { "x":  0.85, "y": -0.15 },
      "corner3": { "x":  0.85, "y":  0.9  },
      "corner4": { "x": -0.85, "y":  0.9  },
      "seats": []
    }
  ],
  "seat_size": 22
}'::jsonb
WHERE name = 'Mây in The Nest';


-- GEM Center → Theater - VIP + Standard
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.5, "y1": -1.0, "x2": 0.5, "y2": -0.88 },
  "zones": [
    {
      "zone_name": "VIP",
      "zone_type": "sitting",
      "color": "#DC2626",
      "corner1": { "x": -0.85, "y": -0.85 },
      "corner2": { "x":  0.85, "y": -0.85 },
      "corner3": { "x":  0.85, "y": -0.1  },
      "corner4": { "x": -0.85, "y": -0.1  },
      "seats": [
        { "seat_name": "A1", "seat_id": "A1", "seat_pos": { "x": -0.65, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A2", "seat_id": "A2", "seat_pos": { "x": -0.4,  "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A3", "seat_id": "A3", "seat_pos": { "x": -0.15, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A4", "seat_id": "A4", "seat_pos": { "x":  0.15, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A5", "seat_id": "A5", "seat_pos": { "x":  0.4,  "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A6", "seat_id": "A6", "seat_pos": { "x":  0.65, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "B1", "seat_id": "B1", "seat_pos": { "x": -0.65, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B2", "seat_id": "B2", "seat_pos": { "x": -0.4,  "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B3", "seat_id": "B3", "seat_pos": { "x": -0.15, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B4", "seat_id": "B4", "seat_pos": { "x":  0.15, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B5", "seat_id": "B5", "seat_pos": { "x":  0.4,  "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "B6", "seat_id": "B6", "seat_pos": { "x":  0.65, "y": -0.55 }, "seat_rotation": 0 },
        { "seat_name": "C1", "seat_id": "C1", "seat_pos": { "x": -0.65, "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C2", "seat_id": "C2", "seat_pos": { "x": -0.4,  "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C3", "seat_id": "C3", "seat_pos": { "x": -0.15, "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C4", "seat_id": "C4", "seat_pos": { "x":  0.15, "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C5", "seat_id": "C5", "seat_pos": { "x":  0.4,  "y": -0.25 }, "seat_rotation": 0 },
        { "seat_name": "C6", "seat_id": "C6", "seat_pos": { "x":  0.65, "y": -0.25 }, "seat_rotation": 0 }
      ]
    },
    {
      "zone_name": "Standard",
      "zone_type": "sitting",
      "color": "#059669",
      "corner1": { "x": -0.85, "y": -0.05 },
      "corner2": { "x":  0.85, "y": -0.05 },
      "corner3": { "x":  0.85, "y":  0.9  },
      "corner4": { "x": -0.85, "y":  0.9  },
      "seats": [
        { "seat_name": "D1", "seat_id": "D1", "seat_pos": { "x": -0.65, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D2", "seat_id": "D2", "seat_pos": { "x": -0.4,  "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D3", "seat_id": "D3", "seat_pos": { "x": -0.15, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D4", "seat_id": "D4", "seat_pos": { "x":  0.15, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D5", "seat_id": "D5", "seat_pos": { "x":  0.4,  "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "D6", "seat_id": "D6", "seat_pos": { "x":  0.65, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "E1", "seat_id": "E1", "seat_pos": { "x": -0.65, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E2", "seat_id": "E2", "seat_pos": { "x": -0.4,  "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E3", "seat_id": "E3", "seat_pos": { "x": -0.15, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E4", "seat_id": "E4", "seat_pos": { "x":  0.15, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E5", "seat_id": "E5", "seat_pos": { "x":  0.4,  "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "E6", "seat_id": "E6", "seat_pos": { "x":  0.65, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "F1", "seat_id": "F1", "seat_pos": { "x": -0.65, "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F2", "seat_id": "F2", "seat_pos": { "x": -0.4,  "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F3", "seat_id": "F3", "seat_pos": { "x": -0.15, "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F4", "seat_id": "F4", "seat_pos": { "x":  0.15, "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F5", "seat_id": "F5", "seat_pos": { "x":  0.4,  "y": 0.6  }, "seat_rotation": 0 },
        { "seat_name": "F6", "seat_id": "F6", "seat_pos": { "x":  0.65, "y": 0.6  }, "seat_rotation": 0 }
      ]
    }
  ],
  "seat_size": 22
}'::jsonb
WHERE name = 'GEM Center';


-- Tinh Tế Cafe → Restaurant layout (intimate, small venue)
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.15, "y1": -1.0, "x2": 0.15, "y2": -0.9 },
  "zones": [
    {
      "zone_name": "Window Section",
      "zone_type": "sitting",
      "color": "#DC2626",
      "corner1": { "x": -0.9, "y": -0.85 },
      "corner2": { "x": -0.3, "y": -0.85 },
      "corner3": { "x": -0.3, "y":  0.9  },
      "corner4": { "x": -0.9, "y":  0.9  },
      "seats": [
        { "seat_name": "W1", "seat_id": "W1", "seat_pos": { "x": -0.7, "y": -0.65 }, "seat_rotation":  90 },
        { "seat_name": "W2", "seat_id": "W2", "seat_pos": { "x": -0.7, "y": -0.35 }, "seat_rotation":  90 },
        { "seat_name": "W3", "seat_id": "W3", "seat_pos": { "x": -0.7, "y": -0.05 }, "seat_rotation":  90 },
        { "seat_name": "W4", "seat_id": "W4", "seat_pos": { "x": -0.7, "y":  0.25 }, "seat_rotation":  90 },
        { "seat_name": "W5", "seat_id": "W5", "seat_pos": { "x": -0.7, "y":  0.55 }, "seat_rotation":  90 }
      ]
    },
    {
      "zone_name": "Main Floor",
      "zone_type": "sitting",
      "color": "#059669",
      "corner1": { "x": -0.25, "y": -0.85 },
      "corner2": { "x":  0.25, "y": -0.85 },
      "corner3": { "x":  0.25, "y":  0.9  },
      "corner4": { "x": -0.25, "y":  0.9  },
      "seats": [
        { "seat_name": "M1", "seat_id": "M1", "seat_pos": { "x": 0.0, "y": -0.65 }, "seat_rotation": 0 },
        { "seat_name": "M2", "seat_id": "M2", "seat_pos": { "x": 0.0, "y": -0.35 }, "seat_rotation": 0 },
        { "seat_name": "M3", "seat_id": "M3", "seat_pos": { "x": 0.0, "y": -0.05 }, "seat_rotation": 0 },
        { "seat_name": "M4", "seat_id": "M4", "seat_pos": { "x": 0.0, "y":  0.25 }, "seat_rotation": 0 },
        { "seat_name": "M5", "seat_id": "M5", "seat_pos": { "x": 0.0, "y":  0.55 }, "seat_rotation": 0 }
      ]
    },
    {
      "zone_name": "Private Room",
      "zone_type": "sitting",
      "color": "#7C3AED",
      "corner1": { "x":  0.3, "y": -0.85 },
      "corner2": { "x":  0.9, "y": -0.85 },
      "corner3": { "x":  0.9, "y":  0.9  },
      "corner4": { "x":  0.3, "y":  0.9  },
      "seats": [
        { "seat_name": "P1", "seat_id": "P1", "seat_pos": { "x": 0.6, "y": -0.65 }, "seat_rotation": -90 },
        { "seat_name": "P2", "seat_id": "P2", "seat_pos": { "x": 0.6, "y": -0.35 }, "seat_rotation": -90 },
        { "seat_name": "P3", "seat_id": "P3", "seat_pos": { "x": 0.6, "y": -0.05 }, "seat_rotation": -90 },
        { "seat_name": "P4", "seat_id": "P4", "seat_pos": { "x": 0.6, "y":  0.25 }, "seat_rotation": -90 },
        { "seat_name": "P5", "seat_id": "P5", "seat_pos": { "x": 0.6, "y":  0.55 }, "seat_rotation": -90 }
      ]
    }
  ],
  "seat_size": 22
}'::jsonb
WHERE name = 'Tinh Tế Cafe';


-- Dreamplex Điện Biên Phủ → Theater - VIP + Standard (workshop/conference style)
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.5, "y1": -1.0, "x2": 0.5, "y2": -0.88 },
  "zones": [
    {
      "zone_name": "VIP",
      "zone_type": "sitting",
      "color": "#059669",
      "corner1": { "x": -0.85, "y": -0.85 },
      "corner2": { "x":  0.85, "y": -0.85 },
      "corner3": { "x":  0.85, "y": -0.1  },
      "corner4": { "x": -0.85, "y": -0.1  },
      "seats": [
        { "seat_name": "A1", "seat_id": "A1", "seat_pos": { "x": -0.65, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A2", "seat_id": "A2", "seat_pos": { "x": -0.4,  "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A3", "seat_id": "A3", "seat_pos": { "x": -0.15, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A4", "seat_id": "A4", "seat_pos": { "x":  0.15, "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A5", "seat_id": "A5", "seat_pos": { "x":  0.4,  "y": -0.75 }, "seat_rotation": 0 },
        { "seat_name": "A6", "seat_id": "A6", "seat_pos": { "x":  0.65, "y": -0.75 }, "seat_rotation": 0 }
      ]
    },
    {
      "zone_name": "Standard",
      "zone_type": "sitting",
      "color": "#0D9488",
      "corner1": { "x": -0.85, "y": -0.05 },
      "corner2": { "x":  0.85, "y": -0.05 },
      "corner3": { "x":  0.85, "y":  0.9  },
      "corner4": { "x": -0.85, "y":  0.9  },
      "seats": [
        { "seat_name": "B1", "seat_id": "B1", "seat_pos": { "x": -0.65, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "B2", "seat_id": "B2", "seat_pos": { "x": -0.4,  "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "B3", "seat_id": "B3", "seat_pos": { "x": -0.15, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "B4", "seat_id": "B4", "seat_pos": { "x":  0.15, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "B5", "seat_id": "B5", "seat_pos": { "x":  0.4,  "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "B6", "seat_id": "B6", "seat_pos": { "x":  0.65, "y": 0.1  }, "seat_rotation": 0 },
        { "seat_name": "C1", "seat_id": "C1", "seat_pos": { "x": -0.65, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "C2", "seat_id": "C2", "seat_pos": { "x": -0.4,  "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "C3", "seat_id": "C3", "seat_pos": { "x": -0.15, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "C4", "seat_id": "C4", "seat_pos": { "x":  0.15, "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "C5", "seat_id": "C5", "seat_pos": { "x":  0.4,  "y": 0.35 }, "seat_rotation": 0 },
        { "seat_name": "C6", "seat_id": "C6", "seat_pos": { "x":  0.65, "y": 0.35 }, "seat_rotation": 0 }
      ]
    }
  ],
  "seat_size": 22
}'::jsonb
WHERE name = 'Dreamplex Điện Biên Phủ';


-- Gigamall Thủ Đức → Standing - VIP + GA (exhibition/open space)
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
  "zones": [
    {
      "zone_name": "VIP",
      "zone_type": "standing",
      "color": "#7C3AED",
      "corner1": { "x": -0.9, "y": -0.8 },
      "corner2": { "x":  0.9, "y": -0.8 },
      "corner3": { "x":  0.9, "y": -0.2 },
      "corner4": { "x": -0.9, "y": -0.2 },
      "seats": []
    },
    {
      "zone_name": "GA",
      "zone_type": "standing",
      "color": "#5B21B6",
      "corner1": { "x": -0.9, "y": -0.15 },
      "corner2": { "x":  0.9, "y": -0.15 },
      "corner3": { "x":  0.9, "y":  0.9  },
      "corner4": { "x": -0.9, "y":  0.9  },
      "seats": []
    }
  ],
  "seat_size": 20
}'::jsonb
WHERE name = 'Gigamall Thủ Đức';


-- Sân Lễ Hội Đền Vua Đinh - Vua Lê → Standing - 3 Zones (large outdoor festival)
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
  "zones": [
    {
      "zone_name": "VVIP",
      "zone_type": "standing",
      "color": "#DC2626",
      "corner1": { "x": -0.9, "y": -0.8  },
      "corner2": { "x":  0.9, "y": -0.8  },
      "corner3": { "x":  0.9, "y": -0.35 },
      "corner4": { "x": -0.9, "y": -0.35 },
      "seats": []
    },
    {
      "zone_name": "VIP",
      "zone_type": "standing",
      "color": "#F59E0B",
      "corner1": { "x": -0.9, "y": -0.3 },
      "corner2": { "x":  0.9, "y": -0.3 },
      "corner3": { "x":  0.9, "y":  0.2 },
      "corner4": { "x": -0.9, "y":  0.2 },
      "seats": []
    },
    {
      "zone_name": "GA Standing",
      "zone_type": "standing",
      "color": "#2563EB",
      "corner1": { "x": -0.9, "y":  0.25 },
      "corner2": { "x":  0.9, "y":  0.25 },
      "corner3": { "x":  0.9, "y":  0.9  },
      "corner4": { "x": -0.9, "y":  0.9  },
      "seats": []
    }
  ],
  "seat_size": 20
}'::jsonb
WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê';


-- Công viên Yên Sở → Standing - VIP + GA (outdoor park concert)
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
  "zones": [
    {
      "zone_name": "VIP",
      "zone_type": "standing",
      "color": "#F59E0B",
      "corner1": { "x": -0.9, "y": -0.8 },
      "corner2": { "x":  0.9, "y": -0.8 },
      "corner3": { "x":  0.9, "y": -0.2 },
      "corner4": { "x": -0.9, "y": -0.2 },
      "seats": []
    },
    {
      "zone_name": "GA",
      "zone_type": "standing",
      "color": "#2563EB",
      "corner1": { "x": -0.9, "y": -0.15 },
      "corner2": { "x":  0.9, "y": -0.15 },
      "corner3": { "x":  0.9, "y":  0.9  },
      "corner4": { "x": -0.9, "y":  0.9  },
      "seats": []
    }
  ],
  "seat_size": 20
}'::jsonb
WHERE name = 'Công viên Yên Sở';


-- Khu đô thị Vạn Phúc → Standing - 5 Zones Festival (large EDM festival)
UPDATE public.venues
SET layout = '{
  "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
  "zones": [
    {
      "zone_name": "SVIP",
      "zone_type": "standing",
      "color": "#DC2626",
      "corner1": { "x": -0.4, "y": -0.8 },
      "corner2": { "x":  0.4, "y": -0.8 },
      "corner3": { "x":  0.4, "y": -0.2 },
      "corner4": { "x": -0.4, "y": -0.2 },
      "seats": []
    },
    {
      "zone_name": "VIP Left",
      "zone_type": "standing",
      "color": "#F59E0B",
      "corner1": { "x": -0.9,  "y": -0.8 },
      "corner2": { "x": -0.45, "y": -0.8 },
      "corner3": { "x": -0.45, "y": -0.2 },
      "corner4": { "x": -0.9,  "y": -0.2 },
      "seats": []
    },
    {
      "zone_name": "VIP Right",
      "zone_type": "standing",
      "color": "#F59E0B",
      "corner1": { "x":  0.45, "y": -0.8 },
      "corner2": { "x":  0.9,  "y": -0.8 },
      "corner3": { "x":  0.9,  "y": -0.2 },
      "corner4": { "x":  0.45, "y": -0.2 },
      "seats": []
    },
    {
      "zone_name": "GA Left",
      "zone_type": "standing",
      "color": "#2563EB",
      "corner1": { "x": -0.9,  "y": -0.15 },
      "corner2": { "x": -0.05, "y": -0.15 },
      "corner3": { "x": -0.05, "y":  0.9  },
      "corner4": { "x": -0.9,  "y":  0.9  },
      "seats": []
    },
    {
      "zone_name": "GA Early Bird",
      "zone_type": "standing",
      "color": "#1D4ED8",
      "corner1": { "x":  0.05, "y": -0.15 },
      "corner2": { "x":  0.9,  "y": -0.15 },
      "corner3": { "x":  0.9,  "y":  0.9  },
      "corner4": { "x":  0.05, "y":  0.9  },
      "seats": []
    }
  ],
  "seat_size": 20
}'::jsonb
WHERE name = 'Khu đô thị Vạn Phúc';
