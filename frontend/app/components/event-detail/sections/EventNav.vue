<script setup lang="ts">
    const scrollToSection = (id: string) => {
        const element = document.getElementById(id);
        element?.scrollIntoView({ behavior: 'smooth' });
    };
</script>

<template>
    <div class="bg-reactive-primary border-bottom border-dark overflow-x-auto mx-4 z-2 nav-sticky">
      <div class="container-xxl">
        <div class="d-flex gap-3 gap-md-4 flex-wrap py-3 nav-tabs-wrapper">
          <button
            @click="scrollToSection('schedule-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('event-detail.schedule_event') }}
          </button>
          <button
            @click="scrollToSection('about-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('event-detail.about_event') }}
          </button>
          <button
            @click="scrollToSection('tickets-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('event-detail.information_tickets') }}
          </button>
          <button
            @click="scrollToSection('organizer-section')"
            class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          >
            {{ $t('event-detail.organizer') }}
          </button>
        </div>
      </div>
    </div>
</template>

<style scoped>
@media (max-width: 768px) {
  .nav-sticky {
    scrollbar-width: none;
  }

  .nav-sticky::-webkit-scrollbar {
    display: none;
  }

  .nav-tabs-wrapper {
    min-width: max-content;
  }
}
</style>