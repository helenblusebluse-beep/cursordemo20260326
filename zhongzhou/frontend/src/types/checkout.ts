export type CheckoutItem = {
  id: number
  checkinId: number
  elderName: string
  idCard: string
  checkoutDate: string
  createdTime: string
}

export type CheckoutPageResult = {
  total: number
  rows: CheckoutItem[]
}

export type CheckoutCandidate = {
  checkinId: number
  elderName: string
  idCard: string
}

export type CheckoutFeeItem = {
  id: number
  billNo: string
  billMonth: string
  itemName: string
  amount: number
  actualDays: number
  refundDays: number
  status: number
}

export type CheckoutDetail = {
  id: number
  checkinId: number
  elderName: string
  idCard: string
  roomNo: string
  careLevel: string
  checkinPeriod: string
  feePeriod: string
  contactPhone: string
  homeAddress?: string
  caregiverNames?: string
  checkoutDate: string
  checkoutReason: string
  agreementDate: string
  agreementFileName: string
  refundMethod: string
  refundVoucherName: string
  refundVoucherUrl?: string
  refundRemark: string
  voucherSubmitter?: string
  voucherSubmitTime?: string
  shouldRefundSubtotal: number
  debtSubtotal: number
  balanceSubtotal: number
  unpaidSubtotal: number
  finalRefundAmount: number
  status: number
  shouldRefundItems: CheckoutFeeItem[]
  debtItems: CheckoutFeeItem[]
  balanceItems: CheckoutFeeItem[]
  unpaidItems: CheckoutFeeItem[]
}
