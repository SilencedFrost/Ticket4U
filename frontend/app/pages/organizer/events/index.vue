<template>
  <div class="p-4 p-md-5">

    <!-- Header -->
    <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
      <div>
        <h2 class="fw-bold text-reactive-primary mb-1">{{ $t('organizer.events.title') }}</h2>
        <p class="text-reactive-secondary mb-0">{{ $t('organizer.events.subtitle') }}</p>
      </div>
      <NuxtLink v-if="canCreate" to="/organizer/events/new" class="btn btn-primary px-4">
        <i class="bi bi-plus-lg me-2"/>{{ $t('organizer.events.create') }}
      </NuxtLink>
    </div>

    <!-- Filters -->
    <div class="card bg-reactive-secondary border-0 p-3 mb-4">
      <div class="row g-3 align-items-end">
        <div class="col-md-5">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.events.search') }}</label>
          <div class="input-group">
            <span class="input-group-text bg-reactive-primary border-0">
              <i class="bi bi-search text-reactive-secondary"/>
            </span>
            <input
              v-model="searchQuery"
              type="text"
              class="form-control bg-reactive-primary border-0 text-reactive-primary"
              :placeholder="$t('organizer.events.search_placeholder')"
            />
          </div>
        </div>
        <div class="col-md-3">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.events.filter_status') }}</label>
          <select v-model="statusFilter" class="form-select bg-reactive-primary border-0 text-reactive-primary">
            <option value="">{{ $t('organizer.events.all_statuses') }}</option>
            <option v-for="s in statuses" :key="s.value" :value="s.value">{{ s.label }}</option>
          </select>
        </div>
        <div class="col-md-2">
          <button class="btn btn-outline-secondary w-100" @click="resetFilters">
            <i class="bi bi-x-circle me-1"/>{{ $t('organizer.events.reset') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-primary"/>
    </div>

    <!-- Error -->
    <div v-else-if="fetchError" class="alert alert-danger">
      <i class="bi bi-exclamation-triangle me-2"/>{{ fetchError }}
      <button class="btn btn-sm btn-outline-danger ms-3" @click="fetchEvents">
        {{ $t('common.action.retry') }}
      </button>
    </div>

    <!-- Empty state -->
    <div v-else-if="filteredEvents.length === 0" class="text-center py-5 text-reactive-secondary">
      <i class="bi bi-calendar-x fs-1 d-block mb-3"/>
      <p class="fw-semibold">{{ $t('organizer.events.empty') }}</p>
      <small>{{ $t('organizer.events.empty_sub') }}</small>
      <div v-if="canCreate" class="mt-4">
        <NuxtLink to="/organizer/events/new" class="btn btn-primary">
          <i class="bi bi-plus-lg me-2"/>{{ $t('organizer.events.create') }}
        </NuxtLink>
      </div>
    </div>

    <!-- Events Table -->
    <div v-else class="card bg-reactive-secondary border-0 overflow-hidden">
      <div class="table-responsive">
        <table class="table table-hover mb-0 organizer-table">
          <thead>
            <tr>
              <th class="text-reactive-secondary small fw-semibold ps-4">{{ $t('organizer.events.col.event') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.events.col.date') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.events.col.status') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.events.col.tickets') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.events.col.revenue') }}</th>
              <th class="text-reactive-secondary small fw-semibold text-end pe-4">{{ $t('organizer.events.col.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in filteredEvents" :key="event.id">

              <!-- Event info -->
              <td class="ps-4 py-3">
                <div class="d-flex align-items-center gap-3">
                  <img
                    v-if="event.bannerUrl"
                    :src="event.bannerUrl"
                    class="event-thumb rounded"
                    alt=""
                  />
                  <div v-else class="event-thumb rounded bg-reactive-primary d-flex align-items-center justify-content-center">
                    <i class="bi bi-image text-reactive-secondary"/>
                  </div>
                  <div>
                    <div class="fw-semibold text-reactive-primary">{{ event.name }}</div>
                    <small class="text-reactive-secondary">
                      <i class="bi bi-geo-alt me-1"/>{{ event.addressLine }}
                    </small>
                  </div>
                </div>
              </td>

              <!-- Date -->
              <td class="py-3">
                <template v-if="event.firstSessionStart">
                  <div class="text-reactive-primary small">{{ formatDate(event.firstSessionStart) }}</div>
                  <small class="text-reactive-secondary">{{ formatTime(event.firstSessionStart) }}</small>
                </template>
                <small v-else class="text-reactive-secondary">—</small>
              </td>

              <!-- Status -->
              <td class="py-3">
                <span class="badge rounded-pill px-3 py-2" :class="getStatusClass(event.status)">
                  {{ getStatusLabel(event.status) }}
                </span>
              </td>

              <!-- Tickets -->
              <td class="py-3">
                <div class="text-reactive-primary small fw-semibold">{{ event.ticketsSold ?? 0 }}</div>
                <small class="text-reactive-secondary">/ {{ event.totalCapacity ?? '—' }}</small>
              </td>

              <!-- Revenue -->
              <td class="py-3">
                <div class="text-reactive-primary small fw-semibold">
                  {{ formatPrice(event.revenue ?? 0) }}
                </div>
              </td>

              <!-- Actions -->
              <td class="py-3 pe-4 text-end">
                <div class="d-flex justify-content-end gap-2">
                  <NuxtLink
                    :to="`/organizer/events/${event.id}`"
                    class="btn btn-sm btn-outline-primary"
                    :title="$t('organizer.events.action.edit')"
                  >
                    <i class="bi bi-pencil"/>
                  </NuxtLink>
                  <NuxtLink
                    :to="`/event-detail/${event.id}`"
                    target="_blank"
                    class="btn btn-sm btn-outline-secondary"
                    :title="$t('organizer.events.action.preview')"
                  >
                    <i class="bi bi-eye"/>
                  </NuxtLink>
                  <button
                    v-if="canDelete"
                    class="btn btn-sm btn-outline-danger"
                    :title="$t('organizer.events.action.delete')"
                    @click="confirmDelete(event)"
                  >
                    <i class="bi bi-trash"/>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Delete Confirm Modal -->
    <div v-if="deleteTarget" class="modal-backdrop-custom" @click.self="deleteTarget = null">
      <div class="modal-box bg-reactive-secondary p-4 rounded-3 shadow-lg" style="max-width: 420px;">
        <h5 class="text-reactive-primary fw-bold mb-2">
          <i class="bi bi-exclamation-triangle text-danger me-2"/>
          {{ $t('organizer.events.delete.title') }}
        </h5>
        <p class="text-reactive-secondary mb-4">
          {{ $t('organizer.events.delete.confirm', { name: deleteTarget.name }) }}
        </p>
        <div v-if="deleteError" class="alert alert-danger py-2 mb-3">
          <i class="bi bi-exclamation-circle me-2"/>{{ deleteError }}
        </div>
        <div class="d-flex gap-2 justify-content-end">
          <button class="btn btn-outline-secondary" :disabled="deleting" @click="deleteTarget = null">
            {{ $t('common.action.cancel') }}
          </button>
          <button class="btn btn-danger" :disabled="deleting" @click="doDeleteEvent">
            <span v-if="deleting" class="spinner-border spinner-border-sm me-2"/>
            <i v-else class="bi bi-trash me-2"/>
            {{ $t('organizer.events.delete.confirm_btn') }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

definePageMeta({ layout: 'organizer', middleware: 'organizer' })

const { t: $t } = useI18n()
const userStore = useUserStore()
const config    = useRuntimeConfig()

const roleId    = userStore.user?.roleId ?? 0
const canCreate = roleId >= 1
const canDelete = roleId >= 2

interface OrgEvent {
  id: string
  name: string
  addressLine: string
  firstSessionStart?: string
  status: string
  bannerUrl?: string
  ticketsSold?: number
  totalCapacity?: number
  revenue?: number
}

// ── Data ───────────────────────────────────────────────────
const events     = ref<OrgEvent[]>([])
const loading    = ref(false)
const fetchError = ref('')

const fetchEvents = async () => {
  loading.value = true; fetchError.value = ''
  try {
    events.value = await $fetch<OrgEvent[]>(
      `${config.public.apiUrl}/organizer/events`,
      { credentials: 'include' }
    )
  } catch (err: any) {
    fetchError.value = err?.data?.message ?? 'Failed to load events'
  } finally {
    loading.value = false
  }
}

onMounted(fetchEvents)

// ── Filters ────────────────────────────────────────────────
const searchQuery  = ref('')
const statusFilter = ref('')

// Status values now uppercase to match new backend enum
const statuses = computed(() => [
  { value: 'EDITING',    label: $t('organizer.events.status.editing')   },
  { value: 'PREMIERE',   label: $t('organizer.events.status.premier')   },
  { value: 'SELLING',    label: $t('organizer.events.status.selling')   },
  { value: 'PAUSED',     label: $t('organizer.events.status.paused')    },
  { value: 'ONGOING',    label: $t('organizer.events.status.ongoing')   },
  { value: 'FINISHED',   label: $t('organizer.events.status.finished')  },
  { value: 'CANCELLED',  label: $t('organizer.events.status.cancelled') },
])

const filteredEvents = computed(() =>
  events.value.filter(e => {
    const matchSearch = !searchQuery.value ||
      e.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      e.addressLine.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchStatus = !statusFilter.value || e.status === statusFilter.value
    return matchSearch && matchStatus
  })
)

const resetFilters = () => { searchQuery.value = ''; statusFilter.value = '' }

// ── Delete ─────────────────────────────────────────────────
const deleteTarget = ref<OrgEvent | null>(null)
const deleting     = ref(false)
const deleteError  = ref('')

const confirmDelete = (event: OrgEvent) => { deleteTarget.value = event; deleteError.value = '' }

const doDeleteEvent = async () => {
  if (!deleteTarget.value) return
  deleting.value = true; deleteError.value = ''
  try {
    await $fetch(
      `${config.public.apiUrl}/organizer/events/${deleteTarget.value.id}`,
      { method: 'DELETE', credentials: 'include' }
    )
    events.value       = events.value.filter(e => e.id !== deleteTarget.value!.id)
    deleteTarget.value = null
  } catch (err: any) {
    deleteError.value = err?.data?.message ?? 'Failed to delete event'
  } finally {
    deleting.value = false
  }
}

// ── Helpers ────────────────────────────────────────────────
const formatDate = (d: string) =>
  new Date(d).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' })

const formatTime = (d: string) =>
  new Date(d).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })

const formatPrice = (p: number) =>
  p === 0 ? '—' : new Intl.NumberFormat('vi-VN').format(p) + ' ₫'

const getStatusClass = (status: string) => ({
  'bg-secondary bg-opacity-10 text-secondary': status === 'EDITING',
  'bg-info bg-opacity-10 text-info':           status === 'PREMIERE',
  'bg-primary bg-opacity-10 text-primary':     status === 'SELLING',
  'bg-warning bg-opacity-10 text-warning':     status === 'PAUSED',
  'bg-success bg-opacity-10 text-success':     status === 'ONGOING',
  'bg-dark bg-opacity-10 text-secondary':      status === 'FINISHED',
  'bg-danger bg-opacity-10 text-danger':       status === 'CANCELLED',
})

const getStatusLabel = (status: string) =>
  statuses.value.find(s => s.value === status)?.label ?? status
</script>

<style scoped>
.organizer-table { color: inherit; }
.organizer-table thead tr { border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.2); }
.organizer-table tbody tr {
  transition: background 0.15s;
  border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.1);
}
.organizer-table tbody tr:last-child { border-bottom: none; }
.organizer-table tbody tr:hover { background: rgba(var(--bs-primary-rgb), 0.04); }
.event-thumb { width: 52px; height: 36px; object-fit: cover; flex-shrink: 0; }
.modal-backdrop-custom {
  position: fixed; inset: 0; background: rgba(0,0,0,0.5);
  z-index: 1050; display: flex; align-items: center;
  justify-content: center; padding: 1rem;
}
</style>