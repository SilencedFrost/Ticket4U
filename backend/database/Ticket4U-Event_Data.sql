-- data truncation
TRUNCATE TABLE public.categories CASCADE;
TRUNCATE TABLE public.venues CASCADE;

--1. Table: Categories
INSERT INTO public.categories (id, name) VALUES
(1, 'Âm nhạc'), 
(2, 'Hội thảo'), 
(3, 'Sân khấu - Nghệ thuật'), 
(4, 'Thể thao'), 
(5, 'Giải trí về đêm');

SELECT setval('public.categories_id_seq', 5);

--2. Table: Venues
INSERT INTO public.venues (id, name, address_line, latitude, longitude, layout, image_url, created_at, updated_at) VALUES
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
INSERT INTO public.events (
    id, name, organizer_id, address_line,
    status, banner_url, created_at,
    about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url
) VALUES
      (uuidv7(), 'Hà Anh Tuấn: Chân Trời Rực Rỡ', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình',
       'PREMIERE', 'https://salt.tkbcdn.com/ts/ds/25/e6/b4/d79786df1e38c39beabe33c462cc381e.jpg', NOW(),
       '<p>Đêm nhạc huyền thoại với giọng ca đầy cảm xúc của Hà Anh Tuấn, cùng những giai điệu bất hủ mang đến không gian âm nhạc đầy mê hoặc. Hãy hòa mình vào không gian mộng mơ giữa đất trời Ninh Bình.</p>',
       '<p>A legendary music night featuring the emotional voice of Ha Anh Tuan with timeless melodies. Immerse yourself in the dreamy atmosphere of Ninh Binh.</p>',
       '<p>Cấm trẻ em dưới 6 tuổi. Không ảnh hưởng đến khán giả khác.</p>',
       '<p>Không hoàn tiền sau khi mua. Chỉ hỗ trợ đổi vé trong trường hợp đặc biệt.</p>',
       'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),

      (uuidv7(), 'VBA 2025: Saigon Heat vs Hanoi Buffaloes', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'CIS Arena, Quận 7, TP. Hồ Chí Minh',
       'FINISHED', 'https://cdn.nextix.cloud/nextix/81/saigonheat_1_2996fb89aa.png?updated_at=2024-06-20T06:55:12.533Z', NOW(),
       '<p>Trận đối đầu kịch tính của giải bóng rổ chuyên nghiệp Việt Nam. Hãy cùng tiếp thêm sức mạnh cho các chiến binh Saigon Heat.</p>',
       '<p>Intense match of the Vietnam Basketball Association. Support the Saigon Heat warriors in this crucial matchup.</p>',
       '<p>Không gây rối, không ảnh hưởng đến người khác.</p>',
       '<p>Hoàn vé theo quy định của BTC trong trường hợp trận đấu bị hủy.</p>',
       'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'),

      (uuidv7(), 'Những Thành Phố Mơ Màng - Summer Tour', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội',
       'PREMIERE', 'https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg', NOW(),
       '<p>Hành trình âm nhạc indie đầy mộng mơ với sự góp mặt của Đen Vâu, Chillies, và Vũ. Hòa mình vào không gian âm nhạc đầy màu sắc giữa thiên nhiên xanh mát.</p>',
       '<p>A dreamy indie music journey featuring Den Vau, Chillies, and Vu. Immerse in colorful music surrounded by lush greenery.</p>',
       '<p>Vui lòng mang theo CCCD khi check-in. Trang phục thoải mái.</p>',
       '<p>Không hoàn trả vé sau khi mua.</p>',
       'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

      (uuidv7(), 'Mây Lang Thang: Đêm Nhạc Trịnh', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Mây Lang Thang, Phường 4, Thành phố Đà Lạt, Tỉnh Lâm Đồng',
       'PREMIERE', 'https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg', NOW(),
       '<p>Thưởng thức những giai điệu nhạc Trịnh bất hủ giữa khung cảnh đồi thông mộng mơ của Đà Lạt. Một đêm nhạc đầy cảm xúc và lãng mạn.</p>',
       '<p>Enjoy timeless Trinh melodies amidst the dreamy pine hills of Dalat. An emotional and romantic music night.</p>',
       '<p>Nên mang theo áo khoác ấm do thời tiết Đà Lạt se lạnh.</p>',
       '<p>Không hoàn tiền vé.</p>',
       'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

      (uuidv7(), 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Nhà Hát Bến Thành, Quận 1, TP. Hồ Chí Minh',
       'PREMIERE', 'https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png', NOW(),
       '<p>Chương trình kịch thiếu nhi được mong chờ nhất hè này. Vở diễn huyền thoại với những câu chuyện cổ tích đầy màu sắc và ý nghĩa.</p>',
       '<p>The most anticipated children''s theater show this summer. A legendary performance with colorful and meaningful fairy tales.</p>',
       '<p>Mỗi vé dành cho một người, trẻ em cần có người lớn đi kèm.</p>',
       '<p>Không hoàn tiền.</p>',
       'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

      (uuidv7(), 'Ravolution Music Festival: Unite', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh',
       'PREMIERE', 'https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg', NOW(),
       '<p>Lễ hội âm nhạc điện tử quốc tế lớn nhất khu vực với dàn DJ Top 100 thế giới. Hãy sẵn sàng cho một đêm bùng nổ không giới hạn.</p>',
       '<p>The biggest international EDM festival in the region featuring Top 100 DJs worldwide. Get ready for an unlimited explosive night.</p>',
       '<p>Trang phục tự do, không mang chất cấm. Cấm trẻ em dưới 16 tuổi.</p>',
       '<p>Hoàn tiền 50% trước 7 ngày diễn ra.</p>',
       'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'),

      (uuidv7(), 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Cà phê Tinh Tế, Quận 3, TP. Hồ Chí Minh',
       'PREMIERE', 'https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg', NOW(),
       '<p>Đêm hài độc thoại giải tỏa căng thẳng cuối tuần với những câu chuyện đời thường đầy hài hước. Cười thoải mái và quên đi mệt mỏi.</p>',
       '<p>A stand-up comedy night to unwind your weekend stress with hilarious everyday stories. Laugh freely and forget your worries.</p>',
       '<p>Vui lòng không quay phim trong buổi diễn.</p>',
       '<p>Không hoàn trả vé.</p>',
       'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

      (uuidv7(), 'Vietnam Tech Summit 2025', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'GEM Center, Quận 1, TP. Hồ Chí Minh',
       'PREMIERE', 'https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg', NOW(),
       '<p>Hội thảo công nghệ lớn nhất năm với sự tham gia của các chuyên gia hàng đầu trong ngành AI, Cloud Computing và Blockchain.</p>',
       '<p>The biggest technology conference of the year featuring top experts in AI, Cloud Computing, and Blockchain.</p>',
       '<p>Khuyến khích mang theo laptop cá nhân.</p>',
       '<p>Hoàn tiền 50% nếu hủy trước 5 ngày.</p>',
       'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),

      (uuidv7(), 'Workshop: Marketing 0 Đồng cho Startup', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Dreamplex Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh',
       'PREMIERE', 'https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png', NOW(),
       '<p>Chia sẻ bí quyết tăng trưởng không cần ngân sách lớn. Học cách tận dụng organic marketing và growth hacking cho startup.</p>',
       '<p>Sharing growth secrets without a big budget. Learn to leverage organic marketing and growth hacking for your startup.</p>',
       '<p>Khuyến khích mang theo laptop cá nhân.</p>',
       '<p>Hoàn tiền 50% nếu hủy trước 3 ngày.</p>',
       'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),

      (uuidv7(), 'Van Gogh Art Lighting Experience', '019cc97a-3385-7821-94ae-c48e8cedd3ff',
       'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh',
       'PREMIERE', 'https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990', NOW(),
       '<p>Triển lãm nghệ thuật tương tác đa giác quan với công nghệ ánh sáng hiện đại. Đắm chìm trong thế giới tranh của Van Gogh.</p>',
       '<p>Multi-sensory interactive art exhibition with modern lighting technology. Immerse yourself in Van Gogh''s world of paintings.</p>',
       '<p>Mỗi lượt tham quan tối đa 60 phút.</p>',
       '<p>Không hỗ trợ hoàn tiền.</p>',
       'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg');

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
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'VVIP', false, 50, 2, 2500000.00,
       'Khu vực VIP cao cấp gần sân khấu, ghế ngồi hạng sang, phục vụ đồ uống cao cấp.',
       'Premium VIP area near stage, luxury seating, premium beverage service.',
       'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
       '["Vòng tay check-in VIP", "Goodie bag", "Meet & Greet"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'GA Standing', true, 800, 4, 650000.00,
       'Khu vực đứng tự do gần sân khấu, trải nghiệm âm nhạc bùng nổ cùng đám đông.',
       'Standing area near stage for an explosive music experience with the crowd.',
       NULL, '["Vòng tay check-in", "Nước suối"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- VBA Saigon Heat
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'Courtside VIP', false, 50, 2, 2500000.00,
       'Ghế ngồi sát sàn đấu, cảm nhận từng bước chạy của cầu thủ.',
       'Courtside seats to feel every move of the players.',
       'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
       '["Áo đấu phiên bản giới hạn", "F&B phục vụ tại chỗ"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'Standard Bleachers', false, 800, 4, 150000.00,
       'Khu vực khán đài tiêu chuẩn với tầm nhìn tốt.',
       'Standard bleachers with good view.',
       NULL, NULL, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Những Thành Phố Mơ Màng
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'Cư Dân VIP', false, 200, 2, 1500000.00,
       'Đặc quyền cư dân VIP với khu vực nghỉ ngơi riêng, quà tặng độc quyền.',
       'VIP resident privileges with private lounge and exclusive gifts.',
       'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
       '["Bộ quà tặng Cư Dân", "Lối đi ưu tiên", "Nước uống miễn phí"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'GA Thường', true, 2000, 4, 650000.00,
       'Khu vực tự do dành cho các cư dân yêu âm nhạc.',
       'General admission area for music-loving residents.',
       NULL, '["Vòng tay vải", "Sticker"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour';

-- Mây Lang Thang
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'Khu VVIP (Gần ca sĩ)', false, 50, 2, 1800000.00,
       'Vị trí đẹp nhất để tương tác cùng nghệ sĩ, thưởng thức nhạc Trịnh bất hủ.',
       'The best spot to interact with the artist, enjoying timeless Trinh melodies.',
       NULL, '["Một phần nước tự chọn", "Đĩa CD nhạc Trịnh"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'Khu Khán Đài', false, 200, 4, 800000.00,
       'Khu vực ngồi thoải mái với tầm nhìn đẹp, thưởng thức đêm nhạc trong không gian lãng mạn.',
       'Comfortable seating with beautiful view, enjoy the music night in romantic atmosphere.',
       NULL, '["Nước uống", "Chăn len"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Kịch Idecaf
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'Khu A (Lầu 1)', false, 300, 4, 350000.00,
       'Khu vực lầu 1 gần sân khấu nhất, phù hợp cho trẻ em.',
       'Floor 1 area closest to the stage, perfect for children.',
       NULL, NULL, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'Khu B (Lầu 2)', false, 200, 4, 250000.00,
       'Khu vực lầu 2 với tầm nhìn toàn cảnh, giá cả phải chăng.',
       'Floor 2 area with panoramic view, affordable price.',
       NULL, NULL, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- Ravolution
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'SVIP Deck', false, 100, 2, 4500000.00,
       'Tận hưởng lễ hội trên khán đài cao cấp với không gian riêng tư.',
       'Enjoy the festival from the premium deck with private space.',
       NULL, '["Private Bar", "Quà tặng từ nhà tài trợ", "Lối đi VIP"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Ravolution Music Festival: Unite';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, gift_image_url, perks, created_at)
SELECT uuidv7(), es.id, 'GA Early Bird', true, 1500, 4, 850000.00,
       'Khu vực tự do cho những người đến sớm, hòa mình vào không khí EDM bùng nổ.',
       'General admission for early arrivals, immerse in explosive EDM atmosphere.',
       NULL, '["Vòng tay vải"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Ravolution Music Festival: Unite';

-- Saigon Tếu
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, created_at)
SELECT uuidv7(), es.id, 'Khu ngồi chính', false, 300, 4, 0.00, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Vietnam Tech Summit
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, created_at)
SELECT uuidv7(), es.id, 'Hội trường chính', false, 500, 4, 0.00, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Vietnam Tech Summit 2025';

-- Workshop Marketing
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, created_at)
SELECT uuidv7(), es.id, 'Khu học viên', false, 100, 4, 0.00, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Workshop: Marketing 0 Đồng cho Startup';

-- Van Gogh
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), es.id, 'VIP (Kèm quà tặng)', false, 200, 2, 1200000.00,
       'Trải nghiệm nghệ thuật đa giác quan không giới hạn.',
       'Unlimited multi-sensory art experience.',
       '["Sổ tay Van Gogh", "Ưu tiên chụp ảnh tại khu vực đặc biệt"]'::jsonb, NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Van Gogh Art Lighting Experience';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, purchase_limit, price, description_vi, description_en, created_at)
SELECT uuidv7(), es.id, 'Standard', false, 1000, 4, 650000.00,
       'Khu vực tham quan tiêu chuẩn.',
       'Standard viewing area.', NOW()
FROM public.event_sessions es JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Van Gogh Art Lighting Experience';

--5. Table: Seats 
-- VVIP - Hà Anh Tuấn (5 rows x 10 cols = 50)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 5) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'VVIP' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- Courtside VIP - VBA (5 rows x 10 cols = 50)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 5) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'Courtside VIP' AND e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Standard Bleachers - VBA (20 rows x 20 cols = 400)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 20) r CROSS JOIN generate_series(1, 20) c
WHERE z.name = 'Standard Bleachers' AND e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Cư Dân VIP - Những Thành Phố Mơ Màng (15 rows x 14 cols = 210)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 15) r CROSS JOIN generate_series(1, 14) c
WHERE z.name = 'Cư Dân VIP' AND e.name = 'Những Thành Phố Mơ Màng - Summer Tour';

