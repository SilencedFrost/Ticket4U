<script setup lang="ts">
const props = withDefaults(
    defineProps<{
        labelKey?: string;
    }>(),
    {
        labelKey: 'settings.personal_information.actions.save',
    },
);
</script>

<template>
    <button class="btn btn-primary d-flex align-items-center gap-2">
        {{ $t(props.labelKey) }}
    </button>
</template>