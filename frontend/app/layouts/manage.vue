<template>
  <div class="manage-shell d-flex">

    <!-- Sidebar -->
    <aside class="manage-sidebar bg-reactive-primary d-flex flex-column" :class="{ collapsed: sidebarCollapsed }">

      <!-- Org Info -->
      <div class="sidebar-header px-3 py-4 border-bottom border-secondary">
        <div class="d-flex align-items-center gap-3">
          <img
            v-if="logoUrl"
            :src="logoUrl"
            class="org-avatar rounded-circle flex-shrink-0"
            alt="org logo"
          />
          <div v-else class="org-avatar rounded-circle flex-shrink-0 bg-primary d-flex align-items-center justify-content-center">
            <i class="bi bi-building text-white" />
          </div>
          <transition name="fade-text">
            <div v-if="!sidebarCollapsed" class="overflow-hidden">
              <div class="fw-bold text-reactive-primary text-truncate" style="max-width: 140px">
                {{ profile?.name ?? 'Organizer' }}
              </div>
              <small class="text-reactive-secondary">Organizer Admin</small>
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
          <i :class="['bi', item.icon, 'fs-5', 'flex-shrink-0']" />
          <transition name="fade-text">
            <span v-if="!sidebarCollapsed" class="text-truncate">{{ item.label }}</span>
          </transition>
        </NuxtLink>
      </nav>

      <!-- Collapse toggle -->
      <div class="sidebar-footer border-top border-secondary py-3">
        <button
          class="sidebar-link d-flex align-items-center gap-3 px-3 py-2 w-100 border-0 bg-transparent"
          @click="sidebarCollapsed = !sidebarCollapsed"
        >
          <i :class="['bi', sidebarCollapsed ? 'bi-chevron-right' : 'bi-chevron-left', 'fs-5', 'flex-shrink-0']" />
          <transition name="fade-text">
            <span v-if="!sidebarCollapsed" class="text-reactive-secondary">{{ $t('organizer.nav.collapse') }}</span>
          </transition>
        </button>
      </div>
    </aside>

    <!-- Right side: topbar + content -->
    <div class="manage-body d-flex flex-column flex-grow-1 overflow-hidden">

      <!-- Topbar -->
      <header class="manage-topbar bg-reactive-primary border-bottom d-flex align-items-center justify-content-end gap-2 px-3 flex-shrink-0">

        <!-- Language switcher -->
        <div ref="langRef" class="position-relative">
          <button class="topbar-btn d-flex align-items-center gap-1" @click.stop="toggleMenu('lang')">
            <i class="bi bi-globe2" />
            <span class="small">{{ locale.toUpperCase() }}</span>
          </button>
          <div v-if="openMenu === 'lang'" class="position-absolute end-0 top-100 mt-2 z-3">
            <language-switcher-drop-down @click="openMenu = 'none'" />
          </div>
        </div>

        <!-- Theme toggle -->
        <button class="topbar-btn d-flex align-items-center" @click="toggleTheme">
          <i v-if="nextTheme === 'dark'" class="bi bi-brightness-high-fill" />
          <i v-else class="bi bi-moon-fill" />
        </button>

        <!-- Account dropdown -->
        <div ref="accountRef" class="position-relative">
          <button class="topbar-btn d-flex align-items-center gap-2" @click.stop="toggleMenu('account')">
            <i class="bi bi-person-circle" style="font-size: 1.25rem" />
            <span class="small d-none d-md-inline text-reactive-secondary">{{ profile?.name ?? '' }}</span>
          </button>
          <div v-if="openMenu === 'account'" class="position-absolute end-0 top-100 mt-2 z-3">
            <div class="card bg-reactive-primary border overflow-hidden" style="min-width: 160px">
              <NuxtLink
                :to="localePath('/settings')"
                class="dropdown-item d-flex align-items-center gap-2 py-2 px-3 text-reactive-primary text-decoration-none"
                @click="openMenu = 'none'"
              >
                <i class="bi bi-gear-fill" /><span>{{ $t('common.settings') }}</span>
              </NuxtLink>
              <div class="dropdown-divider m-0" />
              <button
                class="dropdown-item d-flex align-items-center gap-2 py-2 px-3 text-danger border-0 bg-transparent w-100"
                @click="handleLogout"
              >
                <i class="bi bi-box-arrow-left" /><span>{{ $t('organizer.nav.logout') }}</span>
              </button>
            </div>
          </div>
        </div>
      </header>

      <!-- Page content -->
      <main class="flex-grow-1 overflow-auto bg-reactive-secondary">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { onClickOutside } from '@vueuse/core'
