<template>
  <div class="p-4 p-md-5">

    <!-- Header -->
    <div class="d-flex align-items-center gap-3 mb-4">
      <NuxtLink :to="localePath('/organizer/events')" class="btn btn-sm btn-outline-secondary">
        <i class="bi bi-arrow-left"/>
      </NuxtLink>
      <div>
        <h2 class="fw-bold text-reactive-primary mb-0">
          {{ isNew ? $t('organizer.event_form.create_title') : $t('organizer.event_form.edit_title') }}
        </h2>
        <small class="text-reactive-secondary" v-if="!isNew">ID: {{ eventId }}</small>
      </div>
      <div v-if="!isNew" class="ms-auto d-flex align-items-center gap-2">
        <span class="badge" :class="{
          'bg-secondary':         form.status === 'EDITING',
          'bg-primary':           form.status === 'PREMIERE',
          'bg-success':           form.status === 'SELLING',
          'bg-warning text-dark': form.status === 'PAUSED' || form.status === 'ONGOING',
          'bg-danger':            form.status === 'CANCELLED' || form.status === 'FINISHED',
        }">{{ $t(getStatusI18nKey(form.status)) }}</span>
        <Transition name="draft-toast">
          <span v-if="showDraftSaved" class="badge bg-success d-flex align-items-center gap-1">
            <i class="bi bi-check2"/>{{ $t('organizer.event_form.draft_saved') }}
          </span>
        </Transition>
      </div>
    </div>

    <!-- Step Indicators -->
    <div class="d-flex gap-2 mb-5 step-bar">
      <div
          v-for="(step, i) in steps" :key="i"
          class="step-item d-flex align-items-center gap-2 flex-grow-1"
          :class="{ completed: i < currentStep, active: i === currentStep }"
          @click="goToStep(i)"
      >
        <div class="step-dot d-flex align-items-center justify-content-center rounded-circle flex-shrink-0">
          <i v-if="i < currentStep" class="bi bi-check2"/>
          <span v-else>{{ i + 1 }}</span>
        </div>
        <span class="step-label d-none d-md-block small fw-semibold">{{ step }}</span>
        <div v-if="i < steps.length - 1" class="step-line flex-grow-1"/>
      </div>
    </div>

    <!-- ── STEP 1: Basic Info ── -->
    <div v-if="currentStep === 0">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-4">
          <i class="bi bi-info-circle me-2 text-primary"/>{{ $t('organizer.event_form.step1.title') }}
        </h5>
        <div class="row g-4">
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.name') }} <span class="text-danger">*</span></label>
            <input v-model="form.name" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.name }" :placeholder="$t('organizer.event_form.step1.name_placeholder')"/>
            <div class="invalid-feedback">{{ errors.name }}</div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.category') }} <span class="text-danger">*</span></label>
            <select v-model="form.categoryId" class="form-select bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.categoryId }">
              <option value="">{{ $t('organizer.event_form.step1.category_placeholder') }}</option>
              <option v-for="cat in mockCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
            <div class="invalid-feedback">{{ errors.categoryId }}</div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.status') }}</label>
            <select v-model="form.status" class="form-select bg-reactive-primary border-0 text-reactive-primary">
              <option value="EDITING">{{ $t('organizer.events.status.editing') }}</option>
              <option value="SCHEDULED">{{ $t('organizer.events.status.premier') }}</option>
              <option value="PREMIERE">{{ $t('organizer.events.status.premier') }}</option>
              <option value="SELLING">{{ $t('organizer.events.status.selling') }}</option>
              <option value="PAUSED">{{ $t('organizer.events.status.paused') }}</option>
              <option value="ONGOING">{{ $t('organizer.events.status.ongoing') }}</option>
              <option value="FINISHED">{{ $t('organizer.events.status.finished') }}</option>
              <option value="CANCELLED">{{ $t('organizer.events.status.cancelled') }}</option>
            </select>
          </div>
          <div class="col-12">
            <div class="alert alert-info py-2 small mb-0">
              <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step1.session_hint') }}
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.start_date') }} <span class="text-danger">*</span></label>
            <input v-model="form.startDate" type="datetime-local" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.startDate }"/>
            <div class="invalid-feedback">{{ errors.startDate }}</div>
          </div>
          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.end_date') }} <span class="text-danger">*</span></label>
            <input v-model="form.endDate" type="datetime-local" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.endDate }"/>
            <div class="invalid-feedback">{{ errors.endDate }}</div>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.address') }} <span class="text-danger">*</span></label>
            <input v-model="form.addressLine" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.addressLine }" :placeholder="$t('organizer.event_form.step1.address_placeholder')"/>
            <div class="invalid-feedback">{{ errors.addressLine }}</div>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.banner_url') }}</label>
            <input v-model="form.bannerUrl" type="url" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step1.banner_placeholder')"/>
            <div v-if="form.bannerUrl" class="mt-2">
              <img :src="form.bannerUrl" class="rounded" style="max-height:160px;object-fit:cover;width:100%;"/>
            </div>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-end">
        <button class="btn btn-primary px-4" :disabled="saving" @click="saveStep1">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          {{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/>
        </button>
      </div>
    </div>

    <!-- ── STEP 2: Content ── -->
    <div v-if="currentStep === 1">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-4">
          <i class="bi bi-file-text me-2 text-primary"/>{{ $t('organizer.event_form.step2.title') }}
        </h5>
        <div class="row g-4">
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.about') }}</label>
            <ul class="nav nav-tabs mb-3 border-0">
              <li class="nav-item"><button type="button" class="nav-link px-3" :class="{ active: contentLang === 'vi' }" @click="contentLang = 'vi'">🇻🇳 Tiếng Việt</button></li>
              <li class="nav-item"><button type="button" class="nav-link px-3" :class="{ active: contentLang === 'en' }" @click="contentLang = 'en'">🇺🇸 English</button></li>
            </ul>
            <div class="bg-reactive-primary rounded p-3">
              <textarea
                  v-if="contentLang === 'vi'"
                  v-model="content.aboutVi"
                  class="form-control bg-transparent border-0 text-reactive-primary p-0"
                  style="min-height:180px; resize:vertical;"
                  :placeholder="$t('organizer.event_form.step2.about_vi_placeholder')"
              />
              <textarea
                  v-else
                  v-model="content.aboutEn"
                  class="form-control bg-transparent border-0 text-reactive-primary p-0"
                  style="min-height:180px; resize:vertical;"
                  :placeholder="$t('organizer.event_form.step2.about_en_placeholder')"
              />
            </div>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.terms') }}</label>
            <textarea v-model="content.termsAndConditions" rows="4" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step2.terms_placeholder')"/>
          </div>
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.refund') }}</label>
            <textarea v-model="content.policyRefund" rows="3" class="form-control bg-reactive-primary border-0 text-reactive-primary" :placeholder="$t('organizer.event_form.step2.refund_placeholder')"/>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-between gap-2">
        <button class="btn btn-outline-secondary px-4" @click="currentStep = 0"><i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}</button>
        <button class="btn btn-primary px-4" :disabled="saving" @click="saveStep2">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          {{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/>
        </button>
      </div>
    </div>

    <!-- ── STEP 3: Zones ── -->
    <div v-if="currentStep === 2">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <div class="d-flex align-items-center justify-content-between mb-4">
          <h5 class="fw-semibold text-reactive-primary mb-0"><i class="bi bi-grid me-2 text-primary"/>{{ $t('organizer.event_form.step3.title') }}</h5>
          <button class="btn btn-sm btn-primary" @click="openZoneModal(null)"><i class="bi bi-plus-lg me-1"/>{{ $t('organizer.event_form.step3.add_zone') }}</button>
        </div>
        <div v-if="zones.length === 0" class="text-center py-5 text-reactive-secondary">
          <i class="bi bi-grid fs-1 d-block mb-3"/>
          <p>{{ $t('organizer.event_form.step3.empty') }}</p>
        </div>
        <div v-else class="row g-3">
          <div v-for="zone in zones" :key="zone.id" class="col-md-6 col-xl-4">
            <div class="zone-card card border-0 p-3 h-100">
              <div class="d-flex justify-content-between align-items-start mb-2">
                <div>
                  <div class="fw-semibold text-reactive-primary">{{ zone.name }}</div>
                  <small class="text-reactive-secondary">
                    <i :class="zone.isStanding ? 'bi-people' : 'bi-chair'" class="bi me-1"/>
                    {{ zone.isStanding ? $t('organizer.event_form.step3.standing') : $t('organizer.event_form.step3.seated') }}
                  </small>
                </div>
                <div class="d-flex gap-1">
                  <button class="btn btn-sm btn-outline-primary" @click="openZoneModal(zone)"><i class="bi bi-pencil"/></button>
                  <button class="btn btn-sm btn-outline-danger" @click="deleteZone(zone.id)"><i class="bi bi-trash"/></button>
                </div>
              </div>
              <div class="d-flex justify-content-between small text-reactive-secondary">
                <span><i class="bi bi-people me-1"/>{{ zone.capacity }}</span>
                <span class="fw-semibold text-reactive-primary">{{ formatPrice(zone.price) }}</span>
              </div>
              <div v-if="zone.purchaseLimit" class="small text-reactive-secondary mt-1">
                <i class="bi bi-ticket me-1"/>Max {{ zone.purchaseLimit }} / {{ $t('common.ticket') }}
              </div>
              <div v-if="zone.perks?.length" class="mt-2 d-flex flex-wrap gap-1">
                <span v-for="perk in zone.perks" :key="perk" class="badge small perk-badge">{{ perk }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-between gap-2">
        <button class="btn btn-outline-secondary px-4" @click="currentStep = 1"><i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}</button>
        <button class="btn btn-primary px-4" @click="currentStep = 3">{{ $t('organizer.event_form.save_next') }} <i class="bi bi-arrow-right ms-1"/></button>
      </div>
    </div>

    <!-- ── STEP 4: Layout ── -->
    <div v-if="currentStep === 3">

      <!-- No zones warning -->
      <div v-if="zones.length === 0" class="alert alert-warning d-flex align-items-center gap-2 mb-4">
        <i class="bi bi-exclamation-triangle-fill"/>
        <span>{{ $t('organizer.event_form.step4.no_zones_warning') }}</span>
      </div>

      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-3">
          <i class="bi bi-layers me-2 text-primary"/>{{ $t('organizer.event_form.step4.title') }}
        </h5>

        <!-- Mode toggle -->
        <div class="btn-group mb-4">
          <button class="btn" :class="layoutMode === 'venue' ? 'btn-primary' : 'btn-outline-secondary'" @click="layoutMode = 'venue'">
            <i class="bi bi-building me-2"/>{{ $t('organizer.event_form.step4.venue_mode') }}
          </button>
          <button class="btn" :class="layoutMode === 'custom' ? 'btn-primary' : 'btn-outline-secondary'" @click="layoutMode = 'custom'">
            <i class="bi bi-pencil-square me-2"/>{{ $t('organizer.event_form.step4.custom_mode') }}
          </button>
        </div>

        <!-- ── VENUE MODE ── -->
        <div v-if="layoutMode === 'venue'">

          <!-- Venue picker -->
          <div class="mb-4">
            <label class="form-label small fw-semibold text-reactive-secondary">
              {{ $t('organizer.event_form.step4.venue') }} <span class="text-danger">*</span>
            </label>
            <select v-model="form.venueId" class="form-select bg-reactive-primary border-0 text-reactive-primary">
              <option value="">— {{ $t('organizer.event_form.step4.venue') }} —</option>
              <option v-for="v in mockVenues" :key="v.id" :value="v.id">{{ v.name }} — {{ v.addressLine }}</option>
            </select>
            <div v-if="selectedVenue" class="mt-2 d-flex align-items-center gap-2">
              <img :src="selectedVenue.imageUrl" class="rounded" style="width:48px;height:32px;object-fit:cover;"/>
              <small class="text-reactive-secondary"><i class="bi bi-geo-alt me-1"/>{{ selectedVenue.addressLine }}</small>
            </div>
          </div>

          <!-- Venue layout preview + zone linking (side by side like original) -->
          <div v-if="selectedVenue" class="row g-4">

            <!-- Left: layout preview placeholder -->
            <div class="col-lg-7">
              <div class="fw-semibold text-reactive-primary mb-2">{{ selectedVenue.name }}</div>
              <div class="layout-preview-placeholder bg-reactive-primary rounded d-flex align-items-center justify-content-center" style="height:340px;">
                <div class="text-center text-reactive-secondary">
                  <i class="bi bi-grid-3x3 fs-1 d-block mb-3 opacity-25"/>
                  <small class="opacity-50">{{ $t('organizer.event_form.step4.venue_no_layout') }}</small>
                </div>
              </div>
            </div>

            <!-- Right: zone linking -->
            <div class="col-lg-5">
              <div class="fw-semibold text-reactive-primary mb-3">
                <i class="bi bi-link-45deg me-2 text-primary"/>{{ $t('organizer.event_form.step4.link_zones') }}
              </div>
              <div v-if="zones.length === 0" class="alert alert-warning py-2 small">
                <i class="bi bi-exclamation-triangle me-1"/>{{ $t('organizer.event_form.step4.no_zones_link') }}
              </div>
              <div v-else>
                <p class="small text-reactive-secondary mb-3">{{ $t('organizer.event_form.step4.link_description') }}</p>
                <div v-for="vz in selectedVenue.zoneNames" :key="vz" class="d-flex align-items-center gap-2 mb-2">
                  <small class="text-reactive-primary fw-semibold text-truncate" style="min-width:130px;">{{ vz }}</small>
                  <i class="bi bi-arrow-right text-reactive-secondary flex-shrink-0"/>
                  <select class="form-select form-select-sm bg-reactive-primary border-0 text-reactive-primary flex-grow-1">
                    <option value="">{{ $t('organizer.event_form.step4.decorative') }}</option>
                    <optgroup :label="$t('organizer.event_form.step4.seated_zones')">
                      <option v-for="z in zones.filter(z => !z.isStanding)" :key="z.id" :value="z.id">{{ z.name }}</option>
                    </optgroup>
                    <optgroup :label="$t('organizer.event_form.step4.standing_zones')">
                      <option v-for="z in zones.filter(z => z.isStanding)" :key="z.id" :value="z.id">{{ z.name }}</option>
                    </optgroup>
                  </select>
                </div>
                <div class="alert alert-info py-2 small mt-3 mb-0">
                  <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step4.link_hint') }}
                </div>
              </div>
            </div>

          </div>

          <!-- No venue selected yet -->
          <div v-else class="alert alert-warning py-2 small">
            <i class="bi bi-exclamation-triangle me-1"/>{{ $t('organizer.event_form.step4.no_venue') }}
          </div>

        </div>

        <!-- ── CUSTOM MODE ── -->
        <div v-else>
          <!-- Floor tabs placeholder -->
          <div class="d-flex align-items-center gap-2 mb-3">
            <button class="btn btn-sm btn-primary">Floor 1</button>
            <button class="btn btn-sm btn-outline-primary">
              <i class="bi bi-plus-lg me-1"/>{{ $t('organizer.event_form.step4.add_floor') }}
            </button>
          </div>

          <!-- Canvas placeholder -->
          <div
              class="canvas-placeholder bg-reactive-primary rounded position-relative overflow-hidden"
              style="height:600px; background: repeating-linear-gradient(0deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px), repeating-linear-gradient(90deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px);"
          >
            <!-- Stage bar placeholder -->
            <div class="position-absolute start-50 translate-middle-x bg-warning rounded d-flex align-items-center justify-content-center" style="top:20px;width:260px;height:44px;">
              <small class="fw-bold text-dark">{{ $t('organizer.event_form.step4.stage_screen') }}</small>
            </div>
            <!-- Zone palette -->
            <div v-if="zones.length > 0" class="position-absolute bottom-0 start-0 end-0 p-3 d-flex gap-2 flex-wrap" style="background:rgba(0,0,0,0.3);">
              <small class="text-reactive-secondary me-1 align-self-center">{{ $t('organizer.event_form.step4.place_zone') }}</small>
              <span v-for="zone in zones" :key="zone.id" class="badge zone-palette-badge">{{ zone.name }}</span>
            </div>
            <!-- Centre hint -->
            <div class="position-absolute top-50 start-50 translate-middle text-center text-reactive-secondary" style="pointer-events:none;">
              <i class="bi bi-pencil-square fs-1 d-block mb-2 opacity-25"/>
              <small class="opacity-50">{{ $t('organizer.event_form.step4.hint') }}</small>
            </div>
          </div>

          <!-- Property bar placeholder -->
          <div class="p-3 border-top border-secondary">
            <small class="text-reactive-secondary">
              <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step4.hint') }}
            </small>
          </div>
        </div>

      </div>

      <div class="d-flex justify-content-between gap-2 mt-2">
        <button class="btn btn-outline-secondary px-4" @click="currentStep = 2">
          <i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}
        </button>
        <div class="d-flex gap-2">
          <button class="btn btn-primary px-4" :disabled="saving" @click="mockSaveLayout">
            <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
            <i v-else class="bi bi-floppy me-2"/>{{ $t('organizer.event_form.step4.save_layout') }}
          </button>
          <button class="btn btn-success px-4" @click="router.push(localePath('/organizer/events'))">
            <i class="bi bi-check2 me-2"/>{{ $t('organizer.event_form.step4.save_close') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Error toast -->
    <div v-if="globalError" class="alert alert-danger position-fixed bottom-0 end-0 m-4" style="z-index:2000;max-width:360px;">
      <i class="bi bi-exclamation-circle me-2"/>{{ globalError }}
      <button class="btn-close float-end" @click="globalError = ''"/>
    </div>

    <!-- Zone Modal -->
    <div v-if="showZoneModal" class="modal-backdrop-custom" @click.self="showZoneModal = false">
      <div class="modal-box bg-reactive-secondary p-4 rounded-3 shadow-lg">
        <h5 class="text-reactive-primary fw-bold mb-4">
          {{ editingZone ? $t('organizer.event_form.step3.edit_zone') : $t('organizer.event_form.step3.add_zone') }}
        </h5>
        <div class="row g-3">
          <div class="col-12">
            <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.zone_name') }} *</label>
            <input v-model="zoneForm.name" type="text" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
          </div>
          <div class="col-12">
            <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.type') }}</label>
            <select v-model="zoneForm.isStanding" class="form-select bg-reactive-primary border-0 text-reactive-primary">
              <option :value="false">{{ $t('organizer.event_form.step3.seated') }}</option>
              <option :value="true">{{ $t('organizer.event_form.step3.standing') }}</option>
            </select>
          </div>
          <div class="col-md-6" v-if="zoneForm.isStanding">
            <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.capacity') }} *</label>
            <input v-model.number="zoneForm.capacity" type="number" min="1" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
          </div>
          <template v-else>
            <div class="col-6">
              <label class="form-label small text-reactive-secondary">Rows</label>
              <div class="d-flex align-items-center gap-2">
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridRows = Math.max(1, zoneForm.gridRows - 1)">−</button>
                <input v-model.number="zoneForm.gridRows" type="number" min="1" max="52" class="form-control bg-reactive-primary border-0 text-reactive-primary text-center"/>
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridRows = Math.min(52, zoneForm.gridRows + 1)">+</button>
              </div>
            </div>
            <div class="col-6">
              <label class="form-label small text-reactive-secondary">Seats / row</label>
              <div class="d-flex align-items-center gap-2">
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridCols = Math.max(1, zoneForm.gridCols - 1)">−</button>
                <input v-model.number="zoneForm.gridCols" type="number" min="1" max="200" class="form-control bg-reactive-primary border-0 text-reactive-primary text-center"/>
                <button type="button" class="btn btn-sm btn-outline-secondary px-2" @click="zoneForm.gridCols = Math.min(200, zoneForm.gridCols + 1)">+</button>
              </div>
            </div>
            <div class="col-12">
              <small class="text-reactive-secondary">
                <i class="bi bi-info-circle me-1"/>Total: <span class="text-reactive-primary fw-semibold">{{ zoneForm.gridRows * zoneForm.gridCols }}</span> seats
              </small>
            </div>
          </template>
          <div class="col-md-6">
            <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.price') }} *</label>
            <div class="input-group">
              <input v-model.number="zoneForm.price" type="number" min="0" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
              <span class="input-group-text bg-reactive-primary border-0 text-reactive-secondary">₫</span>
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label small text-reactive-secondary">{{ $t('organizer.event_form.step3.purchase_limit') }}</label>
            <input v-model.number="zoneForm.purchaseLimit" type="number" min="1" class="form-control bg-reactive-primary border-0 text-reactive-primary"/>
          </div>
        </div>
        <div class="d-flex gap-2 justify-content-end mt-4">
          <button class="btn btn-outline-secondary" @click="showZoneModal = false">{{ $t('common.cancel') }}</button>
          <button class="btn btn-primary" @click="saveZone">{{ $t('common.save') }}</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import {
  mockEvents,
  mockCategories,
  mockVenues,
  type MockZone,
  getStatusI18nKey,
  formatPrice,
} from '../mock.data'

const { t: $t } = useI18n()
const localePath = useLocalePath()
const route  = useRoute()
const router = useRouter()

const eventId = computed(() => {
  const id = route.params.id as string
  return id === 'new' ? null : id
})
const isNew = computed(() => !eventId.value)

// ── Steps ──────────────────────────────────────────────────
const currentStep = ref(0)
const steps = computed(() => [
  $t('organizer.event_form.step1.title'),
  $t('organizer.event_form.step2.title'),
  $t('organizer.event_form.step3.title'),
  $t('organizer.event_form.step4.title'),
])
const goToStep = (i: number) => { if (!isNew.value || i <= currentStep.value) currentStep.value = i }

const saving         = ref(false)
const globalError    = ref('')
const showDraftSaved = ref(false)
const triggerDraftSaved = () => {
  showDraftSaved.value = true
  setTimeout(() => { showDraftSaved.value = false }, 2500)
}

// ── Venue derived ──────────────────────────────────────────
const selectedVenue = computed(() => mockVenues.find(v => v.id === form.value.venueId) ?? null)

// ── Form state ─────────────────────────────────────────────
const form = ref({
  name: '', categoryId: '' as any, status: 'EDITING' as string,
  addressLine: '', startDate: '', endDate: '', bannerUrl: '', venueId: '',
})
const errors      = ref<Record<string, string>>({})
const content     = ref({ aboutVi: '', aboutEn: '', termsAndConditions: '', policyRefund: '' })
const contentLang = ref<'vi' | 'en'>('vi')
const layoutMode  = ref<'venue' | 'custom'>('venue')

// ── Zones ──────────────────────────────────────────────────
const zones = ref<MockZone[]>([])

// ── Zone modal ─────────────────────────────────────────────
const showZoneModal = ref(false)
const editingZone   = ref<MockZone | null>(null)
const zoneForm      = ref({ name: '', isStanding: false, capacity: 100, price: 0, purchaseLimit: null as number | null, gridRows: 5, gridCols: 10 })

const openZoneModal = (zone: MockZone | null) => {
  editingZone.value = zone
  if (zone) {
    Object.assign(zoneForm.value, {
      name: zone.name, isStanding: zone.isStanding, capacity: zone.capacity,
      price: zone.price, purchaseLimit: zone.purchaseLimit ?? null,
      gridRows: zone.gridRows ?? 5, gridCols: zone.gridCols ?? 10,
    })
  } else {
    Object.assign(zoneForm.value, { name: '', isStanding: false, capacity: 100, price: 0, purchaseLimit: null, gridRows: 5, gridCols: 10 })
  }
  showZoneModal.value = true
}

const saveZone = () => {
  if (!zoneForm.value.name.trim()) return
  const capacity = zoneForm.value.isStanding
      ? zoneForm.value.capacity
      : zoneForm.value.gridRows * zoneForm.value.gridCols
  if (editingZone.value) {
    const idx = zones.value.findIndex(z => z.id === editingZone.value!.id)
    if (idx >= 0) zones.value[idx] = { ...zones.value[idx], ...zoneForm.value, capacity }
  } else {
    zones.value.push({ id: 'z-new-' + Date.now(), ...zoneForm.value, capacity, perks: [], quantitySold: 0 })
  }
  showZoneModal.value = false
}

const deleteZone = (id: string) => { zones.value = zones.value.filter(z => z.id !== id) }

// ── Load existing event ────────────────────────────────────
onMounted(() => {
  if (!isNew.value && eventId.value) {
    const ev = mockEvents.find(e => e.id === eventId.value)
    if (ev) {
      form.value = {
        name: ev.name, categoryId: ev.categoryId, status: ev.status,
        addressLine: ev.addressLine, bannerUrl: ev.bannerUrl, venueId: ev.venueId ?? '',
        startDate: ev.session.startDate.slice(0, 16),
        endDate:   ev.session.endDate.slice(0, 16),
      }
      content.value = {
        aboutVi: ev.aboutVi ?? '',
        aboutEn: ev.aboutEn ?? '',
        termsAndConditions: ev.termsAndConditions ?? '',
        policyRefund: ev.policyRefund ?? '',
      }
      zones.value = ev.zones.map(z => ({ ...z }))
    }
  }
})

// ── Step saves (mock — simulated delay) ───────────────────
const validateStep1 = () => {
  errors.value = {}
  if (!form.value.name.trim())        errors.value.name        = $t('organizer.event_form.step1.name') + ' is required'
  if (!form.value.categoryId)         errors.value.categoryId  = $t('organizer.event_form.step1.category') + ' is required'
  if (!form.value.addressLine.trim()) errors.value.addressLine = $t('organizer.event_form.step1.address') + ' is required'
  if (!form.value.startDate)          errors.value.startDate   = $t('organizer.event_form.step1.start_date') + ' is required'
  if (!form.value.endDate)            errors.value.endDate     = $t('organizer.event_form.step1.end_date') + ' is required'
  if (form.value.startDate && form.value.endDate && form.value.endDate <= form.value.startDate)
    errors.value.endDate = 'End date must be after start date'
  return Object.keys(errors.value).length === 0
}

const saveStep1 = async () => {
  if (!validateStep1()) return
  saving.value = true
  await new Promise(r => setTimeout(r, 600))
  saving.value = false
  triggerDraftSaved()
  currentStep.value = 1
}

const saveStep2 = async () => {
  saving.value = true
  await new Promise(r => setTimeout(r, 600))
  saving.value = false
  triggerDraftSaved()
  currentStep.value = 2
}

const mockSaveLayout = async () => {
  saving.value = true
  await new Promise(r => setTimeout(r, 800))
  saving.value = false
  triggerDraftSaved()
}
</script>

<style scoped>
.step-bar { align-items: center; }
.step-item { cursor: pointer; min-width: 0; }
.step-dot { width: 32px; height: 32px; font-size: 0.8rem; font-weight: 700; flex-shrink: 0; background: rgba(var(--bs-secondary-rgb), 0.3); color: var(--bs-secondary); transition: background 0.2s, color 0.2s; }
.step-item.active .step-dot    { background: var(--bs-primary); color: #fff; }
.step-item.completed .step-dot { background: #22c55e; color: #fff; }
.step-label { color: var(--bs-secondary); transition: color 0.2s; }
.step-item.active .step-label, .step-item.completed .step-label { color: var(--text-reactive-primary, inherit); }
.step-line { height: 2px; background: rgba(var(--bs-secondary-rgb), 0.25); flex-shrink: 0; min-width: 8px; }
.step-item.completed .step-line { background: #22c55e; }
.zone-card { background: rgba(var(--bs-secondary-rgb), 0.15); border-left: 4px solid #6366f1 !important; transition: box-shadow 0.15s; }
.zone-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.12); }
.perk-badge { background: rgba(99,102,241,0.25); color: #a5b4fc; border: 1px solid rgba(99,102,241,0.4); }
.modal-backdrop-custom { position: fixed; inset: 0; background: rgba(0,0,0,0.55); z-index: 1050; display: flex; align-items: center; justify-content: center; padding: 1rem; overflow-y: auto; }
.modal-box { max-width: 520px; width: 100%; }
.nav-link { color: var(--bs-secondary); background: none; border: none; border-bottom: 2px solid transparent; border-radius: 0; padding: 0.5rem 1rem; cursor: pointer; }
.nav-link.active { color: var(--bs-primary); border-bottom-color: var(--bs-primary); }
.draft-toast-enter-active { transition: opacity 0.3s ease; }
.draft-toast-leave-active { transition: opacity 0.8s ease; }
.draft-toast-enter-from, .draft-toast-leave-to { opacity: 0; }
.zone-palette-badge { background: transparent; border: 1px solid rgba(var(--bs-secondary-rgb), 0.4); border-radius: 6px; padding: 4px 10px; font-size: 0.8rem; color: var(--bs-secondary); cursor: default; }
.layout-preview-placeholder { border: 1px dashed rgba(var(--bs-secondary-rgb), 0.3); }
</style>