/**
 * 学员管理 API 烟测：分页、新增、修改、删除
 * node scripts/smoke-student-api.mjs
 */
import { loginForSmoke, jsonHeaders, tokenHeaders } from './smoke-auth.mjs'

const base = process.env.STUDENT_API_BASE ?? 'http://127.0.0.1:8092'

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

function assert(cond, msg) {
  if (!cond) throw new Error(msg)
}

async function main() {
  console.log('0) login')
  const token = await loginForSmoke(base)

  const stamp = Date.now()
  const suf = String(stamp).slice(-8)
  const studentNo = suf.padStart(10, '0').slice(-10)
  const phone = `1${String(stamp).slice(-10)}`
  const idCardNo = `1101011990${String(stamp % 100000000).padStart(8, '0')}`.slice(0, 18)
  const idCard = idCardNo.length === 18 ? idCardNo : idCardNo.padEnd(18, '0').slice(0, 18)

  console.log('1) GET page')
  let r = await getJson('/api/students?page=1&pageSize=5', token)
  assert(r.json?.code === 1, 'GET page')

  console.log('2) POST create')
  r = await postJson(
    '/api/students',
    {
      studentName: '烟测学',
      studentNo,
      gender: 1,
      phone,
      idCardNo: idCard,
      isCollegeStudent: false,
    },
    token,
  )
  assert(r.json?.code === 1, `create failed: ${JSON.stringify(r.json)}`)

  r = await getJson('/api/students?page=1&pageSize=100', token)
  const row = (r.json?.data?.rows ?? []).find((x) => x.studentNo === studentNo)
  assert(row?.id, 'created student not in list')
  const id = row.id

  console.log('3) PUT update')
  r = await putJson(
    `/api/students/${id}`,
    {
      studentName: '烟测学改',
      studentNo,
      gender: 2,
      phone,
      idCardNo: idCard,
      isCollegeStudent: true,
    },
    token,
  )
  assert(r.json?.code === 1, 'PUT failed')

  console.log('4) GET by id')
  r = await getJson(`/api/students/${id}`, token)
  assert(r.json?.data?.studentName === '烟测学改', 'GET by id')

  console.log('5) DELETE')
  r = await delJson(`/api/students/${id}`, token)
  assert(r.json?.code === 1, 'DELETE failed')

  console.log('smoke-student-api OK')
}

main().catch((e) => {
  console.error(e)
  process.exit(1)
})
