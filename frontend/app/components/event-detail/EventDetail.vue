<script setup lang="ts">
import EventHero from './sections/EventHero.vue';
import EventSchedule from './sections/EventSchedule.vue';
import EventAbout from './sections/EventAbout.vue';
import EventTickets from './sections/EventTickets.vue';
import EventOrganizer from './sections/EventOrganizer.vue';
import EventNav from './sections/EventNav.vue';
import EventRelated from './sections/EventRelated.vue';
import EventAds from './sections/EventAds.vue';

const imgTest2 = 'https://salt.tkbcdn.com/ts/ds/00/6c/a8/6f638ec63c1be2aa163fdda0d3e07106.jpg';
const imgEventThumb =
  'https://firebasestorage.googleapis.com/v0/b/cticket-prod.appspot.com/o/event_images%2Fseatmap_brothers_revised4%20-%20pro.svg?alt=media&token=7105d904-546b-4046-8fa0-f5625e533b1a';
const imgOrganizerLogo =
  'https://salt.tkbcdn.com/ts/ds/be/d4/a4/e6c34b2216009af82feba5f9bca782ad.jpg';
const imgAbout = 'https://assets.cticket.vn/tix/the-brothers/LDP%20THE%20BROTHERS%20(1).webp';
const imgEvent1 =
  'https://images.tkbcdn.com/2/608/332/ts/ds/c5/98/c8/c05ab41ca42f7c41bb7fc720518259db.jpg';
const imgEvent2 =
  'https://images.tkbcdn.com/2/608/332/ts/ds/9d/63/2b/8e002e32d1e883c3615ca708c0293c58.jpg';
const imgEvent3 =
  'https://images.tkbcdn.com/2/608/332/ts/ds/c7/b9/71/7c6753138a01713f966081ac0ac8483b.jpg';
const imgEvent4 =
  'https://images.tkbcdn.com/2/608/332/ts/ds/c2/4d/12/585693b771d27f331cd28960e091a579.jpg';
const imgSidebar =
  'https://ticketbox.vn/_next/image?url=https%3A%2F%2Fsalt.tkbcdn.com%2Fts%2Fds%2Fab%2Fdf%2F5c%2F6da51dd7722a871f5cf65ca2f0e3e08e.jpg&w=3840&q=75';

const eventData = {
  title: 'GIAO HƯỞNG MÙA YÊU - LIVE CONCERT ĐẶC BIỆT TẠI NHÀ HÁT HỒ GƯƠM',
  date: '2025-02-20',
  time: '20:00 - 22:30',
  venue: {
    vi: 'Nhà hát Hồ Gươm',
    en: 'Ho Guom Opera',
  },
  address: {
    vi: '40 Hàng Bài, Phường Cửa Nam, Quận Hoàn Kiếm, Thành Phố Hà Nội',
    en: '40 Hang Bai St, Cua Nam Ward, Hoan Kiem Dist, Hanoi City',
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
    {
      id: 1,
      image: imgEvent1,
      title: 'Nhà Hát Kịch IDECAF: Tám Cám Đại Chiến',
      date: '2026-01-03',
      price: '270.000',
    },
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
      { name: 'B', price: '1.000.000', available: 300 },
    ],
  },
];
</script>

<template>
  <div class="h-auto bg-reactive-primary overflow-x-hidden mw-100">
    <event-hero :event-data="eventData" :hero-image="imgTest2" />
    <event-nav />
    <div class="row m-0 container-xxl mx-auto flex-column flex-lg-row">
      <div class="col-lg-9">
        <event-schedule :event-data="eventData" :event-thumb-image="imgEventThumb" />
        <event-about :event-data="eventData" :about-image="imgAbout" />
        <event-tickets
          :event-data="eventData"
          :event-schedule="eventSchedule"
          :hero-image="imgTest2"
        />
        <event-organizer :event-data="eventData" :organizer-image="imgOrganizerLogo" />
      </div>
      <event-ads :side-bar-image="imgSidebar" />
    </div>
    <event-related :event-data="eventData" />
  </div>
</template>
