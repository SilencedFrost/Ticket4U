<template>
  <div class="d-flex flex-column align-items-center text-center">
    <div class="position-relative mb-4">
      <div
        class="avatar-container rounded-circle p-1 border border-primary border-opacity-25 border-4"
      >
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
    <p class="text-reactive-secondary small mb-3">{{ $t('settings.personalInformation.memberSince') }} {{ $d(new Date('2023-12-01'), 'short') }}</p>
    <div class="d-flex gap-2">
      <span
        class="badge"
        style="
          color: #af88ff;
          background-color: rgba(175, 136, 255, 0.1);
          border: 1px solid rgba(175, 136, 255, 0.2);
          border-radius: 9999px;
          padding: 0.4rem 1rem;
          text-transform: uppercase;
          font-weight: bold;
          letter-spacing: 0.05em;
        "
      >
        Hạng Vàng
      </span>
    </div>
  </div>
</template>

<script setup>
// <!-- TODO: wire up script -->
</script>

<style scoped>
.avatar-container {
  width: 160px;
  height: 160px;
  transition: border-color 0.3s ease;
}

.avatar-container:hover {
  border-color: var(--bs-primary) !important;
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
