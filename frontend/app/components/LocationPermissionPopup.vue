<script setup lang="ts">
const { requestLocation, permissionStatus, isInitialized, setPermissionDenied } = useLocation();

const isVisible = computed(() => isInitialized.value && permissionStatus.value === null);

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
      aria-labelledby="location-modal-title"
      @click.self="handleDeny"
    >
      <div class="modal-dialog modal-dialog-centered shadow-sm">
        <div class="modal-content border-0 rounded-4 overflow-hidden w-75 mx-auto">
          <div class="ratio ratio-16x9">
            <shimmer-img :src="'https://cdn.ticket4u.uk/image/upload/city-map_cqjpt1.jpg'" />
          </div>

          <div class="position-relative px-4 pb-3 text-center bg-reactive-primary">
            <div
              class="position-absolute top-0 start-50 translate-middle h-25 w-100 ratio ratio-1x1 rounded-circle z-0 bg-reactive-primary"
            ></div>

            <div class="position-relative z-1">
              <h3 id="location-modal-title" class="fw-bold fs-3 mb-3">
                {{ $t('permissions.location.title') }}
              </h3>
              <p class="text-reactive-secondary mb-4">
                {{ $t('permissions.location.message') }}
              </p>

              <button class="btn btn-primary w-100 fw-semibold py-2 mb-2" @click="handleAllow">
                {{ $t('common.action.allow') }}
              </button>
              <button class="btn btn-reactive-gray w-100 fw-semibold py-2" @click="handleDeny">
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
