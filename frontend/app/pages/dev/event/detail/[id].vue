<script setup lang="ts">
import type { Event } from '@/features/event/types/Event';
import type { OrganizerSummary } from '~/features/Organizer';
import EventHero from '../../../../features/event/components/detail/EventHero.vue';
import EventNav from '../../../../features/event/components/detail/EventNav.vue';
import EventAbout from '../../../../features/event/components/detail/EventAbout.vue';
import EventSchedule from '../../../../features/event/components/detail/EventSchedule.vue';
import EventOrganizer from '../../../../features/event/components/detail/EventOrganizer.vue';
import EventRelated from '../../../../features/event/components/detail/EventRelated.vue';
const config = useRuntimeConfig();
const route = useRoute();

const event = ref<Event | null>(null);
const organizer = ref<OrganizerSummary | null>(null);
const isLoading = ref(true);

async function getEvent() {
  isLoading.value = true;

  try {
    event.value = await $fetch(
      `${config.public.eventServiceUrl}/public/events/${route.params.id}`,
      {
        method: 'GET',
      },
    );

    if (event.value?.organizerId) {
      fetchOrganizer(event.value.organizerId);
    }
  } catch (e) {
    console.log(e);
    event.value = null;
    organizer.value = null;
  } finally {
    isLoading.value = false;
  }
}

async function fetchOrganizer(id: string) {
  try {
    organizer.value = await $fetch<OrganizerSummary>(
      `${config.public.userServiceUrl}/public/organizers/${id}`,
    );
  } catch (err) {
    console.warn(err);
    organizer.value = null;
  }
}

onMounted(() => getEvent());
</script>

<template>
  <div v-if="isLoading" class="h-auto overflow-x-hidden mw-100">
    <section class="pt-4" role="status" aria-live="polite" aria-label="Đang tải dữ liệu sự kiện">
      <div class="container-xxl">
        <div class="card border-0 shadow-sm overflow-hidden">
          <div class="row g-0">
            <div class="col-12 col-lg-4 p-4 placeholder-glow">
              <span class="placeholder col-8 d-block mb-2"></span>
              <span class="placeholder col-6 d-block mb-2"></span>
              <span class="placeholder col-10 d-block mb-4"></span>
              <hr class="my-3" />
              <span class="placeholder col-5 d-block mb-2"></span>
              <span class="placeholder col-7 d-block mb-4"></span>
              <span class="placeholder col-12 d-block py-2 rounded-2"></span>
            </div>

            <div class="col-12 col-lg-8 bg-secondary bg-opacity-25">
              <div class="ratio ratio-1x1 d-lg-none"></div>
              <div class="ratio ratio-16x9 d-none d-lg-block"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="container-xxl py-4">
      <div class="placeholder-glow d-flex flex-wrap gap-2 mb-4">
        <span class="placeholder col-3"></span>
        <span class="placeholder col-3"></span>
        <span class="placeholder col-2"></span>
        <span class="placeholder col-2"></span>
      </div>

      <div class="card border-0 shadow-sm mb-3">
        <div class="card-body p-4 placeholder-glow">
          <span class="placeholder col-3 d-block mb-3"></span>
          <span class="placeholder col-12 d-block mb-2"></span>
          <span class="placeholder col-12 d-block mb-2"></span>
          <span class="placeholder col-11 d-block mb-2"></span>
          <span class="placeholder col-10 d-block"></span>
        </div>
      </div>

      <div class="card border-0 shadow-sm">
        <div class="card-body p-4 placeholder-glow">
          <span class="placeholder col-4 d-block mb-3"></span>
          <span class="placeholder col-12 d-block mb-2"></span>
          <span class="placeholder col-12 d-block mb-2"></span>
          <span class="placeholder col-8 d-block"></span>
        </div>
      </div>
    </section>
  </div>

  <div v-else-if="event" class="h-auto overflow-x-hidden mw-100">
    <event-hero :event="event" />
    <event-nav />
    <div class="rowz m-0 container-xxl mx-auto flex-column flex-lg-row">
      <div class="col-lg-12">
        <event-schedule :event="event" />
        <event-about :about-vi="event.aboutVi" :about-en="event.aboutEn" />
        <!-- To do: Add event ticket section here -->
        <event-organizer v-if="organizer" :organizer-data="organizer" />
      </div>
    </div>
    <event-related />
  </div>
  <div v-else class="text-center text-reactive-secondary pt-5">
    <i class="bi bi-box2-fill" style="font-size: 80px" />
    <h2>Nothing here</h2>
  </div>
</template>
