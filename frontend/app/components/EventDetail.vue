<script setup lang="ts">
import { formatDate } from '@vueuse/core';
import { ref } from 'vue';
import Header from './Header.vue';
import Footer from './Footer.vue';

const imgTest2 = 'https://salt.tkbcdn.com/ts/ds/00/6c/a8/6f638ec63c1be2aa163fdda0d3e07106.jpg';
const imgEventThumb = 'https://ticketgo.vn/photos/3447/Th%C3%A1ng%2010%20-%202025/69367859ab0f9.jpg';
const imgOrganizerLogo = 'https://salt.tkbcdn.com/ts/ds/be/d4/a4/e6c34b2216009af82feba5f9bca782ad.jpg';
const imgAbout = 'https://ticketgo.vn/photos/3447/Th%C3%A1ng%2010%20-%202025/693679d1cb70d.jpg';
const imgEvent1 = 'https://images.tkbcdn.com/2/608/332/ts/ds/c5/98/c8/c05ab41ca42f7c41bb7fc720518259db.jpg';
const imgEvent2 = 'https://images.tkbcdn.com/2/608/332/ts/ds/9d/63/2b/8e002e32d1e883c3615ca708c0293c58.jpg';
const imgEvent3 = 'https://images.tkbcdn.com/2/608/332/ts/ds/c7/b9/71/7c6753138a01713f966081ac0ac8483b.jpg';
const imgEvent4 = 'https://images.tkbcdn.com/2/608/332/ts/ds/c2/4d/12/585693b771d27f331cd28960e091a579.jpg';
const imgSidebar = 'https://ticketbox.vn/_next/image?url=https%3A%2F%2Fsalt.tkbcdn.com%2Fts%2Fds%2Fab%2Fdf%2F5c%2F6da51dd7722a871f5cf65ca2f0e3e08e.jpg&w=3840&q=75';

const expandedTickets = ref<{[key: string]: boolean}>({});

const scrollToSection = (id: string) => {
  const element = document.getElementById(id);
  element?.scrollIntoView({ behavior: 'smooth' });
};

const toggleTicketDate = (scheduleId: string) => {
  expandedTickets.value[scheduleId] = !expandedTickets.value[scheduleId];
};

const locationForm= ref(false);
const expandAbout = ref(false);

const eventData = {
  title: 'GIAO HƯỞNG MÙA YÊU - LIVE CONCERT ĐẶC BIỆT TẠI NHÀ HÁT HỒ GƯƠM',
  date: 'Chủ Nhật, 03/01/2026',
  time: '20:00 - 22:30',
  venue: 'Nhà Hát Hồ Gươm',
  address: '40 Hàng Bài, Phường Cửa Nam, Quận Hoàn Kiếm, Thành Phố Hà Nội',
  startPrice: '1.550.000 đ',
  svipPrice: '6.650.000 đ',
  svipBenefits:
    'Quyền lợi đi kèm độc quyền cực kỳ hấp dẫn: Postcard thông điệp từ Nghệ sĩ. Ảnh check-in cá nhân. Card Nghệ sĩ có chữ ký. Móc khoa độc quyền. Soundcheck. Bàn đón tiếp và check-in riêng. Lối vào riêng. Tiệc chờ riêng tư cùng rượu vang và đồ ăn nhẹ. Vé cứng thiết kế đặc biệt. Poster chữ ký tay.',
  organizer: {
    name: 'Absolute Media',
    description:
      'Live Concert nghệ thuật kết hợp giữa âm nhạc hiện đại và dàn nhạc semi-classical tinh tế, được sản xuất bởi Absolute Media.',
  },
  schedule: [
    { time: '19:30', title: 'Cửa ngõ', desc: 'Mở cửa' },
    { time: '20:00', title: 'Buổi diễn', desc: 'Bắt đầu' },
    { time: '21:30', title: 'Giải lao', desc: '15 phút' },
    { time: '21:45', title: 'Phần 2', desc: 'Tiếp tục' },
    { time: '22:30', title: 'Kết thúc', desc: 'Hết buổi' },
  ],
  seatTypes: [
    { name: 'SVIP', price: '6.650.000', available: 45 },
    { name: 'VIP', price: '4.400.000', available: 120 },
    { name: 'A', price: '2.200.000', available: 200 },
    { name: 'B', price: '1.550.000', available: 300 },
  ],
  relatedEvents: [
    { id: 1, image: imgEvent1, title: 'Nhà Hát Kịch IDECAF: Tám Cám Đại Chiến', date: '03 tháng 01, 2026', price: '270.000' },
    { id: 2, image: imgEvent2, title: 'Event 2', date: '03 tháng 01, 2026', price: '270.000' },
    { id: 3, image: imgEvent3, title: 'Event 3', date: '03 tháng 01, 2026', price: '270.000' },
    { id: 4, image: imgEvent4, title: 'Event 4', date: '03 tháng 01, 2026', price: '270.000' },
    { id: 5, image: imgEvent1, title: 'Event 5', date: '03 tháng 01, 2026', price: '270.000' },
    { id: 6, image: imgEvent2, title: 'Event 6', date: '03 tháng 01, 2026', price: '270.000' },
    { id: 7, image: imgEvent3, title: 'Event 7', date: '03 tháng 01, 2026', price: '270.000' },
    { id: 8, image: imgEvent4, title: 'Event 8', date: '03 tháng 01, 2026', price: '270.000' },
  ],
};

