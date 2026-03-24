import { clearAuth, getToken } from '@/auth/session'

export type ApiResponse<T> = {
  code: number
  msg?: string
  data: T
}

/**
 * 业务接口统一请求：自动附加请求头 `token`，HTTP 401 时清空登录并跳转登录页。
 */
export async function apiRequest<T>(url: string, init?: RequestInit): Promise<T> {
  const headers = new Headers(init?.headers)
  const token = getToken()
  if (token) headers.set('token', token)
  const isForm = init?.body instanceof FormData
  if (!isForm && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }

  const res = await fetch(url, { ...init, headers })

  if (res.status === 401) {
    clearAuth()
    if (!window.location.pathname.startsWith('/login')) {
      const q = new URLSearchParams()
      q.set('redirect', window.location.pathname + window.location.search)
      window.location.assign(`/login?${q.toString()}`)
    }
    throw new Error('未登录或登录已过期')
  }

  if (!res.ok) {
    const fallback = `请求失败: ${res.status}`
    try {
      const text = await res.text()
      if (!text) throw new Error(fallback)
      try {
        const maybe = JSON.parse(text) as { msg?: string; message?: string }
        throw new Error(maybe.msg ?? maybe.message ?? fallback)
      } catch {
        throw new Error(`${fallback} ${text}`)
      }
    } catch (e) {
      throw e instanceof Error ? e : new Error(fallback)
    }
  }

  const body = (await res.json()) as ApiResponse<T>
  const code = Number(body.code)
  if (!Number.isFinite(code) || code !== 1) {
    throw new Error(body.msg ?? '业务处理失败')
  }
  return body.data
}
