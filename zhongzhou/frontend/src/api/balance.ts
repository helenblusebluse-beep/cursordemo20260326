import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { BalanceQueryPageResult } from '../types/balance'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function queryBalanceQueries(params: {
  elderName?: string
  bedNo?: string
  page: number
  pageSize: number
}): Promise<BalanceQueryPageResult> {
  const res = await http.get<ApiResponse<BalanceQueryPageResult>>('/balance-queries', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询余额失败')
  return res.data.data
}