const eventSchedule = [
  { 
    date: '17.07.2026', 
    id: 'collapseDate1', 
    seatTypes: [
      { name: 'SVIP', price: '3.000.000', available: 100 },
      { name: 'VIP', price: '2.000.000', available: 150 },
      { name: 'A', price: '1.500.000', available: 200 },
      { name: 'B', price: '1.000.000', available: 300 }
    ]
  },
  { 
    date: '17.08.2026', 
    id: 'collapseDate2', 
    seatTypes: [
      { name: 'SVIP', price: '3.500.000', available: 80 },
      { name: 'VIP', price: '2.500.000', available: 250 },
      { name: 'A', price: '1.800.000', available: 180 },
      { name: 'B', price: '1.200.000', available: 350 }
    ] 
  }
];
</script>

<template>
  <div class="bg-reactive-primary" style="min-height: 100vh; overflow-x: hidden; max-width: 100vw;" data-bs-theme="dark">
    <Header />
    <!-- Hero Banner with Categories -->
    <section class="hero-section" :style="{ paddingTop: '1.5rem', minHeight: '450px', position: 'relative', overflow: 'hidden' }">
      <div :style="{ backgroundImage: `url(${imgTest2})`, backgroundSize: 'cover', backgroundPosition: 'center', filter: 'blur(8px)', position: 'absolute', top: 0, left: 0, right: 0, height: '100%', zIndex: 0 }"></div>
      <div style="position: relative; z-index: 1">
        <!-- Event Info and Poster Container -->
        <div class="container-xxl pb-4">
            <div class="g-3 align-items-start hero-grid">
                <!-- Event Card -->
                <div class="col-12">
                    <div class="card d-flex flex-row bg-dark border-dark rounded-5 shadow-lg hero-card" style="max-width: 1250px; margin: auto; overflow: hidden">
                        <div class="card-body p-3 p-md-4 d-flex flex-column hero-card-body">
                            <h5 class="text-white fw-bold lh-sm mb-2" >
                                {{ eventData.title }}
                            </h5>
                            <div class="mb-2" style="font-size: 0.85rem" >
                                <i class="bi bi-calendar" style="color: #07b3df; margin-right: 0.3rem"></i>
                                <span style="color: #07b3df; font-weight: 600">{{ eventData.date }}</span>
                            </div>
                            <div class="mb-2" style="color: #979797; font-size: 0.85rem">
                                <i class="bi bi-geo-alt" style="color: #07b3df; margin-right: 0.3rem"></i>
                                <span style="color: #07b3df; font-weight: 600">{{ eventData.venue }}</span>
                                <p style="font-size: 0.9rem">40 Hàng Bài, Phường Cửa Nam, Quận Hoàn Kiếm, Thành Phố Hà Nội</p>
                            </div>
                            <div class="mt-auto"> 
                                <hr class="bg-secondary my-2" />
                                <p class="text-white fw-semibold mb-1" style="font-size: 0.7rem">Giá từ</p>
                                <p class="text-info fw-bold mb-2" style="font-size: 1.1rem">{{ eventData.startPrice }}</p>
                                <button class="btn btn-info text-white fw-bold w-100 py-1" style="font-size: 0.85rem">Mua vé ngay</button>
                            </div>
                        </div>
                        <img :src="imgTest2" alt="Event Poster" class="w-100 shadow-lg my-dashed-line" style=" object-fit: cover;" />
                    </div>
                </div>
            </div>
        </div>
      </div>
    </section>

    <!-- Navigation Tabs with Scroll Links -->
    <div class="bg-reactive-primary border-bottom nav-tabs-container" style="top: 50px; border-color: #333; z-index: 50; overflow-x: auto;">
      <div class="container-xxl">
        <div class="d-flex gap-3 gap-md-4 nav-tabs-wrapper" style="padding: 1rem 0;">
          <button
            @click="scrollToSection('schedule-section')"
            class="btn btn-link text-white-50 fw-semibold p-0"
            style="font-size: 0.95rem; text-decoration: none"
          >
            Lịch sự kiện
          </button>
          <button
            @click="scrollToSection('about-section')"
            class="btn btn-link text-white-50 fw-semibold p-0"
            style="font-size: 0.95rem; text-decoration: none"
          >
            Về sự kiện
          </button>
          <button
            @click="scrollToSection('tickets-section')"
            class="btn btn-link text-white-50 fw-semibold p-0"
            style="font-size: 0.95rem; text-decoration: none"
          >
            Thông tin vé
          </button>
          <button
            @click="scrollToSection('organizer-section')"
            class="btn btn-link text-white-50 fw-semibold p-0"
                style="font-size: 0.95rem; text-decoration: none"
          >
            Nhà tổ chức
          </button>
        </div>
      </div>
    </div>

    <!-- Location Modal Overlay -->
    <div v-if="locationForm" class="modal-backdrop-custom" @click="locationForm = false">
        <div class="location-modal-wrapper" @click.stop>
            <button 
                @click="locationForm = false"
                type="button" 
                class="btn-close btn-close-white"
                aria-label="Close"
                style="position: absolute; top: 1rem; right: 1rem; z-index: 10"
            ></button>
            <div class="location-modal-content">
                <h6 class="text-white fw-bold mb-4">Nhà Hát Hồ Gươm - 40 Hàng Bài, Phường Cửa Nam, Quận Hoàn Kiếm, Thành Phố Hà Nội</h6>
                <img :src="imgEventThumb" alt="Sơ đồ chỗ ngồi" class="location-modal-image" />
            </div>
        </div>
    </div>

    <div class="row content-row" style="margin: 0px 0px;">
        <div class="col-lg-9">
            <!-- Schedule Section -->
            <section id="schedule-section" class="section-card p-5 card bg-reactive-secondary rounded-5">
                <div class="container-xxl">
                    <h3 class="text-white fw-bold mb-3 mb-md-4" style="font-size: clamp(1rem, 4vw, 1.5rem)">Lịch sự kiện và sơ đồ chỗ ngồi</h3>
                    
                    <!-- Info and Button -->
                    <div class="d-flex flex-column gap-2 mb-3">
                        <h6 class="text-reactive-primary mb-1" style="font-size: clamp(0.9rem, 3vw, 1.1rem)">{{ eventData.title }}</h6>
                        <p class="text-reactive-secondary mb-2" style="font-size: clamp(0.75rem, 2.5vw, 0.9rem)">40 Hàng Bài, Phường Cửa Nam, Quận Hoàn Kiếm, Thành Phố Hà Nội</p>
                        <button class="btn btn-sm btn-outline-info w-auto align-self-start" @click="locationForm = !locationForm">Xem sơ đồ</button>
                    </div>
                    
                    <!-- Image -->
                    <div class="mb-4">
                        <img :src="imgEventThumb" alt="" class="w-100 rounded-3" style="object-fit: cover; max-height: 200px;" />
                    </div>
                    <div class="border-bottom my-4"></div>
                    <div class="row w-100">
                        <div class="date col-lg-6">
                            <p>Chủ Nhật, 03/01/2026</p>
                        </div>
                        <div class="time col-lg-6 text-lg-end">
                            <button class="btn" style="background-color: #07b3df; cursor: pointer; margin-bottom: 0">20:00 - 22:30</button>
                        </div>
                    </div>
                </div>
            </section>

            <!-- About Section -->
            <section id="about-section" class="section-card p-5 card bg-reactive-secondary rounded-5">
                <div class="container-xxl" >
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h3 class="text-white fw-bold" style="font-size: clamp(1.2rem, 4vw, 1.5rem); margin-bottom: 0">Về sự kiện</h3>
                    </div>
                    <div class=" border-dark rounded-3 overflow-hidden mb-4" :style="{'transition': 'max-height 1.5s ease', 'max-height': expandAbout ? '2000px' : '250px', 'height': 'auto' }">
                        <img :src="imgAbout" alt="About" class="card-img-top card"/>
                        <div class="bg-reactive-secondary mt-4">
                            <h5 class="fw-bold mb-3" style="font-size: clamp(0.95rem, 3vw, 1.25rem)">1. Giới thiệu chung</h5>
                            <p class="text-white-50" style="line-height: 1.6; font-size: clamp(0.8rem, 2.5vw, 0.95rem)">Thông tin giới thiệu về sự kiện sẽ được hiển thị tại đây với chi tiết đầy đủ về chương trình và các điểm nổi bật</p>
                        
                            <h5 class="fw-bold mb-3" style="font-size: clamp(0.95rem, 3vw, 1.25rem)">2. Chi tiết sự kiện</h5>
                            <p class="text-white-50" style="line-height: 1.6; font-size: clamp(0.8rem, 2.5vw, 0.95rem)">Chi tiết sự kiện sẽ được hiển thị tại đây bao gồm thông tin về các nghệ sĩ, danh sách chương trình</p>
                        
                            <h5 class="fw-bold mb-3" style="font-size: clamp(0.95rem, 3vw, 1.25rem)">3. Điều Khoản & Điều kiện</h5>
                            <p class="text-white-50" style="line-height: 1.6; font-size: clamp(0.8rem, 2.5vw, 0.95rem)">Điều khoản sẽ được hiển thị tại đây cho khách hàng tham khảo trước khi mua vé</p>
                      </div>
                    </div>
                    <button @click="expandAbout = !expandAbout" class="btn btn-sm btn-outline-info fw-bold mx-auto d-block">
                        {{ expandAbout ? 'Ẩn bớt' : 'Xem thêm' }}
                    </button>
                </div>
            </section>

            <!-- Tickets Section -->
            <section id="tickets-section" class="section-card card bg-reactive-secondary rounded-5">
                <div class="px-2 px-md-3">
                    <h3 class="text-white fw-bold my-3 my-md-5 mx-3 mx-md-5" style="font-size: clamp(1rem, 4vw, 1.5rem)">Thông tin vé</h3>
                    
                    <div v-for="(schedule, dateIdx) in eventSchedule" :key="dateIdx" class="mb-4">
                        
                        <button 
                            @click="toggleTicketDate(schedule.id)"
                            class="btn w-100 text-start text-white p-3 p-md-4 rounded-3 bg-reactive-secondary border-dark" 
                            style="cursor: pointer;"
                            type="button"
                        >
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="d-flex align-items-center gap-2">
                                    <i class="bi bi-calendar-event text-info" style="font-size: clamp(0.9rem, 3vw, 1.1rem)"></i>
                                    <span class="fw-bold" style="font-size: clamp(1rem, 4vw, 1.25rem)">{{ schedule.date }}</span>
                                </div>
                                <i class="bi bi-chevron-down transition-icon" :style="{ transform: expandedTickets[schedule.id] ? 'rotate(180deg)' : 'rotate(0deg)' }"></i>
                            </div>
                        </button>

                        <div v-if="expandedTickets[schedule.id]">
                            <div class="card card-body p-0 rounded-3">

                                <div v-for="(seat, seatIdx) in schedule.seatTypes" :key="seatIdx" 
                                    class="p-3 p-md-5" 
                                    :class="seatIdx !== schedule.seatTypes.length - 1 ? 'border-bottom' : ''" 
                                    style="background-color: #1e1e1e; border-color: #333"
                                >
                                    <div class="row g-3 align-items-start px-1">
                                        <div class="col-12 col-md-8">
                                            <div class="d-flex align-items-center gap-2 mb-2">
                                                <i class="bi bi-tag-fill text-info" style="font-size: 1.3rem"></i>
                                                <span class="text-white fw-bold" style="font-size: 1.1rem">{{ seat.name }}</span>
                                            </div>
                                            <p class="text-info fw-bold mb-2" style="font-size: 1rem">{{ seat.price }} đ / vé</p>
                                            <p class="text-white-50 mb-0" style="font-size: 0.9rem">Còn {{ seat.available }} ghế</p>
                                        </div>
                                        <div class="col-12 col-md-4 text-md-end">
                                            <button class="btn btn-info fw-bold" style="white-space: nowrap">Mua ngay</button>
                                        </div>
                                    </div>
                                    <p v-if="seat.name === 'SVIP'" class="text-white-50 mt-3 mb-0" style="font-size: 0.9rem; line-height: 1.6">{{ eventData.svipBenefits }}</p>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </section>

            <!-- Organizer Section -->
            <section id="organizer-section" class="section-card p-5 card bg-reactive-secondary rounded-5">
                <div class="container-xxl organizer-card">
                    <h3 class="text-white fw-bold mb-4" style="font-size: clamp(1.2rem, 4vw, 1.5rem)">Ban tổ chức</h3>
                    <div class=" border-dark rounded-3">
                        <div class="row g-4 align-items-start">
                            <div class="col-md-4">
                                <img
                                :src="imgOrganizerLogo"
                                alt="Organizer"
                                class="img-fluid rounded-2"
                                style="aspect-ratio: 1; object-fit: cover"
                                />
                            </div>
                            <div class="col-md-8">
                                <h4 class="text-white fw-bold mb-3" style="font-size: 1.3rem">{{ eventData.organizer.name }}</h4>
                                <p class="text-white-50" style="line-height: 1.8; font-size: 0.95rem">{{ eventData.organizer.description }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <div class="col-lg-3 mt-4 sidebar-container d-none d-lg-block">
            <img :src="imgSidebar" alt="" class="rounded-5 sidebar-img">
        </div>
    </div>
    
    
    <!-- Related Events Section -->
    <section class="py-5">
      <div class="container-xxl">
        <h2 class="text-white text-center fw-bold mb-5" style="font-size: clamp(1.3rem, 5vw, 1.8rem)">Có thể bạn cũng thích</h2>

        <div class="row g-3 mb-5 d-flex">
          <div v-for="event in eventData.relatedEvents" :key="event.id" class="col-6 col-lg-3">
            <div class="card bg-dark border-dark rounded-2 h-100 overflow-hidden related-info" style="cursor: pointer; transition: transform 0.5s ease">
              <img :src="event.image" alt="Event" class="card-img-top" style="height: 180px; object-fit: cover" />
              <div class="related-body">
                <p class="text-white fw-semibold mb-2" style="font-size: 1.1rem; line-height: 1.3">{{ event.title }}</p>
                <div style="margin-top: auto !important; ">
                  <p class="text-info fw-bold mb-0" style="font-size: 1.1rem">Từ {{ event.price }} đ</p>
                  <div class="d-flex align-items-center gap-2 mb-2">
                    <i class="bi bi-calendar text-info" style="font-size: 0.85rem"></i>
                    <span class="text-white" style="font-size: 0.85rem">{{ event.date }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="text-center">
          <button class="btn btn-info fw-bold px-4 py-2">Xem thêm sự kiện</button>
        </div>
      </div>
    </section>
    <Footer class="d-none d-md-block" />
  </div>
</template>

<style scoped>
.modal-backdrop-custom {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.85);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  animation: fadeIn 0.3s ease-in-out;
  backdrop-filter: blur(2px);
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.location-modal-wrapper {
  background-color: #1a1a1a;
  border: 2px solid #07b3df;
  border-radius: 1rem;
  padding: 2rem;
  max-width: 800px;
  width: 100%;
  max-height: 85vh;
  overflow-y: auto;
  position: relative;
  animation: slideUp 0.3s ease-out;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.7);
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.location-modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.location-modal-image {
  width: 100%;
  max-width: 1000px;
  height: auto;
  border-radius: 0.5rem;
  object-fit: cover;
  border: 1px solid #374155;
  transition: transform 0.3s ease;
}

.location-modal-image:hover {
  transform: scale(1.05);
}

.btn-close-white {
  filter: brightness(0) invert(1);
  transition: opacity 0.2s ease;
}

.btn-close-white:hover {
  opacity: 0.7;
}

.event-detail {
  font-family: 'Open Sans', sans-serif;
}

button:hover {
  opacity: 0.9;
  transition: opacity 0.3s ease;
}

.my-dashed-line {
    border-left: 2px solid transparent; 
    border-image: repeating-linear-gradient(
        to bottom, 
        #5E5E5E, 
        20px, 
        transparent 20px, 
        transparent 40px
    ) 50; 
}

@media (min-width: 768px) {
  .hero-grid {
    grid-template-columns: 1fr 300px !important;
  }
}

@media (max-width: 1024px) {
  .content-row {
    flex-direction: column;
    margin: 0 !important;
  }
}

@media (max-width: 768px) {
  .event-detail {
    font-size: 0.9rem;
  }
  
  .hero-card {
    flex-direction: column !important;
    height: auto !important;
    min-height: 400px;
  }
  
  .hero-card-body {
    width: 100% !important;
    min-height: 200px;
    order: 2 !important;
  }
  
  .hero-card img {
    order: 1 !important;
    max-height: 300px;
    width: 100% !important;
  }
  
  .my-dashed-line {
    border-left: none !important;
    border-bottom: 2px solid transparent;
    border-image: repeating-linear-gradient(
      to right, 
      #5E5E5E, 
      20px, 
      transparent 20px, 
      transparent 40px
    ) 50;
  }
  
  .nav-tabs-container {
    margin-left: 0.5rem !important;
    margin-right: 0.5rem !important;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
  }
  
  .nav-tabs-container::-webkit-scrollbar {
    display: none;
  }
  
  .nav-tabs-wrapper {
    flex-wrap: nowrap !important;
    min-width: max-content;
  }
  
  .nav-tabs-wrapper button {
    white-space: nowrap;
    flex-shrink: 0;
  }
  
  .section-card {
    margin: 0.9rem 0.5rem !important;
    padding: 1rem !important;
  }
  
  .content-row {
    margin: 0 !important;
  }
  
  .sidebar-container {
    padding: 0 0.5rem;
  }
  
  .sidebar-img {
    width: 50%;
  }
  
  .container-xxl {
    padding-left: 0.5rem !important;
    padding-right: 0.5rem !important;
  }

  .organizer-card img{
    width: 50% !important;
  }
  
  .related-info img{
    height: 120px !important;
  }

  .related-body{
    flex-direction: column !important;
    padding: 0.35rem !important;
  }
  .related-body p{
    font-size: 0.75rem !important;
    padding: none !important;
  }

  .related-body div {
    margin-bottom: 0rem !important;
  }
}

/* Desktop styles */
.hero-card {
  height: 500px;
}

.hero-card-body {
  width: 30%;
  flex-shrink: 0;
}

.section-card {
  margin: 1.5rem;
}

.sidebar-img {
  width: 92%;
}

.nav-tabs-container {
  margin-left: 1.5rem;
  margin-right: 1.5rem;
  overflow-x: visible;
}

.nav-tabs-wrapper {
  flex-wrap: wrap;
}

.transition-icon {
  transition: transform 0.3s ease;
}

button[aria-expanded="true"] .transition-icon {
  transform: rotate(180deg);
}

.related-body{
  display: flex;
  flex-direction: column;
  padding: 1rem ;
}
</style>