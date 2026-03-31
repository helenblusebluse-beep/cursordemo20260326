export interface BillItem {
  type: string
  feeItemName: string
  serviceContent: string
  amount: number
}

export interface BillDetail {
  id: number
  billNo: string
  billType: string
  billMonth: string
  elderName: string
  elderIdNo: string
  relatedOrderNo: string
  billTotalAmount: number
  payableAmount: number
  prepayDeductAmount: number
  billPeriod: string
  totalDays: number
  payDeadline: string
  tradeStatusLabel: string
  createdBy: string
  createdTime: string
  items: BillItem[]
  refundApplicant: string
  refundApplicantType: string
  refundSubmitTime: string
  refundMethod: string
  refundActualDays: number
  refundDays: number
  refundAmount: number
  cancelBy: string
  cancelUserType: string
  cancelTime: string
  cancelReason: string
}
