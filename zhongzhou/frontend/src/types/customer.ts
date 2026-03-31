export type CustomerManageItem = {
  id: number
  customerNickname: string
  customerPhone: string
  signed: '是' | '否'
  orderTrackCount: number
  bindElderNames: string
  firstLoginTime: string
}

export type CustomerManagePageResult = {
  total: number
  rows: CustomerManageItem[]
}
