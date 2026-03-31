<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrder } from '../api/order'
import type { OrderDetail } from '../types/order'
import { useLayoutStore } from '../stores/layout'

const route = useRoute()
const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const detail = ref<OrderDetail | null>(null)

async function loadData(): Promise<void> {
  loading.value = true
  try {
    detail.value = await getOrder(Number(route.params.id))
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally { loading.value = false }
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

const progressNodes = computed(() => {
  const d = detail.value
  if (!d) return []
  if (d.status === 5) {
    return [
      { title: '已下单', time: d.orderTime, done: true },
      { title: '已关闭', time: d.closeTime || d.cancelTime, done: true },
    ]
  }
  if (d.status === 6) {
    if (d.executeTime) {
      return [
        { title: '已下单', time: d.orderTime, done: true },
        { title: '已支付', time: d.payTime, done: true },
        { title: '已执行', time: d.executeTime, done: true },
        { title: '已退款', time: d.refundTime || d.refundApplyTime, done: true },
      ]
    }
    return [
      { title: '已下单', time: d.orderTime, done: true },
      { title: '已支付', time: d.payTime, done: true },
      { title: '已退款', time: d.refundTime || d.refundApplyTime, done: true },
    ]
  }
  const node2Done = d.status >= 2 && d.status !== 5
  const node3Done = d.status >= 3 && d.status !== 5
  const node4Done = d.status >= 4 && d.status !== 5
  return [
    { title: '已下单', time: d.orderTime, done: true },
    { title: '已支付', time: d.payTime, done: node2Done },
    { title: '已执行', time: d.executeTime, done: node3Done },
    { title: '已完成', time: d.finishTime, done: node4Done },
  ]
})

const showCancelRecord = computed(() => !!detail.value && !!detail.value.cancelReason)
const showExecuteRecord = computed(() => !!detail.value && (detail.value.status === 3 || detail.value.status === 4 || detail.value.status === 6) && !!detail.value.executeBy)
const showRefundRecord = computed(() => !!detail.value && !!detail.value.refundStatusText)
const payStatusColor = computed(() => detail.value?.tradeStatus === '已支付' ? '#67c23a' : detail.value?.tradeStatus === '已关闭' ? '#f56c6c' : '#e6a23c')
const refundStatusColor = computed(() => detail.value?.refundStatusText === '退款成功' ? '#67c23a' : detail.value?.refundStatusText === '退款失败' ? '#f56c6c' : '#e6a23c')

onMounted(() => { void loadData() })
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="7-1" class="menu" @select="onMenuSelect">
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
        <el-card class="block-card">
          <template #header><div class="title">订单进度</div></template>
          <div class="progress-row" v-if="detail">
            <div class="p-node" v-for="(x,idx) in progressNodes" :key="idx">
              <div class="circle" :class="{ done: x.done }">{{ idx + 1 }}</div>
              <div class="p-title" :class="{ done: x.done }">{{ x.title }}</div>
              <div class="p-time">{{ x.done ? x.time : '' }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="detail">
          <template #header><div class="title">订单信息</div></template>
          <div class="info-grid">
            <div class="row"><span class="k">订单编号</span><span class="v">{{ detail.orderNo }}</span></div>
            <div class="row"><span class="k">期望服务时间</span><span class="v">{{ detail.expectedServiceTime }}</span></div>
            <div class="row"><span class="k">老人姓名</span><span class="v">{{ detail.elderName }}</span></div>
            <div class="row"><span class="k">床位号</span><span class="v">{{ detail.bedNo }}</span></div>
            <div class="row"><span class="k">护理项目名称</span><span class="v">{{ detail.nursingItemName }}</span></div>
            <div class="row"><span class="k">订单金额</span><span class="v">{{ detail.orderAmount }} 元</span></div>
            <div class="row"><span class="k">下单人</span><span class="v">{{ detail.orderUserName }}</span></div>
            <div class="row"><span class="k">下单人手机号</span><span class="v">{{ detail.orderUserMobile }}</span></div>
            <div class="row row-span"><span class="k">备注</span><span class="v">{{ detail.remark || '-' }}</span></div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="detail">
          <template #header><div class="title">支付记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">交易状态</span><span class="v" :style="{color: payStatusColor}">{{ detail.tradeStatus || '-' }}</span></div>
            <template v-if="detail.tradeStatus === '已支付'">
              <div class="row"><span class="k">支付渠道</span><span class="v">{{ detail.payChannel || '-' }}</span></div>
              <div class="row"><span class="k">支付方式</span><span class="v">{{ detail.payMethod || '-' }}</span></div>
              <div class="row"><span class="k">微信支付订单号</span><span class="v">{{ detail.wxOrderNo || '-' }}</span></div>
              <div class="row"><span class="k">支付金额</span><span class="v">{{ detail.payAmountText || '-' }}</span></div>
            </template>
          </div>
        </el-card>

        <el-card class="block-card" v-if="showCancelRecord && detail">
          <template #header><div class="title">取消记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">取消人</span><span class="v">{{ detail.cancelBy }} <span class="tag">{{ detail.cancelUserType }}</span></span></div>
            <div class="row"><span class="k">取消原因</span><span class="v">{{ detail.cancelReason }}</span></div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="showExecuteRecord && detail">
          <template #header><div class="title">执行记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">执行人</span><span class="v">{{ detail.executeBy }}</span></div>
            <div class="row row-span"><span class="k">执行图片</span><el-image class="exec-img" :src="detail.executeImageUrl" fit="cover" /></div>
            <div class="row row-span"><span class="k">执行记录</span><span class="v">{{ detail.executeRecord }}</span></div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="showRefundRecord && detail">
          <template #header><div class="title">退款记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">退款状态</span><span class="v" :style="{color: refundStatusColor}">{{ detail.refundStatusText }}</span></div>
            <div class="row"><span class="k">申请人</span><span class="v">{{ detail.refundApplicant }} <span class="tag">{{ detail.refundApplicantType }}</span></span></div>
            <div class="row"><span class="k">申请时间</span><span class="v">{{ detail.refundApplyTime }}</span></div>
            <div class="row"><span class="k">退款原因</span><span class="v">{{ detail.refundReason }}</span></div>
            <div class="row"><span class="k">退款渠道</span><span class="v">{{ detail.refundChannel }}</span></div>
            <div class="row"><span class="k">退款方式</span><span class="v">{{ detail.refundMethod }}</span></div>
            <div class="row" v-if="detail.refundNo"><span class="k">退款编号</span><span class="v">{{ detail.refundNo }}</span></div>
            <div class="row" v-if="detail.refundAmountText"><span class="k">退款金额</span><span class="v">{{ detail.refundAmountText }}</span></div>
            <div class="row" v-if="detail.refundFailCode"><span class="k">失败状态码</span><span class="v">{{ detail.refundFailCode }}</span></div>
            <div class="row" v-if="detail.refundFailReason"><span class="k">失败原因</span><span class="v">{{ detail.refundFailReason }}</span></div>
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
.progress-row { display: grid; grid-template-columns: repeat(4, minmax(120px, 1fr)); gap: 8px; }
.p-node { text-align: left; }
.circle { width: 28px; height: 28px; border-radius: 50%; border: 1px solid #c0c4cc; color: #909399; display: inline-flex; align-items: center; justify-content: center; margin-right: 8px; }
.circle.done { background: #1660d6; border-color: #1660d6; color: #fff; }
.p-title { display: inline-block; color: #909399; font-weight: 500; }
.p-title.done { color: #1660d6; }
.p-time { margin-top: 8px; color: #909399; font-size: 12px; min-height: 18px; }
.info-grid, .record-grid { display: grid; grid-template-columns: repeat(2, minmax(220px, 1fr)); row-gap: 12px; column-gap: 24px; }
.row { display: flex; }
.row-span { grid-column: 1 / span 2; }
.k { width: 110px; color: #909399; }
.v { color: #303133; }
.tag { margin-left: 8px; background: #fdf0dd; color: #f2994a; border-radius: 2px; padding: 1px 6px; font-size: 12px; }
.exec-img { width: 110px; height: 110px; border: 1px solid #ebeef5; }
.footer-btn { display: flex; justify-content: center; margin-top: 20px; }
</style>
