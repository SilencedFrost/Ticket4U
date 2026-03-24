<template>
  <!--
    OrganizerCard: individual organizer card
    Props expected (khi viết script): initials, name, email, eventCount, status
  -->
  <div class="am-card">
    <!-- Card Header: Logo + Info -->
    <div class="am-card__header">
      <div class="am-card__logo">
        {{ initials }}
      </div>
      <div class="am-card__info">
        <p class="am-card__name">{{ name }}</p>
        <p class="am-card__email">{{ email }}</p>
      </div>
    </div>

    <!-- Card Meta: Event count + Status -->
    <div class="am-card__meta">
      <span class="am-card__events">
        <strong>{{ eventCount }}</strong> Events
      </span>
      <span
        class="am-status"
        :class="{
          'am-status--active': status === 'active',
          'am-status--pending': status === 'pending',
          'am-status--inactive': status === 'inactive',
        }"
      >
        <span class="am-status__dot"></span>
        {{ status === 'active' ? 'Active' : status === 'pending' ? 'Pending' : 'Inactive' }}
      </span>
    </div>

    <!-- Card Actions -->
    <div class="am-card__actions">
      <button class="btn btn-sm btn-primary">
        <i class="bi bi-pencil-square"></i>
        Edit
      </button>
      <button class="btn btn-sm btn-reactive-gray">
        <i class="bi bi-trash"></i>
        Delete
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  initials: string;
  name: string;
  email: string;
  eventCount: string;
  status: string;
}>();
</script>
