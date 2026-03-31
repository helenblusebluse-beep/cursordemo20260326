import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { RefundDetail, RefundPageResult } from '../types/refund'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function queryRefunds(params: {
  refundNo?: string
  orderNo?: string
  applicant?: string
  beginTime?: string
  endTime?: string
  status: number
  page: number
  pageSize: number
}): Promise<RefundPageResult> {
  const res = await http.get<ApiResponse<RefundPageResult>>('/refunds', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询退款记录失败')
  return res.data.data
}

export async function getRefund(id: number): Promise<RefundDetail> {
  const res = await http.get<ApiResponse<RefundDetail>>(`/refunds/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询退款详情失败')
  return res.data.data
}
