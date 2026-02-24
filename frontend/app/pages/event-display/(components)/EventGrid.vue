<template>
    <div class="row g-3 g-md-4">
        <div v-for="event in events" :key="event.id" class="col-6 col-md-4 col-lg-3">
            <div class="event-card-wrapper cursor-pointer" role="button" tabindex="0" @click="$emit('event-click', event.id)" @keypress.enter="$emit('event-click', event.id)">
                <!-- Event Image -->
                <div class="event-card-img rounded-3 overflow-hidden mb-3">
                    <img :src="event.bannerUrl" :alt="event.name" class="w-100 h-100 object-fit-cover" loading="lazy" />
                </div>

                <!-- Event Info -->
                <div>
                    <h3 class="fw-bold mb-2 text-reactive-primary text-ellipsis-2 fs-5">
                        {{ event.name }}
                    </h3>
                    <p class="text-primary fw-bold mb-1">
                        {{ formatPrice(event.minPrice) }}
                    </p>
                    <p class="text-reactive-secondary mb-0 d-flex align-items-center gap-1 small">
                        <i class="bi bi-calendar-event"></i>
                        <span>{{ formatDate(event.startDate) }}</span>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- Empty State -->
    <div v-if="events.length === 0" class="text-center py-5">
        <p class="text-reactive-secondary mb-0">{{ $t('event_display.label.not_found') }}</p>
    </div>
</template>

<script setup lang="ts">
import type { Event } from '~/pages/(home)/types/home'

interface Props {
    events: Event[]
}

defineProps<Props>()

defineEmits<{
    'event-click': [id: string]
}>()

const formatPrice = (price: number): string => {
    return price ? `${$t('common.price_from')} ${price.toLocaleString('vi-VN')}đ` : $t('common.price_not_updated');
}

const formatDate = (isoDate: string): string => {
    const date = new Date(isoDate)
    const day = date.getDate()
    const month = date.getMonth() + 1
    const year = date.getFullYear()
    return `${day} tháng ${month}, ${year}`
}
</script>

<style scoped>
.event-card-wrapper {
    transition: transform 0.3s ease;
}

.event-card-wrapper:hover {
    transform: translateY(-4px);
}

.event-card-img {
    aspect-ratio: 16 / 9;
    position: relative;
}

.text-ellipsis-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.3;
    min-height: 52px;
}

.cursor-pointer {
    cursor: pointer;
}

@media (max-width: 991.98px) {
    .text-ellipsis-2 {
        font-size: 18px !important;
        min-height: 47px !important;
    }
}

@media (max-width: 767.98px) {
    .text-ellipsis-2 {
        font-size: 16px !important;
        min-height: 42px !important;
    }
}
</style>
