<template>
  <div class="am-page">
    <!-- Page Header -->
    <div class="am-header">
      <h1 class="am-header__title">Admin User Management Tab</h1>
      <div class="am-header__action">
        <button class="am-btn-primary">
          <i class="bi bi-plus-lg"></i>
          Add User
        </button>
      </div>
    </div>

    <!-- Tab Navigation -->
    <AccountTabs />

    <!-- Search + Sort Toolbar -->
    <SearchToolbar>
      <template #actions>
        <!-- Sort Dropdown -->
        <div class="am-dropdown">
          <button class="am-dropdown__trigger">
            Sort by: Newest
            <i class="bi bi-chevron-down"></i>
          </button>
          <!-- Dropdown menu (hidden by default, sẽ toggle qua script sau) -->
          <!--
          <div class="am-dropdown__menu">
            <button class="am-dropdown__item am-dropdown__item--active">Newest</button>
            <button class="am-dropdown__item">Role</button>
            <button class="am-dropdown__item">Status</button>
          </div>
          -->
        </div>
      </template>
    </SearchToolbar>

    <!-- User Table -->
    <UserTable />
  </div>
</template>

<script setup lang="ts">
import AccountTabs from '../../(components)/AccountTabs.vue';
import SearchToolbar from '../../(components)/SearchToolbar.vue';
import UserTable from './UserTable.vue';

import '../../style/account-management.css';
</script>
