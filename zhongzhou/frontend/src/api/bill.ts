import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { BillDetail } from '../types/bill'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function getBill(id: number): Promise<BillDetail> {
  const res = await http.get<ApiResponse<BillDetail>>(`/bills/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询账单详情失败')
  return res.data.data
}
