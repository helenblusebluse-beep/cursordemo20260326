<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  changeNursingPlanStatus,
  createNursingPlan,
  deleteNursingPlan,
  getNursingPlan,
  queryNursingItemOptions,
  queryNursingPlans,
  updateNursingPlan,
} from '../api/nursing'
import type { NursingItemOption, NursingPlan } from '../types/nursing'

type PlanRowForm = {
  nursingItemId: number | null
  expectedServiceTime: string
  executeCycle: '每日' | '每周' | '每月'
  executeFrequency: number
}

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<NursingPlan[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = reactive({ name: '', status: undefined as number | undefined })
const statusOptions = [{ label: '启用', value: 1 }, { label: '禁用', value: 2 }]
const cycleOptions: Array<'每日' | '每周' | '每月'> = ['每日', '每周', '每月']
const itemOptions = ref<NursingItemOption[]>([])

const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit' | 'view'>('add')
const editingId = ref<number | null>(null)
const form = reactive({
  planName: '',
  status: 1,
  sortOrder: 1,
  items: [] as PlanRowForm[],
})
const saving = ref(false)
const readonly = computed(() => dialogMode.value === 'view')
const titleText = computed(() => (dialogMode.value === 'add' ? '新增护理计划' : dialogMode.value === 'edit' ? '修改护理计划' : '查看护理计划'))

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

function resetForm(): void {
  form.planName = ''
  form.status = 1
  form.sortOrder = 1
  form.items = [{ nursingItemId: null, expectedServiceTime: '08:00', executeCycle: '每周', executeFrequency: 1 }]
}

async function loadOptions(): Promise<void> {
  itemOptions.value = await queryNursingItemOptions()
}

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryNursingPlans({ name: query.name || undefined, status: query.status, page: page.value, pageSize: pageSize.value })
    rows.value = data.rows
    total.value = data.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '查询失败')
  } finally {
    loading.value = false
  }
}

function onSearch(): void { page.value = 1; void loadTable() }
function onReset(): void { query.name = ''; query.status = undefined; page.value = 1; void loadTable() }
function openAddDialog(): void { dialogMode.value = 'add'; editingId.value = null; resetForm(); dialogVisible.value = true }

async function openEditDialog(row: NursingPlan): Promise<void> {
  if (!row.canEdit) return
  dialogMode.value = 'edit'
  editingId.value = row.id
  const detail = await getNursingPlan(row.id)
  form.planName = detail.planName
  form.status = detail.status
  form.sortOrder = detail.sortOrder
  form.items = (detail.items || []).map((x) => ({ nursingItemId: x.nursingItemId, expectedServiceTime: x.expectedServiceTime || '08:00', executeCycle: x.executeCycle || '每周', executeFrequency: x.executeFrequency || 1 }))
  if (form.items.length === 0) form.items = [{ nursingItemId: null, expectedServiceTime: '08:00', executeCycle: '每周', executeFrequency: 1 }]
  dialogVisible.value = true
}

async function openViewDialog(row: NursingPlan): Promise<void> {
  dialogMode.value = 'view'
  editingId.value = row.id
  const detail = await getNursingPlan(row.id)
  form.planName = detail.planName
  form.status = detail.status
  form.sortOrder = detail.sortOrder
  form.items = (detail.items || []).map((x) => ({ nursingItemId: x.nursingItemId, expectedServiceTime: x.expectedServiceTime || '08:00', executeCycle: x.executeCycle || '每周', executeFrequency: x.executeFrequency || 1 }))
  if (form.items.length === 0) form.items = [{ nursingItemId: null, expectedServiceTime: '08:00', executeCycle: '每周', executeFrequency: 1 }]
  dialogVisible.value = true
}

const selectedSet = computed(() => new Set(form.items.map((x) => x.nursingItemId).filter(Boolean) as number[]))
const canAddLine = computed(() => selectedSet.value.size < itemOptions.value.length)
function availableForRow(currentId: number | null): NursingItemOption[] {
  return itemOptions.value.filter((x) => x.id === currentId || !selectedSet.value.has(x.id))
}
function addLine(): void {
  if (!canAddLine.value || readonly.value) return
  form.items.push({ nursingItemId: null, expectedServiceTime: '08:00', executeCycle: '每周', executeFrequency: 1 })
}
function removeLine(idx: number): void {
  if (readonly.value) return
  if (form.items.length === 1) return
  form.items.splice(idx, 1)
}

function validateForm(): string | null {
  if (!form.planName.trim()) return '护理计划名称不能为空'
  if (form.planName.trim().length > 10) return '护理计划名称最多10个字符'
  if (!form.items.length) return '护理项目信息不完整，请重新设置'
  for (const row of form.items) {
    if (!row.nursingItemId) return '护理项目信息不完整，请重新设置'
    if (!Number.isInteger(row.executeFrequency) || row.executeFrequency < 1 || row.executeFrequency > 7) return '请输入1-7的整数'
  }
  return null
}

