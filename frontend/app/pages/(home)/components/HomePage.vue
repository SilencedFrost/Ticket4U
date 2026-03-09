<template>
    <div class="home-page bg-reactive-primary">
        <div class="container-xxl py-5">
            <!-- Loading state for featured -->
            <div v-if="loading.featured" class="text-center py-5">
                <div class="spinner-border text-light" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
            </div>

            <!-- Featured Carousel -->
            <FeaturedCarousel v-else-if="featuredEvents.length > 0" :events="featuredEvents" />

            <!-- Error state for featured -->
            <div v-else-if="errors.featured" class="alert alert-danger">
                {{ errors.featured }}
            </div>

            <section class="mb-5">
                <h2 class="text-reactive-primary fs-4 fw-bold mb-4">{{ $t('home_page.section.special') }}</h2>
                <div v-if="loading.special" class="text-center">
                    <div class="spinner-border text-light" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
                <EventCarousel
                    v-else-if="specialEvents.length > 0"
                    :items="specialEvents"
                    :items-per-page="4"
                    col-class="col-lg-3 col-md-6"
                    show-dots
                >
                    <template #default="{ item }">
                        <SpecialEventCard :event="item" />
                    </template>
                </EventCarousel>
                <div v-else-if="errors.special" class="alert alert-danger">{{ errors.special }}</div>
            </section>

            <section class="mb-5">
                <h2 class="text-reactive-primary fs-4 fw-bold mb-4">{{ $t('home_page.section.trending') }}</h2>
                <div v-if="loading.trending" class="text-center">
                    <div class="spinner-border text-light" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
                <EventCarousel
                    v-else-if="trendingEvents.length > 0"
                    :items="trendingEvents"
                    :items-per-page="3"
                    col-class="col-lg-4 col-md-6"
                    show-dots
                >
                    <template #default="{ item }">
                        <TrendingCard :event="item" />
                    </template>
                </EventCarousel>
                <div v-else-if="errors.trending" class="alert alert-danger">{{ errors.trending }}</div>
            </section>

            <EventSection
                v-if="!loading.suggested && suggestedEvents.length > 0"
                :title="$t('home_page.section.recommended')"
                :events="suggestedEvents"
            />

            <!-- Dynamic category sections -->
            <EventSection
                v-for="category in categories"
                :key="category.id"
                v-show="!loading.categories && category.events?.length > 0"
                :title="$t(`common.category.${category.id}`, category.name)"
                :events="category.events ?? []"
                :category-id="category.id"
                show-view-all
            />
        </div>
    </div>
</template>

<script setup lang="ts">
import FeaturedCarousel from './FeaturedCarousel.vue'
import EventCarousel from './EventCarousel.vue'
import SpecialEventCard from './SpecialEventCard.vue'
import TrendingCard from './TrendingCard.vue'
import EventSection from './EventSection.vue'

import { useHomeStore } from '~/stores/homeStore'

const homeStore = useHomeStore()

// Fetch data on mount
onMounted(() => {
    homeStore.fetchAllHomeData()
})

// Use store state directly (reactive)
const { featuredEvents, specialEvents, trendingEvents, suggestedEvents, categories, loading, errors } =
    storeToRefs(homeStore)
</script>

<style scoped>
.home-page {
    min-height: 100vh;
}
</style>
