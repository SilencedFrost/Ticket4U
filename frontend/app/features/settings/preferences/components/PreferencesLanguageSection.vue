<script setup lang="ts">
interface LanguageOption {
  value: string;
  label: string;
}

defineProps<{
  options: LanguageOption[];
}>();

const model = defineModel<string>({ required: true });
</script>

<template>
  <section class="d-flex flex-column gap-3">
    <div>
      <h2 class="h6 fw-bold text-primary mb-1 d-flex align-items-center gap-2">
        <i class="bi bi-translate" aria-hidden="true"></i>
        {{ $t('settings.preferences.language.title') }}
      </h2>
      <p class="small text-reactive-secondary mb-0">
        {{ $t('settings.preferences.language.subtitle') }}
      </p>
    </div>

    <select
      v-model="model"
      class="form-select bg-reactive-primary text-reactive-primary border-secondary-subtle"
      :aria-label="$t('settings.preferences.language.title')"
    >
      <option v-for="option in options" :key="option.value" :value="option.value">
        {{ option.label }}
      </option>
    </select>
  </section>
</template>