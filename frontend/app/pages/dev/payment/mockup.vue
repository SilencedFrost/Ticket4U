<script setup lang="ts">
const fullName = ref('Hùng La Ngọc');
const email = ref('langochungdev@gmail.com');
const phone = ref('0901234567');
const promoCode = ref('');
const agreedPolicy = ref(false);
const secondsLeft = ref(567);
const total = 1050000;
const showSepayPopup = ref(false);
const editingField = ref<'name' | 'email' | 'phone' | null>(null);
const shouldCloseOnPointerUp = ref(false);

const tickets = ref([
  { type: 'GA', zone: '-', row: 'C', seat: '10' },
  { type: 'GA', zone: '-', row: 'C', seat: '11' },
  { type: 'GA', zone: '-', row: 'C', seat: '12' },
]);

const countdownLabel = computed(() => {
  const safe = Math.max(0, secondsLeft.value);
  const m = Math.floor(safe / 60)
    .toString()
    .padStart(2, '0');
  const s = (safe % 60).toString().padStart(2, '0');
  return `${m}:${s}`;
});

const minuteBox = computed(() => countdownLabel.value.split(':')[0] ?? '00');
const secondBox = computed(() => countdownLabel.value.split(':')[1] ?? '00');

const canPay = computed(() => {
  const hasContact =
    fullName.value.trim().length > 0 &&
    email.value.trim().length > 0 &&
    phone.value.trim().length > 0;
  return hasContact && agreedPolicy.value;
});

const errorHint = computed(() => (canPay.value ? '' : 'Vui lòng điền đầy đủ thông tin bắt buộc'));

let countdownTimer: ReturnType<typeof setInterval> | null = null;

function toggleEditing(field: 'name' | 'email' | 'phone') {
  editingField.value = editingField.value === field ? null : field;
}

function finishEditing() {
  editingField.value = null;
}

function openPaymentPopup() {
  if (!canPay.value) return;
  showSepayPopup.value = true;
}

function closePaymentPopup() {
  showSepayPopup.value = false;
}

function confirmPaid() {
  showSepayPopup.value = false;
}

function handleOverlayPointerDown(event: PointerEvent) {
  shouldCloseOnPointerUp.value = event.target === event.currentTarget;
}

function handleOverlayPointerUp(event: PointerEvent) {
  if (shouldCloseOnPointerUp.value && event.target === event.currentTarget) {
    closePaymentPopup();
  }
  shouldCloseOnPointerUp.value = false;
}

function handleEscape(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    closePaymentPopup();
  }
}

onMounted(() => {
  countdownTimer = setInterval(() => {
    if (secondsLeft.value <= 0) return;
    secondsLeft.value -= 1;
  }, 1000);

  window.addEventListener('keydown', handleEscape);
});

onBeforeUnmount(() => {
  if (countdownTimer) clearInterval(countdownTimer);
  window.removeEventListener('keydown', handleEscape);
});
</script>

