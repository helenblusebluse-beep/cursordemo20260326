<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { handleIotAlarmRecord, queryIotAlarmRecords } from '../api/iot'
import type { IotAlarmRecordItem } from '../types/iot'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<IotAlarmRecordItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = reactive({ deviceName: '', createdDate: '', handleStatus: undefined as number | undefined })
const activeStatusTab = ref<'all' | 'pending' | 'processed'>('all')
const dialogVisible = ref(false)
const handlingRow = ref<IotAlarmRecordItem | null>(null)
const handleForm = reactive({ handleTime: '', handleResult: '' })

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
  if (index === '10-1') return void router.push('/profile')
}

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryIotAlarmRecords({
      deviceName: query.deviceName.trim() || undefined,
      createdDate: query.createdDate || undefined,
      handleStatus: query.handleStatus,
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

function onSearch(): void {
  page.value = 1
  void loadTable()
}

function onReset(): void {
  query.deviceName = ''
  query.createdDate = ''
  activeStatusTab.value = 'all'
  query.handleStatus = undefined
  page.value = 1
  void loadTable()
}

function onStatusTabChange(tab: 'all' | 'pending' | 'processed'): void {
  if (tab === 'all') query.handleStatus = undefined
  if (tab === 'pending') query.handleStatus = 0
  if (tab === 'processed') query.handleStatus = 1
  page.value = 1
  void loadTable()
}

function openHandleDialog(row: IotAlarmRecordItem): void {
  if (row.handleStatus === 1) return
  handlingRow.value = row
  handleForm.handleTime = ''
  handleForm.handleResult = ''
  dialogVisible.value = true
}

async function submitHandle(): Promise<void> {
  if (!handlingRow.value) return
  if (!handleForm.handleTime) {
    ElMessage.warning('请选择处理时间')
    return
  }
  const txt = handleForm.handleResult.trim()
  if (!txt) {
    ElMessage.warning('请输入处理结果')
    return
  }
  if (txt.length > 100) {
    ElMessage.warning('处理结果不能超过100字符')
    return
  }
  if (!/^[\u4e00-\u9fa5A-Za-z0-9，。,.、；;：:（）()\-\s]+$/.test(txt)) {
    ElMessage.warning('处理结果不能包含特殊符号')
    return
  }
  try {
    await handleIotAlarmRecord(handlingRow.value.id, {
      handleTime: handleForm.handleTime,
      handleResult: txt,
    })
    ElMessage.success('处理成功')
    dialogVisible.value = false
    void loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  }
}

onMounted(() => { void loadTable() })
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="9-3" class="menu" @select="onMenuSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-sub-menu index="2"><template #title>来访管理</template><el-menu-item index="2-1">预约登记</el-menu-item><el-menu-item index="2-2">来访登记</el-menu-item></el-sub-menu>
        <el-sub-menu index="3"><template #title>入退管理</template><el-menu-item index="3-1">健康评估</el-menu-item><el-menu-item index="3-2">入住办理</el-menu-item><el-menu-item index="3-3">退住办理</el-menu-item></el-sub-menu>
        <el-sub-menu index="4"><template #title>在住管理</template><el-menu-item index="4-1">房型设置</el-menu-item><el-menu-item index="4-2">床位预览</el-menu-item><el-menu-item index="4-3">智能床位</el-menu-item><el-menu-item index="4-4">合同跟踪</el-menu-item><el-menu-item index="4-5">客户管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="6"><template #title>服务管理</template><el-menu-item index="6-1">护理项目</el-menu-item><el-menu-item index="6-2">护理计划</el-menu-item><el-menu-item index="6-3">护理等级</el-menu-item><el-menu-item index="6-4">负责老人</el-menu-item><el-menu-item index="6-5">任务安排</el-menu-item></el-sub-menu>
        <el-sub-menu index="7"><template #title>订单查询</template><el-menu-item index="7-1">订单管理</el-menu-item><el-menu-item index="7-2">退款管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="8"><template #title>财务统计</template><el-menu-item index="8-1">账单管理</el-menu-item><el-menu-item index="8-2">预缴款充值</el-menu-item><el-menu-item index="8-3">余额查询</el-menu-item><el-menu-item index="8-4">欠费老人</el-menu-item></el-sub-menu>
        <el-sub-menu index="9"><template #title>智能检测</template><el-menu-item index="9-1">设备管理</el-menu-item><el-menu-item index="9-2">报警规则</el-menu-item><el-menu-item index="9-3">报警数据</el-menu-item></el-sub-menu>
        <el-sub-menu index="10"><template #title>个人中心</template><el-menu-item index="10-1">个人信息</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header"><el-button text @click="layoutStore.toggleCollapsed">☰</el-button><div class="crumb">智能检测 / 报警数据</div><div class="admin">管理员</div></el-header>
      <el-main class="main">
        <el-card>
          <el-form :model="query" inline class="q-form">
            <el-form-item label="设备名称"><el-input v-model="query.deviceName" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="创建时间">
              <el-date-picker v-model="query.createdDate" type="date" placeholder="请选择日期" value-format="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item><el-button @click="onReset">重置</el-button><el-button type="primary" @click="onSearch">搜索</el-button></el-form-item>
          </el-form>
          <el-tabs v-model="activeStatusTab" class="status-tabs" @tab-change="(v: string | number)=>onStatusTabChange(v as 'all' | 'pending' | 'processed')">
            <el-tab-pane label="全部" name="all" />
            <el-tab-pane label="待处理" name="pending" />
            <el-tab-pane label="已处理" name="processed" />
          </el-tabs>
          <el-table :data="rows" border stripe v-loading="loading">
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column prop="deviceName" label="设备名称" min-width="110" />
            <el-table-column prop="ruleName" label="报警名称" min-width="110" />
            <el-table-column prop="productName" label="所属产品" min-width="110" />
            <el-table-column prop="dataTypeLabel" label="报警类型" min-width="110" />
            <el-table-column prop="accessLocation" label="老人/床位" min-width="110" />
            <el-table-column label="数值内容" min-width="100"><template #default="{ row }">{{ row.metricValue }}</template></el-table-column>
            <el-table-column prop="alarmRuleText" label="报警规则" min-width="200" show-overflow-tooltip />
            <el-table-column prop="triggerTime" label="触发时间" min-width="160" />
            <el-table-column prop="notifyObject" label="通知对象" min-width="140" />
            <el-table-column prop="notifyTime" label="通知时间" min-width="160" />
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="row.handleStatus === 1 ? 'success' : 'warning'" effect="light">{{ row.handleStatusLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="90" fixed="right">
              <template #default="{ row }">
                <el-button v-if="row.handleStatus === 0" type="primary" link @click="openHandleDialog(row)">处理</el-button>
                <el-button v-else type="info" link>处置</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager-wrap"><el-pagination background :current-page="page" :page-size="pageSize" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper, total" :total="total" @current-change="(p:number)=>{page=p; void loadTable()}" @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}" /></div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="dialogVisible" title="填写处理结果" width="640px" destroy-on-close>
    <el-form label-width="100px">
      <el-form-item label="处理时间" required>
        <el-date-picker
          v-model="handleForm.handleTime"
          type="datetime"
          placeholder="请选择"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="处理结果" required>
        <el-input
          v-model="handleForm.handleResult"
          type="textarea"
          :maxlength="100"
          show-word-limit
          placeholder="请输入"
          rows="3"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitHandle">确定</el-button>
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
:deep(.menu .el-menu-item), :deep(.menu .el-sub-menu__title) { color: #303133; }
:deep(.menu .el-menu-item:hover), :deep(.menu .el-sub-menu__title:hover) { background: #f5f7fa; color: #409eff; }
:deep(.menu .el-menu-item.is-active) { background: #ecf5ff; color: #409eff; font-weight: 600; }
.header { height: 56px; background: #fff; display: flex; align-items: center; border-bottom: 1px solid #ebeef5; }
.crumb { margin-left: 12px; color: #606266; flex: 1; }
.admin { color: #909399; font-size: 13px; }
.main { padding: 12px; }
.q-form { margin-bottom: 8px; margin-top: 12px; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.muted { color: #c0c4cc; }
.status-tabs { margin-bottom: 8px; }
</style>
