<template>
  <div class="p-4 p-md-5">

    <!-- Header -->
    <div class="d-flex align-items-center gap-3 mb-4">
      <NuxtLink to="/organizer/events" class="btn btn-sm btn-outline-secondary">
        <i class="bi bi-arrow-left"/>
      </NuxtLink>
      <div>
        <h2 class="fw-bold text-reactive-primary mb-0">
          {{ isNew ? $t('organizer.event_form.create_title') : $t('organizer.event_form.edit_title') }}
        </h2>
        <small class="text-reactive-secondary" v-if="eventId">ID: {{ eventId }}</small>
      </div>
      <span v-if="savedEventId" class="badge bg-success ms-auto">
        <i class="bi bi-check2 me-1"/>{{ $t('organizer.event_form.draft_saved') }}
      </span>
    </div>

    <!-- Step Indicators -->
    <div class="d-flex gap-2 mb-5 step-bar">
      <div
        v-for="(step, i) in steps"
        :key="i"
        class="step-item d-flex align-items-center gap-2 flex-grow-1"
        :class="{ completed: i < currentStep, active: i === currentStep }"
        @click="goToStep(i)"
      >
        <div class="step-dot d-flex align-items-center justify-content-center rounded-circle flex-shrink-0">
          <i v-if="i < currentStep" class="bi bi-check2"/>
          <span v-else>{{ i + 1 }}</span>
        </div>
        <span class="step-label d-none d-md-block small fw-semibold">{{ step.label }}</span>
        <div v-if="i < steps.length - 1" class="step-line flex-grow-1"/>
      </div>
    </div>

    <!-- ── STEP 1: Basic Info ───────────────────────────── -->
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

          <!-- Venue picker -->
          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary">Venue <span class="text-danger">*</span></label>
            <select v-model="form.venueId" class="form-select bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.venueId }">
              <option value="">— Select a venue —</option>
              <option v-for="v in venues" :key="v.id" :value="v.id">{{ v.name }} — {{ v.addressLine }}</option>
            </select>
            <div class="invalid-feedback">{{ errors.venueId }}</div>
            <div v-if="selectedVenue" class="mt-2 d-flex align-items-center gap-2">
              <img v-if="selectedVenue.imageUrl" :src="selectedVenue.imageUrl" class="rounded" style="width:48px;height:32px;object-fit:cover;"/>
              <small class="text-reactive-secondary"><i class="bi bi-geo-alt me-1"/>{{ selectedVenue.addressLine }}</small>
            </div>
          </div>

          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.category') }} <span class="text-danger">*</span></label>
            <select v-model="form.categoryId" class="form-select bg-reactive-primary border-0 text-reactive-primary" :class="{ 'is-invalid': errors.categoryId }">
              <option value="">{{ $t('organizer.event_form.step1.category_placeholder') }}</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
            <div class="invalid-feedback">{{ errors.categoryId }}</div>
          </div>

          <div class="col-md-6">
            <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step1.status') }}</label>
            <select v-model="form.status" class="form-select bg-reactive-primary border-0 text-reactive-primary">
              <option value="EDITING">{{ $t('organizer.events.status.editing') }}</option>
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

    <!-- ── STEP 2: Content ──────────────────────────────── -->
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
            <ClientOnly>
              <RichTextEditor v-if="contentLang === 'vi'" v-model="content.aboutVi" :placeholder="$t('organizer.event_form.step2.about_vi_placeholder')" :min-height="240"/>
              <RichTextEditor v-else v-model="content.aboutEn" :placeholder="$t('organizer.event_form.step2.about_en_placeholder')" :min-height="240"/>
              <template #fallback><div class="form-control bg-reactive-primary border-0" style="min-height:240px;"/></template>
            </ClientOnly>
          </div>

          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.terms') }}</label>
            <ClientOnly>
              <RichTextEditor v-model="content.termsAndConditions" :placeholder="$t('organizer.event_form.step2.terms_placeholder')" :min-height="180"/>
              <template #fallback><div class="form-control bg-reactive-primary border-0" style="min-height:180px;"/></template>
            </ClientOnly>
          </div>

          <div class="col-12">
            <label class="form-label small fw-semibold text-reactive-secondary mb-2">{{ $t('organizer.event_form.step2.refund') }}</label>
            <ClientOnly>
              <RichTextEditor v-model="content.policyRefund" :placeholder="$t('organizer.event_form.step2.refund_placeholder')" :min-height="180"/>
              <template #fallback><div class="form-control bg-reactive-primary border-0" style="min-height:180px;"/></template>
            </ClientOnly>
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

    <!-- ── STEP 3: Zones ────────────────────────────────── -->
    <div v-if="currentStep === 2">
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <div class="d-flex align-items-center justify-content-between mb-4">
          <h5 class="fw-semibold text-reactive-primary mb-0"><i class="bi bi-grid me-2 text-primary"/>{{ $t('organizer.event_form.step3.title') }}</h5>
          <button class="btn btn-sm btn-primary" @click="openZoneModal(null)"><i class="bi bi-plus-lg me-1"/>{{ $t('organizer.event_form.step3.add_zone') }}</button>
        </div>

        <div v-if="!savedSessionId" class="alert alert-warning py-2 small mb-4">
          <i class="bi bi-exclamation-triangle me-1"/>Session not yet created. Please complete Step 1 first.
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
                  <button class="btn btn-sm btn-outline-danger" @click="confirmDeleteZone(zone)"><i class="bi bi-trash"/></button>
                </div>
              </div>
              <div class="d-flex justify-content-between small text-reactive-secondary">
                <span><i class="bi bi-people me-1"/>{{ zone.capacity }}</span>
                <span class="fw-semibold text-reactive-primary">{{ formatPrice(zone.price) }}</span>
              </div>
              <div v-if="zone.purchaseLimit" class="small text-reactive-secondary mt-1">
                <i class="bi bi-ticket me-1"/>Max {{ zone.purchaseLimit }} / person
              </div>
              <div v-if="zone.perks && parsedPerks(zone.perks).length" class="mt-2 d-flex flex-wrap gap-1">
                <span v-for="perk in parsedPerks(zone.perks)" :key="perk" class="badge bg-primary bg-opacity-10 text-primary small">{{ perk }}</span>
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

    <!-- ── STEP 4: Layout ────────────────────────────────── -->
    <div v-if="currentStep === 3">

      <div v-if="zones.length === 0" class="alert alert-warning d-flex align-items-center gap-2 mb-4">
        <i class="bi bi-exclamation-triangle-fill"/>
        <span>Please add ticket zones in Step 3 before setting up the seating layout.</span>
      </div>

      <!-- Layout mode toggle -->
      <div class="card bg-reactive-secondary border-0 p-4 mb-4">
        <h5 class="fw-semibold text-reactive-primary mb-3"><i class="bi bi-layers me-2 text-primary"/>Seating Layout</h5>

        <div class="btn-group mb-4">
          <button class="btn" :class="layoutMode === 'venue' ? 'btn-primary' : 'btn-outline-secondary'" @click="layoutMode = 'venue'">
            <i class="bi bi-building me-2"/>Use Venue Default Layout
          </button>
          <button class="btn" :class="layoutMode === 'custom' ? 'btn-primary' : 'btn-outline-secondary'" @click="layoutMode = 'custom'">
            <i class="bi bi-pencil-square me-2"/>Draw Custom Layout
          </button>
        </div>

        <!-- VENUE DEFAULT MODE -->
        <div v-if="layoutMode === 'venue'">
          <div v-if="!selectedVenue" class="alert alert-warning py-2 small">
            <i class="bi bi-exclamation-triangle me-1"/>No venue selected. Go back to Step 1 and select a venue.
          </div>
          <div v-else-if="!selectedVenueLayout" class="alert alert-info py-2 small">
            <i class="bi bi-info-circle me-1"/>This venue has no default layout configured. Use custom layout instead.
          </div>
          <div v-else>
            <div class="mb-3">
              <div class="fw-semibold text-reactive-primary mb-1">{{ selectedVenue.name }}</div>
              <small class="text-reactive-secondary"><i class="bi bi-geo-alt me-1"/>{{ selectedVenue.addressLine }}</small>
            </div>
            <ClientOnly>
              <LayoutPreview :layout-json="selectedVenueLayout" style="height:300px;"/>
              <template #fallback><div class="bg-reactive-primary rounded" style="height:300px;"/></template>
            </ClientOnly>
            <div class="alert alert-info py-2 small mt-3">
              <i class="bi bi-info-circle me-1"/>
              Using the venue's default layout. Zone names in the layout will be matched to your ticket zones by name.
            </div>
          </div>
        </div>

        <!-- CUSTOM MODE: multi-floor canvas editor -->
        <div v-if="layoutMode === 'custom'">

          <!-- Floor tabs -->
          <div class="d-flex align-items-center gap-2 mb-3 flex-wrap">
            <div class="d-flex gap-1 flex-wrap">
              <button
                v-for="(floor, fi) in layoutFloors"
                :key="floor.floorId"
                class="btn btn-sm"
                :class="activeFloorIdx === fi ? 'btn-primary' : 'btn-outline-secondary'"
                @click="activeFloorIdx = fi"
              >
                Floor {{ fi + 1 }}
                <span v-if="floor.floorName" class="ms-1 opacity-75">({{ floor.floorName }})</span>
              </button>
            </div>
            <button class="btn btn-sm btn-outline-primary" @click="addFloor">
              <i class="bi bi-plus-lg me-1"/>Add Floor
            </button>
            <button v-if="layoutFloors.length > 1" class="btn btn-sm btn-outline-danger" @click="removeFloor(activeFloorIdx)">
              <i class="bi bi-trash me-1"/>Remove Floor
            </button>
          </div>

          <!-- Active floor editor -->
          <div v-if="activeFloor" class="card bg-reactive-primary border-0">

            <!-- Floor name + toolbar -->
            <div class="p-3 border-bottom border-secondary d-flex justify-content-between align-items-center flex-wrap gap-2">
              <div class="d-flex align-items-center gap-2 flex-wrap">
                <input
                  v-model="activeFloor.floorName"
                  type="text"
                  class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary fw-semibold"
                  style="max-width:180px;"
                  placeholder="Floor name"
                />
                <div class="btn-group btn-group-sm">
                  <button v-for="tool in tools" :key="tool.id" class="btn" :class="activeFloor.activeTool === tool.id ? 'btn-primary' : 'btn-outline-secondary'" :title="tool.label" @click="activeFloor.activeTool = tool.id">
                    <i :class="tool.icon"/>
                  </button>
                </div>
                <div class="btn-group btn-group-sm">
                  <button class="btn btn-outline-secondary" @click="activeFloor.scale = Math.min(4, activeFloor.scale + 0.15)"><i class="bi bi-plus-lg"/></button>
                  <button class="btn btn-outline-secondary" @click="activeFloor.scale = Math.max(0.3, activeFloor.scale - 0.15)"><i class="bi bi-dash-lg"/></button>
                  <button class="btn btn-outline-secondary" @click="activeFloor.scale = 1"><i class="bi bi-arrows-fullscreen"/></button>
                </div>
                <button class="btn btn-sm btn-outline-secondary" @click="addCustomShape(activeFloorIdx, 'rect')"><i class="bi bi-square me-1"/>Rect</button>
                <button class="btn btn-sm btn-outline-secondary" @click="addCustomShape(activeFloorIdx, 'ellipse')"><i class="bi bi-circle me-1"/>Ellipse</button>
                <button class="btn btn-sm btn-outline-warning" @click="addStageShape(activeFloorIdx, 'rect')"><i class="bi bi-collection-play me-1"/>Stage</button>
                <input type="color" v-model="activeFloor.selectedColor" class="form-control form-control-sm border-0 p-0" style="width:32px;height:32px;cursor:pointer;background:none;"/>
                <button class="btn btn-sm btn-outline-danger" @click="clearFloor(activeFloorIdx)"><i class="bi bi-trash me-1"/>Clear</button>
              </div>
              <div class="d-flex align-items-center gap-2">
                <button class="btn btn-sm" :class="activeFloor.snapEnabled ? 'btn-primary' : 'btn-outline-secondary'" @click="activeFloor.snapEnabled = !activeFloor.snapEnabled">
                  <i class="bi bi-magnet me-1"/>Snap
                </button>
                <button class="btn btn-sm btn-outline-secondary d-flex align-items-center gap-1" @click="activeFloor.showJsonPanel = !activeFloor.showJsonPanel">
                  <i class="bi" :class="activeFloor.showJsonPanel ? 'bi-code-slash' : 'bi-code'"/>JSON
                </button>
              </div>
            </div>

            <!-- Zone palette -->
            <div v-if="zones.length > 0" class="px-3 py-2 border-bottom border-secondary d-flex gap-2 flex-wrap align-items-center">
              <small class="text-reactive-secondary me-1">Click to place zone:</small>
              <button v-for="zone in zones" :key="zone.id" class="btn btn-sm zone-palette-btn" @click="addZoneShapeToFloor(activeFloorIdx, zone)">
                {{ zone.name }}
              </button>
            </div>

            <!-- Canvas area -->
            <div class="d-flex" style="height:520px;">
              <!-- JSON panel -->
              <div v-if="activeFloor.showJsonPanel" class="border-end border-secondary overflow-auto flex-shrink-0 position-relative" style="width:300px;background:#0d1117;font-family:monospace;font-size:11px;">
                <div class="d-flex justify-content-between align-items-center px-2 py-1 border-bottom border-secondary sticky-top" style="background:#161b22;">
                  <span class="text-success fw-semibold" style="font-size:11px;">floor_layout_json</span>
                  <button class="btn btn-sm p-0 px-1 text-secondary" @click="copyFloorJson(activeFloorIdx)"><i class="bi bi-clipboard"/></button>
                </div>
                <pre class="m-0 p-2 text-light" style="white-space:pre-wrap;word-break:break-all;">{{ floorJsonPreviews[activeFloorIdx] }}</pre>
              </div>

              <!-- Canvas -->
              <div
                :ref="el => setCanvasContainerRef(el, activeFloorIdx)"
                class="position-relative overflow-hidden flex-grow-1"
                style="background: repeating-linear-gradient(0deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px), repeating-linear-gradient(90deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px);"
              >
                <!-- Ghost from floor 0 for reference on upper floors -->
                <canvas
                  v-if="activeFloorIdx > 0"
                  :ref="el => { if (el) activeFloor!.ghostCanvasRef = el as HTMLCanvasElement }"
                  class="position-absolute top-0 start-0"
                  :width="activeFloor.stageSize.width"
                  :height="activeFloor.stageSize.height"
                  style="z-index:1; pointer-events:none; opacity:1;"
                />
                <canvas
                  :ref="el => setFloorCanvasRef(el, activeFloorIdx)"
                  class="position-absolute top-0 start-0"
                  :width="activeFloor.stageSize.width"
                  :height="activeFloor.stageSize.height"
                  style="z-index:5; cursor:crosshair;"
                  @mousedown="(e) => onCanvasMouseDown(e, activeFloorIdx)"
                  @mousemove="(e) => onCanvasMouseMove(e, activeFloorIdx)"
                  @mouseup="(e) => onCanvasMouseUp(e, activeFloorIdx)"
                  @mouseleave="(e) => onCanvasMouseUp(e, activeFloorIdx)"
                  @wheel.prevent="(e) => onCanvasWheel(e, activeFloorIdx)"
                />
                <!-- Seat overlay -->
                <canvas
                  v-if="!loadingSeats"
                  :ref="el => { if (el) activeFloor!.seatCanvasRef = el as HTMLCanvasElement }"
                  class="position-absolute top-0 start-0"
                  :width="activeFloor.stageSize.width"
                  :height="activeFloor.stageSize.height"
                  style="z-index:10; pointer-events:none;"
                />
              </div>
            </div>

            <!-- Shape property bar -->
            <div class="p-3 border-top border-secondary">
              <template v-if="activeFloor.selectedShapeId && activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)">
                <div class="d-flex align-items-center gap-2 flex-wrap">
                  <small class="text-reactive-secondary fw-semibold">Zone:</small>
                  <input :value="activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.label" type="text" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary" style="width:160px;" @change="(e) => { activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.label = (e.target as HTMLInputElement).value; drawFloor(activeFloorIdx) }"/>
                  <div class="d-flex align-items-center gap-1">
                    <small class="text-reactive-secondary">W</small>
                    <input type="number" min="40" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;" :value="Math.round(activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.width)" @change="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.width = Math.max(40, +(e.target as HTMLInputElement).value); drawFloor(activeFloorIdx) }"/>
                    <small class="text-reactive-secondary">H</small>
                    <input type="number" min="30" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;" :value="Math.round(activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.height)" @change="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.height = Math.max(30, +(e.target as HTMLInputElement).value); drawFloor(activeFloorIdx) }"/>
                  </div>
                  <input type="color" :value="activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.color" class="form-control form-control-sm border-0 p-0" style="width:36px;height:32px;" @change="(e) => { activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.color = (e.target as HTMLInputElement).value; drawFloor(activeFloorIdx) }"/>
                  <!-- Link to zone -->
                  <select
                    :value="activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.zoneId ?? ''"
                    class="form-select form-select-sm bg-reactive-secondary border-0 text-reactive-primary"
                    style="width:160px;"
                    @change="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.zoneId = (e.target as HTMLSelectElement).value || undefined; rebuildAndDraw() }"
                  >
                    <option value="">No zone linked</option>
                    <option v-for="z in zones.filter(z => !z.isStanding)" :key="z.id" :value="z.id">{{ z.name }} ({{ z.seatCount }} seats)</option>
                  </select>
                  <button class="btn btn-sm btn-outline-danger ms-auto" @click="deleteSelectedShape(activeFloorIdx)"><i class="bi bi-trash"/></button>
                </div>
                <!-- Rotation -->
                <div class="d-flex align-items-center gap-2 mt-2">
                  <small class="text-reactive-secondary fw-semibold"><i class="bi bi-arrow-clockwise me-1"/>Rotation:</small>
                  <input type="range" min="-180" max="180" step="1"
                    :value="Math.round(((activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)?.rotation ?? 0) * 180 / Math.PI))"
                    class="form-range flex-grow-1"
                    @input="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.rotation = +(e.target as HTMLInputElement).value * Math.PI / 180; drawFloor(activeFloorIdx) }"/>
                  <small class="text-reactive-secondary" style="min-width:42px;text-align:right">{{ Math.round(((activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)?.rotation ?? 0) * 180 / Math.PI)) }}°</small>
                  <button class="btn btn-sm btn-outline-secondary" @click="() => { activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.rotation = 0; drawFloor(activeFloorIdx) }">Reset</button>
                </div>
              </template>
              <template v-else-if="activeFloor.editingStage">
                <div class="d-flex align-items-center gap-2 flex-wrap">
                  <small class="text-reactive-secondary fw-semibold"><i class="bi bi-tv me-1"/>Stage / Screen:</small>
                  <div class="d-flex align-items-center gap-1">
                    <small class="text-reactive-secondary">W</small>
                    <input type="number" min="80" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;" :value="activeFloor.stageBox.width" @change="(e) => { activeFloor!.stageBox.width = Math.max(80, +(e.target as HTMLInputElement).value); drawFloor(activeFloorIdx) }"/>
                    <small class="text-reactive-secondary">H</small>
                    <input type="number" min="20" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;" :value="activeFloor.stageBox.height" @change="(e) => { activeFloor!.stageBox.height = Math.max(20, +(e.target as HTMLInputElement).value); drawFloor(activeFloorIdx) }"/>
                  </div>
                  <button class="btn btn-sm btn-outline-secondary ms-auto" @click="activeFloor.editingStage = false; drawFloor(activeFloorIdx)"><i class="bi bi-x-lg"/></button>
                </div>
              </template>
              <template v-else>
                <small class="text-reactive-secondary"><i class="bi bi-info-circle me-1"/>Click a shape to select. Drag to move, handles to resize, circle handle to rotate. Link a shape to a zone to overlay seats. <span v-if="activeFloor.snapEnabled" class="text-primary ms-1"><i class="bi bi-magnet me-1"/>Snap on</span></small>
              </template>
            </div>
          </div>

          <!-- Seat price popover -->
          <SeatPricePopover
            :seat="selectedSeat"
            :pos="seatPopoverPos"
            :zone-price="selectedSeat ? (zones.find(z => z.id === selectedSeat!.zoneId)?.price ?? 0) : 0"
            :session-id="savedSessionId ?? ''"
            :api-url="config.public.apiUrl"
            @close="selectedSeat = null"
            @updated="onSeatPriceUpdated"
          />
        </div>
      </div>

      <div class="d-flex justify-content-between gap-2 mt-2">
        <button class="btn btn-outline-secondary px-4" @click="currentStep = 2"><i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}</button>
        <button class="btn btn-success px-4" :disabled="saving" @click="saveLayout">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          <i v-else class="bi bi-check2 me-2"/>{{ $t('organizer.event_form.finish') }}
        </button>
      </div>
    </div>

    <!-- Error toast -->
    <div v-if="globalError" class="alert alert-danger position-fixed bottom-0 end-0 m-4" style="z-index:2000;max-width:360px;">
      <i class="bi bi-exclamation-circle me-2"/>{{ globalError }}
      <button class="btn-close float-end" @click="globalError = ''"/>
    </div>

    <!-- Zone Modal -->
    <ZoneModal
      v-if="showZoneModal"
      :zone="editingZone"
      :session-id="savedSessionId!"
      :api-url="config.public.apiUrl"
      @close="showZoneModal = false"
      @saved="onZoneSaved"
    />

    <!-- Delete Zone Confirm -->
    <div v-if="deleteZoneTarget" class="modal-backdrop-custom" @click.self="deleteZoneTarget = null">
      <div class="modal-box bg-reactive-secondary p-4 rounded-3 shadow-lg" style="max-width:420px;">
        <h5 class="text-reactive-primary fw-bold mb-2"><i class="bi bi-exclamation-triangle text-danger me-2"/>{{ $t('organizer.event_form.step3.delete_title') }}</h5>
        <p class="text-reactive-secondary">{{ $t('organizer.event_form.step3.delete_confirm', { name: deleteZoneTarget.name }) }}</p>
        <div class="d-flex gap-2 justify-content-end">
          <button class="btn btn-outline-secondary" @click="deleteZoneTarget = null">{{ $t('common.action.cancel') }}</button>
          <button class="btn btn-danger" :disabled="zoneSaving" @click="doDeleteZone">
            <span v-if="zoneSaving" class="spinner-border spinner-border-sm me-2"/>{{ $t('common.action.delete') }}
          </button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import RichTextEditor from '~/components/crud-event/RichTextEditor.vue'
