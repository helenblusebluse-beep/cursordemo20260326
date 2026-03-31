<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { createVisit, queryVisitRecords } from '../api/visit'
import type { VisitRecordItem } from '../types/visit'

const router = useRouter()
const layoutStore = useLayoutStore()

const loading = ref(false)
const rows = ref<VisitRecordItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const reserveTypeTab = ref<'all' | '1' | '2'>('all')

const query = reactive({
  visitorName: '',
  visitorPhone: '',
  timeRange: [] as string[],
})

const createDialogVisible = ref(false)
const creating = ref(false)
const createForm = reactive({
  reserveType: 1,
  visitorName: '',
  visitorPhone: '',
  elderName: '',
  checkinTime: '',
})

function onMenuSelect(index: string): void {
  if (index === '1') return void router.push('/')
  if (index === '2-1') return void router.push('/appointments/register')
  if (index === '2-2') return
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
    const data = await queryVisitRecords({
      visitorName: query.visitorName || undefined,
      visitorPhone: query.visitorPhone || undefined,
      startTime: query.timeRange[0] || undefined,
      endTime: query.timeRange[1] || undefined,
      reserveType: reserveTypeTab.value === 'all' ? undefined : Number(reserveTypeTab.value),
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
  query.timeRange = []
  reserveTypeTab.value = 'all'
  page.value = 1
  void loadTable()
}

function onReserveTypeTabChange(value: 'all' | '1' | '2'): void {
  reserveTypeTab.value = value
  page.value = 1
  void loadTable()
}

function openCreateDialog(): void {
  createForm.reserveType = 1
  createForm.visitorName = ''
  createForm.visitorPhone = ''
  createForm.elderName = ''
  createForm.checkinTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
  createDialogVisible.value = true
}

async function submitCreate(): Promise<void> {
  if (!createForm.visitorName || !createForm.visitorPhone || !createForm.elderName || !createForm.checkinTime) {
    ElMessage.warning('请完整填写来访登记信息')
    return
  }
  if (createForm.visitorName.length > 10 || createForm.elderName.length > 10) {
    ElMessage.warning('姓名最多10个字符')
    return
  }
  if (!/^1\d{10}$/.test(createForm.visitorPhone)) {
    ElMessage.warning('手机号格式错误，请重新输入')
    return
  }
  creating.value = true
  try {
    await createVisit({ ...createForm })
    createDialogVisible.value = false
    ElMessage.success('来访登记成功')
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '来访登记失败')
  } finally {
    creating.value = false
  }
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
      <el-menu :collapse="layoutStore.collapsed" default-active="2-2" class="menu" @select="onMenuSelect">
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
        <div class="crumb">首页 / 来访管理 / 来访登记</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main">
        <el-card class="page-card">
          <el-form :model="query" inline>
            <el-form-item label="来访人姓名">
              <el-input v-model="query.visitorName" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="来访人手机号">
              <el-input v-model="query.visitorPhone" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="来访时间">
              <el-date-picker
                v-model="query.timeRange"
                type="datetimerange"
                value-format="YYYY-MM-DD HH:mm:ss"
                range-separator="-"
                start-placeholder="请选择开始时间"
                end-placeholder="结束时间"
                style="width: 330px"
              />
            </el-form-item>
            <el-form-item>
              <el-button @click="onReset">重置</el-button>
              <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
          </el-form>

          <div class="toolbar">
            <el-radio-group :model-value="reserveTypeTab" @change="onReserveTypeTabChange">
              <el-radio-button label="all">全部</el-radio-button>
              <el-radio-button label="1">参观来访</el-radio-button>
              <el-radio-button label="2">探访来访</el-radio-button>
            </el-radio-group>
            <el-button type="primary" @click="openCreateDialog">来访登记</el-button>
          </div>

          <el-table :data="rows" border stripe v-loading="loading" table-layout="auto">
            <el-table-column type="index" label="序号" min-width="70" />
            <el-table-column prop="reserveTypeLabel" label="来访类型" min-width="110" />
            <el-table-column prop="visitorName" label="来访人姓名" min-width="120" />
            <el-table-column prop="visitorPhone" label="来访人手机号" min-width="130" />
            <el-table-column prop="elderName" label="老人姓名" min-width="110" />
            <el-table-column prop="checkinTime" label="来访时间" min-width="170" />
            <el-table-column prop="createdTime" label="创建时间" min-width="220" />
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

  <el-dialog v-model="createDialogVisible" title="来访登记" width="640px" :close-on-click-modal="false">
    <el-form label-width="110px">
      <el-form-item label="* 来访类型">
        <el-radio-group v-model="createForm.reserveType">
          <el-radio :label="1">参观来访</el-radio>
          <el-radio :label="2">探访来访</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="* 来访人姓名">
        <el-input v-model="createForm.visitorName" maxlength="10" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="* 来访人手机号">
        <el-input v-model="createForm.visitorPhone" maxlength="11" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="* 老人姓名">
        <el-input v-model="createForm.elderName" maxlength="10" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="* 来访时间">
        <el-date-picker
          v-model="createForm.checkinTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="createDialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="creating" @click="submitCreate">确定</el-button>
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
.toolbar { margin-bottom: 12px; display: flex; justify-content: space-between; align-items: center; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
</style>
