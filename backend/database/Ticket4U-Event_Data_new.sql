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
--Basic info
INSERT INTO public.events(id, organizer_id, status, name, banner_url, created_at) VALUES
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'V-Glow: The Cyber-Heritage Night'           , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'The Echo of An Nam'                         , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026'    , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'CỔ NGHỆ KIÊU HÙNG'                          , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'NEO-LUMINANCE: The Echo of Indochine'       , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Sắc Lam: The Indigo Echo'                   , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'CRYSTAL REALM: The Neon Garden'             , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'SẮT & SON'                                  , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Techno-Sorcery: The Zenith of AI & Robotics', '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Vũ Trụ Cận Thị'                             , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'THE DREAMCATCHER ARCHIPELAGO'               , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'KAIZEN: The Art of Precision'               , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'LUVIA: The Echo of Highlands'               , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Scent of the Soul: The Echoes of Vietnam'   , '{}'::jsonb, now()),
(uuidv7(), '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'PREMIERE', 'Âm Sắc Việt - THE RESONANCE'                , '{}'::jsonb, now());

--Urls

UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775381970/vglow-wide_t6cooz.png", "square": "https://cdn.ticket4u.uk/v1775381967/vglow-sqre_nmypgq.png", "tall": "https://cdn.ticket4u.uk/v1775381974/vglow-tall_wgekmk.png"}'::jsonb WHERE name = 'V-Glow: The Cyber-Heritage Night';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775381964/aecho-wide_cdgl6u.png", "square": "https://cdn.ticket4u.uk/v1775381950/aecho-sqre_efweqg.png", "tall": "https://cdn.ticket4u.uk/v1775381948/aecho-tall_xkm5ic.png"}'::jsonb WHERE name = 'The Echo of An Nam';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775382276/beats-wide_kq6cve.png", "square": "https://cdn.ticket4u.uk/v1775382269/beats-sqre_kf8szt.png", "tall": "https://cdn.ticket4u.uk/v1775382271/beats-tall_nzkxux.png"}'::jsonb WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775382586/tloom-wide_nnsqej.png", "square": "https://cdn.ticket4u.uk/v1775382590/tloom-sqre_zqnaam.png", "tall": "https://cdn.ticket4u.uk/v1775382588/tloom-tall_wlhtt2.png"}'::jsonb WHERE name = 'CỔ NGHỆ KIÊU HÙNG';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775382880/neolu-wide_wiwf4z.png", "square": "https://cdn.ticket4u.uk/v1775382884/neolu-sqre_y2ofzz.png", "tall": "https://cdn.ticket4u.uk/v1775382886/neolu-tall_spbtwy.png"}'::jsonb WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775383130/sclam-wide_ymvfhg.png", "square": "https://cdn.ticket4u.uk/v1775383104/sclam-sqre_uaxxnl.png", "tall": "https://cdn.ticket4u.uk/v1775383057/sclam-tall_froaue.png"}'::jsonb WHERE name = 'Sắc Lam: The Indigo Echo';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775383196/cryst-wide_ccxmzc.png", "square": "https://cdn.ticket4u.uk/v1775383192/cryst-sqre_e76ssg.png", "tall": "https://cdn.ticket4u.uk/v1775383192/cryst-tall_h04ljf.png"}'::jsonb WHERE name = 'CRYSTAL REALM: The Neon Garden';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775383391/satso-wide_vlujh3.png", "square": "https://cdn.ticket4u.uk/v1775383341/satso-sqre_vu6tau.png", "tall": "https://cdn.ticket4u.uk/v1775383338/satso-tall_dgbelz.png"}'::jsonb WHERE name = 'SẮT & SON';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775383504/techs-wide_ekyhab.png", "square": "https://cdn.ticket4u.uk/v1775383502/techs-sqre_ah00iv.png", "tall": "https://cdn.ticket4u.uk/v1775383508/techs-tall_q41wkb.png"}'::jsonb WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775383818/canth-wide_m4gbop.png", "square": "https://cdn.ticket4u.uk/v1775383821/canth-sqre_im0awy.png", "tall": "https://cdn.ticket4u.uk/v1775383941/dream-tall_fkwtkk.png"}'::jsonb WHERE name = 'Vũ Trụ Cận Thị';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775383940/dream-wide_knggr2.png", "square": "https://cdn.ticket4u.uk/v1775383942/dream-sqre_zlwxep.png", "tall": "https://cdn.ticket4u.uk/v1775383941/dream-tall_fkwtkk.png"}'::jsonb WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775384284/kzart-wide_qvhiub.png", "square": "https://cdn.ticket4u.uk/v1775384345/kzart-sqre_v3harc.png", "tall": "https://cdn.ticket4u.uk/v1775384294/kzart-tall_lphcur.png"}'::jsonb WHERE name = 'KAIZEN: The Art of Precision';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775384450/luvia-wide_gyizyq.png", "square": "https://cdn.ticket4u.uk/v1775384446/luvia-sqre_lqtogb.png", "tall": "https://cdn.ticket4u.uk/v1775384451/luvia-tall_dzcyjo.png"}'::jsonb WHERE name = 'LUVIA: The Echo of Highlands';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775390582/echov-wide_paluak.png", "square": "https://cdn.ticket4u.uk/v1775390593/echov-sqre_huj7ep.png", "tall": "https://cdn.ticket4u.uk/v1775390592/echov-tall_tyfbot.png"}'::jsonb WHERE name = 'Scent of the Soul: The Echoes of Vietnam';
UPDATE public.events SET banner_url = '{"wide": "https://cdn.ticket4u.uk/v1775390591/reson-wide_aossid.png", "square": "https://cdn.ticket4u.uk/v1775390583/reson-sqre_igosvf.png", "tall": "https://cdn.ticket4u.uk/v1775390583/reson-tall_segvto.png"}'::jsonb WHERE name = 'Âm Sắc Việt - THE RESONANCE';

-- Descriptions

UPDATE public.events SET 
about_vi = 'V-Glow 2026 là lễ hội âm nhạc và công nghệ đột phá tại TP.HCM, kết hợp giữa nét đẹp Trung Thu truyền thống và phong cách Cyberpunk tương lai. Với sự góp mặt của các ngôi sao V-Pop hàng đầu, triển lãm nghệ thuật AR và diễu hành lồng đèn số, đây là điểm đến không thể bỏ qua cho cộng đồng fan Gen Z.', 
about_en = 'V-Glow 2026 is a revolutionary fan-fest in Ho Chi Minh City, merging Vietnamese traditional mid-autumn heritage with futuristic Cyberpunk aesthetics. Featuring top V-Pop idols, immersive AR art, and a digital lantern parade, it''s the ultimate cultural-tech experience for the Gen Z fanbase.' 
where name = 'V-Glow: The Cyber-Heritage Night';

UPDATE public.events SET 
about_vi = '"Tiếng Vọng An Nam" là lễ hội đêm quy mô lớn tại TP.HCM năm 2026, kết hợp giữa di sản truyền thống và công nghệ tương lai. Với sự góp mặt của các nghệ sĩ hàng đầu như Gemini Hùng Huỳnh và nghệ thuật số đa giác quan, đây là sự kiện văn hóa - công nghệ không thể bỏ lỡ.', 
about_en = '"The Echo of An Nam" is a premier 2026 night festival in Ho Chi Minh City blending traditional Vietnamese heritage with futuristic tech. Featuring top artists like Gemini Hung Huynh and immersive digital art, it''s the ultimate cultural-tech crossover for the modern fanbase.' 
where name = 'The Echo of An Nam';

UPDATE public.events SET 
about_vi = 'Trải nghiệm lễ hội âm nhạc và công nghệ đỉnh cao tại Sài Gòn Neon Beats 2026. Một sự kiện bùng nổ kết hợp giữa âm thanh EDM sôi động, trình diễn Drone Light Show và không gian tương tác ảo (AR) tại "Trung tâm mới" Global City. Đừng bỏ lỡ cơ hội gặp gỡ các nghệ sĩ hàng đầu và đắm mình trong kỷ nguyên số.', 
about_en = 'Experience the ultimate music and technology festival at Saigon Neon Beats 2026. An explosive event blending high-energy EDM, Drone Light Shows, and AR interactive zones at the "New City Center" - Global City. Don''t miss the chance to meet top artists and immerse yourself in the digital era.' 
where name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

UPDATE public.events SET 
about_vi = 'Đắm mình trong sự giao thoa giữa di sản nghìn năm và tư duy sáng tạo hiện đại tại "Cổ Nghệ Kiêu Hùng." Diễn ra tại Văn Miếu - Quốc Tử Giám, sự kiện mang đến những trải nghiệm trình diễn nghề thủ công trực tiếp, nhạc indie-folk và sàn diễn "Cổ phục" độc bản từ các nghệ nhân và nhà thiết kế trẻ hàng đầu.', 
about_en = 'Experience the fusion of Vietnam''s 1,000-year-old heritage and modern creative flair at "The Antique Alchemy." Join us at Hanoi''s historic Temple of Literature for a night of live traditional craftsmanship, indie-folk music, and an exclusive "Antique Runway" featuring the country''s top artisans and Gen-Z designers.' 
where name = 'CỔ NGHỆ KIÊU HÙNG';

UPDATE public.events SET 
about_vi = 'Khám phá NEO-LUMINANCE 2026, lễ hội âm nhạc điện tử và đa dòng nhạc ngoài trời quy mô lớn nhất tại TP.HCM. Với sự góp mặt của DJ/Nhà sản xuất nổi tiếng thế giới cùng các nghệ sĩ hàng đầu Việt Nam, sự kiện là sự giao thoa độc đáo giữa phong cách tương lai Cyberpunk và họa tiết Đông Dương truyền thống. Hãy cùng 25,000 khán giả tại SECC trải nghiệm một đêm hội ánh sáng công nghệ cao và những màn trình diễn âm nhạc bùng nổ.', 
about_en = 'Experience NEO-LUMINANCE 2026, Vietnam''s premier outdoor electronic and fusion music festival in Ho Chi Minh City. Featuring a headline performance by a world-renowned European DJ/Producer (unveiled soon) and top-tier local artists, this event blends futuristic cyberpunk aesthetics with traditional Indochine motifs. Join 25,000 fans at SECC for a night of high-tech light shows, immersive art, and the next wave of Asian-European musical synergy.' 
where name = 'NEO-LUMINANCE: The Echo of Indochine';

UPDATE public.events SET 
about_vi = 'Sắc Lam: The Indigo Echo là triển lãm nghệ thuật đa giác quan kết hợp giữa kỹ thuật nhuộm chàm truyền thống của Việt Nam và công nghệ trình chiếu ánh sáng hiện đại. Cùng khám phá linh hồn của vùng cao qua lăng kính đương đại ngay giữa lòng Sài Gòn.', 
about_en = 'Sắc Lam: The Indigo Echo is a premier immersive art expo blending traditional Vietnamese natural dyeing techniques with cutting-edge digital projection. Experience the soul of the highlands through a modern lens in the heart of Saigon.' 
where name = 'Sắc Lam: The Indigo Echo';

UPDATE public.events SET 
about_vi = 'Crystal Realm: Vườn Neon Huyền Ảo là lễ hội cosplay ngoài trời cao cấp ngay tại trung tâm TP.HCM. Với sân khấu LED khổng lồ, các khu vực chụp ảnh chuyên nghiệp và dàn khách mời là những cosplayer quốc tế hàng đầu, sự kiện là sự kết hợp hoàn mỹ giữa vẻ đẹp thiên nhiên và phong cách cyberpunk tương lai.', 
about_en = 'Crystal Realm: The Neon Garden is a premium outdoor cosplay festival in the heart of Ho Chi Minh City. Featuring a massive LED-integrated stage, professional photo zones, and a guest lineup of top-tier international cosplayers, this event blends the beauty of nature with futuristic cyberpunk aesthetics.' 
where name = 'CRYSTAL REALM: The Neon Garden';

UPDATE public.events SET 
about_vi = 'SẮT & SON là lễ hội âm nhạc Rock lớn nhất năm 2026 tại Sài Gòn, quy tụ những biểu tượng Rock huyền thoại và các ban nhạc Indie đầy nổi loạn. Với không gian công nghiệp gai góc hòa quyện cùng nét văn hóa truyền thống, sự kiện hứa hẹn mang đến những màn trình diễn rực lửa, đánh thức bản lĩnh và đam mê tự do của thế hệ trẻ Việt Nam.', 
about_en = 'SẮT & SON is the premier Rock festival of 2026 in Saigon, bringing together legendary rock icons and rebellious indie bands. Set in a gritty industrial atmosphere infused with traditional cultural motifs, the event promises high-octane performances that ignite the spirit of freedom and raw passion within Vietnam''s youth.' 
where name = 'SẮT & SON';

UPDATE public.events SET 
about_vi = 'Khám phá sự giao thoa giữa huyền bí cổ xưa và đổi mới tương lai tại Techno-Sorcery. Lễ hội duy nhất trong đêm hội tụ "phép thuật đen" của AI tiên tiến và robot hiệu suất cao, đặt trong không gian kiến trúc độc đáo của Bảo tàng Hà Nội. Hãy chứng kiến những cỗ máy biết tư duy và những khối thép chuyển động đầy linh hồn.', 
about_en = 'Experience the fusion of ancient mysticism and futuristic innovation at Techno-Sorcery. This one-night-only festival showcases the "dark magic" of advanced AI and high-performance robotics, set against the iconic architecture of the Hanoi Museum. Witness machines that think and steel that moves with a soul.' 
where name = 'Techno-Sorcery: The Zenith of AI & Robotics';

UPDATE public.events SET 
about_vi = 'Buổi ra mắt thân mật của "Lặng" – ban nhạc Indie-Dream Pop mới nổi. Một hành trình đa giác quan kết hợp giữa âm nhạc và nghệ thuật thị giác trừu tượng trong không gian biệt thự cổ kính giữa lòng Hà Nội.', 
about_en = 'An intimate, low-fidelity debut showcase for "Lặng," an emerging indie-dream pop band. Experience a sensory journey where music meets abstract visual art in one of Hanoi''s most iconic colonial villas.' 
where name = 'Vũ Trụ Cận Thị';

UPDATE public.events SET 
about_vi = 'Đắm chìm trong Đảo Quốc Mộng Mơ, lễ hội âm nhạc và nghệ thuật kỳ ảo tại The Global City, TP.HCM. Trải nghiệm không gian thần tiên mang phong cách Disneyland nhưng đậm chất tâm hồn Việt với sen neon khổng lồ, sân khấu mây bồng bềnh và những nghệ sĩ hàng đầu.', 
about_en = 'Step into The Dreamcatcher Archipelago, an immersive whimsical festival at The Global City, HCMC. Experience a "Disneyland-style" magic reimagined with Vietnamese soul, featuring floating neon lotuses, cloud-shrouded stages, and top-tier musical acts.' 
where name = 'THE DREAMCATCHER ARCHIPELAGO';

UPDATE public.events SET 
about_vi = 'Trải nghiệm Omakase cao cấp độc bản, kết hợp giữa kỹ nghệ truyền thống Nhật Bản và tinh hoa nguyên liệu mùa hè Việt Nam. Sự kiện giới hạn chỉ 24 khách cho một hành trình ẩm thực đầy cảm xúc.', 
about_en = 'An ultra-exclusive, high-end Omakase experience merging traditional Japanese craftsmanship with local Vietnamese seasonal treasures. Limited to 24 seats for an intimate journey of culinary storytelling.' 
where name = 'KAIZEN: The Art of Precision';

UPDATE public.events SET 
about_vi = 'LUVIA: The Echo of Highlands là một trải nghiệm âm thanh đa giác quan độc bản, kết hợp giữa âm nhạc ambient, tiếng vang của đại ngàn và nghệ thuật ánh sáng tại cao nguyên Mộc Châu. Diễn ra tại cây cầu kính Bạch Long kỷ lục, sự kiện mời gọi khán giả cùng "chạm" vào thiên nhiên qua những giai điệu bản địa được tái hiện bằng công nghệ âm thanh 360 độ, mang lại cảm giác thư thái và kết nối tâm hồn tuyệt đối.', 
about_en = 'LUVIA: The Echo of Highlands is a unique multi-sensory auditory experience blending ambient music, forest echoes, and light art in the heart of Moc Chau. Set against the backdrop of the record-breaking Bach Long Glass Bridge, this event invites guests to "touch" nature through indigenous melodies reimagined with 360-degree sound technology, offering ultimate relaxation and spiritual reconnection.' 
where name = 'LUVIA: The Echo of Highlands';

UPDATE public.events SET 
about_vi = 'Trải nghiệm "Hương Sắc Linh Hồn," buổi hòa nhạc 6D đa giác quan đầu tiên tại Việt Nam. Sự kết hợp giữa âm thanh vòm sống động và công nghệ kích hoạt mùi hương đồng bộ sẽ đưa người hâm mộ hành trình qua những vùng ký ức đặc trưng—từ rừng thông sương mù Đà Lạt đến hơi thở mặn mòi của Vịnh Hạ Long—trong khi thưởng thức các màn trình diễn trực tiếp từ những nghệ sĩ indie và dàn nhạc hàng đầu.', 
about_en = 'Experience "Scent of the Soul," Vietnam''s first 6D olfactory-immersive indoor concert. Melding high-fidelity spatial audio with synchronized scent-triggering technology, this event transports fans through the aromatic landscapes of Vietnam—from the misty pine forests of Da Lat to the salty breeze of Ha Long Bay—all while enjoying live performances from top local indie and orchestral artists.' 
where name = 'Scent of the Soul: The Echoes of Vietnam';

UPDATE public.events SET 
about_vi = 'Khám phá sức mạnh của tiếng Việt thông qua âm nhạc tại Âm Sắc Việt - THE RESONANCE. Một đêm nhạc tương tác đa giác quan, nơi ranh giới giữa nghệ sĩ và khán giả bị xóa nhòa bởi những thử thách ngôn ngữ, ứng biến giai điệu và công nghệ trình diễn ánh sáng hiện đại ngay tại trái tim Sài Gòn.', 
about_en = 'Experience the power of the Vietnamese language through music at Âm Sắc Việt - THE RESONANCE. An interactive, multi-sensory concert where the line between performer and audience fades through linguistic challenges, melodic improvisation, and cutting-edge light shows in the heart of Saigon.' 
where name = 'Âm Sắc Việt - THE RESONANCE';

--4. Table: event_categories
INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 1 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night'
UNION ALL SELECT id, 0 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night'
UNION ALL SELECT id, 22 FROM public.events WHERE name = 'V-Glow: The Cyber-Heritage Night';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 1 FROM public.events WHERE name = 'The Echo of An Nam'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'The Echo of An Nam'
UNION ALL SELECT id, 23 FROM public.events WHERE name = 'The Echo of An Nam'
UNION ALL SELECT id, 10 FROM public.events WHERE name = 'The Echo of An Nam';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 1 FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026'
UNION ALL SELECT id, 22 FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026'
UNION ALL SELECT id, 10 FROM public.events WHERE name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 20 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG'
UNION ALL SELECT id, 23 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG'
UNION ALL SELECT id, 21 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG'
UNION ALL SELECT id, 24 FROM public.events WHERE name = 'CỔ NGHỆ KIÊU HÙNG';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 1 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine'
UNION ALL SELECT id, 10 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine'
UNION ALL SELECT id, 22 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'NEO-LUMINANCE: The Echo of Indochine';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 23 FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo'
UNION ALL SELECT id, 9 FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'Sắc Lam: The Indigo Echo';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 1 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden'
UNION ALL SELECT id, 16 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden'
UNION ALL SELECT id, 21 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden'
UNION ALL SELECT id, 17 FROM public.events WHERE name = 'CRYSTAL REALM: The Neon Garden';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 0 FROM public.events WHERE name = 'SẮT & SON'
UNION ALL SELECT id, 1 FROM public.events WHERE name = 'SẮT & SON'
UNION ALL SELECT id, 10 FROM public.events WHERE name = 'SẮT & SON';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 22 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics'
UNION ALL SELECT id, 9 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics'
UNION ALL SELECT id, 5 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics'
UNION ALL SELECT id, 25 FROM public.events WHERE name = 'Techno-Sorcery: The Zenith of AI & Robotics';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 0 FROM public.events WHERE name = 'Vũ Trụ Cận Thị'
UNION ALL SELECT id, 23 FROM public.events WHERE name = 'Vũ Trụ Cận Thị'
UNION ALL SELECT id, 15 FROM public.events WHERE name = 'Vũ Trụ Cận Thị';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 1 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO'
UNION ALL SELECT id, 23 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO'
UNION ALL SELECT id, 19 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'THE DREAMCATCHER ARCHIPELAGO';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 12 FROM public.events WHERE name = 'KAIZEN: The Art of Precision'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'KAIZEN: The Art of Precision'
UNION ALL SELECT id, 23 FROM public.events WHERE name = 'KAIZEN: The Art of Precision';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 0 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands'
UNION ALL SELECT id, 11 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands'
UNION ALL SELECT id, 17 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands'
UNION ALL SELECT id, 28 FROM public.events WHERE name = 'LUVIA: The Echo of Highlands';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 0 FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam'
UNION ALL SELECT id, 23 FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam'
UNION ALL SELECT id, 11 FROM public.events WHERE name = 'Scent of the Soul: The Echoes of Vietnam';

INSERT INTO public.event_categories (event_id, category_id)
SELECT id, 0 FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE'
UNION ALL SELECT id, 20 FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE'
UNION ALL SELECT id, 24 FROM public.events WHERE name = 'Âm Sắc Việt - THE RESONANCE';

--3. Table: Event Sessions 

INSERT INTO public.event_sessions(id, start_date, end_date, status, name, created_at, event_id) VALUES
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'V-Glow: The Cyber-Heritage Night'           )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'The Echo of An Nam'                         )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'SÀI GÒN NEON BEATS: THE CYBER-FEST 2026'    )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'CỔ NGHỆ KIÊU HÙNG'                          )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'NEO-LUMINANCE: The Echo of Indochine'       )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'Sắc Lam: The Indigo Echo'                   )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'CRYSTAL REALM: The Neon Garden'             )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'SẮT & SON'                                  )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'Techno-Sorcery: The Zenith of AI & Robotics')),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'Vũ Trụ Cận Thị'                             )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'THE DREAMCATCHER ARCHIPELAGO'               )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'KAIZEN: The Art of Precision'               )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'LUVIA: The Echo of Highlands'               )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'Scent of the Soul: The Echoes of Vietnam'   )),
(uuidv7(), '2027-01-01T00:00:00Z', '2027-01-02T00:00:00Z', 'SELLING', 'session', now(), (select id from public.events where name = 'Âm Sắc Việt - THE RESONANCE'                ));

--4. Table: Zones 

--5. Venues Layout

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
