export interface SeatingLayoutZoneCol {
  zoneId: string
  colspan: number
}

export interface SeatingLayoutSpecialCol {
  type: 'foh' | 'empty'
  colspan: number
}

export type SeatingLayoutCol = SeatingLayoutZoneCol | SeatingLayoutSpecialCol

export interface SeatingLayoutStageRow {
  type: 'stage'
}

export interface SeatingLayoutZonesRow {
  type: 'zones'
  cols: SeatingLayoutCol[]
}

export type SeatingLayoutRow = SeatingLayoutStageRow | SeatingLayoutZonesRow

export interface SeatingLayout {
  rows: SeatingLayoutRow[]
}

export const isZoneCol = (col: SeatingLayoutCol): col is SeatingLayoutZoneCol => {
  return 'zoneId' in col
}