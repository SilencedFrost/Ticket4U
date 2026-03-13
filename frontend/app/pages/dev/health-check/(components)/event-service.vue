<script lang="ts" setup>
const props = defineProps({
  pollRate: {
    type: Number,
    required: true,
  },
});

const config = useRuntimeConfig();

const isHealthy = reactive({
  service: false,
});

const checkHealth = async () => {
  try {
    await $fetch(config.public.eventHealthUrl);
    isHealthy.service = true;
  } catch {
    isHealthy.service = false;
  }
};

let intervalId: NodeJS.Timeout | null = null;

const startPolling = () => {
  if (intervalId) clearInterval(intervalId);
  intervalId = setInterval(checkHealth, props.pollRate);
};

onMounted(() => {
  checkHealth();
  startPolling();
});

watch(
  () => props.pollRate,
  () => {
    startPolling();
  },
);

onUnmounted(() => {
  if (intervalId) clearInterval(intervalId);
});
</script>

<template>
  <div>
    <div class="card d-flex p-3">
      <!-- Header -->
      <div>
        <h4 class="d-flex align-items-center justify-content-center">
          <span
            class="health-indicator"
            :class="{ healthy: isHealthy.service, unhealthy: !isHealthy.service }"
          />
          Event-service
        </h4>
      </div>

      <hr class="m-0 my-2 p-0" />
    </div>
  </div>
</template>

<style scoped>
.health-indicator {
  display: inline-block;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-right: 10px;
}

.health-indicator.healthy {
  background-color: var(--bs-success);
}

.health-indicator.unhealthy {
  background-color: var(--bs-danger);
}

.health-indicator.loading {
  background-color: var(--bs-secondary);
}
</style>
