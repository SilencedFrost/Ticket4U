<script setup lang="ts">
const scrollToSection = (id: string) => {
  const element = document.getElementById(id);
  element?.scrollIntoView({ behavior: 'smooth' });
};
</script>

<template>
  <div class="bg-reactive-primary shadow-sm">
    <div class="container-lg">
      <div
        class="d-flex gap-3 gap-md-4 flex-nowrap flex-md-wrap py-3 justify-content-md-center overflow-x-auto"
      >
        <button
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small text-nowrap flex-shrink-0"
          @click="scrollToSection('schedule-section')"
        >
          {{ $t('event_detail.section.schedule') }}
        </button>
        <button
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small text-nowrap flex-shrink-0"
          @click="scrollToSection('about-section')"
        >
          {{ $t('event_detail.section.about') }}
        </button>
        <button
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small text-nowrap flex-shrink-0"
          @click="scrollToSection('tickets-section')"
        >
          {{ $t('event_detail.section.ticket_info') }}
        </button>
        <button
          class="btn btn-link text-reactive-primary fw-semibold p-0 text-decoration-none small text-nowrap flex-shrink-0"
          @click="scrollToSection('organizer-section')"
        >
          {{ $t('event_detail.label.organizer') }}
        </button>
      </div>
    </div>
  </div>
</template>
