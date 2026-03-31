<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { UploadFile } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  createRoomType,
  deleteRoomType,
  queryRoomTypes,
  toggleRoomTypeStatus,
  updateRoomType,
  getRoomTypeDetail,
} from '../api/roomType'
import type { RoomTypeItem } from '../types/roomType'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const detailLoading = ref(false)
const rows = ref<RoomTypeItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)

const dialogVisible = ref(false)
const dialogTitle = ref('新增房型')
const editingId = ref<number | null>(null)
const submitting = ref(false)

const form = reactive({
  roomName: '',
  bedCount: 1,
  bedFee: 0,
  introduction: '',
  imageUrls: [] as string[],
  remarks: '',
})

const rules = {
  roomName: [
    { required: true, message: '请输入房间名称', trigger: 'blur' },
    { max: 10, message: '最多10个字符', trigger: 'blur' },
  ],
  bedCount: [{ required: true, message: '请输入床位数量', trigger: 'blur' }],
  bedFee: [{ required: true, message: '请输入床位费用', trigger: 'blur' }],
  introduction: [
    { required: true, message: '请输入房型介绍', trigger: 'blur' },
    { max: 50, message: '最多50个字符', trigger: 'blur' },
  ],
}

const formRef = ref()

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
  form.roomName = ''
  form.bedCount = 1
  form.bedFee = 0
  form.introduction = ''
  form.imageUrls = []
  form.remarks = ''
}

function openCreate(): void {
  editingId.value = null
  dialogTitle.value = '新增房型'
  resetForm()
  dialogVisible.value = true
}

async function openEdit(row: RoomTypeItem): Promise<void> {
  if (row.associatedRoomCount > 0) return
  editingId.value = row.id
  dialogTitle.value = '修改房型'
  resetForm()
  detailLoading.value = true
  try {
    const d = await getRoomTypeDetail(row.id)
    form.roomName = d.roomName
    form.bedCount = d.bedCount ?? 1
    form.bedFee = Number(d.bedFee)
    form.introduction = d.introduction
    form.remarks = d.remarks ?? ''
    if (d.imageUrlsJson) {
      try {
        form.imageUrls = JSON.parse(d.imageUrlsJson) as string[]
      } catch {
        form.imageUrls = []
      }
    }
    dialogVisible.value = true
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    detailLoading.value = false
  }
}

function validateImageFile(rawFile: File): boolean {
  const okType = ['image/jpeg', 'image/png', 'image/jpg'].includes(rawFile.type)
  if (!okType) {
    ElMessage.error('仅支持 PNG / JPG / JPEG')
    return false
  }
  const max = 5 * 1024 * 1024
  if (rawFile.size > max) {
    ElMessage.error('单张图片不超过 5MB')
    return false
  }
  if (form.imageUrls.length >= 50) {
    ElMessage.warning('最多上传50张图片')
    return false
  }
  return true
}

function onUploadChange(_file: UploadFile, fileList: UploadFile[]): void {
  const last = fileList[fileList.length - 1]
  const raw = last?.raw
  if (!raw || !validateImageFile(raw)) return
  const reader = new FileReader()
  reader.onload = () => {
    const s = reader.result as string
    if (form.imageUrls.length < 50) form.imageUrls.push(s)
  }
  reader.readAsDataURL(raw)
}

function removeImage(i: number): void {
  form.imageUrls.splice(i, 1)
}

async function loadTable(): Promise<void> {
  loading.value = true
  try {
    const data = await queryRoomTypes({ page: page.value, pageSize: pageSize.value })
    rows.value = data.rows
    total.value = data.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '查询失败')
  } finally {
    loading.value = false
  }
}

async function onSubmit(): Promise<void> {
  await formRef.value?.validate().catch(() => Promise.reject())
  if (form.imageUrls.length === 0) {
    ElMessage.warning('请上传房间图片')
    return
  }
  submitting.value = true
  try {
    const payload = {
      roomName: form.roomName.trim(),
      bedCount: Number(form.bedCount),
      bedFee: Number(form.bedFee),
      introduction: form.introduction.trim(),
      imageUrls: form.imageUrls,
      remarks: form.remarks.trim() || undefined,
    }
    if (editingId.value == null) {
      await createRoomType(payload)
      ElMessage.success('新增成功')
    } else {
      await updateRoomType(editingId.value, payload)
      ElMessage.success('修改成功')
    }
    dialogVisible.value = false
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  } finally {
    submitting.value = false
  }
}

