<template>
  <div class="am-page">
    <!-- Back Button -->
    <NuxtLink to="/account-management/organizer" class="am-back-btn">
      <i class="bi bi-arrow-left"></i>
      Quay lại danh sách
    </NuxtLink>

    <!-- Page Header -->
    <div class="am-header" style="margin-top: 1rem">
      <h1 class="am-header__title">Organizer Details & Events</h1>
    </div>

    <!-- Organizer Info Card -->
    <OrganizerInfoCard />

    <!-- Sub-accounts Section -->
    <section class="am-section">
      <div class="am-section__header">
        <h2 class="am-section__title">Sub-accounts</h2>
      </div>
      <div class="am-toolbar">
        <div class="am-toolbar__search">
          <i class="bi bi-search am-toolbar__search-icon"></i>
          <input
            type="text"
            class="am-toolbar__search-input"
            placeholder="Search..."
          />
        </div>
        <div class="am-toolbar__spacer"></div>
        <div class="am-dropdown">
          <button class="am-dropdown__trigger">
            Sort by
            <i class="bi bi-chevron-down"></i>
          </button>
        </div>
      </div>
      <SubAccountTable />
    </section>

    <!-- Events Section -->
    <section class="am-section">
      <div class="am-section__header">
        <h2 class="am-section__title">Các sự kiện</h2>
      </div>
      <div class="am-toolbar">
        <div class="am-toolbar__search">
          <i class="bi bi-search am-toolbar__search-icon"></i>
          <input
            type="text"
            class="am-toolbar__search-input"
            placeholder="Search..."
          />
        </div>
        <div class="am-toolbar__spacer"></div>
        <div class="am-dropdown">
          <button class="am-dropdown__trigger">
            Sort by
            <i class="bi bi-chevron-down"></i>
          </button>
        </div>
      </div>
      <OrganizerEventTable />
    </section>
  </div>
</template>

<script setup lang="ts">
import OrganizerInfoCard from './OrganizerInfoCard.vue';
import SubAccountTable from './SubAccountTable.vue';
import OrganizerEventTable from './OrganizerEventTable.vue';

import '../../style/account-management.css';
</script>