import LayoutPreview from '~/components/crud-event/LayoutPreview.vue'
import ZoneModal from '~/components/crud-event/ZoneModal.vue'
import SeatPricePopover from '~/components/crud-event/SeatPricePopover.vue'

definePageMeta({ layout: 'organizer', middleware: 'organizer' })

const { t: $t } = useI18n()
const route  = useRoute()
const router = useRouter()
const config = useRuntimeConfig()

// ── Route ──────────────────────────────────────────────────
const eventId = computed(() => {
  const id = route.params.id as string
  return id === 'new' ? null : id
})
const isNew          = computed(() => !eventId.value)
const savedEventId   = ref<string | null>(eventId.value)
const savedSessionId = ref<string | null>(null)

// ── Steps ──────────────────────────────────────────────────
const currentStep = ref(0)
const steps = computed(() => [
  { label: $t('organizer.event_form.step1.title') },
  { label: $t('organizer.event_form.step2.title') },
  { label: $t('organizer.event_form.step3.title') },
  { label: $t('organizer.event_form.step4.title') },
])
const goToStep = (i: number) => { if (savedEventId.value || i <= currentStep.value) currentStep.value = i }

const saving      = ref(false)
const globalError = ref('')

// ── Venues ─────────────────────────────────────────────────
interface Venue { id: string; name: string; addressLine: string; imageUrl?: string; layout?: string }
const venues = ref<Venue[]>([])

