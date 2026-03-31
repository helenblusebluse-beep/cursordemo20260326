import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type {
  NursingCareLevelBinding,
  NursingCareLevelPageResult,
  NursingItemOption,
  NursingItemPageResult,
  NursingPlan,
  NursingPlanPageResult,
} from '../types/nursing'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryNursingItems(params: {
  name?: string
  status?: number
  page: number
  pageSize: number
}): Promise<NursingItemPageResult> {
  const res = await http.get<ApiResponse<NursingItemPageResult>>('/nursing-items', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理项目失败')
  return res.data.data
}

export async function createNursingItem(payload: {
  itemName: string
  sortOrder: number
  unit?: string
  price: number
  imageName: string
  imageUrl?: string
  nursingRequirement?: string
  status: number
}): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/nursing-items', payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '新增护理项目失败')
  return res.data.data
}

export async function updateNursingItem(
  id: number,
  payload: {
    itemName: string
    sortOrder: number
    unit?: string
    price: number
    imageName: string
    imageUrl?: string
    nursingRequirement?: string
    status: number
  },
): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/nursing-items/${id}`, payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '修改护理项目失败')
}

export async function deleteNursingItem(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/nursing-items/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '删除护理项目失败')
}

export async function changeNursingItemStatus(id: number, status: number): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/nursing-items/${id}/status`, null, { params: { status } })
  if (res.data.code !== 1) throw new Error(res.data.msg || '更新护理项目状态失败')
}

export async function uploadNursingImage(file: File): Promise<{ fileName: string; fileUrl: string }> {
  const form = new FormData()
  form.append('file', file)
  const res = await http.post<ApiResponse<{ fileName: string; fileUrl: string }>>('/files/upload-image', form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  if (res.data.code !== 1) throw new Error(res.data.msg || '上传图片失败')
  return res.data.data
}

export async function queryNursingItemOptions(): Promise<NursingItemOption[]> {
  const res = await http.get<ApiResponse<NursingItemOption[]>>('/nursing-items/options')
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理项目选项失败')
  return res.data.data
}

export async function queryNursingPlans(params: {
  name?: string
  status?: number
  page: number
  pageSize: number
}): Promise<NursingPlanPageResult> {
  const res = await http.get<ApiResponse<NursingPlanPageResult>>('/nursing-plans', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理计划失败')
  return res.data.data
}

export async function getNursingPlan(id: number): Promise<NursingPlan> {
  const res = await http.get<ApiResponse<NursingPlan>>(`/nursing-plans/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理计划详情失败')
  return res.data.data
}

export async function createNursingPlan(payload: {
  planName: string
  sortOrder: number
  status: number
  items: Array<{
    nursingItemId: number | null
    expectedServiceTime: string
    executeCycle: string
    executeFrequency: number
  }>
}): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/nursing-plans', payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '新增护理计划失败')
  return res.data.data
}

export async function updateNursingPlan(
  id: number,
  payload: {
    planName: string
    sortOrder: number
    status: number
    items: Array<{
      nursingItemId: number | null
      expectedServiceTime: string
      executeCycle: string
      executeFrequency: number
    }>
  },
): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/nursing-plans/${id}`, payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '修改护理计划失败')
}

export async function deleteNursingPlan(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/nursing-plans/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '删除护理计划失败')
}

export async function changeNursingPlanStatus(id: number, status: number): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/nursing-plans/${id}/status`, null, { params: { status } })
  if (res.data.code !== 1) throw new Error(res.data.msg || '更新护理计划状态失败')
}

export async function queryNursingLevels(): Promise<NursingCareLevelBinding[]> {
  const res = await http.get<ApiResponse<NursingCareLevelBinding[]>>('/nursing-levels')
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理等级绑定失败')
  return res.data.data
}

export async function queryAvailableNursingPlans(): Promise<Array<{ id: number; planName: string }>> {
  const res = await http.get<ApiResponse<Array<{ id: number; planName: string }>>>('/nursing-levels/available-plans')
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询可绑定护理计划失败')
  return res.data.data
}

export async function updateNursingLevelBindings(careLevel: string, planIds: number[]): Promise<void> {
  const res = await http.post<ApiResponse<null>>('/nursing-levels/bindings', { planIds }, { params: { careLevel } })
  if (res.data.code !== 1) throw new Error(res.data.msg || '保存护理等级绑定失败')
}

export async function queryNursingCareLevels(params: {
  levelName?: string
  status?: number
  page: number
  pageSize: number
}): Promise<NursingCareLevelPageResult> {
  const res = await http.get<ApiResponse<NursingCareLevelPageResult>>('/nursing-care-levels', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询护理等级失败')
  return res.data.data
}

export async function createNursingCareLevel(payload: {
  levelName: string
  planId: number
  nursingFee: number
  status: number
  levelDesc: string
}): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/nursing-care-levels', payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '新增护理等级失败')
  return res.data.data
}

export async function updateNursingCareLevel(
  id: number,
  payload: {
    levelName: string
    planId: number
    nursingFee: number
    status: number
    levelDesc: string
  },
): Promise<void> {
  const res = await http.put<ApiResponse<null>>(`/nursing-care-levels/${id}`, payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '修改护理等级失败')
}

export async function deleteNursingCareLevel(id: number): Promise<void> {
  const res = await http.delete<ApiResponse<null>>(`/nursing-care-levels/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '删除护理等级失败')
}

export async function changeNursingCareLevelStatus(id: number, status: number): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/nursing-care-levels/${id}/status`, null, { params: { status } })
  if (res.data.code !== 1) throw new Error(res.data.msg || '更新护理等级状态失败')
}
