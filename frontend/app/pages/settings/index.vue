<template>
  <div>
    <!-- Redirects to the personal-information tab -->
  </div>
</template>

<script setup>
import { navigateTo } from '#app'

// <!-- TODO: wire up script -->
// Redirect settings index to the personal information tab
// TODO: replace with useLocalePath()
navigateTo('/settings/account')
</script>
