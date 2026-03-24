import { apiRequest } from './http'

export type DepartmentItem = {
  id: number
  deptName: string
  lastOperateTime: string
}

export type PageResult<T> = {
  total: number
  rows: T[]
}

export function listDepartments(params: {
  deptName?: string
  page: number
  pageSize: number
}): Promise<PageResult<DepartmentItem> | DepartmentItem[]> {
  const search = new URLSearchParams()
  if (params.deptName?.trim()) search.set('deptName', params.deptName.trim())
  search.set('page', String(params.page))
  search.set('pageSize', String(params.pageSize))
  return apiRequest<PageResult<DepartmentItem> | DepartmentItem[]>(
    `/api/departments?${search.toString()}`,
    { method: 'GET' },
  )
}

export function createDepartment(deptName: string): Promise<void> {
  return apiRequest<void>('/api/departments', {
    method: 'POST',
    body: JSON.stringify({ deptName }),
  })
}

export function updateDepartment(id: number, deptName: string): Promise<void> {
  return apiRequest<void>(`/api/departments/${id}`, {
    method: 'PUT',
    body: JSON.stringify({ deptName }),
  })
}

export function deleteDepartment(id: number): Promise<void> {
  return apiRequest<void>(`/api/departments/${id}`, {
    method: 'DELETE',
  })
}

