<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { applyCheckout, cancelUnpaidBill, getCheckoutDetail, queryCheckoutCandidates, submitCheckout, uploadCheckoutVoucher } from '../api/checkout'
import type { CheckoutCandidate, CheckoutDetail } from '../types/checkout'
import { useLayoutStore } from '../stores/layout'

const router = useRouter()
const route = useRoute()
const layoutStore = useLayoutStore()
const readonly = computed(() => route.path !== '/checkouts/apply')
const activeMenu = computed(() => '3-3')
const loading = ref(false)
const submitting = ref(false)
const voucherVisible = ref(false)
const candidates = ref<CheckoutCandidate[]>([])
const detail = ref<CheckoutDetail | null>(null)

const form = reactive({
  checkinId: 0,
  elderName: '',
  checkoutDate: '',
  checkoutReason: '',
  agreementDate: '',
  agreementFileName: '',
  idCard: '',
  checkinPeriod: '',
  feePeriod: '',
  roomNo: '',
  contactPhone: '',
  careLevel: '',
  homeAddress: '',
  caregiverNames: '',
})
const voucherForm = reactive({
  refundMethod: '',
  refundVoucherName: '',
  refundRemark: '',
})

const reasonOptions = ['老人个人原因', '护理质量不高', '服务不高', '服务不周', '管理混乱', '费用高昂', '违规退住', '其他']
const finalRefund = computed(() => detail.value?.finalRefundAmount ?? 0)
const isCompletedCheckout = computed(() => readonly.value && detail.value?.status === 2)
const voucherPreviewUrl = computed(() => {
  const u = detail.value?.refundVoucherUrl
  if (u && /^https?:\/\//i.test(u)) return u
  return 'https://picsum.photos/seed/voucher1/240/160'
})

function formatPeriodRange(s: string): string {
  if (!s) return ''
  const parts = s.split('~')
  if (parts.length >= 2) return `${parts[0].trim()} —— ${parts[1].trim()}`
  return s
}
function displayRoomBed(room: string): string {
  if (!room) return ''
  return room.includes('床位') ? room : `${room}床位`
}
function displayDateOnly(s: string): string {
  if (!s) return ''
  return s.slice(0, 10)
}
function onMenuSelect(index: string): void {
  if (index === '1') return void router.push('/')
  if (index === '2-1') return void router.push('/appointments/register')
  if (index === '2-2') return void router.push('/visits/register')
  if (index === '3-1') return void router.push('/health-assessments')
  if (index === '3-2') return void router.push('/checkins')
  if (index === '3-3') return void router.push('/checkouts')
  if (index === '4-1') return void router.push('/room-types')
  if (index === '4-2') return void router.push('/bed-preview')
  if (index === '4-3') return void router.push('/smart-beds')
  if (index === '4-4') return void router.push('/contracts')
  if (index === '4-5') return void router.push('/customers')
  if (index === '6-1') return void router.push('/nursing-items')
  if (index === '6-2') return void router.push('/nursing-plans')
  if (index === '6-3') return void router.push('/nursing-care-levels')
  if (index === '6-4') return void router.push('/responsible-elders')
  if (index === '6-5') return void router.push('/task-schedules')
  if (index === '7-1') return void router.push('/orders')
  if (index === '7-2') return void router.push('/refunds')
  if (index === '8-1') return void router.push('/bills/1')
  if (index === '8-2') return void router.push('/prepay-recharges')
  if (index === '8-3') return void router.push('/balance-queries')
  if (index === '8-4') return void router.push('/arrears-elders')
  if (index === '9-1') return void router.push('/iot/devices')
  if (index === '9-2') return void router.push('/iot/alarm-rules')
  if (index === '9-3') return void router.push('/iot/alarm-records')
}

function onCandidateChange(id: number): void {
  const m = candidates.value.find((x) => x.checkinId === id)
  form.elderName = m?.elderName ?? ''
  form.idCard = m?.idCard ?? ''
}

async function onApply(): Promise<void> {
  if (!form.checkinId || !form.checkoutDate || !form.checkoutReason || !form.agreementDate || !form.agreementFileName) {
    ElMessage.warning('请完整填写必填项')
    return
  }
  submitting.value = true
  try {
    const id = await applyCheckout({
      checkinId: form.checkinId,
      checkoutDate: `${form.checkoutDate} 00:00:00`,
      checkoutReason: form.checkoutReason,
      agreementDate: `${form.agreementDate} 00:00:00`,
      agreementFileName: form.agreementFileName,
    })
    ElMessage.success('发起退住申请成功')
    void router.push(`/checkouts/${id}`)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '发起失败')
  } finally {
    submitting.value = false
  }
}

