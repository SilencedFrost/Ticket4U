<script setup lang="ts">
import type { Showtime } from '@/types/event-detail';
defineProps<{
  showTime: Showtime[];
}>();

const emit = defineEmits(['buyClick']);

const expandedTickets = ref<{ [key: string]: boolean }>({});

const toggleTicketDate = (scheduleId: string) => {
  expandedTickets.value[scheduleId] = !expandedTickets.value[scheduleId];
};
</script>

<template>
  <section id="tickets-section" class="card bg-reactive-secondary m-3 mx-auto mw-100">
    <div class="p-3 p-md-4 mx-3">
      <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 h4">
        {{ $t('event_detail.section.ticket_info') }}
      </h3>

      <div v-for="(schedule, dateIdx) in showTime" :key="dateIdx" class="mb-3">
        <div class="bg-reactive-primary rounded mb-3">
          <button
            class="btn w-100 text-start p-3 p-md-4 d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center border-0 bg-transparent gap-3"
            type="button"
            @click="toggleTicketDate(schedule.id)"
          >
            <div
              class="d-flex flex-column flex-md-row align-items-start align-items-md-center gap-2 flex-grow-1"
            >
              <i class="bi bi-calendar-event text-info fs-5" />
              <span class="text-reactive-primary fw-semibold small"
                >{{ schedule.time }}, {{ $d(new Date(schedule.date), 'long') }}</span
              >
            </div>
            <div
              class="d-flex align-items-center justify-content-between justify-content-md-end gap-2 gap-md-3 w-100 w-md-auto"
            >
              <button class="btn btn-primary fw-bold" @click="emit('buyClick')">
                {{ $t('common.action.buy') }}
              </button>
              <i
                class="bi fs-4 text-reactive-primary flex-shrink-0"
                :class="expandedTickets[schedule.id] ? 'bi-chevron-up' : 'bi-chevron-down'"
              />
            </div>
          </button>
        </div>

        <div v-if="expandedTickets[schedule.id]" class="mt-3">
          <h5 class="text-reactive-primary fw-bold mb-3 ms-2 small">
            {{ $t('event_detail.section.ticket_info') }}
          </h5>

          <div class="d-flex flex-column gap-2 gap-md-3">
            <div
              v-for="(seat, seatIdx) in schedule.seatTypes"
              :key="seatIdx"
              class="bg-reactive-primary rounded overflow-hidden"
            >
              <div class="card-body p-3 p-md-4">
                <div
                  class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2 gap-md-0"
                >
                  <div class="flex-grow-1">
                    <h6 class="text-reactive-primary fw-bold mb-1 mb-md-2">{{ seat.name }}</h6>
                    <p class="text-reactive-primary-50 mb-0 small">
                      {{ seat.available }} {{ $t('event_detail.label.available') }}
                    </p>
                  </div>
                  <div class="text-start text-md-end">
                    <p class="text-primary fw-bold fs-5 mb-0">{{ seat.price }} đ</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
