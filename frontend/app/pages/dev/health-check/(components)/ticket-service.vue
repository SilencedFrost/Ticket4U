<script lang="ts" setup>
import type { ecJWK, userPrincipal } from '../(types)/health.types';
import { emptyJwk, emptyUser } from '../(types)/health.types';

const props = defineProps({
  pollRate: {
    type: Number,
    required: true,
  },
});

const config = useRuntimeConfig();

const healthFeatures = reactive({
  authJwkPresence: false,
  userAuthStatus: false,
});
const authJwk = reactive<ecJWK>(emptyJwk);
const userAuth = reactive<userPrincipal>(emptyUser);
const isHealthy = reactive({
  service: false,
  authJwk: false,
  userAuth: false,
});

const checkHealth = async () => {
  try {
    await $fetch(config.public.ticketHealthUrl);
    isHealthy.service = true;
  } catch {
    isHealthy.service = false;
  }

  if (healthFeatures.authJwkPresence) {
    try {
      Object.assign(authJwk, await $fetch(`${config.public.ticketHealthUrl}/jwk/auth`));
      isHealthy.authJwk = true;
    } catch {
      Object.assign(authJwk, emptyJwk);
      isHealthy.authJwk = false;
    }
  } else {
    isHealthy.authJwk = false;
  }

  if (healthFeatures.userAuthStatus) {
    try {
      Object.assign(
        userAuth,
        await $fetch(`${config.public.ticketHealthUrl}/jwk/user`, {
          credentials: 'include',
        }),
      );
      isHealthy.userAuth = true;
    } catch {
      isHealthy.userAuth = false;
    }
  } else {
    isHealthy.userAuth = false;
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
          Ticket-service
        </h4>
      </div>

      <hr class="m-0 my-2 p-0" />

      <!-- Auth JWK Presence -->
      <div class="mb-2">
        <div class="d-flex align-items-center">
          <!-- Toggle feature -->
          <div class="form-check form-switch">
            <input
              v-model="healthFeatures.authJwkPresence"
              class="form-check-input"
              type="checkbox"
              role="switch"
            />
          </div>
          <!-- Indicator -->
          <span
            class="health-indicator"
            :class="{
              healthy: isHealthy.authJwk,
              unhealthy: !isHealthy.authJwk,
            }"
          />
          Auth JWK presence
        </div>
        <!-- Details -->
        <div
          v-if="isHealthy.authJwk"
          class="bg-reactive-secondary rounded-2 p-2 border d-flex flex-column mt-2"
        >
          <div class="mb-2">Key type: {{ authJwk.kty }}</div>
          <div class="mb-2">Curve: {{ authJwk.crv }}</div>
          <div class="mb-2">Key id: {{ authJwk.kid }}</div>
          <div class="mb-2">X: {{ authJwk.x }}</div>
          <div class="mb-2">Y: {{ authJwk.y }}</div>
          <div>Algorithm: {{ authJwk.alg }}</div>
        </div>
      </div>

      <!-- User auth status -->
      <div>
        <div class="d-flex align-items-center">
          <!-- Toggle feature -->
          <div class="form-check form-switch">
            <input
              v-model="healthFeatures.userAuthStatus"
              class="form-check-input"
              type="checkbox"
              role="switch"
            />
          </div>
          <!-- Indicator -->
          <span
            class="health-indicator"
            :class="{
              healthy: isHealthy.userAuth,
              unhealthy: !isHealthy.userAuth,
            }"
          />
          User auth status
        </div>
        <!-- Details -->
        <div
          v-if="isHealthy.userAuth"
          class="bg-reactive-secondary rounded-2 p-2 border d-flex flex-column mt-2"
        >
          <div class="mb-2">User ID: {{ userAuth.userId }}</div>
          <div>Roles: {{ userAuth.authorities }}</div>
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
