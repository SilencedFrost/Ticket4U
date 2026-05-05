import { computed, type Ref } from 'vue';

export interface Point {
  x: number;
  y: number;
}

export function useInterpolation(rawPoints: Ref<Point[]>) {
  const points = computed(() => [...rawPoints.value].sort((a, b) => a.x - b.x));

  function interpolate(x: number): number {
    const pts = points.value;

    if (pts.length === 0) return 0;
    const first = pts[0];
    const last = pts[pts.length - 1];

    if (!first || !last) return 0;

    if (pts.length === 1) return first.y;

    if (x <= first.x) return first.y;

    if (x >= last.x) return last.y;

    for (let i = 0; i < pts.length - 1; i++) {
      const p0 = pts[i] as Point;
      const p1 = pts[i + 1] as Point;

      if (x < p0.x || x >= p1.x) continue;

      if (p1.x === p0.x) return p0.y;

      const t = (x - p0.x) / (p1.x - p0.x);
      return p0.y + t * (p1.y - p0.y);
    }

    return pts[pts.length - 1]?.y ?? 0;
  }

  return {
    points,
    interpolate,
  };
}
