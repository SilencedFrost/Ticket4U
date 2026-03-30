<script setup lang="ts">
interface Session {
  id: string;
  user_agent: string | null;
  updated_at: string;
}

const { locale } = useI18n();

// TODO: replace with real API composable when backend is ready.
const sessions = ref<Session[]>([
  {
    id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    user_agent: 'Chrome 124 on Windows 11',
    updated_at: new Date().toISOString(),
  },
  {
    id: 'b2c3d4e5-f6a7-8901-bcde-f12345678901',
    user_agent: 'Safari 17 on iPhone 15',
    updated_at: new Date(Date.now() - 86_400_000).toISOString(),
  },
]);

const sessionIds = ref<Map<string, string>>(new Map());

async function deriveSessionId(uuid: string): Promise<string> {
  const encoder = new TextEncoder();
  const data = encoder.encode(uuid);
  const hashBuffer = await crypto.subtle.digest('SHA-256', data);
  const hashArray = Array.from(new Uint8Array(hashBuffer));
  const hex = hashArray.map((byte) => byte.toString(16).padStart(2, '0')).join('');
  return `#${hex.slice(0, 6).toUpperCase()}`;
}

async function computeSessionIds() {
  const entries = await Promise.all(
    sessions.value.map(async (session) => [session.id, await deriveSessionId(session.id)] as const),
  );

  sessionIds.value = new Map(entries);
}

function formatDate(iso: string): string {
  return new Intl.DateTimeFormat(locale.value, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(iso));
}

function resolveUserAgent(value: string | null): string | null {
  if (!value) {
    return null;
  }

  const normalized = value.trim();
  return normalized.length > 0 ? normalized : null;
}

onMounted(() => {
  void computeSessionIds();
});
</script>

<template>
  <div class="card p-3 shadow-sm">
    <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
      {{ $t('settings.security.sessions.title') }}
    </h2>

    <div class="d-flex flex-column gap-0">
      <div
        v-for="(session, index) in sessions"
        :key="session.id"
        class="d-flex align-items-start gap-3 py-3"
        :class="{
          'border-bottom border-secondary-subtle border-opacity-25': index < sessions.length - 1,
        }"
      >
        <div class="flex-shrink-0">
          <code class="text-primary fw-bold">
            {{ sessionIds.get(session.id) || '#------' }}
          </code>
        </div>

        <div class="flex-fill">
          <div class="fw-medium text-reactive-primary">
            <span v-if="resolveUserAgent(session.user_agent)">
              {{ resolveUserAgent(session.user_agent) }}
            </span>
            <span v-else class="text-reactive-secondary fst-italic">
              {{ $t('settings.security.sessions.unknown_device') }}
            </span>
          </div>

          <div class="small text-reactive-secondary mt-1">
            {{ $t('settings.security.sessions.columns.last_access') }}:
            {{ formatDate(session.updated_at) }}
          </div>
        </div>
        <div>
          <span class="text-decoration-underline small text-clickable">Remove</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Bootstrap table token overrides have no utility-class equivalent. */
.session-table {
  --bs-table-color: var(--text-reactive-primary);
  --bs-table-hover-color: var(--text-reactive-primary);
  --bs-table-bg: transparent;
  --bs-table-hover-bg: rgba(var(--bs-secondary-rgb), 0.16);
  --bs-table-border-color: rgba(var(--bs-secondary-rgb), 0.24);
}
</style>
