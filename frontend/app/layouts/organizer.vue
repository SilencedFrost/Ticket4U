<template>
  <div class="organizer-shell d-flex h-100 bg-reactive-primary">

    <!-- Sidebar -->
    <aside class="organizer-sidebar bg-reactive-secondary d-flex flex-column" :class="{ collapsed: sidebarCollapsed }">

      <!-- Logo / Org Info -->
      <div class="sidebar-header px-3 py-4 border-bottom border-secondary">
        <div class="d-flex align-items-center gap-3">
          <img
              v-if="logoUrl"
              :src="logoUrl"
              class="org-avatar rounded-circle flex-shrink-0"
              alt="org logo"
          />
          <div v-else class="org-avatar-placeholder rounded-circle flex-shrink-0 bg-primary d-flex align-items-center justify-content-center">
            <i class="bi bi-building text-white"/>
          </div>
          <transition name="fade-text">
            <div v-if="!sidebarCollapsed" class="overflow-hidden">
              <div class="fw-bold text-reactive-primary text-truncate" style="max-width: 140px;">
                {{ profile?.name ?? 'Organizer' }}
              </div>
              <small class="text-reactive-secondary">{{ roleLabel }}</small>
            </div>
          </transition>
        </div>
      </div>

      <!-- Nav Links -->
      <nav class="flex-grow-1 py-3">
        <NuxtLink
            v-for="item in navItems"
            :key="item.to"
            :to="item.to"
            class="sidebar-link d-flex align-items-center gap-3 px-3 py-2 text-decoration-none"
            :class="{ active: isActive(item.to) }"
        >
          <i :class="['bi', item.icon, 'fs-5', 'flex-shrink-0']"/>
          <transition name="fade-text">
            <span v-if="!sidebarCollapsed" class="text-truncate">{{ item.label }}</span>
          </transition>
        </NuxtLink>
      </nav>

      <!-- Bottom: collapse toggle + logout -->
      <div class="sidebar-footer border-top border-secondary py-3">
        <button
            class="sidebar-link d-flex align-items-center gap-3 px-3 py-2 w-100 border-0 bg-transparent"
            @click="sidebarCollapsed = !sidebarCollapsed"
        >
          <i :class="['bi', sidebarCollapsed ? 'bi-chevron-right' : 'bi-chevron-left', 'fs-5', 'flex-shrink-0']"/>
          <transition name="fade-text">
            <span v-if="!sidebarCollapsed" class="text-reactive-secondary">{{ $t('organizer.nav.collapse') }}</span>
          </transition>
        </button>
        <button
            class="sidebar-link d-flex align-items-center gap-3 px-3 py-2 w-100 border-0 bg-transparent"
            @click="handleLogout"
        >
          <i class="bi bi-box-arrow-left fs-5 flex-shrink-0 text-danger"/>
          <transition name="fade-text">
            <span v-if="!sidebarCollapsed" class="text-danger">{{ $t('organizer.nav.logout') }}</span>
          </transition>
        </button>
      </div>
    </aside>

    <!-- Main content -->
    <main class="organizer-main flex-grow-1 overflow-auto">
      <slot />
    </main>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useOrganizerProfile } from '~/composables/useOrganizerProfile'

const { t: $t } = useI18n()
const localePath = useLocalePath()
const route  = useRoute()
const router = useRouter()

const sidebarCollapsed = ref(false)

// ── Profile ────────────────────────────────────────────────
const { profile, fetchProfile, reset } = useOrganizerProfile()
onMounted(fetchProfile)

const logoUrl = computed(() => profile.value?.logoUrl ?? profile.value?.logo_url ?? null)

// ── Role label ─────────────────────────────────────────────
const roleLabel = computed(() => {
  if (!profile.value) return ''
  return 'Organizer Admin'
})

// ── Nav items ──────────────────────────────────────────────
const navItems = computed(() => [
  { to: localePath('/organizer'),                 label: $t('organizer.nav.home'),     icon: 'bi-house'          },
  { to: localePath('/organizer/events'),          label: $t('organizer.nav.events'),   icon: 'bi-calendar-event' },
  { to: localePath('/organizer/reports'),         label: $t('organizer.nav.reports'),  icon: 'bi-bar-chart'      },
  { to: localePath('/organizer/events/archived'), label: $t('organizer.nav.archived'), icon: 'bi-archive'        },
])

const isActive = (to: string) => {
  const path = route.path
  const cleanPath = path.replace(/^\/(en|vi)/, '')
  const cleanTo   = to.replace(/^\/(en|vi)/, '')
  if (cleanTo === '/organizer') return cleanPath === '/organizer' || cleanPath === '/organizer/'
  return cleanPath.startsWith(cleanTo)
}

// ── Logout ─────────────────────────────────────────────────
const handleLogout = () => {
  reset()
  router.push(localePath('/login'))
}
</script>

<style scoped>
.organizer-shell { min-height: 100vh; }
.organizer-sidebar {
  width: 240px; flex-shrink: 0;
  transition: width 0.25s ease; overflow: hidden;
}
.organizer-sidebar.collapsed { width: 60px; }
.org-avatar, .org-avatar-placeholder {
  width: 40px; height: 40px; object-fit: cover; flex-shrink: 0;
}
.sidebar-link {
  cursor: pointer;
  color: var(--text-reactive-secondary, #6b7280);
  border-radius: 8px; margin: 0 8px;
  transition: background 0.15s, color 0.15s;
  white-space: nowrap;
}
.sidebar-link:hover  { background: rgba(var(--bs-primary-rgb), 0.1);  color: var(--bs-primary); }
.sidebar-link.active { background: rgba(var(--bs-primary-rgb), 0.15); color: var(--bs-primary); font-weight: 600; }
.organizer-main { min-height: 100vh; }
.fade-text-enter-active { transition: opacity 0.15s ease 0.1s; }
.fade-text-leave-active { transition: opacity 0.1s ease; }
.fade-text-enter-from, .fade-text-leave-to { opacity: 0; }
</style>