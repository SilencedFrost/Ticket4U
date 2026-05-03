<script setup lang="ts">
import type { PurchasedTicket } from '../(types)/purchasedTicket'
import ShimmerImg from '~/components/ShimmerImg.vue'

defineProps<{ ticket: PurchasedTicket }>()

const formatter = useFormatter()

const { locale } = useI18n()

function formatDate(dateStr: string): string {
  const date = new Date(dateStr)
  return date.toLocaleDateString(locale.value === 'vi' ? 'vi-VN' : 'en-GB', {
    weekday: 'short',
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  })
}

function formatTime(start: string, end: string): string {
  return `${start} – ${end}`
}

const statusConfig = {
  SOLD: { label: 'Upcoming', class: 'text-bg-success' },
  USED: { label: 'Attended', class: 'text-bg-secondary' },
  CANCELLED: { label: 'Cancelled', class: 'text-bg-danger' },
} as const
</script>

<template>
  <div class="ticket-card bg-reactive-primary rounded-3 overflow-hidden shadow-sm">
    <!-- Image -->
    <div class="ticket-img-wrapper flex-shrink-0">
      <div class="ratio ratio-1x1 h-100">
        <shimmer-img :src="ticket.eventBannerUrl.square" :alt="ticket.eventName" />
      </div>
    </div>

    <!-- Details -->
    <div class="ticket-details d-flex flex-column justify-content-between p-3 flex-grow-1 min-w-0">
      <div>
        <!-- Status badge + name -->
        <div class="d-flex align-items-start justify-content-between gap-2 mb-1">
          <span class="fw-semibold text-reactive-primary ticket-name">{{ ticket.eventName }}</span>
          <span :class="['badge', 'flex-shrink-0', statusConfig[ticket.status].class]">
            {{ statusConfig[ticket.status].label }}
          </span>
        </div>

        <!-- Date & time -->
        <div class="d-flex align-items-center text-reactive-secondary small mb-1">
          <i class="bi bi-calendar3 me-2 flex-shrink-0" />
          <span>{{ formatDate(ticket.eventDate) }}, {{ formatTime(ticket.eventStartTime, ticket.eventEndTime) }}</span>
        </div>

        <!-- Venue -->
        <div class="d-flex align-items-start text-reactive-secondary small mb-2">
          <i class="bi bi-geo-alt me-2 flex-shrink-0 mt-1" />
          <span class="text-truncate-2">{{ ticket.eventVenue }}</span>
        </div>
      </div>

      <!-- Divider -->
      <hr class="my-2 text-reactive-secondary opacity-25" />

      <!-- Seat + price -->
      <div class="d-flex align-items-center justify-content-between flex-wrap gap-2">
        <div class="d-flex align-items-center gap-2 text-reactive-secondary small">
          <i class="bi bi-ticket-perforated" />
          <span class="fw-medium text-reactive-primary">{{ ticket.zoneName }}</span>
          <span class="text-reactive-secondary opacity-75">·</span>
          <span>{{ ticket.seatName }}</span>
        </div>
        <span class="fw-bold text-primary">{{ formatter.formatPrice(ticket.basePrice) }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ticket-card {
  display: flex;
  flex-direction: row;
  border: 1px solid color-mix(in srgb, currentColor 10%, transparent);
  transition: box-shadow 0.2s ease;
}

.ticket-card:hover {
  box-shadow: 0 4px 16px color-mix(in srgb, currentColor 12%, transparent) !important;
}

.ticket-img-wrapper {
  width: 120px;
  min-height: 120px;
}

.ticket-name {
  font-size: 0.95rem;
  line-height: 1.3;
}

.text-truncate-2 {
  display: -webkit-box;
  line-clamp: 2;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* Mobile: stack image on top */
@media (max-width: 575px) {
  .ticket-card {
    flex-direction: column;
  }

  .ticket-img-wrapper {
    width: 100%;
    height: 160px;
    min-height: unset;
  }

  .ticket-img-wrapper .ratio {
    height: 160px;
  }
}
</style>
