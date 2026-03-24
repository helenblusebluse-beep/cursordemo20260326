import { apiRequest } from './http'

export type PageResult<T> = {
  total: number
  rows: T[]
}

export type StudentItem = {
  id: number
  studentName: string
  studentNo: string
  gender: number
  phone: string
  idCardNo: string
  isCollegeStudent: boolean
  address?: string
  education?: string
  graduateDate?: string
  classId?: number
  /** 后端关联班级名称，便于列表展示 */
  className?: string
  demeritCount: number
  demeritScore: number
  lastOperateTime: string
}

export type StudentPayload = {
  studentName: string
  studentNo: string
  gender: number
  phone: string
  idCardNo: string
  isCollegeStudent: boolean
  address?: string
  education?: string
  graduateDate?: string
  classId?: number | null
}

export function pageStudents(params: {
  studentName?: string
  education?: string
  classId?: number
  page: number
  pageSize: number
}): Promise<PageResult<StudentItem>> {
  const sp = new URLSearchParams()
  if (params.studentName?.trim()) sp.set('studentName', params.studentName.trim())
  if (params.education?.trim()) sp.set('education', params.education.trim())
  if (params.classId != null) sp.set('classId', String(params.classId))
  sp.set('page', String(params.page))
  sp.set('pageSize', String(params.pageSize))
  return apiRequest<PageResult<StudentItem>>(`/api/students?${sp.toString()}`, { method: 'GET' })
}

export function createStudent(payload: StudentPayload): Promise<void> {
  return apiRequest<void>('/api/students', { method: 'POST', body: JSON.stringify(payload) })
}

export function updateStudent(id: number, payload: StudentPayload): Promise<void> {
  return apiRequest<void>(`/api/students/${id}`, { method: 'PUT', body: JSON.stringify(payload) })
}

export function deleteStudent(id: number): Promise<void> {
  return apiRequest<void>(`/api/students/${id}`, { method: 'DELETE' })
}

export function deleteStudents(ids: number[]): Promise<void> {
  const q = ids.map((id) => `ids=${id}`).join('&')
  return apiRequest<void>(`/api/students?${q}`, { method: 'DELETE' })
}

export function demeritStudent(id: number, score: number, remark?: string): Promise<void> {
  return apiRequest<void>(`/api/students/${id}/demerits`, {
    method: 'POST',
    body: JSON.stringify({ score, remark }),
  })
}