async function onDelete(row: RoomTypeItem): Promise<void> {
  if (row.associatedRoomCount > 0) return
  try {
    await ElMessageBox.confirm('确定删除该房型吗？', '提示', { type: 'warning' })
    await deleteRoomType(row.id)
    ElMessage.success('已删除')
    await loadTable()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

async function onToggle(row: RoomTypeItem): Promise<void> {
  if (row.associatedRoomCount > 0) return
  const enable = row.status !== 1
  try {
    await toggleRoomTypeStatus(row.id, enable)
    ElMessage.success(enable ? '已启用' : '已禁用')
    await loadTable()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  }
}

function feeDisplay(v: string | number): string {
  const n = Number(v)
  return Number.isFinite(n) ? n.toFixed(2) : '-'
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
      <el-menu :collapse="layoutStore.collapsed" default-active="4-1" class="menu" @select="onMenuSelect">
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
        <div class="crumb">页面一 / 页面二 / 页面三</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main">
        <el-card class="page-card">
          <div class="toolbar">
            <el-button type="primary" @click="openCreate">新增房型</el-button>
          </div>

          <el-table
            :data="rows"
            border
            stripe
            v-loading="loading"
            table-layout="auto"
            :header-cell-style="{ background: '#f5f7fa' }"
          >
            <el-table-column type="index" :index="(i: number) => (page - 1) * pageSize + i + 1" label="序号" width="70" />
            <el-table-column label="房间图片" width="100">
              <template #default="{ row }">
                <el-image
                  v-if="row.thumbUrl"
                  :src="row.thumbUrl"
                  style="width: 56px; height: 42px; border-radius: 4px"
                  fit="cover"
                />
                <span v-else class="muted">—</span>
              </template>
            </el-table-column>
            <el-table-column prop="roomName" label="房间名称" min-width="110" />
            <el-table-column label="床位费用 (元/月)" min-width="130">
              <template #default="{ row }">{{ feeDisplay(row.bedFee) }}</template>
            </el-table-column>
            <el-table-column prop="introduction" label="房型介绍" min-width="200" show-overflow-tooltip />
            <el-table-column prop="createdTime" label="创建时间" min-width="170" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag v-if="row.status === 1" type="success">启用</el-tag>
                <el-tag v-else type="danger">禁用</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="{ row }">
                <el-button
                  link
                  type="primary"
                  :disabled="row.associatedRoomCount > 0"
                  @click="openEdit(row)"
                >
                  修改
                </el-button>
                <el-button link type="danger" :disabled="row.associatedRoomCount > 0" @click="onDelete(row)">
                  删除
                </el-button>
                <el-button
                  v-if="row.status === 1"
                  link
                  type="danger"
                  :disabled="row.associatedRoomCount > 0"
                  @click="onToggle(row)"
                >
                  禁用
                </el-button>
                <el-button
                  v-else
                  link
                  type="primary"
                  :disabled="row.associatedRoomCount > 0"
                  @click="onToggle(row)"
                >
                  启用
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
              @current-change="(p: number) => { page = p; void loadTable() }"
              @size-change="(s: number) => { pageSize = s; page = 1; void loadTable() }"
            />
          </div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" destroy-on-close @closed="resetForm" v-loading="detailLoading">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="房间名称" prop="roomName">
        <el-input v-model="form.roomName" placeholder="请输入" maxlength="10" show-word-limit />
      </el-form-item>
      <el-form-item label="床位数量" prop="bedCount">
        <el-input-number v-model="form.bedCount" :min="1" :max="9999" :step="1" controls-position="right" style="width: 100%" />
      </el-form-item>
      <el-form-item label="床位费用" prop="bedFee">
        <el-input-number v-model="form.bedFee" :min="0" :precision="2" :step="100" style="width: 100%" />
      </el-form-item>
      <el-form-item label="房型介绍" prop="introduction">
        <el-input v-model="form.introduction" type="textarea" :rows="3" placeholder="请输入" maxlength="50" show-word-limit />
      </el-form-item>
      <el-form-item label="房间图片" required>
        <div class="upload-row">
          <el-upload
            list-type="picture-card"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="onUploadChange"
          >
            <span class="upload-plus">+</span>
          </el-upload>
          <div class="upload-hint">点击上传图片，单张≤5MB，PNG/JPG/JPEG，最多50张（{{ form.imageUrls.length }}/50）</div>
        </div>
        <div v-if="form.imageUrls.length" class="thumb-list">
          <div v-for="(u, i) in form.imageUrls" :key="i" class="thumb">
            <el-image :src="u" fit="cover" style="width: 80px; height: 80px; border-radius: 4px" />
            <el-button text type="danger" class="thumb-del" @click="removeImage(i)">删除</el-button>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remarks" type="textarea" :rows="2" placeholder="请输入" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="onSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.page-root {
  min-height: 100vh;
  background: #f5f7fa;
}
.sider {
  background: #fff;
  color: #303133;
  transition: width 0.2s;
  border-right: 1px solid #ebeef5;
}
.brand {
  height: 60px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 12px;
  border-bottom: 1px solid #ebeef5;
}
.brand img {
  border-radius: 50%;
}
.brand-text {
  font-weight: 700;
}
.menu {
  border-right: none;
  background: #fff;
  color: #303133;
}
:deep(.menu .el-menu-item),
:deep(.menu .el-sub-menu__title) {
  color: #303133;
}
:deep(.menu .el-menu-item:hover),
:deep(.menu .el-sub-menu__title:hover) {
  background: #f5f7fa;
  color: #409eff;
}
:deep(.menu .el-menu-item.is-active) {
  background: #ecf5ff;
  color: #409eff;
  font-weight: 600;
}
.header {
  height: 56px;
  background: #fff;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
}
.crumb {
  margin-left: 12px;
  color: #606266;
  flex: 1;
}
.admin {
  color: #909399;
  font-size: 13px;
}
.main {
  padding: 12px;
}
.page-card {
  border-radius: 8px;
}
.toolbar {
  margin-bottom: 12px;
  display: flex;
  justify-content: flex-end;
}
.pager-wrap {
  display: flex;
  justify-content: flex-end;
  padding-top: 12px;
}
.muted {
  color: #909399;
}
.upload-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  flex-wrap: wrap;
}
.upload-hint {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
  max-width: 280px;
}
.upload-plus {
  font-size: 28px;
  color: #8c939d;
}
.thumb-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
}
.thumb {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}
.thumb-del {
  padding: 0;
  font-size: 12px;
}
</style>
