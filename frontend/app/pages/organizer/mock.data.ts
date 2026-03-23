// ─────────────────────────────────────────────────────────────
//  mock/organizer.data.ts
//  Single source of truth for all organizer mock data.
//  Mirrors the DB schema: categories → venues → events → sessions → zones
// ─────────────────────────────────────────────────────────────

// ── Types ─────────────────────────────────────────────────────

export interface MockCategory {
    id: number
    name: string
}

export interface MockVenue {
    id: string
    name: string
    addressLine: string
    imageUrl: string
    zoneNames: string[]   // zone names from venue layout JSON
}

export interface MockZone {
    id: string
    name: string
    isStanding: boolean
    capacity: number
    price: number
    purchaseLimit?: number | null
    perks?: string[]
    descriptionVi?: string
    descriptionEn?: string
    gridRows?: number
    gridCols?: number
    quantitySold?: number
}

export interface MockSession {
    id: string
    name: string
    startDate: string   // ISO string
    endDate: string     // ISO string
    status: string
}

export interface MockEvent {
    id: string
    name: string
    organizerId: string
    categoryId: number
    addressLine: string
    status: 'EDITING' | 'PREMIERE' | 'SELLING' | 'PAUSED' | 'ONGOING' | 'FINISHED' | 'CANCELLED'
    bannerUrl: string
    venueId: string
    aboutVi?: string
    aboutEn?: string
    termsAndConditions?: string
    policyRefund?: string
    session: MockSession
    zones: MockZone[]
}

export interface MockProfile {
    id: string
    name: string
    email: string
    avatarUrl?: string
    roleId: number
}

// ── Profile ───────────────────────────────────────────────────

export const mockProfile: MockProfile = {
    id: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
    name: 'Nguyễn Văn A',
    email: 'organizer@example.com',
    avatarUrl: undefined,
    roleId: 2,
}

// ── Categories ────────────────────────────────────────────────

export const mockCategories: MockCategory[] = [
    { id: 1, name: 'Âm nhạc' },
    { id: 2, name: 'Hội thảo' },
    { id: 3, name: 'Sân khấu - Nghệ thuật' },
    { id: 4, name: 'Thể thao' },
    { id: 5, name: 'Giải trí về đêm' },
]

// ── Venues ────────────────────────────────────────────────────

