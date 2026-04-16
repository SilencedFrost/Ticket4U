<script setup lang="ts">
import { computed } from 'vue'
import {
  mockEvents,
  getAllZones,
  getTicketsSold,
  getTotalCapacity,
  getRevenue,
  getSessionStart,
  getStatusClass,
  getStatusI18nKey,
  formatPrice,
  formatDate,
  formatTime,
} from '../../mock.data'

const { t: $t } = useI18n()
const localePath  = useLocalePath()
const route       = useRoute()

const event = computed(() =>
  mockEvents.find(e => e.id === route.params.eventId)
)

const totalRevenue  = computed(() => event.value ? getRevenue(event.value)       : 0)
const totalSold     = computed(() => event.value ? getTicketsSold(event.value)   : 0)
const totalCapacity = computed(() => event.value ? getTotalCapacity(event.value) : 0)
const soldRate      = computed(() =>
  totalCapacity.value > 0 ? Math.round((totalSold.value / totalCapacity.value) * 100) : 0
)

const totalCheckedIn = computed(() => {
  if (!event.value) return 0
  if (!['ONGOING', 'FINISHED'].includes(event.value.status)) return 0
  return getAllZones(event.value).reduce((sum, z) => {
    const sold = z.quantitySold ?? 0
    const hash = z.id.split('').reduce((a, c) => a + c.charCodeAt(0), 0)
    const ratio = event.value!.status === 'FINISHED'
      ? 0.85 + (hash % 10) / 100
      : 0.40 + (hash % 30) / 100
    return sum + Math.floor(sold * ratio)
  }, 0)
})

const checkedInRate = computed(() =>
  totalSold.value > 0 ? Math.round((totalCheckedIn.value / totalSold.value) * 100) : 0
)

function sessionRevenue(session: typeof event.value.sessions[0]) {
  return session.zones.reduce((s, z) => s + (z.quantitySold ?? 0) * z.price, 0)
}

function sessionSold(session: typeof event.value.sessions[0]) {
  return session.zones.reduce((s, z) => s + (z.quantitySold ?? 0), 0)
}

function zoneFill(zone: { capacity: number; quantitySold?: number }) {
  return zone.capacity > 0 ? Math.round(((zone.quantitySold ?? 0) / zone.capacity) * 100) : 0
}

function fillColor(pct: number) {
  if (pct >= 80) return 'bg-success'
  if (pct >= 50) return 'bg-primary'
  if (pct >= 25) return 'bg-warning'
  return 'bg-danger'
}
</script>

