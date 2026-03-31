export type CheckinItem = {
  id: number
  elderName: string
  idCard: string
  roomNo: string
  careLevel: string
  checkinPeriod: string
  createdTime: string
}

export type CheckinPageResult = {
  total: number
  rows: CheckinItem[]
}

export type CheckinDetail = {
  id: number
  elderName: string
  idCard: string
  birthDate: string
  age: number
  gender: string
  contactPhone: string
  homeAddress: string
  profilePhotoName: string
  idCardFrontName: string
  idCardBackName: string
  roomNo: string
  careLevel: string
  checkinPeriod: string
  feePeriod: string
  nursingFee: number
  bedFee: number
  otherFee: number
  medicalFee: number
  subsidyFee: number
  deposit: number
  contractName: string
  signDate: string
  payerName: string
  payerContact: string
  contractFileName: string
  monthlyAmount: number
  currentAmount: number
  billAmount: number
  billDays: number
  familyMembers: Array<{ familyName: string; familyContact: string; relationType: string }>
  createdTime: string
}

export type CareLevelOption = {
  levelName: string
  nursingFee: number
}
