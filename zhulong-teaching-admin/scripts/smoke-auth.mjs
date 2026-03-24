/**
 * 烟测登录：获取 JWT，后续 /api/** 请求需带请求头 token
 */
export async function loginForSmoke(base, username = 'root', password = 'root') {
  const url = `${base.replace(/\/$/, '')}/login`
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json; charset=utf-8' },
    body: JSON.stringify({ username, password }),
  })
  const text = await res.text()
  if (!text) {
    throw new Error(`登录无响应: HTTP ${res.status}，请确认后端已启动 (${base})`)
  }
  let json
  try {
    json = JSON.parse(text)
  } catch {
    throw new Error(`登录响应非 JSON: HTTP ${res.status} ${text.slice(0, 200)}`)
  }
  if (json.code !== 1 || !json.data?.token) {
    throw new Error(`登录失败: ${JSON.stringify(json)}`)
  }
  return json.data.token
}

export function jsonHeaders(token) {
  return {
    'Content-Type': 'application/json; charset=utf-8',
    token,
  }
}

export function tokenHeaders(token) {
  return { token }
}
