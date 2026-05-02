<script setup lang="ts">
import { FOOTER_CONTACT, FOOTER_SECTIONS, type FooterSectionId } from './footerData';

const openSection = ref<FooterSectionId | null>(null);

function toggleSection(section: FooterSectionId) {
  openSection.value = openSection.value === section ? null : section;
}
</script>

<template>
  <div id="footer-accordion" class="accordion accordion-flush" data-bs-theme="dark">
    <div v-for="section in FOOTER_SECTIONS" :key="section.id" class="accordion-item bg-transparent">
      <h2 :id="`footer-accordion-heading-${section.id}`" class="accordion-header">
        <button
          type="button"
          class="accordion-button px-0 bg-transparent text-white shadow-none"
          :class="openSection === section.id ? '' : 'collapsed'"
          :aria-expanded="openSection === section.id"
          :aria-controls="`footer-accordion-collapse-${section.id}`"
          @click="toggleSection(section.id)"
        >
          <span
            class="d-inline-block pb-2 border-bottom border-3 border-primary text-uppercase fw-bold"
          >
            {{ $t(section.titleKey) }}
          </span>
        </button>
      </h2>

      <div
        :id="`footer-accordion-collapse-${section.id}`"
        class="accordion-collapse collapse"
        :class="{ show: openSection === section.id }"
        :aria-labelledby="`footer-accordion-heading-${section.id}`"
      >
        <div class="accordion-body px-0 pt-3 pb-4">
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
              <div class="fw-bold text-white">{{ FOOTER_CONTACT.phoneText }}</div>
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
    </div>
  </div>
</template>
