import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { PendingAppointmentItem, VisitPageResult } from '../types/visit'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryVisitRecords(params: {
  visitorName?: string
  visitorPhone?: string
  startTime?: string
  endTime?: string
  reserveType?: number
  page: number
  pageSize: number
}): Promise<VisitPageResult> {
  const res = await http.get<ApiResponse<VisitPageResult>>('/visits', { params })
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '查询来访登记失败')
  }
  return res.data.data
}

export async function createVisit(payload: {
  reserveType: number
  visitorName: string
  visitorPhone: string
  elderName: string
  checkinTime: string
}): Promise<void> {
  const res = await http.post<ApiResponse<null>>('/visits', payload)
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '来访登记失败')
  }
}

export async function queryPendingAppointments(): Promise<PendingAppointmentItem[]> {
  const res = await http.get<ApiResponse<PendingAppointmentItem[]>>('/visits/pending-appointments')
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '查询待上门预约失败')
  }
  return res.data.data
}

export async function checkinVisit(payload: { appointmentId: number; checkinTime: string }): Promise<void> {
  const res = await http.post<ApiResponse<null>>('/visits/checkin', payload)
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '办理来访失败')
  }
}

export async function checkoutVisit(id: number, checkoutTime: string): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/visits/${id}/checkout`, { checkoutTime })
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '办理离院失败')
  }
}

export async function deleteVisit(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/visits/${id}`)
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '删除来访失败')
  }
}
