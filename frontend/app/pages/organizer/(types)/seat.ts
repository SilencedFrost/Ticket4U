// TODO: refactor to features/organizer/
export interface Seat {
  id:            string
  zoneId:        string
  name?:         string | null
  rowName?:      string | null
  colName?:      string | null
  seatCode?:     string | null
  /** null means use zone price */
  priceOverride?: number | null
}