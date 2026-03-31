<script setup lang="ts">
import { Refresh } from '@element-plus/icons-vue'
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useLayoutStore } from '../stores/layout'
import {
  fetchIotDeviceDetail,
  fetchIotTslModules,
  fetchIotTslPropertyHistory,
  fetchIotTslRunningStatus,
  fetchIotTslServiceInvocations,
} from '../api/iot'
import type { IotDeviceDetail, IotTslModuleItem, IotTslRuntimeRow } from '../types/iot'

const route = useRoute()
const router = useRouter()
const layoutStore = useLayoutStore()
const loading = ref(false)
const detail = ref<IotDeviceDetail | null>(null)
const activeTab = ref('detail')
const thingSubTab = ref('running')

const tslLoading = ref(false)
const modules = ref<IotTslModuleItem[]>([])
const selectedModuleId = ref<number | null>(null)
const runtimeRows = ref<IotTslRuntimeRow[]>([])
const runtimeTotal = ref(0)
const runtimePage = ref(1)
const runtimePageSize = ref(10)

const dialogVisible = ref(false)
const viewIdentifier = ref('')
const historyRange = ref<'1h' | '24h' | '7d' | 'custom'>('1h')
const customRange = ref<[string, string] | null>(null)
const historyLoading = ref(false)
const historyRows = ref<{ rawValue: string; reportTime: string }[]>([])
const historyTotal = ref(0)
const historyPage = ref(1)
const historyPageSize = ref(5)

const serviceRange = ref<'1h' | '24h' | '7d' | 'custom'>('1h')
const serviceCustomRange = ref<[string, string] | null>(null)
const serviceLoading = ref(false)
const serviceRows = ref<
  { invokeTime: string; serviceIdentifier: string; serviceName: string; inputParams: string; outputParams: string }[]
>([])
const serviceTotal = ref(0)

const serviceShowNextBtn = computed(() => serviceTotal.value > 10)
const serviceNextDisabled = computed(() => serviceRows.value.length >= serviceTotal.value)

const deviceId = () => Number(route.params.id)

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

function dash(v: string | undefined | null): string {
  if (v == null || String(v).trim() === '') return '-'
  return String(v)
}

function goBack(): void {
  void router.push('/iot/devices')
}

function runtimeIndexMethod(index: number): number {
  return (runtimePage.value - 1) * runtimePageSize.value + index + 1
}

function historyIndexMethod(index: number): number {
  return (historyPage.value - 1) * historyPageSize.value + index + 1
}

function serviceIndexMethod(index: number): number {
  return index + 1
}

async function loadDetail(): Promise<void> {
  const id = deviceId()
  if (!Number.isFinite(id) || id < 1) {
    ElMessage.error('参数错误')
    void router.push('/iot/devices')
    return
  }
  loading.value = true
  try {
    detail.value = await fetchIotDeviceDetail(id)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
    void router.push('/iot/devices')
  } finally {
    loading.value = false
  }
}

async function loadThingSubData(): Promise<void> {
  if (thingSubTab.value === 'running') {
    await loadRunning()
  } else if (thingSubTab.value === 'service') {
    await loadServiceFirstPage()
  }
}

async function loadTslModules(): Promise<void> {
  const id = deviceId()
  try {
    modules.value = await fetchIotTslModules(id)
    if (modules.value.length > 0) {
      const first = modules.value[0]
      if (selectedModuleId.value == null || !modules.value.some((m) => m.id === selectedModuleId.value)) {
        selectedModuleId.value = first.id
      }
      await loadThingSubData()
    } else {
      selectedModuleId.value = null
      runtimeRows.value = []
      runtimeTotal.value = 0
      serviceRows.value = []
      serviceTotal.value = 0
    }
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载模块失败')
  }
}

