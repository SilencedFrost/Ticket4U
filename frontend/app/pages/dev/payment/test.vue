<script setup lang="ts">
interface SepayPaymentResponse {
  orderId: string;
  orderCode: string;
  amount: number;
  currency: string;
  bankCode: string;
  accountNumber: string;
  accountName: string;
  qrTemplate: string;
  qrUrl: string;
  paymentStatus: string;
  orderStatus: string;
  transactionId: string | null;
}

interface PaymentStatusResponse {
  orderId: string;
  paymentStatus: string;
  orderStatus: string;
  transactionId: string | null;
  totalAmount: number;
  currency: string;
}

const config = useRuntimeConfig();

const form = reactive({
  orderId: '',
});

const pollRate = ref<number>(3000);
const autoPoll = ref<boolean>(true);
const loading = ref<boolean>(false);
const statusLoading = ref<boolean>(false);
const errorMessage = ref<string>('');
const payment = ref<SepayPaymentResponse | null>(null);
const status = ref<PaymentStatusResponse | null>(null);
const orderIdPattern = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
const isOrderIdValid = computed(() => orderIdPattern.test(form.orderId.trim()));

let intervalId: NodeJS.Timeout | null = null;

const clearPolling = () => {
  if (intervalId) {
    clearInterval(intervalId);
    intervalId = null;
  }
};

const setupPolling = () => {
  clearPolling();

  if (!autoPoll.value || !isOrderIdValid.value) {
    return;
  }

  intervalId = setInterval(() => {
    fetchStatus(false);
  }, pollRate.value);
};

const createSepay = async () => {
  if (!isOrderIdValid.value || loading.value) {
    return;
  }

  loading.value = true;
  errorMessage.value = '';

  try {
    payment.value = await $fetch<SepayPaymentResponse>(
      `${config.public.paymentServiceUrl}/public/payments/sepay`,
      {
        method: 'POST',
        body: {
          orderId: form.orderId.trim(),
        },
      },
    );

    await fetchStatus(false);
    setupPolling();
  } catch {
    errorMessage.value = 'Unable to create SePay payment for this order.';
    payment.value = null;
  } finally {
    loading.value = false;
  }
};

const fetchStatus = async (showLoading = true) => {
  if (!isOrderIdValid.value || statusLoading.value) {
    return;
  }

  if (showLoading) {
    statusLoading.value = true;
  }

  try {
    status.value = await $fetch<PaymentStatusResponse>(
      `${config.public.paymentServiceUrl}/public/payments/orders/${form.orderId.trim()}/status`,
    );
    errorMessage.value = '';
  } catch {
    errorMessage.value = 'Unable to fetch order payment status.';
  } finally {
    if (showLoading) {
      statusLoading.value = false;
    }
  }
};

const resetPage = () => {
  clearPolling();
  form.orderId = '';
  payment.value = null;
  status.value = null;
  errorMessage.value = '';
};

watch([autoPoll, pollRate, () => form.orderId], setupPolling);

onUnmounted(() => {
  clearPolling();
});
</script>

<template>
  <div class="container py-3">
    <div class="card p-3">
      <h4 class="mb-3">Payment Service Test</h4>

      <div class="row g-2 mb-2">
        <div class="col-md-8">
          <input v-model="form.orderId" type="text" class="form-control" placeholder="Order UUID" />
        </div>
        <div class="col-md-4 d-grid">
          <button
            class="btn btn-primary"
            :disabled="!isOrderIdValid || loading"
            @click="createSepay"
          >
            {{ loading ? 'Creating...' : 'Create SePay' }}
          </button>
        </div>
      </div>

      <div class="row g-2 mb-3">
        <div class="col-md-4">
          <label class="form-label mb-1">Poll rate (ms)</label>
          <input v-model.number="pollRate" type="number" min="1000" class="form-control" />
        </div>
        <div class="col-md-4 d-flex align-items-end">
          <div class="form-check form-switch mb-1">
            <input v-model="autoPoll" class="form-check-input" type="checkbox" />
            <label class="form-check-label">Auto poll status</label>
          </div>
        </div>
        <div class="col-md-4 d-flex align-items-end gap-2">
          <button
            class="btn btn-outline-primary"
            :disabled="!isOrderIdValid"
            @click="fetchStatus()"
          >
            {{ statusLoading ? 'Checking...' : 'Check Status' }}
          </button>
          <button class="btn btn-outline-secondary" @click="resetPage">Reset</button>
        </div>
      </div>

      <div v-if="errorMessage" class="alert alert-danger mb-3">{{ errorMessage }}</div>

      <div v-if="payment" class="row g-3">
        <div class="col-md-5">
          <div class="border rounded p-2 bg-white text-center">
            <img :src="payment.qrUrl" alt="SePay QR" class="img-fluid" />
          </div>
        </div>
        <div class="col-md-7">
          <div class="border rounded p-3 h-100 bg-reactive-secondary">
            <p class="mb-1"><strong>Order Code:</strong> {{ payment.orderCode }}</p>
            <p class="mb-1"><strong>Amount:</strong> {{ payment.amount }} {{ payment.currency }}</p>
            <p class="mb-1"><strong>Bank:</strong> {{ payment.bankCode }}</p>
            <p class="mb-1"><strong>Account:</strong> {{ payment.accountNumber }}</p>
            <p class="mb-1"><strong>Name:</strong> {{ payment.accountName || 'N/A' }}</p>
            <p class="mb-1"><strong>Order Status:</strong> {{ payment.orderStatus }}</p>
            <p class="mb-0"><strong>Payment Status:</strong> {{ payment.paymentStatus }}</p>
          </div>
        </div>
      </div>

      <div v-if="status" class="border rounded p-3 mt-3 bg-reactive-secondary">
        <p class="mb-1"><strong>Order:</strong> {{ status.orderId }}</p>
        <p class="mb-1"><strong>Order Status:</strong> {{ status.orderStatus }}</p>
        <p class="mb-1"><strong>Payment Status:</strong> {{ status.paymentStatus }}</p>
        <p class="mb-1"><strong>Transaction ID:</strong> {{ status.transactionId || 'N/A' }}</p>
        <p class="mb-0"><strong>Total:</strong> {{ status.totalAmount }} {{ status.currency }}</p>
      </div>
    </div>
  </div>
</template>
