-- Event Data for the Event table
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
SELECT id, 'Chi tiết hội thảo...', 'Workshop details...', 'Mang laptop', 'Hoàn tiền 50%', 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'
FROM public.events WHERE name = 'Vietnam Tech Summit 2025';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Chi tiết tiếng Việt...', 'English details...', 'Cấm trẻ em', 'Không hoàn tiền', 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'
FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Hành trình âm nhạc đầy mộng mơ dành cho giới trẻ.', 'A dreamy musical journey for the youth.', 'Vui lòng mang theo CCCD khi check-in.', 'Không hoàn trả vé sau khi mua.', 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'
FROM public.events WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Lễ hội âm nhạc điện tử lớn nhất khu vực.', 'The biggest EDM festival in the region.', 'Trang phục tự do, không mang chất cấm.', 'Hoàn tiền 50% trước 7 ngày diễn ra.', 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'
FROM public.events WHERE name = 'Ravolution Music Festival: Unite';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Vở diễn huyền thoại Ngày Xửa Ngày Xưa.', 'The legendary Once Upon A Time play.', 'Mỗi vé dành cho một người, trẻ em cần có người lớn đi kèm.', 'Không hoàn tiền.', 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'
FROM public.events WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Trận đối đầu kịch tính của giải bóng rổ chuyên nghiệp Việt Nam.', 'Intense match of the Vietnam Basketball Association.', 'Không mang đồ ăn thức uống bên ngoài vào sân.', 'Hoàn vé theo quy định của BTC.', 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'
FROM public.events WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Hội thảo chia sẻ kinh nghiệm Marketing 0 đồng.', 'Marketing on a zero-dollar budget workshop.', 'Khuyến khích mang theo laptop cá nhân.', 'Hoàn tiền 50% nếu hủy trước 3 ngày.', 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'
FROM public.events WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Đêm hài độc thoại cười ra nước mắt.', 'A night of hilarious stand-up comedy.', 'Vui lòng không quay phim trong buổi diễn.', 'Không hoàn trả vé.', 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'
FROM public.events WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Triển lãm nghệ thuật ánh sáng tương tác đa giác quan.', 'Multi-sensory interactive light art exhibition.', 'Mỗi lượt tham quan tối đa 60 phút.', 'Không hỗ trợ hoàn tiền.', 'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'
FROM public.events WHERE name = 'Van Gogh Art Lighting Experience';

-- Mình thêm sự kiện "Mây Lang Thang" vì trong 9 record ở trên bị thiếu event này
INSERT INTO public.event_contents (event_id, about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url)
SELECT id, 'Đêm nhạc Trịnh mộc mạc giữa rừng thông.', 'Rustic Trinh music night in the pine forest.', 'Nên mang theo áo khoác ấm.', 'Không hoàn tiền vé.', 'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'
FROM public.events WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

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
    uuidv7(), 
    z.id, 
    'Ghế ' || chr(64 + r) || c, 
    chr(64 + r), 
    c::text, 
    chr(64 + r) || c,
    'AVAILABLE'
FROM public.zones z
JOIN public.events e ON z.event_id = e.id
CROSS JOIN generate_series(1, 5) r 
CROSS JOIN generate_series(1, 10) c 
WHERE z.name = 'VVIP' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';



-- ============================================================
-- Layout Templates
-- ============================================================

-- ── 1. Standing Only - Single Zone ────────────────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Standing - Single Zone',
     'One large standing zone. Good for small clubs or simple GA events.',
     true,
     '{
       "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
       "zones": [
         {
           "zone_name": "GA",
           "zone_type": "standing",
           "color": "#2563EB",
           "corner1": { "x": -0.9, "y": -0.8 },
           "corner2": { "x":  0.9, "y": -0.8 },
           "corner3": { "x":  0.9, "y":  0.9 },
           "corner4": { "x": -0.9, "y":  0.9 },
           "seats": []
         }
       ]
     }'::jsonb);


-- ── 2. Standing - VIP + GA Split ──────────────────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Standing - VIP + GA',
     'Two standing zones. VIP close to stage, GA behind.',
     true,
     '{
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
           "corner3": { "x":  0.9, "y":  0.9 },
           "corner4": { "x": -0.9, "y":  0.9 },
           "seats": []
         }
       ]
     }'::jsonb);


