import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { OrderDetail, OrderPageResult } from '../types/order'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

export async function queryOrders(params: {
  orderNo?: string
  elderName?: string
  orderUserName?: string
  beginTime?: string
  endTime?: string
  status: number
  page: number
  pageSize: number
}): Promise<OrderPageResult> {
  const res = await http.get<ApiResponse<OrderPageResult>>('/orders', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询订单失败')
  return res.data.data
}

export async function getOrder(id: number): Promise<OrderDetail> {
  const res = await http.get<ApiResponse<OrderDetail>>(`/orders/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询订单详情失败')
  return res.data.data
}

export async function cancelOrder(id: number, cancelReason: string): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/orders/${id}/cancel`, { cancelReason })
  if (res.data.code !== 1) throw new Error(res.data.msg || '取消订单失败')
}

export async function refundOrder(id: number, refundReason: string): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/orders/${id}/refund`, { refundReason })
  if (res.data.code !== 1) throw new Error(res.data.msg || '退款失败')
}
