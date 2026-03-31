export interface PrepayRechargeItem {
  id: number
  rechargeNo: string
  elderName: string
  elderIdNo: string
  bedNo: string
  rechargeMethod: string
  rechargeAmount: number
  voucherFileName: string
  voucherFileUrl: string
  rechargeRemark: string
  createdTime: string
}

export interface PrepayElderOption {
  elderId: number
  elderName: string
  elderIdNo: string
  bedNo: string
}

export interface PrepayRechargePageResult {
  total: number
  rows: PrepayRechargeItem[]
}
