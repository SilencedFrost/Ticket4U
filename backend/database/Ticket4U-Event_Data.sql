--1. Table: categories (Thể loại sự kiện)

TRUNCATE TABLE public.categories RESTART IDENTITY CASCADE;

INSERT INTO public.categories (name) VALUES 
('Âm nhạc (Concert)'), 
('Hội thảo (Workshop)'), 
('Sân khấu - Nghệ thuật'), 
('Thể thao'), 
('Nightlife');

--2. Table: Events
INSERT INTO public.events (id, name, organizer_id, category_id, address_line, start_date, end_date, status, banner_url, description) VALUES
(uuidv7(), 'Hà Anh Tuấn: Chân Trời Rực Rỡ', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Sân Lễ Hội Đền Hùng, Ninh Bình', NOW() + INTERVAL '30 days', NOW() + INTERVAL '30 days 4 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/25/e6/b4/d79786df1e38c39beabe33c462cc381e.jpg', 'Đêm nhạc huyền thoại với sự kết hợp của Kitaro'),
(uuidv7(), 'Những Thành Phố Mơ Màng - Summer Tour', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Công viên Yên Sở, Hà Nội', NOW() + INTERVAL '45 days', NOW() + INTERVAL '45 days 6 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg', 'Show diễn Indie với sự góp mặt của Đen Vâu, Chillies, Vũ.'),
(uuidv7(), 'Mây Lang Thang: Đêm Nhạc Trịnh', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Mây Lang Thang, Đà Lạt', NOW() + INTERVAL '25 days', NOW() + INTERVAL '25 days 3 hours', 'PLANNED', 'https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg', 'Thưởng thức nhạc Trịnh giữa đồi thông mộng mơ'),
(uuidv7(), 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 3, 'Nhà Hát Bến Thành, TP.HCM', NOW() + INTERVAL '5 days', NOW() + INTERVAL '5 days 3 hours', 'ONGOING', 'https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png', 'Chương trình kịch thiếu nhi được mong chờ nhất hè này'),
(uuidv7(), 'Ravolution Music Festival: Unite', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 1, 'Khu đô thị Vạn Phúc, Thủ Đức', NOW() + INTERVAL '60 days', NOW() + INTERVAL '61 days', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg', 'Lễ hội âm nhạc điện tử quốc tế với dàn DJ Top 100'),
(uuidv7(), 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 3, 'Cà phê Tinh Tế, Quận 3', NOW() + INTERVAL '7 days', NOW() + INTERVAL '7 days 2 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg', 'Đêm hài độc thoại giải tỏa căng thẳng cuối tuần'),
(uuidv7(), 'Vietnam Tech Summit 2025', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 2, 'GEM Center, Quận 1', NOW() + INTERVAL '10 days', NOW() + INTERVAL '10 days 8 hours', 'ONGOING', 'https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg', 'Hội thảo công nghệ lớn nhất năm'),
(uuidv7(), 'VBA 2025: Saigon Heat vs Hanoi Buffaloes', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 4, 'CIS Arena, Quận 7', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days - 2 hours', 'FINISHED', 'https://salt.tkbcdn.com/ts/ds/45/dc/06/c781b8bb5db8d7c591d508f91a859a0d.jpg', 'Trận derby bóng rổ kịch tính nhất mùa giải'),
(uuidv7(), 'Workshop: Marketing 0 Đồng cho Startup', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 2, 'Dreamplex Điện Biên Phủ', NOW() + INTERVAL '14 days', NOW() + INTERVAL '14 days 4 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png', 'Chia sẻ bí quyết tăng trưởng không cần ngân sách lớn'),
(uuidv7(), 'Van Gogh Art Lighting Experience', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 3, 'Gigamall Thủ Đức', NOW() - INTERVAL '10 days', NOW() + INTERVAL '20 days', 'ONGOING', 'https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990', 'Triển lãm nghệ thuật tương tác đa giác quan');

--3. Table: Event Contents
INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
VALUES 
('00000000-2ed5-7db3-b2fb-69cdc7664400', 'Chi tiết hội thảo...', 'Workshop details...', 'Mang laptop', 'Hoàn tiền 50%', 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),
('00000000-2ec7-7ac1-90b6-20f5889f2000', 'Chi tiết tiếng Việt...', 'English details...', 'Cấm trẻ em', 'Không hoàn tiền', 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),
('00000000-2ed5-766f-ba9b-eb8c92d4f800', 'Hành trình âm nhạc đầy mộng mơ dành cho giới trẻ.', 'A dreamy musical journey for the youth.', 'Vui lòng mang theo CCCD khi check-in.', 'Không hoàn trả vé sau khi mua.', 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),
('00000000-2ed5-79e9-a5ef-885e22274800', 'Lễ hội âm nhạc điện tử lớn nhất khu vực.', 'The biggest EDM festival in the region.', 'Trang phục tự do, không mang chất cấm.', 'Hoàn tiền 50% trước 7 ngày diễn ra.', 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'),
('00000000-2ed5-76b6-b3a3-84dc4ce00c00', 'Vở diễn huyền thoại Ngày Xửa Ngày Xưa.', 'The legendary Once Upon A Time play.', 'Mỗi vé dành cho một người, trẻ em cần có người lớn đi kèm.', 'Không hoàn tiền.', 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),
('00000000-2ed5-73c8-938b-5da1596eac00', 'Trận đối đầu kịch tính của giải bóng rổ chuyên nghiệp Việt Nam.', 'Intense match of the Vietnam Basketball Association.', 'Không mang đồ ăn thức uống bên ngoài vào sân.', 'Hoàn vé theo quy định của BTC.', 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'),
('00000000-2ed5-72b2-8d0e-1bff10dcf400', 'Hội thảo chia sẻ kinh nghiệm Marketing 0 đồng.', 'Marketing on a zero-dollar budget workshop.', 'Khuyến khích mang theo laptop cá nhân.', 'Hoàn tiền 50% nếu hủy trước 3 ngày.', 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),
('00000000-2ed5-7e2e-8252-70ecdbf5c800', 'Đêm hài độc thoại cười ra nước mắt.', 'A night of hilarious stand-up comedy.', 'Vui lòng không quay phim trong buổi diễn.', 'Không hoàn trả vé.', 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),
('00000000-2ed5-769a-8842-16312d3a4c00', 'Triển lãm nghệ thuật ánh sáng tương tác đa giác quan.', 'Multi-sensory interactive light art exhibition.', 'Mỗi lượt tham quan tối đa 60 phút.', 'Không hỗ trợ hoàn tiền.', 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg')
;

--4. Table: Zones

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Cư Dân VIP', false, 200, 0, 1500000 FROM public.events WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'GA Thường', true, 2000, 0, 650000 FROM public.events WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Khu A (Lầu 1)', false, 300, 0, 350000 FROM public.events WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Khu B (Lầu 2)', false, 200, 0, 250000 FROM public.events WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Courtside VIP', false, 50, 0, 2500000 FROM public.events WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Standard Bleachers', false, 800, 0, 150000 FROM public.events WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'SVIP Deck', false, 100, 0, 4500000 FROM public.events WHERE name = 'Ravolution Music Festival: Unite';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'GA Early Bird', true, 1500, 0, 850000 FROM public.events WHERE name = 'Ravolution Music Festival: Unite';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Premium (Kèm tài liệu)', false, 50, 0, 450000 FROM public.events WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Standard', false, 100, 0, 200000 FROM public.events WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Ghế gần sân khấu', false, 40, 0, 400000 FROM public.events WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Ghế thường', false, 60, 0, 250000 FROM public.events WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'VIP (Kèm quà tặng)', false, 200, 0, 1200000 FROM public.events WHERE name = 'Van Gogh Art Lighting Experience';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Standard', false, 1000, 0, 650000 FROM public.events WHERE name = 'Van Gogh Art Lighting Experience';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Khu VVIP (Gần ca sĩ)', false, 50, 0, 1800000 FROM public.events WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Khu Khán Đài', false, 200, 0, 800000 FROM public.events WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- 5. TẠO ZONE CONTENTS (Chi tiết hạng vé)

INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Khu vực đứng gần sân khấu, trải nghiệm âm nhạc bùng nổ', '["Vòng tay check-in", "Nước suối"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'GA Standing' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';
INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Ghế ngồi tiêu chuẩn, tầm nhìn bao quát sân khấu', '["Tài liệu hội thảo", "Teabreak giữa giờ"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'Standard' AND e.name = 'Vietnam Tech Summit 2025';
INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Đặc quyền cư dân VIP với khu vực nghỉ ngơi riêng', '["Bộ quà tặng Cư Dân", "Lối đi ưu tiên", "Nước uống miễn phí"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'Cư Dân VIP' AND e.name = 'Những Thành Phố Mơ Màng - Summer Tour';
INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Khu vực tự do dành cho các cư dân yêu âm nhạc', '["Vòng tay vải", "Sticker"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'GA Thường' AND e.name = 'Những Thành Phố Mơ Màng - Summer Tour';
INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Tận hưởng lễ hội trên khán đài cao cấp', '["Private Bar", "Quà tặng từ nhà tài trợ", "Lối đi VIP"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'SVIP Deck' AND e.name = 'Ravolution Music Festival: Unite';
INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Ghế ngồi sát sàn đấu, cảm nhận từng bước chạy của cầu thủ', '["Áo đấu phiên bản giới hạn", "F&B phục vụ tại chỗ"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'Courtside VIP' AND e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';
INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Vị trí đẹp nhất để tương tác cùng nghệ sĩ', '["Một phần nước tự chọn", "Đĩa CD nhạc Trịnh"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'Khu VVIP (Gần ca sĩ)' AND e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';
INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Trải nghiệm nghệ thuật đa giác quan không giới hạn', '["Sổ tay Van Gogh", "Ưu tiên chụp ảnh tại khu vực đặc biệt"]'::jsonb
FROM public.zones z JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'VIP (Kèm quà tặng)' AND e.name = 'Van Gogh Art Lighting Experience';


-- 6. TẠO GHẾ (SEATS) - Chỉ tạo cho hạng vé VVIP (Ngồi)
-- Kết hợp uuidv7 và vòng lặp tạo ghế A1..A10, B1..B10 ngay trong câu lệnh insert

INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code, status)
SELECT 
    uuidv7(),             -- Tự sinh ID cho ghế
    z.id,                 -- Lấy ID của Zone VVIP
    'Ghế ' || r || c,     -- Tên ghế
    chr(64 + r),          -- Hàng A, B, C...
    c::text,              -- Cột 1, 2, 3...
    chr(64 + r) || c,     -- Mã ghế (A1, A2...)
    'AVAILABLE'
FROM public.zones z
JOIN public.events e ON z.event_id = e.id
CROSS JOIN generate_series(1, 5) r  -- 5 Hàng
CROSS JOIN generate_series(1, 10) c -- 10 Cột
WHERE z.name = 'VVIP' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

như vậy được chưa kiểu lướt 1 lần là vô dữ liệu hết 