<template>
  <div class="flip-payment-page text-reactive-primary">
    <div class="surface-glow"></div>

    <div class="shell-wrap px-2 px-lg-0">
      <div class="payment-shell">
        <main class="payment-main">
          <section class="left-col">
            <article class="panel event-panel">
              <img
                class="event-thumb"
                src="https://images.unsplash.com/photo-1460723237483-7a6dc9d0b212?w=280&h=180&fit=crop"
                alt="Ảnh sự kiện"
              />
              <div class="event-meta">
                <h1 class="event-title">Múa rối nước truyền thống</h1>
                <div class="event-subline">
                  <span><i class="bi bi-calendar3"></i> 14, 08/04</span>
                  <span><i class="bi bi-clock"></i> 18:30 - 19:15</span>
                </div>
                <p class="event-address">55B Nguyễn Thị Minh Khai, P. Bến Thành, Q.1, TP.HCM</p>
              </div>
            </article>

            <article class="panel receiver-panel">
              <h2 class="panel-title">Thông tin nhận vé</h2>
              <p class="panel-subtitle">Vé của bạn sẽ được gửi tới thông tin bên dưới</p>
              <hr class="panel-divider" />

              <div class="receiver-row">
                <i class="bi bi-person"></i>
                <div class="receiver-col">
                  <span class="receiver-label">Họ và tên <b>*</b></span>
                  <input
                    v-if="editingField === 'name'"
                    v-model="fullName"
                    class="inline-edit-input"
                    type="text"
                    @blur="finishEditing"
                    @keyup.enter="finishEditing"
                  />
                  <strong v-else>{{ fullName }}</strong>
                </div>
                <button type="button" class="icon-button" @click="toggleEditing('name')">
                  <i :class="editingField === 'name' ? 'bi bi-check-lg' : 'bi bi-pencil'"></i>
                </button>
              </div>

              <div class="receiver-row">
                <i class="bi bi-envelope"></i>
                <div class="receiver-col">
                  <span class="receiver-label">Email <b>*</b></span>
                  <input
                    v-if="editingField === 'email'"
                    v-model="email"
                    class="inline-edit-input"
                    type="email"
                    @blur="finishEditing"
                    @keyup.enter="finishEditing"
                  />
                  <strong v-else>{{ email }}</strong>
                </div>
                <button type="button" class="icon-button" @click="toggleEditing('email')">
                  <i :class="editingField === 'email' ? 'bi bi-check-lg' : 'bi bi-pencil'"></i>
                </button>
              </div>

              <div class="receiver-row">
                <i class="bi bi-telephone"></i>
                <div class="receiver-col">
                  <span class="receiver-label">Số điện thoại <b>*</b></span>
                  <input
                    v-if="editingField === 'phone'"
                    v-model="phone"
                    class="inline-edit-input"
                    type="tel"
                    @blur="finishEditing"
                    @keyup.enter="finishEditing"
                  />
                  <strong v-else>{{ phone }}</strong>
                </div>
                <button type="button" class="icon-button" @click="toggleEditing('phone')">
                  <i :class="editingField === 'phone' ? 'bi bi-check-lg' : 'bi bi-pencil'"></i>
                </button>
              </div>

              <hr class="panel-divider" />

              <p class="ticket-note">
                <i class="bi bi-qr-code"></i>
                Vé điện tử sẽ được gửi đến email ở trên.<br />
                Vé được lưu trong mục <strong>Vé của bạn</strong> của tài khoản {{ email }}.<br />
                Khi vào cổng, chỉ cần xuất trình mã QR điện tử.
              </p>
            </article>

            <article class="panel method-panel">
              <h2 class="panel-title">Phương thức thanh toán</h2>
              <div class="method-single">
                <i class="bi bi-bank"></i>
                SePay - Chuyển khoản ngân hàng
              </div>
            </article>
          </section>

          <section class="right-col">
            <article class="panel hold-panel">
              <h2 class="panel-title center">Thời gian giữ vé còn</h2>
              <div class="time-boxes">
                <div class="time-cell">{{ minuteBox }}</div>
                <span class="time-dot">:</span>
                <div class="time-cell">{{ secondBox }}</div>
              </div>
            </article>

            <article class="panel policy-panel">
              <h2 class="panel-title">Chính sách ban tổ chức</h2>
              <p class="policy-text">
                Vé đã mua sẽ không được hoàn trả, đổi ngày hay chỉnh sửa dưới bất kỳ trường hợp nào.
              </p>
            </article>

            <article class="panel order-panel">
              <h2 class="panel-title">Thông tin vé ({{ tickets.length }} vé)</h2>

              <div class="ticket-scroll">
                <div v-for="(ticket, index) in tickets" :key="index" class="ticket-row">
                  <div>
                    <small>LOẠI VÉ</small><strong>{{ ticket.type }}</strong>
                  </div>
                  <div>
                    <small>KHU</small><strong>{{ ticket.zone }}</strong>
                  </div>
                  <div>
                    <small>HÀNG</small><strong>{{ ticket.row }}</strong>
                  </div>
                  <div>
                    <small>GHẾ</small><strong>{{ ticket.seat }}</strong>
                  </div>
                </div>
              </div>

              <div class="promo-wrap">
                <input
                  v-model="promoCode"
                  type="text"
                  class="promo-input"
                  placeholder="Mã khuyến mại"
                />
                <button class="promo-btn" type="button">Áp dụng</button>
              </div>

              <div class="total-line">
                <strong>Tổng tiền</strong>
                <strong class="total-amount">{{ total.toLocaleString('vi-VN') }} đ</strong>
              </div>

              <label class="policy-check">
                <input v-model="agreedPolicy" type="checkbox" />
                Đồng ý với chính sách của ban tổ chức
              </label>

              <button class="pay-btn" type="button" :disabled="!canPay" @click="openPaymentPopup">
                Thanh toán
              </button>

              <p v-if="errorHint" class="error-line">{{ errorHint }}</p>
            </article>
          </section>
        </main>
      </div>
    </div>

    <div class="mobile-paybar d-lg-none">
      <div>
        <small>Tổng tiền</small>
        <strong>{{ total.toLocaleString('vi-VN') }} đ</strong>
      </div>
      <button type="button" :disabled="!canPay" @click="openPaymentPopup">Thanh toán</button>
    </div>

    <div
      v-if="showSepayPopup"
      class="sepay-overlay"
      role="dialog"
      aria-modal="true"
      aria-labelledby="sepay-title"
      @pointerdown="handleOverlayPointerDown"
      @pointerup="handleOverlayPointerUp"
    >
      <div class="sepay-modal">
        <div class="sepay-header">
          <div class="sepay-header-left">
            <i class="bi bi-bank"></i>
            <strong id="sepay-title">Chuyển khoản ngân hàng (SePay)</strong>
          </div>
          <button type="button" class="close-btn" @click="closePaymentPopup">
            <i class="bi bi-x-lg"></i>
          </button>
        </div>

        <div class="sepay-timer">
          <span>Thời gian giữ vé còn</span>
          <div class="time-boxes compact">
            <div class="time-cell">{{ minuteBox }}</div>
            <span class="time-dot">:</span>
            <div class="time-cell">{{ secondBox }}</div>
          </div>
        </div>

        <div class="sepay-warning">
          <i class="bi bi-exclamation-triangle"></i>
          <div>
            <p>Tài khoản và QR chỉ dùng cho giao dịch này</p>
            <p>Không lưu hoặc sử dụng lại thông tin này</p>
            <p>Đơn hàng chỉ được xác nhận khi bạn thấy trạng thái thành công</p>
          </div>
        </div>

        <div class="sepay-info-wrap">
          <div class="qr-wrap">
            <img
              src="https://api.qrserver.com/v1/create-qr-code/?size=220x220&data=SEPAY-963012001541042-1050000"
              alt="QR SePay"
            />
            <button type="button" class="download-qr-btn">
              <i class="bi bi-download"></i>
              Tải mã QR
            </button>
          </div>

          <div class="bank-info">
            <div class="info-row"><span>Ngân hàng</span><strong>BIDV</strong></div>
            <div class="info-row"><span>Mã giỏ hàng</span><strong>#44190</strong></div>
            <div class="info-row">
              <span>STK nhận tiền</span><strong class="text-primary">963012001541042</strong>
            </div>
            <div class="info-row"><span>Tên người nhận</span><strong>FLIP VN</strong></div>
            <div class="info-row">
              <span>Số tiền</span
              ><strong class="text-primary">{{ total.toLocaleString('vi-VN') }} đ</strong>
            </div>
          </div>
        </div>

        <div class="sepay-note">
          <strong>Lưu ý:</strong>
          <ul>
            <li>Vui lòng chuyển chính xác số tiền.</li>
          </ul>
        </div>

        <button type="button" class="confirm-paid-btn" @click="confirmPaid">
          Xác nhận đã thanh toán
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.flip-payment-page {
  --page-pad: 12px;
  --global-nav-offset: 72px;
  min-height: 100dvh;
  position: relative;
  background:
    radial-gradient(76% 40% at 50% -8%, rgba(255, 255, 255, 0.06) 0%, rgba(0, 0, 0, 0) 70%),
    linear-gradient(180deg, #111111 0%, #0b0b0b 100%);
  padding-bottom: 84px;
}

.surface-glow {
  position: absolute;
  inset: 0;
  background: radial-gradient(55% 30% at 50% 0, rgba(var(--bs-primary-rgb), 0.18), transparent 74%);
  pointer-events: none;
}

.shell-wrap {
  position: relative;
  z-index: 1;
}

.payment-shell {
  max-width: 1020px;
  height: calc(100dvh - var(--global-nav-offset));
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  padding: var(--page-pad);
}

.payment-main {
  flex: 1;
  min-height: 0;
  display: grid;
  grid-template-columns: 1.35fr 0.95fr;
  gap: 12px;
}

.left-col,
.right-col {
  min-height: 0;
  display: grid;
  gap: 12px;
}

.left-col {
  grid-template-rows: auto 1fr auto;
}

.right-col {
  grid-template-rows: auto auto 1fr;
}

.panel {
  background: rgba(17, 17, 17, 0.96);
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.panel-title {
  font-size: 1.25rem;
  margin: 0 0 8px;
  color: #f8f9ff;
  font-weight: 700;
}

.panel-title.center {
  text-align: center;
}

.panel-subtitle {
  margin: 0;
  color: #96a0ba;
  font-size: 0.95rem;
}

.panel-divider {
  border-color: rgba(122, 132, 165, 0.34);
  margin: 10px 0;
}

.event-panel {
  display: flex;
  align-items: center;
  gap: 12px;
}

.event-thumb {
  width: 118px;
  height: 78px;
  border-radius: 12px;
  object-fit: cover;
}

.event-meta {
  min-width: 0;
}

.event-title {
  margin: 0 0 6px;
  color: #f4f7ff;
  font-size: 1.1rem;
  font-weight: 700;
  line-height: 1.3;
}

.event-subline {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  color: #d3daf0;
  font-size: 0.9rem;
}

.event-subline span {
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.event-address {
  margin: 5px 0 0;
  color: #a7b0c7;
  font-size: 0.9rem;
}

.receiver-panel {
  overflow: auto;
}

.receiver-row {
  display: grid;
  grid-template-columns: 20px 1fr 26px;
  gap: 10px;
  align-items: start;
  margin-bottom: 8px;
}

.receiver-row > i {
  color: #aeb7cf;
  padding-top: 3px;
}

.receiver-col {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
}

.receiver-label {
  color: #ced6ed;
  font-size: 0.9rem;
}

.receiver-label b {
  color: #ef5e66;
}

.receiver-col strong {
  font-size: 1rem;
  line-height: 1.3;
  color: #f5f7ff;
  word-break: break-word;
}

.inline-edit-input {
  width: min(320px, 100%);
  height: 36px;
  border-radius: 8px;
  border: 1px solid rgba(139, 148, 171, 0.42);
  background: rgba(255, 255, 255, 0.04);
  color: #e8edf9;
  font-size: 1rem;
  padding: 0 10px;
}

.icon-button {
  border: 0;
  background: transparent;
  color: #d9dfef;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
}

.ticket-note {
  margin: 0;
  color: #dbe1f2;
  font-size: 0.9rem;
  line-height: 1.45;
}

.ticket-note i {
  margin-right: 6px;
  color: #aeb7cf;
}

.method-single {
  min-height: 42px;
  border-radius: 10px;
  border: 1px solid rgba(139, 149, 176, 0.36);
  background: rgba(255, 255, 255, 0.05);
  color: #ecf0ff;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  font-size: 0.95rem;
  font-weight: 600;
}

.hold-panel {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
}

.time-boxes {
  display: flex;
  align-items: center;
  gap: 6px;
}

.time-cell {
  width: 42px;
  height: 42px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(150, 159, 186, 0.34);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  font-weight: 700;
  color: #f8fbff;
}

.time-dot {
  color: #f3f5ff;
  font-size: 1.3rem;
  font-weight: 700;
  transform: translateY(-2px);
}

.policy-text {
  margin: 0;
  color: #f0f4ff;
  font-size: 0.95rem;
  line-height: 1.45;
}

.order-panel {
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.ticket-scroll {
  max-height: 180px;
  overflow-y: auto;
  padding-right: 6px;
}

.ticket-row {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
  padding: 8px 0;
  border-bottom: 1px solid rgba(122, 132, 165, 0.2);
}

.ticket-row small {
  color: #98a2bf;
  font-size: 0.8rem;
  display: block;
}

.ticket-row strong {
  display: block;
  color: #f7f8ff;
  font-size: 1.05rem;
  line-height: 1.2;
}

.promo-wrap {
  margin-top: 12px;
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 8px;
}

.promo-input {
  height: 42px;
  border-radius: 10px;
  border: 1px solid rgba(139, 148, 171, 0.42);
  background: rgba(255, 255, 255, 0.02);
  color: #e8edf9;
  padding: 0 12px;
  font-size: 1rem;
}

.promo-btn {
  height: 42px;
  border-radius: 10px;
  border: 1px solid rgba(139, 148, 171, 0.42);
  background: rgba(255, 255, 255, 0.07);
  color: #cfd6ea;
  padding: 0 16px;
  font-weight: 700;
  font-size: 0.95rem;
}

.total-line {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  color: #f9fbff;
  font-size: 1rem;
}

.total-amount {
  font-size: 1.35rem;
  line-height: 1.1;
}

.policy-check {
  margin-top: 8px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #edf1ff;
  font-size: 0.9rem;
}

.policy-check input {
  width: 16px;
  height: 16px;
}

.pay-btn {
  margin-top: 10px;
  width: 100%;
  height: 44px;
  border: 0;
  border-radius: 10px;
  background: #d0d2d6;
  color: #2f333b;
  font-weight: 700;
  font-size: 1rem;
}

.pay-btn:disabled {
  opacity: 0.72;
}

.error-line {
  margin: 8px 0 0;
  color: #ff5b64;
  font-size: 0.9rem;
}

.mobile-paybar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 30;
  background: #111111;
  border-top: 1px solid rgba(255, 255, 255, 0.14);
  padding: 10px 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mobile-paybar small {
  display: block;
  color: #9ba6c4;
}

.mobile-paybar strong {
  color: #f8fbff;
}

.mobile-paybar button {
  border: 0;
  border-radius: 9px;
  background: #d7d9dd;
  color: #222831;
  font-weight: 700;
  padding: 9px 13px;
  font-size: 0.95rem;
}

.sepay-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.62);
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 12px;
}

.sepay-modal {
  width: min(760px, 100%);
  max-height: min(94dvh, 920px);
  overflow: auto;
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 14px;
  background: #111111;
  color: #f6f8ff;
  padding: 0;
}

.sepay-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 18px;
  border-bottom: 1px solid rgba(122, 132, 165, 0.3);
}

.sepay-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1.1rem;
}