async function loadServiceFirstPage(): Promise<boolean> {
  const id = deviceId()
  const mid = selectedModuleId.value
  if (mid == null) return false
  if (serviceRange.value === 'custom' && (!serviceCustomRange.value || serviceCustomRange.value.length !== 2)) {
    serviceRows.value = []
    serviceTotal.value = 0
    return false
  }
  let start: string | undefined
  let end: string | undefined
  if (serviceRange.value === 'custom' && serviceCustomRange.value) {
    start = serviceCustomRange.value[0]
    end = serviceCustomRange.value[1]
  }
  serviceLoading.value = true
  try {
    const data = await fetchIotTslServiceInvocations({
      deviceId: id,
      moduleId: mid,
      rangeType: serviceRange.value,
      start,
      end,
      offset: 0,
      limit: 10,
    })
    serviceRows.value = data.rows
    serviceTotal.value = data.total
    return true
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载服务调用失败')
    return false
  } finally {
    serviceLoading.value = false
  }
}

async function loadServiceMore(): Promise<void> {
  const id = deviceId()
  const mid = selectedModuleId.value
  if (mid == null) return
  if (serviceRows.value.length >= serviceTotal.value) return
  if (serviceRange.value === 'custom' && (!serviceCustomRange.value || serviceCustomRange.value.length !== 2)) return
  let start: string | undefined
  let end: string | undefined
  if (serviceRange.value === 'custom' && serviceCustomRange.value) {
    start = serviceCustomRange.value[0]
    end = serviceCustomRange.value[1]
  }
  serviceLoading.value = true
  try {
    const data = await fetchIotTslServiceInvocations({
      deviceId: id,
      moduleId: mid,
      rangeType: serviceRange.value,
      start,
      end,
      offset: serviceRows.value.length,
      limit: 10,
    })
    serviceRows.value = [...serviceRows.value, ...data.rows]
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载更多失败')
  } finally {
    serviceLoading.value = false
  }
}

async function refreshService(): Promise<void> {
  const ok = await loadServiceFirstPage()
  if (ok) ElMessage.success('刷新成功')
}

async function loadRunning(): Promise<void> {
  const id = deviceId()
  const mid = selectedModuleId.value
  if (mid == null) return
  tslLoading.value = true
  try {
    const data = await fetchIotTslRunningStatus(id, mid, runtimePage.value, runtimePageSize.value)
    runtimeRows.value = data.rows
    runtimeTotal.value = data.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载运行状态失败')
  } finally {
    tslLoading.value = false
  }
}

async function loadHistory(): Promise<void> {
  const id = deviceId()
  const mid = selectedModuleId.value
  if (mid == null || !viewIdentifier.value) return
  let start: string | undefined
  let end: string | undefined
  if (historyRange.value === 'custom') {
    if (!customRange.value || customRange.value.length !== 2) {
      historyRows.value = []
      historyTotal.value = 0
      return
    }
    start = customRange.value[0]
    end = customRange.value[1]
  }
  historyLoading.value = true
  try {
    const data = await fetchIotTslPropertyHistory({
      deviceId: id,
      moduleId: mid,
      identifier: viewIdentifier.value,
      rangeType: historyRange.value,
      start,
      end,
      page: historyPage.value,
      pageSize: historyPageSize.value,
    })
    historyRows.value = data.rows
    historyTotal.value = data.total
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载历史数据失败')
  } finally {
    historyLoading.value = false
  }
}

async function openViewData(row: IotTslRuntimeRow): Promise<void> {
  viewIdentifier.value = row.propIdentifier
  historyRange.value = '1h'
  customRange.value = null
  historyPage.value = 1
  historyPageSize.value = 5
  dialogVisible.value = true
  await loadHistory()
}

watch(activeTab, (v) => {
  if (v === 'thing') {
    thingSubTab.value = 'running'
    runtimePage.value = 1
    void loadTslModules()
  }
})

watch(thingSubTab, (v) => {
  if (activeTab.value !== 'thing') return
  if (v === 'running') {
    void loadRunning()
  } else if (v === 'service') {
    void loadServiceFirstPage()
  }
})

watch(selectedModuleId, () => {
  if (activeTab.value !== 'thing') return
  if (thingSubTab.value === 'running') {
    runtimePage.value = 1
    void loadRunning()
  } else if (thingSubTab.value === 'service') {
    void loadServiceFirstPage()
  }
})

