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
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          @click="scrollToSection('schedule-section')"
        >
          {{ $t('event_detail.section.schedule') }}
        </button>
        <button
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          @click="scrollToSection('about-section')"
        >
          {{ $t('event_detail.section.about') }}
        </button>
        <button
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          @click="scrollToSection('tickets-section')"
        >
          {{ $t('event_detail.section.ticket_info') }}
        </button>
        <button
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small"
          @click="scrollToSection('organizer-section')"
        >
          {{ $t('event_detail.label.organizer') }}
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