-- Khu VVIP - Mây Lang Thang (5 rows x 10 cols = 50)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 5) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'Khu VVIP (Gần ca sĩ)' AND e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Khu Khán Đài - Mây Lang Thang (20 rows x 10 cols = 200)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 20) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'Khu Khán Đài' AND e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Khu A Lầu 1 - Kịch Idecaf (20 rows x 15 cols = 300)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 20) r CROSS JOIN generate_series(1, 15) c
WHERE z.name = 'Khu A (Lầu 1)' AND e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- Khu B Lầu 2 - Kịch Idecaf (20 rows x 10 cols = 200)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 20) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'Khu B (Lầu 2)' AND e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- SVIP Deck - Ravolution (10 rows x 10 cols = 100)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 10) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'SVIP Deck' AND e.name = 'Ravolution Music Festival: Unite';

-- Khu ngồi chính - Saigon Tếu (20 rows x 10 cols = 200)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 20) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'Khu ngồi chính' AND e.name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Hội trường chính - Vietnam Tech Summit (25 rows x 20 cols = 500)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 25) r CROSS JOIN generate_series(1, 20) c
WHERE z.name = 'Hội trường chính' AND e.name = 'Vietnam Tech Summit 2025';

-- Khu học viên - Workshop (10 rows x 10 cols = 100)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, 'Ghế ' || chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 10) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'Khu học viên' AND e.name = 'Workshop: Marketing 0 Đồng cho Startup';

