<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useLayoutStore } from '../stores/layout'
import { getContractDetail } from '../api/contract'
import type { ContractTrackingDetail } from '../types/contract'

const router = useRouter()
const route = useRoute()
const layoutStore = useLayoutStore()
const loading = ref(false)
const detail = ref<ContractTrackingDetail | null>(null)

const showPayer = computed(() => {
  if (!detail.value) return false
  return !!detail.value.payerName || !!detail.value.payerContact
})

const showDissolveRecord = computed(() => {
  if (!detail.value) return false
  return !!detail.value.dissolveAgreementFileName || !!detail.value.dissolveDate || !!detail.value.dissolveSubmitter
})

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
  const id = Number(route.params.id)
  if (!id) return
  loading.value = true
  try {
    detail.value = await getContractDetail(id)
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '查询失败')
  } finally {
    loading.value = false
  }
}

function previewFile(fileName: string, fileUrl?: string): void {
  if (fileUrl) {
    window.open(fileUrl, '_blank')
    return
  }
  if (!fileName) {
    ElMessage.warning('暂无可预览文件')
    return
  }
  const html = `<html><head><meta charset="utf-8" /></head><body style="font-family: sans-serif; padding: 24px;">文件预览占位：${fileName}</body></html>`
  const dataUrl = 'data:text/html;charset=utf-8,' + encodeURIComponent(html)
  window.open(dataUrl, '_blank')
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
      <el-menu :collapse="layoutStore.collapsed" default-active="4-4" class="menu" @select="onMenuSelect">
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
      <el-main class="main" v-loading="loading">
        <el-card v-if="detail" class="page-card" shadow="never">
          <section class="section-block">
            <h3 class="section-title">合同详情</h3>
            <div class="detail-grid">
              <div class="field-label">合同编号</div><div>{{ detail.contractNo }}</div>
              <div class="field-label">合同名称</div><div>{{ detail.contractName }}</div>
              <div class="field-label">老人姓名</div><div>{{ detail.elderName }}</div>
              <div class="field-label">老人身份证号</div><div>{{ detail.idCard }}</div>
              <div class="field-label">合同期限</div><div>{{ detail.contractPeriod }}</div>
              <div class="field-label">合同状态</div>
              <div>
                <el-tag :type="detail.contractStatus === '生效中' ? 'success' : detail.contractStatus === '未生效' ? 'info' : detail.contractStatus === '已过期' ? 'warning' : 'danger'" effect="plain">
                  {{ detail.contractStatus }}
                </el-tag>
              </div>
              <div class="field-label">签约日期</div><div>{{ detail.signDate || '-' }}</div>
              <template v-if="showPayer">
                <div class="field-label">丙方姓名</div><div>{{ detail.payerName || '-' }}</div>
                <div class="field-label">丙方联系方式</div><div>{{ detail.payerContact || '-' }}</div>
              </template>
              <div class="field-label">合同文件</div>
              <div>
                <span>{{ detail.contractFileName || '-' }}</span>
                <el-button v-if="detail.contractFileName" link type="primary" @click="previewFile(detail.contractFileName, detail.contractFileUrl)">查看</el-button>
              </div>
            </div>
          </section>

          <section v-if="showDissolveRecord" class="section-block split-top">
            <h3 class="section-title">解除记录</h3>
            <div class="detail-grid">
              <div class="field-label">提交人</div><div>{{ detail.dissolveSubmitter || '-' }}</div>
              <div class="field-label">解约日期</div><div>{{ detail.dissolveDate || '-' }}</div>
              <div class="field-label">解约协议</div>
              <div>
                <span>{{ detail.dissolveAgreementFileName || '-' }}</span>
                <el-button v-if="detail.dissolveAgreementFileName" link type="primary" @click="previewFile(detail.dissolveAgreementFileName, detail.dissolveAgreementFileUrl)">查看</el-button>
              </div>
            </div>
          </section>

          <div class="footer-actions">
            <el-button type="primary" @click="router.push('/contracts')">返回</el-button>
          </div>
        </el-card>
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
.page-card { border-radius: 8px; min-height: 640px; }
.section-block { padding: 8px 4px; }
.section-title { margin: 0 0 16px; font-size: 18px; font-weight: 600; color: #303133; }
.split-top { border-top: 1px solid #ebeef5; margin-top: 16px; padding-top: 20px; }
.detail-grid {
  display: grid;
  grid-template-columns: 120px 1fr;
  row-gap: 14px;
  column-gap: 14px;
  font-size: 14px;
  color: #303133;
}
.field-label { color: #909399; }
.footer-actions {
  margin-top: 28px;
  display: flex;
  justify-content: center;
}
</style>
