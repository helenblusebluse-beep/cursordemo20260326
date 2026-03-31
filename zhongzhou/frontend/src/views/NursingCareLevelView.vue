<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  changeNursingCareLevelStatus,
  createNursingCareLevel,
  deleteNursingCareLevel,
  queryNursingCareLevels,
  queryNursingPlans,
  updateNursingCareLevel,
} from '../api/nursing'
import type { NursingCareLevelItem, NursingPlan } from '../types/nursing'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<NursingCareLevelItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = reactive({ levelName: '', status: undefined as number | undefined })
const statusOptions = [{ label: '启用', value: 1 }, { label: '禁用', value: 2 }]

const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const editingId = ref<number | null>(null)
const form = reactive({
  levelName: '',
  planId: undefined as number | undefined,
  nursingFee: 0,
  status: 1,
  levelDesc: '',
})
const enabledPlans = ref<NursingPlan[]>([])
const saving = ref(false)
const titleText = computed(() => (dialogMode.value === 'add' ? '新增护理等级' : '修改护理等级'))

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

async function loadPlans(): Promise<void> {
  const data = await queryNursingPlans({ status: 1, page: 1, pageSize: 200 })
  enabledPlans.value = data.rows
}

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryNursingCareLevels({
      levelName: query.levelName || undefined,
      status: query.status,
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
  query.levelName = ''
  query.status = undefined
  page.value = 1
  void loadTable()
}

function resetForm(): void {
  form.levelName = ''
  form.planId = undefined
  form.nursingFee = 0
  form.status = 1
  form.levelDesc = ''
}

function openAddDialog(): void {
  dialogMode.value = 'add'
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}

function openEditDialog(row: NursingCareLevelItem): void {
  if (!row.canEdit) return
  dialogMode.value = 'edit'
  editingId.value = row.id
  form.levelName = row.levelName
  form.planId = row.planId
  form.nursingFee = Number(row.nursingFee)
  form.status = row.status
  form.levelDesc = row.levelDesc
  dialogVisible.value = true
}

function validateForm(): string | null {
  if (!form.levelName.trim()) return '等级名称不能为空'
  if (form.levelName.trim().length > 10) return '等级名称最多10个字符'
  if (!form.planId) return '护理计划不能为空'
  if (form.nursingFee < 0) return '护理费用不能为负'
  if (!form.levelDesc.trim()) return '等级说明不能为空'
  if (form.levelDesc.trim().length > 50) return '等级说明最多50个字符'
  return null
}

async function submitDialog(): Promise<void> {
  const err = validateForm()
  if (err) {
    ElMessage.warning(err)
    return
  }
  saving.value = true
  try {
    const payload = {
      levelName: form.levelName.trim(),
      planId: form.planId as number,
      nursingFee: Number(form.nursingFee),
      status: form.status,
      levelDesc: form.levelDesc.trim(),
    }
    if (dialogMode.value === 'add') {
      await createNursingCareLevel(payload)
      ElMessage.success('新增成功')
    } else if (editingId.value) {
      await updateNursingCareLevel(editingId.value, payload)
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

async function onDelete(row: NursingCareLevelItem): Promise<void> {
  if (!row.canDelete) return
  try {
    await ElMessageBox.confirm('确认删除该护理等级吗？', '提示', { type: 'warning' })
    await deleteNursingCareLevel(row.id)
    ElMessage.success('删除成功')
    await loadTable()
  } catch (e) {
    if (e !== 'cancel' && e !== 'close') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

async function onToggleStatus(row: NursingCareLevelItem): Promise<void> {
  const toStatus = row.status === 1 ? 2 : 1
  if (toStatus === 2 && !row.canDisable) return
  try {
    await changeNursingCareLevelStatus(row.id, toStatus)
    ElMessage.success(toStatus === 1 ? '启用成功' : '禁用成功')
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  }
}

onMounted(async () => {
  await loadPlans()
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
      <el-menu :collapse="layoutStore.collapsed" default-active="6-3" class="menu" @select="onMenuSelect">
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
            <el-form-item label="等级名称"><el-input v-model="query.levelName" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="状态"><el-select v-model="query.status" placeholder="请选择" clearable style="width: 160px"><el-option v-for="x in statusOptions" :key="x.value" :label="x.label" :value="x.value" /></el-select></el-form-item>
            <el-form-item><el-button @click="onReset">重置</el-button><el-button type="primary" @click="onSearch">搜索</el-button></el-form-item>
          </el-form>
          <div class="toolbar"><el-button type="primary" @click="openAddDialog">新增护理等级</el-button></div>
          <el-table :data="rows" border stripe v-loading="loading" table-layout="auto">
            <el-table-column type="index" label="序号" width="70" />
            <el-table-column prop="levelName" label="护理等级名称" min-width="130" />
            <el-table-column prop="planName" label="执行护理计划" min-width="160" />
            <el-table-column label="护理费用（元/月）" min-width="130"><template #default="{ row }">{{ Number(row.nursingFee).toFixed(2) }}</template></el-table-column>
            <el-table-column label="状态" min-width="90"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'" effect="plain">{{ row.statusLabel }}</el-tag></template></el-table-column>
            <el-table-column prop="levelDesc" label="等级说明" min-width="220" show-overflow-tooltip />
            <el-table-column prop="createdTime" label="创建时间" min-width="170" />
            <el-table-column label="操作" min-width="190" fixed="right">
              <template #default="{ row }">
                <div class="op-actions">
                  <el-button link type="primary" :disabled="!row.canEdit" @click="openEditDialog(row)">修改</el-button>
                  <el-button link type="danger" :disabled="!row.canDelete" @click="onDelete(row)">删除</el-button>
                  <el-button link :type="row.status === 1 ? 'warning' : 'success'" :disabled="row.status === 1 ? !row.canDisable : false" @click="onToggleStatus(row)">
                    {{ row.status === 1 ? '禁用' : '启用' }}
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager-wrap"><el-pagination background :current-page="page" :page-size="pageSize" :page-sizes="[10,20,50]" layout="sizes, prev, pager, next, jumper, total" :total="total" @current-change="(p:number)=>{page=p; void loadTable()}" @size-change="(s:number)=>{pageSize=s; page=1; void loadTable()}" /></div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="dialogVisible" :title="titleText" width="640px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="*等级名称"><el-input v-model="form.levelName" maxlength="10" placeholder="请输入"><template #suffix><span class="count">{{ form.levelName.length }}/10</span></template></el-input></el-form-item>
      <el-form-item label="*护理计划">
        <el-select v-model="form.planId" placeholder="请设置" style="width: 100%">
          <el-option v-for="p in enabledPlans" :key="p.id" :label="p.planName" :value="p.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="*护理费用"><el-input-number v-model="form.nursingFee" :min="0" :precision="2" :step="0.5" /></el-form-item>
      <el-form-item label="*状态"><el-radio-group v-model="form.status"><el-radio :label="1">启用</el-radio><el-radio :label="2">禁用</el-radio></el-radio-group></el-form-item>
      <el-form-item label="*等级说明"><el-input v-model="form.levelDesc" maxlength="50" placeholder="请输入"><template #suffix><span class="count">{{ form.levelDesc.length }}/50</span></template></el-input></el-form-item>
    </el-form>
    <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="submitDialog">确定</el-button></template>
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
.op-actions { display: flex; align-items: center; gap: 4px; white-space: nowrap; }
</style>
