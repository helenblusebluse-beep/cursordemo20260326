import { apiRequest } from './http'

export type UserItem = {
  id: number
  username: string
  displayName: string
  roleName: string
  status: number
}

export type PageResult<T> = {
  total: number
  rows: T[]
}

export function listUsers(params: {
  keyword?: string
  page: number
  pageSize: number
}): Promise<PageResult<UserItem>> {
  const search = new URLSearchParams()
  if (params.keyword?.trim()) search.set('keyword', params.keyword.trim())
  search.set('page', String(params.page))
  search.set('pageSize', String(params.pageSize))
  return apiRequest<PageResult<UserItem>>(`/api/users?${search.toString()}`, { method: 'GET' })
}

export function createUser(payload: {
  username: string
  password: string
  displayName: string
  roleName: string
  status: number
}): Promise<void> {
  return apiRequest<void>('/api/users', {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function updateUser(
  id: number,
  payload: {
    username: string
    password?: string
    displayName: string
    roleName: string
    status: number
  },
): Promise<void> {
  return apiRequest<void>(`/api/users/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export function deleteUser(id: number): Promise<void> {
  return apiRequest<void>(`/api/users/${id}`, { method: 'DELETE' })
}
