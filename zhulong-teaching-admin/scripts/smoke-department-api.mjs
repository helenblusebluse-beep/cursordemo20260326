/**
 * 部门管理 API 完整烟测：列表、新增、修改、删除
 * node scripts/smoke-department-api.mjs
 * 环境变量 DEPT_API_BASE 默认 http://127.0.0.1:8092
 */
import { loginForSmoke, jsonHeaders, tokenHeaders } from './smoke-auth.mjs'

const base = process.env.DEPT_API_BASE ?? 'http://127.0.0.1:8092'

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

  console.log('1) GET /api/departments?page=1&pageSize=5')
  let r = await getJson('/api/departments?page=1&pageSize=5', token)
  if (r.json?.code !== 1) {
    console.error('GET list failed', r.json)
    process.exit(1)
  }
  console.log('total=', r.json?.data?.total)

  const stamp = Date.now()
  const suf = String(stamp).slice(-6)
  const nameA = `烟测${suf}`
  const nameB = `烟改${suf}`

  console.log('2) POST create', nameA)
  r = await postJson('/api/departments', { deptName: nameA }, token)
  console.log(r.json)
  if (r.json?.code !== 1) {
    console.error('POST create failed')
    process.exit(1)
  }

  console.log('3) find id in list')
  r = await getJson('/api/departments?page=1&pageSize=200', token)
  const rows = r.json?.data?.rows ?? []
  const row = rows.find((x) => x.deptName === nameA)
  if (!row?.id) {
    console.error('created dept not found')
    process.exit(1)
  }
  const id = row.id
  console.log('id=', id)

  console.log('4) PUT update ->', nameB)
  r = await putJson(`/api/departments/${id}`, { deptName: nameB }, token)
  console.log(r.json)
  if (r.json?.code !== 1) {
    console.error('PUT failed')
    process.exit(1)
  }

  console.log('5) GET by id')
  r = await getJson(`/api/departments/${id}`, token)
  if (r.json?.data?.deptName !== nameB) {
    console.error('GET by id name mismatch', r.json?.data)
    process.exit(1)
  }

  console.log('6) DELETE')
  r = await delJson(`/api/departments/${id}`, token)
  console.log(r.json)
  if (r.json?.code !== 1) {
    console.error('DELETE failed')
    process.exit(1)
  }

  console.log('7) list after delete (should not contain id)')
  r = await getJson('/api/departments?page=1&pageSize=500', token)
  const still = (r.json?.data?.rows ?? []).find((x) => x.id === id)
  if (still) {
    console.error('row still in list after delete')
    process.exit(1)
  }

  console.log('smoke-department-api OK')
}

main().catch((e) => {
  console.error(e)
  process.exit(1)
})
