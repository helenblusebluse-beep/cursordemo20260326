export type VisitRecordItem = {
  id: number
  appointmentId: number
  reserveType: number
  reserveTypeLabel: string
  visitorName: string
  visitorPhone: string
  elderName: string
  checkinTime: string
  createdTime: string
  checkoutTime: string | null
  status: number
  statusLabel: string
}

export type VisitPageResult = {
  total: number
  rows: VisitRecordItem[]
}

export type PendingAppointmentItem = {
  id: number
  reserveType: number
  visitorName: string
  visitorPhone: string
  elderName: string
  scheduledTime: string
  reserveTypeLabel: string
}
