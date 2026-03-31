<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { cancelOrder, queryOrders, refundOrder } from '../api/order'
import type { OrderItem } from '../types/order'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<OrderItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const activeTab = ref('0')
const query = reactive({ orderNo: '', elderName: '', orderUserName: '', timeRange: [] as string[] })
const cancelVisible = ref(false)
const refundVisible = ref(false)
const currentId = ref<number | null>(null)
const cancelReason = ref('')
const refundReason = ref('')
const saving = ref(false)
const cancelReasonOptions = ['不需要此项服务了', '费用有点贵', '临时有事，不方便服务', '信息填写错误', '重复下单']

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
    const data = await queryOrders({
      orderNo: query.orderNo || undefined,
      elderName: query.elderName || undefined,
      orderUserName: query.orderUserName || undefined,
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
  } finally {
    loading.value = false
  }
}

const canCancel = (r: OrderItem) => r.status === 1
const canRefund = (r: OrderItem) => r.status === 2 || r.status === 3

function onSearch(): void { page.value = 1; void loadTable() }
function onReset(): void { query.orderNo=''; query.elderName=''; query.orderUserName=''; query.timeRange=[]; page.value=1; void loadTable() }
function onTabChange(): void { page.value=1; void loadTable() }
function onView(r: OrderItem): void { void router.push(`/orders/${r.id}`) }
function openCancel(r: OrderItem): void { if (!canCancel(r)) return; currentId.value = r.id; cancelReason.value = ''; cancelVisible.value = true }
function openRefund(r: OrderItem): void { if (!canRefund(r)) return; currentId.value = r.id; refundReason.value = ''; refundVisible.value = true }

async function submitCancel(): Promise<void> {
  if (!cancelReason.value) {
    ElMessage.warning('请选择取消原因')
    return
  }
  saving.value = true
  try {
    await cancelOrder(currentId.value as number, cancelReason.value)
    ElMessage.success('取消成功')
    cancelVisible.value = false
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '取消失败')
  } finally { saving.value = false }
}

async function submitRefund(): Promise<void> {
  if (!refundReason.value.trim()) {
    ElMessage.warning('请输入退款原因')
    return
  }
  if (refundReason.value.trim().length > 100) {
    ElMessage.warning('退款原因最多100个字符')
    return
  }
  saving.value = true
  try {
    await refundOrder(currentId.value as number, refundReason.value.trim())
    ElMessage.success('退款成功')
    refundVisible.value = false
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '退款失败')
  } finally { saving.value = false }
}

function statusType(status: number): 'info' | 'warning' | 'success' | 'danger' {
  if (status === 1) return 'warning'
  if (status === 2) return 'info'
  if (status === 3) return 'success'
  if (status === 4) return 'success'
  if (status === 6) return 'success'
  return 'danger'
}

onMounted(() => { void loadTable() })
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
      <el-main class="main">
        <el-card>
          <el-form :model="query" inline class="q-form">
            <el-form-item label="订单编号"><el-input v-model="query.orderNo" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="老人姓名"><el-input v-model="query.elderName" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="下单人"><el-input v-model="query.orderUserName" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="创建时间"><el-date-picker v-model="query.timeRange" type="datetimerange" value-format="YYYY-MM-DD HH:mm:ss" start-placeholder="请选择开始时间" end-placeholder="请选择结束时间" /></el-form-item>
            <el-form-item><el-button @click="onReset">重置</el-button><el-button type="primary" @click="onSearch">搜索</el-button></el-form-item>
          </el-form>
          <el-tabs v-model="activeTab" @tab-change="onTabChange">
            <el-tab-pane label="全部" name="0" />
            <el-tab-pane label="待支付" name="1" />
            <el-tab-pane label="待执行" name="2" />
            <el-tab-pane label="已执行" name="3" />
            <el-tab-pane label="已完成" name="4" />
            <el-tab-pane label="已关闭" name="5" />
            <el-tab-pane label="已退款" name="6" />
          </el-tabs>
          <el-table :data="rows" border stripe v-loading="loading">
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column prop="orderNo" label="订单编号" min-width="170" />
            <el-table-column prop="elderName" label="老人姓名" min-width="90" />
            <el-table-column prop="bedNo" label="床位号" min-width="80" />
            <el-table-column prop="nursingItemName" label="护理项目名称" min-width="120" />
            <el-table-column prop="orderAmount" label="订单金额" min-width="90" />
            <el-table-column prop="expectedServiceTime" label="期望服务时间" min-width="170" />
            <el-table-column prop="orderUserName" label="下单人" min-width="90" />
            <el-table-column prop="orderTime" label="创建时间" min-width="170" />
            <el-table-column label="订单状态" min-width="90"><template #default="{ row }"><el-tag :type="statusType(row.status)" effect="plain">{{ row.statusLabel }}</el-tag></template></el-table-column>
            <el-table-column prop="refundStatusLabel" label="交易状态" min-width="90" />
            <el-table-column label="操作" min-width="180">
              <template #default="{ row }">
                <div class="op-actions">
                  <el-button link type="danger" :disabled="!canCancel(row)" @click="openCancel(row)">取消</el-button>
                  <el-button link type="warning" :disabled="!canRefund(row)" @click="openRefund(row)">退款</el-button>
                  <el-button link type="primary" @click="onView(row)">查看</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager-wrap"><el-pagination background :current-page="page" :page-size="pageSize" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper, total" :total="total" @current-change="(p:number)=>{page=p; void loadTable()}" @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}" /></div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="cancelVisible" title="填写取消原因" width="560px">
    <el-form label-width="90px"><el-form-item label="*取消原因"><el-select v-model="cancelReason" placeholder="请选择" style="width:100%"><el-option v-for="x in cancelReasonOptions" :key="x" :label="x" :value="x" /></el-select></el-form-item></el-form>
    <template #footer><el-button @click="cancelVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="submitCancel">确定</el-button></template>
  </el-dialog>

  <el-dialog v-model="refundVisible" title="填写退款原因" width="560px">
    <el-form label-width="90px"><el-form-item label="*退款原因"><el-input v-model="refundReason" type="textarea" :rows="3" maxlength="100" placeholder="请输入" /></el-form-item></el-form>
    <template #footer><el-button @click="refundVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="submitRefund">确定</el-button></template>
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
.op-actions { display: flex; align-items: center; gap: 4px; white-space: nowrap; }
</style>
