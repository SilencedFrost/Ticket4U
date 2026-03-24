<template>
  <div class="am-page">
    <!-- Page Header -->
    <div class="am-header">
      <h1 class="am-header__title">Internal Accounts</h1>
      <div class="am-header__action">
        <button class="am-btn-primary">
          <i class="bi bi-plus-lg"></i>
          Add New Internal User
        </button>
      </div>
    </div>

    <!-- Tab Navigation -->
    <AccountTabs />

    <!-- Filter + Search Toolbar -->
    <div class="am-toolbar">
      <div class="am-toolbar__search">
        <i class="bi bi-search am-toolbar__search-icon"></i>
        <input
          type="text"
          class="am-toolbar__search-input"
          placeholder="Tìm kiếm..."
        />
      </div>

      <!-- Role Filter Dropdown -->
      <RoleFilterDropdown />

      <div class="am-toolbar__spacer"></div>
    </div>

    <!-- Internal Account Table -->
    <InternalAccountTable />
  </div>
</template>

<script setup lang="ts">
import AccountTabs from '../../(components)/AccountTabs.vue';
import RoleFilterDropdown from './RoleFilterDropdown.vue';
import InternalAccountTable from './InternalAccountTable.vue';

import '../../style/account-management.css';
</script>
