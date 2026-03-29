<script setup lang="ts">
import SettingsNavMenu from './SettingsNavMenu.vue';
import '../style/settings.css';

type SettingsTab = 'account' | 'security';

const route = useRoute();
const router = useRouter();
const localePath = useLocalePath();
const { locales } = useI18n();
const previousPageUrl = ref<string | null>(null);

const activeTab = useState<SettingsTab | null>('settings-active-tab', () => null);

const tabs = [
  {
    key: 'account' as const,
    icon: 'bi bi-person',
    labelKey: 'settings.nav.personal_information',
  },
  {
    key: 'security' as const,
    icon: 'bi bi-shield-lock',
    labelKey: 'settings.nav.security',
  },
];

const normalizedPath = computed(() => {
  const path = route.path.replace(/^\/(en|vi)(?=\/|$)/, '');
  if (path.length > 1 && path.endsWith('/')) {
    return path.slice(0, -1);
  }

  return path;
});

watch(
  normalizedPath,
  (path) => {
    if (path.startsWith('/settings/account')) {
      activeTab.value = 'account';
      return;
    }

    if (path.startsWith('/settings/security')) {
      activeTab.value = 'security';
      return;
    }

    if (path === '/settings') {
      activeTab.value = null;
    }
  },
  { immediate: true },
);

const activeTabTitle = computed(() => {
  if (activeTab.value === 'security') {
    return 'settings.nav.security';
  }

  return 'settings.nav.personal_information';
});

const mobileTrackClass = computed(() => ({
  'is-menu': activeTab.value === null,
  'is-content': activeTab.value !== null,
}));

const settingsLocalePrefixes = computed(() =>
  locales.value
    .map((locale) => (typeof locale === 'string' ? locale : locale.code))
    .filter(Boolean)
    .map((code) => `/${code}/settings`),
);

// Shared helpers

/**
 * Normalize a URL or path-like string into a pathname.
 * This supports both absolute URLs and plain relative paths.
 */
function getPathname(urlOrPath: string) {
  try {
    return new URL(urlOrPath, window.location.origin).pathname;
  } catch {
    return urlOrPath;
  }
}

/**
 * Check whether a URL/path points to the Settings section,
 * including locale-prefixed variants like /en/settings.
 */
function isSettingsPath(urlOrPath: string) {
  const pathname = getPathname(urlOrPath);

  if (pathname.startsWith('/settings')) {
    return true;
  }

  return settingsLocalePrefixes.value.some((prefix) => pathname.startsWith(prefix));
}

// Mobile-only handlers

/**
 * Save the pre-settings route once when the layout mounts.
 * This allows the mobile back button on /settings to return to the entry page
 * instead of stepping back to an internal settings route.
 */
onMounted(() => {
  const back = router.options.history.state?.back as string | undefined;

  if (back && !isSettingsPath(back)) {
    previousPageUrl.value = back;
  }
});

/**
 * Open a settings tab and sync the route with the selected tab.
 * A no-op guard prevents duplicate navigation to the same destination.
 */
function openTab(tab: SettingsTab) {
  if (activeTab.value === tab && normalizedPath.value === `/settings/${tab}`) {
    return;
  }

  if (normalizedPath.value === `/settings/${tab}`) {
    activeTab.value = tab;
    return;
  }

  activeTab.value = tab;
  router.push(localePath(`/settings/${tab}`));
}

/**
 * Return from a tab content screen to the mobile settings menu list.
 * This updates UI state only and intentionally keeps the current tab route.
 */
function goBackToMenu() {
  // On mobile, the menu is a UI state, not a dedicated route.
  // Keep the current tab path so switching back to desktop still renders content.
  activeTab.value = null;
}

/**
 * Leave the Settings section and go back to the page visited before entering it.
 * Falls back to the localized home route if no valid previous page is available.
 */
function goBackToPreviousPage() {
  if (previousPageUrl.value) {
    router.push(previousPageUrl.value);
    return;
  }

  router.push(localePath('/'));
}
</script>

<template>
  <div>
    <div class="settings-desktop-shell d-none d-md-block">
      <div class="settings-desktop-layout">
        <aside class="d-flex flex-column h-100 settings-sidebar card rounded-3 border shadow-sm">
          <SettingsNavMenu />
        </aside>

        <main class="settings-content pb-3">
          <slot />
        </main>
      </div>
    </div>

    <div class="settings-mobile-shell d-md-none">
      <div class="settings-mobile-track" :class="mobileTrackClass">
        <section class="settings-mobile-panel settings-mobile-menu-screen text-reactive-primary">
          <header class="settings-mobile-header">
            <button
              type="button"
              class="settings-mobile-back text-reactive-primary"
              aria-label="Back"
              @click="goBackToPreviousPage"
            >
              <i class="bi bi-arrow-left"></i>
            </button>
            <h1 class="settings-mobile-title text-reactive-primary">{{ $t('settings.title') }}</h1>
          </header>

          <nav class="settings-mobile-menu-list" :aria-label="$t('settings.title')">
            <button
              v-for="tab in tabs"
              :key="tab.key"
              type="button"
              class="settings-mobile-menu-item text-reactive-primary"
              @click="openTab(tab.key)"
            >
              <span class="settings-mobile-menu-item-left">
                <i :class="[tab.icon, 'text-reactive-secondary']"></i>
                <span>{{ $t(tab.labelKey) }}</span>
              </span>
              <i class="bi bi-chevron-right text-reactive-secondary"></i>
            </button>
          </nav>
        </section>

        <section class="settings-mobile-panel text-reactive-primary">
          <header class="settings-mobile-header">
            <button
              type="button"
              class="settings-mobile-back text-reactive-primary"
              aria-label="Back"
              @click="goBackToMenu"
            >
              <i class="bi bi-arrow-left"></i>
            </button>
            <h2 class="settings-mobile-title text-reactive-primary">{{ $t(activeTabTitle) }}</h2>
          </header>

          <div class="settings-mobile-content">
            <slot />
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<style scoped>
.settings-sidebar {
  width: 280px;
  min-width: 240px;
  flex-shrink: 0;
}

.settings-content {
  flex: 1;
  min-width: 0;
}

.settings-desktop-layout {
  display: flex;
  gap: 1rem;
  min-height: calc(100vh - 6rem);
}
</style>