const selectedVenue = computed(() => venues.value.find(v => v.id === form.value.venueId) ?? null)
const selectedVenueLayout = computed(() => selectedVenue.value?.layout ?? null)

const fetchVenues = async () => {
  try {
    // Fetch venues from a public or internal endpoint
    venues.value = await $fetch<Venue[]>(`${config.public.apiUrl}/venues`, { credentials: 'include' })
  } catch { venues.value = [] }
}

// ── Categories ─────────────────────────────────────────────
interface Category { id: number; name: string }
const categories = ref<Category[]>([])
const fetchCategories = async () => {
  try { categories.value = await $fetch<Category[]>(`${config.public.apiUrl}/organizer/categories`, { credentials: 'include' }) }
  catch { categories.value = [] }
}

// ── Step 1 form ────────────────────────────────────────────
const form = ref({
  name: '', venueId: '', categoryId: '' as any,
  addressLine: '', startDate: '', endDate: '',
  bannerUrl: '', status: 'EDITING',
})
const errors = ref<Record<string, string>>({})

const validateStep1 = () => {
  errors.value = {}
  if (!form.value.name.trim())        errors.value.name        = 'Name is required'
  if (!form.value.venueId)            errors.value.venueId     = 'Venue is required'
  if (!form.value.categoryId)         errors.value.categoryId  = 'Category is required'
  if (!form.value.addressLine.trim()) errors.value.addressLine = 'Address is required'
  if (!form.value.startDate)          errors.value.startDate   = 'Start date is required'
  if (!form.value.endDate)            errors.value.endDate     = 'End date is required'
  if (form.value.startDate && form.value.endDate && form.value.endDate <= form.value.startDate)
    errors.value.endDate = 'End date must be after start date'
  return Object.keys(errors.value).length === 0
}

const saveStep1 = async () => {
  if (!validateStep1()) return
  saving.value = true; globalError.value = ''
  try {
    const payload = {
      name:        form.value.name,
      venueId:     form.value.venueId,
      categoryId:  Number(form.value.categoryId),
      addressLine: form.value.addressLine,
      bannerUrl:   form.value.bannerUrl || null,
      status:      form.value.status,
      startDate:   new Date(form.value.startDate).toISOString(),
      endDate:     new Date(form.value.endDate).toISOString(),
      // include content fields so they persist across saves
      aboutVi:             content.value.aboutVi || null,
      aboutEn:             content.value.aboutEn || null,
      termsAndConditions:  content.value.termsAndConditions || null,
      policyRefund:        content.value.policyRefund || null,
    }

    if (savedEventId.value) {
      await $fetch(`${config.public.apiUrl}/organizer/events/${savedEventId.value}`, { method: 'PUT', body: payload, credentials: 'include' })
      // Update session dates
      if (savedSessionId.value) {
        await $fetch(
          `${config.public.apiUrl}/organizer/events/${savedEventId.value}/sessions/${savedSessionId.value}`,
          { method: 'PUT', body: { startDate: payload.startDate, endDate: payload.endDate }, credentials: 'include' }
        )
      }
    } else {
      const res = await $fetch<{ id: string; sessions: { id: string }[] }>(
        `${config.public.apiUrl}/organizer/events`,
        { method: 'POST', body: payload, credentials: 'include' }
      )
      savedEventId.value   = res.id
      savedSessionId.value = res.sessions?.[0]?.id ?? null
      history.replaceState({}, '', `/organizer/events/${res.id}`)
    }
    currentStep.value = 1
  } catch (err: any) { globalError.value = err?.data?.message ?? 'Failed to save event' }
  finally { saving.value = false }
}

// ── Step 2: Content ────────────────────────────────────────
// Content is inlined on the event — no separate /content endpoint
const content     = ref({ aboutVi: '', aboutEn: '', termsAndConditions: '', policyRefund: '' })
const contentLang = ref<'vi' | 'en'>('vi')

const saveStep2 = async () => {
  if (!savedEventId.value) return
  saving.value = true; globalError.value = ''
  try {
    // Content fields are part of the event — PUT the full event with updated content
    await $fetch(`${config.public.apiUrl}/organizer/events/${savedEventId.value}`, {
      method: 'PUT',
      body: {
        name:                form.value.name,
        venueId:             form.value.venueId,
        categoryId:          Number(form.value.categoryId),
        addressLine:         form.value.addressLine,
        bannerUrl:           form.value.bannerUrl || null,
        status:              form.value.status,
        startDate:           new Date(form.value.startDate).toISOString(),
        endDate:             new Date(form.value.endDate).toISOString(),
        aboutVi:             content.value.aboutVi || null,
        aboutEn:             content.value.aboutEn || null,
        termsAndConditions:  content.value.termsAndConditions || null,
        policyRefund:        content.value.policyRefund || null,
      },
      credentials: 'include',
    })
    currentStep.value = 2
  } catch (err: any) { globalError.value = err?.data?.message ?? 'Failed to save content' }
  finally { saving.value = false }
}