-- VIP Kèm quà tặng - Van Gogh (20 rows x 10 cols = 200)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 20) r CROSS JOIN generate_series(1, 10) c
WHERE z.name = 'VIP (Kèm quà tặng)' AND e.name = 'Van Gogh Art Lighting Experience';

-- Standard - Van Gogh (25 rows x 20 cols = 500)
INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code)
SELECT uuidv7(), z.id, chr(64 + r) || c, chr(64 + r), c::text, chr(64 + r) || c
FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id
                    CROSS JOIN generate_series(1, 25) r CROSS JOIN generate_series(1, 20) c
WHERE z.name = 'Standard' AND e.name = 'Van Gogh Art Lighting Experience';


--6. Venues Layout
-- ── Venue Layouts (Fixed) ──────────────────────────────────
-- All venues use floors format for consistency
-- Zone names match zoneLinks keys exactly

-- CIS Arena
-- zoneLinks: VIP Floor, Lower Bowl Left, Lower Bowl Right, GA Pit, Lower Bowl Back, Upper Bowl
UPDATE public.venues
SET layout = '{
  "floors": [
    {
      "floor_name": "Floor Level",
      "floor_order": 1,
      "global_seat_size": 10,
      "stage": { "x1": -0.22, "y1": -0.96, "x2": 0.22, "y2": -0.8 },
      "stage_shapes": [],
      "zones": [
        {
          "zone_name": "VIP Floor",
          "zone_type": "standing",
          "color": "#f59e0b",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.28, "y": -0.78 },
          "corner2": { "x":  0.28, "y": -0.78 },
          "corner3": { "x":  0.28, "y": -0.3  },
          "corner4": { "x": -0.28, "y": -0.3  }
        },
        {
          "zone_name": "Lower Bowl Left",
          "zone_type": "standing",
          "color": "#6366f1",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.97, "y": -0.78 },
          "corner2": { "x": -0.3,  "y": -0.78 },
          "corner3": { "x": -0.3,  "y":  0.3  },
          "corner4": { "x": -0.97, "y":  0.3  }
        },
        {
          "zone_name": "Lower Bowl Right",
          "zone_type": "standing",
          "color": "#6366f1",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x":  0.3,  "y": -0.78 },
          "corner2": { "x":  0.97, "y": -0.78 },
          "corner3": { "x":  0.97, "y":  0.3  },
          "corner4": { "x":  0.3,  "y":  0.3  }
        },
        {
          "zone_name": "GA Pit",
          "zone_type": "standing",
          "color": "#22c55e",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.28, "y": -0.28 },
          "corner2": { "x":  0.28, "y": -0.28 },
          "corner3": { "x":  0.28, "y":  0.3  },
          "corner4": { "x": -0.28, "y":  0.3  }
        },
        {
          "zone_name": "Lower Bowl Back",
          "zone_type": "standing",
          "color": "#3b82f6",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.97, "y":  0.32 },
          "corner2": { "x":  0.97, "y":  0.32 },
          "corner3": { "x":  0.97, "y":  0.72 },
          "corner4": { "x": -0.97, "y":  0.72 }
        }
      ]
    },
    {
      "floor_name": "Upper Bowl",
      "floor_order": 2,
      "global_seat_size": 9,
      "stage": { "x1": -0.22, "y1": -0.96, "x2": 0.22, "y2": -0.8 },
      "stage_shapes": [],
      "zones": [
        {
          "zone_name": "Upper Bowl",
          "zone_type": "standing",
          "color": "#8b5cf6",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.97, "y": -0.6 },
          "corner2": { "x":  0.97, "y": -0.6 },
          "corner3": { "x":  0.97, "y":  0.6 },
          "corner4": { "x": -0.97, "y":  0.6 }
        }
      ]
    }
  ]
}'::jsonb
WHERE name = 'CIS Arena';