<template>
  <div class="p-3 p-md-4 p-lg-5">

    <!-- Not found -->
    <div v-if="!event" class="text-center py-5 text-reactive-secondary">
      <i class="bi bi-calendar-x fs-1 d-block mb-3 opacity-25"/>
      <div class="fw-semibold">{{ $t('organizer.revenue.not_found') }}</div>
      <NuxtLink :to="localePath('/manage/dashboard/events')" class="btn btn-outline-secondary mt-4">
        <i class="bi bi-arrow-left me-2"/>{{ $t('organizer.revenue.back') }}
      </NuxtLink>
    </div>

    <template v-else>

      <!-- Header -->
      <div class="d-flex align-items-center gap-3 mb-4">
        <NuxtLink :to="localePath('/manage/dashboard/events')" class="btn btn-sm btn-outline-secondary flex-shrink-0">
          <i class="bi bi-arrow-left"/>
        </NuxtLink>
        <img :src="event.bannerUrl.wide" class="event-banner rounded flex-shrink-0" alt=""/>
        <div class="min-w-0">
          <div class="d-flex align-items-center gap-2 flex-wrap">
            <h2 class="fw-bold text-reactive-primary mb-0 text-truncate">{{ event.name }}</h2>
            <span class="badge rounded-pill px-3 py-2 flex-shrink-0" :class="getStatusClass(event.status)">
              {{ $t(getStatusI18nKey(event.status)) }}
            </span>
          </div>
          <small class="text-reactive-secondary">
            <i class="bi bi-geo-alt me-1"/>{{ event.addressLine }}
            <span class="mx-2">·</span>
            <i class="bi bi-calendar3 me-1"/>{{ formatDate(getSessionStart(event)) }}
            <span class="mx-1">{{ formatTime(getSessionStart(event)) }}</span>
          </small>
        </div>
      </div>

      <!-- Summary stats -->
      <div class="row g-3 mb-5">
        <div class="col-sm-6 col-xl">
          <div class="card shadow-sm p-4 h-100">
            <div class="d-flex align-items-center justify-content-between mb-3">
              <small class="text-reactive-secondary">{{ $t('organizer.revenue.stats.total_revenue') }}</small>
              <div class="stat-icon rounded-circle d-flex align-items-center justify-content-center" style="background:#22c55e22">
                <i class="bi bi-cash-stack" style="color:#22c55e"/>
              </div>
            </div>
            <div class="fw-bold text-reactive-primary" style="font-size:1.6rem;">{{ formatPrice(totalRevenue) }}</div>
          </div>
        </div>
        <div class="col-sm-6 col-xl">
          <div class="card shadow-sm p-4 h-100">
            <div class="d-flex align-items-center justify-content-between mb-3">
              <small class="text-reactive-secondary">{{ $t('organizer.revenue.stats.tickets_sold') }}</small>
              <div class="stat-icon rounded-circle d-flex align-items-center justify-content-center" style="background:#3b82f622">
                <i class="bi bi-ticket-perforated" style="color:#3b82f6"/>
              </div>
            </div>
            <div class="fw-bold text-reactive-primary" style="font-size:1.6rem;">{{ totalSold.toLocaleString('vi-VN') }}</div>
            <small class="text-reactive-secondary">/ {{ totalCapacity.toLocaleString('vi-VN') }}</small>
          </div>
        </div>
        <div class="col-sm-6 col-xl">
          <div class="card shadow-sm p-4 h-100">
            <div class="d-flex align-items-center justify-content-between mb-3">
              <small class="text-reactive-secondary">{{ $t('organizer.revenue.stats.total_capacity') }}</small>
              <div class="stat-icon rounded-circle d-flex align-items-center justify-content-center" style="background:#f59e0b22">
                <i class="bi bi-people" style="color:#f59e0b"/>
              </div>
            </div>
            <div class="fw-bold text-reactive-primary" style="font-size:1.6rem;">{{ totalCapacity.toLocaleString('vi-VN') }}</div>
          </div>
        </div>
        <div class="col-sm-6 col-xl">
          <div class="card shadow-sm p-4 h-100">
            <div class="d-flex align-items-center justify-content-between mb-3">
              <small class="text-reactive-secondary">{{ $t('organizer.revenue.stats.sold_rate') }}</small>
              <div class="stat-icon rounded-circle d-flex align-items-center justify-content-center" style="background:#ec489922">
                <i class="bi bi-pie-chart" style="color:#ec4899"/>
              </div>
            </div>
            <div class="fw-bold text-reactive-primary" style="font-size:1.6rem;">{{ soldRate }}%</div>
            <div class="progress mt-2" style="height:4px;">
              <div class="progress-bar" :class="fillColor(soldRate)" :style="{ width: soldRate + '%' }"/>
            </div>
          </div>
        </div>
        <div class="col-sm-6 col-xl">
          <div class="card shadow-sm p-4 h-100">
            <div class="d-flex align-items-center justify-content-between mb-3">
              <small class="text-reactive-secondary">{{ $t('organizer.revenue.stats.checked_in') }}</small>
              <div class="stat-icon rounded-circle d-flex align-items-center justify-content-center" style="background:#8b5cf622">
                <i class="bi bi-qr-code-scan" style="color:#8b5cf6"/>
              </div>
            </div>
            <div class="fw-bold text-reactive-primary" style="font-size:1.6rem;">
              {{ ['ONGOING','FINISHED'].includes(event.status) ? totalCheckedIn.toLocaleString('vi-VN') : '—' }}
            </div>
            <div v-if="['ONGOING','FINISHED'].includes(event.status)" class="progress mt-2" style="height:4px;">
              <div class="progress-bar bg-success" :style="{ width: checkedInRate + '%' }"/>
            </div>
            <small v-if="['ONGOING','FINISHED'].includes(event.status)" class="text-reactive-secondary mt-1">{{ checkedInRate }}% {{ $t('organizer.revenue.stats.tickets_sold').toLowerCase() }}</small>
          </div>
        </div>
      </div>

      <!-- Per-session breakdown -->
      <h5 class="text-reactive-primary fw-semibold mb-3">{{ $t('organizer.revenue.sessions_title') }}</h5>

      <div v-for="(session, idx) in event.sessions" :key="session.id" class="mb-4">
        <!-- Session header -->
        <div class="d-flex align-items-center gap-2 mb-2">
          <span class="badge bg-primary bg-opacity-10 text-primary rounded-pill px-3 py-2">
            {{ $t('organizer.revenue.session') }} {{ idx + 1 }} — {{ session.name }}
          </span>
          <small class="text-reactive-secondary">
            {{ formatDate(session.startDate) }} {{ formatTime(session.startDate) }}
            –
            {{ formatDate(session.endDate) }} {{ formatTime(session.endDate) }}
          </small>
          <span class="ms-auto text-reactive-secondary small">
            {{ $t('organizer.home.stats.revenue') }}:
            <span class="fw-semibold text-reactive-primary">{{ formatPrice(sessionRevenue(session)) }}</span>
            &nbsp;·&nbsp;
            {{ $t('organizer.revenue.stats.tickets_sold') }}:
            <span class="fw-semibold text-reactive-primary">{{ sessionSold(session).toLocaleString('vi-VN') }}</span>
          </span>
        </div>

        <!-- Zone table -->
        <div class="card shadow-sm overflow-hidden">
          <div v-if="session.zones.length === 0" class="text-center py-4 text-reactive-secondary small">
            {{ $t('organizer.revenue.no_zones') }}
          </div>
          <div v-else class="table-responsive">
            <table class="table table-hover mb-0 revenue-table">
              <thead>
                <tr>
                  <th class="text-reactive-secondary small fw-semibold ps-4">{{ $t('organizer.revenue.zone_table.zone') }}</th>
                  <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.revenue.zone_table.type') }}</th>
                  <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.revenue.zone_table.price') }}</th>
                  <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.revenue.zone_table.capacity') }}</th>
                  <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.revenue.zone_table.tickets') }}</th>
                  <th class="text-reactive-secondary small fw-semibold" style="min-width:120px">{{ $t('organizer.revenue.zone_table.sold') }}</th>
                  <th class="text-reactive-secondary small fw-semibold pe-4">{{ $t('organizer.revenue.zone_table.revenue') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="zone in session.zones" :key="zone.id">
                  <td class="ps-4 py-3">
                    <div class="fw-semibold text-reactive-primary">{{ zone.name }}</div>
                    <small v-if="zone.perks?.length" class="text-reactive-secondary">
                      <i class="bi bi-gift me-1"/>{{ zone.perks.slice(0, 2).join(' · ') }}{{ zone.perks.length > 2 ? ' …' : '' }}
                    </small>
                  </td>
                  <td class="py-3">
                    <span class="badge rounded-pill px-3 py-2"
                      :class="zone.isStanding ? 'bg-info bg-opacity-10 text-info' : 'bg-primary bg-opacity-10 text-primary'">
                      <i :class="zone.isStanding ? 'bi bi-person-standing me-1' : 'bi bi-grid me-1'"/>
                      {{ zone.isStanding ? $t('organizer.revenue.type.standing') : $t('organizer.revenue.type.seated') }}
                    </span>
                  </td>
                  <td class="py-3">
                    <span class="text-reactive-primary small fw-semibold">{{ formatPrice(zone.price) }}</span>
                  </td>
                  <td class="py-3">
                    <span class="text-reactive-primary small">{{ zone.capacity.toLocaleString('vi-VN') }}</span>
                  </td>
                  <td class="py-3">
                    <span class="text-reactive-primary small fw-semibold">{{ (zone.quantitySold ?? 0).toLocaleString('vi-VN') }}</span>
                  </td>
                  <td class="py-3">
                    <div class="d-flex align-items-center gap-2">
                      <div class="progress flex-grow-1" style="height:6px;">
                        <div class="progress-bar" :class="fillColor(zoneFill(zone))" :style="{ width: zoneFill(zone) + '%' }"/>
                      </div>
                      <small class="text-reactive-secondary flex-shrink-0" style="min-width:32px">{{ zoneFill(zone) }}%</small>
                    </div>
                  </td>
                  <td class="py-3 pe-4">
                    <span class="text-reactive-primary small fw-semibold">{{ formatPrice((zone.quantitySold ?? 0) * zone.price) }}</span>
                  </td>
                </tr>
              </tbody>
              <!-- Session total row -->
              <tfoot>
                <tr class="session-total">
                  <td colspan="3" class="ps-4 py-3 text-reactive-secondary small fw-semibold">Total</td>
                  <td class="py-3 text-reactive-primary small fw-semibold">{{ session.zones.reduce((s,z) => s + z.capacity, 0).toLocaleString('vi-VN') }}</td>
                  <td class="py-3 text-reactive-primary small fw-semibold">{{ sessionSold(session).toLocaleString('vi-VN') }}</td>
                  <td class="py-3"/>
                  <td class="py-3 pe-4 text-reactive-primary small fw-semibold">{{ formatPrice(sessionRevenue(session)) }}</td>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>
      </div>

    </template>
  </div>
</template>

<style scoped>
.event-banner { width: 72px; height: 48px; object-fit: cover; }
.stat-icon    { width: 40px; height: 40px; font-size: 1.1rem; }

.revenue-table { color: inherit; }
.revenue-table thead tr  { border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.2); }
.revenue-table tbody tr  { transition: background 0.15s; border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.1); }
.revenue-table tbody tr:last-child > * { border-bottom-width: 0; }
.revenue-table tbody tr:hover { background: rgba(var(--bs-primary-rgb), 0.04); }
.revenue-table tfoot .session-total { border-top: 2px solid rgba(var(--bs-secondary-rgb), 0.25); }
.revenue-table tfoot .session-total > * { border-bottom-width: 0; }
</style>