import LanguageSwitcherDropDown from './components/navigation/menus/LanguageSwitcherDropDown.vue'
import { useOrganizerProfile } from '~/composables/useOrganizerProfile'

const { t: $t, locale } = useI18n()
const { nextTheme, toggleTheme } = useTheme()
const localePath = useLocalePath()
const route = useRoute()
const router = useRouter()
const useUser = useUserStore()

const sidebarCollapsed = ref(false)
const openMenu = ref('none')

const langRef = ref<HTMLElement | null>(null)
const accountRef = ref<HTMLElement | null>(null)
onClickOutside(langRef, () => { if (openMenu.value === 'lang') openMenu.value = 'none' })
onClickOutside(accountRef, () => { if (openMenu.value === 'account') openMenu.value = 'none' })

function toggleMenu(key: string) {
  openMenu.value = openMenu.value === key ? 'none' : key
}

// Profile
const { profile, fetchProfile, reset } = useOrganizerProfile()
onMounted(fetchProfile)

const logoUrl = computed(() => profile.value?.logoUrl ?? profile.value?.logo_url ?? null)

// Nav items
const navItems = computed(() => [
  { to: localePath('/manage'),                  label: $t('organizer.nav.home'),    icon: 'bi-house'          },
  { to: localePath('/manage/dashboard/events'), label: $t('organizer.nav.events'),  icon: 'bi-calendar-event' },
  { to: localePath('/manage/team'),             label: $t('organizer.nav.team'),    icon: 'bi-people'         },
])

const isActive = (to: string) => {
  const path = route.path
  const cleanPath = path.replace(/^\/(en|vi)/, '')
  const cleanTo   = to.replace(/^\/(en|vi)/, '')
  if (cleanTo === '/manage') return cleanPath === '/manage' || cleanPath === '/manage/'
  return cleanPath.startsWith(cleanTo)
}

// Logout
const handleLogout = async () => {
  reset()
  await useUser.logout()
  router.push(localePath('/auth/login'))
}
</script>

<style scoped>
.manage-shell   { min-height: 100vh; }
.manage-sidebar {
  width: 240px; flex-shrink: 0;
  transition: width 0.25s ease; overflow: hidden;
}
.manage-sidebar.collapsed { width: 60px; }
.org-avatar { width: 40px; height: 40px; object-fit: cover; flex-shrink: 0; }
.sidebar-link {
  cursor: pointer;
  color: var(--text-reactive-secondary, #6b7280);
  border-radius: 8px; margin: 0 8px;
  transition: background 0.15s, color 0.15s;
  white-space: nowrap;
}
.sidebar-link:hover  { background: rgba(var(--bs-primary-rgb), 0.1);  color: var(--bs-primary); }
.sidebar-link.active { background: rgba(var(--bs-primary-rgb), 0.15); color: var(--bs-primary); font-weight: 600; }
.manage-topbar { height: 56px; }
.topbar-btn {
  cursor: pointer;
  border: none; background: transparent;
  color: var(--text-reactive-secondary, #6b7280);
  border-radius: 6px; padding: 4px 8px;
  transition: background 0.15s, color 0.15s;
}
.topbar-btn:hover { background: rgba(var(--bs-primary-rgb), 0.1); color: var(--bs-primary); }
.fade-text-enter-active { transition: opacity 0.15s ease 0.1s; }
.fade-text-leave-active { transition: opacity 0.1s ease; }
.fade-text-enter-from, .fade-text-leave-to { opacity: 0; }
</style>
