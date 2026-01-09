<script setup lang="ts">
import type { Showtime } from '@/types/event-detail';

defineProps<{
  showTime: Showtime[];
}>();

const emit = defineEmits(['buyClick']);

const expandedTickets = ref<{ [key: string]: boolean }>({});
const expandedSeatDetails = ref<{ [key: string]: boolean }>({});

const toggleTicketDate = (scheduleId: string) => {
  expandedTickets.value[scheduleId] = !expandedTickets.value[scheduleId];
};

const toggleSeatDetail = (scheduleId: string, seatName: string) => {
  const key = `${scheduleId}-${seatName}`;
  expandedSeatDetails.value[key] = !expandedSeatDetails.value[key];
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
              <button class="btn btn-primary fw-bold" @click.stop="emit('buyClick')">
                {{ $t('common.action.buy') }}
              </button>
              <i
                class="bi btn-link fs-4 text-reactive-primary flex-shrink-0"
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
                    <div
                      v-if="seat.available === 0"
                      class="badge rounded-pill bg-danger-subtle text-danger fw-bold px-3 py-2"
                    >
                      {{ $t('event_detail.label.sold_out') }}
                    </div>
                    <p v-else class="text-reactive-primary mb-0 small">
                      {{ seat.available }} {{ $t('event_detail.label.available') }}
                    </p>
                  </div>
                  <div
                    class="d-flex align-items-center gap-2 gap-md-3 text-start text-md-end flex-wrap justify-content-end"
                  >
                    <p class="text-primary fw-bold fs-5 mb-0">{{ seat.price }}</p>
                    <button
                      v-if="
                        seat.description ||
                        seat.image ||
                        (seat.benefits && seat.benefits.length > 0)
                      "
                      class="btn btn-link text-reactive-primary p-0"
                      @click="toggleSeatDetail(schedule.id, seat.name)"
                    >
                      <i
                        class="bi fs-5"
                        :class="
                          expandedSeatDetails[`${schedule.id}-${seat.name}`]
                            ? 'bi-chevron-up'
                            : 'bi-chevron-down'
                        "
                      />
                    </button>
                  </div>
                </div>
                <div
                  v-if="
                    expandedSeatDetails[`${schedule.id}-${seat.name}`] &&
                    (seat.description || seat.image || (seat.benefits && seat.benefits.length > 0))
                  "
                  class="mt-2 mt-md-3 pt-2 pt-md-3 border-top"
                >
                  <p v-if="seat.description" class="text-reactive-primary mb-3 small">
                    {{ seat.description }}
                  </p>
                  <div class="row g-3">
                    <div v-if="seat.image" class="col-12 col-md-4">
                      <img
                        :src="seat.image"
                        class="img-fluid rounded object-fit-cover w-100"
                        style="max-height: 150px"
                        alt="Ticket thumbnail"
                      />
                    </div>
                    <div
                      v-if="seat.benefits && seat.benefits.length > 0"
                      :class="seat.image ? 'col-12 col-md-8' : 'col-12'"
                    >
                      <ul class="list-unstyled mb-0">
                        <li
                          v-for="(benefit, index) in seat.benefits"
                          :key="index"
                          class="text-reactive-primary small mb-1"
                        >
                          <i class="bi bi-check-circle-fill text-success me-1" />
                          {{ benefit }}
                        </li>
                      </ul>
                    </div>
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
