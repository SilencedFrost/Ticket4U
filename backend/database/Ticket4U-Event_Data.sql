--1. Table: categories 
TRUNCATE TABLE public.categories CASCADE;
ALTER SEQUENCE public.categories_id_seq RESTART WITH 1;

INSERT INTO public.categories (id, name) VALUES 
(1, 'Âm nhạc'), 
(2, 'Hội thảo'), 
(3, 'Sân khấu - Nghệ thuật'), 
(4, 'Thể thao'), 
(5, 'Giải trí về đêm');

SELECT setval('public.categories_id_seq', 5);

--2. Table: Venues
INSERT INTO public.venues (id, address_line, longitude, latitude, json_layout, image_url, created_at, updated_at) VALUES
-- CIS Arena (Saigon Heat)
(uuidv7(), 'CIS Arena, Quận 7, TP. Hồ Chí Minh', 106.719368, 10.731490, NULL, 'https://s.inyourpocket.com/gallery/178413.jpg', NOW(), NULL),

-- Nhà Hát Bến Thành (Kịch Idecaf)
(uuidv7(), 'Nhà Hát Bến Thành, Quận 1, TP. Hồ Chí Minh', 106.690864, 10.769444, NULL, 'http://vietlandmarks.com/upload/142582138454fc4ec85a5d2.jpg', NOW(), NULL),

-- Mây Lang Thang Đà Lạt
(uuidv7(), 'Mây Lang Thang, Phường 4, Thành phố Đà Lạt, Tỉnh Lâm Đồng', 108.436867, 11.934155, NULL, 'https://media2.gody.vn/public/images/place/may-lang-thang-da-lat/614c498b500ff-1632389515.jpeg', NOW(), NULL),

-- GEM Center (Tech Summit)
(uuidv7(), 'GEM Center, Quận 1, TP. Hồ Chí Minh', 106.697150, 10.772510, NULL, 'https://images2.thanhnien.vn/528068263637045248/2023/2/22/1-gem-center-16770590914701989789155.jpg', NOW(), NULL),

-- Cà phê Tinh Tế (Saigon Tếu)
(uuidv7(), 'Cà phê Tinh Tế, Quận 3, TP. Hồ Chí Minh', 106.686500, 10.786200, NULL, 'https://ik.imagekit.io/tvlk/blog/2022/10/quan-cafe-nhieu-cay-xanh-tphcm-1.jpeg?tr=q-70,c-at_max,w-500,h-300,dpr-2', NOW(), NULL),

-- Dreamplex Điện Biên Phủ (Workshop)
(uuidv7(), 'Dreamplex Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh', 106.707300, 10.799800, NULL, 'https://maisonoffice.vn/wp-content/uploads/2021/09/van-phong-cho-thue-dreamplex-195-dien-bien-phu.jpg', NOW(), NULL),

-- Gigamall Thủ Đức (Van Gogh)
(uuidv7(), 'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh', 106.761789, 10.847910, NULL, 'https://bidiland.vn/dataweb/images/tin-tuc/tong-hop-nhung-sieu-thi-va-cho-khu-vuc-quan-9-quan-2-quan-thu-duc-thanh-pho-thu-duc-coop-extra-gigamall-bidiland(1).jpg', NOW(), NULL),

-- Sân Lễ Hội Đền Hùng Ninh Bình (Hà Anh Tuấn)
(uuidv7(), 'Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình', 105.945023, 20.317891, NULL, 'https://mtcs.1cdn.vn/2023/02/16/le-hoi-den-hung.jpg', NOW(), NULL),

