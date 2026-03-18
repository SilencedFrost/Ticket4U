<template>
  <div class="p-4 p-md-5">
    <div class="mb-5">
      <h2 class="fw-bold text-reactive-primary mb-1">
        {{ $t('organizer.home.greeting', { name: profile?.name ?? '...' }) }}
      </h2>
      <p class="text-reactive-secondary">{{ $t('organizer.home.subtitle') }}</p>
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
    <div class="mb-4">
      <h5 class="text-reactive-primary fw-semibold mb-3">{{ $t('organizer.home.quick_actions') }}</h5>
      <div class="d-flex flex-wrap gap-3">
        <NuxtLink :to="localePath('/organizer/events/new')" class="btn btn-primary px-4">
          <i class="bi bi-plus-lg me-2"/>{{ $t('organizer.events.create') }}
        </NuxtLink>
        <NuxtLink :to="localePath('/organizer/events')" class="btn btn-outline-secondary px-4">
          <i class="bi bi-calendar-event me-2"/>{{ $t('organizer.home.view_events') }}
        </NuxtLink>
        <NuxtLink :to="localePath('/organizer/reports')" class="btn btn-outline-secondary px-4">
          <i class="bi bi-bar-chart me-2"/>{{ $t('organizer.home.view_reports') }}
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

definePageMeta({ layout: 'organizer', middleware: 'organizer' })

const { t: $t } = useI18n()
const localePath = useLocalePath()
const config    = useRuntimeConfig()

// ── Reuse profile from layout — no extra API call ──────────
const { profile, fetchProfile } = useOrganizerProfile()
onMounted(fetchProfile) // no-op if already loaded by layout

// ── Events for stats ───────────────────────────────────────
interface OrgEvent {
  ticketsSold?: number
  totalCapacity?: number
  revenue?: number
  status: string
}

const events = ref<OrgEvent[]>([])

onMounted(async () => {
  try {
    events.value = await $fetch<OrgEvent[]>(
      `${config.public.apiUrl}/organizer/events`,
      { credentials: 'include' }
    )
  } catch {}
})

// ── Stats ──────────────────────────────────────────────────
const stats = computed(() => {
  const totalSold    = events.value.reduce((s, e) => s + (e.ticketsSold ?? 0), 0)
  const totalRevenue = events.value.reduce((s, e) => s + (e.revenue ?? 0), 0)
  const upcoming     = events.value.filter(e => ['PREMIERE', 'SELLING'].includes(e.status)).length

  return [
    { label: 'Total Events',  value: String(events.value.length),                                    sub: `${upcoming} upcoming`,    icon: 'bi-calendar-event',    color: '#3b82f6' },
    { label: 'Tickets Sold',  value: totalSold.toLocaleString('vi-VN'),                              sub: 'All time',                icon: 'bi-ticket-perforated', color: '#22c55e' },
    { label: 'Total Revenue', value: formatPrice(totalRevenue),                                       sub: 'All time',                icon: 'bi-cash-stack',        color: '#f59e0b' },
    { label: 'Events Active', value: String(events.value.filter(e => e.status === 'SELLING').length), sub: 'Currently selling',       icon: 'bi-star-fill',         color: '#ec4899' },
  ]
})

const formatPrice = (p: number) =>
  p === 0 ? '₫0' : new Intl.NumberFormat('vi-VN').format(p) + ' ₫'
</script>

<style scoped>
.stat-icon { width: 40px; height: 40px; font-size: 1.1rem; }
</style>