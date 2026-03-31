<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { changeNursingItemStatus, createNursingItem, deleteNursingItem, queryNursingItems, updateNursingItem, uploadNursingImage } from '../api/nursing'
import type { NursingItem } from '../types/nursing'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const rows = ref<NursingItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const query = reactive({
  name: '',
  status: undefined as number | undefined,
})

const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 2 },
]

const dialogVisible = ref(false)
const dialogMode = ref<'add' | 'edit'>('add')
const editingId = ref<number | null>(null)
const form = reactive({
  itemName: '',
  sortOrder: 1,
  unit: '',
  price: 0,
  status: 1,
  imageName: '',
  imageUrl: '',
  nursingRequirement: '',
})
const saving = ref(false)

const titleText = computed(() => (dialogMode.value === 'add' ? '新增护理项目' : '修改护理项目'))

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
    const data = await queryNursingItems({
      name: query.name || undefined,
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
  query.name = ''
  query.status = undefined
  page.value = 1
  void loadTable()
}

function resetForm(): void {
  form.itemName = ''
  form.sortOrder = 1
  form.unit = ''
  form.price = 0
  form.status = 1
  form.imageName = ''
  form.imageUrl = ''
  form.nursingRequirement = ''
}

function openAddDialog(): void {
  dialogMode.value = 'add'
  editingId.value = null
  resetForm()
  dialogVisible.value = true
}

function openEditDialog(row: NursingItem): void {
  if (!row.canEdit) return
  dialogMode.value = 'edit'
  editingId.value = row.id
  form.itemName = row.itemName
  form.sortOrder = row.sortOrder
  form.unit = row.unit || ''
  form.price = Number(row.price)
  form.status = row.status
  form.imageName = row.imageName
  form.imageUrl = row.imageUrl || ''
  form.nursingRequirement = row.nursingRequirement || ''
  dialogVisible.value = true
}

function validateImageFile(file: File): boolean {
  const lower = file.name.toLowerCase()
  const okType = lower.endsWith('.png') || lower.endsWith('.jpg') || lower.endsWith('.jpeg')
  if (!okType) {
    ElMessage.warning('仅支持上传PNG JPG JPEG类型图片')
    return false
  }
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.warning('图片大小不超过5M')
    return false
  }
  return true
}

async function uploadImage(options: any): Promise<void> {
  const file = options.file as File
  if (!validateImageFile(file)) {
    options.onError?.(new Error('文件校验失败'))
    return
  }
  try {
    const data = await uploadNursingImage(file)
    form.imageName = data.fileName
    form.imageUrl = data.fileUrl
    options.onSuccess?.(data)
    ElMessage.success('上传成功')
  } catch (e) {
    options.onError?.(e)
    ElMessage.error(e instanceof Error ? e.message : '上传失败')
  }
}

