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
  <div class="card p-3 rounded-3 shadow-none border">
    <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
      <i class="bi bi-shield-check-fill"></i>
      {{ $t('settings.security.sessions.title') }}
    </h2>

    <div class="table-responsive">
      <table class="table table-borderless table-hover mb-0 align-middle session-table">
        <thead class="bg-reactive-secondary">
          <tr>
            <th scope="col" class="text-reactive-secondary fw-semibold">
              {{ $t('settings.security.sessions.columns.session_id') }}
            </th>
            <th scope="col" class="text-reactive-secondary fw-semibold">
              {{ $t('settings.security.sessions.columns.user_agent') }}
            </th>
            <th scope="col" class="text-reactive-secondary fw-semibold">
              {{ $t('settings.security.sessions.columns.last_access') }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="session in sessions" :key="session.id">
            <td>
              <code class="text-primary fw-bold">
                {{ sessionIds.get(session.id) || '#------' }}
              </code>
            </td>
            <td>
              <span v-if="resolveUserAgent(session.user_agent)">
                {{ resolveUserAgent(session.user_agent) }}
              </span>
              <span v-else class="text-reactive-secondary fst-italic">
                {{ $t('settings.security.sessions.unknown_device') }}
              </span>
            </td>
            <td>{{ formatDate(session.updated_at) }}</td>
          </tr>
        </tbody>
      </table>
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
