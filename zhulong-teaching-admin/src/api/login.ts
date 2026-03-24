import { setSession } from '@/auth/session'

export type LoginResult = {
  id: number
  username: string
  name: string
  token: string
}

/**
 * POST /login，成功后写入 localStorage（token、姓名、id）
 */
export async function login(username: string, password: string): Promise<LoginResult> {
  const res = await fetch('/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  })
  const body = (await res.json()) as { code: number; msg?: string; data?: LoginResult }
  if (body.code !== 1 || !body.data) {
    throw new Error(body.msg ?? '登录失败')
  }
  const d = body.data
  setSession(d.token, d.name, d.id, d.username)
  return d
}
