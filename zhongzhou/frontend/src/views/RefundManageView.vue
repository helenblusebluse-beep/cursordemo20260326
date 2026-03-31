<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { getRefund, queryRefunds } from '../api/refund'
import type { RefundDetail, RefundItem } from '../types/refund'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<RefundItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const activeTab = ref('0')
const query = reactive({ refundNo: '', orderNo: '', applicant: '', timeRange: [] as string[] })
const dialogVisible = ref(false)
const current = ref<RefundDetail | null>(null)
const isRefundSuccess = (x: RefundDetail | null) => !!x?.refundStatusLabel?.includes('成功')
const isRefundFailed = (x: RefundDetail | null) => !!x?.refundStatusLabel?.includes('失败')

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

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryRefunds({
      refundNo: query.refundNo || undefined,
      orderNo: query.orderNo || undefined,
      applicant: query.applicant || undefined,
      beginTime: query.timeRange?.[0] || undefined,
      endTime: query.timeRange?.[1] || undefined,
      status: Number(activeTab.value),
      page: page.value,
      pageSize: pageSize.value,
    })
    rows.value = data.rows
    total.value = data.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally { loading.value = false }
}

function onSearch(): void { page.value = 1; void loadTable() }
function onReset(): void { query.refundNo=''; query.orderNo=''; query.applicant=''; query.timeRange=[]; page.value=1; void loadTable() }
function onTabChange(): void { page.value=1; void loadTable() }

async function onView(row: RefundItem): Promise<void> {
  try {
    current.value = await getRefund(row.id)
    dialogVisible.value = true
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载详情失败')
  }
}

function statusColor(s: string): string {
  if (s.includes('成功')) return '#67c23a'
  if (s.includes('失败')) return '#f56c6c'
  return '#e6a23c'
}

onMounted(() => { void loadTable() })
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="7-2" class="menu" @select="onMenuSelect">
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
      <el-main class="main">
        <el-card>
          <el-form :model="query" inline class="q-form">
            <el-form-item label="退款编号"><el-input v-model="query.refundNo" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="订单编号"><el-input v-model="query.orderNo" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="申请人"><el-input v-model="query.applicant" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="申请时间"><el-date-picker v-model="query.timeRange" type="datetimerange" value-format="YYYY-MM-DD HH:mm:ss" start-placeholder="请选择开始时间" end-placeholder="请选择结束时间" /></el-form-item>
            <el-form-item><el-button @click="onReset">重置</el-button><el-button type="primary" @click="onSearch">搜索</el-button></el-form-item>
          </el-form>
          <el-tabs v-model="activeTab" @tab-change="onTabChange">
            <el-tab-pane label="全部" name="0" />
            <el-tab-pane label="退款处理中" name="1" />
            <el-tab-pane label="退款成功" name="2" />
            <el-tab-pane label="退款失败" name="3" />
          </el-tabs>
          <el-table :data="rows" border stripe v-loading="loading">
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column prop="refundNo" label="退款编号" min-width="170" />
            <el-table-column prop="orderNo" label="订单编号" min-width="170" />
            <el-table-column prop="orderAmount" label="退款金额（元）" min-width="110" />
            <el-table-column prop="applicant" label="申请人" min-width="90" />
            <el-table-column prop="applyTime" label="申请时间" min-width="170" />
            <el-table-column prop="refundTime" label="退款时间" min-width="170" />
            <el-table-column prop="orderStatusLabel" label="订单状态" min-width="90" />
            <el-table-column label="退款状态" min-width="100"><template #default="{row}"><span :style="{color: statusColor(row.refundStatusLabel)}">{{ row.refundStatusLabel }}</span></template></el-table-column>
            <el-table-column label="操作" min-width="80" fixed="right"><template #default="{row}"><el-button link type="primary" @click="onView(row)">查看</el-button></template></el-table-column>
          </el-table>
          <div class="pager-wrap"><el-pagination background :current-page="page" :page-size="pageSize" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper, total" :total="total" @current-change="(p:number)=>{page=p; void loadTable()}" @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}" /></div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="dialogVisible" title="查看退款记录" width="700px">
    <div class="detail-grid" v-if="current">
      <div class="row"><span class="k">退款编号</span><span class="v">{{ current.refundNo || '-' }}</span></div>
      <div class="row"><span class="k">订单编号</span><span class="v">{{ current.orderNo || '-' }}</span></div>

      <div class="row"><span class="k">订单状态</span><span class="v">{{ current.orderStatusLabel || '-' }}</span></div>
      <div class="row"><span class="k">退款状态</span><span class="v" :style="{color: statusColor(current.refundStatusLabel)}">{{ current.refundStatusLabel || '-' }}</span></div>

      <div class="row">
        <span class="k">申请人</span>
        <span class="v">{{ current.applicant || '-' }} <span class="tag">{{ current.applicantType || '-' }}</span></span>
      </div>
      <div class="row"><span class="k">申请时间</span><span class="v">{{ current.applyTime || '-' }}</span></div>

      <div class="row"><span class="k">退款原因</span><span class="v text-wrap">{{ current.refundReason || '-' }}</span></div>
      <div class="row"><span class="k">退款渠道</span><span class="v">{{ current.refundChannel || '-' }}</span></div>

      <div class="row"><span class="k">退款方式</span><span class="v">{{ current.refundMethod || '-' }}</span></div>
      <div class="row" v-if="isRefundSuccess(current)"><span class="k">退款时间</span><span class="v">{{ current.refundTime || '-' }}</span></div>
      <div class="row" v-if="isRefundFailed(current)"><span class="k">失败状态码</span><span class="v">{{ current.failCode || '-' }}</span></div>

      <div class="row" v-if="isRefundSuccess(current)"><span class="k">退款金额</span><span class="v">{{ current.refundAmount || '-' }} 元</span></div>
      <div class="row row-span" v-if="isRefundFailed(current)"><span class="k">失败原因</span><span class="v text-wrap">{{ current.failReason || '-' }}</span></div>
    </div>
  </el-dialog>
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
.q-form { margin-bottom: 8px; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.detail-grid { display: grid; grid-template-columns: repeat(2, minmax(260px, 1fr)); row-gap: 12px; column-gap: 24px; align-items: start; }
.row { display: flex; }
.row-span { grid-column: 1 / span 2; }
.k { width: 92px; color: #909399; }
.v { color: #303133; flex: 1; }
.text-wrap { line-height: 22px; white-space: normal; word-break: break-all; }
.tag { margin-left: 8px; background: #fdf0dd; color: #f2994a; border-radius: 2px; padding: 1px 6px; font-size: 12px; }
</style>