-- Nhà Hát Bến Thành
-- zoneLinks: Orchestra, Box Left, Box Right, Mezzanine, Balcony
UPDATE public.venues
SET layout = '{
  "floors": [
    {
      "floor_name": "Orchestra",
      "floor_order": 1,
      "global_seat_size": 13,
      "stage": { "x1": -0.6, "y1": -0.96, "x2": 0.6, "y2": -0.72 },
      "stage_shapes": [],
      "zones": [
        {
          "zone_name": "Orchestra",
          "zone_type": "sitting",
          "color": "#f59e0b",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.7, "y": -0.7 },
          "corner2": { "x":  0.7, "y": -0.7 },
          "corner3": { "x":  0.7, "y":  0.3 },
          "corner4": { "x": -0.7, "y":  0.3 }
        },
        {
          "zone_name": "Box Left",
          "zone_type": "sitting",
          "color": "#ec4899",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.97, "y": -0.7  },
          "corner2": { "x": -0.72, "y": -0.7  },
          "corner3": { "x": -0.72, "y":  0.1  },
          "corner4": { "x": -0.97, "y":  0.1  }
        },
        {
          "zone_name": "Box Right",
          "zone_type": "sitting",
          "color": "#ec4899",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x":  0.72, "y": -0.7  },
          "corner2": { "x":  0.97, "y": -0.7  },
          "corner3": { "x":  0.97, "y":  0.1  },
          "corner4": { "x":  0.72, "y":  0.1  }
        },
        {
          "zone_name": "Mezzanine",
          "zone_type": "sitting",
          "color": "#6366f1",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.7, "y":  0.32 },
          "corner2": { "x":  0.7, "y":  0.32 },
          "corner3": { "x":  0.7, "y":  0.72 },
          "corner4": { "x": -0.7, "y":  0.72 }
        }
      ]
    },
    {
      "floor_name": "Balcony",
      "floor_order": 2,
      "global_seat_size": 11,
      "stage": { "x1": -0.6, "y1": -0.96, "x2": 0.6, "y2": -0.72 },
      "stage_shapes": [],
      "zones": [
        {
          "zone_name": "Balcony",
          "zone_type": "sitting",
          "color": "#3b82f6",
          "rotation": 0, "accessible": true, "shape_type": "rect",
          "corner1": { "x": -0.85, "y": -0.5 },
          "corner2": { "x":  0.85, "y": -0.5 },
          "corner3": { "x":  0.85, "y":  0.5 },
          "corner4": { "x": -0.85, "y":  0.5 }
        }
      ]
    }
  ]
}'::jsonb
WHERE name = 'Nhà Hát Bến Thành';

