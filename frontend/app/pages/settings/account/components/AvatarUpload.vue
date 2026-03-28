<script setup lang="ts">
import { ref } from 'vue';

// TODO: Replace this mock value with the actual ranking from user store / API result, <if unnecessary, remove this>
const currentRank = ref<'bronze' | 'silver' | 'gold' | 'platinum' | 'diamond'>('gold');
</script>

<template>
  <div class="d-flex flex-column align-items-center justify-content-center text-center w-100 h-100">
    <div class="position-relative mb-3">
      <div
        class="avatar-container rounded-circle p-1 border border-primary border-opacity-25 border-4"
      >
        <!-- TODO: vì sau định fill avatar từ database còn ảnh này chỉ là ảnh mẫu -->
        <img
          src="https://lh3.googleusercontent.com/aida-public/AB6AXuBzWVY-moE0L4lXQv8B0vU0SZMKWpHaS6H-1BieYaLKH8AVFHNe8neO4aspGKVjCu2_PJRpo7B90rxZKf3kxzv51lkCKJ5qYcra71eLTVvlFTaK7g82KXRJ6d42mGEJw7ZLCruei9V6k_YWyTMbv9EggdWvncukchj5sTuvhB8v6GER1Gv2TEj8zDqYIJlxqNVX7DcW6MoXp_eY-ohGlsid8CEIPo2U9momjPfKwhtpxzqyZQfa_csYyhgXAxPazypcaZ4jXPvAw7k"
          alt="Profile avatar"
          class="rounded-circle w-100 h-100"
          style="object-fit: cover"
        />
      </div>
      <button
        class="btn btn-primary rounded-circle position-absolute bottom-0 end-0 p-2 shadow edit-btn d-flex align-items-center justify-content-center"
      >
        <i class="bi bi-pencil-fill small text-white"></i>
      </button>
    </div>
    <h3 class="h6 fw-bold mb-0 text-reactive-primary text-nowrap overflow-visible">
      Nguyễn Minh Quang
    </h3>
    <p class="text-reactive-secondary small mb-1 fw-bold">@minhquangdev</p>
    <p class="text-reactive-secondary small mb-3">
      {{ $t('settings.personal_information.member_since') }}
      {{ $d(new Date('2023-12-01'), 'short') }}
    </p>
    <div class="d-flex gap-2 justify-content-center">
      <span
        class="badge rounded-pill px-3 py-2 text-uppercase fw-bold rank-badge"
        :class="`rank-badge--${currentRank}`"
      >
        {{ $t(`settings.personal_information.rank.${currentRank}`) }}
      </span>
    </div>
  </div>
</template>

<style scoped>
.avatar-container {
  width: 160px;
  height: 160px;
  transition: border-color 0.3s ease;
}

.edit-btn {
  width: 36px;
  height: 36px;
  transform: translate(10%, -10%);
  transition: transform 0.2s ease;
}

.edit-btn:hover {
  transform: translate(10%, -10%) scale(1.1);
}

.rank-badge {
  letter-spacing: 0.05em;
  transition: all 0.3s ease;
}

.rank-badge--bronze {
  color: #cd7f32;
  background-color: rgba(205, 127, 50, 0.1);
  border: 1px solid rgba(205, 127, 50, 0.2);
}

.rank-badge--silver {
  color: #aeb5bc;
  background-color: rgba(174, 181, 188, 0.1);
  border: 1px solid rgba(174, 181, 188, 0.2);
}

.rank-badge--gold {
  color: #f5b82e;
  background-color: rgba(245, 184, 46, 0.1);
  border: 1px solid rgba(245, 184, 46, 0.2);
}

.rank-badge--platinum {
  color: #08c7f7;
  background-color: rgba(8, 199, 247, 0.1);
  border: 1px solid rgba(8, 199, 247, 0.2);
}

.rank-badge--diamond {
  color: #af88ff;
  background-color: rgba(175, 136, 255, 0.1);
  border: 1px solid rgba(175, 136, 255, 0.2);
}
</style>
