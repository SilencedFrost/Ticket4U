<template>
  <div class="p-4 p-md-5">
    <div class="mb-5">
      <h2 class="fw-bold text-reactive-primary mb-1">
        Xin chào, {{ mockProfile.name }} 👋
      </h2>
      <p class="text-reactive-secondary">Chào mừng trở lại trang quản lý sự kiện của bạn.</p>
    </div>

    <!-- Quick stats -->
    <div class="row g-4 mb-5">
      <div v-for="stat in stats" :key="stat.label" class="col-sm-6 col-xl-3">
        <div class="card bg-reactive-secondary p-4 h-100 border-0">
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
      <h5 class="text-reactive-primary fw-semibold mb-3">Thao tác nhanh</h5>
      <div class="d-flex flex-wrap gap-3">
        <NuxtLink to="/organizer/events/new" class="btn btn-primary px-4">
          <i class="bi bi-plus-lg me-2"/>Tạo sự kiện
        </NuxtLink>
        <NuxtLink to="/organizer/events" class="btn btn-outline-secondary px-4">
          <i class="bi bi-calendar-event me-2"/>Xem sự kiện
        </NuxtLink>
        <NuxtLink to="/organizer/reports" class="btn btn-outline-secondary px-4">
          <i class="bi bi-bar-chart me-2"/>Báo cáo
        </NuxtLink>
      </div>
    </div>

    <!-- Recent events -->
    <div>
      <h5 class="text-reactive-primary fw-semibold mb-3">Sự kiện gần đây</h5>
      <div class="card bg-reactive-secondary border-0 overflow-hidden">
        <div class="table-responsive">
          <table class="table table-hover mb-0 organizer-table">
            <thead>
              <tr>
                <th class="text-reactive-secondary small fw-semibold ps-4">Sự kiện</th>
                <th class="text-reactive-secondary small fw-semibold">Ngày</th>
                <th class="text-reactive-secondary small fw-semibold">Trạng thái</th>
                <th class="text-reactive-secondary small fw-semibold">Vé bán</th>
                <th class="text-reactive-secondary small fw-semibold pe-4">Doanh thu</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="event in recentEvents" :key="event.id">
                <td class="ps-4 py-3">
                  <div class="d-flex align-items-center gap-3">
                    <img :src="event.bannerUrl" class="event-thumb rounded" alt=""/>
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
                    {{ getStatusLabel(event.status) }}
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
  mockProfile,
  mockEvents,
  getTicketsSold,
  getTotalCapacity,
  getRevenue,
  getSessionStart,
  getStatusClass,
  getStatusLabel,
  formatPrice,
  formatDate,
  formatTime,
} from './mock/organizer.data'

definePageMeta({ layout: 'organizer', middleware: 'organizer' })

const recentEvents = mockEvents.slice(0, 5)

const stats = computed(() => {
  const totalSold    = mockEvents.reduce((s, e) => s + getTicketsSold(e), 0)
  const totalRevenue = mockEvents.reduce((s, e) => s + getRevenue(e), 0)
  const upcoming     = mockEvents.filter(e => ['PREMIERE', 'SELLING'].includes(e.status)).length
  const active       = mockEvents.filter(e => e.status === 'SELLING').length
  return [
    { label: 'Tổng sự kiện',  value: String(mockEvents.length),         sub: `${upcoming} sắp diễn ra`,  icon: 'bi-calendar-event',    color: '#3b82f6' },
    { label: 'Vé đã bán',     value: totalSold.toLocaleString('vi-VN'), sub: 'Tất cả thời gian',         icon: 'bi-ticket-perforated', color: '#22c55e' },
    { label: 'Doanh thu',     value: formatPrice(totalRevenue),          sub: 'Tất cả thời gian',         icon: 'bi-cash-stack',        color: '#f59e0b' },
    { label: 'Đang bán',      value: String(active),                    sub: 'Đang mở bán',              icon: 'bi-star-fill',         color: '#ec4899' },
  ]
})
</script>

<style scoped>
.stat-icon { width: 40px; height: 40px; font-size: 1.1rem; }
.organizer-table { color: inherit; }
.organizer-table thead tr { border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.2); }
.organizer-table tbody tr { transition: background 0.15s; border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.1); }
.organizer-table tbody tr:last-child { border-bottom: none; }
.organizer-table tbody tr:hover { background: rgba(var(--bs-primary-rgb), 0.04); }
.event-thumb { width: 52px; height: 36px; object-fit: cover; flex-shrink: 0; }
</style>