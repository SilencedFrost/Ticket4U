import { ref, computed } from 'vue'
import type { CartItem } from '../(types)/event-payment.type'

export const useEventPayment = () => {
  const cart = ref<CartItem[]>([])

  const totalPrice = computed(() => {
    return cart.value.reduce((sum, item) => {
      return sum + (item.price * item.quantity)
    }, 0)
  })

  const totalTickets = computed(() => {
    return cart.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  const addToCart = (
    zoneId: string, 
    zoneName: string, 
    quantity: number, 
    price: number
  ) => {
    const existingItem = cart.value.find(item => item.zoneId === zoneId)
    
    if (existingItem) {
      existingItem.quantity += quantity
    } else {
      cart.value.push({
        zoneId,
        name: zoneName,
        quantity,
        price
      })
    }
  }

  const removeFromCart = (index: number) => {
    cart.value.splice(index, 1)
  }

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('vi-VN').format(price) + ' đ'
  }

  return {
    cart,
    totalPrice,
    totalTickets,
    addToCart,
    removeFromCart,
    formatPrice
  }
}