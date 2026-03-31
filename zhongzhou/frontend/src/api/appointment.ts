import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { AppointmentPageResult } from '../types/appointment'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryAppointments(params: {
  visitorName?: string
  visitorPhone?: string
  status?: number
  reserveType?: number
  page: number
  pageSize: number
}): Promise<AppointmentPageResult> {
  const res = await http.get<ApiResponse<AppointmentPageResult>>('/appointments', { params })
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '查询预约失败')
  }
  return res.data.data
}

export async function createAppointment(payload: {
  reserveType: number
  visitorName: string
  visitorPhone: string
  elderName: string
  scheduledTime: string
}): Promise<void> {
  const res = await http.post<ApiResponse<null>>('/appointments', payload)
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '新增预约失败')
  }
}

export async function confirmAppointment(id: number, confirmedTime: string): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/appointments/${id}/confirm`, { confirmedTime })
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '确认到访失败')
  }
}

export async function cancelAppointment(id: number): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/appointments/${id}/cancel`)
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '取消预约失败')
  }
}

export async function deleteAppointment(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/appointments/${id}`)
  if (res.data.code !== 1) {
    throw new Error(res.data.msg || '删除预约失败')
  }
}
