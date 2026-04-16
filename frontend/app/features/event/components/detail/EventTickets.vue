<script setup lang="ts">
import type { EventSessionSummary } from '@/features/event/types/EventSession';
import type { Zone } from '@/features/event/types/Zone';
import { useFormatter } from '@/composables/useFormatter';
import ShimmerImg from '~/components/ShimmerImg.vue';

const { formatPrice } = useFormatter();
const { locale } = useI18n();

defineProps<{
  sessions: EventSessionSummary[];
}>();

const emit = defineEmits(['buyClick']);

const expandedTickets = ref<{ [key: string]: boolean }>({});
const expandedSeatDetails = ref<{ [key: string]: boolean }>({});

function toggleTicketDate(scheduleId: string) {
  expandedTickets.value[scheduleId] = !expandedTickets.value[scheduleId];
}

function toggleSeatDetail(scheduleId: string, zoneId: string) {
  const key = `${scheduleId}-${zoneId}`;
  expandedSeatDetails.value[key] = !expandedSeatDetails.value[key];
}

function sortZonesByPriceDesc(zones: Zone[]) {
  return [...zones].sort((a, b) => {
    if (b.price !== a.price) {
      return b.price - a.price;
    }

    return a.name.localeCompare(b.name, undefined, { numeric: true });
  });
}

function getZoneDescription(zone: Zone) {
  return locale.value === 'vi' ? zone.descriptionVi : zone.descriptionEn;
}

function hasZoneDetails(zone: Zone) {
  return (
    zone.descriptionVi ||
    zone.descriptionEn ||
    zone.giftImageUrl ||
    (zone.perks && zone.perks.length > 0)
  );
}
</script>

<template>
  <section id="tickets-section" class="card m-3 mx-auto overflow-hidden mw-100 pb-4">
    <div class="p-3 p-md-4 mb-3">
      <h5 class="text-primary fw-bold mb-0 pb-2 border-bottom">
        {{ $t('event_detail.section.ticket_info') }}
      </h5>

      <div v-for="schedule in sessions" :key="schedule.id" class="mt-3">
        <div class="card-border mb-3">
          <button
            class="btn w-100 text-start p-3 p-md-3 d-flex flex-row justify-content-between align-items-center border-0 bg-transparent gap-2"
            type="button"
            @click="toggleTicketDate(schedule.id)"
          >
            <div class="d-flex flex-column align-items-start gap-1 flex-grow-1">
              <div class="d-flex flex-column">
                <span class="text-reactive-primary fw-semibold small mb-0">
                  {{
                    $d(new Date(schedule.startDate), {
                      hour: '2-digit',
                      minute: '2-digit',
                      weekday: 'short',
                    })
                  }}
                </span>
                <span class="text-primary fw-bold small mb-0 d-flex align-items-center gap-1">
                  {{
                    $d(new Date(schedule.startDate), {
                      day: '2-digit',
                      month: 'long',
                      year: 'numeric',
                    })
                  }}
                </span>
              </div>
            </div>
            <div class="d-flex align-items-center justify-content-end gap-2 flex-shrink-0">
              <button
                class="btn btn-primary fw-bold small py-1 px-2 py-md-2"
                @click.stop="emit('buyClick')"
              >
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
              v-for="zone in sortZonesByPriceDesc(schedule.zones)"
              :key="zone.id"
              class="card-border overflow-hidden"
            >
              <div class="p-3 p-md-3">
                <div
                  class="d-flex flex-row flex-md-row justify-content-between align-items-center gap-2 gap-md-0"
                >
                  <div
                    class="d-flex flex-row justify-content-between align-items-center w-100 gap-2"
                  >
                    <div class="flex-grow-1 flex-shrink-1 pe-2">
                      <h6 class="text-reactive-primary fw-bold mb-1 mb-md-2 small fs-md-6">
                        {{ zone.name }}
                      </h6>
                      <div
                        v-if="zone.capacity === null"
                        class="badge rounded-pill bg-danger-subtle text-danger fw-bold px-3 py-2 small"
                      >
                        {{ $t('event.status_options.sold_out') }}
                      </div>
                      <p v-else class="text-reactive-primary mb-0 small">
                        {{ zone.capacity }} {{ $t('common.status.available') }}
                      </p>
                    </div>
                    <div
                      class="flex-shrink-0 mx-1 d-flex align-items-center justify-content-center justify-content-md-end text-center text-md-end"
                    >
                      <p class="text-primary fw-bold mb-0 text-nowrap small fs-md-5">
                        {{ formatPrice(zone.price) }}
                      </p>
                    </div>
                  </div>
                  <div class="flex-shrink-0">
                    <button
                      v-if="hasZoneDetails(zone)"
                      class="btn btn-link text-reactive-primary p-0"
                      aria-label="Toggle seat details"
                      @click="toggleSeatDetail(schedule.id, zone.id)"
                    >
                      <i
                        class="bi fs-5"
                        :class="
                          expandedSeatDetails[`${schedule.id}-${zone.id}`]
                            ? 'bi-chevron-up'
                            : 'bi-chevron-down'
                        "
                      />
                    </button>
                  </div>
                </div>
                <div
                  v-if="expandedSeatDetails[`${schedule.id}-${zone.id}`] && hasZoneDetails(zone)"
                  class="mt-2 mt-md-3 pt-2 pt-md-3 border-top"
                >
                  <p v-if="getZoneDescription(zone)" class="text-reactive-primary mb-3 small">
                    {{ getZoneDescription(zone) }}
                  </p>
                  <div class="row g-3">
                    <div v-if="zone.giftImageUrl" class="col-12 col-md-4">
                      <shimmer-img
                        :src="zone.giftImageUrl"
                        class="img-fluid rounded object-fit-cover w-100"
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