-- Mây in The Nest
-- zoneLinks: VIP, Zone A, Zone B, Standing
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Main Area",
    "floor_order": 1,
    "global_seat_size": 16,
    "stage": { "x1": -0.4, "y1": -0.96, "x2": 0.4, "y2": -0.74 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "VIP",
        "zone_type": "sitting",
        "color": "#f59e0b",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.4,  "y": -0.72 },
        "corner2": { "x":  0.4,  "y": -0.72 },
        "corner3": { "x":  0.4,  "y": -0.1  },
        "corner4": { "x": -0.4,  "y": -0.1  }
      },
      {
        "zone_name": "Zone A",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y": -0.72 },
        "corner2": { "x": -0.42, "y": -0.72 },
        "corner3": { "x": -0.42, "y":  0.4  },
        "corner4": { "x": -0.97, "y":  0.4  }
      },
      {
        "zone_name": "Zone B",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x":  0.42, "y": -0.72 },
        "corner2": { "x":  0.97, "y": -0.72 },
        "corner3": { "x":  0.97, "y":  0.4  },
        "corner4": { "x":  0.42, "y":  0.4  }
      },
      {
        "zone_name": "Standing",
        "zone_type": "standing",
        "color": "#22c55e",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y":  0.42 },
        "corner2": { "x":  0.97, "y":  0.42 },
        "corner3": { "x":  0.97, "y":  0.95 },
        "corner4": { "x": -0.97, "y":  0.95 }
      }
    ]
  }]
}'::jsonb
WHERE name = 'Mây in The Nest';

-- GEM Center
-- zoneLinks: VIP, Zone A, Zone B, Zone C, Standing
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Main Hall",
    "floor_order": 1,
    "global_seat_size": 12,
    "stage": { "x1": -0.28, "y1": -0.96, "x2": 0.28, "y2": -0.78 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "VIP",
        "zone_type": "sitting",
        "color": "#f59e0b",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.32, "y": -0.76 },
        "corner2": { "x":  0.32, "y": -0.76 },
        "corner3": { "x":  0.32, "y": -0.38 },
        "corner4": { "x": -0.32, "y": -0.38 }
      },
      {
        "zone_name": "Zone A",
        "zone_type": "sitting",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.95, "y": -0.76 },
        "corner2": { "x": -0.34, "y": -0.76 },
        "corner3": { "x": -0.34, "y":  0.2  },
        "corner4": { "x": -0.95, "y":  0.2  }
      },
      {
        "zone_name": "Zone B",
        "zone_type": "sitting",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x":  0.34, "y": -0.76 },
        "corner2": { "x":  0.95, "y": -0.76 },
        "corner3": { "x":  0.95, "y":  0.2  },
        "corner4": { "x":  0.34, "y":  0.2  }
      },
      {
        "zone_name": "Zone C",
        "zone_type": "sitting",
        "color": "#3b82f6",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.95, "y":  0.22 },
        "corner2": { "x":  0.95, "y":  0.22 },
        "corner3": { "x":  0.95, "y":  0.62 },
        "corner4": { "x": -0.95, "y":  0.62 }
      },
      {
        "zone_name": "Standing",
        "zone_type": "standing",
        "color": "#22c55e",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.95, "y":  0.64 },
        "corner2": { "x":  0.95, "y":  0.64 },
        "corner3": { "x":  0.95, "y":  0.95 },
        "corner4": { "x": -0.95, "y":  0.95 }
      }
    ]
  }]
}'::jsonb
WHERE name = 'GEM Center';

-- Tinh Tế Cafe
-- Saigon Tếu uses custom layout (not venue mode) so zone names don't matter
-- Keeping as-is with Seated + Standing for completeness
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Main Floor",
    "floor_order": 1,
    "global_seat_size": 18,
    "stage": { "x1": -0.5, "y1": -0.96, "x2": 0.5, "y2": -0.68 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "Seated",
        "zone_type": "sitting",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.85, "y": -0.65 },
        "corner2": { "x":  0.85, "y": -0.65 },
        "corner3": { "x":  0.85, "y":  0.3  },
        "corner4": { "x": -0.85, "y":  0.3  }
      },
      {
        "zone_name": "Standing",
        "zone_type": "standing",
        "color": "#22c55e",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.85, "y":  0.35 },
        "corner2": { "x":  0.85, "y":  0.35 },
        "corner3": { "x":  0.85, "y":  0.92 },
        "corner4": { "x": -0.85, "y":  0.92 }
      }
    ]
  }]
}'::jsonb
WHERE name = 'Tinh Tế Cafe';