function validateForm(): string | null {
  if (!form.itemName.trim()) return '名称不能为空'
  if (form.itemName.trim().length > 10) return '名称最多10个字符'
  if (form.unit && form.unit.length > 5) return '单位最多5个字符'
  if (form.price < 0) return '价格不能为负'
  if (!form.imageName.trim()) return '图片不能为空'
  if (form.nursingRequirement && form.nursingRequirement.length > 50) return '护理要求最多50个字符'
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
      itemName: form.itemName.trim(),
      sortOrder: form.sortOrder,
      unit: form.unit.trim() || '',
      price: Number(form.price),
      status: form.status,
      imageName: form.imageName.trim(),
      imageUrl: form.imageUrl || '',
      nursingRequirement: form.nursingRequirement.trim() || '',
    }
    if (dialogMode.value === 'add') {
      await createNursingItem(payload)
      ElMessage.success('新增成功')
    } else if (editingId.value) {
      await updateNursingItem(editingId.value, payload)
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

async function onDelete(row: NursingItem): Promise<void> {
  if (!row.canDelete) return
  try {
    await ElMessageBox.confirm('确认删除该护理项目吗？', '提示', { type: 'warning' })
    await deleteNursingItem(row.id)
    ElMessage.success('删除成功')
    await loadTable()
  } catch (e) {
    if (e !== 'cancel' && e !== 'close') {
      ElMessage.error(e instanceof Error ? e.message : '删除失败')
    }
  }
}

async function onToggleStatus(row: NursingItem): Promise<void> {
  const toStatus = row.status === 1 ? 2 : 1
  if (toStatus === 2 && !row.canDisable) return
  try {
    await changeNursingItemStatus(row.id, toStatus)
    ElMessage.success(toStatus === 1 ? '启用成功' : '禁用成功')
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
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
      <el-menu :collapse="layoutStore.collapsed" default-active="6-1" class="menu" @select="onMenuSelect">
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
        <el-sub-menu index="6">
          <template #title>服务管理</template>
          <el-menu-item index="6-1">护理项目</el-menu-item>
          <el-menu-item index="6-2">护理计划</el-menu-item>
          <el-menu-item index="6-3">护理等级</el-menu-item>
          <el-menu-item index="6-4">负责老人</el-menu-item>
          <el-menu-item index="6-5">任务安排</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="7"><template #title>订单查询</template><el-menu-item index="7-1">订单管理</el-menu-item><el-menu-item index="7-2">退款管理</el-menu-item></el-sub-menu>
        <el-sub-menu index="8"><template #title>财务统计</template><el-menu-item index="8-1">账单管理</el-menu-item><el-menu-item index="8-2">预缴款充值</el-menu-item><el-menu-item index="8-3">余额查询</el-menu-item><el-menu-item index="8-4">欠费老人</el-menu-item></el-sub-menu>
        <el-sub-menu index="9"><template #title>智能检测</template><el-menu-item index="9-1">设备管理</el-menu-item><el-menu-item index="9-2">报警规则</el-menu-item><el-menu-item index="9-3">报警数据</el-menu-item></el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <el-button text @click="layoutStore.toggleCollapsed">☰</el-button>
        <div class="crumb">页面一 -> 页面二 -> 页面三</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main">
        <el-card class="page-card">
          <el-form :model="query" inline>
            <el-form-item label="名称">
              <el-input v-model="query.name" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="query.status" placeholder="请选择" clearable style="width: 160px">
                <el-option v-for="x in statusOptions" :key="x.value" :label="x.label" :value="x.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="onReset">重置</el-button>
              <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
          </el-form>

          <div class="toolbar">
            <el-button type="primary" @click="openAddDialog">新增护理项目</el-button>
          </div>

          <el-table :data="rows" border stripe v-loading="loading" table-layout="auto">
            <el-table-column type="index" label="序号" width="70" />
            <el-table-column prop="itemName" label="名称" min-width="130" />
            <el-table-column prop="sortOrder" label="排序号" min-width="90" />
            <el-table-column prop="unit" label="单位" min-width="90" />
            <el-table-column label="价格（元）" min-width="110">
              <template #default="{ row }">{{ Number(row.price).toFixed(2) }}</template>
            </el-table-column>
            <el-table-column label="图片" min-width="80">
              <template #default="{ row }">
                <img :src="row.imageUrl || 'https://dummyimage.com/32x24/e5e7eb/666&text=图'" alt="图片" class="img-thumb" />
              </template>
            </el-table-column>
            <el-table-column prop="nursingRequirement" label="护理要求" min-width="180" />
            <el-table-column prop="createdTime" label="创建时间" min-width="170" />
            <el-table-column label="状态" min-width="90">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'" effect="plain">{{ row.statusLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="190" fixed="right">
              <template #default="{ row }">
                <div class="op-actions">
                  <el-button link type="primary" :disabled="!row.canEdit" @click="openEditDialog(row)">修改</el-button>
                  <el-button link type="danger" :disabled="!row.canDelete" @click="onDelete(row)">删除</el-button>
                  <el-button
                    link
                    :type="row.status === 1 ? 'warning' : 'success'"
                    :disabled="row.status === 1 ? !row.canDisable : false"
                    @click="onToggleStatus(row)"
                  >
                    {{ row.status === 1 ? '禁用' : '启用' }}
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <div class="pager-wrap">
            <el-pagination
              background
              :current-page="page"
              :page-size="pageSize"
              :page-sizes="[10,20,50]"
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

  <el-dialog v-model="dialogVisible" :title="titleText" width="640px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="* 名称">
        <el-input v-model="form.itemName" maxlength="10" placeholder="请输入">
          <template #suffix><span class="count">{{ form.itemName.length }}/10</span></template>
        </el-input>
      </el-form-item>
      <el-form-item label="排序号">
        <el-input-number v-model="form.sortOrder" :min="1" :max="9999" />
      </el-form-item>
      <el-form-item label="单位">
        <el-input v-model="form.unit" maxlength="5" placeholder="请输入">
          <template #suffix><span class="count">{{ form.unit.length }}/5</span></template>
        </el-input>
      </el-form-item>
      <el-form-item label="* 价格">
        <el-input-number v-model="form.price" :min="0" :precision="2" :step="0.5" />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="2">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="* 图片">
        <div class="upload-wrap">
          <el-upload action="#" :show-file-list="false" :http-request="uploadImage" accept=".png,.jpg,.jpeg">
            <el-button>点击上传图片</el-button>
          </el-upload>
          <span class="upload-tip">图片大小不超过5M，仅支持上传PNG JPG JPEG类型图片</span>
        </div>
        <div v-if="form.imageName" class="uploaded-name">{{ form.imageName }}</div>
      </el-form-item>
      <el-form-item label="护理要求">
        <el-input v-model="form.nursingRequirement" maxlength="50" placeholder="请输入">
          <template #suffix><span class="count">{{ form.nursingRequirement.length }}/50</span></template>
        </el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible=false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="submitDialog">确定</el-button>
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
.page-card { border-radius: 8px; }
.toolbar { display: flex; justify-content: flex-end; margin-bottom: 12px; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.img-thumb { width: 28px; height: 22px; object-fit: cover; border-radius: 2px; border: 1px solid #e5e7eb; }
.count { color: #c0c4cc; font-size: 12px; }
.upload-wrap { display: flex; align-items: center; gap: 10px; }
.upload-tip { color: #909399; font-size: 12px; }
.uploaded-name { margin-top: 6px; color: #606266; font-size: 12px; }
.op-actions { display: flex; align-items: center; gap: 4px; white-space: nowrap; }
</style>
