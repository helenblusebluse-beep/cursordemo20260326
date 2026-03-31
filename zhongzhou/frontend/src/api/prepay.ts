import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { PrepayElderOption, PrepayRechargePageResult } from '../types/prepay'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function queryPrepayRecharges(params: {
  elderName?: string
  bedNo?: string
  page: number
  pageSize: number
}): Promise<PrepayRechargePageResult> {
  const res = await http.get<ApiResponse<PrepayRechargePageResult>>('/prepay-recharges', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询预缴款充值记录失败')
  return res.data.data
}

export async function queryPrepayElderOptions(): Promise<PrepayElderOption[]> {
  const res = await http.get<ApiResponse<PrepayElderOption[]>>('/prepay-recharges/elder-options')
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询老人选项失败')
  return res.data.data
}

export async function createPrepayRecharge(payload: {
  elderId: number
  rechargeMethod: string
  rechargeAmount: number
  voucherFileName: string
  voucherFileUrl: string
  rechargeRemark: string
}): Promise<void> {
  const res = await http.post<ApiResponse<null>>('/prepay-recharges', payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '充值失败')
}
