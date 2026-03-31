export type AppointmentItem = {
  id: number
  reserveType: number
  reserveTypeLabel: string
  visitorName: string
  visitorPhone: string
  elderName: string
  scheduledTime: string
  createdTime: string
  status: number
  statusLabel: string
}

export type AppointmentPageResult = {
  total: number
  rows: AppointmentItem[]
}