-- ── 3. Standing - SVIP + VIP + GA (3 Zones) ───────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Standing - 3 Zones',
     'Three standing zones. SVIP front, VIP middle, GA back.',
     true,
     '{
       "stage": { "x1": -0.4, "y1": -1.0, "x2": 0.4, "y2": -0.85 },
       "zones": [
         {
           "zone_name": "SVIP",
           "zone_type": "standing",
           "color": "#DC2626",
           "corner1": { "x": -0.9, "y": -0.8 },
           "corner2": { "x":  0.9, "y": -0.8 },
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
           "zone_name": "GA",
           "zone_type": "standing",
           "color": "#2563EB",
           "corner1": { "x": -0.9, "y":  0.25 },
           "corner2": { "x":  0.9, "y":  0.25 },
           "corner3": { "x":  0.9, "y":  0.9 },
           "corner4": { "x": -0.9, "y":  0.9 },
           "seats": []
         }
       ]
     }'::jsonb);


-- ── 4. Standing - 5 Zones (Festival Style) ────────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Standing - 5 Zones Festival',
     'Five standing zones. SVIP center front, VIP left/right, GA left/right back.',
     true,
     '{
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
           "corner1": { "x": -0.9, "y": -0.8 },
           "corner2": { "x": -0.45, "y": -0.8 },
           "corner3": { "x": -0.45, "y": -0.2 },
           "corner4": { "x": -0.9, "y": -0.2 },
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
           "corner1": { "x": -0.9, "y": -0.15 },
           "corner2": { "x": -0.05,"y": -0.15 },
           "corner3": { "x": -0.05,"y":  0.9  },
           "corner4": { "x": -0.9, "y":  0.9  },
           "seats": []
         },
         {
           "zone_name": "GA Right",
           "zone_type": "standing",
           "color": "#1D4ED8",
           "corner1": { "x":  0.05, "y": -0.15 },
           "corner2": { "x":  0.9,  "y": -0.15 },
           "corner3": { "x":  0.9,  "y":  0.9  },
           "corner4": { "x":  0.05, "y":  0.9  },
           "seats": []
         }
       ]
     }'::jsonb);