-- Công viên Yên Sở Hà Nội (Những Thành Phố Mơ Màng)
(uuidv7(), 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội', 105.850234, 20.976543, NULL, 'https://gamudagardens.vn/wp-content/uploads/2016/09/ho-yen-so.jpg', NOW(), NULL),

-- Khu đô thị Vạn Phúc (Ravolution)
(uuidv7(), 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh', 106.781234, 10.853456, NULL, 'https://khudothivanphuc.vn/wp-content/uploads/2021/07/cong-vien-ocean-world-van-phuc.jpg', NOW(), NULL);

--3. Table: Events
INSERT INTO public.events (
	id, name, organizer_id, category_id, address_line, 
	status, banner_url,
	created_at, updated_at,
	about_vi, about_en, terms_and_conditions, policy_refund, seating_plan_image_url,
	venue_id, longitude, latitude
) VALUES
-- Hà Anh Tuấn: Chân Trời Rực Rỡ
(uuidv7(), 'Hà Anh Tuấn: Chân Trời Rực Rỡ', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình', 
	'PREMIERE', 
	'https://salt.tkbcdn.com/ts/ds/25/e6/b4/d79786df1e38c39beabe33c462cc381e.jpg', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://images.tkbcdn.com/1/1560/600/Upload/eventcover/2022/12/23/450B75.jpg" alt="Hà Anh Tuấn" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #d63384;">
			HÀ ANH TUẤN - CHÂN TRỜI RỰC RỠ
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Đêm nhạc huyền thoại với giọng ca đầy cảm xúc của Hà Anh Tuấn, cùng những giai điệu bất hủ mang đến không gian âm nhạc đầy mê hoặc. Hãy hòa mình vào không gian mộng mơ giữa đất trời Ninh Bình.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình<br>
			Thời gian mở cửa: 2 giờ trước giờ diễn ra
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://images.tkbcdn.com/1/1560/600/Upload/eventcover/2022/12/23/450B75.jpg" alt="Ha Anh Tuan" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #d63384;">
			HÀ ANH TUẤN - BRILLIANT HORIZON
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			A legendary music night featuring the emotional voice of Ha Anh Tuan with timeless melodies creating an enchanting musical experience. Immerse yourself in the dreamy atmosphere of Ninh Binh.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: Den Hung Festival Ground, Gia Vien District, Ninh Binh Province<br>
			Doors open: 2 hours before showtime
		</p>
	</div>', 
	'<p>Cấm trẻ em dưới 6 tuổi. Không ảnh hưởng đến khán giả khác.</p>', 
	'<p>Không hoàn tiền sau khi mua. Chỉ hỗ trợ đổi vé trong trường hợp đặc biệt.</p>', 
	'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a',
	(SELECT id FROM public.venues WHERE address_line = 'Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình'), 105.945023, 20.317891),

-- VBA 2025: Saigon Heat
(uuidv7(), 'VBA 2025: Saigon Heat vs Hanoi Buffaloes', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 4, 'CIS Arena, Quận 7, TP. Hồ Chí Minh', 
	'FINISHED', 
	'https://cdn.nextix.cloud/nextix/81/saigonheat_1_2996fb89aa.png?updated_at=2024-06-20T06:55:12.533Z', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://cdn.nextix.cloud/nextix/81/saigonheat_1_2996fb89aa.png?updated_at=2024-06-20T06:55:12.533Z" alt="Saigon Heat" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #dc3545;">
			SAIGON HEAT - TRẬN ĐẤU TRÊN SÂN NHÀ
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Chào đón người hâm mộ quay trở lại chảo lửa CIS. Hãy cùng tiếp thêm sức mạnh cho các chiến binh Saigon Heat trong cuộc đối đầu quan trọng sắp tới. Trận đấu hứa hẹn mang đến những giây phút nghẹt thở và kịch tính đến tận giây cuối cùng.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Nhà thi đấu trường Quốc tế CIS, Quận 7<br>
			Thời gian mở cửa: 1 giờ trước giờ thi đấu
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://cdn.nextix.cloud/nextix/81/saigonheat_1_2996fb89aa.png?updated_at=2024-06-20T06:55:12.533Z" alt="Saigon Heat" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #dc3545;">
			SAIGON HEAT - HOME GAME
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Welcome fans back to the CIS arena. Let''s support the Saigon Heat warriors in this crucial matchup. The game promises breathtaking moments and excitement until the final buzzer.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: CIS International School Arena, District 7<br>
			Doors open: 1 hour before tip-off
		</p>
	</div>', 
	'<p>Không gây rối, không ảnh hưởng đến người khác.</p>', 
	'<p>Hoàn vé theo quy định của BTC trong trường hợp trận đấu bị hủy.</p>', 
	'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg',
	(SELECT id FROM public.venues WHERE address_line = 'CIS Arena, Quận 7, TP. Hồ Chí Minh'), 106.719368, 10.731490),

-- Những Thành Phố Mơ Màng
(uuidv7(), 'Những Thành Phố Mơ Màng - Summer Tour', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội', 
	'PREMIERE', 
	'https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg" alt="Những Thành Phố Mơ Màng" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #0d6efd;">
			NHỮNG THÀNH PHỐ MƠ MÀNG - SUMMER TOUR
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Hành trình âm nhạc indie đầy mộng mơ với sự góp mặt của Đen Vâu, Chillies, và Vũ. Cùng hòa mình vào không gian âm nhạc đầy màu sắc giữa thiên nhiên xanh mát.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Công viên Yên Sở, Quận Hoàng Mai, Hà Nội<br>
			Thời gian mở cửa: 3 giờ trước giờ diễn ra
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg" alt="Dreamy Cities" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #0d6efd;">
			DREAMY CITIES - SUMMER TOUR
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			A dreamy indie music journey featuring Den Vau, Chillies, and Vu. Immerse yourself in colorful music surrounded by lush greenery.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: Yen So Park, Hoang Mai District, Hanoi<br>
			Doors open: 3 hours before showtime
		</p>
	</div>', 
	'<p>Vui lòng mang theo CCCD khi check-in. Trang phục thoải mái.</p>', 
	'<p>Không hoàn trả vé sau khi mua.</p>', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
	(SELECT id FROM public.venues WHERE address_line = 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội'), 105.850234, 20.976543),

-- Mây Lang Thang
(uuidv7(), 'Mây Lang Thang: Đêm Nhạc Trịnh', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 1, 'Mây Lang Thang, Phường 4, Thành phố Đà Lạt, Tỉnh Lâm Đồng', 
	'PREMIERE', 
	'https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg" alt="Mây Lang Thang" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #198754;">
			MÂY LANG THANG - ĐÊM NHẠC TRỊNH
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Thưởng thức những giai điệu nhạc Trịnh bất hủ giữa khung cảnh đồi thông mộng mơ của Đà Lạt. Một đêm nhạc đầy cảm xúc và lãng mạn.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Mây Lang Thang, Phường 4, Đà Lạt<br>
			Thời gian mở cửa: 1 giờ trước giờ diễn ra
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg" alt="May Lang Thang" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #198754;">
			MÂY LANG THANG - TRINH MUSIC NIGHT
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Enjoy timeless Trinh melodies amidst the dreamy pine hills of Dalat. An emotional and romantic music night.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: May Lang Thang, Ward 4, Dalat<br>
			Doors open: 1 hour before showtime
		</p>
	</div>', 
	'<p>Nên mang theo áo khoác ấm do thời tiết Đà Lạt se lạnh.</p>', 
	'<p>Không hoàn tiền vé.</p>', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
	(SELECT id FROM public.venues WHERE address_line = 'Mây Lang Thang, Phường 4, Thành phố Đà Lạt, Tỉnh Lâm Đồng'), 108.436867, 11.934155),

-- Kịch Idecaf
(uuidv7(), 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 3, 'Nhà Hát Bến Thành, Quận 1, TP. Hồ Chí Minh', 
	'PREMIERE', 
	'https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png" alt="Ngày Xửa Ngày Xưa" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #fd7e14;">
			KỊCH IDECAF - NGÀY XỬA NGÀY XƯA 35
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Chương trình kịch thiếu nhi được mong chờ nhất hè này. Vở diễn huyền thoại với những câu chuyện cổ tích đầy màu sắc và ý nghĩa.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Nhà Hát Bến Thành, Quận 1, TP. HCM<br>
			Thời gian mở cửa: 30 phút trước giờ diễn
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png" alt="Once Upon A Time" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #fd7e14;">
			IDECAF THEATER - ONCE UPON A TIME 35
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			The most anticipated children''s theater show this summer. A legendary performance with colorful and meaningful fairy tales.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: Ben Thanh Theater, District 1, HCMC<br>
			Doors open: 30 minutes before showtime
		</p>
	</div>', 
	'<p>Mỗi vé dành cho một người, trẻ em cần có người lớn đi kèm.</p>', 
	'<p>Không hoàn tiền.</p>', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
	(SELECT id FROM public.venues WHERE address_line = 'Nhà Hát Bến Thành, Quận 1, TP. Hồ Chí Minh'), 106.690864, 10.769444),

-- Ravolution Music Festival
(uuidv7(), 'Ravolution Music Festival: Unite', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 1, 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh', 
	'PREMIERE', 
	'https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg" alt="Ravolution" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #6610f2;">
			RAVOLUTION MUSIC FESTIVAL: UNITE
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Lễ hội âm nhạc điện tử quốc tế lớn nhất khu vực với dàn DJ Top 100 thế giới. Hãy sẵn sàng cho một đêm bùng nổ không giới hạn.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Khu đô thị Vạn Phúc, Thành phố Thủ Đức<br>
			Thời gian mở cửa: 4 giờ chiều
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg" alt="Ravolution" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #6610f2;">
			RAVOLUTION MUSIC FESTIVAL: UNITE
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			The biggest international EDM festival in the region featuring Top 100 DJs worldwide. Get ready for an unlimited explosive night.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: Van Phuc Urban Area, Thu Duc City<br>
			Doors open: 4 PM
		</p>
	</div>', 
	'<p>Trang phục tự do, không mang chất cấm. Cấm trẻ em dưới 16 tuổi.</p>', 
	'<p>Hoàn tiền 50% trước 7 ngày diễn ra.</p>', 
	'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg',
	(SELECT id FROM public.venues WHERE address_line = 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh'), 106.781234, 10.853456),

-- Saigon Tếu: Hài Độc Thoại
(uuidv7(), 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 3, 'Cà phê Tinh Tế, Quận 3, TP. Hồ Chí Minh', 
	'PREMIERE', 
	'https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg" alt="Saigon Tếu" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #ffc107;">
			SAIGON TẾU - HÀI ĐỘC THOẠI LẺ LOI
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Đêm hài độc thoại giải tỏa căng thẳng cuối tuần với những câu chuyện đời thường đầy hài hước. Cười thoải mái và quên đi mệt mỏi.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Cà phê Tinh Tế, Quận 3, TP. HCM<br>
			Thời gian mở cửa: 30 phút trước giờ diễn
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg" alt="Saigon Teu" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #ffc107;">
			SAIGON TEU - STAND-UP COMEDY
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			A stand-up comedy night to unwind your weekend stress with hilarious everyday stories. Laugh freely and forget your worries.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: Tinh Te Cafe, District 3, HCMC<br>
			Doors open: 30 minutes before showtime
		</p>
	</div>',
	'<p>Vui lòng không quay phim trong buổi diễn.</p>', 
	'<p>Không hoàn trả vé.</p>', 
	'https://salt.tkbcdn.com/ts/ds/3d/50/7e/6c4edb2e7de908c144c81189ef0e3a2c.png',
	(SELECT id FROM public.venues WHERE address_line = 'Cà phê Tinh Tế, Quận 3, TP. Hồ Chí Minh'), 106.686500, 10.786200),

-- Vietnam Tech Summit 2025
(uuidv7(), 'Vietnam Tech Summit 2025', '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 2, 'GEM Center, Quận 1, TP. Hồ Chí Minh', 
	'PREMIERE', 
	'https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg" alt="Tech Summit" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #0dcaf0;">
			VIETNAM TECH SUMMIT 2025
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Hội thảo công nghệ lớn nhất năm với sự tham gia của các chuyên gia hàng đầu trong ngành AI, Cloud Computing và Blockchain.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: GEM Center, Quận 1, TP. HCM<br>
			Thời gian mở cửa: 8:00 AM
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg" alt="Tech Summit" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #0dcaf0;">
			VIETNAM TECH SUMMIT 2025
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			The biggest technology conference of the year featuring top experts in AI, Cloud Computing, and Blockchain industries.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: GEM Center, District 1, HCMC<br>
			Doors open: 8:00 AM
		</p>
	</div>',
	'<p>Khuyến khích mang theo laptop cá nhân.</p>', 
	'<p>Hoàn tiền 50% nếu hủy trước 5 ngày.</p>', 
	'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a',
	(SELECT id FROM public.venues WHERE address_line = 'GEM Center, Quận 1, TP. Hồ Chí Minh'), 106.697150, 10.772510),

-- Workshop Marketing 0 Đồng
(uuidv7(), 'Workshop: Marketing 0 Đồng cho Startup', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 2, 'Dreamplex Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh', 
	'PREMIERE', 
	'https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png" alt="Marketing Workshop" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #20c997;">
			MARKETING 0 ĐỒNG CHO STARTUP
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Chia sẻ bí quyết tăng trưởng không cần ngân sách lớn. Học cách tận dụng organic marketing và growth hacking cho startup.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Dreamplex Điện Biên Phủ, Quận Bình Thạnh<br>
			Thời gian mở cửa: 9:00 AM
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png" alt="Marketing Workshop" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #20c997;">
			ZERO-BUDGET MARKETING FOR STARTUPS
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Sharing growth secrets without a big budget. Learn to leverage organic marketing and growth hacking for your startup.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: Dreamplex Dien Bien Phu, Binh Thanh District<br>
			Doors open: 9:00 AM
		</p>
	</div>',
	'<p>Khuyến khích mang theo laptop cá nhân.</p>', 
	'<p>Hoàn tiền 50% nếu hủy trước 3 ngày.</p>', 
	'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a',
	(SELECT id FROM public.venues WHERE address_line = 'Dreamplex Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh'), 106.707300, 10.799800),

-- Van Gogh Art Experience
(uuidv7(), 'Van Gogh Art Lighting Experience', '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 3, 'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh', 
	'PREMIERE', 
	'https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990', 
	NOW(), NULL,
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990" alt="Van Gogh Experience" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #ffd700;">
			VAN GOGH ART LIGHTING EXPERIENCE
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Triển lãm nghệ thuật tương tác đa giác quan với công nghệ ánh sáng hiện đại. Đắm chìm trong thế giới tranh của Van Gogh.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Địa điểm: Gigamall Thủ Đức, TP. Thủ Đức<br>
			Thời gian mở cửa: 10:00 AM - 10:00 PM
		</p>
	</div>',
	'<div style="max-width: 800px; margin: auto; font-family: ''Open Sans'', sans-serif; text-align: center;">
		<img src="https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990" alt="Van Gogh Experience" style="width: 100%; height: auto; margin-bottom: 30px;">
		<h1 style="font-size: 2rem; font-weight: 700; margin-bottom: 20px; color: #ffd700;">
			VAN GOGH ART LIGHTING EXPERIENCE
		</h1>
		<p style="line-height: 1.8; font-size: 1.1rem; margin-bottom: 20px;">
			Multi-sensory interactive art exhibition with modern lighting technology. Immerse yourself in Van Gogh''s world of paintings.
		</p>
		<p style="font-size: 1rem; color: #6c757d;">
			Venue: Gigamall Thu Duc, Thu Duc City<br>
			Opening hours: 10:00 AM - 10:00 PM
		</p>
	</div>',
	'<p>Mỗi lượt tham quan tối đa 60 phút.</p>', 
	'<p>Không hỗ trợ hoàn tiền.</p>', 
	'https://salt.tkbcdn.com/ts/ds/22/11/d9/bb3f49b5cc337eb5e7d02d413bc64453.jpg',
	(SELECT id FROM public.venues WHERE address_line = 'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh'), 106.761789, 10.847910);

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
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'VVIP', false, 50, 0, 2500000, 
	'Khu vực VIP cao cấp với vị trí đẹp nhất, dịch vụ đặc biệt và nhiều ưu đãi độc quyền. Ghế ngồi hạng sang gần sân khấu, phục vụ đồ uống cao cấp, quà tặng đặc biệt.', 
	'Premium VIP area with the best location, exclusive services and special privileges. Luxury seats near the stage, premium beverage service, special gift package.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Vòng tay check-in VIP", "Goodie bag", "Meet & Greet"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Standing', true, 800, 0, 650000, 
	'Khu vực đứng tự do gần sân khấu, trải nghiệm âm nhạc bùng nổ cùng đám đông.', 
	'Standing area near the stage for an explosive music experience with the crowd.',
	NULL,
	'["Vòng tay check-in", "Nước suối"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- Zones cho Saigon Heat
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Courtside VIP', false, 50, 0, 2500000, 
	'Ghế ngồi sát sàn đấu, cảm nhận từng bước chạy của cầu thủ. Ghế ngồi VIP sát sân, áo đấu phiên bản giới hạn, F&B phục vụ tại chỗ.', 
	'Courtside seats to feel every move of the players. VIP courtside seats, limited edition jersey, F&B service at seat.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Áo đấu phiên bản giới hạn", "F&B phục vụ tại chỗ"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Standard Bleachers', false, 800, 0, 150000, 
	'Khu vực khán đài tiêu chuẩn với tầm nhìn tốt. Giá cả phải chăng cho người hâm mộ.', 
	'Standard bleachers area with good view. Affordable price for fans.',
	NULL,
	NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'VBA 2025: Saigon Heat vs Hanoi Buffaloes';

-- Zones cho Những Thành Phố Mơ Màng
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Cư Dân VIP', false, 200, 0, 1500000, 
	'Đặc quyền cư dân VIP với khu vực nghỉ ngơi riêng, quà tặng độc quyền, lối đi ưu tiên và nước uống miễn phí cả ngày.', 
	'VIP resident privileges with private lounge, exclusive gifts, priority access and unlimited beverages.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Bộ quà tặng Cư Dân", "Lối đi ưu tiên", "Nước uống miễn phí"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Thường', true, 2000, 0, 650000, 
	'Khu vực tự do dành cho các cư dân yêu âm nhạc. Hòa mình vào không khí sôi động của festival.', 
	'General admission area for music-loving residents. Immerse in the vibrant festival atmosphere.',
	NULL,
	'["Vòng tay vải", "Sticker"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Những Thành Phố Mơ Màng - Summer Tour';

-- Zones cho Mây Lang Thang
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu VVIP (Gần ca sĩ)', false, 50, 0, 1800000, 
	'Vị trí đẹp nhất để tương tác cùng nghệ sĩ, thưởng thức nhạc Trịnh bất hủ. Một phần nước tự chọn cao cấp, đĩa CD nhạc Trịnh kỷ niệm, cơ hội chụp ảnh với nghệ sĩ.', 
	'The best spot to interact with the artist, enjoying timeless Trinh melodies. Premium beverage of choice, commemorative CD, photo opportunity with artist.',
	NULL,
	'["Một phần nước tự chọn", "Đĩa CD nhạc Trịnh"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu Khán Đài', false, 200, 0, 800000, 
	'Khu vực ngồi thoải mái với tầm nhìn đẹp, thưởng thức đêm nhạc trong không gian lãng mạn của Đà Lạt.', 
	'Comfortable seating area with beautiful view, enjoy the music night in romantic Dalat atmosphere.',
	NULL,
	'["Nước uống", "Chăn len"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Mây Lang Thang: Đêm Nhạc Trịnh';

-- Zones cho Kịch Idecaf
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu A (Lầu 1)', false, 300, 0, 350000, 
	'Khu vực lầu 1 gần sân khấu nhất, phù hợp cho trẻ em để có trải nghiệm tốt nhất.', 
	'Floor 1 area closest to the stage, perfect for children to have the best experience.',
	NULL,
	NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu B (Lầu 2)', false, 200, 0, 250000, 
	'Khu vực lầu 2 với tầm nhìn toàn cảnh, giá cả phải chăng.', 
	'Floor 2 area with panoramic view, affordable price.',
	NULL,
	NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35';

-- Zones cho Ravolution
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'SVIP Deck', false, 100, 0, 4500000, 
	'Tận hưởng lễ hội trên khán đài cao cấp với không gian riêng tư. Private Bar với bartender riêng, quà tặng từ nhà tài trợ cao cấp, lối đi VIP không xếp hàng, khu vực nghỉ ngơi riêng biệt.', 
	'Enjoy the festival from premium deck with private space. Private Bar with dedicated bartender, premium sponsor gifts, VIP access with no queuing, private lounge area.',
	'https://salt.tkbcdn.com/ts/ds/3f/2b/6d/05bfae404c85f7ba8f3b8f1c982b86ab.png',
	'["Private Bar", "Quà tặng từ nhà tài trợ", "Lối đi VIP"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Ravolution Music Festival: Unite';

INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'GA Early Bird', true, 1500, 0, 850000, 
	'Khu vực tự do cho những người đến sớm, hòa mình vào không khí EDM bùng nổ cùng hàng nghìn fan hâm mộ.', 
	'General admission area for early arrivals, immerse in explosive EDM atmosphere with thousands of fans.',
	NULL,
	'["Vòng tay vải"]'::jsonb, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Ravolution Music Festival: Unite';

-- Zones cho Saigon Tếu (FREE)
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu ngồi chính', false, 200, 0, 0, 
	NULL, NULL, NULL, NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Zones cho Vietnam Tech Summit (FREE)
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Hội trường chính', false, 500, 0, 0, 
	NULL, NULL, NULL, NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Vietnam Tech Summit 2025';

-- Zones cho Workshop Marketing (FREE)
INSERT INTO public.zones (id, session_id, name, is_standing, capacity, quantity_sold, price, 
	description_vi, description_en, gift_image_url, perks, created_at, updated_at)
SELECT uuidv7(), es.id, 'Khu học viên', false, 100, 0, 0, 
	NULL, NULL, NULL, NULL, NOW(), NULL
FROM public.event_sessions es
JOIN public.events e ON es.event_id = e.id
WHERE e.name = 'Workshop: Marketing 0 Đồng cho Startup';

--5. Table: Seats 
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
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 5) r 
CROSS JOIN generate_series(1, 10) c 
WHERE z.name = 'VVIP' AND e.name = 'Hà Anh Tuấn: Chân Trời Rực Rỡ';

-- Seats cho Saigon Tếu (FREE)
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
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 20) r 
CROSS JOIN generate_series(1, 10) c 
WHERE z.name = 'Khu ngồi chính' AND e.name = 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi';

-- Seats cho Vietnam Tech Summit (FREE)
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
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 25) r 
CROSS JOIN generate_series(1, 20) c 
WHERE z.name = 'Hội trường chính' AND e.name = 'Vietnam Tech Summit 2025';

-- Seats cho Workshop Marketing (FREE)
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
JOIN public.event_sessions es ON z.session_id = es.id
JOIN public.events e ON es.event_id = e.id
CROSS JOIN generate_series(1, 10) r 
CROSS JOIN generate_series(1, 10) c 
WHERE z.name = 'Khu học viên' AND e.name = 'Workshop: Marketing 0 Đồng cho Startup';


