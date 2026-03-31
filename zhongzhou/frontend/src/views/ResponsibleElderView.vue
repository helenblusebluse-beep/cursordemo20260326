<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import {
  queryBedCaregivers,
  queryCaregiverOptions,
  queryResponsibleFloors,
  queryResponsibleFloorView,
  queryRoomCaregivers,
  saveBedCaregivers,
  saveRoomCaregivers,
} from '../api/responsible'
import type { ResponsibleFloorTab, ResponsibleFloorView } from '../types/responsible'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const floors = ref<ResponsibleFloorTab[]>([])
const activeFloorId = ref('')
const detail = ref<ResponsibleFloorView | null>(null)
const caregiverOptions = ref<string[]>([])

const dialogVisible = ref(false)
const dialogMode = ref<'bed' | 'room'>('bed')
const currentBedId = ref<number | null>(null)
const currentRoomId = ref<number | null>(null)
const checkedNames = ref<string[]>([])
const saving = ref(false)

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

async function loadAll(): Promise<void> {
  loading.value = true
  try {
    const [fs, options] = await Promise.all([queryResponsibleFloors(), queryCaregiverOptions()])
    floors.value = fs
    caregiverOptions.value = options
    if (!floors.value.length) {
      detail.value = null
      activeFloorId.value = ''
      return
    }
    if (!activeFloorId.value) activeFloorId.value = String(floors.value[0].id)
    detail.value = await queryResponsibleFloorView(Number(activeFloorId.value))
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

async function onTabChange(tab: string | number): Promise<void> {
  activeFloorId.value = String(tab)
  loading.value = true
  try {
    detail.value = await queryResponsibleFloorView(Number(tab))
  } finally {
    loading.value = false
  }
}

function displayNames(names: string[]): string {
  return names.length ? names.slice(0, 4).join('、') : '当前床位没有安排护理员'
}

async function openBedDialog(bedId: number): Promise<void> {
  dialogMode.value = 'bed'
  currentBedId.value = bedId
  currentRoomId.value = null
  checkedNames.value = await queryBedCaregivers(bedId)
  dialogVisible.value = true
}

async function openRoomDialog(roomId: number): Promise<void> {
  dialogMode.value = 'room'
  currentRoomId.value = roomId
  currentBedId.value = null
  checkedNames.value = await queryRoomCaregivers(roomId)
  dialogVisible.value = true
}

function onCheckChange(values: string[]): void {
  if (values.length > 4) {
    checkedNames.value = values.slice(0, 4)
  }
}

async function saveDialog(): Promise<void> {
  if (!checkedNames.value.length) {
    ElMessage.warning('请选择护理员姓名')
    return
  }
  saving.value = true
  try {
    if (dialogMode.value === 'bed' && currentBedId.value) {
      await saveBedCaregivers(currentBedId.value, checkedNames.value)
    } else if (dialogMode.value === 'room' && currentRoomId.value) {
      await saveRoomCaregivers(currentRoomId.value, checkedNames.value)
    }
    ElMessage.success('设置成功')
    dialogVisible.value = false
    await loadAll()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => { void loadAll() })
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="6-4" class="menu" @select="onMenuSelect">
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
      <el-main class="main" v-loading="loading">
        <el-card class="page-card">
          <el-tabs v-if="floors.length" v-model="activeFloorId" @tab-change="onTabChange">
            <el-tab-pane v-for="f in floors" :key="f.id" :name="String(f.id)" :label="f.floorName" />
          </el-tabs>
          <div v-if="detail" class="room-list">
            <div class="room-block" v-for="room in detail.rooms" :key="room.roomId">
              <div class="room-head">
                <div class="room-no">房间号：{{ room.roomNo }}</div>
                <el-button type="primary" size="small" @click="openRoomDialog(room.roomId)">批量设置护理员</el-button>
              </div>
              <div class="bed-grid">
                <div class="bed-card" v-for="bed in room.beds" :key="bed.bedId">
                  <div class="bed-top"><span>床位号：{{ bed.bedName }}</span><el-button link type="primary" @click="openBedDialog(bed.bedId)">设置护理员</el-button></div>
                  <div class="line">老人姓名：{{ bed.elderName || '-' }}</div>
                  <div class="line">护理员姓名：{{ displayNames(bed.caregiverNames) }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="dialogVisible" title="设置护理员" width="520px" destroy-on-close>
    <el-form label-width="90px">
      <el-form-item label="*护理员姓名">
        <el-checkbox-group v-model="checkedNames" @change="onCheckChange">
          <el-checkbox v-for="name in caregiverOptions" :key="name" :label="name" :disabled="checkedNames.length >= 4 && !checkedNames.includes(name)" />
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible=false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="saveDialog">确定</el-button>
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
.room-list { display: flex; flex-direction: column; gap: 14px; }
.room-block { border-top: 1px solid #ebeef5; padding-top: 12px; }
.room-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.bed-grid { display: grid; grid-template-columns: repeat(3, minmax(260px, 1fr)); gap: 10px; }
.bed-card { background: #f9fafc; border: 1px solid #ebeef5; border-radius: 4px; padding: 10px; }
.bed-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; }
.line { color: #606266; line-height: 24px; }
</style>