watch([runtimePage, runtimePageSize], () => {
  if (activeTab.value === 'thing' && thingSubTab.value === 'running') {
    void loadRunning()
  }
})

watch([historyPage, historyPageSize], () => {
  if (dialogVisible.value) void loadHistory()
})

watch(historyRange, (r) => {
  if (!dialogVisible.value) return
  if (r !== 'custom') {
    customRange.value = null
    historyPage.value = 1
    void loadHistory()
  }
})

watch(customRange, (v) => {
  if (!dialogVisible.value || historyRange.value !== 'custom') return
  if (v && v.length === 2 && v[0] && v[1]) {
    historyPage.value = 1
    void loadHistory()
  }
})

watch(serviceRange, (r) => {
  if (activeTab.value !== 'thing' || thingSubTab.value !== 'service') return
  if (r !== 'custom') {
    serviceCustomRange.value = null
    void loadServiceFirstPage()
  } else {
    serviceRows.value = []
    serviceTotal.value = 0
  }
})

watch(serviceCustomRange, (v) => {
  if (activeTab.value !== 'thing' || thingSubTab.value !== 'service') return
  if (serviceRange.value !== 'custom') return
  if (v && v.length === 2 && v[0] && v[1]) void loadServiceFirstPage()
})

onMounted(() => {
  void loadDetail()
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
      <el-header class="header">
        <el-button text @click="layoutStore.toggleCollapsed">☰</el-button>
        <div class="crumb">智能检测 &gt; 设备管理 &gt; 设备详情</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main" v-loading="loading">
        <template v-if="detail">
          <el-card class="block-card" shadow="never">
            <template #header><span class="block-title">设备信息</span></template>
            <el-descriptions :column="2" border class="info-desc">
              <el-descriptions-item label="设备名称">{{ detail.deviceName }}</el-descriptions-item>
              <el-descriptions-item label="所属产品">{{ detail.productName }}</el-descriptions-item>
              <el-descriptions-item label="DeviceSecret">
                <span class="mono">{{ detail.deviceSecret }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="ProductKey">
                <span class="mono">{{ detail.productKey }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="设备状态">
                <el-tag v-if="detail.onlineStatus === 1" type="success" effect="light">在线</el-tag>
                <el-tag v-else type="info" effect="light">离线</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="接入位置">{{ detail.accessLocationDisplay }}</el-descriptions-item>
            </el-descriptions>
          </el-card>

          <el-card class="block-card tab-card" shadow="never">
            <el-tabs v-model="activeTab">
              <el-tab-pane label="设备详情" name="detail">
                <el-descriptions :column="1" border class="detail-desc">
                  <el-descriptions-item label="设备名称">{{ detail.deviceName }}</el-descriptions-item>
                  <el-descriptions-item label="备注名称">{{ detail.remarkName }}</el-descriptions-item>
                  <el-descriptions-item label="设备类型">{{ detail.deviceType }}</el-descriptions-item>
                  <el-descriptions-item label="地域">{{ dash(detail.region) }}</el-descriptions-item>
                  <el-descriptions-item label="节点类型">{{ dash(detail.nodeType) }}</el-descriptions-item>
                  <el-descriptions-item label="认证方式">{{ dash(detail.authMethod) }}</el-descriptions-item>
                  <el-descriptions-item label="IP地址">{{ dash(detail.ipAddress) }}</el-descriptions-item>
                  <el-descriptions-item label="固件版本">{{ dash(detail.firmwareVersion) }}</el-descriptions-item>
                  <el-descriptions-item label="创建人">{{ dash(detail.creatorName) }}</el-descriptions-item>
                  <el-descriptions-item label="创建时间">{{ detail.createdTime }}</el-descriptions-item>
                  <el-descriptions-item label="激活时间">{{ dash(detail.activationTime) }}</el-descriptions-item>
                </el-descriptions>
              </el-tab-pane>
              <el-tab-pane label="物模型数据" name="thing">
                <el-tabs v-model="thingSubTab" class="inner-tabs">
                  <el-tab-pane label="运行状态" name="running">
                    <div class="tsl-layout">
                      <div class="tsl-side">
                        <div class="tsl-side-title">默认模块</div>
                        <div
                          v-for="m in modules"
                          :key="m.id"
                          class="mod-item"
                          :class="{ active: selectedModuleId === m.id }"
                          @click="selectedModuleId = m.id"
                        >
                          <div class="mod-name">{{ m.moduleName }}</div>
                          <div class="mod-key">标识符: {{ m.moduleKey }}</div>
                        </div>
                        <div v-if="modules.length === 0" class="mod-empty">暂无模块</div>
                      </div>
                      <div class="tsl-main">
                        <el-table :data="runtimeRows" border stripe v-loading="tslLoading" empty-text="暂无数据">
                          <el-table-column type="index" :index="runtimeIndexMethod" label="序号" width="64" />
                          <el-table-column prop="propIdentifier" label="标识符" min-width="140" show-overflow-tooltip />
                          <el-table-column prop="functionName" label="功能名称" min-width="120" />
                          <el-table-column prop="updateTime" label="更新时间" min-width="170" />
                          <el-table-column prop="dataValue" label="数据值" min-width="120" show-overflow-tooltip />
                          <el-table-column label="操作" width="100" fixed="right">
                            <template #default="{ row }">
                              <el-button type="primary" link @click="openViewData(row)">查看数据</el-button>
                            </template>
                          </el-table-column>
                        </el-table>
                        <div class="pager-wrap">
                          <el-pagination
                            v-model:current-page="runtimePage"
                            v-model:page-size="runtimePageSize"
                            :total="runtimeTotal"
                            :page-sizes="[10, 20, 50, 100]"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                          />
                        </div>
                      </div>
                    </div>
                  </el-tab-pane>
                  <el-tab-pane label="服务调用" name="service">
                    <div class="tsl-layout">
                      <div class="tsl-side">
                        <div class="tsl-side-title">默认模块</div>
                        <div
                          v-for="m in modules"
                          :key="'svc-' + m.id"
                          class="mod-item"
                          :class="{ active: selectedModuleId === m.id }"
                          @click="selectedModuleId = m.id"
                        >
                          <div class="mod-name">{{ m.moduleName }}</div>
                          <div class="mod-key">标识符: {{ m.moduleKey }}</div>
                        </div>
                        <div v-if="modules.length === 0" class="mod-empty">暂无模块</div>
                      </div>
                      <div class="tsl-main">
                        <div class="service-toolbar">
                          <div class="service-toolbar-left">
                            <span class="svc-label">时间范围</span>
                            <el-select v-model="serviceRange" style="width: 140px">
                              <el-option label="1小时" value="1h" />
                              <el-option label="24小时" value="24h" />
                              <el-option label="7天" value="7d" />
                              <el-option label="自定义" value="custom" />
                            </el-select>
                            <el-date-picker
                              v-if="serviceRange === 'custom'"
                              v-model="serviceCustomRange"
                              type="datetimerange"
                              range-separator="至"
                              start-placeholder="开始"
                              end-placeholder="结束"
                              value-format="YYYY-MM-DD HH:mm:ss"
                              style="width: 360px; margin-left: 8px"
                            />
                          </div>
                          <el-button circle text type="primary" title="刷新" @click="refreshService">
                            <el-icon :size="18"><Refresh /></el-icon>
                          </el-button>
                        </div>
                        <el-table
                          :data="serviceRows"
                          border
                          stripe
                          v-loading="serviceLoading"
                          empty-text="暂无数据"
                        >
                          <el-table-column type="index" :index="serviceIndexMethod" label="序号" width="64" />
                          <el-table-column prop="invokeTime" label="时间" min-width="170" />
                          <el-table-column prop="serviceIdentifier" label="标识符" min-width="120" show-overflow-tooltip />
                          <el-table-column prop="serviceName" label="服务名称" min-width="120" />
                          <el-table-column prop="inputParams" label="输入参数" min-width="100" show-overflow-tooltip />
                          <el-table-column prop="outputParams" label="输出参数" min-width="220" show-overflow-tooltip />
                        </el-table>
                        <div v-if="serviceShowNextBtn" class="service-next-wrap">
                          <el-button type="primary" :disabled="serviceNextDisabled" @click="loadServiceMore">
                            下一页
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </el-tab-pane>
            </el-tabs>
          </el-card>

          <div class="footer-actions">
            <el-button type="primary" @click="goBack">返回</el-button>
          </div>
        </template>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="dialogVisible" title="查看数据" width="720px" destroy-on-close @closed="historyRows = []">
    <div class="dialog-toolbar">
      <span class="dlg-label">时间范围</span>
      <el-select v-model="historyRange" style="width: 140px">
        <el-option label="1小时" value="1h" />
        <el-option label="24小时" value="24h" />
        <el-option label="7天" value="7d" />
        <el-option label="自定义" value="custom" />
      </el-select>
      <el-date-picker
        v-if="historyRange === 'custom'"
        v-model="customRange"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始"
        end-placeholder="结束"
        value-format="YYYY-MM-DD HH:mm:ss"
        style="width: 360px; margin-left: 8px"
      />
    </div>
    <el-table :data="historyRows" border stripe v-loading="historyLoading" class="dlg-table">
      <el-table-column type="index" :index="historyIndexMethod" label="序号" width="64" />
      <el-table-column prop="rawValue" label="原始值" min-width="140" />
      <el-table-column prop="reportTime" label="时间" min-width="170" />
    </el-table>
    <div class="pager-wrap dlg-pager">
      <el-pagination
        v-model:current-page="historyPage"
        v-model:page-size="historyPageSize"
        :total="historyTotal"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        background
      />
    </div>
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
.block-card { margin-bottom: 12px; border-radius: 4px; }
.tab-card :deep(.el-card__body) { padding-top: 8px; }
.block-title { font-weight: 600; font-size: 15px; color: #303133; }
.info-desc :deep(.el-descriptions__label) { width: 120px; }
.detail-desc :deep(.el-descriptions__label) { width: 120px; }
.mono { font-family: ui-monospace, Consolas, monospace; font-size: 13px; word-break: break-all; }
.footer-actions { display: flex; justify-content: center; padding: 16px 0 24px; }
.inner-tabs :deep(.el-tabs__header) { margin-bottom: 12px; }
.tsl-layout { display: flex; gap: 12px; min-height: 320px; }
.tsl-side { width: 220px; flex-shrink: 0; border: 1px solid #ebeef5; border-radius: 4px; padding: 8px; background: #fafafa; }
.tsl-side-title { font-weight: 600; margin-bottom: 8px; color: #303133; }
.mod-item { padding: 10px 8px; border-radius: 4px; cursor: pointer; margin-bottom: 6px; border: 1px solid transparent; }
.mod-item:hover { background: #ecf5ff; }
.mod-item.active { background: #ecf5ff; border-color: #b3d8ff; }
.mod-name { font-size: 13px; color: #303133; line-height: 1.4; }
.mod-key { font-size: 12px; color: #909399; margin-top: 4px; }
.mod-empty { color: #909399; font-size: 13px; padding: 8px; }
.tsl-main { flex: 1; min-width: 0; }
.pager-wrap { display: flex; justify-content: flex-end; padding-top: 12px; }
.dialog-toolbar { display: flex; align-items: center; margin-bottom: 12px; flex-wrap: wrap; gap: 8px; }
.dlg-label { color: #606266; font-size: 14px; }
.dlg-table { margin-top: 4px; }
.dlg-pager { margin-top: 12px; }
.service-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  flex-wrap: wrap;
  gap: 8px;
}
.service-toolbar-left {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.svc-label {
  color: #606266;
  font-size: 14px;
}
.service-next-wrap {
  display: flex;
  justify-content: center;
  padding-top: 16px;
}
</style>
