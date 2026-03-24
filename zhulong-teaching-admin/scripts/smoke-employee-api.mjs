/**
 * 员工管理 API 烟测：分页/筛选、详情、新增、修改、单删、批量删
 * node scripts/smoke-employee-api.mjs
 * 环境变量 EMP_API_BASE 默认 http://127.0.0.1:8092
 */
import { loginForSmoke, jsonHeaders, tokenHeaders } from './smoke-auth.mjs'

const base = process.env.EMP_API_BASE ?? 'http://127.0.0.1:8092'

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
  const suf = String(stamp).slice(-6)
  const u1 = `smoke_a_${suf}`
  const u2 = `smoke_b_${suf}`
  const phone1 = `1${String(stamp).slice(-10)}`
  const phone2 = `1${String(stamp + 1).slice(-10)}`

  console.log('1) GET departments (need dept id)')
  let r = await getJson('/api/departments?page=1&pageSize=5', token)
  assert(r.json?.code === 1, 'GET departments code')
  const deptRows = r.json?.data?.rows ?? []
  assert(deptRows.length > 0, 'no departments')
  const deptId = deptRows[0].id

  console.log('2) GET /api/employees?page=1&pageSize=10')
  r = await getJson('/api/employees?page=1&pageSize=10', token)
  assert(r.json?.code === 1, 'GET employees page')
  const beforeTotal = Number(r.json?.data?.total ?? 0)

  console.log('3) GET filter empName + gender + date range')
  r = await getJson(
    '/api/employees?empName=赵&gender=2&startDate=2020-01-01&endDate=2030-12-31&page=1&pageSize=10',
    token,
  )
  assert(r.json?.code === 1, 'GET employees filter')

  console.log('4) POST create', u1)
  r = await postJson(
    '/api/employees',
    {
      username: u1,
      empName: '烟测甲',
      gender: 1,
      phone: phone1,
      deptId,
      positionName: '讲师',
      salary: 5000,
      avatarUrl: 'https://example.com/a.png',
      entryDate: '2024-01-15',
      workHistories: [
        { startDate: '2020-01-01', endDate: '2021-06-30', companyName: '测试公司', positionName: '开发' },
      ],
    },
    token,
  )
  assert(r.json?.code === 1, `POST create failed: ${JSON.stringify(r.json)}`)

  r = await getJson('/api/employees?page=1&pageSize=200', token)
  const row1 = (r.json?.data?.rows ?? []).find((x) => x.username === u1)
  assert(row1?.id, 'created employee not in list')
  const id1 = row1.id

  console.log('5) GET by id', id1)
  r = await getJson(`/api/employees/${id1}`, token)
  assert(r.json?.code === 1 && r.json?.data?.username === u1, 'GET by id')

  console.log('6) PUT update')
  r = await putJson(
    `/api/employees/${id1}`,
    {
      username: u1,
      empName: '烟测甲改',
      gender: 2,
      phone: phone1,
      deptId,
      positionName: '班主任',
      salary: 5100,
      avatarUrl: 'https://example.com/b.png',
      entryDate: '2024-02-01',
      workHistories: [],
    },
    token,
  )
  assert(r.json?.code === 1, `PUT failed: ${JSON.stringify(r.json)}`)

  r = await getJson(`/api/employees/${id1}`, token)
  assert(r.json?.data?.empName === '烟测甲改' && r.json?.data?.gender === 2, 'PUT not persisted')

  console.log('7) POST second employee for batch delete', u2)
  r = await postJson(
    '/api/employees',
    {
      username: u2,
      empName: '烟测乙',
      gender: 1,
      phone: phone2,
      deptId,
      positionName: '咨询师',
      salary: 4000,
      entryDate: '2024-03-01',
    },
    token,
  )
  assert(r.json?.code === 1, 'POST second failed')

  r = await getJson('/api/employees?page=1&pageSize=500', token)
  const row2 = (r.json?.data?.rows ?? []).find((x) => x.username === u2)
  assert(row2?.id, 'second not in list')
  const id2 = row2.id

  console.log('8) DELETE batch ids=', id1, id2)
  const q = `ids=${id1}&ids=${id2}`
  r = await delJson(`/api/employees?${q}`, token)
  assert(r.json?.code === 1, `batch DELETE failed: ${JSON.stringify(r.json)}`)

  r = await getJson(`/api/employees/${id1}`, token)
  assert(r.json?.code !== 1, 'soft-deleted employee should not return success on detail')

  r = await getJson('/api/employees?page=1&pageSize=500', token)
  const still = (r.json?.data?.rows ?? []).filter((x) => x.username === u1 || x.username === u2)
  assert(still.length === 0, 'deleted rows still in page list')

  const afterTotal = Number(r.json?.data?.total ?? 0)
  assert(afterTotal === beforeTotal, `total should restore: before=${beforeTotal} after=${afterTotal}`)

  console.log('9) POST + DELETE single')
  const u3 = `smoke_c_${suf}`
  const phone3 = `1${String(stamp + 2).slice(-10)}`
  r = await postJson(
    '/api/employees',
    {
      username: u3,
      empName: '烟测丙',
      gender: 1,
      phone: phone3,
      deptId,
      entryDate: '2024-04-01',
    },
    token,
  )
  assert(r.json?.code === 1, 'POST third failed')
  r = await getJson('/api/employees?page=1&pageSize=500', token)
  const row3 = (r.json?.data?.rows ?? []).find((x) => x.username === u3)
  const id3 = row3.id
  r = await delJson(`/api/employees/${id3}`, token)
  assert(r.json?.code === 1, 'single DELETE failed')

  console.log('smoke-employee-api OK')
}

main().catch((e) => {
  console.error(e)
  process.exit(1)
})
