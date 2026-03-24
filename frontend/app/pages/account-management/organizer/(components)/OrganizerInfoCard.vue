<template>
  <!--
    OrganizerInfoCard: hiển thị thông tin tổng quan organizer
    Props expected (khi viết script): organizer object
  -->
  <div class="am-info-card">
    <!-- Logo placeholder -->
    <div class="am-info-card__logo">
      VIB
    </div>

    <!-- Organizer info -->
    <div class="am-info-card__body">
      <h2 class="am-info-card__name">VIB Entertainment</h2>
      <p class="am-info-card__email">contact@vib.vn</p>

      <div class="am-info-card__stats">
        <div class="am-info-card__stat">
          <span class="am-info-card__stat-label">Rating</span>
          <span class="am-info-card__stat-value">⭐ 4.8</span>
        </div>

        <div class="am-info-card__stat">
          <span class="am-info-card__stat-label">Số sự kiện đang tổ chức</span>
          <span class="am-info-card__stat-value">12</span>
        </div>

        <div class="am-info-card__stat">
          <span class="am-info-card__stat-label">Tài khoản Ngân hàng</span>
          <span class="am-info-card__stat-value">1234567890 (Vietcombank)</span>
        </div>

        <div class="am-info-card__stat">
          <span class="am-info-card__stat-label">Status</span>
          <span class="am-status am-status--active">
            <span class="am-status__dot"></span>
            Active
          </span>
        </div>
      </div>
    </div>
  </div>
</template>