// ── Zones ──────────────────────────────────────────────────
interface Zone {
  id?: string; name: string; isStanding: boolean; capacity: number; price: number
  purchaseLimit?: number | null; descriptionVi?: string; descriptionEn?: string
  perks?: string | string[]; quantitySold?: number; seatCount?: number
}

const zones            = ref<Zone[]>([])
const showZoneModal    = ref(false)
const editingZone      = ref<Zone | null>(null)
const deleteZoneTarget = ref<Zone | null>(null)
const zoneSaving       = ref(false)

const openZoneModal = (zone: Zone | null) => { editingZone.value = zone ? { ...zone } : null; showZoneModal.value = true }

const onZoneSaved = (savedZone: Zone) => {
  const idx = zones.value.findIndex(z => z.id === savedZone.id)
  if (idx >= 0) zones.value[idx] = savedZone
  else zones.value.push(savedZone)
  showZoneModal.value = false
}

const fetchZones = async () => {
  if (!savedSessionId.value) return
  try { zones.value = await $fetch<Zone[]>(`${config.public.apiUrl}/organizer/sessions/${savedSessionId.value}/zones`, { credentials: 'include' }) }
  catch { zones.value = [] }
}

const confirmDeleteZone = (zone: Zone) => { deleteZoneTarget.value = zone }
const doDeleteZone = async () => {
  if (!deleteZoneTarget.value?.id || !savedSessionId.value) return
  zoneSaving.value = true
  try {
    await $fetch(`${config.public.apiUrl}/organizer/sessions/${savedSessionId.value}/zones/${deleteZoneTarget.value.id}`, { method: 'DELETE', credentials: 'include' })
    zones.value = zones.value.filter(z => z.id !== deleteZoneTarget.value!.id)
    deleteZoneTarget.value = null
  } catch (err: any) { globalError.value = err?.data?.message ?? 'Failed to delete zone' }
  finally { zoneSaving.value = false }
}

// ── Step 4: Layout ─────────────────────────────────────────
// layoutMode: 'venue' = use venue default, 'custom' = draw per-event layout
// Multiple floors are supported in custom mode — stored as an array in the JSON:
// { floors: [ { floorName, floorOrder, stage, zones, stage_shapes }, ... ] }

type LayoutMode = 'venue' | 'custom'
const layoutMode = ref<LayoutMode>('venue')

interface CanvasShape {
  id: string; type: 'rect' | 'ellipse'; x: number; y: number
  width: number; height: number; label: string; color: string
  accessible: boolean; zoneId?: string; rotation?: number; isStage?: boolean
}

interface LayoutFloor {
  floorId:        string
  floorName:      string
  floorOrder:     number
  canvasShapes:   CanvasShape[]
  selectedShapeId: string | null
  activeTool:     'select' | 'move'
  selectedColor:  string
  scale:          number
  stageSize:      { width: number; height: number }
  stageBox:       { x: number; y: number; width: number; height: number }
  editingStage:   boolean
  showJsonPanel:  boolean
  snapEnabled:    boolean
  layoutCanvasRef: HTMLCanvasElement | null
  seatCanvasRef:   HTMLCanvasElement | null
  ghostCanvasRef:  HTMLCanvasElement | null
}

const SNAP_THRESHOLD = 8
const CANVAS_W = 900
const CANVAS_H = 520
const HANDLE_R = 5

const tools = [
  { id: 'select', icon: 'bi bi-cursor',      label: 'Select' },
  { id: 'move',   icon: 'bi bi-arrows-move', label: 'Pan' },
]

const layoutFloors    = ref<LayoutFloor[]>([])
const activeFloorIdx  = ref(0)
const activeFloor     = computed(() => layoutFloors.value[activeFloorIdx.value] ?? null)
const canvasContainerRefs: Record<number, HTMLElement> = {}

// ── Seat overlay ───────────────────────────────────────────
interface SeatInfo { id: string; seatCode: string; priceOverride: number | null; zoneId: string }
interface SeatsByZone { [zoneId: string]: SeatInfo[] }
const seatsByZone    = ref<SeatsByZone>({})
const selectedSeat   = ref<SeatInfo | null>(null)
const seatPopoverPos = ref({ x: 0, y: 0 })
const loadingSeats   = ref(false)
const seatHitMap     = ref<Array<Array<{ seatId: string; seat: SeatInfo; cx: number; cy: number; r: number }>>>([])

// ── Drag state — plain JS, never reactive ──────────────────
const drag = {
  active: false, fi: -1, targetId: '' as string,
  startX: 0, startY: 0, origX: 0, origY: 0,
  snapGuideX: null as number | null, snapGuideY: null as number | null,
  resizeHandle: null as string | null, resizeOrigW: 0, resizeOrigH: 0,
  panOffsetX: 0, panOffsetY: 0,
  rotatingShape: false, rotOrigAngle: 0, rotCx: 0, rotCy: 0,
}

const floorPan: Record<string, { x: number; y: number; scale: number }> = {}
const getFloorPan = (floorId: string) => {
  if (!floorPan[floorId]) floorPan[floorId] = { x: 0, y: 0, scale: 1 }
  return floorPan[floorId]
}

// ── Floor factory ──────────────────────────────────────────
const makeFloor = (order: number): LayoutFloor => ({
  floorId: crypto.randomUUID(),
  floorName: order === 1 ? 'Main Floor' : `Floor ${order}`,
  floorOrder: order,
  canvasShapes: [], selectedShapeId: null,
  activeTool: 'select', selectedColor: '#6366f1', scale: 1,
  stageSize: { width: 0, height: 0 },
  stageBox: { x: CANVAS_W / 2 - 120, y: order === 1 ? 20 : -200, width: 240, height: 44 },
  editingStage: false, showJsonPanel: false, snapEnabled: true,
  layoutCanvasRef: null, seatCanvasRef: null, ghostCanvasRef: null,
})

const addFloor = () => {
  const floor = makeFloor(layoutFloors.value.length + 1)
  layoutFloors.value.push(floor)
  activeFloorIdx.value = layoutFloors.value.length - 1
  nextTick(() => {
    const fi = activeFloorIdx.value
    const el = canvasContainerRefs[fi]
    if (el) layoutFloors.value[fi].stageSize = { width: el.clientWidth, height: CANVAS_H }
  })
}

const removeFloor = (fi: number) => {
  layoutFloors.value.splice(fi, 1)
  layoutFloors.value.forEach((f, i) => { f.floorOrder = i + 1 })
  activeFloorIdx.value = Math.min(fi, layoutFloors.value.length - 1)
}

// ── Canvas ref management ──────────────────────────────────
const setCanvasContainerRef = (el: any, fi: number) => {
  if (!el) return
  canvasContainerRefs[fi] = el
  nextTick(() => {
    const floor = layoutFloors.value[fi]
    if (floor && el.clientWidth > 0 && floor.stageSize.width === 0) {
      floor.stageSize = { width: el.clientWidth, height: el.clientHeight || CANVAS_H }
      nextTick(() => drawFloor(fi))
    }
  })
}

const setFloorCanvasRef = (el: any, fi: number) => {
  if (!el) return
  layoutFloors.value[fi].layoutCanvasRef = el as HTMLCanvasElement
  nextTick(() => drawFloor(fi))
}

// ── Coordinate helpers ─────────────────────────────────────
const toLogical = (fi: number, px: number, py: number) => {
  const floor = layoutFloors.value[fi]
  const pan   = getFloorPan(floor.floorId)
  const scaleX = (floor.stageSize.width  / CANVAS_W) * pan.scale
  const scaleY = (floor.stageSize.height / CANVAS_H) * pan.scale
  return { x: (px - pan.x) / scaleX, y: (py - pan.y) / scaleY }
}

const toNorm = (px: number, total: number) => parseFloat((((px / total) * 2) - 1).toFixed(3))

// ── Snap ───────────────────────────────────────────────────
const getSnapEdges = (fi: number, excludeId: string) => {
  const floor = layoutFloors.value[fi]
  const x: number[] = [CANVAS_W / 2], y: number[] = [CANVAS_H / 2]
  const sb = floor.stageBox
  x.push(sb.x, sb.x + sb.width / 2, sb.x + sb.width)
  y.push(sb.y, sb.y + sb.height / 2, sb.y + sb.height)
  for (const s of floor.canvasShapes) {
    if (s.id === excludeId) continue
    x.push(s.x, s.x + s.width / 2, s.x + s.width)
    y.push(s.y, s.y + s.height / 2, s.y + s.height)
  }
  return { x, y }
}

const trySnap = (val: number, list: number[]): number | null => {
  for (const c of list) if (Math.abs(val - c) < SNAP_THRESHOLD) return c
  return null
}

const snapPosition = (fi: number, excludeId: string, rawX: number, rawY: number, w: number, h: number) => {
  const floor = layoutFloors.value[fi]
  if (!floor.snapEnabled) { drag.snapGuideX = null; drag.snapGuideY = null; return { x: rawX, y: rawY } }
  const edges = getSnapEdges(fi, excludeId)
  let finalX = rawX, finalY = rawY
  drag.snapGuideX = null; drag.snapGuideY = null
  const sL = trySnap(rawX, edges.x), sC = trySnap(rawX + w/2, edges.x), sR = trySnap(rawX + w, edges.x)
  if      (sL !== null) { finalX = sL;       drag.snapGuideX = sL }
  else if (sC !== null) { finalX = sC - w/2; drag.snapGuideX = sC }
  else if (sR !== null) { finalX = sR - w;   drag.snapGuideX = sR }
  const sT = trySnap(rawY, edges.y), sMid = trySnap(rawY + h/2, edges.y), sB = trySnap(rawY + h, edges.y)
  if      (sT   !== null) { finalY = sT;        drag.snapGuideY = sT }
  else if (sMid !== null) { finalY = sMid - h/2; drag.snapGuideY = sMid }
  else if (sB   !== null) { finalY = sB - h;    drag.snapGuideY = sB }
  return { x: finalX, y: finalY }
}

