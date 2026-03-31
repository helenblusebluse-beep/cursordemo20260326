export type ContractTrackingItem = {
  id: number
  contractNo: string
  contractName: string
  elderName: string
  idCard: string
  contractPeriod: string
  contractStatus: '未生效' | '生效中' | '已过期' | '已失效'
  createdTime: string
}

export type ContractTrackingPageResult = {
  total: number
  rows: ContractTrackingItem[]
}

export type ContractTrackingDetail = {
  id: number
  checkinId: number
  contractNo: string
  contractName: string
  elderName: string
  idCard: string
  contractStartTime: string
  contractEndTime: string
  contractPeriod: string
  contractStatus: string
  signDate: string
  payerName: string
  payerContact: string
  createdTime: string
  contractFileName: string
  contractFileUrl: string
  dissolveSubmitter: string
  dissolveDate: string
  dissolveAgreementFileName: string
  dissolveAgreementFileUrl: string
}
