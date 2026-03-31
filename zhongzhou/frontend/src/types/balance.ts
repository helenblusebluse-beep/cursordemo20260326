export interface BalanceQueryItem {
  id: number
  elderName: string
  bedNo: string
  accountBalance: number
  depositBalance: number
  changedTime: string
}

export interface BalanceQueryPageResult {
  total: number
  rows: BalanceQueryItem[]
}
