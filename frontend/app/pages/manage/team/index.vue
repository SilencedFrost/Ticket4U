<script setup lang="ts">
import { ref, computed } from 'vue'
import {
  mockStaff,
  ASSIGNABLE_ROLES,
  getStaffDisplayName,
  getAssignableRole,
  formatDate,
} from '../mock.data'
import type { OrganizerStaff } from '../(types)/staff'
import InviteModal from './(components)/InviteModal.vue'

const { t: $t } = useI18n()

const staff        = ref([...mockStaff])
const showInvite   = ref(false)
const confirmRemove = ref(null as OrganizerStaff | null)
const editRoleFor  = ref(null as OrganizerStaff | null)
const newRoleId    = ref('')
const filterRole   = ref('')
const searchQuery  = ref('')

// ── Computed ──────────────────────────────────────────────────

const filteredStaff = computed(function () {
  return staff.value.filter(function (s) {
    const matchRole   = !filterRole.value || s.roleId === Number(filterRole.value)
    const matchSearch = !searchQuery.value ||
      getStaffDisplayName(s).toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      s.email.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchRole && matchSearch
  })
})

const stats = computed(function () {
  return ASSIGNABLE_ROLES.map(function (r) {
    return { ...r, count: staff.value.filter(function (s) { return s.roleId === r.id }).length }
  })
})

// ── Actions ───────────────────────────────────────────────────

function handleInvite(payload: { email: string; roleId: number }) {
  staff.value.push({
    id:        'staff-new-' + Date.now(),
    email:     payload.email,
    username:  payload.email.split('@')[0],
    roleId:    payload.roleId as OrganizerStaff['roleId'],
    invitedAt: new Date().toISOString(),
    isActive:  true,
  })
}

function toggleActive(member: OrganizerStaff) {
  const idx = staff.value.findIndex(function (s) { return s.id === member.id })
  if (idx >= 0) staff.value[idx] = { ...staff.value[idx], isActive: !staff.value[idx].isActive }
}

function openEditRole(member: OrganizerStaff) {
  editRoleFor.value = member
  newRoleId.value   = String(member.roleId)
}

function saveRole() {
  if (!editRoleFor.value || !newRoleId.value) return
  const idx = staff.value.findIndex(function (s) { return s.id === editRoleFor.value!.id })
  if (idx >= 0) staff.value[idx] = { ...staff.value[idx], roleId: Number(newRoleId.value) as OrganizerStaff['roleId'] }
  editRoleFor.value = null
}

function confirmRemoveMember() {
  if (!confirmRemove.value) return
  staff.value = staff.value.filter(function (s) { return s.id !== confirmRemove.value!.id })
  confirmRemove.value = null
}
</script>

