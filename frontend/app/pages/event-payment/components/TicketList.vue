<template>
  <div>
    <h5 class="mb-3 text-reactive-primary">Ticket Information</h5>

    <div v-if="tickets.length === 0" class="text-center py-5 text-reactive-secondary">
      <p>No tickets available</p>
    </div>

    <TicketAccordion
      v-for="ticket in tickets"
      :key="ticket.id"
      :ticket="ticket"
      @add="handleAddTicket"
    />
  </div>
</template>

<script setup lang="ts">
import type { Ticket } from '../types/ticket'
import TicketAccordion from './TicketAccordion.vue'

interface Props {
  tickets: Ticket[]
}

interface Emits {
  (e: 'addTicket', ticket: Ticket, quantity: number): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const handleAddTicket = (ticket: Ticket, quantity: number) => {
  emit('addTicket', ticket, quantity)
}
</script>