import { apiRequest } from './http'

export type PageResult<T> = {
  total: number
  rows: T[]
}

export type ClassItem = {
  id: number
  className: string
  classroom?: string
  startDate: string
  endDate: string
  headTeacherEmpId?: number
  headTeacherName?: string
  /** 在读学员人数（库中统计） */
  studentCount?: number
  subjectName: string
  status: number
  lastOperateTime: string
}

export type ClassPayload = {
  className: string
  classroom?: string
  startDate: string
  endDate: string
  headTeacherEmpId?: number
  subjectName: string
}

export function pageClasses(params: {
  className?: string
  startDate?: string
  endDate?: string
  page: number
  pageSize: number
}): Promise<PageResult<ClassItem>> {
  const sp = new URLSearchParams()
  if (params.className?.trim()) sp.set('className', params.className.trim())
  if (params.startDate) sp.set('startDate', params.startDate)
  if (params.endDate) sp.set('endDate', params.endDate)
  sp.set('page', String(params.page))
  sp.set('pageSize', String(params.pageSize))
  return apiRequest<PageResult<ClassItem>>(`/api/classes?${sp.toString()}`, { method: 'GET' })
}

export function createClass(payload: ClassPayload): Promise<void> {
  return apiRequest<void>('/api/classes', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function updateClass(id: number, payload: ClassPayload): Promise<void> {
  return apiRequest<void>(`/api/classes/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export function deleteClass(id: number): Promise<void> {
  return apiRequest<void>(`/api/classes/${id}`, {
    method: 'DELETE',
  })
}
