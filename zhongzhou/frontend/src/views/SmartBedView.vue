<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useLayoutStore } from '../stores/layout'
import { listSmartFloors, getSmartFloorView } from '../api/smartBed'
import type { SmartFloorTab, SmartFloorView } from '../types/smartBed'
import RoomDeviceIcon from '../components/smart-bed/RoomDeviceIcon.vue'
import BedDeviceIcon from '../components/smart-bed/BedDeviceIcon.vue'
import PresenceIcon from '../components/smart-bed/PresenceIcon.vue'
import MetricIcon from '../components/smart-bed/MetricIcon.vue'

const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const floors = ref<SmartFloorTab[]>([])
const activeFloorId = ref('')
const detail = ref<SmartFloorView | null>(null)

/** 附件图1：烟感 / 摄像机 / 门窗；温湿度→摄像机位；声光→告警形 */
function roomIconKind(t?: string | null): 'smoke' | 'camera' | 'door' | 'alarm' {
  switch (t) {
    case 'smoke':
      return 'smoke'
    case 'temp':
      return 'camera'
    case 'door':
      return 'door'
    case 'alarm':
      return 'alarm'
    default:
      return 'smoke'
  }
}

/** 附件图5：身份识别、手环、床、告警 */
function bedIconKind(t?: string | null): 'id' | 'wear' | 'bed' | 'alarm' {
  switch (t) {
    case 'id':
      return 'id'
    case 'wear':
      return 'wear'
    case 'bed':
      return 'bed'
    case 'alarm':
      return 'alarm'
    default:
      return 'bed'
  }
}

