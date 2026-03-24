<template>
  <div class="am-card-grid">
    <!-- Card 1 -->
    <OrganizerCard
      initials="EM"
      name="EventMaster Pro"
      email="contact@eventmaster.com"
      event-count="12"
      status="active"
    />

    <!-- Card 2 -->
    <OrganizerCard
      initials="CC"
      name="ConcertConnect"
      email="info@concertconnect.vn"
      event-count="8"
      status="active"
    />

    <!-- Card 3 -->
    <OrganizerCard
      initials="SL"
      name="ShowLight Events"
      email="hello@showlight.com"
      event-count="5"
      status="pending"
    />

    <!-- Card 4 -->
    <OrganizerCard
      initials="FZ"
      name="FestZone"
      email="team@festzone.vn"
      event-count="2"
      status="active"
    />

    <!-- Card 5 -->
    <OrganizerCard
      initials="MT"
      name="MusicTown"
      email="support@musictown.com"
      event-count="15"
      status="active"
    />

    <!-- Card 6 -->
    <OrganizerCard
      initials="LV"
      name="LiveVenue Studio"
      email="booking@livevenue.vn"
      event-count="3"
      status="pending"
    />
  </div>
</template>

<script setup lang="ts">
import OrganizerCard from './OrganizerCard.vue';
</script>
