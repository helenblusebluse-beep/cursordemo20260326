export type NursingItem = {
  id: number
  itemName: string
  sortOrder: number
  unit: string
  price: number
  imageName: string
  imageUrl?: string
  nursingRequirement: string
  status: number
  statusLabel: '启用' | '禁用'
  bindPlanCount: number
  createdTime: string
  canEdit: boolean
  canDelete: boolean
  canDisable: boolean
}

export type NursingItemPageResult = {
  total: number
  rows: NursingItem[]
}

export type NursingItemOption = {
  id: number
  itemName: string
}

export type NursingPlanItem = {
  rowNo: number
  nursingItemId: number
  nursingItemName: string
  expectedServiceTime: string
  executeCycle: '每日' | '每周' | '每月'
  executeFrequency: number
}

export type NursingPlan = {
  id: number
  planName: string
  sortOrder: number
  status: number
  statusLabel: '启用' | '禁用'
  createdTime: string
  bindLevelCount: number
  canEdit: boolean
  canDelete: boolean
  canDisable: boolean
  items?: NursingPlanItem[]
}

export type NursingPlanPageResult = {
  total: number
  rows: NursingPlan[]
}

export type NursingCareLevelBinding = {
  levelName: string
  planIds: number[]
}

export type NursingCareLevelItem = {
  id: number
  levelName: string
  planId: number
  planName: string
  nursingFee: number
  status: number
  statusLabel: '启用' | '禁用'
  levelDesc: string
  createdTime: string
  canEdit: boolean
  canDelete: boolean
  canDisable: boolean
}

export type NursingCareLevelPageResult = {
  total: number
  rows: NursingCareLevelItem[]
}
