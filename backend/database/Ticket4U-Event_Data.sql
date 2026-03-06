--1. Table: categories (Thể loại sự kiện)

TRUNCATE TABLE public.categories RESTART IDENTITY CASCADE;

INSERT INTO public.categories (name) VALUES 
('Âm nhạc'), 
('Hội thảo'), 
('Sân khấu - Nghệ thuật'), 
('Thể thao'), 
('Giải trí về đêm');

--2. Table: Events (đã gộp event_contents vào)
INSERT INTO public.events (
	id, name, organizer_id, category_id, address_line, 
	start_date, end_date, status, banner_url, description_vi, description_en,
	created_at,
	about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url
) VALUES
(uuidv7(), 'Hà Anh Tuấn: Chân Trời Rực Rỡ', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình', 
	NOW() + INTERVAL '30 days', NOW() + INTERVAL '30 days 4 hours', 'PLANNED', 
	'https://salt.tkbcdn.com/ts/ds/25/e6/b4/d79786df1e38c39beabe33c462cc381e.jpg', 
	'Đêm nhạc huyền thoại với sự kết hợp của Kitaro',
	'A legendary music night featuring a collaboration with Kitaro',
	NOW(),
	'Chi tiết tiếng Việt...', 'English details...', 'Cấm trẻ em', 'Không hoàn tiền', 
	'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),

(uuidv7(), 'Những Thành Phố Mơ Màng - Summer Tour', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội', 
	NOW() + INTERVAL '45 days', NOW() + INTERVAL '45 days 6 hours', 'PLANNED', 
	'https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg', 
	'Show diễn Indie với sự góp mặt của Đen Vâu, Chillies, Vũ.',
	'Indie show featuring Den Vau, Chillies, and Vu.',
	NOW(),
	'Hành trình âm nhạc đầy mộng mơ dành cho giới trẻ.', 'A dreamy musical journey for the youth.', 
	'Vui lòng mang theo CCCD khi check-in.', 'Không hoàn trả vé sau khi mua.', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

(uuidv7(), 'Mây Lang Thang: Đêm Nhạc Trịnh', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Mây Lang Thang, Phường 4, Thành phố Đà Lạt, Tỉnh Lâm Đồng', 
	NOW() + INTERVAL '25 days', NOW() + INTERVAL '25 days 3 hours', 'PLANNED', 
	'https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg', 
	'Thưởng thức nhạc Trịnh giữa đồi thông mộng mơ',
	'Enjoy Trinh music amidst the dreamy pine hills',
	NOW(),
	'Đêm nhạc Trịnh mộc mạc giữa rừng thông.', 'Rustic Trinh music night in the pine forest.', 
	'Nên mang theo áo khoác ấm.', 'Không hoàn tiền vé.', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

(uuidv7(), 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 3, 'Nhà Hát Bến Thành, Quận 1, TP. Hồ Chí Minh', 
	NOW() + INTERVAL '5 days', NOW() + INTERVAL '5 days 3 hours', 'ONGOING', 
	'https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png', 
	'Chương trình kịch thiếu nhi được mong chờ nhất hè này',
	'The most anticipated children''s theater show this summer',
	NOW(),
	'Vở diễn huyền thoại Ngày Xửa Ngày Xưa.', 'The legendary Once Upon A Time play.', 
	'Mỗi vé dành cho một người, trẻ em cần có người lớn đi kèm.', 'Không hoàn tiền.', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

(uuidv7(), 'Ravolution Music Festival: Unite', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 1, 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh', 
	NOW() + INTERVAL '60 days', NOW() + INTERVAL '61 days', 'PLANNED', 
	'https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg', 
	'Lễ hội âm nhạc điện tử quốc tế với dàn DJ Top 100',
	'International electronic music festival featuring Top 100 DJs',
	NOW(),
	'Lễ hội âm nhạc điện tử lớn nhất khu vực.', 'The biggest EDM festival in the region.', 
	'Trang phục tự do, không mang chất cấm.', 'Hoàn tiền 50% trước 7 ngày diễn ra.', 
	'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'),

(uuidv7(), 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 3, 'Cà phê Tinh Tế, Quận 3, TP. Hồ Chí Minh', 
	NOW() + INTERVAL '7 days', NOW() + INTERVAL '7 days 2 hours', 'PLANNED', 
	'https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg', 
	'Đêm hài độc thoại giải tỏa căng thẳng cuối tuần',
	'A stand-up comedy night to unwind your weekend stress',
	NOW(),
	'Đêm hài độc thoại cười ra nước mắt.', 'A night of hilarious stand-up comedy.', 
	'Vui lòng không quay phim trong buổi diễn.', 'Không hoàn trả vé.', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png'),

(uuidv7(), 'Vietnam Tech Summit 2025', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 2, 'GEM Center, Quận 1, TP. Hồ Chí Minh', 
	NOW() + INTERVAL '10 days', NOW() + INTERVAL '10 days 8 hours', 'ONGOING', 
	'https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg', 
	'Hội thảo công nghệ lớn nhất năm',
	'The biggest technology conference of the year',
	NOW(),
	'Chi tiết hội thảo...', 'Workshop details...', 'Mang laptop', 'Hoàn tiền 50%', 
	'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),

(uuidv7(), 'VBA 2025: Saigon Heat vs Hanoi Buffaloes', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 4, 'CIS Arena, Quận 7, TP. Hồ Chí Minh', 
	NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days - 2 hours', 'FINISHED', 
	'https://salt.tkbcdn.com/ts/ds/45/dc/06/c781b8bb5db8d7c591d508f91a859a0d.jpg', 
	'Trận derby bóng rổ kịch tính nhất mùa giải',
	'The most thrilling basketball derby match of the season',
	NOW(),
	'Trận đối đầu kịch tính của giải bóng rổ chuyên nghiệp Việt Nam.', 'Intense match of the Vietnam Basketball Association.', 
	'Không mang đồ ăn thức uống bên ngoài vào sân.', 'Hoàn vé theo quy định của BTC.', 
	'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg'),

(uuidv7(), 'Workshop: Marketing 0 Đồng cho Startup', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 2, 'Dreamplex Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh', 
	NOW() + INTERVAL '14 days', NOW() + INTERVAL '14 days 4 hours', 'PLANNED', 
	'https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png', 
	'Chia sẻ bí quyết tăng trưởng không cần ngân sách lớn',
	'Sharing growth secrets without a big budget',
	NOW(),
	'Hội thảo chia sẻ kinh nghiệm Marketing 0 đồng.', 'Marketing on a zero-dollar budget workshop.', 
	'Khuyến khích mang theo laptop cá nhân.', 'Hoàn tiền 50% nếu hủy trước 3 ngày.', 
	'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a'),

(uuidv7(), 'Van Gogh Art Lighting Experience', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 3, 'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh', 
	NOW() - INTERVAL '10 days', NOW() + INTERVAL '20 days', 'ONGOING', 
	'https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990', 
	'Triển lãm nghệ thuật tương tác đa giác quan',
	'Multi-sensory interactive art exhibition',
	NOW(),
	'Triển lãm nghệ thuật ánh sáng tương tác đa giác quan.', 'Multi-sensory interactive light art exhibition.', 
	'Mỗi lượt tham quan tối đa 60 phút.', 'Không hỗ trợ hoàn tiền.', 
	'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg');

--3. Table: Zones (đã gộp zone_contents vào)
-- Zones cho sự kiện: Hà Anh Tuấn
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'VVIP', false, 50, 0, 2500000, 
	'Khu vực VIP cao cấp với dịch vụ đặc biệt', 
	'Premium VIP area with exclusive services',
	'["Vòng tay check-in VIP", "Goodie bag", "Meet & Greet"]'::jsonb, NOW()
FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'GA Standing', true, 800, 0, 650000, 
	'Khu vực đứng gần sân khấu, trải nghiệm âm nhạc bùng nổ', 
	'Standing area near the stage for an explosive music experience',
	'["Vòng tay check-in", "Nước suối"]'::jsonb, NOW()
FROM public.events WHERE name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- Zones cho sự kiện: Những Thành Phố Mơ Màng
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Cư Dân VIP', false, 200, 0, 1500000, 
	'Đặc quyền cư dân VIP với khu vực nghỉ ngơi riêng', 
	'VIP resident privileges with a private lounge area',
	'["Bộ quà tặng Cư Dân", "Lối đi ưu tiên", "Nước uống miễn phí"]'::jsonb, NOW()
FROM public.events WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'GA Thường', true, 2000, 0, 650000, 
	'Khu vực tự do dành cho các cư dân yêu âm nhạc', 
	'General admission area for music-loving residents',
	'["Vòng tay vải", "Sticker"]'::jsonb, NOW()
FROM public.events WHERE name = 'Những Thành Phố Mơ Màng - Summer Tour';

-- Zones cho sự kiện: Mây Lang Thang
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Khu VVIP (Gần ca sĩ)', false, 50, 0, 1800000, 
	'Vị trí đẹp nhất để tương tác cùng nghệ sĩ', 
	'The best spot to interact with the artist',
	'["Một phần nước tự chọn", "Đĩa CD nhạc Trịnh"]'::jsonb, NOW()
FROM public.events WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Khu Khán Đài', false, 200, 0, 800000, 
	'Khu vực ngồi thoải mái với tầm nhìn đẹp', 
	'Comfortable seating area with a beautiful view',
	'["Nước uống", "Chăn len"]'::jsonb, NOW()
FROM public.events WHERE name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Zones cho sự kiện: Kịch Idecaf
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Khu A (Lầu 1)', false, 300, 0, 350000, 
	'Khu vực lầu 1 gần sân khấu nhất', 
	'Floor 1 area closest to the stage',
	NULL, NOW()
FROM public.events WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Khu B (Lầu 2)', false, 200, 0, 250000, 
	'Khu vực lầu 2 tầm nhìn toàn cảnh', 
	'Floor 2 area with a panoramic view',
	NULL, NOW()
FROM public.events WHERE name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- Zones cho sự kiện: Ravolution Music Festival
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'SVIP Deck', false, 100, 0, 4500000, 
	'Tận hưởng lễ hội trên khán đài cao cấp', 
	'Enjoy the festival from the premium deck',
	'["Private Bar", "Quà tặng từ nhà tài trợ", "Lối đi VIP"]'::jsonb, NOW()
FROM public.events WHERE name = 'Ravolution Music Festival: Unite';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'GA Early Bird', true, 1500, 0, 850000, 
	'Khu vực tự do cho những người đến sớm', 
	'General admission area for early arrivals',
	'["Vòng tay vải"]'::jsonb, NOW()
FROM public.events WHERE name = 'Ravolution Music Festival: Unite';

-- Zones cho sự kiện: Saigon Tếu
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Ghế gần sân khấu', false, 40, 0, 400000, 
	'Ghế ngồi gần sân khấu, tương tác trực tiếp với diễn viên', 
	'Seats close to the stage for direct interaction with performers',
	'["Một ly nước"]'::jsonb, NOW()
FROM public.events WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Ghế thường', false, 60, 0, 250000, 
	'Ghế ngồi tiêu chuẩn', 
	'Standard seating',
	NULL, NOW()
FROM public.events WHERE name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Zones cho sự kiện: Vietnam Tech Summit
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'VIP Premium', false, 50, 0, 800000, 
	'Ghế ngồi VIP với đặc quyền đặc biệt', 
	'VIP seating with exclusive privileges',
	'["Tài liệu VIP", "Suất ăn trưa cao cấp", "Networking session"]'::jsonb, NOW()
FROM public.events WHERE name = 'Vietnam Tech Summit 2025';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Standard', false, 200, 0, 400000, 
	'Ghế ngồi tiêu chuẩn, tầm nhìn bao quát sân khấu', 
	'Standard seating with a full view of the stage',
	'["Tài liệu hội thảo", "Teabreak giữa giờ"]'::jsonb, NOW()
FROM public.events WHERE name = 'Vietnam Tech Summit 2025';

-- Zones cho sự kiện: VBA 2025
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Courtside VIP', false, 50, 0, 2500000, 
	'Ghế ngồi sát sàn đấu, cảm nhận từng bước chạy của cầu thủ', 
	'Courtside seats to feel every move of the players',
	'["Áo đấu phiên bản giới hạn", "F&B phục vụ tại chỗ"]'::jsonb, NOW()
FROM public.events WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Standard Bleachers', false, 800, 0, 150000, 
	'Khu vực khán đài tiêu chuẩn', 
	'Standard bleachers area',
	NULL, NOW()
FROM public.events WHERE name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Zones cho sự kiện: Workshop Marketing
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Premium (Kèm tài liệu)', false, 50, 0, 450000, 
	'Gói Premium với tài liệu đầy đủ và chứng chỉ', 
	'Premium package with full documentation and certificate',
	'["Tài liệu in màu", "Chứng chỉ", "Networking lunch"]'::jsonb, NOW()
FROM public.events WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Standard', false, 100, 0, 200000, 
	'Gói tham dự tiêu chuẩn', 
	'Standard attendance package',
	'["Tài liệu PDF"]'::jsonb, NOW()
FROM public.events WHERE name = 'Workshop: Marketing 0 Đồng cho Startup';

-- Zones cho sự kiện: Van Gogh
INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'VIP (Kèm quà tặng)', false, 200, 0, 1200000, 
	'Trải nghiệm nghệ thuật đa giác quan không giới hạn', 
	'Unlimited multi-sensory art experience',
	'["Sổ tay Van Gogh", "Ưu tiên chụp ảnh tại khu vực đặc biệt"]'::jsonb, NOW()
FROM public.events WHERE name = 'Van Gogh Art Lighting Experience';

INSERT INTO public.zones (id, event_id, name, is_standing, capacity, quantity_sold, price, description_vi, description_en, perks, created_at)
SELECT uuidv7(), id, 'Standard', false, 1000, 0, 650000, 
	'Vé tham quan tiêu chuẩn', 
	'Standard admission ticket',
	'["Tờ giới thiệu"]'::jsonb, NOW()
FROM public.events WHERE name = 'Van Gogh Art Lighting Experience';

--4. Table: Seats (Tạo ghế cho zone VVIP của sự kiện Hà Anh Tuấn)
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


