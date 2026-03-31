import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { ArrearsBillRow, ArrearsElderPageResult } from '../types/arrears'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function queryArrearsElders(params: {
  elderName?: string
  bedNo?: string
  page: number
  pageSize: number
}): Promise<ArrearsElderPageResult> {
  const res = await http.get<ApiResponse<ArrearsElderPageResult>>('/arrears-elders', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询欠费老人失败')
  return res.data.data
}

export async function queryArrearsBills(elderName: string, bedNo: string): Promise<ArrearsBillRow[]> {
  const res = await http.get<ApiResponse<ArrearsBillRow[]>>('/arrears-elders/bills', {
    params: { elderName, bedNo },
  })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询欠费账单失败')
  return res.data.data
}
