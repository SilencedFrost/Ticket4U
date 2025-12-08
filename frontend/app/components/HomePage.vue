<template>
  <div class="homepage bg-reactive-primary">
    <!-- New Event Carousel -->
    <section class="new-event-section py-5">
      <div class="container-xxl">
        <div class="position-relative">
          <div class="row g-3">
            <div class="col-md-6">
              <div class="event-card-large rounded-4 position-relative overflow-hidden">
                <img :src="newEvents[currentNewEvent]?.image || ''" alt="" class="w-100 h-100">
                <button class="btn btn-light position-absolute bottom-0 start-0 m-3 rounded-2">Xem chi tiết</button>
              </div>
            </div>
            <div class="col-md-6">
              <div class="event-card-large rounded-4 position-relative overflow-hidden">
                <img :src="newEvents[(currentNewEvent + 1) % newEvents.length]?.image || ''" alt="" class="w-100 h-100">
                <button class="btn btn-light position-absolute bottom-0 start-0 m-3 rounded-2">Xem chi tiết</button>
              </div>
            </div>
          </div>
          
          <!-- Navigation buttons -->
          <button class="btn btn-dark carousel-btn carousel-btn-prev" @click="prevNewEvent">
            &lt;
          </button>
          <button class="btn btn-dark carousel-btn carousel-btn-next" @click="nextNewEvent">
            &gt;
          </button>
          
          <!-- Dots indicator -->
          <div class="carousel-dots">
            <span 
              v-for="(_, index) in Math.ceil(newEvents.length / 2)" 
              :key="index"
              :class="{ active: index === Math.floor(currentNewEvent / 2) }"
              @click="currentNewEvent = index * 2"
            ></span>
          </div>
        </div>
      </div>
    </section>

    <!-- Special Events -->
    <section class="special-event-section py-5">
      <div class="container-xxl">
        <h2 class="section-title text-white fw-bold mb-4">Sự kiện đặc biệt</h2>
        <div class="position-relative">
          <div class="row g-3">
            <div 
              v-for="(event, index) in getVisibleSpecialEvents()" 
              :key="index"
              class="col-md-3"
            >
              <div class="event-card rounded-4 overflow-hidden">
                <img :src="event.image" alt="" class="w-100 h-100 object-fit-cover">
              </div>
            </div>
          </div>
          
          <button class="btn btn-dark carousel-btn carousel-btn-prev" @click="prevSpecialEvent">
            &lt;
          </button>
          <button class="btn btn-dark carousel-btn carousel-btn-next" @click="nextSpecialEvent">
            &gt;
          </button>
        </div>
      </div>
    </section>

    <!-- Trendy Events -->
    <section class="trendy-event-section py-5">
      <div class="container-xxl">
        <h2 class="section-title text-white fw-bold mb-4">Sự kiện xu hướng</h2>
        <div class="position-relative">
          <div class="row g-4">
            <div 
              v-for="(event, index) in getVisibleTrendyEvents()" 
              :key="index"
              class="col-md-4"
            >
              <div class="trendy-event-wrapper d-flex align-items-end">
                <div class="trendy-badge" v-html="event.badge"></div>
                <div class="trendy-card rounded-3 overflow-hidden">
                  <img :src="event.image" alt="event" class="w-100 h-100 object-fit-cover">
                </div>
              </div>
            </div>
          </div>
          
          <button class="btn btn-dark carousel-btn carousel-btn-prev" @click="prevTrendyEvent">
            &lt;
          </button>
          <button class="btn btn-dark carousel-btn carousel-btn-next" @click="nextTrendyEvent">
            &gt;
          </button>
        </div>
      </div>
    </section>

    <!-- Suggest Events -->
    <section class="suggest-event-section py-5">
      <div class="container-xxl">
        <h2 class="section-title text-white fw-bold mb-4">Dành cho bạn</h2>
        <div class="position-relative">
          <div class="row g-3">
            <div 
              v-for="(event, index) in getVisibleSuggestEvents()" 
              :key="index"
              class="col-md-3"
            >
              <div class="suggest-card">
                <div class="suggest-card-image rounded-3 overflow-hidden mb-3">
                  <img :src="event.image" alt="" class="w-100 h-100 object-fit-cover">
                </div>
                <h3 class="suggest-card-title text-white fw-bold mb-2">{{ event.title }}</h3>
                <p class="suggest-card-price mb-1" style="color: #07b3df; font-weight: 500;">{{ event.price }}</p>
                <p class="suggest-card-date text-white-50">{{ event.date }}</p>
              </div>
            </div>
          </div>
          
          <button class="btn btn-dark carousel-btn carousel-btn-prev" @click="prevSuggestEvent">
            &lt;
          </button>
          <button class="btn btn-dark carousel-btn carousel-btn-next" @click="nextSuggestEvent">
            &gt;
          </button>
        </div>
      </div>
    </section>

    <!-- Music Events -->
    <section class="music-event-section py-5">
      <div class="container-xxl">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2 class="section-title text-white fw-bold mb-0">Nhạc sống</h2>
          <NuxtLink to="/event/display" class="text-white text-decoration-none" style="font-weight: 200;">Xem thêm &gt;</NuxtLink>
        </div>
        <div class="row g-3">
          <div 
            v-for="(event, index) in musicEvents.slice(0, 4)" 
            :key="index"
            class="col-md-3"
          >
            <div class="suggest-card">
              <div class="suggest-card-image rounded-3 overflow-hidden mb-3">
                <img :src="event.image" alt="" class="w-100 h-100 object-fit-cover">
              </div>
              <h3 class="suggest-card-title text-white fw-bold mb-2">{{ event.title }}</h3>
              <p class="suggest-card-price mb-1" style="color: #07b3df; font-weight: 500;">{{ event.price }}</p>
              <p class="suggest-card-date text-white-50">{{ event.date }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Art Events -->
    <section class="art-event-section py-5">
      <div class="container-xxl">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2 class="section-title text-white fw-bold mb-0">Sân khấu và nghệ thuật</h2>
          <NuxtLink to="/event/display" class="text-white text-decoration-none" style="font-weight: 200;">Xem thêm &gt;</NuxtLink>
        </div>
        <div class="row g-3">
          <div 
            v-for="(event, index) in artEvents.slice(0, 4)" 
            :key="index"
            class="col-md-3"
          >
            <div class="suggest-card">
              <div class="suggest-card-image rounded-3 overflow-hidden mb-3">
                <img :src="event.image" alt="" class="w-100 h-100 object-fit-cover">
              </div>
              <h3 class="suggest-card-title text-white fw-bold mb-2">{{ event.title }}</h3>
              <p class="suggest-card-price mb-1" style="color: #07b3df; font-weight: 500;">{{ event.price }}</p>
              <p class="suggest-card-date text-white-50">{{ event.date }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Other Events -->
    <section class="other-event-section py-5">
      <div class="container-xxl">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2 class="section-title text-white fw-bold mb-0">Thể loại khác</h2>
          <NuxtLink to="/event/display" class="text-white text-decoration-none" style="font-weight: 200;">Xem thêm &gt;</NuxtLink>
        </div>
        <div class="row g-3">
          <div 
            v-for="(event, index) in otherEvents.slice(0, 4)" 
            :key="index"
            class="col-md-3"
          >
            <div class="suggest-card">
              <div class="suggest-card-image rounded-3 overflow-hidden mb-3">
                <img :src="event.image" alt="" class="w-100 h-100 object-fit-cover">
              </div>
              <h3 class="suggest-card-title text-white fw-bold mb-2">{{ event.title }}</h3>
              <p class="suggest-card-price mb-1" style="color: #07b3df; font-weight: 500;">{{ event.price }}</p>
              <p class="suggest-card-date text-white-50">{{ event.date }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Nice Places -->
    <section class="nice-place-section py-5">
      <div class="container-xxl">
        <h2 class="section-title text-white fw-bold mb-4">Điểm đến thú vị</h2>
        <div class="row g-3">
          <div 
            v-for="(place, index) in nicePlaces" 
            :key="index"
            class="col-md-3"
          >
            <div class="place-card rounded-4 overflow-hidden position-relative">
              <img :src="place.image" alt="" class="w-100 h-100 object-fit-cover">
              <div class="place-overlay position-absolute bottom-0 start-0 end-0 p-4">
                <h3 class="text-white fw-bold text-center fs-2">{{ place.name }}</h3>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// Type definitions
interface NewEvent {
  image: string
}

interface SpecialEvent {
  image: string
}

interface TrendyEvent {
  image: string
  badge: string
}

interface SuggestEvent {
  image: string
  title: string
  price: string
  date: string
}

interface NicePlace {
  image: string
  name: string
}

// New Events data
const newEvents = ref<NewEvent[]>([
  { image: 'https://www.figma.com/api/mcp/asset/5d2f04a5-58e9-4144-899a-ea7908db0a47' },
  { image: 'https://www.figma.com/api/mcp/asset/cddd8d16-b0d5-45d9-b470-758b54fa7abf' },
  { image: 'https://www.figma.com/api/mcp/asset/5d2f04a5-58e9-4144-899a-ea7908db0a47' },
  { image: 'https://www.figma.com/api/mcp/asset/cddd8d16-b0d5-45d9-b470-758b54fa7abf' },
])

// Special Events data
const specialEvents = ref<SpecialEvent[]>([
  { image: 'https://www.figma.com/api/mcp/asset/4855039a-af6a-43e5-b01a-7df026311dbf' },
  { image: 'https://www.figma.com/api/mcp/asset/812f2012-922e-46ec-8130-6de320144cef' },
  { image: 'https://www.figma.com/api/mcp/asset/e3746f96-d689-4589-9345-09d7adfd7819' },
  { image: 'https://www.figma.com/api/mcp/asset/d7e6c10a-7e50-4865-b816-c954f7fb84d0' },
  { image: 'https://www.figma.com/api/mcp/asset/4855039a-af6a-43e5-b01a-7df026311dbf' },
  { image: 'https://www.figma.com/api/mcp/asset/812f2012-922e-46ec-8130-6de320144cef' },
])

// Trendy Events data
const trendyEvents = ref<TrendyEvent[]>([
  { 
    image: 'https://www.figma.com/api/mcp/asset/5f99206f-baec-4ad8-bcd3-8120563c2670',
    badge: `<svg xmlns="http://www.w3.org/2000/svg" width="60" height="150" viewBox="0 0 60 150" fill="none">
<path d="M0 13.35L25.1044 11.398V150H39.8511V0L0 2.55V13.35ZM48.8022 0H45.1178V150H59.8622V9.6L48.8022 0Z" fill="#07B3DF"/>
<path d="M0 28.5L20.0133 27.15V15.9L0 17.1V28.5Z" fill="#07B3DF"/>
</svg>`
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/da3fea4d-b27e-405e-a827-ea458ea98593',
    badge: `<svg xmlns="http://www.w3.org/2000/svg" width="110" height="150" viewBox="0 0 110 150" fill="none">
<path d="M12.1457 44.2178C12.4715 33.5638 15.3855 25.6118 20.0823 19.6118C25.1049 13.6098 31.0968 9.70779 38.7075 7.30579C46.2419 5.0087 54.1338 3.99353 62.0296 4.30579C69.8043 4.60579 77.4151 5.95578 83.8947 8.05779C78.1238 4.81445 71.8273 2.53053 65.2694 1.30179C57.9825 -0.0482143 50.5336 -0.348214 42.9166 0.401786C35.4677 1.30179 28.3426 3.40179 21.8651 6.70579C15.3855 10.0058 10.3649 14.6578 6.15377 20.9598C2.10453 27.2618 0 35.2158 0 44.8178V49.1678H12.1457V44.2178Z" fill="#07B3DF"/>
<path d="M86.6395 80.351C91.9732 74.9448 95.8528 69.0924 98.4378 62.3377C100.861 56.0311 101.669 49.7285 101.346 43.1258C100.861 36.8192 99.082 31.1169 95.5256 25.5626C92.2943 20.6066 87.2817 16.2568 81.3017 12.9535C74.835 9.65014 67.2395 8.00146 58.0262 8.00146C50.4306 8.00146 44.1255 8.90183 38.6282 11.0027C33.6177 12.9535 29.7401 15.5045 26.5068 18.5058C23.5966 21.9592 21.1731 24.8103 19.717 28.5659C18.2629 31.8692 17.2935 35.1686 16.8088 38.4739C16.4836 41.4751 16.1626 44.1762 16.1626 46.129V49.1303H27.9629V45.8289C27.6398 43.4259 27.9629 40.8749 28.6092 37.4255C29.0939 34.5703 30.3864 31.1189 32.6504 28.2677C34.7528 24.8163 37.663 22.2653 41.8636 19.8603C46.2279 17.9075 51.5637 16.559 58.6745 16.559C64.495 16.559 69.8266 17.9075 74.0293 20.6086C78.7167 22.8615 81.9501 26.1629 84.5372 29.9144C87.2837 33.816 88.4168 37.8697 88.4168 41.7753C88.4168 49.1303 87.2837 55.4348 84.8603 61.4393C82.5963 66.9936 78.7188 72.3958 73.3851 77.0497C68.3725 81.4035 62.231 87.2578 54.1508 94.6128C46.23 102.27 37.9861 109.923 29.7422 117.578C21.4983 125.533 14.3875 132.14 8.73065 137.842C2.91022 143.099 0 146.1 0 146.1V150.001H100.215L103.774 139.043H24.0896C24.0896 139.043 26.513 137.09 31.2025 132.438C35.8878 128.085 41.5446 122.532 48.6576 116.226C55.4453 109.921 62.2331 103.316 69.5076 96.5636C76.2953 90.2571 81.9542 84.7028 86.6395 80.351Z" fill="#07B3DF"/>
<path d="M47.6196 124.492L35.7978 135.746H105.762L109.649 124.492H47.6196ZM83.8947 43.1655C83.8947 37.4655 82.7636 32.9615 79.8476 29.5115C77.4172 26.2095 74.1774 23.9595 70.2921 22.6095C66.2429 21.2595 62.3555 20.6575 57.9846 20.9575C53.7713 20.9575 49.884 21.5575 46.6462 22.9095C42.9208 24.2595 40.4904 25.9095 39.0334 27.8595C49.3983 24.5595 57.3349 24.8595 62.6813 28.9115C68.0257 32.9615 70.6159 38.2115 70.9417 44.8175C71.2655 51.4175 68.8351 57.4195 64.1383 62.8235C57.3349 69.4235 47.9413 78.7275 36.6051 89.9815C24.7832 101.536 15.0679 110.838 7.12926 118.192C3.24398 122.542 0.651716 124.792 0.325867 125.242V139.496L68.0236 75.7255C68.0236 75.7255 68.833 74.9735 70.618 73.7735C72.0729 72.1235 74.1815 69.7235 75.9602 67.0195C78.0668 64.0195 79.8476 60.7195 81.6262 56.8175C83.0853 52.4655 83.8947 48.1155 83.8947 43.1635V43.1655Z" fill="#07B3DF"/>
</svg>`
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/f2268936-ace6-4fa6-a48d-14bfa94f8058',
    badge: `<svg xmlns="http://www.w3.org/2000/svg" width="110" height="150" viewBox="0 0 110 150" fill="none">
<path d="M70.133 11.26L26.2913 60.494L31.9437 71.604L94.4237 0.00402832H8.87848L11.4722 11.26H70.133Z" fill="#07B3DF"/>
<path d="M12.0839 15.312L14.527 25.822H51.3442L59.8972 15.312H12.0839ZM56.6879 75.806H59.133C60.5089 76.106 62.6472 76.106 64.7874 76.856C67.2305 77.608 69.3707 78.656 71.8119 80.308C73.9521 81.66 76.0903 84.212 77.4663 86.912C79.2996 90.064 80.0619 93.818 80.0619 98.77C80.0619 104.324 78.5374 109.126 76.0903 113.63C73.3423 117.836 69.3707 120.69 63.7182 122.338C58.3726 124.138 52.1084 123.838 43.8604 121.738C44.6246 123.838 47.0677 125.492 52.1084 126.54C56.6918 127.892 62.0393 128.34 67.9967 127.292C73.954 126.54 79.6046 123.84 84.1879 118.884C89.2286 114.384 91.9786 107.028 93.0477 97.42C93.0477 90.816 91.6737 85.26 89.5354 81.058C87.0923 77.154 84.1898 74.152 81.133 72.05C77.9256 69.95 74.4095 68.15 71.204 67.55C67.6898 66.496 64.7854 66.046 62.6472 66.046H59.1349L104.042 14.71L99.7674 0L34.0781 75.804H56.686L56.6879 75.806Z" fill="#07B3DF"/>
<path d="M75.7835 53.292L68.759 62.598C68.759 62.598 69.6737 62.898 71.812 63.35C73.6453 63.35 76.0904 64.402 78.9948 65.45C82.245 66.8701 85.24 68.8501 87.8527 71.306C90.6027 74.16 93.0477 77.61 95.186 82.416C97.0193 87.37 98.0884 93.222 98.0884 100.428C98.0884 105.682 96.562 110.634 94.1169 114.388C91.6718 118.29 88.4625 121.292 84.6472 124.142C80.6756 126.546 76.8546 128.646 72.5781 129.698C68.2997 131.048 64.7855 131.798 61.2732 131.798C55.6188 131.798 50.5781 130.748 46.7609 128.646C42.9398 126.546 39.7325 123.842 37.5942 120.99C35.1491 117.838 33.3158 114.686 32.2486 111.684C30.8746 108.532 30.1084 106.13 29.8055 104.028C29.4986 101.926 29.0413 100.876 29.0413 100.876H16.3623C16.3623 100.876 16.3623 101.926 16.6672 104.328C17.2418 107.364 17.956 110.37 18.8074 113.336C20.1814 116.788 22.0148 120.69 24.4598 124.144C26.9049 127.896 30.5697 131.346 34.6937 134.5C39.277 137.354 44.9295 139.456 51.6511 140.806C54.8584 141.106 59.1349 141.106 64.4825 141.106C70.1349 140.806 75.7874 139.756 81.7467 137.654C87.8565 136.002 93.509 132.402 98.3972 127.896C103.285 123.396 106.495 116.788 108.328 108.532C110.007 99.076 110.466 91.12 108.633 84.816C106.954 78.21 104.357 73.108 101.149 68.904C97.9294 65.0243 94.1089 61.7272 89.8442 59.148C86.332 57.048 82.8158 55.396 80.0677 54.644C77.1634 53.594 75.7893 53.294 75.7893 53.294L75.7835 53.292Z" fill="#07B3DF"/>
<path d="M11.779 100.874H0.0167198C-0.292052 114.684 3.68146 125.492 11.4722 133.148C18.8055 140.804 28.4295 145.608 39.2771 148.158C50.2771 150.562 60.9683 150.562 71.2041 148.458C81.135 146.358 88.4664 142.604 93.0497 136.598C83.5781 141.554 74.4115 144.256 64.7874 144.558C55.3158 145.308 46.7629 143.958 38.6634 140.504C30.5697 137.052 24.1511 131.8 19.1123 125.192C14.2241 118.288 11.7809 110.182 11.7809 100.876L11.779 100.874Z" fill="#07B3DF"/>
</svg>`
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/5f99206f-baec-4ad8-bcd3-8120563c2670',
    badge: `<svg xmlns="http://www.w3.org/2000/svg" width="60" height="150" viewBox="0 0 60 150" fill="none">
<path d="M0 13.35L25.1044 11.398V150H39.8511V0L0 2.55V13.35ZM48.8022 0H45.1178V150H59.8622V9.6L48.8022 0Z" fill="#07B3DF"/>
<path d="M0 28.5L20.0133 27.15V15.9L0 17.1V28.5Z" fill="#07B3DF"/>
</svg>`
  },
])

// Suggest Events data
const suggestEvents = ref<SuggestEvent[]>([
  { 
    image: 'https://www.figma.com/api/mcp/asset/0efb35f2-972b-446e-baac-4076f54b8350',
    title: 'KỊCH MA 4D: OÁN LINH CHI',
    price: 'Từ 100.000đ',
    date: '26 tháng 11,2025'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/3067ce1c-93de-4976-8259-163dd7f02cc5',
    title: 'THE GENTLEMEN - COUNTDOWN CONCERT 2026',
    price: 'Từ 900.000đ',
    date: '31 tháng 12,2025'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/f9333c84-58fe-4b89-b801-d81fcba75bf0',
    title: 'CHÀO SHOW',
    price: 'Từ 1.040.000đ',
    date: '25 tháng 11,2025'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/14588a50-7003-4ce4-9c45-46951fabdd18',
    title: '[DẾ GARDEN] Stress Breaker Workshop',
    price: 'Từ 375.000đ',
    date: '25 Tháng 11,2025'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/0efb35f2-972b-446e-baac-4076f54b8350',
    title: 'KỊCH MA 4D: OÁN LINH CHI',
    price: 'Từ 100.000đ',
    date: '26 tháng 11,2025'
  },
])

// Music Events data
const musicEvents = ref<SuggestEvent[]>([
  { 
    image: 'https://www.figma.com/api/mcp/asset/29216888-4302-4d2e-b9d4-a947358d33ad',
    title: 'SOOBIN LIVE CONCERT: ALL-ROUNDER THE FINAL',
    price: 'Từ 100.000đ',
    date: '26 tháng 11,2025'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/bf55b19b-b5f4-4f9e-bd83-2e77b78cefaf',
    title: '[TP.HCM] Những Thành Phố Mơ Màng Year End 2025',
    price: 'Từ 900.000đ',
    date: '31 tháng 12,2025'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/6f27e258-ad17-45ac-9239-84e283eed935',
    title: 'ANH TRAI "SAY HI" 2025 CONCERT',
    price: 'Từ 1.040.000đ',
    date: '25 tháng 11,2025'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/5522f5d7-47dd-4adb-a8f6-063f0f6761e4',
    title: '[CAT&MOUSE] CA SĨ VICKY NHUNG + CA SĨ PHAN DUY ANH',
    price: 'Từ 375.000đ',
    date: '25 Tháng 11,2025'
  },
])

// Art Events data (reusing music events for demo)
const artEvents = ref<SuggestEvent[]>([...musicEvents.value])

// Other Events data (reusing music events for demo)
const otherEvents = ref<SuggestEvent[]>([...musicEvents.value])

// Nice Places data
const nicePlaces = ref<NicePlace[]>([
  { 
    image: 'https://www.figma.com/api/mcp/asset/c743865a-1eea-4e6f-b9db-0640c3f671f2',
    name: 'TP. Hồ Chí Minh'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/2cb7a085-5375-4348-a303-1e94a75c1981',
    name: 'Hà Nội'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/d640209c-090b-4d60-a8bf-d062db24c2ec',
    name: 'Đà Lạt'
  },
  { 
    image: 'https://www.figma.com/api/mcp/asset/d4872140-271d-44f5-9784-e989b74c0231',
    name: 'Vị trí khác'
  },
])

// Carousel states
const currentNewEvent = ref(0)
const currentSpecialEvent = ref(0)
const currentTrendyEvent = ref(0)
const currentSuggestEvent = ref(0)

// Navigation functions
const nextNewEvent = () => {
  currentNewEvent.value = (currentNewEvent.value + 2) % newEvents.value.length
}

const prevNewEvent = () => {
  currentNewEvent.value = currentNewEvent.value - 2 < 0 
    ? newEvents.value.length - 2 
    : currentNewEvent.value - 2
}

const nextSpecialEvent = () => {
  currentSpecialEvent.value = (currentSpecialEvent.value + 1) % specialEvents.value.length
}

const prevSpecialEvent = () => {
  currentSpecialEvent.value = currentSpecialEvent.value - 1 < 0 
    ? specialEvents.value.length - 1 
    : currentSpecialEvent.value - 1
}

const nextTrendyEvent = () => {
  currentTrendyEvent.value = (currentTrendyEvent.value + 1) % trendyEvents.value.length
}

const prevTrendyEvent = () => {
  currentTrendyEvent.value = currentTrendyEvent.value - 1 < 0 
    ? trendyEvents.value.length - 1 
    : currentTrendyEvent.value - 1
}

const nextSuggestEvent = () => {
  currentSuggestEvent.value = (currentSuggestEvent.value + 1) % suggestEvents.value.length
}

const prevSuggestEvent = () => {
  currentSuggestEvent.value = currentSuggestEvent.value - 1 < 0 
    ? suggestEvents.value.length - 1 
    : currentSuggestEvent.value - 1
}

// Helper functions to get visible items
const getVisibleSpecialEvents = (): SpecialEvent[] => {
  const visible: SpecialEvent[] = []
  for (let i = 0; i < 4; i++) {
    const event = specialEvents.value[(currentSpecialEvent.value + i) % specialEvents.value.length]
    if (event) visible.push(event)
  }
  return visible
}

const getVisibleTrendyEvents = (): TrendyEvent[] => {
  const visible: TrendyEvent[] = []
  for (let i = 0; i < 3; i++) {
    const event = trendyEvents.value[(currentTrendyEvent.value + i) % trendyEvents.value.length]
    if (event) visible.push(event)
  }
  return visible
}

const getVisibleSuggestEvents = (): SuggestEvent[] => {
  const visible: SuggestEvent[] = []
  for (let i = 0; i < 4; i++) {
    const event = suggestEvents.value[(currentSuggestEvent.value + i) % suggestEvents.value.length]
    if (event) visible.push(event)
  }
  return visible
}
</script>

<style scoped>
.homepage {
  min-height: 100vh;
  background-color: #111111;
}

.section-title {
  font-size: 1.5rem;
}

/* Event Cards */
.event-card-large {
  width: 100%;
  aspect-ratio: 807 / 460;
  position: relative;
}

.event-card-large img {
  display: block;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.event-card {
  height: 483px;
}

/* Trendy Event Section */
.trendy-event-wrapper {
  display: flex;
  align-items: flex-start;
  gap: 0;
}

.trendy-badge {
  width: 53px;
  height: 75px;
  flex-shrink: 0;
  display: flex;
  align-items: flex-start;
  justify-content: center;
}

.trendy-badge :deep(svg) {
  width: 53px;
  height: 75px;
  display: block;
}

.trendy-card {
  flex: 1;
  aspect-ratio: 16 / 9;
  position: relative;
}

.suggest-card-image {
  aspect-ratio: 16 / 9;
  width: 100%;
  height: auto;
}

.suggest-card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.suggest-card-title {
  font-size: 1.25rem;
  line-height: 1.3;
  min-height: 2.6em;
}

.suggest-card-price {
  font-size: 1rem;
}

.suggest-card-date {
  font-size: 1rem;
}

.place-card {
  height: 470px;
  position: relative;
}

.place-overlay {
  background: linear-gradient(to top, rgba(0,0,0,0.6), transparent);
}

/* Carousel Controls */
.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 70px;
  border-radius: 10px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  font-size: 1.5rem;
  font-weight: bold;
  cursor: pointer;
  z-index: 10;
  transition: background-color 0.3s;
}

.carousel-btn:hover {
  background-color: rgba(0, 0, 0, 0.7);
}

.carousel-btn-prev {
  left: 0;
}

.carousel-btn-next {
  right: 0;
}

/* Carousel Dots */
.carousel-dots {
  position: absolute;
  bottom: -30px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 10;
}

.carousel-dots span {
  width: 15px;
  height: 15px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.3);
  cursor: pointer;
  transition: background-color 0.3s;
}

.carousel-dots span.active {
  background-color: rgba(255, 255, 255, 0.8);
}

.carousel-dots span:hover {
  background-color: rgba(255, 255, 255, 0.6);
}
</style>
