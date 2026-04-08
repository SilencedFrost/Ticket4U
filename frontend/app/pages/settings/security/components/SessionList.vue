<script setup lang="ts">
import type { FetchError } from 'ofetch';
import type { Session } from '../../types/settings';
import { useSettingsApi } from '../../../../features/settings/composables/useSettingsApi';

const { fetchSessions, deleteSession, extractMessage } = useSettingsApi();
const loading = ref(false);
const genericError = ref('');

const sessions = ref<Session[]>([]);
// TODO: backend thêm field isCurrent: boolean vào Session response
// để identify session hiện tại của người dùng
const deletingIds = ref<Set<string>>(new Set());

async function loadSessions() {
  loading.value = true;
  genericError.value = '';

  try {
    sessions.value = await fetchSessions();
  } catch (err) {
    const fetchError = err as FetchError;
    genericError.value = !fetchError.statusCode
      ? 'auth.error.network'
      : extractMessage(fetchError) ?? 'auth.error.unknown';
  } finally {
    loading.value = false;
  }
}

async function removeSession(displayId: string) {
  if (deletingIds.value.has(displayId)) {
    return;
  }

  genericError.value = '';
  deletingIds.value.add(displayId);

  try {
    await deleteSession(displayId);
    sessions.value = sessions.value.filter((session) => session.displayId !== displayId);
  } catch (err) {
    const fetchError = err as FetchError;
    genericError.value = !fetchError.statusCode
      ? 'auth.error.network'
      : extractMessage(fetchError) ?? 'auth.error.unknown';
  } finally {
    deletingIds.value.delete(displayId);
  }
}

function resolveUserAgent(value: string | null): string | null {
  if (!value) {
    return null;
  }

  const normalized = value.trim();
  return normalized.length > 0 ? normalized : null;
}

onMounted(() => {
  void loadSessions();
});
</script>

<template>
  <div class="card p-3 shadow-sm">
    <h2 class="h6 fw-bold mb-3 d-flex align-items-center gap-2 text-primary">
      {{ $t('settings.security.sessions.title') }}
    </h2>

    <div v-if="genericError" class="alert alert-danger py-2" role="alert">
      {{ $t(genericError) }}
    </div>

    <div v-if="!loading && sessions.length === 0" class="text-reactive-secondary small">
      {{ $t('settings.security.sessions.empty') }}
    </div>

    <div class="d-flex flex-column gap-0">
      <div
        v-for="(session, index) in sessions"
        :key="session.displayId"
        class="d-flex align-items-start gap-3 py-3"
        :class="{
          'border-bottom border-secondary-subtle border-opacity-25': index < sessions.length - 1,
        }"
      >
        <div class="flex-shrink-0">
          <code class="text-primary fw-bold">
            #{{ session.displayId.toUpperCase() }}
          </code>
          <!-- TODO: hiển thị badge "Thiết bị này" nếu session.isCurrent === true -->
        </div>

        <div class="flex-fill">
          <div class="fw-medium text-reactive-primary">
            <span v-if="resolveUserAgent(session.userAgent)">
              {{ resolveUserAgent(session.userAgent) }}
            </span>
            <span v-else class="text-reactive-secondary fst-italic">
              {{ $t('settings.security.sessions.unknown_device') }}
            </span>
          </div>

          <div class="small text-reactive-secondary mt-1">
            {{ $t('settings.security.sessions.columns.last_access') }}:
            {{ $d(new Date(session.updatedAt), 'short') }}
          </div>
        </div>
        <div>
          <button
            type="button"
            class="btn btn-link p-0 text-decoration-underline small text-clickable"
            :disabled="deletingIds.has(session.displayId)"
            @click="removeSession(session.displayId)"
          >
            {{ $t('common.action.remove') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
