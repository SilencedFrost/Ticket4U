<script setup lang="ts">
defineProps<{
  eventData: any,
  eventThumbImage: string
}>();
</script>

<template>
    <!-- Schedule Section -->
    <section id="schedule-section" class="p-3 p-md-4 card bg-reactive-secondary m-3 mx-auto mw-100">
        <div class="container-xxl">
            <h3 class="text-reactive-primary fw-bold mb-3 mb-md-4 h4"> {{ $t('event-detail.schedule_location') }}</h3>
            
            <!-- Info and Button -->
            <div class="d-flex flex-column gap-2 mb-3">
                <h6 class="text-reactive-primary mb-1">{{ eventData.title }}</h6>
                <p class="text-reactive-secondary mb-2 small">{{ eventData.address[$i18n.locale] }}</p>
            </div>
            
            <!-- Image -->
            <div class="mb-4">
                <img :src="eventThumbImage" alt="" class="w-auto rounded-3 object-fit-cover img-fluid mh-100" />
            </div>
            <div class="border-bottom my-4"></div>
            <div class="row w-100">
                <div class="date col-lg-6">
                    <p>{{ $d(new Date(eventData.date), 'long') }}</p>
                </div>
                <div class="time col-lg-6 text-lg-end">
                    <button class="btn bg-primary text-reactive-primary mb-0">{{ eventData.time }}</button>
                </div>
            </div>
        </div>
    </section>
</template>