export interface TaskScheduleItem {
  id: number
  elderName: string
  bedNo: string
  nursingItemName: string
  taskType: string
  caregiverName: string
  expectedServiceTime: string
  createdTime: string
  executeBy: string
  executeTime: string
  cancelBy: string
  cancelTime: string
  status: number
  statusLabel: string
}

export interface TaskScheduleDetail extends TaskScheduleItem {
  elderGender: string
  elderAge: number
  careLevelName: string
  caregiverNames: string
  elderAvatarUrl: string
  orderNo: string
  createdBy: string
  remark: string
  cancelReason: string
  executeTime: string
  executeImageName: string
  executeImageUrl: string
  executeRecord: string
}

export interface TaskSchedulePageResult {
  total: number
  rows: TaskScheduleItem[]
}
