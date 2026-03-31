import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { ContractTrackingDetail, ContractTrackingPageResult } from '../types/contract'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryContracts(params: {
  contractNo?: string
  elderName?: string
  contractStatus?: string
  page: number
  pageSize: number
}): Promise<ContractTrackingPageResult> {
  const res = await http.get<ApiResponse<ContractTrackingPageResult>>('/contracts', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询合同跟踪失败')
  return res.data.data
}

export async function getContractDetail(id: number): Promise<ContractTrackingDetail> {
  const res = await http.get<ApiResponse<ContractTrackingDetail>>(`/contracts/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询合同详情失败')
  return res.data.data
}
