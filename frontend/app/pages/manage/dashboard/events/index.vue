<script setup lang="ts">
import { ref, computed } from 'vue'
import {
  mockEvents,
  type Event,
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
const localePath = useLocalePath()

// Local reactive copy so deletes work without mutating the shared array
const events = ref<Event[]>([...mockEvents])

// ── Filters ────────────────────────────────────────────────
const searchQuery  = ref('')
const statusFilter = ref('')

const statusOptions = computed(() => [
  { value: 'EDITING',   label: $t('manage.events.status.editing')   },
  { value: 'SCHEDULED', label: $t('manage.events.status.premier')   },
  { value: 'PREMIERE',  label: $t('manage.events.status.premier')   },
  { value: 'SELLING',   label: $t('manage.events.status.selling')   },
  { value: 'PAUSED',    label: $t('manage.events.status.paused')    },
  { value: 'ONGOING',   label: $t('manage.events.status.ongoing')   },
  { value: 'FINISHED',  label: $t('manage.events.status.finished')  },
  { value: 'CANCELLED', label: $t('manage.events.status.cancelled') },
])

const filteredEvents = computed(() =>
    events.value.filter(e => {
      const q = searchQuery.value.toLowerCase()
      const matchSearch = !q || e.name.toLowerCase().includes(q) || e.addressLine.toLowerCase().includes(q)
      const matchStatus = !statusFilter.value || e.status === statusFilter.value
      return matchSearch && matchStatus
    })
)

const PAGE_SIZE   = 10
const currentPage = ref(1)

watch([searchQuery, statusFilter], () => { currentPage.value = 1 })

const totalPages  = computed(() => Math.ceil(filteredEvents.value.length / PAGE_SIZE))
const pagedEvents = computed(() =>
  filteredEvents.value.slice((currentPage.value - 1) * PAGE_SIZE, currentPage.value * PAGE_SIZE)
)

const resetFilters = () => { searchQuery.value = ''; statusFilter.value = '' }

// ── Delete (mock — local state only) ──────────────────────
const deleteTarget = ref<MockEvent | null>(null)
const confirmDelete = (event: MockEvent) => { deleteTarget.value = event }
const doDelete = () => {
  if (!deleteTarget.value) return
  events.value = events.value.filter(e => e.id !== deleteTarget.value!.id)
  deleteTarget.value = null
}
</script>

<style scoped>
.organizer-table {
  color: inherit;
}
.organizer-table thead tr {
  border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.2);
}
.organizer-table tbody tr {
  transition: background 0.15s;
  border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.1);
}
.organizer-table tbody tr:last-child > * {
  border-bottom-width: 0;
}
.organizer-table tbody tr:hover {
  background: rgba(var(--bs-primary-rgb), 0.04);
}
.event-thumb {
  width: 52px;
  height: 36px;
  object-fit: cover;
  flex-shrink: 0;
}
.modal-backdrop-custom {
  position: fixed; inset: 0;
  background: rgba(0,0,0,0.5);
  z-index: 1050; display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}
.modal-box {
  max-width: 420px;
  width: 100%;
}
</style>

