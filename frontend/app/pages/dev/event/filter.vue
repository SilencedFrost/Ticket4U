<script setup lang="ts">
import FilterBar from '~/features/filter/components/FilterBar.vue';
import { EventStatus } from '~/features/event/types/Event';
import type { FilterStatusOption, MainFilterSection } from '~/features/filter/types';

const { t } = useI18n();

const mainSections: MainFilterSection[] = ['price', 'category', 'status'];

const statusOptions = computed<FilterStatusOption[]>(() => [
  {
    value: EventStatus.PREMIERE,
    label: t('status.status_options.premiere'),
  },
  {
    value: EventStatus.SELLING,
    label: t('status.status_options.selling'),
  },
  {
    value: EventStatus.ONGOING,
    label: t('status.status_options.ongoing'),
  },
  {
    value: EventStatus.FINISHED,
    label: t('status.status_options.finished'),
  },
]);
</script>

<template>
  <div class="p-3">
    <ClientOnly>
      <FilterBar :main-sections="mainSections" :status-options="statusOptions" />
    </ClientOnly>
  </div>
</template>
