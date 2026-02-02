import { ref, computed } from 'vue'
import type { Ticket } from '../types/ticket'

export const useTickets = () => {
  const selectedTickets = ref<Ticket[]>([])
  
  const addTicket = (ticket: Ticket) => {
    if (!selectedTickets.value.find(t => t.id === ticket.id)) {
      selectedTickets.value.push(ticket)
    }
  }
  
  const removeTicket = (ticketId: string) => {
    selectedTickets.value = selectedTickets.value.filter(t => t.id !== ticketId)
  }
  
  const clearTickets = () => {
    selectedTickets.value = []
  }
  
  const totalPrice = computed(() => {
    return selectedTickets.value.reduce((sum, ticket) => sum + ticket.price, 0)
  })
  
  return {
    selectedTickets,
    addTicket,
    removeTicket,
    clearTickets,
    totalPrice
  }
}