export const mockVenues: MockVenue[] = [
    {
        id: 'v1',
        name: 'CIS Arena',
        addressLine: '7, Đường số 23, Phú Mỹ Hưng, Phường Tân Mỹ, TP.Hồ Chí Minh',
        imageUrl: 'https://s.inyourpocket.com/gallery/178413.jpg',
        zoneNames: ['Courtside Left', 'Courtside Right', 'Bleachers'],
    },
    {
        id: 'v2',
        name: 'Nhà Hát Bến Thành',
        addressLine: '6 Đường Mạc Đĩnh Chi, Phường Sài Gòn, TP.Hồ Chí Minh',
        imageUrl: 'http://vietlandmarks.com/upload/142582138454fc4ec85a5d2.jpg',
        zoneNames: ['VIP', 'Standard'],
    },
    {
        id: 'v3',
        name: 'Mây in The Nest',
        addressLine: '519 Thôn Măng Lin, P. Langbiang, Đà Lạt, Tỉnh Lâm Đồng',
        imageUrl: 'https://media2.gody.vn/public/images/place/may-lang-thang-da-lat/614c498b500ff-1632389515.jpeg',
        zoneNames: ['VIP', 'GA'],
    },
    {
        id: 'v4',
        name: 'GEM Center',
        addressLine: '8 Đường Nguyễn Bỉnh Khiêm, Phường Sài Gòn, TP.Hồ Chí Minh',
        imageUrl: 'https://images2.thanhnien.vn/528068263637045248/2023/2/22/1-gem-center-16770590914701989789155.jpg',
        zoneNames: ['VIP', 'Standard'],
    },
    {
        id: 'v5',
        name: 'Tinh Tế Cafe',
        addressLine: '351/56 Đ. Lê Văn Sỹ, Phường Nhiêu Lộc, TP.Hồ Chí Minh',
        imageUrl: 'https://ik.imagekit.io/tvlk/blog/2022/10/quan-cafe-nhieu-cay-xanh-tphcm-1.jpeg?tr=q-70,c-at_max,w-500,h-300,dpr-2',
        zoneNames: ['Window Section', 'Main Floor', 'Private Room'],
    },
    {
        id: 'v6',
        name: 'Dreamplex Điện Biên Phủ',
        addressLine: '195 Đường Điện Biên Phủ, Phường Gia Định, TP.Hồ Chí Minh',
        imageUrl: 'https://maisonoffice.vn/wp-content/uploads/2021/09/van-phong-cho-thue-dreamplex-195-dien-bien-phu.jpg',
        zoneNames: ['VIP', 'Standard'],
    },
    {
        id: 'v7',
        name: 'Gigamall Thủ Đức',
        addressLine: '240-242 Phạm Văn Đồng, Phường Hiệp Bình, TP.Hồ Chí Minh',
        imageUrl: 'https://bidiland.vn/dataweb/images/tin-tuc/tong-hop-nhung-sieu-thi-va-cho-khu-vuc-quan-9-quan-2-quan-thu-duc-thanh-pho-thu-duc-coop-extra-gigamall-bidiland(1).jpg',
        zoneNames: ['VIP', 'GA'],
    },
    {
        id: 'v8',
        name: 'Sân Lễ Hội Đền Vua Đinh - Vua Lê',
        addressLine: 'Xã Trường Yên, Huyện Hoa Lư, Tỉnh Ninh Bình',
        imageUrl: 'https://mtcs.1cdn.vn/2023/02/16/le-hoi-den-hung.jpg',
        zoneNames: ['VVIP', 'VIP', 'GA Standing'],
    },
    {
        id: 'v9',
        name: 'Công viên Yên Sở',
        addressLine: 'QL1A, Gamuda Central, Thủ đô Hà Nội',
        imageUrl: 'https://gamudagardens.vn/wp-content/uploads/2016/09/ho-yen-so.jpg',
        zoneNames: ['VIP', 'GA'],
    },
    {
        id: 'v10',
        name: 'Khu đô thị Vạn Phúc',
        addressLine: '375, Quốc lộ 13, Phường Hiệp Bình, TPHCM',
        imageUrl: 'https://khudothivanphuc.vn/wp-content/uploads/2021/07/cong-vien-ocean-world-van-phuc.jpg',
        zoneNames: ['SVIP', 'VIP Left', 'VIP Right', 'GA Left', 'GA Early Bird'],
    },
]

// ── Helper — offset from now ───────────────────────────────────
const fromNow = (days: number, hours = 0) =>
    new Date(Date.now() + days * 86_400_000 + hours * 3_600_000).toISOString()

// ── Events (with embedded session + zones) ────────────────────

