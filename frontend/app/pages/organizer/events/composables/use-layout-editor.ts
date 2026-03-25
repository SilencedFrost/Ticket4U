import { ref, computed, nextTick, watch } from 'vue'
import type { Zone } from './use-zones'

// ── Types ──────────────────────────────────────────────────
export interface CanvasShape {
    id: string; type: 'rect' | 'ellipse'; x: number; y: number
    width: number; height: number; label: string; color: string
    accessible: boolean; zoneId?: string; rotation?: number
    isStage?: boolean; seatSize?: number
}

export interface SeatTransform {
    seatId: string; dx: number; dy: number; rotation: number; scale: number
}

export interface LayoutFloor {
    floorId: string; floorName: string; floorOrder: number
    canvasShapes: CanvasShape[]; selectedShapeId: string | null
    activeTool: 'select' | 'move'; selectedColor: string; scale: number
    stageSize: { width: number; height: number }
    stageBox: { x: number; y: number; width: number; height: number }
    editingStage: boolean; showJsonPanel: boolean; snapEnabled: boolean
    globalSeatSize: number
    seatTransforms: Record<string, SeatTransform>
    selectedSeatId: string | null
    layoutCanvasRef: HTMLCanvasElement | null
    seatCanvasRef: HTMLCanvasElement | null
    ghostCanvasRef: HTMLCanvasElement | null
}

export interface SeatInfo {
    id: string; seatCode: string; priceOverride: number | null; zoneId: string
}

export interface SeatsByZone { [zoneId: string]: SeatInfo[] }

export type LayoutMode = 'venue' | 'custom'

// ── Constants ──────────────────────────────────────────────
export const SNAP_THRESHOLD  = 8
export const CANVAS_W        = 900
export const CANVAS_H        = 520
export const HANDLE_R        = 5
export const DEFAULT_SEAT_SIZE = 14

