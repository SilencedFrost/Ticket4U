<template>
  <div class="p-4 p-md-5">

    <!-- Header -->
    <div class="d-flex align-items-center gap-3 mb-4">
      <NuxtLink to="/organizer/events" class="btn btn-sm btn-outline-secondary">
        <i class="bi bi-arrow-left"/>
      </NuxtLink>
      <div>
        <h2 class="fw-bold text-reactive-primary mb-0">
          {{ isNew ? 'Tạo sự kiện mới' : 'Chỉnh sửa sự kiện' }}
        </h2>
        <small class="text-reactive-secondary" v-if="!isNew">ID: {{ eventId }}</small>
      </div>
      <div v-if="!isNew" class="ms-auto d-flex align-items-center gap-2">
        <span class="badge" :class="{
          'bg-secondary':         form.status === 'EDITING',
          'bg-primary':           form.status === 'PREMIERE',
          'bg-success':           form.status === 'SELLING',
          'bg-warning text-dark': form.status === 'PAUSED' || form.status === 'ONGOING',
          'bg-danger':            form.status === 'CANCELLED' || form.status === 'FINISHED',
        }">{{ getStatusLabel(form.status) }}</span>
        <Transition name="draft-toast">
          <span v-if="showDraftSaved" class="badge bg-success d-flex align-items-center gap-1">
            <i class="bi bi-check2"/>Đã lưu
          </span>
        </Transition>
      </div>
    </div>

    <!-- Step Indicators -->
    <div class="d-flex gap-2 mb-5 step-bar">
      <div
        v-for="(step, i) in steps" :key="i"
        class="step-item d-flex align-items-center gap-2 flex-grow-1"
        :class="{ completed: i < currentStep, active: i === currentStep }"
        @click="goToStep(i)"
      >
        <div class="step-dot d-flex align-items-center justify-content-center rounded-circle flex-shrink-0">
          <i v-if="i < currentStep" class="bi bi-check2"/>
          <span v-else>{{ i + 1 }}</span>
        </div>
        <span class="step-label d-none d-md-block small fw-semibold">{{ step }}</span>
        <div v-if="i < steps.length - 1" class="step-line flex-grow-1"/>
      </div>
    </div>

    <!-- ── STEP 1: Basic Info ── -->
    <div v-if="currentStep === 0">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-4">
          <i class="bi bi-info-circle me-2 text-primary"/>Thông tin cơ bản
        </h5>
        <div class="row g-4">
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary">Tên sự kiện <span class="text-danger">*</span></label>
            <input v-model="form.name" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.name }" placeholder="Nhập tên sự kiện..."/>
            <div class="invalid-feedback">{{ errors.name }}</div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">Danh mục <span class="text-danger">*</span></label>
            <select v-model="form.categoryId" class="form-select bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.categoryId }">
              <option value="">— Chọn danh mục —</option>
              <option v-for="cat in mockCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
            <div class="invalid-feedback">{{ errors.categoryId }}</div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">Trạng thái</label>
            <select v-model="form.status" class="form-select bg-reactive-primary border-0 text-reactive-primary">
              <option v-for="s in statusOptions" :key="s.value" :value="s.value">{{ s.label }}</option>
            </select>
          </div>
          <div class="col-12">
            <div class="alert alert-info py-2 small mb-0">
              <i class="bi bi-info-circle me-1"/>Ngày bắt đầu và kết thúc sẽ được sử dụng cho buổi diễn chính của sự kiện.
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">Ngày bắt đầu <span class="text-danger">*</span></label>
            <input v-model="form.startDate" type="datetime-local" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.startDate }"/>
            <div class="invalid-feedback">{{ errors.startDate }}</div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">Ngày kết thúc <span class="text-danger">*</span></label>
            <input v-model="form.endDate" type="datetime-local" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.endDate }"/>
            <div class="invalid-feedback">{{ errors.endDate }}</div>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary">Địa chỉ <span class="text-danger">*</span></label>
            <input v-model="form.addressLine" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.addressLine }" placeholder="Nhập địa chỉ tổ chức..."/>
            <div class="invalid-feedback">{{ errors.addressLine }}</div>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary">URL Banner</label>
            <input v-model="form.bannerUrl" type="url" class="form-control bg-reactive-primary border-0 text-reactive-primary" placeholder="https://..."/>
            <div v-if="form.bannerUrl" class="mt-2">
              <img :src="form.bannerUrl" class="rounded" style="max-height:160px;object-fit:cover;width:100%;"/>
            </div>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-end">
        <button class="btn btn-primary px-4" :disabled="saving" @click="saveStep1">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          Lưu & Tiếp theo <i class="bi bi-arrow-right ms-1"/>
        </button>
      </div>
    </div>

    <!-- ── STEP 2: Content ── -->
    <div v-if="currentStep === 1">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-4">
          <i class="bi bi-file-text me-2 text-primary"/>Nội dung sự kiện
        </h5>
        <div class="row g-4">
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">Giới thiệu sự kiện</label>
            <ul class="nav nav-tabs mb-3 border-0">
              <li class="nav-item"><button type="button" class="nav-link px-3" :class="{ active: contentLang === 'vi' }" @click="contentLang = 'vi'">🇻🇳 Tiếng Việt</button></li>
              <li class="nav-item"><button type="button" class="nav-link px-3" :class="{ active: contentLang === 'en' }" @click="contentLang = 'en'">🇺🇸 English</button></li>
            </ul>
            <div class="bg-reactive-primary rounded p-3">
              <textarea
                v-if="contentLang === 'vi'"
                v-model="content.aboutVi"
                class="form-control bg-transparent border-0 text-reactive-primary p-0"
                style="min-height:180px; resize:vertical;"
                placeholder="Nhập nội dung giới thiệu bằng tiếng Việt..."
              />
              <textarea
                v-else
                v-model="content.aboutEn"
                class="form-control bg-transparent border-0 text-reactive-primary p-0"
                style="min-height:180px; resize:vertical;"
                placeholder="Enter event description in English..."
              />
            </div>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">Điều khoản & Điều kiện</label>
            <textarea v-model="content.termsAndConditions" rows="4" class="form-control bg-reactive-primary border-0 text-reactive-primary" placeholder="Nhập điều khoản sử dụng..."/>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">Chính sách hoàn vé</label>
            <textarea v-model="content.policyRefund" rows="3" class="form-control bg-reactive-primary border-0 text-reactive-primary" placeholder="Nhập chính sách hoàn vé..."/>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-between gap-2">
        <button class="btn btn-outline-secondary px-4" @click="currentStep = 0"><i class="bi bi-arrow-left me-1"/>Quay lại</button>
        <button class="btn btn-primary px-4" :disabled="saving" @click="saveStep2">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          Lưu & Tiếp theo <i class="bi bi-arrow-right ms-1"/>
        </button>
      </div>
    </div>

    <!-- ── STEP 3: Zones ── -->
    <div v-if="currentStep === 2">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <div class="d-flex align-items-center justify-content-between mb-4">
          <h5 class="fw-semibold text-reactive-primary mb-0"><i class="bi bi-grid me-2 text-primary"/>Khu vực & Vé</h5>
          <button class="btn btn-sm btn-primary" @click="openZoneModal(null)"><i class="bi bi-plus-lg me-1"/>Thêm khu vực</button>
        </div>
        <div v-if="zones.length === 0" class="text-center py-5 text-reactive-secondary">
          <i class="bi bi-grid fs-1 d-block mb-3"/>
          <p>Chưa có khu vực nào. Hãy thêm khu vực đầu tiên.</p>
        </div>
        <div v-else class="row g-3">
          <div v-for="zone in zones" :key="zone.id" class="col-md-6 col-xl-4">
            <div class="zone-card card border-0 p-3 h-100">
              <div class="d-flex justify-content-between align-items-start mb-2">
                <div>
                  <div class="fw-semibold text-reactive-primary">{{ zone.name }}</div>
                  <small class="text-reactive-secondary">
                    <i :class="zone.isStanding ? 'bi-people' : 'bi-chair'" class="bi me-1"/>
                    {{ zone.isStanding ? 'Standing' : 'Seated' }}
                  </small>
                </div>
                <div class="d-flex gap-1">
                  <button class="btn btn-sm btn-outline-primary" @click="openZoneModal(zone)"><i class="bi bi-pencil"/></button>
                  <button class="btn btn-sm btn-outline-danger" @click="deleteZone(zone.id)"><i class="bi bi-trash"/></button>
                </div>
              </div>
              <div class="d-flex justify-content-between small text-reactive-secondary">
                <span><i class="bi bi-people me-1"/>{{ zone.capacity }}</span>
                <span class="fw-semibold text-reactive-primary">{{ formatPrice(zone.price) }}</span>
              </div>
              <div v-if="zone.purchaseLimit" class="small text-reactive-secondary mt-1">
                <i class="bi bi-ticket me-1"/>Tối đa {{ zone.purchaseLimit }} / người
              </div>
              <div v-if="zone.perks?.length" class="mt-2 d-flex flex-wrap gap-1">
                <span v-for="perk in zone.perks" :key="perk" class="badge small perk-badge">{{ perk }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-between gap-2">
        <button class="btn btn-outline-secondary px-4" @click="currentStep = 1"><i class="bi bi-arrow-left me-1"/>Quay lại</button>
        <button class="btn btn-primary px-4" @click="currentStep = 3">Lưu & Tiếp theo <i class="bi bi-arrow-right ms-1"/></button>
      </div>
    </div>

    <!-- ── STEP 4: Layout ── -->
    <div v-if="currentStep === 3">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-3"><i class="bi bi-layers me-2 text-primary"/>Sơ đồ chỗ ngồi</h5>
        <div class="alert alert-info py-2 small mb-4">
          <i class="bi bi-info-circle me-1"/>Bước này yêu cầu canvas editor — trong mock này hiển thị preview tĩnh.
        </div>

        <div class="btn-group mb-4">
          <button class="btn" :class="layoutMode === 'venue' ? 'btn-primary' : 'btn-outline-secondary'" @click="layoutMode = 'venue'">
            <i class="bi bi-building me-2"/>Dùng sơ đồ venue
          </button>
          <button class="btn" :class="layoutMode === 'custom' ? 'btn-primary' : 'btn-outline-secondary'" @click="layoutMode = 'custom'">
            <i class="bi bi-pencil-square me-2"/>Tự vẽ sơ đồ
          </button>
        </div>

        <div v-if="layoutMode === 'venue'">
          <div class="col-md-6 mb-3">
            <label class="form-label small fw-semibold text-reactive-secondary">Venue <span class="text-danger">*</span></label>
            <select v-model="form.venueId" class="form-select bg-reactive-primary border-0 text-reactive-primary">
              <option value="">— Chọn venue —</option>
              <option v-for="v in mockVenues" :key="v.id" :value="v.id">{{ v.name }} — {{ v.addressLine }}</option>
            </select>
          </div>
          <div v-if="selectedVenue" class="d-flex align-items-center gap-2 mb-3">
            <img :src="selectedVenue.imageUrl" class="rounded" style="width:60px;height:40px;object-fit:cover;"/>
            <small class="text-reactive-secondary"><i class="bi bi-geo-alt me-1"/>{{ selectedVenue.addressLine }}</small>
          </div>
          <div v-if="selectedVenue && zones.length > 0" class="mt-3">
            <p class="small text-reactive-secondary mb-3 fw-semibold">Liên kết khu vực venue → khu vực sự kiện</p>
            <div v-for="vz in selectedVenue.zoneNames" :key="vz" class="d-flex align-items-center gap-2 mb-2">
              <small class="text-reactive-primary fw-semibold" style="min-width:140px;">{{ vz }}</small>
              <i class="bi bi-arrow-right text-reactive-secondary"/>
              <select class="form-select form-select-sm bg-reactive-primary border-0 text-reactive-primary" style="max-width:200px;">
                <option value="">— Trang trí —</option>
                <option v-for="z in zones" :key="z.id" :value="z.id">{{ z.name }}</option>
              </select>
            </div>
          </div>
        </div>

        <div v-else>
          <div class="bg-reactive-primary rounded d-flex align-items-center justify-content-center" style="height:340px;">
            <div class="text-center text-reactive-secondary">
              <i class="bi bi-pencil-square fs-1 d-block mb-3 opacity-50"/>
              <p class="small">Canvas editor sẽ hiển thị ở đây</p>
              <p class="small opacity-50">Kéo thả, vẽ và tùy chỉnh sơ đồ chỗ ngồi</p>
            </div>
          </div>
        </div>
      </div>

      <div class="d-flex justify-content-between gap-2 mt-2">
        <button class="btn btn-outline-secondary px-4" @click="currentStep = 2"><i class="bi bi-arrow-left me-1"/>Quay lại</button>
        <div class="d-flex gap-2">
          <button class="btn btn-primary px-4" :disabled="saving" @click="mockSaveLayout">
            <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
            <i v-else class="bi bi-floppy me-2"/>Lưu sơ đồ
          </button>
          <button class="btn btn-success px-4" @click="router.push('/organizer/events')">
            <i class="bi bi-check2 me-2"/>Lưu & Đóng
          </button>
        </div>
      </div>
    </div>

    <!-- Error toast -->
    <div v-if="globalError" class="alert alert-danger position-fixed bottom-0 end-0 m-4" style="z-index:2000;max-width:360px;">
      <i class="bi bi-exclamation-circle me-2"/>{{ globalError }}
      <button class="btn-close float-end" @click="globalError = ''"/>
    </div>

    <!-- Zone Modal -->
    <div v-if="showZoneModal" class="modal-backdrop-custom" @click.self="showZoneModal = false">
      <div class="modal-box bg-reactive-secondary p-4 rounded-3 shadow-lg">
        <h5 class="text-reactive-primary fw-bold mb-4">
          {{ editingZone ? 'Chỉnh sửa khu vực' : 'Thêm khu vực' }}
        </h5>
        <div class="row g-3">
          <div class="col-12">
            <label class="form-label small text-reactive-secondary">Tên khu vực *</label>
            <input v-model="zoneForm.name" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
          </div>
          <div class="col-12">
            <label class="form-label small text-reactive-secondary">Loại</label>
            <select v-model="zoneForm.isStanding" class="form-select bg-reactive-primary border-0 text-reactive-primary">
              <option :value="false">Ghế ngồi (Seated)</option>
              <option :value="true">Đứng tự do (Standing)</option>
            </select>
          </div>
          <div class="col-md-6" v-if="zoneForm.isStanding">
            <label class="form-label small text-reactive-secondary">Sức chứa *</label>
            <input v-model.number="zoneForm.capacity" type="number" min="1" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
          </div>
          <template v-else>
            <div class="col-6">
              <label class="form-label small text-reactive-secondary">Số hàng</label>
              <div class="d-flex align-items-center gap-2">
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridRows = Math.max(1, zoneForm.gridRows - 1)">−</button>
                <input v-model.number="zoneForm.gridRows" type="number" min="1" max="52" class="form-control bg-reactive-primary border-0 text-reactive-primary text-center"/>
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridRows = Math.min(52, zoneForm.gridRows + 1)">+</button>
              </div>
            </div>
            <div class="col-6">
              <label class="form-label small text-reactive-secondary">Ghế / hàng</label>
              <div class="d-flex align-items-center gap-2">
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridCols = Math.max(1, zoneForm.gridCols - 1)">−</button>
                <input v-model.number="zoneForm.gridCols" type="number" min="1" max="200" class="form-control bg-reactive-primary border-0 text-reactive-primary text-center"/>
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridCols = Math.min(200, zoneForm.gridCols + 1)">+</button>
              </div>
            </div>
            <div class="col-12">
              <small class="text-reactive-secondary">
                <i class="bi bi-info-circle me-1"/>Tổng: <span class="text-reactive-primary fw-semibold">{{ zoneForm.gridRows * zoneForm.gridCols }}</span> ghế
              </small>
            </div>
          </template>
          <div class="col-md-6">
            <label class="form-label small text-reactive-secondary">Giá (₫) *</label>
            <div class="input-group">
              <input v-model.number="zoneForm.price" type="number" min="0" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
              <span class="input-group-text bg-reactive-primary border-0 text-reactive-secondary">₫</span>
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label small text-reactive-secondary">Giới hạn mua / người</label>
            <input v-model.number="zoneForm.purchaseLimit" type="number" min="1" class="form-control bg-reactive-primary border-0 text-reactive-primary" placeholder="Không giới hạn"/>
          </div>
        </div>
        <div class="d-flex gap-2 justify-content-end mt-4">
          <button class="btn btn-outline-secondary" @click="showZoneModal = false">Hủy</button>
          <button class="btn btn-primary" @click="saveZone">Lưu</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import {
  mockEvents,
  mockCategories,
  mockVenues,
  type MockZone,
  getStatusLabel,
  formatPrice,
} from '~/mock/organizer.data'

definePageMeta({ layout: 'organizer' })

const route  = useRoute()
const router = useRouter()

const eventId = computed(() => {
  const id = route.params.id as string
  return id === 'new' ? null : id
})
const isNew = computed(() => !eventId.value)

// ── Steps ──────────────────────────────────────────────────
const currentStep = ref(0)
const steps = ['Thông tin cơ bản', 'Nội dung', 'Khu vực & Vé', 'Sơ đồ chỗ ngồi']
const goToStep = (i: number) => { if (!isNew.value || i <= currentStep.value) currentStep.value = i }

const saving         = ref(false)
const globalError    = ref('')
const showDraftSaved = ref(false)
const triggerDraftSaved = () => {
  showDraftSaved.value = true
  setTimeout(() => { showDraftSaved.value = false }, 2500)
}

// ── Status options ─────────────────────────────────────────
const statusOptions = [
  { value: 'EDITING',   label: 'Nháp' },
  { value: 'PREMIERE',  label: 'Sắp mở bán' },
  { value: 'SELLING',   label: 'Đang bán' },
  { value: 'PAUSED',    label: 'Tạm dừng' },
  { value: 'ONGOING',   label: 'Đang diễn' },
  { value: 'FINISHED',  label: 'Đã kết thúc' },
  { value: 'CANCELLED', label: 'Đã hủy' },
]

// ── Venue derived ──────────────────────────────────────────
const selectedVenue = computed(() => mockVenues.find(v => v.id === form.value.venueId) ?? null)

// ── Form state ─────────────────────────────────────────────
const form = ref({
  name: '', categoryId: '' as any, status: 'EDITING' as string,
  addressLine: '', startDate: '', endDate: '', bannerUrl: '', venueId: '',
})
const errors      = ref<Record<string, string>>({})
const content     = ref({ aboutVi: '', aboutEn: '', termsAndConditions: '', policyRefund: '' })
const contentLang = ref<'vi' | 'en'>('vi')
const layoutMode  = ref<'venue' | 'custom'>('venue')

// ── Zones (local reactive copy of seed zones) ──────────────
const zones = ref<MockZone[]>([])

// ── Zone modal ─────────────────────────────────────────────
const showZoneModal = ref(false)
const editingZone   = ref<MockZone | null>(null)
const zoneForm      = ref({ name: '', isStanding: false, capacity: 100, price: 0, purchaseLimit: null as number | null, gridRows: 5, gridCols: 10 })

const openZoneModal = (zone: MockZone | null) => {
  editingZone.value = zone
  if (zone) {
    Object.assign(zoneForm.value, {
      name: zone.name, isStanding: zone.isStanding, capacity: zone.capacity,
      price: zone.price, purchaseLimit: zone.purchaseLimit ?? null,
      gridRows: zone.gridRows ?? 5, gridCols: zone.gridCols ?? 10,
    })
  } else {
    Object.assign(zoneForm.value, { name: '', isStanding: false, capacity: 100, price: 0, purchaseLimit: null, gridRows: 5, gridCols: 10 })
  }
  showZoneModal.value = true
}

const saveZone = () => {
  if (!zoneForm.value.name.trim()) return
  const capacity = zoneForm.value.isStanding
    ? zoneForm.value.capacity
    : zoneForm.value.gridRows * zoneForm.value.gridCols
  if (editingZone.value) {
    const idx = zones.value.findIndex(z => z.id === editingZone.value!.id)
    if (idx >= 0) zones.value[idx] = { ...zones.value[idx], ...zoneForm.value, capacity }
  } else {
    zones.value.push({ id: 'z-new-' + Date.now(), ...zoneForm.value, capacity, perks: [], quantitySold: 0 })
  }
  showZoneModal.value = false
}

const deleteZone = (id: string) => { zones.value = zones.value.filter(z => z.id !== id) }

// ── Load existing event from mock data ─────────────────────
onMounted(() => {
  if (!isNew.value && eventId.value) {
    const ev = mockEvents.find(e => e.id === eventId.value)
    if (ev) {
      form.value = {
        name: ev.name, categoryId: ev.categoryId, status: ev.status,
        addressLine: ev.addressLine, bannerUrl: ev.bannerUrl, venueId: ev.venueId ?? '',
        startDate: ev.session.startDate.slice(0, 16),
        endDate:   ev.session.endDate.slice(0, 16),
      }
      content.value = {
        aboutVi: ev.aboutVi ?? '',
        aboutEn: ev.aboutEn ?? '',
        termsAndConditions: ev.termsAndConditions ?? '',
        policyRefund: ev.policyRefund ?? '',
      }
      // Deep copy zones so edits don't mutate the shared mock data
      zones.value = ev.zones.map(z => ({ ...z }))
    }
  }
})

// ── Step saves (mock — simulated delay) ───────────────────
const validateStep1 = () => {
  errors.value = {}
  if (!form.value.name.trim())        errors.value.name        = 'Tên sự kiện là bắt buộc'
  if (!form.value.categoryId)         errors.value.categoryId  = 'Danh mục là bắt buộc'
  if (!form.value.addressLine.trim()) errors.value.addressLine = 'Địa chỉ là bắt buộc'
  if (!form.value.startDate)          errors.value.startDate   = 'Ngày bắt đầu là bắt buộc'
  if (!form.value.endDate)            errors.value.endDate     = 'Ngày kết thúc là bắt buộc'
  if (form.value.startDate && form.value.endDate && form.value.endDate <= form.value.startDate)
    errors.value.endDate = 'Ngày kết thúc phải sau ngày bắt đầu'
  return Object.keys(errors.value).length === 0
}

const saveStep1 = async () => {
  if (!validateStep1()) return
  saving.value = true
  await new Promise(r => setTimeout(r, 600))
  saving.value = false
  triggerDraftSaved()
  currentStep.value = 1
}

const saveStep2 = async () => {
  saving.value = true
  await new Promise(r => setTimeout(r, 600))
  saving.value = false
  triggerDraftSaved()
  currentStep.value = 2
}

const mockSaveLayout = async () => {
  saving.value = true
  await new Promise(r => setTimeout(r, 800))
  saving.value = false
  triggerDraftSaved()
}
</script>

<style scoped>
.step-bar { align-items: center; }
.step-item { cursor: pointer; min-width: 0; }
.step-dot { width: 32px; height: 32px; font-size: 0.8rem; font-weight: 700; flex-shrink: 0; background: rgba(var(--bs-secondary-rgb), 0.3); color: var(--bs-secondary); transition: background 0.2s, color 0.2s; }
.step-item.active .step-dot    { background: var(--bs-primary); color: #fff; }
.step-item.completed .step-dot { background: #22c55e; color: #fff; }
.step-label { color: var(--bs-secondary); transition: color 0.2s; }
.step-item.active .step-label, .step-item.completed .step-label { color: var(--text-reactive-primary, inherit); }
.step-line { height: 2px; background: rgba(var(--bs-secondary-rgb), 0.25); flex-shrink: 0; min-width: 8px; }
.step-item.completed .step-line { background: #22c55e; }
.zone-card { background: rgba(var(--bs-secondary-rgb), 0.15); border-left: 4px solid #6366f1 !important; transition: box-shadow 0.15s; }
.zone-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.12); }
.perk-badge { background: rgba(99,102,241,0.25); color: #a5b4fc; border: 1px solid rgba(99,102,241,0.4); }
.modal-backdrop-custom { position: fixed; inset: 0; background: rgba(0,0,0,0.55); z-index: 1050; display: flex; align-items: center; justify-content: center; padding: 1rem; overflow-y: auto; }
.modal-box { max-width: 520px; width: 100%; }
.nav-link { color: var(--bs-secondary); background: none; border: none; border-bottom: 2px solid transparent; border-radius: 0; padding: 0.5rem 1rem; cursor: pointer; }
.nav-link.active { color: var(--bs-primary); border-bottom-color: var(--bs-primary); }
.draft-toast-enter-active { transition: opacity 0.3s ease; }
.draft-toast-leave-active { transition: opacity 0.8s ease; }
.draft-toast-enter-from, .draft-toast-leave-to { opacity: 0; }
</style>