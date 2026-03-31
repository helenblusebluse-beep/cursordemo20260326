export interface OrderItem {
  id: number
  orderNo: string
  elderName: string
  bedNo: string
  nursingItemName: string
  orderAmount: number
  expectedServiceTime: string
  orderUserName: string
  orderTime: string
  status: number
  statusLabel: string
  refundStatus: number
  refundStatusLabel: string
}

export interface OrderDetail extends OrderItem {
  nursingItemType: string
  orderUserMobile: string
  remark: string
  payTime: string
  executeTime: string
  finishTime: string
  closeTime: string
  closeType: string
  cancelBy: string
  cancelUserType: string
  cancelReason: string
  cancelTime: string
  tradeStatus: string
  payChannel: string
  payMethod: string
  wxOrderNo: string
  payAmountText: string
  executeBy: string
  executeImageUrl: string
  executeRecord: string
  refundStatusText: string
  refundApplicant: string
  refundApplicantType: string
  refundApplyTime: string
  refundChannel: string
  refundMethod: string
  refundNo: string
  refundAmountText: string
  refundFailCode: string
  refundFailReason: string
  refundReason: string
  refundTime: string
}

export interface OrderPageResult {
  total: number
  rows: OrderItem[]
}