async function submitDialog(): Promise<void> {
  if (readonly.value) return
  const err = validateForm()
  if (err) {
    ElMessage.warning(err)
    return
  }
  saving.value = true
  try {
    const payload = { planName: form.planName.trim(), status: form.status, sortOrder: form.sortOrder, items: form.items }
    if (dialogMode.value === 'add') {
      await createNursingPlan(payload)
      ElMessage.success('新增成功')
    } else if (editingId.value) {
      await updateNursingPlan(editingId.value, payload)
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  } finally {
    saving.value = false
  }
}

async function onDelete(row: NursingPlan): Promise<void> {
  if (!row.canDelete) return
  try {
    await ElMessageBox.confirm('确认删除该护理计划吗？', '提示', { type: 'warning' })
    await deleteNursingPlan(row.id)
    ElMessage.success('删除成功')
    await loadTable()
  } catch (e) {
    if (e !== 'cancel' && e !== 'close') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

async function onToggleStatus(row: NursingPlan): Promise<void> {
  const toStatus = row.status === 1 ? 2 : 1
  if (toStatus === 2 && !row.canDisable) return
  try {
    await changeNursingPlanStatus(row.id, toStatus)
    ElMessage.success(toStatus === 1 ? '启用成功' : '禁用成功')
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  }
}

onMounted(async () => {
  await loadOptions()
  await loadTable()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="6-2" class="menu" @select="onMenuSelect">
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
        <el-card class="page-card">
          <el-form :model="query" inline>
            <el-form-item label="名称"><el-input v-model="query.name" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="状态"><el-select v-model="query.status" placeholder="请选择" clearable style="width: 160px"><el-option v-for="x in statusOptions" :key="x.value" :label="x.label" :value="x.value" /></el-select></el-form-item>
            <el-form-item><el-button @click="onReset">重置</el-button><el-button type="primary" @click="onSearch">搜索</el-button></el-form-item>
          </el-form>
          <div class="toolbar"><el-button type="primary" @click="openAddDialog">新增护理计划</el-button></div>
          <el-table :data="rows" border stripe v-loading="loading" table-layout="auto">
            <el-table-column type="index" label="序号" width="70" />
            <el-table-column prop="planName" label="名称" min-width="180" />
            <el-table-column prop="createdTime" label="创建时间" min-width="180" />
            <el-table-column label="状态" min-width="90"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'" effect="plain">{{ row.statusLabel }}</el-tag></template></el-table-column>
            <el-table-column label="操作" min-width="200" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" :disabled="!row.canEdit" @click="openEditDialog(row)">修改</el-button>
                <el-button link type="danger" :disabled="!row.canDelete" @click="onDelete(row)">删除</el-button>
                <el-button link type="info" @click="openViewDialog(row)">查看</el-button>
                <el-button link :type="row.status === 1 ? 'warning' : 'success'" :disabled="row.status === 1 ? !row.canDisable : false" @click="onToggleStatus(row)">
                  {{ row.status === 1 ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager-wrap"><el-pagination background :current-page="page" :page-size="pageSize" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper, total" :total="total" @current-change="(p:number)=>{page=p; void loadTable()}" @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}" /></div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="dialogVisible" :title="titleText" width="860px" destroy-on-close>
    <el-form label-width="100px">
      <el-form-item label="*护理计划名称"><el-input v-model="form.planName" maxlength="10" :disabled="readonly" placeholder="请输入"><template #suffix><span class="count">{{ form.planName.length }}/10</span></template></el-input></el-form-item>
      <el-form-item label="*状态"><el-radio-group v-model="form.status" :disabled="readonly"><el-radio :label="1">启用</el-radio><el-radio :label="2">禁用</el-radio></el-radio-group></el-form-item>
      <el-form-item label="*排序"><el-input-number v-model="form.sortOrder" :min="1" :max="9999" :disabled="readonly" /></el-form-item>
      <el-form-item label="护理项目">
        <table class="inner-table">
          <thead><tr><th style="width:56px">序号</th><th>护理项目名称</th><th>期望服务时间</th><th>执行周期</th><th>执行频次（次）</th><th style="width:86px">操作</th></tr></thead>
          <tbody>
            <tr v-for="(x, i) in form.items" :key="i">
              <td>{{ i + 1 }}</td>
              <td><el-select v-model="x.nursingItemId" placeholder="请选择" :disabled="readonly" style="width: 160px"><el-option v-for="op in availableForRow(x.nursingItemId)" :key="op.id" :label="op.itemName" :value="op.id" /></el-select></td>
              <td><el-time-picker v-model="x.expectedServiceTime" value-format="HH:mm" format="HH:mm" :clearable="false" :disabled="readonly" /></td>
              <td><el-select v-model="x.executeCycle" :disabled="readonly" style="width: 120px"><el-option v-for="cy in cycleOptions" :key="cy" :label="cy" :value="cy" /></el-select></td>
              <td><el-input-number v-model="x.executeFrequency" :min="1" :max="7" :disabled="readonly" /></td>
              <td>
                <el-button link type="danger" :disabled="readonly || form.items.length === 1" @click="removeLine(i)">-</el-button>
                <el-button v-if="i === form.items.length - 1 && canAddLine" link type="primary" :disabled="readonly" @click="addLine">+</el-button>
              </td>
            </tr>
          </tbody>
        </table>
      </el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible=false">{{ readonly ? '关闭' : '取消' }}</el-button><el-button v-if="!readonly" type="primary" :loading="saving" @click="submitDialog">确定</el-button></template>
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
.page-card { border-radius: 8px; }
.toolbar { display: flex; justify-content: flex-end; margin-bottom: 12px; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.count { color: #c0c4cc; font-size: 12px; }
.inner-table { width: 100%; border-collapse: collapse; border: 1px solid #ebeef5; }
.inner-table th, .inner-table td { border: 1px solid #ebeef5; padding: 8px; text-align: left; }
</style>
