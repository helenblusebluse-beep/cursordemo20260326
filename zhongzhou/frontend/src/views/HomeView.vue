<script setup lang="ts">
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import { fetchDashboard } from '../api/dashboard'
import type { DashboardData } from '../types/dashboard'
import { useLayoutStore } from '../stores/layout'
import { useRouter } from 'vue-router'

const layoutStore = useLayoutStore()
const router = useRouter()
const loading = ref(false)
const dashboard = ref<DashboardData | null>(null)
const trendChartRef = ref<HTMLDivElement | null>(null)
const pieChartRef = ref<HTMLDivElement | null>(null)
const ageChartRef = ref<HTMLDivElement | null>(null)

function renderCharts(data: DashboardData): void {
  if (trendChartRef.value) {
    const chart = echarts.init(trendChartRef.value)
    chart.setOption({
      grid: { left: 36, right: 16, top: 20, bottom: 24 },
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: data.serviceTrend.map((it) => it.date) },
      yAxis: { type: 'value' },
      series: [
        {
          type: 'line',
          smooth: true,
          data: data.serviceTrend.map((it) => it.count),
          areaStyle: { opacity: 0.2 },
          lineStyle: { width: 3, color: '#409eff' },
          itemStyle: { color: '#409eff' },
        },
      ],
    })
  }

  if (pieChartRef.value) {
    const chart = echarts.init(pieChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'item' },
      legend: { right: 10, top: 'center', orient: 'vertical' },
      series: [
        {
          type: 'pie',
          radius: ['38%', '70%'],
          center: ['32%', '50%'],
          data: data.levelDistribution.map((it) => ({ name: it.name, value: it.value })),
        },
      ],
    })
  }

  if (ageChartRef.value) {
    const chart = echarts.init(ageChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { top: 4 },
      grid: { left: 36, right: 16, top: 36, bottom: 24 },
      xAxis: { type: 'category', data: data.ageDistribution.map((it) => it.range) },
      yAxis: { type: 'value' },
      series: [
        { type: 'bar', name: '男', data: data.ageDistribution.map((it) => it.male), itemStyle: { color: '#67c23a' } },
        { type: 'bar', name: '女', data: data.ageDistribution.map((it) => it.female), itemStyle: { color: '#409eff' } },
      ],
    })
  }
}

async function loadDashboard(): Promise<void> {
  loading.value = true
  try {
    dashboard.value = await fetchDashboard()
    renderCharts(dashboard.value)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  void loadDashboard()
})

