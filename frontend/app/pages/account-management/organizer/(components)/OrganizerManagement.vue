<template>
  <div class="am-page">
    <!-- Page Header -->
    <div class="am-header">
      <h1 class="am-header__title">Organizer Management</h1>
      <div class="am-header__action">
        <button class="am-btn-primary">
          <i class="bi bi-plus-lg"></i>
          Add New Organizer
        </button>
      </div>
    </div>

    <!-- Tab Navigation -->
    <AccountTabs />

    <!-- Search Toolbar -->
    <SearchToolbar />

    <!-- Organizer Card Grid -->
    <OrganizerGrid />

    <!-- Pagination -->
    <PaginationBar />
  </div>
</template>

<script setup lang="ts">
import AccountTabs from '../../(components)/AccountTabs.vue';
import SearchToolbar from '../../(components)/SearchToolbar.vue';
import PaginationBar from '../../(components)/PaginationBar.vue';
import OrganizerGrid from './OrganizerGrid.vue';

import '../../style/account-management.css';
</script>
