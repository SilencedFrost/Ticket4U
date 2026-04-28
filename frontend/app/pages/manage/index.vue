<template>
  <div class="p-3 p-md-4 p-lg-5">
    <div class="mb-5">
      <h2 class="fw-bold text-reactive-primary mb-1">
        {{ $t('manage.home.greeting', { name: userStore.user.username }) }} 👋
      </h2>
      <p class="text-reactive-secondary">{{ $t('manage.home.subtitle') }}</p>
    </div>

    <!-- Quick stats -->
    <div class="row g-4 mb-5">
      <div v-for="stat in stats" :key="stat.label" class="col-sm-6 col-xl-3">
        <div class="card shadow-sm p-4 h-100">
          <div class="d-flex align-items-center justify-content-between mb-3">
            <span class="text-reactive-secondary small">{{ stat.label }}</span>
            <div class="stat-icon rounded-circle d-flex align-items-center justify-content-center" :style="{ background: stat.color + '22' }">
              <i :class="['bi', stat.icon]" :style="{ color: stat.color }"/>
            </div>
          </div>
          <div class="fw-bold text-reactive-primary" style="font-size: 1.75rem;">{{ stat.value }}</div>
          <small class="text-reactive-secondary mt-1">{{ stat.sub }}</small>
        </div>
      </div>
    </div>

    <!-- Quick actions -->
    <div class="mb-5">
      <h5 class="text-reactive-primary fw-semibold mb-3">{{ $t('manage.home.quick_actions') }}</h5>
      <div class="d-flex flex-wrap gap-3">
        <NuxtLink :to="localePath('/manage/event/new')" class="btn btn-primary px-4">
          <i class="bi bi-plus-lg me-2"/>{{ $t('manage.events.create') }}
        </NuxtLink>
        <NuxtLink :to="localePath('/manage/dashboard/events')" class="btn btn-outline-secondary px-4">
          <i class="bi bi-calendar-event me-2"/>{{ $t('manage.home.view_events') }}
        </NuxtLink>
      </div>
    </div>

    <!-- Recent events -->
    <div>
      <h5 class="text-reactive-primary fw-semibold mb-3">{{ $t('manage.events.title') }}</h5>
      <div class="card shadow-sm overflow-hidden">
        <div class="table-responsive">
          <table class="table table-hover mb-0 manage-table">
            <thead>
            <tr>
              <th class="text-reactive-secondary small fw-semibold ps-4">{{ $t('manage.events.col.event') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('manage.events.col.date') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('manage.events.col.status') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('manage.events.col.tickets') }}</th>
              <th class="text-reactive-secondary small fw-semibold pe-4">{{ $t('manage.events.col.revenue') }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="event in recentEvents" :key="event.id" class="cursor-pointer" @click="$router.push(localePath(`/manage/dashboard/event/${event.id}`))">
              <td class="ps-4 py-3">
                <div class="d-flex align-items-center gap-3">
                  <img :src="event.bannerUrl.wide" class="event-thumb rounded" alt=""/>
                  <div>
                    <div class="fw-semibold text-reactive-primary">{{ event.name }}</div>
                    <small class="text-reactive-secondary">
                      <i class="bi bi-geo-alt me-1"/>{{ event.addressLine }}
                    </small>
                  </div>
                </div>
              </td>
              <td class="py-3">
                <div class="text-reactive-primary small">{{ formatDate(getSessionStart(event)) }}</div>
                <small class="text-reactive-secondary">{{ formatTime(getSessionStart(event)) }}</small>
              </td>
              <td class="py-3">
                  <span class="badge rounded-pill px-3 py-2" :class="getStatusClass(event.status)">
                    {{ $t(getStatusI18nKey(event.status)) }}
                  </span>
              </td>
              <td class="py-3">
                <div class="text-reactive-primary small fw-semibold">{{ getTicketsSold(event) }}</div>
                <small class="text-reactive-secondary">/ {{ getTotalCapacity(event) }}</small>
              </td>
              <td class="py-3 pe-4">
                <div class="text-reactive-primary small fw-semibold">{{ formatPrice(getRevenue(event)) }}</div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  mockEvents,
  getTicketsSold,
  getTotalCapacity,
  getRevenue,
  getSessionStart,
  getStatusClass,
  getStatusI18nKey,
  formatPrice,
  formatDate,
  formatTime,
} from './mock.data'

const { t: $t } = useI18n()
const localePath = useLocalePath()
const userStore = useUserStore()

const recentEvents = mockEvents.slice(0, 5)

const stats = computed(() => {
  const totalSold    = mockEvents.reduce((s, e) => s + getTicketsSold(e), 0)
  const totalRevenue = mockEvents.reduce((s, e) => s + getRevenue(e), 0)
  const upcoming     = mockEvents.filter(e => ['PREMIERE', 'SELLING'].includes(e.status)).length
  const active       = mockEvents.filter(e => e.status === 'SELLING').length
  return [
    { label: $t('manage.home.stats.total_events'), value: String(mockEvents.length),         sub: $t('manage.home.stats.upcoming', { n: upcoming }), icon: 'bi-calendar-event',    color: '#3b82f6' },
    { label: $t('manage.home.stats.tickets_sold'), value: totalSold.toLocaleString('vi-VN'), sub: $t('manage.home.stats.this_month'),                icon: 'bi-ticket-perforated', color: '#22c55e' },
    { label: $t('manage.home.stats.revenue'),      value: formatPrice(totalRevenue),          sub: $t('manage.home.stats.this_month'),                icon: 'bi-cash-stack',        color: '#f59e0b' },
    { label: $t('manage.home.stats.active_events'),  value: String(active),                    sub: $t('manage.home.stats.this_month'),                icon: 'bi-star-fill',         color: '#ec4899' },
  ]
})
</script>

<style scoped>
.stat-icon {
  width: 40px; height: 40px; font-size: 1.1rem;
}
.manage-table { --bs-table-bg: transparent; color: inherit; }
.manage-table thead tr {
  border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.2);
}
.manage-table tbody tr {
  transition: background 0.15s; border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.1);
  cursor: pointer;
}
.manage-table tbody tr:last-child > * {
  border-bottom-width: 0;
}
.manage-table tbody tr:hover {
  background: rgba(var(--bs-primary-rgb), 0.04);
}
.event-thumb {
  width: 52px; height: 36px; object-fit: cover; flex-shrink: 0;
}
</style>