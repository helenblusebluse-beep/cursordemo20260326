export interface ArrearsElderItem {
  elderName: string
  bedNo: string
  arrearsAmount: number
  payDeadline: string
}

export interface ArrearsElderPageResult {
  total: number
  rows: ArrearsElderItem[]
}

export interface ArrearsBillRow {
  billNo: string
  billMonth: string
  payDeadline: string
  payableAmount: number
}