export const mockEvents: MockEvent[] = [
    // ── 1. Hà Anh Tuấn ───────────────────────────────────────
    {
        id: '1',
        name: 'Hà Anh Tuấn: Chân Trời Rực Rỡ',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
        categoryId: 1,
        addressLine: 'Sân Lễ Hội Đền Hùng, Huyện Gia Viễn, Tỉnh Ninh Bình',
        status: 'PREMIERE',
        bannerUrl: 'https://salt.tkbcdn.com/ts/ds/25/e6/b4/d79786df1e38c39beabe33c462cc381e.jpg',
        venueId: 'v8',
        aboutVi: '<p>Đêm nhạc huyền thoại với giọng ca đầy cảm xúc của Hà Anh Tuấn, cùng những giai điệu bất hủ mang đến không gian âm nhạc đầy mê hoặc.</p>',
        aboutEn: '<p>A legendary music night featuring the emotional voice of Ha Anh Tuan with timeless melodies creating an enchanting musical experience.</p>',
        termsAndConditions: '<p>Cấm trẻ em dưới 6 tuổi. Không ảnh hưởng đến khán giả khác.</p>',
        policyRefund: '<p>Không hoàn tiền sau khi mua. Chỉ hỗ trợ đổi vé trong trường hợp đặc biệt.</p>',
        session: {
            id: 's1', name: 'Show chính',
            startDate: fromNow(30), endDate: fromNow(30, 4), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z1-1', name: 'VVIP', isStanding: false, capacity: 50, price: 2_500_000,
                purchaseLimit: 4, quantitySold: 18, gridRows: 5, gridCols: 10,
                perks: ['Vòng tay check-in VIP', 'Goodie bag', 'Meet & Greet'],
                descriptionVi: 'Khu vực VIP cao cấp với vị trí đẹp nhất, dịch vụ đặc biệt và nhiều ưu đãi độc quyền.',
                descriptionEn: 'Premium VIP area with the best location, exclusive services and special privileges.',
            },
            {
                id: 'z1-2', name: 'GA Standing', isStanding: true, capacity: 800, price: 650_000,
                purchaseLimit: null, quantitySold: 294,
                perks: ['Vòng tay check-in', 'Nước suối'],
                descriptionVi: 'Khu vực đứng tự do gần sân khấu, trải nghiệm âm nhạc bùng nổ cùng đám đông.',
                descriptionEn: 'Standing area near the stage for an explosive music experience with the crowd.',
            },
        ],
    },

    // ── 2. Saigon Heat ────────────────────────────────────────
    {
        id: '2',
        name: 'VBA 2025: Saigon Heat vs Hanoi Buffaloes',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
        categoryId: 4,
        addressLine: 'CIS Arena, Quận 7, TP. Hồ Chí Minh',
        status: 'FINISHED',
        bannerUrl: 'https://cdn.nextix.cloud/nextix/81/saigonheat_1_2996fb89aa.png?updated_at=2024-06-20T06:55:12.533Z',
        venueId: 'v1',
        aboutVi: '<p>Chào đón người hâm mộ quay trở lại chảo lửa CIS. Trận đấu hứa hẹn mang đến những giây phút nghẹt thở và kịch tính đến tận giây cuối cùng.</p>',
        aboutEn: '<p>Welcome fans back to the CIS arena. The game promises breathtaking moments and excitement until the final buzzer.</p>',
        termsAndConditions: '<p>Không gây rối, không ảnh hưởng đến người khác.</p>',
        policyRefund: '<p>Hoàn vé theo quy định của BTC trong trường hợp trận đấu bị hủy.</p>',
        session: {
            id: 's2', name: 'Trận chính',
            startDate: fromNow(-2), endDate: fromNow(-2, 2), status: 'FINISHED',
        },
        zones: [
            {
                id: 'z2-1', name: 'Courtside VIP', isStanding: false, capacity: 50, price: 2_500_000,
                purchaseLimit: 2, quantitySold: 50, gridRows: 5, gridCols: 10,
                perks: ['Áo đấu phiên bản giới hạn', 'F&B phục vụ tại chỗ'],
                descriptionVi: 'Ghế ngồi sát sàn đấu, cảm nhận từng bước chạy của cầu thủ.',
                descriptionEn: 'Courtside seats to feel every move of the players.',
            },
            {
                id: 'z2-2', name: 'Standard Bleachers', isStanding: false, capacity: 800, price: 150_000,
                purchaseLimit: null, quantitySold: 800, gridRows: 20, gridCols: 40,
                perks: [],
                descriptionVi: 'Khu vực khán đài tiêu chuẩn với tầm nhìn tốt.',
                descriptionEn: 'Standard bleachers area with good view.',
            },
        ],
    },

    // ── 3. Những Thành Phố Mơ Màng ───────────────────────────
    {
        id: '3',
        name: 'Những Thành Phố Mơ Màng - Summer Tour',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
        categoryId: 1,
        addressLine: 'Công viên Yên Sở, Quận Hoàng Mai, Hà Nội',
        status: 'PREMIERE',
        bannerUrl: 'https://salt.tkbcdn.com/ts/ds/9f/0b/d4/f19a8a171d730418077d310ff82e7224.jpg',
        venueId: 'v9',
        aboutVi: '<p>Hành trình âm nhạc indie đầy mộng mơ với sự góp mặt của Đen Vâu, Chillies, và Vũ.</p>',
        aboutEn: '<p>A dreamy indie music journey featuring Den Vau, Chillies, and Vu.</p>',
        termsAndConditions: '<p>Vui lòng mang theo CCCD khi check-in. Trang phục thoải mái.</p>',
        policyRefund: '<p>Không hoàn trả vé sau khi mua.</p>',
        session: {
            id: 's3', name: 'Đêm diễn Summer Tour',
            startDate: fromNow(45), endDate: fromNow(45, 6), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z3-1', name: 'Cư Dân VIP', isStanding: false, capacity: 200, price: 1_500_000,
                purchaseLimit: null, quantitySold: 120, gridRows: 10, gridCols: 20,
                perks: ['Bộ quà tặng Cư Dân', 'Lối đi ưu tiên', 'Nước uống miễn phí'],
                descriptionVi: 'Đặc quyền cư dân VIP với khu vực nghỉ ngơi riêng, quà tặng độc quyền.',
                descriptionEn: 'VIP resident privileges with private lounge, exclusive gifts.',
            },
            {
                id: 'z3-2', name: 'GA Thường', isStanding: true, capacity: 2000, price: 650_000,
                purchaseLimit: null, quantitySold: 660,
                perks: ['Vòng tay vải', 'Sticker'],
                descriptionVi: 'Khu vực tự do dành cho các cư dân yêu âm nhạc.',
                descriptionEn: 'General admission area for music-loving residents.',
            },
        ],
    },

    // ── 4. Mây Lang Thang ─────────────────────────────────────
    {
        id: '4',
        name: 'Mây Lang Thang: Đêm Nhạc Trịnh',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
        categoryId: 1,
        addressLine: 'Mây Lang Thang, Phường 4, Thành phố Đà Lạt, Tỉnh Lâm Đồng',
        status: 'PREMIERE',
        bannerUrl: 'https://images.tkbcdn.com/2/608/332/Upload/eventcover/2023/02/09/01E775.jpg',
        venueId: 'v3',
        aboutVi: '<p>Thưởng thức những giai điệu nhạc Trịnh bất hủ giữa khung cảnh đồi thông mộng mơ của Đà Lạt.</p>',
        aboutEn: '<p>Enjoy timeless Trinh melodies amidst the dreamy pine hills of Dalat.</p>',
        termsAndConditions: '<p>Nên mang theo áo khoác ấm do thời tiết Đà Lạt se lạnh.</p>',
        policyRefund: '<p>Không hoàn tiền vé.</p>',
        session: {
            id: 's4', name: 'Đêm nhạc Trịnh',
            startDate: fromNow(25), endDate: fromNow(25, 3), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z4-1', name: 'Khu VVIP (Gần ca sĩ)', isStanding: false, capacity: 50, price: 1_800_000,
                purchaseLimit: null, quantitySold: 22, gridRows: 5, gridCols: 10,
                perks: ['Một phần nước tự chọn', 'Đĩa CD nhạc Trịnh'],
                descriptionVi: 'Vị trí đẹp nhất để tương tác cùng nghệ sĩ.',
                descriptionEn: 'The best spot to interact with the artist.',
            },
            {
                id: 'z4-2', name: 'Khu Khán Đài', isStanding: false, capacity: 200, price: 800_000,
                purchaseLimit: null, quantitySold: 73, gridRows: 10, gridCols: 20,
                perks: ['Nước uống', 'Chăn len'],
                descriptionVi: 'Khu vực ngồi thoải mái với tầm nhìn đẹp.',
                descriptionEn: 'Comfortable seating area with beautiful view.',
            },
        ],
    },

    // ── 5. Kịch Idecaf ────────────────────────────────────────
    {
        id: '5',
        name: 'Kịch Idecaf: Ngày Xửa Ngày Xưa 35',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
        categoryId: 3,
        addressLine: 'Nhà Hát Bến Thành, Quận 1, TP. Hồ Chí Minh',
        status: 'SELLING',
        bannerUrl: 'https://salt.tkbcdn.com/ts/ds/30/a9/34/a0c1474e974b399040081c8c98492939.png',
        venueId: 'v2',
        aboutVi: '<p>Chương trình kịch thiếu nhi được mong chờ nhất hè này. Vở diễn huyền thoại với những câu chuyện cổ tích đầy màu sắc.</p>',
        aboutEn: '<p>The most anticipated children\'s theater show this summer. A legendary performance with colorful fairy tales.</p>',
        termsAndConditions: '<p>Mỗi vé dành cho một người, trẻ em cần có người lớn đi kèm.</p>',
        policyRefund: '<p>Không hoàn tiền.</p>',
        session: {
            id: 's5', name: 'Buổi chiều Chủ Nhật',
            startDate: fromNow(5), endDate: fromNow(5, 3), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z5-1', name: 'Khu A (Lầu 1)', isStanding: false, capacity: 300, price: 350_000,
                purchaseLimit: null, quantitySold: 180, gridRows: 15, gridCols: 20,
                perks: [],
                descriptionVi: 'Khu vực lầu 1 gần sân khấu nhất.',
                descriptionEn: 'Floor 1 area closest to the stage.',
            },
            {
                id: 'z5-2', name: 'Khu B (Lầu 2)', isStanding: false, capacity: 200, price: 250_000,
                purchaseLimit: null, quantitySold: 40, gridRows: 10, gridCols: 20,
                perks: [],
                descriptionVi: 'Khu vực lầu 2 với tầm nhìn toàn cảnh.',
                descriptionEn: 'Floor 2 area with panoramic view.',
            },
        ],
    },

    // ── 6. Ravolution ─────────────────────────────────────────
    {
        id: '6',
        name: 'Ravolution Music Festival: Unite',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
        categoryId: 5,
        addressLine: 'Khu đô thị Vạn Phúc, Thành phố Thủ Đức, TP. Hồ Chí Minh',
        status: 'SELLING',
        bannerUrl: 'https://salt.tkbcdn.com/ts/ds/da/e7/ff/44433776efbd1c9e0f56570c16aa0d93.jpg',
        venueId: 'v10',
        aboutVi: '<p>Lễ hội âm nhạc điện tử quốc tế lớn nhất khu vực với dàn DJ Top 100 thế giới. Hãy sẵn sàng cho một đêm bùng nổ không giới hạn.</p>',
        aboutEn: '<p>The biggest international EDM festival in the region featuring Top 100 DJs worldwide. Get ready for an unlimited explosive night.</p>',
        termsAndConditions: '<p>Trang phục tự do, không mang chất cấm. Cấm trẻ em dưới 16 tuổi.</p>',
        policyRefund: '<p>Hoàn tiền 50% trước 7 ngày diễn ra.</p>',
        session: {
            id: 's6', name: 'Festival Day 1',
            startDate: fromNow(60), endDate: fromNow(61), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z6-1', name: 'SVIP Deck', isStanding: false, capacity: 100, price: 4_500_000,
                purchaseLimit: 4, quantitySold: 38, gridRows: 5, gridCols: 20,
                perks: ['Private Bar', 'Quà tặng từ nhà tài trợ', 'Lối đi VIP'],
                descriptionVi: 'Tận hưởng lễ hội trên khán đài cao cấp với không gian riêng tư.',
                descriptionEn: 'Enjoy the festival from premium deck with private space.',
            },
            {
                id: 'z6-2', name: 'GA Early Bird', isStanding: true, capacity: 1500, price: 850_000,
                purchaseLimit: null, quantitySold: 382,
                perks: ['Vòng tay vải'],
                descriptionVi: 'Khu vực tự do cho những người đến sớm.',
                descriptionEn: 'General admission area for early arrivals.',
            },
        ],
    },

    // ── 7. Saigon Tếu ─────────────────────────────────────────
    {
        id: '7',
        name: 'Saigon Tếu: Hài Độc Thoại - Lẻ Loi',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
        categoryId: 3,
        addressLine: 'Cà phê Tinh Tế, Quận 3, TP. Hồ Chí Minh',
        status: 'PREMIERE',
        bannerUrl: 'https://salt.tkbcdn.com/ts/ds/9e/f5/96/cc2541579f1e20c7ad7bcb74083bf669.jpg',
        venueId: 'v5',
        aboutVi: '<p>Đêm hài độc thoại giải tỏa căng thẳng cuối tuần với những câu chuyện đời thường đầy hài hước.</p>',
        aboutEn: '<p>A stand-up comedy night to unwind your weekend stress with hilarious everyday stories.</p>',
        termsAndConditions: '<p>Vui lòng không quay phim trong buổi diễn.</p>',
        policyRefund: '<p>Không hoàn trả vé.</p>',
        session: {
            id: 's7', name: 'Đêm hài độc thoại',
            startDate: fromNow(7), endDate: fromNow(7, 2), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z7-1', name: 'Khu ngồi chính', isStanding: false, capacity: 200, price: 0,
                purchaseLimit: null, quantitySold: 0, gridRows: 20, gridCols: 10,
                perks: [],
            },
        ],
    },

    // ── 8. Vietnam Tech Summit ────────────────────────────────
    {
        id: '8',
        name: 'Vietnam Tech Summit 2025',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9cc5',
        categoryId: 2,
        addressLine: 'GEM Center, Quận 1, TP. Hồ Chí Minh',
        status: 'PREMIERE',
        bannerUrl: 'https://salt.tkbcdn.com/ts/ds/f8/cd/d1/d4b92bf62a49463c0650f1cf053be65f.jpg',
        venueId: 'v4',
        aboutVi: '<p>Hội thảo công nghệ lớn nhất năm với sự tham gia của các chuyên gia hàng đầu trong ngành AI, Cloud Computing và Blockchain.</p>',
        aboutEn: '<p>The biggest technology conference of the year featuring top experts in AI, Cloud Computing, and Blockchain industries.</p>',
        termsAndConditions: '<p>Khuyến khích mang theo laptop cá nhân.</p>',
        policyRefund: '<p>Hoàn tiền 50% nếu hủy trước 5 ngày.</p>',
        session: {
            id: 's8', name: 'Tech Summit Day 1',
            startDate: fromNow(10), endDate: fromNow(10, 8), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z8-1', name: 'Hội trường chính', isStanding: false, capacity: 500, price: 0,
                purchaseLimit: null, quantitySold: 0, gridRows: 25, gridCols: 20,
                perks: [],
            },
        ],
    },

    // ── 9. Workshop Marketing ─────────────────────────────────
    {
        id: '9',
        name: 'Workshop: Marketing 0 Đồng cho Startup',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
        categoryId: 2,
        addressLine: 'Dreamplex Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh',
        status: 'PREMIERE',
        bannerUrl: 'https://salt.tkbcdn.com/ts/ds/5b/0d/82/e97fb295a99a2df11a1975affb349409.png',
        venueId: 'v6',
        aboutVi: '<p>Chia sẻ bí quyết tăng trưởng không cần ngân sách lớn. Học cách tận dụng organic marketing và growth hacking cho startup.</p>',
        aboutEn: '<p>Sharing growth secrets without a big budget. Learn to leverage organic marketing and growth hacking for your startup.</p>',
        termsAndConditions: '<p>Khuyến khích mang theo laptop cá nhân.</p>',
        policyRefund: '<p>Hoàn tiền 50% nếu hủy trước 3 ngày.</p>',
        session: {
            id: 's9', name: 'Workshop Session',
            startDate: fromNow(14), endDate: fromNow(14, 4), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z9-1', name: 'Khu học viên', isStanding: false, capacity: 100, price: 0,
                purchaseLimit: null, quantitySold: 0, gridRows: 10, gridCols: 10,
                perks: [],
            },
        ],
    },

    // ── 10. Van Gogh ──────────────────────────────────────────
    {
        id: '10',
        name: 'Van Gogh Art Lighting Experience',
        organizerId: '019bb098-c487-7bdb-9082-f51c9e8a9bb2',
        categoryId: 3,
        addressLine: 'Gigamall Thủ Đức, Thành phố Thủ Đức, TP. Hồ Chí Minh',
        status: 'ONGOING',
        bannerUrl: 'https://theme.hstatic.net/200000815177/1001237592/14/custompage_gioithieu_banner03.jpg?v=2990',
        venueId: 'v7',
        aboutVi: '<p>Triển lãm nghệ thuật tương tác đa giác quan với công nghệ ánh sáng hiện đại. Đắm chìm trong thế giới tranh của Van Gogh.</p>',
        aboutEn: '<p>Multi-sensory interactive art exhibition with modern lighting technology. Immerse yourself in Van Gogh\'s world of paintings.</p>',
        termsAndConditions: '<p>Mỗi lượt tham quan tối đa 60 phút.</p>',
        policyRefund: '<p>Không hỗ trợ hoàn tiền.</p>',
        session: {
            id: 's10', name: 'Exhibition Period',
            startDate: fromNow(-10), endDate: fromNow(20), status: 'ONGOING',
        },
        zones: [
            {
                id: 'z10-1', name: 'VIP', isStanding: true, capacity: 200, price: 350_000,
                purchaseLimit: null, quantitySold: 890,
                perks: ['Lối vào ưu tiên'],
            },
            {
                id: 'z10-2', name: 'GA', isStanding: true, capacity: 9999, price: 200_000,
                purchaseLimit: null, quantitySold: 350,
                perks: [],
            },
        ],
    },
]

