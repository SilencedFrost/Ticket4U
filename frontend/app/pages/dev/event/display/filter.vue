<script setup lang="ts">
import FilterBar from '~/features/filter/components/FilterBar.vue';
import { EventStatus } from '~/features/event/types/Event';
import type { FilterStatusOption, MainFilterSection } from '~/features/filter/types';

const { t } = useI18n();

const mainSections: MainFilterSection[] = ['price', 'category', 'status'];

const ACTIVE_STATUS_KEYS = [
  EventStatus.PREMIERE,
  EventStatus.SELLING,
  EventStatus.ONGOING,
  EventStatus.FINISHED,
];

const statusOptions = computed<FilterStatusOption[]>(() =>
  ACTIVE_STATUS_KEYS.map((status) => ({
    value: status,
    label: t(`status.status_options.${status.toLowerCase()}`),
  })),
);
</script>

<template>
  <div class="p-3">
    <ClientOnly>
      <FilterBar :main-sections="mainSections" :status-options="statusOptions" />
    </ClientOnly>
  </div>
</template>
