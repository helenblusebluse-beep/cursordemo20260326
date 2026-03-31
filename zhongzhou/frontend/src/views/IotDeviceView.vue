<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { ElLoading, ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  createIotDevice,
  deleteIotDevice,
  fetchIotDeviceDetail,
  fetchIotProducts,
  queryIotDevices,
  syncIotProducts,
  updateIotDevice,
} from '../api/iot'
import { queryCustomers } from '../api/customer'
import { getFloorDetail, listFloors } from '../api/bedPreview'
import type { IotDeviceRow, IotProductItem } from '../types/iot'
import type { CustomerManageItem } from '../types/customer'

const router = useRouter()
const layoutStore = useLayoutStore()

const DEVICE_TYPES = ['随身设备', '固定设备'] as const

const loading = ref(false)
const syncLoading = ref(false)
const rows = ref<IotDeviceRow[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = reactive<{ deviceName: string; productId: number | undefined; deviceType: string }>({
  deviceName: '',
  productId: undefined,
  deviceType: '',
})

const products = ref<IotProductItem[]>([])

const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const editingId = ref<number | null>(null)

const form = reactive({
  deviceName: '',
  remarkName: '',
  productId: undefined as number | undefined,
  deviceType: '随身设备',
  bindElderId: null as number | null,
  bindBedId: null as number | null,
  bedCascaderValue: null as number[] | null,
})

const productNameReadonly = ref('')
const bedOptions = ref<CascaderOption[]>([])

type CascaderOption = { value: number; label: string; children?: CascaderOption[] }

const elderDialogVisible = ref(false)
const elderLoading = ref(false)
const elderRows = ref<CustomerManageItem[]>([])
const elderPickName = ref('')

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

async function loadProducts(): Promise<void> {
  try {
    products.value = await fetchIotProducts()
  } catch {
    products.value = []
  }
}

async function buildBedOptions(): Promise<void> {
  const tabs = await listFloors()
  const out: CascaderOption[] = []
  for (const t of tabs) {
    const detail = await getFloorDetail(t.id)
    const roomChildren: CascaderOption[] = []
    for (const room of detail.rooms) {
      roomChildren.push({
        value: room.id,
        label: room.roomNo,
        children: room.beds.map((b) => ({ value: b.id, label: b.bedName })),
      })
    }
    out.push({ value: t.id, label: t.floorName, children: roomChildren })
  }
  bedOptions.value = out
}

function findBedPath(bedId: number, opts: CascaderOption[]): number[] | null {
  for (const f of opts) {
    if (!f.children) continue
    for (const r of f.children) {
      if (!r.children) continue
      for (const b of r.children) {
        if (b.value === bedId) return [f.value, r.value, b.value]
      }
    }
  }
  return null
}

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryIotDevices({
      deviceName: query.deviceName || undefined,
      productId: query.productId,
      deviceType: query.deviceType || undefined,
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
  query.productId = undefined
  query.deviceType = ''
  page.value = 1
  void loadTable()
}

function resetForm(): void {
  form.deviceName = ''
  form.remarkName = ''
  form.productId = undefined
  form.deviceType = '随身设备'
  form.bindElderId = null
  form.bindBedId = null
  form.bedCascaderValue = null
  elderPickName.value = ''
  productNameReadonly.value = ''
}

async function openAdd(): Promise<void> {
  await loadProducts()
  if (products.value.length === 0) {
    ElMessage.warning('未创建产品，请先创建产品')
    return
  }
  dialogMode.value = 'add'
  editingId.value = null
  resetForm()
  try {
    await buildBedOptions()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载床位数据失败')
    return
  }
  dialogVisible.value = true
}

async function openEdit(row: IotDeviceRow): Promise<void> {
  await loadProducts()
  try {
    await buildBedOptions()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载床位数据失败')
    return
  }
  dialogMode.value = 'edit'
  editingId.value = row.id
  resetForm()
  try {
    const d = await fetchIotDeviceDetail(row.id)
    form.deviceName = d.deviceName
    form.remarkName = d.remarkName
    form.productId = d.productId
    productNameReadonly.value = d.productName
    form.deviceType = d.deviceType
    form.bindElderId = d.bindElderId
    form.bindBedId = d.bindBedId
    if (d.deviceType === '固定设备' && d.bindBedId != null) {
      form.bedCascaderValue = findBedPath(d.bindBedId, bedOptions.value)
    }
    dialogVisible.value = true
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  }
}

async function onDialogOpened(): Promise<void> {
  if (dialogMode.value === 'edit' && editingId.value && form.deviceType === '随身设备' && form.bindElderId) {
    try {
      const data = await queryCustomers({ page: 1, pageSize: 500 })
      elderRows.value = data.rows
      const hit = data.rows.find((r) => r.id === form.bindElderId)
      elderPickName.value = hit ? hit.customerNickname : `ID:${form.bindElderId}`
    } catch {
      /* ignore */
    }
  }
}

function onDeviceTypeChange(): void {
  form.bindElderId = null
  form.bindBedId = null
  form.bedCascaderValue = null
  elderPickName.value = ''
}

watch(
  () => form.bedCascaderValue,
  (val) => {
    if (val && val.length >= 3) {
      form.bindBedId = val[val.length - 1]!
    } else {
      form.bindBedId = null
    }
  },
)

async function openElderDialog(): Promise<void> {
  elderDialogVisible.value = true
  elderLoading.value = true
  try {
    const data = await queryCustomers({ page: 1, pageSize: 500 })
    elderRows.value = data.rows
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载客户失败')
  } finally {
    elderLoading.value = false
  }
}

function pickElder(row: CustomerManageItem): void {
  form.bindElderId = row.id
  elderPickName.value = row.customerNickname
  elderDialogVisible.value = false
}

async function submitForm(): Promise<void> {
  const name = form.deviceName.trim()
  const remark = form.remarkName.trim()
  if (!name || name.length > 15) {
    ElMessage.warning('请输入设备名称，不超过15字')
    return
  }
  if (!remark || remark.length > 15) {
    ElMessage.warning('请输入备注名称，不超过15字')
    return
  }
  if (dialogMode.value === 'add') {
    if (form.productId == null) {
      ElMessage.warning('请选择所属产品')
      return
    }
    if (form.deviceType === '随身设备') {
      if (form.bindElderId == null) {
        ElMessage.warning('请选择接入位置（老人）')
        return
      }
    } else {
      if (form.bindBedId == null || !form.bedCascaderValue || form.bedCascaderValue.length < 3) {
        ElMessage.warning('请级联选择到具体床位')
        return
      }
    }
    try {
      await createIotDevice({
        deviceName: name,
        remarkName: remark,
        productId: form.productId,
        deviceType: form.deviceType,
        bindElderId: form.deviceType === '随身设备' ? form.bindElderId : null,
        bindBedId: form.deviceType === '固定设备' ? form.bindBedId : null,
      })
      ElMessage.success('保存成功')
      dialogVisible.value = false
      void loadTable()
    } catch (e) {
      ElMessage.error(e instanceof Error ? e.message : '保存失败')
    }
    return
  }
  if (editingId.value == null) return
  if (form.deviceType === '随身设备') {
    if (form.bindElderId == null) {
      ElMessage.warning('请选择接入位置（老人）')
      return
    }
  } else {
    if (form.bindBedId == null || !form.bedCascaderValue || form.bedCascaderValue.length < 3) {
      ElMessage.warning('请级联选择到具体床位')
      return
    }
  }
  try {
    await updateIotDevice(editingId.value, {
      remarkName: remark,
      deviceType: form.deviceType,
      bindElderId: form.deviceType === '随身设备' ? form.bindElderId : null,
      bindBedId: form.deviceType === '固定设备' ? form.bindBedId : null,
    })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    void loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function onDelete(row: IotDeviceRow): Promise<void> {
  try {
    await ElMessageBox.confirm('确认删除该设备？删除后老人与设备将自动解绑。', '确认删除', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    })
    await deleteIotDevice(row.id)
    ElMessage.success('删除成功')
    void loadTable()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function goView(row: IotDeviceRow): void {
  void router.push(`/iot/devices/${row.id}`)
}

async function onSync(): Promise<void> {
  const inst = ElLoading.service({
    lock: true,
    text: '数据同步中',
    background: 'rgba(255,255,255,0.65)',
  })
  syncLoading.value = true
  try {
    await syncIotProducts()
    ElMessage.success('同步数据成功')
    await loadProducts()
    void loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '同步数据失败')
  } finally {
    inst.close()
    syncLoading.value = false
  }
}

const indexMethod = (i: number): number => (page.value - 1) * pageSize.value + i + 1

const productOptions = computed(() => products.value)

onMounted(() => {
  void loadProducts()
  void loadTable()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand"><img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" /><div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div></div>
      <el-menu :collapse="layoutStore.collapsed" default-active="9-1" class="menu" @select="onMenuSelect">
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
      <el-header class="header"><el-button text @click="layoutStore.toggleCollapsed">☰</el-button><div class="crumb">智能检测 / 设备管理</div><div class="admin">管理员</div></el-header>
      <el-main class="main">
        <el-card>
          <el-form :model="query" inline class="q-form">
            <el-form-item label="设备名称"><el-input v-model="query.deviceName" placeholder="请输入" clearable /></el-form-item>
            <el-form-item label="产品名称">
              <el-select v-model="query.productId" placeholder="请选择" clearable style="width: 180px">
                <el-option v-for="p in productOptions" :key="p.id" :label="p.productName" :value="p.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="设备类型">
              <el-select v-model="query.deviceType" placeholder="请选择" clearable style="width: 140px">
                <el-option v-for="t in DEVICE_TYPES" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="onReset">重置</el-button>
              <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
          </el-form>

          <div class="toolbar">
            <div />
            <div class="toolbar-btns">
              <el-button type="primary" :loading="syncLoading" @click="onSync">同步数据</el-button>
              <el-button type="primary" @click="openAdd">新增设备</el-button>
            </div>
          </div>

          <el-table :data="rows" border stripe v-loading="loading">
            <el-table-column type="index" :index="indexMethod" label="序号" width="64" />
            <el-table-column prop="deviceName" label="设备名称" min-width="160" show-overflow-tooltip />
            <el-table-column prop="remarkName" label="备注名称" min-width="120" show-overflow-tooltip />
            <el-table-column prop="productName" label="产品名称" min-width="120" show-overflow-tooltip />
            <el-table-column prop="accessLocationDisplay" label="接入位置" min-width="140" show-overflow-tooltip />
            <el-table-column prop="deviceType" label="设备类型" width="100" />
            <el-table-column prop="createdTime" label="创建时间" min-width="170" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="danger" link @click="onDelete(row)">删除</el-button>
                <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
                <el-button type="primary" link @click="goView(row)">查看</el-button>
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
              @current-change="() => loadTable()"
              @size-change="() => { page = 1; loadTable() }"
            />
          </div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog
    v-model="dialogVisible"
    :title="dialogMode === 'add' ? '新增设备' : '编辑设备'"
    width="560px"
    destroy-on-close
    @opened="onDialogOpened"
  >
    <el-form label-width="100px" @submit.prevent>
      <el-form-item label="设备名称" required>
        <el-input
          v-model="form.deviceName"
          placeholder="请输入"
          maxlength="15"
          show-word-limit
          :disabled="dialogMode === 'edit'"
        />
      </el-form-item>
      <el-form-item label="备注名称" required>
        <el-input v-model="form.remarkName" placeholder="请输入" maxlength="15" show-word-limit />
      </el-form-item>
      <el-form-item label="所属产品" required>
        <el-select v-if="dialogMode === 'add'" v-model="form.productId" placeholder="请选择" style="width: 100%">
          <el-option v-for="p in productOptions" :key="p.id" :label="p.productName" :value="p.id" />
        </el-select>
        <el-input v-else :model-value="productNameReadonly" disabled />
      </el-form-item>
      <el-form-item label="设备类型" required>
        <el-radio-group v-model="form.deviceType" @change="onDeviceTypeChange">
          <el-radio label="随身设备">随身设备</el-radio>
          <el-radio label="固定设备">固定设备</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.deviceType === '随身设备'" label="接入位置" required>
        <div class="loc-row">
          <el-input :model-value="elderPickName" placeholder="请选择" readonly style="flex: 1" />
          <el-button @click="openElderDialog">选择老人</el-button>
        </div>
      </el-form-item>
      <el-form-item v-else label="接入位置" required>
        <el-cascader
          v-model="form.bedCascaderValue"
          :options="bedOptions"
          :props="{ value: 'value', label: 'label', children: 'children' }"
          placeholder="请选择"
          clearable
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="elderDialogVisible" title="选择老人" width="640px" destroy-on-close>
    <el-table v-loading="elderLoading" :data="elderRows" border stripe max-height="360" @row-click="pickElder">
      <el-table-column prop="customerNickname" label="客户昵称" min-width="120" />
      <el-table-column prop="customerPhone" label="手机号" width="130" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="primary" link @click.stop="pickElder(row)">选择</el-button>
        </template>
      </el-table-column>
    </el-table>
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
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.toolbar-btns { display: flex; gap: 8px; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.loc-row { display: flex; gap: 8px; width: 100%; align-items: center; }
</style>
