<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  deleteIotAlarmRule,
  fetchIotProducts,
  queryIotAlarmRules,
  setIotAlarmRuleEnabled,
} from '../api/iot'
import type { IotAlarmRuleItem, IotProductItem } from '../types/iot'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<IotAlarmRuleItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = reactive({ ruleName: '', productId: undefined as number | undefined })
const productOptions = ref<IotProductItem[]>([])

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

function indexMethod(index: number): number {
  return (page.value - 1) * pageSize.value + index + 1
}

async function loadProducts(): Promise<void> {
  try {
    productOptions.value = await fetchIotProducts()
  } catch {
    productOptions.value = []
  }
}

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryIotAlarmRules({
      ruleName: query.ruleName.trim() || undefined,
      productId: query.productId,
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
  query.ruleName = ''
  query.productId = undefined
  page.value = 1
  void loadTable()
}

function goAdd(): void {
  void router.push('/iot/alarm-rules/add')
}

function goEdit(row: IotAlarmRuleItem): void {
  void router.push(`/iot/alarm-rules/${row.id}/edit`)
}

async function onToggle(row: IotAlarmRuleItem): Promise<void> {
  const next: 0 | 1 = row.enabled === 1 ? 0 : 1
  try {
    await setIotAlarmRuleEnabled(row.id, next)
    ElMessage.success('操作成功')
    void loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  }
}

async function onDelete(row: IotAlarmRuleItem): Promise<void> {
  try {
    await ElMessageBox.confirm('确定删除该规则？', '提示', { type: 'warning' })
    await deleteIotAlarmRule(row.id)
    ElMessage.success('已删除')
    void loadTable()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function statusTagType(row: IotAlarmRuleItem): 'success' | 'danger' {
  return row.enabled === 1 ? 'success' : 'danger'
}

onMounted(() => {
  void loadProducts()
  void loadTable()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="9-2" class="menu" @select="onMenuSelect">
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
      <el-header class="header"><el-button text @click="layoutStore.toggleCollapsed">☰</el-button><div class="crumb">智能检测 &gt; 报警规则</div><div class="admin">管理员</div></el-header>
      <el-main class="main">
        <el-card shadow="never" class="main-card">
          <el-form :model="query" inline class="q-form">
            <el-form-item label="报警规则名称"><el-input v-model="query.ruleName" placeholder="请输入" clearable style="width: 200px" /></el-form-item>
            <el-form-item label="所属产品">
              <el-select v-model="query.productId" placeholder="请选择" clearable style="width: 200px">
                <el-option v-for="p in productOptions" :key="p.id" :label="p.productName" :value="p.id" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="onReset">重置</el-button>
              <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
          </el-form>
          <div class="toolbar-actions">
            <el-button type="primary" @click="goAdd">新增报警规则</el-button>
          </div>
          <el-table :data="rows" border stripe v-loading="loading" class="rule-table">
            <el-table-column type="index" :index="indexMethod" label="序号" width="64" />
            <el-table-column prop="ruleName" label="报警规则名称" min-width="140" show-overflow-tooltip />
            <el-table-column prop="productName" label="所属产品" min-width="120" />
            <el-table-column prop="deviceDisplay" label="关联设备" min-width="140" show-overflow-tooltip />
            <el-table-column prop="alarmRuleText" label="报警规则" min-width="260" show-overflow-tooltip />
            <el-table-column prop="effectivePeriod" label="报警生效时段" min-width="180" />
            <el-table-column prop="createdTime" label="创建时间" min-width="170" />
            <el-table-column label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="statusTagType(row)" effect="light">{{ row.enabledLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="danger" link @click="onDelete(row)">删除</el-button>
                <el-button type="primary" link @click="goEdit(row)">修改</el-button>
                <el-button type="primary" link @click="onToggle(row)">{{ row.enabled === 1 ? '禁用' : '启用' }}</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager-wrap">
            <el-pagination
              background
              v-model:current-page="page"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50]"
              layout="sizes, prev, pager, next, jumper, total"
              :total="total"
              @current-change="() => void loadTable()"
              @size-change="() => { page = 1; void loadTable() }"
            />
          </div>
        </el-card>
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
.main-card { border-radius: 4px; }
.q-form { margin-bottom: 12px; }
.toolbar-actions { display: flex; justify-content: flex-end; margin-bottom: 12px; }
.rule-table { width: 100%; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
</style>
