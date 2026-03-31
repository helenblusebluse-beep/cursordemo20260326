/**
 * 验证：报警规则/运行状态/服务调用/报警数据 均满足联调数量
 * 用法: node scripts/verify-iot-test-data.mjs
 * 环境: API_BASE 默认 http://127.0.0.1:8080/api
 */
const API_BASE = process.env.API_BASE || 'http://127.0.0.1:8080/api'

async function getJson(url) {
  const res = await fetch(url)
  const text = await res.text()
  let data
  try {
    data = JSON.parse(text)
  } catch {
    throw new Error(`非 JSON 响应 ${res.status}: ${text.slice(0, 200)}`)
  }
  if (!res.ok) {
    throw new Error(`HTTP ${res.status}: ${data.msg || text}`)
  }
  if (data.code !== 1) {
    throw new Error(data.msg || '业务失败')
  }
  return data.data
}

function assert(cond, msg) {
  if (!cond) throw new Error(msg)
}

async function main() {
  console.log('API_BASE =', API_BASE)

  const rules = await getJson(`${API_BASE}/iot/alarm-rules?page=1&pageSize=1`)
  assert(rules.total >= 30, `报警规则总数应≥30，当前 ${rules.total}`)
  console.log('[OK] 报警规则 total =', rules.total)

  const mods2 = await getJson(`${API_BASE}/iot/devices/2/tsl/modules`)
  assert(mods2 && mods2.length > 0, '设备2 无物模型模块')
  const moduleId2 = mods2[0].id
  const run = await getJson(
    `${API_BASE}/iot/devices/2/tsl/running-status?moduleId=${moduleId2}&page=1&pageSize=10`,
  )
  assert(run.total >= 30, `运行状态总数应≥30，当前 ${run.total}`)
  console.log('[OK] 运行状态 total =', run.total)

  const mods4 = await getJson(`${API_BASE}/iot/devices/4/tsl/modules`)
  assert(mods4 && mods4.length > 0, '设备4 无物模型模块')
  const moduleId4 = mods4[0].id
  const svc = await getJson(
    `${API_BASE}/iot/devices/4/tsl/service-invocations?moduleId=${moduleId4}&rangeType=1h&offset=0&limit=10`,
  )
  assert(svc.total >= 30, `服务调用总数应≥30，当前 ${svc.total}`)
  console.log('[OK] 服务调用 total =', svc.total)

  const records = await getJson(`${API_BASE}/iot/alarm-records?page=1&pageSize=10`)
  assert(records.total >= 30, `报警数据总数应≥30，当前 ${records.total}`)
  console.log('[OK] 报警数据 total =', records.total)

  console.log('\n全部检查通过。')
}

main().catch((e) => {
  console.error('[FAIL]', e.message)
  process.exit(1)
})
