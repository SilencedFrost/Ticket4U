<template>
  <!--
    RoleFilterDropdown: Filter by Role dropdown
    Props expected (khi viết script): selectedRole, roles[]
  -->
  <div class="am-dropdown">
    <button class="am-dropdown__trigger">
      <i class="bi bi-funnel"></i>
      Filter by Role
      <i class="bi bi-chevron-down"></i>
    </button>
    <!-- Dropdown menu (hidden by default, sẽ toggle qua script sau) -->
    <!--
    <div class="am-dropdown__menu">
      <button class="am-dropdown__item">System Admin</button>
      <button class="am-dropdown__item">Agent Support</button>
      <button class="am-dropdown__item">Manager</button>
      <button class="am-dropdown__item">Moderator</button>
    </div>
    -->
  </div>
</template>