.close-btn {
  width: 30px;
  height: 30px;
  border: 0;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.08);
  color: #f7f9ff;
}

.sepay-timer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding: 14px;
  background: #0a0a0a;
  border-bottom: 1px solid rgba(122, 132, 165, 0.3);
  font-size: 1rem;
  font-weight: 600;
}

.time-boxes.compact .time-cell {
  width: 40px;
  height: 40px;
  font-size: 1.05rem;
}

.sepay-warning {
  margin: 14px 18px;
  border-radius: 10px;
  background: #f2df76;
  color: #3f3100;
  display: flex;
  gap: 10px;
  padding: 10px 12px;
  font-size: 1rem;
}

.sepay-warning i {
  font-size: 1.15rem;
  margin-top: 2px;
}

.sepay-warning p {
  margin: 0;
}

.sepay-info-wrap {
  padding: 8px 18px 0;
  display: grid;
  grid-template-columns: 230px minmax(0, 1fr);
  gap: 12px;
}

.qr-wrap {
  border: 1px solid rgba(122, 132, 165, 0.3);
  border-radius: 12px;
  padding: 10px;
}

.qr-wrap img {
  width: 100%;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: block;
}

.download-qr-btn {
  width: 100%;
  margin-top: 8px;
  height: 40px;
  border-radius: 10px;
  border: 1px solid rgba(122, 132, 165, 0.3);
  background: transparent;
  color: #edf2ff;
  font-size: 1rem;
}