-- Dreamplex Điện Biên Phủ
-- zoneLinks: Zone A, Zone B, Standing
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Event Space",
    "floor_order": 1,
    "global_seat_size": 14,
    "stage": { "x1": -0.35, "y1": -0.96, "x2": 0.35, "y2": -0.76 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "Zone A",
        "zone_type": "sitting",
        "color": "#f59e0b",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.85, "y": -0.74 },
        "corner2": { "x":  0.85, "y": -0.74 },
        "corner3": { "x":  0.85, "y": -0.08 },
        "corner4": { "x": -0.85, "y": -0.08 }
      },
      {
        "zone_name": "Zone B",
        "zone_type": "sitting",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.85, "y": -0.06 },
        "corner2": { "x":  0.85, "y": -0.06 },
        "corner3": { "x":  0.85, "y":  0.55 },
        "corner4": { "x": -0.85, "y":  0.55 }
      },
      {
        "zone_name": "Standing",
        "zone_type": "standing",
        "color": "#22c55e",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.85, "y":  0.57 },
        "corner2": { "x":  0.85, "y":  0.57 },
        "corner3": { "x":  0.85, "y":  0.95 },
        "corner4": { "x": -0.85, "y":  0.95 }
      }
    ]
  }]
}'::jsonb
WHERE name = 'Dreamplex Điện Biên Phủ';

-- Gigamall Thủ Đức
-- zoneLinks: VIP, GA
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Exhibition Space",
    "floor_order": 1,
    "global_seat_size": 20,
    "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "VIP",
        "zone_type": "standing",
        "color": "#7c3aed",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.9, "y": -0.8 },
        "corner2": { "x":  0.9, "y": -0.8 },
        "corner3": { "x":  0.9, "y": -0.2 },
        "corner4": { "x": -0.9, "y": -0.2 }
      },
      {
        "zone_name": "GA",
        "zone_type": "standing",
        "color": "#5b21b6",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.9, "y": -0.15 },
        "corner2": { "x":  0.9, "y": -0.15 },
        "corner3": { "x":  0.9, "y":  0.9  },
        "corner4": { "x": -0.9, "y":  0.9  }
      }
    ]
  }]
}'::jsonb
WHERE name = 'Gigamall Thủ Đức';

-- Sân Lễ Hội Đền Vua Đinh - Vua Lê
-- zoneLinks: VIP, Zone A, Zone B, GA
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Grounds",
    "floor_order": 1,
    "global_seat_size": 14,
    "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "VIP",
        "zone_type": "standing",
        "color": "#f59e0b",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.5,  "y": -0.72 },
        "corner2": { "x":  0.5,  "y": -0.72 },
        "corner3": { "x":  0.5,  "y": -0.15 },
        "corner4": { "x": -0.5,  "y": -0.15 }
      },
      {
        "zone_name": "Zone A",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y": -0.72 },
        "corner2": { "x": -0.52, "y": -0.72 },
        "corner3": { "x": -0.52, "y":  0.5  },
        "corner4": { "x": -0.97, "y":  0.5  }
      },
      {
        "zone_name": "Zone B",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x":  0.52, "y": -0.72 },
        "corner2": { "x":  0.97, "y": -0.72 },
        "corner3": { "x":  0.97, "y":  0.5  },
        "corner4": { "x":  0.52, "y":  0.5  }
      },
      {
        "zone_name": "GA",
        "zone_type": "standing",
        "color": "#22c55e",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y":  0.52 },
        "corner2": { "x":  0.97, "y":  0.52 },
        "corner3": { "x":  0.97, "y":  0.97 },
        "corner4": { "x": -0.97, "y":  0.97 }
      }
    ]
  }]
}'::jsonb
WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê';

-- Công viên Yên Sở
-- zoneLinks: VIP, Zone A, Zone B, GA
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Main Stage Area",
    "floor_order": 1,
    "global_seat_size": 12,
    "stage": { "x1": -0.35, "y1": -1.0, "x2": 0.35, "y2": -0.76 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "VIP",
        "zone_type": "standing",
        "color": "#f59e0b",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.4,  "y": -0.74 },
        "corner2": { "x":  0.4,  "y": -0.74 },
        "corner3": { "x":  0.4,  "y": -0.2  },
        "corner4": { "x": -0.4,  "y": -0.2  }
      },
      {
        "zone_name": "Zone A",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y": -0.74 },
        "corner2": { "x": -0.42, "y": -0.74 },
        "corner3": { "x": -0.42, "y":  0.35 },
        "corner4": { "x": -0.97, "y":  0.35 }
      },
      {
        "zone_name": "Zone B",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x":  0.42, "y": -0.74 },
        "corner2": { "x":  0.97, "y": -0.74 },
        "corner3": { "x":  0.97, "y":  0.35 },
        "corner4": { "x":  0.42, "y":  0.35 }
      },
      {
        "zone_name": "GA",
        "zone_type": "standing",
        "color": "#22c55e",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y":  0.37 },
        "corner2": { "x":  0.97, "y":  0.37 },
        "corner3": { "x":  0.97, "y":  0.97 },
        "corner4": { "x": -0.97, "y":  0.97 }
      }
    ]
  }]
}'::jsonb
WHERE name = 'Công viên Yên Sở';