<template>
  <div class="p-3 p-md-4 p-lg-5">

    <!-- Header -->
    <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
      <div>
        <h2 class="fw-bold text-reactive-primary mb-1">{{ $t('manage.events.title') }}</h2>
        <p class="text-reactive-secondary mb-0">{{ $t('manage.events.subtitle') }}</p>
      </div>
      <NuxtLink :to="localePath('/manage/event/new')" class="btn btn-primary px-4">
        <i class="bi bi-plus-lg me-2"/>{{ $t('manage.events.create') }}
      </NuxtLink>
    </div>

    <!-- Filters -->
    <div class="card shadow-sm p-3 mb-4">
      <div class="row g-3 align-items-end">
        <div class="col-md-5">
          <label class="form-label small text-reactive-secondary">{{ $t('manage.events.search') }}</label>
          <div class="input-group">
            <span class="input-group-text">
              <i class="bi bi-search text-reactive-secondary"/>
            </span>
            <input
                v-model="searchQuery"
                type="text"
                class="form-control"
                :placeholder="$t('manage.events.search_placeholder')"
            />
          </div>
        </div>
        <div class="col-md-3">
          <label class="form-label small text-reactive-secondary">{{ $t('manage.events.filter_status') }}</label>
          <select v-model="statusFilter" class="form-select">
            <option value="">{{ $t('manage.events.all_statuses') }}</option>
            <option v-for="s in statusOptions" :key="s.value" :value="s.value">{{ s.label }}</option>
          </select>
        </div>
        <div class="col-md-2">
          <button class="btn btn-outline-secondary w-100" @click="resetFilters">
            <i class="bi bi-x-circle me-1"/>{{ $t('manage.events.reset') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Empty -->
    <div v-if="filteredEvents.length === 0" class="text-center py-5 text-reactive-secondary">
      <i class="bi bi-calendar-x fs-1 d-block mb-3"/>
      <p class="fw-semibold">{{ $t('manage.events.empty') }}</p>
      <small>{{ $t('manage.events.empty_sub') }}</small>
      <div class="mt-4">
        <NuxtLink :to="localePath('/manage/event/new')" class="btn btn-primary">
          <i class="bi bi-plus-lg me-2"/>{{ $t('manage.events.create') }}
        </NuxtLink>
      </div>
    </div>

    <!-- Events Table -->
    <div v-else class="card shadow-sm overflow-hidden">
      <div class="table-responsive">
        <table class="table table-hover mb-0 organizer-table">
          <thead>
          <tr>
            <th class="text-reactive-secondary small fw-semibold ps-4">{{ $t('manage.events.col.event') }}</th>
            <th class="text-reactive-secondary small fw-semibold">{{ $t('manage.events.col.date') }}</th>
            <th class="text-reactive-secondary small fw-semibold">{{ $t('manage.events.col.status') }}</th>
            <th class="text-reactive-secondary small fw-semibold">{{ $t('manage.events.col.tickets') }}</th>
            <th class="text-reactive-secondary small fw-semibold">{{ $t('manage.events.col.revenue') }}</th>
            <th class="text-reactive-secondary small fw-semibold text-end pe-4">{{ $t('manage.events.col.actions') }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="event in pagedEvents" :key="event.id" class="event-row" style="cursor:pointer" @click="navigateTo(localePath(`/manage/dashboard/event/${event.id}`))">
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
            <td class="py-3">
              <div class="text-reactive-primary small fw-semibold">{{ formatPrice(getRevenue(event)) }}</div>
            </td>
            <td class="py-3 pe-4 text-end" @click.stop>
              <div class="d-flex justify-content-end gap-2">
                <NuxtLink :to="localePath(`/manage/event/${event.id}`)" class="btn btn-sm btn-outline-primary" :title="$t('manage.events.action.edit')">
                  <i class="bi bi-pencil"/>
                </NuxtLink>
                <NuxtLink :to="localePath(`/manage/event/${event.id}/preview`)" target="_blank" class="btn btn-sm btn-outline-secondary" :title="$t('manage.events.action.preview')">
                  <i class="bi bi-eye"/>
                </NuxtLink>
                <button class="btn btn-sm btn-outline-danger" :title="$t('manage.events.action.delete')" @click="confirmDelete(event)">
                  <i class="bi bi-trash"/>
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="d-flex align-items-center justify-content-between px-3 py-2 border-top">
        <small class="text-reactive-secondary">
          {{ $t('common.showing') }}
          {{ (currentPage - 1) * PAGE_SIZE + 1 }}–{{ Math.min(currentPage * PAGE_SIZE, filteredEvents.length) }}
          {{ $t('common.of') }} {{ filteredEvents.length }}
        </small>
        <ul class="pagination pagination-sm mb-0">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <button class="page-link" @click="currentPage--">&#8249;</button>
          </li>
          <li
            v-for="p in totalPages"
            :key="p"
            class="page-item"
            :class="{ active: p === currentPage }"
          >
            <button class="page-link" @click="currentPage = p">{{ p }}</button>
          </li>
          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <button class="page-link" @click="currentPage++">&#8250;</button>
          </li>
        </ul>
      </div>
    </div>

    <!-- Delete Confirm Modal -->
    <div v-if="deleteTarget" class="modal-backdrop-custom" @click.self="deleteTarget = null">
      <div class="modal-box card shadow p-4 rounded-3">
        <h5 class="text-reactive-primary fw-bold mb-2">
          <i class="bi bi-exclamation-triangle text-danger me-2"/>{{ $t('manage.events.delete.title') }}
        </h5>
        <p class="text-reactive-secondary mb-4">
          {{ $t('manage.events.delete.confirm', { name: deleteTarget.name }) }}
        </p>
        <div class="d-flex gap-2 justify-content-end">
          <button class="btn btn-outline-secondary" @click="deleteTarget = null">{{ $t('common.cancel') }}</button>
          <button class="btn btn-danger" @click="doDelete">
            <i class="bi bi-trash me-2"/>{{ $t('manage.events.delete.confirm_btn') }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

