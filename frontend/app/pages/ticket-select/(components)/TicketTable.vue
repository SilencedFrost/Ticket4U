<template>
  <div>
    <h5 class="mb-3">Giá vé</h5>

    <div class="table-responsive">
      <table class="table table-borderless">
        <tbody>
          <tr 
            v-for="ticket in tickets" 
            :key="ticket.id"
            class="menu-item-left"
            :class="{ 
              'border-primary': selectedTicketId === ticket.id,
              'opacity-50': !ticket.available 
            }"
            @click="handleSelectTicket(ticket)"
            style="cursor: pointer;"
          >
            <td class="py-3">
              <div class="d-flex align-items-center">
                <div 
                  class="rounded me-3" 
                  :style="{ 
                    backgroundColor: ticket.color,
                    width: '30px',
                    height: '30px'
                  }"
                ></div>
                <span>{{ ticket.name }}</span>
              </div>
            </td>
            <td class="py-3 text-end text-success fw-bold">
              {{ formatPrice(ticket.price) }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Ticket } from '../types/ticket'

interface Props {
  tickets: Ticket[]
  selectedTicketId: string | null
}

interface Emits {
  (e: 'select', ticket: Ticket): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const handleSelectTicket = (ticket: Ticket) => {
  if (ticket.available) {
    emit('select', ticket)
  }
}

const formatPrice = (price: number) => {
  return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
}
</script>