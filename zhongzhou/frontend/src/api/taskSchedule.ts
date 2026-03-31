import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { TaskScheduleDetail, TaskSchedulePageResult } from '../types/taskSchedule'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryTaskSchedules(params: {
  elderName?: string
  caregiverName?: string
  nursingItemName?: string
  beginTime?: string
  endTime?: string
  status: number
  page: number
  pageSize: number
}): Promise<TaskSchedulePageResult> {
  const res = await http.get<ApiResponse<TaskSchedulePageResult>>('/task-schedules', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询任务失败')
  return res.data.data
}

export async function getTaskSchedule(id: number): Promise<TaskScheduleDetail> {
  const res = await http.get<ApiResponse<TaskScheduleDetail>>(`/task-schedules/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询任务详情失败')
  return res.data.data
}

export async function cancelTaskSchedule(id: number, cancelReason: string): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/task-schedules/${id}/cancel`, { cancelReason })
  if (res.data.code !== 1) throw new Error(res.data.msg || '取消任务失败')
}

export async function executeTaskSchedule(
  id: number,
  payload: { executeTime: string; executeImageName: string; executeImageUrl: string; executeBy: string; executeRecord: string },
): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/task-schedules/${id}/execute`, payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '执行任务失败')
}

export async function rescheduleTaskSchedule(id: number, expectedServiceTime: string): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/task-schedules/${id}/reschedule`, { expectedServiceTime })
  if (res.data.code !== 1) throw new Error(res.data.msg || '改期失败')
}
