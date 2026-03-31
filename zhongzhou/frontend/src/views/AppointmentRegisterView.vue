<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  confirmAppointment,
  queryAppointments,
} from '../api/appointment'
import type { AppointmentItem } from '../types/appointment'

const router = useRouter()
const layoutStore = useLayoutStore()

const loading = ref(false)
const rows = ref<AppointmentItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const query = reactive({
  visitorName: '',
  visitorPhone: '',
  status: undefined as number | undefined,
  reserveType: undefined as number | undefined,
})
const reserveTypeTab = ref<'all' | '1' | '2'>('all')

const confirmDialogVisible = ref(false)
const confirmSubmitting = ref(false)
const currentId = ref<number | null>(null)

const confirmTime = ref('')

function onMenuSelect(index: string): void {
  if (index === '1') return void router.push('/')
  if (index === '2-1') return
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
    const data = await queryAppointments({
      visitorName: query.visitorName,
      visitorPhone: query.visitorPhone,
      status: query.status,
      reserveType: query.reserveType,
      page: page.value,
      pageSize: pageSize.value,
    })
    rows.value = data.rows
    total.value = data.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '查询失败')
  } finally {
    loading.value = false
  }
}

function onSearch(): void {
  page.value = 1
  void loadTable()
}

function onReset(): void {
  query.visitorName = ''
  query.visitorPhone = ''
  query.status = undefined
  query.reserveType = undefined
  reserveTypeTab.value = 'all'
  page.value = 1
  void loadTable()
}

function openConfirmDialog(row: AppointmentItem): void {
  if (!canArrive(row)) {
    return
  }
  currentId.value = row.id
  confirmTime.value = row.scheduledTime || ''
  confirmDialogVisible.value = true
}

async function submitConfirm(): Promise<void> {
  if (!currentId.value || !confirmTime.value) {
    ElMessage.warning('请填写来访时间')
    return
  }
  confirmSubmitting.value = true
  try {
    await confirmAppointment(currentId.value, confirmTime.value)
    confirmDialogVisible.value = false
    ElMessage.success('确认到院成功')
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '确认失败')
  } finally {
    confirmSubmitting.value = false
  }
}

function isExpired(row: AppointmentItem): boolean {
  if (row.status !== 1 || !row.scheduledTime) {
    return false
  }
  const ts = Date.parse(row.scheduledTime.replace(' ', 'T'))
  return Number.isFinite(ts) && ts < Date.now()
}

function displayStatus(row: AppointmentItem): string {
  if (isExpired(row)) {
    return '已过期'
  }
  return row.statusLabel
}

function statusType(row: AppointmentItem): '' | 'success' | 'warning' | 'danger' {
  if (row.status === 2) return 'success'
  if (row.status === 3) return 'danger'
  return 'warning'
}

function canArrive(row: AppointmentItem): boolean {
  return row.status === 1
}

function onReserveTypeTabChange(value: 'all' | '1' | '2'): void {
  reserveTypeTab.value = value
  query.reserveType = value === 'all' ? undefined : Number(value)
  page.value = 1
  void loadTable()
}

onMounted(() => {
  void loadTable()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="2-1" class="menu" @select="onMenuSelect">
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
        <div class="crumb">首页 / 来访管理 / 预约登记</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main">
        <el-card class="page-card">
          <el-form :model="query" inline>
            <el-form-item label="预约人姓名">
              <el-input v-model="query.visitorName" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="预约人手机号">
              <el-input v-model="query.visitorPhone" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="预约状态">
              <el-select v-model="query.status" placeholder="请选择" clearable style="width: 140px">
                <el-option label="待上门" :value="1" />
                <el-option label="已完成" :value="2" />
                <el-option label="已取消" :value="3" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="onReset">重置</el-button>
              <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
          </el-form>

          <div class="type-tabs">
            <el-radio-group :model-value="reserveTypeTab" @change="onReserveTypeTabChange">
              <el-radio-button label="all">全部</el-radio-button>
              <el-radio-button label="1">参观预约</el-radio-button>
              <el-radio-button label="2">探访预约</el-radio-button>
            </el-radio-group>
          </div>

          <el-table :data="rows" border stripe v-loading="loading" table-layout="auto">
            <el-table-column type="index" label="序号" min-width="70" />
            <el-table-column prop="reserveTypeLabel" label="预约类型" min-width="110" />
            <el-table-column prop="visitorName" label="预约人姓名" min-width="120" />
            <el-table-column prop="visitorPhone" label="预约人手机号" min-width="130" />
            <el-table-column prop="elderName" label="老人姓名" min-width="110" />
            <el-table-column prop="scheduledTime" label="预约时间" min-width="170" />
            <el-table-column prop="createdTime" label="创建时间" min-width="170" />
            <el-table-column label="预约状态" min-width="110">
              <template #default="{ row }">
                <el-tag :type="statusType(row)">{{ displayStatus(row) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="120">
              <template #default="{ row }">
                <el-button
                  link
                  :type="canArrive(row) ? 'primary' : 'info'"
                  class="op-btn"
                  :disabled="!canArrive(row)"
                  @click="openConfirmDialog(row)"
                >
                  到院
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pager-wrap">
            <el-pagination
              background
              :current-page="page"
              :page-size="pageSize"
              :page-sizes="[10, 20, 50]"
              layout="sizes, prev, pager, next, jumper, total"
              :total="total"
              @current-change="(p:number)=>{page=p; void loadTable()}"
              @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}"
            />
          </div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="confirmDialogVisible" title="确认到院时间" width="460px" :close-on-click-modal="false">
    <el-form label-width="100px">
      <el-form-item label="*来访时间">
        <el-date-picker
          v-model="confirmTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="confirmDialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="confirmSubmitting" @click="submitConfirm">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.page-root { min-height: 100vh; background: #f5f7fa; }
.sider { background: #fff; color: #303133; transition: width .2s; border-right: 1px solid #ebeef5; }
.brand { height: 60px; display: flex; align-items: center; gap: 8px; padding: 0 12px; border-bottom: 1px solid #ebeef5; }
.brand img { border-radius: 50%; }
.brand-text { font-weight: 700; }
.menu { border-right: none; background: #fff; color: #303133; }
:deep(.menu .el-menu-item),
:deep(.menu .el-sub-menu__title) { color: #303133; }
:deep(.menu .el-menu-item:hover),
:deep(.menu .el-sub-menu__title:hover) { background: #f5f7fa; color: #409eff; }
:deep(.menu .el-menu-item.is-active) { background: #ecf5ff; color: #409eff; font-weight: 600; }
.header { height: 56px; background: #fff; display: flex; align-items: center; border-bottom: 1px solid #ebeef5; }
.crumb { margin-left: 12px; color: #606266; flex: 1; }
.admin { color: #909399; font-size: 13px; }
.main { padding: 12px; }
.page-card { border-radius: 8px; }
.type-tabs { margin-bottom: 12px; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.op-btn { padding-left: 0; padding-right: 0; }
</style>