-- Khu đô thị Vạn Phúc
-- zoneLinks: VIP, Zone A, Zone B, GA (mapped to SVIP Deck and GA Early Bird)
UPDATE public.venues
SET layout = '{
  "floors": [{
    "floor_name": "Main Stage",
    "floor_order": 1,
    "global_seat_size": 12,
    "stage": { "x1": -0.32, "y1": -1.0, "x2": 0.32, "y2": -0.76 },
    "stage_shapes": [],
    "zones": [
      {
        "zone_name": "VIP",
        "zone_type": "standing",
        "color": "#f59e0b",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.38, "y": -0.74 },
        "corner2": { "x":  0.38, "y": -0.74 },
        "corner3": { "x":  0.38, "y": -0.18 },
        "corner4": { "x": -0.38, "y": -0.18 }
      },
      {
        "zone_name": "Zone A",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y": -0.74 },
        "corner2": { "x": -0.4,  "y": -0.74 },
        "corner3": { "x": -0.4,  "y":  0.4  },
        "corner4": { "x": -0.97, "y":  0.4  }
      },
      {
        "zone_name": "Zone B",
        "zone_type": "standing",
        "color": "#6366f1",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x":  0.4,  "y": -0.74 },
        "corner2": { "x":  0.97, "y": -0.74 },
        "corner3": { "x":  0.97, "y":  0.4  },
        "corner4": { "x":  0.4,  "y":  0.4  }
      },
      {
        "zone_name": "GA",
        "zone_type": "standing",
        "color": "#22c55e",
        "rotation": 0, "accessible": true, "shape_type": "rect",
        "corner1": { "x": -0.97, "y":  0.42 },
        "corner2": { "x":  0.97, "y":  0.42 },
        "corner3": { "x":  0.97, "y":  0.97 },
        "corner4": { "x": -0.97, "y":  0.97 }
      }
    ]
  }]
}'::jsonb
WHERE name = 'Khu đô thị Vạn Phúc';

-- 7. Layout links for events use Venue Layout
-- Hà Anh Tuấn: Chân Trời Rực Rỡ
-- Venue: Sân Lễ Hội Đền Vua Đinh - Vua Lê
-- Venue zones: VIP, Zone A, Zone B, GA
-- Event zones: VVIP, GA Standing
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ' AND z.name = 'VVIP'),
                'Zone A', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ' AND z.name = 'GA Standing'),
                'Zone B', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ' AND z.name = 'GA Standing'),
                'GA',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ' AND z.name = 'GA Standing')
                     )
             )
WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- VBA 2025: Saigon Heat vs Hanoi Buffaloes
-- Venue: CIS Arena
-- Venue zones: VIP Floor, Lower Bowl Left, Lower Bowl Right, GA Pit, Lower Bowl Back, Upper Bowl
-- Event zones: Courtside VIP, Standard Bleachers
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'CIS Arena'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP Floor',         (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes' AND z.name = 'Courtside VIP'),
                'Lower Bowl Left',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes' AND z.name = 'Standard Bleachers'),
                'Lower Bowl Right',  (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes' AND z.name = 'Standard Bleachers'),
                'GA Pit',            (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes' AND z.name = 'Standard Bleachers'),
                'Lower Bowl Back',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes' AND z.name = 'Standard Bleachers'),
                'Upper Bowl',        (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes' AND z.name = 'Standard Bleachers')
                     )
             )
WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Những Thành Phố Mơ Màng - Summer Tour
-- Venue: Công viên Yên Sở
-- Venue zones: VIP, Zone A, Zone B, GA
-- Event zones: Cư Dân VIP, GA Thường
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour' AND z.name = 'Cư Dân VIP'),
                'Zone A', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour' AND z.name = 'GA Thường'),
                'Zone B', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour' AND z.name = 'GA Thường'),
                'GA',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour' AND z.name = 'GA Thường')
                     )
             )
WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';

-- Mây Lang Thang: Đêm Nhạc Trịnh
-- Venue: Mây in The Nest
-- Venue zones: VIP, Zone A, Zone B, Standing
-- Event zones: Khu VVIP (Gần ca sĩ), Khu Khán Đài
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Mây in The Nest'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh' AND z.name = 'Khu VVIP (Gần ca sĩ)'),
                'Zone A',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh' AND z.name = 'Khu Khán Đài'),
                'Zone B',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh' AND z.name = 'Khu Khán Đài'),
                'Standing', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh' AND z.name = 'Khu Khán Đài')
                     )
             )
WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Kịch Idecaf: Ngày Xửa Ngày Xưa 35
-- Venue: Nhà Hát Bến Thành
-- Venue zones (floor 1): Orchestra, Box Left, Box Right, Mezzanine
-- Venue zones (floor 2): Balcony
-- Event zones: Khu A (Lầu 1), Khu B (Lầu 2)
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Nhà Hát Bến Thành'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'Orchestra',  (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35' AND z.name = 'Khu A (Lầu 1)'),
                'Box Left',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35' AND z.name = 'Khu A (Lầu 1)'),
                'Box Right',  (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35' AND z.name = 'Khu A (Lầu 1)'),
                'Mezzanine',  (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35' AND z.name = 'Khu A (Lầu 1)'),
                'Balcony',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35' AND z.name = 'Khu B (Lầu 2)')
                     )
             )
WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- Ravolution Music Festival: Unite
-- Venue: Khu đô thị Vạn Phúc
-- Venue zones: VIP, Zone A, Zone B, GA
-- Event zones: SVIP Deck, GA Early Bird
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Ravolution Music Festival: Unite' AND z.name = 'SVIP Deck'),
                'Zone A', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Ravolution Music Festival: Unite' AND z.name = 'GA Early Bird'),
                'Zone B', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Ravolution Music Festival: Unite' AND z.name = 'GA Early Bird'),
                'GA',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Ravolution Music Festival: Unite' AND z.name = 'GA Early Bird')
                     )
             )
WHERE name = 'Ravolution Music Festival: Unite';


-- Vietnam Tech Summit 2025
-- Venue: GEM Center
-- Venue zones: VIP, Zone A, Zone B, Zone C, Standing
-- Event zones: Hội trường chính
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'GEM Center'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',      (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Vietnam Tech Summit 2025' AND z.name = 'Hội trường chính'),
                'Zone A',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Vietnam Tech Summit 2025' AND z.name = 'Hội trường chính'),
                'Zone B',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Vietnam Tech Summit 2025' AND z.name = 'Hội trường chính'),
                'Zone C',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Vietnam Tech Summit 2025' AND z.name = 'Hội trường chính'),
                'Standing', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Vietnam Tech Summit 2025' AND z.name = 'Hội trường chính')
                     )
             )
WHERE name = 'Vietnam Tech Summit 2025';

-- Workshop: Marketing 0 Đồng cho Startup
-- Venue: Dreamplex Điện Biên Phủ
-- Venue zones: Zone A, Zone B, Standing
-- Event zones: Khu học viên
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Dreamplex Điện Biên Phủ'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'Zone A',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Workshop: Marketing 0 Đồng cho Startup' AND z.name = 'Khu học viên'),
                'Zone B',   (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Workshop: Marketing 0 Đồng cho Startup' AND z.name = 'Khu học viên'),
                'Standing', (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Workshop: Marketing 0 Đồng cho Startup' AND z.name = 'Khu học viên')
                     )
             )
WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';

-- Van Gogh Art Lighting Experience
-- Venue: Gigamall Thủ Đức (truncated in CSV but has VIP + GA standing zones)
-- Event zones: VIP (Kèm quà tặng), Standard
UPDATE public.events
SET layout = jsonb_build_object(
        'venueId',   (SELECT id FROM public.venues WHERE name = 'Gigamall Thủ Đức'),
        'venueMode', true,
        'zoneLinks', jsonb_build_object(
                'VIP',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Van Gogh Art Lighting Experience' AND z.name = 'VIP (Kèm quà tặng)'),
                'GA',     (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Van Gogh Art Lighting Experience' AND z.name = 'Standard')
                     )
             )
WHERE name = 'Van Gogh Art Lighting Experience';

-- 8. Custom Layout
-- Custom Layout: Saigon Tếu
-- Must run after zones are inserted
UPDATE public.events
SET layout = jsonb_build_object(
        'floors', jsonb_build_array(
                jsonb_build_object(
                        'floor_name',       'Main Floor',
                        'floor_order',      1,
                        'global_seat_size', 14,
                        'stage',            jsonb_build_object('x1', -0.267, 'x2', 0.267, 'y1', -0.923, 'y2', -0.754),
                        'stage_shapes',     jsonb_build_array(),
                        'zones',            jsonb_build_array(
                                jsonb_build_object(
                                        'zone_id',    (SELECT z.id FROM public.zones z JOIN public.event_sessions es ON z.session_id = es.id JOIN public.events e ON es.event_id = e.id WHERE e.name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi' AND z.name = 'Khu ngồi chính'),
                                        'zone_name',  'Khu ngồi chính',
                                        'zone_type',  'standing',
                                        'accessible', true,
                                        'shape_type', 'rect',
                                        'color',      '#6366f1',
                                        'rotation',   0,
                                        'corner1',    jsonb_build_object('x', -0.379, 'y', -0.679),
                                        'corner2',    jsonb_build_object('x',  0.379, 'y', -0.679),
                                        'corner3',    jsonb_build_object('x',  0.379, 'y',  0.325),
                                        'corner4',    jsonb_build_object('x', -0.379, 'y',  0.325)
                                )
                                            )
                )
                  )
             )
WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';


-- 8. Update venue_id for each events
UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Sân Lễ Hội Đền Vua Đinh - Vua Lê')
WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'CIS Arena')
WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Công viên Yên Sở')
WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Mây in The Nest')
WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Nhà Hát Bến Thành')
WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Khu đô thị Vạn Phúc')
WHERE name = 'Ravolution Music Festival: Unite';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'GEM Center')
WHERE name = 'Vietnam Tech Summit 2025';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Dreamplex Điện Biên Phủ')
WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';

UPDATE public.events SET venue_id = (SELECT id FROM public.venues WHERE name = 'Gigamall Thủ Đức')
WHERE name = 'Van Gogh Art Lighting Experience';
