<script setup lang="ts">
import TicketCard from './(components)/TicketCard.vue'
import { useMyTickets } from './composables/useMyTickets'

const { tickets, loading, activeTickets, pastTickets, cancelledTickets } = useMyTickets()

type FilterTab = 'all' | 'active' | 'past' | 'cancelled'
const activeTab = ref<FilterTab>('all')

const displayedTickets = computed(() => {
  switch (activeTab.value) {
    case 'active': return activeTickets.value
    case 'past': return pastTickets.value
    case 'cancelled': return cancelledTickets.value
    default: return tickets.value
  }
})

const tabs: { key: FilterTab; label: string; count: ComputedRef<number> }[] = [
  { key: 'all', label: 'All', count: computed(() => tickets.value.length) },
  { key: 'active', label: 'Upcoming', count: computed(() => activeTickets.value.length) },
  { key: 'past', label: 'Attended', count: computed(() => pastTickets.value.length) },
  { key: 'cancelled', label: 'Cancelled', count: computed(() => cancelledTickets.value.length) },
]
</script>

<template>
  <div class="container py-4 py-md-5">
    <!-- Header -->
    <div class="mb-4">
      <h3 class="fw-bold text-reactive-primary mb-1">
        <i class="bi bi-ticket-perforated me-2" />My Tickets
      </h3>
      <p class="text-reactive-secondary small mb-0">
        View all tickets you have purchased.
      </p>
    </div>

    <!-- Filter tabs -->
    <ul class="nav nav-pills mb-4 gap-1 flex-nowrap overflow-x-auto pb-1">
      <li v-for="tab in tabs" :key="tab.key" class="nav-item flex-shrink-0">
        <button
          :class="['nav-link', 'px-3', 'py-1', { active: activeTab === tab.key }]"
          @click="activeTab = tab.key"
        >
          {{ tab.label }}
          <span
            :class="[
              'badge',
              'ms-1',
              activeTab === tab.key ? 'text-bg-light text-primary' : 'text-bg-secondary',
            ]"
          >
            {{ tab.count.value }}
          </span>
        </button>
      </li>
    </ul>

    <!-- Loading state -->
    <div v-if="loading" class="d-flex justify-content-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <!-- Empty state -->
    <div
      v-else-if="displayedTickets.length === 0"
      class="d-flex flex-column align-items-center justify-content-center py-5 text-reactive-secondary"
    >
      <i class="bi bi-ticket mb-3" style="font-size: 3rem; opacity: 0.4" />
      <p class="mb-0 fw-medium">No tickets found</p>
      <small class="opacity-75">Tickets you purchase will appear here.</small>
    </div>

    <!-- Ticket list -->
    <div v-else class="d-flex flex-column gap-3">
      <ticket-card
        v-for="ticket in displayedTickets"
        :key="ticket.id"
        :ticket="ticket"
      />
    </div>
  </div>
</template>