async function loadFloorsAndDetail(): Promise<void> {
  loading.value = true
  try {
    floors.value = await listSmartFloors()
    if (!floors.value.length) {
      detail.value = null
      activeFloorId.value = ''
      return
    }
    if (!activeFloorId.value || !floors.value.some((f) => String(f.id) === activeFloorId.value)) {
      activeFloorId.value = String(floors.value[0].id)
    }
    detail.value = await getSmartFloorView(Number(activeFloorId.value))
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

async function onTabChange(name: string | number): Promise<void> {
  activeFloorId.value = String(name)
  loading.value = true
  try {
    detail.value = await getSmartFloorView(Number(name))
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
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

onMounted(() => {
  void loadFloorsAndDetail()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="4-3" class="menu" @select="onMenuSelect">
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
        <div class="crumb">在住管理 / 智能床位</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main" v-loading="loading">
        <el-card class="page-card" shadow="never">
          <div class="smart-title">智能床位</div>
          <div v-if="floors.length" class="floor-tabs-wrap">
            <el-tabs v-model="activeFloorId" class="floor-tabs" @tab-change="onTabChange">
              <el-tab-pane v-for="f in floors" :key="f.id" :name="String(f.id)">
                <template #label>
                  <span class="floor-tab-label">
                    {{ f.floorName }}
                    <i v-if="f.hasAnomaly" class="floor-anomaly-dot" aria-hidden="true" />
                  </span>
                </template>
              </el-tab-pane>
            </el-tabs>
          </div>
          <div v-else class="empty-tip">当前没有绑定设备的楼层</div>

          <div v-if="detail && detail.rooms.length" class="rooms-wrap">
            <section v-for="room in detail.rooms" :key="room.roomId" class="smart-room">
              <div class="smart-room-head">
                <div class="smart-room-left">
                  <span class="room-no-text">房间号: {{ room.roomNo }}</span>
                  <div class="room-device-icons">
                    <el-tooltip
                      v-for="d in room.roomDevices"
                      :key="d.deviceName + d.productCategory"
                      placement="top"
                      effect="dark"
                    >
                      <template #content>
                        <div>{{ d.productCategory }}</div>
                        <div>{{ d.deviceName }}</div>
                      </template>
                      <RoomDeviceIcon class="dev-icon-slot" :kind="roomIconKind(d.iconType)" :size="32" />
                    </el-tooltip>
                  </div>
                </div>
                <div class="smart-room-right">
                  <span>房门状态: {{ room.doorStatus }}</span>
                  <span>温度: {{ room.temperature }}</span>
                  <span>湿度: {{ room.humidity }}</span>
                  <span>报警状态: {{ room.alarmStatus }}</span>
                </div>
              </div>
              <div v-if="room.roomMessage" class="room-banner">{{ room.roomMessage }}</div>
              <div v-if="room.beds.length" class="bed-grid">
                <div
                  v-for="bed in room.beds"
                  :key="bed.bedId"
                  class="bed-card"
                  :class="{ 'bed-card--alarm': bed.alarm }"
                >
                  <div class="bed-card-top">
                    <div class="bed-card-titles">
                      <div>床位号: {{ bed.bedName }}</div>
                      <div>老人姓名: {{ bed.elderName }}</div>
                    </div>
                    <div class="bed-device-icons">
                      <el-tooltip
                        v-for="d in bed.devices"
                        :key="d.deviceName + d.productCategory"
                        placement="top"
                        effect="dark"
                      >
                        <template #content>
                          <div>{{ d.productCategory }}</div>
                          <div>{{ d.deviceName }}</div>
                        </template>
                        <BedDeviceIcon class="dev-icon-slot sm" :kind="bedIconKind(d.iconType)" :size="22" />
                      </el-tooltip>
                    </div>
                  </div>
                  <div v-if="bed.message" class="bed-msg">{{ bed.message }}</div>
                  <div v-else class="bed-card-body">
                    <div class="bed-presence">
                      <PresenceIcon
                        v-if="bed.presenceLabel"
                        class="presence-icon"
                        :label="bed.presenceLabel"
                        :size="56"
                      />
                      <div class="presence-txt">{{ bed.presenceLabel }}</div>
                    </div>
                    <div class="bed-metrics">
                      <template v-if="bed.showHeartBreath">
                        <div class="metric-row">
                          <MetricIcon kind="hr" :size="24" />
                          <span>心率: {{ bed.heartRate ?? '-' }}次/分</span>
                        </div>
                        <div class="metric-row">
                          <MetricIcon kind="br" :size="24" />
                          <span>呼吸率: {{ bed.breathRate ?? '-' }}次/分</span>
                        </div>
                      </template>
                      <template v-else-if="bed.showLeftBed">
                        <div class="metric-row">
                          <MetricIcon kind="left-count" :size="24" />
                          <span>离床次数: {{ bed.leftBedCount ?? '-' }}次</span>
                        </div>
                        <div class="metric-row">
                          <MetricIcon kind="left-time" :size="24" />
                          <span>离床时间: {{ bed.leftBedTime ?? '-' }}</span>
                        </div>
                      </template>
                    </div>
                  </div>
                </div>
              </div>
            </section>
          </div>
          <div v-else-if="detail && !detail.rooms.length" class="empty-tip">本楼层暂无房间数据</div>
        </el-card>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.page-root {
  min-height: 100vh;
}
.sider {
  border-right: 1px solid #ebeef5;
  background: #fff;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px;
  font-weight: 600;
}
.brand-text {
  font-size: 16px;
}
.menu {
  border-right: none;
}
.header {
  display: flex;
  align-items: center;
  gap: 16px;
  border-bottom: 1px solid #ebeef5;
  background: #fff;
}
.crumb {
  flex: 1;
  color: #606266;
  font-size: 14px;
}
.admin {
  color: #909399;
  font-size: 13px;
}
.main {
  background: #f5f7fa;
  padding: 16px;
}
.page-card {
  border-radius: 8px;
}
.smart-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #303133;
}
.floor-tabs-wrap {
  margin-bottom: 20px;
}
.floor-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
}
.floor-tab-label {
  position: relative;
  padding-right: 10px;
}
.floor-anomaly-dot {
  position: absolute;
  top: 2px;
  right: 0;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #f56c6c;
}
.empty-tip {
  color: #909399;
  padding: 24px 0;
}
.rooms-wrap {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.smart-room {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 16px;
}
.smart-room-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 8px;
}
.smart-room-left {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}
.room-no-text {
  font-weight: 600;
  color: #303133;
}
.room-device-icons,
.bed-device-icons {
  display: flex;
  align-items: center;
  gap: 8px;
}
.dev-icon-slot {
  cursor: default;
  display: inline-flex;
  vertical-align: middle;
}
.smart-room-right {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 13px;
  color: #606266;
}
.room-banner {
  background: #fdf6ec;
  color: #e6a23c;
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 13px;
  margin-bottom: 12px;
}
.bed-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}
@media (max-width: 1100px) {
  .bed-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
@media (max-width: 640px) {
  .bed-grid {
    grid-template-columns: 1fr;
  }
}
.bed-card {
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 12px;
  background: #fafafa;
}
.bed-card--alarm {
  background: #fef0f0;
  border-color: #fbc4c4;
}
.bed-card-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 8px;
  margin-bottom: 10px;
  font-size: 13px;
  color: #303133;
}
.bed-card-titles {
  line-height: 1.6;
}
.bed-msg {
  text-align: center;
  color: #606266;
  padding: 20px 8px;
  font-size: 13px;
}
.bed-card-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}
.bed-presence {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  min-width: 72px;
}
.presence-icon {
  flex-shrink: 0;
}
.presence-txt {
  font-size: 12px;
  color: #409eff;
  font-weight: 500;
}
.bed-card--alarm .presence-txt {
  color: #f56c6c;
}
.bed-metrics {
  flex: 1;
  font-size: 13px;
  color: #303133;
  line-height: 1.8;
}
.metric-row {
  display: flex;
  align-items: center;
  gap: 8px;
}
.bed-card--alarm .bed-metrics {
  color: #c45656;
}
</style>
