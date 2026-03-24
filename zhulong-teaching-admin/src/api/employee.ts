import { apiRequest } from './http'

export type PageResult<T> = {
  total: number
  rows: T[]
}

export type WorkHistoryItem = {
  startDate?: string
  endDate?: string
  companyName?: string
  positionName?: string
}

export type EmployeeItem = {
  id: number
  username: string
  empName: string
  gender: number
  phone: string
  deptId?: number
  positionName?: string
  salary?: string
  avatarUrl?: string
  entryDate?: string
  lastOperateTime?: string
  workHistories?: WorkHistoryItem[]
}

export type EmployeePayload = {
  username: string
  empName: string
  gender: number
  phone: string
  deptId?: number
  positionName?: string
  salary?: number
  avatarUrl?: string
  entryDate?: string
  workHistories?: WorkHistoryItem[]
}

export function pageEmployees(params: {
  empName?: string
  gender?: number
  startDate?: string
  endDate?: string
  page: number
  pageSize: number
}): Promise<PageResult<EmployeeItem>> {
  const sp = new URLSearchParams()
  if (params.empName?.trim()) sp.set('empName', params.empName.trim())
  if (params.gender) sp.set('gender', String(params.gender))
  if (params.startDate) sp.set('startDate', params.startDate)
  if (params.endDate) sp.set('endDate', params.endDate)
  sp.set('page', String(params.page))
  sp.set('pageSize', String(params.pageSize))
  return apiRequest<PageResult<EmployeeItem>>(`/api/employees?${sp.toString()}`, { method: 'GET' })
}

/** 上传头像到 SFTP 文件服务器，返回可写入 avatarUrl 的 HTTP 地址 */
export async function uploadEmployeeAvatar(file: File): Promise<string> {
  const fd = new FormData()
  fd.append('file', file)
  const data = await apiRequest<{ url: string }>('/api/employees/avatar', { method: 'POST', body: fd })
  const url = data?.url
  if (!url) throw new Error('上传返回缺少 url')
  return url
}

export function getEmployee(id: number): Promise<EmployeeItem> {
  return apiRequest<EmployeeItem>(`/api/employees/${id}`, { method: 'GET' })
}

export function createEmployee(payload: EmployeePayload): Promise<void> {
  return apiRequest<void>('/api/employees', { method: 'POST', body: JSON.stringify(payload) })
}

export function updateEmployee(id: number, payload: EmployeePayload): Promise<void> {
  return apiRequest<void>(`/api/employees/${id}`, { method: 'PUT', body: JSON.stringify(payload) })
}

export function deleteEmployee(id: number): Promise<void> {
  return apiRequest<void>(`/api/employees/${id}`, { method: 'DELETE' })
}

export function deleteEmployees(ids: number[]): Promise<void> {
  const q = ids.map((id) => `ids=${id}`).join('&')
  return apiRequest<void>(`/api/employees?${q}`, { method: 'DELETE' })
}
