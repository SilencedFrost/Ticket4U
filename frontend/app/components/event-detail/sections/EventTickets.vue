<script setup lang="ts">
defineProps<{
    eventData: any,
    eventSchedule: any,
    heroImage: string
}>();

const emit = defineEmits(['buyClick']); 

const expandedTickets = ref<{[key: string]: boolean}>({});

const toggleTicketDate = (scheduleId: string) => {
  expandedTickets.value[scheduleId] = !expandedTickets.value[scheduleId];
};
</script>

<template>
    <section id="tickets-section" class="card bg-reactive-secondary rounded-5 m-3 mx-auto mw-100">
        <div class="p-3 p-md-4">
            <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 h4">{{ $t('event-detail.information_tickets') }}</h3>
            
            <div v-for="(schedule, dateIdx) in eventSchedule" :key="dateIdx" class="mb-3">
                <div class="card bg-reactive-primary border-0 rounded-3 shadow-sm mb-3">
                    <button 
                        @click="toggleTicketDate(schedule.id)"
                        class="btn w-100 text-start p-3 p-md-4 d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center border-0 bg-transparent gap-3" 
                        type="button"
                    >
                        <div class="d-flex flex-column flex-md-row align-items-start align-items-md-center gap-2 flex-grow-1">
                            <i class="bi bi-calendar-event text-info fs-5"></i>
                            <span class="text-reactive-primary fw-semibold small">{{ schedule.time }}, {{ $d(new Date(schedule.date), 'long') }}</span>
                        </div>
                        <div class="d-flex align-items-center justify-content-between justify-content-md-end gap-2 gap-md-3 w-100 w-md-auto">
                            <button @click="emit('buyClick')" class="btn bg-primary fw-bold px-3 px-md-4 py-2 rounded-3 small">{{ $t('event-detail.buy_tickets') }}</button>
                            <i class="bi fs-4 text-reactive-primary flex-shrink-0" :class="expandedTickets[schedule.id] ? 'bi-chevron-up' : 'bi-chevron-down'"></i>
                        </div>
                    </button>
                </div>

                <div v-if="expandedTickets[schedule.id]" class="mt-3">
                    <h5 class="text-reactive-primary fw-bold mb-3 ms-2 small">{{ $t('event-detail.information_tickets') }}</h5>
                    
                    <div class="d-flex flex-column gap-2 gap-md-3">
                        <div v-for="(seat, seatIdx) in schedule.seatTypes" :key="seatIdx" 
                            class="card bg-reactive-primary border-0 rounded-3 shadow-sm overflow-hidden"
                        >
                            <div class="card-body p-3 p-md-4">
                                <div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2 gap-md-0">
                                    <div class="flex-grow-1">
                                        <h6 class="text-reactive-primary fw-bold mb-1 mb-md-2">{{ seat.name }}</h6>
                                        <p class="text-reactive-primary-50 mb-0 small"> {{ seat.available }} {{ $t('event-detail.seat_left') }}</p>
                                    </div>
                                    <div class="text-start text-md-end">
                                        <p class="text-primary fw-bold fs-5 mb-0">{{ seat.price }} đ</p>
                                    </div>
                                </div>
                                <div v-if="seat.name === 'SVIP'" class="mt-2 mt-md-3 pt-2 pt-md-3 border-top border-secondary">
                                    <p class="text-reactive-primary-50 mb-0 small">{{ eventData.svipBenefits }}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>