async function loadDetail(id: number): Promise<void> {
  loading.value = true
  try {
    const d = await getCheckoutDetail(id)
    detail.value = d
    form.checkinId = d.checkinId
    form.elderName = d.elderName
    form.checkoutReason = d.checkoutReason
    form.agreementFileName = d.agreementFileName
    form.idCard = d.idCard
    form.checkinPeriod = d.checkinPeriod
    form.feePeriod = d.feePeriod
    form.roomNo = d.roomNo
    form.contactPhone = d.contactPhone
    form.careLevel = d.careLevel
    form.homeAddress = d.homeAddress ?? ''
    form.caregiverNames = d.caregiverNames ?? ''
    form.checkoutDate = (d.checkoutDate || '').replace(/\s.*/, '').slice(0, 10)
    form.agreementDate = (d.agreementDate || '').slice(0, 10)
  } finally {
    loading.value = false
  }
}

async function loadCandidates(): Promise<void> {
  candidates.value = await queryCheckoutCandidates()
}

async function onCancelUnpaid(feeId: number): Promise<void> {
  if (!detail.value) return
  await cancelUnpaidBill(detail.value.id, feeId)
  await loadDetail(detail.value.id)
}

async function onVoucherConfirm(): Promise<void> {
  if (!detail.value) return
  if (!voucherForm.refundMethod || !voucherForm.refundVoucherName || !voucherForm.refundRemark) {
    ElMessage.warning('请完整填写退款凭证')
    return
  }
  await uploadCheckoutVoucher(detail.value.id, voucherForm)
  voucherVisible.value = false
  ElMessage.success('上传成功')
  await loadDetail(detail.value.id)
}

async function onSubmit(): Promise<void> {
  if (!detail.value) return
  try {
    await submitCheckout(detail.value.id)
    ElMessage.success('提交成功')
    void router.push('/checkouts')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '提交失败')
  }
}

onMounted(async () => {
  if (readonly.value) {
    await loadDetail(Number(route.params.id))
  } else {
    await loadCandidates()
  }
})
</script>

