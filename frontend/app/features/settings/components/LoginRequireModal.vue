<script setup lang="ts">
const props = defineProps<{
  show: boolean;
  targetPath: string; // e.g. '/settings/account' (không có locale prefix)
}>();
const emit = defineEmits<{ close: [] }>();

const localePath = useLocalePath();
const router = useRouter();

function goToLogin() {
  const loginPath = localePath('/auth/login');
  router.push(`${loginPath}?redirect=${encodeURIComponent(props.targetPath)}`);
  emit('close');
}
</script>

<template>
  <Teleport to="body">
    <Transition name="fade">
      <div v-if="show" class="modal d-block" tabindex="-1" @click.self="emit('close')">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content bg-reactive-primary text-reactive-primary">
            <div class="modal-header border-secondary">
              <h5 class="modal-title fw-bold">{{ $t('settings.login_required.title') }}</h5>
              <button type="button" class="btn-close" :aria-label="$t('common.close')" @click="emit('close')" />
            </div>
            <div class="modal-body text-reactive-secondary">
              {{ $t('settings.login_required.message') }}
            </div>
            <div class="modal-footer border-secondary">
              <button type="button" class="btn btn-secondary" @click="emit('close')">
                {{ $t('settings.login_required.cancel') }}
              </button>
              <button type="button" class="btn btn-primary" @click="goToLogin">
                {{ $t('settings.login_required.confirm') }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </Transition>
    <Transition name="fade">
      <div v-if="show" class="modal-backdrop show" />
    </Transition>
  </Teleport>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>