.bank-info {
  border: 1px solid rgba(122, 132, 165, 0.3);
  border-radius: 12px;
  padding: 8px 14px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.06), rgba(255, 255, 255, 0.03));
}

.info-row {
  min-height: 48px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(122, 132, 165, 0.2);
  gap: 12px;
}

.info-row:last-child {
  border-bottom: 0;
}

.info-row span {
  font-size: 1.05rem;
  color: #d9dfef;
}

.info-row strong {
  font-size: 1.05rem;
  color: #f8fbff;
}

.info-row .text-primary {
  color: rgb(var(--bs-primary-rgb)) !important;
}

.sepay-note {
  margin: 14px 18px 0;
  font-size: 1rem;
}

.sepay-note ul {
  margin: 6px 0 0;
  padding-left: 20px;
}

.confirm-paid-btn {
  margin: 12px 18px 18px;
  width: calc(100% - 36px);
  height: 48px;
  border: 0;
  border-radius: 10px;
  background: #d7d9dd;
  color: #1f2530;
  font-weight: 700;
  font-size: 1.1rem;
}

@media (max-width: 991.98px) {
  .payment-shell {
    height: auto;
    min-height: calc(100dvh - var(--global-nav-offset));
    padding: 10px;
  }

  .payment-main {
    grid-template-columns: minmax(0, 1fr);
    gap: 10px;
  }

  .left-col,
  .right-col {
    grid-template-rows: auto;
    overflow: visible;
  }

  .panel {
    padding: 12px;
    border-radius: 12px;
  }

  .panel-title {
    font-size: 1.1rem;
  }

  .event-panel {
    align-items: flex-start;
  }

  .event-thumb {
    width: 88px;
    height: 68px;
  }

  .event-title {
    font-size: 1rem;
    margin-bottom: 4px;
  }

  .event-subline,
  .event-address,
  .panel-subtitle,
  .policy-text,
  .ticket-note,
  .receiver-label,
  .policy-check,
  .error-line,
  .promo-btn {
    font-size: 0.875rem;
  }

  .receiver-col strong {
    font-size: 0.95rem;
  }

  .inline-edit-input,
  .promo-input {
    font-size: 1rem;
  }

  .ticket-row strong {
    font-size: 0.95rem;
  }

  .time-cell {
    width: 38px;
    height: 38px;
    font-size: 1rem;
  }

  .time-dot {
    font-size: 1.1rem;
  }

  .total-line {
    font-size: 0.95rem;
  }

  .total-amount {
    font-size: 1.2rem;
  }

  .pay-btn {
    font-size: 0.95rem;
    height: 42px;
  }

  .sepay-modal {
    max-height: 95dvh;
  }

  .sepay-header {
    padding: 12px;
  }

  .sepay-header-left {
    font-size: 1rem;
  }

  .sepay-timer {
    font-size: 0.95rem;
    flex-direction: column;
    gap: 8px;
  }

  .sepay-warning {
    margin: 12px;
    font-size: 0.9rem;
  }

  .sepay-info-wrap {
    padding: 0 12px;
    grid-template-columns: 1fr;
  }

  .sepay-note {
    margin: 12px;
    font-size: 0.9rem;
  }

  .confirm-paid-btn {
    margin: 8px 12px 12px;
    width: calc(100% - 24px);
    height: 44px;
    font-size: 1rem;
  }
}

@media (hover: hover) {
  .icon-button:hover,
  .close-btn:hover,
  .download-qr-btn:hover {
    opacity: 0.9;
  }

  .pay-btn:hover:enabled,
  .confirm-paid-btn:hover {
    filter: brightness(0.98);
  }
}
</style>
