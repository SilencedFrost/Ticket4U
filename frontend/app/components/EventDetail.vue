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

const expandAbout = ref(false);

const eventData = {
  title: 'GIAO HƯỞNG MÙA YÊU - LIVE CONCERT ĐẶC BIỆT TẠI NHÀ HÁT HỒ GƯƠM',
  date: '2025-02-20',
  time: '20:00 - 22:30',
  venue: {
      vi: 'Nhà hát Hồ Gươm',
      en: 'Ho Guom Opera'
    },
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
  <div class="bg-reactive-primary min-vh-100 overflow-x-hidden mw-100" >
    <!-- Hero Banner with Categories -->
    <section class="pt-4 position-relative overflow-hidden min-vh-50">
      <div class="position-absolute top-0 h-100 start-0 end-0 overflow-hidden">
        <img :src="imgTest2" class="w-100 h-100 object-fit-cover hero-bg-blur">
      </div>
      <div class="position-relative z-1">
        <!-- Event Info and Poster Container -->
        <div class="container-xxl pb-4">
            <div class="g-3 align-items-start hero-grid">
                <!-- Event Card -->
                <div class="col-12 h-auto">
                  <div class="card d-flex flex-column flex-md-row bg-reactive-secondary border-dark rounded-5 shadow-lg mx-auto overflow-hidden">
                      <div class="card-body p-4 d-flex flex-column order-2 order-md-1 col-12 col-md-3">
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
                                <span class="text-primary fw-semibold">{{ eventData.venue[$i18n.locale] }}</span>
                                <p class="mb-0 small">{{ eventData.address[$i18n.locale] }}</p>
                            </div>
                            <div class="mt-auto"> 
                                <hr class="bg-secondary my-2" />
                                <p class="text-reactive-primary fw-semibold mb-1 text-xs">{{ $t('even-detail.price_from') }}</p>
                                <p class="text-info fw-bold mb-2 fs-6">{{ eventData.startPrice }}</p>
                                <button class="btn btn-info text-reactive-primary fw-bold w-100  py-1 small">{{ $t('even-detail.buy_tickets') }}</button>
                            </div>
                      </div>
                      <img :src="imgTest2" class="order-1 order-md-2 col-12 col-md-9 object-fit-cover shadow-lg my-dashed-line" />
                  </div>
                </div>
            </div>
        </div>
      </div>
    </section>

    <!-- Navigation Tabs with Scroll Links -->
    <div class="bg-reactive-primary border-bottom border-dark overflow-x-auto mx-4 sticky-top nav-sticky">
      <div class="container-xxl">
        <div class="d-flex gap-3 gap-md-4 flex-wrap py-3 nav-tabs-wrapper">
          <button
            @click="scrollToSection('schedule-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('even-detail.schedule_event') }}
          </button>
          <button
            @click="scrollToSection('about-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('even-detail.about_event') }}
          </button>
          <button
            @click="scrollToSection('tickets-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('even-detail.information_tickets') }}
          </button>
          <button
            @click="scrollToSection('organizer-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('even-detail.organizer') }}
          </button>
        </div>
      </div>
    </div>

    <div class="row m-0 container-xxl mx-auto flex-column flex-lg-row">
        <div class="col-lg-9">
            <!-- Schedule Section -->
            <section id="schedule-section" class="p-3 p-md-4 card bg-reactive-secondary rounded-5 m-3 mx-auto mw-100">
                <div class="container-xxl">
                    <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 h4"> {{ $t('even-detail.schedule_location') }}</h3>
                    
                    <!-- Info and Button -->
                    <div class="d-flex flex-column gap-2 mb-3">
                        <h6 class="text-reactive-primary mb-1">{{ eventData.title }}</h6>
                        <p class="text-reactive-secondary mb-2 small">{{ eventData.address[$i18n.locale] }}</p>
                    </div>
                    
                    <!-- Image -->
                    <div class="mb-4">
                        <img :src="imgEventThumb" alt="" class="w-auto rounded-3 object-fit-cover img-fluid mh-100" />
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
            <section id="about-section" class="p-3 p-md-4 card bg-reactive-secondary rounded-5 m-3 mx-auto mw-100">
                <div class="container-xxl">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h3 class="text-reactive-primary fw-bold mb-0 h4">{{ $t('even-detail.about_event') }}</h3>
                    </div>
                    <div class="border-dark rounded-3 overflow-hidden mb-4 about-expandable" :class="{'expanded': expandAbout}">
                        <img :src="imgAbout" alt="About" class="card-img-top card"/>
                        <div class="bg-reactive-secondary p-3 p-md-4">
                            <h5 class="fw-bold mb-3">1. Giới thiệu chung</h5>
                            <p class="text-reactive-primary-50">Thông tin giới thiệu về sự kiện sẽ được hiển thị tại đây với chi tiết đầy đủ về chương trình và các điểm nổi bật</p>
                        
                            <h5 class="fw-bold mb-3">2. Chi tiết sự kiện</h5>
                            <p class="text-reactive-primary-50">Chi tiết sự kiện sẽ được hiển thị tại đây bao gồm thông tin về các nghệ sĩ, danh sách chương trình</p>
                        
                            <h5 class="fw-bold mb-3">3. Điều Khoản & Điều kiện</h5>
                            <p class="text-reactive-primary-50">Điều khoản sẽ được hiển thị tại đây cho khách hàng tham khảo trước khi mua vé</p>
                        </div>
                    </div>
                    <button @click="expandAbout = !expandAbout" class="btn btn-sm btn-outline-info fw-bold mx-auto d-block">
                        {{ expandAbout ? $t('even-detail.see_less') : $t('even-detail.see_more') }}
                    </button>
                </div>
            </section>

            <!-- Tickets Section -->
            <section id="tickets-section" class="card bg-reactive-secondary rounded-5 m-3 mx-auto mw-100">
                <div class="p-3 p-md-4">
                    <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 h4">{{ $t('even-detail.information_tickets') }}</h3>
                    
                    <div v-for="(schedule, dateIdx) in eventSchedule" :key="dateIdx" class="mb-3">
                        <div class="card bg-reactive-primary border-0 rounded-3 shadow-sm mb-3">
                            <button 
                                @click="toggleTicketDate(schedule.id)"
                                class="btn w-100 text-start p-3 p-md-4 d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center border-0 bg-transparent gap-3" 
                                type="button"
                            >
                                <div class="d-flex flex-column flex-md-row align-items-start align-items-md-center gap-2 flex-grow-1">
                                    <i class="bi bi-calendar-event text-info fs-5"></i>
                                    <span class="text-reactive-primary fw-semibold small">{{ schedule.time }}, {{ $d(new Date(schedule.date), 'long') }}</span>
                                </div>
                                <div class="d-flex align-items-center justify-content-between justify-content-md-end gap-2 gap-md-3 w-100 w-md-auto">
                                    <button class="btn bg-primary fw-bold px-3 px-md-4 py-2 rounded-3 small">{{ $t('even-detail.buy_tickets') }}</button>
                                    <i class="bi fs-4 text-reactive-primary flex-shrink-0" :class="expandedTickets[schedule.id] ? 'bi-chevron-up' : 'bi-chevron-down'"></i>
                                </div>
                            </button>
                        </div>

                        <div v-if="expandedTickets[schedule.id]" class="mt-3">
                            <h5 class="text-reactive-primary fw-bold mb-3 ms-2 small">{{ $t('even-detail.information_tickets') }}</h5>
                            
                            <div class="d-flex flex-column gap-2 gap-md-3">
                                <div v-for="(seat, seatIdx) in schedule.seatTypes" :key="seatIdx" 
                                    class="card bg-reactive-primary border-0 rounded-3 shadow-sm overflow-hidden"
                                >
                                    <div class="card-body p-3 p-md-4">
                                        <div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2 gap-md-0">
                                            <div class="flex-grow-1">
                                                <h6 class="text-reactive-primary fw-bold mb-1 mb-md-2">{{ seat.name }}</h6>
                                                <p class="text-reactive-primary-50 mb-0 small"> {{ seat.available }} {{ $t('even-detail.seat_left') }}</p>
                                            </div>
                                            <div class="text-start text-md-end">
                                                <p class="text-primary fw-bold fs-5 mb-0">{{ seat.price }} đ</p>
                                            </div>
                                        </div>
                                        <div v-if="seat.name === 'SVIP'" class="mt-2 mt-md-3 pt-2 pt-md-3 border-top border-secondary">
                                            <p class="text-reactive-primary-50 mb-0 small">{{ eventData.svipBenefits }}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- Organizer Section -->
            <section id="organizer-section" class="p-3 p-md-4 card bg-reactive-secondary rounded-5 m-3 mx-auto mw-100">
                <div class="container-xxl organizer-card">
                    <h3 class="text-reactive-primary fw-bold mb-4 h4">{{ $t('even-detail.organizer') }}</h3>
                    <div class="border-dark rounded-3">
                        <div class="row g-4 align-items-start">
                            <div class="col-md-4">
                                <img
                                :src="imgOrganizerLogo"
                                alt="Organizer"
                                class="img-fluid rounded-2 ratio ratio-1x1 object-fit-cover"
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
        <div class="col-lg-3 mt-3 d-none d-lg-block px-2 ">
            <img :src="imgSidebar" alt="" class="rounded-5 img-fluid object-fit-cover"/>
        </div>
    </div>
    
    
    <!-- Related Events Section -->
    <section class="py-5">
      <div class="container-xxl">
        <h2 class="text-reactive-primary text-center fw-bold mb-5 related-title">{{ $t('even-detail.maybe_you_like') }}</h2>

        <div class="row g-3 mb-5 d-flex">
          <div v-for="event in eventData.relatedEvents" :key="event.id" class="col-6 col-lg-3">
            <div class="card bg-reactive-secondary rounded-3 h-100 overflow-hidden">
              <img :src="event.image" alt="Event" class="card-img-top object-fit-cover h-auto" />
              <div class="d-flex flex-column p-3">
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
.hero-bg-blur {
  filter: blur(8px);
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

.about-expandable {
  max-height: 250px;
  transition: max-height 2.0s;
}

.about-expandable.expanded {
  max-height: 5000px;
}

@media (max-width: 768px) {
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

  .nav-sticky {
    scrollbar-width: none;
  }

  .nav-sticky::-webkit-scrollbar {
    display: none;
  }

  .nav-tabs-wrapper {
    min-width: max-content;
  }
}
</style>