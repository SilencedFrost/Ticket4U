<script setup lang="ts">
import { FOOTER_CONTACT, FOOTER_SECTIONS } from './footerData';
</script>

<template>
  <div class="row g-5">
    <div v-for="section in FOOTER_SECTIONS" :key="section.id" class="col-6 col-lg-3">
      <h6 class="mb-4 text-uppercase fw-bold">
        <span class="d-inline-block pb-3 border-bottom border-3 border-primary">
          {{ $t(section.titleKey) }}
        </span>
      </h6>

      <ul v-if="section.type === 'links'" class="list-unstyled d-flex flex-column gap-3 mb-0">
        <li v-for="item in section.items" :key="item.labelKey">
          <a
            :href="item.href"
            class="text-clickable user-select-auto text-decoration-none d-inline-flex align-items-center gap-2"
          >
            <span aria-hidden="true">•</span>
            <span>{{ $t(item.labelKey) }}</span>
          </a>
        </li>
      </ul>

      <div v-else>
        <div class="mb-3">
          <div class="small text-uppercase fw-bold text-reactive-secondary mb-1">
            {{ $t(FOOTER_CONTACT.phoneLabelKey) }}
          </div>
          <div class="fw-bold">{{ FOOTER_CONTACT.phoneText }}</div>
        </div>

        <div>
          <div class="small text-uppercase fw-bold text-reactive-secondary mb-1">
            {{ $t(FOOTER_CONTACT.emailLabelKey) }}
          </div>
          <a
            :href="`mailto:${FOOTER_CONTACT.email}`"
            class="link-primary text-decoration-none fw-bold"
          >
            {{ FOOTER_CONTACT.email }}
          </a>
        </div>
      </div>
    </div>
  </div>
</template>