// ── Canvas renderer ────────────────────────────────────────
const roundRect = (ctx: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, r: number) => {
  ctx.beginPath(); ctx.roundRect(x, y, w, h, r)
}

const drawResizeHandles = (ctx: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, scale: number) => {
  const r = HANDLE_R / scale
  for (const [hx, hy] of [[x, y], [x + w, y], [x + w, y + h], [x, y + h]]) {
    ctx.fillStyle = '#6366f1'; ctx.beginPath(); ctx.arc(hx as number, hy as number, r, 0, Math.PI * 2); ctx.fill()
    ctx.strokeStyle = '#fff'; ctx.lineWidth = 1.5 / scale; ctx.beginPath(); ctx.arc(hx as number, hy as number, r, 0, Math.PI * 2); ctx.stroke()
  }
  const mr = r * 0.85
  for (const [hx, hy] of [[x + w/2, y], [x + w, y + h/2], [x + w/2, y + h], [x, y + h/2]]) {
    ctx.fillStyle = '#a5b4fc'
    ctx.beginPath(); ctx.roundRect((hx as number) - mr, (hy as number) - mr, mr * 2, mr * 2, 2); ctx.fill()
    ctx.strokeStyle = '#fff'; ctx.lineWidth = 1 / scale
    ctx.beginPath(); ctx.roundRect((hx as number) - mr, (hy as number) - mr, mr * 2, mr * 2, 2); ctx.stroke()
  }
}

const drawFloor = (fi: number) => {
  const floor  = layoutFloors.value[fi]
  const canvas = floor?.layoutCanvasRef
  if (!canvas || floor.stageSize.width === 0) return
  const ctx = canvas.getContext('2d')
  if (!ctx) return

  const W = floor.stageSize.width, H = floor.stageSize.height
  const pan    = getFloorPan(floor.floorId)
  const scaleX = (W / CANVAS_W) * pan.scale
  const scaleY = (H / CANVAS_H) * pan.scale

  ctx.clearRect(0, 0, W, H)
  ctx.save()
  ctx.translate(pan.x, pan.y)
  ctx.scale(scaleX, scaleY)

  // Stage
  const sb = floor.stageBox
  ctx.fillStyle = '#f59e0b'
  roundRect(ctx, sb.x, sb.y, sb.width, sb.height, 6); ctx.fill()
  if (floor.editingStage) {
    ctx.strokeStyle = '#ffffff'; ctx.lineWidth = 2 / scaleX
    roundRect(ctx, sb.x, sb.y, sb.width, sb.height, 6); ctx.stroke()
    drawResizeHandles(ctx, sb.x, sb.y, sb.width, sb.height, scaleX)
  }
  ctx.save()
  ctx.setTransform(1, 0, 0, 1, 0, 0)
  ctx.font = '500 12px sans-serif'; ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
  ctx.fillStyle = '#1a1a1a'
  ctx.fillText('Stage / Screen', (sb.x + sb.width / 2) * scaleX + pan.x, (sb.y + sb.height / 2) * scaleY + pan.y)
  ctx.restore()

  // Shapes
  for (const shape of floor.canvasShapes) {
    const isSelected = floor.selectedShapeId === shape.id
    const rot = shape.rotation ?? 0
    const cx  = shape.x + shape.width / 2, cy = shape.y + shape.height / 2

    ctx.globalAlpha = shape.isStage ? 0.92 : 1
    ctx.save()
    ctx.translate(cx, cy); ctx.rotate(rot); ctx.translate(-cx, -cy)

    const fillColor   = shape.isStage ? '#f59e0b' : shape.color + '44'
    const strokeColor = shape.isStage ? (isSelected ? '#fff' : '#d97706') : (isSelected ? '#ffffff' : shape.color)

    if (shape.type === 'rect') {
      ctx.fillStyle = fillColor; roundRect(ctx, shape.x, shape.y, shape.width, shape.height, 6); ctx.fill()
      ctx.strokeStyle = strokeColor; ctx.lineWidth = (isSelected ? 2.5 : 1.5) / scaleX
      roundRect(ctx, shape.x, shape.y, shape.width, shape.height, 6); ctx.stroke()
    } else {
      ctx.fillStyle = fillColor; ctx.beginPath(); ctx.ellipse(cx, cy, shape.width / 2, shape.height / 2, 0, 0, Math.PI * 2); ctx.fill()
      ctx.strokeStyle = strokeColor; ctx.lineWidth = (isSelected ? 2.5 : 1.5) / scaleX
      ctx.beginPath(); ctx.ellipse(cx, cy, shape.width / 2, shape.height / 2, 0, 0, Math.PI * 2); ctx.stroke()
    }

    ctx.globalAlpha = 1
    ctx.save()
    ctx.setTransform(1, 0, 0, 1, 0, 0)
    ctx.fillStyle = shape.isStage ? '#1a1a1a' : '#ffffff'
    ctx.font = '500 12px sans-serif'; ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
    ctx.translate(cx * scaleX + pan.x, cy * scaleY + pan.y); ctx.rotate(rot)
    ctx.fillText(shape.label, 0, 0)
    ctx.restore()

    if (isSelected) {
      drawResizeHandles(ctx, shape.x, shape.y, shape.width, shape.height, scaleX)
      const rHandleY = shape.y - 28 / scaleX
      ctx.strokeStyle = '#ffffff'; ctx.lineWidth = 1 / scaleX
      ctx.beginPath(); ctx.moveTo(cx, shape.y); ctx.lineTo(cx, rHandleY); ctx.stroke()
      ctx.fillStyle = '#6366f1'; ctx.beginPath(); ctx.arc(cx, rHandleY, 6 / scaleX, 0, Math.PI * 2); ctx.fill()
      ctx.strokeStyle = '#fff'; ctx.lineWidth = 1.5 / scaleX; ctx.beginPath(); ctx.arc(cx, rHandleY, 6 / scaleX, 0, Math.PI * 2); ctx.stroke()
    }
    ctx.restore()
  }

  // Snap guides
  ctx.strokeStyle = '#6366f1'; ctx.lineWidth = 1 / scaleX
  ctx.setLineDash([4 / scaleX, 4 / scaleX]); ctx.globalAlpha = 0.8
  if (drag.snapGuideX !== null) { ctx.beginPath(); ctx.moveTo(drag.snapGuideX, 0); ctx.lineTo(drag.snapGuideX, CANVAS_H); ctx.stroke() }
  if (drag.snapGuideY !== null) { ctx.beginPath(); ctx.moveTo(0, drag.snapGuideY); ctx.lineTo(CANVAS_W, drag.snapGuideY); ctx.stroke() }
  ctx.setLineDash([]); ctx.globalAlpha = 1
  ctx.restore()

  // Ghost on upper floors
  if (fi === 0) layoutFloors.value.forEach((_, ui) => { if (ui > 0) drawGhostCanvas(ui) })
  else drawGhostCanvas(fi)
}

const drawGhostCanvas = (fi: number) => {
  if (fi === 0) return
  const floor  = layoutFloors.value[fi]
  const canvas = floor?.ghostCanvasRef
  if (!canvas || floor.stageSize.width === 0) return
  const src = layoutFloors.value[0]
  if (!src || src.stageSize.width === 0) return

  const ctx = canvas.getContext('2d')
  if (!ctx) return

  const W = floor.stageSize.width, H = floor.stageSize.height
  const pan    = getFloorPan(floor.floorId)
  const scaleX = (W / CANVAS_W) * pan.scale, scaleY = (H / CANVAS_H) * pan.scale

  ctx.clearRect(0, 0, W, H)
  ctx.save(); ctx.translate(pan.x, pan.y); ctx.scale(scaleX, scaleY); ctx.globalAlpha = 0.22

  const sb = src.stageBox
  ctx.fillStyle = '#f59e0b'; roundRect(ctx, sb.x, sb.y, sb.width, sb.height, 6); ctx.fill()
  for (const s of src.canvasShapes) {
    const rot = s.rotation ?? 0, cx = s.x + s.width / 2, cy = s.y + s.height / 2
    ctx.save(); ctx.translate(cx, cy); ctx.rotate(rot); ctx.translate(-cx, -cy)
    ctx.fillStyle = s.isStage ? '#f59e0b' : s.color + '66'
    if (s.type === 'ellipse') { ctx.beginPath(); ctx.ellipse(cx, cy, s.width / 2, s.height / 2, 0, 0, Math.PI * 2); ctx.fill() }
    else { roundRect(ctx, s.x, s.y, s.width, s.height, 6); ctx.fill() }
    ctx.restore()
  }
  ctx.restore()
}

// ── Hit test ───────────────────────────────────────────────
type HitTarget = { kind: 'shape'; id: string } | { kind: 'stage' } | { kind: 'resize'; id: string; handle: string } | { kind: 'stageResize'; handle: string } | { kind: 'rotate'; id: string } | null

