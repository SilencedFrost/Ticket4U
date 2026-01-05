<script setup lang="ts">
import EventHero from './sections/EventHero.vue';
import EventSchedule from './sections/EventSchedule.vue';
import EventAbout from './sections/EventAbout.vue';
import EventTickets from './sections/EventTickets.vue';
import EventOrganizer from './sections/EventOrganizer.vue';
import EventNav from './sections/EventNav.vue';
import EventRelated from './sections/EventRelated.vue';
import EventAds from './sections/EventAds.vue';
import type { Showtime, EventDetail } from '@/types/event-detail';

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

const eventData: EventDetail = {
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

const showtimes: Showtime[] = [
  {
    id: 'collapseDate1',
    date: '2025-02-20',
    time: '20:00 - 22:30',
    seatTypes: [
      {
        name: 'SVIP - Dừa Lòng',
        price: '3.000.000 đ',
        available: 100,
        image: 'https://salt.tkbcdn.com/ts/ds/c8/14/ee/4e882acd2e9f1d8a4e03380751f772ba.jpg',
        benefits: [
          'Check-in lối đi riêng',
          'Tặng bộ quà tặng Exclusive',
          'Phục vụ đồ uống tại chỗ',
        ],
      },
      {
        name: 'Vé Phổ Thông (Zone B)',
        price: '500.000 đ',
        available: 300,
        description: 'Vé ngồi khu vực cánh gà, tầm nhìn bao quát.',
      },
      {
        name: 'Vé Tiết Kiệm',
        price: '300.000 đ',
        available: 0,
        description: 'Vé ngồi khu vực cánh gà, tầm nhìn bao quát.',
        image: 'https://salt.tkbcdn.com/ts/ds/c8/14/ee/4e882acd2e9f1d8a4e03380751f772ba.jpg',
        benefits: [
          'Check-in lối đi riêng',
          'Tặng bộ quà tặng Exclusive',
          'Phục vụ đồ uống tại chỗ',
        ],
      },
    ],
  },
];
</script>

<template>
  <div class="h-auto bg-reactive-primary overflow-x-hidden mw-100">
    <event-hero
      :title="eventData.title"
      :date="eventData.date"
      :time="eventData.time"
      :venue="eventData.venue"
      :address="eventData.address"
      :start-price="eventData.startPrice"
      :hero-image="imgTest2"
    />
    <event-nav />
    <div class="row m-0 container-xxl mx-auto flex-column flex-lg-row">
      <div class="col-lg-9">
        <event-schedule
          :title="eventData.title"
          :address="eventData.address"
          :date="eventData.date"
          :time="eventData.time"
          :event-thumb-image="imgEventThumb"
        />
        <event-about :event-data="eventData" :about-image="imgAbout" />
        <event-tickets :show-time="showtimes" />
        <event-organizer :event-data="eventData.organizer" :organizer-image="imgOrganizerLogo" />
      </div>
      <event-ads :side-bar-image="imgSidebar" />
    </div>
    <event-related :event-data="eventData.relatedEvents" />
  </div>
</template>
