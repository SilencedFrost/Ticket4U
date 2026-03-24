<template>
  <nav class="am-tabs">
    <NuxtLink
      to="/account-management/user"
      class="am-tabs__item"
      active-class="am-tabs__item--active"
    >
      Users
    </NuxtLink>
    <NuxtLink
      to="/account-management/organizer"
      class="am-tabs__item"
      active-class="am-tabs__item--active"
    >
      Organizers
    </NuxtLink>
    <NuxtLink
      to="/account-management/internal-account"
      class="am-tabs__item"
      active-class="am-tabs__item--active"
    >
      Internal Accounts
    </NuxtLink>
  </nav>
</template>
