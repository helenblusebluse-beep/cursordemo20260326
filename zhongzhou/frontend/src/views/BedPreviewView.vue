<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  listFloors,
  getFloorDetail,
  createFloor,
  updateFloor,
  deleteFloor,
  createRoom,
  updateRoom,
  deleteRoom,
  createBed,
  updateBed,
  deleteBed,
} from '../api/bedPreview'
import { listEnabledRoomTypeOptions } from '../api/roomType'
import type { FloorDetail, FloorTab, BedItem, RoomCard } from '../types/bedPreview'

const router = useRouter()
const layoutStore = useLayoutStore()

const loading = ref(false)
const floors = ref<FloorTab[]>([])
const activeFloorId = ref('')
const detail = ref<FloorDetail | null>(null)
const hoverBedId = ref<number | null>(null)

const roomTypeOptions = ref<Array<{ id: number; roomName: string }>>([])

const floorDialog = ref(false)
const floorDialogMode = ref<'add' | 'edit'>('add')
const editingFloorId = ref<number | null>(null)
const floorForm = reactive({ floorName: '', sortOrder: 1 })
const floorSubmitting = ref(false)

const roomDialog = ref(false)
const roomDialogMode = ref<'add' | 'edit'>('add')
const editingRoomId = ref<number | null>(null)
const roomForm = reactive({ roomNo: '', roomTypeId: undefined as number | undefined, sortOrder: 1 })
const roomSubmitting = ref(false)

const bedDialog = ref(false)
const bedDialogMode = ref<'add' | 'edit'>('add')
const bedRoomId = ref<number | null>(null)
const editingBedId = ref<number | null>(null)
const bedForm = reactive({ bedName: '', sortOrder: 1 })
const bedSubmitting = ref(false)

const currentFloorTab = computed(() => floors.value.find((f) => String(f.id) === activeFloorId.value))

const editingFloorHasRooms = computed(() => {
  if (editingFloorId.value == null) return true
  return floors.value.find((f) => f.id === editingFloorId.value)?.hasRooms ?? true
})

async function refreshFloorsAndDetail(): Promise<void> {
  floors.value = await listFloors()
  if (!floors.value.length) {
    detail.value = null
    activeFloorId.value = ''
    return
  }
  if (!activeFloorId.value || !floors.value.some((f) => String(f.id) === activeFloorId.value)) {
    activeFloorId.value = String(floors.value[0].id)
  }
  await loadDetail(Number(activeFloorId.value))
}

