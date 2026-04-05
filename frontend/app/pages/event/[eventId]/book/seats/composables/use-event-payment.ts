import { ref, computed } from 'vue'
import type { CartItem } from '../(types)/event-payment'
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
      const existing = cart.value.find(item => item.zoneId === zoneId && !item.isStanding)
      if (existing) {
        existing.quantity += quantity
        existing.seats = [...(existing.seats ?? []), ...(seats ?? [])]
      } else {
        cart.value.push({ zoneId, name: zoneName, quantity, price, isStanding: false, seats: seats ?? [] })
      }
    }
  }

  function removeFromCart(index: number) {
    cart.value.splice(index, 1)
  }

  function formatPrice(price: number): string {
    return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
  }

  return { cart, totalPrice, totalTickets, addToCart, removeFromCart, formatPrice }
}