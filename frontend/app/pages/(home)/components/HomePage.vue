<template>
  <div class="home-page bg-reactive-primary">
    <LocationPermissionPopup />

    <div class="container-xxl py-5">
      <FeaturedCarousel :events="featuredEvents" />

      <section class="mb-5">
        <h2 class="text-reactive-primary fs-4 fw-bold mb-4">Sự kiện đặc biệt</h2>
        <EventCarousel
          :items="specialEvents"
          :items-per-page="4"
          col-class="col-lg-3 col-md-6"
          show-dots
        >
          <template #default="{ item }">
            <SpecialEventCard :event="item" />
          </template>
        </EventCarousel>
      </section>

      <section class="mb-5">
        <h2 class="text-reactive-primary fs-4 fw-bold mb-4">Sự kiện xu hướng</h2>
        <EventCarousel
          :items="trendingEvents"
          :items-per-page="3"
          col-class="col-lg-4 col-md-6"
          show-dots
        >
          <template #default="{ item }">
            <TrendingCard :event="item" />
          </template>
        </EventCarousel>
      </section>

      <EventSection title="Dành cho bạn" :events="suggestedEvents" />

      <EventSection title="Nhạc sống" :events="musicEvents" show-view-all />

      <EventSection title="Sân khấu và nghệ thuật" :events="musicEvents" show-view-all />

      <EventSection title="Thể loại khác" :events="musicEvents" show-view-all />

      <section class="mb-5">
        <h2 class="text-white fs-4 fw-bold mb-4">Điểm đến thú vị</h2>
        <div class="row g-3">
          <div v-for="place in places" :key="place.id" class="col-lg-3 col-md-6">
            <PlaceCard :place="place" />
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import FeaturedCarousel from './FeaturedCarousel.vue';
import EventCarousel from './EventCarousel.vue';
import SpecialEventCard from './SpecialEventCard.vue';
import TrendingCard from './TrendingCard.vue';
import EventSection from './EventSection.vue';
import PlaceCard from './PlaceCard.vue';
import LocationPermissionPopup from './LocationPermissionPopup.vue';

import {
  featuredEvents,
  specialEvents,
  trendingEvents,
  suggestedEvents,
  musicEvents,
  places,
} from '../data/events';
</script>

<style scoped>
.home-page {
  min-height: 100vh;
}
</style>