const checkHandles = (lx: number, ly: number, x: number, y: number, w: number, h: number, r: number): string | null => {
  for (const [name, hx, hy] of [['nw', x, y], ['ne', x+w, y], ['se', x+w, y+h], ['sw', x, y+h], ['n', x+w/2, y], ['e', x+w, y+h/2], ['s', x+w/2, y+h], ['w', x, y+h/2]] as [string, number, number][])
    if (Math.hypot(lx - hx, ly - hy) <= r * 1.5) return name
  return null
}

const checkHandlesRotated = (lx: number, ly: number, x: number, y: number, w: number, h: number, rot: number, r: number): string | null => {
  const cx = x + w / 2, cy = y + h / 2
  for (const [name, hx, hy] of [['nw', x, y], ['ne', x+w, y], ['se', x+w, y+h], ['sw', x, y+h], ['n', x+w/2, y], ['e', x+w, y+h/2], ['s', x+w/2, y+h], ['w', x, y+h/2]] as [string, number, number][]) {
    const dx = hx - cx, dy = hy - cy
    const rx = cx + dx * Math.cos(rot) - dy * Math.sin(rot), ry = cy + dx * Math.sin(rot) + dy * Math.cos(rot)
    if (Math.hypot(lx - rx, ly - ry) <= r * 1.5) return name
  }
  return null
}

const hitTest = (fi: number, lx: number, ly: number): HitTarget => {
  const floor  = layoutFloors.value[fi]
  const pan    = getFloorPan(floor.floorId)
  const scaleX = (floor.stageSize.width / CANVAS_W) * pan.scale
  const HR     = HANDLE_R / scaleX

  const selShape = floor.canvasShapes.find(s => s.id === floor.selectedShapeId)
  if (selShape) {
    const rot = selShape.rotation ?? 0, cx = selShape.x + selShape.width / 2, cy = selShape.y + selShape.height / 2
    const rHandleLocalY = selShape.y - 28 / scaleX
    const rHx = cx + Math.sin(rot) * (rHandleLocalY - cy) * -1, rHy = cy + Math.cos(rot) * (rHandleLocalY - cy)
    if (Math.hypot(lx - rHx, ly - rHy) <= 8 / scaleX) return { kind: 'rotate', id: selShape.id }
    const handle = checkHandlesRotated(lx, ly, selShape.x, selShape.y, selShape.width, selShape.height, rot, HR)
    if (handle) return { kind: 'resize', id: selShape.id, handle }
  }
  if (floor.editingStage) {
    const handle = checkHandles(lx, ly, floor.stageBox.x, floor.stageBox.y, floor.stageBox.width, floor.stageBox.height, HR)
    if (handle) return { kind: 'stageResize', handle }
  }
  for (let i = floor.canvasShapes.length - 1; i >= 0; i--) {
    const s = floor.canvasShapes[i]
    const rot = s.rotation ?? 0, cx = s.x + s.width / 2, cy = s.y + s.height / 2
    const dx = lx - cx, dy = ly - cy
    const lxL = dx * Math.cos(-rot) - dy * Math.sin(-rot) + cx, lyL = dx * Math.sin(-rot) + dy * Math.cos(-rot) + cy
    if (s.type === 'ellipse') { if (((lxL - cx) / (s.width / 2)) ** 2 + ((lyL - cy) / (s.height / 2)) ** 2 <= 1) return { kind: 'shape', id: s.id } }
    else { if (lxL >= s.x && lxL <= s.x + s.width && lyL >= s.y && lyL <= s.y + s.height) return { kind: 'shape', id: s.id } }
  }
  const sb = floor.stageBox
  if (lx >= sb.x && lx <= sb.x + sb.width && ly >= sb.y && ly <= sb.y + sb.height) return { kind: 'stage' }
  return null
}

// ── Mouse handlers ─────────────────────────────────────────
const onCanvasMouseDown = (e: MouseEvent, fi: number) => {
  if (e.button !== 0) return
  const floor = layoutFloors.value[fi]
  const canvas = floor.layoutCanvasRef; if (!canvas) return
  const rect = canvas.getBoundingClientRect()
  const px = e.clientX - rect.left, py = e.clientY - rect.top
  const { x: lx, y: ly } = toLogical(fi, px, py)

  if (floor.activeTool === 'move') {
    drag.active = true; drag.fi = fi; drag.targetId = '__pan__'
    drag.startX = px; drag.startY = py
    drag.panOffsetX = getFloorPan(floor.floorId).x; drag.panOffsetY = getFloorPan(floor.floorId).y
    canvas.style.cursor = 'grabbing'; return
  }

  const hit = hitTest(fi, lx, ly)
  if (!hit) {
    if (floor.selectedShapeId !== null || floor.editingStage) { floor.selectedShapeId = null; floor.editingStage = false; drawFloor(fi) }
    return
  }
  if (hit.kind === 'rotate') {
    const shape = floor.canvasShapes.find(s => s.id === hit.id)!
    drag.active = true; drag.fi = fi; drag.targetId = hit.id; drag.rotatingShape = true
    drag.startX = lx; drag.startY = ly
    drag.rotCx = shape.x + shape.width / 2; drag.rotCy = shape.y + shape.height / 2
    drag.rotOrigAngle = shape.rotation ?? 0; return
  }
  if (hit.kind === 'resize') {
    const shape = floor.canvasShapes.find(s => s.id === hit.id)!
    drag.active = true; drag.fi = fi; drag.targetId = hit.id
    drag.resizeHandle = hit.handle; drag.startX = lx; drag.startY = ly
    drag.origX = shape.x; drag.origY = shape.y; drag.resizeOrigW = shape.width; drag.resizeOrigH = shape.height
    drag.rotatingShape = false; return
  }
  if (hit.kind === 'stageResize') {
    drag.active = true; drag.fi = fi; drag.targetId = '__stageResize__'
    drag.resizeHandle = hit.handle; drag.startX = lx; drag.startY = ly
    drag.origX = floor.stageBox.x; drag.origY = floor.stageBox.y
    drag.resizeOrigW = floor.stageBox.width; drag.resizeOrigH = floor.stageBox.height
    drag.rotatingShape = false; return
  }
  if (hit.kind === 'stage') {
    floor.editingStage = true; floor.selectedShapeId = null
    drag.active = true; drag.fi = fi; drag.targetId = '__stage__'
    drag.startX = lx; drag.startY = ly; drag.origX = floor.stageBox.x; drag.origY = floor.stageBox.y
    drag.rotatingShape = false; drawFloor(fi); return
  }
  if (hit.kind === 'shape') {
    const shape = floor.canvasShapes.find(s => s.id === hit.id)!
    floor.selectedShapeId = hit.id; floor.editingStage = false
    drag.active = true; drag.fi = fi; drag.targetId = hit.id
    drag.resizeHandle = null; drag.rotatingShape = false
    drag.startX = lx; drag.startY = ly; drag.origX = shape.x; drag.origY = shape.y
    drawFloor(fi)
  }
}

const onCanvasMouseMove = (e: MouseEvent, fi: number) => {
  if (!drag.active || drag.fi !== fi) return
  const floor = layoutFloors.value[fi]
  const canvas = floor.layoutCanvasRef; if (!canvas) return
  const rect = canvas.getBoundingClientRect()
  const px = e.clientX - rect.left, py = e.clientY - rect.top

  if (drag.targetId === '__pan__') {
    const pan = getFloorPan(floor.floorId)
    pan.x = drag.panOffsetX + (px - drag.startX); pan.y = drag.panOffsetY + (py - drag.startY)
    drawFloor(fi); if (fi > 0) drawGhostCanvas(fi); return
  }

  const { x: lx, y: ly } = toLogical(fi, px, py)
  const dx = lx - drag.startX, dy = ly - drag.startY

  if (drag.rotatingShape) {
    const shape = floor.canvasShapes.find(s => s.id === drag.targetId)
    if (shape) {
      const angle = Math.atan2(ly - drag.rotCy, lx - drag.rotCx)
      const startAngle = Math.atan2(drag.startY - drag.rotCy, drag.startX - drag.rotCx)
      shape.rotation = drag.rotOrigAngle + (angle - startAngle); drawFloor(fi)
    }
    return
  }

  if (drag.resizeHandle) {
    const isStage = drag.targetId === '__stageResize__'
    const target  = isStage ? floor.stageBox : floor.canvasShapes.find(s => s.id === drag.targetId)!
    applyResize(target, drag.resizeHandle, drag.origX, drag.origY, drag.resizeOrigW, drag.resizeOrigH, dx, dy)
    drawFloor(fi); return
  }
  if (drag.targetId === '__stage__') {
    const { x, y } = snapPosition(fi, '__stage__', drag.origX + dx, drag.origY + dy, floor.stageBox.width, floor.stageBox.height)
    floor.stageBox.x = x; floor.stageBox.y = y; drawFloor(fi); return
  }
  const shape = floor.canvasShapes.find(s => s.id === drag.targetId)
  if (shape) {
    const { x, y } = snapPosition(fi, shape.id, drag.origX + dx, drag.origY + dy, shape.width, shape.height)
    shape.x = x; shape.y = y; drawFloor(fi)
  }
}

const onCanvasMouseUp = (_e: MouseEvent, fi: number) => {
  if (!drag.active || drag.fi !== fi) return
  drag.active = false; drag.snapGuideX = null; drag.snapGuideY = null
  drag.resizeHandle = null; drag.rotatingShape = false
  const floor = layoutFloors.value[fi]
  if (floor.layoutCanvasRef) floor.layoutCanvasRef.style.cursor = 'crosshair'
  drawFloor(fi)
}

const onCanvasWheel = (e: WheelEvent, fi: number) => {
  const floor = layoutFloors.value[fi]
  const pan   = getFloorPan(floor.floorId)
  pan.scale   = Math.min(4, Math.max(0.25, pan.scale + (e.deltaY > 0 ? -0.1 : 0.1)))
  floor.scale = pan.scale; drawFloor(fi)
  if (fi > 0) drawGhostCanvas(fi)
}

