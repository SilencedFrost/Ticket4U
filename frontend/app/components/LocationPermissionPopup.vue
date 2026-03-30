<script setup lang="ts">
const { requestLocation, permissionStatus, setPermissionDenied } = useLocation();

const isVisible = computed(() => permissionStatus.value === null);

const handleAllow = async () => {
  await requestLocation();
};

const handleDeny = () => {
  setPermissionDenied();
};
</script>

<template>
  <div v-if="isVisible">
    <div
      class="modal fade show d-block"
      tabindex="-1"
      role="dialog"
      aria-modal="true"
      @click.self="handleDeny"
    >
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content border-0 rounded-4 overflow-hidden w-75 mx-auto">
          <div class="ratio ratio-16x9">
            <img
              src="https://i.pinimg.com/736x/e9/86/32/e9863256e032559c9aea6d0fe1e74d79.jpg"
              alt="Location access"
              class="w-100 h-100 object-fit-cover"
            />
          </div>

          <div class="position-relative px-4 pb-3 text-center bg-reactive-secondary">
            <div
              class="position-absolute top-0 start-50 translate-middle h-25 w-100 ratio ratio-1x1 rounded-circle z-0 bg-reactive-secondary"
            ></div>

            <div class="position-relative z-1">
              <h3 class="fw-bold text-reactive-primary fs-3 mb-3">
                {{ $t('permissions.location.title') }}
              </h3>
              <p class="text-reactive-secondary mb-4">
                {{ $t('permissions.location.message') }}
              </p>

              <button
                class="btn btn-primary w-100 fw-semibold py-2 mb-2 text-reactive-primary"
                @click="handleAllow"
              >
                {{ $t('common.action.allow') }}
              </button>
              <button
                class="btn bg-reactive-secondary text-reactive-secondary w-100 fw-semibold py-2"
                @click="handleDeny"
              >
                {{ $t('common.action.deny') }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="modal-backdrop fade show" @click="handleDeny"></div>
  </div>
</template>
