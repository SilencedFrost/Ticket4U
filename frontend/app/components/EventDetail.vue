<script setup lang="ts">
import { ref } from 'vue';
const imgTest2 = 'https://salt.tkbcdn.com/ts/ds/00/6c/a8/6f638ec63c1be2aa163fdda0d3e07106.jpg';
const imgEventThumb = 'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a';
const imgOrganizerLogo = 'https://salt.tkbcdn.com/ts/ds/be/d4/a4/e6c34b2216009af82feba5f9bca782ad.jpg';
const imgAbout = 'https://assets.cticket.vn/tix/the-brothers/LDP%20THE%20BROTHERS%20(1).webp';
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
  date: '2025-02-20',
  time: '20:00 - 22:30',
  venue: 'Nhà Hát Hồ Gươm',
  address: {
    vi: '40 Hàng Bài, Phường Cửa Nam, Quận Hoàn Kiếm, Thành Phố Hà Nội',
    en: '40 Hang Bai St, Cua Nam Ward, Hoan Kiem Dist, Hanoi City'
  },
  startPrice: '1.550.000 đ',
  svipPrice: '6.650.000 đ',
  svipBenefits:
    'Quyền lợi đi kèm độc quyền cực kỳ hấp dẫn: Postcard thông điệp từ Nghệ sĩ. Ảnh check-in cá nhân. Card Nghệ sĩ có chữ ký. Móc khoa độc quyền. Soundcheck. Bàn đón tiếp và check-in riêng. Lối vào riêng. Tiệc chờ riêng tư cùng rượu vang và đồ ăn nhẹ. Vé cứng thiết kế đặc biệt. Poster chữ ký tay.',
  organizer: {
    name: 'Absolute Media',
    description:
      'Live Concert nghệ thuật kết hợp giữa âm nhạc hiện đại và dàn nhạc semi-classical tinh tế, được sản xuất bởi Absolute Media.',
  },
  relatedEvents: [
    { id: 1, image: imgEvent1, title: 'Nhà Hát Kịch IDECAF: Tám Cám Đại Chiến', date: '2026-01-03', price: '270.000' },
    { id: 2, image: imgEvent2, title: 'Event 2', date: '2026-01-03', price: '270.000' },
    { id: 3, image: imgEvent3, title: 'Event 3', date: '2026-01-03', price: '270.000' },
    { id: 4, image: imgEvent4, title: 'Event 4', date: '2026-01-03', price: '270.000' },
    { id: 5, image: imgEvent1, title: 'Event 5', date: '2026-01-03', price: '270.000' },
    { id: 6, image: imgEvent2, title: 'Event 6', date: '2026-01-03', price: '270.000' },
    { id: 7, image: imgEvent3, title: 'Event 7', date: '2026-01-03', price: '270.000' },
    { id: 8, image: imgEvent4, title: 'Event 8', date: '2026-01-03', price: '270.000' },
  ],
};

const eventSchedule = [
  { 
    date: '2025-02-20', 
    time: '20:00 - 22:30',
    id: 'collapseDate1', 
    seatTypes: [
      { name: 'SVIP', price: '3.000.000', available: 100 },
      { name: 'VIP', price: '2.000.000', available: 150 },
      { name: 'A', price: '1.500.000', available: 200 },
      { name: 'B', price: '1.000.000', available: 300 }
    ]
  }
];
</script>

