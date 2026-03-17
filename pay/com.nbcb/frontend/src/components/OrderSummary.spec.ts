import { describe, it, expect, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import OrderSummary from './OrderSummary.vue';
import * as discountApi from '../api/discountApi';

describe('OrderSummary', () => {
  it('renders and validates discount code', async () => {
    const spy = vi.spyOn(discountApi, 'validateDiscount').mockResolvedValue({
      valid: true,
      message: 'ok',
      discountAmount: 10,
      finalAmount: 90,
      subtotal: 100,
      taxableAmount: 90,
      taxAmount: 9,
      totalAmount: 99
    });

    const wrapper = mount(OrderSummary);

    await wrapper.get('input[placeholder="请输入折扣码"]').setValue('CODE');
    await wrapper.get('button').trigger('click');

    expect(spy).toHaveBeenCalled();
    expect(wrapper.text()).toContain('应付总额：99.00 元');
  });
});

