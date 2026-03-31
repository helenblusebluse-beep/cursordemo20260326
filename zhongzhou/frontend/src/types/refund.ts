export interface RefundItem {
  id: number
  refundNo: string
  orderNo: string
  orderAmount: number
  applicant: string
  applyTime: string
  refundTime: string
  orderStatusLabel: string
  refundStatusLabel: string
}

export interface RefundDetail {
  refundNo: string
  orderNo: string
  orderStatusLabel: string
  refundStatusLabel: string
  applicant: string
  applicantType: string
  applyTime: string
  refundReason: string
  refundChannel: string
  refundMethod: string
  refundTime: string
  refundAmount: number
  failCode: string
  failReason: string
}

export interface RefundPageResult {
  total: number
  rows: RefundItem[]
}
