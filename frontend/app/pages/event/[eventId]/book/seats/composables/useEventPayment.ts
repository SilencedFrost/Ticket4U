import { ref, computed } from 'vue'
import type { CartItem } from '../(types)/eventPayment'
import type { SelectedSeat } from '../(types)/ticket'

export const useEventPayment = () => {
  const cart = ref<CartItem[]>([])

  const totalPrice = computed(() =>
    cart.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  )

  const totalTickets = computed(() =>
    cart.value.reduce((sum, item) => sum + item.quantity, 0)
  )

  function addToCart(
    zoneId:     string,
    zoneName:   string,
    quantity:   number,
    price:      number,
    isStanding: boolean,
    seats?:     SelectedSeat[]
  ) {
    if (isStanding) {
      const existing = cart.value.find(item => item.zoneId === zoneId && item.isStanding)
      if (existing) { existing.quantity += quantity }
      else { cart.value.push({ zoneId, name: zoneName, quantity, price, isStanding: true }) }
    } else {
      cart.value.push({ zoneId, name: zoneName, quantity, price, isStanding: false, seats: seats ?? [] })
    }
  }

  function removeFromCart(index: number) {
    cart.value.splice(index, 1)
  }

  function removeSeatFromCart(itemIndex: number, seatUuid: string) {
    const item = cart.value[itemIndex]
    if (!item?.seats) return
    const filtered = item.seats.filter(s => s.seatUuid !== seatUuid)
    if (filtered.length === 0) {
      // Remove the whole entry when no seats remain
      cart.value.splice(itemIndex, 1)
    } else {
      // Splice in place so Vue's reactivity detects the change reliably
      item.seats.splice(0, item.seats.length, ...filtered)
      item.quantity = filtered.length
    }
  }

  function formatPrice(price: number): string {
    return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
  }

  return { cart, totalPrice, totalTickets, addToCart, removeFromCart, removeSeatFromCart, formatPrice }
}