const applyResize = (
  target: { x: number; y: number; width: number; height: number },
  handle: string, ox: number, oy: number, ow: number, oh: number, dx: number, dy: number
) => {
  const MIN_W = 40, MIN_H = 30
  let newX = ox, newY = oy, newW = ow, newH = oh
  switch (handle) {
    case 'se': newW = Math.max(MIN_W, ow + dx); newH = Math.max(MIN_H, oh + dy); break
    case 'sw': { const nw = Math.max(MIN_W, ow - dx); newX = ox + (ow - nw); newW = nw; newH = Math.max(MIN_H, oh + dy); break }
    case 'ne': { newW = Math.max(MIN_W, ow + dx); const nh = Math.max(MIN_H, oh - dy); newY = oy + (oh - nh); newH = nh; break }
    case 'nw': { const nw = Math.max(MIN_W, ow - dx); newX = ox + (ow - nw); newW = nw; const nh = Math.max(MIN_H, oh - dy); newY = oy + (oh - nh); newH = nh; break }
    case 'n':  { const nh = Math.max(MIN_H, oh - dy); newY = oy + (oh - nh); newH = nh; break }
    case 's':  newH = Math.max(MIN_H, oh + dy); break
    case 'e':  newW = Math.max(MIN_W, ow + dx); break
    case 'w':  { const nw = Math.max(MIN_W, ow - dx); newX = ox + (ow - nw); newW = nw; break }
  }
  target.x = newX; target.y = newY; target.width = Math.max(MIN_W, newW); target.height = Math.max(MIN_H, newH)
}

// ── Shape actions ──────────────────────────────────────────
const addCustomShape = (fi: number, type: 'rect' | 'ellipse') => {
  const floor = layoutFloors.value[fi]
  const shape: CanvasShape = {
    id: crypto.randomUUID(), type, x: CANVAS_W / 2 - 100, y: CANVAS_H / 2 - 60,
    width: 200, height: 120, label: `Zone ${floor.canvasShapes.length + 1}`,
    color: floor.selectedColor, accessible: true,
  }
  floor.canvasShapes.push(shape); floor.selectedShapeId = shape.id; drawFloor(fi)
}

const addStageShape = (fi: number, type: 'rect' | 'ellipse') => {
  const floor = layoutFloors.value[fi]
  const stageCount = floor.canvasShapes.filter(s => s.isStage).length
  floor.canvasShapes.push({
    id: crypto.randomUUID(), type, x: CANVAS_W / 2 - 100, y: 20 + stageCount * 70,
    width: 200, height: 50, label: stageCount === 0 ? 'Stage' : `Stage ${stageCount + 1}`,
    color: '#f59e0b', accessible: false, isStage: true,
  })
  floor.selectedShapeId = floor.canvasShapes[floor.canvasShapes.length - 1].id
  drawFloor(fi)
}

const addZoneShapeToFloor = (fi: number, zone: Zone) => {
  const floor = layoutFloors.value[fi]
  const already = floor.canvasShapes.find(s => s.label === zone.name)
  if (already) { floor.selectedShapeId = already.id; drawFloor(fi); return }
  floor.canvasShapes.push({
    id: crypto.randomUUID(), type: 'rect',
    x: CANVAS_W / 2 - 120, y: Math.min(80 + floor.canvasShapes.length * 30, CANVAS_H - 140),
    width: 240, height: 120, label: zone.name, color: '#6366f1', accessible: true,
  })
  floor.selectedShapeId = floor.canvasShapes[floor.canvasShapes.length - 1].id
  drawFloor(fi)
}

const clearFloor = (fi: number) => {
  const floor = layoutFloors.value[fi]
  floor.canvasShapes = []; floor.selectedShapeId = null; floor.editingStage = false
  drag.snapGuideX = null; drag.snapGuideY = null; drawFloor(fi)
}

const deleteSelectedShape = (fi: number) => {
  const floor = layoutFloors.value[fi]
  floor.canvasShapes    = floor.canvasShapes.filter(s => s.id !== floor.selectedShapeId)
  floor.selectedShapeId = null; drawFloor(fi)
}

// ── Build layout JSON ──────────────────────────────────────
// Multiple floors are stored as an array under a "floors" key:
// { "floors": [ { "floor_name", "floor_order", "stage", "zones", "stage_shapes" }, ... ] }

const buildFloorJson = (floor: LayoutFloor) => {
  const w = CANVAS_W, h = CANVAS_H, sb = floor.stageBox
  const zoneShapes  = floor.canvasShapes.filter(s => !s.isStage)
  const stageShapes = floor.canvasShapes.filter(s => s.isStage)
  return {
    floor_name:  floor.floorName,
    floor_order: floor.floorOrder,
    stage: { x1: toNorm(sb.x, w), y1: toNorm(sb.y, h), x2: toNorm(sb.x + sb.width, w), y2: toNorm(sb.y + sb.height, h) },
    zones: zoneShapes.map(s => ({
      zone_name:  s.label,
      zone_type:  s.accessible ? 'standing' : 'non_public',
      accessible: s.accessible,
      color:      s.color,
      zone_id:    s.zoneId ?? null,
      rotation:   s.rotation ?? 0,
      shape_type: s.type,
      corner1: { x: toNorm(s.x, w),           y: toNorm(s.y, h) },
      corner2: { x: toNorm(s.x + s.width, w), y: toNorm(s.y, h) },
      corner3: { x: toNorm(s.x + s.width, w), y: toNorm(s.y + s.height, h) },
      corner4: { x: toNorm(s.x, w),           y: toNorm(s.y + s.height, h) },
      seats: [],
    })),
    stage_shapes: stageShapes.map(s => ({
      label: s.label, type: s.type, rotation: s.rotation ?? 0,
      corner1: { x: toNorm(s.x, w),           y: toNorm(s.y, h) },
      corner2: { x: toNorm(s.x + s.width, w), y: toNorm(s.y, h) },
      corner4: { x: toNorm(s.x, w),           y: toNorm(s.y + s.height, h) },
    })),
  }
}

const buildFullLayoutJson = () => JSON.stringify({
  floors: layoutFloors.value.map(floor => buildFloorJson(floor))
})

const floorJsonPreviews = computed(() =>
  layoutFloors.value.map(floor => {
    try { return JSON.stringify(buildFloorJson(floor), null, 2) } catch { return '{}' }
  })
)

const copyFloorJson = (fi: number) => {
  navigator.clipboard?.writeText(floorJsonPreviews.value[fi])
}

// ── Seat overlay ───────────────────────────────────────────
const loadSeatsForStep4 = async () => {
  if (!savedSessionId.value || zones.value.length === 0) return
  loadingSeats.value = true
  try {
    const seatedZones = zones.value.filter(z => !z.isStanding && z.id)
    const results = await Promise.all(seatedZones.map(async (zone) => {
      const seats = await $fetch<SeatInfo[]>(
        `${config.public.apiUrl}/organizer/sessions/${savedSessionId.value}/zones/${zone.id}/seats`,
        { credentials: 'include' }
      )
      return { zoneId: zone.id!, seats }
    }))
    const next: SeatsByZone = {}
    for (const { zoneId, seats } of results) next[zoneId] = seats
    seatsByZone.value = next
  } catch (err) { console.error(err) }
  finally { loadingSeats.value = false }
}

const onSeatPriceUpdated = (seatId: string, priceOverride: number | null) => {
  for (const zoneId in seatsByZone.value) {
    const seat = seatsByZone.value[zoneId].find(s => s.id === seatId)
    if (seat) { seat.priceOverride = priceOverride; break }
  }
}

// Simple seat canvas renderer (no transforms — just zone overlays)
const drawSeatsOnCanvas = (fi: number) => {
  const floor = layoutFloors.value[fi]
  const canvas = floor?.seatCanvasRef; if (!canvas) return
  const ctx = canvas.getContext('2d'); if (!ctx) return
  ctx.clearRect(0, 0, canvas.width, canvas.height)

  const pan    = getFloorPan(floor.floorId)
  const scaleX = (floor.stageSize.width  / CANVAS_W) * pan.scale
  const scaleY = (floor.stageSize.height / CANVAS_H) * pan.scale

  ctx.save(); ctx.translate(pan.x, pan.y)

  for (const shape of floor.canvasShapes) {
    if (!shape.zoneId) continue
    const seats = seatsByZone.value[shape.zoneId]
    if (!seats || seats.length === 0) continue

    const baseR    = Math.max(4, 14 * scaleX)
    const gap      = baseR * 2 + 2
    const shapeW   = shape.width * scaleX
    const shapeH   = shape.height * scaleY

    // Group seats by row
    const rowMap = new Map<string, SeatInfo[]>()
    for (const seat of seats) {
      const m = (seat.seatCode ?? '').match(/^([A-Za-z]+)/)
      const row = m ? m[1].toUpperCase() : '__'
      if (!rowMap.has(row)) rowMap.set(row, [])
      rowMap.get(row)!.push(seat)
    }
    const seatRows = [...rowMap.keys()].sort().map(k => rowMap.get(k)!)
    const gridH    = seatRows.length * gap - 2
    const originY  = shape.y * scaleY + (shapeH - gridH) / 2 + baseR

    seatRows.forEach((rowSeats, rowIdx) => {
      const rowGridW   = rowSeats.length * gap - 2
      const rowOriginX = shape.x * scaleX + (shapeW - rowGridW) / 2 + baseR
      rowSeats.forEach((seat, colIdx) => {
        const cx = rowOriginX + colIdx * gap
        const cy = originY + rowIdx * gap
        ctx.fillStyle   = seat.priceOverride !== null ? '#f59e0b' : (shape.color + 'dd')
        ctx.globalAlpha = 0.9
        ctx.beginPath(); ctx.arc(cx, cy, baseR, 0, Math.PI * 2); ctx.fill()
        if (baseR >= 8) {
          ctx.globalAlpha = 0.92; ctx.fillStyle = '#ffffff'
          ctx.font = `500 ${Math.max(7, Math.min(11, baseR * 0.72))}px sans-serif`
          ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
          ctx.fillText(seat.seatCode ?? '', cx, cy)
        }
      })
    })
  }
  ctx.globalAlpha = 1; ctx.restore()
}

