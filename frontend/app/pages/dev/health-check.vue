<script lang="ts" setup>
const config = useRuntimeConfig();

interface ecJWK {
  kty: string;
  crv: string;
  kid: string;
  x: string;
  y: string;
  alg: string;
}

interface userPrincipal {
  userId: string;
  authorities: string;
}

const emptyJwk: ecJWK = {
  kty: '',
  crv: '',
  kid: '',
  x: '',
  y: '',
  alg: '',
};

const emptyUser: userPrincipal = {
  userId: '',
  authorities: '',
};

const pollRate = ref<number>(5000);
const ticketHealthFeatures = reactive({
  authJwkPresence: false,
  userAuthStatus: false,
});
const ticketAuthJwk = reactive<ecJWK>(emptyJwk);
const ticketUserAuth = reactive<userPrincipal>(emptyUser);
const isHealthy = reactive({
  ticketService: false,
  ticketAuthJwk: false,
  ticketUserAuth: false,
});

const checkHealth = async () => {
  try {
    await $fetch(config.public.ticketHealthUrl);
    isHealthy.ticketService = true;
  } catch {
    isHealthy.ticketService = false;
  }

  if (ticketHealthFeatures.authJwkPresence) {
    try {
      Object.assign(ticketAuthJwk, await $fetch(`${config.public.ticketHealthUrl}/jwk/auth`));
      isHealthy.ticketAuthJwk = true;
    } catch {
      Object.assign(ticketAuthJwk, emptyJwk);
      isHealthy.ticketAuthJwk = false;
    }
  } else {
    isHealthy.ticketAuthJwk = false;
  }

  if (ticketHealthFeatures.userAuthStatus) {
    try {
      Object.assign(
        ticketUserAuth,
        await $fetch(`${config.public.ticketHealthUrl}/jwk/user`, {
          credentials: 'include',
        }),
      );
      isHealthy.ticketUserAuth = true;
    } catch {
      isHealthy.ticketUserAuth = false;
    }
  } else {
    isHealthy.ticketUserAuth = false;
  }
};

let intervalId: NodeJS.Timeout | null = null;

const startPolling = () => {
  if (intervalId) clearInterval(intervalId);
  intervalId = setInterval(checkHealth, pollRate.value);
};

onMounted(() => {
  checkHealth();
  startPolling();
});

watch(pollRate, () => {
  startPolling();
});

onUnmounted(() => {
  if (intervalId) clearInterval(intervalId);
});
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
    <div class="row">
      <!-- Ticket service column -->
      <div class="col">
        <div class="card d-flex p-3 m-2">
          <!-- Header -->
          <div>
            <h4 class="d-flex align-items-center justify-content-center">
              <span
                class="health-indicator"
                :class="{ healthy: isHealthy.ticketService, unhealthy: !isHealthy.ticketService }"
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
                  v-model="ticketHealthFeatures.authJwkPresence"
                  class="form-check-input"
                  type="checkbox"
                  role="switch"
                />
              </div>
              <!-- Indicator -->
              <span
                class="health-indicator"
                :class="{
                  healthy: isHealthy.ticketAuthJwk,
                  unhealthy: !isHealthy.ticketAuthJwk,
                }"
              />
              Auth JWK presence
            </div>
            <!-- Details -->
            <div
              v-if="isHealthy.ticketAuthJwk"
              class="bg-reactive-secondary rounded-2 p-2 border d-flex flex-column mt-2"
            >
              <div class="mb-2">Key type: {{ ticketAuthJwk.kty }}</div>
              <div class="mb-2">Curve: {{ ticketAuthJwk.crv }}</div>
              <div class="mb-2">Key id: {{ ticketAuthJwk.kid }}</div>
              <div class="mb-2">X: {{ ticketAuthJwk.x }}</div>
              <div class="mb-2">Y: {{ ticketAuthJwk.y }}</div>
              <div>Algorithm: {{ ticketAuthJwk.alg }}</div>
            </div>
          </div>

          <!-- User auth status -->
          <div>
            <div class="d-flex align-items-center">
              <!-- Toggle feature -->
              <div class="form-check form-switch">
                <input
                  v-model="ticketHealthFeatures.userAuthStatus"
                  class="form-check-input"
                  type="checkbox"
                  role="switch"
                />
              </div>
              <!-- Indicator -->
              <span
                class="health-indicator"
                :class="{
                  healthy: isHealthy.ticketUserAuth,
                  unhealthy: !isHealthy.ticketUserAuth,
                }"
              />
              User auth status
            </div>
            <!-- Details -->
            <div
              v-if="isHealthy.ticketUserAuth"
              class="bg-reactive-secondary rounded-2 p-2 border d-flex flex-column mt-2"
            >
              <div class="mb-2">User ID: {{ ticketUserAuth.userId }}</div>
              <div>Roles: {{ ticketUserAuth.authorities }}</div>
            </div>
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