-- ── 5. Cinema Layout ──────────────────────────────────────
-- 8 rows x 10 seats, all facing stage (rotation 0)
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Cinema',
     'Classic cinema seating. Rows of seats all facing the screen.',
     true,
     '{
       "stage": { "x1": -0.5, "y1": -1.0, "x2": 0.5, "y2": -0.88 },
       "zones": [
         {
           "zone_name": "Screen Zone",
           "zone_type": "sitting",
           "color": "#059669",
           "corner1": { "x": -0.9, "y": -0.85 },
           "corner2": { "x":  0.9, "y": -0.85 },
           "corner3": { "x":  0.9, "y":  0.9  },
           "corner4": { "x": -0.9, "y":  0.9  },
           "seats": [
             { "seat_name": "A1",  "seat_id": "A1",  "seat_pos": { "x": -0.72, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A2",  "seat_id": "A2",  "seat_pos": { "x": -0.56, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A3",  "seat_id": "A3",  "seat_pos": { "x": -0.40, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A4",  "seat_id": "A4",  "seat_pos": { "x": -0.24, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A5",  "seat_id": "A5",  "seat_pos": { "x": -0.08, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A6",  "seat_id": "A6",  "seat_pos": { "x":  0.08, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A7",  "seat_id": "A7",  "seat_pos": { "x":  0.24, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A8",  "seat_id": "A8",  "seat_pos": { "x":  0.40, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A9",  "seat_id": "A9",  "seat_pos": { "x":  0.56, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "A10", "seat_id": "A10", "seat_pos": { "x":  0.72, "y": -0.75 }, "seat_rotation": 0 },
             { "seat_name": "B1",  "seat_id": "B1",  "seat_pos": { "x": -0.72, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B2",  "seat_id": "B2",  "seat_pos": { "x": -0.56, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B3",  "seat_id": "B3",  "seat_pos": { "x": -0.40, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B4",  "seat_id": "B4",  "seat_pos": { "x": -0.24, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B5",  "seat_id": "B5",  "seat_pos": { "x": -0.08, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B6",  "seat_id": "B6",  "seat_pos": { "x":  0.08, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B7",  "seat_id": "B7",  "seat_pos": { "x":  0.24, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B8",  "seat_id": "B8",  "seat_pos": { "x":  0.40, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B9",  "seat_id": "B9",  "seat_pos": { "x":  0.56, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "B10", "seat_id": "B10", "seat_pos": { "x":  0.72, "y": -0.55 }, "seat_rotation": 0 },
             { "seat_name": "C1",  "seat_id": "C1",  "seat_pos": { "x": -0.72, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C2",  "seat_id": "C2",  "seat_pos": { "x": -0.56, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C3",  "seat_id": "C3",  "seat_pos": { "x": -0.40, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C4",  "seat_id": "C4",  "seat_pos": { "x": -0.24, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C5",  "seat_id": "C5",  "seat_pos": { "x": -0.08, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C6",  "seat_id": "C6",  "seat_pos": { "x":  0.08, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C7",  "seat_id": "C7",  "seat_pos": { "x":  0.24, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C8",  "seat_id": "C8",  "seat_pos": { "x":  0.40, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C9",  "seat_id": "C9",  "seat_pos": { "x":  0.56, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "C10", "seat_id": "C10", "seat_pos": { "x":  0.72, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "D1",  "seat_id": "D1",  "seat_pos": { "x": -0.72, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D2",  "seat_id": "D2",  "seat_pos": { "x": -0.56, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D3",  "seat_id": "D3",  "seat_pos": { "x": -0.40, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D4",  "seat_id": "D4",  "seat_pos": { "x": -0.24, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D5",  "seat_id": "D5",  "seat_pos": { "x": -0.08, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D6",  "seat_id": "D6",  "seat_pos": { "x":  0.08, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D7",  "seat_id": "D7",  "seat_pos": { "x":  0.24, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D8",  "seat_id": "D8",  "seat_pos": { "x":  0.40, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D9",  "seat_id": "D9",  "seat_pos": { "x":  0.56, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "D10", "seat_id": "D10", "seat_pos": { "x":  0.72, "y": -0.15 }, "seat_rotation": 0 },
             { "seat_name": "E1",  "seat_id": "E1",  "seat_pos": { "x": -0.72, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E2",  "seat_id": "E2",  "seat_pos": { "x": -0.56, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E3",  "seat_id": "E3",  "seat_pos": { "x": -0.40, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E4",  "seat_id": "E4",  "seat_pos": { "x": -0.24, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E5",  "seat_id": "E5",  "seat_pos": { "x": -0.08, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E6",  "seat_id": "E6",  "seat_pos": { "x":  0.08, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E7",  "seat_id": "E7",  "seat_pos": { "x":  0.24, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E8",  "seat_id": "E8",  "seat_pos": { "x":  0.40, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E9",  "seat_id": "E9",  "seat_pos": { "x":  0.56, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "E10", "seat_id": "E10", "seat_pos": { "x":  0.72, "y":  0.05 }, "seat_rotation": 0 },
             { "seat_name": "F1",  "seat_id": "F1",  "seat_pos": { "x": -0.72, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F2",  "seat_id": "F2",  "seat_pos": { "x": -0.56, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F3",  "seat_id": "F3",  "seat_pos": { "x": -0.40, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F4",  "seat_id": "F4",  "seat_pos": { "x": -0.24, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F5",  "seat_id": "F5",  "seat_pos": { "x": -0.08, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F6",  "seat_id": "F6",  "seat_pos": { "x":  0.08, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F7",  "seat_id": "F7",  "seat_pos": { "x":  0.24, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F8",  "seat_id": "F8",  "seat_pos": { "x":  0.40, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F9",  "seat_id": "F9",  "seat_pos": { "x":  0.56, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "F10", "seat_id": "F10", "seat_pos": { "x":  0.72, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "G1",  "seat_id": "G1",  "seat_pos": { "x": -0.72, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G2",  "seat_id": "G2",  "seat_pos": { "x": -0.56, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G3",  "seat_id": "G3",  "seat_pos": { "x": -0.40, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G4",  "seat_id": "G4",  "seat_pos": { "x": -0.24, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G5",  "seat_id": "G5",  "seat_pos": { "x": -0.08, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G6",  "seat_id": "G6",  "seat_pos": { "x":  0.08, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G7",  "seat_id": "G7",  "seat_pos": { "x":  0.24, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G8",  "seat_id": "G8",  "seat_pos": { "x":  0.40, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G9",  "seat_id": "G9",  "seat_pos": { "x":  0.56, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "G10", "seat_id": "G10", "seat_pos": { "x":  0.72, "y":  0.55 }, "seat_rotation": 0 },
             { "seat_name": "H1",  "seat_id": "H1",  "seat_pos": { "x": -0.72, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H2",  "seat_id": "H2",  "seat_pos": { "x": -0.56, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H3",  "seat_id": "H3",  "seat_pos": { "x": -0.40, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H4",  "seat_id": "H4",  "seat_pos": { "x": -0.24, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H5",  "seat_id": "H5",  "seat_pos": { "x": -0.08, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H6",  "seat_id": "H6",  "seat_pos": { "x":  0.08, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H7",  "seat_id": "H7",  "seat_pos": { "x":  0.24, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H8",  "seat_id": "H8",  "seat_pos": { "x":  0.40, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H9",  "seat_id": "H9",  "seat_pos": { "x":  0.56, "y":  0.75 }, "seat_rotation": 0 },
             { "seat_name": "H10", "seat_id": "H10", "seat_pos": { "x":  0.72, "y":  0.75 }, "seat_rotation": 0 }
           ]
         }
       ]
     }'::jsonb);


-- ── 6. Theater - VIP + Standard ───────────────────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Theater - VIP + Standard',
     'Two seated zones. VIP front rows, Standard back rows.',
     true,
     '{
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
       ]
     }'::jsonb);


-- ── 7. Sports Arena ───────────────────────────────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Sports Arena',
     'Courtside VIP on left and right, bleachers wrapping the back.',
     true,
     '{
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
             { "seat_name": "L-A1", "seat_id": "L-A1", "seat_pos": { "x": -0.8,  "y": -0.35 }, "seat_rotation":  90 },
             { "seat_name": "L-A2", "seat_id": "L-A2", "seat_pos": { "x": -0.8,  "y": -0.1  }, "seat_rotation":  90 },
             { "seat_name": "L-A3", "seat_id": "L-A3", "seat_pos": { "x": -0.8,  "y":  0.15 }, "seat_rotation":  90 },
             { "seat_name": "L-A4", "seat_id": "L-A4", "seat_pos": { "x": -0.8,  "y":  0.4  }, "seat_rotation":  90 },
             { "seat_name": "L-B1", "seat_id": "L-B1", "seat_pos": { "x": -0.6,  "y": -0.35 }, "seat_rotation":  90 },
             { "seat_name": "L-B2", "seat_id": "L-B2", "seat_pos": { "x": -0.6,  "y": -0.1  }, "seat_rotation":  90 },
             { "seat_name": "L-B3", "seat_id": "L-B3", "seat_pos": { "x": -0.6,  "y":  0.15 }, "seat_rotation":  90 },
             { "seat_name": "L-B4", "seat_id": "L-B4", "seat_pos": { "x": -0.6,  "y":  0.4  }, "seat_rotation":  90 }
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
             { "seat_name": "R-A1", "seat_id": "R-A1", "seat_pos": { "x":  0.6,  "y": -0.35 }, "seat_rotation": -90 },
             { "seat_name": "R-A2", "seat_id": "R-A2", "seat_pos": { "x":  0.6,  "y": -0.1  }, "seat_rotation": -90 },
             { "seat_name": "R-A3", "seat_id": "R-A3", "seat_pos": { "x":  0.6,  "y":  0.15 }, "seat_rotation": -90 },
             { "seat_name": "R-A4", "seat_id": "R-A4", "seat_pos": { "x":  0.6,  "y":  0.4  }, "seat_rotation": -90 },
             { "seat_name": "R-B1", "seat_id": "R-B1", "seat_pos": { "x":  0.8,  "y": -0.35 }, "seat_rotation": -90 },
             { "seat_name": "R-B2", "seat_id": "R-B2", "seat_pos": { "x":  0.8,  "y": -0.1  }, "seat_rotation": -90 },
             { "seat_name": "R-B3", "seat_id": "R-B3", "seat_pos": { "x":  0.8,  "y":  0.15 }, "seat_rotation": -90 },
             { "seat_name": "R-B4", "seat_id": "R-B4", "seat_pos": { "x":  0.8,  "y":  0.4  }, "seat_rotation": -90 }
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
       ]
     }'::jsonb);


-- ── 8. Club Layout ────────────────────────────────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Club',
     'Dance floor center, VIP lounge left, bar area right.',
     true,
     '{
       "stage": { "x1": -0.25, "y1": -1.0, "x2": 0.25, "y2": -0.88 },
       "zones": [
         {
           "zone_name": "Dance Floor",
           "zone_type": "standing",
           "color": "#DB2777",
           "corner1": { "x": -0.45, "y": -0.85 },
           "corner2": { "x":  0.45, "y": -0.85 },
           "corner3": { "x":  0.45, "y":  0.4  },
           "corner4": { "x": -0.45, "y":  0.4  },
           "seats": []
         },
         {
           "zone_name": "VIP Lounge",
           "zone_type": "sitting",
           "color": "#7C3AED",
           "corner1": { "x": -0.9,  "y": -0.85 },
           "corner2": { "x": -0.5,  "y": -0.85 },
           "corner3": { "x": -0.5,  "y":  0.9  },
           "corner4": { "x": -0.9,  "y":  0.9  },
           "seats": [
             { "seat_name": "V1", "seat_id": "V1", "seat_pos": { "x": -0.75, "y": -0.6  }, "seat_rotation":  90 },
             { "seat_name": "V2", "seat_id": "V2", "seat_pos": { "x": -0.75, "y": -0.3  }, "seat_rotation":  90 },
             { "seat_name": "V3", "seat_id": "V3", "seat_pos": { "x": -0.75, "y":  0.0  }, "seat_rotation":  90 },
             { "seat_name": "V4", "seat_id": "V4", "seat_pos": { "x": -0.75, "y":  0.3  }, "seat_rotation":  90 },
             { "seat_name": "V5", "seat_id": "V5", "seat_pos": { "x": -0.75, "y":  0.6  }, "seat_rotation":  90 }
           ]
         },
         {
           "zone_name": "Bar Area",
           "zone_type": "standing",
           "color": "#D97706",
           "corner1": { "x":  0.5,  "y": -0.85 },
           "corner2": { "x":  0.9,  "y": -0.85 },
           "corner3": { "x":  0.9,  "y":  0.9  },
           "corner4": { "x":  0.5,  "y":  0.9  },
           "seats": []
         },
         {
           "zone_name": "GA Back",
           "zone_type": "standing",
           "color": "#2563EB",
           "corner1": { "x": -0.45, "y":  0.45 },
           "corner2": { "x":  0.45, "y":  0.45 },
           "corner3": { "x":  0.45, "y":  0.9  },
           "corner4": { "x": -0.45, "y":  0.9  },
           "seats": []
         }
       ]
     }'::jsonb);


-- ── 9. Restaurant ─────────────────────────────────────────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Restaurant',
     'Three dining sections: Premium window, Main floor, Private room.',
     true,
     '{
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
             { "seat_name": "M1", "seat_id": "M1", "seat_pos": { "x":  0.0, "y": -0.65 }, "seat_rotation": 0 },
             { "seat_name": "M2", "seat_id": "M2", "seat_pos": { "x":  0.0, "y": -0.35 }, "seat_rotation": 0 },
             { "seat_name": "M3", "seat_id": "M3", "seat_pos": { "x":  0.0, "y": -0.05 }, "seat_rotation": 0 },
             { "seat_name": "M4", "seat_id": "M4", "seat_pos": { "x":  0.0, "y":  0.25 }, "seat_rotation": 0 },
             { "seat_name": "M5", "seat_id": "M5", "seat_pos": { "x":  0.0, "y":  0.55 }, "seat_rotation": 0 }
           ]
         },
         {
           "zone_name": "Private Room",
           "zone_type": "sitting",
           "color": "#7C3AED",
           "corner1": { "x":  0.3,  "y": -0.85 },
           "corner2": { "x":  0.9,  "y": -0.85 },
           "corner3": { "x":  0.9,  "y":  0.9  },
           "corner4": { "x":  0.3,  "y":  0.9  },
           "seats": [
             { "seat_name": "P1", "seat_id": "P1", "seat_pos": { "x":  0.6, "y": -0.65 }, "seat_rotation": -90 },
             { "seat_name": "P2", "seat_id": "P2", "seat_pos": { "x":  0.6, "y": -0.35 }, "seat_rotation": -90 },
             { "seat_name": "P3", "seat_id": "P3", "seat_pos": { "x":  0.6, "y": -0.05 }, "seat_rotation": -90 },
             { "seat_name": "P4", "seat_id": "P4", "seat_pos": { "x":  0.6, "y":  0.25 }, "seat_rotation": -90 },
             { "seat_name": "P5", "seat_id": "P5", "seat_pos": { "x":  0.6, "y":  0.55 }, "seat_rotation": -90 }
           ]
         }
       ]
     }'::jsonb);


-- ── 10. Concert Classic - VIP Sitting + GA Standing ───────
INSERT INTO public.venue_layouts (name, description, is_public, layout_json) VALUES
    ('Concert Classic',
     'VIP seated zone up front, large GA standing zone at the back.',
     true,
     '{
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
       ]
     }'::jsonb);