<script setup lang="ts">
import { useSettingsApi } from '../../composables/useSettingsApi';

const { currentUser, fetchCurrentUser } = useSettingsApi();

const DEFAULT_AVATAR_URL = 'https://cdn.ticket4u.uk/image/upload/default-profile_s2bneu.jpg';

const fullName = computed(() => {
  const user = currentUser.value;
  if (!user) {
    return '';
  }

  const parts = [user.lastName, user.firstName]
    .map((part) => part?.trim() ?? '')
    .filter((part) => part.length > 0);

  if (parts.length > 0) {
    return parts.join(' ');
  }

  return user.username || user.email;
});

const username = computed(() => {
  const value = currentUser.value?.username?.trim();
  return value ? `@${value}` : '';
});

const createdAtDate = computed(() => {
  const value = currentUser.value?.createdAt;
  return value ? new Date(value) : null;
});

onMounted(() => {
  if (!currentUser.value) {
    void fetchCurrentUser();
  }
});
</script>

<template>
  <div class="d-flex flex-column align-items-center justify-content-center text-center w-100 h-100">
    <div class="position-relative mb-3">
      <div
        class="avatar-container rounded-circle p-1 border border-primary border-opacity-25 border-4"
      >
        <div class="h-100 w-100 rounded-circle overflow-hidden">
          <shimmer-img :src="DEFAULT_AVATAR_URL" />
        </div>
      </div>
      <button
        class="btn btn-primary rounded-circle position-absolute bottom-0 end-0 p-2 shadow edit-btn d-flex align-items-center justify-content-center"
      >
        <i class="bi bi-pencil-fill small text-white"></i>
      </button>
    </div>
    <h3 class="h6 fw-bold mb-0 text-reactive-primary text-nowrap overflow-visible">
      {{ fullName || '-' }}
    </h3>
    <p class="text-reactive-secondary small mb-1 fw-bold">{{ username || '-' }}</p>
    <p class="text-reactive-secondary small mb-3">
      {{ $t('settings.personal_information.member_since') }}
      {{ createdAtDate ? $d(createdAtDate, 'short') : '-' }}
    </p>
  </div>
</template>

<style scoped>
.avatar-container {
  height: 160px;
  aspect-ratio: 1;
}

.edit-btn {
  width: 36px;
  height: 36px;
}
</style>