function onMenuSelect(index: string): void {
  if (index === '1') {
    return
  }
  if (index === '2-1') {
    void router.push('/appointments/register')
    return
  }
  if (index === '2-2') {
    void router.push('/visits/register')
    return
  }
  if (index === '3-1') {
    void router.push('/health-assessments')
    return
  }
  if (index === '3-2') {
    void router.push('/checkins')
    return
  }
  if (index === '3-3') {
    void router.push('/checkouts')
    return
  }
  if (index === '4-1') {
    void router.push('/room-types')
    return
  }
  if (index === '4-2') {
    void router.push('/bed-preview')
    return
  }
  if (index === '4-3') {
    void router.push('/smart-beds')
    return
  }
  if (index === '4-4') {
    void router.push('/contracts')
    return
  }
  if (index === '4-5') {
    void router.push('/customers')
    return
  }
  if (index === '6-1') {
    void router.push('/nursing-items')
    return
  }
  if (index === '6-2') {
    void router.push('/nursing-plans')
    return
  }
  if (index === '6-3') {
    void router.push('/nursing-care-levels')
    return
  }
  if (index === '6-4') {
    void router.push('/responsible-elders')
    return
  }
  if (index === '6-5') {
    void router.push('/task-schedules')
    return
  }
  if (index === '7-1') {
    void router.push('/orders')
    return
  }
  if (index === '7-2') {
    void router.push('/refunds')
    return
  }
  if (index === '8-1') {
    void router.push('/bills/1')
    return
  }
  if (index === '8-2') {
    void router.push('/prepay-recharges')
    return
  }
  if (index === '8-3') {
    void router.push('/balance-queries')
    return
  }
  if (index === '8-4') {
    void router.push('/arrears-elders')
    return
  }
  if (index === '9-1') {
    void router.push('/iot/devices')
    return
  }
  if (index === '9-2') {
    void router.push('/iot/alarm-rules')
    return
  }
  if (index === '9-3') {
    void router.push('/iot/alarm-records')
  }
}
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="1" class="menu" @select="onMenuSelect">
        <el-menu-item index="1">首页</el-menu-item>
        <el-sub-menu index="2">
          <template #title>来访管理</template>
          <el-menu-item index="2-1">预约登记</el-menu-item>
          <el-menu-item index="2-2">来访登记</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>入退管理</template>
          <el-menu-item index="3-1">入住评估</el-menu-item>
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
        <el-sub-menu index="7">
          <template #title>订单查询</template>
          <el-menu-item index="7-1">订单管理</el-menu-item>
          <el-menu-item index="7-2">退款管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="8">
          <template #title>财务统计</template>
          <el-menu-item index="8-1">账单管理</el-menu-item>
          <el-menu-item index="8-2">预缴款充值</el-menu-item>
          <el-menu-item index="8-3">余额查询</el-menu-item>
          <el-menu-item index="8-4">欠费老人</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="9">
          <template #title>智能检测</template>
          <el-menu-item index="9-1">设备管理</el-menu-item>
          <el-menu-item index="9-2">报警规则</el-menu-item>
          <el-menu-item index="9-3">报警数据</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <el-button text @click="layoutStore.toggleCollapsed">☰</el-button>
        <div class="crumb">首页 / 看板</div>
        <div class="admin">管理员</div>
      </el-header>
      <el-main class="main" v-loading="loading">
        <template v-if="dashboard">
          <div class="top-grid">
            <el-card class="card">
              <template #header>
                <div class="card-header">数据概览 <span class="update">数据更新：{{ dashboard.updateTime }}</span></div>
              </template>
              <div class="ring-list">
                <div class="ring-item"><el-progress type="dashboard" :percentage="75"><template #default>{{ dashboard.overview.elderCount }}人</template></el-progress><p>老人数量</p></div>
                <div class="ring-item"><el-progress type="dashboard" color="#67c23a" :percentage="82"><template #default>{{ dashboard.overview.bedCount }}个</template></el-progress><p>床位数量</p></div>
                <div class="ring-item"><el-progress type="dashboard" color="#8a8df6" :percentage="72"><template #default>{{ dashboard.overview.serviceTotal }}</template></el-progress><p>服务总数量</p></div>
                <div class="ring-item"><el-progress type="dashboard" color="#e6a23c" :percentage="68"><template #default>{{ dashboard.overview.employeeCount }}</template></el-progress><p>员工数量</p></div>
                <div class="ring-item"><el-progress type="dashboard" color="#f56c6c" :percentage="88"><template #default>{{ dashboard.overview.incomeTotal }}</template></el-progress><p>收入金额</p></div>
              </div>
            </el-card>

            <el-card class="card profile-card">
              <template #header><div class="card-header">我的信息</div></template>
              <div class="profile">
                <el-avatar :size="64">管</el-avatar>
                <div>
                  <h4>{{ dashboard.profile.name }}</h4>
                  <p>{{ dashboard.profile.greeting }}</p>
                  <small>{{ dashboard.profile.title }}</small>
                </div>
              </div>
            </el-card>
          </div>

          <div class="mid-grid">
            <el-card class="card">
              <template #header><div class="card-header">数据统计</div></template>
              <div ref="trendChartRef" class="chart trend"></div>
            </el-card>
            <el-card class="card func-card">
              <template #header><div class="card-header">常用功能</div></template>
              <div class="func-grid">
                <div v-for="item in dashboard.quickActions" :key="item.key" class="func-item">
                  <span class="func-icon">□</span>
                  <span>{{ item.label }}</span>
                </div>
              </div>
            </el-card>
          </div>

          <div class="bottom-grid">
            <el-card class="card">
              <template #header><div class="card-header">老人等级分布</div></template>
              <div ref="pieChartRef" class="chart short"></div>
            </el-card>
            <el-card class="card">
              <template #header><div class="card-header">老人年龄分布</div></template>
              <div ref="ageChartRef" class="chart short"></div>
            </el-card>
            <el-card class="card">
              <template #header><div class="card-header">预约总览</div></template>
              <el-timeline>
                <el-timeline-item v-for="(item, idx) in dashboard.appointments" :key="idx" :timestamp="item.time">
                  <strong>{{ item.type }}</strong>：{{ item.elderName }}（{{ item.contact }}）
                </el-timeline-item>
              </el-timeline>
            </el-card>
          </div>
        </template>
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
:deep(.menu .el-menu-item),
:deep(.menu .el-sub-menu__title) { color: #303133; }
:deep(.menu .el-menu-item:hover),
:deep(.menu .el-sub-menu__title:hover) { background: #f5f7fa; color: #409eff; }
:deep(.menu .el-menu-item.is-active) { background: #ecf5ff; color: #409eff; font-weight: 600; }
.header { height: 56px; background: #fff; display: flex; align-items: center; border-bottom: 1px solid #ebeef5; }
.crumb { margin-left: 12px; color: #606266; flex: 1; }
.admin { color: #909399; font-size: 13px; }
.main { padding: 12px; }
.top-grid { display: grid; grid-template-columns: 3fr 1.1fr; gap: 12px; margin-bottom: 12px; }
.mid-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 12px; margin-bottom: 12px; }
.bottom-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 12px; }
.card { border-radius: 8px; }
.card-header { font-weight: 700; color: #303133; display: flex; justify-content: space-between; }
.update { font-weight: 400; color: #909399; font-size: 12px; }
.ring-list { display: grid; grid-template-columns: repeat(5, 1fr); gap: 8px; }
.ring-item { text-align: center; }
.ring-item p { margin-top: 4px; color: #606266; font-size: 12px; }
.profile { display: flex; gap: 12px; align-items: center; }
.profile h4 { margin: 0 0 6px; }
.profile p { margin: 0 0 4px; color: #606266; font-size: 13px; }
.profile small { color: #909399; }
.chart { width: 100%; }
.trend { height: 280px; }
.short { height: 240px; }
.func-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.func-item { border: 1px solid #ebeef5; border-radius: 8px; padding: 10px 6px; text-align: center; color: #409eff; display: flex; flex-direction: column; gap: 6px; align-items: center; }
.func-icon { display: inline-flex; align-items: center; justify-content: center; width: 24px; height: 24px; border: 1px solid #a0cfff; border-radius: 50%; }
@media (max-width: 1360px) {
  .top-grid, .mid-grid, .bottom-grid { grid-template-columns: 1fr; }
  .ring-list { grid-template-columns: repeat(2, 1fr); }
}
</style>