export const useLayoutEditor = (apiUrl: string) => {
    const layoutMode     = ref<LayoutMode>('venue')
    const layoutFloors   = ref<LayoutFloor[]>([])
    const activeFloorIdx = ref(0)
    const activeFloor    = computed(() => layoutFloors.value[activeFloorIdx.value] ?? null)
    const layoutSaved    = ref(false)

    const seatsByZone  = ref<SeatsByZone>({})
    const loadingSeats = ref(false)
    const seatHitMap   = ref<Array<Array<{ seatId: string; seat: SeatInfo; cx: number; cy: number; r: number }>>>([])

    const canvasContainerRefs: Record<number, HTMLElement> = {}

    const venueZoneLinks = ref<Record<string, string>>({})

    const drag = {
        active: false, fi: -1, targetId: '' as string,
        startX: 0, startY: 0, origX: 0, origY: 0,
        snapGuideX: null as number | null, snapGuideY: null as number | null,
        resizeHandle: null as string | null, resizeOrigW: 0, resizeOrigH: 0,
        panOffsetX: 0, panOffsetY: 0,
        rotatingShape: false, rotOrigAngle: 0, rotCx: 0, rotCy: 0,
        seatMode: '' as '' | 'move' | 'rotate' | 'resize',
        seatId: '' as string,
        seatOrigDx: 0, seatOrigDy: 0, seatOrigRot: 0, seatOrigScale: 1,
        seatCx: 0, seatCy: 0,
    }

    const floorPan: Record<string, { x: number; y: number; scale: number }> = {}
    const getFloorPan = (floorId: string) => {
        if (!floorPan[floorId]) floorPan[floorId] = { x: 0, y: 0, scale: 1 }
        return floorPan[floorId]
    }

    // ── Floor factory ────────────────────────────────────────
    const makeFloor = (order: number): LayoutFloor => ({
        floorId: crypto.randomUUID(),
        floorName: order === 1 ? 'Main Floor' : `Floor ${order}`,
        floorOrder: order,
        canvasShapes: [], selectedShapeId: null,
        activeTool: 'select', selectedColor: '#6366f1', scale: 1,
        stageSize: { width: 0, height: 0 },
        stageBox: { x: CANVAS_W / 2 - 120, y: order === 1 ? 20 : -200, width: 240, height: 44 },
        editingStage: false, showJsonPanel: false, snapEnabled: true,
        globalSeatSize: DEFAULT_SEAT_SIZE,
        seatTransforms: {}, selectedSeatId: null,
        layoutCanvasRef: null, seatCanvasRef: null, ghostCanvasRef: null,
    })

    const addFloor = () => {
        layoutFloors.value.push(makeFloor(layoutFloors.value.length + 1))
        activeFloorIdx.value = layoutFloors.value.length - 1
        nextTick(() => {
            const fi = activeFloorIdx.value
            const el = canvasContainerRefs[fi]
            const f = layoutFloors.value[fi]; if (el && f) f.stageSize = { width: el.clientWidth, height: CANVAS_H }
        })
    }

    const removeFloor = (fi: number) => {
        layoutFloors.value.splice(fi, 1)
        layoutFloors.value.forEach((f, i) => { f.floorOrder = i + 1 })
        activeFloorIdx.value = Math.min(fi, layoutFloors.value.length - 1)
    }

    // ── Canvas refs ──────────────────────────────────────────
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
        const floorRef = layoutFloors.value[fi]; if (!floorRef) return
        floorRef.layoutCanvasRef = el as HTMLCanvasElement
        nextTick(() => {
            drawFloor(fi)
            if (floorRef.canvasShapes.length > 0 && Object.keys(seatsByZone.value).length > 0)
                rebuildAndDraw()
        })
    }

    // ── Coordinates ──────────────────────────────────────────
    const toLogical = (fi: number, px: number, py: number) => {
        const floor  = layoutFloors.value[fi]
        const pan    = getFloorPan(floor.floorId)
        const scaleX = (floor.stageSize.width  / CANVAS_W) * pan.scale
        const scaleY = (floor.stageSize.height / CANVAS_H) * pan.scale
        return { x: (px - pan.x) / scaleX, y: (py - pan.y) / scaleY }
    }

    const toNorm = (px: number, total: number) => parseFloat((((px / total) * 2) - 1).toFixed(3))

    // ── Snap ─────────────────────────────────────────────────
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
        if (!floor) return { x: rawX, y: rawY }
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

    // ── Canvas renderer ──────────────────────────────────────
    const roundRect = (ctx: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, r: number) => {
        ctx.beginPath(); ctx.roundRect(x, y, w, h, r)
    }

    const drawResizeHandles = (ctx: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, scale: number) => {
        const r = HANDLE_R / scale
        for (const [hx, hy] of [[x, y], [x+w, y], [x+w, y+h], [x, y+h]] as [number, number][]) {
            ctx.fillStyle = '#6366f1'; ctx.beginPath(); ctx.arc(hx, hy, r, 0, Math.PI * 2); ctx.fill()
            ctx.strokeStyle = '#fff'; ctx.lineWidth = 1.5 / scale; ctx.beginPath(); ctx.arc(hx, hy, r, 0, Math.PI * 2); ctx.stroke()
        }
        const mr = r * 0.85
        for (const [hx, hy] of [[x+w/2, y], [x+w, y+h/2], [x+w/2, y+h], [x, y+h/2]] as [number, number][]) {
            ctx.fillStyle = '#a5b4fc'
            ctx.beginPath(); ctx.roundRect(hx - mr, hy - mr, mr * 2, mr * 2, 2); ctx.fill()
            ctx.strokeStyle = '#fff'; ctx.lineWidth = 1 / scale
            ctx.beginPath(); ctx.roundRect(hx - mr, hy - mr, mr * 2, mr * 2, 2); ctx.stroke()
        }
    }

    const drawFloor = (fi: number) => {
        const floor  = layoutFloors.value[fi]
        if (!floor) return
        const canvas = floor.layoutCanvasRef
        if (!canvas || floor.stageSize.width === 0) return
        const ctx = canvas.getContext('2d'); if (!ctx) return
        const W = floor.stageSize.width, H = floor.stageSize.height
        const pan    = getFloorPan(floor.floorId)
        const scaleX = (W / CANVAS_W) * pan.scale
        const scaleY = (H / CANVAS_H) * pan.scale
        ctx.clearRect(0, 0, W, H)
        ctx.save(); ctx.translate(pan.x, pan.y); ctx.scale(scaleX, scaleY)
        const sb = floor.stageBox
        ctx.fillStyle = '#f59e0b'; roundRect(ctx, sb.x, sb.y, sb.width, sb.height, 6); ctx.fill()
        if (floor.editingStage) {
            ctx.strokeStyle = '#ffffff'; ctx.lineWidth = 2 / scaleX
            roundRect(ctx, sb.x, sb.y, sb.width, sb.height, 6); ctx.stroke()
            drawResizeHandles(ctx, sb.x, sb.y, sb.width, sb.height, scaleX)
        }
        ctx.save(); ctx.setTransform(1, 0, 0, 1, 0, 0)
        ctx.font = '500 12px sans-serif'; ctx.textAlign = 'center'; ctx.textBaseline = 'middle'; ctx.fillStyle = '#1a1a1a'
        ctx.fillText('Stage / Screen', (sb.x + sb.width / 2) * scaleX + pan.x, (sb.y + sb.height / 2) * scaleY + pan.y)
        ctx.restore()
        for (const shape of floor.canvasShapes) {
            const isSelected = floor.selectedShapeId === shape.id
            const rot = shape.rotation ?? 0
            const cx = shape.x + shape.width / 2, cy = shape.y + shape.height / 2
            ctx.globalAlpha = shape.isStage ? 0.92 : 1
            ctx.save(); ctx.translate(cx, cy); ctx.rotate(rot); ctx.translate(-cx, -cy)
            const fillColor   = shape.isStage ? '#f59e0b' : shape.color + '44'
            const strokeColor = shape.isStage ? (isSelected ? '#fff' : '#d97706') : (isSelected ? '#ffffff' : shape.color)
            if (shape.type === 'rect') {
                ctx.fillStyle = fillColor; roundRect(ctx, shape.x, shape.y, shape.width, shape.height, 6); ctx.fill()
                ctx.strokeStyle = strokeColor; ctx.lineWidth = (isSelected ? 2.5 : 1.5) / scaleX
                roundRect(ctx, shape.x, shape.y, shape.width, shape.height, 6); ctx.stroke()
            } else {
                ctx.fillStyle = fillColor; ctx.beginPath(); ctx.ellipse(cx, cy, shape.width/2, shape.height/2, 0, 0, Math.PI*2); ctx.fill()
                ctx.strokeStyle = strokeColor; ctx.lineWidth = (isSelected ? 2.5 : 1.5) / scaleX
                ctx.beginPath(); ctx.ellipse(cx, cy, shape.width/2, shape.height/2, 0, 0, Math.PI*2); ctx.stroke()
            }
            ctx.globalAlpha = 1
            ctx.save(); ctx.setTransform(1, 0, 0, 1, 0, 0)
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
                ctx.fillStyle = '#6366f1'; ctx.beginPath(); ctx.arc(cx, rHandleY, 6/scaleX, 0, Math.PI*2); ctx.fill()
                ctx.strokeStyle = '#fff'; ctx.lineWidth = 1.5 / scaleX
                ctx.beginPath(); ctx.arc(cx, rHandleY, 6/scaleX, 0, Math.PI*2); ctx.stroke()
            }
            ctx.restore()
        }
        ctx.strokeStyle = '#6366f1'; ctx.lineWidth = 1 / scaleX
        ctx.setLineDash([4/scaleX, 4/scaleX]); ctx.globalAlpha = 0.8
        if (drag.snapGuideX !== null) { ctx.beginPath(); ctx.moveTo(drag.snapGuideX, 0); ctx.lineTo(drag.snapGuideX, CANVAS_H); ctx.stroke() }
        if (drag.snapGuideY !== null) { ctx.beginPath(); ctx.moveTo(0, drag.snapGuideY); ctx.lineTo(CANVAS_W, drag.snapGuideY); ctx.stroke() }
        ctx.setLineDash([]); ctx.globalAlpha = 1
        ctx.restore()
        if (fi === 0) layoutFloors.value.forEach((_, ui) => { if (ui > 0) drawGhostCanvas(ui) })
        else drawGhostCanvas(fi)
    }

    const drawGhostCanvas = (fi: number) => {
        if (fi === 0) return
        const floor  = layoutFloors.value[fi]
        if (!floor) return
        const canvas = floor.ghostCanvasRef
        if (!canvas || floor.stageSize.width === 0) return
        const src = layoutFloors.value[0]
        if (!src || src.stageSize.width === 0) return
        const ctx = canvas.getContext('2d'); if (!ctx) return
        const W = floor.stageSize.width, H = floor.stageSize.height
        const pan    = getFloorPan(floor.floorId)
        const scaleX = (W / CANVAS_W) * pan.scale, scaleY = (H / CANVAS_H) * pan.scale
        ctx.clearRect(0, 0, W, H)
        ctx.save(); ctx.translate(pan.x, pan.y); ctx.scale(scaleX, scaleY); ctx.globalAlpha = 0.22
        const sb = src.stageBox
        ctx.fillStyle = '#f59e0b'; roundRect(ctx, sb.x, sb.y, sb.width, sb.height, 6); ctx.fill()
        for (const s of src.canvasShapes) {
            const rot = s.rotation ?? 0, cx = s.x + s.width/2, cy = s.y + s.height/2
            ctx.save(); ctx.translate(cx, cy); ctx.rotate(rot); ctx.translate(-cx, -cy)
            ctx.fillStyle = s.isStage ? '#f59e0b' : s.color + '66'
            if (s.type === 'ellipse') { ctx.beginPath(); ctx.ellipse(cx, cy, s.width/2, s.height/2, 0, 0, Math.PI*2); ctx.fill() }
            else { roundRect(ctx, s.x, s.y, s.width, s.height, 6); ctx.fill() }
            ctx.restore()
        }
        ctx.restore()
    }

    // ── Hit test ─────────────────────────────────────────────
    type HitTarget = { kind: 'shape'; id: string } | { kind: 'stage' } | { kind: 'resize'; id: string; handle: string } | { kind: 'stageResize'; handle: string } | { kind: 'rotate'; id: string } | null

    const checkHandles = (lx: number, ly: number, x: number, y: number, w: number, h: number, r: number): string | null => {
        for (const [name, hx, hy] of [['nw',x,y],['ne',x+w,y],['se',x+w,y+h],['sw',x,y+h],['n',x+w/2,y],['e',x+w,y+h/2],['s',x+w/2,y+h],['w',x,y+h/2]] as [string,number,number][])
            if (Math.hypot(lx-hx, ly-hy) <= r*1.5) return name
        return null
    }

    const checkHandlesRotated = (lx: number, ly: number, x: number, y: number, w: number, h: number, rot: number, r: number): string | null => {
        const cx = x+w/2, cy = y+h/2
        for (const [name, hx, hy] of [['nw',x,y],['ne',x+w,y],['se',x+w,y+h],['sw',x,y+h],['n',x+w/2,y],['e',x+w,y+h/2],['s',x+w/2,y+h],['w',x,y+h/2]] as [string,number,number][]) {
            const dx = hx-cx, dy = hy-cy
            const rx = cx + dx*Math.cos(rot) - dy*Math.sin(rot), ry = cy + dx*Math.sin(rot) + dy*Math.cos(rot)
            if (Math.hypot(lx-rx, ly-ry) <= r*1.5) return name
        }
        return null
    }

    const hitTest = (fi: number, lx: number, ly: number): HitTarget => {
        const floor  = layoutFloors.value[fi]
        if (!floor) return null
        const pan    = getFloorPan(floor.floorId)
        const scaleX = (floor.stageSize.width / CANVAS_W) * pan.scale
        const HR     = HANDLE_R / scaleX
        const selShape = floor.canvasShapes.find(s => s.id === floor.selectedShapeId)
        if (selShape) {
            const rot = selShape.rotation ?? 0, cx = selShape.x + selShape.width/2, cy = selShape.y + selShape.height/2
            const rHandleLocalY = selShape.y - 28/scaleX
            const rHx = cx + Math.sin(rot)*(rHandleLocalY-cy)*-1, rHy = cy + Math.cos(rot)*(rHandleLocalY-cy)
            if (Math.hypot(lx-rHx, ly-rHy) <= 8/scaleX) return { kind: 'rotate', id: selShape.id }
            const handle = checkHandlesRotated(lx, ly, selShape.x, selShape.y, selShape.width, selShape.height, rot, HR)
            if (handle) return { kind: 'resize', id: selShape.id, handle }
        }
        if (floor.editingStage) {
            const handle = checkHandles(lx, ly, floor.stageBox.x, floor.stageBox.y, floor.stageBox.width, floor.stageBox.height, HR)
            if (handle) return { kind: 'stageResize', handle }
        }
        for (let i = floor.canvasShapes.length - 1; i >= 0; i--) {
            const s = floor.canvasShapes[i]
            const rot = s.rotation ?? 0, cx = s.x + s.width/2, cy = s.y + s.height/2
            const dx = lx-cx, dy = ly-cy
            const lxL = dx*Math.cos(-rot) - dy*Math.sin(-rot) + cx
            const lyL = dx*Math.sin(-rot) + dy*Math.cos(-rot) + cy
            if (s.type === 'ellipse') { if (((lxL-cx)/(s.width/2))**2 + ((lyL-cy)/(s.height/2))**2 <= 1) return { kind: 'shape', id: s.id } }
            else { if (lxL >= s.x && lxL <= s.x+s.width && lyL >= s.y && lyL <= s.y+s.height) return { kind: 'shape', id: s.id } }
        }
        const sb = floor.stageBox
        if (lx >= sb.x && lx <= sb.x+sb.width && ly >= sb.y && ly <= sb.y+sb.height) return { kind: 'stage' }
        return null
    }

    // ── Seat transform ───────────────────────────────────────
    const ensureSeatTransform = (fi: number, seatId: string) => {
        const floor = layoutFloors.value[fi]
        if (!floor) return
        if (!floor.seatTransforms[seatId])
            floor.seatTransforms[seatId] = { seatId, dx: 0, dy: 0, rotation: 0, scale: 1 }
    }

    // ── Mouse handlers ───────────────────────────────────────
    const onCanvasMouseDown = (e: MouseEvent, fi: number) => {
        if (e.button !== 0) return
        const floor  = layoutFloors.value[fi]
        if (!floor) return
        const canvas = floor.layoutCanvasRef; if (!canvas) return
        const rect   = canvas.getBoundingClientRect()
        const px = e.clientX - rect.left, py = e.clientY - rect.top
        const { x: lx, y: ly } = toLogical(fi, px, py)
        if (floor.activeTool === 'move') {
            drag.active = true; drag.fi = fi; drag.targetId = '__pan__'
            drag.startX = px; drag.startY = py
            drag.panOffsetX = getFloorPan(floor.floorId).x; drag.panOffsetY = getFloorPan(floor.floorId).y
            canvas.style.cursor = 'grabbing'; return
        }
        const hits    = seatHitMap.value[fi] ?? []
        const seatHit = hits.find(s => Math.hypot(s.cx - px, s.cy - py) <= s.r + 4)
        if (seatHit) {
            const tf  = floor.seatTransforms[seatHit.seatId] ?? { seatId: seatHit.seatId, dx: 0, dy: 0, rotation: 0, scale: 1 }
            const rot = tf.rotation, r = seatHit.r
            const rHx = seatHit.cx + Math.sin(rot)*-(r+13), rHy = seatHit.cy - Math.cos(rot)*(r+13)
            if (floor.selectedSeatId === seatHit.seatId && Math.hypot(px-rHx, py-rHy) <= 8) {
                drag.active = true; drag.fi = fi; drag.seatMode = 'rotate'; drag.seatId = seatHit.seatId
                drag.startX = px; drag.startY = py; drag.seatCx = seatHit.cx; drag.seatCy = seatHit.cy
                drag.seatOrigRot = tf.rotation; return
            }
            const bRx = seatHit.cx + Math.cos(rot)*r + Math.sin(rot)*r*-1
            const bRy = seatHit.cy + Math.sin(rot)*r + Math.cos(rot)*r
            if (floor.selectedSeatId === seatHit.seatId && Math.hypot(px-bRx, py-bRy) <= 7) {
                drag.active = true; drag.fi = fi; drag.seatMode = 'resize'; drag.seatId = seatHit.seatId
                drag.startX = px; drag.startY = py; drag.seatCx = seatHit.cx; drag.seatCy = seatHit.cy
                drag.seatOrigScale = tf.scale; return
            }
            floor.selectedSeatId = seatHit.seatId; floor.selectedShapeId = null; floor.editingStage = false
            ensureSeatTransform(fi, seatHit.seatId)
            drag.active = true; drag.fi = fi; drag.seatMode = 'move'; drag.seatId = seatHit.seatId
            drag.startX = px; drag.startY = py
            drag.seatOrigDx = floor.seatTransforms[seatHit.seatId]?.dx ?? 0
            drag.seatOrigDy = floor.seatTransforms[seatHit.seatId]?.dy ?? 0
            drawFloor(fi); drawSeatsOnCanvas(fi); return
        }
        if (floor.selectedSeatId !== null) { floor.selectedSeatId = null; drawSeatsOnCanvas(fi) }
        const hit = hitTest(fi, lx, ly)
        if (!hit) { if (floor.selectedShapeId !== null || floor.editingStage) { floor.selectedShapeId = null; floor.editingStage = false; drawFloor(fi) }; return }
        if (hit.kind === 'rotate') {
            const shape = floor.canvasShapes.find(s => s.id === hit.id)!
            drag.active = true; drag.fi = fi; drag.targetId = hit.id; drag.rotatingShape = true
            drag.startX = lx; drag.startY = ly
            drag.rotCx = shape.x + shape.width/2; drag.rotCy = shape.y + shape.height/2
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
        const floor  = layoutFloors.value[fi]
        if (!floor) return
        const canvas = floor.layoutCanvasRef; if (!canvas) return
        const rect   = canvas.getBoundingClientRect()
        const px = e.clientX - rect.left, py = e.clientY - rect.top
        if (drag.targetId === '__pan__') {
            const pan = getFloorPan(floor.floorId)
            pan.x = drag.panOffsetX + (px - drag.startX); pan.y = drag.panOffsetY + (py - drag.startY)
            drawFloor(fi); if (fi > 0) drawGhostCanvas(fi); rebuildAndDraw(); return
        }
        if (drag.seatMode === 'move') {
            const tf = floor.seatTransforms[drag.seatId]
            if (tf) { tf.dx = drag.seatOrigDx + (px - drag.startX); tf.dy = drag.seatOrigDy + (py - drag.startY); rebuildAndDraw(); return }
        }
        if (drag.seatMode === 'rotate') {
            const tf = floor.seatTransforms[drag.seatId]
            if (tf) { tf.rotation = drag.seatOrigRot + (Math.atan2(py-drag.seatCy, px-drag.seatCx) - Math.atan2(drag.startY-drag.seatCy, drag.startX-drag.seatCx)); drawSeatsOnCanvas(fi); return }
        }
        if (drag.seatMode === 'resize') {
            const dist = Math.hypot(px-drag.seatCx, py-drag.seatCy)
            const origDist = Math.hypot(drag.startX-drag.seatCx, drag.startY-drag.seatCy)
            const tf = floor.seatTransforms[drag.seatId]
            if (tf && origDist > 0) { tf.scale = Math.max(0.3, Math.min(4, drag.seatOrigScale*(dist/origDist))); rebuildAndDraw(); return }
        }
        const { x: lx, y: ly } = toLogical(fi, px, py)
        const dx = lx - drag.startX, dy = ly - drag.startY
        if (drag.rotatingShape) {
            const shape = floor.canvasShapes.find(s => s.id === drag.targetId)
            if (shape) { shape.rotation = drag.rotOrigAngle + (Math.atan2(ly-drag.rotCy, lx-drag.rotCx) - Math.atan2(drag.startY-drag.rotCy, drag.startX-drag.rotCx)); drawFloor(fi); if (shape.zoneId) rebuildAndDraw() }; return
        }
        if (drag.resizeHandle) {
            const isStage = drag.targetId === '__stageResize__'
            const resizeTarget = isStage ? floor.stageBox : floor.canvasShapes.find(s => s.id === drag.targetId)!
            applyResize(resizeTarget, drag.resizeHandle, drag.origX, drag.origY, drag.resizeOrigW, drag.resizeOrigH, dx, dy)
            drawFloor(fi); if (!isStage && (resizeTarget as CanvasShape).zoneId) rebuildAndDraw(); return
        }
        if (drag.targetId === '__stage__') {
            const { x, y } = snapPosition(fi, '__stage__', drag.origX+dx, drag.origY+dy, floor.stageBox.width, floor.stageBox.height)
            floor.stageBox.x = x; floor.stageBox.y = y; drawFloor(fi); return
        }
        const shape = floor.canvasShapes.find(s => s.id === drag.targetId)
        if (shape) {
            const { x, y } = snapPosition(fi, shape.id, drag.origX+dx, drag.origY+dy, shape.width, shape.height)
            shape.x = x; shape.y = y; drawFloor(fi); if (shape.zoneId) rebuildAndDraw()
        }
    }

    const onCanvasMouseUp = (_e: MouseEvent, fi: number) => {
        if (!drag.active || drag.fi !== fi) return
        drag.active = false; drag.snapGuideX = null; drag.snapGuideY = null
        drag.resizeHandle = null; drag.rotatingShape = false; drag.seatMode = ''
        const floor = layoutFloors.value[fi]
        if (!floor) return
        if (floor.layoutCanvasRef) floor.layoutCanvasRef.style.cursor = 'crosshair'
        drawFloor(fi); rebuildAndDraw()
    }

    const onCanvasWheel = (e: WheelEvent, fi: number) => {
        const floor = layoutFloors.value[fi]
        if (!floor) return
        const pan   = getFloorPan(floor.floorId)
        pan.scale   = Math.min(4, Math.max(0.25, pan.scale + (e.deltaY > 0 ? -0.1 : 0.1)))
        floor.scale = pan.scale
        drawFloor(fi); if (fi > 0) drawGhostCanvas(fi); rebuildAndDraw()
    }

    const applyResize = (target: { x: number; y: number; width: number; height: number }, handle: string, ox: number, oy: number, ow: number, oh: number, dx: number, dy: number) => {
        const MIN_W = 40, MIN_H = 30
        let newX = ox, newY = oy, newW = ow, newH = oh
        switch (handle) {
            case 'se': newW = Math.max(MIN_W, ow+dx); newH = Math.max(MIN_H, oh+dy); break
            case 'sw': { const nw = Math.max(MIN_W, ow-dx); newX = ox+(ow-nw); newW = nw; newH = Math.max(MIN_H, oh+dy); break }
            case 'ne': { newW = Math.max(MIN_W, ow+dx); const nh = Math.max(MIN_H, oh-dy); newY = oy+(oh-nh); newH = nh; break }
            case 'nw': { const nw = Math.max(MIN_W, ow-dx); newX = ox+(ow-nw); newW = nw; const nh = Math.max(MIN_H, oh-dy); newY = oy+(oh-nh); newH = nh; break }
            case 'n':  { const nh = Math.max(MIN_H, oh-dy); newY = oy+(oh-nh); newH = nh; break }
            case 's':  newH = Math.max(MIN_H, oh+dy); break
            case 'e':  newW = Math.max(MIN_W, ow+dx); break
            case 'w':  { const nw = Math.max(MIN_W, ow-dx); newX = ox+(ow-nw); newW = nw; break }
        }
        target.x = newX; target.y = newY; target.width = Math.max(MIN_W, newW); target.height = Math.max(MIN_H, newH)
    }

    // ── Shape actions ────────────────────────────────────────
    const addCustomShape = (fi: number, type: 'rect' | 'ellipse') => {
        const floor = layoutFloors.value[fi]
        if (!floor) return
        floor.canvasShapes.push({ id: crypto.randomUUID(), type, x: CANVAS_W/2-100, y: CANVAS_H/2-60, width: 200, height: 120, label: `Zone ${floor.canvasShapes.length+1}`, color: floor.selectedColor, accessible: true })
        floor.selectedShapeId = floor.canvasShapes[floor.canvasShapes.length-1]?.id ?? null; drawFloor(fi)
    }

    const addStageShape = (fi: number, type: 'rect' | 'ellipse') => {
        const floor = layoutFloors.value[fi]
        if (!floor) return
        const stageCount = floor.canvasShapes.filter(s => s.isStage).length
        floor.canvasShapes.push({ id: crypto.randomUUID(), type, x: CANVAS_W/2-100, y: 20+stageCount*70, width: 200, height: 50, label: stageCount === 0 ? 'Stage' : `Stage ${stageCount+1}`, color: '#f59e0b', accessible: false, isStage: true })
        floor.selectedShapeId = floor.canvasShapes[floor.canvasShapes.length-1]?.id ?? null; drawFloor(fi)
    }

    const autoResizeShapeForZone = (fi: number, shape: CanvasShape, zone: Zone) => {
        if (zone.isStanding || !zone.seatCount) return
        const floor   = layoutFloors.value[fi]
        if (!floor) return
        const seatR   = (shape.seatSize ?? floor.globalSeatSize) / 2
        const gap     = seatR * 2 + 2
        const PADDING = seatR * 1.5
        const cols    = (zone as any).gridCols ?? Math.ceil(Math.sqrt(zone.seatCount))
        const rows    = Math.ceil(zone.seatCount / cols)
        shape.width   = Math.max(160, cols * gap + PADDING * 2)
        shape.height  = Math.max(80, rows * gap + PADDING * 2)
    }

    const addZoneShapeToFloor = (fi: number, zone: Zone) => {
        const floor   = layoutFloors.value[fi]
        if (!floor) return
        const already = floor.canvasShapes.find(s => s.zoneId === zone.id || s.label === zone.name)
        if (already) { already.zoneId = zone.id; floor.selectedShapeId = already.id; autoResizeShapeForZone(fi, already, zone); rebuildAndDraw(); return }
        const nonStageShapes = floor.canvasShapes.filter(s => !s.isStage)
        const col = nonStageShapes.length % 3
        const row = Math.floor(nonStageShapes.length / 3)
        const newShape: CanvasShape = { id: crypto.randomUUID(), type: 'rect', x: 40+col*280, y: Math.min(80+row*160, CANVAS_H-140), width: 240, height: 120, label: zone.name, color: '#6366f1', accessible: !zone.isStanding, zoneId: zone.id }
        autoResizeShapeForZone(fi, newShape, zone)
        floor.canvasShapes.push(newShape); floor.selectedShapeId = newShape.id; rebuildAndDraw()
    }

    const clearFloor = (fi: number) => {
        const floor = layoutFloors.value[fi]
        if (!floor) return
        floor.canvasShapes = []; floor.selectedShapeId = null; floor.editingStage = false
        drag.snapGuideX = null; drag.snapGuideY = null; drawFloor(fi)
    }

    const deleteSelectedShape = (fi: number) => {
        const floor = layoutFloors.value[fi]
        if (!floor) return
        floor.canvasShapes    = floor.canvasShapes.filter(s => s.id !== floor.selectedShapeId)
        floor.selectedShapeId = null; drawFloor(fi)
    }

    const isZoneLinked = (zone: Zone): boolean => {
        if (!zone.id) return false
        return layoutFloors.value.some(floor => floor.canvasShapes.some(s => s.zoneId === zone.id))
    }

    // ── Seat rendering ───────────────────────────────────────
    const groupSeatsByRow = (seats: SeatInfo[]): SeatInfo[][] => {
        const map = new Map<string, SeatInfo[]>()
        for (const seat of seats) {
            const m   = (seat.seatCode ?? '').match(/^([A-Za-z]+)/)
            const row = m?.[1]?.toUpperCase() ?? '__'
            if (!map.has(row)) map.set(row, [])
            map.get(row)!.push(seat)
        }
        for (const arr of map.values())
            arr.sort((a, b) => parseInt((a.seatCode ?? '').replace(/\D/g, '') || '0') - parseInt((b.seatCode ?? '').replace(/\D/g, '') || '0'))
        return [...map.keys()].sort().map(k => map.get(k)!)
    }

    const computeSeatPixelPositions = (fi: number, shape: CanvasShape, seats: SeatInfo[], floor: LayoutFloor) => {
        const pan    = getFloorPan(floor.floorId)
        const scaleX = (floor.stageSize.width  / CANVAS_W) * pan.scale
        const scaleY = (floor.stageSize.height / CANVAS_H) * pan.scale
        const seatPx = (shape.seatSize ?? floor.globalSeatSize) / 2
        const baseR  = Math.max(2, seatPx * scaleX)
        const gap    = baseR * 2 + 3
        const PADDING = baseR * 1.5
        const shapeX = pan.x + shape.x * scaleX
        const shapeY = pan.y + shape.y * scaleY
        const shapeW = shape.width  * scaleX
        const shapeH = shape.height * scaleY
        const seatRows = groupSeatsByRow(seats)
        const gridH    = seatRows.length * gap - gap + baseR * 2
        const originY  = shapeY + PADDING + Math.max(0, (shapeH - PADDING*2 - gridH) / 2)
        const result: Array<{ seat: SeatInfo; cx: number; cy: number; r: number; rot: number }> = []
        seatRows.forEach((rowSeats, rowIdx) => {
            const rowGridW   = rowSeats.length * gap - gap + baseR * 2
            const rowOriginX = shapeX + PADDING + Math.max(0, (shapeW - PADDING*2 - rowGridW) / 2)
            const baseCy     = originY + rowIdx * gap
            rowSeats.forEach((seat, colIdx) => {
                const tf  = floor.seatTransforms[seat.id]
                const cx  = rowOriginX + colIdx * gap + (tf?.dx ?? 0)
                const cy  = baseCy + (tf?.dy ?? 0)
                const rot = tf?.rotation ?? 0
                const r   = baseR * (tf?.scale ?? 1)
                result.push({ seat, cx, cy, r, rot })
            })
        })
        return { result, baseR, scaleX }
    }

    const drawSeatsOnCanvas = (fi: number) => {
        const floor  = layoutFloors.value[fi]
        if (!floor) return
        const canvas = floor.seatCanvasRef; if (!canvas) return
        const ctx    = canvas.getContext('2d'); if (!ctx) return
        ctx.clearRect(0, 0, canvas.width, canvas.height)
        for (const shape of floor.canvasShapes) {
            if (!shape.zoneId) continue
            const seats = seatsByZone.value[shape.zoneId]
            if (!seats || seats.length === 0) continue
            const { result } = computeSeatPixelPositions(fi, shape, seats, floor)
            for (const { seat, cx, cy, r, rot } of result) {
                const isSelectedSeat = floor.selectedSeatId === seat.id
                ctx.save(); ctx.translate(cx, cy); ctx.rotate(rot)
                ctx.fillStyle   = seat.priceOverride !== null ? '#f59e0b' : (shape.color + 'dd')
                ctx.globalAlpha = 0.9
                ctx.strokeStyle = isSelectedSeat ? '#ffffff' : 'transparent'
                ctx.lineWidth   = isSelectedSeat ? 2 : 0
                ctx.beginPath(); ctx.arc(0, 0, r, 0, Math.PI*2); ctx.fill()
                if (isSelectedSeat) ctx.stroke()
                if (r >= 8) {
                    ctx.globalAlpha = 0.92; ctx.fillStyle = '#ffffff'
                    ctx.font = `500 ${Math.max(7, Math.min(11, r*0.72))}px sans-serif`
                    ctx.textAlign = 'center'; ctx.textBaseline = 'middle'
                    ctx.fillText(seat.seatCode ?? '', 0, 0)
                }
                if (isSelectedSeat) {
                    ctx.globalAlpha = 1
                    ctx.strokeStyle = '#ffffff'; ctx.lineWidth = 1
                    ctx.beginPath(); ctx.moveTo(0, -r); ctx.lineTo(0, -r-10); ctx.stroke()
                    ctx.fillStyle = '#6366f1'; ctx.beginPath(); ctx.arc(0, -r-13, 5, 0, Math.PI*2); ctx.fill()
                    ctx.strokeStyle = '#fff'; ctx.lineWidth = 1.5; ctx.beginPath(); ctx.arc(0, -r-13, 5, 0, Math.PI*2); ctx.stroke()
                    ctx.fillStyle = '#6366f1'; ctx.beginPath(); ctx.arc(r, r, 4, 0, Math.PI*2); ctx.fill()
                    ctx.strokeStyle = '#fff'; ctx.lineWidth = 1.5; ctx.beginPath(); ctx.arc(r, r, 4, 0, Math.PI*2); ctx.stroke()
                }
                ctx.restore()
            }
        }
    }

    const rebuildAndDraw = () => {
        const newMap: typeof seatHitMap.value = []
        layoutFloors.value.forEach((floor, fi) => {
            const entries: typeof seatHitMap.value[0] = []
            for (const shape of floor.canvasShapes) {
                if (!shape.zoneId) continue
                const seats = seatsByZone.value[shape.zoneId]
                if (!seats || seats.length === 0) continue
                const { result } = computeSeatPixelPositions(fi, shape, seats, floor)
                for (const { seat, cx, cy, r } of result) entries.push({ seatId: seat.id, seat, cx, cy, r })
            }
            newMap[fi] = entries
        })
        seatHitMap.value = newMap
        nextTick(() => layoutFloors.value.forEach((_, fi) => drawSeatsOnCanvas(fi)))
    }

    // ── Layout JSON ──────────────────────────────────────────
    const buildFloorJson = (floor: LayoutFloor) => {
        const w = CANVAS_W, h = CANVAS_H, sb = floor.stageBox
        const zoneShapes  = floor.canvasShapes.filter(s => !s.isStage)
        const stageShapes = floor.canvasShapes.filter(s => s.isStage)
        return {
            floor_name: floor.floorName, floor_order: floor.floorOrder, global_seat_size: floor.globalSeatSize,
            stage: { x1: toNorm(sb.x, w), y1: toNorm(sb.y, h), x2: toNorm(sb.x+sb.width, w), y2: toNorm(sb.y+sb.height, h) },
            zones: zoneShapes.map(s => ({
                zone_name: s.label, zone_type: s.accessible ? 'standing' : 'non_public',
                accessible: s.accessible, color: s.color, zone_id: s.zoneId ?? null,
                rotation: s.rotation ?? 0, shape_type: s.type, seat_size: s.seatSize ?? null,
                corner1: { x: toNorm(s.x, w),         y: toNorm(s.y, h) },
                corner2: { x: toNorm(s.x+s.width, w), y: toNorm(s.y, h) },
                corner3: { x: toNorm(s.x+s.width, w), y: toNorm(s.y+s.height, h) },
                corner4: { x: toNorm(s.x, w),         y: toNorm(s.y+s.height, h) },
                seat_transforms: s.zoneId
                    ? Object.fromEntries(Object.entries(floor.seatTransforms)
                        .filter(([seatId]) => seatsByZone.value[s.zoneId!]?.some(seat => seat.id === seatId))
                        .map(([seatId, tf]) => [seatId, { dx: tf.dx, dy: tf.dy, rotation: tf.rotation, scale: tf.scale }]))
                    : {},
            })),
            stage_shapes: stageShapes.map(s => ({
                label: s.label, type: s.type, rotation: s.rotation ?? 0,
                corner1: { x: toNorm(s.x, w),         y: toNorm(s.y, h) },
                corner2: { x: toNorm(s.x+s.width, w), y: toNorm(s.y, h) },
                corner4: { x: toNorm(s.x, w),         y: toNorm(s.y+s.height, h) },
            })),
        }
    }

    const buildFullLayoutJson = () => JSON.stringify({ floors: layoutFloors.value.map(floor => buildFloorJson(floor)) })

    const floorJsonPreviews = computed(() =>
        layoutFloors.value.map(floor => { try { return JSON.stringify(buildFloorJson(floor), null, 2) } catch { return '{}' } })
    )

    const copyFloorJson = (fi: number) => { const preview = floorJsonPreviews.value[fi]; if (preview) navigator.clipboard?.writeText(preview) }

    // ── Load seats ───────────────────────────────────────────
    const loadSeatsForStep4 = async (sessionId: string, zones: Zone[]) => {
        if (!sessionId || zones.length === 0) return
        loadingSeats.value = true
        try {
            const seatedZones = zones.filter(z => !z.isStanding && z.id)
            const results     = await Promise.all(seatedZones.map(async (zone) => {
                const seats = await $fetch<SeatInfo[]>(`${apiUrl}/events/sessions/${sessionId}/zones/${zone.id}/seats`, { credentials: 'include' })
                return { zoneId: zone.id!, seats }
            }))
            const next: SeatsByZone = {}
            for (const { zoneId, seats } of results) next[zoneId] = seats
            seatsByZone.value = next
        } catch { /* silent */ }
        finally { loadingSeats.value = false }
    }

    // ── Apply / reload layout ────────────────────────────────
    const applyLayout = async (savedEventId: string, form: { venueId: string; [key: string]: unknown }, selectedVenueLayout: string | null, zones: Zone[]) => {
        if (layoutMode.value === 'venue' && !form.venueId) throw new Error('Venue is required for venue layout')

        if (layoutMode.value === 'venue' && form.venueId && selectedVenueLayout) {
            const zoneLinks = Object.entries(venueZoneLinks.value)
                .filter(([, zoneId]) => zoneId)
                .map(([venueZoneName, zoneId]) => ({ venueZoneName, zoneId }))
            await $fetch(`${apiUrl}/events/${savedEventId}/layout`, {
                method: 'PUT', body: { useVenueLayout: true, venueId: form.venueId, venueZoneLinks: zoneLinks }, credentials: 'include'
            })
        } else if (layoutMode.value === 'venue') {
            throw new Error('No venue layout available')
        } else {
            if (layoutFloors.value.length === 0) throw new Error('No floors defined')
            await $fetch(`${apiUrl}/events/${savedEventId}/layout`, {
                method: 'PUT', body: { useVenueLayout: false, customLayoutJson: buildFullLayoutJson() }, credentials: 'include'
            })
        }
        layoutSaved.value = true
    }

    const reloadSavedLayout = async (savedEventId: string) => {
        const event = await $fetch<Record<string, unknown>>(`${apiUrl}/events/${savedEventId}`, { credentials: 'include' })
        const rawLayout = event.layout ?? event.eventLayout ?? null
        if (!rawLayout) throw new Error('no_saved_layout')
        const parsed = JSON.parse(rawLayout)
        if (!parsed.floors || !Array.isArray(parsed.floors)) throw new Error('no_saved_layout')
        const toPixel = (n: number, total: number) => ((n + 1) / 2) * total
        layoutFloors.value = (parsed.floors as Record<string, unknown>[]).map((fl) => {
            const floor = makeFloor(fl.floor_order ?? 1)
            floor.floorName      = fl.floor_name ?? floor.floorName
            floor.globalSeatSize = fl.global_seat_size ?? DEFAULT_SEAT_SIZE
            floor.canvasShapes   = (fl.zones ?? []).map((z: any) => {
                const x = toPixel(z.corner1?.x ?? -0.3, CANVAS_W), y = toPixel(z.corner1?.y ?? -0.5, CANVAS_H)
                return { id: crypto.randomUUID(), type: (z.shape_type ?? 'rect') as 'rect'|'ellipse', x, y,
                    width: Math.max(toPixel(z.corner2?.x ?? 0.3, CANVAS_W) - x, 80),
                    height: Math.max(toPixel(z.corner4?.y ?? 0.5, CANVAS_H) - y, 60),
                    label: z.zone_name ?? 'Zone', color: z.color ?? '#6366f1',
                    accessible: z.accessible !== false, zoneId: z.zone_id ?? undefined,
                    rotation: z.rotation ?? 0, seatSize: z.seat_size ?? undefined }
            })
            for (const z of (fl.zones ?? [])) {
                if (z.seat_transforms) {
                    for (const [seatId, tf] of Object.entries(z.seat_transforms as Record<string, any>))
                        floor.seatTransforms[seatId] = { seatId, dx: tf.dx??0, dy: tf.dy??0, rotation: tf.rotation??0, scale: tf.scale??1 }
                }
            }
            const stageShapes = (fl.stage_shapes ?? []).map((s: any) => {
                const x = toPixel(s.corner1?.x ?? -0.1, CANVAS_W), y = toPixel(s.corner1?.y ?? -0.5, CANVAS_H)
                return { id: crypto.randomUUID(), type: (s.type ?? 'rect') as 'rect'|'ellipse', x, y,
                    width: Math.max(toPixel(s.corner2?.x ?? 0.1, CANVAS_W) - x, 60),
                    height: Math.max(toPixel(s.corner4?.y ?? -0.3, CANVAS_H) - y, 30),
                    label: s.label ?? 'Stage', color: '#f59e0b', accessible: false, isStage: true, rotation: s.rotation ?? 0 }
            })
            floor.canvasShapes = [...floor.canvasShapes, ...stageShapes]
            if (fl.stage) {
                floor.stageBox = { x: toPixel(fl.stage.x1, CANVAS_W), y: toPixel(fl.stage.y1, CANVAS_H),
                    width: Math.round(toPixel(fl.stage.x2, CANVAS_W) - toPixel(fl.stage.x1, CANVAS_W)),
                    height: Math.round(toPixel(fl.stage.y2, CANVAS_H) - toPixel(fl.stage.y1, CANVAS_H)) }
            }
            return floor
        })
        layoutMode.value  = 'custom'
        layoutSaved.value = true
        await nextTick()
        setTimeout(() => {
            layoutFloors.value.forEach((floor, fi) => {
                const el = canvasContainerRefs[fi]
                if (el && floor.stageSize.width === 0) floor.stageSize = { width: el.clientWidth, height: el.clientHeight || CANVAS_H }
            })
            layoutFloors.value.forEach((_, fi) => drawFloor(fi))
            rebuildAndDraw()
        }, 50)
    }

    // ── Inline seat price ────────────────────────────────────
    const selectedSeatInfo = computed<SeatInfo | null>(() => {
        const floor = activeFloor.value
        if (!floor?.selectedSeatId) return null
        for (const zoneId in seatsByZone.value) {
            const seat = seatsByZone.value[zoneId].find(s => s.id === floor.selectedSeatId)
            if (seat) return seat
        }
        return null
    })

    const saveSeatPriceInline = async (val: string, savedSessionId: string) => {
        if (!selectedSeatInfo.value) return
        const seat          = selectedSeatInfo.value
        const priceOverride = val === '' ? null : Number(val)
        await $fetch(`${apiUrl}/events/sessions/${savedSessionId}/zones/${seat.zoneId}/seats/${seat.id}/price`,
            { method: 'PATCH', body: { priceOverride }, credentials: 'include' })
        seat.priceOverride = priceOverride
        nextTick(() => layoutFloors.value.forEach((_, fi) => drawSeatsOnCanvas(fi)))
    }

    // ── Watchers ─────────────────────────────────────────────
    watch(seatsByZone, rebuildAndDraw, { deep: false })
    watch(
        () => layoutFloors.value.map(f => f.canvasShapes.map(s => s.zoneId).join(',')).join('|'),
        rebuildAndDraw, { flush: 'post' }
    )
    watch(
        () => layoutFloors.value.map(f => f.selectedSeatId).join(','),
        () => nextTick(() => layoutFloors.value.forEach((_, fi) => drawSeatsOnCanvas(fi)))
    )
    watch(layoutMode, (newMode, oldMode) => {
        if (newMode === 'custom' && oldMode === 'venue') {
            layoutFloors.value.forEach(floor => {
                floor.canvasShapes    = floor.canvasShapes.filter(s => s.isStage)
                floor.selectedShapeId = null; floor.selectedSeatId = null
            })
            seatsByZone.value = {}
            nextTick(() => layoutFloors.value.forEach((_, fi) => drawFloor(fi)))
        }
    })

    return {
        layoutMode, layoutFloors, activeFloor, activeFloorIdx, layoutSaved,
        seatsByZone, loadingSeats, seatHitMap, canvasContainerRefs,
        venueZoneLinks, selectedSeatInfo,
        makeFloor, addFloor, removeFloor,
        setCanvasContainerRef, setFloorCanvasRef,
        drawFloor, drawSeatsOnCanvas, rebuildAndDraw,
        onCanvasMouseDown, onCanvasMouseMove, onCanvasMouseUp, onCanvasWheel,
        addCustomShape, addStageShape, addZoneShapeToFloor, clearFloor, deleteSelectedShape, isZoneLinked,
        autoResizeShapeForZone, ensureSeatTransform,
        floorJsonPreviews, copyFloorJson, buildFullLayoutJson,
        loadSeatsForStep4, applyLayout, reloadSavedLayout, saveSeatPriceInline,
    }
}