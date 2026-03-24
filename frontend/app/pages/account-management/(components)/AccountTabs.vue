<template>
  <nav class="am-tabs">
    <NuxtLinkLocale
      to="/account-management/user"
      class="am-tabs__item"
      active-class="am-tabs__item--active"
    >
      Users
    </NuxtLinkLocale>
    <NuxtLinkLocale
      to="/account-management/organizer"
      class="am-tabs__item"
      active-class="am-tabs__item--active"
    >
      Organizers
    </NuxtLinkLocale>
    <NuxtLinkLocale
      to="/account-management/internal-account"
      class="am-tabs__item"
      active-class="am-tabs__item--active"
    >
      Internal Accounts
    </NuxtLinkLocale>
  </nav>
</template>
