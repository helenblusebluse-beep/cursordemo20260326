/**
 * 班级管理 API 烟测：node scripts/smoke-class-api.mjs
 * 环境变量 CLASS_API_BASE 默认 http://127.0.0.1:8092
 */
import { loginForSmoke, jsonHeaders, tokenHeaders } from './smoke-auth.mjs'

const base = process.env.CLASS_API_BASE ?? 'http://127.0.0.1:8092'

async function getJson(path, token) {
  const r = await fetch(`${base}${path}`, { headers: tokenHeaders(token) })
  const text = await r.text()
  if (!text) throw new Error(`空响应 HTTP ${r.status} ${path}`)
  return { ok: r.ok, status: r.status, json: JSON.parse(text) }
}

async function postJson(path, body, token) {
  const r = await fetch(`${base}${path}`, {
    method: 'POST',
    headers: jsonHeaders(token),
    body: JSON.stringify(body),
  })
  const text = await r.text()
  if (!text) throw new Error(`空响应 HTTP ${r.status} ${path}`)
  return { ok: r.ok, status: r.status, json: JSON.parse(text) }
}

async function putJson(path, body, token) {
  const r = await fetch(`${base}${path}`, {
    method: 'PUT',
    headers: jsonHeaders(token),
    body: JSON.stringify(body),
  })
  const text = await r.text()
  if (!text) throw new Error(`空响应 HTTP ${r.status} ${path}`)
  return { ok: r.ok, status: r.status, json: JSON.parse(text) }
}

async function delJson(path, token) {
  const r = await fetch(`${base}${path}`, { method: 'DELETE', headers: tokenHeaders(token) })
  const text = await r.text()
  if (!text) throw new Error(`空响应 HTTP ${r.status} ${path}`)
  return { ok: r.ok, status: r.status, json: JSON.parse(text) }
}

async function main() {
  console.log('0) login')
  const token = await loginForSmoke(base)

  const stamp = Date.now()
  const className = `SmokeCls${stamp}期`
  const payload = {
    className,
    classroom: 'A101',
    startDate: '2026-01-01',
    endDate: '2026-06-30',
    subjectName: 'JavaEE',
  }

  console.log('1) GET page')
  let r = await getJson('/api/classes?page=1&pageSize=5', token)
  console.log('code=', r.json?.code, 'total=', r.json?.data?.total)

  console.log('2) POST create')
  r = await postJson('/api/classes', payload, token)
  console.log('POST', r.json)
  if (r.json?.code !== 1) {
    console.error('create failed')
    process.exit(1)
  }

  const list = (await getJson('/api/classes?page=1&pageSize=50', token)).json?.data?.rows ?? []
  const row = list.find((x) => x.className === className)
  if (!row?.id) {
    console.error('created row not found in list')
    process.exit(1)
  }
  const id = row.id
  console.log('created id=', id)

  console.log('3) PUT update')
  r = await putJson(
    `/api/classes/${id}`,
    {
      ...payload,
      className,
      classroom: 'B202',
      endDate: '2026-07-31',
    },
    token,
  )
  console.log('PUT', r.json)
  if (r.json?.code !== 1) process.exit(1)

  console.log('4) GET by id')
  r = await getJson(`/api/classes/${id}`, token)
  console.log('GET id', r.json?.data?.className, r.json?.data?.classroom)

  console.log('5) DELETE')
  r = await delJson(`/api/classes/${id}`, token)
  console.log('DELETE', r.json)
  if (r.json?.code !== 1) process.exit(1)

  console.log('6) list after delete')
  r = await getJson('/api/classes?page=1&pageSize=200', token)
  const still = (r.json?.data?.rows ?? []).find((x) => x.id === id)
  console.log('still in list?', !!still)

  console.log('smoke-class-api OK')
}

main().catch((e) => {
  console.error(e)
  process.exit(1)
})