<template>
  <el-container class="page-root" v-loading="loading">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" :default-active="activeMenu" class="menu" @select="onMenuSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-sub-menu index="2">
          <template #title>来访管理</template>
          <el-menu-item index="2-1">预约登记</el-menu-item>
          <el-menu-item index="2-2">来访登记</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>入退管理</template>
          <el-menu-item index="3-1">健康评估</el-menu-item>
          <el-menu-item index="3-2">入住办理</el-menu-item>
          <el-menu-item index="3-3">退住办理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>在住管理</template>
          <el-menu-item index="4-1">房型设置</el-menu-item>
          <el-menu-item index="4-2">床位预览</el-menu-item>
          <el-menu-item index="4-3">智能床位</el-menu-item>
          <el-menu-item index="4-4">合同跟踪</el-menu-item>
          <el-menu-item index="4-5">客户管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="6"><template #title>服务管理</template><el-menu-item index="6-1">护理项目</el-menu-item><el-menu-item index="6-2">护理计划</el-menu-item><el-menu-item index="6-3">护理等级</el-menu-item><el-menu-item index="6-4">负责老人</el-menu-item><el-menu-item index="6-5">任务安排</el-menu-item></el-sub-menu>
        <el-sub-menu index="7"><template #title>订单查询</template><el-menu-item index="7-1">订单管理</el-menu-item><el-menu-item index="7-2">退款管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="8"><template #title>财务统计</template><el-menu-item index="8-1">账单管理</el-menu-item><el-menu-item index="8-2">预缴款充值</el-menu-item><el-menu-item index="8-3">余额查询</el-menu-item><el-menu-item index="8-4">欠费老人</el-menu-item></el-sub-menu>
        <el-sub-menu index="9"><template #title>智能检测</template><el-menu-item index="9-1">设备管理</el-menu-item><el-menu-item index="9-2">报警规则</el-menu-item><el-menu-item index="9-3">报警数据</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-button text @click="layoutStore.toggleCollapsed">☰</el-button>
        <div class="crumb">页面一 / 页面二 / 页面三</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="page">
    <!-- 发起退住申请 -->
    <template v-if="!readonly">
      <el-card class="block">
        <template #header>基本信息</template>
        <div class="grid2">
          <el-form-item label="*老人姓名">
            <el-select v-model="form.checkinId" placeholder="请选择" @change="onCandidateChange">
              <el-option v-for="item in candidates" :key="item.checkinId" :label="item.elderName" :value="item.checkinId" />
            </el-select>
          </el-form-item>
          <el-form-item label="*退住日期"><el-date-picker v-model="form.checkoutDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="*退住原因">
            <el-select v-model="form.checkoutReason" placeholder="请选择">
              <el-option v-for="it in reasonOptions" :key="it" :label="it" :value="it" />
            </el-select>
          </el-form-item>
          <el-form-item label="老人身份证号"><el-input v-model="form.idCard" disabled /></el-form-item>
          <el-form-item label="入住期限"><el-input v-model="form.checkinPeriod" disabled /></el-form-item>
          <el-form-item label="费用期限"><el-input v-model="form.feePeriod" disabled /></el-form-item>
          <el-form-item label="护理等级"><el-input v-model="form.careLevel" disabled /></el-form-item>
          <el-form-item label="入住床位"><el-input v-model="form.roomNo" disabled /></el-form-item>
          <el-form-item label="护理员姓名"><el-input disabled placeholder="护理员姓名" /></el-form-item>
          <el-form-item label="联系方式"><el-input v-model="form.contactPhone" disabled /></el-form-item>
        </div>
      </el-card>
      <el-card class="block">
        <template #header>解除协议</template>
        <div class="grid2">
          <el-form-item label="*解除日期"><el-date-picker v-model="form.agreementDate" type="date" value-format="YYYY-MM-DD" /></el-form-item>
          <el-form-item label="*解除协议"><el-input v-model="form.agreementFileName" placeholder="上传pdf文件" /></el-form-item>
        </div>
      </el-card>
      <div class="actions">
        <el-button @click="router.push('/checkouts')">返回</el-button>
        <el-button type="primary" :loading="submitting" @click="onApply">提交</el-button>
      </div>
    </template>

    <!-- 已退住：只读回显（竖向布局） -->
    <template v-else-if="detail && isCompletedCheckout">
      <el-card class="block">
        <template #header>基本信息</template>
        <div class="done-grid">
          <div class="done-col">
            <div class="kv"><span class="k">老人姓名</span><span class="v">{{ form.elderName }}</span></div>
            <div class="kv"><span class="k">退住原因</span><span class="v">{{ form.checkoutReason }}</span></div>
            <div class="kv"><span class="k">入住期限</span><span class="v">{{ formatPeriodRange(form.checkinPeriod) }}</span></div>
            <div class="kv"><span class="k">护理等级</span><span class="v">{{ form.careLevel }}</span></div>
            <div class="kv"><span class="k">护理员姓名</span><span class="v">{{ form.caregiverNames || '—' }}</span></div>
            <div class="kv"><span class="k">家庭住址</span><span class="v">{{ form.homeAddress || '—' }}</span></div>
          </div>
          <div class="done-col">
            <div class="kv"><span class="k">退住日期</span><span class="v">{{ displayDateOnly(form.checkoutDate) }}</span></div>
            <div class="kv"><span class="k">老人身份证号</span><span class="v">{{ form.idCard }}</span></div>
            <div class="kv"><span class="k">费用期限</span><span class="v">{{ formatPeriodRange(form.feePeriod) }}</span></div>
            <div class="kv"><span class="k">入住床位</span><span class="v">{{ displayRoomBed(form.roomNo) }}</span></div>
            <div class="kv"><span class="k">联系方式</span><span class="v">{{ form.contactPhone }}</span></div>
          </div>
        </div>
      </el-card>

      <el-card class="block">
        <template #header>解除协议</template>
        <div class="done-grid single">
          <div class="kv"><span class="k">解除日期</span><span class="v">{{ displayDateOnly(form.agreementDate) }}</span></div>
          <div class="kv align-start">
            <span class="k">解除协议</span>
            <span class="v file-line">
              {{ form.agreementFileName }}
              <el-link type="primary" :underline="false" href="#" @click.prevent>查看</el-link>
            </span>
          </div>
        </div>
      </el-card>

      <el-card class="block">
        <template #header>费用清算</template>
        <div class="fees-grid">
          <el-card class="fee-col" shadow="never">
            <template #header>
              <div class="fee-card-title-row">
                <span class="fee-card-name">应退</span>
                <span class="fee-card-meta">待办：{{ detail.shouldRefundItems.length }}&nbsp;&nbsp;小计：{{ detail.shouldRefundSubtotal.toFixed(2) }}元</span>
              </div>
            </template>
            <div v-for="x in detail.shouldRefundItems" :key="x.id" class="fee-item">
              <div class="fee-item-left">
                <div class="fee-label">账单编号：{{ x.billNo }}</div>
                <div class="fee-tag-row">
                  <el-tag type="primary" effect="plain" class="fee-tag">{{ x.itemName }}</el-tag>
                </div>
                <div class="fee-label">账单月份：{{ x.billMonth }}</div>
                <div class="fee-label">实际天数：{{ x.actualDays }}天</div>
                <div class="fee-label">退款天数：{{ x.refundDays }}天</div>
              </div>
              <div class="fee-item-right">
                <div class="fee-amount">可退金额：{{ x.amount.toFixed(2) }}元</div>
              </div>
            </div>
          </el-card>

          <el-card class="fee-col" shadow="never">
            <template #header>
              <div class="fee-card-title-row">
                <span class="fee-card-name">欠费</span>
                <span class="fee-card-meta">待办：{{ detail.debtItems.length }}&nbsp;&nbsp;小计：{{ detail.debtSubtotal.toFixed(2) }}元</span>
              </div>
            </template>
            <div v-for="x in detail.debtItems" :key="x.id" class="fee-item">
              <div class="fee-item-left">
                <div class="fee-label">账单编号：{{ x.billNo }}</div>
                <div class="fee-tag-row">
                  <el-tag type="primary" effect="plain" class="fee-tag">{{ x.itemName }}</el-tag>
                </div>
                <div class="fee-label">账单月份：{{ x.billMonth }}</div>
              </div>
              <div class="fee-item-right">
                <div class="fee-amount">应付金额：{{ x.amount.toFixed(2) }}元</div>
              </div>
            </div>
          </el-card>
        </div>

        <div class="balance-dashed">
          <div class="balance-head">
            <span class="balance-title">余额</span>
            <span class="balance-meta">小计：{{ detail.balanceSubtotal.toFixed(2) }}元</span>
          </div>
          <div class="balance-box">押金金额：{{ (detail.balanceItems[0]?.amount ?? 0).toFixed(2) }}元</div>
          <div class="balance-box">预缴款金额：{{ (detail.balanceItems[1]?.amount ?? 0).toFixed(2) }}元</div>
        </div>
      </el-card>

      <el-card class="block">
        <template #header><span class="voucher-section-title">退款凭证</span></template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="提交人">{{ detail.voucherSubmitter || '—' }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ detail.voucherSubmitTime || '—' }}</el-descriptions-item>
          <el-descriptions-item label="退款方式">{{ detail.refundMethod || '—' }}</el-descriptions-item>
          <el-descriptions-item label="退款凭证">
            <el-image
              class="voucher-thumb"
              :src="voucherPreviewUrl"
              :preview-src-list="[voucherPreviewUrl]"
              fit="cover"
            />
            <div class="voucher-name">{{ detail.refundVoucherName }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="退款备注" :span="2">{{ detail.refundRemark || '—' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <div class="done-footer">
        <el-button type="primary" @click="router.push('/checkouts')">返回</el-button>
      </div>
    </template>

    <!-- 草稿退住详情：可继续提交 -->
    <template v-else-if="detail">
    <el-card class="block">
      <template #header>基本信息</template>
      <div class="grid2">
        <el-form-item label="*老人姓名">
          <el-input v-model="form.elderName" disabled />
        </el-form-item>
        <el-form-item label="*退住日期"><el-date-picker v-model="form.checkoutDate" type="date" value-format="YYYY-MM-DD" :disabled="readonly" /></el-form-item>
        <el-form-item label="*退住原因">
          <el-select v-model="form.checkoutReason" :disabled="readonly" placeholder="请选择">
            <el-option v-for="it in reasonOptions" :key="it" :label="it" :value="it" />
          </el-select>
        </el-form-item>
        <el-form-item label="老人身份证号"><el-input v-model="form.idCard" disabled /></el-form-item>
        <el-form-item label="入住期限"><el-input v-model="form.checkinPeriod" disabled /></el-form-item>
        <el-form-item label="费用期限"><el-input v-model="form.feePeriod" disabled /></el-form-item>
        <el-form-item label="护理等级"><el-input v-model="form.careLevel" disabled /></el-form-item>
        <el-form-item label="入住床位"><el-input v-model="form.roomNo" disabled /></el-form-item>
        <el-form-item label="护理员姓名"><el-input v-model="form.caregiverNames" disabled placeholder="护理员姓名" /></el-form-item>
        <el-form-item label="联系方式"><el-input v-model="form.contactPhone" disabled /></el-form-item>
        <el-form-item label="家庭住址"><el-input v-model="form.homeAddress" disabled /></el-form-item>
      </div>
    </el-card>

    <el-card class="block">
      <template #header>解除协议</template>
      <div class="grid2">
        <el-form-item label="*解除日期"><el-date-picker v-model="form.agreementDate" type="date" value-format="YYYY-MM-DD" :disabled="readonly" /></el-form-item>
        <el-form-item label="*解除协议"><el-input v-model="form.agreementFileName" placeholder="上传pdf文件" :disabled="readonly" /></el-form-item>
      </div>
    </el-card>

    <el-card class="block">
      <template #header>费用清算</template>

      <div class="fees-grid">
        <el-card class="fee-col" shadow="never">
          <template #header>
            <div class="fee-card-title">
              <span class="fee-card-name">应退</span>
              <span class="fee-card-meta">待办：{{ detail.shouldRefundItems.length }}&nbsp;&nbsp; 小计：{{ detail.shouldRefundSubtotal.toFixed(2) }}元</span>
            </div>
          </template>
          <div v-for="x in detail.shouldRefundItems" :key="x.id" class="fee-item">
            <div class="fee-item-left">
              <div class="fee-label">账单编号：{{ x.billNo }}</div>
              <div class="fee-tag-row">
                <el-tag type="info" effect="plain" class="fee-tag">{{ x.itemName }}</el-tag>
              </div>
              <div class="fee-label">账单月份：{{ x.billMonth }}</div>
              <div class="fee-label">实际天数：{{ x.actualDays }}天</div>
              <div class="fee-label">退款天数：{{ x.refundDays }}天</div>
            </div>
            <div class="fee-item-right">
              <div class="fee-amount">可退金额：{{ x.amount.toFixed(2) }}元</div>
            </div>
          </div>
        </el-card>

        <el-card class="fee-col" shadow="never">
          <template #header>
            <div class="fee-card-title">
              <span class="fee-card-name">欠费</span>
              <span class="fee-card-meta">待办：{{ detail.debtItems.length }}&nbsp;&nbsp; 小计：{{ detail.debtSubtotal.toFixed(2) }}元</span>
            </div>
          </template>
          <div v-for="x in detail.debtItems" :key="x.id" class="fee-item">
            <div class="fee-item-left">
              <div class="fee-label">账单编号：{{ x.billNo }}</div>
              <div class="fee-tag-row">
                <el-tag type="info" effect="plain" class="fee-tag">{{ x.itemName }}</el-tag>
              </div>
              <div class="fee-label">账单月份：{{ x.billMonth }}</div>
            </div>
            <div class="fee-item-right">
              <div class="fee-amount">应付金额：{{ x.amount.toFixed(2) }}元</div>
            </div>
          </div>
        </el-card>

        <el-card class="fee-col" shadow="never">
          <template #header>
            <div class="fee-card-title">
              <span class="fee-card-name">余额</span>
              <span class="fee-card-meta">小计：{{ detail.balanceSubtotal.toFixed(2) }}元</span>
            </div>
          </template>
          <div class="fee-item balance-summary">
            <div class="fee-item-left">
              <div class="fee-label">押金金额：{{ (detail.balanceItems[0]?.amount ?? 0).toFixed(2) }}元</div>
            </div>
          </div>
          <div class="fee-item balance-summary">
            <div class="fee-item-left">
              <div class="fee-label">预缴款金额：{{ (detail.balanceItems[1]?.amount ?? 0).toFixed(2) }}元</div>
            </div>
            <div class="fee-item-right">
              <el-button size="small" type="primary" plain>上传充值凭证</el-button>
            </div>
          </div>
        </el-card>

        <el-card class="fee-col" shadow="never">
          <template #header>
            <div class="fee-card-title">
              <span class="fee-card-name">未缴</span>
              <span class="fee-card-meta">待办：{{ detail.unpaidItems.length }}&nbsp;&nbsp; 小计：{{ detail.unpaidSubtotal.toFixed(2) }}元</span>
            </div>
          </template>
          <div v-for="x in detail.unpaidItems" :key="x.id" class="fee-item">
            <div class="fee-item-left">
              <div class="fee-label">账单编号：{{ x.billNo }}</div>
              <div class="fee-tag-row">
                <el-tag type="info" effect="plain" class="fee-tag">{{ x.itemName }}</el-tag>
              </div>
            </div>
            <div class="fee-item-right fee-item-right-actions">
              <div class="fee-amount">应付金额：{{ x.amount.toFixed(2) }}元</div>
              <el-button
                v-if="x.status === 1 && detail.status !== 2"
                size="small"
                type="primary"
                @click="onCancelUnpaid(x.id)"
              >
                取消
              </el-button>
              <el-tag v-else-if="x.status !== 1" type="info" effect="plain">已取消</el-tag>
              <el-tag v-else type="info" effect="plain">已处理</el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <div class="final-line">
        最终退款金额 = 应退 - 欠费 + 余额 - 未缴 = {{ finalRefund.toFixed(2) }}元
      </div>

      <div class="upload-row">
        <el-button
          :type="finalRefund < 0 ? 'primary' : 'info'"
          :disabled="finalRefund >= 0"
          @click="voucherVisible = true"
        >
          上传退款凭证
        </el-button>
      </div>

      <div class="fee-bottom-actions">
        <el-button @click="router.push('/checkouts')">返回</el-button>
        <el-button v-if="detail.status !== 2" type="primary" @click="onSubmit">提交</el-button>
      </div>
    </el-card>

    </template>

    <el-dialog v-model="voucherVisible" title="上传退款凭证" width="560px">
      <el-form label-width="90px">
        <el-form-item label="*退款方式">
          <el-select v-model="voucherForm.refundMethod"><el-option label="现金" value="现金" /><el-option label="转账" value="转账" /><el-option label="微信" value="微信" /></el-select>
        </el-form-item>
        <el-form-item label="*退款凭证"><el-input v-model="voucherForm.refundVoucherName" placeholder="上传图片" /></el-form-item>
        <el-form-item label="*退款备注"><el-input v-model="voucherForm.refundRemark" maxlength="50" show-word-limit /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="voucherVisible=false">取消</el-button>
        <el-button type="primary" @click="onVoucherConfirm">确定</el-button>
      </template>
    </el-dialog>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.page-root { min-height: 100vh; background: #f5f7fa; }
.sider { background: #fff; color: #303133; transition: width .2s; border-right: 1px solid #ebeef5; }
.brand { height: 60px; display: flex; align-items: center; gap: 8px; padding: 0 12px; border-bottom: 1px solid #ebeef5; }
.brand img { border-radius: 50%; }
.brand-text { font-weight: 700; }
.menu { border-right: none; background: #fff; color: #303133; }
:deep(.menu .el-menu-item), :deep(.menu .el-sub-menu__title) { color: #303133; }
:deep(.menu .el-menu-item:hover), :deep(.menu .el-sub-menu__title:hover) { background: #f5f7fa; color: #409eff; }
:deep(.menu .el-menu-item.is-active) { background: #ecf5ff; color: #409eff; font-weight: 600; }
.header { height: 56px; background: #fff; display: flex; align-items: center; border-bottom: 1px solid #ebeef5; }
.crumb { margin-left: 12px; color: #606266; flex: 1; }
.admin { color: #909399; font-size: 13px; }
 .page { padding: 12px; background: #f5f7fa; min-height: 100vh; }
 .block { margin-bottom: 12px; }
 .grid2 { display: grid; grid-template-columns: 1fr 1fr; gap: 10px 16px; }
 .fees-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
 .fee-col { min-height: 260px; }
 .fee-card-title { display: flex; align-items: baseline; gap: 12px; }
 .fee-card-name { font-weight: 700; }
 .fee-card-meta { color: #909399; font-size: 13px; }
 .fee-item { padding: 10px 0; border-bottom: 1px dashed #ebeef5; display: flex; justify-content: space-between; gap: 12px; }
 .fee-item:last-child { border-bottom: none; }
 .balance-summary { min-height: 56px; }
 .fee-item-left { flex: 1; min-width: 0; }
 .fee-item-right { width: 210px; display: flex; flex-direction: column; align-items: flex-end; justify-content: center; gap: 8px; }
 .fee-item-right-actions { display: flex; align-items: center; gap: 10px; }
 .fee-label { color: #606266; font-size: 13px; margin: 4px 0; }
 .fee-tag-row { margin: 6px 0; }
 .fee-amount { font-weight: 600; color: #303133; text-align: right; }
 .final-line { margin-top: 10px; text-align: center; font-weight: 700; color: #303133; }
 .upload-row { display: flex; justify-content: center; margin-top: 10px; }
 .fee-bottom-actions { display: flex; justify-content: center; gap: 18px; margin-top: 14px; }
 .actions { display: flex; justify-content: center; gap: 12px; margin-top: 12px; }
.done-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px 32px; }
.done-grid.single { grid-template-columns: 1fr 1fr; }
.done-col { min-width: 0; }
.kv { display: flex; align-items: flex-start; gap: 12px; margin-bottom: 14px; font-size: 14px; color: #303133; }
.kv.align-start { align-items: flex-start; }
.k { color: #909399; min-width: 100px; text-align: right; flex-shrink: 0; }
.v { flex: 1; word-break: break-all; }
.file-line { display: inline-flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.fee-card-title-row { display: flex; justify-content: space-between; align-items: center; width: 100%; gap: 8px; flex-wrap: wrap; }
.balance-dashed { margin-top: 16px; padding: 12px; border: 1px dashed #dcdfe6; border-radius: 8px; background: #fafafa; }
.balance-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.balance-title { font-weight: 700; color: #303133; }
.balance-meta { color: #909399; font-size: 13px; }
.balance-box { background: #fff; border: 1px solid #ebeef5; border-radius: 6px; padding: 12px 14px; margin-bottom: 8px; color: #303133; font-size: 14px; }
.balance-box:last-child { margin-bottom: 0; }
.voucher-section-title { font-weight: 700; font-size: 15px; }
.voucher-thumb { width: 120px; height: 80px; border-radius: 4px; border: 1px solid #ebeef5; }
.voucher-name { margin-top: 6px; font-size: 12px; color: #909399; }
.done-footer { display: flex; justify-content: center; margin: 20px 0 8px; }
</style>
