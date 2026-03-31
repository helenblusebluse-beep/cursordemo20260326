<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useLayoutStore } from '../stores/layout'
import { getBill } from '../api/bill'
import type { BillDetail } from '../types/bill'

const route = useRoute()
const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const detail = ref<BillDetail | null>(null)

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

const hasRefund = computed(() => !!detail.value?.refundApplicant)
const hasCancel = computed(() => !!detail.value?.cancelBy)
const statusColor = computed(() => {
  const s = detail.value?.tradeStatusLabel || ''
  if (s.includes('已支付')) return '#67c23a'
  if (s.includes('关闭')) return '#909399'
  return '#e6a23c'
})

async function loadData(): Promise<void> {
  loading.value = true
  try {
    detail.value = await getBill(Number(route.params.id))
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载账单详情失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => { void loadData() })
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="8-1" class="menu" @select="onMenuSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-sub-menu index="2"><template #title>来访管理</template><el-menu-item index="2-1">预约登记</el-menu-item><el-menu-item index="2-2">来访登记</el-menu-item></el-sub-menu>
        <el-sub-menu index="3"><template #title>入退管理</template><el-menu-item index="3-1">健康评估</el-menu-item><el-menu-item index="3-2">入住办理</el-menu-item><el-menu-item index="3-3">退住办理</el-menu-item></el-sub-menu>
        <el-sub-menu index="4"><template #title>在住管理</template><el-menu-item index="4-1">房型设置</el-menu-item><el-menu-item index="4-2">床位预览</el-menu-item><el-menu-item index="4-3">智能床位</el-menu-item><el-menu-item index="4-4">合同跟踪</el-menu-item><el-menu-item index="4-5">客户管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="6"><template #title>服务管理</template><el-menu-item index="6-1">护理项目</el-menu-item><el-menu-item index="6-2">护理计划</el-menu-item><el-menu-item index="6-3">护理等级</el-menu-item><el-menu-item index="6-4">负责老人</el-menu-item><el-menu-item index="6-5">任务安排</el-menu-item></el-sub-menu>
        <el-sub-menu index="7"><template #title>订单查询</template><el-menu-item index="7-1">订单管理</el-menu-item><el-menu-item index="7-2">退款管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="8"><template #title>财务统计</template><el-menu-item index="8-1">账单管理</el-menu-item><el-menu-item index="8-2">预缴款充值</el-menu-item><el-menu-item index="8-3">余额查询</el-menu-item><el-menu-item index="8-4">欠费老人</el-menu-item></el-sub-menu>
        <el-sub-menu index="9"><template #title>智能检测</template><el-menu-item index="9-1">设备管理</el-menu-item><el-menu-item index="9-2">报警规则</el-menu-item><el-menu-item index="9-3">报警数据</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header"><el-button text @click="layoutStore.toggleCollapsed">☰</el-button><div class="crumb">页面一 -> 页面二 -> 页面三</div><div class="admin">管理员</div></el-header>
      <el-main class="main" v-loading="loading">
        <el-card class="block-card" v-if="detail">
          <template #header><div class="title">账单信息</div></template>
          <div class="info-grid">
            <div class="row"><span class="k">账单编号</span><span class="v">{{ detail.billNo }} <el-button size="small" link type="primary">{{ detail.billType }}</el-button></span></div>
            <div class="row"><span class="k">账单月份</span><span class="v">{{ detail.billMonth }}</span></div>
            <div class="row"><span class="k">老人姓名</span><span class="v">{{ detail.elderName }}</span></div>
            <div class="row"><span class="k">老人身份证号</span><span class="v">{{ detail.elderIdNo }}</span></div>
            <div class="row"><span class="k">账单金额（元）</span><span class="v">{{ detail.billTotalAmount?.toFixed(2) }}</span></div>
            <div class="row"><span class="k">应付金额（元）</span><span class="v">{{ detail.payableAmount?.toFixed(2) }}</span></div>
            <div class="row"><span class="k">账单周期</span><span class="v">{{ detail.billPeriod }}</span></div>
            <div class="row"><span class="k">共计天数（天）</span><span class="v">{{ detail.totalDays }}</span></div>
            <div class="row"><span class="k">交易状态</span><span class="v" :style="{ color: statusColor }">{{ detail.tradeStatusLabel }}</span></div>
            <div class="row"><span class="k">支付截止时间</span><span class="v">{{ detail.payDeadline }}</span></div>
            <div class="row"><span class="k">创建人</span><span class="v">{{ detail.createdBy }}</span></div>
            <div class="row"><span class="k">创建时间</span><span class="v">{{ detail.createdTime }}</span></div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="detail">
          <template #header><div class="title">账单明细</div></template>
          <el-table :data="detail.items" border>
            <el-table-column prop="type" label="类型" min-width="120" />
            <el-table-column prop="feeItemName" label="费用项目" min-width="180" />
            <el-table-column prop="serviceContent" label="服务内容" min-width="180" />
            <el-table-column label="金额（元）" min-width="140">
              <template #default="{ row }">{{ Number(row.amount || 0).toFixed(2) }}</template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card class="block-card" v-if="detail && hasRefund">
          <template #header><div class="title">退款记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">申请人</span><span class="v">{{ detail.refundApplicant }} <span class="tag">{{ detail.refundApplicantType }}</span></span></div>
            <div class="row"><span class="k">提交时间</span><span class="v">{{ detail.refundSubmitTime }}</span></div>
            <div class="row"><span class="k">退款方式</span><span class="v">{{ detail.refundMethod }}</span></div>
            <div class="row"><span class="k">实际天数</span><span class="v">{{ detail.refundActualDays }}天</span></div>
            <div class="row"><span class="k">退款天数</span><span class="v">{{ detail.refundDays }}天</span></div>
            <div class="row"><span class="k">实退金额</span><span class="v">{{ detail.refundAmount?.toFixed(2) }} 元</span></div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="detail && hasCancel">
          <template #header><div class="title">取消记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">取消人</span><span class="v">{{ detail.cancelBy }} <span class="tag">{{ detail.cancelUserType }}</span></span></div>
            <div class="row"><span class="k">取消时间</span><span class="v">{{ detail.cancelTime }}</span></div>
            <div class="row row-span"><span class="k">取消原因</span><span class="v">{{ detail.cancelReason }}</span></div>
          </div>
        </el-card>

        <div class="footer-btn"><el-button type="primary" @click="router.back()">返回</el-button></div>
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
.main { padding: 12px; }
.block-card { margin-bottom: 12px; }
.title { font-weight: 600; }
.info-grid, .record-grid { display: grid; grid-template-columns: repeat(2, minmax(220px, 1fr)); row-gap: 12px; column-gap: 24px; }
.row { display: flex; }
.row-span { grid-column: 1 / span 2; }
.k { width: 120px; color: #909399; }
.v { color: #303133; }
.tag { margin-left: 8px; background: #fdf0dd; color: #f2994a; border-radius: 2px; padding: 1px 6px; font-size: 12px; }
.footer-btn { display: flex; justify-content: center; margin-top: 20px; }
</style>
