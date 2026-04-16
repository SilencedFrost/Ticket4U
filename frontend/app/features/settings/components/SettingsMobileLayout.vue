<script setup lang="ts">
import '../style/settings.css';

const { currentTheme } = useTheme();
const { isLoggedIn } = storeToRefs(useUserStore());

type SettingsTab = 'account' | 'security' | 'preferences';
type SettingsTabItem = {
  key: SettingsTab;
  icon: string;
  labelKey: string;
  guestRestricted?: boolean;
  disabled: boolean;
};

const route = useRoute();
const router = useRouter();
const localePath = useLocalePath();
const { locales } = useI18n();
const previousPageUrl = ref<string | null>(null);

const activeTab = useState<SettingsTab | null>('settings-active-tab', () => null);

const tabs = computed<SettingsTabItem[]>(() => {
  // TODO: Không disable tab account/security cho khách. Khi bấm tab,
  // hiển thị modal xác nhận đăng nhập với thông báo "Để sử dụng tính năng này bạn cần đăng nhập"
  // và 2 nút "Quay lại" + "Đăng nhập" để tránh chuyển trang ngoài ý muốn.
  // Nếu chọn "Đăng nhập", chuyển sang login kèm redirect để quay lại đúng tab sau khi đăng nhập.
  const items = [
    { key: 'account' as const, icon: 'bi bi-person', labelKey: 'settings.nav.personal_information', guestRestricted: true },
    { key: 'security' as const, icon: 'bi bi-shield-lock', labelKey: 'settings.nav.security', guestRestricted: true },
    { key: 'preferences' as const, icon: 'bi bi-palette', labelKey: 'settings.nav.preferences' },
  ];

  return items.map((item) => ({
    ...item,
    disabled: Boolean(item.guestRestricted && !isLoggedIn.value),
  }));
});

const normalizedPath = computed(() => {
  const path = route.path.replace(/^\/(en|vi)(?=\/|$)/, '');
  if (path.length > 1 && path.endsWith('/')) return path.slice(0, -1);
  return path;
});

watch(normalizedPath, (path) => {
  if (path.startsWith('/settings/account')) { activeTab.value = 'account'; return; }
  if (path.startsWith('/settings/security')) { activeTab.value = 'security'; return; }
  if (path.startsWith('/settings/preferences')) { activeTab.value = 'preferences'; return; }
  if (path === '/settings') activeTab.value = null;
}, { immediate: true });

const activeTabTitle = computed(() => {
  if (activeTab.value === 'security') return 'settings.nav.security';
  if (activeTab.value === 'preferences') return 'settings.nav.preferences';
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
    .map((code) => `/${code}/settings`)
);

function getPathname(urlOrPath: string) {
  try { return new URL(urlOrPath, globalThis.window.location.origin).pathname; }
  catch { return urlOrPath; }
}

function isSettingsPath(urlOrPath: string) {
  const pathname = getPathname(urlOrPath);
  if (pathname.startsWith('/settings')) return true;
  return settingsLocalePrefixes.value.some((prefix) => pathname.startsWith(prefix));
}

onMounted(() => {
  const back = router.options.history.state?.back as string | undefined;
  if (back && !isSettingsPath(back)) previousPageUrl.value = back;
});

function openTab(tab: SettingsTab) {
  if (activeTab.value === tab && normalizedPath.value === `/settings/${tab}`) return;
  if (normalizedPath.value === `/settings/${tab}`) { activeTab.value = tab; return; }
  activeTab.value = tab;
  router.push(localePath(`/settings/${tab}`));
}

function handleTabClick(tab: SettingsTabItem) {
  if (tab.disabled) return;
  openTab(tab.key);
}

function goBackToMenu() { activeTab.value = null; }

function goBackToPreviousPage() {
  if (previousPageUrl.value) { router.push(previousPageUrl.value); return; }
  router.push(localePath('/'));
}
</script>

<template>
  <div class="settings-mobile-shell">
    <div class="settings-mobile-track" :class="mobileTrackClass">
      <section class="settings-mobile-panel settings-mobile-menu-screen text-reactive-primary">
        <header
          class="settings-mobile-header"
          :class="['bg-reactive-primary', 'shadow-sm', { 'border-bottom': currentTheme == 'dark' }]"
        >
          <button type="button" class="settings-mobile-back text-reactive-primary" aria-label="Back" @click="goBackToPreviousPage">
            <i class="bi bi-arrow-left"></i>
          </button>
          <h1 class="settings-mobile-title text-reactive-primary">{{ $t('settings.title') }}</h1>
        </header>
        <nav :aria-label="$t('settings.title')">
          <button
            v-for="tab in tabs" :key="tab.key" type="button"
            :class="[
              'settings-mobile-menu-item',
              'text-reactive-primary',
              'bg-reactive-primary',
              'shadow-sm',
              { 'border-bottom': currentTheme == 'dark' },
              { 'is-disabled': tab.disabled },
            ]"
            :disabled="tab.disabled"
            :aria-disabled="tab.disabled ? 'true' : 'false'"
            @click="handleTabClick(tab)"
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
        <header :class="['settings-mobile-header', 'shadow-sm', { 'border-bottom': currentTheme == 'dark' }]">
          <button type="button" class="settings-mobile-back text-reactive-primary" aria-label="Back" @click="goBackToMenu">
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
</template>