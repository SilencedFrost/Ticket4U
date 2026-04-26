<script setup lang="ts">
export type ModalPreset = 'confirm' | 'yes-no' | 'ok' | 'ok-cancel'
export type ModalAction = 'confirm' | 'cancel' | 'close'

export interface ModalButton {
  label: string
  variant?: string // Bootstrap btn class: 'btn-primary', 'btn-reactive-gray', etc.
  action: ModalAction
}

const PRESETS: Record<ModalPreset, ModalButton[]> = {
  confirm: [
    { label: 'Cancel', variant: 'btn-reactive-gray', action: 'cancel' },
    { label: 'Confirm', variant: 'btn-primary', action: 'confirm' },
  ],
  'yes-no': [
    { label: 'No', variant: 'btn-reactive-gray', action: 'cancel' },
    { label: 'Yes', variant: 'btn-primary', action: 'confirm' },
  ],
  ok: [
    { label: 'OK', variant: 'btn-primary', action: 'confirm' },
  ],
  'ok-cancel': [
    { label: 'Cancel', variant: 'btn-reactive-gray', action: 'cancel' },
    { label: 'OK', variant: 'btn-primary', action: 'confirm' },
  ],
}

const props = withDefaults(
  defineProps<{
    show: boolean
    title?: string
    preset?: ModalPreset
    buttons?: ModalButton[]
    closable?: boolean
    size?: 'sm' | 'md' | 'lg'
  }>(),
  {
    title: undefined,
    buttons: undefined,
    preset: 'confirm',
    closable: true,
    size: 'md',
  },
)

const emit = defineEmits<{
  confirm: []
  cancel: []
  close: []
}>()

const resolvedButtons = computed<ModalButton[]>(() => props.buttons ?? PRESETS[props.preset])

const sizeClass = computed(() => ({
  sm: 'modal-sm',
  md: '',
  lg: 'modal-lg',
}[props.size]))

function handle(action: ModalAction) {
  if (action === 'confirm') emit('confirm')
  else if (action === 'cancel') emit('cancel')
  else emit('close')
}
</script>

<template>
  <Teleport to="body">
    <Transition name="app-modal">
      <div
        v-if="show"
        class="modal d-block"
        tabindex="-1"
        @click.self="closable && emit('close')"
      >
        <div class="modal-dialog modal-dialog-centered" :class="sizeClass">
          <div class="app-modal pe-auto bg-reactive-primary text-reactive-primary rounded-3 p-4 shadow">
            <!-- Title row -->
            <div class="d-flex align-items-center mb-3" :class="title ? 'justify-content-between' : 'justify-content-end'">
              <h5 v-if="title" class="fw-bold mb-0 me-3">{{ title }}</h5>
              <button
                v-if="closable"
                type="button"
                class="btn-close flex-shrink-0"
                @click="emit('close')"
              />
            </div>

            <!-- Body slot -->
            <div class="text-reactive-secondary mb-4">
              <slot />
            </div>

            <!-- Buttons -->
            <div class="d-flex gap-2 justify-content-end">
              <button
                v-for="btn in resolvedButtons"
                :key="btn.action"
                type="button"
                class="btn"
                :class="btn.variant ?? 'btn-secondary'"
                @click="handle(btn.action)"
              >
                {{ btn.label }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </Transition>

    <Transition name="app-modal">
      <div v-if="show" class="modal-backdrop show" />
    </Transition>
  </Teleport>
</template>

<style scoped>
.app-modal-enter-active,
.app-modal-leave-active {
  transition: opacity 0.15s ease;
}
.app-modal-enter-from,
.app-modal-leave-to {
  opacity: 0;
}

</style>