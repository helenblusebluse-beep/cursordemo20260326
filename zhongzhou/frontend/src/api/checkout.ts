import axios from 'axios'
import type { ApiResponse } from '../types/dashboard'
import type { CheckoutCandidate, CheckoutDetail, CheckoutPageResult } from '../types/checkout'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

export async function queryCheckouts(params: {
  elderName?: string
  idCard?: string
  page: number
  pageSize: number
}): Promise<CheckoutPageResult> {
  const res = await http.get<ApiResponse<CheckoutPageResult>>('/checkouts', { params })
  if (res.data.code !== 1) throw new Error(res.data.msg || '查询退住办理失败')
  return res.data.data
}

export async function applyCheckout(payload: {
  checkinId: number
  checkoutDate: string
  checkoutReason: string
  agreementDate: string
  agreementFileName: string
}): Promise<number> {
  const res = await http.post<ApiResponse<number>>('/checkouts/apply', payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '发起退住申请失败')
  return res.data.data
}

export async function queryCheckoutCandidates(): Promise<CheckoutCandidate[]> {
  const res = await http.get<ApiResponse<CheckoutCandidate[]>>('/checkouts/candidates')
  if (res.data.code !== 1) throw new Error(res.data.msg || '获取老人列表失败')
  return res.data.data
}

export async function getCheckoutDetail(id: number): Promise<CheckoutDetail> {
  const res = await http.get<ApiResponse<CheckoutDetail>>(`/checkouts/${id}`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '获取退住详情失败')
  return res.data.data
}

export async function uploadCheckoutVoucher(
  id: number,
  payload: { refundMethod: string; refundVoucherName: string; refundRemark: string; refundVoucherUrl?: string; voucherSubmitter?: string },
): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/checkouts/${id}/voucher`, payload)
  if (res.data.code !== 1) throw new Error(res.data.msg || '上传退款凭证失败')
}

export async function cancelUnpaidBill(id: number, feeId: number): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/checkouts/${id}/unpaid/${feeId}/cancel`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '取消未缴账单失败')
}

export async function submitCheckout(id: number): Promise<void> {
  const res = await http.post<ApiResponse<null>>(`/checkouts/${id}/submit`)
  if (res.data.code !== 1) throw new Error(res.data.msg || '提交退住申请失败')
}
