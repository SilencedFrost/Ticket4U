<script setup lang="ts">
import type { ModalButton } from '~/components/AppModal.vue';

const props = defineProps<{
  show: boolean
  targetPath: string // e.g. '/settings/account' (không có locale prefix)
}>()
const emit = defineEmits<{ close: [] }>()

const { t } = useI18n()
const localePath = useLocalePath()
// const router = useRouter()

function goToLogin() {
  navigateTo({
    path: localePath('/auth/login'),
    query: { redirect: props.targetPath },
  })
}

const buttons = computed<ModalButton[]>(() => [
  { label: t('settings.login_required.cancel'), variant: 'btn-reactive-gray', action: 'cancel' },
  { label: t('settings.login_required.confirm'), variant: 'btn-primary', action: 'confirm' },
])
</script>

<template>
  <AppModal
    :show="show"
    :title="$t('settings.login_required.title')"
    :buttons="buttons"
    @confirm="goToLogin"
    @cancel="emit('close')"
    @close="emit('close')"
  >
    {{ $t('settings.login_required.message') }}
  </AppModal>
</template>