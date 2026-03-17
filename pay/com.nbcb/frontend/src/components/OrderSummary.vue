<template>
  <div class="order-summary">
    <h1>订单汇总</h1>

    <div class="field">
      <label>商品小计（元）</label>
      <input
        type="number"
        v-model.number="subtotal"
        min="0"
        step="0.01"
      />
    </div>

    <div class="field">
      <label>税率（例如 0.1 表示 10%）</label>
      <input
        type="number"
        v-model.number="taxRate"
        min="0"
        step="0.01"
      />
    </div>

    <div class="discount-section">
      <label>折扣码</label>
      <div class="discount-input">
        <input
          type="text"
          v-model="discountCode"
          placeholder="请输入折扣码"
        />
        <button @click="onValidateDiscount" :disabled="validating">
          {{ validating ? '验证中...' : '验证' }}
        </button>
      </div>
      <p v-if="discountMessage" :class="{ error: !discountValid, success: discountValid }">
        {{ discountMessage }}
      </p>
    </div>

    <div class="summary">
      <p>商品小计：{{ pricing.subtotal.toFixed(2) }} 元</p>
      <p>折扣金额：-{{ pricing.discountAmount.toFixed(2) }} 元</p>
      <p>计税基础：{{ pricing.taxableAmount.toFixed(2) }} 元</p>
      <p>税额：{{ pricing.taxAmount.toFixed(2) }} 元</p>
      <p class="total">应付总额：{{ pricing.totalAmount.toFixed(2) }} 元</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { validateDiscount } from '../api/discountApi';

const subtotal = ref(100);
const taxRate = ref(0.1);
const discountCode = ref('');
const validating = ref(false);
const discountValid = ref(false);
const discountMessage = ref('');

const pricing = reactive({
  subtotal: 100,
  discountAmount: 0,
  taxableAmount: 100,
  taxAmount: 10,
  totalAmount: 110
});

async function onValidateDiscount() {
  validating.value = true;
  try {
    const data = await validateDiscount({
      code: discountCode.value,
      orderAmount: subtotal.value,
      taxRate: taxRate.value
    });
    discountValid.value = data.valid;
    discountMessage.value = data.message;

    pricing.subtotal = data.subtotal;
    pricing.discountAmount = data.taxableAmount - data.subtotal + data.taxAmount - data.taxAmount + data.discountAmount;
    // 上面一行只是保证类型正确，这里直接用后端返回的字段更清晰：
    pricing.discountAmount = data.discountAmount;
    pricing.taxableAmount = data.taxableAmount;
    pricing.taxAmount = data.taxAmount;
    pricing.totalAmount = data.totalAmount;
  } catch (e) {
    discountValid.value = false;
    discountMessage.value = '折扣码验证失败，请稍后重试';
  } finally {
    validating.value = false;
  }
}

watch([subtotal, taxRate], () => {
  discountValid.value = false;
  discountMessage.value = '';
});
</script>

<style scoped>
.order-summary {
  max-width: 480px;
  margin: 40px auto;
  padding: 24px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.field {
  margin-bottom: 16px;
}

label {
  display: block;
  margin-bottom: 4px;
  font-weight: 600;
}

input[type='number'],
input[type='text'] {
  width: 100%;
  padding: 8px 10px;
  border-radius: 4px;
  border: 1px solid #d1d5db;
  font-size: 14px;
}

.discount-section {
  margin-top: 16px;
  margin-bottom: 16px;
}

.discount-input {
  display: flex;
  gap: 8px;
}

button {
  padding: 8px 14px;
  border-radius: 4px;
  border: none;
  background-color: #2563eb;
  color: #ffffff;
  font-size: 14px;
  cursor: pointer;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.summary {
  margin-top: 16px;
  border-top: 1px solid #e5e7eb;
  padding-top: 12px;
}

.summary p {
  margin: 4px 0;
}

.summary .total {
  margin-top: 8px;
  font-size: 16px;
  font-weight: 700;
}

.error {
  color: #dc2626;
}

.success {
  color: #16a34a;
}
</style>

