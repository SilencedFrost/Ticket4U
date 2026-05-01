<script setup lang="ts">
const route = useRoute();
const checkoutStore = useCheckoutStore();
const { formatPrice } = useFormatter();

const orderId = computed(() => String(route.query.orderId ?? ''));
const transactionId = computed(() => String(route.query.transactionId ?? ''));
const orderCode = computed(() => String(route.query.orderCode ?? ''));
const currency = computed(() => {
  const value = String(route.query.currency ?? 'VND');
  return value === 'USD' ? 'USD' : 'VND';
});
const amount = computed(() => {
  const rawValue = Number(route.query.amount ?? 0);
  return Number.isFinite(rawValue) ? rawValue : 0;
});

onMounted(() => {
  checkoutStore.clearCheckoutSession();
});
</script>

<template>
  <div class="payment-success-page d-flex align-items-center justify-content-center px-3">
    <div class="success-glow"></div>

    <div class="success-card text-center">
      <div class="success-icon-wrap">
        <i class="bi bi-check-lg success-icon"></i>
      </div>

      <p class="eyebrow mb-2">Thanh toán thành công</p>
      <h1 class="title mb-3">Giao dịch của bạn đã được xác nhận</h1>
      <p class="subtitle mb-4">
        Hệ thống đã ghi nhận trạng thái <strong>PAID</strong> và hoàn tất xác nhận đơn hàng.
      </p>

      <div class="info-grid text-start">
        <div class="info-row">
          <span>Order ID</span>
          <strong>{{ orderId || 'N/A' }}</strong>
        </div>
        <div class="info-row" v-if="orderCode">
          <span>Order Code</span>
          <strong>{{ orderCode }}</strong>
        </div>
        <div class="info-row">
          <span>Transaction ID</span>
          <strong>{{ transactionId || 'N/A' }}</strong>
        </div>
        <div class="info-row">
          <span>Số tiền</span>
          <strong>{{ formatPrice(amount, currency) }}</strong>
        </div>
      </div>

      <div class="action-row d-flex flex-column flex-sm-row gap-2 justify-content-center mt-4">
        <NuxtLink class="btn btn-light fw-semibold" to="/">Về trang chủ</NuxtLink>
        <NuxtLink class="btn btn-outline-light fw-semibold" to="/payment"
          >Tạo thanh toán mới</NuxtLink
        >
      </div>
    </div>
  </div>
</template>

<style scoped>
.payment-success-page {
  min-height: 100dvh;
  position: relative;
  overflow: hidden;
  background:
    radial-gradient(60% 42% at 50% 0%, rgba(34, 197, 94, 0.26) 0%, rgba(0, 0, 0, 0) 72%),
    linear-gradient(180deg, #07130e 0%, #050807 100%);
  color: #f8fff9;
}

.success-glow {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(22% 20% at 20% 18%, rgba(59, 130, 246, 0.18), transparent 70%),
    radial-gradient(18% 18% at 82% 20%, rgba(34, 197, 94, 0.22), transparent 72%),
    radial-gradient(18% 18% at 50% 82%, rgba(16, 185, 129, 0.12), transparent 72%);
  pointer-events: none;
}

.success-card {
  position: relative;
  z-index: 1;
  width: min(720px, 100%);
  padding: 2rem;
  border-radius: 24px;
  background: rgba(8, 16, 11, 0.88);
  border: 1px solid rgba(148, 163, 184, 0.22);
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.45);
  backdrop-filter: blur(16px);
}

.success-icon-wrap {
  width: 88px;
  height: 88px;
  margin: 0 auto 1rem;
  border-radius: 50%;
  display: grid;
  place-items: center;
  background: linear-gradient(180deg, rgba(34, 197, 94, 0.3), rgba(16, 185, 129, 0.16));
  border: 1px solid rgba(74, 222, 128, 0.3);
}

.success-icon {
  font-size: 2.8rem;
  line-height: 1;
  color: #86efac;
}

.eyebrow {
  text-transform: uppercase;
  letter-spacing: 0.24em;
  color: #86efac;
  font-size: 0.78rem;
  font-weight: 700;
}

.title {
  font-size: clamp(1.8rem, 4vw, 3rem);
  line-height: 1.1;
  font-weight: 800;
}

.subtitle {
  color: rgba(226, 232, 240, 0.86);
  margin-bottom: 0;
}

.info-grid {
  display: grid;
  gap: 0.75rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  padding: 0.9rem 1rem;
  border-radius: 16px;
  background: rgba(15, 23, 18, 0.92);
  border: 1px solid rgba(148, 163, 184, 0.14);
}

.info-row span {
  color: rgba(226, 232, 240, 0.74);
}

.info-row strong {
  text-align: right;
  word-break: break-word;
}

@media (max-width: 575.98px) {
  .success-card {
    padding: 1.25rem;
    border-radius: 20px;
  }

  .info-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .info-row strong {
    text-align: left;
  }
}
</style>
