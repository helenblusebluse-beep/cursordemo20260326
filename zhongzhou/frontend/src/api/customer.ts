import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { CustomerManagePageResult } from '../types/customer'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryCustomers(params: {
  customerNickname?: string
  customerPhone?: string
  signed?: string
  page: number
  pageSize: number
}): Promise<CustomerManagePageResult> {
  const res = await http.get<ApiResponse<CustomerManagePageResult>>('/customers', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询客户管理失败')
  return res.data.data
}
