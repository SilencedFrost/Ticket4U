<script lang="ts" setup>
import TicketService from './(components)/ticket-service.vue';

const pollRate = ref<number>(5000);
</script>

<template>
  <div>
    <!-- poll rate adjuster -->
    <div class="card p-2 m-2 mb-0 d-flex flex-row align-items-center">
      <label for="poll-rate" class="mx-2">Poll rate (ms): </label
      ><input
        id="poll-rate"
        v-model="pollRate"
        type="text"
        class="form-control"
        style="width: 300px"
      />
    </div>
    <!-- Services -->
    <div class="row">
      <ticket-service :poll-rate="pollRate" />
    </div>
  </div>
</template>
