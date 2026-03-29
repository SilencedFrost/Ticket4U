<script setup lang="ts">
// TODO: fill in script
</script>

<template>
  <div class="d-flex flex-column align-items-center justify-content-center text-center w-100 h-100">
    <div class="position-relative mb-3">
      <div
        class="avatar-container rounded-circle p-1 border border-primary border-opacity-25 border-4"
      >
        <!-- TODO: vì sau định fill avatar từ database còn ảnh này chỉ là ảnh mẫu -->
        <nuxt-img
          src="https://lh3.googleusercontent.com/aida-public/AB6AXuBzWVY-moE0L4lXQv8B0vU0SZMKWpHaS6H-1BieYaLKH8AVFHNe8neO4aspGKVjCu2_PJRpo7B90rxZKf3kxzv51lkCKJ5qYcra71eLTVvlFTaK7g82KXRJ6d42mGEJw7ZLCruei9V6k_YWyTMbv9EggdWvncukchj5sTuvhB8v6GER1Gv2TEj8zDqYIJlxqNVX7DcW6MoXp_eY-ohGlsid8CEIPo2U9momjPfKwhtpxzqyZQfa_csYyhgXAxPazypcaZ4jXPvAw7k"
          alt="Profile avatar"
          placeholder
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
</style>