async function loadDetail(floorId: number): Promise<void> {
  loading.value = true
  try {
    detail.value = await getFloorDetail(floorId)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function onTabChange(name: string | number): void {
  activeFloorId.value = String(name)
  void loadDetail(Number(name))
}

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

function openAddFloor(): void {
  floorDialogMode.value = 'add'
  editingFloorId.value = null
  floorForm.floorName = ''
  floorForm.sortOrder = floors.value.length ? Math.max(...floors.value.map((f) => f.sortOrder)) + 1 : 1
  floorDialog.value = true
}

function openEditFloorFromTab(): void {
  const f = currentFloorTab.value
  if (!f) return
  floorDialogMode.value = 'edit'
  editingFloorId.value = f.id
  floorForm.floorName = f.floorName
  floorForm.sortOrder = f.sortOrder
  floorDialog.value = true
}

async function submitFloor(): Promise<void> {
  if (!floorForm.floorName.trim()) {
    ElMessage.warning('请输入楼层名称')
    return
  }
  floorSubmitting.value = true
  try {
    const payload = { floorName: floorForm.floorName.trim(), sortOrder: floorForm.sortOrder }
    if (floorDialogMode.value === 'add') {
      const id = await createFloor(payload)
      ElMessage.success('新增成功')
      floorDialog.value = false
      activeFloorId.value = String(id)
      await refreshFloorsAndDetail()
    } else if (editingFloorId.value != null) {
      await updateFloor(editingFloorId.value, payload)
      ElMessage.success('保存成功')
      floorDialog.value = false
      await refreshFloorsAndDetail()
    }
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  } finally {
    floorSubmitting.value = false
  }
}

async function onDeleteFloorInDialog(): Promise<void> {
  if (editingFloorId.value == null) return
  const f = floors.value.find((x) => x.id === editingFloorId.value)
  if (f?.hasRooms) return
  try {
    await ElMessageBox.confirm('确认删除该楼层？', '确认删除', { type: 'warning' })
    await deleteFloor(editingFloorId.value)
    ElMessage.success('已删除')
    floorDialog.value = false
    await refreshFloorsAndDetail()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

async function openAddRoom(): Promise<void> {
  if (!activeFloorId.value) {
    ElMessage.warning('请先选择楼层')
    return
  }
  roomTypeOptions.value = await listEnabledRoomTypeOptions()
  if (!roomTypeOptions.value.length) {
    ElMessage.warning('暂无启用房型，请先在房型设置中启用房型')
    return
  }
  roomDialogMode.value = 'add'
  editingRoomId.value = null
  roomForm.roomNo = ''
  roomForm.roomTypeId = roomTypeOptions.value[0].id
  roomForm.sortOrder = 1
  roomDialog.value = true
}

async function openEditRoom(row: RoomCard): Promise<void> {
  roomTypeOptions.value = await listEnabledRoomTypeOptions()
  roomDialogMode.value = 'edit'
  editingRoomId.value = row.id
  roomForm.roomNo = row.roomNo
  roomForm.roomTypeId = row.roomTypeId
  roomForm.sortOrder = row.sortOrder
  roomDialog.value = true
}

async function submitRoom(): Promise<void> {
  if (!roomForm.roomNo.trim() || roomForm.roomTypeId == null) {
    ElMessage.warning('请完整填写')
    return
  }
  const payload = {
    roomNo: roomForm.roomNo.trim(),
    roomTypeId: roomForm.roomTypeId,
    sortOrder: roomForm.sortOrder,
  }
  roomSubmitting.value = true
  try {
    if (roomDialogMode.value === 'add') {
      await createRoom(Number(activeFloorId.value), payload)
      ElMessage.success('新增成功')
    } else if (editingRoomId.value != null) {
      await updateRoom(editingRoomId.value, payload)
      ElMessage.success('保存成功')
    }
    roomDialog.value = false
    await refreshFloorsAndDetail()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  } finally {
    roomSubmitting.value = false
  }
}

async function onDeleteRoom(row: RoomCard): Promise<void> {
  if (!row.canDeleteRoom) return
  try {
    await ElMessageBox.confirm('确认删除该房间？', '提示', { type: 'warning' })
    await deleteRoom(row.id)
    ElMessage.success('已删除')
    await refreshFloorsAndDetail()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function openAddBed(roomId: number): void {
  bedDialogMode.value = 'add'
  bedRoomId.value = roomId
  editingBedId.value = null
  bedForm.bedName = ''
  bedForm.sortOrder = 1
  bedDialog.value = true
}

function openEditBed(b: BedItem, roomId: number): void {
  bedDialogMode.value = 'edit'
  bedRoomId.value = roomId
  editingBedId.value = b.id
  bedForm.bedName = b.bedName
  bedForm.sortOrder = b.sortOrder
  bedDialog.value = true
}

async function submitBed(): Promise<void> {
  if (!bedForm.bedName.trim()) {
    ElMessage.warning('请输入床位名称')
    return
  }
  bedSubmitting.value = true
  try {
    const payload = { bedName: bedForm.bedName.trim(), sortOrder: bedForm.sortOrder }
    if (bedDialogMode.value === 'add' && bedRoomId.value != null) {
      await createBed(bedRoomId.value, payload)
      ElMessage.success('新增成功')
    } else if (bedDialogMode.value === 'edit' && editingBedId.value != null) {
      await updateBed(editingBedId.value, payload)
      ElMessage.success('保存成功')
    }
    bedDialog.value = false
    await refreshFloorsAndDetail()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '操作失败')
  } finally {
    bedSubmitting.value = false
  }
}

async function onDeleteBed(b: BedItem): Promise<void> {
  if (!b.canDelete) return
  try {
    await ElMessageBox.confirm('确认删除该床位？', '提示', { type: 'warning' })
    await deleteBed(b.id)
    ElMessage.success('已删除')
    await refreshFloorsAndDetail()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e instanceof Error ? e.message : '删除失败')
  }
}

function statusLabel(status: number): string {
  if (status === 2) return '已入住'
  if (status === 3) return '请假中'
  return '空闲'
}

function elderDisplay(b: BedItem): string {
  if (b.status === 1) return '空闲'
  return b.elderName || '—'
}

onMounted(() => {
  void refreshFloorsAndDetail()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="4-2" class="menu" @select="onMenuSelect">
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
      <el-main class="main" v-loading="loading">
        <el-card class="page-card">
          <div class="top-bar">
            <div class="tabs-row">
              <el-tabs v-if="floors.length" v-model="activeFloorId" class="floor-tabs" @tab-change="onTabChange">
                <el-tab-pane v-for="f in floors" :key="f.id" :label="f.floorName" :name="String(f.id)" />
              </el-tabs>
              <span v-else class="muted">暂无楼层，请先新增楼层</span>
              <el-button
                v-if="activeFloorId"
                text
                type="primary"
                class="edit-floor-btn"
                :icon="Edit"
                @click="openEditFloorFromTab"
              />
            </div>
            <div class="actions">
              <el-button type="primary" @click="openAddFloor">新增楼层</el-button>
              <el-button type="primary" @click="openAddRoom">新增房间</el-button>
            </div>
          </div>

          <div class="legend">
            <span class="leg-item"><i class="bed-dot s1" /> 空闲</span>
            <span class="leg-item"><i class="bed-dot s2" /> 已入住</span>
            <span class="leg-item"><i class="bed-dot s3" /> 请假中</span>
          </div>

          <div v-if="detail" class="room-grid">
            <el-card v-for="room in detail.rooms" :key="room.id" class="room-card" shadow="never">
              <div class="room-head">
                <span class="room-title">房间号: {{ room.roomNo }}</span>
                <div class="room-actions">
                  <el-button link type="primary" @click="openEditRoom(room)">编辑</el-button>
                  <el-button link type="danger" :disabled="!room.canDeleteRoom" @click="onDeleteRoom(room)">删除</el-button>
                  <el-button type="primary" size="small" @click="openAddBed(room.id)">新增床位</el-button>
                </div>
              </div>
              <div class="room-meta muted">房型：{{ room.roomTypeName }}</div>
              <div v-if="!room.beds.length" class="empty-beds">当前房间没有安排床位</div>
              <div v-else class="bed-grid">
                <div
                  v-for="b in room.beds"
                  :key="b.id"
                  class="bed-cell"
                  @mouseenter="hoverBedId = b.id"
                  @mouseleave="hoverBedId = null"
                >
                  <div class="bed-overlay" v-show="hoverBedId === b.id">
                    <el-button circle size="small" :icon="Edit" @click="openEditBed(b, room.id)" />
                    <el-button circle size="small" type="danger" :disabled="!b.canDelete" @click="onDeleteBed(b)">
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </div>
                  <div class="bed-icon" :class="'st' + b.status" />
                  <div class="bed-no">床位号 {{ b.bedName }}</div>
                  <div class="bed-elder">老人姓名 {{ elderDisplay(b) }}</div>
                  <div class="bed-st muted">{{ statusLabel(b.status) }}</div>
                </div>
              </div>
            </el-card>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="floorDialog" :title="floorDialogMode === 'add' ? '新增楼层' : '编辑楼层'" width="420px" destroy-on-close>
    <el-form label-width="88px">
      <el-form-item label="楼层名称" required>
        <el-input v-model="floorForm.floorName" maxlength="5" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="排序" required>
        <el-input-number v-model="floorForm.sortOrder" :min="1" :step="1" controls-position="right" style="width: 100%" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="floorDialog = false">取消</el-button>
      <el-button v-if="floorDialogMode === 'edit'" type="danger" :disabled="editingFloorHasRooms" @click="onDeleteFloorInDialog">
        删除
      </el-button>
      <el-button type="primary" :loading="floorSubmitting" @click="submitFloor">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="roomDialog" :title="roomDialogMode === 'add' ? '新增房间' : '编辑房间'" width="440px" destroy-on-close>
    <el-form label-width="88px">
      <el-form-item label="房间号" required>
        <el-input v-model="roomForm.roomNo" maxlength="5" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="房间类型" required>
        <el-select v-model="roomForm.roomTypeId" placeholder="请选择" style="width: 100%">
          <el-option v-for="o in roomTypeOptions" :key="o.id" :label="o.roomName" :value="o.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="排序" required>
        <el-input-number v-model="roomForm.sortOrder" :min="1" :step="1" controls-position="right" style="width: 100%" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="roomDialog = false">取消</el-button>
      <el-button type="primary" :loading="roomSubmitting" @click="submitRoom">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="bedDialog" :title="bedDialogMode === 'add' ? '新增床位' : '编辑床位'" width="400px" destroy-on-close>
    <el-form label-width="88px">
      <el-form-item label="床位名称" required>
        <el-input v-model="bedForm.bedName" maxlength="10" show-word-limit placeholder="请输入" />
      </el-form-item>
      <el-form-item label="排序" required>
        <el-input-number v-model="bedForm.sortOrder" :min="1" :step="1" controls-position="right" style="width: 100%" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="bedDialog = false">取消</el-button>
      <el-button type="primary" :loading="bedSubmitting" @click="submitBed">确定</el-button>
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
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}
.tabs-row {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
  min-width: 200px;
}
.floor-tabs {
  flex: 1;
  min-width: 0;
}
.edit-floor-btn {
  flex-shrink: 0;
}
.actions {
  display: flex;
  gap: 8px;
}
.legend {
  display: flex;
  gap: 20px;
  margin-bottom: 12px;
  font-size: 13px;
  color: #606266;
}
.leg-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}
.bed-dot {
  display: inline-block;
  width: 18px;
  height: 14px;
  border-radius: 3px;
}
.bed-dot.s1 {
  background: #c0c4cc;
}
.bed-dot.s2 {
  background: #67c23a;
}
.bed-dot.s3 {
  background: #e6a23c;
}
.room-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 12px;
}
.room-card {
  border: 1px solid #ebeef5;
}
.room-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
  flex-wrap: wrap;
}
.room-title {
  font-weight: 600;
  color: #303133;
}
.room-actions {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-wrap: wrap;
}
.room-meta {
  font-size: 12px;
  margin-bottom: 8px;
}
.muted {
  color: #909399;
}
.empty-beds {
  color: #909399;
  font-size: 13px;
  padding: 12px 0;
}
.bed-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.bed-cell {
  position: relative;
  width: 128px;
  padding: 8px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  background: #fafafa;
}
.bed-overlay {
  position: absolute;
  inset: 0;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: rgba(0, 0, 0, 0.45);
  border-radius: 6px;
}
.bed-icon {
  width: 48px;
  height: 36px;
  border-radius: 6px;
  margin-bottom: 6px;
}
.bed-icon.st1 {
  background: #c0c4cc;
}
.bed-icon.st2 {
  background: #67c23a;
}
.bed-icon.st3 {
  background: #e6a23c;
}
.bed-no {
  font-size: 12px;
  color: #303133;
}
.bed-elder {
  font-size: 12px;
  color: #606266;
  margin-top: 2px;
  word-break: break-all;
}
.bed-st {
  font-size: 11px;
  margin-top: 2px;
}
</style>