<template>
  <div class="bg-reactive-primary event-wrapper" data-bs-theme="light">
    <Header />
    <!-- Hero Banner with Categories -->
    <section class="hero-section">
      <div class="hero-bg-blur" :style="{ backgroundImage: `url(${imgTest2})` }"></div>
      <div class="position-relative z-1">
        <!-- Event Info and Poster Container -->
        <div class="container-xxl pb-4">
            <div class="g-3 align-items-start hero-grid">
                <!-- Event Card -->
                <div class="col-12">
                    <div class="card d-flex flex-row bg-reactive-secondary border-dark rounded-5 shadow-lg hero-card mx-auto overflow-hidden hero-card-max-width">
                        <div class="card-body p-3 p-md-4 d-flex flex-column hero-card-body">
                            <h5 class="text-reactive-primary fw-bold lh-sm mb-2">
                                {{ eventData.title }}
                            </h5>
                            <div class="mb-2 small">
                                <i class="bi bi-calendar text-primary me-1"></i>
                                <span class="text-primary fw-semibold">{{ eventData.time }},
                                  {{ $d(new Date(eventData.date), 'short') }}
                                </span>
                            </div>
                            <div class="mb-2 text-secondary small">
                                <i class="bi bi-geo-alt text-primary me-1"></i>
                                <span class="text-primary fw-semibold">{{ eventData.venue }}</span>
                                <p class="mb-0 small">{{ eventData.address[$i18n.locale] }}</p>
                            </div>
                            <div class="mt-auto"> 
                                <hr class="bg-secondary my-2" />
                                <p class="text-reactive-primary fw-semibold mb-1 text-xs">{{ $t('even-detail.price_from') }}</p>
                                <p class="text-info fw-bold mb-2 fs-6">{{ eventData.startPrice }}</p>
                                <button class="btn btn-info text-reactive-primary fw-bold w-100 py-1 small">{{ $t('even-detail.buy_tickets') }}</button>
                            </div>
                        </div>
                        <img :src="imgTest2" alt="Event Poster" class="w-100 shadow-lg my-dashed-line img-cover" />
                    </div>
                </div>
            </div>
        </div>
      </div>
    </section>

    <!-- Navigation Tabs with Scroll Links -->
    <div class="bg-reactive-primary border-bottom nav-tabs-container overflow-x-auto">
      <div class="container-xxl">
        <div class="d-flex gap-3 gap-md-4 nav-tabs-wrapper py-3">
          <button
            @click="scrollToSection('schedule-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none nav-tab-btn"
          >
            {{ $t('even-detail.schedule_event') }}
          </button>
          <button
            @click="scrollToSection('about-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none nav-tab-btn"
          >
            {{ $t('even-detail.about_event') }}
          </button>
          <button
            @click="scrollToSection('tickets-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none nav-tab-btn"
          >
            {{ $t('even-detail.information_tickets') }}
          </button>
          <button
            @click="scrollToSection('organizer-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none nav-tab-btn"
          >
            {{ $t('even-detail.organizer') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Location Modal Overlay -->
    <div v-if="locationForm" class="modal-backdrop-custom" @click="locationForm = false">
        <div class="location-modal-wrapper bg-reactive-primary" @click.stop>
            <button 
                @click="locationForm = false"
                type="button" 
                class="btn-close btn-close-white position-absolute top-0 end-0 m-3 z-10"
                aria-label="Close"
            ></button>
            <div class="location-modal-content">
                <h6 class="text-reactive-primary fw-bold mb-4">{{ eventData.address[$i18n.locale] }}</h6>
                <img :src="imgEventThumb" alt="Sơ đồ chỗ ngồi" class="location-modal-image" />
            </div>
        </div>
    </div>

    <div class="row content-row m-0">
        <div class="col-lg-9">
            <!-- Schedule Section -->
            <section id="schedule-section" class="section-card p-5 card bg-reactive-secondary rounded-5">
                <div class="container-xxl">
                    <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 section-title"> {{ $t('even-detail.schedule_location') }}</h3>
                    
                    <!-- Info and Button -->
                    <div class="d-flex flex-column gap-2 mb-3">
                        <h6 class="text-reactive-primary mb-1 schedule-title">{{ eventData.title }}</h6>
                        <p class="text-reactive-secondary mb-2 schedule-address">{{ eventData.address[$i18n.locale] }}</p>
                        <button class="btn btn-sm btn-outline-info w-auto align-self-start" @click="locationForm = !locationForm">{{ $t('even-detail.watch_location') }}</button>
                    </div>
                    
                    <!-- Image -->
                    <div class="mb-4">
                        <img :src="imgEventThumb" alt="" class="w-100 rounded-3 img-cover schedule-img" />
                    </div>
                    <div class="border-bottom my-4"></div>
                    <div class="row w-100">
                        <div class="date col-lg-6">
                            <p>{{ $d(new Date(eventData.date), 'long') }}</p>
                        </div>
                        <div class="time col-lg-6 text-lg-end">
                            <button class="btn bg-primary text-reactive-primary mb-0">{{ eventData.time }}</button>
                        </div>
                    </div>
                </div>
            </section>

            <!-- About Section -->
            <section id="about-section" class="section-card p-5 card bg-reactive-secondary rounded-5">
                <div class="container-xxl">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h3 class="text-reactive-primary fw-bold mb-0 section-title-alt">{{ $t('even-detail.about_event') }}</h3>
                    </div>
                    <div class="border-dark rounded-3 overflow-hidden mb-4 about-expandable" :class="{'expanded': expandAbout}">
                        <img :src="imgAbout" alt="About" class="card-img-top card"/>
                        <div class="bg-reactive-secondary p-3 p-md-4">
                            <h5 class="fw-bold mb-3 about-subtitle">1. Giới thiệu chung</h5>
                            <p class="text-reactive-primary-50 about-text">Thông tin giới thiệu về sự kiện sẽ được hiển thị tại đây với chi tiết đầy đủ về chương trình và các điểm nổi bật</p>
                        
                            <h5 class="fw-bold mb-3 about-subtitle">2. Chi tiết sự kiện</h5>
                            <p class="text-reactive-primary-50 about-text">Chi tiết sự kiện sẽ được hiển thị tại đây bao gồm thông tin về các nghệ sĩ, danh sách chương trình</p>
                        
                            <h5 class="fw-bold mb-3 about-subtitle">3. Điều Khoản & Điều kiện</h5>
                            <p class="text-reactive-primary-50 about-text">Điều khoản sẽ được hiển thị tại đây cho khách hàng tham khảo trước khi mua vé</p>
                        </div>
                    </div>
                    <button @click="expandAbout = !expandAbout" class="btn btn-sm btn-outline-info fw-bold mx-auto d-block">
                        {{ expandAbout ? $t('even-detail.hide_more') : $t('even-detail.watch_more') }}
                    </button>
                </div>
            </section>

            <!-- Tickets Section -->
            <section id="tickets-section" class="section-card card bg-reactive-secondary rounded-5">
                <div class="px-2 px-md-3">
                    <h3 class="text-reactive-primary fw-bold my-3 my-md-5 mx-3 mx-md-5 section-title">{{ $t('even-detail.information_tickets') }}</h3>
                    
                    <div v-for="(schedule, dateIdx) in eventSchedule" :key="dateIdx" class="mb-4">
                        
                        <button 
                            @click="toggleTicketDate(schedule.id)"
                            class="btn w-100 text-start text-reactive-primary p-3 p-md-4 rounded-3 bg-reactive-secondary border cursor-pointer" 
                            type="button"
                        >
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="d-flex align-items-center gap-2">
                                    <i class="bi bi-calendar-event text-info ticket-icon"></i>
                                    <span class="fw-bold ticket-date">{{ schedule.time }}, {{ $d(new Date(schedule.date), 'long') }}</span>
                                </div>
                                <i class="bi bi-chevron-down transition-rotate" :class="{'rotate-180': expandedTickets[schedule.id]}"></i>
                            </div>
                        </button>

                        <div v-if="expandedTickets[schedule.id]">
                            <div class="card card-body p-0 rounded-3">

                                <div v-for="(seat, seatIdx) in schedule.seatTypes" :key="seatIdx" 
                                    class="p-3 p-md-5 bg-reactive-secondary" 
                                    :class="seatIdx !== schedule.seatTypes.length - 1 ? 'border-bottom' : ''"
                                >
                                    <div class="row g-3 align-items-start px-1">
                                        <div class="col-12 col-md-8">
                                            <div class="d-flex align-items-center gap-2 mb-2">
                                                <i class="bi bi-tag-fill text-info fs-5"></i>
                                                <span class="text-reactive-primary fw-bold fs-6">{{ seat.name }}</span>
                                            </div>
                                            <p class="text-info fw-bold mb-2 fs-6">{{ seat.price }} đ / vé</p>
                                            <p class="text-reactive-primary-50 mb-0 small">Còn {{ seat.available }} ghế</p>
                                        </div>
                                        <div class="col-12 col-md-4 text-md-end">
                                            <button class="btn btn-info fw-bold text-nowrap">{{ $t('even-detail.buy_tickets') }}</button>
                                        </div>
                                    </div>
                                    <p v-if="seat.name === 'SVIP'" class="text-reactive-primary-50 mt-3 mb-0 small lh-base">{{ eventData.svipBenefits }}</p>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </section>

            <!-- Organizer Section -->
            <section id="organizer-section" class="section-card p-5 card bg-reactive-secondary rounded-5">
                <div class="container-xxl organizer-card">
                    <h3 class="text-reactive-primary fw-bold mb-4 section-title-alt">{{ $t('even-detail.organizer') }}</h3>
                    <div class="border-dark rounded-3">
                        <div class="row g-4 align-items-start">
                            <div class="col-md-4">
                                <img
                                :src="imgOrganizerLogo"
                                alt="Organizer"
                                class="img-fluid rounded-2 ratio ratio-1x1 img-cover"
                                />
                            </div>
                            <div class="col-md-8">
                                <h4 class="text-reactive-primary fw-bold mb-3 fs-5">{{ eventData.organizer.name }}</h4>
                                <p class="text-reactive-primary-50 lh-lg small">{{ eventData.organizer.description }}</p>
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
        <h2 class="text-reactive-primary text-center fw-bold mb-5 related-title">{{ $t('even-detail.maybe_you_like') }}</h2>

        <div class="row g-3 mb-5 d-flex">
          <div v-for="event in eventData.relatedEvents" :key="event.id" class="col-6 col-lg-3">
            <div class="card bg-reactive-secondary rounded-2 h-100 overflow-hidden cursor-pointer card-hover">
              <img :src="event.image" alt="Event" class="card-img-top related-img" />
              <div class="related-body">
                <p class="text-reactive-primary fw-semibold mb-2 fs-6 lh-sm">{{ event.title }}</p>
                <div class="mt-auto">
                  <p class="text-info fw-bold mb-0 fs-6">{{ $t('even-detail.price_from') }} {{ event.price }} đ</p>
                  <div class="d-flex align-items-center gap-2 mb-2">
                    <i class="bi bi-calendar text-info small"></i>
                    <span class="text-reactive-primary small">{{ $d(new Date(event.date), 'short') }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="text-center">
          <button class="btn btn-info fw-bold px-4 py-2">{{ $t('even-detail.view_more_events') }}</button>
        </div>
      </div>
    </section>
    <Footer class="d-none d-md-block" />
  </div>
</template>

<style scoped>
/* Layout % wrapper */
.event-wrapper {
  min-height: 100vh;
  overflow-x: hidden;
  max-width: 100vw;
}

/* Hero section*/
.hero-section {
  padding-top: 1.5rem;
  min-height: 450px;
  position: relative;
  overflow: hidden;
}

.hero-bg-blur {
  background-size: cover;
  background-position: center;
  filter: blur(8px);
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100%;
  z-index: 0;
}

.hero-card {
  height: 500px;
}

.hero-card-body {
  width: 30%;
  flex-shrink: 0;
}

.hero-card-max-width {
  max-width: 1250px;
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

/* Navigation tabs */
.nav-tabs-container {
  top: 50px;
  border-color: #333 !important;
  z-index: 50;
  margin-left: 1.5rem;
  margin-right: 1.5rem;
  overflow-x: visible;
}

.nav-tab-btn {
  font-size: 0.95rem;
}

.nav-tabs-wrapper {
  flex-wrap: wrap;
}

/* Location modal */
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

.location-modal-wrapper {
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

/* Section titles */
.section-title {
  font-size: clamp(1rem, 4vw, 1.5rem);
}

.section-title-alt {
  font-size: clamp(1.2rem, 4vw, 1.5rem);
}

.section-card {
  margin: 1.5rem;
}

/* Schedule section */
.schedule-title {
  font-size: clamp(0.9rem, 3vw, 1.1rem);
}

.schedule-address {
  font-size: clamp(0.75rem, 2.5vw, 0.9rem);
}

.schedule-img {
  max-height: 200px;
}

/* About section */
.about-expandable {
  transition: max-height 1.5s ease;
  max-height: 250px;
  height: auto;
}

.about-expandable.expanded {
  max-height: 5000px;
}

.about-subtitle {
  font-size: clamp(0.95rem, 3vw, 1.25rem);
}

.about-text {
  line-height: 1.6;
  font-size: clamp(0.8rem, 2.5vw, 0.95rem);
}

/* Tickets section */
.seat-item {
  background-color: #1e1e1e;
  border-color: #333 !important;
}

/* Related events section */
.related-body {
  display: flex;
  flex-direction: column;
  padding: 1rem;
}

.related-img {
  height: 180px;
  object-fit: cover;
}

/* Sidebar */
.sidebar-img {
  width: 92%;
}

/* Utility classes */
.z-1 {
  z-index: 1;
}

.z-10 {
  z-index: 10;
}

.text-xs {
  font-size: 0.7rem;
}

.img-cover {
  object-fit: cover;
}

.cursor-pointer {
  cursor: pointer;
}

.transition-rotate {
  transition: transform 0.3s ease;
}

.card-hover {
  transition: transform 0.5s ease;
}

/* Animations */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
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

/* Responsive - tablet */
@media (max-width: 1024px) {
  .content-row {
    flex-direction: column;
    margin: 0 !important;
  }
}

/* Responsive - mobile */
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

/* Responsive - desktop */
@media (min-width: 768px) {
  .hero-grid {
    grid-template-columns: 1fr 300px !important;
  }
}
</style>