// ── Derived helpers (use these in components) ─────────────────

/** Total tickets sold across all zones for one event */
export const getTicketsSold = (event: MockEvent): number =>
    event.zones.reduce((s, z) => s + (z.quantitySold ?? 0), 0)

/** Total capacity across all zones for one event */
export const getTotalCapacity = (event: MockEvent): number =>
    event.zones.reduce((s, z) => s + z.capacity, 0)

/** Total revenue for one event */
export const getRevenue = (event: MockEvent): number =>
    event.zones.reduce((s, z) => s + (z.quantitySold ?? 0) * z.price, 0)

/** First session start date shortcut */
export const getSessionStart = (event: MockEvent): string =>
    event.session.startDate



/** Status badge CSS class — includes all backend enum values */
export const getStatusClass = (status: string): string => ({
    EDITING:   'bg-secondary bg-opacity-10 text-secondary',
    SCHEDULED: 'bg-info bg-opacity-10 text-info',
    PREMIERE:  'bg-info bg-opacity-10 text-info',
    SELLING:   'bg-primary bg-opacity-10 text-primary',
    PAUSED:    'bg-warning bg-opacity-10 text-warning',
    ONGOING:   'bg-success bg-opacity-10 text-success',
    FINISHED:  'bg-dark bg-opacity-10 text-secondary',
    CANCELLED: 'bg-danger bg-opacity-10 text-danger',
}[status] ?? 'bg-secondary bg-opacity-10 text-secondary')

/** Status i18n key — pass result to $t() in Vue components */
export const getStatusI18nKey = (status: string): string => ({
    EDITING:   'organizer.events.status.editing',
    SCHEDULED: 'organizer.events.status.premier',
    PREMIERE:  'organizer.events.status.premier',
    SELLING:   'organizer.events.status.selling',
    PAUSED:    'organizer.events.status.paused',
    ONGOING:   'organizer.events.status.ongoing',
    FINISHED:  'organizer.events.status.finished',
    CANCELLED: 'organizer.events.status.cancelled',
}[status] ?? status)

/** Format VND price */
export const formatPrice = (p: number): string =>
    new Intl.NumberFormat('vi-VN').format(p) + ' ₫'

/** Format date vi-VN */
export const formatDate = (d: string): string =>
    new Date(d).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' })

/** Format time vi-VN */
export const formatTime = (d: string): string =>
    new Date(d).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })