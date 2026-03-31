<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { useLayoutStore } from '../stores/layout'
import { getTaskSchedule } from '../api/taskSchedule'
import type { TaskScheduleDetail } from '../types/taskSchedule'

const router = useRouter()
const route = useRoute()
const layoutStore = useLayoutStore()
const loading = ref(false)
const detail = ref<TaskScheduleDetail | null>(null)

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

async function loadDetail(): Promise<void> {
  loading.value = true
  try {
    const id = Number(route.params.id)
    detail.value = await getTaskSchedule(id)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载详情失败')
  } finally {
    loading.value = false
  }
}

function goBack(): void {
  void router.back()
}

function statusClass(status: number): string {
  if (status === 2) return 'status-done'
  if (status === 3) return 'status-cancel'
  return 'status-pending'
}

function formatExecuteRecord(text: string): string {
  const raw = (text || '').trim()
  if (!raw) return '-'
  const chars = Array.from(raw)
  const lines: string[] = []
  for (let i = 0; i < chars.length && lines.length < 4; i += 25) {
    lines.push(chars.slice(i, i + 25).join(''))
  }
  return lines.join('\n')
}

onMounted(() => {
  void loadDetail()
})
</script>

<template>
  <el-container class="page-root">
    <el-aside :width="layoutStore.collapsed ? '72px' : '220px'" class="sider">
      <div class="brand">
        <img src="https://dummyimage.com/36x36/8ec96b/ffffff&text=中" alt="logo" />
        <div v-if="!layoutStore.collapsed" class="brand-text">中州养老</div>
      </div>
      <el-menu :collapse="layoutStore.collapsed" default-active="6-5" class="menu" @select="onMenuSelect">
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
        <el-card class="block-card" v-if="detail">
          <template #header><div class="title">基本信息</div></template>
          <div class="base-wrap">
            <div class="base-grid">
              <div class="row"><span class="k">老人姓名</span><span class="v">{{ detail.elderName }}</span></div>
              <div class="row"><span class="k">护理等级</span><span class="v">{{ detail.careLevelName }}</span></div>
              <div class="row"><span class="k">性别</span><span class="v">{{ detail.elderGender }}</span></div>
              <div class="row"><span class="k">年龄</span><span class="v">{{ detail.elderAge }}岁</span></div>
              <div class="row"><span class="k">床位号</span><span class="v">{{ detail.bedNo }}</span></div>
              <div class="row"><span class="k">护理员姓名</span><span class="v">{{ detail.caregiverNames }}</span></div>
            </div>
            <el-image class="avatar" :src="detail.elderAvatarUrl" fit="cover" />
          </div>
        </el-card>

        <el-card class="block-card" v-if="detail">
          <template #header><div class="title">护理项目</div></template>
          <div class="item-grid">
            <div class="row"><span class="k">护理项目名称</span><span class="v">{{ detail.nursingItemName }}</span></div>
            <div class="row"><span class="k">关联单据</span><span class="v">{{ detail.orderNo }}</span></div>
            <div class="row"><span class="k">执行状态</span><span class="v" :class="statusClass(detail.status)">{{ detail.statusLabel }}</span></div>
            <div class="row"><span class="k">项目类别</span><span class="v">{{ detail.taskType }}</span></div>
            <div class="row"><span class="k">创建人</span><span class="v">{{ detail.createdBy }}</span></div>
            <div class="row"><span class="k">创建时间</span><span class="v">{{ detail.createdTime }}</span></div>
            <div class="row"><span class="k">期望服务时间</span><span class="v">{{ detail.expectedServiceTime }}</span></div>
            <div class="row row-remark"><span class="k">备注信息</span><span class="v clamp2">{{ detail.remark || '-' }}</span></div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="detail && detail.status === 2">
          <template #header><div class="title">执行记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">执行人</span><span class="v">{{ detail.executeBy || '-' }}</span></div>
            <div class="row"><span class="k">执行时间</span><span class="v">{{ detail.executeTime || '-' }}</span></div>
            <div class="row"><span class="k">执行图片</span><el-image class="record-image" :src="detail.executeImageUrl" fit="cover" /></div>
            <div class="row row-record"><span class="k">执行记录</span><span class="v record-text">{{ formatExecuteRecord(detail.executeRecord) }}</span></div>
          </div>
        </el-card>

        <el-card class="block-card" v-if="detail && detail.status === 3">
          <template #header><div class="title">取消记录</div></template>
          <div class="record-grid">
            <div class="row"><span class="k">取消人</span><span class="v">{{ detail.cancelBy || '-' }}</span></div>
            <div class="row"><span class="k">取消时间</span><span class="v">{{ detail.cancelTime || '-' }}</span></div>
            <div class="row row-record"><span class="k">取消原因</span><span class="v record-text cancel-text">{{ detail.cancelReason || '-' }}</span></div>
          </div>
        </el-card>

        <div class="footer-btn"><el-button type="primary" @click="goBack">返回</el-button></div>
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
:deep(.menu .el-menu-item), :deep(.menu .el-sub-menu__title) { color: #303133; }
:deep(.menu .el-menu-item:hover), :deep(.menu .el-sub-menu__title:hover) { background: #f5f7fa; color: #409eff; }
:deep(.menu .el-menu-item.is-active) { background: #ecf5ff; color: #409eff; font-weight: 600; }
.header { height: 56px; background: #fff; display: flex; align-items: center; border-bottom: 1px solid #ebeef5; }
.crumb { margin-left: 12px; color: #606266; flex: 1; }
.admin { color: #909399; font-size: 13px; }
.main { padding: 12px; }
.block-card { margin-bottom: 12px; }
.title { font-weight: 600; }
.base-wrap { display: flex; justify-content: space-between; gap: 16px; }
.base-grid, .item-grid { flex: 1; display: grid; grid-template-columns: repeat(2, minmax(220px, 1fr)); row-gap: 12px; column-gap: 24px; }
.record-grid { display: grid; grid-template-columns: repeat(2, minmax(220px, 1fr)); row-gap: 12px; column-gap: 24px; }
.row { display: flex; }
.k { width: 92px; color: #909399; }
.v { color: #303133; }
.row-remark { grid-column: 1 / span 2; }
.row-record { grid-column: 1 / span 2; }
.clamp2 { display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.avatar { width: 88px; height: 118px; border-radius: 2px; border: 1px solid #ebeef5; }
.record-image { width: 116px; height: 116px; border: 1px solid #ebeef5; border-radius: 2px; }
.record-text { white-space: pre-line; max-width: 560px; line-height: 22px; }
.status-pending { color: #e6a23c; }
.status-done { color: #67c23a; }
.status-cancel { color: #909399; }
.cancel-text { max-width: 560px; }
.footer-btn { display: flex; justify-content: center; margin-top: 28px; }
</style>
