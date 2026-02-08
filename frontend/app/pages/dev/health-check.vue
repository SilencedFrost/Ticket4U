<script lang="ts" setup>
const config = useRuntimeConfig();

interface ecJWK {
  kty: string;
  crv: string;
  kid: string;
  x: string;
  y: string;
}

const emptyJwk: ecJWK = {
  kty: '',
  crv: '',
  kid: '',
  x: '',
  y: '',
};

const ticketAuthJwk = reactive<ecJWK>(emptyJwk);
const isHealthy = reactive({
  ticketService: false,
  ticketAuthJwk: false,
});

const checkHealth = async () => {
  try {
    await $fetch(config.public.ticketHealthUrl);
    isHealthy.ticketService = true;
  } catch {
    isHealthy.ticketService = false;
  }

  try {
    Object.assign(ticketAuthJwk, await $fetch(`${config.public.ticketHealthUrl}/jwk/auth`));
    isHealthy.ticketAuthJwk = true;
  } catch {
    Object.assign(ticketAuthJwk, emptyJwk);
    isHealthy.ticketAuthJwk = false;
  }
};

let intervalId: NodeJS.Timeout | null = null;

onMounted(() => {
  checkHealth();
  // Poll every 500ms
  intervalId = setInterval(checkHealth, 500);
});

onUnmounted(() => {
  if (intervalId) clearInterval(intervalId);
});
</script>

<template>
  <div class="row">
    <div class="col">
      <div class="p-2">
        <div class="card d-flex p-3">
          <h4 class="d-flex align-items-center justify-content-center">
            <span
              class="health-indicator"
              :class="{ healthy: isHealthy.ticketService, unhealthy: !isHealthy.ticketService }"
            />
            Ticket-service
          </h4>
          <hr class="m-0 my-2 p-0" />
          <div class="d-flex align-items-center">
            <span
              class="health-indicator"
              :class="{
                healthy: isHealthy.ticketAuthJwk,
                unhealthy: !isHealthy.ticketAuthJwk,
              }"
            />
            Auth JWK present
          </div>
          <div
            v-if="isHealthy.ticketAuthJwk"
            class="bg-reactive-secondary p-2 border rounded-2 d-flex flex-column mt-2"
          >
            <div class="mb-2">Key type: {{ ticketAuthJwk.kty }}</div>
            <div class="mb-2">Curve: {{ ticketAuthJwk.crv }}</div>
            <div class="mb-2">Key id: {{ ticketAuthJwk.kid }}</div>
            <div class="mb-2">X: {{ ticketAuthJwk.x }}</div>
            <div>Y: {{ ticketAuthJwk.y }}</div>
          </div>
        </div>
      </div>
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