const rebuildAndDraw = () => {
  nextTick(() => layoutFloors.value.forEach((_, fi) => drawSeatsOnCanvas(fi)))
}

// ── Save layout ────────────────────────────────────────────
const saveLayout = async () => {
  if (!savedEventId.value) return
  saving.value = true; globalError.value = ''
  try {
    if (layoutMode.value === 'venue') {
      await $fetch(`${config.public.apiUrl}/organizer/events/${savedEventId.value}/layout`, {
        method: 'PUT',
        body: { useVenueLayout: true },
        credentials: 'include',
      })
    } else {
      if (layoutFloors.value.length === 0) {
        globalError.value = 'Please add at least one floor in the custom layout.'; saving.value = false; return
      }
      const customLayoutJson = buildFullLayoutJson()
      await $fetch(`${config.public.apiUrl}/organizer/events/${savedEventId.value}/layout`, {
        method: 'PUT',
        body: { useVenueLayout: false, customLayoutJson },
        credentials: 'include',
      })
    }
    router.push('/organizer/events')
  } catch (err: any) { globalError.value = err?.data?.message ?? 'Failed to save layout' }
  finally { saving.value = false }
}

// ── Load existing event ────────────────────────────────────
const loadEvent = async () => {
  if (!savedEventId.value) return
  try {
    const [event, eventSessions, layoutData] = await Promise.all([
      $fetch<any>(`${config.public.apiUrl}/organizer/events/${savedEventId.value}`, { credentials: 'include' }),
      $fetch<any[]>(`${config.public.apiUrl}/organizer/events/${savedEventId.value}/sessions`, { credentials: 'include' }).catch(() => []),
      $fetch<any>(`${config.public.apiUrl}/organizer/events/${savedEventId.value}/layout`, { credentials: 'include' }).catch(() => null),
    ])

    form.value = {
      name:        event.name ?? '',
      venueId:     event.venueId ?? '',
      categoryId:  event.categoryId ?? '',
      addressLine: event.addressLine ?? '',
      bannerUrl:   event.bannerUrl ?? '',
      status:      event.status ?? 'EDITING',
      startDate:   eventSessions[0]?.startDate ? eventSessions[0].startDate.slice(0, 16) : '',
      endDate:     eventSessions[0]?.endDate   ? eventSessions[0].endDate.slice(0, 16)   : '',
    }

    content.value = {
      aboutVi:            event.aboutVi ?? '',
      aboutEn:            event.aboutEn ?? '',
      termsAndConditions: event.termsAndConditions ?? '',
      policyRefund:       event.policyRefund ?? '',
    }

    if (eventSessions.length > 0) {
      savedSessionId.value = eventSessions[0].id
      zones.value = await $fetch<Zone[]>(
        `${config.public.apiUrl}/organizer/sessions/${savedSessionId.value}/zones`,
        { credentials: 'include' }
      ).catch(() => [])
    }

    // Restore layout
    if (layoutData?.eventLayout) {
      try {
        const parsed = JSON.parse(layoutData.eventLayout)
        if (parsed.floors && Array.isArray(parsed.floors)) {
          // Custom multi-floor layout
          layoutMode.value  = 'custom'
          layoutFloors.value = parsed.floors.map((fl: any) => {
            const floor = makeFloor(fl.floor_order ?? 1)
            floor.floorName = fl.floor_name ?? floor.floorName

            const toPixel = (n: number, total: number) => ((n + 1) / 2) * total
            floor.canvasShapes = (fl.zones ?? []).map((z: any) => {
              const x = toPixel(z.corner1?.x ?? -0.3, CANVAS_W), y = toPixel(z.corner1?.y ?? -0.5, CANVAS_H)
              return {
                id: crypto.randomUUID(), type: (z.shape_type ?? 'rect') as 'rect' | 'ellipse', x, y,
                width:  Math.max(toPixel(z.corner2?.x ?? 0.3, CANVAS_W) - x, 80),
                height: Math.max(toPixel(z.corner4?.y ?? 0.5, CANVAS_H) - y, 60),
                label:  z.zone_name ?? 'Zone', color: z.color ?? '#6366f1',
                accessible: z.accessible !== false,
                zoneId: z.zone_id ? (zones.value.find(zn => zn.id === z.zone_id)?.id ?? zones.value.find(zn => zn.name === z.zone_name)?.id) : undefined,
                rotation: z.rotation ?? 0,
              }
            })
            const stageShapes = (fl.stage_shapes ?? []).map((s: any) => {
              const x = toPixel(s.corner1?.x ?? -0.1, CANVAS_W), y = toPixel(s.corner1?.y ?? -0.5, CANVAS_H)
              return {
                id: crypto.randomUUID(), type: (s.type ?? 'rect') as 'rect' | 'ellipse', x, y,
                width: Math.max(toPixel(s.corner2?.x ?? 0.1, CANVAS_W) - x, 60),
                height: Math.max(toPixel(s.corner4?.y ?? -0.3, CANVAS_H) - y, 30),
                label: s.label ?? 'Stage', color: '#f59e0b', accessible: false, isStage: true, rotation: s.rotation ?? 0,
              }
            })
            floor.canvasShapes = [...floor.canvasShapes, ...stageShapes]
            if (fl.stage) {
              floor.stageBox = {
                x: toPixel(fl.stage.x1, CANVAS_W), y: toPixel(fl.stage.y1, CANVAS_H),
                width: Math.round(toPixel(fl.stage.x2, CANVAS_W) - toPixel(fl.stage.x1, CANVAS_W)),
                height: Math.round(toPixel(fl.stage.y2, CANVAS_H) - toPixel(fl.stage.y1, CANVAS_H)),
              }
            }
            return floor
          })
          nextTick(() => layoutFloors.value.forEach((_, fi) => drawFloor(fi)))
        }
      } catch { /* leave default */ }
    } else {
      // No custom layout — default to venue mode
      layoutMode.value = 'venue'
    }
  } catch { globalError.value = 'Failed to load event data' }
}

// ── Lifecycle ──────────────────────────────────────────────
onMounted(async () => {
  await Promise.all([fetchCategories(), fetchVenues()])
  if (!isNew.value) await loadEvent()
  // Initialize with one floor if custom mode and no floors
  if (layoutFloors.value.length === 0) layoutFloors.value.push(makeFloor(1))
})

watch(currentStep, async (step) => {
  if (step === 2 && savedSessionId.value) await fetchZones()
  if (step === 3 && savedSessionId.value) {
    await fetchZones()
    await loadSeatsForStep4()
    nextTick(() => layoutFloors.value.forEach((_, fi) => drawFloor(fi)))
  }
})

watch(seatsByZone, rebuildAndDraw, { deep: false })

// ── Helpers ────────────────────────────────────────────────
const parsedPerks = (perks: string | string[] | undefined): string[] => {
  if (!perks) return []
  if (Array.isArray(perks)) return perks
  try { return JSON.parse(perks) } catch { return [] }
}

const formatPrice = (p: number) => p === 0 ? 'Free' : new Intl.NumberFormat('vi-VN').format(p) + ' ₫'
</script>

<style scoped>
.step-bar { align-items: center; }
.step-item { cursor: pointer; min-width: 0; }
.step-dot {
  width: 32px; height: 32px; font-size: 0.8rem; font-weight: 700; flex-shrink: 0;
  background: rgba(var(--bs-secondary-rgb), 0.3); color: var(--bs-secondary);
  transition: background 0.2s, color 0.2s;
}
.step-item.active .step-dot    { background: var(--bs-primary); color: #fff; }
.step-item.completed .step-dot { background: #22c55e; color: #fff; }
.step-label { color: var(--bs-secondary); transition: color 0.2s; }
.step-item.active .step-label, .step-item.completed .step-label { color: var(--text-reactive-primary, inherit); }
.step-line { height: 2px; background: rgba(var(--bs-secondary-rgb), 0.25); flex-shrink: 0; min-width: 8px; }
.step-item.completed .step-line { background: #22c55e; }
.zone-card { background: rgba(var(--bs-secondary-rgb), 0.15); border-left: 4px solid #6366f1; transition: box-shadow 0.15s; }
.zone-card:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.12); }
.modal-backdrop-custom { position: fixed; inset: 0; background: rgba(0,0,0,0.55); z-index: 1050; display: flex; align-items: center; justify-content: center; padding: 1rem; }
.nav-link { color: var(--bs-secondary); background: none; border: none; border-bottom: 2px solid transparent; border-radius: 0; padding: 0.5rem 1rem; cursor: pointer; }
.nav-link.active { color: var(--bs-primary); border-bottom-color: var(--bs-primary); }
.zone-palette-btn { background: transparent; border: 1px solid rgba(var(--bs-secondary-rgb), 0.4); border-radius: 6px; padding: 4px 10px; font-size: 0.8rem; color: var(--bs-secondary); transition: background 0.15s; }
.zone-palette-btn:hover { background: rgba(255,255,255,0.08); color: var(--text-reactive-primary, inherit); }
</style>