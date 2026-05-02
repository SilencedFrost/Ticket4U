import { defineStore } from 'pinia';

export type CheckoutEvent = {
  id: string;
  title: string;
  date: string;
  time: string;
  venue: string;
  bannerUrl?: {
    wide: string;
    square: string;
    tall: string;
  };
};

export type CheckoutCartItem = {
  zoneId: string;
  name: string;
  quantity: number;
  price: number;
  isStanding: boolean;
  seats?: Array<{
    seatId: string;
    seatName: string;
    seatUuid: string;
    zoneUuid?: string;
    zoneName: string;
    zoneColor: string;
    price: number;
  }>;
};

export type CheckoutSession = {
  eventId: string;
  sessionId: string;
  event: CheckoutEvent;
  cart: CheckoutCartItem[];
  totalPrice: number;
  totalTickets: number;
  orderId: string | null;
};

const STORAGE_KEY = 'ticket4u.checkout-session';

function readSession(): CheckoutSession | null {
  if (!import.meta.client) {
    return null;
  }

  const raw = sessionStorage.getItem(STORAGE_KEY);
  if (!raw) {
    return null;
  }

  try {
    return JSON.parse(raw) as CheckoutSession;
  } catch {
    sessionStorage.removeItem(STORAGE_KEY);
    return null;
  }
}

export const useCheckoutStore = defineStore('checkout', () => {
  const checkoutSession = ref<CheckoutSession | null>(null);

  function restoreCheckoutSession() {
    checkoutSession.value = readSession();
  }

  if (import.meta.client) {
    watch(
      checkoutSession,
      (value) => {
        if (value) {
          sessionStorage.setItem(STORAGE_KEY, JSON.stringify(value));
          return;
        }

        sessionStorage.removeItem(STORAGE_KEY);
      },
      { deep: true },
    );
  }

  const hasCheckoutSession = computed(() => checkoutSession.value !== null);

  function setCheckoutSession(session: Omit<CheckoutSession, 'orderId'>) {
    checkoutSession.value = {
      ...session,
      orderId: checkoutSession.value?.orderId ?? null,
    };
  }

  function setOrderId(orderId: string) {
    if (!checkoutSession.value) {
      return;
    }

    checkoutSession.value.orderId = orderId;
  }

  function clearCheckoutSession() {
    checkoutSession.value = null;
  }

  return {
    checkoutSession,
    hasCheckoutSession,
    restoreCheckoutSession,
    setCheckoutSession,
    setOrderId,
    clearCheckoutSession,
  };
});