<template>
  <div class="p-3 p-md-4 p-lg-5">

    <!-- Header -->
    <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
      <div>
        <h2 class="fw-bold text-reactive-primary mb-1">{{ $t('organizer.team.title') }}</h2>
        <p class="text-reactive-secondary mb-0">{{ $t('organizer.team.subtitle') }}</p>
      </div>
      <button class="btn btn-primary px-4" @click="showInvite = true">
        <i class="bi bi-person-plus me-2"/>{{ $t('organizer.team.invite') }}
      </button>
    </div>

    <!-- Role stats -->
    <div class="row g-3 mb-4">
      <div v-for="stat in stats" :key="stat.id" class="col-6 col-md-3">
        <div class="card shadow-sm p-3 h-100">
          <div class="d-flex align-items-center justify-content-between mb-2">
            <small class="text-reactive-secondary">{{ $t('organizer.team.roles.' + stat.key) }}</small>
            <div
              class="stat-icon rounded-circle d-flex align-items-center justify-content-center"
              :style="{ background: stat.color + '22' }"
            >
              <i :class="['bi', stat.icon]" :style="{ color: stat.color }"/>
            </div>
          </div>
          <div class="fw-bold text-reactive-primary fs-4">{{ stat.count }}</div>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="card shadow-sm p-3 mb-4">
      <div class="row g-3 align-items-end">
        <div class="col-md-5">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.events.search') }}</label>
          <div class="input-group">
            <span class="input-group-text">
              <i class="bi bi-search text-reactive-secondary"/>
            </span>
            <input
              v-model="searchQuery"
              type="text"
              class="form-control"
              :placeholder="$t('organizer.team.search_placeholder')"
            />
          </div>
        </div>
        <div class="col-md-3">
          <label class="form-label small text-reactive-secondary">{{ $t('organizer.team.filter_role') }}</label>
          <select v-model="filterRole" class="form-select">
            <option value="">{{ $t('organizer.team.all_roles') }}</option>
            <option v-for="r in ASSIGNABLE_ROLES" :key="r.id" :value="r.id">
              {{ $t('organizer.team.roles.' + r.key) }}
            </option>
          </select>
        </div>
        <div class="col-md-2">
          <button class="btn btn-outline-secondary w-100" @click="searchQuery = ''; filterRole = ''">
            {{ $t('organizer.events.reset') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Staff table -->
    <div class="card shadow-sm overflow-hidden">

      <div v-if="filteredStaff.length === 0" class="text-center py-5 text-reactive-secondary">
        <i class="bi bi-people fs-1 d-block mb-3 opacity-25"/>
        <div class="fw-semibold mb-1">{{ $t('organizer.team.empty') }}</div>
        <small class="opacity-75">{{ $t('organizer.team.empty_sub') }}</small>
      </div>

      <div v-else class="table-responsive">
        <table class="table table-hover mb-0 team-table">
          <thead>
            <tr>
              <th class="text-reactive-secondary small fw-semibold ps-4">{{ $t('organizer.team.col.member') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.team.col.role') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.team.col.status') }}</th>
              <th class="text-reactive-secondary small fw-semibold">{{ $t('organizer.team.col.invited') }}</th>
              <th class="text-reactive-secondary small fw-semibold pe-4">{{ $t('organizer.team.col.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="member in filteredStaff" :key="member.id">

              <!-- Member -->
              <td class="ps-4 py-3">
                <div class="d-flex align-items-center gap-3">
                  <div
                    class="member-avatar rounded-circle d-flex align-items-center justify-content-center flex-shrink-0 bg-primary bg-opacity-25"
                  >
                    <span class="fw-bold text-primary small">
                      {{ getStaffDisplayName(member).charAt(0).toUpperCase() }}
                    </span>
                  </div>
                  <div>
                    <div class="fw-semibold text-reactive-primary">{{ getStaffDisplayName(member) }}</div>
                    <small class="text-reactive-secondary">{{ member.email }}</small>
                  </div>
                </div>
              </td>

              <!-- Role -->
              <td class="py-3">
                <template v-if="editRoleFor && editRoleFor.id === member.id">
                  <div class="d-flex align-items-center gap-2">
                    <select
                      v-model="newRoleId"
                      class="form-select form-select-sm"
                      style="max-width: 160px;"
                    >
                      <option v-for="r in ASSIGNABLE_ROLES" :key="r.id" :value="r.id">
                        {{ $t('organizer.team.roles.' + r.key) }}
                      </option>
                    </select>
                    <button class="btn btn-sm btn-primary" @click="saveRole">
                      <i class="bi bi-check-lg"/>
                    </button>
                    <button class="btn btn-sm btn-outline-secondary" @click="editRoleFor = null">
                      <i class="bi bi-x-lg"/>
                    </button>
                  </div>
                </template>
                <template v-else>
                  <span
                    class="badge rounded-pill px-3 py-2"
                    :style="{
                      background: (getAssignableRole(member.roleId)?.color ?? '#888') + '22',
                      color: getAssignableRole(member.roleId)?.color ?? '#888',
                    }"
                  >
                    <i :class="['bi', getAssignableRole(member.roleId)?.icon ?? 'bi-person', 'me-1']"/>
                    {{ $t('organizer.team.roles.' + (getAssignableRole(member.roleId)?.key ?? '')) }}
                  </span>
                </template>
              </td>

              <!-- Status -->
              <td class="py-3">
                <span
                  class="badge rounded-pill px-3 py-2"
                  :class="member.isActive ? 'bg-success bg-opacity-25 text-success' : 'bg-secondary bg-opacity-25 text-secondary'"
                >
                  {{ member.isActive ? $t('organizer.team.status.active') : $t('organizer.team.status.inactive') }}
                </span>
              </td>

              <!-- Invited at -->
              <td class="py-3">
                <small class="text-reactive-secondary">{{ formatDate(member.invitedAt) }}</small>
              </td>

              <!-- Actions -->
              <td class="py-3 pe-4">
                <div class="d-flex gap-2">
                  <button
                    class="btn btn-sm btn-outline-secondary"
                    :title="$t('organizer.team.action.edit_role')"
                    @click="openEditRole(member)"
                  >
                    <i class="bi bi-pencil"/>
                  </button>
                  <button
                    class="btn btn-sm"
                    :class="member.isActive ? 'btn-outline-warning' : 'btn-outline-success'"
                    :title="member.isActive ? $t('organizer.team.action.deactivate') : $t('organizer.team.action.activate')"
                    @click="toggleActive(member)"
                  >
                    <i :class="member.isActive ? 'bi bi-pause-fill' : 'bi bi-play-fill'"/>
                  </button>
                  <button
                    class="btn btn-sm btn-outline-danger"
                    :title="$t('organizer.team.action.remove')"
                    @click="confirmRemove = member"
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

    <!-- Invite modal -->
    <InviteModal :show="showInvite" @close="showInvite = false" @invite="handleInvite"/>

    <!-- Remove confirm modal -->
    <Teleport to="body">
      <div v-if="confirmRemove" class="modal-backdrop-custom" @click.self="confirmRemove = null">
        <div class="modal-box card shadow p-4">
          <h5 class="fw-bold text-reactive-primary mb-3">
            <i class="bi bi-exclamation-triangle-fill text-danger me-2"/>
            {{ $t('organizer.team.remove_confirm.title') }}
          </h5>
          <p class="text-reactive-secondary mb-4">
            {{ $t('organizer.team.remove_confirm.confirm', { name: getStaffDisplayName(confirmRemove) }) }}
          </p>
          <div class="d-flex justify-content-end gap-2">
            <button class="btn btn-outline-secondary px-4" @click="confirmRemove = null">
              {{ $t('organizer.team.invite_modal.cancel') }}
            </button>
            <button class="btn btn-danger px-4" @click="confirmRemoveMember">
              {{ $t('organizer.team.remove_confirm.confirm_btn') }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>

  </div>
</template>

<style scoped>
.member-avatar { width: 40px; height: 40px; }
.team-table { color: inherit; }
.team-table thead tr { border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.2); }
.team-table tbody tr { transition: background 0.15s; border-bottom: 1px solid rgba(var(--bs-secondary-rgb), 0.1); }
.team-table tbody tr:last-child { border-bottom: none; }
.team-table tbody tr:hover { background: rgba(var(--bs-primary-rgb), 0.04); }
.stat-icon { width: 36px; height: 36px; font-size: 1rem; }
.modal-backdrop-custom {
  position: fixed; inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex; align-items: center; justify-content: center;
  z-index: 1050;
}
.modal-box { width: 100%; max-width: 480px; border-radius: 12px; }
</style>
