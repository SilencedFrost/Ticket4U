<template>
  <div>
    <div v-if="zones.length === 0" class="alert alert-warning d-flex align-items-center gap-2 mb-4">
      <i class="bi bi-exclamation-triangle-fill"/>
      <span>{{ $t('organizer.event_form.step4.no_zones_warning') }}</span>
    </div>

    <div class="card bg-reactive-secondary border-0 p-4 mb-4">
      <h5 class="fw-semibold text-reactive-primary mb-3"><i class="bi bi-layers me-2 text-primary"/>{{ $t('organizer.event_form.step4.title') }}</h5>

      <!-- Venue picker -->
      <div v-if="le.layoutMode.value === 'venue'" class="mb-4">
        <label class="form-label small fw-semibold text-reactive-secondary">{{ $t('organizer.event_form.step4.venue') }} <span class="text-danger">*</span></label>
        <select v-model="form.venueId" class="form-select bg-reactive-primary border-0 text-reactive-primary">
          <option value="">— Select a venue —</option>
          <option v-for="v in venues" :key="v.id" :value="v.id">{{ v.name }} — {{ v.addressLine }}</option>
        </select>
        <div v-if="selectedVenue" class="mt-2 d-flex align-items-center gap-2">
          <img v-if="selectedVenue.imageUrl" :src="selectedVenue.imageUrl" class="rounded" style="width:48px;height:32px;object-fit:cover;"/>
          <small class="text-reactive-secondary"><i class="bi bi-geo-alt me-1"/>{{ selectedVenue.addressLine }}</small>
        </div>
      </div>

      <!-- Layout mode toggle -->
      <div class="btn-group mb-4">
        <button class="btn" :class="le.layoutMode.value === 'venue' ? 'btn-primary' : 'btn-outline-secondary'" @click="le.layoutMode.value = 'venue'">
          <i class="bi bi-building me-2"/>{{ $t('organizer.event_form.step4.venue_mode') }}
        </button>
        <button class="btn" :class="le.layoutMode.value === 'custom' ? 'btn-primary' : 'btn-outline-secondary'" @click="le.layoutMode.value = 'custom'">
          <i class="bi bi-pencil-square me-2"/>{{ $t('organizer.event_form.step4.custom_mode') }}
        </button>
      </div>

      <!-- VENUE MODE -->
      <div v-if="le.layoutMode.value === 'venue'">
        <div v-if="!selectedVenue" class="alert alert-warning py-2 small">
          <i class="bi bi-exclamation-triangle me-1"/>{{ $t('organizer.event_form.step4.no_venue') }}
        </div>
        <div v-else-if="!selectedVenueLayout" class="alert alert-info py-2 small">
          <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step4.venue_no_layout') }}
        </div>
        <div v-else class="row g-4">
          <div class="col-lg-7">
            <div class="fw-semibold text-reactive-primary mb-2">{{ selectedVenue.name }}</div>
            <ClientOnly>
              <LayoutPreview :layout-json="selectedVenueLayout" style="height:340px;"/>
              <template #fallback><div class="bg-reactive-primary rounded" style="height:340px;"/></template>
            </ClientOnly>
          </div>
          <div class="col-lg-5">
            <div class="fw-semibold text-reactive-primary mb-3">
              <i class="bi bi-link-45deg me-2 text-primary"/>{{ $t('organizer.event_form.step4.link_zones') }}
            </div>
            <div v-if="zones.length === 0" class="alert alert-warning py-2 small">
              <i class="bi bi-exclamation-triangle me-1"/>{{ $t('organizer.event_form.step4.no_zones_link') }}
            </div>
            <div v-else>
              <p class="small text-reactive-secondary mb-3">{{ $t('organizer.event_form.step4.link_description') }}</p>
              <div v-for="venueZone in venueLayoutZones" :key="venueZone.zone_name" class="d-flex align-items-center gap-2 mb-2">
                <div class="d-flex align-items-center gap-2 flex-shrink-0" style="min-width:130px;">
                  <div class="rounded-circle flex-shrink-0" :style="{ width:'10px', height:'10px', background: venueZone.color ?? '#6366f1' }"/>
                  <small class="text-reactive-primary fw-semibold text-truncate">{{ venueZone.zone_name }}</small>
                </div>
                <i class="bi bi-arrow-right text-reactive-secondary flex-shrink-0"/>
                <select
                    :value="le.venueZoneLinks.value[venueZone.zone_name] ?? ''"
                        class="form-select form-select-sm bg-reactive-primary border-0 text-reactive-primary flex-grow-1"
                        @change="(e) => le.venueZoneLinks.value[venueZone.zone_name] = (e.target as HTMLSelectElement).value || ''">
                  <option value="">{{ $t('organizer.event_form.step4.decorative') }}</option>
                  <optgroup :label="$t('organizer.event_form.step4.seated_zones')">
                    <option v-for="z in zones.filter(z => !z.isStanding)" :key="z.id" :value="z.id!">{{ z.name }} ({{ z.seatCount }} seats)</option>
                  </optgroup>
                  <optgroup :label="$t('organizer.event_form.step4.standing_zones')">
                    <option v-for="z in zones.filter(z => z.isStanding)" :key="z.id" :value="z.id!">{{ z.name }} (standing · {{ z.capacity }})</option>
                  </optgroup>
                </select>
              </div>
              <div class="alert alert-info py-2 small mt-3 mb-0">
                <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step4.link_hint') }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- CUSTOM MODE -->
      <div v-if="le.layoutMode.value === 'custom'">
        <!-- Floor tabs -->
        <div class="d-flex align-items-center gap-2 mb-3 flex-wrap">
          <div class="d-flex gap-1 flex-wrap">
            <button
                v-for="(floor, fi) in le.layoutFloors.value" :key="floor.floorId"
                    class="btn btn-sm" :class="le.activeFloorIdx.value === fi ? 'btn-primary' : 'btn-outline-secondary'"
                    @click="le.activeFloorIdx.value = fi">
              Floor {{ fi + 1 }}
              <span v-if="floor.floorName" class="ms-1 opacity-75">({{ floor.floorName }})</span>
            </button>
          </div>
          <button class="btn btn-sm btn-outline-primary" @click="le.addFloor()"><i class="bi bi-plus-lg me-1"/>{{ $t('organizer.event_form.step4.add_floor') }}</button>
          <button v-if="le.layoutFloors.value.length > 1" class="btn btn-sm btn-outline-danger" @click="le.removeFloor(le.activeFloorIdx.value)"><i class="bi bi-trash me-1"/>{{ $t('organizer.event_form.step4.remove_floor') }}</button>
        </div>

        <div v-if="activeFloor" class="card bg-reactive-primary border-0">
          <!-- Toolbar -->
          <div class="p-3 border-bottom border-secondary d-flex justify-content-between align-items-center flex-wrap gap-2">
            <div class="d-flex align-items-center gap-2 flex-wrap">
              <input v-model="activeFloor.floorName" type="text" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary fw-semibold" style="max-width:180px;" :placeholder="$t('organizer.event_form.step4.floor_name_placeholder')"/>
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
              <button class="btn btn-sm btn-outline-secondary" @click="le.addCustomShape(activeFloorIdx, 'rect')"><i class="bi bi-square me-1"/>{{ $t('organizer.event_form.step4.shape_rect') }}</button>
              <button class="btn btn-sm btn-outline-secondary" @click="le.addCustomShape(activeFloorIdx, 'ellipse')"><i class="bi bi-circle me-1"/>{{ $t('organizer.event_form.step4.shape_ellipse') }}</button>
              <button class="btn btn-sm btn-outline-warning" @click="le.addStageShape(activeFloorIdx, 'rect')"><i class="bi bi-collection-play me-1"/>{{ $t('organizer.event_form.step4.stage') }}</button>
              <input type="color"
                     v-model="activeFloor.selectedColor" class="form-control form-control-sm border-0 p-0" style="width:32px;height:32px;cursor:pointer;background:none;"/>
              <button class="btn btn-sm btn-outline-danger" @click="le.clearFloor(activeFloorIdx)"><i class="bi bi-trash me-1"/>{{ $t('organizer.event_form.step4.clear') }}</button>
              <div class="d-flex align-items-center gap-1 ms-1">
                <i class="bi bi-circle text-reactive-secondary" style="font-size:0.7rem;"/>
                <input
                    type="range" min="6" max="32" step="1" :value="activeFloor.globalSeatSize" class="form-range" style="width:72px;"
                       @input="(e) => { activeFloor!.globalSeatSize = +(e.target as HTMLInputElement).value; le.rebuildAndDraw() }"/>
                <small class="text-reactive-secondary" style="min-width:22px;">{{ activeFloor.globalSeatSize }}</small>
              </div>
            </div>
            <div class="d-flex align-items-center gap-2">
              <button class="btn btn-sm" :class="activeFloor.snapEnabled ? 'btn-primary' : 'btn-outline-secondary'" @click="activeFloor.snapEnabled = !activeFloor.snapEnabled">
                <i class="bi bi-magnet me-1"/>{{ $t('organizer.event_form.step4.snap') }}
              </button>
              <button class="btn btn-sm btn-outline-secondary" @click="activeFloor.showJsonPanel = !activeFloor.showJsonPanel">
                <i class="bi" :class="activeFloor.showJsonPanel ? 'bi-code-slash' : 'bi-code'"/>JSON
              </button>
              <button
                  class="btn btn-sm btn-outline-info" :disabled="!le.layoutSaved.value"
                      :title="le.layoutSaved.value ? $t('organizer.event_form.step4.load_saved') : $t('organizer.event_form.step4.no_saved_layout')"
                      @click="onReloadSavedLayout">
                <i class="bi bi-arrow-counterclockwise me-1"/>{{ $t('organizer.event_form.step4.load_saved') }}
              </button>
            </div>
          </div>

          <!-- Zone palette -->
          <div v-if="zones.length > 0" class="px-3 py-2 border-bottom border-secondary d-flex gap-2 flex-wrap align-items-center">
            <small class="text-reactive-secondary me-1">{{ $t('organizer.event_form.step4.place_zone') }}</small>
            <button
                v-for="zone in zones" :key="zone.id"
                    class="btn btn-sm zone-palette-btn" :class="le.isZoneLinked(zone) ? 'zone-palette-btn--linked' : ''"
                    :title="le.isZoneLinked(zone) ? $t('organizer.event_form.step4.already_placed') : $t('organizer.event_form.step4.place_on_canvas')"
                    @click="le.addZoneShapeToFloor(activeFloorIdx, zone)">
              <i v-if="le.isZoneLinked(zone)" class="bi bi-check2 me-1 text-success"/>
              {{ zone.name }}
            </button>
          </div>

          <!-- Canvas -->
          <div class="d-flex" style="height:600px;">
            <div v-if="activeFloor.showJsonPanel" class="border-end border-secondary overflow-auto flex-shrink-0" style="width:300px;background:#0d1117;font-family:monospace;font-size:11px;">
              <div class="d-flex justify-content-between align-items-center px-2 py-1 border-bottom border-secondary sticky-top" style="background:#161b22;">
                <span class="text-success fw-semibold" style="font-size:11px;">{{ $t('organizer.event_form.step4.json_panel') }}</span>
                <button class="btn btn-sm p-0 px-1 text-secondary" @click="le.copyFloorJson(activeFloorIdx)"><i class="bi bi-clipboard"/></button>
              </div>
              <pre class="m-0 p-2 text-light" style="white-space:pre-wrap;word-break:break-all;">{{ le.floorJsonPreviews.value[activeFloorIdx] }}</pre>
            </div>
            <div
                :ref="el => le.setCanvasContainerRef(el, activeFloorIdx)"
                 class="position-relative overflow-hidden flex-grow-1"
                 style="background: repeating-linear-gradient(0deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px), repeating-linear-gradient(90deg,transparent,transparent 39px,rgba(255,255,255,.04) 39px,rgba(255,255,255,.04) 40px); user-select:none;">
              <canvas
                  v-if="activeFloorIdx > 0"
                      :ref="el => { if (el) activeFloor!.ghostCanvasRef = el as HTMLCanvasElement }"
                      class="position-absolute top-0 start-0"
                      :width="activeFloor.stageSize.width" :height="activeFloor.stageSize.height"
                      style="z-index:1; pointer-events:none;"/>
              <canvas
                  :ref="el => le.setFloorCanvasRef(el, activeFloorIdx)"
                      class="position-absolute top-0 start-0"
                      :width="activeFloor.stageSize.width" :height="activeFloor.stageSize.height"
                      style="z-index:5; cursor:crosshair;"
                      @mousedown="(e) => le.onCanvasMouseDown(e, activeFloorIdx)"
                      @mousemove="(e) => le.onCanvasMouseMove(e, activeFloorIdx)"
                      @mouseup="(e) => le.onCanvasMouseUp(e, activeFloorIdx)"
                      @mouseleave="(e) => le.onCanvasMouseUp(e, activeFloorIdx)"
                      @wheel.prevent="(e) => le.onCanvasWheel(e, activeFloorIdx)"/>
              <canvas
                  v-if="!le.loadingSeats.value"
                      :ref="el => { if (el) activeFloor!.seatCanvasRef = el as HTMLCanvasElement }"
                      class="position-absolute top-0 start-0"
                      :width="activeFloor.stageSize.width" :height="activeFloor.stageSize.height"
                      style="z-index:10; pointer-events:none;"/>
            </div>
          </div>

          <!-- Property bar -->
          <div class="p-3 border-top border-secondary">
            <!-- Shape selected -->
            <template v-if="activeFloor.selectedShapeId && activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)">
              <div class="d-flex align-items-center gap-2 flex-wrap">
                <small class="text-reactive-secondary fw-semibold">{{ $t('organizer.event_form.step4.zone_label') }}</small>
                <input
                    :value="activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.label"
                       type="text" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary" style="width:160px;"
                       @change="(e) => { activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.label = (e.target as HTMLInputElement).value; le.drawFloor(activeFloorIdx) }"/>
                <div class="d-flex align-items-center gap-1">
                  <small class="text-reactive-secondary">W</small>
                  <input
                      type="number" min="40" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;"
                         :value="Math.round(activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.width)"
                         @change="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.width = Math.max(40, +(e.target as HTMLInputElement).value); le.drawFloor(activeFloorIdx) }"/>
                  <small class="text-reactive-secondary">H</small>
                  <input
                      type="number" min="30" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;"
                         :value="Math.round(activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.height)"
                         @change="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.height = Math.max(30, +(e.target as HTMLInputElement).value); le.drawFloor(activeFloorIdx) }"/>
                </div>
                <input
                    type="color" :value="activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.color"
                       class="form-control form-control-sm border-0 p-0" style="width:36px;height:32px;"
                       @change="(e) => { activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.color = (e.target as HTMLInputElement).value; le.drawFloor(activeFloorIdx) }"/>
                <div
                     v-if="!activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.isStage"
                     class="d-flex align-items-center gap-1 ms-1"
                >
                  <i class="bi bi-circle text-reactive-secondary" style="font-size:0.7rem;"/>
                  <input type="range" min="6" max="32" step="1"
                         :value="activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.seatSize ?? activeFloor.globalSeatSize"
                         class="form-range" style="width:72px;"
                         @input="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.seatSize = +(e.target as HTMLInputElement).value; le.rebuildAndDraw() }"/>
                  <small class="text-reactive-secondary" style="min-width:22px;">{{ activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.seatSize ?? activeFloor.globalSeatSize }}</small>
                  <button
                      class="btn btn-sm btn-outline-secondary p-0 px-1"
                      title="Reset to global size"
                          @click="() => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.seatSize = undefined; le.rebuildAndDraw() }">
                    <i class="bi bi-arrow-counterclockwise" style="font-size:0.7rem;"/>
                  </button>
                </div>
                <select
                    :value="activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.zoneId ?? ''"
                        class="form-select form-select-sm bg-reactive-secondary border-0 text-reactive-primary" style="width:160px;"
                        @change="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.zoneId = (e.target as HTMLSelectElement).value || undefined; if (s.zoneId) { const z = zones.find(z => z.id === s.zoneId); if (z) le.autoResizeShapeForZone(activeFloorIdx, s, z) }; le.rebuildAndDraw() }">
                  <option value="">{{ $t('organizer.event_form.step4.decorative') }}</option>
                  <optgroup :label="$t('organizer.event_form.step4.seated_zones')">
                    <option v-for="z in zones.filter(z => !z.isStanding)" :key="z.id" :value="z.id">{{ z.name }} ({{ z.seatCount }} seats)</option>
                  </optgroup>
                  <optgroup :label="$t('organizer.event_form.step4.standing_zones')">
                    <option v-for="z in zones.filter(z => z.isStanding)" :key="z.id" :value="z.id">{{ z.name }} (standing · {{ z.capacity }})</option>
                  </optgroup>
                </select>
                <button class="btn btn-sm btn-outline-danger ms-auto" @click="le.deleteSelectedShape(activeFloorIdx)"><i class="bi bi-trash"/></button>
              </div>
              <div class="d-flex align-items-center gap-2 mt-2">
                <small class="text-reactive-secondary fw-semibold"><i class="bi bi-arrow-clockwise me-1"/>{{ $t('organizer.event_form.step4.rotation') }}</small>
                <input type="range" min="-180" max="180" step="1"
                       :value="Math.round(((activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)?.rotation ?? 0) * 180 / Math.PI))"
                       class="form-range flex-grow-1"
                       @input="(e) => { const s = activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!; s.rotation = +(e.target as HTMLInputElement).value * Math.PI / 180; le.drawFloor(activeFloorIdx) }"/>
                <small class="text-reactive-secondary" style="min-width:42px;text-align:right">{{ Math.round(((activeFloor.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)?.rotation ?? 0) * 180 / Math.PI)) }}°</small>
                <button class="btn btn-sm btn-outline-secondary flex-shrink-0" @click="() => { activeFloor!.canvasShapes.find(s => s.id === activeFloor!.selectedShapeId)!.rotation = 0; le.drawFloor(activeFloorIdx) }">{{ $t('common.reset') }}</button>
              </div>
            </template>

            <!-- Seat selected -->
            <template v-else-if="activeFloor.selectedSeatId">
              <div class="d-flex align-items-center gap-2 flex-wrap">
                <small class="text-reactive-secondary fw-semibold"><i class="bi bi-circle me-1"/>{{ le.selectedSeatInfo.value?.seatCode ?? 'Seat' }}</small>
                <div class="vr mx-1 opacity-25"/>
                <small class="text-reactive-secondary"><i class="bi bi-tag me-1"/>{{ $t('organizer.event_form.step4.price_override') }}</small>
                <div class="input-group input-group-sm" style="width:160px;">
                  <input type="number" min="0"
                         :value="le.selectedSeatInfo.value?.priceOverride ?? ''"
                         class="form-control bg-reactive-secondary border-0 text-reactive-primary"
                         :placeholder="selectedSeatZonePrice !== null ? `Default (${formatPrice(selectedSeatZonePrice)})` : 'Default'"
                         @change="(e) => onSaveSeatPrice((e.target as HTMLInputElement).value)"/>
                  <span class="input-group-text bg-reactive-secondary border-0 text-reactive-secondary">₫</span>
                </div>
                <button
                    v-if="le.selectedSeatInfo.value?.priceOverride !== null && le.selectedSeatInfo.value?.priceOverride !== undefined"
                        class="btn btn-sm btn-outline-secondary" @click="onSaveSeatPrice('')"><i class="bi bi-x"/></button>
                <div class="vr mx-1 opacity-25"/>
                <small class="text-reactive-secondary"><i class="bi bi-arrow-clockwise me-1"/>{{ $t('organizer.event_form.step4.rotation_short') }}</small>
                <input type="range" min="-180" max="180" step="1"
                       :value="Math.round((activeFloor.seatTransforms[activeFloor.selectedSeatId]?.rotation ?? 0) * 180 / Math.PI)"
                       class="form-range" style="width:80px;"
                       @input="(e) => { le.ensureSeatTransform(activeFloorIdx, activeFloor!.selectedSeatId!); activeFloor!.seatTransforms[activeFloor!.selectedSeatId!].rotation = +(e.target as HTMLInputElement).value * Math.PI / 180; le.drawSeatsOnCanvas(activeFloorIdx) }"/>
                <small class="text-reactive-secondary" style="min-width:32px;">{{ Math.round((activeFloor.seatTransforms[activeFloor.selectedSeatId]?.rotation ?? 0) * 180 / Math.PI) }}°</small>
                <div class="vr mx-1 opacity-25"/>
                <small class="text-reactive-secondary"><i class="bi bi-arrows-angle-expand me-1"/>{{ $t('organizer.event_form.step4.size') }}</small>
                <input type="range" min="0.3" max="3" step="0.05"
                       :value="activeFloor.seatTransforms[activeFloor.selectedSeatId]?.scale ?? 1"
                       class="form-range" style="width:70px;"
                       @input="(e) => { le.ensureSeatTransform(activeFloorIdx, activeFloor!.selectedSeatId!); activeFloor!.seatTransforms[activeFloor!.selectedSeatId!].scale = +(e.target as HTMLInputElement).value; le.rebuildAndDraw() }"/>
                <small class="text-reactive-secondary" style="min-width:30px;">{{ ((activeFloor.seatTransforms[activeFloor.selectedSeatId]?.scale ?? 1) * 100).toFixed(0) }}%</small>
                <div class="vr mx-1 opacity-25"/>
                <button class="btn btn-sm btn-outline-secondary flex-shrink-0" @click="() => { delete activeFloor!.seatTransforms[activeFloor!.selectedSeatId!]; le.rebuildAndDraw() }">
                  <i class="bi bi-arrow-counterclockwise me-1"/>{{ $t('common.reset') }}
                </button>
                <button class="btn btn-sm btn-outline-secondary ms-auto" @click="activeFloor.selectedSeatId = null; le.drawSeatsOnCanvas(activeFloorIdx)"><i class="bi bi-x-lg"/></button>
              </div>
            </template>

            <!-- Stage selected -->
            <template v-else-if="activeFloor.editingStage">
              <div class="d-flex align-items-center gap-2 flex-wrap">
                <small class="text-reactive-secondary fw-semibold"><i class="bi bi-tv me-1"/>{{ $t('organizer.event_form.step4.stage_screen') }}</small>
                <div class="d-flex align-items-center gap-1">
                  <small class="text-reactive-secondary">W</small>
                  <input type="number" min="80" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;"
                         :value="activeFloor.stageBox.width"
                         @change="(e) => { activeFloor!.stageBox.width = Math.max(80, +(e.target as HTMLInputElement).value); le.drawFloor(activeFloorIdx) }"/>
                  <small class="text-reactive-secondary">H</small>
                  <input type="number" min="20" class="form-control form-control-sm bg-reactive-secondary border-0 text-reactive-primary text-center" style="width:72px;"
                         :value="activeFloor.stageBox.height"
                         @change="(e) => { activeFloor!.stageBox.height = Math.max(20, +(e.target as HTMLInputElement).value); le.drawFloor(activeFloorIdx) }"/>
                </div>
                <button class="btn btn-sm btn-outline-secondary ms-auto" @click="activeFloor.editingStage = false; le.drawFloor(activeFloorIdx)"><i class="bi bi-x-lg"/></button>
              </div>
            </template>

            <!-- Nothing selected -->
            <template v-else>
              <small class="text-reactive-secondary">
                <i class="bi bi-info-circle me-1"/>{{ $t('organizer.event_form.step4.hint') }}
                <span v-if="activeFloor.snapEnabled" class="text-primary ms-1"><i class="bi bi-magnet me-1"/>{{ $t('organizer.event_form.step4.snap') }} on</span>
              </small>
            </template>
          </div>
        </div>
      </div>
    </div>

    <div class="d-flex justify-content-between gap-2 mt-2">
      <button class="btn btn-outline-secondary px-4" @click="$emit('back')"><i class="bi bi-arrow-left me-1"/>{{ $t('organizer.event_form.back') }}</button>
      <div class="d-flex gap-2">
        <button class="btn btn-primary px-4" :disabled="saving" @click="onApplyLayout">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          <i v-else class="bi bi-floppy me-2"/>
          {{ $t('organizer.event_form.step4.save_layout') }}
        </button>
        <button class="btn btn-success px-4" :disabled="saving" @click="onApplyAndClose">
          <span v-if="saving" class="spinner-border spinner-border-sm me-2"/>
          <i v-else class="bi bi-check2 me-2"/>
          {{ $t('organizer.event_form.step4.save_close') }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import LayoutPreview from './LayoutPreview.vue'
import type { Zone } from '../composables/use-zones'
import type { useLayoutEditor } from '../composables/use-layout-editor'

const props = defineProps<{
  layoutEditor:    ReturnType<typeof useLayoutEditor>
  zones:           Zone[]
  savedEventId:    string | null
  savedSessionId:  string | null
  form:            { venueId: string; [key: string]: any }
}>()

const emit = defineEmits<{
  (e: 'back'): void
  (e: 'globalError', msg: string): void
}>()

const le     = props.layoutEditor
const saving = ref(false)

const { t: $t }  = useI18n()
const router     = useRouter()
const localePath = useLocalePath()
const config     = useRuntimeConfig()

const activeFloorIdx = computed(() => le.activeFloorIdx.value)
const activeFloor    = computed(() => le.activeFloor.value)

const tools = [
  { id: 'select', icon: 'bi bi-cursor',      label: 'Select' },
  { id: 'move',   icon: 'bi bi-arrows-move', label: 'Pan'    },
]

// Venues for venue mode
interface Venue { id: string; name: string; addressLine: string; imageUrl?: string; layout?: string }
const venues = ref<Venue[]>([])
const fetchVenues = async () => {
  try { venues.value = await $fetch<Venue[]>(`${config.public.apiUrl}/events/venues`, { credentials: 'include' }) }
  catch { venues.value = [] }
}
fetchVenues()

const selectedVenue       = computed(() => venues.value.find(v => v.id === props.form.venueId) ?? null)
const selectedVenueLayout = computed(() => selectedVenue.value?.layout ?? null)

const venueLayoutZones = computed(() => {
  if (!selectedVenueLayout.value) return []
  try {
    const parsed = JSON.parse(selectedVenueLayout.value)
    const zones: any[] = []
    if (parsed.floors) { for (const floor of parsed.floors) if (floor.zones) zones.push(...floor.zones) }
    else if (parsed.zones) zones.push(...parsed.zones)
    return zones
  } catch { return [] }
})

const selectedSeatZonePrice = computed<number | null>(() => {
  if (!le.selectedSeatInfo.value) return null
  return props.zones.find(z => z.id === le.selectedSeatInfo.value!.zoneId)?.price ?? null
})

const formatPrice = (p: number) => p === 0 ? 'Free' : new Intl.NumberFormat('vi-VN').format(p) + ' ₫'

const onSaveSeatPrice = async (val: string) => {
  if (!props.savedSessionId) return
  try { await le.saveSeatPriceInline(val, props.savedSessionId) }
  catch (err: any) { emit('globalError', err?.data?.message ?? 'Failed to update seat price') }
}

const onReloadSavedLayout = async () => {
  if (!props.savedEventId) return
  try { await le.reloadSavedLayout(props.savedEventId) }
  catch (err: any) {
    if (err.message === 'no_saved_layout') emit('globalError', $t('organizer.event_form.step4.no_saved_layout'))
    else emit('globalError', err?.data?.message ?? 'Failed to load saved layout')
  }
}

const onApplyLayout = async () => {
  if (!props.savedEventId) return
  saving.value = true
  try {
    await le.applyLayout(props.savedEventId, props.form, selectedVenueLayout.value, props.zones)
    if (props.savedSessionId) {
      // Refresh zones and seats after layout save
      await $fetch<any[]>(`${config.public.apiUrl}/events/sessions/${props.savedSessionId}/zones`, { credentials: 'include' })
          .then(z => { /* zones are managed in parent */ }).catch(() => {})
      await le.loadSeatsForStep4(props.savedSessionId, props.zones)
    }
    le.rebuildAndDraw()
  } catch (err: any) { emit('globalError', err.message ?? 'Failed to apply layout') }
  finally { saving.value = false }
}

const onApplyAndClose = async () => {
  await onApplyLayout()
  router.push(localePath('/organizer/events'))
}
</script>

<style scoped>
.zone-palette-btn { background: transparent; border: 1px solid rgba(var(--bs-secondary-rgb), 0.4); border-radius: 6px; padding: 4px 10px; font-size: 0.8rem; color: var(--bs-secondary); transition: background 0.15s; }
.zone-palette-btn:hover { background: rgba(255,255,255,0.08); color: var(--text-reactive-primary, inherit); }
.zone-palette-btn--linked { border-color: #22c55e; color: #22c55e; background: rgba(34,197,94,0.08); }
.zone-palette-btn--linked:hover { background: rgba(34,197,94,0.15); }
</style>