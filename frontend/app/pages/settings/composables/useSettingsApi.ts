import type { FetchError } from 'ofetch';
import type { ChangeInfo, ChangePassword, Session, UserSummary } from '../types/settings';

function asObject(value: unknown): Record<string, unknown> | null {
  if (!value || typeof value !== 'object' || Array.isArray(value)) {
    return null;
  }
  return value as Record<string, unknown>;
}

export function useSettingsApi() {
  const config = useRuntimeConfig();
  const currentUser = useState<UserSummary | null>('settings-current-user', () => null);

  async function fetchCurrentUser(force = false): Promise<UserSummary> {
    if (!force && currentUser.value) {
      return currentUser.value;
    }

    const user = await $fetch<UserSummary>(`${config.public.userServiceUrl}/users`, {
      credentials: 'include',
    });

    currentUser.value = user;
    return user;
  }

  async function updateCurrentUser(payload: ChangeInfo): Promise<UserSummary> {
    const updatedUser = await $fetch<UserSummary>(`${config.public.userServiceUrl}/users`, {
      method: 'PATCH',
      credentials: 'include',
      body: payload,
    });

    currentUser.value = updatedUser;
    return updatedUser;
  }

  async function changePassword(payload: ChangePassword): Promise<void> {
    await $fetch(`${config.public.userServiceUrl}/users/password`, {
      method: 'PATCH',
      credentials: 'include',
      body: payload,
    });
  }

  async function fetchSessions(): Promise<Session[]> {
    return $fetch<Session[]>(`${config.public.userServiceUrl}/sessions`, {
      credentials: 'include',
    });
  }

  async function deleteSession(displayId: string): Promise<void> {
    await $fetch(`${config.public.userServiceUrl}/sessions/${encodeURIComponent(displayId)}`, {
      method: 'DELETE',
      credentials: 'include',
    });
  }

  function extractFieldErrors(error: FetchError): Record<string, string> {
    const result: Record<string, string> = {};
    const data = asObject(error.data);

    if (!data) {
      return result;
    }

    for (const [key, value] of Object.entries(data)) {
      if (typeof value === 'string') {
        result[key] = value;
      }
    }

    return result;
  }

  function extractMessage(error: FetchError): string | null {
    const data = asObject(error.data);
    const message = data?.message;
    return typeof message === 'string' && message.trim().length > 0 ? message : null;
  }

  return {
    currentUser,
    fetchCurrentUser,
    updateCurrentUser,
    changePassword,
    fetchSessions,
    deleteSession,
    extractFieldErrors,
    extractMessage,
  };
}
