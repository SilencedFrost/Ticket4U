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
(uuidv7(), 'Hà Anh Tuấn: Chân Trời Rực Rỡ', uuidv7(), 1, 'Sân Lễ Hội Đền Hùng, Ninh Bình', NOW() + INTERVAL '30 days', NOW() + INTERVAL '30 days 4 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/25/e6/b4/d79786df1e38c39beabe33c462cc381e.jpg', 'Đêm nhạc huyền thoại với sự kết hợp của Kitaro'),
(uuidv7(), 'Vietnam Tech Summit 2025', uuidv7(), 2, 'GEM Center, Quận 1', NOW() + INTERVAL '10 days', NOW() + INTERVAL '10 days 8 hours', 'ONGOING', 'https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg', 'Hội thảo công nghệ lớn nhất năm'),
(uuidv7(), 'Những Thành Phố Mơ Màng - Summer Tour', uuidv7(), 1, 'Công viên Yên Sở, Hà Nội', NOW() + INTERVAL '45 days', NOW() + INTERVAL '45 days 6 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg', 'Show diễn Indie với sự góp mặt của Đen Vâu, Chillies, Vũ.'),
(uuidv7(), 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35', uuidv7(), 3, 'Nhà Hát Bến Thành, TP.HCM', NOW() + INTERVAL '5 days', NOW() + INTERVAL '5 days 3 hours', 'ONGOING', 'https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png', 'Chương trình kịch thiếu nhi được mong chờ nhất hè này'),
(uuidv7(), 'VBA 2025: Saigon Heat vs Hanoi Buffaloes', uuidv7(), 4, 'CIS Arena, Quận 7', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days - 2 hours', 'FINISHED', 'https://salt.tkbcdn.com/ts/ds/45/dc/06/c781b8bb5db8d7c591d508f91a859a0d.jpg', 'Trận derby bóng rổ kịch tính nhất mùa giải'),
(uuidv7(), 'Ravolution Music Festival: Unite', uuidv7(), 1, 'Khu đô thị Vạn Phúc, Thủ Đức', NOW() + INTERVAL '60 days', NOW() + INTERVAL '61 days', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg', 'Lễ hội âm nhạc điện tử quốc tế với dàn DJ Top 100'),
(uuidv7(), 'Workshop: Marketing 0 Đồng cho Startup', uuidv7(), 2, 'Dreamplex Điện Biên Phủ', NOW() + INTERVAL '14 days', NOW() + INTERVAL '14 days 4 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png', 'Chia sẻ bí quyết tăng trưởng không cần ngân sách lớn'),
(uuidv7(), 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi', uuidv7(), 3, 'Cà phê Tinh Tế, Quận 3', NOW() + INTERVAL '7 days', NOW() + INTERVAL '7 days 2 hours', 'PLANNED', 'https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg', 'Đêm hài độc thoại giải tỏa căng thẳng cuối tuần'),
(uuidv7(), 'Van Gogh Art Lighting Experience', uuidv7(), 3, 'Gigamall Thủ Đức', NOW() - INTERVAL '10 days', NOW() + INTERVAL '20 days', 'ONGOING', 'https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990', 'Triển lãm nghệ thuật tương tác đa giác quan'),
(uuidv7(), 'Mây Lang Thang: Đêm Nhạc Trịnh', uuidv7(), 1, 'Mây Lang Thang, Đà Lạt', NOW() + INTERVAL '25 days', NOW() + INTERVAL '25 days 3 hours', 'PLANNED', 'https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg', 'Thưởng thức nhạc Trịnh giữa đồi thông mộng mơ');

--3. Table: Event Contents
--Dùng SELECT để lấy ID của Event vừa tạo ở trên dựa vào Tên

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund)
SELECT id, 'Chi tiết tiếng Việt...', 'English details...', 'Cấm trẻ em', 'Không hoàn tiền'
FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund)
SELECT id, 'Chi tiết hội thảo...', 'Workshop details...', 'Mang laptop', 'Hoàn tiền 50%'
FROM public.events WHERE name = 'Vietnam Tech Summit 2025';

--4. Table: Zones

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'VVIP', false, 100, 0, 5000000 
FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'GA Standing', true, 1000, 0, 1000000 
FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price)
SELECT uuidv7(), id, 'Standard', false, 500, 0, 200000 
FROM public.events WHERE name = 'Vietnam Tech Summit 2025';

-- 5. TẠO ZONE CONTENTS (Chi tiết hạng vé)

INSERT INTO public.zone_contents (zone_id, description, perks)
SELECT z.id, 'Ghế ngồi gần sân khấu', '["Lightstick", "Nước uống"]'::jsonb
FROM public.zones z 
JOIN public.events e ON z.event_id = e.id 
WHERE z.name = 'VVIP' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';


-- 6. TẠO GHẾ (SEATS) - Chỉ tạo cho hạng vé VVIP (Ngồi)
-- Kết hợp uuidv7 và vòng lặp tạo ghế A1..A10, B1..B10 ngay trong câu lệnh insert

INSERT INTO public.seats (id, zone_id, name, row_name, col_name, seat_code, status)
SELECT 
    uuidv7(),             -- Tự sinh ID cho ghế
    z.id,                 -- Lấy ID của Zone VVIP
    'Ghế ' || r || c,     -- Tên ghế
    chr(64 + r),          -- Hàng A, B, C...
    c::text,              -- Cột 1, 2, 3...
    chr(64 + r) || c,     -- Mã ghế (A1, A2...)
    'AVAILABLE'
FROM public.zones z
JOIN public.events e ON z.event_id = e.id
CROSS JOIN generate_series(1, 5) r  -- 5 Hàng
CROSS JOIN generate_series(1, 10) c -- 10 Cột
WHERE z.name = 'VVIP' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';