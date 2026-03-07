<script setup lang="ts">
import type { Showtime, Zone } from '@/pages/event-detail/types/event-detail';
import { useFormatter } from '@/composables/useFormatter';

const { formatPrice } = useFormatter();
const { locale } = useI18n();

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

const getZoneDescription = (zone: Zone) => {
  return locale.value === 'vi' ? zone.descriptionVi : zone.descriptionEn;
};

const hasZoneDetails = (zone: Zone) => {
  return (
    zone.descriptionVi ||
    zone.descriptionEn ||
    zone.giftImageUrl ||
    (zone.perks && zone.perks.length > 0)
  );
};
</script>

<template>
  <section id="tickets-section" class="card bg-reactive-secondary m-3 mx-auto mw-100">
    <div class="p-3 p-md-4 mx-3">
      <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 h4">
        {{ $t('event_detail.section.ticket_info') }}
      </h3>

      <div v-for="(schedule, dateIdx) in showTime" :key="dateIdx" class="mb-3">
        <div class="card-border mb-3">
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
              v-for="(zone, zoneIdx) in schedule.zones"
              :key="zoneIdx"
              class="card-border overflow-hidden"
            >
              <div class="p-3 p-md-4">
                <div
                  class="d-flex flex-row flex-md-row justify-content-between align-items-start align-items-md-center gap-2 gap-md-0"
                >
                  <div
                    class="d-flex flex-column flex-md-row flex-grow-1 gap-2 gap-md-3 align-items-start align-items-md-center w-100"
                  >
                    <div class="order-2 order-md-1 flex-grow-1 flex-shrink-1">
                      <h6 class="text-reactive-primary fw-bold mb-1 mb-md-2 fs-6">
                        {{ zone.name }}
                      </h6>
                      <div
                        v-if="zone.available === 0"
                        class="badge rounded-pill bg-danger-subtle text-danger fw-bold px-3 py-2 small"
                      >
                        {{ $t('event_detail.label.sold_out') }}
                      </div>
                      <p v-else class="text-reactive-primary mb-0 small">
                        {{ zone.available }} {{ $t('event_detail.label.available') }}
                      </p>
                    </div>
                    <div class="order-1 order-md-2 flex-shrink-0">
                      <p class="text-primary fw-bold fs-6 fs-md-5 mb-0 text-nowrap">
                        {{ formatPrice(zone.price) }}
                      </p>
                    </div>
                  </div>
                  <div class="flex-shrink-0">
                    <button
                      v-if="hasZoneDetails(zone)"
                      class="btn btn-link text-reactive-primary p-0"
                      @click="toggleSeatDetail(schedule.id, zone.name)"
                    >
                      <i
                        class="bi fs-5"
                        :class="
                          expandedSeatDetails[`${schedule.id}-${zone.name}`]
                            ? 'bi-chevron-up'
                            : 'bi-chevron-down'
                        "
                      />
                    </button>
                  </div>
                </div>
                <div
                  v-if="expandedSeatDetails[`${schedule.id}-${zone.name}`] && hasZoneDetails(zone)"
                  class="mt-2 mt-md-3 pt-2 pt-md-3 border-top"
                >
                  <p v-if="getZoneDescription(zone)" class="text-reactive-primary mb-3 small">
                    {{ getZoneDescription(zone) }}
                  </p>
                  <div class="row g-3">
                    <div v-if="zone.giftImageUrl" class="col-12 col-md-4">
                      <img
                        :src="zone.giftImageUrl"
                        class="img-fluid rounded object-fit-cover w-100"
                        style="max-height: 150px"
                        alt="Ticket thumbnail"
                      />
                    </div>
                    <div
                      v-if="zone.perks && zone.perks.length > 0"
                      :class="zone.giftImageUrl ? 'col-12 col-md-8' : 'col-12'"
                    >
                      <ul class="list-unstyled mb-0">
                        <li
                          v-for="(perk, index) in zone.perks"
                          :key="index"
                          class="text-reactive-primary small mb-1"
                        >
                          <i class="bi bi-check-circle-fill text-success me-1" />
                          {{ perk }}
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
