<template>
  <div class="p-4 p-md-5">

    <!-- Header -->
    <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
      <div>
        <h2 class="fw-bold text-reactive-primary mb-1">Sự kiện của tôi</h2>
        <p class="text-reactive-secondary mb-0">Quản lý và theo dõi toàn bộ sự kiện của bạn.</p>
      </div>
      <NuxtLink to="/organizer/events/new" class="btn btn-primary px-4">
        <i class="bi bi-plus-lg me-2"/>Tạo sự kiện
      </NuxtLink>
    </div>

    <!-- Filters -->
    <div class="card bg-reactive-secondary border-0 p-3 mb-4">
      <div class="row g-3 align-items-end">
        <div class="col-md-5">
          <label class="form-label small text-reactive-secondary">Tìm kiếm</label>
          <div class="input-group">
            <span class="input-group-text bg-reactive-primary border-0">
              <i class="bi bi-search text-reactive-secondary"/>
            </span>
            <input
              v-model="searchQuery"
              type="text"
              class="form-control bg-reactive-primary border-0 text-reactive-primary"
              placeholder="Tên sự kiện, địa điểm..."
            />
          </div>
        </div>
        <div class="col-md-3">
          <label class="form-label small text-reactive-secondary">Trạng thái</label>
          <select v-model="statusFilter" class="form-select bg-reactive-primary border-0 text-reactive-primary">
            <option value="">Tất cả trạng thái</option>
            <option v-for="s in statusOptions" :key="s.value" :value="s.value">{{ s.label }}</option>
          </select>
        </div>
        <div class="col-md-2">
          <button class="btn btn-outline-secondary w-100" @click="resetFilters">
            <i class="bi bi-x-circle me-1"/>Xóa lọc
          </button>
        </div>
      </div>
    </div>

    <!-- Empty -->
    <div v-if="filteredEvents.length === 0" class="text-center py-5 text-reactive-secondary">
      <i class="bi bi-calendar-x fs-1 d-block mb-3"/>
      <p class="fw-semibold">Không tìm thấy sự kiện nào</p>
      <small>Thử thay đổi bộ lọc hoặc tạo sự kiện mới.</small>
    </div>

    <!-- Events Table -->
    <div v-else class="card bg-reactive-secondary border-0 overflow-hidden">
      <div class="table-responsive">
        <table class="table table-hover mb-0 organizer-table">
          <thead>
            <tr>
              <th class="text-reactive-secondary small fw-semibold ps-4">Sự kiện</th>
              <th class="text-reactive-secondary small fw-semibold">Ngày</th>
              <th class="text-reactive-secondary small fw-semibold">Trạng thái</th>
              <th class="text-reactive-secondary small fw-semibold">Vé</th>
              <th class="text-reactive-secondary small fw-semibold">Doanh thu</th>
              <th class="text-reactive-secondary small fw-semibold text-end pe-4">Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in filteredEvents" :key="event.id">
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
              <td class="py-3">
                <div class="text-reactive-primary small fw-semibold">{{ formatPrice(getRevenue(event)) }}</div>
              </td>
              <td class="py-3 pe-4 text-end">
                <div class="d-flex justify-content-end gap-2">
                  <NuxtLink :to="`/organizer/events/${event.id}`" class="btn btn-sm btn-outline-primary" title="Chỉnh sửa">
                    <i class="bi bi-pencil"/>
                  </NuxtLink>
                  <NuxtLink :to="`/event-detail/${event.id}`" target="_blank" class="btn btn-sm btn-outline-secondary" title="Xem trước">
                    <i class="bi bi-eye"/>
                  </NuxtLink>
                  <button class="btn btn-sm btn-outline-danger" title="Xóa" @click="confirmDelete(event)">
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
      <div class="modal-box bg-reactive-secondary p-4 rounded-3 shadow-lg">
        <h5 class="text-reactive-primary fw-bold mb-2">
          <i class="bi bi-exclamation-triangle text-danger me-2"/>Xóa sự kiện
        </h5>
        <p class="text-reactive-secondary mb-4">
          Bạn có chắc muốn xóa <strong class="text-reactive-primary">{{ deleteTarget.name }}</strong>? Hành động này không thể hoàn tác.
        </p>
        <div class="d-flex gap-2 justify-content-end">
          <button class="btn btn-outline-secondary" @click="deleteTarget = null">Hủy</button>
          <button class="btn btn-danger" @click="doDelete">
            <i class="bi bi-trash me-2"/>Xóa
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import {
  mockEvents,
  type MockEvent,
  getTicketsSold,
  getTotalCapacity,
  getRevenue,
  getSessionStart,
  getStatusClass,
  getStatusLabel,
  formatPrice,
  formatDate,
  formatTime,
} from '~/mock/organizer.data'

definePageMeta({ layout: 'organizer', middleware: 'organizer' })

// Local reactive copy so deletes work without mutating the shared array
const events = ref<MockEvent[]>([...mockEvents])

// ── Filters ────────────────────────────────────────────────
const searchQuery  = ref('')
const statusFilter = ref('')

const statusOptions = [
  { value: 'EDITING',   label: 'Nháp' },
  { value: 'PREMIERE',  label: 'Sắp mở bán' },
  { value: 'SELLING',   label: 'Đang bán' },
  { value: 'PAUSED',    label: 'Tạm dừng' },
  { value: 'ONGOING',   label: 'Đang diễn' },
  { value: 'FINISHED',  label: 'Đã kết thúc' },
  { value: 'CANCELLED', label: 'Đã hủy' },
]

const filteredEvents = computed(() =>
  events.value.filter(e => {
    const q = searchQuery.value.toLowerCase()
    const matchSearch = !q || e.name.toLowerCase().includes(q) || e.addressLine.toLowerCase().includes(q)
    const matchStatus = !statusFilter.value || e.status === statusFilter.value
    return matchSearch && matchStatus
  })
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
.organizer-table { color: inherit; }
.organizer-table thead tr { border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.2); }
.organizer-table tbody tr { transition: background 0.15s; border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.1); }
.organizer-table tbody tr:last-child { border-bottom: none; }
.organizer-table tbody tr:hover { background: rgba(var(--bs-primary-rgb), 0.04); }
.event-thumb { width: 52px; height: 36px; object-fit: cover; flex-shrink: 0; }
.modal-backdrop-custom { position: fixed; inset: 0; background: rgba(0,0,0,0.5); z-index: 1050; display: flex; align-items: center; justify-content: center; padding: 1rem; }
.modal-box { max-width: 420px; width: 100%; }
</style>