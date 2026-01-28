<script setup lang="ts">
const scrollToSection = (id: string) => {
  const element = document.getElementById(id);
  element?.scrollIntoView({ behavior: 'smooth' });
};
</script>

<template>
  <div class="bg-reactive-primary shadow-sm">
    <div class="container-lg">
      <div class="d-flex gap-3 gap-md-4 flex-wrap py-3 justify-content-center">
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
