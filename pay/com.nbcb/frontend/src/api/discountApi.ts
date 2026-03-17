import axios from 'axios';

export interface DiscountValidateRequest {
  code: string;
  orderAmount: number;
  taxRate?: number;
}

export interface DiscountValidateResponse {
  valid: boolean;
  message: string;
  discountAmount: number;
  finalAmount: number;
  subtotal: number;
  taxableAmount: number;
  taxAmount: number;
  totalAmount: number;
}

export async function validateDiscount(data: DiscountValidateRequest) {
  const resp = await axios.post<DiscountValidateResponse>('/api/discount/validate', data);
  return resp.data;
}

